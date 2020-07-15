package cn.eeepay.framework.model.cjt;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Date;

/**
 * 超级推分润奖励规则表 cjt_profit_rule
 * 
 * @author tans
 * @date 2019-05-24
 */
public class CjtProfitRule
{

	public static final String recommend = "recommend";
	public static final String activity = "activity";
	public static final String register = "register";
	public static final String posTrade = "posTrade";
	public static final String noCardTrade = "noCardTrade";
	public static final String trade = "trade";
	public static final String fenAmountProfitAgent = "fenAmountProfitAgent";
	public static final String profitMode1 = "1";//固定金额
	public static final String profitMode2 = "2";//固定比例，例如0.04%
	public static final String profitMode3 = "3";//参与活动
	public static final String cjt_tx_service_id = "CJT_TX_SERVICE_ID";

	private Integer id;
	/** 规则编号 */
	private String profitRuleNo;
	/** 奖励类型 recommend推荐奖励 activity活动补贴 posTrade刷卡交易 noCardTrade无卡交易 register注册奖励 */
	private String profitType;
	/** 奖励条件 无限制条件则为空 */
	private String profitCondition;
	/** 条件内对应的参数 */
	private String profitOrgs;
	/** 奖励对象 ownself商户本身 oneMer上一级商户 twoMer上二级商户 */
	private String profitTarget;
	/** 奖励方式 1固定金额 2固定比例 3参与活动 */
	private String profitMode;
	/** 本级商户奖励 */
	private String profit0;
	/** 上一级商户奖励 */
	private String profit1;
	/** 上二级商户奖励 */
	private String profit2;
	/** 奖励规则状态 0关闭 1正常 */
	private Integer status;
	/** 备注 */
	private String remark;
	/** 创建时间 */
	private Date createTime;
	/** 数据最后更新时间 */
	private Date lastUpdateTime;

	private String successDay;//审核成功天数内
	private String posAmount;//刷卡交易累计满多少元

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setProfitRuleNo(String profitRuleNo) 
	{
		this.profitRuleNo = profitRuleNo;
	}

	public String getProfitRuleNo() 
	{
		return profitRuleNo;
	}
	public void setProfitType(String profitType) 
	{
		this.profitType = profitType;
	}

	public String getProfitType() 
	{
		return profitType;
	}
	public void setProfitCondition(String profitCondition) 
	{
		this.profitCondition = profitCondition;
	}

	public String getProfitCondition() 
	{
		return profitCondition;
	}
	public void setProfitOrgs(String profitOrgs) 
	{
		this.profitOrgs = profitOrgs;
	}

	public String getProfitOrgs() 
	{
		return profitOrgs;
	}
	public void setProfitTarget(String profitTarget) 
	{
		this.profitTarget = profitTarget;
	}

	public String getProfitTarget() 
	{
		return profitTarget;
	}
	public void setProfitMode(String profitMode) 
	{
		this.profitMode = profitMode;
	}

	public String getProfitMode() 
	{
		return profitMode;
	}
	public String getProfit0() {
		return profit0;
	}

	public void setProfit0(String profit0) {
		this.profit0 = profit0;
	}

	public String getProfit1() {
		return profit1;
	}

	public void setProfit1(String profit1) {
		this.profit1 = profit1;
	}

	public String getProfit2() {
		return profit2;
	}

	public void setProfit2(String profit2) {
		this.profit2 = profit2;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public Integer getStatus()
	{
		return status;
	}
	public void setRemark(String remark) 
	{
		this.remark = remark;
	}

	public String getRemark() 
	{
		return remark;
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

	public String getSuccessDay() {
		return successDay;
	}

	public void setSuccessDay(String successDay) {
		this.successDay = successDay;
	}

	public String getPosAmount() {
		return posAmount;
	}

	public void setPosAmount(String posAmount) {
		this.posAmount = posAmount;
	}
}
