package cn.eeepay.framework.service.unTransactionalImpl;

import cn.eeepay.framework.dao.*;
import cn.eeepay.framework.model.ActivityDetail;
import cn.eeepay.framework.model.ActivityDetailBackstage;
import cn.eeepay.framework.model.CashBackDetail;
import cn.eeepay.framework.service.HappyReturnJobService;
import cn.eeepay.framework.util.ClientInterface;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service("happyReturnJobService")
public class HappyReturnJobServiceImpl implements HappyReturnJobService {

    private final static Logger log = LoggerFactory.getLogger(HappyReturnJobServiceImpl.class);
    @Resource
    private ActivityDetailBackstageDao activityDetailBackstageDao;

    @Resource
    private ActivityDetailDao activityDetailDao;

    @Resource
    private MerchantInfoDao merchantInfoDao;

    @Resource
    private SysDictDao sysDictDao;

    @Resource
    private TransInfoDao transInfoDao;

    public void happyReturnJob(){
        int maxCount=3;
        //获取2欢乐返核算3欢乐返清算
        List<ActivityDetailBackstage> actList=activityDetailBackstageDao.getActivityDetailBackstageListByHappyReturn();
        if(actList!=null&&actList.size()>0){
            //先关闭 活动账户提现开关 0是可提现，1是正在执行入账
            sysDictDao.updateValueBySysKey("1","HAPPY_TIXIAN_SWITCH");
            log.info("活动账户提现开关.............关闭.............入账开始");
            String sysValue=sysDictDao.getValueByKey("ACCOUNT_SERVICE_URL");
            String url=sysValue+"/agentAccountController/agentActAccountEnterByUps.do";
            for(int i=0;i<actList.size();i++){
                ActivityDetailBackstage act=actList.get(i);
                ActivityDetail ad =activityDetailDao.getActivityDetail(act.getActId());
                List<CashBackDetail> cashBackDetailList=activityDetailDao.getCashBackDetailById(ad.getId(),1);
                String allAgentValue=sysDictDao.getValueByKey("ALLAGENT_SERVICE_URL");
                String allAgentUrl=allAgentValue+"/activity/accActOrder";
                if("3".equals(ad.getRecommendedSource())){
                    try {
                        String result=ClientInterface.allAgentAccActOrder(ad,allAgentUrl);
                        JSONObject jsonObject = JSONObject.parseObject(result);
                        if (jsonObject.getInteger("status")==200) {
                            log.info("活动号:"+act.getActId()+",人人代理入账!");
                            activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                        }
                        continue;
                    } catch (Exception e) {
                        log.error("人人代理入账异常,活动id:"+act.getActId(),e);
                        int newcount=act.getSendNum()+1;
                        if(newcount<maxCount){
                            activityDetailBackstageDao.updateActivityDetailBackstage(newcount,act.getId());
                        }else{
                            activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                        }
                    }
                }
                if("2".equals(act.getActState())){
                    //欢乐返财务核算
                    try {
                        if("1".equals(ad.getAccountCheckStatus())&&cashBackDetailList!=null&&cashBackDetailList.size()>0){
                            boolean cashBackSwitchAgent=false;
                            BigDecimal oldCashBackAmount=BigDecimal.ZERO;
                            for (CashBackDetail c:cashBackDetailList){
                                if(cashBackSwitchAgent){
                                    activityDetailDao.updateCashBackDetail(c);
                                    continue;
                                }
                                //'返现开关 1-打开, 0-关闭' 如果代理商返现开关关闭 或者 返现金额为0，则不入账，可以直接跳过
                                if("0".equals(c.getAgentCashBackSwitch())){
                                    cashBackSwitchAgent=true;
                                    c.setRemark("返现开关关闭，不执行返现");
                                    activityDetailDao.updateCashBackDetail(c);
                                    log.info("活动号:"+act.getActId()+",代理商"+c.getAgentNo()+",返现开关关闭，不执行返现!");
                                    continue;
                                }
                                if(c.getCashBackAmount().compareTo(BigDecimal.ZERO)!=1){
                                    //返现金额为0 改为以入账
                                    c.setEntryStatus("1");
                                    activityDetailDao.updateCashBackDetail(c);
                                    log.info("活动号:"+act.getActId()+",代理商"+c.getAgentNo()+",返现金额为0 改为以入账!");
                                    continue;
                                }
                                //判断代理商返现开关是打开，如果打开 且 返现金额不为0，则调记账规则进行入账
                                String accountMsg="";
                                if(c.getAgentLevel().equals("1")){
                                    oldCashBackAmount=c.getCashBackAmount();
                                    ad.setCashBackAmount(c.getCashBackAmount());
                                    accountMsg = ClientInterface.accountCheck(ad);
                                    JSONObject json = JSONObject.parseObject(accountMsg);
                                    if (json.getBooleanValue("status")) {
                                        c.setEntryStatus("1");
                                        activityDetailDao.updateCashBackDetail(c);
                                        activityDetailDao.updateAgreeAccountCheckStatusById(act.getActId());
                                        transInfoDao.updateReturnAgent(ad.getActiveOrder(), 6);
                                    }else{
                                        c.setRemark(json.getString("msg"));
                                        activityDetailDao.updateCashBackDetail(c);
                                        log.info("活动号:"+act.getActId()+",账户接口返回欢乐返财务核算失败!");
                                    }
                                }else{
                                    if(c.getCashBackAmount().compareTo(oldCashBackAmount)==1){
                                        c.setRemark("上级倒挂,不需要入账");
                                        activityDetailDao.updateCashBackDetail(c);
                                        log.info("活动号:"+act.getActId()+",代理商"+c.getAgentNo()+",上级倒挂入账失败!");
                                        break;
                                    }else{
                                        oldCashBackAmount=c.getCashBackAmount();
                                    }
                                    accountMsg = ClientInterface.subAgentAccount(c,url);
                                    JSONObject json = JSONObject.parseObject(accountMsg);
                                    if (json.getBooleanValue("status")) {
                                        c.setEntryStatus("1");
                                        activityDetailDao.updateCashBackDetail(c);
                                    }else{
                                        c.setRemark(json.getString("msg"));
                                        activityDetailDao.updateCashBackDetail(c);
                                        log.info("活动号:"+act.getActId()+",代理商"+c.getAgentNo()+",账户接口返回欢乐返财务核算入账失败!");
                                    }
                                }
                            }
                        }
                        activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
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
                        if("1".equals(ad.getLiquidationStatus())&&cashBackDetailList!=null&&cashBackDetailList.size()>0){
                            boolean cashBackSwitchAgent=false;
                            BigDecimal oldCashBackAmount=BigDecimal.ZERO;
                            for (CashBackDetail c:cashBackDetailList){
                                if(cashBackSwitchAgent){
                                    activityDetailDao.updateCashBackDetail(c);
                                    continue;
                                }
                                //'返现开关 1-打开, 0-关闭' 如果代理商返现开关关闭 或者 返现金额为0，则不入账，可以直接跳过
                                if("0".equals(c.getAgentCashBackSwitch())){
                                    cashBackSwitchAgent=true;
                                    c.setRemark("返现开关关闭，不执行返现");
                                    activityDetailDao.updateCashBackDetail(c);
                                    log.info("活动号:"+act.getActId()+",代理商"+c.getAgentNo()+",返现开关关闭，不执行返现!");
                                    continue;
                                }
                                if(c.getCashBackAmount().compareTo(BigDecimal.ZERO)!=1){
                                    //返现金额为0 改为以入账
                                    c.setEntryStatus("1");
                                    activityDetailDao.updateCashBackDetail(c);
                                    log.info("活动号:"+act.getActId()+",代理商"+c.getAgentNo()+",返现金额为0 改为以入账!");
                                    continue;
                                }
                                //判断代理商返现开关是打开，如果打开 且 返现金额不为0，则调记账规则进行入账
                                String accountMsg="";
                                if(c.getAgentLevel().equals("1")){
                                    oldCashBackAmount=c.getCashBackAmount();
                                    ad.setCashBackAmount(c.getCashBackAmount());
                                    accountMsg = ClientInterface.liquidation(ad);
                                    JSONObject json = JSONObject.parseObject(accountMsg);
                                    if (json.getBooleanValue("status")) {
                                        c.setEntryStatus("1");
                                        activityDetailDao.updateCashBackDetail(c);
                                        activityDetailDao.updateAgreeLiquidationStatusById(act.getActId());
                                        transInfoDao.updateReturnAgent(ad.getActiveOrder(), 6);
                                    }else{
                                        c.setRemark(json.getString("msg"));
                                        activityDetailDao.updateCashBackDetail(c);
                                        log.info("活动号:"+act.getActId()+",账户接口返回欢乐返清算核算入账失败!");
                                    }
                                }else{
                                    if(c.getCashBackAmount().compareTo(oldCashBackAmount)==1){
                                        c.setRemark("上级倒挂,不需要入账");
                                        activityDetailDao.updateCashBackDetail(c);
                                        log.info("活动号:"+act.getActId()+",代理商"+c.getAgentNo()+",上级倒挂入账失败!");
                                        break;
                                    }else{
                                        oldCashBackAmount=c.getCashBackAmount();
                                    }
                                    accountMsg = ClientInterface.subAgentAccount(c,url);
                                    JSONObject json = JSONObject.parseObject(accountMsg);
                                    if (json.getBooleanValue("status")) {
                                        c.setEntryStatus("1");
                                        activityDetailDao.updateCashBackDetail(c);
                                    }else{
                                        c.setRemark(json.getString("msg"));
                                        activityDetailDao.updateCashBackDetail(c);
                                        log.info("活动号:"+act.getActId()+",代理商"+c.getAgentNo()+",账户接口返回欢乐返清算核算入账失败!");
                                    }
                                }
                            }
                        }
                        activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                    } catch (Exception e) {
                        log.error("欢乐返清算核算异常,活动id:"+act.getActId(),e);
                        int newcount=act.getSendNum()+1;
                        if(newcount<maxCount){
                            activityDetailBackstageDao.updateActivityDetailBackstage(newcount,act.getId());
                        }else{
                            activityDetailBackstageDao.deleteActivityDetailBackstage(act.getId());
                        }
                    }
                }
            }
            //打开 活动账户提现开关 0是可提现，1是正在执行入账
            sysDictDao.updateValueBySysKey("0","HAPPY_TIXIAN_SWITCH");
            log.info("活动账户提现开关.............打开.............入账结束");
        }
    }
}
