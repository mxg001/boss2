package cn.eeepay.framework.util.zf;

import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Md5;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 中付接口
 * @author tans
 * @date 2018/8/30 14:24
 */
public class ZfCilent {

    private static final Logger log = LoggerFactory.getLogger(ZfCilent.class);

    /**
     * 查询纯代付资金账户信息
     * @return
     */
    public static Result pureAcctInfo(String userName){
        Result result = new Result();
        String url = ZfConstant.url + ZfConstant.interfaceName;
        String key = ZfConstant.key;
        String charset = ZfConstant.charset;
        String sign = Md5.md5Str(userName+key, charset).toUpperCase();
        Map<String,String> map = new HashMap<String,String>();
        map.put("userName", userName);
        map.put("md5Date", sign);
        log.info("中付查询出款余额,sign:{}",sign);
        String rsStr = ClientInterface.httpPost(url,map);
        log.info("中付查询出款余额,返回结果:{}",rsStr);
        if(StringUtils.isBlank(rsStr)){
            result.setMsg("调用接口查询失败");
            return result;
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
        return result;
    }
}
