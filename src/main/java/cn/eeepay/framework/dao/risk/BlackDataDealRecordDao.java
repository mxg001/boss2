package cn.eeepay.framework.dao.risk;

import cn.eeepay.framework.model.risk.DealRecord;
import org.apache.ibatis.annotations.*;

/**
 * @author MXG
 * create 2018/12/25
 */
public interface BlackDataDealRecordDao {

    @Select("select *,date_format(create_time,'%Y-%m-%d %H:%i:%s') as createTime " +
            "from black_data_deal_record " +
            "where order_no=#{orderNo} and status <> '0' " +
            "order by create_time DESC limit 1")
    @ResultType(DealRecord.class)
    DealRecord selectByOrder(@Param("orderNo") String orderNo);

    @Insert("insert into black_data_deal_record(order_no,orig_order_no,risk_deal_template_no,risk_deal_msg,status,creater,create_time) " +
            "values(#{record.orderNo},#{record.origOrderNo},#{record.riskDealTemplateNo}," +
            "#{record.riskDealMsg},#{record.status},#{record.creater},now())")
    int insert(@Param("record") DealRecord record);

    @Select("select *,date_format(create_time,'%Y-%m-%d %H:%i:%s') as createTime " +
            "from black_data_deal_record " +
            "where orig_order_no=#{origOrderNo} and status=#{status} " +
            "order by create_time DESC limit 1")
    @ResultType(DealRecord.class)
    DealRecord selectByOrigOrderNoAndStatus(@Param("origOrderNo") String origOrderNo, @Param("status") String status);

    @Update("update black_data_deal_record set status=#{status} where order_no=#{orderNo}")
    int updateStatusByOrderNo(@Param("orderNo") String orderNo, @Param("status") String status);
}
