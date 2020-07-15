package cn.eeepay.framework.util;

import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.cjt.CjtProfitDetail;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWTSigner;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ClientInterface {
	private String host;
	private Map<String, String> params;
	private Map<String, String> headers;
	private CloseableHttpClient client;
	private static final DecimalFormat format = new java.text.DecimalFormat("0.00");
	private static final Logger log = LoggerFactory.getLogger(ClientInterface.class);

	public ClientInterface(String host, Map<String, String> headers, Map<String, String> params) {
		this.host = host;
		this.params = params;
		this.headers = headers;
		this.client = HttpClients.createDefault();
	}

	public ClientInterface(String host, Map<String, String> params) {
		this(host, null, params);
	}

	public static String handlerVipTask(Integer userId, String vipScoreUrl, MerchantInfo merchantInfo, String businessNo, String teamId, String key) {
		vipScoreUrl+="/business/receiveBusiness";
		Map<String,Object> leaguerInfo = new HashMap<>();
		leaguerInfo.put("triggerType", "check");
		leaguerInfo.put("triggerValue", null);
		leaguerInfo.put("triggerOrderNo", System.currentTimeMillis());
		leaguerInfo.put("originUserNo", merchantInfo.getMerchantNo());
		leaguerInfo.put("teamId",teamId);
		leaguerInfo.put("mobilePhone", merchantInfo.getMobilephone());
		leaguerInfo.put("businessNo", businessNo);
		String data = JSON.toJSONString(leaguerInfo);
		Long timestamp=DateUtil.dateToUnixTimestamp();
		String signStr="businessNo="+businessNo+"&data="+data+"&timestamp="+timestamp+key;
		String sign = Md5.md5Str(signStr);
		Map<String,String> params = new HashMap<>();
		params.put("businessNo",businessNo);
		params.put("data",data);
		params.put("sign",sign);
		params.put("timestamp",timestamp+"");
		log.info("进件审核通过触发会员系统 请求路径：{},参数：{}", vipScoreUrl, params);
		String returnMsg = new ClientInterface(vipScoreUrl, null).postRequest2(params);
		log.info("返回结果：{}", returnMsg);
		return returnMsg;
	}

	public String postRequest() {
		try {
			HttpPost post = new HttpPost(host);
			if (headers != null)
				post.setHeaders(setHeaders());
			post.setEntity(setParams());
			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(3000)
					.setConnectTimeout(3000).setSocketTimeout(30000).build();
			post.setConfig(requestConfig);
			HttpResponse res = client.execute(post);
			log.info("---------------------Http请求响应状态码：" + res.getStatusLine().getStatusCode());
			if (res.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(res.getEntity());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public String postRequest2(Map<String, String> param) {
		try {
			HttpPost post = new HttpPost(host);
			if (headers != null)
				post.setHeaders(setHeaders());
			// 创建参数列表
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,"utf-8");
				post.setEntity(entity);
			}
			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(3000)
					.setConnectTimeout(3000).setSocketTimeout(30000).build();
			post.setConfig(requestConfig);
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(res.getEntity());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

//    private HttpEntity setParams1() throws UnsupportedEncodingException {
//        if(params!=null){
//            StringBuilder sb = new StringBuilder();
//            for(String key:params.keySet()){
//                sb.append("key=" + URLEncoder.encode(params.get(key), "utf8") + "&");
//            }
//            if (sb.length() > 0){
//                sb.deleteCharAt(sb.length() - 1);
//            }
//            return new StringEntity(sb.toString());
//        }
//        return null;
//    }

	public String postRequestBody(String param) {
		try {
			log.info("请求参数：" + param);
			HttpPost post = new HttpPost(host);
			if (headers != null)
				post.setHeaders(setHeaders());
			post.setEntity(new StringEntity(param, "utf-8"));
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == 200) {
				String resultStr=EntityUtils.toString(res.getEntity());
				log.info("请求返回值：" + resultStr);
				return resultStr;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public String postRequestBodyWithJson(String param) {
		String result = "";
		try {
			log.info("请求参数：" + param);
			HttpPost post = new HttpPost(host);
			if (headers != null)
				post.setHeaders(setHeaders());
			StringEntity entity = new StringEntity(param, "utf-8");
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			post.setEntity(entity);
			HttpResponse res = client.execute(post);
			log.info("--------------------" + res.getStatusLine().getStatusCode());
			result = EntityUtils.toString(res.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public String getRequest() {
		try {
			HttpGet get = new HttpGet(host + "?" + EntityUtils.toString(setParams(), "ISO-8859-1"));
			if (headers != null)
				get.setHeaders(setHeaders());
			HttpResponse res = client.execute(get);
			log.info("-----------------" + res.getStatusLine().getStatusCode());
			if (res.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(res.getEntity());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public UrlEncodedFormEntity setParams() throws UnsupportedEncodingException {
		if (params != null) {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				list.add(new BasicNameValuePair(key, params.get(key)));
			}
			return new UrlEncodedFormEntity(list);
		}
		return null;
	}

	public Header[] setHeaders() throws UnsupportedEncodingException {
		if (headers != null) {
			Header[] headerArray = new Header[headers.size()];
			int i = 0;
			for (String key : headers.keySet()) {
				headerArray[i] = new BasicHeader(key, headers.get(key));
				i++;
			}
			return headerArray;
		}
		return null;
	}

	public static void main(String[] args) {
//		checkOrderProfit("100");
//		String batchNo = "1000";
//		String url = "http://192.168.8.49:2111/superbank-mobile/order/checkOrderProfit";
//		String url = "http://192.168.0.200:8020/repay/oemCreate";

		//{"redOrderId":"1","sign":"wefe12312212212"}


		//map.put("oem_name", "123");
//		map.put("sign", sign);


		//String str = httpPost(url,map);

		//System.out.println(str);

//		Map<String,String> params=new HashMap<>();
//		params.put("baseInfo", "{'serviceName':'','serviceType':'','rateCard':'','rateHolidays':'','quotaHolidays':'','quotaCard':'','fixedRate':'','fixedQuota':'','rateCheckStatus':'','rateLockStatus':'','quotaCheckStatus':'','quotaLockStatus':''}");
//		params.put("pageNo", "1");
//		params.put("pageSize", "10");
//		System.out.println(new ClientInterface("http://192.168.1.160:8088/boss2/service/queryServiceList", params).postRequest());
		
		
	/*	String url = "http://192.168.1.203:2111/superbank-mobile/order/closeRedOrder";
		Map<String,String> map = new HashMap<String,String>();
		String sign = Md5.md5Str("24500" + "&" + Constants.SUPER_BANK_SECRET);
		map.put("redOrderId", "24500");
		map.put("sign", sign);
		System.out.println(new ClientInterface(url, map).postRequest());*/
		//String url = "http://192.168.1.26:8081/superbank-mobile/order/checkBankaProfit";
		//	String url = "http://192.168.1.26:8081/superbank-mobile/order/checkBankaProfit";
		//	String url = "http://192.168.1.26:8081/superbank-mobile/order/closeRedOrder";
		String url = "http://192.168.1.203:2111/superbank-mobile/order/closeRedOrder";

		String sign = Md5.md5Str("10000" + "&" + Constants.SUPER_BANK_SECRET);
		Map<String, String> map = new HashMap<String, String>();
		map.put("redOrderId", "10000");
		map.put("sign", sign);

		String rsStr = httpPost2(url, map);
		System.out.println(rsStr);
	}

	public static String createAgentAccount(String agentNo) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("accountType", "A");
		claims.put("userId", agentNo);
		return baseClient(Constants.ACCOUT_HOST, claims);
	}

	public static String createMerchantAccount(String merNo) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("accountType", "M");
		claims.put("userId", merNo);
		return baseClient(Constants.ACCOUT_HOST, claims);
	}

	public static String transFrozenAccount(Map<String, Object> map) {
		return baseClient(Constants.TRANS_FROZEN, map);
	}

	public static String baseClient(String host, Map<String, Object> map) {
		final long iat = System.currentTimeMillis() / 1000l;
		map.put("exp", iat + 300L);
		map.put("iat", iat);
		map.put("jti", UUID.randomUUID().toString());
		Map<String, String> params = new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(map));
		log.info("请求路径：{},参数：{}", host, JSONObject.toJSONString(map));
		String returnStr = new ClientInterface(host, params).postRequest();
		log.info("返回结果：{}", returnStr);
		return returnStr;
	}

	public static String baseClientBySecret(String host, Map<String, Object> map, String secret) {
		final long iat = System.currentTimeMillis() / 1000l;
		map.put("exp", iat + 300L);
		map.put("iat", iat);
		map.put("jti", UUID.randomUUID().toString());
		Map<String, String> params = new HashMap<>();
		params.put("token", new JWTSigner(secret).sign(map));
		log.info("请求路径：{},参数：{}", host, JSONObject.toJSONString(map));
		String returnStr = new ClientInterface(host, params).postRequest();
		log.info("返回结果：{}", returnStr);
		return returnStr;
	}

	public static String baseNoClient(String host, Map<String, String> map) {
		log.info("请求路径：{},参数：{}", host, JSONObject.toJSONString(map));
		String returnStr = new ClientInterface(host, map).postRequest();
		log.info("返回结果：{}", returnStr);
		return returnStr;
	}

	public static String postRequest(String url) {
		Map<String, Object> params = new HashMap<>();
		return baseClient(url, params);
	}

	public static String findAllBankAccountInfoList() throws Exception {
		final Map<String, Object> claims = new HashMap<String, Object>();
		return baseClient(Constants.SHARE_BILL_ACCOUNT, claims);
	}

	/**
	 * 获取商户所有账号余额
	 *
	 * @param merNo
	 * @return {"msg":"查询成功","balance":"0.00","avaliBalance":0,"status":true}
	 */
	public static String getMerchantAccountAllBalance(String merNo) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("selectType", "2");
		claims.put("accountType", "M");
		claims.put("userId", merNo);
		claims.put("accountOwner", "000001");
		//claims.put("subjectNo", "224105");
		claims.put("currencyNo", "1");
		return baseClient(Constants.ACCOUNT_ALL_BALANCE_URL, claims);
	}

	//查询中钢余额
	public static String getMerchantZGBalance(String url,String accountNo) {
		final HashMap<String, String> claims = new HashMap<>();
		String key = "a822c4435022fc90c3b649a1e007c049";//key
		String signStr = accountNo + key;
		claims.put("accountNo", accountNo);//订单号
		claims.put("sign", Md5.md5Str(signStr));
		log.info("请求路径：{},参数：{}", url, JSONObject.toJSONString(claims));
		String returnStr = new ClientInterface(url, claims).postRequest();
		log.info("返回结果：{}", returnStr);
		return returnStr;
	}

	/**
	 * 查询易票联余额
	 * @param url
	 * @param accountNo
	 * @return
	 */
	public static String getMerchantYPLBalance(String url,String accountNo) {
		final HashMap<String, String> claims = new HashMap<>();
		String key = "a822c4435022fc90c3b649a1e007c049";
		String channel = "YPL_ZQ";
		String signStr = accountNo +channel + key;
		claims.put("accountNo", accountNo);//订单号
		claims.put("channel", channel);
		claims.put("sign", Md5.md5Str(signStr));
		log.info("请求路径：{},参数：{}", url, JSONObject.toJSONString(claims));
		String returnStr = new ClientInterface(url, claims).postRequest();
		log.info("返回结果：{}", returnStr);
		return returnStr;
	}

	/**
	 * 发送自动审件请求
	 *
	 * @param merNo，bpId，cardAccountNo
	 * @return {"data":{"checkFailItem":",,,","checkStatus":"2"},"msg":"审核完成","status":"success"}
	 */
	public static String toAutoCheck(String coreUrl, String merNo, String bpId, String cardAccountNo) {
		final HashMap<String, String> claims = new HashMap<String, String>();
		claims.put("merchantNo", merNo);
		claims.put("bpId", bpId);
		claims.put("cardAccountNo", cardAccountNo);
		return baseNoClient(coreUrl, claims);
	}

	/**
	 * 获取商户账号余额
	 *
	 * @param merNo
	 * @return
	 */
	public static String getMerchantAccountBalance(String merNo) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("selectType", "2");
		claims.put("accountType", "M");
		claims.put("userId", merNo);
		claims.put("accountOwner", "000001");
		claims.put("subjectNo", "224101001");
		claims.put("currencyNo", "1");
		return baseClient(Constants.ACCOUNT_BALANCE_URL, claims);
	}

	/**
	 * 获取商户账号余额
	 *
	 * @param merNo
	 * @return
	 */
	public static String getMerchantAccountBalance(String merNo, String subjectNo) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("selectType", "2");
		claims.put("accountType", "M");
		claims.put("userId", merNo);
		claims.put("accountOwner", "000001");
		claims.put("subjectNo", subjectNo);
		claims.put("currencyNo", "1");
		return baseClient(Constants.ACCOUNT_BALANCE_URL, claims);
	}

	/**
	 * 超级银行家-代理商账户
	 */
	public static String getSuperBankAccountBalance(String merNo, String subjectNo) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("selectType", "2");
		claims.put("accountType", "A");
		claims.put("userId", merNo);
		claims.put("accountOwner", "000001");
		claims.put("subjectNo", subjectNo);
		claims.put("currencyNo", "1");
		return baseClient(Constants.ACCOUNT_BALANCE_URL, claims);
	}

	/**
	 * 获取微创业商户账号余额
	 *
	 * @param merNo
	 * @return
	 */
	public static String getSuperPushUserBalance(String merNo) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("selectType", "2");
		claims.put("accountType", "M");
		claims.put("userId", merNo);
		claims.put("accountOwner", "000001");
		claims.put("subjectNo", "224103");
		claims.put("currencyNo", "1");
		return baseClient(Constants.ACCOUNT_BALANCE_URL, claims);
	}

	/**
	 * 冻结解冻商户账号
	 */
	public static String transFrozenAccountNo(Map<String, Object> map) {
		return baseClient(Constants.ACCOUNT_STATUS, map);
	}

	/**
	 * 通道同步
	 */
	public static String toChannelSyn(Map<String, String> map) {
		String repayAccessUrl = map.get("repayAccessUrl").toString();
		map.remove("repayAccessUrl");
		return baseNoClient(repayAccessUrl + Constants.CHANNEL_SYN, map);
	}


	/**
	 * 获取商户交易记录
	 *
	 * @param merNo
	 * @param recordDate1     起始日期
	 * @param recordDate2     截止日期
	 * @param debitCreditSide 收入\支出？
	 * @return {"msg":"查询成功","data":{"pageNum":0,"pageSize":10,"size":0,
	 * "orderBy":null,"startRow":0,"endRow":0,"total":0,"pages":0,"list"
	 * :[],"firstPage":0,"prePage":0,"nextPage":0,"lastPage":0,
	 * "isFirstPage":false,"isLastPage":true,"hasPreviousPage":false,
	 * "hasNextPage":false,"navigatePages":8,"navigatepageNums":[]},
	 * "status":true}
	 */
	public static String selectMerchantAccountTransInfoList(String merNo, String recordDate1, String recordDate2,
															String debitCreditSide, int page, int pageSize) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		final long iat = System.currentTimeMillis() / 1000l; // issued at claim
		final long exp = iat + 300L; // expires claim. In this case the token expires in 300 seconds
		final String jti = UUID.randomUUID().toString();
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);

//		claims.put("selectType", "2");
		claims.put("accountType", "M");
		claims.put("userId", merNo);
		claims.put("accountOwner", "000001");
		//add by tans 2017.3.31
//		claims.put("subjectNo", "224105");
		claims.put("currencyNo", "1");

		claims.put("recordDate1", recordDate1);
		claims.put("recordDate2", recordDate2);
		claims.put("debitCreditSide", debitCreditSide);

		Map<String, String> params = new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		params.put("page", String.valueOf(page));
		params.put("pageSize", String.valueOf(pageSize));
		return new ClientInterface(Constants.ACCOUNT_DETAIL_URL, params).postRequest();
	}

	/**
	 * 根据科目号查询商户的账户明细
	 *
	 * @param merNo
	 * @param subjectNo
	 * @param recordDate1
	 * @param recordDate2
	 * @param debitCreditSide
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public static String selectMerchantAccountTransInfoList(String merNo, String subjectNo,
															String recordDate1, String recordDate2,
															String debitCreditSide, int page, int pageSize) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		final long iat = System.currentTimeMillis() / 1000l; // issued at claim
		final long exp = iat + 300L; // expires claim. In this case the token expires in 300 seconds
		final String jti = UUID.randomUUID().toString();
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);

//		claims.put("selectType", "2");
		claims.put("accountType", "M");
		claims.put("userId", merNo);
		claims.put("accountOwner", "000001");
		claims.put("subjectNo", subjectNo);
		claims.put("currencyNo", "1");

		claims.put("recordDate1", recordDate1);
		claims.put("recordDate2", recordDate2);
		claims.put("debitCreditSide", debitCreditSide);

		Map<String, String> params = new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		params.put("page", String.valueOf(page));
		params.put("pageSize", String.valueOf(pageSize));
		String returnStr = new ClientInterface(Constants.ACCOUNT_DETAIL_URL, params).postRequest();
		log.info("查询账户明细，url:{},参数:{}", Constants.ACCOUNT_DETAIL_URL, claims);
		log.info("查询账户明细，返回结果:{}", returnStr);
		return returnStr;
	}

	public static String httpRecordAccount(CollectiveTransOrder info) throws ClientProtocolException, IOException {
		HashMap<String, Object> claims = new HashMap<String, Object>();
//		claims.putAll(new BeanMap(info));
		claims.put("fromSystem", "transSystem"); //交易系统（固定值）
		if (info.getTransTime() != null) {
			DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			claims.put("transDate", fmt.format(info.getTransTime()));//交易日期
		} else {
			claims.put("transDate", "");
		}
		claims.put("fromSerialNo", info.getOrderNo()); //交易流水号
		if (info.getAcqOrgId() != null) {
			claims.put("acqOrgId", info.getAcqOrgId().toString());//收单机构ID
		} else {
			claims.put("acqOrgId", "");
		}
		if (info.getAcqMerchantFee() != null) {
			claims.put("acqMerchantFee", format.format((info.getAcqMerchantFee())));//收单机构费率
		} else {
			claims.put("acqMerchantFee", "");
		}
		if (info.getTransAmount() != null) {
			claims.put("transAmount", format.format((info.getTransAmount())));//交易金额
		} else {
			claims.put("transAmount", "");//交易金额
		}
		claims.put("transOrderNo", info.getOrderNo());// 交易订单号
		claims.put("transTypeCode", "000001"); //T1交易记账（固定值）
		claims.put("cardNo", info.getAccountNo());//卡号
		claims.put("holidays", info.getHolidaysMark());//节假表标
//		claims.put("acqMerchantNo", scanCodeTrans.getAcqMerchantNo());//收单机构商户
		claims.put("acqServiceId", info.getAcqServiceId().toString());//收单服务ID
		claims.put("acqEnname", info.getAcqEnname());//收单机构英文名
		claims.put("merchantNo", info.getMerchantNo());//商户编号
		claims.put("serviceId", info.getServiceId().toString());//商户服务ID
		if (info.getMerchantFee() != null) {
			claims.put("merchantFee", format.format((info.getMerchantFee())));//商户手续费	
		} else {
			claims.put("merchantFee", "");
		}
		claims.put("oneAgentNo", info.getOneAgentNo());//一级代理商
		if (info.getProfits1() != null) {
			claims.put("agentShareAmount", info.getProfits1().toString());//一级代理商分润
		} else {
			claims.put("agentShareAmount", "0");
		}
		claims.put("directAgentNo", info.getAgentNo());//直属代理商
		claims.put("deviceSn", info.getDeviceSn());//sn
		claims.put("merchantRate", info.getMerchantRate());//sn
		claims.put("acqRate", info.getAcqMerchantRate());//sn
		if (info.getAcqMerchantFee() != null) {
			claims.put("acqOrgFee1", info.getAcqMerchantFee().toString());//sn
		} else {
			claims.put("acqOrgFee1", "");
		}
		//terminalNo
		return baseClient(Constants.TRANS_T0_RECORD_ACCOUNT, claims);
	}

	/**
	 * 存量代理商欢乐账户开设
	 * by ivan
	 *
	 * @param agentNo
	 * @return
	 */
	public static String levelOneCreateAcc(String agentNo) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("accountType", "A");
		claims.put("userId", agentNo);
		claims.put("subjectNo", "224106");
		return baseClient(Constants.HAPPY_SEND_ACCOUT_HOST, claims);
	}

	/**
	 * 欢乐送-财务核算记账
	 *
	 * @return
	 * @author Ivan
	 * @date 2017/03/29
	 */
	public static String happySendAgentRecordAccountForCaiWu(ActivityDetail ad) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("fromSystem", "boss");//来源系统
		claims.put("transTypeCode", "000042");//交易科目编号
		claims.put("oneAgentNo", String.valueOf(ad.getOneAgentNo()));//一级代理商编号
		claims.put("agentNo", String.valueOf(ad.getAgentNo()));//所属代理商编号
		claims.put("transAmount", String.valueOf(ad.getFrozenAmout()));//交易金额(核算冻结金额)
		claims.put("fromSerialNo", String.valueOf("B27-" + ad.getId()));//来源流水号
		claims.put("merchantNo", ad.getMerchantNo());//商户号
		if (ad.getTransTime() == null) {
			claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));//交易时间
		} else {
			claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd").format(ad.getTransTime()));//交易时间
		}
		claims.put("transOrderNo", String.valueOf(ad.getActiveOrder()));//交易订单号
		return baseClient(Constants.HAPPY_SEND_ACCOUT_HOST_FOR_CASIN, claims);
	}

	public static String createAccountByAcc(String agentNo, String subjectNo) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("accountType", "A");
		claims.put("userId", agentNo);
		claims.put("subjectNo", subjectNo);
		return baseClient(Constants.HAPPY_SEND_ACCOUT_HOST, claims);
	}

	/**
	 * 清算核算
	 *
	 * @param activityDetail
	 * @return
	 * @author tans
	 * @date 2017年6月27日 上午11:42:20
	 */
	public static String liquidation(ActivityDetail activityDetail) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("fromSystem", "boss");//来源系统
		claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(activityDetail.getTransTime()));//交易时间
		claims.put("transAmount", String.valueOf(activityDetail.getTransAmount()));//交易金额
		claims.put("cashBackAmount", String.valueOf(activityDetail.getCashBackAmount()));//返现金额
		claims.put("activeAmount", String.valueOf(activityDetail.getTransTotal()));//活动金额
		claims.put("fromSerialNo", String.valueOf("B37-" + activityDetail.getId()));//来源流水号
		claims.put("transTypeCode", "000037");//交易科目编号
		claims.put("agentNo", activityDetail.getOneAgentNo());//一级代理商编号
		claims.put("dongAgentNo", activityDetail.getAgentNo());//所属代理商编号
		claims.put("acqOrgId", String.valueOf(activityDetail.getAcqOrgId()));//收单机构英文名
		claims.put("transOrderNo", activityDetail.getActiveOrder());//交易订单号
		claims.put("acqMerchantFee", String.valueOf(activityDetail.getAcqMerchantFee()));//收单手续费
		String url = Constants.HAPPY_RETURN_ACCOUNT_80_CLEAR;
//		if("008".equals(activityDetail.getActivityCode())){
//			url = Constants.HAPPY_RETURN_ACCOUNT_80_CLEAR;
//		}
//		if("009".equals(activityDetail.getActivityCode())){
//			url = Constants.HAPPY_RETURN_ACCOUNT_150_CLEAR;
//		}
		return baseClient(url, claims);
	}

	/**
	 * 财务核算
	 *
	 * @param activityDetail
	 * @return
	 * @author tans
	 * @date 2017年6月27日 上午11:42:20
	 */
	public static String accountCheck(ActivityDetail activityDetail) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("fromSystem", "boss");//来源系统
		claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(activityDetail.getTransTime()));//交易时间
		claims.put("transAmount", String.valueOf(activityDetail.getTransAmount()));//交易金额
		claims.put("cashBackAmount", String.valueOf(activityDetail.getCashBackAmount()));//返现金额
		claims.put("activeAmount", String.valueOf(activityDetail.getTransTotal()));//活动金额
		claims.put("fromSerialNo", String.valueOf("B37-" + activityDetail.getId()));//来源流水号
		claims.put("transTypeCode", "000039");//交易科目编号
		claims.put("agentNo", activityDetail.getOneAgentNo());//代理商编号
		claims.put("acqOrgId", String.valueOf(activityDetail.getAcqOrgId()));//收单机构英文名
		claims.put("transOrderNo", activityDetail.getActiveOrder());//交易订单号
		claims.put("acqMerchantFee", String.valueOf(activityDetail.getAcqMerchantFee()));//收单手续费
		String url = Constants.HAPPY_RETURN_ACCOUNT_150_CHECK;
//		if("008".equals(activityDetail.getActivityCode())){
//			url = Constants.HAPPY_RETURN_ACCOUNT_80_CLEAR;
//		}
//		if("009".equals(activityDetail.getActivityCode())){
//			url = Constants.HAPPY_RETURN_ACCOUNT_150_CHECK;
//		}
		return baseClient(url, claims);
	}

	public static String subAgentAccount(CashBackDetail cashBackDetail, String url) {
		//查找cash_back_detail
		Map<String, Object> claims = new HashMap<>();
		claims.put("fromSystem", "boss");//来源系统
		claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cashBackDetail.getCreateTime()));//交易时间
		claims.put("amount", String.valueOf(cashBackDetail.getCashBackAmount()));//交易金额
		claims.put("agentNoUp", String.valueOf(cashBackDetail.getParentId()));//上级代理商编号
		claims.put("agentNoDown", String.valueOf(cashBackDetail.getAgentNo()));//当前代理商编号
		claims.put("fromSerialNo", String.valueOf(cashBackDetail.getActiveOrder() + "_" + cashBackDetail.getAgentNo()));//来源流水号
		claims.put("transTypeCode", "000086");//交易科目编号
		return baseClient(url, claims);
	}

	//欢乐返记账失败，重新记账
	public static String hlfRecode(CollectiveTransOrder order) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("fromSystem", "transSystem"); //交易系统（固定值）
		claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd").format(order.getTransTime()));//交易日期
		claims.put("transAmount", order.getTransAmount().setScale(2, RoundingMode.HALF_UP).toString());//交易金额
		claims.put("fromSerialNo", order.getOrderNo()); //交易订单号做唯一标识
		claims.put("transTypeCode", "000036"); //欢乐送交易记账（固定值）
		claims.put("acqOrgId", String.valueOf(order.getAcqOrgId()));//收单机构ID
		claims.put("acqMerchantNo", order.getAcqMerchantNo());//收单机构商户
		claims.put("acqServiceId", order.getAcqServiceId().toString());//收单服务ID
		claims.put("acqMerchantFee", order.getAcqMerchantFee().setScale(2, RoundingMode.HALF_UP).toString());//收单机构费率
		claims.put("agentNo", order.getAgentNo());
		String account = baseClient(Constants.HLF_RECORD_ACCOUT, claims);
		log.info("欢乐返首笔激活订单：{}，发送记账：{}", order.getOrderNo(), account);
		return account;
	}

	//邀请有奖，入账
	public static String invitePrizseRecord(InvitePrizesMerchantInfo info) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		String account = "";
		claims.put("fromSystem", "boss"); //交易系统（固定值）
		claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd").format(info.getCreateTime()));//创建时间
		claims.put("amount", info.getPrizesAmount().setScale(2, RoundingMode.HALF_UP).toString());//入账金额
		claims.put("fromSerialNo", String.valueOf(info.getId())); //主键做唯一标识
		if ("1".equals(info.getPrizesType())) {
			// 商户
			claims.put("merchantNo", info.getPrizesObject());//商户编号
			claims.put("agentNo", info.getAgentNo());//所属代理商编号
			claims.put("transTypeCode", "000048"); //邀请有奖000048商户入账（固定值）
			account = baseClient(Constants.YQYJ_RECORD_ACCOUT_MERCHANT, claims);
			log.info("邀请有奖商户入账，InvitePrizesMerchantInfo：{}", info);
		} else if ("2".equals(info.getPrizesType())) {
			// 代理商
			claims.put("oneAgentNo", info.getPrizesObject());//一级代理商编号
			claims.put("agentNo", info.getAgentNo());//所属代理商编号
			claims.put("transTypeCode", "000041"); //邀请有奖000041代理商入账（固定值）
			account = baseClient(Constants.YQYJ_RECORD_ACCOUT, claims);
			log.info("邀请有奖代理商入账，InvitePrizesMerchantInfo：{}", info);
		}
		return account;
	}

	//信用卡还款-结算订单管理，再次出款
	public static String repayAgainPayment(String orderNo) {
		final HashMap<String, String> claims = new HashMap<>();
		String key = "21e57d89ff18dc78ec5251062ba871d7";//key
		String signStr = "orderNo=" + orderNo + key;
		claims.put("orderNo", orderNo);//订单号
		claims.put("sign", Md5.md5Str(signStr));
		log.info("请求路径：{},参数：{}", Constants.REPAY_AGAIN_PAYMENT, JSONObject.toJSONString(claims));
		String returnStr = new ClientInterface(Constants.REPAY_AGAIN_PAYMENT, claims).postRequest();
		log.info("返回结果：{}", returnStr);
		return returnStr;
	}

	//信用卡还款-开户
	public static String creditMerchantOpenAccount(String merchantNo) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("accountType", "M");//类型
		claims.put("subjectNoStr", "224113,224101001");//科目号用逗号隔开"224113,224101001"
		claims.put("userId", merchantNo);//商户号
		return baseClientBySecret(Constants.CREDIT_MERCHANT_OPEN_ACCOUNT, claims, Constants.NEW_ACCOUNT_API_SECURITY);
	}

	//信用卡还款-查询用户金额
	public static String repayAccountAmountInfo(String merchantNo) {
		final HashMap<String, String> claims = new HashMap<>();
		claims.put("merNo", merchantNo);
		log.info("请求路径：{},参数：{}", Constants.REPAY_ACCOUNT_AMOUNT_INFO, JSONObject.toJSONString(claims));
		String returnStr = new ClientInterface(Constants.REPAY_ACCOUNT_AMOUNT_INFO, claims).postRequest();
		log.info("返回结果：{}", returnStr);
		return returnStr;
	}

	//商户上游账户查询
	public static String sftAccountSearch(String baseurl, String merchantNo) {
		final HashMap<String, String> claims = new HashMap<>();
		String str = "zfMerchant/sftAccountSearch";
		String url = baseurl + str;
		claims.put("merchantNo", merchantNo);
		log.info("请求路径：{},参数：{}", url, JSONObject.toJSONString(claims));
		String returnStr = new ClientInterface(url, claims).postRequest();
		log.info("返回结果：{}", returnStr);
		return returnStr;
	}

	//差错账优化，同步交易状态
	public static String syncTransStatus(String orderNos) {
		final HashMap<String, String> claims = new HashMap<>();
		claims.put("orders", orderNos);
		log.info("请求路径：{},参数：{}", Constants.SYNC_TRANS_STATUS, JSONObject.toJSONString(claims));
		String returnStr = new ClientInterface(Constants.SYNC_TRANS_STATUS, claims).postRequest();
		log.info("返回结果：{}", returnStr);
		return returnStr;
	}

	/**
	 * 欢乐返商户-奖励入账(奖励)
	 * liuks 2017-12-26
	 */
	public static String happyBackDaYuRecordAccount(ActivityDetail info) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		String account = "";
		claims.put("agentLevel", "1");//当前代理商级别
		claims.put("agentNo", info.getOneAgentNo());//当前代理商23223
		claims.put("amount", info.getFullAmount().setScale(2, RoundingMode.HALF_UP).toString());//入账金额
		claims.put("fromSerialNo", String.valueOf(info.getId()));//来源系统流水号，每笔唯一，发起端需保存
		claims.put("fromSystem", "boss");//来源系统固定
		claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));//交易时间
		claims.put("transTypeCode", "000061");//交易码固定
		claims.put("transOrderNo", info.getActiveOrder());//当前订单编号
		claims.put("merchantNo", info.getMerchantNo());// 当前商户号
		claims.put("agentNode", "0-"+info.getOneAgentNo()+"-");// 当前代理商节点
		log.info("欢乐返商户-奖励入账(奖励),ActivityDetail:{}", info);
		account = baseClient(Constants.HAPPY_BACK_DA_YU, claims);
		log.info("欢乐返商户-奖励入账(奖励)，returnMsg:{}", account);
		return account;
	}

	/**
	 * 欢乐返商户-奖励入账(扣减)
	 * liuks 2017-12-26
	 */
	public static String happyBackXiaoYuRecordAccount(ActivityDetail info) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		String account = "";
		claims.put("fromSystem", "boss"); //交易系统（固定值）
		claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));//创建时间
		claims.put("fromSerialNo", String.valueOf(info.getId())); //主键做唯一标识
		claims.put("transTypeCode", "000060"); //欢乐返商户-奖励入账(扣减)（固定值）
		claims.put("merchantNo", info.getMerchantNo());
		claims.put("agentNo", info.getOneAgentNo());
		claims.put("agentName", info.getOneAgentName());
		claims.put("transOrderNo", info.getActiveOrder());//当前订单编号
		claims.put("amount", info.getEmptyAmount().setScale(2, RoundingMode.HALF_UP).toString());//入账金额
		log.info("欢乐返商户-奖励入账(扣减),ActivityDetail:{}", info);
		account = baseClient(Constants.HAPPY_BACK_XIAO_YU, claims);
		log.info("欢乐返商户-奖励入账(扣减)，returnMsg:{}", account);
		return account;
	}

	/**
	 * 欢乐返商户-奖励入账(全级奖励)
	 */
	public static String happyBackDaYuRecordAccount2(String url,CashBackDetail info,String merchantNo) {
		url += "/happyBackController/happyBackDaYuRecordAccount.do";
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		String response = "";
		claims.put("agentLevel", info.getAgentLevel());//当前代理商级别
		claims.put("agentNo", info.getAgentNo());//当前代理商23223
		claims.put("amount", info.getCashBackAmount().setScale(2, RoundingMode.HALF_UP).toString());//入账金额
		claims.put("fromSerialNo", "CBD-"+String.valueOf(info.getId()));//来源系统流水号，每笔唯一，发起端需保存
		claims.put("fromSystem", "boss");//来源系统固定
		claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));//交易时间
		claims.put("transTypeCode", "000061");//交易码固定
		claims.put("transOrderNo", info.getActiveOrder());//当前订单编号
		claims.put("merchantNo", merchantNo);// 当前商户号
		claims.put("agentNode", info.getAgentNode());// 当前代理商节点
		log.info("欢乐返商户-奖励入账(全级奖励),url:{},ActivityDetail:{}", url,info);
		response = baseClient(url, claims);
		log.info("欢乐返商户-奖励入账(全级奖励)，returnMsg:{}", response);
		return response;
	}

	/**
	 * 欢乐返商户-奖励入账(全级扣减)
	 */
	public static String newHappyBackXiaoYuRecordAccount(String url,CashBackDetail info,String merchantNo) {
		url += "/happyBackController/newHappyBackXiaoYuRecordAccount.do";
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		String response = "";
		claims.put("fromSystem", "boss");//来源系统固定
		claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));//交易时间
		claims.put("amount", info.getCashBackAmount().setScale(2, RoundingMode.HALF_UP).toString());//待扣金额
		claims.put("fromSerialNo", "CBD-"+String.valueOf(info.getId()));//来源系统流水号，每笔唯一，发起端需保存
		claims.put("transTypeCode", info.getAgentLevel().equals("1")?"000060":"000126");// 如果是当前代理商一级传000060，二级则传000126
		claims.put("parentAgentNo", info.getAgentLevel().equals("1")?"":info.getParentId());//父级代理商
		claims.put("currentAgentNo", info.getAgentNo());//当前代理商23223
		claims.put("currentLevel", info.getAgentLevel());//当前代理商 级别
		claims.put("transOrderNo", info.getActiveOrder());// 当前订单编号
		claims.put("agentName", info.getAgentName());// 当前代理商名称
		claims.put("agentNode", info.getAgentNode());// 当前代理商节点
		claims.put("merchantNo", merchantNo);// 当前商户号
		log.info("欢乐返商户-奖励入账(全级扣减),url:{},ActivityDetail:{}", url,info);
		response = baseClient(url, claims);
		log.info("欢乐返商户-奖励入账(全级扣减)，returnMsg:{}", response);
		return response;
	}

	/**  交易码
	 *  000121   超级盟主欢乐返满奖入账
	 *  000122  超级盟主交易分润(VIP类)
	 *
	 * @throws Exception
	 */
	public static String peragentAccountDemo(String url,ActivityDetail ad) throws Exception {
		final String secret = "zouruijin";
		final long iat = System.currentTimeMillis() / 1000l; // issued at claim
		final long exp = iat + 60L; // expires claim. In this case the token
		// expires in 60 seconds
		final String jti = UUID.randomUUID().toString();
		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);
		//以下参数都为字符串
		claims.put("agentNo", ad.getOneAgentNo());// 超级盟主商编号
		claims.put("amount", ad.getFullAmount().setScale(2, RoundingMode.HALF_UP).toString());// 分润金额
		claims.put("transTypeCode", "000121");// 交易码固定  // 000121    超级盟主欢乐返满奖入账
		// 000122    超级盟主交易分润(VIP类)
		claims.put("fromSerialNo", String.valueOf(ad.getId()));// 来源系统流水号
		claims.put("transOrderNo", String.valueOf(ad.getId()));// 发生交易订单号
		claims.put("fromSystem", "SaSystem");// 来源系统固定
		claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));// 记账日期

		final String token = signer.sign(claims);
		Map<String, String> params=new HashMap<String, String>();
		params.put("token", token);
		System.out.println(url);
		String response = new ClientInterface(url, params).postRequest();
		System.out.println("response:" + response);
		return response;
	}

	/**
	 * 超级银行家，计算分润
	 *
	 * @param batchNo
	 * @return
	 */
	public static String checkOrderProfit(String batchNo) {
		String url = Constants.SUPER_BANK_CHECK_ORDER_PROFIT;
		String sign = Md5.md5Str(batchNo + "&" + Constants.SUPER_BANK_SECRET);
		Map<String, String> map = new HashMap<String, String>();
		map.put("batchNo", batchNo);
		map.put("sign", sign);
		log.info("请求路径：{},参数：{}", url, JSONObject.toJSONString(map));
		String rsStr = httpPost2(url, map);
		log.info("返回结果：{}", rsStr);
		return rsStr;
	}

	/**
	 * 超级银行家，计算办卡分润
	 *
	 * @param batchNo
	 * @return
	 */
	public static String checkBankProfit(String batchNo, String bonus) {
		String url = Constants.SUPER_BANK_BANKA_ORDER_PROFIT;
		if ("2".equals(bonus)) {
			url = Constants.SUPER_BANK_SHOUSHUA_ORDER_PROFIT;
		}
		String sign = Md5.md5Str(batchNo + "&" + Constants.SUPER_BANK_SECRET);
		Map<String, String> map = new HashMap<String, String>();
		map.put("batchNo", batchNo);
		map.put("sign", sign);
		log.info("请求路径：{},参数：{}", url, JSONObject.toJSONString(map));
		String rsStr = httpPost2(url, map);
		log.info("返回结果：{}", rsStr);
		return rsStr;
	}


	/**
	 * 超级银行家，分润入账
	 *
	 * @param order
	 * @return
	 */
	public static String profitAccount(OrderMain order) {
		final HashMap<String, Object> claims = new HashMap<>();
		claims.put("fromSystem", "superbank");
		claims.put("transDate", order.getCreateDateStr());
		claims.put("fromSerialNo", order.getOrderNo());
		claims.put("transTypeCode", "000059");//固定值
		claims.put("transOrderNo", order.getOrderNo());
		claims.put("merchantNo1", order.getOneUserCode());
		if (StringUtils.isNotBlank(order.getOneUserCode())) {
			claims.put("mchProfitAmount1", String.valueOf(order.getOneUserProfit()));
		}
		claims.put("merchantNo2", order.getTwoUserCode());
		if (StringUtils.isNotBlank(order.getTwoUserCode())) {
			claims.put("mchProfitAmount2", String.valueOf(order.getTwoUserProfit()));
		}
		claims.put("merchantNo3", order.getThrUserCode());
		if (StringUtils.isNotBlank(order.getThrUserCode())) {
			claims.put("mchProfitAmount3", String.valueOf(order.getThrUserProfit()));
		}
		claims.put("merchantNo4", order.getFouUserCode());
		if (StringUtils.isNotBlank(order.getFouUserCode())) {
			claims.put("mchProfitAmount4", String.valueOf(order.getFouUserProfit()));
		}
		claims.put("agentNo", String.valueOf(order.getOrgId()));
		claims.put("agentProfitAmount", String.valueOf(order.getOrgProfit()));
		return baseClient(Constants.SUPER_BANK_PROFIT_ACCOUNT, claims);
	}


	/**
	 * 超级银行家红包，通过busType和url刷新
	 */
	public static String redProductPush(String orgId, String url, String busType) {
		final HashMap<String, String> claims = new HashMap<>();
		String sign = null;
		if (!StringUtils.isBlank(orgId)) {
			claims.put("orgId", orgId);
			sign = Md5.md5Str(orgId + "&" + Constants.SUPER_BANK_SECRET);
		} else {
			sign = Md5.md5Str(busType + "&" + Constants.SUPER_BANK_SECRET);
		}
		claims.put("busType", busType);
		claims.put("sign", sign);
		log.info("请求路径：{},参数：{}", url, JSONObject.toJSONString(claims));
		String rsStr = httpPost2(url, claims);
		log.info("返回结果：{}", rsStr);
		return rsStr;
	}

	/**
	 * 超级银行家，通过orgId和url刷新
	 */
	public static String superBankPushByOrgIdAndUrl(String orgId, String url, String productType, String sourceId) {
		String sign = Md5.md5Str(orgId + "&" + Constants.SUPER_BANK_SECRET);
		final HashMap<String, String> claims = new HashMap<>();
		claims.put("sign", sign);
		claims.put("orgId", orgId);
		if (!StringUtils.isBlank(productType)) {
			claims.put("productType", productType);
		}
		if (!StringUtils.isBlank(sourceId)) {
			claims.put("sourceId", sourceId);
		}
		log.info("请求路径：{},参数：{}", url, JSONObject.toJSONString(claims));
		String rsStr = httpPost2(url, claims);
		log.info("返回结果：{}", rsStr);
		return rsStr;
	}

	/**
	 * 超级银行家，通过orgId和url刷新
	 */
	public static String superBankSysOption(String url) {
		final HashMap<String, String> claims = new HashMap<>();
		claims.put("sign", "cb3890a396dfd3bea9cf5eab030d4735");
		claims.put("busType", "666");
		log.info("请求路径：{},参数：{}", url, JSONObject.toJSONString(claims));
		String rsStr = httpPost2(url, claims);
		log.info("返回结果：{}", rsStr);
		return rsStr;
	}

	/**
	 * 超级银行家，通过orgId和url刷新
	 */
	public static String superBankPushLoanSource(String id, String url) {
		String sign = Md5.md5Str(id + "&" + Constants.SUPER_BANK_SECRET);
		final HashMap<String, String> claims = new HashMap<>();
		claims.put("sign", sign);
		claims.put("id", id);
		log.info("请求路径：{},参数：{}", url, JSONObject.toJSONString(claims));
		String rsStr = httpPost2(url, claims);
		log.info("返回结果：{}", rsStr);
		return rsStr;
	}


	/**
	 * 超级银行家，微信公众号推送
	 *
	 * @param orderNo
	 */
	public static void superBankPush(String orderNo) {
		String sign = Md5.md5Str(orderNo + "&" + Constants.SUPER_BANK_SECRET);
		final HashMap<String, String> claims = new HashMap<>();
		String url = Constants.SUPER_BANK_PUSH;
		claims.put("sign", sign);
		claims.put("orderNo", orderNo);
		log.info("请求路径：{},参数：{}", url, JSONObject.toJSONString(claims));
		String rsStr = httpPost2(url, claims);
		log.info("返回结果：{}", rsStr);
		return;
	}

	/**
	 * 公共httpPost请求方法
	 */
	@SuppressWarnings("rawtypes")
	public static String httpPost(String url, Map<String, String> map) {
		String jsonStr = "";
		CloseableHttpClient httpclient = null;
		HttpPost httppost = null;
		try {
			httpclient = HttpClientBuilder.create().build();
			httppost = new HttpPost(url);
			//配置请求的超时设置
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(6000)
					.setConnectTimeout(6000)
					.setSocketTimeout(30000).build();
			httppost.setConfig(requestConfig);
			if (null != map) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				Iterator iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					params.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
				}
				httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			}
			CloseableHttpResponse response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity rs = response.getEntity();
				jsonStr = EntityUtils.toString(rs);
			}
			httppost.releaseConnection();
			httpclient.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != httppost) {
					httppost.releaseConnection();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				if (null != httpclient) {
					httpclient.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return jsonStr;
	}

	/**
	 * 商户 | 代理商开户
	 *
	 * @param subjectNo
	 * @param userType
	 * @param userId
	 * @return
	 */
	public static String createAccount(String subjectNo, String userType, String userId) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("accountType", userType);
		claims.put("userId", userId);
		claims.put("subjectNo", subjectNo);
		return baseClient(Constants.HAPPY_SEND_ACCOUT_HOST, claims);
	}

	/**
	 * 创建超级还OEM
	 *
	 * @param orgInfo
	 * @return
	 */
	public static String createRepayOem(OrgInfo orgInfo) {
		String url = Constants.CREATE_REPAY_OEM;
		String returnStr = "";
		if (orgInfo == null || StringUtils.isBlank(orgInfo.getOrgName()) || StringUtils.isBlank(orgInfo.getV2AgentNumber())
				|| orgInfo.getTradeFeeRate() == null || orgInfo.getTradeSingleFee() == null
				|| orgInfo.getWithdrawFeeRate() == null || orgInfo.getWithdrawSingleFee() == null
				|| StringUtils.isBlank(orgInfo.getCompanyNo()) || StringUtils.isBlank(orgInfo.getCompanyName())
				|| StringUtils.isBlank(orgInfo.getServicePhone())) {
			returnStr = "{\"status\":403,\"msg\":\"参数不能为空\",\"data\":null}";
			return returnStr;
		}
		Map<String, String> map = new HashMap<>();
		map.put("oem_name", orgInfo.getOrgName());
		map.put("agent_no", orgInfo.getV2AgentNumber());
		map.put("trade_fee_rate", String.valueOf(orgInfo.getTradeFeeRate() / 100d));
		map.put("trade_single_fee", String.valueOf(orgInfo.getTradeSingleFee()));
		map.put("perfect_repay_fee_rate", String.valueOf(orgInfo.getTradeFeeRateWmjh() / 100d));
		map.put("perfect_repay_single_fee", String.valueOf(orgInfo.getTradeSingleFeeWmjh()));
		map.put("withdraw_fee_rate", String.valueOf(orgInfo.getWithdrawFeeRate() / 100d));
		map.put("withdraw_single_fee", String.valueOf(orgInfo.getWithdrawSingleFee()));
		map.put("company_no", String.valueOf(orgInfo.getCompanyNo()));
		map.put("company_name", String.valueOf(orgInfo.getCompanyName()));
		map.put("service_phone", String.valueOf(orgInfo.getServicePhone()));
		log.info("请求路径:{},参数:{}", url, JSONObject.toJSONString(map));
		returnStr = httpPost(url, map);
		log.info("返回结果:{}", returnStr);
		return returnStr;
	}

	/**
	 * 公共httpPost请求方法
	 */
	public static String httpPost2(String url, Map<String, String> map) {
		String jsonStr = "";
		CloseableHttpClient httpclient = null;
		HttpPost httppost = null;
		try {
			httpclient = HttpClientBuilder.create().build();
			httppost = new HttpPost(url);
			//配置请求的超时设置
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(60000)
					.setConnectTimeout(60000)
					.setSocketTimeout(60000).build();
			httppost.setConfig(requestConfig);
			httppost.setHeader("Content-Type", "application/json");

			StringEntity s = new StringEntity(JSONObject.toJSONString(map));
			s.setContentEncoding("UTF-8");
			httppost.setEntity(s);

			CloseableHttpResponse response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity rs = response.getEntity();
				jsonStr = EntityUtils.toString(rs);
			}
			httppost.releaseConnection();
			httpclient.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != httppost) {
					httppost.releaseConnection();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				if (null != httpclient) {
					httpclient.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return jsonStr;
	}

	/**
	 * 券查询优化接口,通知core清除缓存
	 *
	 * @param state 1 活动开关 2 代理商控制开关 3 可参与活动代理商列表 4 可购买券列表
	 * @return
	 */
	public static String flushActivityCache(int state) {
		String type = null;
		if (state == 1) {
			type = "openActivity";  //活动开关
		} else if (state == 2) {
			type = "openAgent";//代理商控制
		} else if (state == 3) {
			type = "agentList";//可参与活动代理商列表
		} else if (state == 4) {
			type = "rechargeList"; //可购买券列表
		}
		final HashMap<String, String> claims = new HashMap<>();
		claims.put("type", type);
		log.info("券查询优化接口,通知core清除缓存:{},参数：{}", Constants.FLUSH_ACTIVITY_CACHE, JSONObject.toJSONString(claims));
		String returnStr = "";
		try {
			returnStr = new ClientInterface(Constants.FLUSH_ACTIVITY_CACHE, claims).postRequest();
			log.info("券查询优化接口,通知core清除缓存返回结果:{}", returnStr);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("券查询优化接口请求失败:" + e.getMessage());
		}
		return returnStr;
	}

	// 超级还-挂起订单处理
	public static String wakePerfectRepayPlan(String batchNo, String operator) {
		String url = Constants.WAKE_PERFECT_REPAY_PLAN;
		final HashMap<String, String> claims = new HashMap<>();
		claims.put("batchNos", batchNo);
		claims.put("operator", operator);
		log.info("请求路径：{},参数：{}", url, JSONObject.toJSONString(claims));
		String returnStr = new ClientInterface(url, claims).postRequest();
		log.info("返回结果：{}", returnStr);
		return returnStr;
	}

	/**
	 * 调用core接口，绑定或解绑机具
	 *
	 * @param url
	 * @param sn
	 * @param merNo
	 * @param mbpId
	 * @param type
	 * @return
	 */
	public static String bindingOrUnBindTerminal(String url, String sn, String merNo, String mbpId, String type) {
		Map<String, String> claims = new HashMap<>();
		claims.put("sn", sn);
		claims.put("merchantNo", merNo);
		claims.put("mbpId", mbpId);
		claims.put("type", type);
		log.info("调用机具的绑定或解绑,url:{},参数：{}", url, JSONObject.toJSONString(claims));
		String returnStr = new ClientInterface(url, claims).postRequest();
		log.info("返回结果:{}", returnStr);
		return returnStr;
	}

	/**
	 * 修改商户时，调用core接口，同步商户信息
	 *
	 * @param url
	 * @param merNo
	 * @param mbpId
	 * @param bpId
	 * @return
	 */
	public static String ysUpdateMerInfo(String url, String merNo, String mbpId, String bpId) {
		Map<String, String> claims = new HashMap<>();
		claims.put("merchantNo", merNo);
		claims.put("mbpId", mbpId);
		claims.put("bpId", bpId);
		log.info("调用core接口，同步商户信息,url:{},参数：{}", url, JSONObject.toJSONString(claims));
		String returnStr = new ClientInterface(url, claims).postRequest();
		log.info("返回结果:{}", returnStr);
		return returnStr;
	}

	/**
	 * 彩票生成主订单和分润
	 *
	 * @param batchNo
	 * @return
	 */
	public static String lotteryGenOrderAndProfit(String batchNo, String url) {
		String sign = Md5.md5Str(batchNo + "&" + Constants.SUPER_BANK_SECRET);

		Map<String, String> claims = new HashMap<>();
		claims.put("batchNo", batchNo);
		claims.put("sign", sign);
		log.info("调用彩票生成主订单和分润,url:{},参数：{}", url, JSONObject.toJSONString(claims));
		log.info("请求路径：{},参数：{}", url, JSONObject.toJSONString(claims));
		String rsStr = httpPost2(url, claims);
		log.info("返回结果1：{}", rsStr);
		return rsStr;
	}

	/**
	 * 超级银行家，通过ruleId刷新排行榜缓存
	 *
	 */
	public static String superBankPushRankingRule(String ruleId, String url) {
		String sign = Md5.md5Str(ruleId + "&" + Constants.SUPER_BANK_SECRET);
		final HashMap<String, String> claims = new HashMap<>();
		claims.put("sign", sign);
		claims.put("ruleId", ruleId);
		log.info("请求路径：{},参数：{}", url, JSONObject.toJSONString(claims));
		String rsStr = httpPost2(url, claims);
		log.info("返回结果：{}", rsStr);
		return rsStr;
	}

	/**
	 * 超级盟主商注册
	 *
	 * @param agentInfo
	 * @param url
	 * @return
	 */
	public static String allAgentUserToReg(AgentInfo agentInfo, String url) {
		Map<String, String> claims = new HashMap<>();
		claims.put("regType", "boss");//注册类型  app 超级盟主商app 注册  h5  H5连接注册 ,agent 代理商注册 ,merchant 商户注册
		claims.put("mobile", agentInfo.getMobilephone());//手机号
		claims.put("brandCode", agentInfo.getAgentOem());//所属品牌
		claims.put("userType", "1");//用户类型  1-机构，2-大盟主，3-盟主
		claims.put("recCode", "");//推荐码,如果是注册机构就不用填，如果是注册大盟主必填(值为其机构的user_code)
		claims.put("agentNo", agentInfo.getAgentNo());//代理商编号
		claims.put("agentNode", agentInfo.getAgentNode());//代理商节点
		claims.put("creater", agentInfo.getCreator());//创建人
		claims.put("sign", Md5.reqSign(claims));//加密
		return baseNoClient(url, claims);
	}

	/**
	 * 超级盟主入账
	 *
	 * @param info
	 * @param url
	 * @return
	 */
	public static String allAgentAccActOrder(ActivityDetail info, String url) {
		Map<String, String> claims = new HashMap<>();
		claims.put("order_no", info.getActiveOrder());//订单号
		return httpPost(url, claims);
	}
	
	/**
	 * 	刷新红包业务组织分类缓存
	 * @param orgId
	 * @param url
	 * @return
	 */
	public static String refreshRedOrgSortCache(String orgId, String url) {
		String sign = Md5.md5Str(orgId + "&" + Constants.SUPER_BANK_SECRET);
		final HashMap<String, String> claims = new HashMap<>();
		claims.put("sign", sign);
		claims.put("orgId", orgId);
		log.info("请求路径：{},参数：{}", url, JSONObject.toJSONString(claims));
		String rsStr = httpPost2(url, claims);
		log.info("返回结果：{}", rsStr);
		return rsStr;
	}

	/**
	 * 账户API查询调账和冻结金额接口
	 *
	 * @param agentNo
	 * @return
	 * @author lmc
	 */
	public static String findAgentPreAdjustBalance(String agentNo, String url) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("subjectNo", "224106");//交易科目编号
		claims.put("agentNo",agentNo);//代理商编号
		return baseClient(url, claims);
	}

	/**
	 * 代理商活动补贴账户预调帐处理
	 *
	 * @param agentNo
	 * @author lmc
	 */
	public static String agentPreAdjustBalance(String agentNo, String agentName, BigDecimal amount, String agentLevel, String url) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("agentNo", agentNo);//代理商编号
		claims.put("agentName", agentName);//代理商名称
		claims.put("amount", amount.toString());//调账金额
		claims.put("agentLevel", agentLevel);//代理商级别
		claims.put("autoMark", "autoMark");//区分参数
		return baseClient(url, claims);
	}

	/**
	 * 欢乐返账户入账
	 * @author lmc
	 */
	public static String happyBackAccounting(CashBackDetail cashBackDetail, String url,String merchantNo) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("agentLevel", cashBackDetail.getAgentLevel());//当前代理商 级别
		claims.put("agentNo", cashBackDetail.getAgentNo());//代理商编号
		claims.put("cashBackAmount", String.valueOf(cashBackDetail.getCashBackAmount()));//返现金额
		claims.put("fromSerialNo", String.valueOf("B37-" + cashBackDetail.getId()));//来源系统流水号，每笔唯一，发起端需保存
		claims.put("transOrderNo", cashBackDetail.getActiveOrder());//当前订单编号
		claims.put("fromSystem", "transSystem");//来源系统固定
		claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cashBackDetail.getCreateTime()));//交易时间
		claims.put("transTypeCode", "000119");//交易码固定
		claims.put("merchantNo", merchantNo);// 当前商户号
		claims.put("agentNode", cashBackDetail.getAgentNode());// 当前代理商节点23226-23223-
		return baseClient(url, claims);
	}

	/**
	 * 超级推商户批量入账
	 */
	public static String cjtRecharge(CjtProfitDetail baseInfo, String url) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		String returnMsg = "";
		claims.put("fromSystem", "boss"); //交易系统（固定值）
		claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd").format(baseInfo.getCreateTime()));//创建时间
		claims.put("amount", String.valueOf(baseInfo.getProfitAmount()));//入账金额
		claims.put("fromSerialNo", String.valueOf(baseInfo.getOrderNo())); //分润明细流水号做唯一标识
		claims.put("merchantNo", baseInfo.getMerchantNo());//商户编号
		claims.put("transTypeCode", "CJT1005"); //交易类型编号
		returnMsg = baseClient(url, claims);
		return returnMsg;
	}
	/**
	 * 超级推代理商批量入账
	 */
	public static String cjtRechargeAgent(CjtProfitDetail baseInfo,AgentInfo agentInfo, String url) {
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		String returnMsg = "";
		claims.put("fromSystem", "boss"); //交易系统（固定值）
		claims.put("transDate", new SimpleDateFormat("yyyy-MM-dd").format(baseInfo.getCreateTime()));//创建时间
		claims.put("amount", String.valueOf(baseInfo.getProfitAmount()));//入账金额
		claims.put("fromSerialNo", "agent_"+String.valueOf(baseInfo.getOrderNo())); //分润明细流水号做唯一标识
		claims.put("agentNo", baseInfo.getMerchantNo());//商户编号
		claims.put("agentNode", agentInfo.getAgentNode());//代理商节点
		claims.put("merchantNo", baseInfo.getFromMerchantNo());//收益来源商户编号
		claims.put("agentLevel", agentInfo.getAgentLevel().toString());//代理商等级
		claims.put("transTypeCode", "CJT1009"); //交易类型编号
		returnMsg = baseClient(url, claims);
		return returnMsg;
	}

	public static String postBodyRequest(String url, Map<String, Object> paramMap) {
		String param = JSONObject.toJSONString(paramMap);
		log.info("请求路径：{},参数：{}", url, param);
		String returnStr = new ClientInterface(url, null).postRequestBody(param);
		log.info("返回结果：{}", returnStr);
		return returnStr;
	}

	public static String merchantMigrate(String url) {
		Map<String, String> params = new HashMap<>();
		String key="46940880d9f79f27bb7f85ca67102bfdylkj@@agentapi2#$$^&pretty";
		Long timestamp=DateUtil.dateToUnixTimestamp();
		String returnMsg = "";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("timestamp", timestamp + "");
		headers.put("sign", Md5.md5Str("timestamp="+timestamp+"&key="+key));
		headers.put("app-info", JSONObject.toJSONString(headers));
		log.info("请求路径：{},参数：{}", url, params);
		returnMsg = new ClientInterface(url, headers, params).getRequest();
		log.info("返回结果：{}", returnMsg);
		return returnMsg;
	}

	public static String updateVipInfo(String url,MerchantInfo merchantInfo,String telNo,String businessNo,String teamId,String key) {
		url+="/vipinfo/updateVipInfo";
		Map<String,Object> leaguerInfo = new HashMap<>();
		leaguerInfo.put("leaguerName", merchantInfo.getMerchantName());
		leaguerInfo.put("businessNo", businessNo);
		leaguerInfo.put("originUserNo", merchantInfo.getMerchantNo());
		leaguerInfo.put("mobilePhone", telNo);
		leaguerInfo.put("newMobilePhone", merchantInfo.getMobilephone());// 修改手机号是传新手机号
		leaguerInfo.put("idCardNo", merchantInfo.getIdCardNo());
		leaguerInfo.put("teamId",teamId);
		String data = JSON.toJSONString(leaguerInfo);
		Long timestamp=DateUtil.dateToUnixTimestamp();
		String signStr="businessNo="+businessNo+"&data="+data+"&timestamp="+timestamp+key;
		//log.info("------------"+signStr);
		String sign = Md5.md5Str(signStr);
		Map<String,String> params = new HashMap<>();
		params.put("businessNo",businessNo);
		params.put("data",data);
		params.put("sign",sign);
		params.put("timestamp",timestamp+"");
		log.info("请求路径：{},参数：{}", url, params);
		String returnMsg = new ClientInterface(url, null).postRequest2(params);
		log.info("返回结果：{}", returnMsg);
		return returnMsg;
	}

    /**
     * 出款userId
     * transfer_modify_settle_card(出款:修改结算卡)
     * transfer_relieve_merchant_blacklist(出款:解除商户黑名单)
     * transfer_relieve_purse_blacklist(出款:解除钱包黑名单)
     * transfer_relieve_quick_pass_blacklist(出款:解除闪付黑名单)
     * @param transferId
     * @param userId
     * @return
     */
	public static String resetTransfer(String transferId,String userId){
		Map<String, String> map=new HashMap<String, String>();
		String url=Constants.SETTLE_TRANS+"?transferId="+transferId+"&userId="+userId;
		String result=ClientInterface.baseNoClient(url,map);
		log.info("手工结算，出款url：" + url);
		log.info("手工结算，出款返回信息：" + result);
		return result;
	}

	/**
	 * 新欢乐送一级代理商奖励
	 * @param info
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String xhlfOneAgentAccount(XhlfAgentAccountDetail info, String url) {
		Map<String, Object> claims = new HashMap<String, Object>();
		//以下参数均必填
		claims.put("agentNo", info.getAgentNo()); //当前代理商
		claims.put("agentLevel", info.getAgentLevel()); //当前代理商级别
		claims.put("agentNode", info.getAgentNode()); // 当前代理商节点
		claims.put("merchantNo", info.getMerchantNo()); // 当前商户号
		claims.put("amount", info.getAmount().toString()); //入账金额
		claims.put("fromSerialNo", String.valueOf(info.getId())); //来源系统流水号，每笔唯一，发起端需保存
		claims.put("transOrderNo", info.getActiveOrder()); //当前订单编号
		claims.put("transDate", DateUtil.getLongFormatDate(info.getCreateTime())); //交易时间
		claims.put("fromSystem", "boss"); //来源系统固定
		if("3".equals(info.getSubType())){
			claims.put("transTypeCode", "000164"); //交易码固定
		}else{
			claims.put("transTypeCode", "000158"); //交易码固定
		}
		return baseClient(url, claims);
	}

	/**
	 * 新欢乐送一级以下代理商奖励
	 * @param info
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String xhlfAgentAccount(XhlfAgentAccountDetail info, String url) {
		Map<String, Object> claims = new HashMap<String, Object>();
		//以下参数均必填
		claims.put("agentNoUp", info.getParentAgentNo()); //上级代理商
		claims.put("agentNoDown", info.getAgentNo()); //下级代理商
		claims.put("agentLevel", info.getAgentLevel()); //当前代理商级别
		claims.put("agentNode", info.getAgentNode()); // 当前代理商节点
		claims.put("merchantNo", info.getMerchantNo()); // 当前商户号
		claims.put("amount", info.getAmount().toString()); //入账金额
		claims.put("fromSerialNo", String.valueOf(info.getId())); //来源系统流水号，每笔唯一，发起端需保存
		claims.put("transOrderNo", info.getActiveOrder()); //当前订单编号
		claims.put("transDate", DateUtil.getLongFormatDate(info.getCreateTime())); //交易时间
		claims.put("fromSystem", "boss"); //来源系统固定
		if("3".equals(info.getSubType())){
			claims.put("transTypeCode", "000165"); //交易码固定
		}else{
			claims.put("transTypeCode", "000159"); //交易码固定
		}

		return baseClient(url, claims);
	}

	/**
	 * 新欢乐送商户入账
	 * @param info
	 * @param url
	 * @return
	 */
	public static String xhlfMerchantAccount(XhlfActivityMerchantOrder info, String url) {
		Map<String, Object> claims = new HashMap<String, Object>();
		//以下参数均必填
		claims.put("merchantNo", info.getMerchantNo());// 当前商户
		claims.put("amount", info.getRewardAmount().toString());//奖励金额
		claims.put("transDate", DateUtil.getLongFormatDate(info.getActiveTime())); //交易时间
		claims.put("fromSerialNo", String.valueOf(info.getId()));//来源系统流水号，每笔唯一，发起端需保存
		claims.put("transOrderNo", info.getActiveOrder());// 当前订单编号
		claims.put("fromSystem", "boss");//来源系统固定
		claims.put("transTypeCode", "000157");// 固定
		return baseClient(url, claims);
	}

	/**
	 * risk130
	 * @param url
	 * @param idCardNo
	 * @param settleCardNo
	 * @param key
	 * @return
	 */
	public static String risk130(String url,String idCardNo,String settleCardNo,String key) {
		url+="/riskhandle/risk130";
		String signStr=idCardNo+settleCardNo;
		//log.info("------------"+signStr);
		String sign = Md5.MD5Encode(signStr,key,"UTF-8");
		Map<String,String> params = new HashMap<>();
		params.put("idCardNo",idCardNo);
		params.put("settleCardNo",settleCardNo);
		params.put("hmac",sign);
		log.info("请求路径：{},参数：{}", url, params);
		String returnMsg = new ClientInterface(url, null).postRequest2(params);
		log.info("返回结果：{}", returnMsg);
		return returnMsg;
	}

	/**
	 * 清除商户长token
	 * @param url
	 * @param merchantNo
	 * @return
	 */
	public static String clearLongToken(String url,String merchantNo,String sn){
		url+="/mer/user/clearLongToken";
		Map<String,String> params = new HashMap<>();
		params.put("merchantNo",merchantNo);
		params.put("sn",sn);
		log.info("清除商户长token请求路径：{},参数：{}", url, params);
		String returnMsg = new ClientInterface(url, null).postRequest2(params);
		log.info("返回结果：{}", returnMsg);
		return returnMsg;
	}

}