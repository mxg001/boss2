package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.RegularTasksDao;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.AgentShareTaskService;
import cn.eeepay.framework.service.OutAccountServiceService;
import cn.eeepay.framework.service.TimeTaskCollectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 定时任务系统调用接口集合
 * @author MXG
 * create 2018/11/06
 */
@Service("timeTaskCollectionService")
public class TimeTaskCollectionServiceImpl implements TimeTaskCollectionService {

//    @Resource
//    private SysDictService sysDictService;
//    @Resource
//    private TransInfoService transInfoService;
//    @Resource
//    private MerchantInfoService merchantInfoService;
//    @Resource
//    private ZqMerchantInfoService zqMerchantInfoService;
//    @Resource
//    private RedisService redisService;
    @Autowired
    private AgentShareTaskService agentShareTaskService;
    @Autowired
    private OutAccountServiceService outAccountServiceService;
//    @Resource
//    private UpdateCheGuanHomeOrderService updateCheGuanHomeOrderService;
//
//    @Resource
//    private TransInfoDao transInfoDao;
//    @Resource
//    private SysDictDao sysDictDao;
//    @Resource
//    private UserCouponDao userCouponDao;
    @Resource
    private RegularTasksDao regularTasksDao;

    private static final Logger log = LoggerFactory.getLogger(TimeTaskCollectionServiceImpl.class);

//    @Override
//    public void createMerAcc() {
//        List<MerchantInfo> merList = merchantInfoService.selectByMerAccount();
//        log.info("===========需要开户数量==="+merList.size());
//        if(merList!=null && merList.size()>0){
//            for (MerchantInfo merchantInfo : merList){
//                String merchantNo = merchantInfo.getMerchantNo();
//                String acc= ClientInterface.createMerchantAccount(merchantNo);
//                log.info("商户批量开户,商户号:{},返回结果:{}", merchantNo,acc);
//                JSONObject returnJson = JSONObject.parseObject(acc);
//                if(returnJson.getBooleanValue("status") || "外部账号已经存在".equals(returnJson.getString("msg"))){
//                    int i = merchantInfoService.updateMerAcoount(merchantNo);
//                    if(i>0){
//                        log.info("开立商户账户成功");
//                    }else{
//                        log.info( "开立商户账户失败");
//                    }
//                }else{
//                    log.info( "开立商户账户失败");
//                }
//            }
//        }
//    }
//
//    @Override
//    public int updateSettleStatus() {
//        int res1 =0;//T0的单转T1结算
//        int res2 =0;//提现失败的单转T1
//        // 如果不是节假日执行
//        if (transInfoDao.getHolidayFlag(new SimpleDateFormat("yyyy-MM-dd").format(new Date())) == null) {
//            // 获取更新参数
//            String channels = sysDictService.getValues("NO_UPDATE_SETTLE_STATUS");
//            String maxdate = sysDictDao.getValueByKey("NO_UPDATE_SETTLE_STATUS_DATA");
//            String currentDate = DateUtil.getCurrentDate();
//            maxdate = DateUtil.getBeforeDate(new Date(), Integer.parseInt(maxdate));
//            log.info("更改交易结算状态，订单时间{}~{}",maxdate, currentDate);
//            // T0的单转T1结算
//            res1=transInfoDao.updateT0SettleStatus(channels,maxdate,currentDate);
//            // 提现失败的单转T1
//            res2=transInfoDao.updateT1SettleStatus(channels,maxdate,currentDate);
//        }else{
//            String transchannels = sysDictService.getValues("HOLIDAYS_UPDATE_SETTLE_STATUS");
//            if(transchannels.length() > 0){
//                String channels = sysDictService.getValues("NO_UPDATE_SETTLE_STATUS");
//                String maxdate = sysDictDao.getValueByKey("NO_UPDATE_SETTLE_STATUS_DATA");
//                String currentDate = DateUtil.getCurrentDate();
//                maxdate = DateUtil.getBeforeDate(new Date(), Integer.parseInt(maxdate));
//                log.info("更改交易结算状态，订单时间{}~{}",maxdate, currentDate);
//                // T0的单转T1结算
//                res1=transInfoDao.updateT0SettleStatus2(channels,maxdate,transchannels,currentDate);
//                // 提现失败的单转T1
//                res2=transInfoDao.updateT1SettleStatus2(channels,maxdate,transchannels,currentDate);
//            }
//        }
//        log.info("更新交易的结算状态,T0转T1条数:{}，提现失败的单转T1:{}", res1, res2);
//        return res1 + res2;
//    }
//
//    @Override
//    public void updateCouponStatus() {
//        log.info("定时更新优惠劵状态,执行时间:" + new Date());
//        userCouponDao.updateCouponStatus();
//        //获取过期充值返数据写入核销表
//        List<UserCoupon> expCoupons = userCouponDao.queryExpCoupons();
//        if(expCoupons != null && expCoupons.size() > 0){
//            String order = DateUtil.getMessageTextTime().toString();
//            for(int i = 0; i < expCoupons.size(); i ++){
//                expCoupons.get(i).setToken("EXP"+order+i);
//                expCoupons.get(i).setCouponStatus("0");
//                expCoupons.get(i).setCancelVerificationType("3");
//            }
//            userCouponDao.batchInsertexpCoupon(expCoupons);
//        }
//        //获取过期的充值返商户、金额、提示消息
//        List<Map<String, Object>> expCoupon = userCouponDao.queryExpCoupon();
//        if(expCoupons != null && expCoupon.size() > 0){
//            for(int i = 0; i < expCoupon.size(); i ++){
//                try {
//                    String point = String.valueOf(new BigDecimal(expCoupon.get(i).get("balance").toString()).multiply(new BigDecimal("100")).intValueExact());
//                    String msm = expCoupon.get(i).get("activity_notice").toString()+"过期"+point+"积分";
//                    List<NameValuePair> list=new ArrayList<NameValuePair>();
//                    list.add(new BasicNameValuePair("title", "充值券过期提醒"));//标题
//                    list.add(new BasicNameValuePair("notice", msm));//消息内容
//                    list.add(new BasicNameValuePair("mer_no", expCoupon.get(i).get("merchant_no").toString()));//商户号
//                    //如果不.returnContent()，request不会释放，顶多100个，超过100个将不会发出请求
//                    Content content = Request.Post(Constants.EXPCOUPON_MSM_URL).body(new UrlEncodedFormEntity(list, "utf-8")).execute().returnContent();
//                    if(content != null){
//                        log.info("returnMsg:{}", content.asString());
//                    }
//                    //successCount++;
//                } catch (Exception e) {
//                    log.error("充值卷过期提醒异常:{}",expCoupon.get(i));
//                    log.error(e.toString());
//                    break;
//                }
//            }
//        }
//    }
//
//
//    @Override
//    public void ysMerAdd() {
//        log.info("=============YS同步商户定时任务开始=========");
//        String paramStr;
//        String merchantNo = null;
//        SysDict sysDict = sysDictService.getByKey("ZFZQ_ACCESS_URL");
//        String accessUrl = sysDict.getSysValue() + "zfMerchant/zfMerUpdate";
//        SysDict type = sysDictService.getByKey("YS_MERADD_TIME");
//        List<ZqMerchantInfo> zqMerList = zqMerchantInfoService.selectYsSyncMer(String.valueOf(type.getSysValue()));
//        if(zqMerList != null && zqMerList.size()>0){
//            for (ZqMerchantInfo zqMerInfo : zqMerList) {
//                try{
//                    merchantNo = zqMerInfo.getMerchantNo();
//                    String bpId = zqMerInfo.getBpId().toString();
//                    Map<String, Object> marMap = new HashMap<String, Object>();
//                    List<String> channelList = new ArrayList<>();
//                    channelList.add("YS_ZQ");
//                    marMap.put("merchantNo", merchantNo);
//                    marMap.put("bpId", bpId);
//                    marMap.put("operator", merchantNo);
//                    marMap.put("changeSettleCard", "0");
//                    marMap.put("channelCode", channelList);
//                    paramStr = JSON.toJSONString(marMap);
//                    new ClientInterface(accessUrl, null).postRequestBody(paramStr);
//                }catch(Exception e){
//                    log.error("YS商户定时任务同步异常,商户号:{}",new Object[]{merchantNo});
//                }
//
//            }
//        }
//    }
//
//    @Override
//    public void reSettle() {
//        try {
//            log.info("未出款/出款失败 重新发起出款, reSettle_start");
//            String last=(String)redisService.select("RE_SETTLE_TASK_LAST");
//            String count=(String)redisService.select("RE_SETTLE_TASK_COUNT");
//            String channelNames = (String) redisService.select("RE_SETTLE_TASK_CHANNELS");
//            String limitNumbersStr = (String) redisService.select("RE_SETTLE_TASK_LIMIT");
//            Integer limitNumbers = 0;
//            if(!StringUtils.isNumeric(last)){
//                last="900";
//            }
//            if(!StringUtils.isNumeric(count)){
//                count="6000";
//            }
//            if(StringUtils.isBlank(channelNames)){
//                channelNames="'YS_ZQ'";
//            }
//            if(!StringUtils.isNumeric(limitNumbersStr)){
//                limitNumbers = 1000;
//            } else {
//                limitNumbers = Integer.parseInt(limitNumbersStr);
//            }
//            Calendar calendar=Calendar.getInstance();
//            calendar.add(Calendar.SECOND,Integer.parseInt(last)*-1);
//            Date endDate = calendar.getTime();
//            calendar.add(Calendar.SECOND,Integer.parseInt(count)*-1);
//            Date startDate = calendar.getTime();
//            List<CollectiveTransOrder> list = transInfoService.getUnSettle(channelNames, startDate, endDate,limitNumbers);
//
//            for (CollectiveTransOrder c:list){
//                String url= Constants.SETTLE_TRANS+"?transferId="+c.getId()+"&userId=reSettle";
//                log.info("再次结算订单reSettle_transferId:{}",c.getId());
//                String result= ClientInterface.baseNoClient(url,null);
//                log.info("再次结算订单[{}]返回信息：{}",new String[]{c.getOrderNo(),result});
//
//                // 异步方案暂时不可取
////                threadpool.execute(new ReSettleRunnable(c.getId(), c.getOrderNo()));
//            }
//            log.info("未出款/出款失败 重新发起出款, reSettle_end,异步处理");
//        }catch (Exception e){
//            log.error("未出款/出款失败 重新发起出款异常",e);
//        }
//    }

    @Override
    public void updateAcqServiceRate() {
        log.info("替换收单服务费率 开始" + new Date());
        List<AcqServiceRateTask> acqServiceRateTasks = regularTasksDao.selectByEffectiveDate();
        if (acqServiceRateTasks == null || acqServiceRateTasks.size() == 0) {
            return;
        }
        List<Integer> sbAsrtIds = new ArrayList<>();
        List<Integer> sbIds = new ArrayList<>();
        for (int i = 0; i < acqServiceRateTasks.size(); i++) {
            sbAsrtIds.add(acqServiceRateTasks.get(i).getAcqServiceRateId());
            sbIds.add(acqServiceRateTasks.get(i).getId());
        }
        List<AcqServiceRate> acqServiceRates = regularTasksDao.selectAsrInfoByIds(sbAsrtIds);

        // 现在两个集合都查出来了，需要交换数据
        if (acqServiceRates == null || acqServiceRates.size() == 0) {
            return;
        }

        regularTasksDao.updateBatchAcqServiceRateByTask(acqServiceRateTasks);
        regularTasksDao.insertBatchAcqServiceRateTaskByRate(acqServiceRates);
        regularTasksDao.deleteBatchAcqServiceRateTaskByIds(sbIds);
        log.info("替换收单服务费率 结束" + new Date());
    }

    @Override
    public void updateAgentShare() {
        log.info("代理商分润任务: " + new Date());
        List<AgentShareRuleTask> list = agentShareTaskService.findByEffective();
        if (list == null || list.size() == 0) {
            return;
        }
        AgentShareRule temp = null;
        List<AgentShareRule> ruleList = new ArrayList<AgentShareRule>();
        List<Integer> taskIdList = new ArrayList<Integer>();
        List<AgentShareRuleTask> tempList = new ArrayList<AgentShareRuleTask>();
        // 放到正在执行的分润表
        // 批量一次300个
        for (int i = 1; i < list.size() + 1; i++) {
            temp = agentShareTaskService.getById(list.get(i - 1).getShareId());
            if (list.get(i - 1).getEfficientDate().getTime() > temp.getEfficientDate().getTime()) {
                ruleList.add(temp);
                taskIdList.add(list.get(i - 1).getId());
                tempList.add(list.get(i - 1));
            }
            if (i % 300 == 0) {
                agentShareTaskService.updateByTaskBatch(tempList);
                agentShareTaskService.updateByRuleBatch(ruleList, taskIdList);
                ruleList.clear();
                taskIdList.clear();
                tempList.clear();
            }
        }
        if(tempList.size() > 0){
            agentShareTaskService.updateByTaskBatch(tempList);
        }
        if(ruleList.size() > 0){
            agentShareTaskService.updateByRuleBatch(ruleList, taskIdList);
        }
        agentShareTaskService.updateProfitUpdateRecordStatus();
    }

    @Override
    public void updateOutAccountService() {
        log.info("出款费率定时任务:" + new Date());
        List<OutAccountServiceRateTask> list = outAccountServiceService.findByEffective();
        if (list == null || list.size() == 0) {
            return;
        }
        OutAccountServiceRate temp = null;

        List<OutAccountServiceRate> rateList = new ArrayList<OutAccountServiceRate>();
        List<Integer> taskIdList = new ArrayList<Integer>();
        // 放到正在执行的分润表
        for (OutAccountServiceRateTask rateTask : list) {
            // 查询原rule
            temp = outAccountServiceService.getById(rateTask.getOutAccountServiceRateId());
            if (rateTask.getEffectiveDate().getTime() > temp.getEffectiveDate().getTime()) {
                rateList.add(temp);
                taskIdList.add(rateTask.getId());
            }
            rateList.add(temp);
            taskIdList.add(rateTask.getId());
        }
        outAccountServiceService.updateByTaskBatch(list);
        outAccountServiceService.updateByRateBatch(rateList, taskIdList);
    }

//    @Override
//    public void updateCarManagerStatus() {
//        try {
//            log.info("修改车管家订单结算状态后台线程 启动:----------->");
//            int count=updateCheGuanHomeOrderService.updateCheGuanHomeOrder();
//            if(count>0) {
//                log.info("修改车管家订单结算状态后台线程:本次共更新了 " + count + " 条记录!");
//            }
//            log.info("修改车管家订单结算状态后台线程 结束!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("修改车管家订单结算状态后台线程 异常: "+e.getMessage());
//        }
//    }
}
