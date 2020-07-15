package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * table:super.org_info desc:超级银行家组织表
 *
 * @author tans
 * @date 2017-11-29
 */
public class OrgInfo {
	private Long orgId;// 组织ID

	private String orgName;// 组织名称

	private String orgLogo;// 组织图标

	private String orgLogoUrl;// 组织图标地址

	private String weixinSign;// 微信公众号

	private String superOrgcode;// 超级还组织编号

	private String v2AgentNumber;// V2代理商编号

	private String v2Orgcode;// V2组织编号

	private BigDecimal agentPrice;// 代理价格

	private BigDecimal agentCost;// 产品代理成本

	private BigDecimal agentPushCost;// 产品代理发放成本

	private String receiveWxCost;// 产品收款微信成本
	private String receiveZfbCost;// 产品收款支付宝成本
	private String receiveKjCost;// 产品收款快捷成本

	private String receivePushCost;// 产品收款发放成本

	private String repaymentCost;// 产品超级还款成本(分期计划)
	private String repaymentCostWmjh;// 产品超级还款成本(完美计划)
	private String repaymentPushCost;// 产品超级还款发放成本(分期计划)
	private String repaymentPushCostWmjh;// 产品超级还款发放成本(完美计划)

	private Integer upManagerNum;// 产品超级还款发放成本

	private Integer upManagerCardnum;// 升经理需发展卡数

	private Integer upBankerNum;// 升银行家需发展个数

	private Integer upBankerCardnum;// 升银行家需发展卡数

	private Long updateBy;// 修改人

	private Date updateDate;// 修改时间

	private Date createDate;// 创建时间

	private String remark;// 备注

	private String authorizedUnit;// 授权单位

	private String authorizedUnitSeal;// 授权单位印章
	private String authorizedUnitSealUrl;// 授权单位印章url

	private Double tradeFeeRate;// 超级还交易费率(分期计划)
	private String tradeFeeRateStr;// 超级还交易费率(分期计划)
	private Double tradeFeeRateWmjh;// 超级还交易费率(完美计划)
	private String tradeFeeRateWmjhStr;// 超级还交易费率(完美计划)
	private Double tradeSingleFee;// 超级还交易单笔手续费分期计划)
	private String tradeSingleFeeStr;// 超级还交易单笔手续费分期计划)
	private Double tradeSingleFeeWmjh;// 超级还交易单笔手续费(完美计划)
	private String tradeSingleFeeWmjhStr;// 超级还交易单笔手续费(完美计划)
	private Double withdrawFeeRate;// 超级还提现费率
	private String withdrawFeeRateStr;// 超级还提现费率
	private Double withdrawSingleFee;// 超级还提现单笔手续费
	private String withdrawSingleFeeStr;// 超级还提现单笔手续费
	private String companyNo;// 公司编号
	private String companyName;// 公司名称

	private String servicePhone;// 服务电话
	private String publicAccount;// 公众号
	private String publicAccountName;// 公众号名称
	private String appid;// 公众号appid
	private String secret;// 公众号secret
	private String encodingAesKey;// 公众号密文
	private BigDecimal openRepayPrice;// 开通超级还售价
	private String openRepayCost;// 开通超级还代理成本
	private String openRepayPushCost;// 开通超级还发放成本

	private String isSupportPay;// 是否支持支付 ０-否 １-是
	private String publicQrCode;// 公众号二维码
	private String publicQrCodeUrl;// 公众号二维码
	private String appQrCode;// 公众号二维码
	private String appQrCodeUrl;// 公众号二维码


	private String redMoneyStatus;// 红包开关，0关闭 1开启
	private String redMoneyProfitStatus;// 单个订单红包超过品牌分润开关，0关闭 1开启
	private String businessEmail;// 商务邮箱
	private String uiStatus;// 平台UI风格，0超级银行家(自用) 1金色 2橙色 3黄色
	private String memberCenterLogo;// 会员中心LOGO
	private String memberCenterLogoUrl;// 会员中心LOGO
	private String startPage;// 启动页
	private String startPageUrl;// 启动页
	private String appLogo;// APP LOGO
	private String appLogoUrl;// APP LOGO
	private String shareMessageLogo;// 分享消息LOGO
	private String shareMessageLogoUrl;// 分享消息LOGO
	private String shareTemplateImage1;// 分享模板图片1
	private String shareTemplateImage1Url;// 分享模板图片1
	private String shareTemplateImage2;// 分享模板图片2
	private String shareTemplateImage2Url;// 分享模板图片2
	private String shareTemplateImage3;// 分享模板图片3
	private String shareTemplateImage3Url;// 分享模板图片3
	private String dayStart;// 当月day_start号到day_end号
	private String dayEnd;// 当月day_start号到day_end号
	private String monthCardNum;// 当月day_start号到day_end号，直推办理month_card_num张卡
	private String yearCardNum;// 当年累计直推办理多少张卡
	private BigDecimal redMoneyMin;// 红包随机金额，最小
	private BigDecimal redMoneyMax;// 红包随机金额，最大
	private BigDecimal withdrawMoneyMin;// 超过多少元的才能提现
	private String accountStatus; // 开户状态 0未开户 1已开户
	private String pushAppKey; // app推送key
	private String pushMasterSecret; // app推送secret
	private String indexStyle;// 首页样式：1 每行三列，2每行四列

	private String homeBackground;//首页背景
	private String homeBackgroundUrl;//首页背景

	private String isOpen;// 是否外放组织 0-否 1-是

	private String notifyUrl;// 订单结果通知地址

	private String batchOrderNotifyUrl;// 批量订单结果通知地址

	private String openAppUrl;// 外放平台APP首页地址

	private String openAppName;// 外放平台APP首页地址

	private String getUserinfoUrl;//用户信息查询地址

	private String aliPId;//支付宝支付PID
	private String aliAppId;//支付宝支付APPID
	private String aliPrivateKey;//支付宝商户私钥
	private String aliPublicKey;//支付宝公钥

	private String startAnalysisData; //开启数据分析定时任务0.否，1.开启
	private String publicKey;//超级兑公钥
	private String privateKey;//超级兑密钥
	private String cjdAppKey;//超级兑key
	private String cjdOemOn;//超级兑oem编号
	private  int pointExchangeRatio;//用户积分兑换比例
	private BigDecimal platformCost;//积分兑换平台成本

    private List<ModulesNewStyles> tutorModelList; //优秀导师模块
    private List<ModulesNewStyles> bankModelList; //银行家大学模块-训练营展示图片

    private String isUserSendprofit;			//用户是否发放佣金 0-否 1-是

	private String userAlias;				//普通会员别称
	private String memberAlias;				//专员别称
	private String managerAlias;				//经理别称
	private String bankerAlias;				//银行家别称

	private String isUserShare;					//普通用户是否可以分享 0-否 1-是
	private String isProfitBankerBanker;		//银行家上面的银行家是否参与分润 0-否 1-是

	private String upMemberNeedpay;			//升专员是否需要缴费 0-否 1-是
	private String upMemberNeedperfect;		//升专员是否需要完善资料 0-否 1-是
	private String upMemberNeedlock;			//升专员是否需要锁粉数 0-否 1-是
	private Integer upMemberLocknum;				//升专员需锁粉数
	private Integer upMemberMposnum;				//升专员需采购mpos数

	private Integer upManagerLocknum;				//升经理需锁粉数
	private Integer upManagerMposnum;				//升经理需采购mpos数

	private Integer upBankerLocknum;				//升银行家需锁粉数
	private Integer upBankerMposnum;				//升银行家需采购mpos数
	private String checkIds;					//选中的业务名称


	public String getStartAnalysisData() {
		return startAnalysisData;
	}

	public void setStartAnalysisData(String startAnalysisData) {
		this.startAnalysisData = startAnalysisData;
	}

	public String getOpenAppName() {
		return openAppName;
	}

	public void setOpenAppName(String openAppName) {
		this.openAppName = openAppName;
	}

	private String openAppIntro;// 外放组织应用简介

	private String openMerchantKey;// 商户应用密钥

	private String keyVersion;// 商户应用密钥版本

	private String functionId;

	private List<OpenFunctionConf> openFunctionConfList;

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getBatchOrderNotifyUrl() {
		return batchOrderNotifyUrl;
	}

	public void setBatchOrderNotifyUrl(String batchOrderNotifyUrl) {
		this.batchOrderNotifyUrl = batchOrderNotifyUrl;
	}

	public String getOpenAppUrl() {
		return openAppUrl;
	}

	public void setOpenAppUrl(String openAppUrl) {
		this.openAppUrl = openAppUrl;
	}

	public String getOpenAppIntro() {
		return openAppIntro;
	}

	public void setOpenAppIntro(String openAppIntro) {
		this.openAppIntro = openAppIntro;
	}

	public String getOpenMerchantKey() {
		return openMerchantKey;
	}

	public void setOpenMerchantKey(String openMerchantKey) {
		this.openMerchantKey = openMerchantKey;
	}

	public String getKeyVersion() {
		return keyVersion;
	}

	public void setKeyVersion(String keyVersion) {
		this.keyVersion = keyVersion;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName == null ? null : orgName.trim();
	}

	public String getOrgLogo() {
		return orgLogo;
	}

	public void setOrgLogo(String orgLogo) {
		this.orgLogo = orgLogo == null ? null : orgLogo.trim();
	}

	public String getWeixinSign() {
		return weixinSign;
	}

	public void setWeixinSign(String weixinSign) {
		this.weixinSign = weixinSign == null ? null : weixinSign.trim();
	}

	public String getSuperOrgcode() {
		return superOrgcode;
	}

	public void setSuperOrgcode(String superOrgcode) {
		this.superOrgcode = superOrgcode == null ? null : superOrgcode.trim();
	}

	public String getV2AgentNumber() {
		return v2AgentNumber;
	}

	public void setV2AgentNumber(String v2AgentNumber) {
		this.v2AgentNumber = v2AgentNumber == null ? null : v2AgentNumber.trim();
	}

	public String getV2Orgcode() {
		return v2Orgcode;
	}

	public void setV2Orgcode(String v2Orgcode) {
		this.v2Orgcode = v2Orgcode == null ? null : v2Orgcode.trim();
	}

	public BigDecimal getAgentPrice() {
		return agentPrice;
	}

	public void setAgentPrice(BigDecimal agentPrice) {
		this.agentPrice = agentPrice;
	}

	public BigDecimal getAgentCost() {
		return agentCost;
	}

	public void setAgentCost(BigDecimal agentCost) {
		this.agentCost = agentCost;
	}

	public BigDecimal getAgentPushCost() {
		return agentPushCost;
	}

	public void setAgentPushCost(BigDecimal agentPushCost) {
		this.agentPushCost = agentPushCost;
	}

	public String getReceivePushCost() {
		return receivePushCost;
	}

	public void setReceivePushCost(String receivePushCost) {
		this.receivePushCost = receivePushCost == null ? null : receivePushCost.trim();
	}

	public String getRepaymentCost() {
		return repaymentCost;
	}

	public void setRepaymentCost(String repaymentCost) {
		this.repaymentCost = repaymentCost == null ? null : repaymentCost.trim();
	}

	public String getRepaymentPushCost() {
		return repaymentPushCost;
	}

	public void setRepaymentPushCost(String repaymentPushCost) {
		this.repaymentPushCost = repaymentPushCost == null ? null : repaymentPushCost.trim();
	}

	public Integer getUpManagerNum() {
		return upManagerNum;
	}

	public void setUpManagerNum(Integer upManagerNum) {
		this.upManagerNum = upManagerNum;
	}

	public Integer getUpManagerCardnum() {
		return upManagerCardnum;
	}

	public void setUpManagerCardnum(Integer upManagerCardnum) {
		this.upManagerCardnum = upManagerCardnum;
	}

	public Integer getUpBankerNum() {
		return upBankerNum;
	}

	public void setUpBankerNum(Integer upBankerNum) {
		this.upBankerNum = upBankerNum;
	}

	public Integer getUpBankerCardnum() {
		return upBankerCardnum;
	}

	public void setUpBankerCardnum(Integer upBankerCardnum) {
		this.upBankerCardnum = upBankerCardnum;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getOrgLogoUrl() {
		return orgLogoUrl;
	}

	public void setOrgLogoUrl(String orgLogoUrl) {
		this.orgLogoUrl = orgLogoUrl;
	}

	public Double getTradeFeeRate() {
		return tradeFeeRate;
	}

	public void setTradeFeeRate(Double tradeFeeRate) {
		this.tradeFeeRate = tradeFeeRate;
	}

	public Double getTradeSingleFee() {
		return tradeSingleFee;
	}

	public void setTradeSingleFee(Double tradeSingleFee) {
		this.tradeSingleFee = tradeSingleFee;
	}

	public Double getWithdrawFeeRate() {
		return withdrawFeeRate;
	}

	public void setWithdrawFeeRate(Double withdrawFeeRate) {
		this.withdrawFeeRate = withdrawFeeRate;
	}

	public Double getWithdrawSingleFee() {
		return withdrawSingleFee;
	}

	public void setWithdrawSingleFee(Double withdrawSingleFee) {
		this.withdrawSingleFee = withdrawSingleFee;
	}

	public String getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getServicePhone() {
		return servicePhone;
	}

	public void setServicePhone(String servicePhone) {
		this.servicePhone = servicePhone;
	}

	public String getPublicAccount() {
		return publicAccount;
	}

	public void setPublicAccount(String publicAccount) {
		this.publicAccount = publicAccount;
	}

	public String getPublicAccountName() {
		return publicAccountName;
	}

	public void setPublicAccountName(String publicAccountName) {
		this.publicAccountName = publicAccountName;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getEncodingAesKey() {
		return encodingAesKey;
	}

	public void setEncodingAesKey(String encodingAesKey) {
		this.encodingAesKey = encodingAesKey;
	}

	public String getReceiveWxCost() {
		return receiveWxCost;
	}

	public void setReceiveWxCost(String receiveWxCost) {
		this.receiveWxCost = receiveWxCost;
	}

	public String getReceiveZfbCost() {
		return receiveZfbCost;
	}

	public void setReceiveZfbCost(String receiveZfbCost) {
		this.receiveZfbCost = receiveZfbCost;
	}

	public String getReceiveKjCost() {
		return receiveKjCost;
	}

	public void setReceiveKjCost(String receiveKjCost) {
		this.receiveKjCost = receiveKjCost;
	}

	public BigDecimal getOpenRepayPrice() {
		return openRepayPrice;
	}

	public void setOpenRepayPrice(BigDecimal openRepayPrice) {
		this.openRepayPrice = openRepayPrice;
	}

	public String getOpenRepayCost() {
		return openRepayCost;
	}

	public void setOpenRepayCost(String openRepayCost) {
		this.openRepayCost = openRepayCost;
	}

	public String getOpenRepayPushCost() {
		return openRepayPushCost;
	}

	public void setOpenRepayPushCost(String openRepayPushCost) {
		this.openRepayPushCost = openRepayPushCost;
	}

	public String getAuthorizedUnit() {
		return authorizedUnit;
	}

	public void setAuthorizedUnit(String authorizedUnit) {
		this.authorizedUnit = authorizedUnit;
	}

	public String getAuthorizedUnitSeal() {
		return authorizedUnitSeal;
	}

	public void setAuthorizedUnitSeal(String authorizedUnitSeal) {
		this.authorizedUnitSeal = authorizedUnitSeal;
	}

	public String getAuthorizedUnitSealUrl() {
		return authorizedUnitSealUrl;
	}

	public void setAuthorizedUnitSealUrl(String authorizedUnitSealUrl) {
		this.authorizedUnitSealUrl = authorizedUnitSealUrl;
	}

	public String getIsSupportPay() {
		return isSupportPay;
	}

	public void setIsSupportPay(String isSupportPay) {
		this.isSupportPay = isSupportPay;
	}

	public String getPublicQrCode() {
		return publicQrCode;
	}

	public void setPublicQrCode(String publicQrCode) {
		this.publicQrCode = publicQrCode;
	}

	public String getPublicQrCodeUrl() {
		return publicQrCodeUrl;
	}

	public void setPublicQrCodeUrl(String publicQrCodeUrl) {
		this.publicQrCodeUrl = publicQrCodeUrl;
	}

	public String getRedMoneyStatus() {
		return redMoneyStatus;
	}

	public void setRedMoneyStatus(String redMoneyStatus) {
		this.redMoneyStatus = redMoneyStatus;
	}

	public String getRedMoneyProfitStatus() {
		return redMoneyProfitStatus;
	}

	public void setRedMoneyProfitStatus(String redMoneyProfitStatus) {
		this.redMoneyProfitStatus = redMoneyProfitStatus;
	}

	public String getBusinessEmail() {
		return businessEmail;
	}

	public void setBusinessEmail(String businessEmail) {
		this.businessEmail = businessEmail;
	}

	public String getUiStatus() {
		return uiStatus;
	}

	public void setUiStatus(String uiStatus) {
		this.uiStatus = uiStatus;
	}

	public String getMemberCenterLogo() {
		return memberCenterLogo;
	}

	public void setMemberCenterLogo(String memberCenterLogo) {
		this.memberCenterLogo = memberCenterLogo;
	}

	public String getStartPage() {
		return startPage;
	}

	public void setStartPage(String startPage) {
		this.startPage = startPage;
	}

	public String getAppLogo() {
		return appLogo;
	}

	public void setAppLogo(String appLogo) {
		this.appLogo = appLogo;
	}

	public String getShareMessageLogo() {
		return shareMessageLogo;
	}

	public void setShareMessageLogo(String shareMessageLogo) {
		this.shareMessageLogo = shareMessageLogo;
	}

	public String getShareTemplateImage1() {
		return shareTemplateImage1;
	}

	public void setShareTemplateImage1(String shareTemplateImage1) {
		this.shareTemplateImage1 = shareTemplateImage1;
	}

	public String getShareTemplateImage2() {
		return shareTemplateImage2;
	}

	public void setShareTemplateImage2(String shareTemplateImage2) {
		this.shareTemplateImage2 = shareTemplateImage2;
	}

	public String getShareTemplateImage3() {
		return shareTemplateImage3;
	}

	public void setShareTemplateImage3(String shareTemplateImage3) {
		this.shareTemplateImage3 = shareTemplateImage3;
	}

	public String getDayStart() {
		return dayStart;
	}

	public void setDayStart(String dayStart) {
		this.dayStart = dayStart;
	}

	public String getDayEnd() {
		return dayEnd;
	}

	public void setDayEnd(String dayEnd) {
		this.dayEnd = dayEnd;
	}

	public String getMonthCardNum() {
		return monthCardNum;
	}

	public void setMonthCardNum(String monthCardNum) {
		this.monthCardNum = monthCardNum;
	}

	public String getYearCardNum() {
		return yearCardNum;
	}

	public void setYearCardNum(String yearCardNum) {
		this.yearCardNum = yearCardNum;
	}

	public BigDecimal getRedMoneyMin() {
		return redMoneyMin;
	}

	public void setRedMoneyMin(BigDecimal redMoneyMin) {
		this.redMoneyMin = redMoneyMin;
	}

	public BigDecimal getRedMoneyMax() {
		return redMoneyMax;
	}

	public void setRedMoneyMax(BigDecimal redMoneyMax) {
		this.redMoneyMax = redMoneyMax;
	}

	public BigDecimal getWithdrawMoneyMin() {
		return withdrawMoneyMin;
	}

	public void setWithdrawMoneyMin(BigDecimal withdrawMoneyMin) {
		this.withdrawMoneyMin = withdrawMoneyMin;
	}

	public String getMemberCenterLogoUrl() {
		return memberCenterLogoUrl;
	}

	public void setMemberCenterLogoUrl(String memberCenterLogoUrl) {
		this.memberCenterLogoUrl = memberCenterLogoUrl;
	}

	public String getStartPageUrl() {
		return startPageUrl;
	}

	public void setStartPageUrl(String startPageUrl) {
		this.startPageUrl = startPageUrl;
	}

	public String getAppLogoUrl() {
		return appLogoUrl;
	}

	public void setAppLogoUrl(String appLogoUrl) {
		this.appLogoUrl = appLogoUrl;
	}

	public String getShareMessageLogoUrl() {
		return shareMessageLogoUrl;
	}

	public void setShareMessageLogoUrl(String shareMessageLogoUrl) {
		this.shareMessageLogoUrl = shareMessageLogoUrl;
	}

	public String getShareTemplateImage1Url() {
		return shareTemplateImage1Url;
	}

	public void setShareTemplateImage1Url(String shareTemplateImage1Url) {
		this.shareTemplateImage1Url = shareTemplateImage1Url;
	}

	public String getShareTemplateImage2Url() {
		return shareTemplateImage2Url;
	}

	public void setShareTemplateImage2Url(String shareTemplateImage2Url) {
		this.shareTemplateImage2Url = shareTemplateImage2Url;
	}

	public String getShareTemplateImage3Url() {
		return shareTemplateImage3Url;
	}

	public void setShareTemplateImage3Url(String shareTemplateImage3Url) {
		this.shareTemplateImage3Url = shareTemplateImage3Url;
	}

	public String getTradeFeeRateStr() {
		return tradeFeeRateStr;
	}

	public void setTradeFeeRateStr(String tradeFeeRateStr) {
		this.tradeFeeRateStr = tradeFeeRateStr;
	}

	public String getTradeSingleFeeStr() {
		return tradeSingleFeeStr;
	}

	public void setTradeSingleFeeStr(String tradeSingleFeeStr) {
		this.tradeSingleFeeStr = tradeSingleFeeStr;
	}

	public String getWithdrawFeeRateStr() {
		return withdrawFeeRateStr;
	}

	public void setWithdrawFeeRateStr(String withdrawFeeRateStr) {
		this.withdrawFeeRateStr = withdrawFeeRateStr;
	}

	public String getWithdrawSingleFeeStr() {
		return withdrawSingleFeeStr;
	}

	public void setWithdrawSingleFeeStr(String withdrawSingleFeeStr) {
		this.withdrawSingleFeeStr = withdrawSingleFeeStr;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getPushAppKey() {
		return pushAppKey;
	}

	public void setPushAppKey(String pushAppKey) {
		this.pushAppKey = pushAppKey;
	}

	public String getPushMasterSecret() {
		return pushMasterSecret;
	}

	public void setPushMasterSecret(String pushMasterSecret) {
		this.pushMasterSecret = pushMasterSecret;
	}

	public String getIndexStyle() {
		return indexStyle;
	}

	public void setIndexStyle(String indexStyle) {
		this.indexStyle = indexStyle;
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public List<OpenFunctionConf> getOpenFunctionConfList() {
		return openFunctionConfList;
	}

	public void setOpenFunctionConfList(List<OpenFunctionConf> openFunctionConfList) {
		this.openFunctionConfList = openFunctionConfList;
	}

	public String getHomeBackground() {
		return homeBackground;
	}

	public void setHomeBackground(String homeBackground) {
		this.homeBackground = homeBackground;
	}


	public String getHomeBackgroundUrl() {
		return homeBackgroundUrl;
	}

	public void setHomeBackgroundUrl(String homeBackgroundUrl) {
		this.homeBackgroundUrl = homeBackgroundUrl;
	}

	public String getGetUserinfoUrl() {
		return getUserinfoUrl;
	}

	public void setGetUserinfoUrl(String getUserinfoUrl) {
		this.getUserinfoUrl = getUserinfoUrl;
	}

	public String getAliPId() {
		return aliPId;
	}

	public void setAliPId(String aliPId) {
		this.aliPId = aliPId;
	}

	public String getAliAppId() {
		return aliAppId;
	}

	public void setAliAppId(String aliAppId) {
		this.aliAppId = aliAppId;
	}

	public String getAliPrivateKey() {
		return aliPrivateKey;
	}

	public void setAliPrivateKey(String aliPrivateKey) {
		this.aliPrivateKey = aliPrivateKey;
	}

	public String getAliPublicKey() {
		return aliPublicKey;
	}

	public void setAliPublicKey(String aliPublicKey) {
		this.aliPublicKey = aliPublicKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getCjdAppKey() {
		return cjdAppKey;
	}

	public void setCjdAppKey(String cjdAppKey) {
		this.cjdAppKey = cjdAppKey;
	}

	public String getCjdOemOn() {
		return cjdOemOn;
	}

	public void setCjdOemOn(String cjdOemOn) {
		this.cjdOemOn = cjdOemOn;
	}

	public int getPointExchangeRatio() {
		return pointExchangeRatio;
	}

	public void setPointExchangeRatio(int pointExchangeRatio) {
		this.pointExchangeRatio = pointExchangeRatio;
	}

	public BigDecimal getPlatformCost() {
		return platformCost;
	}

	public void setPlatformCost(BigDecimal platformCost) {
		this.platformCost = platformCost;
	}
	public String getTradeFeeRateWmjhStr() {
		return tradeFeeRateWmjhStr;
	}

	public void setTradeFeeRateWmjhStr(String tradeFeeRateWmjhStr) {
		this.tradeFeeRateWmjhStr = tradeFeeRateWmjhStr;
	}

	public String getTradeSingleFeeWmjhStr() {
		return tradeSingleFeeWmjhStr;
	}

	public void setTradeSingleFeeWmjhStr(String tradeSingleFeeWmjhStr) {
		this.tradeSingleFeeWmjhStr = tradeSingleFeeWmjhStr;
	}
	public String getRepaymentCostWmjh() {
		return repaymentCostWmjh;
	}

	public void setRepaymentCostWmjh(String repaymentCostWmjh) {
		this.repaymentCostWmjh = repaymentCostWmjh;
	}

	public String getRepaymentPushCostWmjh() {
		return repaymentPushCostWmjh;
	}

	public void setRepaymentPushCostWmjh(String repaymentPushCostWmjh) {
		this.repaymentPushCostWmjh = repaymentPushCostWmjh;
	}

	public Double getTradeFeeRateWmjh() {
		return tradeFeeRateWmjh;
	}

	public void setTradeFeeRateWmjh(Double tradeFeeRateWmjh) {
		this.tradeFeeRateWmjh = tradeFeeRateWmjh;
	}

	public Double getTradeSingleFeeWmjh() {
		return tradeSingleFeeWmjh;
	}

	public void setTradeSingleFeeWmjh(Double tradeSingleFeeWmjh) {
		this.tradeSingleFeeWmjh = tradeSingleFeeWmjh;
	}

	public List<ModulesNewStyles> getTutorModelList() {
		return tutorModelList;
	}

	public void setTutorModelList(List<ModulesNewStyles> tutorModelList) {
		this.tutorModelList = tutorModelList;
	}

	public List<ModulesNewStyles> getBankModelList() {
		return bankModelList;
	}

	public void setBankModelList(List<ModulesNewStyles> bankModelList) {
		this.bankModelList = bankModelList;
	}

	public String getIsUserSendprofit() {
		return isUserSendprofit;
	}

	public void setIsUserSendprofit(String isUserSendprofit) {
		this.isUserSendprofit = isUserSendprofit;
	}

	public String getUserAlias() {
		return userAlias;
	}

	public void setUserAlias(String userAlias) {
		this.userAlias = userAlias;
	}

	public String getMemberAlias() {
		return memberAlias;
	}

	public void setMemberAlias(String memberAlias) {
		this.memberAlias = memberAlias;
	}

	public String getManagerAlias() {
		return managerAlias;
	}

	public void setManagerAlias(String managerAlias) {
		this.managerAlias = managerAlias;
	}

	public String getBankerAlias() {
		return bankerAlias;
	}

	public void setBankerAlias(String bankerAlias) {
		this.bankerAlias = bankerAlias;
	}

	public String getIsUserShare() {
		return isUserShare;
	}

	public void setIsUserShare(String isUserShare) {
		this.isUserShare = isUserShare;
	}

	public String getIsProfitBankerBanker() {
		return isProfitBankerBanker;
	}

	public void setIsProfitBankerBanker(String isProfitBankerBanker) {
		this.isProfitBankerBanker = isProfitBankerBanker;
	}

	public String getUpMemberNeedpay() {
		return upMemberNeedpay;
	}

	public void setUpMemberNeedpay(String upMemberNeedpay) {
		this.upMemberNeedpay = upMemberNeedpay;
	}

	public String getUpMemberNeedperfect() {
		return upMemberNeedperfect;
	}

	public void setUpMemberNeedperfect(String upMemberNeedperfect) {
		this.upMemberNeedperfect = upMemberNeedperfect;
	}

	public String getUpMemberNeedlock() {
		return upMemberNeedlock;
	}

	public void setUpMemberNeedlock(String upMemberNeedlock) {
		this.upMemberNeedlock = upMemberNeedlock;
	}

	public Integer getUpMemberLocknum() {
		return upMemberLocknum;
	}

	public void setUpMemberLocknum(Integer upMemberLocknum) {
		this.upMemberLocknum = upMemberLocknum;
	}

	public Integer getUpMemberMposnum() {
		return upMemberMposnum;
	}

	public void setUpMemberMposnum(Integer upMemberMposnum) {
		this.upMemberMposnum = upMemberMposnum;
	}

	public Integer getUpManagerLocknum() {
		return upManagerLocknum;
	}

	public void setUpManagerLocknum(Integer upManagerLocknum) {
		this.upManagerLocknum = upManagerLocknum;
	}

	public Integer getUpManagerMposnum() {
		return upManagerMposnum;
	}

	public void setUpManagerMposnum(Integer upManagerMposnum) {
		this.upManagerMposnum = upManagerMposnum;
	}

	public Integer getUpBankerLocknum() {
		return upBankerLocknum;
	}

	public void setUpBankerLocknum(Integer upBankerLocknum) {
		this.upBankerLocknum = upBankerLocknum;
	}

	public Integer getUpBankerMposnum() {
		return upBankerMposnum;
	}

	public void setUpBankerMposnum(Integer upBankerMposnum) {
		this.upBankerMposnum = upBankerMposnum;
	}

	public String getCheckIds() {
		return checkIds;
	}

	public void setCheckIds(String checkIds) {
		this.checkIds = checkIds;
	}

	public String getAppQrCode() {
		return appQrCode;
	}

	public void setAppQrCode(String appQrCode) {
		this.appQrCode = appQrCode;
	}

	public String getAppQrCodeUrl() {
		return appQrCodeUrl;
	}

	public void setAppQrCodeUrl(String appQrCodeUrl) {
		this.appQrCodeUrl = appQrCodeUrl;
	}
}