package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.*;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.BusinessProductDefineService;
import cn.eeepay.framework.service.ServiceProService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.util.CellRangeAddress;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("businessProductDefineService")
@Transactional
public class BusinessProductDefineServiceImpl implements BusinessProductDefineService {

	private static final Logger log = LoggerFactory.getLogger(BusinessProductDefineServiceImpl.class);

	@Resource
	private BusinessProductDefineDao businessProductDefineDao;
	
	@Resource
	private BusinessProductHardwareDao businessProductHardwareDao;
	
	@Resource
	private BusinessProductInfoDao businessProductInfoDao;
	
	@Resource
	private BusinessRequireItemDao businessRequireItemDao;
	
	@Resource
	private SeqService seqService;
	
	@Resource
	private TeamInfoDao teamInfoDao;
	
	@Resource
	private ServiceDao serviceDao;
	
	@Resource
	private AddRequireItemDao addRequireItemDao;
	
	@Resource
	private HardwareProductDao hardwareProductDao;
	
	@Resource
	private AgentBusinessProductDao agentBusinessProductDao;
	
	@Resource
	private MerchantBusinessProductDao merchantBusinessProductDao;

	@Resource
	private ServiceProService serviceProService;
	
	
	@Override
	public List<BusinessProductDefine> selectAllInfo() {
		return businessProductDefineDao.selectAllInfo();
	}

	@Override
	public String selectBpNameByBpId(String bpId) {
		return businessProductDefineDao.selectBpNameByBpId(bpId);
	}

	@Override
	public List<BusinessProductDefine> selectProdcuteByTeamIdAndAgentNo(String agentNo, String teamId) {
		return businessProductDefineDao.selectProdcuteByTeamIdAndAgentNo(agentNo,teamId);
	}

	@Override
	public List<BusinessProductDefine> selectAllInfoByBpId(Long bpId) {
		return businessProductDefineDao.selectAllInfoByBpId(bpId);
	}

	@Override
	public List<BusinessProductDefine> selectAllInfoByName(String bpId) {
		return businessProductDefineDao.selectAllInfoByName(bpId);
	}

	@Override
	public Map<Long, String> selectBpNameMap() {
		List<BusinessProductDefine> list = selectAllInfo();
		if(list == null || list.isEmpty()){
			return null;
		}
		Map<Long, String> map = new HashMap<>();
		for(BusinessProductDefine item: list){
			map.put(item.getBpId(), item.getBpName());
		}
		return map;
	}

	@Override
	/**
	 * 业务产品查询 条件查询 by tans
	 */
	public List<BusinessProductDefine> selectByCondition(Page<BusinessProductDefine> page, BusinessProductDefine bpd) {
		return businessProductDefineDao.selectByParam(page, bpd);
	}

	@Override
	public void exportQueryProduct(BusinessProductDefine bpd, HttpServletResponse response,Map<String, Object> msgMap) {

		List<BusinessProductDefine> infos = null;
		try {
			infos = businessProductDefineDao.getExportQueryProduct(bpd);
			if(infos==null || infos.size()<=0){
				return;
			}
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String fileName = "业务产品查询"+sdf.format(new Date())+".xlsx";
			String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
			long begin  = System.currentTimeMillis();
			for (BusinessProductDefine info : infos) {
				Map<String, Object> map = new HashMap<>();
				map.put("bpName",info.getBpName());
				map.put("agentName",info.getAgentShowName());
				map.put("bpType",info.getBpType()==null?"":"1".equals(info.getBpType())?"个人":"2".equals(info.getBpType())?"个体商户":"3".equals(info.getBpType())?"企业商户":"");
				map.put("saleTime",(info.getSaleStarttime()==null?"":sdf.format(info.getSaleStarttime()))+"--"+(info.getSaleEndtime()==null?"":sdf.format(info.getSaleEndtime())));
				map.put("proxy",switchYN(info.getProxy()));
				map.put("isOem",switchYN(info.getIsOem()));
				map.put("teamName",info.getTeamName());
				map.put("relyHardware",switchYN(info.getRelyHardware()+""));
				map.put("notCheck",switchYN(info.getNotCheck()));
				map.put("allowWebItem",switchYN(info.getAllowWebItem()+""));
				map.put("allowIndividualApply",switchYN(info.getAllowIndividualApply()+""));
				map.put("linkProduct",info.getLinkProductName());
				map.put("ownBpName",info.getOwnBpName());
				//获取服务
				List<ServiceInfo> services = serviceDao.getBusinessServiceInfoToExport(info.getBpId());
				//获取硬件
				List<HardwareProduct> hards = businessProductHardwareDao.findByProduct(info.getBpId().toString());
				//获取进件项
				List<AddRequireItem> items = businessRequireItemDao.findBusinessRequireItem(info.getBpId());


				int servicesSize = services!=null?services.size():0;
				int hardsSize = hards!=null?hards.size():0;
				int itemsSize = items!=null?items.size():0;

				int maxRow = (servicesSize > hardsSize ? servicesSize : hardsSize) > itemsSize ?(servicesSize > hardsSize ? servicesSize : hardsSize) : itemsSize;
				map.put("maxRow",maxRow+"");
				map.put("serviceInfos",services);
				map.put("hardInfos",hards);
				map.put("itemInfos",items);
				data.add(map);
			}
			long end = System.currentTimeMillis();
			log.info("封装数据消耗时间："+(end - begin));


			BpdExport export = new BpdExport();
			String[] cols = new String[] { "bpName", "agentName", "bpType", "saleTime",
					"proxy","isOem","teamName","relyHardware","notCheck","allowWebItem","allowIndividualApply","linkProduct","ownBpName"
					,"hardName","serviceName","serviceType","tFlag","itemName"
			};
			String[] colsName = new String[] { "业务产品名称", "代理商展示名称", "类型", "可销售日期",
					"可否代理","是否OEM","所属组织","依赖硬件", "证件资料完整时无需人工审核","允许在web端进件","允许单独申请","自动开通关联业务产品","关联自营业务产品","关联硬件产品",
					"服务名称","服务类型","T1T0标志","进件要求项名称"
			};
			OutputStream outputStream = response.getOutputStream();


			long merBegin = System.currentTimeMillis();
			export.customExport(cols, colsName, data, outputStream);
			long merEnd = System.currentTimeMillis();
			log.info("总 合并单元格时间 ： "+(merEnd-merBegin));
			outputStream.close();
			msgMap.put("status",true);
		} catch (Exception e) {
			msgMap.put("status",false);
			msgMap.put("msg","业务产品导出失败");
			log.error("业务产品导出失败");
			e.printStackTrace();
		}
	}



	private List<ServiceInfo> getServiceInfosByBpId(String bpId){
		List<String> serviceIds = businessProductInfoDao.findByProduct(bpId);

		List<ServiceInfo> services = new ArrayList<>();
		if (serviceIds != null) {
			for (String serviceId : serviceIds) {
				ServiceInfo service = serviceDao.findServiceName(serviceId);
				if(service==null){
					continue;
				}
				//费率是否区分银行卡种类:1-是，0-否  节假日标志:1-只工作日，2-只节假日，0-不限
				boolean rateCardFlag = false;//费率是否区分银行卡种类
				if (service.getRateCard() != null) {
					rateCardFlag = 1 == service.getRateCard();
				}
				boolean rateHolidaysFlag = false;//费率区分节假日
				if (service.getRateHolidays() != null) {
					rateHolidaysFlag = 1 == service.getRateHolidays();
				}
				ServiceRate dJWrate = null;//贷记卡工作日
				ServiceRate jJWrate = null;//借记卡工作日

				if (rateCardFlag) {
					if (rateHolidaysFlag) {
						dJWrate = serviceProService.getRate("1", service.getServiceId(), "1");
						jJWrate = serviceProService.getRate("2", service.getServiceId(), "1");
					} else {
						dJWrate = serviceProService.getRate("1", service.getServiceId(), "0");
						jJWrate = serviceProService.getRate("2", service.getServiceId(), "0");
					}
				} else {
					if (rateHolidaysFlag) {
						ServiceRate wRate = serviceProService.getRate("0", service.getServiceId(), "1");
						dJWrate = wRate;
						jJWrate = wRate;
					} else {
						ServiceRate rate = serviceProService.getRate("0", service.getServiceId(), "0");
						dJWrate = rate;
						jJWrate = rate;
					}
				}
				if (jJWrate != null) {
					service.setjRate(jJWrate.getMerRate());
				}
				if (dJWrate != null) {
					service.setdRate(dJWrate.getMerRate());
				}

				services.add(service);
			}
		}
		return services;
	}

	private String switchYN(String val){
		if(val==null){
			return null;
		}
		String res = null;
		switch (val){
			case "1":
				res = "是";
				break;
			case "0":
				res = "否";
				break;
			default:res = null;
				break;
		}
		return res;
	}


	/**
	 * 根据业务产品ID，查询业务产品的详情 包含关联的自营业务产品名称
	 */
	@Override
	public BusinessProductDefine selectById(String id) {
		return businessProductDefineDao.selectDetailById(id);
	}

	/**
	 * 根据业务产品ID，查询业务产品的详情
	 */
	@Override
	public BusinessProductDefine selectBybpId(String bpId) {
		return businessProductDefineDao.selectBybpId(bpId);
	}

	@Override
	public List<BusinessProductDefine> selectBpTeam() {
		return businessProductDefineDao.selectBpTeam();
	}

	@Override
	public List<BusinessProductDefine> selectOtherProduct(String i) {
		return businessProductDefineDao.queryOtherProduct("");
	}

	@SuppressWarnings("unchecked")
	@Override
	public int insertOrUpdate(Map<String, Object> info) {
		int num = 0;
		BusinessProductDefine product = (BusinessProductDefine) info.get("product");
		List<ServiceInfo> services = (List<ServiceInfo>) info.get("services");
		List<AddRequireItem> items = (List<AddRequireItem>) info.get("items");
		List<HardwareProduct> hards = (List<HardwareProduct>) info.get("hards");
		String userName = info.get("createPerson")==null?"":info.get("createPerson").toString();
		if (product != null) {
			if( "100010".equals(product.getTeamId())){
				product.setOwnBpId(null);
				product.setIsOem("0");
			} else {
				product.setIsOem("1");
			}
			if (product.getBpId() != null) { 
				// 保存修改后的业务产品
				num = businessProductDefineDao.update(product);
				businessProductInfoDao.deleteProductByPid(product.getBpId().toString());
				businessRequireItemDao.deleteProductByPid(product.getBpId().toString());
				businessProductHardwareDao.deleteProductByPid(product.getBpId().toString());
				if (services != null) {
					Set<Long> serviceIdSet = new HashSet<>();
					for (ServiceInfo service : services) {
						if (service != null) {
							serviceIdSet.add(service.getServiceId());
							if(service.getLinkService()!=null){
								serviceIdSet.add(service.getLinkService());
							}
						}
					}
					Object[]serviceIds = serviceIdSet.toArray();
					businessProductInfoDao.insertBatchService(product.getBpId().toString(),serviceIds);
				}
				if (items != null) {
					for (AddRequireItem item : items) {
						if (item != null) {
							Map<String, String> map = new HashMap<>();
							map.put("bpId", product.getBpId().toString());
							map.put("itemId", item.getItemId().toString());
							businessRequireItemDao.insert(map);
						}
					}
				}
				if (product.getLimitHard()!=null && "0".equals(product.getLimitHard())){	//硬件产品不限，业务产品和硬件的关联表将硬件的ID置为“0”表示不限
					businessProductHardwareDao.insert("0",product.getBpId().toString());
				} else {										
					if (hards != null) {
						businessProductHardwareDao.insertBatch(hards, product.getBpId().toString());
					}
				}
			} else {
				// 保存新增的业务产品,以及相关联表
			//	product.setBpId(seqService.createKey(Constants.PRODUCT_SEQ, "%01d"));
				product.setEffectiveStatus(1);//新增的业务产品，默认为生效
				product.setCreateTime(new Date());
				product.setCreatePerson(userName);
				num = businessProductDefineDao.insert(product);
				if (services != null) {
					Set<Long> serviceIdSet = new HashSet<>();
					for (ServiceInfo service : services) {
						if (service != null) {
							serviceIdSet.add(service.getServiceId());
							if(service.getLinkService()!=null){
								serviceIdSet.add(service.getLinkService());
							}
						}
					}
					Object[]serviceIds = serviceIdSet.toArray();
					businessProductInfoDao.insertBatchService(product.getBpId().toString(),serviceIds);
				}
				if (items != null) {
					for (AddRequireItem item : items) {
						if (item != null) {
							Map<String, String> map = new HashMap<>();
							map.put("bpId", product.getBpId().toString());
							map.put("itemId", item.getItemId().toString());
							businessRequireItemDao.insert(map);
						}
					}
				}
				if (product.getLimitHard()!=null && "0".equals(product.getLimitHard())){	//硬件产品不限，业务产品和硬件的关联表将硬件的ID置为“0”表示不限
					businessProductHardwareDao.insert("0",product.getBpId().toString());
				} else {										
					if (hards != null) {
						businessProductHardwareDao.insertBatch(hards, product.getBpId().toString());
					}
				}
			}
		}
		return num;
	}

	@Override
	public Map<String, Object> selectLinkInfo(String bpId) {
		Map<String, Object> msg = new HashMap<>();
		List<TeamInfo> allTeam = teamInfoDao.selectTeamName();
		List<ServiceInfo> allService = serviceDao.findAllServiceName();
		List<AddRequireItem> allItem = addRequireItemDao.selectAllRequireName();
		List<HardwareProduct> allHard = hardwareProductDao.findAllHardwareName();
//		List<String> hardIds = businessProductHardwareDao.findByProduct(bpId);
		List<BusinessProductDefine> otherProducts = businessProductDefineDao.queryOtherProduct(bpId);
		List<BusinessProductDefine> otherOemProducts = businessProductDefineDao.queryOtherOemProduct(bpId);
		msg.put("allService", allService);
		msg.put("allItem", allItem);
		msg.put("allHard", allHard);
		msg.put("allTeam", allTeam);
		msg.put("otherProducts", otherProducts);
		msg.put("otherOemProducts", otherOemProducts);
		return msg;
	}

	@Override
	public Map<String, Object> selectDetailById(String id) {
		Map<String, Object> msg = new HashMap<>();
		BusinessProductDefine product = businessProductDefineDao.selectDetailById(id);
//		if(product.getTwoCode() != null && !"".equals(product.getTwoCode())){
//			String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, product.getTwoCode(), new Date(64063065600000L));
//			product.setTwoCodeUrl(url);
//		}
		if(product.getBpImg() != null && !"".equals(product.getBpImg())){
			String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, product.getBpImg(), new Date(64063065600000L));
			product.setBpImgUrl(url);
		}
		// 利用查询得到的业务产品ID，查询中间关联表`business_product_info`，查出所有对应的服务id
		// 通过这些服务ID，查询得到服务的ID和名称
		List<String> serviceIds = businessProductInfoDao.findByProduct(id);
		List<ServiceInfo> services = new ArrayList<>();
		if (serviceIds != null) {
			for (String serviceId : serviceIds) {
				ServiceInfo service = serviceDao.findServiceName(serviceId);
				if(service==null){
					continue;
				}
				//费率是否区分银行卡种类:1-是，0-否  节假日标志:1-只工作日，2-只节假日，0-不限
				boolean rateCardFlag = service.getRateCard()==null?false:1==service.getRateCard();//费率是否区分银行卡种类
				boolean rateHolidaysFlag = service.getRateHolidays()==null?false:1==service.getRateHolidays();//费率是否区分节假日
				ServiceRate dJWrate = null;//贷记卡工作日
				ServiceRate jJWrate = null;//借记卡工作日

				if(rateCardFlag){
					if(rateHolidaysFlag){
						dJWrate =serviceProService.getRate("1", service.getServiceId(), "1");
						jJWrate = serviceProService.getRate("2", service.getServiceId(), "1");
					}else{
						dJWrate = serviceProService.getRate("1", service.getServiceId(), "0");
						jJWrate = serviceProService.getRate("2", service.getServiceId(), "0");
					}
				}else{
					if(rateHolidaysFlag){
						ServiceRate wRate = serviceProService.getRate("0", service.getServiceId(), "1");
						dJWrate = wRate;
						jJWrate = wRate;
					}else{
						ServiceRate rate = serviceProService.getRate("0", service.getServiceId(), "0");
						dJWrate = rate;
						jJWrate = rate;
					}
				}
				if(jJWrate!=null){
					jJWrate.setMerRate(serviceProService.profitExpression(jJWrate));
					service.setjRate(jJWrate.getMerRate());
				}
				if(dJWrate!=null){
					dJWrate.setMerRate(serviceProService.profitExpression(dJWrate));
					service.setdRate(dJWrate.getMerRate());
				}

				services.add(service);
			}
		}
		// 利用查询得到的业务产品ID，查询中间关联表`business_require_item`，查出所有对应的服务id
		// 通过这些服务ID，查询得到服务的ID和名称
		List<String> requireIds = businessRequireItemDao.findByProduct(id);
		List<AddRequireItem> items = new ArrayList<>();
		if (requireIds != null) {
			for (String requireId : requireIds) {
				AddRequireItem item = addRequireItemDao.selectRequireName(requireId);
				items.add(item);
			}
		}
		// 利用查询得到的业务产品ID，查询中间关联表`business_product_hardware`，查出所有对应的硬件产品id
		// 通过这些硬件产品ID，查询得到硬件产品的ID和名称
		List<HardwareProduct> hards = businessProductHardwareDao.findByProduct(id.toString());
		if(hards != null && hards.size()>0){
			product.setLimitHard("1");
		} else {
			product.setLimitHard("0");
		}
		
		msg.put("status", true);
		msg.put("product", product);
		msg.put("services", services);
		msg.put("items", items);
		msg.put("hards", hards);
		return msg;
	}

	@Override
	public boolean selectRecord(Integer bpId) {
		boolean flag = false;
		if(bpId != null){
			Integer abpId = agentBusinessProductDao.findIdByBp(bpId.toString());
			Integer mbpId = merchantBusinessProductDao.findIdByBp(bpId.toString());
			if(abpId != null || mbpId != null){
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public int selectExistName(Long bpId, String bpName, String type) {
		return businessProductInfoDao.selectExistName(bpId, bpName, type);
	}

	@Override
	public List<Long> findByService(Long serviceId) {
		return businessProductInfoDao.findByService(serviceId);
	}

	@Override
	public Map<String, Object> getProductBase(Integer bpId) {
		Map<String, Object> msg = new HashMap<>();
		BusinessProductDefine product = businessProductInfoDao.getProductBase(bpId);
		if(product.getBpImg() != null && !"".equals(product.getBpImg())){
			String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, product.getBpImg(), new Date(64063065600000L));
			product.setBpImgUrl(url);
		}
		List<BusinessProductDefine> list = businessProductInfoDao.getOtherProduct(product);
		
		// 利用查询得到的业务产品ID，查询中间关联表`business_product_hardware`，查出所有对应的硬件产品id
		// 通过这些硬件产品ID，查询得到硬件产品的ID和名称
		List<HardwareProduct> hards = businessProductHardwareDao.findByProduct(bpId.toString());
		if(hards != null && hards.size()>0){
			product.setLimitHard("1");
		} else {
			product.setLimitHard("0");
		}
		List<HardwareProduct> allHards = hardwareProductDao.findAllHardwareName();
		//gw
		List<AddRequireItem> allItem = addRequireItemDao.selectAllRequireName();
		msg.put("allItem", allItem);
		
		List<AddRequireItem> bpIdItem = addRequireItemDao.selectItemByBpId(bpId.toString());
		msg.put("bpIdItem", bpIdItem);
		//
		msg.put("allHards", allHards);
		msg.put("hards", hards);
		msg.put("product", product);
		msg.put("otherProducts", list);
		return msg;
				
	}

	@Override
	public int update(BusinessProductDefine product) {
		return businessProductDefineDao.update(product);
	}

	@Override
	public int updateProductBase(String params) {
		JSONObject json = JSON.parseObject(params);
		BusinessProductDefine product = json.getObject("baseInfo", BusinessProductDefine.class);
		String bpId = product.getBpId().toString();
		String [] reqItemsArray ={};
		String [] sItemsArray ={};
        StringBuilder reqItems = new StringBuilder("");
        StringBuilder sItems = new StringBuilder("");
		List<AddRequireItem> items = JSON.parseArray(json.getJSONArray("items").toJSONString(),AddRequireItem.class);
		//对进件项进行处理开始
		if (items != null) {
			for (AddRequireItem item : items) {
				if (item != null) {
					reqItems.append(item.getItemId().toString());
					reqItems.append(",");
				}
			}
		}
			if(reqItems.length()>0){
				reqItemsArray =  reqItems.toString().split(",");
			}
			
			List<String> requireIds = businessRequireItemDao.findByProduct(bpId);
			
			if (requireIds != null) {
				for (String requireId : requireIds) {
					sItems.append(requireId);
					sItems.append(",");
				}
			}
			
			if(sItems.length()>0){
				sItemsArray =  sItems.toString().split(",");
			}
			
			
		log.info(bpId+"原items是==>"+ Arrays.asList(sItemsArray)+"修改之后==>"+Arrays.asList(reqItemsArray));
		//String[] result_union = StringUtil.union(sItemsArray, reqItemsArray);//
		String[] result_insect = StringUtil.intersect(sItemsArray, reqItemsArray);//交集不变
		String[] delResult_minus = StringUtil.minus(result_insect, sItemsArray);//差集
		String[] insertResult_minus = StringUtil.minus(result_insect, reqItemsArray);//差集
		for (String str : delResult_minus) {
			System.out.println("交集需要删除==>" + str);
			businessRequireItemDao.deleteProductByItemAndbpId(bpId, str);
		}
		for (String str : insertResult_minus) {
			System.out.println("修改之后需要插入==>" + str);
			businessRequireItemDao.insertProductByItemAndbpId(bpId, str);
		}
		//对进件项进行处理结束
		List<HardwareProduct> hards = JSON.parseArray(json.getJSONArray("hards").toJSONString(),
				HardwareProduct.class);
		int num = businessProductDefineDao.update(product);
		businessProductHardwareDao.deleteProductByPid(product.getBpId().toString());
		if (product.getLimitHard()!=null && "0".equals(product.getLimitHard())){	//硬件产品不限，业务产品和硬件的关联表将硬件的ID置为“0”表示不限
			businessProductHardwareDao.insert("0",product.getBpId().toString());
		} else {					
			businessProductHardwareDao.insertBatch(hards,product.getBpId().toString());
		}
		return num;
	}

	@Override
	public BusinessProductDefine selectBybpIdAndAgentNo(String bpId, String agentNo) {
		
		return businessProductDefineDao.selectBybpIdAndAgentNo(bpId, agentNo);
	}

	@Override
	public List<BusinessProductDefine> getProductByServiceType(String[] serviceTypes) {
		return businessProductDefineDao.getProductByServiceType(serviceTypes);
	}

	@Override
	public List<BusinessProductDefine> getProduct() {
		return businessProductDefineDao.getProduct();
	}

	@Override
	public List<BusinessProductDefine> getProductByTeam(String teamId) {
		List<String> teamIdList = new ArrayList<>();
		if(StringUtils.isNotEmpty(teamId)) {
			teamIdList = Arrays.asList(teamId.split(","));
		}
		return businessProductDefineDao.getProductByTeam(teamIdList);
	}

	@Override
	public List<BusinessProductDefine> getTeamOtherBp(String teamId, String bpId) {
		return businessProductDefineDao.getTeamOtherBp(teamId, bpId);
	}

	@Override
	public Result updateEffectiveStatus(BusinessProductDefine baseInfo) {
		Result result = new Result();
		if(baseInfo == null || baseInfo.getBpId() == null
				|| baseInfo.getEffectiveStatus() == null){
			result.setMsg("参数非法");
			return result;
		}
		if(baseInfo.getEffectiveStatus() == 1){
			result.setMsg("失效业务产品不能再生效");
			return result;
		}
		//如果业务产品下，有服务未失效，提示服务名称，并返回
		if (existsEffectiveService(baseInfo, result)) return result;
		//检查自己是队长还是队员
		BusinessProductDefine product = businessProductDefineDao.selectBybpId(String.valueOf(baseInfo.getBpId()));
		if(product.getAllowIndividualApply() == 1){
			//如果队员有未失效的业务产品，提示业务产品名称，并返回
			if (existsEffectiveProduct(baseInfo, result)) return result;
		}
		//更改业务产品状态
		int num = businessProductDefineDao.updateEffectiveStatus(baseInfo);
		if(num == 1){
			result.setStatus(true);
			result.setMsg("操作成功");
		} else {
			result.setMsg("操作失败");
		}
		return result;
	}

	/**
	 * 检查是否有生效的队员
	 * @param baseInfo
	 * @param result
	 * @return
	 */
	private boolean existsEffectiveProduct(BusinessProductDefine baseInfo, Result result) {
		List<BusinessProductDefine> effectiveProductList = businessProductDefineDao.selectEffectiveMemberProductByBpId(baseInfo.getBpId());
		if(effectiveProductList != null && effectiveProductList.size() > 0){
            StringBuilder productName = new StringBuilder();
            for(BusinessProductDefine item: effectiveProductList){
                productName.append(item.getBpName()).append(",");
            }
            result.setMsg("有队员业务产品在使用中:" + productName.substring(0, productName.length() - 1) + "。");
			return true;
        }
		return false;
	}

	/**
	 * 检查是否有生效的服务
	 * @param baseInfo
	 * @param result
	 * @return
	 */
	private boolean existsEffectiveService(BusinessProductDefine baseInfo, Result result) {
		List<ServiceInfo> effectiveServiceList = businessProductDefineDao.selectEffectiveServiceByBpId(baseInfo.getBpId());
		if(effectiveServiceList != null && effectiveServiceList.size() > 0){
			StringBuilder serviceName = new StringBuilder();
			for(ServiceInfo item: effectiveServiceList){
				serviceName.append(item.getServiceName()).append(",");
			}
			result.setMsg("有服务在使用中:" + serviceName.substring(0, serviceName.length() - 1) + "。");
			return true;
		}
		return false;
	}

	@Override
	public List<String> selectTeamIdsWithBpIds(List<String> ids) {
		return businessProductDefineDao.selectTeamIdsWithBpIds(ids);
	}

	@Override
	public List<BusinessProductDefine> selectAllInfoByTeamId(String teamId) {
		if(StringUtils.isNotEmpty(teamId)){
			return businessProductDefineDao.selectAllInfoByTeamId(teamId);
		}else{
			return businessProductDefineDao.selectAllInfo();
		}

	}

	class BpdExport{
		private String[] cols;
		private String[] colsName;
		private double[] cellWidth;
		private List<Map<String, String>> data;
		private SXSSFWorkbook wb;
		private Sheet sheet;
		private int rowIdx = 0;
		public BpdExport() {
			wb = new SXSSFWorkbook(100);
			sheet = wb.createSheet("Sheet1");
		}

		public void customExport(String[] cols, String[] colsName, List<Map<String, Object>> data, OutputStream outputStream)
				throws IOException {
			this.cols = cols;
			this.colsName = colsName;
			builTitle();
			customWriteData(data);
			wb.write(outputStream);
			outputStream.flush();
		}
		/**
		 * 构建表格表头
		 *
		 * @throws IOException
		 */
		private void builTitle() throws IOException {
			Cell cell = null;
			Row row = null;
			int cols = this.colsName.length;
			CellStyle headStyle = createHeaderStyle(wb);
			row = sheet.createRow(rowIdx);
			row.setHeightInPoints((short) 25);
			for (short i = 0; i < cols; i++) {
				double _cellWidth  = 6000 ;
				if (cellWidth != null) {
					_cellWidth = cellWidth[i];
				}
				sheet.setColumnWidth(i, (short) _cellWidth);
				cell = row.createCell(i);
				cell.setCellStyle(headStyle);
				// 定义单元格为字符串类型,不设置默认为“常规”
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(this.colsName[i]);
			}
		}
		private void customWriteData(List<Map<String, Object>> rowData) {
			Object cellValue = null;
			try {
				CellStyle bodyStyle = customCreateBodyStyle(wb);
				int mainMergeRowBegin = 1;//主表信息开始合并行数
				int mainMergeRowEnd = 0;//主表信息结束合并行数

				for (int i = 0; i < rowData.size(); i++) {
					int maxRow = Integer.parseInt(rowData.get(i).get("maxRow").toString());
					if(maxRow>1){
						List<ServiceInfo> serviceInfos = rowData.get(i).get("serviceInfos")!=null?(List<ServiceInfo>)rowData.get(i).get("serviceInfos"):null;
						List<HardwareProduct> hardInfos = rowData.get(i).get("hardInfos")!=null?(List<HardwareProduct>)rowData.get(i).get("hardInfos"):null;
						List<AddRequireItem> itemInfos = rowData.get(i).get("itemInfos")!=null?(List<AddRequireItem>)rowData.get(i).get("itemInfos"):null;
						int hardSize = hardInfos==null?0:hardInfos.size();
						int serviceSize = serviceInfos==null?0:serviceInfos.size();
						int itemSize = itemInfos==null?0:itemInfos.size();
						for (int autoRow = 0; autoRow < maxRow; autoRow++) {
							int currentRow = ++rowIdx;
							mainMergeRowEnd = currentRow +1 ;
							Row row = sheet.createRow(currentRow);
							for (short j = 0; j < this.cols.length; j++) {
								Cell cell = row.createCell(j);
								// 定义单元格为字符串类型,不设置默认为“常规”
								cell.setCellType(Cell.CELL_TYPE_STRING);
								cell.setCellStyle(bodyStyle);
								String col = this.cols[j];
								if("hardName".equals(col)){
									if(hardInfos!=null && autoRow < hardSize){
										if(hardInfos.get(autoRow)!=null){
											cellValue = hardInfos.get(autoRow).getTypeName();
										}
									}
								}else if("serviceName".equals(col)){
									if(serviceInfos!=null && autoRow < serviceSize){
										if(serviceInfos.get(autoRow)!=null){
											cellValue = serviceInfos.get(autoRow).getServiceName();
										}
									}
								}else if("serviceType".equals(col)){
									if(serviceInfos!=null && autoRow < serviceSize){
										if(serviceInfos.get(autoRow)!=null){
											cellValue = serviceInfos.get(autoRow).getServiceTypeName();
										}
									}
								}else if("tFlag".equals(col)){
									if(serviceInfos!=null && autoRow < serviceSize){
										if(serviceInfos.get(autoRow)!=null){
											Integer tFlag = serviceInfos.get(autoRow).gettFlag();
											cellValue = tFlag==null?"":tFlag==0?"不涉及":tFlag==1?"T0":tFlag==2?"T1":tFlag==3?"T0和T1":"";//T0T1标志：0-不涉及，1-T0，2-T1, 3-T0和T1
										}
									}
								}else if("itemName".equals(col)){
									if(itemInfos!=null && autoRow < itemSize){
										if(itemInfos.get(autoRow)!=null){
											cellValue = itemInfos.get(autoRow).getItemName();
										}
									}
								}else{
									cellValue = rowData.get(i).get(col);
								}
								cell.setCellValue(cellValue == null ? "" : cellValue.toString());
								cellValue = null;
							}
						}
						long begin1 = System.currentTimeMillis();
						//合并主表信息单元格
						if(mainMergeRowEnd > 0){
							for (int i1 = 0; i1 < 13; i1++) {
								CellRangeAddress cra = new CellRangeAddress(mainMergeRowBegin,mainMergeRowEnd-1,i1,i1);
								sheet.addMergedRegion(cra);
							}
							mainMergeRowBegin = mainMergeRowEnd ;
						}
						long end1 = System.currentTimeMillis();
						if((end1-begin1)>100){
							log.info("合并单元格消耗时间过长： "+(end1-begin1)+" 合并条数: "+maxRow);
						}
					}else{
						// 创建行
						Row row = sheet.createRow(++rowIdx);
						for (short j = 0; j < this.cols.length; j++) {
							Cell cell = row.createCell(j);
							// 定义单元格为字符串类型,不设置默认为“常规”
							cell.setCellType(Cell.CELL_TYPE_STRING);
							cell.setCellStyle(bodyStyle);
							cellValue = rowData.get(i).get(this.cols[j]);
							cell.setCellValue(cellValue == null ? "" : cellValue.toString());
						}
					}
				}

			} catch (Exception e) {
				log.error("业务产品服务导出异常",e);
			}
		}
		/**
		 * 构建表格列头样式
		 *
		 * @param wb
		 * @return
		 */
		private CellStyle createHeaderStyle(Workbook wb) {

			// 设置字体
			Font font = wb.createFont();
			font.setFontHeightInPoints((short) 12); // 字体高度
			font.setColor(Font.COLOR_NORMAL); // 字体颜色
			font.setFontName("黑体"); // 字体
			font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 宽度

			// 设置单元格样式
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFont(font);
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER); // 水平布局：居中
			// 边框
			cellStyle.setBorderTop((short) 1);
			cellStyle.setBorderBottom((short) 1);
			cellStyle.setBorderLeft((short) 1);
			cellStyle.setBorderRight((short) 1);

			cellStyle.setWrapText(true);
			return cellStyle;

		}
		private CellStyle customCreateBodyStyle(Workbook wb) {

			// 设置单元格样式
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER); // 水平布局：居中
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中
			// 边框
			cellStyle.setBorderTop((short) 1);
			cellStyle.setBorderBottom((short) 1);
			cellStyle.setBorderLeft((short) 1);
			cellStyle.setBorderRight((short) 1);

			cellStyle.setWrapText(true);
			return cellStyle;
		}
	}

}
