package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.WarningEvents;

import java.util.List;

/**
 * Created by Administrator on 2018/1/10/010.
 * @author liuks
 * 预警事件service
 */
public interface WarningEventsService {

    List<WarningEvents> getWarningEventsAll(Page<WarningEvents> page, WarningEvents we);

    List<WarningEvents> exportAllInfo(WarningEvents we);

    int insertWarningEvents(WarningEvents we);

    List<WarningEvents> selectWarningEvents(WarningEvents we);

    List<WarningEvents> selectWarningEventsTask(WarningEvents weCondition);
}
