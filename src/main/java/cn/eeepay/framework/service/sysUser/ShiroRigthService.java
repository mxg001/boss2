package cn.eeepay.framework.service.sysUser;

import java.util.List;

import cn.eeepay.framework.model.ShiroRigth;

public interface ShiroRigthService {
	int insertShiroRigth(ShiroRigth shiroRigth)  throws Exception;
	int deleteShiroRigthByRigthCode(String rigthCode)  throws Exception;
	ShiroRigth findShiroRigthById(Integer id)  throws Exception;
	ShiroRigth findShiroRigthByRigthCode(String rigthCode)  throws Exception;
	List<ShiroRigth> findAllShiroRigth()  throws Exception;
	List<ShiroRigth> findUserRolePrivilegeRigth(Integer userId)  throws Exception;
	List<ShiroRigth> findUserWithRolesPrivilegeRigthByParentId(Integer userId,Integer parentId)   throws Exception;
	List<ShiroRigth> findShiroRigthByParentId(Integer parentId)   throws Exception;
	List<ShiroRigth> findRolePrivilegeRigthByParentId(Integer roleId,Integer parentId)   throws Exception;
}
