package cn.eeepay.framework.service.impl;

import cn.eeepay.boss.job.*;
import cn.eeepay.boss.quartz.AutoJobDTO;
import cn.eeepay.boss.quartz.QuartzManager;
import cn.eeepay.boss.quartz.QuartzSimpleManager;
import cn.eeepay.framework.model.AcqOrg;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.AcqOrgService;
import cn.eeepay.framework.service.QuartzAddJodService;
import cn.eeepay.framework.service.SysDictService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/3/8/008.
 * @author  liuks
 * 添加定时任务Job 到容器中
 */
@Service("quartzAddJodService")
public class QuartzAddJodServiceImpl  implements QuartzAddJodService {

    private final Logger log = LoggerFactory.getLogger(QuartzAddJodServiceImpl.class);

    @Resource
    private QuartzManager quartzManager;
    @Resource
    private QuartzSimpleManager quartzSimpleManager;
    @Resource
    private AcqOrgService acqOrgService;
    @Resource
    private SysDictService sysDictService;
    @Resource
    private Scheduler scheduler ;

    /**
     *通用添加JOB,支持定时任务监控
     * jobName 未数据字典key值，存定时的执行表达式 * * 10 * * ?
     * 只支持表达式
     * 建议 jobName 与 jobGroup 配置一样
     * state 1 表示添加 2，只涮新
     */
    @Override
    public void addPublicJob(String jobName, String jobGroup,Class<?> jobClass,int state) {
        SysDict sysDict = sysDictService.getByKey(jobName);
        if(sysDict!=null){
            AutoJobDTO job = new AutoJobDTO();
            job.setJob_id(jobName);
            job.setJob_name(jobGroup);
            job.setJob_group(jobGroup);
            String time = sysDict.getSysValue();//单位为：分钟
            if(time!=null&&!"".equals(time)){
                if("0".equals(time)){//配置未0时清除定时
                    quartzManager.removeJob(job);
                }else{
                    String[] strs=time.split(" ");
                    if(strs!=null&&strs.length==6){
                        //是表达式
                        job.setJob_time(time);
                        try {
                            if(state==1){
                                quartzManager.addJob(job, jobClass);
                                log.info("定时任务"+jobName+" "+jobGroup+"添加定时任务成功!");
                            }else if(state==2){
                                quartzManager.rinseTheNew(job);
                                log.info("定时任务"+jobName+" "+jobGroup+"涮新定时任务成功!");
                            }
                        } catch (SchedulerException e) {
                            e.printStackTrace();
                            log.info("定时任务"+jobName+" "+jobGroup+"添加异常!");
                        }
                    }else{
                        log.info("定时任务"+jobName+" "+jobGroup+"配置的表达式格式错误!");
                    }
                }
            }else{
                log.info("定时任务"+jobName+" "+jobGroup+"未配置执行表达式!");
            }
        }else{
            log.info("定时任务"+jobName+" "+jobGroup+"未配置执行表达式!");
        }
    }


    /**
     * 遍历收单机构表，启动所有收单机构的定时任务
     * @throws SchedulerException
     */
    @Override
    public void addAcqorgJob() throws SchedulerException {
        List<AcqOrg> acqList = acqOrgService.selectBoxAllInfo();
        if (acqList != null && !acqList.isEmpty()) {
            for (AcqOrg acq : acqList) {
                //启动定时任务
                AutoJobDTO job = new AutoJobDTO();
                job.setJob_id(acq.getId().toString());
                //会根据name和group来查询是否存在该条任务，因此name设置为id唯一性
                job.setJob_name(acq.getId().toString());
                job.setJob_group("ACQ_ORG");
                String time = acq.getDayAlteredTime();  //time eg:   00:01
                if (StringUtils.isNotBlank(time)) {
                    String[] timeStr = time.split(":");
                    String conStr = timeStr[2]+" "+timeStr[1]+" "+timeStr[0]+" * * ?";
                    job.setJob_time(conStr);
                    quartzManager.addJob(job, OutAccountServiceJob.class);
                    log.info("-----------定时任务："+acq.getAcqEnname()+" 启动------------");
                }
            }
        }
    }


}
