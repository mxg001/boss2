package cn.eeepay.framework.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShiroRole {
    private Integer id;

    private String roleCode;

    private String roleName;

    private String roleRemake;

    private Integer roleState;

    private String createOperator;

    private Date createTime;

    private Date updateTime;
    
    private List<RoleRight> roleRigths = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode == null ? null : roleCode.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public String getRoleRemake() {
        return roleRemake;
    }

    public void setRoleRemake(String roleRemake) {
        this.roleRemake = roleRemake == null ? null : roleRemake.trim();
    }

    public Integer getRoleState() {
        return roleState;
    }

    public void setRoleState(Integer roleState) {
        this.roleState = roleState;
    }

    public String getCreateOperator() {
        return createOperator;
    }

    public void setCreateOperator(String createOperator) {
        this.createOperator = createOperator == null ? null : createOperator.trim();
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

	public List<RoleRight> getRoleRigths() {
		return roleRigths;
	}

	public void setRoleRigths(List<RoleRight> roleRigths) {
		this.roleRigths = roleRigths;
	}
    
    
}