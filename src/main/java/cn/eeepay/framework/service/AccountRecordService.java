package cn.eeepay.framework.service;

import cn.eeepay.framework.model.CollectiveTransOrder;

/**
 * @author tans
 * @date 2019/2/27 17:13
 */
public interface AccountRecordService {

    /**
     * 交易调账户系统记账
     */
    void accountRecordTask();

    /**
     * 处理单个记账
     * @param info
     * 返回0 记账失败，1 欢乐返首笔激活记账成功，2 账户记账成功
     */
    int accountRecordInfo(CollectiveTransOrder info);

}
