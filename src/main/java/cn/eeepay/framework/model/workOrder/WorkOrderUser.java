package cn.eeepay.framework.model.workOrder;

import org.mybatis.spring.annotation.MapperScan;

import java.util.Date;

/**
 * @author ：quanhz
 * @date ：Created in 2020/4/23 10:57
 */
public class WorkOrderUser {
    private Long id;
    private String bossUserName;
    private String bossRealName;
    private Integer userId;
    private Long deptNo;
    private String deptName;
    private String roleType;
    private Integer status;
    private Integer dutyType;
    private String dutyData;
    private String dutySale;
    private String dutyWorkType;
    private Date createTime;
    private String operator;
    private String selectedIds;
    private String dutyDataParam;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBossUserName() {
        return bossUserName;
    }

    public void setBossUserName(String bossUserName) {
        this.bossUserName = bossUserName;
    }

    public Long getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(Long deptNo) {
        this.deptNo = deptNo;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDutyType() {
        return dutyType;
    }

    public void setDutyType(Integer dutyType) {
        this.dutyType = dutyType;
    }

    public String getDutyData() {
        return dutyData;
    }

    public void setDutyData(String dutyData) {
        this.dutyData = dutyData;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getBossRealName() {
        return bossRealName;
    }

    public void setBossRealName(String bossRealName) {
        this.bossRealName = bossRealName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(String selectedIds) {
        this.selectedIds = selectedIds;
    }

    public String getDutySale() {
        return dutySale;
    }

    public void setDutySale(String dutySale) {
        this.dutySale = dutySale;
    }

    public String getDutyWorkType() {
        return dutyWorkType;
    }

    public void setDutyWorkType(String dutyWorkType) {
        this.dutyWorkType = dutyWorkType;
    }

    public String getDutyDataParam() {
        return dutyDataParam;
    }

    public void setDutyDataParam(String dutyDataParam) {
        this.dutyDataParam = dutyDataParam;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
