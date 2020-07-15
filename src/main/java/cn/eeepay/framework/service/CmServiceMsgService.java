package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmUserMessageInfo;

public interface CmServiceMsgService {

	/**
	 * 服务消息列表查询
	 * @author	mays
	 * @date	2018年4月9日
	 */
	List<CmUserMessageInfo> selectMsgInfo(Page<CmUserMessageInfo> page, CmUserMessageInfo info);

	/**
	 * 根据id查询消息详情
	 * @author	mays
	 * @date	2018年4月9日
	 */
	CmUserMessageInfo selectMsgInfoById(String id);

	/**
	 * 回收服务消息
	 * @author	mays
	 * @date	2018年4月9日
	 */
	int updateMsgIsDelById(String id);

}
