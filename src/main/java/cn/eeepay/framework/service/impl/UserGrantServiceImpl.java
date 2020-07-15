package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.GrantDao;
import cn.eeepay.framework.dao.RightDao;
import cn.eeepay.framework.dao.RoleDao;
import cn.eeepay.framework.dao.UserDao;
import cn.eeepay.framework.model.MenuInfo;
import cn.eeepay.framework.model.RightInfo;
import cn.eeepay.framework.service.UserGrantService;
@Service("userGrantService")
@Transactional
public class UserGrantServiceImpl implements UserGrantService {
	@Resource
	public UserDao userDao;
	@Resource
	public RoleDao roleDao;
	@Resource
	public RightDao rightDao;
	@Resource
	public GrantDao grantDao;
	@Override
	public int updateRoleToUser(Integer userId, List<Integer> roleList) {
		userDao.delUserRoles(userId);
		if(roleList==null || roleList.size()==0){
			return 1;
		}
		return grantDao.grantUserRole(userId,roleList,"user_role");
	}
	@Override
	public int updateRightToUser(Integer userId, List<Integer> rightList) {
		userDao.delUserRights(userId);
		if(rightList==null || rightList.size()==0){
			return 1;
		}
		return grantDao.grantUserRight(userId,rightList,"user_right");
	}
	@Override
	public int updateRoleAddUser(Integer roleId, List<Integer> userList) {
		roleDao.delUserRoles(roleId);
		if(userList==null || userList.size()==0){
			return 1;
		}
		return grantDao.grantUserRole(roleId,userList,"role_user");
	}
	@Override
	public int updateRightToRole(Integer roleId, List<Integer> rightList) {
		roleDao.delRoleRights(roleId);
		if(rightList==null || rightList.size()==0){
			return 1;
		}
		return grantDao.grantRoleRight(roleId,rightList,"role_right");
	}
	@Override
	public int updateRightAddUser(Integer rightId, List<Integer> userList) {
		rightDao.delUsers(rightId);
		if(userList==null || userList.size()==0){
			return 1;
		}
		return grantDao.grantUserRight(rightId,userList,"right_user");
	}
	@Override
	public int updateRightAddRole(Integer rightId, List<Integer> roleList) {
		rightDao.delRoles(rightId);
		if(roleList==null || roleList.size()==0){
			return 1;
		}
		return grantDao.grantRoleRight(rightId,roleList,"right_role");
	}
	
	@Override
	public int deleteUserRole(Integer userId, Integer roleId) {
		return grantDao.delUserRole(userId, roleId);
	}
	@Override
	public int insertUserRole(Integer userId, Integer roleId) {
		return grantDao.insertUserRole(userId, roleId);
	}
	@Override
	public int deleteUserRight(Integer userId, Integer rightId) {
		return grantDao.delUserRight(userId, rightId);
	}
	@Override
	public int insertUserRight(Integer userId, Integer rightId) {
		return grantDao.insertUserRight(userId, rightId);
	}
	@Override
	public int deleteRoleRight(Integer roleId, Integer rightId) {
		return grantDao.delRoleRight(roleId, rightId);
	}
	@Override
	public int insertRoleRight(Integer roleId, Integer rightId) {
		return grantDao.insertRoleRight(roleId, rightId);
	}
	
	
	@Override
	public int updateMenuToRight(Integer right, List<Integer> menuList) {
		grantDao.deleteRightMenuByRightId(right);
		if(menuList==null || menuList.size()==0){
			return 1;
		}
		return grantDao.grantUserRight(right,menuList,"right_menu");
	}
	@Override
	public int updateRightToMenu(Integer menu, List<Integer> rightList) {
		grantDao.deleteRightMenuByMenuId(menu);
		if(rightList==null || rightList.size()==0){
			return 1;
		}
		return grantDao.grantUserRight(menu,rightList,"menu_right");
	}
	@Override
	public int deleteRightMenu(Integer rightId, Integer menuId) {
		return grantDao.delRightMenu(rightId,menuId);
	}
	@Override
	public int insertRightMenu(Integer rightId, Integer menuId) {
		return grantDao.insertRightMenu(rightId,menuId);
	}
	@Override
	public List<RightInfo> getUserAllRight(Integer userId) {
		return grantDao.getUserAllRight(userId);
	}
	@Override
	public int updateMenuToUser(Integer userId, List<Integer> menuIds) {
		grantDao.deleteMenuByUser(userId);
		if(menuIds==null || menuIds.size()==0){
			return 1;
		}
		return grantDao.grantUserMenu(userId,menuIds,"user_menu");
	}
	@Override
	public List<MenuInfo> getUserAllMenu(Integer uId) {
		return grantDao.getUserAllMenu(uId);
	}
}
