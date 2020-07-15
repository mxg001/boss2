package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoExchange.ExchangeHelpCenterDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.HelpCenter;
import cn.eeepay.framework.service.ExchangeHelpCenterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/5/11/011.
 * @author  liuks
 * 帮助中心service实现类
 */
@Service("exchangeHelpCenterService")
public class ExchangeHelpCenterServiceImpl implements ExchangeHelpCenterService {

    @Resource
    private ExchangeHelpCenterDao exchangeHelpCenterDao;
    @Override
    public List<HelpCenter> selectAllList(HelpCenter help, Page<HelpCenter> page) {
        return exchangeHelpCenterDao.selectAllList(help,page);
    }

    @Override
    public int addHelpCenter(HelpCenter help) {
        return exchangeHelpCenterDao.addHelpCenter(help);
    }

    @Override
    public HelpCenter getHelpCenter(long id) {
        return exchangeHelpCenterDao.getHelpCenter(id);
    }

    @Override
    public int updateHelpCenter(HelpCenter help) {
        return exchangeHelpCenterDao.updateHelpCenter(help);
    }

    @Override
    public int deleteHelpCenter(long id) {
        return exchangeHelpCenterDao.deleteHelpCenter(id);
    }

    @Override
    public int updateHelpCenterState(long id, String state) {
        return exchangeHelpCenterDao.updateHelpCenterState(id,state);
    }
}
