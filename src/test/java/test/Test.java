package test;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.util.HttpUtils;
import cn.hutool.core.codec.Base64;

public class Test {

    static class A{
        int num = 6;
        int price = 0;
        List<Integer> list = new ArrayList<>();

        public A(int num, int price) {
            this.num = num;
            this.price = price;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public  List<Integer> getList() {
            List<Integer> list = new ArrayList<>();
            if(this.num > 0){
                for(int i =0; i < this.num; i++){
                    list.add(i);
                }
            }
            return list;
        }

        public int getArrOne(int i){
            return getList().get(i%(this.num + 1)) * this.price;
        }
    }

    public static void main(String[] args) throws Exception {
    	
    	
    	Map<String, Object> oneMap = new HashMap<>();
    	Map<String, Object> twoMap = new HashMap<>();
    	oneMap.put("distinct_id", "test1");
		oneMap.put("time", new Date().getTime());
		oneMap.put("type", "profile_set");
		oneMap.put("project", "default");
		oneMap.put("properties", twoMap);

		twoMap.put("second_id", "test1");
		twoMap.put("orgnize_id", "100090");
		twoMap.put("merchant_no", "258121000031719");
		twoMap.put("bank_name", "上海浦东发展银行深圳分行");
		twoMap.put("first_level_agent_no", "22226");
		twoMap.put("sign_source", "关联进件系统自动开通");
		twoMap.put("user_type", "商户");
		twoMap.put("business_product1", "银POS无卡支付");
		twoMap.put("merchant_status", "正常");
		twoMap.put("$province", "北京");
		twoMap.put("$city", "北京市");
		twoMap.put("device_id", "14444444306");
		twoMap.put("product_type1", "203");
		twoMap.put("age", "1989年10月25日");
		twoMap.put("gender", "女");
		twoMap.put("registration_time", "1997-11-03 12:03:55");
		twoMap.put("agent_no", "22226");
		//JSON BASE64 URLEncoder
		JSONObject jsonObject = new JSONObject(oneMap);
		
		
		
		
		
		System.out.println(jsonObject);
		
        String encode = Base64.encode(jsonObject.toString().getBytes());
        String data = URLEncoder.encode(encode,"UTF-8");
		String paramSC = "data=" + data + "&zip=0";
		String sendPost = HttpUtils.sendPost("https://shenceapi.sqianbao.cn/sa?project=default&token=saf7c5e114", paramSC, "UTF-8");
    	
    	System.out.println(sendPost);
    	
    }
    
/*    public static void main1(String[] args) throws Exception {
    	 // 从神策分析获取的数据接收的 URL
        final String SA_SERVER_URL = "https://shenceapi.sqianbao.cn/sa?project=default&token=saf7c5e114";
        // 使用 Debug 模式，并且导入 Debug 模式下所发送的数据
        final boolean SA_WRITE_DATA = true;

        // 使用 DebugConsumer 初始化 SensorsAnalytics
        final SensorsAnalytics sa = new SensorsAnalytics(
                new SensorsAnalytics.DebugConsumer(SA_SERVER_URL, SA_WRITE_DATA));
        String distinctId = "12345";
        // 用户浏览商品
          Map<String, Object> properties = new HashMap<String, Object>();
          
          twoMap.put("$city", "深圳");
  		twoMap.put("$province", "广东省");
          
          // 记录用户浏览商品事件
          sa.profileSet("12345", true, properties);
          
         //sa.track(distinctId, true, "", properties);

	}*/
}
