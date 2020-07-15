package cn.eeepay.framework.model;


import java.util.Date;

public class RepayMerchantInfo {

	public static String NFC_START_DATE = "2020-03-02";

	private Integer id;
	private String merchantNo;
	private String openid;
	private String cardNo;
	private String transRate;
	private String oneAgentNo;
	private String agentNo;
	private String agentNode;
	private String userName;
	private String idCardNo;
	private String mobileNo;
	private String merAccount;	//商户是否已在账户(1为是0为否)
	private Date createTime;	//激活时间(商户表的创建时间)

	private Date enterTime;	//入驻时间(微信用户表的创建时间)
	private String nickname;	//昵称
	private String agentName;

	private String sCreateTime;
	private String eCreateTime;
	private String sEnterTime;
	private String eEnterTime;

	private String sfzzmUrl;	//身份证正面
	private String sfzfmUrl;	//身份证反面
	private String scsfzUrl;	//手持身份证
	private String headimgurl;	//微信头像

	private String proMerNo;	//业务商户号
	private String oneAgentName;

	private String status;//商户状态 0-正常 1-关闭
	private int statusValue;//商户状态对应的数值

	private String selectType;//"nfc":表示超级NFC，其他都表示查询超级还
	private String nfcStartDate;

	public String getOneAgentName() {
		return oneAgentName;
	}
	public void setOneAgentName(String oneAgentName) {
		this.oneAgentName = oneAgentName;
	}
	public String getProMerNo() {
		return proMerNo;
	}
	public void setProMerNo(String proMerNo) {
		this.proMerNo = proMerNo;
	}
	public String getMerAccount() {
		return merAccount;
	}
	public void setMerAccount(String merAccount) {
		this.merAccount = merAccount;
	}
	public String getSfzzmUrl() {
		return sfzzmUrl;
	}
	public void setSfzzmUrl(String sfzzmUrl) {
		this.sfzzmUrl = sfzzmUrl;
	}
	public String getSfzfmUrl() {
		return sfzfmUrl;
	}
	public void setSfzfmUrl(String sfzfmUrl) {
		this.sfzfmUrl = sfzfmUrl;
	}
	public String getScsfzUrl() {
		return scsfzUrl;
	}
	public void setScsfzUrl(String scsfzUrl) {
		this.scsfzUrl = scsfzUrl;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getTransRate() {
		return transRate;
	}
	public void setTransRate(String transRate) {
		this.transRate = transRate;
	}
	public String getOneAgentNo() {
		return oneAgentNo;
	}
	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}
	public String getAgentNo() {
		return agentNo;
	}
	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}
	public String getAgentNode() {
		return agentNode;
	}
	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getEnterTime() {
		return enterTime;
	}
	public void setEnterTime(Date enterTime) {
		this.enterTime = enterTime;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getsCreateTime() {
		return sCreateTime;
	}
	public void setsCreateTime(String sCreateTime) {
		this.sCreateTime = sCreateTime;
	}
	public String geteCreateTime() {
		return eCreateTime;
	}
	public void seteCreateTime(String eCreateTime) {
		this.eCreateTime = eCreateTime;
	}
	public String getsEnterTime() {
		return sEnterTime;
	}
	public void setsEnterTime(String sEnterTime) {
		this.sEnterTime = sEnterTime;
	}
	public String geteEnterTime() {
		return eEnterTime;
	}
	public void seteEnterTime(String eEnterTime) {
		this.eEnterTime = eEnterTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getStatusValue() {
		return statusValue;
	}
	public void setStatusValue(int statusValue) {
		this.statusValue = statusValue;
	}

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}

	public String getNfcStartDate() {
		return nfcStartDate;
	}

	public void setNfcStartDate(String nfcStartDate) {
		this.nfcStartDate = nfcStartDate;
	}
}
