package cn.eeepay.framework.service.sysUser.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.sysUser.ShiroRoleDao;
import cn.eeepay.framework.model.ShiroRole;
import cn.eeepay.framework.service.sysUser.ShiroRoleService;
//@Service("shiroRoleService")
//@Transactional
public class ShiroRoleServiceImpl implements ShiroRoleService {
	@Resource
	public ShiroRoleDao shiroRoleDao;

	@Override
	public ShiroRole findShiroRoleByRoleCode(String roleCode) throws Exception {
		return shiroRoleDao.findShiroRoleByRoleCode(roleCode);
	}

	@Override
	public ShiroRole findShiroRoleById(Integer id) throws Exception {
		return shiroRoleDao.findShiroRoleById(id);
	}

	@Override
	public List<ShiroRole> findAllShiroRole() throws Exception {
		return shiroRoleDao.findAllShiroRole();
	}

	@Override
	public int updateShiroRole(ShiroRole shiroRole) {
		return shiroRoleDao.updateShiroRole(shiroRole);
	}

	@Override
	public int insertShiroRole(ShiroRole shiroRole) {
		return shiroRoleDao.insertShiroRole(shiroRole);
	}

}
