package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.*;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("regularTasks")
@Transactional
public class RegularTasks {

	@Resource
	private RegularTasksDao regularTasksDao;

	@Resource
	private TransInfoDao transInfoDao;

	@Resource
	private AcqMerchantDao acqMerchantDao;

	@Resource
	private UserCouponDao userCouponDao;
	@Resource
	private SysDictDao sysDictDao;
	
	@Autowired
	private OutAccountServiceService outAccountServiceService;
	@Autowired
	private SysDictServiceImpl sysDictService;
	@Autowired
	private AgentShareTaskService agentShareTaskService;
	@Autowired
	private MerchantMigrateService merchantMigrateService;
	@Autowired
	private TransInfoService transInfoService;

	//车管家订单更新服务类
	@Resource
	private UpdateCheGuanHomeOrderService updateCheGuanHomeOrderService;

	private static final Logger logger = LoggerFactory.getLogger(RegularTasks.class);

	/**
	 * 替换收单服务费率
	 * 
	 * @return
	 */
	//@Scheduled(cron = "0 0 0 * * ?")
	public void updateAcqServiceRate() {
		System.out.println("测试收单定时任务" + new Date());
		// 1.根据日期获取任务中对应的收单服务费率acqServiceRateTask
		// 2.根据acqServiceRateTask中的收单服务费率ID，查出真实的收单服务费率acqServiceRate
		// 3.为acqServiceRateTask设置收单服务ID，acqServiceRate.getAcqServiceId()
		// 4.根据收单服务费率ID将acqServiceRateTask更新到acq_service_rate，
		// 5.删除acq_service_rate_task生效的那条，并将acqServiceRate插入acq_service_rate_task
		// Date date = new Date();
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// String dateStr = sdf.format(date);
		List<AcqServiceRateTask> acqServiceRateTasks = regularTasksDao.selectByEffectiveDate();
		if (acqServiceRateTasks == null || acqServiceRateTasks.size() == 0) {
			return;
		}
		List<Integer> sbAsrtIds = new ArrayList<>();
		List<Integer> sbIds = new ArrayList<>();
		for (int i = 0; i < acqServiceRateTasks.size(); i++) {
			sbAsrtIds.add(acqServiceRateTasks.get(i).getAcqServiceRateId());
			sbIds.add(acqServiceRateTasks.get(i).getId());
		}
		List<AcqServiceRate> acqServiceRates = regularTasksDao.selectAsrInfoByIds(sbAsrtIds);

		// 现在两个集合都查出来了，需要交换数据
		if (acqServiceRates == null || acqServiceRates.size() == 0) {
			return;
		}

		regularTasksDao.updateBatchAcqServiceRateByTask(acqServiceRateTasks);
		regularTasksDao.insertBatchAcqServiceRateTaskByRate(acqServiceRates);
		regularTasksDao.deleteBatchAcqServiceRateTaskByIds(sbIds);
		System.out.println("定时收单服务费率");
	}

	/**
	 * 分润任务：从备份分润任务表，将生效的分润任务更新到正在执行的分润表
	 */
//	@Scheduled(cron = "0 0 0 * * ?")
	public void updateAgentShareTask() {
		List<AgentShareRuleTask> list = agentShareTaskService.findByEffective();
		if (list == null || list.size() == 0) {
			return;
		}
		AgentShareRule temp = null;
		List<AgentShareRule> ruleList = new ArrayList<AgentShareRule>();
		List<Integer> taskIdList = new ArrayList<Integer>();
		List<AgentShareRuleTask> tempList = new ArrayList<AgentShareRuleTask>();
		// 放到正在执行的分润表
		// 批量一次300个
		for (int i = 1; i < list.size() + 1; i++) {
			temp = agentShareTaskService.getById(list.get(i - 1).getShareId());
			if (list.get(i - 1).getEfficientDate().getTime() > temp.getEfficientDate().getTime()) {
				ruleList.add(temp);
				taskIdList.add(list.get(i - 1).getId());
				tempList.add(list.get(i - 1));
			}
			if (i % 300 == 0) {
				agentShareTaskService.updateByTaskBatch(tempList);
				agentShareTaskService.updateByRuleBatch(ruleList, taskIdList);
				ruleList.clear();
				taskIdList.clear();
				tempList.clear();
			}
		}
		agentShareTaskService.updateByTaskBatch(tempList);
		agentShareTaskService.updateByRuleBatch(ruleList, taskIdList);
		// for (AgentShareRuleTask ruleTask : list) {
		// //查询原rule
		// temp = agentShareTaskService.getById(ruleTask.getShareId());
		// if(ruleTask.getEfficientDate().getTime()>temp.getEfficientDate().getTime()){
		// ruleList.add(temp);
		// taskIdList.add(ruleTask.getId());
		// }
		// //一条条更新
		// //agentShareTaskService.updateByTask(ruleTask);
		// //更新任务表
		// //agentShareTaskService.updateByRule(temp, ruleTask.getId());
		// }
		// agentShareTaskService.updateByTaskBatch(list);
		// agentShareTaskService.updateByRuleBatch(ruleList, taskIdList);
		logger.debug("代理商分润任务: " + new Date());
	}

	//@Scheduled(cron = "0 0 0 * * ?")
	public void updateOutAccountServiceTask() {
		List<OutAccountServiceRateTask> list = outAccountServiceService.findByEffective();
		if (list == null || list.size() == 0) {
			return;
		}
		OutAccountServiceRate temp = null;

		List<OutAccountServiceRate> rateList = new ArrayList<OutAccountServiceRate>();
		List<Integer> taskIdList = new ArrayList<Integer>();
		// 放到正在执行的分润表
		for (OutAccountServiceRateTask rateTask : list) {
			// 查询原rule
			temp = outAccountServiceService.getById(rateTask.getOutAccountServiceRateId());
			if (rateTask.getEffectiveDate().getTime() > temp.getEffectiveDate().getTime()) {
				rateList.add(temp);
				taskIdList.add(rateTask.getId());
			}
			rateList.add(temp);
			taskIdList.add(rateTask.getId());
		}
		outAccountServiceService.updateByTaskBatch(list);
		outAccountServiceService.updateByRateBatch(rateList, taskIdList);
		System.out.println("定时出款");
	}

	public int updateSettleStatus() {
		int res1 =0;//T0的单转T1结算
		int res2 =0;//提现失败的单转T1
		// 如果不是节假日执行
		if (transInfoDao.getHolidayFlag(new SimpleDateFormat("yyyy-MM-dd").format(new Date())) == null) {
			// 获取更新参数
			String channels = sysDictService.getValues("NO_UPDATE_SETTLE_STATUS");
			String maxdate = sysDictDao.getValueByKey("NO_UPDATE_SETTLE_STATUS_DATA");
			String currentDate = DateUtil.getCurrentDate();
			maxdate = DateUtil.getBeforeDate(new Date(), Integer.parseInt(maxdate));
			logger.info("更改交易结算状态，订单时间{}~{}",maxdate, currentDate);
			// T0的单转T1结算
			res1=transInfoDao.updateT0SettleStatus(channels,maxdate,currentDate);
			// 提现失败的单转T1
			res2=transInfoDao.updateT1SettleStatus(channels,maxdate,currentDate);
		}else{
			String transchannels = sysDictService.getValues("HOLIDAYS_UPDATE_SETTLE_STATUS");
			if(transchannels.length() > 0){
				String channels = sysDictService.getValues("NO_UPDATE_SETTLE_STATUS");
				String maxdate = sysDictDao.getValueByKey("NO_UPDATE_SETTLE_STATUS_DATA");
				String currentDate = DateUtil.getCurrentDate();
				maxdate = DateUtil.getBeforeDate(new Date(), Integer.parseInt(maxdate));
				logger.info("更改交易结算状态，订单时间{}~{}",maxdate, currentDate);
				// T0的单转T1结算
				res1=transInfoDao.updateT0SettleStatus2(channels,maxdate,transchannels,currentDate);
				// 提现失败的单转T1
				res2=transInfoDao.updateT1SettleStatus2(channels,maxdate,transchannels,currentDate);
			}
		}
		logger.info("更新交易的结算状态,T0转T1条数:{}，提现失败的单转T1:{}", res1, res2);
		return res1 + res2;
	}

	//@Scheduled(cron = "0 0 0 * * ?")
	public void updateBatchAcqMerchantQuota() {
		acqMerchantDao.updateBatchAcqMerchantQuota();
	}

	//@Scheduled(cron = "0 0 0 1 * ?") // 每个月执行一次
	public void merchantMigrate() {
		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
			System.out.println(sdf.format(date));
			System.out.println("=======================================");
			List<String> merchantNos = new ArrayList<>();
			merchantMigrateService.migrate(merchantNos);
			//神策传送
            merchantMigrateService.scBymerchantNos(merchantNos);
		} catch (Exception e) {
			logger.error("商户迁移转移一级代理商失败", e);
		}
	}

	/**
	 * 更新优惠券状态
	 */
	public void updateCouponStatus() {
		userCouponDao.updateCouponStatus();
	}

	/**
	 * 查询统计今天过期的充值返券
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryExpCoupon() {
		return userCouponDao.queryExpCoupon();
	}

	public int batchInsertexpCoupon(List<UserCoupon> expCoupon) {
		return userCouponDao.batchInsertexpCoupon(expCoupon);
	}

	/**
	 * 查询今天过期的充值返券
	 * 
	 * @return
	 */
	public List<UserCoupon> queryExpCoupons() {
		return userCouponDao.queryExpCoupons();
	}

	/**
	 * 机具申请,直属代理商超过三天没处理,转为一级代理商处理;每天凌晨一点执行
	 * @author	mays
	 * @date	2017年8月10日 下午5:13:51
	 */
	/*@Scheduled(cron = "0 0 1 * * ?")
	public void terminalApplyTimeOutProcess() {
		int num = regularTasksDao.updateTerminalApplyStatus();
		logger.info("将直属代理商超过三天没处理的机具申请转为一级代理商处理,本次处理 " + num + " 条");
	}*/

	/**
	 * 车管家前一天的已完成的订单修改成为5状态
	 * collective_trans_order
	 * @author liuks
	 * @date 2017年11月14日
	 * 每天凌晨两点执行
	 */
	//@Scheduled(cron ="0 15 0 * * ?")
	public void updateCheGuanHomeOrder() {
		try {
			logger.info("修改车管家订单结算状态后台线程启动:----------->");
			int count=updateCheGuanHomeOrderService.updateCheGuanHomeOrder();
			if(count>0) {
				logger.info("修改车管家订单结算状态后台线程:本次共更新了" + count + "条记录!");
			}
			logger.info("修改车管家订单结算状态后台线程结束!");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("修改车管家订单结算状态后台线程:"+e.getMessage());
		}
	}

	/**
	 * 2小时同步一次交易状态
	 * @author	mays
	 * @date	2017年12月11日
	 */
	//@Scheduled(cron = "0 0 1/2 * * ?")
	public void syncTransStatusTask(){
		transInfoService.syncTransStatus();
	}

}