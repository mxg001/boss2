package cn.eeepay.framework.service.unTransactionalImpl;

import cn.eeepay.framework.dao.HappyBackActivityMerchantDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.model.HappyBackActivityMerchant;
import cn.eeepay.framework.model.happyBack.FilterDate;
import cn.eeepay.framework.model.happyBack.FilterPage;
import cn.eeepay.framework.service.HlfActivityMerchantJobService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service("hlfActivityMerchantJobService")
public class HlfActivityMerchantJobServiceImpl implements HlfActivityMerchantJobService {

    private static final Logger log = LoggerFactory.getLogger(HlfActivityMerchantJobServiceImpl.class);
    @Resource
    private HappyBackActivityMerchantDao happyBackActivityMerchantDao;
    @Resource
    private SysDictService sysDictService;
    @Resource
    private SysDictDao sysDictDao;

    @Override
    public void hlfActivityMerchantJob() {
        //加载数据字典配置值
        int dateTerm=Integer.parseInt(sysDictService.getValueByKey("happyBackTermDay"));
        int dateDedTerm=Integer.parseInt(sysDictService.getValueByKey("happyBackTermDedDay"));
        final int switchTerm=Integer.parseInt(sysDictService.getValueByKey("happyBackSwitch"));

        final CountDownLatch end = new CountDownLatch(2);//开启计数器

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

        //查询满奖
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("活跃商户活动定时统计,满奖累计执行开始.....!");
                    FilterDate adCon=new FilterDate();
                    adCon.setEndDate(theOtherDay.getTime());
                    sumReward(adCon,yesterday,switchTerm);//调用统计逻辑
                    log.info("活跃商户活动定时统计,满奖累计执行结束.....!");
                }catch (Exception e){
                    log.error("活跃商户活动定时统计,满奖累计异常",e);
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
                    log.info("活跃商户活动定时统计,扣减逻辑执行开始.....!");
                    FilterDate adCon=new FilterDate();
                    adCon.setStartDate(theOtherDedDay.getTime());
                    adCon.setEndDate(yesterday.getTime());
                    sumDeduction(adCon);
                    log.info("活跃商户活动定时统计,扣减逻辑执行结束.....!");
                }catch (Exception e){
                    log.error("活跃商户活动定时统计,扣减逻辑异常",e);
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
            log.info("活跃商户活动定时统计-调用接口返回定时任务系统!");
        }catch (Exception e){
            log.error("活跃商户活动定时统计,等待异常!",e);
        }
    }

    /**
     * 奖励统计
     */
    private void sumReward(FilterDate adCon,Calendar yesterday,int switchTerm){
        String ACCOUNT_SERVICE_URL_VALUE = sysDictDao.getValueByKey("ACCOUNT_SERVICE_URL");
        FilterPage page=new FilterPage();
        while(true){
            List<HappyBackActivityMerchant> list=happyBackActivityMerchantDao.happyBackActivityMerchantRewardPage(adCon,page);
            if(list==null||list.size()<=0){//已经没有数据了
                break;
            }
            for(HappyBackActivityMerchant hbMer:list){
                //根据开关,达标是否再次统计 o统计1统计
                if(0==switchTerm){
                    if("1".equals(hbMer.getTargetStatus())){
                        log.info("活跃商户活动定时统计,MerchantNo:{},已达标不继续统计",hbMer.getMerchantNo());
                        continue;
                    }
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String rewardStartTime=sdf.format(Timestamp.valueOf(hbMer.getRewardStartTime()));
                String rewardEndTime=sdf.format(Timestamp.valueOf(hbMer.getRewardEndTime()));
                String yesterdayStr=sdf.format(yesterday.getTime());
                List<Map<String,String>> dateMap = getKeyValueForDate(rewardStartTime,rewardEndTime);
                log.info("昨天yesterdayStr:{},奖励时间段dateMap:{}",yesterdayStr,dateMap);
                int num=0;
                boolean isReward=false;
                BigDecimal totalSum=BigDecimal.ZERO;
                for(Map<String,String> date : dateMap){
                    date.put("merchant_no",hbMer.getMerchantNo());
                    Map sumMap=happyBackActivityMerchantDao.happyBackMerTransMonthSum(date);
                    BigDecimal total = BigDecimal.ZERO;
                    if(sumMap!=null&&StringUtil.isNotBlank(sumMap.get("total"))) {
                        total=new BigDecimal(sumMap.get("total").toString());
                    }
                    totalSum=totalSum.add(total);
                    if(total.compareTo(hbMer.getRewardTotalAmount())!=-1) {
                        isReward=true;
                        num++;
                    }
                }
                hbMer.setTotalAmount(totalSum);
                //判断每个月是否达标
                if(hbMer.getRewardType()==0){
                    if(num!=hbMer.getRewardMonth()){
                        isReward=false;
                    }
                }
                if(isReward){
                    judgeStandard(hbMer,ACCOUNT_SERVICE_URL_VALUE);
                }
                log.info("------------------活跃商户活动更新累计金额和状态");
                happyBackActivityMerchantDao.updateHappyBackActivityMerchantOrderReward(hbMer);
            }
            page.setPage(page.getPage()+1);//页数加1
        }
    }

    //判断是否达标
    private void judgeStandard(HappyBackActivityMerchant hbMer,String url){
        if(hbMer.getRewardAccountStatus()!=null&&hbMer.getRewardAccountStatus().equals("1")){
            return;
        }
        Date date=new Date();
        //已达标
        if(!hbMer.getTargetStatus().equals("1")){
            hbMer.setTargetStatus("1");
            hbMer.setRewardAccountStatus("0");
            hbMer.setTargetTime(date);
        }
        //调用满奖
        String returnMsg = happyBackSubsidyRecordAccount(url,hbMer);
        Map<String, Object> result = JSON.parseObject(returnMsg);
        if (result != null) {
            boolean status = Boolean.valueOf(result.get("status").toString());
            if(status){
                //已奖励
                hbMer.setRewardAmount(hbMer.getRewardAmountConfig());
                hbMer.setRewardAccountStatus("1");
                hbMer.setRewardAccountTime(date);
            }
        }
    }

    /**
     * 扣减统计
     */
    private void sumDeduction(FilterDate adCon){
        String ACCOUNT_SERVICE_URL_VALUE = sysDictDao.getValueByKey("ACCOUNT_SERVICE_URL");
        List<HappyBackActivityMerchant> list=happyBackActivityMerchantDao.happyBackActivityMerchantDeductPage(adCon);
        for(HappyBackActivityMerchant hbMer:list) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String deductStartTime = sdf.format(Timestamp.valueOf(hbMer.getDeductStartTime()));
            String deductEndTime = sdf.format(Timestamp.valueOf(hbMer.getDeductEndTime()));
            String yesterdayStr = sdf.format(adCon.getEndDate());
            List<Map<String, String>> dateMap=getKeyValueForDate(deductStartTime, deductEndTime);
            int num = 0;
            boolean isDeduct = false;
            log.info("昨天yesterdayStr:{},扣减时间段dateMap:{}",yesterdayStr,dateMap);
            for (int i=0;i<dateMap.size();i++){
                Map<String, String> date=dateMap.get(i);
                date.put("merchant_no", hbMer.getMerchantNo());
                Map sumMap = happyBackActivityMerchantDao.happyBackMerTransMonthSum(date);
                BigDecimal total = BigDecimal.ZERO;
                if (sumMap!=null&&StringUtil.isNotBlank(sumMap.get("total"))) {
                    total = new BigDecimal(sumMap.get("total").toString());
                }
                if (total.compareTo(hbMer.getDeductTotalAmount()) == -1) {
                    if(hbMer.getDeductType()==0){
                        if(i!=(dateMap.size()-1)){
                            isDeduct = true;
                            num++;
                        }else if(deductEndTime.compareTo(yesterdayStr)<=0){
                            isDeduct = true;
                            num++;
                        }
                    }else {
                        if (dateMap.get(i).get("endDate").compareTo(yesterdayStr)<=0) {
                            isDeduct = true;
                        }
                    }
                }
            }
            //判断每个月是否达标
            if (hbMer.getDeductType() == 0) {
                if (deductEndTime.compareTo(yesterdayStr)!=1) {
                    if (num != hbMer.getDeductMonth()) {
                        isDeduct = false;
                    }
                } else {
                    isDeduct = false;
                }
            }
            if (isDeduct) {
                judgeDeduction(hbMer, ACCOUNT_SERVICE_URL_VALUE);
            }
            log.info("------------------活跃商户活动更新累计金额和状态");
            happyBackActivityMerchantDao.updateHappyBackActivityMerchantOrderDeduct(hbMer);
        }
    }

    //判断是扣减
    private void judgeDeduction(HappyBackActivityMerchant hbMer, String url){
        Date date=new Date();
        hbMer.setDeductAmount(hbMer.getDeductAmountConfig());
        hbMer.setDeductStatus("3");
        //调用扣减
        String returnMsg = happyBackNoStandardRecordAccount(url,hbMer);
        Map<String, Object> result = JSON.parseObject(returnMsg);
        if (result != null) {
            String msgCode = result.get("msgCode").toString();
            if("000000".equals(msgCode)){
                //已扣减
                hbMer.setDeductStatus("1");
                hbMer.setDeductTime(date);
            }else if("000002".equals(msgCode)){
                //发起预调账
                hbMer.setDeductStatus("2");
                hbMer.setDeductTime(date);
            }else{
                hbMer.setDeductStatus("3");
            }
        }else{
            hbMer.setDeductStatus("3");
        }
    }


    /**
     * 活跃商户活动-奖励入账(满奖)
     */
    public static String happyBackSubsidyRecordAccount(String url, HappyBackActivityMerchant info) {
        url += "/happyBackController/happyBackSubsidyRecordAccount.do";
        final HashMap<String, Object> claims = new HashMap<String, Object>();
        String response = "";
        claims.put("transOrderNo", info.getActiveOrder());// 当前订单编号
        claims.put("merchantNo", info.getMerchantNo()); //当前商户编号
        claims.put("agentNo", info.getOneAgentNo()); //当前代理商23223
        claims.put("subsidyAmount", String.valueOf(info.getRewardAmountConfig())); //入账金额
        claims.put("fromSerialNo", "HYSHJ-"+String.valueOf(info.getId())); //来源系统流水号，每笔唯一，发起端需保存
        claims.put("fromSystem", "boss"); //来源系统固定
        claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date())); //交易时间
        claims.put("transTypeCode", "000124"); //交易码固定

        log.info("活跃商户活动-奖励入账,url:{},HappyBackActivityMerchant:{}", url,info);
        response = ClientInterface.baseClient(url, claims);
        log.info("活跃商户活动-奖励入账，returnMsg:{}", response);
        return response;
    }

    /**
     * 活跃商户活动-奖励入账(扣减)
     */
    public static String happyBackNoStandardRecordAccount(String url, HappyBackActivityMerchant info) {
        url += "/happyBackController/happyBackNoStandardRecordAccount.do";
        final HashMap<String, Object> claims = new HashMap<String, Object>();
        String response = "";
        claims.put("transOrderNo", info.getActiveOrder());// 当前订单编号
        claims.put("merchantNo", info.getMerchantNo()); //当前商户号
        claims.put("agentNo", info.getOneAgentNo()); //当前代理商23223
        claims.put("agentName", info.getOneAgentName()); //当前代理商23223
        claims.put("subsidyAmount", String.valueOf(info.getDeductAmountConfig())); //入账金额
        claims.put("fromSerialNo", "HYSHK-"+String.valueOf(info.getId())); //来源系统流水号，每笔唯一，发起端需保存
        claims.put("fromSystem", "boss"); //来源系统固定
        claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date())); //交易时间
        claims.put("transTypeCode", "000125"); //交易码固定

        log.info("活跃商户活动-扣减,url:{},HappyBackActivityMerchant:{}", url,info);
        response = ClientInterface.baseClient(url, claims);
        log.info("活跃商户活动-扣减，returnMsg:{}", response);
        return response;
    }

    /**
     * 根据一段时间区间，按月份拆分成多个时间段
     * @param startDate 开始日期
     * @param endDate  结束日期
     * @return
     */
    public static List<Map<String,String>> getKeyValueForDate(String startDate,String endDate) {
        List<Map<String,String>> list = null;
        try {
            list = new ArrayList<Map<String,String>>();

            String firstDay = "";
            String lastDay = "";
            Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);// 定义起始日期

            Date d2 = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);// 定义结束日期

            Calendar dd = Calendar.getInstance();// 定义日期实例
            dd.setTime(d1);// 设置日期起始时间
            Calendar cale = Calendar.getInstance();

            Calendar c = Calendar.getInstance();
            c.setTime(d2);

            int startDay = d1.getDate();
            int endDay = d2.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Map<String,String> keyValueForDate = null;

            while (dd.getTime().before(d2)) {// 判断是否到结束日期
                keyValueForDate = new HashMap<String,String>();
                cale.setTime(dd.getTime());

                if(dd.getTime().equals(d1)){
                    cale.set(Calendar.DAY_OF_MONTH, dd
                            .getActualMaximum(Calendar.DAY_OF_MONTH));
                    lastDay = sdf.format(cale.getTime());
                    keyValueForDate.put("startDate",sdf.format(d1));
                    keyValueForDate.put("endDate",lastDay);
                }else if(dd.get(Calendar.MONTH) == d2.getMonth() && dd.get(Calendar.YEAR) == c.get(Calendar.YEAR)){
                    cale.set(Calendar.DAY_OF_MONTH,1);//取第一天
                    firstDay = sdf.format(cale.getTime());

                    keyValueForDate.put("startDate",firstDay);
                    keyValueForDate.put("endDate",sdf.format(d2));
                }else {
                    cale.set(Calendar.DAY_OF_MONTH,1);//取第一天
                    firstDay = sdf.format(cale.getTime());

                    cale.set(Calendar.DAY_OF_MONTH, dd
                            .getActualMaximum(Calendar.DAY_OF_MONTH));
                    lastDay = sdf.format(cale.getTime());
                    keyValueForDate.put("startDate",firstDay);
                    keyValueForDate.put("endDate",lastDay);
                }
                list.add(keyValueForDate);
                dd.add(Calendar.MONTH, 1);// 进行当前日期月份加1

            }

            if(endDay<startDay){
                keyValueForDate = new HashMap<String,String>();
                cale.setTime(d2);
                cale.set(Calendar.DAY_OF_MONTH,1);//取第一天
                firstDay = sdf.format(cale.getTime());

                keyValueForDate.put("startDate",firstDay);
                keyValueForDate.put("endDate",sdf.format(d2));

                list.add(keyValueForDate);
            }
        } catch (Exception e) {
            return null;
        }

        return list;
    }

    @Override
    public Map<String,Object> merRewardAccountStatus(String ids){
        log.info("------------------活跃商户活动手动批量入账开始");
        Map<String,Object> msg=new HashMap<>();
        String ACCOUNT_SERVICE_URL_VALUE = sysDictDao.getValueByKey("ACCOUNT_SERVICE_URL");
        List<HappyBackActivityMerchant> list =happyBackActivityMerchantDao.selectMerRewardAccountStatusByIds(ids);
        if(list.size()==0){
            msg.put("msg","没有符合奖励入账的数据" );
            msg.put("status", false);
        }else{
            for(HappyBackActivityMerchant hbMer:list){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String rewardStartTime=sdf.format(Timestamp.valueOf(hbMer.getRewardStartTime()));
                String rewardEndTime=sdf.format(Timestamp.valueOf(hbMer.getRewardEndTime()));
                List<Map<String,String>> dateMap = getKeyValueForDate(rewardStartTime,rewardEndTime);
                int num=0;
                boolean isReward=false;
                BigDecimal totalSum=BigDecimal.ZERO;
                for(Map<String,String> date : dateMap){
                    date.put("merchant_no",hbMer.getMerchantNo());
                    Map sumMap=happyBackActivityMerchantDao.happyBackMerTransMonthSum(date);
                    if(sumMap!=null&& StringUtil.isNotBlank(sumMap.get("total"))) {
                        BigDecimal total=new BigDecimal(sumMap.get("total").toString());
                        totalSum=totalSum.add(total);
                        if(total.compareTo(hbMer.getRewardTotalAmount())!=-1) {
                            isReward=true;
                            num++;
                        }
                    }
                }
                hbMer.setTotalAmount(totalSum);
                //判断每个月是否达标
                if(hbMer.getRewardType()==0){
                    if(num!=hbMer.getRewardMonth()){
                        isReward=false;
                    }
                }
                if(isReward){
                    log.info("------------------活跃商户活动手动批量入账,merchant_no:{}",hbMer.getMerchantNo());
                    judgeStandard(hbMer,ACCOUNT_SERVICE_URL_VALUE);
                }
                happyBackActivityMerchantDao.updateHappyBackActivityMerchantOrderReward(hbMer);
            }
            log.info("------------------活跃商户活动手动批量入账结束");
            msg.put("msg","奖励入账操作完成" );
            msg.put("status", true);
        }
        return msg;
    }

}
