package cn.eeepay.framework.service.impl.exchangeActivate;

import cn.eeepay.framework.daoExchange.exchangeActivate.ExchangeActivateBannerDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateBanner;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateBannerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * banner serivce实现类
 */
@Service("exchangeActivateBannerService")
public class ExchangeActivateBannerServiceImpl implements ExchangeActivateBannerService {

    @Resource
    private ExchangeActivateBannerDao exchangeActivateBannerDao;

    @Override
    public List<ExchangeActivateBanner> selectAllList(ExchangeActivateBanner banner, Page<ExchangeActivateBanner> page) {
        return exchangeActivateBannerDao.selectAllList(banner,page);
    }

    @Override
    public int addBanner(ExchangeActivateBanner banner) {
        return exchangeActivateBannerDao.addBanner(banner);
    }

    @Override
    public ExchangeActivateBanner getBanner(long id) {
        return exchangeActivateBannerDao.getBanner(id);
    }

    @Override
    public int updateBanner(ExchangeActivateBanner banner) {
        int num=0;
        if(banner.getImgUrl()!=null&&!"".equals(banner.getImgUrl())){
            num=exchangeActivateBannerDao.updateBanner(banner);
        }else{
            num=exchangeActivateBannerDao.updateBannerNoImg(banner);
        }
        return num;
    }

    @Override
    public int deleteBanner(long id) {
        return exchangeActivateBannerDao.deleteBanner(id);
    }

    @Override
    public int closeBanner(long id, String state) {
        return exchangeActivateBannerDao.closeBanner(id,state);
    }
}
