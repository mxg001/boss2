package cn.eeepay.framework.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.RoleDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RightInfo;
import cn.eeepay.framework.model.RoleInfo;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.RoleService;

@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {

	@Resource
	private RoleDao roleDao;
	
	@Override
	public Page<RoleInfo> selectRoleByCondition(RoleInfo role, Page<RoleInfo> page) {
		roleDao.selectRoleByCondition(role, page);
		return page;
	}

	@Override
	public int insertRole(RoleInfo role) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		role.setCreateOperator(principal.getId().toString());
		return roleDao.saveRole(role);
	}

	@Override
	public int updateRole(RoleInfo role) {
		// TODO Auto-generated method stub
		return roleDao.updateRole(role);
	}

	@Override
	public List<RoleInfo> getAllRoles() {
		// TODO Auto-generated method stub
		return roleDao.getAllRoles();
	}
	
	@Override
	public int deleteRoles(Integer roleId) {
		roleDao.delUserRoles(roleId);
		roleDao.delRoleRights(roleId);
		int num=roleDao.delRole(roleId);
		return num;
	}
	
	@Override
	public List<UserInfo> getUsersByRole(Integer id) {
		return roleDao.getUsersByRole(id);
	}

	@Override
	public List<RightInfo> getRightsByRole(Integer id) {
		return roleDao.getRightsByRole(id);
	}

}
