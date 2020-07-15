package cn.eeepay.boss.listener;

import cn.eeepay.boss.job.TimedTaskJob;
import cn.eeepay.boss.quartz.QuartzManager;
import cn.eeepay.framework.service.*;
import org.apache.commons.logging.LogFactory;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Resource;

/**
 * spring容器加载完后启动的监听器
 * 目的： 服务器重新启动，需要将出款服务定时任务重新启动
 * @author Administrator
 *
 */
public class SpringLoadedListener  implements ApplicationListener<ContextRefreshedEvent>{
	@Autowired
	private QuartzManager quartzManager;

	@Resource
	private TimedTaskService timedTaskService;

	@Resource
	private QuartzAddJodService quartzAddJodService;

	private final Logger log = LoggerFactory.getLogger(SpringLoadedListener.class);

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//防止重复执行。
		try {
			//容器启动时，先初始化线程状态为未启动
			timedTaskService.clearTimedTaskState();

			//遍历收单机构表，启动所有收单机构的定时任务
			quartzAddJodService.addAcqorgJob();

			//定时任务监控 添加
			quartzAddJodService.addPublicJob("timedTaskMonitor","timedTaskMonitor",TimedTaskJob.class,1);

			quartzManager.startJobs();
		} catch (Exception e) {
			e.printStackTrace();
			LogFactory.getLog(SpringLoadedListener.class).error("----spring容器初始化出款服务定时任务出现异常--------");
			log.error("----spring容器初始化出款服务定时任务出现异常--------");
		}
	}

}
