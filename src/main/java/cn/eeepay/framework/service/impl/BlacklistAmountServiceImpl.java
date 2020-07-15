package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.BlacklistAmountDao;
import cn.eeepay.framework.dao.JumpRouteDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BlacklistAmount;
import cn.eeepay.framework.model.JumpRouteConfig;
import cn.eeepay.framework.service.BlacklistAmountService;
import cn.eeepay.framework.util.BossBaseException;
import cn.eeepay.framework.util.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 金额黑名单 服务层实现
 * @author tans
 * @date 2019-08-09
 */
@Service
public class BlacklistAmountServiceImpl implements BlacklistAmountService{

    @Resource
    private BlacklistAmountDao blacklistAmountDao;

    @Resource
    private JumpRouteDao jumpRouteDao;

    /**
     * 条件查询金额黑名单
     * @param baseInfo
     * @return
     */
    @Override
    public void selectPage(Page<BlacklistAmount> page, BlacklistAmount baseInfo) {
        blacklistAmountDao.selectPage(page, baseInfo);
        return;
    }

    /**
     * 新增金额黑名单
     * @param baseInfo
     * @return
     */
    @Override
    public int insert(BlacklistAmount baseInfo) {
        //检查路由是否存在
        JumpRouteConfig config = jumpRouteDao.getById(baseInfo.getJumpRuleId());
        if(config == null) {
            throw new BossBaseException("路由不存在");
        }
        //检查跳转规则ID和金额，在`blacklist_amount` 组合唯一
        if(blacklistAmountDao.selectExists(baseInfo) > 0) {
            throw new BossBaseException("路由ID和金额组合已存在");
        }
        baseInfo.setOperator(CommonUtil.getLoginUserName());
        return blacklistAmountDao.insert(baseInfo);
    }

    /**
     * 删除金额黑名单
     * @param id
     * @return
     */
    @Override
    public int delete(Integer id) {
        return blacklistAmountDao.delete(id);
    }
}
