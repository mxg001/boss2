package cn.eeepay.framework.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class Constants {
	public static final String sys_config_list_redis_key = "sys_config_list_redis_key";

	/*阿里云存储boss附件临时bucket*/
	public static final String ALIYUN_OSS_TEMP_TUCKET="boss-temp";

	/*阿里云存储boss附件bucket*/
	public static final String ALIYUN_OSS_ATTCH_TUCKET="agent-attch";
	public static final String ALIYUN_OSS_SIGN_TUCKET="sign-img";
	//代理商用户序列key
	public static final String AGENT_USER_SEQ ="user_no_seq";
	//机具终端序列
	public static final String TERMINAL_NO_SEQ ="terminal_id_seq";
	//收款码序列
	public static final String GATHER_CODE_SEQ = "gather_code_seq";
	//机具终端序列_移公社
	public static final String TERMINAL_NO_YGS_SEQ ="terminal_id_ygs_seq";
	
	public static final Integer AGENT_TEAM_ID = 999;
	//超级银行家商户科目号
	public static final String SUPER_BANK_SUBJECT_NO = "224115";
	//超级银行家代理商科目号
	public static final String SUPER_BANK_AGENT_SUBJECT_NO = "224116";
	//超级银行家，订单批次号的序列
	public static final String SUPER_BANK_BATCH_NO = "ORDER_BATCH_NO";
	//超级银行家，银行家后台的秘钥
	public static final String SUPER_BANK_SECRET = "superBankKey!@#$%^&*5646665656check";
	//数据库，从库
	public static final String DATA_SOURCE_SLAVE = "slave";
	//数据库，主库
	public static final String DATA_SOURCE_MASTER = "master";
	//数据库，归档数据库
	public static final String DATA_SOURCE_HISTORY = "history";

	//超级银行家，计算订单分润
	public static String SUPER_BANK_CHECK_ORDER_PROFIT = "http://192.168.1.26:8080/superbank-mobile/order/checkOrderProfit";
	//超级银行家，计算办卡分润
	public static String SUPER_BANK_BANKA_ORDER_PROFIT = "http://192.168.1.26:8081/superbank-mobile/order/checkBankaProfit";
	//超级银行家，计算首刷分润
	public static String SUPER_BANK_SHOUSHUA_ORDER_PROFIT = "http://192.168.1.26:8081/superbank-mobile/order/checkShouShuaProfit";
	//超级银行家，微信公众号推送
	public static String SUPER_BANK_PUSH = "http://192.168.1.26:8080/superbank-mobile/order/noticeOrderProfit";
	//超级银行家，分润入账
	public static String SUPER_BANK_PROFIT_ACCOUNT = "http://192.168.3.30:7025/superBankController/bankTransProfitRecordAccount.do";
	//超级银行家，开通超级还OEM
	public static String CREATE_REPAY_OEM = "http://192.168.1.182:8080/yfb-repay/oemCreate";
	//超级银行家，计算排行榜分润
	public static String SUPER_BANK_PROFIT_RANKING = "/superbank-mobile/order/rankingInput";
	//超级银行家，通过订单号计算分润
	public static String SUPER_BANK_PROFIT_ORDER_NO = "/superbank-mobile/order/checkBankaProfitOrder";
	
	public static String NOWTRANSFER_HOST;

	public static String CLOSE_RED = "http://192.168.1.203:2111/superbank-mobile/order/closeRedOrder";
	/**
	 * 艾创小盒子KEK
	 * */
	public static final String ITRON_KEK = "B21B113A5C673DF441AAC9DBDBD7D9AA";

	/**
	 * 新大陆 me30 kek
	 */
	public static final String NEWLAND_KEK="FF1B113A5C673DF441BBC9DBDBD7D9CC";
	
	/**
	 * BBPOS  key
	 */
	public static final String BBPOS_KEK="CB2B113A5C673DF441BBC9DBDBD7D9BB";
	
	/**
	 * 上海赛付P27   KEK
	 */
	public static final String SHSF_KEK="27CFF84316B5BC4022103D563D75C024";

	/**
	 * 神州安付KEK
	 */
	public static final String SZAF_KEK="CC913F54F81B57CF4A26265115644258";
	/**
	 * 天喻刷卡器KEK
	 */
	public static final String TY_KEK="6B99E4316F5FC4033D75C3103D56BCD3";
	//欢乐送-财务核算记账
	public static String HAPPY_SEND_ACCOUT_HOST_FOR_CASIN = "http://192.168.3.37:7025/recordAccountController/happySendAgentRecordAccountForCaiWu.do";
	//代理商开账户
	public static String ACCOUT_HOST="http://192.168.3.180:7025/extAccountController/createDefaultExtAccount.do";
	//代理商欢乐送账户开账户
	public static String HAPPY_SEND_ACCOUT_HOST="http://192.168.3.37:7025/extAccountController/createExtAccountBySubjectNo.do";
	//交易冻结与解冻
	public static String TRANS_FROZEN= "http://192.168.3.180:7025/recordAccountController/transFreeze.do";
	//分润结算账户，开户银行-开户名-开户账号
	public static String SHARE_BILL_ACCOUNT= "http://192.168.3.180:7025/bankAccountController/findAllBankAccountInfoList.do";
	
	//账户冻结解冻
	public static String ACCOUNT_STATUS= "http://192.168.3.180:7025/extAccountController/updateExtAccountStatus.do";

	//通道同步
	public static String CHANNEL_SYN= "merRepay/merReport";

	//账户记账，定时任务，交易成功但是记账失败的记录定时记账
	public static String TRANS_T0_RECORD_ACCOUNT= "http://192.168.3.180:7025/recordAccountController/recordAccountForT1.do";
	
	//账户加密码值
	public static String ACCOUNT_API_SECURITY="zouruijin";
	//国栋给的，账户加密码值
	public static String NEW_ACCOUNT_API_SECURITY="creditBackMoney";

	//获取商户账号余额
	public static String ACCOUNT_BALANCE_URL = "http://192.168.3.180:7025/extAccountController/findExtAccountBalance.do";
	//获取商户所有账号余额
	public static String ACCOUNT_ALL_BALANCE_URL = "http://192.168.3.180:7025/extAccountController/findExtAccountAllBalance.do";
	//获取商户账号交易记录
	public static String ACCOUNT_DETAIL_URL = "http://192.168.3.180:7025/extAccountController/findExtAccountTransInfoList.do";
	//结算交易
//	public static final String SETTLE_TRANS="http://172.16.3.71:7030/flowmoney/transfer/resetTransfer";
	public static String SETTLE_TRANS="http://192.168.3.180:7030/flowmoney/transfer/resetTransfer";
	//	public static final String SETTLE_TRANS="http://192.168.3.25:7030/transfer/resetTransfer";	//tang
	
	//欢乐返-循环送清算
	public static String HAPPY_RETURN_ACCOUNT_80_CLEAR = "http://192.168.3.30:7025/happyBackController/happyBackAuditLoopFeedRecordAccount.do";
	//欢乐返清算
	public static String HAPPY_RETURN_ACCOUNT_150_CLEAR = "http://192.168.3.37:7025/happyBackController/happyBackAudit80RecordAccount.do";
	//欢乐返核算
	public static String HAPPY_RETURN_ACCOUNT_150_CHECK = "http://192.168.3.30:7025/happyBackController/happyBackCaiwuRecordAccount.do";
	//欢乐返记账
	public static String HLF_RECORD_ACCOUT = "http://192.168.3.30:7025/happyBackController/happyBackActiveRecordAccount.do";
	//充值返过期发消息
	public static String EXPCOUPON_MSM_URL = "http://192.168.3.180:8099/coupon/addNotice";
	
	//邀请有奖，代理商入账
	public static String YQYJ_RECORD_ACCOUT = "http://192.168.4.4:7025/superPushController/invitePrizesRecordAccount.do";
	//邀请有奖，商户入账
	public static String YQYJ_RECORD_ACCOUT_MERCHANT = "http://192.168.4.4:7025/superPushController/invitePrizesMerchantRecordAccount.do";

	//信用卡还款，开户
	public static String CREDIT_MERCHANT_OPEN_ACCOUNT = "http://192.168.4.4:7025/creditCardRepaymentsController/createCreditMerExtAccount.do";

	//信用卡还款，再次出款
	public static String REPAY_AGAIN_PAYMENT = "http://192.168.4.13:8082/postalCash/cash";
	//信用卡还款，查询账户金额信息
	public static String REPAY_ACCOUNT_AMOUNT_INFO = "http://192.168.4.13:8082/merchant/accInfo";

	//差错账优化，同步交易状态
	public static String SYNC_TRANS_STATUS = "http://192.168.6.66:6666/orderQuery/sync";

	//一级代理商 欢乐返商户-奖励入账(增加)
	public static String  HAPPY_BACK_DA_YU="http://192.168.4.4:7025/happyBackController/happyBackDaYuRecordAccount.do";
	//一级代理商 欢乐返商户-奖励入账(扣减)
	public static String  HAPPY_BACK_XIAO_YU="http://192.168.4.4:7025/happyBackController/happyBackXiaoYuRecordAccount.do";

	//券查询优化接口,通知core清楚缓存
	public static String FLUSH_ACTIVITY_CACHE="http://192.168.4.8:8081/rechargeReturn/flushActivityCache";

	//根据组织id刷新组织
	public static String REFRESH_ORG_INFO="/superbank-mobile/refresh/refreshOrgInfo";
	
    //刷新组织分润配置
	public static String REFRESH_ORG_CONF="/superbank-mobile/refresh/refreshOrgConf";
	
	//刷新组织所有分润配置
	public static String REFRESH_ORG_CONF_ALL="/superbank-mobile/refresh/refreshOrgConfAll";
	//根据组织id刷新公告(当公告配置、修改、删除时都要调用)
	public static String REFRESH_NOTICE_INFO="/superbank-mobile/refresh/refreshNoticeInfo";

	//刷新信用卡奖金配置(当配置更改时调用)
	public static String REFRESH_CREDIT_CONF="/superbank-mobile/refresh/refreshCreditConf";

	//刷新银行家字典配置(当配置更改时调用)
	public static String REFRESH_SYS_OPTION="/superbank-mobile/refresh/refreshSysParams";


	//刷新信用卡银行配置(当信用卡银行更改时调用)
	public static String REFRESH_CREDIT_SOURCE="/superbank-mobile/refresh/refreshCreditSource";

	
	//刷新贷款奖金配置(当贷款奖金配置更改时调用)
	public static String REFRESH_LOAN_CONF="/superbank-mobile/refresh/refreshLoanConf";
	
	//刷新贷款机构(当贷款机构有更改时调用)
	public static String REFRESH_LOAN_SOURCE="/superbank-mobile/refresh/refreshLoanSource";
	//根据组织id刷新广告栏
	public static String REFRESH_BANNER_INFO="/superbank-mobile/refresh/refreshBannerInfo";
	//刷新业务红包发放配置
	public static String REFRESH_RED_CONF="/superbank-mobile/refresh/refreshRedConf";
	//刷新红包开关配置
	public static String REFRESH_RED_OPEN="/superbank-mobile/refresh/refreshRedOpen";
	//刷新红包业务管理配置
	public static String REFRESH_RED_PRO="/superbank-mobile/refresh/refreshRedPro";
	//刷新个人红包发放配置
	public static String REFRESH_RED_USER_CONF="/superbank-mobile/refresh/refreshRedUserConf";

	//超级还-挂起订单处理
	public static String WAKE_PERFECT_REPAY_PLAN = "http://192.168.4.33:5059/merRepay/wakePerfectRepayPlan";

	//调用core，对机具进行绑定或解绑
	public static String BIND_OR_UNBIND = "/zfMerchant/merBindOrUnBindTmstpos";
	//调用core，修改商户信息
	public static String YS_UPDATE_MERCHANT_INFO = "/zfMerchant/ysUpdateMerInfo";
	//刷新排行榜配置
	public static String REFRESH_RANKING_RULE="/superbank-mobile/refresh/refreshRankingRule";

	//深度报告类型
	public static String DEPTH_REPORT_TYPE = "/superbank/wx/credit!depthReport_show.action?orderNo=";
	//黑名单多头类型
	public static String BLACKLIST_MULTIHEAD_TYPE = "/superbank/wx/credit!moreReport_show.action?orderNo=";
	//信用评测类型
	public static String CREDIT_RATING_TYPE="/superbank/wx/credit!personalCre_show.action?orderNo=";

	//调用core，自动审件请求
	public static String MER_AUTO_CHECK_URL = "/autoCheck/autoCheckMerchant";

	//红包业务组织分类缓存刷新URl
	public static String REFRESH_RED_ORG_SORT="/superbank-mobile/refresh/refreshOrgSortConf";

	
	//神策配置
	public static String BEHAVIOUR_SERVER_URL="https://shenceapi.sqianbao.cn/sa";
	public static String BEHAVIOUR_SERVER_TOKEN="saf7c5e114";
	public static String BEHAVIOUR_SERVER_PROJECT="default";//唯一,没有创建,有则覆盖
	public static String BEHAVIOUR_TYPE_PROFILE_SET="profile_set";//唯一,没有创建,有则覆盖

	public static final String THREE_RECORDED_URL= "/thirdPartyIncomeController/incomeAccount.do";

	//调用core接口的key,sys_config
	public static String CORE_KEY = "CORE_KEY";
	public static String CORE_URL = "CORE_URL";

	/**
	 * Gson，不会序列化空值属性
	 */
	public static final Gson gsonLight = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	//新欢乐返
	public static String XHLF_ONE_AGENT_ACCOUNT = "/newHappyBackController/agentProfitRecordAccount.do";//一级入账
	public static String XHLF_AGENT_ACCOUNT = "/newHappyBackController/upToDownRecordAccount.do";//一级以下入账
	public static String XHLF_MERCHANT_ACCOUNT = "/newHappyBackController/mchProfitRecordAccount.do";//商户入账


	@Value("${SERVER.ACCOUT_HOST}")
	public void setACCOUT_HOST(String aCCOUT_HOST) {
		Constants.ACCOUT_HOST = aCCOUT_HOST;
	}
	@Value("${SERVER.TRANS_FROZEN}")
	public void setTRANS_FROZEN(String tRANS_FROZEN) {
		Constants.TRANS_FROZEN = tRANS_FROZEN;
	}
	@Value("${SERVER.SHARE_BILL_ACCOUNT}")
	public void setSHARE_BILL_ACCOUNT(String sHARE_BILL_ACCOUNT) {
		Constants.SHARE_BILL_ACCOUNT = sHARE_BILL_ACCOUNT;
	}
	@Value("${SERVER.ACCOUNT_STATUS}")
	public void setACCOUNT_STATUS(String aCCOUNT_STATUS) {
		Constants.ACCOUNT_STATUS = aCCOUNT_STATUS;
	}
	@Value("${SERVER.TRANS_T0_RECORD_ACCOUNT}")
	public void setTRANS_T0_RECORD_ACCOUNT(String tRANS_T0_RECORD_ACCOUNT) {
		Constants.TRANS_T0_RECORD_ACCOUNT = tRANS_T0_RECORD_ACCOUNT;
	}
	@Value("${SERVER.ACCOUNT_API_SECURITY}")
	public void setACCOUNT_API_SECURITY(String aCCOUNT_API_SECURITY) {
		Constants.ACCOUNT_API_SECURITY = aCCOUNT_API_SECURITY;
	}
	@Value("${SERVER.NEW_ACCOUNT_API_SECURITY}")
	public void setNEW_ACCOUNT_API_SECURITY(String NEW_ACCOUNT_API_SECURITY) {
		Constants.NEW_ACCOUNT_API_SECURITY = NEW_ACCOUNT_API_SECURITY;
	}
	@Value("${SERVER.ACCOUNT_BALANCE_URL}")
	public void setACCOUNT_BALANCE_URL(String aCCOUNT_BALANCE_URL) {
		Constants.ACCOUNT_BALANCE_URL = aCCOUNT_BALANCE_URL;
	}
	
	@Value("${SERVER.ACCOUNT_ALL_BALANCE_URL}")
	public void setACCOUNT_ALL_BALANCE_URL(String aCCOUNT_ALL_BALANCE_URL) {
		Constants.ACCOUNT_ALL_BALANCE_URL = aCCOUNT_ALL_BALANCE_URL;
	}
	
	@Value("${SERVER.ACCOUNT_DETAIL_URL}")
	public void setACCOUNT_DETAIL_URL(String aCCOUNT_DETAIL_URL) {
		Constants.ACCOUNT_DETAIL_URL = aCCOUNT_DETAIL_URL;
	}
	@Value("${SERVER.SETTLE_TRANS}")
	public void setSETTLE_TRANS(String sETTLE_TRANS) {
		Constants.SETTLE_TRANS = sETTLE_TRANS;
	}

	@Value("${SERVER.HAPPY_SEND_ACCOUT_HOST_FOR_CASIN}")
	public void setHAPPY_SEND_ACCOUT_HOST_FOR_CASIN(String hAPPY_SEND_ACCOUT_HOST_FOR_CASIN){
		Constants.HAPPY_SEND_ACCOUT_HOST_FOR_CASIN = hAPPY_SEND_ACCOUT_HOST_FOR_CASIN;
	}
	@Value("${SERVER.HAPPY_SEND_ACCOUT_HOST}")
	public void setHAPPY_SEND_ACCOUT_HOST(String hAPPY_SEND_ACCOUT_HOST){
		Constants.HAPPY_SEND_ACCOUT_HOST = hAPPY_SEND_ACCOUT_HOST;
	}
	@Value("${SERVER.NOWTRANSFER_HOST}")
	public void setNOWTRANSFER_HOST(String NOWTRANSFER_HOST){
		Constants.NOWTRANSFER_HOST = NOWTRANSFER_HOST;
	}
	@Value("${SERVER.HAPPY_RETURN_ACCOUNT_80_CLEAR}")
	public void setHAPPY_RETURN_ACCOUNT_80_CLEAR(String hAPPY_RETURN_ACCOUNT_80_CLEAR){
		Constants.HAPPY_RETURN_ACCOUNT_80_CLEAR = hAPPY_RETURN_ACCOUNT_80_CLEAR;
	}
//	@Value("${SERVER.HAPPY_RETURN_ACCOUNT_80_CLEAR}")
//	public void getHAPPY_RETURN_ACCOUNT_80_CLEAR(String hAPPY_RETURN_ACCOUNT_80_CLEAR){
//		Constants.HAPPY_RETURN_ACCOUNT_80_CLEAR = hAPPY_RETURN_ACCOUNT_80_CLEAR;
//	}
	@Value("${SERVER.HAPPY_RETURN_ACCOUNT_150_CLEAR}")
	public void setHAPPY_RETURN_ACCOUNT_150_CLEAR(String HAPPY_RETURN_ACCOUNT_150_CLEAR){
		Constants.HAPPY_RETURN_ACCOUNT_150_CLEAR = HAPPY_RETURN_ACCOUNT_150_CLEAR;
	}
//	@Value("${SERVER.HAPPY_RETURN_ACCOUNT_150_CLEAR}")
//	public void getHAPPY_RETURN_ACCOUNT_150_CLEAR(String HAPPY_RETURN_ACCOUNT_150_CLEAR){
//		Constants.HAPPY_RETURN_ACCOUNT_150_CLEAR = HAPPY_RETURN_ACCOUNT_150_CLEAR;
//	}
	@Value("${SERVER.HAPPY_RETURN_ACCOUNT_150_CHECK}")
	public void setHAPPY_RETURN_ACCOUNT_150_CHECK(String HAPPY_RETURN_ACCOUNT_150_CHECK){
		Constants.HAPPY_RETURN_ACCOUNT_150_CHECK = HAPPY_RETURN_ACCOUNT_150_CHECK;
	}
//	@Value("${SERVER.HAPPY_RETURN_ACCOUNT_150_CHECK}")
//	public void getHAPPY_RETURN_ACCOUNT_150_CHECK(String hAPPY_RETURN_ACCOUNT_150_CHECK){
//		Constants.HAPPY_RETURN_ACCOUNT_150_CHECK = hAPPY_RETURN_ACCOUNT_150_CHECK;
//	}
	@Value("${SERVER.HLF_RECORD_ACCOUT}")
	public void setHLF_RECORD_ACCOUT(String hLF_RECORD_ACCOUT) {
		Constants.HLF_RECORD_ACCOUT = hLF_RECORD_ACCOUT;
	}
	@Value("${SERVER.EXPCOUPON_MSM_URL}")
	public void setEXPCOUPON_MSM_URL(String EXPCOUPON_MSM_URL) {
		Constants.EXPCOUPON_MSM_URL = EXPCOUPON_MSM_URL;
	}
	@Value("${SERVER.YQYJ_RECORD_ACCOUT}")
	public void setYQYJ_RECORD_ACCOUT(String YQYJ_RECORD_ACCOUT) {
		Constants.YQYJ_RECORD_ACCOUT = YQYJ_RECORD_ACCOUT;
	}
	@Value("${SERVER.YQYJ_RECORD_ACCOUT_MERCHANT}")
	public void setYQYJ_RECORD_ACCOUT_MERCHANT(String YQYJ_RECORD_ACCOUT_MERCHANT) {
		Constants.YQYJ_RECORD_ACCOUT_MERCHANT = YQYJ_RECORD_ACCOUT_MERCHANT;
	}
	
	@Value("${SERVER.REPAY_AGAIN_PAYMENT}")
	public void setREPAY_AGAIN_PAYMENT(String REPAY_AGAIN_PAYMENT) {
		Constants.REPAY_AGAIN_PAYMENT = REPAY_AGAIN_PAYMENT;
	}
	@Value("${SERVER.CREDIT_MERCHANT_OPEN_ACCOUNT}")
	public void setCREDIT_MERCHANT_OPEN_ACCOUNT(String CREDIT_MERCHANT_OPEN_ACCOUNT) {
		Constants.CREDIT_MERCHANT_OPEN_ACCOUNT = CREDIT_MERCHANT_OPEN_ACCOUNT;
	}
	@Value("${SERVER.REPAY_ACCOUNT_AMOUNT_INFO}")
	public void setREPAY_ACCOUNT_AMOUNT_INFO(String REPAY_ACCOUNT_AMOUNT_INFO) {
		Constants.REPAY_ACCOUNT_AMOUNT_INFO = REPAY_ACCOUNT_AMOUNT_INFO;
	}
	@Value("${SERVER.SYNC_TRANS_STATUS}")
	public void setSYNC_TRANS_STATUS(String SYNC_TRANS_STATUS) {
		Constants.SYNC_TRANS_STATUS = SYNC_TRANS_STATUS;
	}
	@Value("${SERVER.SUPER_BANK_CHECK_ORDER_PROFIT}")
	public void setSUPER_BANK_CHECK_ORDER_PROFIT(String SUPER_BANK_CHECK_ORDER_PROFIT) {
		Constants.SUPER_BANK_CHECK_ORDER_PROFIT = SUPER_BANK_CHECK_ORDER_PROFIT;
	}
	@Value("${SERVER.SUPER_BANK_BANKA_ORDER_PROFIT}")
	public void setSUPER_BANK_BANKA_ORDER_PROFIT(String SUPER_BANK_BANKA_ORDER_PROFIT) {
		Constants.SUPER_BANK_BANKA_ORDER_PROFIT = SUPER_BANK_BANKA_ORDER_PROFIT;
	}
	@Value("${SERVER.SUPER_BANK_SHOUSHUA_ORDER_PROFIT}")
	public void setSUPER_BANK_SHOUSHUA_ORDER_PROFIT(String SUPER_BANK_SHOUSHUA_ORDER_PROFIT) {
		Constants.SUPER_BANK_SHOUSHUA_ORDER_PROFIT = SUPER_BANK_SHOUSHUA_ORDER_PROFIT;
	}
	@Value("${SERVER.SUPER_BANK_PUSH}")
	public void setSUPER_BANK_PUSH(String SUPER_BANK_PUSH) {
		Constants.SUPER_BANK_PUSH = SUPER_BANK_PUSH;
	}
	@Value("${SERVER.SUPER_BANK_PROFIT_ACCOUNT}")
	public void setSUPER_BANK_PROFIT_ACCOUNT(String SUPER_BANK_PROFIT_ACCOUNT) {
		Constants.SUPER_BANK_PROFIT_ACCOUNT = SUPER_BANK_PROFIT_ACCOUNT;
	}
	@Value("${SERVER.CREATE_REPAY_OEM}")
	public void setCREATE_REPAY_OEM(String CREATE_REPAY_OEM) {
		Constants.CREATE_REPAY_OEM = CREATE_REPAY_OEM;
	}

	@Value("${SERVER.FLUSH_ACTIVITY_CACHE}")
	public void setFLUSH_ACTIVITY_CACHE(String FLUSH_ACTIVITY_CACHE) {
		Constants.FLUSH_ACTIVITY_CACHE = FLUSH_ACTIVITY_CACHE;
	}

	//liuks
	@Value("${SERVER.HAPPY_BACK_DA_YU}")
	public void setHAPPY_BACK_DA_YU(String HAPPY_BACK_DA_YU) {
		Constants.HAPPY_BACK_DA_YU = HAPPY_BACK_DA_YU;
	}
	@Value("${SERVER.HAPPY_BACK_XIAO_YU}")
	public void setHAPPY_BACK_XIAO_YU(String HAPPY_BACK_XIAO_YU) {
		Constants.HAPPY_BACK_XIAO_YU = HAPPY_BACK_XIAO_YU;
	}
	@Value("${SERVER.WAKE_PERFECT_REPAY_PLAN}")
	public void setWAKE_PERFECT_REPAY_PLAN(String WAKE_PERFECT_REPAY_PLAN) {
		Constants.WAKE_PERFECT_REPAY_PLAN = WAKE_PERFECT_REPAY_PLAN;
	}

	public static final Integer ORDER_CTO_ORIGIN=0;//订单来源collective_trans_order

	public static final Integer ORDER_ST_ORIGIN=1;//订单来源settle_transfer

}
