package cn.eeepay.framework.service.impl.cjt;

import cn.eeepay.framework.dao.cjt.CjtWhiteMerDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.cjt.CjtWhiteMer;
import cn.eeepay.framework.service.cjt.CjtWhiteMerService;
import cn.eeepay.framework.util.BossBaseException;
import cn.eeepay.framework.util.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 超级推商户白名单 服务层实现
 * @author tans
 * @date 2019-05-29
 */
@Service
public class CjtWhiteMerServiceImpl implements CjtWhiteMerService {

    @Resource
    private CjtWhiteMerDao cjtWhiteMerDao;

    /**
     * 条件查询超级推商户白名单
     * @param cjtWhiteMer
     * @return
     */
    @Override
    public void selectPage(Page<CjtWhiteMer> page, CjtWhiteMer cjtWhiteMer) {
        cjtWhiteMerDao.selectPage(page, cjtWhiteMer);
        return;
    }

    /**
     * 新增超级推商户白名单
     * @param cjtWhiteMer
     * @return
     */
    @Override
    public int insert(CjtWhiteMer cjtWhiteMer) {
        //校验商户号是否已存在
        CjtWhiteMer baseInfo = cjtWhiteMerDao.selectDetailByMer(cjtWhiteMer.getMerchantNo());
        if(baseInfo != null){
            throw new BossBaseException("重复添加");
        }
        cjtWhiteMer.setStatus(1);
        cjtWhiteMer.setCreateTime(new Date());
        cjtWhiteMer.setCreater(CommonUtil.getLoginUserName());
        return cjtWhiteMerDao.insert(cjtWhiteMer);
    }

    /**
     * 修改超级推商户白名单
     * @param cjtWhiteMer
     * @return
     */
    @Override
    public int update(CjtWhiteMer cjtWhiteMer) {
        //校验商户号是否已存在
        CjtWhiteMer baseInfo = cjtWhiteMerDao.selectDetailByMer(cjtWhiteMer.getMerchantNo());
        if(baseInfo != null && baseInfo.getId() != cjtWhiteMer.getId()) {
            throw new BossBaseException("重复添加");
        }
        return cjtWhiteMerDao.update(cjtWhiteMer);
    }

    @Override
    public int delete(Integer id) {
        return cjtWhiteMerDao.delete(id);
    }

    @Override
    public int updateStatus(CjtWhiteMer baseInfo) {
        return cjtWhiteMerDao.updateStatus(baseInfo);
    }
}
