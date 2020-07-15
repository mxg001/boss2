package cn.eeepay.framework.model;


import java.util.Date;

/**
 * superbank.creditcard_apply_record
 */
public class CreditcardApplyRecord {
    private Long id;

    private String bankCode;//银行编码，关联银行ID

    private Date applyDate;//申请日期

    private Date issueDate;//发卡日期

    private Date firstBrushDate;//首刷日期

    private String userName;//姓名

    private String idCardNo;//身份证号

    private String newAccountStatus;//是否是新户：0.否，1.是

    private String cardNo;//卡号

    private Date createTime;

    private Integer operator;

    private String checkStatus;//审核状态

    private String mobilephone;//手机号

    private String batchNo;//批次号

    private String bankNickName;//银行别称

    private String message;//匹配提示信息

    private String code;//银行编码

    private String ruleCode;//银行导入匹配规则编码

    private String bankName;//银行名称

    private String userCode;//用户ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getFirstBrushDate() {
        return firstBrushDate;
    }

    public void setFirstBrushDate(Date firstBrushDate) {
        this.firstBrushDate = firstBrushDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo == null ? null : idCardNo.trim();
    }

    public String getNewAccountStatus() {
        return newAccountStatus;
    }

    public void setNewAccountStatus(String newAccountStatus) {
        this.newAccountStatus = newAccountStatus == null ? null : newAccountStatus.trim();
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBankNickName() {
        return bankNickName;
    }

    public void setBankNickName(String bankNickName) {
        this.bankNickName = bankNickName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}