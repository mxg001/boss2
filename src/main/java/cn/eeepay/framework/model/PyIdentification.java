package cn.eeepay.framework.model;

import java.util.Date;

public class PyIdentification {
    private Integer id;

    private String batNo;

    private String reportId;

    private String identName;

    private String idCard;

    private Integer identiStatus;

    private String errorMsg;

    private String accountNo;

    private Integer bySystem;

    private Date createTime;

    private String createPerson;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBatNo() {
        return batNo;
    }

    public void setBatNo(String batNo) {
        this.batNo = batNo == null ? null : batNo.trim();
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId == null ? null : reportId.trim();
    }

    public String getIdentName() {
        return identName;
    }

    public void setIdentName(String identName) {
        this.identName = identName == null ? null : identName.trim();
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }

    public Integer getIdentiStatus() {
        return identiStatus;
    }

    public void setIdentiStatus(Integer identiStatus) {
        this.identiStatus = identiStatus;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg == null ? null : errorMsg.trim();
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo == null ? null : accountNo.trim();
    }

    public Integer getBySystem() {
        return bySystem;
    }

    public void setBySystem(Integer bySystem) {
        this.bySystem = bySystem;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson == null ? null : createPerson.trim();
    }
}