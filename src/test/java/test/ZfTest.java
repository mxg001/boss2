package test;

import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.Md5;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tans
 * @date 2018/8/24 14:25
 */
public class ZfTest {

    public static void main(String[] args) {
        String url = "http://183.239.128.110:8457/PosMerchant/pureAcctInfo";
        String userName = "ceshidaili";
        String key = "UA859SDOOSDM858asdfasd1asdkj";
        String charset = "ISO-8859-1";

        String sign = Md5.md5Str(userName+key, charset);
        Map<String,String> map = new HashMap<String,String>();
        map.put("userName", userName);
        map.put("md5Dat", sign.toUpperCase());

        String rsStr = ClientInterface.httpPost(url,map);
        System.out.println(rsStr);
        Result result = new Result();
        if(StringUtils.isBlank(rsStr)){
            result.setMsg("调用接口查询失败");
            return;
        }
        JSONArray resultArray = JSONObject.parseArray(rsStr);
        JSONObject resultJson = resultArray.getJSONObject(0);
        if("1".equals(resultJson.getString("status")) ){
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(resultJson.getString("money"));
        } else {
            result.setMsg(resultJson.getString("dealMsg"));
        }
        return;
    }

}
