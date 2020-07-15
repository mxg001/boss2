package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.UserCouponService;
import cn.eeepay.framework.service.VerificationInfoService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/verificationInfo")
public class VerificationInfoAction {
    private static final Logger log = LoggerFactory.getLogger(VerificationInfoAction.class);


    @Resource
    private VerificationInfoService verificationInfoService;

    @Resource
    private UserCouponService userCouponService;

    @Resource
    private SysDictService sysDictService;

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @ResponseBody
    @RequestMapping("/verificationInfoQuery")
    public Object verificationInfoQuery(@RequestParam String info , @Param("page")Page<Map<String,Object>> page){
        Map<String,Object> res = new HashMap<>();
        try {
            Map<String,String> params = JSONObject.parseObject(info,Map.class);
            verificationInfoService.verificationInfoQuery(params,page);

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
            Map<String, Object> verCount = verificationInfoService.verificationInfoCount(params);
            res.put("page",page);
            res.put("verCount",verCount);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }


    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/verificationInfoExport")
    @ResponseBody
    public void verificationInfoExport(@RequestParam String info , HttpServletResponse response){
       try {
           Map<String,String> params = JSONObject.parseObject(info,Map.class);
           List<Map<String,Object>> listData = verificationInfoService.verificationAllInfoQuery(params);

           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
           String fileName = "券使用记录" + sdf.format(new Date()) + ".xls";
           String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
           response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
           List<Map<String, String>> data = new ArrayList<>();
           SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           Map<String, String> couponCodeMap = sysDictService.selectMapByKey("COUPON_CODE");

           Map<String, String> cancelVerTypeMap = sysDictService.selectMapByKey("CANCEL_VERIFICATION_CODE");//核销方式

           Map<String, String> couponTypeMap=sysDictService.selectMapByKey("COUPON_TYPE");//券类型

           for (Map<String,Object> tempCoup : listData){
               String cou = JSONObject.toJSONString(tempCoup);
               JSONObject temp = JSONObject.parseObject(cou);
               temp.put("getTime",temp.getDate("getTime")==null?"":sdf2.format(temp.getDate("getTime")));
               temp.put("createTime",temp.getDate("createTime")==null?"":sdf2.format(temp.getDate("createTime")));
               Map<String,String> tempData = (Map) temp;
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
               if(StringUtils.isNotBlank(tempData.get("couponCode"))){
                   tempData.put("couponCode", couponCodeMap.get(tempData.get("couponCode")));
               }
               tempData.put("couponType", couponTypeMap.get(tempData.get("couponType")));
               tempData.put("type", cancelVerTypeMap.get(tempData.get("type")));
               data.add(tempData);
           }

           ListDataExcelExport export = new ListDataExcelExport();
           String[] cols = new String[]{"id", "merchantName", "merchantNo", "mobileNo", "agentName", "oneAgentName",
                   "verFee", "couponCode","couponType", "type", "orderNo","transAmount", "couponNo", "getTime", "createTime"};
           String[] colsName = new String[]{"序号", "商户名称", "商户编号", "商户手机号", "直属代理商","一级代理商",
                   "抵扣手续费","活动来源","券类型", "使用说明", "关联交易订单","交易金额", "关联券", "券获取时间","券使用时间"};
           OutputStream ouputStream = response.getOutputStream();
           export.export(cols, colsName, data,ouputStream);
           ouputStream.close();
       }catch (Exception e){
           e.printStackTrace();
       }
    }
}
