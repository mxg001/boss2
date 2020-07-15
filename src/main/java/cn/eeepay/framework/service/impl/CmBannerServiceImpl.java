package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.daoCreditMgr.CmBannerDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmBannerInfo;
import cn.eeepay.framework.service.CmBannerService;

@Service("cmBannerService")
@Transactional
public class CmBannerServiceImpl implements CmBannerService{
//	private static final Logger log = LoggerFactory.getLogger(CmUserManageServiceImpl.class);

	@Resource
	private CmBannerDao cmBannerDao;

	/**
	 * Banner查询
	 * @author	mays
	 * @date	2018年3月29日
	 */
	public List<CmBannerInfo> selectInfo(Page<CmBannerInfo> page, CmBannerInfo info) {
		return cmBannerDao.selectInfo(page, info);
	}

	/**
	 * 新增Banner
	 * @author	mays
	 * @date	2018年3月30日
	 */
	public int addBanner(CmBannerInfo info) {
		return cmBannerDao.addBanner(info);
	}

	/**
	 * 根据id查询Banner
	 * @author	mays
	 * @date	2018年3月30日
	 */
	public CmBannerInfo queryBannerById(String id) {
		return cmBannerDao.queryBannerById(id);
	}

	/**
	 * 修改Banner
	 * @author	mays
	 * @date	2018年3月30日
	 */
	public int updateBanner(CmBannerInfo info) {
		return cmBannerDao.updateBanner(info);
	}

	/**
	 * 根据id删除Banner
	 * @author	mays
	 * @date	2018年4月2日
	 */
	public int delBannerById(String id) {
		return cmBannerDao.delBannerById(id);
	}

	/**
	 * 修改Banner状态
	 * @author	mays
	 * @date	2018年4月2日
	 */
	public int updateBannerStatus(String id, String status) {
		return cmBannerDao.updateBannerStatus(id, status);
	}

}
