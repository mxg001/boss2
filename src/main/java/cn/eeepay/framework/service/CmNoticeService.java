package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmNoticeInfo;

public interface CmNoticeService {

	/**
	 * 公告查询
	 * @author mays
	 * @date 2018年4月2日
	 */
	List<CmNoticeInfo> selectInfo(Page<CmNoticeInfo> page, CmNoticeInfo info);

	/**
	 * 新增公告
	 * @author mays
	 * @date 2018年4月2日
	 */
	int addNotice(CmNoticeInfo info);

	/**
	 * 根据id查询公告
	 * @author mays
	 * @date 2018年4月3日
	 */
	CmNoticeInfo queryNoticeById(String id);

	/**
	 * 修改公告
	 * @author mays
	 * @date 2018年4月3日
	 */
	int updateNotice(CmNoticeInfo info);

	/**
	 * 根据id删除公告，置为'已废弃'
	 * @author mays
	 * @date 2018年4月3日
	 */
	int delNoticeById(String id);

	/**
	 * 修改'弹窗提示开关'
	 * @author mays
	 * @date 2018年4月3日
	 */
	int updateNoticePop(String id, String popSwitch);

	/**
	 * 下发或收回公告
	 * @author mays
	 * @date 2018年4月3日
	 */
	int sendOrRecoverNotice(CmNoticeInfo info);

}
