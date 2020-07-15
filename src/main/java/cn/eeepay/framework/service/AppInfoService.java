package cn.eeepay.framework.service;

import java.util.List;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AppInfo;
import cn.eeepay.framework.model.MobileVerInfo;

public interface AppInfoService {

	List<AppInfo> selectAllInfo(Page<AppInfo> page,AppInfo ais);
	
	AppInfo selectDetail(int id);
	
	int insert(AppInfo ais);
	
	List<AppInfo> selectInfoBox();
	
	List<AppInfo> selectInfoBoxName(String parentId);
	
	AppInfo selectInfo(int id);
	
	AppInfo selectInfos(String name,String parenId);
	
	int update(AppInfo ais);
	
	AppInfo findInfo(String appId);
	
	List<MobileVerInfo> selectChildAllInfo(Page<MobileVerInfo> page,String appId);
	
	MobileVerInfo findChildDetailInfo(String id);
	
	int updateMviInfo(MobileVerInfo mvi);
	
	int insertMviInfo(MobileVerInfo mvi);

	/**
	 * 校验teamId和appNo组合唯一
	 * @param teamId
	 * @param appNo
	 * @return
	 */
    boolean checkUniqueApp(String teamId, String appNo);

	List<AppInfo> getAllAppInfo();
}
