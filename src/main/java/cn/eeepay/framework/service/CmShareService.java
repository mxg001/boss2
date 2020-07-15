package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmShare;

public interface CmShareService {

	/**
	 * 分润查询
	 * @author	mays
	 * @date	2018年5月24日
	 */
	List<CmShare> selectShareInfo(Page<CmShare> page, CmShare info);

	List<CmShare> exportShareInfo(CmShare info);

	Map<String, String> sumShareInfo(CmShare info);

}
