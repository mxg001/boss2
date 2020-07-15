package cn.eeepay.framework.service.cjt;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.cjt.CjtWhiteMer;

/**
 * 超级推商户白名单 服务层
 *
 * @author tans
 * @date 2019-05-29
 */
public interface CjtWhiteMerService {

    /**
     * 条件查询超级推商户白名单
     * @param baseInfo
     * @return
     */
    void selectPage(Page<CjtWhiteMer> page, CjtWhiteMer baseInfo);

    /**
     * 新增超级推商户白名单
     * @param baseInfo
     * @return
     */
    int insert(CjtWhiteMer baseInfo);

    /**
     * 修改超级推商户白名单
     * @param baseInfo
     * @return
     */
    int update(CjtWhiteMer baseInfo);

    int delete(Integer id);

    int updateStatus(CjtWhiteMer baseInfo);
}
