package cn.eeepay.framework.service.impl.allAgent;

import cn.eeepay.framework.daoAllAgent.BannerAllAgentDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.BannerAllAgent;
import cn.eeepay.framework.service.allAgent.BannerAllAgentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * banner serivce实现类
 */
@Service("bannerAllAgentService")
public class BannerAllAgentServiceImpl implements BannerAllAgentService {

    @Resource
    private BannerAllAgentDao bannerAllAgentDao;

    @Override
    public List<BannerAllAgent> selectAllList(BannerAllAgent banner, Page<BannerAllAgent> page) {
        return bannerAllAgentDao.selectAllList(banner,page);
    }

    @Override
    public int addBanner(BannerAllAgent banner) {
        return bannerAllAgentDao.addBanner(banner);
    }

    @Override
    public BannerAllAgent getBanner(long id) {
        return bannerAllAgentDao.getBanner(id);
    }

    @Override
    public int updateBanner(BannerAllAgent banner) {
        int num=0;
        if(banner.getImgUrl()!=null&&!"".equals(banner.getImgUrl())){
            num=bannerAllAgentDao.updateBanner(banner);
        }else{
            num=bannerAllAgentDao.updateBannerNoImg(banner);
        }
        return num;
    }

    @Override
    public int deleteBanner(long id) {
        return bannerAllAgentDao.deleteBanner(id);
    }

    @Override
    public int closeBanner(long id, String state) {
        return bannerAllAgentDao.closeBanner(id,state);
    }
}
