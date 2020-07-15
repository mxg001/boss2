package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoExchange.ExchangeBannerDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ExchangeBanner;
import cn.eeepay.framework.service.ExchangeBannerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * banner serivce实现类
 */
@Service("exchangeBannerService")
public class ExchangeBannerServiceImpl implements ExchangeBannerService {

    @Resource
    private ExchangeBannerDao exchangeBannerDao;

    @Override
    public List<ExchangeBanner> selectAllList(ExchangeBanner banner, Page<ExchangeBanner> page) {
        return exchangeBannerDao.selectAllList(banner,page);
    }

    @Override
    public int addBanner(ExchangeBanner banner) {
        return exchangeBannerDao.addBanner(banner);
    }

    @Override
    public ExchangeBanner getBanner(long id) {
        return exchangeBannerDao.getBanner(id);
    }

    @Override
    public int updateBanner(ExchangeBanner banner) {
        int num=0;
        if(banner.getImgUrl()!=null&&!"".equals(banner.getImgUrl())){
            num=exchangeBannerDao.updateBanner(banner);
        }else{
            num=exchangeBannerDao.updateBannerNoImg(banner);
        }
        return num;
    }

    @Override
    public int deleteBanner(long id) {
        return exchangeBannerDao.deleteBanner(id);
    }

    @Override
    public int closeBanner(long id, String state) {
        return exchangeBannerDao.closeBanner(id,state);
    }
}
