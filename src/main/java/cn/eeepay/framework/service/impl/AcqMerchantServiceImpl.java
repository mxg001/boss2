package cn.eeepay.framework.service.impl;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.exchangeActivate.ExcActRoute;
import cn.eeepay.framework.model.exchangeActivate.ExcActRouteGood;
import cn.eeepay.framework.model.risk.TradeRestrict;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.AcqMerchantDao;
import cn.eeepay.framework.db.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

@Service("acqMerchantService")
@Transactional
public class AcqMerchantServiceImpl implements AcqMerchantService {
	private static final Logger log = LoggerFactory.getLogger(AcqMerchantServiceImpl.class);
	@Resource
	private AcqMerchantDao acqMerchantDao;
	
	//收单机构终端
	@Resource
	private AcqTerminalService acqTerminalService;
	@Resource
	private SysDictService sysDictService;
	@Resource
	private AcqInMerchantService acqInMerchantService;
	@Resource
	private MerchantInfoService merchantInfoService;
	@Resource
	private MerchantBusinessProductService merchantBusinessProductService;


	
	/**
	 * 收单机构商户新增和终端新增
	 * acqMerchant:失效的记录
	 */
	@Override
	public int insert(final AcqMerchant record, List<String> list, AcqMerchant acqMerchant) {
		//如果存在已经失效的记录，将其merchantNo清空
		if (acqMerchant != null && acqMerchant.getAcqStatus() == 0){ // 已失效的记录
			acqMerchantDao.clearMerchantNo(acqMerchant.getId());
		}
		int i=0;
	    i = acqMerchantDao.insert(record);
	    if(i!=1){
			throw new RuntimeException("收单机构商户新增失败");
		}
	    AcqTerminal act=new AcqTerminal();
		act.setAcqMerchantNo(record.getAcqMerchantNo());
		act.setAcqOrgId(record.getAcqOrgId().toString());
		act.setCreatePerson(record.getCreatePerson());
		for (int j = 0; j < list.size(); j++) {
			if(list.get(j)==null||"".equals(list.get(j))){
				return 0;
			}
			act.setAcqTerminalNo(list.get(j));
			if(acqTerminalService.selectTerminalByTerNo(act)!=null){
				throw new RuntimeException("收单机构终端已存在，请注意检查");
			}
			i=acqTerminalService.insert(act);
			if(i!=1){
				throw new RuntimeException("收单机构商户新增失败");
			}
		}

		changeMerchantBusiness(record);

		// 同步ES接口
		new Thread(new Runnable() {
			@Override
			public void run() {
				synchronizeAcqMerchant(record.getAcqMerchantNo(), record.getMerchantNo());
			}
		}).start();

		return i;
	}

	/**
	 * 更换普通商户业务产品
	 * @param record
	 */
	public void changeMerchantBusiness(AcqMerchant record){
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		final String merchantNo = record.getMerchantNo();
		if(StringUtils.isNotBlank(merchantNo)){
			AcqInMerchant acqInMerchant = acqInMerchantService.selectByMerchantNo(merchantNo);
			if(null == acqInMerchant){
				return;
			}
			String changeMerBusinessInfo = acqInMerchant.getChangeMerBusinessInfo();
			if(StringUtils.isNotBlank(changeMerBusinessInfo)){
				JSONObject jsonObject = JSONObject.parseObject(changeMerBusinessInfo);
				String info = jsonObject.get("changeBusinessInfo").toString();
				List<Business> businesses = JSONArray.parseArray(info, Business.class);

				MerchantInfo merchantInfo = merchantInfoService.selectByMerNo(merchantNo);
				for (Business business : businesses) {
					String oldBpId = business.getOldBpId();
					final String newBpId = business.getNewBpId();
					if(oldBpId.equals(newBpId)){
						continue;
					}
					log.info("更换业务产品： oldBpId=" + oldBpId + " newBpId=" + newBpId);
					if(acqMerchantDao.updateMerBusTer(newBpId, merchantNo, oldBpId) < 1){
						throw new RuntimeException("更新机具信息失败");
					}
					if ( merchantBusinessProductService.updateMerchantBusinessProduct(merchantNo, oldBpId, newBpId) != 1) {
						throw new RuntimeException("更新业务产品失败");
					}

					MerchantBusinessProductHistory hisMbp = new MerchantBusinessProductHistory();
					hisMbp.setSourceBpId(oldBpId);
					hisMbp.setNewBpId(newBpId);
					hisMbp.setOperationType("2");// 2更换
					hisMbp.setOperationPersonType("1");// 1运营
					hisMbp.setCreateTime(new Date());
					hisMbp.setOperationPersonNo(principal.getUsername());// 操作人
					hisMbp.setMerchantNo(merchantNo);
					if (merchantBusinessProductService.insertMerBusProHis(hisMbp) != 1) {
						throw new RuntimeException("写入商户业务产品历史表失败");
					}

					// 删除审核失败进件项
					merchantBusinessProductService.deleteMerBusItem(merchantNo);

					updateMerchantService(merchantInfo, oldBpId, newBpId);
					// 如果是在中付同步过,且同步成功的话,这需要条用中付的修改接口(修改费率)
					if (merchantBusinessProductService.countZF_ZQAndSyncSuccess(merchantNo, newBpId) >= 1) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									Thread.sleep(500);
									SysDict sysDict = sysDictService.getByKey("ZFZQ_ACCESS_URL");
									String accessUrl = sysDict.getSysValue() + "zfMerchant/zfMerUpdate";
									Map<String, Object> marMap = new HashMap<String, Object>();

									marMap.put("merchantNo", merchantNo);
									marMap.put("bpId", newBpId);
									marMap.put("operator", principal.getUsername());
									marMap.put("changeSettleCard", "0");
									List<String> channelList = new ArrayList<>();
									channelList.add("ZF_ZQ");
									marMap.put("channelCode", channelList);
									String paramStr = JSON.toJSONString(marMap);
									log.info("ZFZQ_ACCESS_URL:" + accessUrl + "\n paramStr:" + paramStr);
									String result = new ClientInterface(accessUrl, null).postRequestBody(paramStr);
									log.error("调用上游同步返回数据:" + result);
								} catch (Exception e) {
									log.error("调用上游同步费率接口失败", e);
								}
							}
						}).start();
					}

				}
			}
		}

	}

	public void synchronizeAcqMerchant(String acqMerchantNo, String merchantNo){
		log.info("调用agentApi同步ES接口，acqMerchantNo=" + acqMerchantNo + ";merchantNo=" + merchantNo);
		String url = sysDictService.getValueByKey("agentApi2_url") + "/esDataMigrate/changeAcqMerchantNoToEs";
		Map<String, String> params = new HashMap<>();
		params.put("acqMerchantNo", acqMerchantNo);
		params.put("merchantNo", merchantNo);
		String key="46940880d9f79f27bb7f85ca67102bfdylkj@@agentapi2#$$^&pretty";
		Long timestamp= DateUtil.dateToUnixTimestamp();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("timestamp", timestamp + "");
		String signStr = "";
		if(StringUtils.isNotBlank(acqMerchantNo)){
			signStr = "acqMerchantNo="+acqMerchantNo+"&merchantNo="+merchantNo+"&timestamp="+timestamp+"&key="+key;
		}else {
			signStr = "merchantNo="+merchantNo+"&timestamp="+timestamp+"&key="+key;
		}
		headers.put("sign", Md5.md5Str(signStr));
		headers.put("app-info", JSONObject.toJSONString(headers));
		String result = new ClientInterface(url, headers, params).postRequestBodyWithJson(JSONObject.toJSONString(params));
		log.info("同步ES接口结果：" + result);
	}


	private void updateMerchantService(MerchantInfo merchantInfo, String oldBpId, String newBpId) {
		log.info("更改商户服务");
		List<ServiceInfoBean> oldServiceInfoList = merchantBusinessProductService.listServiceInfoByBpId(oldBpId);
		log.info("oldServiceInfoList -> " + oldServiceInfoList);
		List<ServiceInfoBean> newServiceInfoList = merchantBusinessProductService.listServiceInfoByBpId(newBpId);
		log.info("newServiceInfoList -> " + newServiceInfoList);
		if (oldServiceInfoList == null || newServiceInfoList == null) {
			throw new RuntimeException("没有找到相应的服务信息");
		}
		if (oldServiceInfoList.size() != newServiceInfoList.size()) {
			throw new RuntimeException("新旧业务产品的服务信息不一一对应");
		}
		Map<String, ServiceInfoBean> oldServiceInfoMap = translateServiceId2MapKey(oldServiceInfoList);
		Map<String, ServiceInfoBean> newServiceInfoMap = translateServiceType2MapKey(newServiceInfoList);
		log.info("oldServiceInfoMap -> " + oldServiceInfoMap);
		log.info("newServiceInfoMap -> " + newServiceInfoMap);

		String oneAgentNo = merchantInfo.getOneAgentNo();
		String merchantNo = merchantInfo.getMerchantNo();

		// 删除商户服务费率
		merchantBusinessProductService.deleteMerRate(oldBpId, merchantNo);
		// 删除商户服务限额
		merchantBusinessProductService.deleteMerQuota(oldBpId, merchantNo);

		for (Map.Entry<String, ServiceInfoBean> oldServiceEntry : oldServiceInfoMap.entrySet()) {
			String oldServiceId = oldServiceEntry.getKey();
			String oldServiceType = oldServiceEntry.getValue().getServiceType();
			ServiceInfoBean newServiceInfoBean = newServiceInfoMap.get(oldServiceType);
			if (newServiceInfoBean == null || StringUtils.isBlank(newServiceInfoBean.getServiceId())) {
				throw new RuntimeException("旧业务产品的的服务(" + oldServiceId + ")没找到对应的新服务");
			}
			String newServiceId = newServiceInfoBean.getServiceId();
			if (merchantBusinessProductService.updateMerchantService(merchantNo, oldBpId, newBpId, oldServiceId, newServiceId) != 1) {
				throw new RuntimeException("更新商户服务失败");
			}

			if (StringUtils.equals("0", newServiceInfoBean.getFixedRate())) {
				throw new RuntimeException("新业务产品费率不固定");
				/*try {
					List<ServiceRate> newServiceRateList = merchantInfoService.getServiceRateByServiceId(oneAgentNo, newServiceId);
					if (newServiceRateList != null && !newServiceRateList.isEmpty()) {
						merchantBusinessProductService.bacthInsertServiceRate(newServiceRateList, merchantNo);
					}
				} catch (Exception e) {
					log.error("更新商户服务签约费率失败：" + e);
					throw  new RuntimeException("更新商户服务签约费率失败");
				}*/
			}

			if (StringUtils.equals("0", newServiceInfoBean.getFixedQuota())) {
				throw new RuntimeException("新业务产品额度不固定");
				/*try {
					List<ServiceQuota> newServiceQuotaList = merchantInfoService.getServiceQuotaByServiceId(oneAgentNo, newServiceId);
					if (newServiceQuotaList != null && !newServiceQuotaList.isEmpty()) {
						merchantBusinessProductService.bacthInsertServiceQuota(newServiceQuotaList, merchantNo);
					}
				} catch (Exception e) {
					log.error("更新服务基本信息表失败", e);
					throw new RuntimeException("更新服务基本信息表失败");
				}*/
			}


		}
	}

	private Map<String, ServiceInfoBean> translateServiceId2MapKey(List<ServiceInfoBean> serviceInfoList) {
		Map<String, ServiceInfoBean> resultMap = new HashMap<>();
		for (ServiceInfoBean bean : serviceInfoList) {
			resultMap.put(bean.getServiceId(), bean);
		}
		return resultMap;
	}

	private Map<String, ServiceInfoBean> translateServiceType2MapKey(List<ServiceInfoBean> serviceInfoList) {
		Map<String, ServiceInfoBean> resultMap = new HashMap<>();
		for (ServiceInfoBean bean : serviceInfoList) {
			resultMap.put(bean.getServiceType(), bean);
		}
		return resultMap;
	}

	@Override
	public int updateByPrimaryKey(final AcqMerchant record) {
		if(StringUtils.isBlank(record.getMerchantNo())){
			record.setMerchantNo(null);
		}
		int i = acqMerchantDao.updateByPrimaryKey(record);
		//标记acq_merchant表的merchant_no
		if(i==1 && StringUtils.isBlank(record.getMerchantNo()) && StringUtils.isNotBlank(record.getOldMerchantNo()) ){
			acqInMerchantService.updateMerchantNo(record.getOldMerchantNo());
		}

		// 同步ES接口
		// 如果新的merchantNo为空则传旧的，如果旧的也为空，则不同步
		boolean flag = true;
		if(StringUtils.isBlank(record.getMerchantNo())){ // 新普通商户号为空
			if(StringUtils.isBlank(record.getOldMerchantNo())){ // 新、旧 普通商户号为空，不调
				flag = false;
			}else {
				record.setMerchantNo(record.getOldMerchantNo()); // 新的为空，旧的不为空 则调用时传旧的商户号
				record.setAcqMerchantNo("");
			}
		}else if(StringUtils.isNotBlank(record.getOldMerchantNo())
				&& StringUtils.equals(record.getMerchantNo(), record.getOldMerchantNo())){ //新、旧 相等 不调
			flag = false;
		}

		// 再判断特约商户号是否改变
		/*if(!flag){
			if(!StringUtils.equals(record.getOldAcqMerchantNo(), record.getAcqMerchantNo())){
				if(StringUtils.isBlank(record.getOldMerchantNo()) && StringUtils.isBlank(record.getMerchantNo())){
					flag = false;
				}else {
					flag = true;
				}
			}
		}*/

		if(flag){
			new Thread(new Runnable() {
				@Override
				public void run() {
					String merchantNo = record.getMerchantNo();
					String acqMerchantNo = record.getAcqMerchantNo();
					synchronizeAcqMerchant(acqMerchantNo, merchantNo);
				}
			}).start();
		}

		return i;
	}

	@Override
	public List<AcqMerchant> selectAllInfo(Page<AcqMerchant> page, AcqMerchant amc) {
		return acqMerchantDao.selectAllInfo(page, amc);
	}

	@Override
	public int updateStatusByid(AcqMerchant record) {
		return acqMerchantDao.updateStatusByid(record);
	}
	@Override
	public AcqMerchant selectInfoByAcqmerNo(AcqMerchant record) {
		return acqMerchantDao.selectInfoByAcqmerNo(record);
	}

	@Override
	public AcqMerchant selectByPrimaryKey(Integer id) {
		return acqMerchantDao.selectByPrimaryKey(id);
	}

	@Override
	public AcqMerchant selectInfoByMerNo(String merchantNo) {
		return acqMerchantDao.selectInfoByMerNo(merchantNo);
	}

	@Override
	public int selectOrgMerExistByMerNo(String acqMerchantNo) {
		return acqMerchantDao.selectOrgMerExistByMerNo(acqMerchantNo);
	}

	@Override
	public int insertInfo(AcqMerchant record,AcqTerminal aa) {
		int num=0;
		AcqTerminal aa1 = acqTerminalService.selectTerminalByTerNo(aa);
		if(aa1!=null){//判断是否存在这个收单终端 存在就修改，不存在就新增
			if(aa1.getAcqMerchantNo()==null||aa1.getAcqMerchantNo().equals("")){
				num=acqTerminalService.updateInfo(aa);
				if(num!=1){
					throw new RuntimeException("收单商户为："+record.getAcqMerchantNo()+"新增失败，请注意检查");
				}
			}
		}else{
			num=acqMerchantDao.insert(record);
			if(num!=1){
				throw new RuntimeException("收单商户为："+record.getAcqMerchantNo()+"新增失败，请注意检查");
			}
			num=acqTerminalService.insert(aa);
			if(num!=1){
				throw new RuntimeException("收单商户为："+record.getAcqMerchantNo()+"新增失败，请注意检查");
			}
		}
		return num;
	}

	@Override
	public List<AcqMerchant> importDetailSelect(AcqMerchant amc) {
		return acqMerchantDao.importDetailSelect(amc);
	}

	@Override
	public void importDetail(List<AcqMerchant> list, HttpServletResponse response) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		String fileName = "收单机构商户列表"+sdf.format(new Date())+".xlsx" ;
		String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		if(list.size()<1){
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id",null);
			maps.put("agentName",null);
			maps.put("acqMerchantNo",null);
			maps.put("acqMerchantName",null);
			maps.put("source",null);
			maps.put("acqName",null);
			maps.put("serviceName",null);
			maps.put("dayAmount",null);
			maps.put("merchantNo",null);
			maps.put("merchantName",null);
			maps.put("acqMerchantCode",null);
			maps.put("repPay",null);
			maps.put("acqMerchantType",null);
			maps.put("acqStatus",null);
			maps.put("createTime", null);
			data.add(maps);
		}else{
			//代付
			Map<String, String> repPayMap=new HashMap<String, String>();
			repPayMap.put("1","否");
			repPayMap.put("2","是");

			Map<String, String> sourceMap=sysDictService.selectMapByKey("acqMerSource");//收单商户来源途径
			Map<String, String> acqMerchantTypeMap=sysDictService.selectMapByKey("ACQ_MERCHANT_TYPE");//收单商户类别

			Map<String, String> acqStatusMap=new HashMap<String, String>();
			acqStatusMap.put("0","关闭");
			acqStatusMap.put("1","开启");

			for (AcqMerchant or : list) {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id",or.getId()==null?"":or.getId().toString());
				maps.put("agentName",or.getAgentName());
				maps.put("acqMerchantNo",or.getAcqMerchantNo());
				maps.put("acqMerchantName",or.getAcqMerchantName());
				maps.put("source",sourceMap.get(or.getSource()));
				maps.put("acqName",or.getAcqName());
				maps.put("serviceName",or.getServiceName());
				maps.put("dayAmount",or.getDayAmount()==null?"":or.getDayAmount().toString());
				maps.put("merchantNo",or.getMerchantNo());
				maps.put("merchantName",or.getMerchantName());
				maps.put("acqMerchantCode",or.getAcqMerchantCode());
				maps.put("repPay",repPayMap.get(or.getRepPay()==null?"":or.getRepPay().toString()));
				maps.put("acqMerchantType",acqMerchantTypeMap.get(or.getAcqMerchantType()==null?"":or.getAcqMerchantType().toString()));
				maps.put("acqStatus",acqStatusMap.get(or.getAcqStatus()==null?"":or.getAcqStatus().toString()));
				maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"id","agentName","acqMerchantNo","acqMerchantName","source","acqName",
				"serviceName","dayAmount","merchantNo","merchantName","acqMerchantCode","repPay","acqMerchantType",
				"acqStatus","createTime"
		};
		String[] colsName = new String[]{"序号","代理商名称","收单机构商户编号","收单机构商户名称","来源途径","收单机构",
				"收单服务","今日收单金额","商户编号","商户名称","收单机构对应收单商户进件编号","代付","类别",
				"状态","创建时间"
		};
		OutputStream ouputStream =null;
		try {
			ouputStream=response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		}catch (Exception e){
			log.error("导出收单机构商户列表失败!",e);
		}finally {
			if(ouputStream!=null){
				ouputStream.close();
			}
		}


	}

	@Override
	public Map<String, Object> acqMerBatchColseimport(MultipartFile file) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		Workbook wb = WorkbookFactory.create(file.getInputStream());
		//读取第一个sheet
		Sheet sheet = wb.getSheetAt(0);
		// 遍历所有单元格，读取单元格
		int row_num = sheet.getLastRowNum();

		List<Map<String,Object>> errorList=new ArrayList<Map<String,Object>>();
		List<String> acqMerNoList=new ArrayList<>();//校验
		List<String> updateList=new ArrayList<String>();//需要更新的

		for (int i = 1; i <= row_num; i++) {
			Row row = sheet.getRow(i);
			String acqMerchantNo1 = CellUtil.getCellValue(row.getCell(0));//收单机构商户编号

			Map<String,Object> errorMap=new HashMap<String,Object>();
			int rowNum=i+1;
			if(acqMerchantNo1==null||"".equals(acqMerchantNo1)){
				errorMap.put("msg","第"+rowNum+"行,收单机构商户编号为空!");
				errorList.add(errorMap);
				continue;
			}
			String acqMerchantNo=acqMerchantNo1.split("\\.")[0];

			//验证重复
			if(acqMerNoList.contains(acqMerchantNo)){
				errorMap.put("msg","第"+rowNum+"行,收单机构商户编号("+acqMerchantNo+")重复!");
				errorList.add(errorMap);
				continue;
			}else{
				acqMerNoList.add(acqMerchantNo);
			}

			AcqMerchant old = acqMerchantDao.getAcqMerchantInfo(acqMerchantNo);
			if(old==null){
				errorMap.put("msg", "第"+rowNum+"行,收单机构商户编号("+acqMerchantNo+")不存在!");
				errorList.add(errorMap);
				continue;
			}else{
				if("0".equals(old.getAcqStatus()==null?"0":old.getAcqStatus().toString())){
					errorMap.put("msg", "第"+rowNum+"行,收单机构商户编号("+acqMerchantNo+")已经是关闭状态,不需要处理!");
					errorList.add(errorMap);
					continue;
				}else{
					updateList.add(acqMerchantNo);
				}
			}
		}
		int num=0;
		if(updateList!=null&&updateList.size()>0){
			for(String str:updateList){
				num=num+acqMerchantDao.updateInfoStatus(0,str);
			}
		}
		msg.put("errorList", errorList);
		msg.put("status", true);
		msg.put("msg", "导入成功,总共"+row_num+"条数据,成功关闭"+num+"个收单机构商户,失败"+errorList.size()+"条数据!");
		return msg;
	}

	@Override
	public AcqMerchant selectByMerchantNo(String merchantNo) {
		return acqMerchantDao.selectByMerchantNo(merchantNo);
	}
}
