package cn.eeepay.framework.service.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateBanner;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * banner service
 */
public interface ExchangeActivateBannerService {

    List<ExchangeActivateBanner> selectAllList(ExchangeActivateBanner banner, Page<ExchangeActivateBanner> page);

    int addBanner(ExchangeActivateBanner banner);

    ExchangeActivateBanner getBanner(long id);

    int updateBanner(ExchangeActivateBanner banner);

    int deleteBanner(long id);

    int closeBanner(long id, String state);
}
