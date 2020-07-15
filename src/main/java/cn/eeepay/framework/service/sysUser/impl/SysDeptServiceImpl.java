package cn.eeepay.framework.service.sysUser.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.sysUser.SysDeptDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.SysDept;
import cn.eeepay.framework.service.sysUser.SysDeptService;
//@Service("sysDeptService")
//@Transactional
public class SysDeptServiceImpl implements SysDeptService {
	@Resource
	public SysDeptDao sysDeptDao;

	@Override
	public int insert(SysDept sysDept) throws Exception {
		return sysDeptDao.insert(sysDept);
	}

	@Override
	public List<SysDept> findSysDeptList(SysDept sysDept, Sort sort, Page<SysDept> page) throws Exception {
		return sysDeptDao.findSysDeptList(sysDept, sort, page);
	}

	@Override
	public List<SysDept> findAllSysDeptList() throws Exception {
		return sysDeptDao.findAllSysDeptList();
	}

	@Override
	public SysDept findSysDeptById(String id) throws Exception {
		return sysDeptDao.findSysDeptById(id);
	}
}
