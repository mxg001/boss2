package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MenuInfo;
import cn.eeepay.framework.model.RightInfo;
import cn.eeepay.framework.model.RoleInfo;
import cn.eeepay.framework.model.UserInfo;

public interface RightService {
	//查询权限
	Page<RightInfo> selectRightByCondition(RightInfo right, Page<RightInfo> page);
	//新增权限
	int insertRight(RightInfo right);
	//修改权限
	int updateRight(RightInfo right);
	//删除权限
	int deleteRight(Integer rightId);
	
	Map<String, Object> checkCodeUnique(String rightCode);
	//权限已授权的角色
	List<RoleInfo> getRoleByRight(Integer rightId);
	
	//权限已授权的用户
	List<UserInfo> getUserByRight(Integer rightId);
	//查询所有的权限
	List<RightInfo> getAllRights();
	//权限已授权的菜单
	List<MenuInfo> getMenuByRight(Integer id);
}
