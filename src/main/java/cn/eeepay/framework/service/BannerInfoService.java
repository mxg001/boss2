package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AppInfo;
import cn.eeepay.framework.model.BannerInfo;

public interface BannerInfoService {

	List<BannerInfo> selectByCondition(BannerInfo banner,Page<BannerInfo> page);

	int updateStatus(BannerInfo banner);

	BannerInfo selectDetailById(String id);

	Map<String, Object> selectLinkInfo(String string);

	int insertOrUpdate(BannerInfo banner);

	List<AppInfo> getAppInfo();

	int deleteBanner(Integer id);

}
