package cn.eeepay.framework.service.impl.exchangeActivate;

import cn.eeepay.framework.daoExchange.exchangeActivate.RdmpSysConfigDao;
import cn.eeepay.framework.service.exchangeActivate.HttpJfpdapiService;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/10/12/012.
 * @author  liuks
 * 封装请求 JFPD_API 通道接口 实现类
 */
@Service("httpJfpdapiService")
public class HttpJfpdapiServiceImpl implements HttpJfpdapiService {


    private static final Logger log = LoggerFactory.getLogger(HttpJfpdapiServiceImpl.class);

    public static final String ROUTE_NO="JFPD_API";

    @Resource
    private RdmpSysConfigDao rdmpSysConfigDao;

    /**
     * 多媒体下载接口
     * /roots/:root_id/media
     * @return
     */
    @Override
    public String httpMedia(String goodTypeNo){
        String str=roots("/roots/"+goodTypeNo+"/media");
        return str;
    }

    /**
     * 订单查询接口
     * Url	/users/:user_id/orders/:order_id
     * @param orderNo
     * @return
     */
    @Override
    public String httpOrder(String orderNo) {
        String bhsgd_user_id=rdmpSysConfigDao.getConfigValue("BHSGD_USER_ID");
        if(bhsgd_user_id==null||"".equals(bhsgd_user_id)){
            log.info("系统为配置上游用户ID!");
            return null;
        }
        String str=roots("/users/"+bhsgd_user_id+"/orders/"+orderNo);
        return str;
    }


    private String roots(String strUrl) {
        String bhsgd_url=rdmpSysConfigDao.getConfigValue("BHSGD_URL");
        if(bhsgd_url==null||"".equals(bhsgd_url)){
            log.info("系统为配置请求域名!");
            return null;
        }
        String bhsgd_app_secret=rdmpSysConfigDao.getConfigValue("BHSGD_APP_SECRET");
        if(bhsgd_app_secret==null||"".equals(bhsgd_app_secret)){
            log.info("系统为配置上游请求加密!");
            return null;
        }

        String logTag = RandomUtil.simpleUUID();
        String url = bhsgd_url+strUrl;
        String body = "";
        String contentType = "application/json";
        HttpRequest request = HttpUtil.createGet(url);
        request.header("Authentication", bhsgdHmac(url, body,"GET",bhsgd_app_secret));
        request.contentType(contentType);
        if(StringUtils.isNotBlank(body)){
            request.body(body);
        }
        String res = request.execute().body();
        log.info(String.format("[%s]response:[%s]", logTag, res));
        return res;
    }

    /**
     * 封装上游加密
     * @param url
     * @param jsonStr
     * @param method "GET" "POST"
     * @param app_secret 加密文
     */
    private String bhsgdHmac(String url, String jsonStr,String method,String app_secret) {
        String logTag = RandomUtil.simpleUUID();
        String time_stamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonce = RandomUtil.randomString(16);
        String data = method + "+" + url + "+" + time_stamp + "+" + nonce + "+" + app_secret + "+" + jsonStr;
        log.info(String.format("[%s]hmacData[%s]", logTag, data));
        String digest = SecureUtil.sha1(data);
        log.info(String.format("[%s]hmacDigest[%s]", logTag, digest));
        String hmac = String.format("hmac %s:%s:%s", time_stamp, nonce, digest);
        log.info(String.format("[%s]hmacResult[%s]", logTag, hmac));
        return hmac;
    }
}
