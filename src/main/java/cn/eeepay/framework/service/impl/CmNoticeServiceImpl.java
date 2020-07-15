package cn.eeepay.framework.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.daoCreditMgr.CmNoticeDao;
import cn.eeepay.framework.daoCreditMgr.CmUserManageDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmNoticeInfo;
import cn.eeepay.framework.model.CmOrgInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.CmNoticeService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtil;

@Service("cmNoticeService")
@Transactional
public class CmNoticeServiceImpl implements CmNoticeService {

	@Resource
	private CmNoticeDao cmNoticeDao;

	@Resource
	private CmUserManageDao cmUserManageDao;

	/**
	 * 公告查询
	 * @author mays
	 * @date 2018年4月2日
	 */
	public List<CmNoticeInfo> selectInfo(Page<CmNoticeInfo> page, CmNoticeInfo info) {
		cmNoticeDao.selectInfo(page, info);
		// 获取所有组织的map集合<orgId,orgName>
		List<CmOrgInfo> orgAllInfo = cmUserManageDao.selectOrgAllInfo();
		HashMap<String, String> orgMap = new HashMap<>();
		for (CmOrgInfo coi : orgAllInfo) {
			orgMap.put(coi.getOrgId(), coi.getOrgName());
		}
		// 转换
		List<CmNoticeInfo> result = page.getResult();
		for (CmNoticeInfo cni : result) {
			StringBuilder orgName = new StringBuilder();
			String orgId = cni.getOrgId();
			if (orgId == null || "".equals(orgId)) {
				continue;
			}
			if ("-1".equals(orgId)) {
				cni.setOrgId("全部");
			} else {
				String[] orgIdArr = orgId.split(",");
				for (String item : orgIdArr) {
					orgName.append(orgMap.get(item));
					orgName.append(",");
				}
				cni.setOrgId(orgName.substring(0, orgName.length() - 1).toString());
			}
		}
		page.setResult(result);
		return result;
	}

	/**
	 * 新增公告
	 * @author mays
	 * @date 2018年4月2日
	 */
	public int addNotice(CmNoticeInfo info) {
		UserLoginInfo loginInfo = CommonUtil.getLoginUser();
		addAndUpdate(info, loginInfo);
		info.setCreateBy(loginInfo.getUsername());
		return cmNoticeDao.addNotice(info);
	}

	/**
	 * 新增和修改公告前的数据处理
	 * @author mays
	 * @date 2018年4月3日
	 */
	private void addAndUpdate(CmNoticeInfo info, UserLoginInfo loginInfo) {
		// 如果是立即发布
		if ("1".equals(info.getSubmitType())) {
			info.setStatus("2");// 已发布
			if (StringUtils.isNotBlank(info.getSendTimeStr())) {
				info.setSendTime(DateUtil.parseLongDateTime(info.getSendTimeStr()));
			} else {
				info.setSendTime(new Date());
			}
			info.setSenderId(loginInfo.getId().toString());
			info.setSenderName(loginInfo.getUsername());
		} else {
			if (StringUtils.isNotBlank(info.getSendTimeStr())) {
				info.setSendTime(DateUtil.parseLongDateTime(info.getSendTimeStr()));
			}
			info.setStatus("1");// 待发布
		}
		if (StringUtils.isNotBlank(info.getIconName())) {
			Date expiresDate = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
			String iconUrl = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, info.getIconName(), expiresDate);
			info.setIconUrl(iconUrl);
		}
		String orgId = info.getOrgId();
		// 如果orgId最后有逗号，则去掉
		if (orgId.length() > 0 && ",".equals(orgId.substring(orgId.length() - 1))) {
			info.setOrgId(orgId.substring(0, orgId.length() - 1));
		}
	}

	/**
	 * 根据id查询公告
	 * @author mays
	 * @date 2018年4月3日
	 */
	public CmNoticeInfo queryNoticeById(String id) {
		return cmNoticeDao.queryNoticeById(id);
	}

	/**
	 * 修改公告
	 * @author mays
	 * @date 2018年4月3日
	 */
	public int updateNotice(CmNoticeInfo info) {
		addAndUpdate(info, CommonUtil.getLoginUser());
		return cmNoticeDao.updateNotice(info);
	}

	/**
	 * 根据id删除公告，置为'已废弃'
	 * @author mays
	 * @date 2018年4月3日
	 */
	public int delNoticeById(String id) {
		return cmNoticeDao.delNoticeById(id);
	}

	/**
	 * 修改'弹窗提示开关'
	 * @author mays
	 * @date 2018年4月3日
	 */
	public int updateNoticePop(String id, String popSwitch) {
		return cmNoticeDao.updateNoticePop(id, popSwitch);
	}

	/**
	 * 下发或收回公告
	 * @author mays
	 * @date 2018年4月3日
	 */
	public int sendOrRecoverNotice(CmNoticeInfo info) {
		UserLoginInfo loginInfo = CommonUtil.getLoginUser();
		if ("2".equals(info.getStatus())) {// 下发
			info.setSendTime(new Date());
			info.setSenderId(loginInfo.getId().toString());
			info.setSenderName(loginInfo.getUsername());
		} else {// 收回
			info.setSendTime(null);
			info.setSenderId(null);
			info.setSenderName(null);
		}
		return cmNoticeDao.sendOrRecoverNotice(info);
	}

}
