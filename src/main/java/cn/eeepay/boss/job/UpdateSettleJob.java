package cn.eeepay.boss.job;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.eeepay.framework.service.impl.RegularTasks;

@DisallowConcurrentExecution
public class UpdateSettleJob implements Job{
	
	private final Logger log = LoggerFactory.getLogger(UpdateSettleJob.class);
	@Resource
	private RegularTasks regularTasks;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		log.info("定时更新交易的结算状态,执行时间:" + new Date());
		regularTasks.updateSettleStatus();
	}

}
