package cn.eeepay.framework.service.unTransactionalImpl;

import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.dao.TransInfoDao;
import cn.eeepay.framework.dao.UserCouponDao;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.UserCoupon;
import cn.eeepay.framework.model.ZqMerchantInfo;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.service.impl.SensorsService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author tans
 * @date 2019/4/12 10:33
 */
@Service("taskService")
public class TaskServiceImpl implements TaskService {

    @Resource
    private SysDictService sysDictService;
    @Resource
    private TransInfoService transInfoService;
    @Resource
    private MerchantInfoService merchantInfoService;
    @Resource
    private ZqMerchantInfoService zqMerchantInfoService;
    @Resource
    private RedisService redisService;
    @Resource
    private UpdateCheGuanHomeOrderService updateCheGuanHomeOrderService;

    @Resource
    private TransInfoDao transInfoDao;
    @Resource
    private SysDictDao sysDictDao;
    @Resource
    private UserCouponDao userCouponDao;
    @Resource
    private SensorsService sensorsService;
    // 商户业务产品
    @Resource
    private MerchantBusinessProductService merchantBusinessProductService;

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Override
    public void createMerAcc() {
        List<MerchantInfo> merList = merchantInfoService.selectByMerAccount();
        log.info("===========需要开户数量==="+merList.size());
        if(merList!=null && merList.size()>0){
            for (MerchantInfo merchantInfo : merList){
                String merchantNo = merchantInfo.getMerchantNo();
                String acc= ClientInterface.createMerchantAccount(merchantNo);
                log.info("商户批量开户,商户号:{},返回结果:{}", merchantNo,acc);
                JSONObject returnJson = JSONObject.parseObject(acc);
                if(returnJson.getBooleanValue("status") || "外部账号已经存在".equals(returnJson.getString("msg"))){
                    int i = merchantInfoService.updateMerAcoount(merchantNo);
                    if(i>0){
                        log.info("开立商户账户成功");
                    }else{
                        log.info( "开立商户账户失败");
                    }
                }else{
                    log.info( "开立商户账户失败");
                }
            }
        }
    }

    @Override
    public int updateSettleStatus() {
        int res1 =0;//T0的单转T1结算
//        int res2 =0;//提现失败的单转T1
        List<String> orderList = new ArrayList<>();
        // 如果不是节假日执行
        if (transInfoDao.getHolidayFlag(new SimpleDateFormat("yyyy-MM-dd").format(new Date())) == null) {
            // 获取更新参数
            String channels = sysDictService.getValues("NO_UPDATE_SETTLE_STATUS");
            String maxdate = sysDictDao.getValueByKey("NO_UPDATE_SETTLE_STATUS_DATA");
            String currentDate = DateUtil.getCurrentDate();
            maxdate = DateUtil.getBeforeDate(new Date(), Integer.parseInt(maxdate));
            log.info("更改交易结算状态，订单时间{}~{}",maxdate, currentDate);
            //先找出订单号，再更新
            orderList = transInfoDao.selectT0SettleOrderList(channels,maxdate,currentDate);
            List<String> t1OrderNoList = transInfoDao.selectT1SettleOrderList(channels,maxdate,currentDate);
            orderList.addAll(t1OrderNoList);
            // T0的单转T1结算
//            res1=transInfoDao.updateT0SettleStatus(channels,maxdate,currentDate);
            // 提现失败的单转T1
//            res2=transInfoDao.updateT1SettleStatus(channels,maxdate,currentDate);
        }else{
            String transchannels = sysDictService.getValues("HOLIDAYS_UPDATE_SETTLE_STATUS");
            if(transchannels.length() > 0){
                String channels = sysDictService.getValues("NO_UPDATE_SETTLE_STATUS");
                String maxdate = sysDictDao.getValueByKey("NO_UPDATE_SETTLE_STATUS_DATA");
                String currentDate = DateUtil.getCurrentDate();
                maxdate = DateUtil.getBeforeDate(new Date(), Integer.parseInt(maxdate));
                log.info("更改交易结算状态，订单时间{}~{}",maxdate, currentDate);
                //先查出订单号，再更新
                orderList = transInfoDao.selectT0HolidaySettleOrderList(channels,maxdate,transchannels,currentDate);
                List<String> t1OrderNoList = transInfoDao.selectT1HolidaySettleOrderList(channels,maxdate,transchannels,currentDate);
                orderList.addAll(t1OrderNoList);
                // T0的单转T1结算
//                res1=transInfoDao.updateT0SettleStatus2(channels,maxdate,transchannels,currentDate);
//                // 提现失败的单转T1
//                res2=transInfoDao.updateT1SettleStatus2(channels,maxdate,transchannels,currentDate);
            }
        }
        res1 = updateSettleBatch(orderList);
        log.info("更新交易的结算状态,条数:{}", res1);
        return res1;
    }

    private int updateSettleBatch(List<String> t0OrderNoList) {
        int num = 0;
        if(t0OrderNoList == null || t0OrderNoList.isEmpty()){
            return num;
        }
        List<String> list = new ArrayList<>();
        for(int i = 0; i < t0OrderNoList.size(); i++){
            if(i == 0 || i % 300 > 0){
                list.add(t0OrderNoList.get(i));
            }
            if(list.size() % 300 == 0){
                num += transInfoDao.updateSettle(list);
                list.clear();
            }
        }
        if(list != null && list.size() > 0){
            num += transInfoDao.updateSettle(list);
        }
        return num;
    }

    @Override
    public void updateCouponStatus() {
        log.info("定时更新优惠劵状态,执行时间:" + new Date());
        userCouponDao.updateCouponStatus();
        Map couponCodeMap=sysDictService.selectMapByKey("COUPON_CODE");
        //获取过期充值返数据写入核销表
        List<UserCoupon> expCoupons = userCouponDao.queryExpCoupons();
        List<UserCoupon> insertCoupons=new ArrayList<UserCoupon>();
        if(expCoupons != null && expCoupons.size() > 0){
            String order = DateUtil.getMessageTextTime().toString();
            for(int i = 0; i < expCoupons.size(); i ++){

                if(expCoupons.get(i).getCouponCode().equals("3")||expCoupons.get(i).getCouponCode().equals("6")){
                    UserCoupon userCoupon=expCoupons.get(i);
                    userCoupon.setToken("EXP"+order+i);
                    userCoupon.setCouponStatus("0");
                    userCoupon.setCancelVerificationType("3");
                    insertCoupons.add(userCoupon);
                }

                sensorsService.invalidCoupon(expCoupons.get(i),couponCodeMap);
            }
            if(insertCoupons != null && insertCoupons.size() > 0){
                userCouponDao.batchInsertexpCoupon(insertCoupons);
            }
        }
        //获取过期的充值返商户、金额、提示消息
        List<Map<String, Object>> expCoupon = userCouponDao.queryExpCoupon();
        if(expCoupons != null && expCoupon.size() > 0){
            for(int i = 0; i < expCoupon.size(); i ++){
                try {
                    String point = String.valueOf(new BigDecimal(expCoupon.get(i).get("balance").toString()).multiply(new BigDecimal("100")).intValueExact());
                    String msm = expCoupon.get(i).get("activity_notice").toString()+"过期"+point+"积分";
                    List<NameValuePair> list=new ArrayList<NameValuePair>();
                    list.add(new BasicNameValuePair("title", "充值券过期提醒"));//标题
                    list.add(new BasicNameValuePair("notice", msm));//消息内容
                    list.add(new BasicNameValuePair("mer_no", expCoupon.get(i).get("merchant_no").toString()));//商户号
                    //如果不.returnContent()，request不会释放，顶多100个，超过100个将不会发出请求
                    Content content = Request.Post(Constants.EXPCOUPON_MSM_URL).body(new UrlEncodedFormEntity(list, "utf-8")).execute().returnContent();
                    if(content != null){
                        log.info("returnMsg:{}", content.asString());
                    }
                    //successCount++;
                } catch (Exception e) {
                    log.error("充值卷过期提醒异常:{}",expCoupon.get(i),e);
                    break;
                }
            }
        }
    }


    @Override
    public void ysMerAdd() {
        log.info("=============YS同步商户定时任务开始=========");
        String paramStr;
        String merchantNo = null;
        SysDict sysDict = sysDictService.getByKey("ZFZQ_ACCESS_URL");
        String accessUrl = sysDict.getSysValue() + "zfMerchant/zfMerUpdate";
        SysDict type = sysDictService.getByKey("YS_MERADD_TIME");
        List<ZqMerchantInfo> zqMerList = zqMerchantInfoService.selectYsSyncMer(String.valueOf(type.getSysValue()));
        if(zqMerList != null && zqMerList.size()>0){
            for (ZqMerchantInfo zqMerInfo : zqMerList) {
                try{
                    merchantNo = zqMerInfo.getMerchantNo();
                    String bpId = zqMerInfo.getBpId().toString();
                    Map<String, Object> marMap = new HashMap<String, Object>();
                    List<String> channelList = new ArrayList<>();
                    channelList.add("YS_ZQ");
                    marMap.put("merchantNo", merchantNo);
                    marMap.put("bpId", bpId);
                    marMap.put("operator", merchantNo);
                    marMap.put("changeSettleCard", "0");
                    marMap.put("channelCode", channelList);
                    paramStr = JSON.toJSONString(marMap);
                    new ClientInterface(accessUrl, null).postRequestBody(paramStr);
                }catch(Exception e){
                    log.error("YS商户定时任务同步异常,商户号:{}",new Object[]{merchantNo});
                }

            }
        }
    }

    @Override
    public void updateCarManagerStatus() {
        try {
            log.info("修改车管家订单结算状态后台线程 启动:----------->");
            int count=updateCheGuanHomeOrderService.updateCheGuanHomeOrder();
            if(count>0) {
                log.info("修改车管家订单结算状态后台线程:本次共更新了 " + count + " 条记录!");
            }
            log.info("修改车管家订单结算状态后台线程 结束!");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("修改车管家订单结算状态后台线程 异常: "+e.getMessage());
        }
    }
}
