package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MerchantBusinessProduct;
import cn.eeepay.framework.service.MerchantBusinessProductService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.UserCouponService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping(value = "/userCoupon")
public class UserCouponAction {

    @Resource
    private UserCouponService userCouponService;

    @Resource
    private MerchantBusinessProductService merchantBusinessProductService;

    @Resource
    private SysDictService sysDictService;

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @ResponseBody
    @RequestMapping(value = "/userCouponList.do")
    public Map<String,Object> userCouponList(@RequestParam String info,@Param("page") Page<Map<String,Object>> page){
        Map<String,Object> resResult = new HashMap();
        JSONObject jsonObject = JSONObject.parseObject(info);
        Date startTime = jsonObject.getDate("startTime");
        Date endTime = jsonObject.getDate("endTime");
        String coupCode = jsonObject.getString("coupCode");
        Map params = jsonObject;
        params.put("coupCode",coupCode);
        if(startTime!=null){
            params.put("startTime", DateUtil.getFormatDate("yyyy-MM-dd",startTime)+" 00:00:00");
        }
        if(endTime!=null){
            params.put("endTime", DateUtil.getFormatDate("yyyy-MM-dd",endTime)+" 23:59:59");
        }
        String id =jsonObject.getString("merId");

        MerchantBusinessProduct mbp = merchantBusinessProductService.selectByPrimaryKey(Long.valueOf(id));
        String  merchantNo = mbp.getMerchantNo();
        params.put("merchantNo",merchantNo);

        List<Map<String,Object>> coupCodes = userCouponService.coupCode("COUPON_CODE");

        List<Map<String,Object>> embAccount = new ArrayList<>();
        Map<String,Object> supEmbAccount = userCouponService.userCpAcc(merchantNo,new String[]{"1","2","6"});
        supEmbAccount.put("accName","鼓励金");
        Map<String,Object> czEmbAccount = userCouponService.userCpAcc(merchantNo,new String[]{"3"});
        czEmbAccount.put("accName","充值返");
        embAccount.add(supEmbAccount);
        embAccount.add(czEmbAccount);

        userCouponService.userCouponList(params,page);
        resResult.put("page",page);
        resResult.put("embAccount",embAccount);
        resResult.put("coupCodes",coupCodes);
        return resResult;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @ResponseBody
    @RequestMapping(value = "/verificationList.do")
    public Map<String,Object> verificationList(@RequestParam String info,@Param("page") Page<Map<String,Object>> page){

        Map<String,Object> resResult = new HashMap();
        JSONObject jsonObject = JSONObject.parseObject(info);
        String id =jsonObject.getString("merId");
        MerchantBusinessProduct mbp = merchantBusinessProductService.selectByPrimaryKey(Long.valueOf(id));
        String  merchantNo = mbp.getMerchantNo();
        userCouponService.verificationList(merchantNo,page);
        for(Map<String,Object> temp : page.getResult()){
            String couponNos = "";
            String orderNo = StringUtil.filterNull(temp.get("orderNo"));
            String type = StringUtil.filterNull(temp.get("type"));
            List<Map<String,Object>> conpons = userCouponService.couponList(orderNo,type);
            for (Map<String,Object> tem : conpons){
                couponNos+=tem.get("cno");
                couponNos+=",";
            }
            if(couponNos.endsWith(",")){
                couponNos= couponNos.substring(0,couponNos.length()-1);
            }
            temp.put("couponNo",couponNos);
        }
        resResult.put("page",page);
        return resResult;
    }



    }
