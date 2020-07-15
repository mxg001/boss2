package cn.eeepay.framework.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.RightDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MenuInfo;
import cn.eeepay.framework.model.RightInfo;
import cn.eeepay.framework.model.RoleInfo;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.service.RightService;

@Service("rightService")
@Transactional
public class RightServiceImpl implements RightService {

	@Resource
	private RightDao rightDao;
	
	@Override
	public Page<RightInfo> selectRightByCondition(RightInfo right, Page<RightInfo> page) {
		rightDao.selecRightByCondition(right, page);
		return page;
	}


	@Override
	public int insertRight(RightInfo right) {
		return rightDao.saveRight(right);
	}

	@Override
	public int updateRight(RightInfo right) {
		return rightDao.updateRight(right);
	}

	@Override
	public Map<String, Object> checkCodeUnique(String rightCode) {
		Map<String, Object> msg = new HashMap<>();
		Integer num = rightDao.checkCodeUnique(rightCode);
		if(num != null){
			msg.put("msg", "权限编码已存在");
			msg.put("status", false);
		}  else {
			msg.put("msg", "权限编码可以使用");
			msg.put("status", true);
		}
		return msg;
	}

	@Override
	public int deleteRight(Integer rightId) {
		rightDao.delUsers(rightId);
		rightDao.delRoles(rightId);
		int num = rightDao.delRight(rightId);
		return num;
	}

	@Override
	public List<RoleInfo> getRoleByRight(Integer rightId) {
		return rightDao.getRoleByRight(rightId);
	}

	@Override
	public List<UserInfo> getUserByRight(Integer rightId) {
		return rightDao.getUserByRight(rightId);
	}

	@Override
	public List<RightInfo> getAllRights() {
		return rightDao.getAllRights();
	}


	@Override
	public List<MenuInfo> getMenuByRight(Integer id) {
		return rightDao.getMenuByRight(id);
	}
	
	
}
