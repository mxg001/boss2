package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * cm_vip_config
 * @author	mays
 * @date	2018年5月19日
 */
public class CmVipConfig {

	private Integer id;
	private BigDecimal vipFee;
	private Integer validPeriod;
	private Integer type;
	private BigDecimal agentShare;
	private Date createTime;
	private Date updateTime;
	private Integer vipCharge;
	private Integer agentComfig;
	private Integer cardLimit;
	private Integer cardLimitNum;


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public BigDecimal getVipFee() {
		return vipFee;
	}
	public void setVipFee(BigDecimal vipFee) {
		this.vipFee = vipFee;
	}
	public Integer getValidPeriod() {
		return validPeriod;
	}
	public void setValidPeriod(Integer validPeriod) {
		this.validPeriod = validPeriod;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public BigDecimal getAgentShare() {
		return agentShare;
	}
	public void setAgentShare(BigDecimal agentShare) {
		this.agentShare = agentShare;
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

	public Integer getVipCharge() {
		return vipCharge;
	}

	public void setVipCharge(Integer vipCharge) {
		this.vipCharge = vipCharge;
	}

	public Integer getAgentComfig() {
		return agentComfig;
	}

	public void setAgentComfig(Integer agentComfig) {
		this.agentComfig = agentComfig;
	}

	public Integer getCardLimit() {
		return cardLimit;
	}

	public void setCardLimit(Integer cardLimit) {
		this.cardLimit = cardLimit;
	}

	public Integer getCardLimitNum() {
		return cardLimitNum;
	}

	public void setCardLimitNum(Integer cardLimitNum) {
		this.cardLimitNum = cardLimitNum;
	}
}
