package cn.eeepay.framework.service.sysUser;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.ShiroUser;

public interface UserService {
	int insertUser(ShiroUser user);
	List<ShiroUser> getUsers(ShiroUser user,Sort sort,Page<ShiroUser> page);
	List<ShiroUser> findUsersWithRole();
	ShiroUser findUsersWithRoleByUserName(String userName);
	ShiroUser findUserById(Integer id);
	int updateUser(ShiroUser shiroUser);
	int updateUserPwd(Integer id,String password);
	boolean isBlocked(String key);
}
