package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class RedTerritoryBonusDetail extends  RedTerritoryBonusEveryday {
    private long everydayId;
    private int type;
    private int bonusCount;
    private BigDecimal basicBonusAmount;
    private BigDecimal bonusAmount;
    private Date createTime;
    private String createBy;
    private int creditCount;
    private BigDecimal creditBasicBonus;
    private BigDecimal creditBonusAmount;
    private int loanCount;
    private BigDecimal loanBasicBonus;
    private BigDecimal loanBonusAmount;
    private int receiptCount;
    private BigDecimal receiptBasicBonus;
    private BigDecimal receiptBonusAmount;
    private int insuranceCount;
    private BigDecimal insuranceBasicBonus;
    private BigDecimal insuranceBonusAmount;
    private int bigDataCount;
    private BigDecimal bigDataBasicBonus;
    private BigDecimal bigDataBonusAmount;
    private int violationCount;
    private BigDecimal violationBasicBonus;
    private BigDecimal violationBonusAmount;
    private int repayCount;
    private BigDecimal repayBasicBonus;
    private BigDecimal repayBonusAmount;
    private int upgradeCount;
    private BigDecimal upgradeBasicBonus;
    private BigDecimal upgradeBonusAmount;
    private int redCount;
    private BigDecimal redBonus;
    private BigDecimal redAmount;


    public long getEverydayId() {
        return everydayId;
    }

    public void setEverydayId(long everydayId) {
        this.everydayId = everydayId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBonusCount() {
        return bonusCount;
    }

    public void setBonusCount(int bonusCount) {
        this.bonusCount = bonusCount;
    }

    public BigDecimal getBasicBonusAmount() {
        return basicBonusAmount;
    }

    public void setBasicBonusAmount(BigDecimal basicBonusAmount) {
        this.basicBonusAmount = basicBonusAmount;
    }

    public BigDecimal getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(BigDecimal bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public int getCreditCount() {
        return creditCount;
    }

    public void setCreditCount(int creditCount) {
        this.creditCount = creditCount;
    }

    public BigDecimal getCreditBasicBonus() {
        return creditBasicBonus;
    }

    public void setCreditBasicBonus(BigDecimal creditBasicBonus) {
        this.creditBasicBonus = creditBasicBonus;
    }

    public BigDecimal getCreditBonusAmount() {
        return creditBonusAmount;
    }

    public void setCreditBonusAmount(BigDecimal creditBonusAmount) {
        this.creditBonusAmount = creditBonusAmount;
    }

    public int getLoanCount() {
        return loanCount;
    }

    public void setLoanCount(int loanCount) {
        this.loanCount = loanCount;
    }

    public BigDecimal getLoanBasicBonus() {
        return loanBasicBonus;
    }

    public void setLoanBasicBonus(BigDecimal loanBasicBonus) {
        this.loanBasicBonus = loanBasicBonus;
    }

    public BigDecimal getLoanBonusAmount() {
        return loanBonusAmount;
    }

    public void setLoanBonusAmount(BigDecimal loanBonusAmount) {
        this.loanBonusAmount = loanBonusAmount;
    }

    public int getReceiptCount() {
        return receiptCount;
    }

    public void setReceiptCount(int receiptCount) {
        this.receiptCount = receiptCount;
    }

    public BigDecimal getReceiptBasicBonus() {
        return receiptBasicBonus;
    }

    public void setReceiptBasicBonus(BigDecimal receiptBasicBonus) {
        this.receiptBasicBonus = receiptBasicBonus;
    }

    public BigDecimal getReceiptBonusAmount() {
        return receiptBonusAmount;
    }

    public void setReceiptBonusAmount(BigDecimal receiptBonusAmount) {
        this.receiptBonusAmount = receiptBonusAmount;
    }

    public int getInsuranceCount() {
        return insuranceCount;
    }

    public void setInsuranceCount(int insuranceCount) {
        this.insuranceCount = insuranceCount;
    }

    public BigDecimal getInsuranceBasicBonus() {
        return insuranceBasicBonus;
    }

    public void setInsuranceBasicBonus(BigDecimal insuranceBasicBonus) {
        this.insuranceBasicBonus = insuranceBasicBonus;
    }

    public BigDecimal getInsuranceBonusAmount() {
        return insuranceBonusAmount;
    }

    public void setInsuranceBonusAmount(BigDecimal insuranceBonusAmount) {
        this.insuranceBonusAmount = insuranceBonusAmount;
    }

    public int getBigDataCount() {
        return bigDataCount;
    }

    public void setBigDataCount(int bigDataCount) {
        this.bigDataCount = bigDataCount;
    }

    public BigDecimal getBigDataBasicBonus() {
        return bigDataBasicBonus;
    }

    public void setBigDataBasicBonus(BigDecimal bigDataBasicBonus) {
        this.bigDataBasicBonus = bigDataBasicBonus;
    }

    public BigDecimal getBigDataBonusAmount() {
        return bigDataBonusAmount;
    }

    public void setBigDataBonusAmount(BigDecimal bigDataBonusAmount) {
        this.bigDataBonusAmount = bigDataBonusAmount;
    }

    public int getViolationCount() {
        return violationCount;
    }

    public void setViolationCount(int violationCount) {
        this.violationCount = violationCount;
    }

    public BigDecimal getViolationBasicBonus() {
        return violationBasicBonus;
    }

    public void setViolationBasicBonus(BigDecimal violationBasicBonus) {
        this.violationBasicBonus = violationBasicBonus;
    }

    public BigDecimal getViolationBonusAmount() {
        return violationBonusAmount;
    }

    public void setViolationBonusAmount(BigDecimal violationBonusAmount) {
        this.violationBonusAmount = violationBonusAmount;
    }

    public int getRepayCount() {
        return repayCount;
    }

    public void setRepayCount(int repayCount) {
        this.repayCount = repayCount;
    }

    public BigDecimal getRepayBasicBonus() {
        return repayBasicBonus;
    }

    public void setRepayBasicBonus(BigDecimal repayBasicBonus) {
        this.repayBasicBonus = repayBasicBonus;
    }

    public BigDecimal getRepayBonusAmount() {
        return repayBonusAmount;
    }

    public void setRepayBonusAmount(BigDecimal repayBonusAmount) {
        this.repayBonusAmount = repayBonusAmount;
    }

    public int getUpgradeCount() {
        return upgradeCount;
    }

    public void setUpgradeCount(int upgradeCount) {
        this.upgradeCount = upgradeCount;
    }

    public BigDecimal getUpgradeBasicBonus() {
        return upgradeBasicBonus;
    }

    public void setUpgradeBasicBonus(BigDecimal upgradeBasicBonus) {
        this.upgradeBasicBonus = upgradeBasicBonus;
    }

    public BigDecimal getUpgradeBonusAmount() {
        return upgradeBonusAmount;
    }

    public void setUpgradeBonusAmount(BigDecimal upgradeBonusAmount) {
        this.upgradeBonusAmount = upgradeBonusAmount;
    }

    public int getRedCount() {
        return redCount;
    }

    public void setRedCount(int redCount) {
        this.redCount = redCount;
    }

    public BigDecimal getRedBonus() {
        return redBonus;
    }

    public void setRedBonus(BigDecimal redBonus) {
        this.redBonus = redBonus;
    }

    public BigDecimal getRedAmount() {
        return redAmount;
    }

    public void setRedAmount(BigDecimal redAmount) {
        this.redAmount = redAmount;
    }

}
