package cn.eeepay.framework.model;//

import java.math.BigDecimal;//
import java.util.Date;//

/**
 * 用于优惠券赠送明细查询和统计
 * @author ivan
 *
 */
public class CouponTotal {
	private int id;//优惠券ID
	private String merchantName;//商户名称
	private String merchantNo;//商户编码
	private String mobilephone;//商户手机号
	private String  couponCode;//活动类型
	private String agentName;//代理商名称
	private String oneAgentName;//一级代理商名称
	private BigDecimal giftAmount;//赠送金额（针对充值返）
	private BigDecimal transAmount;//购买金额（针对充值返）
	private String payOrderNo;//支付订单号（针对充值返）
	private BigDecimal faceValue;//面值
	private BigDecimal balance;//可用金额
	private BigDecimal useValue;//已使用金额
	private String couponStatus;//状态
	private Date startTime;//赠送日期
	private Date endTime;//失效日期
	private String couponNo;//优惠券编码
	private BigDecimal totalFaceValue;//赠送金额合计
	private BigDecimal totalBalance;//可用金额合计
	private BigDecimal totalUseValue;//已使用金额合计
	private BigDecimal totalInvalidValue;//失效金额合计

	private BigDecimal transAmountBeg;
	private BigDecimal transAmountEnd;
	private BigDecimal giftAmountBeg;
	private BigDecimal giftAmountEnd;
	private String couponNoBeg;
	private String couponNoEnd;

	private Date endStartTime;//赠送日期
	private Date endEndTime;//失效日期

	private String payMethod;//支付方式

	private int pageFirst;//每页开始第几行
	private int pageSize;//每页大小
	private int totalNum;

	private String couponType;//券类型


	public Date getEndStartTime() {
		return endStartTime;
	}

	public void setEndStartTime(Date endStartTime) {
		this.endStartTime = endStartTime;
	}

	public Date getEndEndTime() {
		return endEndTime;
	}

	public void setEndEndTime(Date endEndTime) {
		this.endEndTime = endEndTime;
	}

	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
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
	public BigDecimal getFaceValue() {
		return faceValue;
	}
	public void setFaceValue(BigDecimal faceValue) {
		this.faceValue = faceValue;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getUseValue() {
		return useValue;
	}
	public void setUseValue(BigDecimal useValue) {
		this.useValue = useValue;
	}
	public String getCouponStatus() {
		return couponStatus;
	}
	public void setCouponStatus(String couponStatus) {
		this.couponStatus = couponStatus;
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
	public String getCouponNo() {
		return couponNo;
	}
	public void setCouponNo(String couponNo) {
		this.couponNo = couponNo;
	}
	public BigDecimal getTotalFaceValue() {
		return totalFaceValue;
	}
	public void setTotalFaceValue(BigDecimal totalFaceValue) {
		this.totalFaceValue = totalFaceValue;
	}
	public BigDecimal getTotalBalance() {
		return totalBalance;
	}
	public void setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
	}
	public BigDecimal getTotalUseValue() {
		return totalUseValue;
	}
	public void setTotalUseValue(BigDecimal totalUseValue) {
		this.totalUseValue = totalUseValue;
	}
	public BigDecimal getTotalInvalidValue() {
		return totalInvalidValue;
	}
	public void setTotalInvalidValue(BigDecimal totalInvalidValue) {
		this.totalInvalidValue = totalInvalidValue;
	}

	public BigDecimal getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}

	public String getPayOrderNo() {
		return payOrderNo;
	}

	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
	}

	public BigDecimal getGiftAmount() {
		return giftAmount;
	}

	public void setGiftAmount(BigDecimal giftAmount) {
		this.giftAmount = giftAmount;
	}

	public BigDecimal getTransAmountBeg() {
		return transAmountBeg;
	}

	public void setTransAmountBeg(BigDecimal transAmountBeg) {
		this.transAmountBeg = transAmountBeg;
	}

	public BigDecimal getTransAmountEnd() {
		return transAmountEnd;
	}

	public void setTransAmountEnd(BigDecimal transAmountEnd) {
		this.transAmountEnd = transAmountEnd;
	}

	public BigDecimal getGiftAmountBeg() {
		return giftAmountBeg;
	}

	public void setGiftAmountBeg(BigDecimal giftAmountBeg) {
		this.giftAmountBeg = giftAmountBeg;
	}

	public BigDecimal getGiftAmountEnd() {
		return giftAmountEnd;
	}

	public void setGiftAmountEnd(BigDecimal giftAmountEnd) {
		this.giftAmountEnd = giftAmountEnd;
	}

	public String getCouponNoBeg() {
		return couponNoBeg;
	}

	public void setCouponNoBeg(String couponNoBeg) {
		this.couponNoBeg = couponNoBeg;
	}

	public String getCouponNoEnd() {
		return couponNoEnd;
	}

	public void setCouponNoEnd(String couponNoEnd) {
		this.couponNoEnd = couponNoEnd;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public int getPageFirst() {
		return pageFirst;
	}

	public void setPageFirst(int pageFirst) {
		this.pageFirst = pageFirst;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}
}
