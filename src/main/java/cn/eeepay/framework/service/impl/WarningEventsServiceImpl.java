package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.WarningEventsDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.WarningEvents;
import cn.eeepay.framework.service.WarningEventsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/1/10/010.
 * @author liuks
 * 预警事件实现service类
 */
@Service("warningEventsService")
@Transactional
public class WarningEventsServiceImpl implements WarningEventsService{
    @Resource
    private WarningEventsDao warningEventsDao;

    @Override
    public List<WarningEvents> getWarningEventsAll(Page<WarningEvents> page, WarningEvents we) {
        return warningEventsDao.getWarningEventsAll(page,we);
    }

    @Override
    public List<WarningEvents> exportAllInfo(WarningEvents we) {
        return warningEventsDao.exportAllInfo(we);
    }

    @Override
    public int insertWarningEvents(WarningEvents we) {
        return warningEventsDao.insertWarningEvents(we);
    }

    @Override
    public List<WarningEvents> selectWarningEvents(WarningEvents we) {
        return warningEventsDao.selectWarningEvents(we);
    }

    @Override
    public List<WarningEvents> selectWarningEventsTask(WarningEvents we) {
        return warningEventsDao.selectWarningEventsTask(we);
    }
}
