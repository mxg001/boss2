package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.StaticUserInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.StaticUserInfo;
import cn.eeepay.framework.service.StaticUserInfoService;

@Service("staticUserInfoService")
@Transactional
public class StaticUserInfoServiceImpl implements StaticUserInfoService{

	@Resource
	private StaticUserInfoDao staticUserInfoDao;

	@Resource
	private SeqService seqService;
	
	@Override
	public List<StaticUserInfo> selectAllInfo(Page<StaticUserInfo> page, StaticUserInfo staticUserInfo) {
		return staticUserInfoDao.selectAllInfo(page, staticUserInfo);
	}

	@Override
	public int addInfo(StaticUserInfo staticUserInfo) {
		staticUserInfo.setStaticUserId(Integer.valueOf(seqService.createKey("app_no_seq")));
		return staticUserInfoDao.addInfo(staticUserInfo);
	}

	@Override
	public int deleteInfo(String id) {
		return staticUserInfoDao.deleteInfo(id);
	}

	@Override
	public List<StaticUserInfo> findInfo(StaticUserInfo staticUserInfo) {
		return staticUserInfoDao.findInfo(staticUserInfo);
	}

	@Override
	public List<StaticUserInfo> selectAllInfoImprot(StaticUserInfo staticUserInfo) {
		return staticUserInfoDao.selectAllInfoImprot(staticUserInfo);
	}
}
