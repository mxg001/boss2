package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmCardInfo;
import cn.eeepay.framework.model.CmOrgInfo;
import cn.eeepay.framework.model.CmUserInfo;

public interface CmUserManageService {

	/**
	 * 查询所有组织
	 * @author	mays
	 * @date	2018年3月29日
	 */
	List<CmOrgInfo> selectOrgAllInfo();

	/**
	 * 查询用户信息
	 * @author	mays
	 * @date	2018年4月3日
	 */
	List<CmUserInfo> selectUserInfo(Page<CmUserInfo> page, CmUserInfo info);

	/**
	 * 导出用户信息
	 * @author	mays
	 * @date	2018年5月22日
	 */
	List<CmUserInfo> exportCmUser(CmUserInfo info);

	/**
	 * 根据userNo查询用户信息
	 * @author	mays
	 * @date	2018年4月6日
	 */
	CmUserInfo selectUserInfoByUserNo(String userNo);

	/**
	 * 根据userNo查询用户卡片信息
	 * @author	mays
	 * @date	2018年4月6日
	 */
	List<CmCardInfo> selectCardInfoByUserNo(String userNo);

	/**
	 * 修改用户信息
	 * @author	mays
	 * @date	2018年4月6日
	 */
	int updateUserInfo(String userNo, String mobileNo);

}
