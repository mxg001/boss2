package cn.eeepay.framework.model;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public class UserRight {
    private Integer id;
    private Integer userId;
    private Integer rightId;
    private ShiroRigth shiroRigth;

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