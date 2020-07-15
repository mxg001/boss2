package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmCardInfo;

public interface CmCardService {

	/**
	 * 卡片列表查询
	 * @author mays
	 * @date 2018年4月8日
	 */
	List<CmCardInfo> selectCardInfo(Page<CmCardInfo> page, CmCardInfo info);

	List<CmCardInfo> exportCmCard(CmCardInfo info);

	/**
	 * 根据id查询卡片信息
	 * @author mays
	 * @date 2018年4月8日
	 */
	CmCardInfo queryCardInfoById(String id);

	/**
	 * 修改卡片信息
	 * @author mays
	 * @date 2018年4月8日
	 */
	int updateCardInfo(CmCardInfo info);

}
