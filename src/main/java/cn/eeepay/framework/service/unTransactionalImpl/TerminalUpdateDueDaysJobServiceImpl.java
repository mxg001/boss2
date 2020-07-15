package cn.eeepay.framework.service.unTransactionalImpl;

import cn.eeepay.framework.dao.TerminalInfoDao;
import cn.eeepay.framework.service.TerminalUpdateDueDaysJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TerminalUpdateDueDaysJobServiceImpl implements TerminalUpdateDueDaysJobService {

    @Resource
    private TerminalInfoDao terminalInfoDao;

    private Logger log = LoggerFactory.getLogger(TerminalUpdateDueDaysJobServiceImpl.class);

    @Override
    public void terminalChangeDueDaysJob() {
        //更新所有未达标和考核中的机具的剩余天数
        log.info("批量更新机具剩余天数开始");
        int num = terminalInfoDao.updateDueDaysBatch();
        log.info("批量更新机具剩余天数结束，一共更新了" + num + "条记录");
    }
}
