package cn.eeepay.framework.daoAllAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.AfterSaleOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface AfterSaleOrderDao {

    @SelectProvider(type=SqlProvider.class,method="queryAfterSaleOrderList")
    @ResultType(AfterSaleOrder.class)
    List<AfterSaleOrder> queryAfterSaleOrderList(@Param("info") AfterSaleOrder info,@Param("page") Page<AfterSaleOrder> page);

    @SelectProvider(type=SqlProvider.class,method="queryAfterSaleOrderCount")
    @ResultType(Map.class)
    Map<String, Object> queryAfterSaleOrderCount(@Param("info") AfterSaleOrder info);

    @Update("update pa_after_sale set deal_desc=#{info.dealDesc},deal_img=#{info.dealImg},status=2,handler=2,deal_time=now() where id=#{info.id}")
    int updateProcessAfterSaleOrder(@Param("info") AfterSaleOrder info);

    class SqlProvider{

        public String queryAfterSaleOrderList(final Map<String, Object> param) {
            final AfterSaleOrder info = (AfterSaleOrder) param.get("info");
            SQL sql = new SQL(){{
                SELECT("pas.*,po.ship_way");
                FROM("pa_after_sale pas");
                LEFT_OUTER_JOIN("pa_order po on po.order_no=pas.pay_order");
            }};
            where(sql, info);
            sql.ORDER_BY("pas.apply_time DESC");
            return sql.toString();
        }

        public String queryAfterSaleOrderCount(final Map<String, Object> param) {
            final AfterSaleOrder info = (AfterSaleOrder) param.get("info");
            SQL sql = new SQL(){{
                SELECT("sum(if(pas.status=1,1,0)) waitPlatformCount,sum(if(pas.status=0,1,0)) waitAgencyCount," +
                        "sum(if(pas.status=0 and TO_DAYS(NOW())- TO_DAYS(pas.apply_time) > 3 and TO_DAYS(NOW())- TO_DAYS(pas.apply_time) <= 7,1,0)) agencyMoreThreeCount," +
                        "sum(if(pas.status=0 and TO_DAYS(NOW())- TO_DAYS(pas.apply_time) > 7,1,0)) agencyMoreSevenCount");
                FROM("pa_after_sale pas");
                LEFT_OUTER_JOIN("pa_order po on po.order_no=pas.pay_order");
            }};
            where(sql, info);
            return sql.toString();
        }

        public void where(SQL sql, AfterSaleOrder info) {
            if(StringUtils.isNotBlank(info.getOrderNo())){
                sql.WHERE("pas.order_no = #{info.orderNo} ");
            }
            if(StringUtils.isNotBlank(info.getPayOrder())){
                sql.WHERE("pas.pay_order = #{info.payOrder} ");
            }
            if(info.getStatus()!=null){
                sql.WHERE("pas.status = #{info.status} ");
            }
            if(info.getAscription()!=null){
                sql.WHERE("pas.ascription = #{info.ascription} ");
            }
            if(info.getHandler()!=null){
                sql.WHERE("pas.handler = #{info.handler} ");
            }
            if(StringUtils.isNotBlank(info.getSaleType())){
                sql.WHERE("pas.sale_type = #{info.saleType} ");
            }
            if(StringUtils.isNotBlank(info.getApplyStartTime())){
                sql.WHERE("pas.apply_time  >= #{info.applyStartTime} ");
            }
            if(StringUtils.isNotBlank(info.getApplyEndTime())){
                sql.WHERE("pas.apply_time  <= #{info.applyEndTime} ");
            }
            if(StringUtils.isNotBlank(info.getDealStartTime())){
                sql.WHERE("pas.deal_time  >= #{info.dealStartTime} ");
            }
            if(StringUtils.isNotBlank(info.getDealEndTime())){
                sql.WHERE("pas.deal_time  <= #{info.dealEndTime} ");
            }
            if(info.getShipWay()!=null){
                sql.WHERE("po.ship_way = #{info.shipWay} ");
            }
        }
    }
}
