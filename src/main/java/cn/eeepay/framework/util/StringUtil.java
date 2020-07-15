package cn.eeepay.framework.util;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class StringUtil {
	
	/**
	 * In语句最多只能包含1000个元素
	 */
	private static final int IN_MAX_NUM = 1000;



	/**
	 * 判断是否含有特殊字符
	 *
	 * @param str
	 * @return true为包含，false为不包含
	 */
	public static boolean isSpecialChar(String str) {
		//String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
		String regEx = "[+/?\\\\$&';#=:]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}

	/**
	 * In 拼接
	 * 
	 * @return
	 */
	public static String inJoint(String columnName, String listName, int size) {
		StringBuilder inSb = new StringBuilder();

		if (size <= StringUtil.IN_MAX_NUM) {
			inSb.append(columnName).append(" IN(");
			for (int i = 0; i < size; i++) {
				inSb.append("#{").append(listName).append("[").append(i).append("]},");
			}
			inSb.deleteCharAt(inSb.length() - 1);
			inSb.append(")");
		} else {
			inSb.append("(");
			// 列表角标
			int listSize = 0;
			// in的个数
			int inSize = size % StringUtil.IN_MAX_NUM > 0 ? size / StringUtil.IN_MAX_NUM + 1 : size / StringUtil.IN_MAX_NUM;
			for (int i = 1; i <= inSize; i++) {
				inSb.append(columnName).append(" IN(");
				// in里面的元素数
				int subSize = i * StringUtil.IN_MAX_NUM > size ? size % StringUtil.IN_MAX_NUM : StringUtil.IN_MAX_NUM;
				for (int j = 0; j < subSize; j++) {
					inSb.append("#{").append(listName).append("[").append(listSize++).append("]},");
				}
				inSb.deleteCharAt(inSb.length() - 1);
				inSb.append(")");
				inSb.append(" OR ");
			}
			inSb.delete(inSb.length() - 4, inSb.length());
			inSb.append(")");
		}
		return inSb.toString();
	}

	/**
	 * 字符串左补0
	 * */
	public static String stringFillLeftZero(String str, int len) {
		if (str.length() < len) {
			StringBuffer sb = new StringBuffer(len);
			for (int i = 0; i < len - str.length(); i++)
				sb.append('0');
			sb.append(str);
			return new String(sb);
		} else
			return str;
	}

	/**
	 * 功能：不定长参数,其中一个参数为null或空则返回true,负责返回false
	 *
	 * @param str
	 * @return boolean
	 */
	public static boolean isEmpty(String... str) {
		for (String s : str) {
			if (StringUtils.isEmpty(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 功能：不定长参数,其中一个参数为null或空或为空格字符串则返回true,负责返回false
	 *
	 * @param str
	 * @return boolean
	 */
	public static boolean isBlank(String... str) {
		for (String s : str) {
			if (StringUtils.isBlank(s))
				return true;
		}
		return false;
	}

	public static boolean isBlank(Object obj) {
		return null == obj ? true : StringUtils.isBlank(String.valueOf(obj));
	}

	public static boolean isNotBlank(Object obj) {
		return null == obj ? false : StringUtils.isNotBlank(String.valueOf(obj));
	}

	/**
	 * 功能：判断字符串是否是数值. 默认允许有正负号,默认允许有小数点
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		boolean sign = true;
		int point_bef = Integer.MAX_VALUE;// 小数点前有几位
		int point_aft = Integer.MAX_VALUE;// 小数点后有几位
		return isNumeric(str, sign, point_bef, point_aft);
	}

	/**
	 * 功能：判断字符串是否是数值
	 *
	 * @param str
	 * @param sign
	 *          是否允许有正负号
	 * @param point
	 *          是否允许有小数点
	 * @return
	 */
	public static boolean isNumeric(String str, boolean sign, boolean point) {
		int point_bef = Integer.MAX_VALUE;// 小数点前有几位
		int point_aft = Integer.MAX_VALUE;// 小数点后有几位
		if (!point)
			point_aft = 0;

		return isNumeric(str, sign, point_bef, point_aft);
	}

	/**
	 * 功能：判断字符串是否是数值
	 *
	 * @param str
	 * @param sign
	 *          是否允许有正负号
	 * @param point_bef
	 *          精度,小数点前有几位
	 * @param point_aft
	 *          精度,小数点后有几位,如果为0,则为整数
	 *
	 * @return
	 */
	public static boolean isNumeric(String str, boolean sign, int point_bef,
									int point_aft) {
		if (StringUtil.isBlank(str)) {
			return false;
		}
		boolean point = true;// 是否允许小数点
		if (point_aft == 0) {
			point = false;// 不允许有小数点
		} else {
			point = true;
		}
		StringBuffer pat = new StringBuffer();
		if (sign) {
			pat.append("[+|-]?");
		}
		if (point_bef == 0) {
			pat.append("[0]");
		} else {
			pat.append("[0-9]{1,");
			pat.append(point_bef);
			pat.append("}");
		}
		if (point && str.indexOf(".") != -1) {// 允许小数点,并且有小数点
			pat.append("[.]");
			pat.append("[0-9]{1,");// 小数点后必须有一位
			pat.append(point_aft);
			pat.append("}");
		}
		Pattern pattern = Pattern.compile(pat.toString());
		if (!pattern.matcher(str).matches()) {
			return false;
		} else {// 排除如00.1,返回false
			if (str.indexOf(".") != -1
					&& str.substring(0, str.indexOf(".")).length() > 1
					&& Integer.valueOf(str.substring(0, str.indexOf("."))) == 0) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * 功能：查看字符串是否有这个子字符串
	 *
	 * @param str
	 *          主字符串
	 * @param substr
	 *          字字符串
	 * @return
	 */
	public static boolean hasSubstring(String str, String substr) {
		if (str == null || substr == null)
			return false;
		int strLen = str.length();
		int substrLen = substr.length();
		for (int i = 0; (i + substrLen) <= strLen; i++) {
			if (str.substring(i, i + substrLen).equalsIgnoreCase(substr)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 功能：验证是否是正确的手机号
	 *
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile(String mobile) {
		if (StringUtil.isBlank(mobile))
			return false;
		return Pattern.matches("^(1[3|5|8])\\d{9}$", mobile);
	}

	/**
	 * 功能：字符串不以"/"结尾，则在串尾加"/"
	 *
	 * @param s
	 * @return
	 */
	public static String addSlashInEnd(String s) {
		if (s != null) {
			s = s.trim();
			if (!s.endsWith("/")) {
				s = s + "/";
			}
		} else {
			s = "";
		}
		return s;
	}

	/**
	 * 功能：字符串不以"/"结尾，则在串尾加"/";字符串如果以/开头,则去掉第一个/
	 *
	 * @return
	 */
	public static String dealSlash(String s) {
		if (s != null) {
			s = s.trim();
			if (!s.endsWith("/")) {
				s = s + "/";
			}
			if (s.startsWith("/")) {
				s = s.substring(1, s.length());
			}
		} else {
			s = "";
		}
		return s;

	}

	/**
	 * 功能：传入一个数字类型的参数，返回一个小数点后两位的小数
	 *
	 * @param parm
	 */
	public static String converDouble(String parm) {
		if (isNumeric(parm, false, true)) {
			if (parm.indexOf(".") >= 0) {
				String value = parm.substring(parm.indexOf(".") + 1);
				if (value.length() == 1) {
					return parm + "0";
				} else if (value.length() > 2) {
					return parm.substring(0, parm.indexOf(".") + 1)
							+ value.substring(0, 2);
				} else {
					return parm;
				}
			} else {
				return parm + ".00";
			}
		}
		return null;
	}


	/**
	 *
	 * @param obj
	 * @return String
	 * @obj==null,或obj是空字符串，就返回参数ifEmptyThen，否则返回obj.toString。
	 */

	public static String ifEmptyThen(Object obj,String ifEmptyThen) {
		String ret="";
		if(obj==null||String.valueOf(obj)==""){
			ret=ifEmptyThen;
		}else{
			ret=obj.toString();
		}
		return ret;
	}

	/**
	 * 功能：传入一个对象，如果为null，则输出为"",如果不为null,就调用toString()方法
	 *
	 */
	public static String filterNull(Object s) {
		if (s == null) {
			return "";
		} else {
			return s.toString();
		}
	}


	/**
	 * 字符串左补空格
	 * @throws UnsupportedEncodingException
	 */
	public static String stringFillLeftSpace(String str, int len) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer(len);
		if(!isEmpty(str)){
			int strLen = str.getBytes("GBK").length;
			if (strLen < len) {
				for (int i = 0; i < len - strLen; i++)
					sb.append(" ");
				sb.append(str);
				return new String(sb);
			} else
				return str;
		}else{
			for (int i = 0; i < len ; i++)
				sb.append(" ");
			return new String(sb);
		}
	}
	/**
	 * 字符串右补空格
	 * @throws UnsupportedEncodingException
	 */
	public static String stringFillRightSpace(String str, int len) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer(len);
		if(!isEmpty(str)){
			int strLen = str.getBytes("GBK").length;
			if (strLen < len) {
				sb.append(str);
				for (int i = 0; i < len - strLen; i++)
					sb.append(" ");
				return new String(sb);
			} else
				return str;
		}else{
			for (int i = 0; i < len ; i++)
				sb.append(" ");
			return new String(sb);
		}
	}
	
	/**
	 * <h1>校验参数长度</h1>
	 * 参数顺序：minLength 最小长度、value 需要校验的值、maxLength 最大参数
	 * <br><h1>校验公式： value.length() > minLength && value.length() < maxLength</h1>
	 * @param minLength 最小长度
	 * @param value 需要校验的值
	 * @param maxLength 最大参数
	 * @return boolean 校验值于最小以及最大之间则成立 否则不成立
	 * 
	 */
	public static boolean checkLength(int minLength, String value, int maxLength){
		return value.length() > minLength && value.length() < maxLength ? true : false;
	}

	/**
	 *   trim全角空格
	 * @param str
	 * @return
	 */
	public static String trimChinese(String str) {
		return str.replace("　", "").trim();
	}
	/**
	* 功能：判断一个字符串是否包含特殊字符
	* @param string 要判断的字符串
	* @return true 提供的参数string不包含特殊字符
	* @return false 提供的参数string包含特殊字符
	*/
	public static boolean isConSpeCharacters(String string) {
	   // TODO Auto-generated method stub
	   if(string.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length()==0){
	    //如果不包含特殊字符
	    return true;
	   }
	   return false;
	}

	
	// 求两个字符串数组的并集，利用set的元素唯一性
		public static String[] union(String[] arr1, String[] arr2)
		{
			Set<String> set = new HashSet<String>();
			for (String str : arr1)
			{
				set.add(str);
			}
			for (String str : arr2)
			{
				set.add(str);
			}
			String[] result = {};
			return set.toArray(result);
		}
		
		// 求两个数组的交集
		public static String[] intersect(String[] arr1, String[] arr2)
		{
			Map<String, Boolean> map = new HashMap<String, Boolean>();
			List<String> list = new ArrayList<String>();
			for (String str : arr1)
			{
				if (!map.containsKey(str))
				{
					map.put(str, Boolean.FALSE);
				}
			}
			for (String str : arr2)
			{
				if (map.containsKey(str))
				{
					map.put(str, Boolean.TRUE);
				}
			}

			for (Iterator<Entry<String, Boolean>> it = map.entrySet().iterator();it.hasNext();)
			{
				Entry<String,Boolean> e = (Entry<String,Boolean>)it.next();
				if (e.getValue().equals(Boolean.TRUE))
				{
					list.add(e.getKey());
				}
			}
			return list.toArray(new String[] {});
		}
		
		 //求两个数组的差集   
	    public static String[] minus(String[] arr1, String[] arr2) {   
	        LinkedList<String> list = new LinkedList<String>();   
	        LinkedList<String> history = new LinkedList<String>();   
	        String[] longerArr = arr1;   
	        String[] shorterArr = arr2;   
	        //找出较长的数组来减较短的数组   
	        if (arr1.length > arr2.length) {   
	            longerArr = arr2;   
	            shorterArr = arr1;   
	        }   
	        for (String str : longerArr) {   
	            if (!list.contains(str)) {   
	                list.add(str);   
	            }   
	        }   
	        for (String str : shorterArr) {   
	            if (list.contains(str)) {   
	                history.add(str);   
	                list.remove(str);   
	            } else {   
	                if (!history.contains(str)) {   
	                    list.add(str);   
	                }   
	            }   
	        }   
	  
	        String[] result = {};   
	        return list.toArray(result);   
	    }   
	  //生成随机数字和字母,  
	    public static  String getStringRandom(int length) {  
	          
	        String val = "";  
	        Random random = new Random();  
	          
	        //参数length，表示生成几位随机数  
	        for(int i = 0; i < length; i++) {  
	              
	            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
	            //输出字母还是数字  
	            if( "char".equalsIgnoreCase(charOrNum) ) {  
	                //输出是大写字母还是小写字母  
//	                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
	                val += (char)(random.nextInt(26) + 97);  
	            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
	                val += String.valueOf(random.nextInt(10));  
	            }  
	        }  
	        return val;  
	    }

	/**
	 * 判断字符数组是否为空
	 */
	public static boolean areNotEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isEmpty(value);
			}
		}
		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 *
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);

			if (i == keys.size() - 1) {
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

	/**
	 * @param str 处理的字段
	 * @param state 0手机号 1身份证号
	 * @return
	 */
	public static String sensitiveInformationHandle(String str,int state){
		if(StringUtils.isNotBlank(str)){
			if(0==state){
				return str.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
			}else if(1==state){
				return str.replaceAll("(\\d{6})\\d{8}(\\w{4})","$1********$2");
			}else if(2==state){
				int length=str.length()-10;
				StringBuffer sb=new StringBuffer();
				for(int i=0;i<length;i++){
					sb.append("*");
				}
				return str.replaceAll("(\\d{6})\\d+(\\d{4})","$1"+sb.toString()+"$2");
			} else if(3==state){
				if(str.indexOf("@") < 0) {
					return null;
				}
				String emailName = str.split("@")[0];
				int length = 0;
				if(emailName.length() <= 3 ) {
					length = 1;
				} else {
					length=emailName.length()-3;
				}
				StringBuffer sb=new StringBuffer();
				for(int i=0;i<length;i++){
					sb.append("*");
				}
				return str.replaceAll( "(.{1,3}).*?(@.*)", "$1" +sb.toString()+ "$2");
			} else if(4==state){
				int length=str.length()-10;
				StringBuffer sb=new StringBuffer();
				for(int i=0;i<length;i++){
					sb.append("*");
				}
				return str.replaceAll("(\\d{6})\\d+(\\d{4})","$1"+sb.toString()+"$2");
			}
		}
		return null;
	}

	/**
	 * 获取小数点之前的字符串
	 * @param str
	 * @return
	 */
	public static String getBeforePointStr(String str) {
		if(StringUtil.isEmpty(str)) {
			return str;
		}
//		if(str.indexOf("E") != -1 || str.indexOf("e") != -1 || str.indexOf("+") != -1) {
//			BigDecimal bigDecimal = new BigDecimal(str);
//			str = bigDecimal.toPlainString();
//		}
		if(str.contains(".")) {
			str = str.substring(0, str.indexOf("."));
		}
		return str;
	}

	/***
	 * 删除最后一个字符
	 * @param str
	 * @return
	 */
	public static String delLastChar(String str){
		if(StringUtils.isEmpty(str)) {
			return str;
		}
		return str.substring(0,str.length() - 1);
	}

	public static List<String> strToList(String str,String splitStr) {
		if(isEmpty(str)){
			return null;
		}
		List<String> lists = new ArrayList<String>();
		String[] strs=str.split(splitStr);
		if(strs != null && strs.length > 0){
			for(String s:strs){
				lists.add(s);
			}
		}
		return lists;
	}
}
