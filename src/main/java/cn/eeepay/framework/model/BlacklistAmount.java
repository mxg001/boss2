package cn.eeepay.framework.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 金额黑名单表 blacklist_amount
 * 
 * @author tans
 * @date 2019-08-09
 */
public class BlacklistAmount
{
	
	/**  */
	private Integer id;
	/** 跳转路由集群规则ID */
	private Integer jumpRuleId;
	/** 金额 */
	private BigDecimal amount;
	/** 创建时间 */
	private Date createTime;
	/** 操作人 */
	private String operator;
	/** 最后更新时间 */
	private Date lastUpdateTime;

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setJumpRuleId(Integer jumpRuleId) 
	{
		this.jumpRuleId = jumpRuleId;
	}

	public Integer getJumpRuleId() 
	{
		return jumpRuleId;
	}
	public void setAmount(BigDecimal amount) 
	{
		this.amount = amount;
	}

	public BigDecimal getAmount() 
	{
		return amount;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}
	public void setOperator(String operator) 
	{
		this.operator = operator;
	}

	public String getOperator() 
	{
		return operator;
	}
	public void setLastUpdateTime(Date lastUpdateTime) 
	{
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getLastUpdateTime() 
	{
		return lastUpdateTime;
	}

}
