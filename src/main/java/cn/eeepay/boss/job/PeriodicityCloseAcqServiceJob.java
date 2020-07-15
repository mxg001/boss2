package cn.eeepay.boss.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.eeepay.framework.service.AcqServiceProService;

/**
 * 周期性关闭收单相关服务作业
 * 
 * @author Qiujian
 *
 */
@DisallowConcurrentExecution
public class PeriodicityCloseAcqServiceJob implements Job {

	@Autowired
	private AcqServiceProService service;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		service.periodicityClose();
	}

}
