package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.ChangeLogDao;
import cn.eeepay.framework.model.ChangeLog;
import cn.eeepay.framework.service.ChangeLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("changeLogService")
public class ChangeLogServiceImpl implements ChangeLogService {

    @Resource
    private ChangeLogDao changeLogDao;

    @Override
    public int insertChangeLog(ChangeLog changeLog){
        return changeLogDao.insertChangeLog(changeLog);
    }
}
