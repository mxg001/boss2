package cn.eeepay.framework.service.impl;

import cn.eeepay.boss.job.OutAccountServiceJob;
import cn.eeepay.boss.quartz.AutoJobDTO;
import cn.eeepay.boss.quartz.QuartzManager;
import cn.eeepay.framework.dao.TimedTaskDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqOrg;
import cn.eeepay.framework.model.TimedTask;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/3/8/008.
 * @author  liuks
 * 定时任务监控service实现类
 */
@Service("timedTaskService")
@Transactional
public class TimedTaskServiceImpl implements TimedTaskService {
    private static final Logger log = LoggerFactory.getLogger(TimedTaskServiceImpl.class);

    public final static int THRESHOLD=30;//阀值
    @Resource
    private TimedTaskDao timedTaskDao;

    @Resource
    private Scheduler scheduler;

    @Resource
    private QuartzManager quartzManager;

    @Resource
    private TimingProduceService timingProduceService;

    @Resource
    private  QuartzAddJodService quartzAddJodService;

    @Resource
    private TimingProduceAdJobService timingProduceAdJobService;

    @Resource
    private UpdatehappyBackSumAmountAddJobService updatehappyBackSumAmountAddJobService;

    @Resource
    private ActivityDetailBackstageService activityDetailBackstageService;

    @Resource
    private AcqOrgService acqOrgService;
    /**
     * 分页查询定时任务监控表
     * @param tim
     * @param page
     * @return
     */
    @Override
    public List<TimedTask> selectAllList(TimedTask tim, Page<TimedTask> page) {
        return timedTaskDao.selectAllList(tim,page);
    }

    /**
     * 从Quartz获取相关的信息
     */
    @Override
    public void getSchedulerTimedTask() {
        Date date=new Date();
        log.info("运行定时任务监控定时!");
        try {
            List<String> jobGroupNames = scheduler.getJobGroupNames();
            if(jobGroupNames!=null&&jobGroupNames.size()>0){
                for (String groupName : jobGroupNames) {
                    Set<JobKey> jobKeys= scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName));
                    if(jobKeys!=null&&jobKeys.size()>0){
                        for (JobKey jobKey : jobKeys) {
                            String jobName = jobKey.getName();
                            String jobGroup = jobKey.getGroup();
                            TimedTask oldTimedTask=timedTaskDao.getTimedTask(jobName,jobGroup);
                            List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                            if(triggers!=null&&triggers.size()>0){
                                for (Trigger trigger : triggers) {
                                    String expression=null;
                                    if(trigger instanceof CronTrigger){
                                        expression=((CronTrigger)trigger).getCronExpression();//表达式
                                    }else if(trigger instanceof SimpleTrigger){
                                        expression=((SimpleTrigger)trigger).getRepeatInterval()+"";
                                    }else{

                                    }
                                    Trigger.TriggerState state = scheduler.getTriggerState(trigger.getKey());
                                    TimedTask timedTask=new TimedTask(); //构建参数值
                                    timedTask.setTaskName(jobName);
                                    timedTask.setTaskGroup(jobGroup);
                                    timedTask.setExpression(expression);
                                    timedTask.setTaskStatus(state.name());
                                    timedTask.setEnabledState(1);
                                    timedTask.setRetrievalTime(date);
                                    timedTask.setLastTime(trigger.getPreviousFireTime());
                                    timedTask.setNextTime(trigger.getNextFireTime());

                                    if(oldTimedTask!=null){
                                        //更新
                                        if("BLOCKED".equals(state.name())||"ERROR".equals(state.name())){//异常分支
                                            timedTask.setAbnormalTime(date);
                                            Date oldDate=oldTimedTask.getAbnormalTime();
                                            if(oldDate!=null){
                                                if(oldTimedTask.getWarningState()==1&&oldTimedTask.getOvertimeWarningState()==1){
                                                    //定时任务开启预警
                                                    int max=oldTimedTask.getEarlyWarningThreshold();
                                                    if(max<=0){
                                                        max=TimedTaskServiceImpl.THRESHOLD;
                                                        //如果设置阀值小于0,则默认30分
                                                    }
                                                    int interval=DateUtils.diffMinute(oldDate,date);
                                                    if(interval>=max){
                                                        //预警
                                                        timedTask.setId(oldTimedTask.getId());
                                                        timingProduceService.taskWarningEvents(oldDate,date,timedTask,1);
                                                        //调用预警
                                                        timedTask.setAbnormalTime(null);
                                                        timedTaskDao.updateTimedTaskAbnormalTime(timedTask);
                                                    }else{
                                                        timedTaskDao.updateTimedTask(timedTask);
                                                    }
                                                }else{
                                                    //未设置监控
                                                    timedTaskDao.updateTimedTask(timedTask);
                                                }
                                            }else{//首次异常
                                                timedTaskDao.updateTimedTaskAbnormalTime(timedTask);
                                            }
                                        }else{//正常分支
                                            timedTask.setAbnormalTime(null);
                                            timedTaskDao.updateTimedTaskAbnormalTime(timedTask);
                                        }
                                    }else{
                                        //新增
                                        if("BLOCKED".equals(state.name())||"ERROR".equals(state.name())) {//异常分支
                                            timedTask.setAbnormalTime(date);
                                        }else{
                                            timedTask.setAbnormalTime(null);
                                        }
                                        timedTaskDao.insertTimedTask(timedTask);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //检测未启动的
            List<TimedTask> listTask=timedTaskDao.getTimedTaskList();
            if(listTask!=null&&listTask.size()>0){
               for(TimedTask tt:listTask){
                   if(tt.getEnabledState()==0){//未启动
                       timingProduceService.taskWarningEvents(null,date,tt,2);
                   }
               }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearTimedTaskState() {
        timedTaskDao.clearTimedTaskState();
    }

    @Override
    public void closeTimedTask(String jobName,String jobGroup) {
        AutoJobDTO job = new AutoJobDTO();
        job.setJob_id(jobName);
        job.setJob_name(jobName);
        job.setJob_group(jobGroup);
        quartzManager.removeJob(job);
    }

    @Override
    public void resetTimedTask(String jobName, String jobGroup) {
        if(jobName!=null&&!"".equals(jobName)&&jobGroup!=null&&!"".equals(jobGroup)){
            try {
                if("ACQ_ORG".equals(jobGroup)){
                    //收单机构日切服务
                    AcqOrg acq= acqOrgService.selectByPrimaryKey(Integer.valueOf(jobName));
                    //启动定时任务
                    AutoJobDTO job = new AutoJobDTO();
                    job.setJob_id(acq.getId().toString());
                    //会根据name和group来查询是否存在该条任务，因此name设置为id唯一性
                    job.setJob_name(acq.getId().toString());
                    job.setJob_group("ACQ_ORG");
                    String time = acq.getDayAlteredTime();  //time eg:   00:01
                    if (StringUtils.isNotBlank(time)) {
                        String[] timeStr = time.split(":");
                        String conStr = timeStr[2] + " " + timeStr[1] + " " + timeStr[0] + " * * ?";
                        job.setJob_time(conStr);
                        quartzManager.addJob(job, OutAccountServiceJob.class);
                        log.info("-----------定时任务：" + acq.getAcqEnname() + " 重启------------");
                        return;
                    }
                }else{
                    //通用
                    quartzAddJodService.addPublicJob(jobName,jobGroup,null,2);
                }
            } catch (SchedulerException e) {
                e.printStackTrace();
                log.info("重启定时任务异常:"+e.getMessage());
            }
        }
    }


    @Override
    public int updateEnabledState(String jobName, String jobGroup) {
        return timedTaskDao.updateEnabledState(jobName,jobGroup);
    }

    @Override
    public TimedTask getdetailTimedTask(int id) {
        return timedTaskDao.getdetailTimedTask(id);
    }

    @Override
    public int saveDetailTimedTask(TimedTask tim) {
        if(tim.getOvertimeWarningState()==0){
            tim.setEarlyWarningThreshold(null);
        }
        if(tim.getWarningState()==0){
            tim.setErrorWarningState(0);
            tim.setOvertimeWarningState(0);
            tim.setEarlyWarningThreshold(null);
        }
        return timedTaskDao.saveDetailTimedTask(tim);
    }

    @Override
    public List<TimedTask> getTimedTaskByWarningState(int warningState) {
        return timedTaskDao.getTimedTaskByWarningState(warningState);
    }
}
