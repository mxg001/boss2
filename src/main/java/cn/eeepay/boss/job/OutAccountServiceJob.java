package cn.eeepay.boss.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.eeepay.boss.quartz.AutoJobDTO;
import cn.eeepay.framework.service.OutAccountServiceService;

@DisallowConcurrentExecution
public class OutAccountServiceJob implements Job{

	private final Logger log = LoggerFactory.getLogger(OutAccountServiceJob.class);
	@Autowired
	private OutAccountServiceService outAccountServiceService;

	@Override
	public void execute(JobExecutionContext paramJobExecutionContext) throws JobExecutionException {
		AutoJobDTO scheduleJob = (AutoJobDTO) paramJobExecutionContext.getMergedJobDataMap().get("scheduleJob");
		Integer acqOrgId = Integer.parseInt(scheduleJob.getJob_id());
		int i=outAccountServiceService.updateResetDayTotalAmount(acqOrgId);
		if(i>0){
			log.info(scheduleJob.getJob_name()+"--出款服务,已重置出款额度");
		}else{
			log.info(scheduleJob.getJob_name()+"--出款服务,重置出额度已执行,未更新!");
		}
	}

}
