package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 鼓励金活动管理
 * @author IVAN
 *
 */
public class CouponActivityInfo {
	 private int id;//主键
	 private String activetiyCode;
	 private String activetiyType;//活动类别(详见字典表 COUPON_TYPE)
	 private String  activityName;//活动名称
	 private String   activityExplain;//活动说明
	 private String  activityFirst;//活动优先级：A-E优先级递增
	 private int  effectiveDays;//有效天数
	 private String activityNotice;//活动下发通知信息
	 private Date createTime;//创建时间
	 private Date updateTime;//更新时间
	 private String  operator;//操作人
	 private String  status;//状态:0 有效、1关闭

	private Integer statusInt;//状态:0 有效、1关闭

	private String upOpenExplan;
	private String openExplan;
	private String buyPush;
	private String maturityGivePush;
	 
	 /**
	  * 
	  * 券信息
	  */
	private int couponId;
	private String  couponCode;//优惠卷类型(详见字典表 COUPON_TYPE)
	private String  couponName;//优惠券名称
	private BigDecimal  couponAmount;//优惠券面值
	private int  couponCount;//可参与次数、可获取数量、优惠券日发行数（-1：不限）
	private String  cancelVerificationCode;//用途、核销方式(详见字典表CANCEL_VERIFICATION_TYPE)
	
	private BigDecimal transRate;

	public BigDecimal getTransRate() {
		return transRate;
	}
	public void setTransRate(BigDecimal transRate) {
		this.transRate = transRate;
	}
	public int getCouponId() {
		return couponId;
	}
	public void setCouponId(int couponId) {
		this.couponId = couponId;
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
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getActivityExplain() {
		return activityExplain;
	}
	public void setActivityExplain(String activityExplain) {
		this.activityExplain = activityExplain;
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

	public String getActivityNotice() {
		return activityNotice;
	}
	public void setActivityNotice(String activityNotice) {
		this.activityNotice = activityNotice;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getActivetiyType() {
		return activetiyType;
	}
	public void setActivetiyType(String activetiyType) {
		this.activetiyType = activetiyType;
	}

	public Integer getStatusInt() {
		return statusInt;
	}

	public void setStatusInt(Integer statusInt) {
		this.statusInt = statusInt;
	}

	public String getUpOpenExplan() {
		return upOpenExplan;
	}

	public void setUpOpenExplan(String upOpenExplan) {
		this.upOpenExplan = upOpenExplan;
	}

	public String getOpenExplan() {
		return openExplan;
	}

	public void setOpenExplan(String openExplan) {
		this.openExplan = openExplan;
	}

	public String getBuyPush() {
		return buyPush;
	}

	public void setBuyPush(String buyPush) {
		this.buyPush = buyPush;
	}

	public String getMaturityGivePush() {
		return maturityGivePush;
	}

	public void setMaturityGivePush(String maturityGivePush) {
		this.maturityGivePush = maturityGivePush;
	}
}
