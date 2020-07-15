package cn.eeepay.framework.util.zf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author tans
 * @date 2018/8/30 14:23
 */
@Component
public class ZfConstant {

    public static String url;

    public static String interfaceName;

    public static String key;

    public static String charset;

    @Value("${pureAcctInfo.url}")
    public void setUrl(String url) {
        ZfConstant.url = url;
    }

    @Value("${pureAcctInfo.interfaceName}")
    public void setInterfaceName(String interfaceName) {
        ZfConstant.interfaceName = interfaceName;
    }

    @Value("${pureAcctInfo.key}")
    public void setKey(String key) {
        ZfConstant.key = key;
    }

    @Value("${pureAcctInfo.charset}")
    public void setCharset(String charset) {
        ZfConstant.charset = charset;
    }
}

