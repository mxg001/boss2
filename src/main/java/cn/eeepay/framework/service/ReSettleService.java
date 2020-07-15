package cn.eeepay.framework.service;

import cn.eeepay.framework.model.CollectiveTransOrder;

import java.util.Date;
import java.util.List;

/**
 * @author tans
 * @date 2019/1/10 15:06
 */
public interface ReSettleService {

    void reSettle();

    List<CollectiveTransOrder> getUnSettle(List<String> channelNames, Date startDate, Date endDate, Integer limitNumbers);


}
