package cn.eeepay.framework.model;

import java.util.Date;

public class InsuranceCompany {
    private Integer companyNo;//保险公司ID

    private String companyName;//保险公司名称

    private String companyNickName;//保险公司别称

    private String source;//来源

    private String showLogo;//显示logo

    private int showOrder;//显示顺序

    private String showLogoUrl;

    private Integer createOrderType;//订单创建方式,1:实际回调创建,2:批量导入创建

    private String createOrderTypeStr;
    private String ruleCode;//导入匹配规则编码

    private String shareRuleRemark;//分润规则备注

    private Integer status;//状态,0:关闭,1:开启

    private String statusStr;//状态,0:关闭,1:开启

    private String createBy;//创建人

    private String updateBy;//修改人

    private Date createDate;//创建时间

    private Date updateDate;//修改时间

    public Integer getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(Integer companyNo) {
        this.companyNo = companyNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getCompanyNickName() {
        return companyNickName;
    }

    public void setCompanyNickName(String companyNickName) {
        this.companyNickName = companyNickName == null ? null : companyNickName.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getShowLogo() {
        return showLogo;
    }

    public void setShowLogo(String showLogo) {
        this.showLogo = showLogo == null ? null : showLogo.trim();
    }

    public Integer getCreateOrderType() {
        return createOrderType;
    }

    public void setCreateOrderType(Integer createOrderType) {
        this.createOrderType = createOrderType;
    }

    public int getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(int showOrder) {
        this.showOrder = showOrder;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode == null ? null : ruleCode.trim();
    }

    public String getShareRuleRemark() {
        return shareRuleRemark;
    }

    public void setShareRuleRemark(String shareRuleRemark) {
        this.shareRuleRemark = shareRuleRemark == null ? null : shareRuleRemark.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getShowLogoUrl() {
        return showLogoUrl;
    }

    public void setShowLogoUrl(String showLogoUrl) {
        this.showLogoUrl = showLogoUrl;
    }

    public String getCreateOrderTypeStr() {
        return createOrderTypeStr;
    }

    public void setCreateOrderTypeStr(String createOrderTypeStr) {
        this.createOrderTypeStr = createOrderTypeStr;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }
}