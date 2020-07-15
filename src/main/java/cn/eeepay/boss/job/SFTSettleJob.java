package cn.eeepay.boss.job;
/**
 * 盛付通直清定时对上游不接收出款的时间段内的出款请求进行出款
 * @author lix
 */
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.eeepay.framework.dao.TransInfoDao;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtil;

@DisallowConcurrentExecution
public class SFTSettleJob implements Job {

	private final Logger log = LoggerFactory.getLogger(SFTSettleJob.class);
	@Resource
	private TransInfoDao transInfoDao;
	private String starttime = " 22:00:00",endtime = " 09:00:00";
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Date d = new Date();
		log.info("定时发起盛付通结算任务,执行时间:" + d);
		starttime = DateUtil.getBeforeDateString(d)+starttime;
		endtime = DateUtil.getCurrentDate()+endtime;
		List<Map<String, Object>> settleList = transInfoDao.querySFTSettle("SFT_ZQ", starttime, endtime);
		if(settleList != null && settleList.size() >0){
			for(int i = 0; i < settleList.size(); i ++){
				String url=Constants.SETTLE_TRANS+"?transferId="+settleList.get(i).get("id")+"&userId=SFT_task";
				log.info("结算请求：" + url);
				String result=ClientInterface.baseNoClient(url,null);
				log.info("结算返回信息：" + result);
			}
		}
	}

}
