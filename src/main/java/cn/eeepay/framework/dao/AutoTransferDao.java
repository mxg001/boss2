package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.CollectiveTransOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AutoTransferDao {

    @Update(" UPDATE auto_transfer SET `status`='3' WHERE trans_order_no=#{orderNo} AND `status`=#{status}")
    int updateByOrderNoAndStatus(@Param("orderNo") String orderNo, @Param("status")String status);

    @Insert(" INSERT INTO auto_transfer(`trans_order_no`,`merchant_no`,`busi_type`,`settle_type`,`status`,`create_person`)" +
            " VALUES(#{orderNo},#{merchantNo},#{busiType},#{SettleType},#{status},#{createPerson})")
    int insertOne(@Param("orderNo") String orderNo, @Param("merchantNo")String merchantNo,  @Param("busiType") String busiType, @Param("SettleType")String SettleType,@Param("status")String status, @Param("createPerson")String createPerson);

    @Select(" SELECT id FROM auto_transfer WHERE trans_order_no=#{orderNo} AND STATUS=#{status}")
    String selectByOrderNoAndStatus(@Param("orderNo") String orderNo, @Param("status")String status);

    @Update(" UPDATE auto_transfer SET `status`='3' WHERE merchant_no=#{merchantNo} AND `status`=#{status}")
    int updateByMerchantNoAndStatus(@Param("merchantNo") String merchantNo, @Param("status")String status);

    @Select("select count(id) from sys_calendar WHERE sys_date=#{sysDate} and status=1")
    int getHolidayFlag(@Param("sysDate")String sysDate);

    @Select("SELECT order_no " +
            "FROM collective_trans_order " +
            "WHERE settlement_method='1' AND trans_status='SUCCESS' AND freeze_status='0' AND trans_type='PURCHASE' " +
            "AND account=1 " +
            "AND (settle_status=0 OR settle_status IS NULL OR settle_status='3' OR settle_status='4') " +
            "AND acq_enname='YS_ZQ' " +
            "AND trans_time >= DATE_SUB(CURDATE(), INTERVAL #{autoTransferLastDay} DAY) AND trans_time < DATE_SUB(CURDATE(), INTERVAL 1 HOUR)")
    List<String> selectT1Orders(@Param("autoTransferLastDay") int autoTransferLastDay);

    @Select("SELECT order_no " +
            "FROM collective_trans_order " +
            "WHERE settlement_method='0' AND trans_status='SUCCESS' AND freeze_status='0' AND trans_type='PURCHASE' " +
            "AND account=1 " +
            "AND settle_status='4' " +
            "AND acq_enname='YS_ZQ' " +
            "AND trans_time >= DATE_SUB(CURDATE(), INTERVAL #{autoTransferLastDay} DAY) AND trans_time < DATE_SUB(CURDATE(), INTERVAL 1 HOUR)")
    List<String> selectToT1Orders(@Param("autoTransferLastDay") int autoTransferLastDay);

    @Select("SELECT order_no,merchant_no,trans_status,trans_time,settlement_method,settle_status,freeze_status,trans_type,trans_msg " +
            "FROM collective_trans_order " +
            "WHERE settlement_method='0' AND trans_status='SUCCESS' AND freeze_status='0' AND trans_type='PURCHASE' " +
            "AND account=1 " +
            "AND (settle_status=0 OR settle_status IS NULL OR settle_status='3') " +
            "AND order_no=#{orderNo} " +
            "AND trans_time < NOW()-INTERVAL #{reSettleTaskCount} SECOND")
    @ResultType(CollectiveTransOrder.class)
    CollectiveTransOrder selectT0ByOrderNoAndTransTime(@Param("orderNo") String orderNo, @Param("reSettleTaskCount") int reSettleTaskCount);

    @Select("SELECT order_no,merchant_no,trans_status,trans_time,settlement_method,settle_status,freeze_status,trans_type,trans_msg " +
            "FROM collective_trans_order " +
            "WHERE settlement_method='1' AND trans_status='SUCCESS' AND freeze_status='0' AND trans_type='PURCHASE' " +
            "AND account=1 " +
            "AND (settle_status=0 OR settle_status IS NULL OR settle_status='3' OR settle_status='4') " +
            "AND order_no=#{orderNo} " +
            "AND trans_time < DATE_SUB(CURDATE(), INTERVAL #{autoTransferLastDay} DAY)")
    @ResultType(CollectiveTransOrder.class)
    CollectiveTransOrder selectT1ByOrderNoAndTransTime(@Param("orderNo") String orderNo, @Param("autoTransferLastDay")int autoTransferLastDay);

    @Select("SELECT order_no,merchant_no,trans_status,trans_time,settlement_method,settle_status,freeze_status,trans_type,trans_msg " +
            "FROM collective_trans_order WHERE settlement_method='0' AND trans_status='SUCCESS' AND freeze_status='0' AND trans_type='PURCHASE' " +
            "AND account=1 " +
            "AND settle_status='4' " +
            "AND order_no =#{orderNo} " +
            "AND trans_time < DATE_SUB(CURDATE(), INTERVAL #{autoTransferLastDay} DAY)")
    @ResultType(CollectiveTransOrder.class)
    CollectiveTransOrder selectToT1ByOrderNoAndTransTime(@Param("orderNo") String orderNo, @Param("autoTransferLastDay") int autoTransferLastDay);
}
