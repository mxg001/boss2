package cn.eeepay.framework.service.unTransactionalImpl.abstractJob;

import cn.eeepay.framework.dao.ScheduleDao;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Md5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/15/015.
 * @author  liuks
 * 定时任务JOB
 */
@Component
public abstract class ScheduleJob implements Runnable{

    private static final Logger log = LoggerFactory.getLogger(ScheduleJob.class);

    private String runNo;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private ScheduleDao scheduleDao;

    public String getRunNo() {
        return runNo;
    }

    public void setRunNo(String runNo) {
        this.runNo = runNo;
    }

    /**
     * 任务执行
     */
    protected abstract void runTask(String runNo);

    @Override
    public void run() {
        try {
            log.info("定时任务执行,runningNo:{}",runNo);
            runTask(runNo);
            log.info("定时任务执行回调,runningNo:{}",runNo);
            callback(runNo,"complete");
        }catch (Exception e){
            e.printStackTrace();
            log.error("定时任务执行异常,runningNo:{}",runNo,e);
        }
    }

    private void callback(String runNo, String status) {
        try {
            String BOSS_TASK_KEY=sysDictService.getValueByKey("BOSS_TASK_KEY");
            String BOSS_TASK_CALLBACK=sysDictService.getValueByKey("BOSS_TASK_CALLBACK");
            scheduleDao.updateStatus(runNo, status);
            Map<String, String> param = new HashMap<>();
            param.put("runningNo", runNo);
            param.put("runningStatus", status);
            param.put("hmac", Md5.md5Str(runNo + status + BOSS_TASK_KEY));
            log.info("任务回调,runningNo:{}",runNo);
            String result=new ClientInterface(BOSS_TASK_CALLBACK, param).postRequest();
            log.info("定时任务回调,runningNo:{}，返回值：{}",runNo,result);
        } catch (Exception e) {
            log.error("回调调度系统异常,runningNo:{}", runNo, e);
        }
    }

}
