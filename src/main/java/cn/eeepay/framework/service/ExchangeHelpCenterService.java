package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.HelpCenter;

import java.util.List;

/**
 * Created by Administrator on 2018/5/11/011.
 * @author  liuks
 * 帮助中心service
 */
public interface ExchangeHelpCenterService {

    List<HelpCenter> selectAllList(HelpCenter help, Page<HelpCenter> page);

    int addHelpCenter(HelpCenter help);

    HelpCenter getHelpCenter(long id);

    int updateHelpCenter(HelpCenter help);

    int deleteHelpCenter(long id);

    int updateHelpCenterState(long id, String state);
}
