package cn.eeepay.boss.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.util.ClientInterface;

@Controller
@RequestMapping(value="/merchantInfo")
public class MerchantInfoAction {
	private static final Logger log = LoggerFactory.getLogger(MerchantInfoAction.class);
	//商户信息
	@Resource
	private MerchantInfoService merchantInfoService;
	@Resource
	private MerchantServiceProService merchantServiceProService;
	@Resource
	private PyIdentificationService pyIdentificationService;
	@Resource
	private UserService userService;
	@Resource
	private OpenPlatformService openPlatformService;
	@Resource
	private PosCardBinService posCardBinService;
	@Resource
	private PosCnapsService posCnapsService;


	// 支行查询
	@RequestMapping(value = "/selectCnaps")
	@ResponseBody
	public Object selectCnaps(@RequestBody String param) throws Exception {
		Map<String, Object> mapJson = new HashMap<>();
		try {
			JSONObject json = JSON.parseObject(param);
			String cityName = json.getString("cityName");// 市
			String pris = json.getString("pris");// 省
			if (cityName == null || cityName.equals("")) {
				mapJson.put("bols", false);
				mapJson.put("msg", "参数有误");
				return mapJson;
			}
			if (pris == null || pris.equals("")) {
				mapJson.put("bols", false);
				mapJson.put("msg", "参数有误");
				return mapJson;
			}
			String bankName = "%" + json.getString("backName") + "%";
			if (pris.equals("北京")) {
				List<PosCnaps> list = posCnapsService.query(bankName, "%" + pris + "%");
				if (list.size() == 0) {
					mapJson.put("bols", false);
					mapJson.put("msg", "数据为空");
					return mapJson;
				}
				mapJson.put("bols", true);
				mapJson.put("list", list);
				return mapJson;
			}
			if (pris.equals("上海")) {
				List<PosCnaps> list = posCnapsService.query(bankName, "%" + pris + "%");
				if (list.size() == 0) {
					mapJson.put("bols", false);
					mapJson.put("msg", "数据为空");
					return mapJson;
				}
				mapJson.put("bols", true);
				mapJson.put("list", list);
				return mapJson;
			}
			if (pris.equals("天津")) {
				List<PosCnaps> list = posCnapsService.query(bankName, "%" + pris + "%");
				if (list.size() == 0) {
					mapJson.put("bols", false);
					mapJson.put("msg", "数据为空");
					return mapJson;
				}
				mapJson.put("bols", true);
				mapJson.put("list", list);
				return mapJson;
			}
			if (pris.equals("重庆")) {
				List<PosCnaps> list = posCnapsService.query(bankName, "%" + pris + "%");
				if (list.size() == 0) {
					mapJson.put("bols", false);
					mapJson.put("msg", "数据为空");
					return mapJson;
				}
				mapJson.put("bols", true);
				mapJson.put("list", list);
				return mapJson;
			}
			// tgh316处理市问题
			if (cityName.endsWith("州")) {
				cityName = cityName.substring(0, cityName.length() - 1);
			}
			String city = cityName.contains("市") ? "%" + cityName.substring(0, cityName.indexOf("市")) + "%"
					: "%" + cityName + "%";

			List<PosCnaps> list = posCnapsService.query(bankName, city);
			if (list.size() == 0) {
				mapJson.put("bols", false);
				mapJson.put("msg", "数据为空");
			} else {
				mapJson.put("bols", true);
				mapJson.put("list", list);
			}
		} catch (Exception e) {
			log.error("支行查询报错", e);
			mapJson.put("bols", false);
			mapJson.put("msg", "支行查询报错");
		}
		return mapJson;
	}

	// 银行查询
	@RequestMapping(value = "/getBackName")
	@ResponseBody
	public Object getBackName(@RequestBody String param) throws Exception {
		Map<String, Object> mapJson = new HashMap<String, Object>();
		try {
			JSONObject json = JSON.parseObject(param);
			String accountNo = json.getString("accountNo");
			List<PosCardBin> cards = posCardBinService.queryAllInfo(accountNo);
			if (cards.size() != 0) {
				mapJson.put("lists", cards);
				mapJson.put("bols", true);
			} else {
				mapJson.put("msg", "没有查到对应的银行，请检查开户账号是否正确");
				mapJson.put("bols", false);
			}
		} catch (Exception e) {
			log.error("支行查询报错", e);
			mapJson.put("bols", false);
			mapJson.put("msg", "支行查询报错");
		}
		return mapJson;
	}
		
	//商户初始化
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectAllInfo.do")
	@ResponseBody
	public Object selectAllInfo(@RequestParam("agentNo")String param) throws Exception{
		List<MerchantInfo> listMer = null;
		try {
			listMer=merchantInfoService.selectByNameInfoByTermianl(JSON.parseObject(param,String.class));
		} catch (Exception e) {
			log.error("商户初始化失败----",e);
		}
		return listMer;
	}
	
	//经营范围下拉框
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectSysName")
	@ResponseBody
	public Object selectSysName() throws Exception{
		List<SysDict> listDic = null;
		try {
			listDic=merchantInfoService.selectOneInfo();
		} catch (Exception e) {
			log.error("经营范围下拉框失败----",e);
		}
		return listDic;
	}
	
	//经营范围二级下拉框
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectTwoSysName")
	@ResponseBody
	public Object selectTwoSysName(@RequestParam("key")String param) throws Exception{
		List<SysDict> listDic = null;
		try {
			String key=JSON.parseObject(param,String.class);
			listDic=merchantInfoService.selectTwoInfoByParentId(key);
		} catch (Exception e) {
			log.error("经营范围二级下拉框失败----",e);
		}
		return listDic;
	}
	
	
	//获取商户账号信息
	@RequestMapping(value="/getAccountInfo")
	@ResponseBody
	public Object getAccountInfo(@RequestBody String param) {
		Map<String, Object> maps=new HashMap<String, Object>();
		maps.put("bols", true);
		maps.put("msg", "获取商户账号信息异常");
		try{
			JSONObject json=JSON.parseObject(param);
			String merNo=json.getString("merNo");
//			String merNo="211000000742";
			//账户信息
			String str = ClientInterface.getMerchantAccountAllBalance(merNo);
			if(StringUtils.isNotBlank(str)){
				JSONObject jsons=JSON.parseObject(str);
				if(jsons==null || !jsons.getBoolean("status")){
					maps.put("bols", false);
					maps.put("msg", "获取商户账号信息失败");
					return maps;
				}
				List<AccountInfo> ainfo=JSON.parseArray(jsons.getJSONArray("data").toJSONString(),AccountInfo.class);
				if(ainfo.size()==0){
					maps.put("bols", false);
					maps.put("msg", "获取商户账号信息失败");
					return maps;
				}
				maps.put("alist", ainfo);
			}
			//账户交易记录
			String str1 = ClientInterface.selectMerchantAccountTransInfoList(merNo.trim(), null, null, null, 1, 10);
			if(StringUtils.isNotBlank(str1)){
				JSONObject jsons1=JSON.parseObject(str1);
				JSONObject jsons2=JSON.parseObject(jsons1.getString("data"));
				if(jsons2!=null){
					List<AccountInfoRecord> slist=JSON.parseArray(jsons2.getJSONArray("list").toJSONString(),AccountInfoRecord.class);
					if(slist.size()<1){
						if(!jsons1.getBoolean("status")){
							maps.put("bols", false);
							maps.put("msg", "获取商户账号交易记录信息失败");
							return maps;
						}
					}
					maps.put("list", slist);
					maps.put("total", jsons2.getString("total"));
					maps.put("bols", true);
				}
			}
		}catch(Exception e){
			log.error("获取商户账号信息异常",e);
			maps.put("bols", false);
			maps.put("msg", "获取商户账号信息异常");
		}
		return maps;
	}
	
	//获取商户账户交易记录
	@RequestMapping(value="/getAccountTranInfo")
	@ResponseBody
	public Object getAccountTranInfo(@RequestParam("info") String param) throws Exception{
		Map<String, Object> maps=new HashMap<String, Object>();
		try{
			JSONObject json=JSON.parseObject(param);
//			System.out.println("aa====="+json);
			String merNo=json.getString("merNo");
//			String merNo="211000000742";
			Date sdate=json.getDate("sdate");
			Date edate=json.getDate("edata");
			String operation=json.getString("operation");
			Integer pageNo=json.getInteger("pageNo");
			Integer pageSize=json.getInteger("pageSize");
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
			String sd="";
			String ed="";
			if(sdate!=null){
				sd = sdf.format(sdate);  
			}
			if(edate!=null){
				ed = sdf.format(edate);  
			}
			//账户交易记录
//			System.out.println(merNo+"\t"+sd+"\t"+ed+"\t"+operation+"\t"+pageNo+"\t"+pageSize);
			String str1 = ClientInterface.selectMerchantAccountTransInfoList(merNo.trim(), sd.trim(), ed.trim(), operation.trim(), pageNo, pageSize);
			JSONObject jsons1=JSON.parseObject(str1);
			JSONObject jsons2=JSON.parseObject(jsons1.getString("data"));
			List<AccountInfoRecord> slist=JSON.parseArray(jsons2.getJSONArray("list").toJSONString(),AccountInfoRecord.class);
			maps.put("bols", true);
			maps.put("list", slist);
			maps.put("total", jsons2.getString("total"));
		}catch(Exception e){
			System.out.println(e);
			log.error("取商户账户交易记录异常",e);
			maps.put("bols", false);
			maps.put("msg", "取商户账户交易记录异常");
		}
		return maps;
	}
	
	//开设商户账户
	@RequestMapping(value="/createAccount")
	@ResponseBody
	@SystemLog(description = "开设商户账户",operCode="merchant.openAccount")
	public Object createAccount(@RequestBody String param) throws Exception{
		Map<String, Object> maps=new HashMap<String, Object>();
		try{
			JSONObject json=JSON.parseObject(param);
			String acc=ClientInterface.createMerchantAccount(json.getString("merNo"));
			JSONObject returnJson = JSONObject.parseObject(acc);
//			如果开户成功，或者商户已开户
			if(returnJson.getBooleanValue("status") || "外部账号已经存在".equals(returnJson.getString("msg"))){
				int i = merchantInfoService.updateMerAcoount(json.getString("merNo"));
				if(i>0){
					maps.put("bols", true);
					maps.put("msg", "开立商户账户成功");
				}else{
					maps.put("bols", false);
					maps.put("msg", "开立商户账户失败");
				}
			}else{
				maps.put("bols", false);
				maps.put("msg", "开立商户账户失败");
			}
		}catch(Exception e){
			log.error("开立商户账户异常",e);
			maps.put("bols", false);
			maps.put("msg", "开立商户账户异常");
		}
		return maps;
	}
	
	//批量开设商户账户
	@RequestMapping(value="/createAllAccount")
	@ResponseBody
	@SystemLog(description = "批量开设商户账户",operCode="merchant.openAccountBatch")
	public Object createAllAccount(@RequestBody String param) throws Exception{
		Map<String, Object> maps=new HashMap<String, Object>();
		try{
			List<String> merchantNoList = JSONObject.parseArray(param, String.class);
			if(merchantNoList.size()<=0){
				maps.put("bols", false);
				maps.put("msg", "没有要开立的商户账号");
				return maps;
			}
			merchantInfoService.updateMerAccountBatch(merchantNoList);
			maps.put("bols", true);
			maps.put("msg", "开立商户账户成功");
		}catch(Exception e){
			log.error("开立商户账户异常",e);
			maps.put("bols", false);
			maps.put("msg", "开立商户账户异常");
		}
		return maps;
	}
	
	/**
	 * 实名认证
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/checkBankNameIDCard")
	@ResponseBody
	public Object checkBankNameIDCard(@RequestBody String param) throws Exception{
		Map<String, String> maps=new HashMap<String, String>();
		Map<String, String> params=new HashMap<String, String>();
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			JSONObject json=JSON.parseObject(param);
			String accountNo = (String) json.get("accountNo");
			String name = (String) json.get("name");
			String card = (String) json.get("card");
			String cnapsNo = (String) json.get("cnapsNo");
			PyIdentification pyp=new PyIdentification();
			pyp.setCreatePerson(principal.getId().toString());
			pyp.setBySystem(1);
			pyp.setAccountNo(accountNo);
			pyp.setIdCard(card);
			pyp.setIdentName(name);
			if(StringUtils.isEmpty(accountNo) || StringUtils.isEmpty(name)|| StringUtils.isEmpty(card)){
				maps.put("msg", "必要数据为空，请检查数据");
				maps.put("bols", "f");
			}else{
//				params.put("accountNo", accountNo);//银行卡号
//				params.put("name", name);//姓名
//				params.put("card", card);//身份证
//				params.put("cnapsNo", cnapsNo);//银行行号
				//accountNo 银行卡号; name 姓名;card 身份证;cnapsNo 联行行号
				PyIdentification ppp = pyIdentificationService.queryByCheckInfo(name, card, accountNo);
				if(ppp==null){//去走检查
					maps=openPlatformService.doAuthen(accountNo,name,card,null);
					String errCode = maps.get("errCode");
					String errMsg_ = maps.get("errMsg");
					boolean flag = "00".equalsIgnoreCase(errCode);
//					log.info("身份证验证结果：是否成功:{};开户名:{};银行卡号:{};身份证:{};验证结果:{};错误信息:{};异常信息:{};",new Object[]
//							{flag,account_name,account_no,id_card_no,errCode,errMsg_,exceptionMsg});
					//如果身份验证失败，刚不再自动审核，按旧有的注册流程走。
					
					pyp.setErrorMsg(errMsg_);
					if (!flag) {
						log.info("身份证验证失败");
						maps.put("msg", "开户名、身份证、银行卡号不匹配");
						maps.put("bols", "f");
						maps.put("errMsg", errMsg_);
					}else{
						maps.put("msg", "验证通过");
						maps.put("bols", "t");
					}
				}else{//查看是否通过
					if(ppp.getIdentiStatus()==1){
						maps.put("msg", "验证通过");
						maps.put("bols", "t");
					}else{//去走检查
						maps=openPlatformService.doAuthen(accountNo,name,card,null);
						String errCode = maps.get("errCode");
						String errMsg_ = maps.get("errMsg");
						boolean flag = "00".equalsIgnoreCase(errCode);
//						log.info("身份证验证结果：是否成功:{};开户名:{};银行卡号:{};身份证:{};验证结果:{};错误信息:{};异常信息:{};",new Object[]
//								{flag,account_name,account_no,id_card_no,errCode,errMsg_,exceptionMsg});
						//如果身份验证失败，刚不再自动审核，按旧有的注册流程走。
						pyp.setErrorMsg(errMsg_);
						if (!flag) {
							log.info("身份证验证失败");
							maps.put("msg", "开户名、身份证、银行卡号不匹配");
							maps.put("bols", "f");
							maps.put("errMsg", errMsg_);
						}else{
							maps.put("msg", "验证通过");
							maps.put("bols", "t");
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("实名认证报错",e);
			maps.put("msg", "实名认证报错");
			maps.put("bols", "f");
		}
		return maps;
	}

//	public Map<String, String> checkBank_Name_IDCard(Map<String, String> params) {
//		log.info("验证身份证、开户名、银行卡：" + params);
//		String ip ="http://www.yfbpay.cn/boss/api/checkMain";
//		//logger.info("数据库中配置的身份证、开户名、银行卡验证接口地址："+ip);
//		ip = (ip==null ? "http://www.yfbpay.cn/boss/api/checkMain" : ip);
//		StringBuffer url = new StringBuffer(ip);
//		url.append("?");
//		String responseBody = null;
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("errCode", "faild");
//		map.put("errMsg", "开户名+账号+证件号码,校验失败");
//		map.put("exceptionMsg", "--");
//		try {
//			//(1:验证账号+户名  2：验证账号+户名+证件号)
//			String verifyType = "2";
//			verifyType = (StringUtils.isEmpty(verifyType)) ? "2" : verifyType;
//			url.append("verifyType=").append(verifyType);
//
//			//标识为AGENT 代理商系统访问接口
//			String channel = params.get("channel");
//			channel = (StringUtils.isEmpty(channel)) ? "AGENT" : channel;
//			url.append("&channel=").append(channel);
//
//			//身份证号码
//			String identityId = params.get("card");
//			url.append("&identityId=").append(identityId);
//
//			//银行卡号
//			String accNo = params.get("accountNo");
//			url.append("&accNo=").append(URLEncoder.encode(accNo, "UTF-8"));
//
//			//开户名称
//			String accName = params.get("name");
//			url.append("&accName=").append(URLEncoder.encode(accName, "UTF-8"));
//
//			//清算联行号
//			String cnapsNo = params.get("cnapsNo");
//			if(StringUtils.isEmpty(cnapsNo)){ //如果没有传入清算联行号，则从 CardBin 中取
//				try {
//					String bankNoTemp=posCardBinService.queryBankNo(accNo);
//					if(bankNoTemp.equals("0")||bankNoTemp.equals("1")){
//						cnapsNo = null;
//					}else{
//						cnapsNo = bankNoTemp;
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			url.append("&bankNo=").append(cnapsNo);
//			String finalUrl = url.toString();
//			log.info("验证身份证、开户名、银行卡，最终URL："+finalUrl);
////			HttpClient client = new HttpClient();
////
////			//设置连接超时时间
////			client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
////			//设置读取超时时间
////			client.getHttpConnectionManager().getParams().setSoTimeout(20000);
////
////			// 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
////			HttpMethod method = new GetMethod(finalUrl);
////			// 使用POST方法
////			// HttpMethod method = new PostMethod(finalUrl);
////			client.executeMethod(method);
////			String statusLine = method.getStatusLine().toString();
//
//			responseBody = ClientInterface.postRequest(finalUrl);
//			String errCode = responseBody.substring(responseBody.indexOf("<errCode>")+9, responseBody.indexOf("</errCode>"));
//			String errMsg = responseBody.substring(responseBody.indexOf("<errMsg>")+8, responseBody.indexOf("</errMsg>"));
//			map.put("errCode", errCode);
//			map.put("errMsg", errMsg);
//			// response.sendRedirect(url.toString());
////	    } catch (ConnectTimeoutException cte) {
////	    	log.info("验证身份证、开户名、银行卡：连接超时<ConnectTimeoutException>");
////	        map.put("exceptionMsg", "连接超时");
////	        cte.printStackTrace();
////	    } catch (SocketTimeoutException ste) {
////	    	log.info("验证身份证、开户名、银行卡：读取超时<SocketTimeoutException>");
////	        map.put("exceptionMsg", "读取超时");
////	        ste.printStackTrace();
//		} catch (IOException e) {
//			log.info("验证身份证、开户名、银行卡：其他异常<IOException>");
//			e.printStackTrace();
//		}
//		return map;
//	}

	
	/**
	 * 重置密码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/restPwd.do")
	@ResponseBody
	@SystemLog(description = "商户重置密码",operCode="merchant.restPwd")
	public Map<String, Object> restPwd(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json=JSON.parseObject(param);
			String merchantNo=json.getString("merchantNo");

			MerchantInfo info=merchantInfoService.selectByMerNo(merchantNo);
			int i =userService.merchantRestPwd(info.getMobilephone(),json.getString("teamId"));
			if(i>0){
				msg.put("msg", "重置密码成功:abc888888");
			}else{
				msg.put("msg", "重置密码失败或没有该商户");
			}
		} catch (Exception e) {
			log.error("重置密码失败");
			msg.put("msg", "重置密码失败");
		}
		return msg;
	}
	
	@RequestMapping(value="/checkServiceType")
	@ResponseBody
	public Map<String, Object> checkServiceType(String merId) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			List<MerchantService> list = merchantServiceProService.selectByMerId(merId);
			if(list.size()==0){
				msg.put("msg", "当前商户没有服务");
				msg.put("status", false);
				msg.put("list", list);
			}else{
				msg.put("list", list);
				msg.put("status", true);
			}
		} catch (Exception e) {
			log.error("查询失败");
			msg.put("msg", "查询失败");
			msg.put("status", false);
		}
		return msg;
	}
	
	@RequestMapping(value="/updatRiskStatus")
	@ResponseBody
	@SystemLog(description = "商户冻结状态",operCode="merchant.updateRiskStatus")
	public Map<String, Object> updatRiskStatus(@RequestBody String param) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json=JSON.parseObject(param);
			String merId=json.getString("merId").trim();
			String riskStatus=json.getString("merRiskStatus").trim();
			int i = merchantInfoService.updateRiskStatus(merId, riskStatus);
			if(i>0){
				msg.put("msg", "修改成功");
				msg.put("status", true);
			}else{
				msg.put("msg", "修改失败");
				msg.put("status", false);
			}
		} catch (Exception e) {
			log.error("修改报错");
			msg.put("msg", "修改报错");
			msg.put("status", false);
			String str=e.getMessage();
			if(e.getMessage()==null){
				msg.put("msg", "修改报错");
				return msg;
			}
			if(str.contains("\r\n")||str.contains("\n"))
				msg.put("msg", "修改异常");
			else
				msg.put("msg", str);	
		
		}
		return msg;
	}
	
	//平台审核人初始化
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectAllUserBox")
	@ResponseBody
	public Object selectAllUserBox() throws Exception{
		List<UserInfo> list = null;
		try {
			list=userService.getAllUsers();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			log.error("平台审核人初始化----",e);
		}
		return list;
	}
	
	public static class AccountInfo{
		private String controlAmount;
		private String balance;
		private String avaliBalance;
		private String settlingAmount;
		private String accountNo;
		private String preFreezeAmount;
		
		public String getControlAmount() {
			return controlAmount;
		}
		public void setControlAmount(String controlAmount) {
			this.controlAmount = controlAmount;
		}
		public String getBalance() {
			return balance;
		}
		public void setBalance(String balance) {
			this.balance = balance;
		}
		public String getAvaliBalance() {
			return avaliBalance;
		}
		public void setAvaliBalance(String avaliBalance) {
			this.avaliBalance = avaliBalance;
		}
		public String getSettlingAmount() {
			return settlingAmount;
		}
		public void setSettlingAmount(String settlingAmount) {
			this.settlingAmount = settlingAmount;
		}
		public String getAccountNo() {
			return accountNo;
		}
		public void setAccountNo(String accountNo) {
			this.accountNo = accountNo;
		}
		public String getPreFreezeAmount() {
			return preFreezeAmount;
		}
		public void setPreFreezeAmount(String preFreezeAmount) {
			this.preFreezeAmount = preFreezeAmount;
		}
	}
	
	public static class AccountInfoRecord{
		private String id;
		private String serialNo;
		private String balance;
		private String avaliBalance;
		private String recordDate;
		private String childSerialNo;
		private String recordAmount;
		private String accountNo;
		private String recordTime;
		private String debitCreditSide;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getSerialNo() {
			return serialNo;
		}
		public void setSerialNo(String serialNo) {
			this.serialNo = serialNo;
		}
		public String getBalance() {
			return balance;
		}
		public void setBalance(String balance) {
			this.balance = balance;
		}
		public String getAvaliBalance() {
			return avaliBalance;
		}
		public void setAvaliBalance(String avaliBalance) {
			this.avaliBalance = avaliBalance;
		}
		public String getRecordDate() {
			return recordDate;
		}
		public void setRecordDate(String recordDate) {
			this.recordDate = recordDate;
		}
		public String getChildSerialNo() {
			return childSerialNo;
		}
		public void setChildSerialNo(String childSerialNo) {
			this.childSerialNo = childSerialNo;
		}
		public String getRecordAmount() {
			return recordAmount;
		}
		public void setRecordAmount(String recordAmount) {
			this.recordAmount = recordAmount;
		}
		public String getAccountNo() {
			return accountNo;
		}
		public void setAccountNo(String accountNo) {
			this.accountNo = accountNo;
		}
		public String getRecordTime() {
			return recordTime;
		}
		public void setRecordTime(String recordTime) {
			this.recordTime = recordTime;
		}
		public String getDebitCreditSide() {
			return debitCreditSide;
		}
		public void setDebitCreditSide(String debitCreditSide) {
			this.debitCreditSide = debitCreditSide;
		}
		
	}
}
