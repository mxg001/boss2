package cn.eeepay.framework.service.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateNotice;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * 公告service
 */
public interface ExchangeActivateNoticeService {

    List<ExchangeActivateNotice> selectAllList(ExchangeActivateNotice notice, Page<ExchangeActivateNotice> page);

    int addNotice(ExchangeActivateNotice notice);

    ExchangeActivateNotice getNotice(long id);

    int updateNotice(ExchangeActivateNotice notice);

    int updateNoticeState(long id, int state);
}
