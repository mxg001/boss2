package cn.eeepay.framework.service.impl;


import cn.eeepay.boss.job.ActivityDetailBackstageJob;
import cn.eeepay.boss.quartz.AutoJobDTO;
import cn.eeepay.boss.quartz.QuartzManager;
import cn.eeepay.framework.dao.ActivityDetailBackstageDao;
import cn.eeepay.framework.dao.ActivityDetailDao;
import cn.eeepay.framework.dao.TransInfoDao;
import cn.eeepay.framework.model.ActivityDetail;
import cn.eeepay.framework.model.ActivityDetailBackstage;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.ActivityDetailBackstageService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.ClientInterface;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/30/030.
 * @author liuks
 * 欢乐送,欢乐返活动延时核算清算 定时 serviceImpl
 */
@Service("activityDetailBackstageService")
public class ActivityDetailBackstageServiceImpl implements ActivityDetailBackstageService {

    public final static String JOBKEY="activityDetailBackstage";//JOB定时任务

    public final static String EXPRESSION="activityDetailBackstageExpression";//数据字典key

    private static final Logger log = LoggerFactory.getLogger(ActivityDetailBackstageServiceImpl.class);

    @Resource
    private ActivityDetailBackstageDao activityDetailBackstageDao;

    @Resource
    private ActivityDetailDao activityDetailDao;

    @Resource
    private TransInfoDao transInfoDao;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private QuartzManager quartzManager;

    @Override
    public int insertActivityDetailBackstage(ActivityDetailBackstage act) {
        return activityDetailBackstageDao.insertActivityDetailBackstage(act);
    }

    @Override
    public List<ActivityDetailBackstage> getActivityDetailBackstage(int actId,String actState) {
        return activityDetailBackstageDao.getActivityDetailBackstage(actId,actState);
    }


    @Override
    public void execute() {
        int maxCount=10;
        List<ActivityDetailBackstage> actList=activityDetailBackstageDao.getActivityDetailBackstageListAll();
        if(actList!=null&&actList.size()>0){
            for(int i=0;i<actList.size();i++){
                ActivityDetailBackstage act=actList.get(i);
                ActivityDetail ad =activityDetailDao.getActivityDetail(act.getActId());
                if("1".equals(act.getActState())){
                    //欢乐送核算
                    try {
                        if(ad.getCheckStatus()!=1){
                            String accountMsg = ClientInterface.happySendAgentRecordAccountForCaiWu(ad);
                            if(JSONObject.parseObject(accountMsg).getBooleanValue("status")){
                                activityDetailDao.updateAdjustStatus(ad.getId(),"1",act.getUserId()+"");//核算
                                activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                            }else{
                                log.info("活动号:"+act.getActId()+",账户接口返回欢乐送核算失败!");
                                activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                            }
                        }else{
                            activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                        }
                    } catch (Exception e) {
                        log.error("欢乐送核算异常,活动id:"+act.getActId(),e);
                        int newcount=act.getSendNum()+1;
                        if(newcount<maxCount){
                            activityDetailBackstageDao.updateActivityDetailBackstage(newcount,act.getId());
                        }else{
                            activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                        }
                    }
                /*}else if("2".equals(act.getActState())){
                    //欢乐返财务核算
                    try {
                        if(!"1".equals(ad.getAccountCheckStatus())) {
                            String accountMsg = ClientInterface.accountCheck(ad);
                            JSONObject json = JSONObject.parseObject(accountMsg);
                            if (json.getBooleanValue("status")) {
                                activityDetailDao.updateAgreeAccountCheckStatus(act.getActId(), "1", act.getUserId());
                                transInfoDao.updateReturnAgent(ad.getActiveOrder(), 6);
                                activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                            }else{
                                log.info("活动号:"+act.getActId()+",账户接口返回欢乐返财务核算失败!");
                                activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                            }
                        }else{
                            activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                        }
                    } catch (Exception e) {
                        log.error("欢乐返财务核算异常,活动id:"+act.getActId(),e);
                        int newcount=act.getSendNum()+1;
                        if(newcount<maxCount){
                            activityDetailBackstageDao.updateActivityDetailBackstage(newcount,act.getId());
                        }else{
                            activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                        }
                    }
                }else if("3".equals(act.getActState())){
                    //欢乐返清算核算
                    try {
                        if(!"1".equals(ad.getLiquidationStatus())){
                            String accountMsg = ClientInterface.liquidation(ad);
                            JSONObject json = JSONObject.parseObject(accountMsg);
                            if(json.getBooleanValue("status")){
                                activityDetailDao.updateAgreeLiquidationStatus(act.getActId(),"1",act.getUserId());
                                transInfoDao.updateReturnAgent(ad.getActiveOrder(), 6);
                                activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                            }else{
                                log.info("活动号:"+act.getActId()+",账户接口返回欢乐返清算核算失败!");
                                activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                            }
                        }else{
                            activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                        }
                    } catch (Exception e) {
                        log.error("欢乐返清算核算异常,活动id:"+act.getActId(),e);
                        int newcount=act.getSendNum()+1;
                        if(newcount<maxCount){
                            activityDetailBackstageDao.updateActivityDetailBackstage(newcount,act.getId());
                        }else{
                            activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                        }
                    }*/
                }else if("4".equals(act.getActState())){
                    //欢乐返奖励入账
                    try {
                        if(ad.getAddAmountTime()==null){
                            String returnMsg = ClientInterface.happyBackDaYuRecordAccount(ad);
                            Map<String, Object> result = JSON.parseObject(returnMsg);
                            String msgCode=result.get("msgCode").toString();
                            if("000000".equals(msgCode)){
                                //更新数据库值
                                ActivityDetail newAd=new ActivityDetail();
                                newAd.setId(ad.getId());
                                newAd.setStatus(9);
                                activityDetailDao.updateRewardIsBooked(newAd);
                                activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                            }else{
                                log.info("活动号:"+act.getActId()+",账户接口返回欢乐返奖励入账失败!");
                                activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                            }
                        }else{
                            activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                        }
                    }catch (Exception e) {
                        log.error("欢乐返奖励入账异常,活动id:"+act.getActId(),e);
                        int newcount=act.getSendNum()+1;
                        if(newcount<maxCount){
                            activityDetailBackstageDao.updateActivityDetailBackstage(newcount,act.getId());
                        }else{
                            activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                        }
                    }
                }
                if(i>1&&i%1000==0){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public String addJob() {
        SysDict cycleSysDict=sysDictService.getByKey(ActivityDetailBackstageServiceImpl.EXPRESSION);
        //启动定时任务
        AutoJobDTO job = new AutoJobDTO();
        job.setJob_id(ActivityDetailBackstageServiceImpl.JOBKEY);
        job.setJob_name(ActivityDetailBackstageServiceImpl.JOBKEY);
        job.setJob_group(ActivityDetailBackstageServiceImpl.JOBKEY);
        if(cycleSysDict==null){
            log.info("欢乐送,欢乐返活动延时核算清算定时数据字典配置未配置!");
            return null;
        }
        //会根据name和group来查询是否存在该条任务，因此name设置为id唯一性
        if(cycleSysDict.getSysValue()==null||"".equals(cycleSysDict.getSysValue())){
            log.info("欢乐送,欢乐返活动延时核算清算定时数据字典配置数据异常!");
            quartzManager.removeJob(job);
            return null;
        }
        job.setJob_time(cycleSysDict.getSysValue());
        try {
            quartzManager.addJob(job,ActivityDetailBackstageJob.class);
            log.info("欢乐送,欢乐返活动延时核算清算定时设置成功!");
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.info("欢乐送,欢乐返活动延时核算清算定时设置失败!");
        }
        return "OK";
    }

    @Override
    public int countActivityDetailBackstage(String actState) {
        return activityDetailBackstageDao.countActivityDetailBackstage(actState);
    }
}
