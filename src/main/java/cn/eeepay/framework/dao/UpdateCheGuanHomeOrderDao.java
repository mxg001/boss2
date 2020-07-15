package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.CheGuanHomeOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author liuks
 * 车管家类的订单状态(当前时间的前一天00：00：00分到23:59:59)更新为不结算
 */
public interface UpdateCheGuanHomeOrderDao {


    @Update(
            "update collective_trans_order set settle_status=#{info.settleStatus}  " +
                    " where  merchant_no=#{info.merchantNo}" +
                    " and create_time>=#{info.createTimeBegin} and create_time<=#{info.createTimeEnd} " +
                    " and trans_status=#{info.transStatus}"
    )
    int updateCheGuanHomeOrder(@Param("info")CheGuanHomeOrder bean);
}
