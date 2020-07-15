package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.OrderEventInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderEventDao {

    @Select(
            " select * from " +
                    "order_event where trans_order_no=#{orderNo} and trans_source=#{transSource}"
    )
    List<OrderEventInfo> getOrderEventListJY(@Param("orderNo") String orderNo,@Param("transSource") String transSource);

    @Select(
            " select oe.* from " +
                    "order_event oe" +
                    " inner join settle_transfer st on st.account_serial_no=oe.trans_order_no and st.status=4"+
                    " where st.order_no=#{orderNo} and oe.trans_source=#{transSource}"
    )
    List<OrderEventInfo> getOrderEventListDF(@Param("orderNo") String orderNo,@Param("transSource") String transSource);

}
