package cn.eeepay.framework.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public class SysDept {
    private Integer id;
    private String deptName;
    private List<ShiroUser> users = new ArrayList<>();
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public List<ShiroUser> getUsers() {
		return users;
	}
	public void setUsers(List<ShiroUser> users) {
		this.users = users;
	}

    
    
}