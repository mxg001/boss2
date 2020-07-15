package cn.eeepay.framework.model;

import java.util.Date;

/**
 * @author tans
 * @date 2018/3/26
 * superbank.bank_import
 */
public class BankImport {
    private Integer id;

    private String bankCode;//银行编码

    private String bankName;//银行名称

    private String bankNickName;//银行别称

    private String fileUrl;//导入文件地址
    private String fileUrlStr;//导入文件地址

    private Date importTime;//导入时间
    private String importTimeStart;//导入时间
    private String importTimeEnd;//导入时间

    private String importBy;//导入操作人

    private String status;//状态，0无需操作，1已匹配，2待匹配

    private Date createTime;//修改时间
    private Date updateTime;//修改时间

    private String updateBy;//操作人

    private String batchNo;//批次号

    private Integer totalNum;
    private Integer successNum;
    private Integer failNum;
    private String message;
    private String bonusType;//导入的记录类型，1发卡，2首刷

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankNickName() {
        return bankNickName;
    }

    public void setBankNickName(String bankNickName) {
        this.bankNickName = bankNickName == null ? null : bankNickName.trim();
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }

    public Date getImportTime() {
        return importTime;
    }

    public void setImportTime(Date importTime) {
        this.importTime = importTime;
    }

    public String getImportBy() {
        return importBy;
    }

    public void setImportBy(String importBy) {
        this.importBy = importBy == null ? null : importBy.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public String getImportTimeStart() {
        return importTimeStart;
    }

    public void setImportTimeStart(String importTimeStart) {
        this.importTimeStart = importTimeStart;
    }

    public String getImportTimeEnd() {
        return importTimeEnd;
    }

    public void setImportTimeEnd(String importTimeEnd) {
        this.importTimeEnd = importTimeEnd;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFileUrlStr() {
        return fileUrlStr;
    }

    public void setFileUrlStr(String fileUrlStr) {
        this.fileUrlStr = fileUrlStr;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(Integer successNum) {
        this.successNum = successNum;
    }

    public Integer getFailNum() {
        return failNum;
    }

    public void setFailNum(Integer failNum) {
        this.failNum = failNum;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBonusType() {
        return bonusType;
    }

    public void setBonusType(String bonusType) {
        this.bonusType = bonusType;
    }
}