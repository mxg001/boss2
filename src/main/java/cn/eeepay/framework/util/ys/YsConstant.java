package cn.eeepay.framework.util.ys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author tans
 * @date 2018/8/31 10:28
 */
@Component
public class YsConstant {

    public static String url;
    public static String interfaceName;
    public static String method;
    public static String charset;
    public static String version;
    public static String sign_type;

    public static String businessgate;

    public static String shanghu_test;
    public static String shanghu_test_user_name;
    public static String shanghu_test_certificate;
    public static String shanghu_test_password;
    public static String shanghu_test_account_no;

    public static String qhyl;
    public static String qhyl_user_name;
    public static String qhyl_certificate;
    public static String qhyl_password;
    public static String qhyl_account_no;

    public static String mfjf;
    public static String mfjf_user_name;
    public static String mfjf_certificate;
    public static String mfjf_password;
    public static String mfjf_account_no;

    public static String mfjf002;
    public static String mfjf002_user_name;
    public static String mfjf002_certificate;
    public static String mfjf002_password;
    public static String mfjf002_account_no;

    public static String paishuju;
    public static String paishuju_user_name;
    public static String paishuju_certificate;
    public static String paishuju_password;
    public static String paishuju_account_no;

    @Value("${gateway.url}")
    public void setUrl(String url) {
        YsConstant.url = url;
    }

    @Value("${gateway.interfaceName}")
    public void setInterfaceName(String interfaceName) {
        YsConstant.interfaceName = interfaceName;
    }

    @Value("${gateway.qhyl}")
    public void setQhyl(String qhyl) {
        YsConstant.qhyl = qhyl;
    }

    @Value("${gateway.qhyl_user_name}")
    public void setQhyl_user_name(String qhyl_user_name) {
        YsConstant.qhyl_user_name = qhyl_user_name;
    }

    @Value("${gateway.qhyl_certificate}")
    public void setQhyl_certificate(String qhyl_certificate) {
        YsConstant.qhyl_certificate = qhyl_certificate;
    }

    @Value("${gateway.qhyl_password}")
    public void setQhyl_password(String qhyl_password) {
        YsConstant.qhyl_password = qhyl_password;
    }

    @Value("${gateway.qhyl_account_no}")
    public void setQhyl_account_no(String qhyl_account_no) {
        YsConstant.qhyl_account_no = qhyl_account_no;
    }

    @Value("${gateway.mfjf}")
    public void setMfjf(String mfjf) {
        YsConstant.mfjf = mfjf;
    }

    @Value("${gateway.mfjf_user_name}")
    public void setMfjf_user_name(String mfjf_user_name) {
        YsConstant.mfjf_user_name = mfjf_user_name;
    }

    @Value("${gateway.mfjf_certificate}")
    public void setMfjf_certificate(String mfjf_certificate) {
        YsConstant.mfjf_certificate = mfjf_certificate;
    }

    @Value("${gateway.mfjf_password}")
    public void setMfjf_password(String mfjf_password) {
        YsConstant.mfjf_password = mfjf_password;
    }

    @Value("${gateway.mfjf_account_no}")
    public void setMfjf_account_no(String mfjf_account_no) {
        YsConstant.mfjf_account_no = mfjf_account_no;
    }

    @Value("${gateway.mfjf002}")
    public void setMfjf002(String mfjf002) {
        YsConstant.mfjf002 = mfjf002;
    }

    @Value("${gateway.mfjf002_user_name}")
    public void setMfjf002_user_name(String mfjf002_user_name) {
        YsConstant.mfjf002_user_name = mfjf002_user_name;
    }

    @Value("${gateway.mfjf002_certificate}")
    public  void setMfjf002_certificate(String mfjf002_certificate) {
        YsConstant.mfjf002_certificate = mfjf002_certificate;
    }

    @Value("${gateway.mfjf002_password}")
    public void setMfjf002_password(String mfjf002_password) {
        YsConstant.mfjf002_password = mfjf002_password;
    }

    @Value("${gateway.mfjf002_account_no}")
    public void setMfjf002_account_no(String mfjf002_account_no) {
        YsConstant.mfjf002_account_no = mfjf002_account_no;
    }

    @Value("${gateway.shanghu_test}")
    public void setShanghu_test(String shanghu_test) {
        YsConstant.shanghu_test = shanghu_test;
    }

    @Value("${gateway.shanghu_test_user_name}")
    public void setShanghu_test_user_name(String shanghu_test_user_name) {
        YsConstant.shanghu_test_user_name = shanghu_test_user_name;
    }

    @Value("${gateway.shanghu_test_certificate}")
    public void setShanghu_test_certificate(String shanghu_test_certificate) {
        YsConstant.shanghu_test_certificate = shanghu_test_certificate;
    }

    @Value("${gateway.shanghu_test_password}")
    public void setShanghu_test_password(String shanghu_test_password) {
        YsConstant.shanghu_test_password = shanghu_test_password;
    }

    @Value("${gateway.shanghu_test_account_no}")
    public void setShanghu_test_account_no(String shanghu_test_account_no) {
        YsConstant.shanghu_test_account_no = shanghu_test_account_no;
    }

    @Value("${gateway.method}")
    public void setMethod(String method) {
        YsConstant.method = method;
    }

    @Value("${gateway.charset}")
    public void setCharset(String charset) {
        YsConstant.charset = charset;
    }

    @Value("${gateway.version}")
    public void setVersion(String version) {
        YsConstant.version = version;
    }

    @Value("${gateway.sign_type}")
    public void setSign_type(String sign_type) {
        YsConstant.sign_type = sign_type;
    }

    @Value("${gateway.businessgate}")
    public void setBusinessgate_prod(String businessgate) {
        YsConstant.businessgate = businessgate;
    }

    @Value("${gateway.paishuju}")
    public void setPaishuju(String paishuju) {
        YsConstant.paishuju = paishuju;
    }

    @Value("${gateway.paishuju_user_name}")
    public void setPaishuju_user_name(String paishuju_user_name) {
        YsConstant.paishuju_user_name = paishuju_user_name;
    }

    @Value("${gateway.paishuju_certificate}")
    public void setPaishuju_certificate(String paishuju_certificate) {
        YsConstant.paishuju_certificate = paishuju_certificate;
    }

    @Value("${gateway.paishuju_password}")
    public void setPaishuju_password(String paishuju_password) {
        YsConstant.paishuju_password = paishuju_password;
    }

    @Value("${gateway.paishuju_account_no}")
    public void setPaishuju_account_no(String paishuju_account_no) {
        YsConstant.paishuju_account_no = paishuju_account_no;
    }
}
