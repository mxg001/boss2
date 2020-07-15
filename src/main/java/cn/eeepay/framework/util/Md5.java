package cn.eeepay.framework.util;

import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.SortedMap;

/**
 * 生产md5
 * 
 * @author dj
 * 
 */
public class Md5 {
	/**
	 * 根据明文生成md5密文
	 * 
	 * @param str
	 *            要加密的明文
	 * @return md5密文
	 */
	public static String md5Str(String str) {
		MessageDigest messageDigest = null;
		StringBuffer md5StrBuff = new StringBuffer();

		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));

			byte[] byteArray = messageDigest.digest();

			for (int i = 0; i < byteArray.length; i++) {
				if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
					md5StrBuff.append("0").append(
							Integer.toHexString(0xFF & byteArray[i]));
				else
					md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}



		return md5StrBuff.toString();
	}
	
	public static String md5Str(String str, String charset) {
		MessageDigest messageDigest = null;
		StringBuffer md5StrBuff = new StringBuffer();
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes(charset));
			byte[] byteArray = messageDigest.digest();

			for (int i = 0; i < byteArray.length; i++) {
				if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
					md5StrBuff.append("0").append(
							Integer.toHexString(0xFF & byteArray[i]));
				else
					md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return md5StrBuff.toString();
	}

	public static String MD5Encode(String strSrc, String key,String charset) {
		try {
			if(StringUtil.isBlank(charset))
				charset="UTF-8";
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(strSrc.getBytes(charset));
			StringBuilder result = new StringBuilder(32);
			byte[] temp;
			temp = md5.digest(key.getBytes(charset));
			for (int i = 0; i < temp.length; i++) {
				result.append(Integer.toHexString(
						(0x000000ff & temp[i]) | 0xffffff00).substring(6));
			}
			return result.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}


	public static String reqSign(Map param){
		String key = "f2d7caf72ff9084460e431a7122d1fe5";
		String jsonStr = JSONObject.toJSONString(param);
		SortedMap<String,String> sortedMap = JSONObject.parseObject(jsonStr,SortedMap.class);
		String str = "";
		for(String keyStr : sortedMap.keySet() ){
			//去掉加密的md5key参数
			if (keyStr.equals("sign")) {
				continue;
			}
			str += keyStr;
			str += "=";
			str += sortedMap.get(keyStr);
			str += "&";
		}
		str += key;
		System.out.println(str);
		String sign=Md5.md5Str(str);
		return sign;
	}
}
