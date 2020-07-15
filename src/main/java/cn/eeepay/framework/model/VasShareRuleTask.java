package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 增值服务分润历史
 *
 * @author Administrator
 */
public class VasShareRuleTask {

    private int id;

    private int vasRuleId;//增值服务分润表vas_share_rule.id
    private String vasServiceNo;//增值服务编号
    private String vasServiceName;//增值服务名称

    private String teamId;//组织ID
    private String teamName;//组织名称

    private String teamEntryId;//子级组织ID
    private String teamEntryName;//子级组织名称

    private String rateType;//费率类型:1-每笔固定金额，2-扣率
    private BigDecimal singleNumAmount;//每笔固定值
    private BigDecimal rate;//扣率

    private BigDecimal perFixCost;//每笔固定值
    private BigDecimal costRate;//扣率
    private BigDecimal shareProfitPercent;//代理商固定分润百分比

    private BigDecimal agentPerFixCost;//直属代理商成本每笔固定值
    private BigDecimal agentCostRate;//直属代理商成本扣率
    private BigDecimal defaultPerFixCost;//默认下级分润,直属代理商成本每笔固定值
    private BigDecimal defaultCostRate;//默认下级分润,直属代理商成本扣率
    private BigDecimal defaultShareProfitPercent;//默认下级代理商固定分润百分比

    private Integer effectiveStatus;//是否生效:0-未生效，1-生效，2-已失效


    private Date effectiveDate;//生效日期
    private Date createTime;

    private Date lastUpdateTime;

    private String operator;
    private String remark;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVasRuleId() {
        return vasRuleId;
    }

    public void setVasRuleId(int vasRuleId) {
        this.vasRuleId = vasRuleId;
    }

    public String getVasServiceNo() {
        return vasServiceNo;
    }

    public void setVasServiceNo(String vasServiceNo) {
        this.vasServiceNo = vasServiceNo;
    }

    public String getVasServiceName() {
        return vasServiceName;
    }

    public void setVasServiceName(String vasServiceName) {
        this.vasServiceName = vasServiceName;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamEntryId() {
        return teamEntryId;
    }

    public void setTeamEntryId(String teamEntryId) {
        this.teamEntryId = teamEntryId;
    }

    public String getTeamEntryName() {
        return teamEntryName;
    }

    public void setTeamEntryName(String teamEntryName) {
        this.teamEntryName = teamEntryName;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public BigDecimal getSingleNumAmount() {
        return singleNumAmount;
    }

    public void setSingleNumAmount(BigDecimal singleNumAmount) {
        this.singleNumAmount = singleNumAmount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getPerFixCost() {
        return perFixCost;
    }

    public void setPerFixCost(BigDecimal perFixCost) {
        this.perFixCost = perFixCost;
    }

    public BigDecimal getCostRate() {
        return costRate;
    }

    public void setCostRate(BigDecimal costRate) {
        this.costRate = costRate;
    }

    public BigDecimal getShareProfitPercent() {
        return shareProfitPercent;
    }

    public void setShareProfitPercent(BigDecimal shareProfitPercent) {
        this.shareProfitPercent = shareProfitPercent;
    }

    public BigDecimal getAgentPerFixCost() {
        return agentPerFixCost;
    }

    public void setAgentPerFixCost(BigDecimal agentPerFixCost) {
        this.agentPerFixCost = agentPerFixCost;
    }

    public BigDecimal getAgentCostRate() {
        return agentCostRate;
    }

    public void setAgentCostRate(BigDecimal agentCostRate) {
        this.agentCostRate = agentCostRate;
    }

    public BigDecimal getDefaultPerFixCost() {
        return defaultPerFixCost;
    }

    public void setDefaultPerFixCost(BigDecimal defaultPerFixCost) {
        this.defaultPerFixCost = defaultPerFixCost;
    }

    public BigDecimal getDefaultCostRate() {
        return defaultCostRate;
    }

    public void setDefaultCostRate(BigDecimal defaultCostRate) {
        this.defaultCostRate = defaultCostRate;
    }

    public BigDecimal getDefaultShareProfitPercent() {
        return defaultShareProfitPercent;
    }

    public void setDefaultShareProfitPercent(BigDecimal defaultShareProfitPercent) {
        this.defaultShareProfitPercent = defaultShareProfitPercent;
    }

    public Integer getEffectiveStatus() {
        return effectiveStatus;
    }

    public void setEffectiveStatus(Integer effectiveStatus) {
        this.effectiveStatus = effectiveStatus;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "VasShareRuleTask{" +
                "id=" + id +
                ", vasRuleId='" + vasRuleId + '\'' +
                ", vasServiceNo='" + vasServiceNo + '\'' +
                ", vasServiceName='" + vasServiceName + '\'' +
                ", teamId='" + teamId + '\'' +
                ", teamName='" + teamName + '\'' +
                ", teamEntryId='" + teamEntryId + '\'' +
                ", teamEntryName='" + teamEntryName + '\'' +
                ", rateType='" + rateType + '\'' +
                ", singleNumAmount=" + singleNumAmount +
                ", rate=" + rate +
                ", perFixCost=" + perFixCost +
                ", costRate=" + costRate +
                ", shareProfitPercent=" + shareProfitPercent +
                ", agentPerFixCost=" + agentPerFixCost +
                ", agentCostRate=" + agentCostRate +
                ", defaultPerFixCost=" + defaultPerFixCost +
                ", defaultCostRate=" + defaultCostRate +
                ", defaultShareProfitPercent=" + defaultShareProfitPercent +
                ", effectiveStatus=" + effectiveStatus +
                ", effectiveDate=" + effectiveDate +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", operator='" + operator + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
