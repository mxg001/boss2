package cn.eeepay.framework.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

import org.springframework.util.StringUtils;

/**
 * Created by Administrator on 2018/1/8/008.
 * @author liuks
 * 预警人实体
 * 对应表 warning_people
 */
public class WarningPeople {
    private Integer id;//预警人ID

    private Integer userId;//用户id

    private String userName;//用户名

    private String name;//姓名

    private String phone;//手机号

    private Integer status;//状态：1-收单机构预警人员，2-出款预警人员

    private String sids;//分配服务id
    
	public String getSids() {
		if (sids!=null && sids.trim().equals("")) 
		return null;
		return sids;
	}

	public void setSids(String sids) {
		this.sids = sids;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;//创建时间

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date modify_time;//最后修改时间

    private Integer total;//多少人

    private String assignmentTask;//分配的定时任务预警

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getModify_time() {
        return modify_time;
    }

    public void setModify_time(Date modify_time) {
        this.modify_time = modify_time;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getAssignmentTask() {
        return assignmentTask;
    }

    public void setAssignmentTask(String assignmentTask) {
        this.assignmentTask = assignmentTask;
    }
}
