package cn.eeepay.framework.service.impl.capitalInsurance;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateOrder;
import cn.eeepay.framework.util.ListDataExcelExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.dao.capitalInsurance.BillEneryDao;
import cn.eeepay.framework.dao.capitalInsurance.ShareReporDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.ShareReport;
import cn.eeepay.framework.model.capitalInsurance.ShareReportTotal;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.capitalInsurance.ShareReporService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.StringUtil;

@Service("shareReporService")
@Transactional
public class ShareReporServiceImpl implements ShareReporService{
	private static final Logger log = LoggerFactory.getLogger(ShareReporServiceImpl.class);
	@Resource
	private ShareReporDao shareReporDao;
	@Resource
	private BillEneryDao billEneryDao;
	@Resource
	private SysDictService dictService;

	@Resource
	private AgentInfoDao agentInfoDao;

	@Override
	public List<ShareReport>  queryAllInfo(ShareReport tis, Page<ShareReport> page) {
		return shareReporDao.queryAllInfo(tis,page);
		
	}

	@Override
	public List<ShareReport> importDetailSelect(ShareReport tis) {
		return shareReporDao.importDetailSelect(tis);
	}

	@Override
	public ShareReportTotal queryAmount(ShareReport tis) {
		return shareReporDao.queryAmount(tis);
	}

	@Override
	public Integer isInitShareReport(String reportMonth) {
		return shareReporDao.isInitShareReport(reportMonth);
	}

	@Override
	public Integer initShareReport(ShareReport report, String sDate, String eDate) {
		Integer count =  shareReporDao.initShareReport(report);
		if(count > 0){
			log.info("汇总数据新增成功，batchNo：{}",report.getBatchNo());
			Integer updateCount = billEneryDao.updateReportStatus(report.getBatchNo(),sDate,eDate);
			log.info("对账明细汇总完成，batchNo：{},count：{}",report.getBatchNo(),updateCount);
			return 1;
		}else{
			return 0;
		}
	}

	@Override
	public int insuranceProfitRecordAccount(Integer id) {
		ShareReport report = shareReporDao.getShareReport(id);
		String url = dictService.getValueByKey("ACCOUNT_SERVICE_URL");
		if(StringUtil.isBlank(url)){
			log.error("账户服务器地址不存在{ACCOUNT_SERVICE_URL}");
			return 0;
		}
		String agentNo = report.getOneAgentNo();
		AgentInfo  agentInfo = agentInfoDao.select(agentNo);
		if("11".equals(agentInfo.getAgentType())){
			//return peragentProfitRecordAccount(report,url,id);
			//超级盟主 超级盟主 入账比例0,的直接更新为已入账
			return shareReporDao.updateAccountStatus(id,1);
		}else{
			//其他
			return profitRecordAccount(report,url,id);
		}
	}
	//普通保费分润入账
	private int  profitRecordAccount(ShareReport report,String url,Integer id){
		Map<String, Object> claims = new HashMap<>();
		claims.put("fromSystem","boss");// 来源系统  boss
		claims.put("transDate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));// 交易日期
		claims.put("amount",report.getShareAmount().toString());// 金额
		claims.put("fromSerialNo",report.getBatchNo()+"-"+report.getOneAgentNo());// 来源系统流水号 这玩意唯一
		claims.put("agentNo",report.getOneAgentNo());// 代理商编号
		claims.put("transTypeCode","000101");// 交易码
		claims.put("transOrderNo",report.getBatchNo());// 交易订单号
		String acc = ClientInterface.baseClient(url+"/insuranceController/insuranceProfitRecordAccount.do", claims);
		log.info("资金险代理商月分润入账[{}]", acc);
		if (JSONObject.parseObject(acc).getBooleanValue("status")) {
			log.info(id + "入账成功");
			shareReporDao.updateAccountStatus(id,1);
			return 1;
		} else {
			log.info(id + "入账失败");
			shareReporDao.updateAccountStatus(id,2);
			return 0;
		}
	}
	//超级盟主保费分润入账
	private int  peragentProfitRecordAccount(ShareReport report,String url,Integer id){
		Map<String, Object> claims = new HashMap<>();
		claims.put("agentNo", report.getOneAgentNo());// 超级盟主商编号
		claims.put("amount", report.getShareAmount().toString());// 分润金额
		claims.put("transTypeCode", "000118");// 交易码固定
		claims.put("fromSerialNo", report.getBatchNo()+"-"+report.getOneAgentNo());// 来源系统流水号
		claims.put("transOrderNo", report.getBatchNo());// 发生交易订单号
		claims.put("fromSystem", "boss");// 来源系统固定
		claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));// 记账日期
		String acc = ClientInterface.baseClient(url+"/peragentController/peragentAccount.do", claims);
		log.info("资金险代理商月分润入账[{}]", acc);
		if (JSONObject.parseObject(acc).getBooleanValue("status")) {
			log.info(id + "入账成功");
			shareReporDao.updateAccountStatus(id,1);
			return 1;
		} else {
			log.info(id + "入账失败");
			shareReporDao.updateAccountStatus(id,2);
			return 0;
		}
	}


	@Override
	public void importDetail(List<ShareReport> list, HttpServletResponse response) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		String fileName = "代理商分润月表列表"+sdf.format(new Date())+".xlsx" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		if(list.size()<1){
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("batchNo",null);
			maps.put("createTime", null);
			maps.put("billMonth",null);
			maps.put("oneAgentNo",null);
			maps.put("oneAgentName",null);
			maps.put("agentType",null);
			maps.put("totalAmount",null);
			maps.put("totalCount",null);
			maps.put("shareRate",null);
			maps.put("shareAmount",null);
			maps.put("accountStatus",null);
			maps.put("accountTime", null);
			data.add(maps);
		}else{
			Map<String, String> accountStatusMap=new HashMap<String, String>();
			accountStatusMap.put("1","已入账");
			accountStatusMap.put("2","入账失败");
			accountStatusMap.put("3","未入账");
			Map<String, String> agentTypeList= dictService.selectMapByKey("AGENT_TYPE");
			

			for (ShareReport or : list) {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("batchNo",or.getBatchNo()==null?null:or.getBatchNo());
				maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
				maps.put("billMonth",or.getBillMonth()==null?null:or.getBillMonth());
				maps.put("oneAgentNo",or.getOneAgentNo()==null?null:or.getOneAgentNo());
				maps.put("oneAgentName",or.getOneAgentName()==null?null:or.getOneAgentName());
				maps.put("agentType",agentTypeList.get(or.getAgentType()));
				maps.put("totalAmount",or.getTotalAmount()==null?"":or.getTotalAmount().toString());
				maps.put("totalCount",or.getTotalCount()==null?"":or.getTotalCount().toString());
				maps.put("shareRate",or.getShareRate()==null?"":or.getShareRate().toString());
				maps.put("shareAmount",or.getShareAmount()==null?"":or.getShareAmount().toString());
				maps.put("accountStatus",accountStatusMap.get(or.getAccountStatus().toString()));
				maps.put("accountTime", or.getAccountTime()==null?"":sdf1.format(or.getAccountTime()));
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"batchNo","createTime","billMonth","oneAgentNo","oneAgentName","agentType",
				"totalAmount","totalCount","shareRate","shareAmount","accountStatus","accountTime"
		};
		String[] colsName = new String[]{"汇总批次号","汇总时间","保单创建月份","一级代理商编号","一级代理商名称","代理商类型",
				"保费总金额","保单数","代理商分润百分比(%)","代理商分润金额","分润入账状态","入账时间"
		};
		OutputStream ouputStream =null;
		try {
			ouputStream=response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		}catch (Exception e){
			log.error("导出代理商分润月表列表失败!",e);
		}finally {
			if(ouputStream!=null){
				ouputStream.close();
			}
		}
	}

}
