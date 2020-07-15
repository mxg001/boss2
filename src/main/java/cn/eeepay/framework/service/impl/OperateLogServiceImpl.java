package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.OperateLogDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OperateLog;
import cn.eeepay.framework.service.OperateLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author MXG
 * create 2018/06/29
 */
@Service("operateLogService")
public class OperateLogServiceImpl implements OperateLogService  {

    @Resource
    private OperateLogDao operateLogDao;

    @Override
    public int saveMerStatusChangeToLog(OperateLog operateLog) {
        return operateLogDao.save(operateLog);
    }

    @Override
    public List<OperateLog> queryByOperateCodeAndBeOperator(String operateCode, String merchantNo) {
        return operateLogDao.queryByOperateCodeAndBeOperator(operateCode, merchantNo);
    }

    @Override
    public List<OperateLog> queryAllMerStatusLog(Page<OperateLog> page, String operateCode, String merchantNo) {
        return operateLogDao.queryAllMerStatusLog(page, operateCode, merchantNo);
    }
}
