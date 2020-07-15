package cn.eeepay.framework.model.cjt;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 新版超级推商户表 cjt_merchant_info
 * 
 * @author tans
 * @date 2019-06-14
 */
public class CjtMerchantInfo
{

	/**  */
	private Integer id;
	/** 用户ID,对应user_info表 */
	private String userId;
	/** 用户状态 0异常 1正常,2未完善 */
	private String status;
	/** 本级商户编号 */
	private String merchantNo;
	/** 来源 1商户分享链接 2绑定超级推机具 */
	private String source;
	/** 上一级商户编号 */
	private String oneMerchantNo;
	/** 上二级商户编号 */
	private String twoMerchantNo;
	/** 本级商户节点(two_merchant_no-one_merchant_no-merchant_no) */
	private String merchantNode;
	/** 本级商户直属代理商的节点 */
	private String agentNode;
	/** 创建时间 */
	private Date createTime;
	/** 数据最后更新时间 */
	private Date lastUpdateTime;

	private String merchantName;//商户名称
	private String mobilephone;//商户手机号
	private String activityStatus;//活动达标状态，0-未达标 1-已达标
	private String terBindStatus;//机具绑定状态，0-未绑定，1-已绑定
	private String terApplyStatus;//机具申领状态，0-未申领，1-已申领
	private String createTimeStart;
	private String createTimeEnd;
	private String agentNo;
	private String agentName;
	private BigDecimal profitAmount;//分润金额，累计收益
	private String oneAgentNo;//一级代理商编号
	private String lawyer;//法人
	private String idCardNo;//身份证号
	private String merchantStatus;//商户状态
	private BigDecimal preFrozenAmount;//预冻结金额
	private String merchantType;//商户类型
	private String address;//商户经营详细地址
	private String saleName;//一级代理商所属销售
	private String oneAgentName;//一级代理商
	private String oneMerchantName;
	private String twoMerchantName;
	private BigDecimal avaliBalance;//可用余额

	private BigDecimal haveAmount;//已累计的交易金额
	private BigDecimal targetAmount;//达标目标金额
	private Date startTime;//活动达标日期
	private Date endTime;//活动达标截止日期
	private String targetTimeStart;
	private String targetTimeEnd;
	private Date targetTime;//达标时间


	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setUserId(String userId) 
	{
		this.userId = userId;
	}

	public String getUserId() 
	{
		return userId;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}
	public void setMerchantNo(String merchantNo) 
	{
		this.merchantNo = merchantNo;
	}

	public String getMerchantNo() 
	{
		return merchantNo;
	}
	public void setSource(String source) 
	{
		this.source = source;
	}

	public String getSource() 
	{
		return source;
	}
	public void setOneMerchantNo(String oneMerchantNo) 
	{
		this.oneMerchantNo = oneMerchantNo;
	}

	public String getOneMerchantNo() 
	{
		return oneMerchantNo;
	}
	public void setTwoMerchantNo(String twoMerchantNo) 
	{
		this.twoMerchantNo = twoMerchantNo;
	}

	public String getTwoMerchantNo() 
	{
		return twoMerchantNo;
	}
	public void setMerchantNode(String merchantNode) 
	{
		this.merchantNode = merchantNode;
	}

	public String getMerchantNode() 
	{
		return merchantNode;
	}
	public void setAgentNode(String agentNode) 
	{
		this.agentNode = agentNode;
	}

	public String getAgentNode() 
	{
		return agentNode;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) 
	{
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getLastUpdateTime() 
	{
		return lastUpdateTime;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public String getTerBindStatus() {
		return terBindStatus;
	}

	public void setTerBindStatus(String terBindStatus) {
		this.terBindStatus = terBindStatus;
	}

	public String getTerApplyStatus() {
		return terApplyStatus;
	}

	public void setTerApplyStatus(String terApplyStatus) {
		this.terApplyStatus = terApplyStatus;
	}

	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public BigDecimal getProfitAmount() {
		return profitAmount;
	}

	public void setProfitAmount(BigDecimal profitAmount) {
		this.profitAmount = profitAmount;
	}

	public String getOneAgentNo() {
		return oneAgentNo;
	}

	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}

	public String getLawyer() {
		return lawyer;
	}

	public void setLawyer(String lawyer) {
		this.lawyer = lawyer;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getMerchantStatus() {
		return merchantStatus;
	}

	public void setMerchantStatus(String merchantStatus) {
		this.merchantStatus = merchantStatus;
	}

	public BigDecimal getPreFrozenAmount() {
		return preFrozenAmount;
	}

	public void setPreFrozenAmount(BigDecimal preFrozenAmount) {
		this.preFrozenAmount = preFrozenAmount;
	}

	public String getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}

	public String getOneAgentName() {
		return oneAgentName;
	}

	public void setOneAgentName(String oneAgentName) {
		this.oneAgentName = oneAgentName;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getOneMerchantName() {
		return oneMerchantName;
	}

	public void setOneMerchantName(String oneMerchantName) {
		this.oneMerchantName = oneMerchantName;
	}

	public String getTwoMerchantName() {
		return twoMerchantName;
	}

	public void setTwoMerchantName(String twoMerchantName) {
		this.twoMerchantName = twoMerchantName;
	}

	public BigDecimal getAvaliBalance() {
		return avaliBalance;
	}

	public void setAvaliBalance(BigDecimal avaliBalance) {
		this.avaliBalance = avaliBalance;
	}

	public BigDecimal getHaveAmount() {
		return haveAmount;
	}

	public void setHaveAmount(BigDecimal haveAmount) {
		this.haveAmount = haveAmount;
	}

	public BigDecimal getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(BigDecimal targetAmount) {
		this.targetAmount = targetAmount;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getTargetTimeStart() {
		return targetTimeStart;
	}

	public void setTargetTimeStart(String targetTimeStart) {
		this.targetTimeStart = targetTimeStart;
	}

	public String getTargetTimeEnd() {
		return targetTimeEnd;
	}

	public void setTargetTimeEnd(String targetTimeEnd) {
		this.targetTimeEnd = targetTimeEnd;
	}

	public Date getTargetTime() {
		return targetTime;
	}

	public void setTargetTime(Date targetTime) {
		this.targetTime = targetTime;
	}
}
