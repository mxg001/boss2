package cn.eeepay.framework.service.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateHelpCenter;

import java.util.List;

/**
 * Created by Administrator on 2018/5/11/011.
 * @author  liuks
 * 帮助中心service
 */
public interface ExchangeActivateHelpCenterService {

    List<ExchangeActivateHelpCenter> selectAllList(ExchangeActivateHelpCenter help, Page<ExchangeActivateHelpCenter> page);

    int addHelpCenter(ExchangeActivateHelpCenter help);

    ExchangeActivateHelpCenter getHelpCenter(long id);

    int updateHelpCenter(ExchangeActivateHelpCenter help);

    int deleteHelpCenter(long id);

    int updateHelpCenterState(long id, String state);
}
