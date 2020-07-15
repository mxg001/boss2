package cn.eeepay.framework.service;

import java.util.List;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.StaticUserInfo;

public interface StaticUserInfoService {

	List<StaticUserInfo> selectAllInfo(Page<StaticUserInfo> page,StaticUserInfo staticUserInfo);
	int addInfo(StaticUserInfo staticUserInfo);
	List<StaticUserInfo> findInfo(StaticUserInfo staticUserInfo);
	int deleteInfo(String id);
	List<StaticUserInfo> selectAllInfoImprot(StaticUserInfo staticUserInfo);
}
