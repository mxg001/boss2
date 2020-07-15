package test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.util.HttpUtils;
import cn.eeepay.framework.util.Md5;

public class Test2 {
	
    public static void main(String[] args) throws Exception{
    	final String Token = "a185faf4c55807bca4bcfdaae06b58fa";
    	final String USER_INFO_ID = "4IE5DhZE4IJ2gdv9YmDr6jfaaIBBlyafFNPz2wYXtqN";
    	String acqIntoNo="b159a79e1550483755836";
    	
    	
    	//安卓
    	String agentNo = "1446";

    	//ios
    	//String agentNo = "1337";
    	String content = "您有一条收单商户进件审核不通过,请修改后重新提交";
		Map<String, Object> map = new HashMap<>();
		map.put("acqIntoNo", acqIntoNo);
		map.put("type", "defeated");
		JSONObject jsonObject = new JSONObject(map);
        //String authCode = F88F3552977641886918F85641EBAD2A;
        String authCode = Md5.md5Str(USER_INFO_ID + agentNo + Token).toUpperCase();
        String paramStr = "token=" + Token + "&merchantNo=" + agentNo + "&authCode=" + authCode  +"&content="+URLEncoder.encode(content,"UTF-8")+"&ext="+URLEncoder.encode(jsonObject.toString(),"UTF-8");
        String url = "http://192.168.3.14:8080/riskhandle/commonJPush";
        String str = HttpUtils.sendPost(url, paramStr, "UTF-8");
        System.out.println("极光推送结果：" + str);
    }


}
