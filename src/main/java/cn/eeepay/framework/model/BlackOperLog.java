package cn.eeepay.framework.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
/**
 * table black_oper_log
 * desc 黑名单操作记录日志表
 */
public class BlackOperLog {
    private Long id;
    private String rollNo;
    private String blackType;
    private Date createTime;
    private String operationType;
    private String createBy;
    private String remark;

    public Long getId() {
        return id;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getBlackType() {
        return blackType;
    }

    public void setBlackType(String blackType) {
        this.blackType = blackType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setId(Long id) {
        this.id = id;
    }
}