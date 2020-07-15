package cn.eeepay.framework.service.unTransactionalImpl;

import cn.eeepay.framework.dao.ActivityDetailDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.dao.UpdatehappyBackSumAmountDao;
import cn.eeepay.framework.daoAllAgent.AgentUserDao;
import cn.eeepay.framework.daoHistory.HappyBackHistoryDao;
import cn.eeepay.framework.model.ActivityDetail;
import cn.eeepay.framework.model.CashBackDetail;
import cn.eeepay.framework.model.happyBack.FilterDate;
import cn.eeepay.framework.model.happyBack.FilterPage;
import cn.eeepay.framework.model.happyBack.HappyBackSumAmount;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.UpdatehappyBackSumAmountService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 欢乐返商户交易金额累计 service
 * liuks
 *2017/12/26/026.
 */
@Service("updatehappyBackSumAmountService")
public class UpdatehappyBackSumAmountServiceImpl implements UpdatehappyBackSumAmountService {

    private static final Logger log = LoggerFactory.getLogger(UpdatehappyBackSumAmountServiceImpl.class);

    @Resource
    private UpdatehappyBackSumAmountDao updatehappyBackSumAmountDao;
    @Resource
    private ActivityDetailDao activityDetailDao;
    @Resource
    private HappyBackHistoryDao happyBackHistoryDao;
    @Resource
    private SysDictService sysDictService;
    @Resource
    private SysDictDao sysDictDao;
    @Resource
    private AgentUserDao agentUserDao;

    @Override
    public  void updatehappyBackCumulative()  {
        //加载数据字典配置值
        int dateTerm=Integer.parseInt(sysDictService.getValueByKey("happyBackTermDay"));
        int dateDedTerm=Integer.parseInt(sysDictService.getValueByKey("happyBackTermDedDay"));
        int monthTerm=Integer.parseInt(sysDictService.getValueByKey("happyBackTermMonth"));
        final int switchTerm=Integer.parseInt(sysDictService.getValueByKey("happyBackSwitch"));

        final CountDownLatch end = new CountDownLatch(3);//开启计数器

        //昨天23:59:59
        final Calendar yesterday = new GregorianCalendar();
        yesterday.set(Calendar.HOUR_OF_DAY, 23);
        yesterday.set(Calendar.MINUTE, 59);
        yesterday.set(Calendar.SECOND, 59);
        yesterday.set(Calendar.MILLISECOND, 0);
        yesterday.add(Calendar.DAY_OF_MONTH, -1);

        //前几天的 23:59:59
        final Calendar theOtherDay = new GregorianCalendar();
        theOtherDay.set(Calendar.HOUR_OF_DAY,23);
        theOtherDay.set(Calendar.MINUTE,59);
        theOtherDay.set(Calendar.SECOND,59);
        theOtherDay.set(Calendar.MILLISECOND,0);
        theOtherDay.add(Calendar.DAY_OF_MONTH,0-dateTerm);

        //扣减的前几天的 23:59:59
        final Calendar theOtherDedDay = new GregorianCalendar();
        theOtherDedDay.set(Calendar.HOUR_OF_DAY,23);
        theOtherDedDay.set(Calendar.MINUTE,59);
        theOtherDedDay.set(Calendar.SECOND,59);
        theOtherDedDay.set(Calendar.MILLISECOND,0);
        theOtherDedDay.add(Calendar.DAY_OF_MONTH,0-dateDedTerm);

        //几月前的 00:00:00
        final Calendar theOtherMonth = new GregorianCalendar();
        theOtherMonth.set(Calendar.HOUR_OF_DAY,0);
        theOtherMonth.set(Calendar.MINUTE,0);
        theOtherMonth.set(Calendar.SECOND,0);
        theOtherMonth.set(Calendar.MILLISECOND,0);
        theOtherMonth.add(Calendar.MONTH, 0-monthTerm);

        //查询minOverTime 数据为空的
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("欢乐返定时统计满奖不满,满奖累计(minOverTime is null)执行开始.....!");
                    FilterDate adCon=new FilterDate();
                    adCon.setTimeNull(0);
                    adCon.setEndDate(theOtherDay.getTime());
                    sumReward(adCon,yesterday,theOtherMonth,switchTerm);//调用统计逻辑
                    log.info("欢乐返定时统计满奖不满,满奖累计(minOverTime is null)执行结束.....!");
                }catch (Exception e){
                    log.error("欢乐返定时统计满奖不满,满奖累计(minOverTime不为空)异常",e);
                }finally {
                    end.countDown();
                }
            }
        }).start();

        //--------------------------------------------------------------------------
        //查询 minOverTime 不为空的
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("欢乐返定时统计满奖不满,满奖累计(minOverTime不为空)执行开始.....!");
                    FilterDate adCon=new FilterDate();
                    adCon.setTimeNull(1);
                    adCon.setEndDate(theOtherDay.getTime());
                    sumReward(adCon,yesterday,theOtherMonth,switchTerm);//调用统计逻辑
                    log.info("欢乐返定时统计满奖不满,满奖累计(minOverTime不为空)执行结束.....!");
                }catch (Exception e){
                    log.error("欢乐返定时统计满奖不满,满奖累计(minOverTime不为空)异常",e);
                }finally {
                    end.countDown();
                }
            }
        }).start();


        //--------------------------------------------------------------------------
        //扣减逻辑
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("欢乐返定时统计满奖不满,扣减逻辑执行开始.....!");
                    FilterDate adCon=new FilterDate();
                    adCon.setStartDate(theOtherDedDay.getTime());
                    adCon.setEndDate(yesterday.getTime());
                    sumDeduction(adCon,theOtherMonth);
                    log.info("欢乐返定时统计满奖不满,扣减逻辑执行结束.....!");
                }catch (Exception e){
                    log.error("欢乐返定时统计满奖不满,扣减逻辑异常",e);
                }finally {
                    end.countDown();
                }
            }
        }).start();

        //--------------------------------------------------------------------------
        //等待返回
        //执行最多时间
        try {
            end.await(12*60*60*1000, TimeUnit.MILLISECONDS);
            log.info("欢乐返定时统计满奖不满扣-调用接口返回定时任务系统!");
        }catch (Exception e){
            log.error("欢乐返定时统计满奖不满扣,等待异常!",e);
        }
    }


    /**
     * 奖励统计
     */
    private void sumReward(FilterDate adCon,Calendar yesterday,Calendar theOtherMonth,int switchTerm){
        FilterPage page=new FilterPage();

        while(true){
            List<ActivityDetail> list=updatehappyBackSumAmountDao.happyBackListRewardPage(adCon,page);
            if(list==null||list.size()<=0){//已经没有数据了
                break;
            }
            for(ActivityDetail ad:list){
                //根据开关,达标是否再次统计 o统计1统计
                if(0==switchTerm){
                    if("1".equals(ad.getIsStandard())){
                        log.info("欢乐返定时统计满奖不满扣,MerchantNo:{},已达标不继续统计",ad.getMerchantNo());
                        continue;
                    }
                }

                FilterDate sumCon=new FilterDate();
                //统计条件
                sumCon.setMerchantNo(ad.getMerchantNo());
                sumCon.setNowDate(yesterday.getTime());//昨天23:59:59

                //激活时间的当天0时0分0秒
                Calendar activeDate=Calendar.getInstance();
                activeDate.setTime(ad.getActiveTime());
                activeDate.set(Calendar.HOUR_OF_DAY, 0);
                activeDate.set(Calendar.MINUTE, 0);
                activeDate.set(Calendar.SECOND, 0);
                activeDate.set(Calendar.MILLISECOND, 0);
                sumCon.setStartDate(activeDate.getTime());
                sumCon.setCountTradeScope(ad.getCountTradeScope());

                if(0==adCon.getTimeNull()){
                    if(ad.getOverdueTime().getTime()<=yesterday.getTime().getTime()){
                        sumCon.setEndDate(ad.getOverdueTime());
                    }else{
                        sumCon.setEndDate(yesterday.getTime());
                    }
                }else if(1==adCon.getTimeNull()){
                    if(ad.getMinOverdueTime().getTime()<=yesterday.getTime().getTime()){
                        sumCon.setEndDate(ad.getMinOverdueTime());
                    }else{
                        sumCon.setEndDate(yesterday.getTime());
                    }
                }
                log.info("欢乐返定时奖励统计-当前库,交易条件:"+sumCon.toString());

                //统计主库金额
                HappyBackSumAmount sumAmount= updatehappyBackSumAmountDao.happyBackSumAmoun(sumCon);

                HappyBackSumAmount sumAmountOld=null;
                //当激活时间在半年前时,需要统计历史库
                if(ad.getActiveTime().getTime()<=theOtherMonth.getTime().getTime()){
                    //统计历史库金额
                    log.info("欢乐返定时奖励统计-历史库,交易条件:"+sumCon.toString());
                    sumAmountOld= happyBackHistoryDao.happyBackSumAmoun(sumCon);
                }
                //计算是否达标
                judgeStandard(ad,sumAmount,sumAmountOld,sumCon);
            }
            page.setPage(page.getPage()+1);//页数加1
        }
    }
    //判断是否达标
    private void judgeStandard(ActivityDetail ad,HappyBackSumAmount sumAmount,HappyBackSumAmount sumAmountOld,FilterDate sumCon){
        BigDecimal conditions=ad.getCumulateAmountAdd(); //奖励阈值
        BigDecimal total=BigDecimal.ZERO;
        if(sumAmount!=null&&sumAmount.getTotal()!=null&&BigDecimal.ZERO.compareTo(sumAmount.getTotal())<0){
            total=total.add(sumAmount.getTotal());
            log.info("欢乐返定时统计满奖不满扣-满奖,MerchantNo:{},主库 total:{}",ad.getMerchantNo(),sumAmount.getTotal());
        }
        if(sumAmountOld!=null&&sumAmountOld.getTotal()!=null&&BigDecimal.ZERO.compareTo(sumAmountOld.getTotal())<0){
            total=total.add(sumAmountOld.getTotal());
            log.info("欢乐返定时统计满奖不满扣-满奖,MerchantNo:{},历史库 total:{}",ad.getMerchantNo(),sumAmountOld.getTotal());
        }
        if(total!=null&&BigDecimal.ZERO.compareTo(total)<0) {
            if (conditions!=null&&total.compareTo(conditions) >= 0) {//a>=b
                //达标
                if ("0".equals(ad.getIsStandard())) {//状态为达标
                    log.info("欢乐返满奖开始---id"+ad.getId());
                    updateAdReward(1,ad,total,sumCon.getNowDate());
                    oneRewardIsBooked(ad.getId());
                    log.info("欢乐返满奖结束---id"+ad.getId());
                }else if("1".equals(ad.getIsStandard())) {//状态已达标
                    updateAdReward(0,ad,total,sumCon.getNowDate());
                }
            }else{
                updateAdReward(0,ad,total,sumCon.getNowDate());
            }
        }
    }
    /**
     * 奖励累计
     * @param state 状态 0直接累计 1 更新达标状态累加
     * @param ad 更新对象
     * @param nowAmount 增加金额
     */
    private void updateAdReward(int state,ActivityDetail ad,BigDecimal nowAmount,Date nowDate){
        if(state==0){
            //不满足累计
            ad.setCumulateTransAmount(nowAmount);
            activityDetailDao.updateActivityDetailForSum(ad);
        }else if(state==1){
            //达标
            //满足条件，标记已达标
            ad.setIsStandard("1");
            ad.setStandardTime(nowDate);
            ad.setCumulateTransAmount(nowAmount);
            activityDetailDao.updateActivityDetailForStandard(ad);
        }
    }

    /**
     * 扣减统计
     */
    private void sumDeduction(FilterDate adCon,Calendar theOtherMonth){
        List<ActivityDetail> list=updatehappyBackSumAmountDao.happyBackListDeduction(adCon);
        for(ActivityDetail ad:list){
            FilterDate sumCon=new FilterDate();
            //统计条件
            sumCon.setMerchantNo(ad.getMerchantNo());

            //激活时间的当天0时0分0秒
            Calendar activeDate=Calendar.getInstance();
            activeDate.setTime(ad.getActiveTime());
            activeDate.set(Calendar.HOUR_OF_DAY, 0);
            activeDate.set(Calendar.MINUTE, 0);
            activeDate.set(Calendar.SECOND, 0);
            activeDate.set(Calendar.MILLISECOND, 0);
            sumCon.setStartDate(activeDate.getTime());

            sumCon.setEndDate(ad.getOverdueTime());
            sumCon.setCountTradeScope(ad.getCountTradeScope());

            log.info("欢乐返定时扣减统计-当前库,交易条件:"+sumCon.toString());

            //统计全量
            HappyBackSumAmount sumAmount= updatehappyBackSumAmountDao.happyBackSumAmoun(sumCon);

            HappyBackSumAmount sumAmountOld=null;
            //当激活时间在半年前时,需要统计历史库
            if(ad.getActiveTime().getTime()<=theOtherMonth.getTime().getTime()){
                //统计历史库金额
                log.info("欢乐返定时扣减统计-历史库,交易条件:"+sumCon.toString());
                sumAmountOld= happyBackHistoryDao.happyBackSumAmoun(sumCon);
            }
            //判断是否扣减
            judgeDeduction(ad,sumAmount,sumAmountOld);
        }
    }
    //判断是扣减
    private void judgeDeduction(ActivityDetail ad,HappyBackSumAmount sumAmount,HappyBackSumAmount sumAmountOld){
        BigDecimal conditions=ad.getCumulateAmountMinus(); //扣减阈值
        BigDecimal total=BigDecimal.ZERO;
        if(sumAmount!=null&&sumAmount.getTotal()!=null&&BigDecimal.ZERO.compareTo(sumAmount.getTotal())<0){
            total=total.add(sumAmount.getTotal());
            log.info("欢乐返定时统计满奖不满扣-扣减,MerchantNo:{},历史库 total:{}",ad.getMerchantNo(),sumAmount.getTotal());
        }
        if(sumAmountOld!=null&&sumAmountOld.getTotal()!=null&&BigDecimal.ZERO.compareTo(sumAmountOld.getTotal())<0){
            total=total.add(sumAmountOld.getTotal());
            log.info("欢乐返定时统计满奖不满扣-扣减,MerchantNo:{},历史库 total:{}",ad.getMerchantNo(),sumAmountOld.getTotal());
        }
        if(conditions!=null && BigDecimal.ZERO.compareTo(conditions)<0
                && total.compareTo(conditions)<0){//统计值小于阀值

            if (BigDecimal.ZERO.compareTo(ad.getEmptyAmount())<0) {//奖励金额>0
                log.info("欢乐返扣款进入接口，MerchantNo:{}",ad.getMerchantNo());
                updateActivityDetailForRewardIsBooked(ad);
            }
        }
        //更新统计值
        updateAdDeduction(ad,total);
    }

    /**
     * 扣减汇总更新
     */
    private void updateAdDeduction(ActivityDetail ad,BigDecimal nowAmount){
        ad.setCumulateTransMinAmount(nowAmount);
        activityDetailDao.updateAdDeduction(ad);
    }



    /**
     * 调用接口处理以及后续数据处理
     * liuks
     * 奖励入账更新数据
     */
    // msgCode = "000000"; 记账成功
    // msgCode = "111111"; 记账失败
    // msgCode = "999999"; 记账异常
    // msgCode  "000002"  //成功发起预调账
    // msgCode  "000003"  //预调账失败
    private void updateActivityDetailForRewardIsBooked(ActivityDetail useAd){
        String sysValue=sysDictDao.getValueByKey("ACCOUNT_SERVICE_URL");
        List<CashBackDetail> cashBackDetailList=activityDetailDao.getCashBackDetailById(useAd.getId(),3);
        //调用接口
        if(cashBackDetailList==null||cashBackDetailList.size()==0){
            String returnMsg = ClientInterface.happyBackXiaoYuRecordAccount(useAd);
            Map<String, Object> result = JSON.parseObject(returnMsg);
            if (result != null) {
                String msgCode=result.get("msgCode")==null?"":result.get("msgCode").toString();
                if("000000".equals(msgCode)){
                    //更新数据库值
                    ActivityDetail newAd=new ActivityDetail();
                    newAd.setId(useAd.getId());
                    newAd.setStatus(7);//7,8
                    activityDetailDao.updateRewardIsBookedMinus(newAd);
                }else if("000002".equals(msgCode)){
                    //更新数据库值
                    ActivityDetail newAd=new ActivityDetail();
                    newAd.setId(useAd.getId());
                    newAd.setStatus(8);//7,8
                    activityDetailDao.updateRewardIsBookedMinus(newAd);
                }
            }
        }else{
            BigDecimal oldCashBackAmount=BigDecimal.ZERO;
            for (int i=0;i<cashBackDetailList.size();i++){
                BigDecimal cashBackAmount=cashBackDetailList.get(i).getCashBackAmount();
                if(StringUtil.isBlank(cashBackAmount)){
                    log.info("订单id:"+useAd.getId()+",代理商"+cashBackDetailList.get(i).getAgentNo()+",不满扣开金额为空不入账跳过!");
                    break;
                }
                //'不满扣开关 1-打开, 0-关闭' 不入账，可以直接跳过
                cashBackDetailList.get(i).setAgentCashBackSwitch(cashBackDetailList.get(i).getNotFullDeductSwitch());
                if("0".equals(cashBackDetailList.get(i).getNotFullDeductSwitch())){
                    cashBackDetailList.get(i).setRemark("不满扣开关关闭，不执行不满扣");
                    activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                    log.info("订单id:"+useAd.getId()+",代理商"+cashBackDetailList.get(i).getAgentNo()+",不满扣开关关闭，不执行不满扣!");
                    break;
                }
               /* if(c.getCashBackAmount().compareTo(BigDecimal.ZERO)!=1){
                    //不满扣为0 改为以入账
                    c.setEntryStatus("1");
                    activityDetailDao.updateCashBackDetail(c);
                    log.info("订单id:"+useAd.getId()+",代理商"+c.getAgentNo()+",不满扣金额为0 改为以入已扣款!");
                    continue;
                }*/
                if(cashBackDetailList.get(i).getAgentLevel().equals("1")) {
                    oldCashBackAmount = cashBackAmount;
                }
                if(cashBackAmount.compareTo(oldCashBackAmount)==1){
                    cashBackDetailList.get(i).setRemark("上级倒挂,不需要扣款");
                    activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                    log.info("订单id:"+useAd.getId()+",代理商"+cashBackDetailList.get(i).getAgentNo()+",上级倒挂扣款失败!");
                    break;
                }else{
                    oldCashBackAmount=cashBackAmount;
                }
                //取差值
                /*if((i+1)<cashBackDetailList.size()){
                    BigDecimal cashBackAmount2=cashBackDetailList.get(i+1).getCashBackAmount();
                    cashBackAmount2=StringUtil.isBlank(cashBackAmount2)?BigDecimal.ZERO:cashBackAmount2;
                    if((cashBackAmount.subtract(cashBackAmount2)).compareTo(BigDecimal.ZERO)!=-1&&"1".equals(cashBackDetailList.get(i+1).getNotFullDeductSwitch())){
                        cashBackDetailList.get(i).setCashBackAmount((cashBackAmount.subtract(cashBackAmount2)));
                    }
                }*/
                String returnMsg = ClientInterface.newHappyBackXiaoYuRecordAccount(sysValue,cashBackDetailList.get(i),useAd.getMerchantNo());
                Map<String, Object> result = JSON.parseObject(returnMsg);
                if (result != null) {
                    String msgCode=result.get("msgCode")==null?"":result.get("msgCode").toString();
                    if("000000".equals(msgCode)){
                        cashBackDetailList.get(i).setEntryStatus("1");
                        activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                        //更新数据库值
                        if(cashBackDetailList.get(i).getAgentLevel().equals("1")) {
                            ActivityDetail newAd=new ActivityDetail();
                            newAd.setId(useAd.getId());
                            newAd.setStatus(7);//7,8
                            activityDetailDao.updateRewardIsBookedMinus(newAd);
                        }
                    }else if("000002".equals(msgCode)){
                        cashBackDetailList.get(i).setPreTransferStatus(1);
                        cashBackDetailList.get(i).setPreTransferTime(new Date());
                        activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                        //更新数据库值
                        if(cashBackDetailList.get(i).getAgentLevel().equals("1")) {
                            ActivityDetail newAd=new ActivityDetail();
                            newAd.setId(useAd.getId());
                            newAd.setStatus(8);//7,8
                            activityDetailDao.updateRewardIsBookedMinus(newAd);
                        }
                    }else{
                        cashBackDetailList.get(i).setRemark(result.get("msg").toString());
                        activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                        log.info("订单id:"+useAd.getId()+",代理商"+cashBackDetailList.get(i).getAgentNo()+",扣款失败!");
                    }
                }else{
                    cashBackDetailList.get(i).setRemark("账户系统异常");
                    activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                    log.info("订单id:"+useAd.getId()+",代理商"+cashBackDetailList.get(i).getAgentNo()+",扣款失败!");
                }
            }
        }
    }

    /**
     * 奖励入账
     * @param id
     * @return
     */
    public Map<String, Object> oneRewardIsBooked(Integer id) {
        Map<String, Object> msg = new HashMap<String, Object>();
        ActivityDetail resultAd=activityDetailDao.getActivityDetailById(id);
        //状态为已返代理商 6  ,已达标，奖励时间为空
        if ((resultAd.getStatus() == 6||resultAd.getStatus() == 7||resultAd.getStatus() == 8) && "1".equals(resultAd.getIsStandard()) && resultAd.getAddAmountTime() == null) {
            if (BigDecimal.ZERO.equals(resultAd.getFullAmount())) {
                log.info("满奖M元的值为0，不需要奖励+id为", id);
                msg.put("status", false);
                msg.put("msg", "满奖M元的值为0，不需要奖励");
                return msg;
            } else {
                try{
                    List<CashBackDetail> cashBackDetailList=activityDetailDao.getCashBackDetailById(resultAd.getId(),2);
                    //查询预调账
                    String ACCOUNT_SERVICE_URL_VALUE = sysDictDao.getValueByKey("ACCOUNT_SERVICE_URL");
                    String findAgentPreUrl = ACCOUNT_SERVICE_URL_VALUE + "/agentAccountController/findAgentPreAdjustBalance.do";
                    String adjustBalanceMsg = ClientInterface.findAgentPreAdjustBalance(resultAd.getOneAgentNo(), findAgentPreUrl);
                    JSONObject adjustJson = JSONObject.parseObject(adjustBalanceMsg);
                    //执行入账
                    String msgCode="";
                    boolean status=false;
                    if(cashBackDetailList==null||cashBackDetailList.size()==0){
                        if(resultAd.getAgentType()!=null&&"11".equals(resultAd.getAgentType())){
                            //超级盟主满奖
                            String url = ACCOUNT_SERVICE_URL_VALUE+"/peragentController/peragentAccount.do";
                            String returnMsg = ClientInterface.peragentAccountDemo(url,resultAd);
                            Map<String, Object> result = JSON.parseObject(returnMsg);
                            status = ((JSONObject) result).getBooleanValue("status");
                        }else {
                            String returnMsg = ClientInterface.happyBackDaYuRecordAccount(resultAd);
                            Map<String, Object> result = JSON.parseObject(returnMsg);
                            msgCode = result.get("msgCode").toString();
                        }
                        if("000000".equals(msgCode)||status){
                            //如果入账成功后有需要调账金额去调账
                            /*if (adjustJson.getBooleanValue("status")) {
                                BigDecimal freeAmount = adjustJson.getBigDecimal("freeAmount");
                                BigDecimal preAdjustAmount = adjustJson.getBigDecimal("preAdjustAmount");
                                if (freeAmount.compareTo(BigDecimal.ZERO) != 0 || preAdjustAmount.compareTo(BigDecimal.ZERO) != 0){
                                    String agentPreAdjustUrl = ACCOUNT_SERVICE_URL_VALUE + "/agentAccountController/agentActPreAdjustment.do";
                                    ClientInterface.agentPreAdjustBalance(resultAd.getOneAgentNo(), resultAd.getOneAgentName(), preAdjustAmount, "1", agentPreAdjustUrl);
                                }
                            }*/
                            //更新数据库值
                            ActivityDetail newAd=new ActivityDetail();
                            newAd.setId(resultAd.getId());
                            newAd.setStatus(9);
                            activityDetailDao.updateRewardIsBooked(newAd);
                            msg.put("status", true);
                            msg.put("msg", "奖励入账成功!");
                            return msg;
                        }
                    }else{
                        BigDecimal oldCashBackAmount=BigDecimal.ZERO;
                        for (int i=0;i<cashBackDetailList.size();i++){
                            BigDecimal cashBackAmount=cashBackDetailList.get(i).getCashBackAmount();
                            if(StringUtil.isBlank(cashBackAmount)){
                                log.info("订单id:"+resultAd.getId()+",代理商"+cashBackDetailList.get(i).getAgentNo()+",不满扣开金额为空不入账跳过!");
                                break;
                            }
                            //'奖励入账开关 1-打开, 0-关闭' 不入账，可以直接跳过
                            cashBackDetailList.get(i).setAgentCashBackSwitch(cashBackDetailList.get(i).getFullPrizeSwitch());
                            if("0".equals(cashBackDetailList.get(i).getFullPrizeSwitch())){
                                cashBackDetailList.get(i).setRemark("满奖开关关闭，不执行满奖");
                                activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                                log.info("订单id:"+resultAd.getId()+",代理商"+cashBackDetailList.get(i).getAgentNo()+",满奖开关关闭，不执行满奖!");
                                break;
                            }
                            if(cashBackAmount.compareTo(BigDecimal.ZERO)!=1){
                                status=true;
                                //返现金额为0 改为以入账
                                cashBackDetailList.get(i).setEntryStatus("1");
                                activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                                log.info("订单id:"+resultAd.getId()+",代理商"+cashBackDetailList.get(i).getAgentNo()+",满奖金额为0 改为以入账!");
                                continue;
                            }
                            if(cashBackDetailList.get(i).getAgentLevel().equals("1")) {
                                oldCashBackAmount = cashBackAmount;
                            }
                            if(cashBackAmount.compareTo(oldCashBackAmount)==1){
                                cashBackDetailList.get(i).setRemark("上级倒挂,不需要满奖");
                                activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                                log.info("订单id:"+resultAd.getId()+",代理商"+cashBackDetailList.get(i).getAgentNo()+",上级倒挂入账失败!");
                                break;
                            }else{
                                oldCashBackAmount=cashBackAmount;
                            }
                            //查询预调账
                            adjustBalanceMsg = ClientInterface.findAgentPreAdjustBalance(cashBackDetailList.get(i).getAgentNo(), findAgentPreUrl);
                            adjustJson = JSONObject.parseObject(adjustBalanceMsg);
                            BigDecimal freeAmount = adjustJson.getBigDecimal("freeAmount");
                            BigDecimal preAdjustAmount = adjustJson.getBigDecimal("preAdjustAmount");
                            //取差值
                            if(cashBackDetailList.get(i).getAgentLevel().equals("1")){
                                if((i+1)<cashBackDetailList.size()&&freeAmount.compareTo(BigDecimal.ZERO) == 0 && preAdjustAmount.compareTo(BigDecimal.ZERO) == 0){
                                    BigDecimal cashBackAmount2=cashBackDetailList.get(i+1).getCashBackAmount();
                                    cashBackAmount2=StringUtil.isBlank(cashBackAmount2)?BigDecimal.ZERO:cashBackAmount2;
                                    if((cashBackAmount.subtract(cashBackAmount2)).compareTo(BigDecimal.ZERO)!=-1&&"1".equals(cashBackDetailList.get(i+1).getFullPrizeSwitch())){
                                        cashBackDetailList.get(i).setCashBackAmount((cashBackAmount.subtract(cashBackAmount2)));
                                    }
                                }
                            }else{
                                if((i+1)<cashBackDetailList.size()){
                                    BigDecimal cashBackAmount2=cashBackDetailList.get(i+1).getCashBackAmount();
                                    cashBackAmount2=StringUtil.isBlank(cashBackAmount2)?BigDecimal.ZERO:cashBackAmount2;
                                    if((cashBackAmount.subtract(cashBackAmount2)).compareTo(BigDecimal.ZERO)!=-1&&"1".equals(cashBackDetailList.get(i+1).getFullPrizeSwitch())){
                                        cashBackDetailList.get(i).setCashBackAmount((cashBackAmount.subtract(cashBackAmount2)));
                                    }
                                }
                            }
                            //执行入账
                            String returnMsg = ClientInterface.happyBackDaYuRecordAccount2(ACCOUNT_SERVICE_URL_VALUE,cashBackDetailList.get(i),resultAd.getMerchantNo());
                            Map<String, Object> result = JSON.parseObject(returnMsg);
                            msgCode = result.get("msgCode").toString();
                            if("000000".equals(msgCode)){
                                status=true;
                                cashBackDetailList.get(i).setEntryStatus("1");
                                activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                                //如果入账成功后有需要调账金额去调账
                                if (adjustJson.getBooleanValue("status")) {
                                    if (freeAmount.compareTo(BigDecimal.ZERO) != 0 || preAdjustAmount.compareTo(BigDecimal.ZERO) != 0){
                                        //String agentPreAdjustUrl = ACCOUNT_SERVICE_URL_VALUE + "/agentAccountController/agentActPreAdjustment.do";
                                        //ClientInterface.agentPreAdjustBalance(cashBackDetailList.get(i).getAgentNo(), cashBackDetailList.get(i).getAgentName(), preAdjustAmount, cashBackDetailList.get(i).getAgentLevel(), agentPreAdjustUrl);
                                        if(cashBackDetailList.get(i).getAgentLevel().equals("1")){
                                            cashBackDetailList.get(i).setRemark("预冻结或者预调帐金额不为0，全部入账当前级别！");
                                            cashBackDetailList.get(i).setEntryStatus("1");
                                            activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                                            break;
                                        }else{
                                            cashBackDetailList.get(i).setEntryStatus("1");
                                            activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                                        }
                                    }
                                }
                            }else{
                                cashBackDetailList.get(i).setRemark(result.get("msg").toString());
                                activityDetailDao.updateCashBackDetail(cashBackDetailList.get(i));
                                log.info("订单id:"+resultAd.getId()+",代理商"+cashBackDetailList.get(i).getAgentNo()+",满奖失败!");
                            }
                        }
                        if(status){
                            //更新数据库值
                            ActivityDetail newAd=new ActivityDetail();
                            newAd.setId(resultAd.getId());
                            newAd.setStatus(9);
                            activityDetailDao.updateRewardIsBooked(newAd);
                            msg.put("status", true);
                            msg.put("msg", "奖励入账成功!");
                            return msg;
                        }
                    }
                }catch (Exception e) {
                    log.error("奖励入账异常", e);
                    msg.put("status", false);
                    msg.put("msg", "奖励入账异常!");
                }
            }
        }else{
            log.info("奖励入账失败,请选择满足奖励条件的商户!+id为"+id);
            msg.put("status", false);
            msg.put("msg", "奖励入账失败,请选择满足奖励条件的商户!");
        }
        return msg;
    }

}
