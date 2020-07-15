package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 云闪付
 * @author mays
 * @date 2017年10月23日
 */
public class CloudPayInfo {

	private Integer id;
	private String merchantNo;		//商户编号
	private BigDecimal transAmount;	//交易金额
	private BigDecimal transFee;	//交易手续费(商户手续费)
	private BigDecimal discountFee;	//优惠后手续费
	private BigDecimal merchantProfit;	//收益金额
	private BigDecimal transRate;	//活动交易费率
	private String activityCode;	//活动类型(数据字典KEY：COUPON_CODE)
	private String orderNo;			//交易订单号
	private Date createTime;		//创建时间
	private String createPerson;	//创建人
	private String remark;

	private String merchantName;	//商户名称
	private String mobilephone;		//商户手机号
	private String agentNo;			//直属代理商编号
	private String agentName;		//直属代理商名称
	private String oneAgentName;	//一级代理商名称

	private String sTime;
	private String eTime;
	private BigDecimal sTransAmount;
	private BigDecimal eTransAmount;
	private BigDecimal sMerchantProfit;
	private BigDecimal eMerchantProfit;

	public BigDecimal getsTransAmount() {
		return sTransAmount;
	}
	public void setsTransAmount(BigDecimal sTransAmount) {
		this.sTransAmount = sTransAmount;
	}
	public BigDecimal geteTransAmount() {
		return eTransAmount;
	}
	public void seteTransAmount(BigDecimal eTransAmount) {
		this.eTransAmount = eTransAmount;
	}
	public BigDecimal getsMerchantProfit() {
		return sMerchantProfit;
	}
	public void setsMerchantProfit(BigDecimal sMerchantProfit) {
		this.sMerchantProfit = sMerchantProfit;
	}
	public BigDecimal geteMerchantProfit() {
		return eMerchantProfit;
	}
	public void seteMerchantProfit(BigDecimal eMerchantProfit) {
		this.eMerchantProfit = eMerchantProfit;
	}
	public String getsTime() {
		return sTime;
	}
	public void setsTime(String sTime) {
		this.sTime = sTime;
	}
	public String geteTime() {
		return eTime;
	}
	public void seteTime(String eTime) {
		this.eTime = eTime;
	}
	public String getAgentNo() {
		return agentNo;
	}
	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
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
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getOneAgentName() {
		return oneAgentName;
	}
	public void setOneAgentName(String oneAgentName) {
		this.oneAgentName = oneAgentName;
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
	public BigDecimal getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}
	public BigDecimal getTransFee() {
		return transFee;
	}
	public void setTransFee(BigDecimal transFee) {
		this.transFee = transFee;
	}
	public BigDecimal getDiscountFee() {
		return discountFee;
	}
	public void setDiscountFee(BigDecimal discountFee) {
		this.discountFee = discountFee;
	}
	public BigDecimal getMerchantProfit() {
		return merchantProfit;
	}
	public void setMerchantProfit(BigDecimal merchantProfit) {
		this.merchantProfit = merchantProfit;
	}
	public BigDecimal getTransRate() {
		return transRate;
	}
	public void setTransRate(BigDecimal transRate) {
		this.transRate = transRate;
	}
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreatePerson() {
		return createPerson;
	}
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
