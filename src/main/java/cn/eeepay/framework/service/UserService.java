package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MenuInfo;
import cn.eeepay.framework.model.RightInfo;
import cn.eeepay.framework.model.RoleInfo;
import cn.eeepay.framework.model.UserInfo;

public interface UserService {
	// 查询用户
	void selectUserByCondition(UserInfo user, Page<UserInfo> page);

	// 新增用户
	int insertUser(UserInfo user);

	// 修改用户
	int updateUser(UserInfo user);
	
	//重置密码
	int restPwd(Integer id);
	
	//商户重置密码
	int merchantRestPwd(String telNo,String teamId);
	
	//修改密码
	int updateUserPwd(Map<String, Object> param);

	// 删除用户
	int deleteUser(Integer userId);

	Map<String, Object> checkNameUnique(String userName);

	// 用户已授权的角色
	List<RoleInfo> getRolesByUser(Integer id);

	// 用户已授权的权限
	List<RightInfo> getRightsByUser(Integer id);

	List<UserInfo> getAllUsers();

	List<MenuInfo> getMenuByUser(Integer id);

	List<UserInfo> selectUserByMenuCode(String menuCode);

	// List<RoleInfo> getOtherRolesByUser(Integer id);
	
	//待完善商户信息查询
	List<UserInfo> perfectMerchantQuery(UserInfo user, Page<UserInfo> page);

	// 删除待完善商户
	int delPerfectMerchant(Integer id);
	
	List<UserInfo> findUserBox();

	UserInfo selectUserByUserName(String userName);
	
	UserInfo selectInfoByTelNo(String telNo,String teamId);
	
	int updateInfoByMp(String telNo,String newTelNo,String teamId);

	List<UserInfo> getUserlimit(UserInfo user);

	UserInfo getUserInfoById(Integer id);
}
