package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import cn.eeepay.framework.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.daoCreditMgr.CmUserManageDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmCardInfo;
import cn.eeepay.framework.model.CmOrgInfo;
import cn.eeepay.framework.model.CmUserInfo;
import cn.eeepay.framework.service.CmUserManageService;

@Service("cmUserManageService")
@Transactional
public class CmUserManageServiceImpl implements CmUserManageService{
//	private static final Logger log = LoggerFactory.getLogger(CmUserManageServiceImpl.class);

	@Resource
	private CmUserManageDao cmUserManageDao;
	
	/**
	 * 查询所有组织
	 * @author	mays
	 * @date	2018年3月29日
	 */
	public List<CmOrgInfo> selectOrgAllInfo() {
		return cmUserManageDao.selectOrgAllInfo();
	}

	/**
	 * 查询用户信息
	 * @author	mays
	 * @date	2018年4月3日
	 */
	public List<CmUserInfo> selectUserInfo(Page<CmUserInfo> page, CmUserInfo info) {
		List<CmUserInfo> list=cmUserManageDao.selectUserInfo(page, info);
		dataProcessingList(page.getResult());
		return list;
	}

	/**
	 * 导出用户信息
	 */
	public List<CmUserInfo> exportCmUser(CmUserInfo info) {
		List<CmUserInfo> list=cmUserManageDao.exportCmUser(info);
		dataProcessingList(list);
		return list;
	}

	/**
	 * 数据处理List
	 */
	private void dataProcessingList(List<CmUserInfo> list){
		if(list!=null&&list.size()>0){
			for(CmUserInfo item:list){
				if(item!=null){
					item.setMobileNo(StringUtil.sensitiveInformationHandle(item.getMobileNo(),0));
				}
			}
		}
	}

	/**
	 * 根据userNo查询用户信息
	 */
	public CmUserInfo selectUserInfoByUserNo(String userNo) {
		CmUserInfo info = cmUserManageDao.selectUserInfoByUserNo(userNo);
		if (!"1".equals(info.getIsVip())) {
			info.setUserType(0);
		}
		return info;
	}

	/**
	 * 根据userNo查询用户卡片信息
	 */
	public List<CmCardInfo> selectCardInfoByUserNo(String userNo) {
		return cmUserManageDao.selectCardInfoByUserNo(userNo);
	}

	/**
	 * 修改用户信息
	 */
	public int updateUserInfo(String userNo, String mobileNo) {
		return cmUserManageDao.updateUserInfo(userNo, mobileNo);
	}

}
