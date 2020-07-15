package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OperateLog;

import java.util.List;

/**
 * @author MXG
 * create 2018/06/29
 */
public interface OperateLogService {

    int saveMerStatusChangeToLog(OperateLog operateLog);

    /**
     * 查找商户状态修改日志
     * @param merchantNo
     * @return
     */
    List<OperateLog> queryByOperateCodeAndBeOperator(String operateCode, String merchantNo);

    /**
     * 查询单个用户的左右状态修改日志
     * @param page
     * @param merchantNo
     */
    List<OperateLog> queryAllMerStatusLog(Page<OperateLog> page, String operateCode, String merchantNo);
}
