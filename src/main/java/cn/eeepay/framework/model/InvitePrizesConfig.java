package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 邀请有奖，代理商配置
 * @author tans
 * @date 2017年8月19日 上午11:21:28
 */
public class InvitePrizesConfig {
	
    private Integer id;

    private String agentNo;//代理商编号

    private Date startDate;//参加活动的起始日期

    private Date endDate;//参加活动的结束日期

    private String activityAction;//活动动作编号，4 邀请有奖, 来源coupon_activity_info.activetiy_code

    private Date createTime;//创建时间

    private Date updateTime;//更新时间

    private String operator;//操作人
    
    private Integer activityStatus;//活动状态,0:未开始，1：进行中，2：已结束
    
    private Date currentDate;//当前日期
    
    private String agentName;//代理商名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo == null ? null : agentNo.trim();
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getActivityAction() {
        return activityAction;
    }

    public void setActivityAction(String activityAction) {
        this.activityAction = activityAction == null ? null : activityAction.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

	public Integer getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(Integer activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
    
}
