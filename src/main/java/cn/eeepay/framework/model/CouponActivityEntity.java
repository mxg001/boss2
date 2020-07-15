package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 活动优惠券配置
 * @author ivan
 *
 */
public class CouponActivityEntity {
	 private int id; //主键
	 private String activetiyCode; //活动编号(详见字典表 COUPON_TYPE)
	 private String couponCode; //优惠卷类型(详见字典表 COUPON_TYPE)
	 private String couponName; //优惠券名称
	 private BigDecimal couponAmount; //优惠券面值
	 private int couponCount; //可参与次数、可获取数量、优惠券日发行数（-1：不限）
	 private String  cancelVerificationCode; //用途、核销方式(详见字典表CANCEL_VERIFICATION_TYPE)
	 private Date createTime; //创建时间
	 private Date  updateTime; //更新时间
	 private String operator; //操作人
	 private int  isstatus; //状态:0 有效、1关闭
	 private BigDecimal  giftAmount; //赠送金额
	 private String couponExplain; //券说明
	 private int purchaseCount; //商户每天可购买数
	 private int purchaseTotal; //可购买总数
	 private int integralScale; //与积分兑换比例
	 private int  isshelves; //是否上架：1、上架 2、下架
	 private String activityFirst;//活动优先级：A-E优先级递增
	 private int effectiveDays;//'有效天数
	 private int surplusCount;//当日剩余
	 private String orderBy;//当日剩余
	 private BigDecimal transRate;//交易费率（%）
	 private String couponType;//卷原有类型（抽奖）
	 private BigDecimal backRate;//返现比例
	 private BigDecimal couponStandard;//达标金额

	private int sum;

	public BigDecimal getTransRate() {
		return transRate;
	}

	public void setTransRate(BigDecimal transRate) {
		this.transRate = transRate;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public int getSurplusCount() {
		return surplusCount;
	}
	public void setSurplusCount(int surplusCount) {
		this.surplusCount = surplusCount;
	}
	public String getActivityFirst() {
		return activityFirst;
	}
	public void setActivityFirst(String activityFirst) {
		this.activityFirst = activityFirst;
	}
	public int getEffectiveDays() {
		return effectiveDays;
	}
	public void setEffectiveDays(int effectiveDays) {
		this.effectiveDays = effectiveDays;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getActivetiyCode() {
		return activetiyCode;
	}
	public void setActivetiyCode(String activetiyCode) {
		this.activetiyCode = activetiyCode;
	}
	
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public BigDecimal getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(BigDecimal couponAmount) {
		this.couponAmount = couponAmount;
	}
	public int getCouponCount() {
		return couponCount;
	}
	public void setCouponCount(int couponCount) {
		this.couponCount = couponCount;
	}
	public String getCancelVerificationCode() {
		return cancelVerificationCode;
	}
	public void setCancelVerificationCode(String cancelVerificationCode) {
		this.cancelVerificationCode = cancelVerificationCode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
	public int getIsstatus() {
		return isstatus;
	}
	public void setIsstatus(int isstatus) {
		this.isstatus = isstatus;
	}
	public BigDecimal getGiftAmount() {
		return giftAmount;
	}
	public void setGiftAmount(BigDecimal giftAmount) {
		this.giftAmount = giftAmount;
	}
	public String getCouponExplain() {
		return couponExplain;
	}
	public void setCouponExplain(String couponExplain) {
		this.couponExplain = couponExplain;
	}
	public int getPurchaseCount() {
		return purchaseCount;
	}
	public void setPurchaseCount(int purchaseCount) {
		this.purchaseCount = purchaseCount;
	}
	public int getPurchaseTotal() {
		return purchaseTotal;
	}
	public void setPurchaseTotal(int purchaseTotal) {
		this.purchaseTotal = purchaseTotal;
	}
	public int getIntegralScale() {
		return integralScale;
	}
	public void setIntegralScale(int integralScale) {
		this.integralScale = integralScale;
	}
	public int getIsshelves() {
		return isshelves;
	}
	public void setIsshelves(int isshelves) {
		this.isshelves = isshelves;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public BigDecimal getBackRate() {
		return backRate;
	}

	public void setBackRate(BigDecimal backRate) {
		this.backRate = backRate;
	}

	public BigDecimal getCouponStandard() {
		return couponStandard;
	}

	public void setCouponStandard(BigDecimal couponStandard) {
		this.couponStandard = couponStandard;
	}
}

