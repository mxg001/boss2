package test;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.http.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class cjdTest {
    public static void main(String[] args) {

	        RSA rsa = SecureUtil.rsa();
			String privateKey = rsa.getPrivateKeyBase64();
			String publicKey = rsa.getPublicKeyBase64();
			System.out.println(publicKey);
	        String oem_no = "";
	        String app_key = "";
	        String service_name = "";

	        //接32位不唯一请求ID，相同请求ID会请求到缓存数据
	        String url = "http://test-gscjd.pay-world.cn/open/receive/".concat(RandomUtil.simpleUUID());
	        Map params = new HashMap();

	        {
	            {//OEM注册,注册成功后,会响应oem_no,app_key
	                service_name = "oemRegister";
	                params.put("agent_no", "3572");
	                params.put("public_key", publicKey);
	                params.put("order_call_back", "http://yhjh5ts.ygs001.com/superbank/wx/exchange!getOrderList.action");
	                params.put("company_name", "超级银行家VIP测试");
	                params.put("share_rate", "0");
	                params.put("oem_fee", "0");
					params.put("company_id", "3572");
	            }

	            oem_no="OPEN10042";
	            app_key="6313f2850d09f4d3d80693152429b340";
	        }

	        //使用RSA私钥，对数据进行加密
	        params.put("oem_no", oem_no);
	        params.put("app_key", app_key);
	        params.put("service_name", service_name);

	        //post 请求
	        String res = HttpUtil.post(url,params);
	        System.out.println(res);
	        System.err.println(res);


    }

}
