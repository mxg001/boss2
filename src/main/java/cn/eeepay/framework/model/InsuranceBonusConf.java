package cn.eeepay.framework.model;

import java.util.Date;

public class InsuranceBonusConf {
    private Integer id;

    private Long orgId;//组织id

    private String orgName;//组织名称

    private Integer companyNo;//保险公司ID

    private String companyNickName;//保险公司别称

    private Integer productId;//保险产品ID

    private String productName;//保险产品名称

    private String totalBonus;//保单总奖金

    private String companyBonus;//公司截留奖金

    private String orgBonus;//品牌截留奖金

    private String profitType;//1-固定奖金，2-按比例发放

    private int status;

    private String statusStr;

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

    public Integer getCompanyNo() {
        return companyNo;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setCompanyNo(Integer companyNo) {
        this.companyNo = companyNo;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(String totalBonus) {
        this.totalBonus = totalBonus == null ? null : totalBonus.trim();
    }

    public String getCompanyBonus() {
        return companyBonus;
    }

    public void setCompanyBonus(String companyBonus) {
        this.companyBonus = companyBonus == null ? null : companyBonus.trim();
    }

    public String getOrgBonus() {
        return orgBonus;
    }

    public void setOrgBonus(String orgBonus) {
        this.orgBonus = orgBonus == null ? null : orgBonus.trim();
    }

    public String getProfitType() {
        return profitType;
    }

    public String getCompanyNickName() {
        return companyNickName;
    }

    public void setCompanyNickName(String companyNickName) {
        this.companyNickName = companyNickName;
    }

    public String getProductName() {
        return productName;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProfitType(String profitType) {
        this.profitType = profitType == null ? null : profitType.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}