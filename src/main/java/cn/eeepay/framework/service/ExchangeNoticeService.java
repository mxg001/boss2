package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ExchangeNotice;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * 公告service
 */
public interface ExchangeNoticeService {

    List<ExchangeNotice> selectAllList(ExchangeNotice notice, Page<ExchangeNotice> page);

    int addNotice(ExchangeNotice notice);

    ExchangeNotice getNotice(long id);

    int updateNotice(ExchangeNotice notice);

    int updateNoticeState(long id, int state);
}
