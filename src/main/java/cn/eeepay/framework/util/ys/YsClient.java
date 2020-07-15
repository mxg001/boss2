package cn.eeepay.framework.util.ys;

import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tans
 * @date 2018/8/31 10:28
 */
public class YsClient {

    private static final Logger log = LoggerFactory.getLogger(YsClient.class);

    /**
     * 查询纯代付资金账户信息
     * 1.通过证书拿到密钥
     * 2.加密完，调接口
     * 3.验签
     * 4.若验签通过，拿到账户金额
     * @return
     */
    public static Result pureAcctInfo(String userCode){
        Result result = new Result();
        try{
            String url = YsConstant.url + YsConstant.interfaceName;
            String certificate = "";//证书地址
            String password = "";//证书密码
            String account_no = "";//代付账号
            String userName = "";//公司名称
            if(userCode.equals(YsConstant.qhyl)){
                userName = YsConstant.qhyl_user_name;
                password = YsConstant.qhyl_password;
                certificate = YsConstant.qhyl_certificate;
                account_no = YsConstant.qhyl_account_no;
            } else if(userCode.equals(YsConstant.mfjf)){
                userName = YsConstant.mfjf_user_name;
                password = YsConstant.mfjf_password;
                certificate = YsConstant.mfjf_certificate;
                account_no = YsConstant.mfjf_account_no;
            } else if(userCode.equals(YsConstant.mfjf002)){
                userName = YsConstant.mfjf002_user_name;
                password = YsConstant.mfjf002_password;
                certificate = YsConstant.mfjf002_certificate;
                account_no = YsConstant.mfjf002_account_no;
            }else if(userCode.equals(YsConstant.shanghu_test)){
                userName = YsConstant.shanghu_test_user_name;
                password = YsConstant.shanghu_test_password;
                certificate = YsConstant.shanghu_test_certificate;
                account_no = YsConstant.shanghu_test_account_no;
            }else if(userCode.equals(YsConstant.paishuju)){
                userName = YsConstant.paishuju_user_name;
                password = YsConstant.paishuju_password;
                certificate = YsConstant.paishuju_certificate;
                account_no = YsConstant.paishuju_account_no;
            } else {
                result.setMsg("user_code参数异常");
                return result;
            }
            Map<String,String> bizContentMap = new HashMap<String,String>();
            bizContentMap.put("user_code",userCode);
            bizContentMap.put("user_name",userName);
            bizContentMap.put("account_no",account_no);
            String biz_content = JSONObject.toJSONString(bizContentMap);

            Map<String,String> map = new HashMap<String,String>();
            String method = YsConstant.method;
            String charset = YsConstant.charset;
            String version = YsConstant.version;
            String sign_type = YsConstant.sign_type;
            map.put("method", method);
            map.put("partner_id", userCode);
            map.put("timestamp", DateUtil.getLongCurrentDate());
            map.put("charset", charset);
            map.put("version", version);
            map.put("sign_type", sign_type);
            map.put("biz_content", biz_content);

            //加密数据
//            log.info("证书地址:{}，password:{}",certificate,password);
            Map<String, String> dataMap = YsUtils.buildRequestPara( map,certificate,password);
            //调用接口
            log.info("请求路径url:{}", url);
            String returnStr = ClientInterface.httpPost(url,dataMap);
            log.info("返回结果returnMsg:{}",returnStr);
            if(StringUtils.isBlank(returnStr)){
                result.setMsg("调用银盛查询纯代付资金账户信息失败");
                return result;
            }
            //组装验签数据
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            Map<String, Object> o = gson.fromJson(returnStr, new TypeToken<Map<String, Object>>() {
            }.getType());
            Object ysepay_online_user_account_get_response = o.get("ysepay_online_user_account_get_response");
            Object sign = o.get("sign");
            String ysepay_online_user_account_get_responseStr = gson.toJson(ysepay_online_user_account_get_response);
            String signStr = gson.toJson(sign);

            //验签文件地址
            InputStream publicCertFileInputStream = YsClient.class.getResourceAsStream(YsConstant.businessgate);

            //验签
//            log.info("验签地址:{}，password:{}",YsConstant.businessgate);
            boolean isSign = SignUtils.rsaCheckContent(publicCertFileInputStream,ysepay_online_user_account_get_responseStr
                    ,signStr,"utf-8");

            log.info("验签状态isSign:{}",isSign);

            if(!isSign){
                result.setMsg("调用银盛查询纯代付资金账户信息失败");
                return result;
            }
            JSONObject resultJson = JSONObject.parseObject(returnStr);
            //如果返回失败,测试环境没有status，防止为空
            if(resultJson.getBoolean("status") != null && !resultJson.getBoolean("status")){
                result.setMsg(resultJson.getString("msg"));
                return result;
            }
            JSONArray resultArray = JSONObject.parseArray(ysepay_online_user_account_get_responseStr);
            JSONObject accountJson = resultArray.getJSONObject(0);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(accountJson.getString("available_amount"));
        } catch (Exception e){
            log.error("调用银盛查询纯代付资金账户信息异常", e);
            result = ResponseUtil.buildResult(e);
            return result;
        }
        return result;
    }
}
