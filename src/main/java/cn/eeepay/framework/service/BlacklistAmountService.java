package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BlacklistAmount;
/**
 * 金额黑名单 服务层
 *
 * @author tans
 * @date 2019-08-09
 */
public interface BlacklistAmountService {

    /**
     * 条件查询金额黑名单
     * @param baseInfo
     * @return
     */
    void selectPage(Page<BlacklistAmount> page, BlacklistAmount baseInfo);

    /**
     * 新增金额黑名单
     * @param baseInfo
     * @return
     */
    int insert(BlacklistAmount baseInfo);

    /**
     * 删除金额黑名单
     * @param id
     * @return
     */
    int delete(Integer id);
}
