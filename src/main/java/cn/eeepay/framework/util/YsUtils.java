package cn.eeepay.framework.util;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * @author tans
 * @date 2018/8/28 9:41
 */
public class YsUtils {

    /**
     * 获取证书路径并且签名
     * @param sParaTemp
     * @param password
     * @param certificate
     * @return
     */
    public static Map<String, String> buildRequestPara(Map<String, String> sParaTemp, String certificate, String password) {

        //除去数组中的空值和签名参数
        Map<String, String> sPara = SignUtils.paraFilter(sParaTemp);

        //读取证书
        InputStream pfxCertFileInputStream = YsUtils.class.getResourceAsStream(certificate);

        String mysign = "";
        try {
            //遍历以及根据重新排序
            String signContent = SignUtils.getSignContent(sPara);

            mysign = SignUtils.rsaSign(signContent, sParaTemp.get("charset"), pfxCertFileInputStream, certificate,password);

        } catch (Exception e) {
            throw new RuntimeException("签名失败，请检查证书文件是否存在，密码是否正确");
        }

        sPara.put("sign", mysign);
        return sPara;
    }

}
