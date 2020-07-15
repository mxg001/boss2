package cn.eeepay.framework.service.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.BannerAllAgent;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * banner service
 */
public interface BannerAllAgentService {

    List<BannerAllAgent> selectAllList(BannerAllAgent banner, Page<BannerAllAgent> page);

    int addBanner(BannerAllAgent banner);

    BannerAllAgent getBanner(long id);

    int updateBanner(BannerAllAgent banner);

    int deleteBanner(long id);

    int closeBanner(long id, String state);
}
