//package test;
//
//import cn.eeepay.framework.model.Result;
//import cn.eeepay.framework.util.*;
//import com.alibaba.fastjson.JSONObject;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.reflect.TypeToken;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.ServletContext;
//import javax.servlet.http.HttpServletRequest;
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author tans
// * @date 2018/8/27 16:05
// */
//
//public class YsTest {
//    public static void main(String[] args) {
//        Result result = new Result();
//        try {
//            HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            String url = "https://mertest.ysepay.com/openapi_gateway/gateway.do";
////        String url = "https://openapi.ysepay.com/gateway.do";
//            String userCode = "shanghu_test";
//            String userName = "银盛支付商户测试公司";
//
//            Map<String,String> bizContentMap = new HashMap<String,String>();
//            bizContentMap.put("user_code",userCode);
//            bizContentMap.put("user_name",userName);
//            bizContentMap.put("account_no","0000400009620093");
//            String biz_content = JSONObject.toJSONString(bizContentMap);
//
//            Map<String,String> map = new HashMap<String,String>();
//            map.put("method", "ysepay.online.user.account.get");
//            map.put("partner_id", userCode);
//            map.put("timestamp", DateUtil.getLongCurrentDate());
//            map.put("charset", "GBK");
//            map.put("sign_type", "RSA");
//            map.put("version", "3.0");
//            map.put("biz_content", biz_content);
//
//            Map<String, String> dataMap = YsUtils.buildRequestPara( map, password, certificate);
//
//            String rsStr = ClientInterface.httpPost(url,dataMap);
//
//
//            System.out.println("returnMsg:" + rsStr);
//
//            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//            Map<String, Object> o = gson.fromJson(rsStr, new TypeToken<Map<String, Object>>() {
//            }.getType());
//            Object ysepay_online_user_account_get_response = o.get("ysepay_online_user_account_get_response");
//            Object sign = o.get("sign");
//
//            String ysepay_online_user_account_get_responseStr = gson.toJson(ysepay_online_user_account_get_response);
//            String signStr = gson.toJson(sign);
////            boolean isSign = YsUtils.verifySign(servletRequest, resultMap);
//            ServletContext servletContext = servletRequest.getServletContext();
//            InputStream publicCertFileInputStream = servletContext
//                    .getResourceAsStream("/WEB-INF/classes/ysconfig/businessgate.cer");
//
//            boolean isSign = SignUtils.rsaCheckContent(publicCertFileInputStream,ysepay_online_user_account_get_responseStr
//                    ,signStr,"utf-8");
//
//
//            System.out.println("验签状态：" + isSign);
//
//            if(isSign){
//                result.setStatus(true);
//                result.setMsg("查询成功");
//                result.setData(rsStr);
//            }
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return;
//    }
//}
