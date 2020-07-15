package cn.eeepay.framework.daoAllAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.MachineBuyOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface MachineBuyOrderDao {

    @SelectProvider(type=SqlProvider.class,method="queryMachineBuyOrderList")
    @ResultType(MachineBuyOrder.class)
    List<MachineBuyOrder> queryMachineBuyOrderList(@Param("info") MachineBuyOrder info, @Param("page") Page<MachineBuyOrder> page);

    @Select("select * from pa_order where order_no = #{orderNo}")
    @ResultType(MachineBuyOrder.class)
    MachineBuyOrder queryMachineBuyOrderByOrderNo(@Param("orderNo") String orderNo);

    @SelectProvider(type=SqlProvider.class,method="queryMachineBuyOrderCount")
    @ResultType(Map.class)
    Map<String,Object> queryMachineBuyOrderCount(@Param("info") MachineBuyOrder info);

    @Select("select po.*,pau.agent_no from pa_order po " +
            "LEFT JOIN pa_agent_user pau on pau.user_code=po.poster_code " +
            "where po.entry_status=0 and po.order_status=2 and po.pre_entry_status=1 " +
            "and po.is_platform!=1 and po.trans_status=1 and po.create_time like concat(#{time},'%') ")
    @ResultType(MachineBuyOrder.class)
    List<MachineBuyOrder> queryMachineBuyOrderByTime(@Param("time") String time);

    @UpdateProvider(type=SqlProvider.class,method="updateMachineBuyOrderAccountEntry")
    int updateMachineBuyOrderAccountEntry(@Param("info") MachineBuyOrder info);

    class SqlProvider{

        public String queryMachineBuyOrderList(final Map<String, Object> param) {
            final MachineBuyOrder info = (MachineBuyOrder) param.get("info");
            SQL sql = new SQL(){{
                SELECT("po.*,u.real_name,u.parent_id,u2.agent_no,u2.real_name one_user_name," +
                        "pb.brand_name,pg.img,psd.share_amount,psd.acc_status,psd.acc_time,pas.status after_sale_status");
                FROM("pa_order po");
                LEFT_OUTER_JOIN("pa_user_info u on u.user_code=po.user_code");
                LEFT_OUTER_JOIN("pa_user_info u2 on u2.user_code=po.one_user_code");
                LEFT_OUTER_JOIN("pa_brand pb on pb.brand_code=po.brand_code");
                LEFT_OUTER_JOIN("pa_goods pg on pg.goods_code=po.good_code");
                LEFT_OUTER_JOIN("pa_share_detail psd on psd.trans_no=po.order_no");
                LEFT_OUTER_JOIN("pa_after_sale pas on pas.id=po.after_sale_id");
            }};
            where(sql, info);
            sql.ORDER_BY("po.create_time DESC");
            return sql.toString();
        }

        public String queryMachineBuyOrderCount(final Map<String, Object> param) {
            final MachineBuyOrder info = (MachineBuyOrder) param.get("info");
            SQL sql = new SQL(){{
                SELECT("sum(if(po.order_status=2,1,0)) receivedCount,sum(if(po.order_status=2,po.total_amount,0)) receivedAmountCount," +
                        "sum(if(po.order_status=0,1,0)) waitSendCount,sum(if(po.order_status=0,po.total_amount,0)) waitSendAmountCount," +
                        "sum(if(po.entry_status=1,po.entry_amount,0)) entryAmountCount,sum(if(po.entry_status=0,po.goods_total,0)) goodsTotalCount," +
                        "sum(if(psd.acc_status='ENTERACCOUNTED',psd.share_amount,0)) shareAmountCount,sum(if(psd.acc_status='NOENTERACCOUNT',psd.share_amount,0)) shareNoAmountCount," +
                        "sum(if(po.order_status=2 and po.ship_way=1,po.num,0)) snCount");
                FROM("pa_order po");
                LEFT_OUTER_JOIN("pa_user_info u on u.user_code=po.user_code");
                LEFT_OUTER_JOIN("pa_user_info u2 on u2.user_code=po.one_user_code");
                LEFT_OUTER_JOIN("pa_brand pb on pb.brand_code=po.brand_code");
                LEFT_OUTER_JOIN("pa_goods pg on pg.goods_code=po.good_code");
                LEFT_OUTER_JOIN("pa_share_detail psd on psd.trans_no=po.order_no");
                LEFT_OUTER_JOIN("pa_after_sale pas on pas.pay_order=po.order_no");
            }};
            where(sql, info);
            return sql.toString();
        }

        public void where(SQL sql, MachineBuyOrder info) {
            if(StringUtils.isNotBlank(info.getOrderNo())){
                sql.WHERE("po.order_no = #{info.orderNo} ");
            }
            if(StringUtils.isNotBlank(info.getRealName())){
                sql.WHERE("u.real_name like concat('%',#{info.realName},'%') ");
            }
            if(StringUtils.isNotBlank(info.getUserCode())){
                sql.WHERE("po.user_code = #{info.userCode} ");
            }
            if(StringUtils.isNotBlank(info.getOneUserCode())){
                sql.WHERE("po.one_user_code = #{info.oneUserCode} ");
            }
            if(StringUtils.isNotBlank(info.getOneUserName())){
                sql.WHERE("u2.real_name like concat('%',#{info.oneUserName},'%') ");
            }
            if(info.getOrderStatus()!=null){
                sql.WHERE("po.order_status = #{info.orderStatus} ");
            }
            if(StringUtils.isNotBlank(info.getTransChannel())){
                sql.WHERE("po.trans_channel  = #{info.transChannel} ");
            }
            if(StringUtils.isNotBlank(info.getBrandCode())){
                sql.WHERE("po.brand_code  = #{info.brandCode} ");
            }
            if(StringUtils.isNotBlank(info.getTransNo())){
                sql.WHERE("po.trans_no  = #{info.transNo} ");
            }
            if(StringUtils.isNotBlank(info.getEntryStatus())){
                sql.WHERE("po.entry_status  = #{info.entryStatus} ");
            }
            if(StringUtils.isNotBlank(info.getAccStatus())){
                sql.WHERE("psd.acc_status  = #{info.accStatus} ");
            }
            if(info.getIsPlatform()!=null){
                sql.WHERE("po.is_platform  = #{info.isPlatform} ");
            }
            if(StringUtils.isNotBlank(info.getAgentNo())){
                sql.WHERE("u2.agent_no  = #{info.agentNo} ");
            }
            if(StringUtils.isNotBlank(info.getStartTime())){
                sql.WHERE("po.create_time  >= #{info.startTime} ");
            }
            if(StringUtils.isNotBlank(info.getEndTime())){
                sql.WHERE("po.create_time  <= #{info.endTime} ");
            }
            if(StringUtils.isNotBlank(info.getTransStartTime())){
                sql.WHERE("po.trans_time  >= #{info.transStartTime} ");
            }
            if(StringUtils.isNotBlank(info.getTransEndTime())){
                sql.WHERE("po.trans_time  <= #{info.transEndTime} ");
            }
            if(StringUtils.isNotBlank(info.getSendStartTime())){
                sql.WHERE("po.send_time  >= #{info.sendStartTime} ");
            }
            if(StringUtils.isNotBlank(info.getSendEndTime())){
                sql.WHERE("po.send_time  <= #{info.sendEndTime} ");
            }
            if(StringUtils.isNotBlank(info.getSendType())){
                sql.WHERE("po.send_type  = #{info.sendType} ");
            }
            if(StringUtils.isNotBlank(info.getParentId())){
                sql.WHERE("u.parent_id  = #{info.parentId} ");
            }
            if(StringUtils.isNotBlank(info.getEntryStartTime())){
                sql.WHERE("po.entry_time  >= #{info.entryStartTime} ");
            }
            if(StringUtils.isNotBlank(info.getEntryEndTime())){
                sql.WHERE("po.entry_time  <= #{info.entryEndTime} ");
            }
            if(StringUtils.isNotBlank(info.getShareStartTime())){
                sql.WHERE("psd.acc_time  >= #{info.shareStartTime} ");
            }
            if(StringUtils.isNotBlank(info.getShareEndTime())){
                sql.WHERE("psd.acc_time  <= #{info.shareEndTime} ");
            }
            if(info.getAfterSaleStatus()!=null){
                sql.WHERE("pas.status  = #{info.afterSaleStatus} ");
            }
            if(info.getTransStatus()!=null){
                sql.WHERE("po.trans_status  = #{info.transStatus} ");
            }
            if(info.getShipWay()!=null){
                sql.WHERE("po.ship_way  = #{info.shipWay} ");
            }
        }

        public String updateMachineBuyOrderAccountEntry(final Map<String, Object> param) {
            final MachineBuyOrder info = (MachineBuyOrder) param.get("info");
            SQL sql = new SQL(){{
                UPDATE("pa_order");
                SET("entry_time=NOW(),entry_user=#{info.entryUser},entry_remark=#{info.entryRemark}");
                if(StringUtils.isNotBlank(info.getEntryStatus())){
                    SET("entry_status=#{info.entryStatus}");
                }
                if(info.getEntryAmount()!=null){
                    SET("entry_amount=#{info.entryAmount}");
                }
                WHERE("id=#{info.id}");
            }};
            return sql.toString();
        }
    }
}
