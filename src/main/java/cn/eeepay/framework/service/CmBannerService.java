package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmBannerInfo;

public interface CmBannerService {

	/**
	 * Banner查询
	 * @author	mays
	 * @date	2018年3月29日
	 */
	List<CmBannerInfo> selectInfo(Page<CmBannerInfo> page, CmBannerInfo info);

	/**
	 * 新增Banner
	 * @author	mays
	 * @date	2018年3月30日
	 */
	int addBanner(CmBannerInfo info);

	/**
	 * 根据id查询Banner
	 * @author	mays
	 * @date	2018年3月30日
	 */
	CmBannerInfo queryBannerById(String id);

	/**
	 * 修改Banner
	 * @author	mays
	 * @date	2018年3月30日
	 */
	int updateBanner(CmBannerInfo info);

	/**
	 * 根据id删除Banner
	 * @author	mays
	 * @date	2018年4月2日
	 */
	int delBannerById(String id);

	/**
	 * 修改Banner状态
	 * @author	mays
	 * @date	2018年4月2日
	 */
	int updateBannerStatus(String id, String status);

}
