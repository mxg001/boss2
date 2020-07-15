package cn.eeepay.framework.model;

import java.util.Date;

public class BonusConf {
    private Integer id;

    private Long orgId;//组织id

    private String orgName;//组织名称

    private String agencyAlias;//机构别称

    private String totalBonus;//总奖金

    private String companyBonus;//公司截留奖金

    private String orgBonus;//品牌截留奖金

    private String profitType;//1-固定奖金，2-按比例发放

    private String rewardClaim;//奖励要求

    private String shareRate;//积分兑换比例

    private String type;//类型

    private String createBy;//创建人

    private String updateBy;//修改人

    private Date createDate;//创建时间

    private Date updateDate;//修改时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getAgencyAlias() {
        return agencyAlias;
    }

    public void setAgencyAlias(String agencyAlias) {
        this.agencyAlias = agencyAlias;
    }

    public String getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(String totalBonus) {
        this.totalBonus = totalBonus;
    }

    public String getCompanyBonus() {
        return companyBonus;
    }

    public void setCompanyBonus(String companyBonus) {
        this.companyBonus = companyBonus;
    }

    public String getOrgBonus() {
        return orgBonus;
    }

    public void setOrgBonus(String orgBonus) {
        this.orgBonus = orgBonus;
    }

    public String getProfitType() {
        return profitType;
    }

    public void setProfitType(String profitType) {
        this.profitType = profitType;
    }

    public String getRewardClaim() {
        return rewardClaim;
    }

    public void setRewardClaim(String rewardClaim) {
        this.rewardClaim = rewardClaim;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public String getShareRate() {
        return shareRate;
    }

    public void setShareRate(String shareRate) {
        this.shareRate = shareRate;
    }
}