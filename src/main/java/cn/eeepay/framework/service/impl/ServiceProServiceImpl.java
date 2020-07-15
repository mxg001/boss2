package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.*;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.ServiceProService;
import cn.eeepay.framework.util.ListDataExcelExport;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Description: <／p>
 * <p>Company: ls.eeepay.cn<／p> 
 * @author 沙
 * @date 2016年5月11日
 */
@Service("serviceProService")
@Transactional
public class ServiceProServiceImpl implements ServiceProService {
	private static final Logger log = LoggerFactory.getLogger(ServiceProService.class);
	private static final Pattern pattern=Pattern.compile("^(\\d+(\\.\\d+)?)~(\\d+(\\.\\d+)?)%~(\\d+(\\.\\d+)?)$");
	private static final DecimalFormat format=new java.text.DecimalFormat("0.00");
	@Resource
	private ServiceDao serviceDao;
	@Resource
	private SeqService seqService;
	@Resource
	private BusinessProductInfoDao businessProductInfoDao;
	
	@Resource
	private AgentInfoDao agentInfoDao;
	
	@Resource
	private AgentInfoService agentInfoService;

	@Resource
	private SysDictDao sysDictDao;
	
	@Resource
	private AgentBusinessProductDao agentBusinessProductDao;
	
	public int existServiceName(ServiceInfo serviceInfo){
		return serviceDao.existServiceName(serviceInfo);
	}

	public int existAgentShowName(ServiceInfo serviceInfo){
		return serviceDao.existAgentShowName(serviceInfo);
	}

	/**
	 * 添加服务类别的基础信息
	 * @param serviceInfo
	 * @return 
	 */
	@Override
	public int insertServiceInfo(ServiceInfo serviceInfo){
		if(serviceInfo==null){
			return 0;
		}
		int serviceType = serviceInfo.getServiceType();
		if(serviceType == 10000 || serviceType == 10001){//贴现与关联提现服务类型
			serviceInfo.settFlag(null);
		}
		if(serviceInfo.gettFlag()==null || serviceInfo.gettFlag() == 0 || serviceInfo.gettFlag() == 2){
			serviceInfo.setLinkService(null);
		}
		int num=serviceDao.insertServiceInfo(serviceInfo);
		if(serviceInfo.getRates()!=null&&serviceInfo.getRates().size()>0){
			for(ServiceRate rate:serviceInfo.getRates()){
				rate.setServiceId(serviceInfo.getServiceId());
				rate.setIsGlobal(1);
			}
			serviceDao.insertServiceRateList(serviceInfo.getRates());
		}	
		if(serviceInfo.getQuotas()!=null&&serviceInfo.getQuotas().size()>0){
			for(ServiceQuota quota:serviceInfo.getQuotas()){
				quota.setServiceId(serviceInfo.getServiceId());
				quota.setIsGlobal(1);
			}
			serviceDao.insertServiceQuotaList(serviceInfo.getQuotas());
		}
		return num;
	}
	/**
	 * 批量添加服务费率
	 * @param list
	 * @return 条数
	 */
	@Override
	public int insertServiceRateList(List<ServiceRate> list){
		return serviceDao.insertServiceRateList(list);
	}
	
	/**
	 * 批量添加服务额度
	 * @param list
	 * @return
	 */
	@Override
	public int insertServiceQuotaList(List<ServiceQuota> list){
		return serviceDao.insertServiceQuotaList(list);
	}
	
	/**
	 * 更新服务信息,更新服务定义
	 * @param serviceInfo
	 * @return
	 */
	@Override
	public int updateServiceInfo(ServiceInfo serviceInfo){
		int numServiceName = serviceDao.existServiceName(serviceInfo);
		if(numServiceName==1){
			throw new RuntimeException("服务名称已存在!");
		}
		int numShowAgentName = serviceDao.existAgentShowName(serviceInfo);
		if(numShowAgentName == 1){
			throw new RuntimeException("代理商展示名称已存在!");
		}
		int serviceType = serviceInfo.getServiceType();
		if(serviceType==10000||serviceType==10001){//贴现与关联提现服务类型
			serviceInfo.settFlag(null);
		}
		if(serviceInfo.gettFlag()==null || serviceInfo.gettFlag()==0||serviceInfo.gettFlag()==2 ){
			serviceInfo.setLinkService(null);
		}
		
		int count=serviceDao.updateServiceInfo(serviceInfo);
		if(count!=1){
			throw new RuntimeException("修改失败");
		}
		serviceDao.deleteServiceRateByFK(serviceInfo.getServiceId(),"0");
		serviceDao.deleteServiceQuotaByFK(serviceInfo.getServiceId(),"0");
		serviceDao.insertServiceRateList(serviceInfo.getRates());
		serviceDao.insertServiceQuotaList(serviceInfo.getQuotas());
		//修改一级代理商勾选了“与公司一致”的费率和限额
		for(ServiceRate rate: serviceInfo.getRates()){
			serviceDao.updateAgentServiceRateList(rate);
		}
		for(ServiceQuota quota: serviceInfo.getQuotas()){
			serviceDao.updateAgentServiceQuotaList(quota);
		}
		return count;
	}
	
	/**
	 * 删除服务信息,删除服务定义
	 * @param serviceInfo
	 * @return
	 */
	@Override
	public int deleteServiceInfo(ServiceInfo serviceInfo){
		serviceDao.deleteServiceRateByFK(serviceInfo.getServiceId(),"0");
		serviceDao.deleteServiceQuotaByFK(serviceInfo.getServiceId(),"0");
		return serviceDao.deleteServiceInfo(serviceInfo.getServiceId());
	}

	@Override
	public void getServiceInfoToExport(Map<String, Object> jsonMap, HttpServletResponse response,Map<String, Object> msgMap) {

		try {
			List<ServiceInfo> serviceInfos= serviceDao.getServiceInfoToExport(jsonMap);
			if(serviceInfos==null || serviceInfos.size()<=0){
				return;
			}
			List<Map<String, String>> data = new ArrayList<Map<String, String>>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String fileName = "服务种类查询"+sdf.format(new Date())+".xlsx";
			String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
			long begin = System.currentTimeMillis();
			for (ServiceInfo info : serviceInfos) {
				Map<String,String> map = new HashMap<>();
				//服务名称
				map.put("serviceName",info.getServiceName());
				//代理商展示名称
				map.put("agentName",info.getAgentShowName());
				//费率区分节假日
				map.put("rateHolidays",switchYN(info.getRateHolidays()));
				//服务类型
				map.put("serviceType",info.getServiceTypeName());
				//额度区分银行卡种类
				map.put("quotaCard",switchYN(info.getQuotaCard()));
				//限额区分节假日
				map.put("quotaHolidays",switchYN(info.getQuotaHolidays()));
				//服务管控费率固定标志
				map.put("fixedRate",switchGuDing(info.getFixedRate()));
				//服务管控额度固定标志
				map.put("fixedQuota",switchGuDing(info.getFixedQuota()));
				//费率区分银行卡种类
				map.put("rateCard",switchYN(info.getRateCard()));
				//可使用日期
				map.put("useTime",(info.getUseStarttime()!=null?sdf.format(info.getUseStarttime()):"")+"--"+(info.getUseEndtime()!=null?sdf.format(info.getUseEndtime()):""));
				//每日允许交易时间
				map.put("tradTime",info.getTradStart()+"--"+info.getTradEnd());
				//可用银行卡
				map.put("bankCard",info.getBankCard()==null?"":0==info.getBankCard()?"不限":1==info.getBankCard()?"信用卡":2==info.getBankCard()?"银行卡":"");
				//T0T1标志 0-不涉及，1-T0，2-T1, 3-T0和T1
				map.put("tFlag",info.gettFlag()==null?"":0==info.gettFlag()?"不涉及":1==info.gettFlag()?"T0":2==info.gettFlag()?"T1":3==info.gettFlag()?"T0和T1":"");
				//关联提现服务
				map.put("linkService",info.getLinkServiceName());
				
				boolean rateCardFlag = info.getRateCard()==null?false:1==info.getRateCard();//费率是否区分银行卡种类
				boolean rateHolidaysFlag = info.getRateHolidays()==null?false:1==info.getRateHolidays();//费率是否区分节假日
				boolean quotaCardFlag = info.getQuotaCard()==null?false:1==info.getQuotaCard();//额度区分银行卡种类
				boolean quotaHolidaysFlag = info.getQuotaHolidays()==null?false:1==info.getQuotaHolidays();////限额区分节假日



				long begin1 = System.currentTimeMillis();
				List<ServiceRate> srsList = serviceDao.getServiceRate(info.getServiceId(),"0");
				List<ServiceQuota> sqsList = serviceDao.getServiceQuota(info.getServiceId(),"0");

				List<ServiceRate> srs = getServiceRateByCondition(rateCardFlag,rateHolidaysFlag,srsList);
				List<ServiceQuota> sqs = getServiceQuotaByCondition(quotaCardFlag,quotaHolidaysFlag,sqsList);
				long end1 = System.currentTimeMillis();
				log.info("消耗时间------------ ："+(end1 - begin1) );


				String[] cardType = {"贷记卡","贷记卡","借记卡","借记卡"};

				for (int i = 0; i < 4; i++) {
					ServiceRate currSr = srs.get(i);
					if(currSr!=null){
						map.put("cardType"+i,cardType[i]);//银行卡种类
						map.put("rateType"+i,"1".equals(currSr.getRateType())?"每笔固定金额":"2".equals(currSr.getRateType())?"扣率":"3".equals(currSr.getRateType())?"扣率带保底封顶":
								"4".equals(currSr.getRateType())?"扣率+固定金额":"5".equals(currSr.getRateType())?"单笔阶梯 扣率":"");//1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯 扣率
						currSr.setMerRate(profitExpression(currSr));
						map.put("rate"+i,currSr.getMerRate()+"");//商户费率
						map.put("holidayMark"+i,"1".equals(currSr.getHolidaysMark())?"工作日":"2".equals(currSr.getHolidaysMark())?"节假日":"0".equals(currSr.getHolidaysMark())?"不限":"");//节假日标记
					}
					ServiceQuota currSq = sqs.get(i);
					if(currSq!=null){
						map.put("todayMax"+i,currSq.getSingleDayAmount()+"");//单日最大交易金额
						map.put("singleMin"+i,currSq.getSingleMinAmount()+"");////单笔最小交易金额
						map.put("singleMax"+i,currSq.getSingleCountAmount()+"");//单笔最大交易金额
						map.put("singleDayCardMax"+i,currSq.getSingleDaycardAmount()+"");//单日单卡最大交易额
						map.put("singleDayCardCount"+i,currSq.getSingleDaycardCount()+"");//单日单卡最大交易笔数
					}
				}
				data.add(map);
			}
			long end = System.currentTimeMillis();
			log.info("总消耗时间########### ："+(end - begin) );

			ListDataExcelExport export = new ListDataExcelExport();
			String[] cols = new String[] { "serviceName", "agentName", "rateHolidays", "serviceType",
					"quotaCard","quotaHolidays","fixedRate","fixedQuota","rateCard","useTime","tradTime","bankCard","tFlag","linkService",
					"cardType0","rateType0","rate0","holidayMark0","todayMax0","singleMin0","singleMax0","singleDayCardMax0","singleDayCardCount0",
					"cardType1","rateType1","rate1","holidayMark1","todayMax1","singleMin1","singleMax1","singleDayCardMax1","singleDayCardCount1",
					"cardType2","rateType2","rate2","holidayMark2","todayMax2","singleMin2","singleMax2","singleDayCardMax2","singleDayCardCount2",
					"cardType3","rateType3","rate3","holidayMark3","todayMax3","singleMin3","singleMax3","singleDayCardMax3","singleDayCardCount3",
			};
			String[] colsName = new String[] { "服务名称", "代理商展示名称", "费率区分节假日", "服务类型",
					"额度区分银行卡种类","限额区分节假日","服务管控费率固定标志","服务管控额度固定标志", "费率区分银行卡种类","可使用日期","每日允许交易时间","可用银行卡","T0T1标志","关联提现服务",
					"银行卡种类","费率类型","商户费率","节假日标记", "单日最大交易金额","单笔最小交易金额","单笔最大交易金额","单日单卡最大交易额","单日单卡最大交易笔数",
					"银行卡种类","费率类型","商户费率","节假日标记", "单日最大交易金额","单笔最小交易金额","单笔最大交易金额","单日单卡最大交易额","单日单卡最大交易笔数",
					"银行卡种类","费率类型","商户费率","节假日标记", "单日最大交易金额","单笔最小交易金额","单笔最大交易金额","单日单卡最大交易额","单日单卡最大交易笔数",
					"银行卡种类","费率类型","商户费率","节假日标记", "单日最大交易金额","单笔最小交易金额","单笔最大交易金额","单日单卡最大交易额","单日单卡最大交易笔数",
			};
			OutputStream outputStream = response.getOutputStream();
			long begin2 = System.currentTimeMillis();
			export.export(cols, colsName, data, outputStream);
			long end2 = System.currentTimeMillis();
			log.info("服务产品导出处理excl消耗时间: "+(end2-begin2));
			outputStream.close();
			msgMap.put("status",true);
		} catch (Exception e) {
			log.error("服务种类查询导出出错! ");
			e.printStackTrace();
			msgMap.put("status", false);
			msgMap.put("msg", "服务种类查询导出出错！");
		}

	}

	private List<ServiceQuota> getServiceQuotaByCondition(boolean rateCardFlag,boolean rateHolidaysFlag,List<ServiceQuota> sqs){
		ArrayList<ServiceQuota> result = new ArrayList<>();
		if(sqs!=null || sqs.size()>0){
			ServiceQuota dJWquota = new ServiceQuota();//贷记卡工作日额度
			ServiceQuota dJHquota = new ServiceQuota();//贷记卡节假日额度
			ServiceQuota jJWquota = new ServiceQuota();//借记卡工作日额度
			ServiceQuota jJHquota = new ServiceQuota();//借记卡节假日额度
			for (ServiceQuota sq : sqs) {
				if(sq.getCardType()==null || sq.getCardType()==null){
					continue;
				}
				if(rateCardFlag){
					if(rateHolidaysFlag){
						if(sq.getCardType().equals("1")){
							if(sq.getHolidaysMark().equals("1")){
								dJWquota = sq;
							}else if(sq.getHolidaysMark().equals("2")){
								dJHquota = sq;
							}
						}else if(sq.getCardType().equals("2")){
							if(sq.getHolidaysMark().equals("1")){
								jJWquota = sq;
							}else if(sq.getHolidaysMark().equals("2")){
								jJHquota = sq;
							}
						}
					}else{
						if(sq.getCardType().equals("1") && sq.getHolidaysMark().equals("0")){
							dJWquota = sq;
							dJHquota = sq;
						}else if(sq.getCardType().equals("2") && sq.getHolidaysMark().equals("0")){
							jJWquota = sq;
							jJHquota = sq;
						}
					}
				}else{
					if(rateHolidaysFlag){
						if(sq.getCardType().equals("0") && sq.getHolidaysMark().equals("1")){
							dJWquota = sq;
							jJWquota = sq;
						}else if(sq.getCardType().equals("0") && sq.getHolidaysMark().equals("2")){
							dJHquota = sq;
							jJHquota = sq;
						}
					}else{
						if(sq.getCardType().equals("0") && sq.getHolidaysMark().equals("0")){
							dJWquota = sq;
							dJHquota = sq;
							jJWquota = sq;
							jJHquota = sq;
						}

					}
				}
			}
			result.add(dJWquota);
			result.add(dJHquota);
			result.add(jJWquota);
			result.add(jJHquota);
		}
		return result;
	}

	private List<ServiceRate> getServiceRateByCondition(boolean rateCardFlag,boolean rateHolidaysFlag,List<ServiceRate> srs){
		ArrayList<ServiceRate> result = new ArrayList<>();
		if(srs!=null || srs.size()>0){
			ServiceRate dJWrate = new ServiceRate();//贷记卡工作日费率
			ServiceRate dJHrate = new ServiceRate();//贷记卡节假日费率
			ServiceRate jJWrate = new ServiceRate();//借记卡工作日费率
			ServiceRate jJHrate = new ServiceRate();//借记卡节假日费率
			for (ServiceRate sr : srs) {
				if(sr.getCardType()==null || sr.getRateType()==null){
					continue;
				}
				if(rateCardFlag){
					if(rateHolidaysFlag){
						if(sr.getCardType().equals("1")){
							if(sr.getHolidaysMark().equals("1")){
								dJWrate = sr;
							}else if(sr.getHolidaysMark().equals("2")){
								dJHrate = sr;
							}
						}else if(sr.getCardType().equals("2")){
							if(sr.getHolidaysMark().equals("1")){
								jJWrate = sr;
							}else if(sr.getHolidaysMark().equals("2")){
								jJHrate = sr;
							}
						}
					}else{
						if(sr.getCardType().equals("1") && sr.getHolidaysMark().equals("0")){
							dJWrate = sr;
							dJHrate = sr;
						}else if(sr.getCardType().equals("2") && sr.getHolidaysMark().equals("0")){
							jJWrate = sr;
							jJHrate = sr;
						}
					}
				}else{
					if(rateHolidaysFlag){
						if(sr.getCardType().equals("0") && sr.getHolidaysMark().equals("1")){
							dJWrate = sr;
							jJWrate = sr;
						}else if(sr.getCardType().equals("0") && sr.getHolidaysMark().equals("2")){
							dJHrate = sr;
							jJHrate = sr;
						}
					}else{
						if(sr.getCardType().equals("0") && sr.getHolidaysMark().equals("0")){
							dJWrate = sr;
							dJHrate = sr;
							jJWrate = sr;
							jJHrate = sr;
						}
					}
				}
			}
			result.add(dJWrate);
			result.add(dJHrate);
			result.add(jJWrate);
			result.add(jJHrate);
		}

		return result;
	}


	private String switchGuDing(Integer val){
		if(val==null){
			return null;
		}
		String res = null;
		switch (val){
			case 1:
				res = "固定";
				break;
			case 0:
				res = "不固定";
				break;
			default:res = null;
				break;
		}
		return res;
	}

	private String switchYN(Integer val){
		if(val==null){
			return null;
		}
		String res = null;
		switch (val){
			case 1:
				res = "是";
			break;
			case 0:
				res = "否";
			break;
			default:res = null;
			break;
		}
		return res;
	}

	@Override
	public ServiceRate getRate(String cardType,Long serviceId,String holidayMark){

		ServiceRate sr = new ServiceRate();
		try {
			sr.setCardType(cardType);
			sr.setServiceId(serviceId);
			sr.setHolidaysMark(holidayMark);
			return serviceDao.getServiceRateByRate(sr);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询服务费率出错",e);
		}
		return null;
	}

	private ServiceQuota getQuota(String quotaType,Long serviceId,String holidayMark){
		ServiceQuota result  = null;
		try {
			ServiceQuota sqs = new ServiceQuota();
			sqs.setServiceId(serviceId);
			sqs.setAgentNo("0");
			sqs.setHolidaysMark(holidayMark);
			sqs.setCardType(quotaType);
			return serviceDao.queryServiceQuota(sqs);
		} catch (Exception e) {
			log.error("查询服务额度出错",e);
		}
		return result;
	}

	@Override
	public String getServiceTypeName(Integer type) {
		if (type==null) {
			return null;
		}

		List<Map<String, String>> serviceType = null;
		try {
			serviceType = sysDictDao.getListByKey("SERVICE_TYPE");
			if (serviceType != null && serviceType.size() > 0) {
				for (Map<String, String> map : serviceType) {
					if (map.get("sys_value") != null) {
						if (type == Integer.valueOf(map.get("sys_value"))) {
							return map.get("sys_name") == null ? "" : map.get("sys_name");
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("查询服务类型错误",e);
		}

		return null;
	}


	@Override
	public List<ServiceInfo> getServiceInfo(Map<String,Object> info,Page<ServiceInfo> page){
		List<ServiceInfo> result = serviceDao.getServiceInfo(info,page);
		for (ServiceInfo serviceInfo : result) {
			//费率是否区分银行卡种类:1-是，0-否  节假日标志:1-只工作日，2-只节假日，0-不限
			if(serviceInfo.getRateCard()!=null){

				boolean rateCardFlag = 1==serviceInfo.getRateCard();//费率是否区分银行卡种类
				boolean rateHolidaysFlag = serviceInfo.getRateHolidays()==null?false:1==serviceInfo.getRateHolidays();//费率是否区分节假日
				ServiceRate dJWrate = null;//贷记卡工作日
				ServiceRate jJWrate = null;//借记卡工作日

				if(rateCardFlag){
					if(rateHolidaysFlag){
						dJWrate = getRate("1", serviceInfo.getServiceId(), "1");
						jJWrate = getRate("2", serviceInfo.getServiceId(), "1");
					}else{
						dJWrate = getRate("1", serviceInfo.getServiceId(), "0");
						jJWrate = getRate("2", serviceInfo.getServiceId(), "0");
					}
				}else{
					if(rateHolidaysFlag){
						ServiceRate wRate = getRate("0", serviceInfo.getServiceId(), "1");
						dJWrate = wRate;
						jJWrate = wRate;

					}else{
						ServiceRate rate = getRate("0", serviceInfo.getServiceId(), "0");
						dJWrate = rate;
						jJWrate = rate;
					}
				}
				if(jJWrate!=null){
					jJWrate.setMerRate(profitExpression(jJWrate));
					serviceInfo.setjRate(jJWrate.getMerRate());
				}
				if(dJWrate!=null){
					dJWrate.setMerRate(profitExpression(dJWrate));
					serviceInfo.setdRate(dJWrate.getMerRate());
				}
			}
		}
		return result;
	}
	/*
	 * 查询服务信息，费率和控管信息
	 */
	@Override
	public List<ServiceInfo> getServiceInfoWithDetail(ServiceInfo info,Page<ServiceInfo> page){
		
		return null;
	}
	@Override
	public ServiceInfo queryServiceDetail(ServiceInfo info) {
		ServiceInfo serviceInfo=serviceDao.queryServiceInfo(info.getServiceId());
		if(serviceInfo!=null){
			List<ServiceRate> rates=serviceDao.getServiceRate(serviceInfo.getServiceId(),"0");
			for(ServiceRate r:rates){
				r.setMerRate(profitExpression(r));
			}
			List<ServiceQuota> quotas=serviceDao.getServiceQuota(serviceInfo.getServiceId(),"0");
			serviceInfo.setRates(rates);
			serviceInfo.setQuotas(quotas);
			//获取关联的服务
			if(serviceInfo.getLinkService()!=null && serviceInfo.getLinkService()!=0){
				ServiceInfo linkService = serviceDao.findServiceName(serviceInfo.getLinkService().toString());
				if(linkService!=null){
					serviceInfo.setLinkServiceName(linkService.getServiceName());
				}
			}
			serviceInfo.setUsedStatus(0);
			//当代理商或者商户已含有时，基本信息和服务管控费率方式不能修改，但是值可以改
			//1.根据服务Id去找所有包含此服务的业务产品的bpId
			List<Long> bpIds = businessProductInfoDao.findByService(serviceInfo.getServiceId()); 
			//2.根据找到的业务产品的bpId去判断是否有代理商已经使用，如果有，设置服务已被使用
			if(bpIds!=null&& bpIds.size()>0){
				int num = agentBusinessProductDao.findIdByBpIds(bpIds);
				if(num>0){
					serviceInfo.setUsedStatus(1);
				}
			}
		}
		return serviceInfo;
	}
	
	/**
	 * 修改服务费率 
	 */
	@Override
	public int updateServiceStatus(String id, String status) {
		return serviceDao.updateServiceStatus(id,status);
	}
	
	/**
	 * @param rate 表达式中的设置成相应的属性
	 * @param isChange： true-允许类型可以变，false-不允许类型改变
	 */
	@Override
	public void setServiceRate(ServiceRate rate,boolean isChange){
		String type=rate.getRateType();
		if(rate.getMerRate().indexOf("<")!=-1){
			if(!rate.getMerRate().matches("^(\\d+(\\.\\d+)?)%(<(\\d+(\\.\\d+)?)<(\\d+(\\.\\d+)?)%)+"))
				throw new RuntimeException("服务费率设置错误！");
			String[] strs=rate.getMerRate().split("<");
			rate.setLadder1Rate(new BigDecimal(strs[0].substring(0, strs[0].lastIndexOf("%"))));
			rate.setLadder1Max(new BigDecimal(strs[1]));
			rate.setLadder2Rate(new BigDecimal(strs[2].substring(0, strs[2].lastIndexOf("%"))));
			if(strs.length>3){
				rate.setLadder2Max(new BigDecimal(strs[3]));
				rate.setLadder3Rate(new BigDecimal(strs[4].substring(0, strs[4].lastIndexOf("%"))));
				if(rate.getLadder2Max().compareTo(rate.getLadder1Max())<=0)
					throw new RuntimeException("服务费率设置错误！");
			}
			if(strs.length>5){
				rate.setLadder3Max(new BigDecimal(strs[5]));
				rate.setLadder4Rate(new BigDecimal(strs[6].substring(0, strs[6].lastIndexOf("%"))));
				if(rate.getLadder3Max().compareTo(rate.getLadder2Max())<=0)
					throw new RuntimeException("服务费率设置错误！");
			}
			if(strs.length>7){
				rate.setLadder4Max(new BigDecimal(strs[7]));
				if(rate.getLadder4Max().compareTo(rate.getLadder3Max())<=0)
					throw new RuntimeException("服务费率设置错误！");
			}
			rate.setRateType("5");
		}else if(rate.getMerRate().indexOf("+")!=-1){
			if(!rate.getMerRate().matches("^(\\d+(\\.\\d+)?)%\\+(\\d+(\\.\\d+)?)"))
				throw new RuntimeException("服务费率设置错误！");
			String[] temp=rate.getMerRate().split("%\\+");
			rate.setRate(new BigDecimal(temp[0]));
			rate.setSingleNumAmount(new BigDecimal(temp[1]));
			rate.setRateType("4");
		}else if(rate.getMerRate().indexOf("~")!=-1){
			String[] temp=rate.getMerRate().split("~");
			if(temp.length==3){
				if(!rate.getMerRate().matches("^(\\d+(\\.\\d+)?)~(\\d+(\\.\\d+)?)%~(\\d+(\\.\\d+)?)"))
					throw new RuntimeException("服务费率设置错误！");
				Matcher m = pattern.matcher(rate.getMerRate());
				while(m.find()){
					rate.setSafeLine(new BigDecimal(m.group(1)));
					rate.setRate(new BigDecimal(m.group(3)));
					rate.setCapping(new BigDecimal(m.group(5)));
				}
				if(rate.getSafeLine()==null||rate.getCapping().compareTo(rate.getSafeLine())<0)
					throw new RuntimeException("服务费率设置错误！");
				rate.setRateType("3");
			}else if(temp.length==2){//新增封顶规则6
				if(!rate.getMerRate().matches("^(\\d+(\\.\\d+)?)%~(\\d+(\\.\\d+)?)"))
					throw new RuntimeException("服务费率设置错误！");
				String[] str=rate.getMerRate().split("%~");
				rate.setRate(new BigDecimal(str[0]));
				rate.setCapping(new BigDecimal(str[1]));
				rate.setRateType("6");
			}else{
				throw new RuntimeException("服务费率设置错误！");
			}
		}else if(rate.getMerRate().indexOf("%")!=-1){
			String str_=rate.getMerRate().substring(0, rate.getMerRate().indexOf("%"));
			if(str_.matches("\\d+(\\.\\d+)?")){
				rate.setRate(new BigDecimal(str_));
			}else{
				throw new RuntimeException("服务费率设置错误！");
			}
			rate.setRateType("2");
		}else {
			if(rate.getMerRate().matches("\\d+(\\.\\d+)?")){
				rate.setSingleNumAmount(new BigDecimal(rate.getMerRate()));
			}else{
				throw new RuntimeException("服务费率设置错误！");
			}
			rate.setRateType("1");
		}
		if(!isChange&&!rate.getRateType().equals(type)){
			throw new RuntimeException("服务费率类型的格式不正确！");
		}
	}
	
	@Override
	public String profitExpression(ServiceRate rule){
		if(rule==null||StringUtils.isEmpty(rule.getRateType())) return "";
		String profitExp=null;
		switch(rule.getRateType()){
			case "1": 
				profitExp=rule.getSingleNumAmount()==null?"":format.format(rule.getSingleNumAmount());
				break;
			case "2":
				profitExp=rule.getRate()==null?"":format.format(rule.getRate())+"%";
				break;
			case "3":
				profitExp=format.format(rule.getSafeLine())+"~"+format.format(rule.getRate())+"%~"+format.format(rule.getCapping());
				break;
			case "4":
				profitExp=format.format(rule.getRate())+"%+"+format.format(rule.getSingleNumAmount());	
				break;
			case "5":
				StringBuffer sb=new StringBuffer();
				sb.append(format.format(rule.getLadder1Rate())).append("%").append("<").append(format.format(rule.getLadder1Max()))
				  .append("<").append(format.format(rule.getLadder2Rate())).append("%");
				if(rule.getLadder2Max()!=null){
					sb.append("<").append(format.format(rule.getLadder2Max()))
					  .append("<").append(format.format(rule.getLadder3Rate())).append("%");
					if(rule.getLadder3Max()!=null){
						sb.append("<").append(format.format(rule.getLadder3Max()))
						  .append("<").append(format.format(rule.getLadder4Rate())).append("%");
						if(rule.getLadder4Max()!=null){
							sb.append("<").append(format.format(rule.getLadder4Max()));
						}
					}
				}
				profitExp=sb.toString();
				break;
			case "6":
				profitExp=format.format(rule.getRate())+"%~"+format.format(rule.getCapping());
			default : ;	
		}
		return profitExp;
	}
	@Override
	public ServiceRate queryServiceRate(ServiceRate sr) {
		return serviceDao.queryServiceRate(sr);
	}
	@Override
	public ServiceInfo queryServiceInfo(Long serviceId) {
		return serviceDao.queryServiceInfo(serviceId);
	}
	@Override
	public ServiceQuota queryServiceQuota(ServiceQuota sq) {
		return serviceDao.queryServiceQuota(sq);
	}
	@Override
	public List<ServiceInfo> selectServiceInfo() {
		return serviceDao.selectServiceInfo();
	}
	@Override
	public List<ServiceRate> getServiceAllRate(Long serviceId,String agentId) {
		return serviceDao.getServiceAllRate(serviceId,agentId);
	}
	@Override
	public List<ServiceQuota> getServiceAllQuota(Long serviceId,String agentId) {
		return serviceDao.getServiceAllQuota(serviceId,agentId);
	}
	@Override
	public List<ServiceInfo> getLinkServices() {
		return serviceDao.getLinkServices();
	}


	//=========sober===================================
	@Override
	public List<AgentShareRule> queryAgentProfit(ServiceInfo info) {
		List<AgentShareRule> list = serviceDao.queryByParams(info.getServiceId());
		if (list.size()==0) {
			list=serviceDao.queryAgentProfit(info.getServiceId());
		}
		for (AgentShareRule rule : list) {
			agentInfoService.profitExpression(rule);
//			StringBuffer sb=new StringBuffer();
//			if (rule.getLadder1Rate()!=null||rule.getLadder1Max()!=null||rule.getLadder1Max()!=null||rule.getLadder2Rate()!=null||
//					rule.getLadder2Max()!=null||rule.getLadder3Rate()!=null||rule.getLadder3Max()!=null||rule.getLadder4Rate()!=null) {
//				sb.append(rule.getLadder1Rate()).append("%<").append(rule.getLadder1Max())
//				  .append("<").append(rule.getLadder2Rate()).append("%<").append(rule.getLadder2Max())
//				  .append("<").append(rule.getLadder3Rate()).append("%<").append(rule.getLadder3Max())
//				  .append("<").append(rule.getLadder4Rate()).append("%");
//				rule.setLadderRate(sb.toString());	
//			}
		}
		return list;
	}

	@Override
	public int saveAgentProfit(JSONObject json) {
		List<AgentShareRule> list=JSONArray.parseArray(json.getString("serviceInfo"), AgentShareRule.class);
		int i = 0;
		Long serviceId = Long.valueOf(list.get(0).getServiceId());
		if (serviceDao.queryByAgentNo(serviceId).size()<=0) {
			System.out.println("进入新增..............");
			for(AgentShareRule rule:list){
				System.out.println("income: "+rule.getIncome());
				agentInfoService.setShareRule(rule);
				rule.setAgentNo("0");
				rule.setCheckStatus(0);
				rule.setLockStatus(0);
			}
			i = agentInfoDao.insertAgentShareList(list);
		}else {
			System.out.println("进入更新..............");
			List<AgentShareRule> listAgentShareRule = new ArrayList<>();
			for (AgentShareRule share : list) {
				AgentShareRule agentShareRule=new AgentShareRule();
				agentShareRule.setId(share.getId());
				agentShareRule.setProfitType(share.getProfitType());
				agentShareRule.setIncome(share.getIncome());
				agentShareRule.setLadderRate(share.getLadderRate());
				agentShareRule.setShareProfitPercent(share.getShareProfitPercent());
				if (share.getCost()!=null) {
					agentShareRule.setCost(share.getCost());
				}
				agentInfoService.setShareRule(agentShareRule);
				listAgentShareRule.add(agentShareRule);
			}
			System.out.println(listAgentShareRule.size()+"条数据");
			i = agentInfoDao.updateAgentShareList(listAgentShareRule);
		}
		
		return i;
	}

	@Override
	public Result updateEffectiveStatus(ServiceInfo baseInfo) {
		Result result = new Result();
		if(baseInfo == null || baseInfo.getServiceId() == null
				|| baseInfo.getEffectiveStatus() == null){
			result.setMsg("参数非法");
			return result;
		}
		if(baseInfo.getEffectiveStatus() == 1){
			result.setMsg("失效的服务不能再生效");
			return result;
		}
		//如果所属的业务产品，是队长，其队员下面如果有生效的同类型服务，则不能失效
        if (existsMemberService(baseInfo, result)) return result;
        int num = serviceDao.updateEffectiveStatus(baseInfo);
        if(num == 1){
            result.setStatus(true);
            result.setMsg("操作成功");
        } else {
            result.setMsg("操作失败");
        }
		return result;
	}

	@Override
	public List<ServiceInfo> selectServiceName() {
		return serviceDao.selectServiceName();
	}

	@Override
	public Map<Long, String> selectServiceNameMap() {
		List<ServiceInfo> list = selectServiceName();
		if(list == null || list.isEmpty()){
			return null;
		}
		Map<Long, String> map = new HashMap<>();
		for(ServiceInfo item: list){
			map.put(item.getServiceId(), item.getServiceName());
		}
		return map;
	}

	private boolean existsMemberService(ServiceInfo baseInfo, Result result) {
        //根据服务找到业务产品
        BusinessProductDefine product = serviceDao.getProductByService(baseInfo.getServiceId());
        if(product != null && product.getAllowIndividualApply() == 1){
            ServiceInfo serviceInfo = serviceDao.findServiceName(String.valueOf(baseInfo.getServiceId()));
            //如果业务产品有群组，且为队长，其队员下面如果有生效的同类型服务，则不能失效
            List<ServiceInfo> serviceList = serviceDao.getMemberServiceByProduct(product.getBpId(), serviceInfo.getServiceType());
            if(serviceList != null && serviceList.size() >0){
                StringBuilder serviceName = new StringBuilder();
                for(ServiceInfo item: serviceList){
                    serviceName.append(item.getServiceName()).append(",");
                }
                result.setMsg("有队员服务在使用中:" + serviceName.substring(0, serviceName.length() - 1) + "。");
                return true;
            }
        }
        return false;
    }

}
