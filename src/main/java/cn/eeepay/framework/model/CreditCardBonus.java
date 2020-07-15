package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 信用卡奖金配置表
 * @author Administrator
 *
 */
public class CreditCardBonus {

	private Long id;
	
	private Long orgId;//组织ID
	
	private Long sourceId;//信用卡银行ID
	
	private String orgCost;//品牌组织成本
	
	private String orgPushCost;//品牌组织发放奖金
	
	private String updateBy;
	
	private Date updateDate;
	
	private String orgName;//组织名称
	
	private String bankName;//银行名称
	private String bankNickName;//银行别称

	private String bankBonus;//总奖金  (配置在银行表)
	
	private String isOnlyone;//是否首次办卡奖励

	private BigDecimal cardCompanyBonus;//发卡公司截留奖金
	private BigDecimal cardOemBonus;//发卡品牌截留奖金
	private BigDecimal firstCompanyBonus;//首刷公司截留奖金
	private BigDecimal firstOemBonus;//首刷品牌截留奖金

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getOrgCost() {
		return orgCost;
	}

	public void setOrgCost(String orgCost) {
		this.orgCost = orgCost;
	}

	public String getOrgPushCost() {
		return orgPushCost;
	}

	public void setOrgPushCost(String orgPushCost) {
		this.orgPushCost = orgPushCost;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankBonus() {
		return bankBonus;
	}

	public void setBankBonus(String bankBonus) {
		this.bankBonus = bankBonus;
	}

	public String getIsOnlyone() {
		return isOnlyone;
	}

	public void setIsOnlyone(String isOnlyone) {
		this.isOnlyone = isOnlyone;
	}

	public BigDecimal getCardCompanyBonus() {
		return cardCompanyBonus;
	}

	public void setCardCompanyBonus(BigDecimal cardCompanyBonus) {
		this.cardCompanyBonus = cardCompanyBonus;
	}

	public BigDecimal getCardOemBonus() {
		return cardOemBonus;
	}

	public void setCardOemBonus(BigDecimal cardOemBonus) {
		this.cardOemBonus = cardOemBonus;
	}

	public BigDecimal getFirstCompanyBonus() {
		return firstCompanyBonus;
	}

	public void setFirstCompanyBonus(BigDecimal firstCompanyBonus) {
		this.firstCompanyBonus = firstCompanyBonus;
	}

	public BigDecimal getFirstOemBonus() {
		return firstOemBonus;
	}

	public void setFirstOemBonus(BigDecimal firstOemBonus) {
		this.firstOemBonus = firstOemBonus;
	}

	public String getBankNickName() {
		return bankNickName;
	}

	public void setBankNickName(String bankNickName) {
		this.bankNickName = bankNickName;
	}
	
	private BigDecimal cardBonus;//发卡奖金

	private BigDecimal firstBrushBonus;//首刷奖金

	private String isOpen;// 是否外放组织 0-否 1-是
	
	public BigDecimal getCardBonus() {
		return cardBonus;
	}

	public void setCardBonus(BigDecimal cardBonus) {
		this.cardBonus = cardBonus;
	}

	public BigDecimal getFirstBrushBonus() {
		return firstBrushBonus;
	}

	public void setFirstBrushBonus(BigDecimal firstBrushBonus) {
		this.firstBrushBonus = firstBrushBonus;
	}

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

	
}
