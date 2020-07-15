package cn.eeepay.framework.service.sysUser;

import java.util.List;

import cn.eeepay.framework.model.ShiroRole;

public interface ShiroRoleService {
	ShiroRole findShiroRoleByRoleCode(String roleCode)  throws Exception;
	ShiroRole findShiroRoleById(Integer id)  throws Exception;
	List<ShiroRole> findAllShiroRole()  throws Exception;
	int updateShiroRole(ShiroRole shiroRole);
	int insertShiroRole(ShiroRole shiroRole);
}
