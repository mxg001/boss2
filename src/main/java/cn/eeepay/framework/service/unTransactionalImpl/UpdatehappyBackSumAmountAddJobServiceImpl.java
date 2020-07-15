package cn.eeepay.framework.service.unTransactionalImpl;

import cn.eeepay.boss.job.HappyBackCumulativeJob;
import cn.eeepay.boss.quartz.AutoJobDTO;
import cn.eeepay.boss.quartz.QuartzManager;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.UpdatehappyBackSumAmountAddJobService;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/1/19/019.
 * @author  liuks
 * 添加欢乐返定时统计Job
 */
@Service("updatehappyBackSumAmountAddJobService")
public class UpdatehappyBackSumAmountAddJobServiceImpl implements UpdatehappyBackSumAmountAddJobService {

    public final static String JOBKEY="happyBackSumAmount";//JOB定时任务

    public final static String EXPRESSION="happyBackSumAmountExpression";//数据字典key

    private static final Logger log = LoggerFactory.getLogger(UpdatehappyBackSumAmountAddJobServiceImpl.class);

    @Resource
    private SysDictService sysDictService;

    @Resource
    private QuartzManager quartzManager;

    /**
     * 添加定时任务
     */
    @Override
    public String addJobHappyBackSumAmount() {
        SysDict cycleSysDict=sysDictService.getByKey(UpdatehappyBackSumAmountAddJobServiceImpl.EXPRESSION);

        //启动定时任务
        AutoJobDTO job = new AutoJobDTO();
        job.setJob_id(UpdatehappyBackSumAmountAddJobServiceImpl.JOBKEY);
        job.setJob_name(UpdatehappyBackSumAmountAddJobServiceImpl.JOBKEY);
        job.setJob_group(UpdatehappyBackSumAmountAddJobServiceImpl.JOBKEY);
        if(cycleSysDict==null){
            log.info("欢乐返定时统计总金额数据字典配置未配置!");
            return null;
        }
        //会根据name和group来查询是否存在该条任务，因此name设置为id唯一性
        if(cycleSysDict.getSysValue()==null||"".equals(cycleSysDict.getSysValue())){
            log.info("欢乐返定时统计总金额数据字典配置数据异常!");
            quartzManager.removeJob(job);
            return null;
        }

        job.setJob_time(cycleSysDict.getSysValue());
        try {
            quartzManager.addJob(job,HappyBackCumulativeJob.class);
            log.info("欢乐返定时统计总金额定时设置成功!");
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.info("欢乐返定时统计总金额定时设置失败!");
        }
        return "OK";
    }
}
