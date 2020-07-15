package cn.eeepay.framework.service;

import cn.eeepay.framework.model.CollectiveTransOrder;
import org.apache.ibatis.annotations.Update;

public interface AutoTransferService {

    int updateByOrderNoAndStatus(String orderNo, String status);

    int insertOne(String orderNo, String merchantNo,String busiType, String SettleType, String status, String createPerson);

    String selectByOrderNoAndStatus(String orderNo, String s);

    int updateByMerchantNoAndStatus(String rollNo, String s);

    void t1AutoTransfer();

    CollectiveTransOrder selectT0ByOrderNoAndTransTime(String orderNo, int reSettleTaskCount);

    CollectiveTransOrder selectT1ByOrderNoAndTransTime(String orderNo, int autoTransferLastDay);

    CollectiveTransOrder selectToT1ByOrderNoAndTransTime(String orderNo, int autoTransferLastDay);
}
