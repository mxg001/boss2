package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.AppInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AppInfo;
import cn.eeepay.framework.model.MobileVerInfo;
import cn.eeepay.framework.service.AppInfoService;

@Service("appInfoService")
@Transactional
public class AppInfoServiceImpl implements AppInfoService {

	@Resource
	private AppInfoDao appInfoDao;

	@Resource
	private SeqService seqService;
	
	@Override
	public List<AppInfo> selectAllInfo(Page<AppInfo> page, AppInfo ais) {
		return appInfoDao.selectAllInfo(page, ais);
	}

	@Override
	public AppInfo selectDetail(int id) {
		return appInfoDao.selectDetail(id);
	}

	@Override
	public int insert(AppInfo ais) {
		//改为页面输入，updateBy tans 2018/1/20
//		ais.setAppNo(seqService.createKey("app_no_seq"));

		return appInfoDao.insert(ais);
	}

	@Override
	public List<AppInfo> selectInfoBox() {
		return appInfoDao.selectInfoBox();
	}

	@Override
	public List<AppInfo> selectInfoBoxName(String parentId) {
		return appInfoDao.selectInfoBoxName(parentId);
	}

	@Override
	public AppInfo selectInfo(int id) {
		return appInfoDao.selectInfo(id);
	}

	@Override
	public AppInfo selectInfos(String name, String parenId) {
		return appInfoDao.selectInfos(name, parenId);
	}

	@Override
	public int update(AppInfo ais) {
		return appInfoDao.update(ais);
	}

	@Override
	public List<MobileVerInfo> selectChildAllInfo(Page<MobileVerInfo> page, String appId) {
		return appInfoDao.selectChildAllInfo(page, appId);
	}

	@Override
	public AppInfo findInfo(String appId) {
		return appInfoDao.findInfo(appId);
	}

	@Override
	public MobileVerInfo findChildDetailInfo(String id) {
		return appInfoDao.findChildDetailInfo(id);
	}

	@Override
	public int updateMviInfo(MobileVerInfo mvi) {
		return appInfoDao.updateMviInfo(mvi);
	}

	@Override
	public int insertMviInfo(MobileVerInfo mvi) {
		return appInfoDao.insertMviInfo(mvi);
	}

	/**
	 * 校验teamId和appNo组合唯一
	 * @param teamId
	 * @param appNo
	 * @return
	 */
	@Override
	public boolean checkUniqueApp(String teamId, String appNo) {
		if(StringUtils.isBlank(teamId) || StringUtils.isBlank(appNo)){
			return false;
		}
		int num = appInfoDao.checkUniqueApp(teamId, appNo);
		if(num == 0){
			return true;
		}
		return false;
	}

	@Override
	public List<AppInfo> getAllAppInfo() {
		return appInfoDao.getAllAppInfo();
	}


}
