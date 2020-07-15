package cn.eeepay.framework.model.allAgent;

import java.util.Date;

public class PersistNickName {

    private Integer id;
    private String sensitiveNo;
    private String status;
    private Date createTime;
    private Date modifyTime;
    private String keyWord;
    private String keyType;

    private String createTimeBegin;
    private String createTimeEnd;

    private String errorResult;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSensitiveNo() {
        return sensitiveNo;
    }

    public void setSensitiveNo(String sensitiveNo) {
        this.sensitiveNo = sensitiveNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(String createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getErrorResult() {
        return errorResult;
    }

    public void setErrorResult(String errorResult) {
        this.errorResult = errorResult;
    }
}
