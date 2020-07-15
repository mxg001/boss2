package cn.eeepay.framework.model.cjt;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 超级推收益明细表 cjt_profit_detail
 * 
 * @author tans
 * @date 2019-06-14
 */
public class CjtProfitDetail
{

	public static Map<String, String> levelMap = new HashMap<>();
	public static Map<String, String> profitTypeMap = new HashMap<>();
	public static Map<String, String> transTypeMap = new HashMap<>();
	public static Map<String, String> rechargeStatusMap = new HashMap<>();
	static {
		levelMap.put("zero", "本级商户");
		levelMap.put("one", "一级商户");
		levelMap.put("two", "二级商户");
		//交易类型：无卡交易，刷卡交易
		//收益类型：交易分润，推荐奖励，活动补贴
		//'奖励类型 recommend推荐奖励 activity活动补贴 posTrade刷卡交易 noCardTrade无卡交易',
		profitTypeMap.put("recommend", "推荐奖励");
		profitTypeMap.put("activity", "活动补贴");
		profitTypeMap.put("posTrade", "交易分润");
		profitTypeMap.put("noCardTrade", "交易分润");

        transTypeMap.put("posTrade", "刷卡交易");
        transTypeMap.put("noCardTrade", "无卡交易");
        rechargeStatusMap.put("0", "未入账");
		rechargeStatusMap.put("1", "已入账");
		rechargeStatusMap.put("2", "入账失败");

	}

	/**  */
	private Long id;
	/** 订单号 */
	private String orderNo;
	/** 收益商户号 */
	private String merchantNo;
	/** 收益来源商户编号 */
	private String fromMerchantNo;
	/** 收益来源级别（属于几级收益，即mer_no为from_mer_no的上几级），zero本级收益 one上一级收益 two上二级收益 */
	private String fromLevel;
	/** 奖励类型 recommend推荐奖励 activity活动补贴 posTrade刷卡交易 noCardTrade无卡交易 */
	private String profitType;
	/** 收益来源订单号 */
	private String profitFromOrderNo;
	/** 收益来源业务金额 */
	private BigDecimal profitFromAmount;
	/** 收益比例(如0.00015，固定金额时为空) */
	private BigDecimal profitRate;
	/** 收益金额 */
	private BigDecimal profitAmount;
	/** 入账状态 0-未入账 1-已入账 2-入账失败 */
	private String rechargeStatus;
	/** 创建时间 */
	private Date createTime;
	/** 入账时间 */
	private Date rechargeTime;
	/** 数据最后更新时间 */
	private Date lastUpdateTime;

	private String createTimeStr;
	private String createTimeStart;
	private String createTimeEnd;
	private String rechargeTimeStr;
	private String rechargeTimeStart;
	private String rechargeTimeEnd;
	private String merchantName;//商户名称
	private String fromLevelStr;//收益级别
	private String transTypeStr;//交易类型
	private String profitTypeStr;//收益类型
	private String rechargeStatusStr;//记账状态
	private String transType;//交易类型
	private String profitRateStr;
	private String transTypeCode;

	private String userType;//奖励用户类型,"A":代理商,"M":商户


	public void setId(Long id) 
	{
		this.id = id;
	}

	public Long getId() 
	{
		return id;
	}
	public void setOrderNo(String orderNo) 
	{
		this.orderNo = orderNo;
	}

	public String getOrderNo() 
	{
		return orderNo;
	}
	public void setMerchantNo(String merchantNo) 
	{
		this.merchantNo = merchantNo;
	}

	public String getMerchantNo() 
	{
		return merchantNo;
	}
	public void setFromMerchantNo(String fromMerchantNo) 
	{
		this.fromMerchantNo = fromMerchantNo;
	}

	public String getFromMerchantNo() 
	{
		return fromMerchantNo;
	}
	public void setFromLevel(String fromLevel) 
	{
		this.fromLevel = fromLevel;
	}

	public String getFromLevel() 
	{
		return fromLevel;
	}
	public void setProfitType(String profitType) 
	{
		this.profitType = profitType;
	}

	public String getProfitType() 
	{
		return profitType;
	}
	public void setProfitFromOrderNo(String profitFromOrderNo) 
	{
		this.profitFromOrderNo = profitFromOrderNo;
	}

	public String getProfitFromOrderNo() 
	{
		return profitFromOrderNo;
	}
	public void setProfitFromAmount(BigDecimal profitFromAmount) 
	{
		this.profitFromAmount = profitFromAmount;
	}

	public BigDecimal getProfitFromAmount() 
	{
		return profitFromAmount;
	}
	public void setProfitRate(BigDecimal profitRate) 
	{
		this.profitRate = profitRate;
	}

	public BigDecimal getProfitRate() 
	{
		return profitRate;
	}
	public void setProfitAmount(BigDecimal profitAmount) 
	{
		this.profitAmount = profitAmount;
	}

	public BigDecimal getProfitAmount() 
	{
		return profitAmount;
	}
	public void setRechargeStatus(String rechargeStatus) 
	{
		this.rechargeStatus = rechargeStatus;
	}

	public String getRechargeStatus() 
	{
		return rechargeStatus;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}
	public void setRechargeTime(Date rechargeTime) 
	{
		this.rechargeTime = rechargeTime;
	}

	public Date getRechargeTime() 
	{
		return rechargeTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) 
	{
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getLastUpdateTime() 
	{
		return lastUpdateTime;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
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

	public String getRechargeTimeStr() {
		return rechargeTimeStr;
	}

	public void setRechargeTimeStr(String rechargeTimeStr) {
		this.rechargeTimeStr = rechargeTimeStr;
	}

	public String getRechargeTimeStart() {
		return rechargeTimeStart;
	}

	public void setRechargeTimeStart(String rechargeTimeStart) {
		this.rechargeTimeStart = rechargeTimeStart;
	}

	public String getRechargeTimeEnd() {
		return rechargeTimeEnd;
	}

	public void setRechargeTimeEnd(String rechargeTimeEnd) {
		this.rechargeTimeEnd = rechargeTimeEnd;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getFromLevelStr() {
		return fromLevelStr;
	}

	public void setFromLevelStr(String fromLevelStr) {
		this.fromLevelStr = fromLevelStr;
	}

	public String getTransTypeStr() {
		return transTypeStr;
	}

	public void setTransTypeStr(String transTypeStr) {
		this.transTypeStr = transTypeStr;
	}

	public String getProfitTypeStr() {
		return profitTypeStr;
	}

	public void setProfitTypeStr(String profitTypeStr) {
		this.profitTypeStr = profitTypeStr;
	}

	public String getRechargeStatusStr() {
		return rechargeStatusStr;
	}

	public void setRechargeStatusStr(String rechargeStatusStr) {
		this.rechargeStatusStr = rechargeStatusStr;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getProfitRateStr() {
		return profitRateStr;
	}

	public void setProfitRateStr(String profitRateStr) {
		this.profitRateStr = profitRateStr;
	}

	public String getTransTypeCode() {
		return transTypeCode;
	}

	public void setTransTypeCode(String transTypeCode) {
		this.transTypeCode = transTypeCode;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("merchantNo", getMerchantNo())
            .append("fromMerchantNo", getFromMerchantNo())
            .append("fromLevel", getFromLevel())
            .append("profitType", getProfitType())
            .append("profitFromOrderNo", getProfitFromOrderNo())
            .append("profitFromAmount", getProfitFromAmount())
            .append("profitRate", getProfitRate())
            .append("profitAmount", getProfitAmount())
            .append("rechargeStatus", getRechargeStatus())
            .append("createTime", getCreateTime())
            .append("rechargeTime", getRechargeTime())
            .append("lastUpdateTime", getLastUpdateTime())
			.append("userType", getUserType())
            .toString();
    }
}
