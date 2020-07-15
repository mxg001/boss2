package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ExchangeBanner;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * banner service
 */
public interface ExchangeBannerService {

    List<ExchangeBanner> selectAllList(ExchangeBanner banner, Page<ExchangeBanner> page);

    int addBanner(ExchangeBanner banner);

    ExchangeBanner getBanner(long id);

    int updateBanner(ExchangeBanner banner);

    int deleteBanner(long id);

    int closeBanner(long id, String state);
}
