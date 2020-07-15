package cn.eeepay.framework.service.impl.exchangeActivate;

import cn.eeepay.framework.daoExchange.exchangeActivate.ExchangeActivateHelpCenterDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateHelpCenter;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateHelpCenterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/5/11/011.
 * @author  liuks
 * 帮助中心service实现类
 */
@Service("exchangeActivateHelpCenterService")
public class ExchangeActivateHelpCenterServiceImpl implements ExchangeActivateHelpCenterService {

    @Resource
    private ExchangeActivateHelpCenterDao exchangeActivateHelpCenterDao;

    @Override
    public List<ExchangeActivateHelpCenter> selectAllList(ExchangeActivateHelpCenter help, Page<ExchangeActivateHelpCenter> page) {
        return exchangeActivateHelpCenterDao.selectAllList(help,page);
    }

    @Override
    public int addHelpCenter(ExchangeActivateHelpCenter help) {
        return exchangeActivateHelpCenterDao.addHelpCenter(help);
    }

    @Override
    public ExchangeActivateHelpCenter getHelpCenter(long id) {
        return exchangeActivateHelpCenterDao.getHelpCenter(id);
    }

    @Override
    public int updateHelpCenter(ExchangeActivateHelpCenter help) {
        return exchangeActivateHelpCenterDao.updateHelpCenter(help);
    }

    @Override
    public int deleteHelpCenter(long id) {
        return exchangeActivateHelpCenterDao.deleteHelpCenter(id);
    }

    @Override
    public int updateHelpCenterState(long id, String state) {
        return exchangeActivateHelpCenterDao.updateHelpCenterState(id,state);
    }
}
