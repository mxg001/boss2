package cn.eeepay.framework.model;//

import java.math.BigDecimal;
import java.util.Date;//

/**
 * @author tans
 * @date 2018/9/1 14:59
 * 自选行业配置表
 * table:zx_industry_config
 */
public class ZxIndustryConfig {

    private String activetiyCode;//活动编号
    private Long teamId;//组织ID
    private Integer rateType;//费率类型,1:固定金额,2:费率
    private BigDecimal fixedAmount;//固定金额
    private BigDecimal rate;//费率
    private String operator;//操作人
    private Date createTime;//创建时间
    private Date updateTime;//修改时间

    private String teamName;//组织名称

    public String getActivetiyCode() {
        return activetiyCode;
    }

    public void setActivetiyCode(String activetiyCode) {
        this.activetiyCode = activetiyCode;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Integer getRateType() {
        return rateType;
    }

    public void setRateType(Integer rateType) {
        this.rateType = rateType;
    }

    public BigDecimal getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
