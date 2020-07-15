package cn.eeepay.framework.model;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public class RoleRight {
    private Integer id;
    private Integer roleId;
    private Integer rightId;
    private ShiroRigth shiroRigth;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getRightId() {
		return rightId;
	}

	public void setRightId(Integer rightId) {
		this.rightId = rightId;
	}

	public ShiroRigth getShiroRigth() {
		return shiroRigth;
	}

	public void setShiroRigth(ShiroRigth shiroRigth) {
		this.shiroRigth = shiroRigth;
	}

    
    
}