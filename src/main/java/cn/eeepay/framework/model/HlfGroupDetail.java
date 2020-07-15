package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 欢乐返分组详情
 *
 * @author Administrator
 */
public class HlfGroupDetail {
    private Integer id;
    private Integer groupId;
    private String activityTypeNo;
    private String activityTypeName;
    private String orgName;
    private String teamEntryName;
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getActivityTypeNo() {
        return activityTypeNo;
    }

    public void setActivityTypeNo(String activityTypeNo) {
        this.activityTypeNo = activityTypeNo;
    }

    public String getActivityTypeName() {
        return activityTypeName;
    }

    public void setActivityTypeName(String activityTypeName) {
        this.activityTypeName = activityTypeName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getTeamEntryName() {
        return teamEntryName;
    }

    public void setTeamEntryName(String teamEntryName) {
        this.teamEntryName = teamEntryName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
