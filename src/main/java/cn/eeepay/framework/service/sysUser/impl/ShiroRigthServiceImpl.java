package cn.eeepay.framework.service.sysUser.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.sysUser.ShiroRigthDao;
import cn.eeepay.framework.model.ShiroRigth;
import cn.eeepay.framework.service.sysUser.ShiroRigthService;
@Service("shiroRigthService")
@Transactional
public class ShiroRigthServiceImpl implements ShiroRigthService {
	@Resource
	public ShiroRigthDao shiroRigthDao;

	@Override
	public ShiroRigth findShiroRigthById(Integer id) throws Exception {
		return shiroRigthDao.findShiroRigthById(id);
	}

	@Override
	public List<ShiroRigth> findUserRolePrivilegeRigth(Integer userId) throws Exception {
		return shiroRigthDao.findUserRolePrivilegeRigth(userId);
	}
	@Override
	public ShiroRigth findShiroRigthByRigthCode(String rigthCode) throws Exception {
		return shiroRigthDao.findShiroRigthByRigthCode(rigthCode);
	}
	@Override
	public List<ShiroRigth> findUserWithRolesPrivilegeRigthByParentId(Integer userId, Integer parentId) throws Exception {
		return shiroRigthDao.findUserWithRolesPrivilegeRigthByParentId(userId, parentId);
	}

	@Override
	public List<ShiroRigth> findShiroRigthByParentId(Integer parentId) throws Exception {
		return shiroRigthDao.findShireRigthByParentId(parentId);
	}

	@Override
	public List<ShiroRigth> findAllShiroRigth() throws Exception {
		return shiroRigthDao.findAllShiroRigth();
	}

	@Override
	public List<ShiroRigth> findRolePrivilegeRigthByParentId(Integer roleId,Integer parentId) throws Exception {
		return shiroRigthDao.findRolePrivilegeRigthByParentId(roleId,parentId);
	}

	@Override
	public int insertShiroRigth(ShiroRigth shiroRigth) throws Exception {
		return shiroRigthDao.insertShiroRigth(shiroRigth);
	}

	@Override
	public int deleteShiroRigthByRigthCode(String rigthCode) throws Exception {
		return shiroRigthDao.deleteShiroRigthByRigthCode(rigthCode);
	}
}
