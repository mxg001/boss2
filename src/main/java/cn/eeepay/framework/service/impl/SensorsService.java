package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.UserCoupon;
import cn.eeepay.framework.service.BusinessProductDefineService;
import cn.eeepay.framework.service.MerchantBusinessProductService;
import cn.eeepay.framework.util.SensorsData;
import cn.eeepay.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service("sensorsService")
public class SensorsService {

    private static final Logger log = LoggerFactory.getLogger(SensorsService.class);

    // 商户业务产品
    @Resource
    private MerchantBusinessProductService merchantBusinessProductService;
    // 业务产品（服务套餐）定义
    @Resource
    private BusinessProductDefineService businessProductDefineService;

    public void buyCouponDetail(Map couponMap, String couponNo, String merchantNo, Object couponStartDate, Object endDate, String couponCode){
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>调用神策");
        BigDecimal couponAmount = new BigDecimal(StringUtil.isBlank(couponMap.get("coupon_amount"))?"0":couponMap.get("coupon_amount").toString());
        BigDecimal giftAmount = new BigDecimal(StringUtil.isBlank(couponMap.get("gift_amount"))?"0":couponMap.get("gift_amount").toString());
        BigDecimal faceValue = new BigDecimal(StringUtil.isBlank(couponMap.get("face_value"))?"0":couponMap.get("face_value").toString());
        Map scMap=new HashMap();
        scMap.put("coupon_no",couponNo);
        scMap.put("merchant_no",merchantNo);
        scMap.put("get_time",couponStartDate);
        scMap.put("expried_time",endDate);
        scMap.put("buy_amount",couponAmount);
        scMap.put("donate_amount",giftAmount);
        scMap.put("total_amount",faceValue);
        if("3".equals(couponCode)) {
            scMap.put("coupon_type","超级积分");
        }else {
            scMap.put("coupon_type","鼓励金");
        }
        scMap.put("get_Coupon_type","手工赠券");
        SensorsData.builder(SensorsData.Type.track, "buy_CouponDetail",merchantBusinessProductService.getUserIdByMerchantInfo(merchantNo)).setProperties(scMap).flush();
    }

    public void checkUser(MerchantInfo merchantInfo,String bpId,String username,String opinion,boolean isResult){
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>调用神策");
        Map scMap=new HashMap();
        scMap.put("merchant_no",merchantInfo.getMerchantNo());
        scMap.put("check_method","人工审核");
        scMap.put("check_channel","人工");
        scMap.put("province",merchantInfo.getProvince());
        scMap.put("city",merchantInfo.getCity());
        scMap.put("user_business_product", businessProductDefineService.selectBybpId(bpId).getBpName());
        scMap.put("operater", username);
        if(isResult){
            scMap.put("check_result","成功");
            scMap.put("check_reasons",opinion);
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>调用神策 审核成功");
        }else{
            scMap.put("check_result","失败");
            scMap.put("check_reasons",opinion);//失败原因
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>调用神策 审核失败");
        }
        SensorsData.builder(SensorsData.Type.track, "check_user",merchantBusinessProductService.getUserIdByMerchantInfo(merchantInfo.getMerchantNo())).setProperties(scMap).flush();
    }

    public void invalidCoupon(UserCoupon userCoupon,Map couponCodeMap){
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>调用神策");
        Map scMap=new HashMap();
        scMap.put("coupon_no",userCoupon.getCouponNo());
        scMap.put("merchant_no",userCoupon.getMerchantNo());
        scMap.put("total_amount",userCoupon.getFaceValue());
        scMap.put("donate_amount",userCoupon.getGiftAmount());
        scMap.put("expired_Coupon_amount",userCoupon.getBalance());
        scMap.put("used_Coupon_amount",userCoupon.getFaceValue().subtract(userCoupon.getBalance()));
        scMap.put("get_Coupon_type",couponCodeMap.get(userCoupon.getCouponCode()));
        if(userCoupon.getCouponCode().equals("3")){
            scMap.put("coupon_type","超级积分");
        }else{
            scMap.put("coupon_type","鼓励金");
        }
        scMap.put("get_Coupon_time",userCoupon.getEndTime());
        SensorsData.builder(SensorsData.Type.track, "Invalid_Coupon",merchantBusinessProductService.getUserIdByMerchantInfo(userCoupon.getMerchantNo())).setProperties(scMap).flush();
    }

}
