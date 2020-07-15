package cn.eeepay.framework.daoExchange.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchange.WriteOffHis;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/17/017.
 * @author  liuks
 * 兑换订单 dao
 */
public interface ExchangeActivateOrderDao {

    //兑换订单查询
    @SelectProvider(type=ExchangeActivateOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeActivateOrder.class)
    List<ExchangeActivateOrder> selectAllList(@Param("order") ExchangeActivateOrder order, @Param("page") Page<ExchangeActivateOrder> page);

    //兑换订单统计
    @SelectProvider(type=ExchangeActivateOrderDao.SqlProvider.class,method="selectSum")
    @ResultType(TotalAmount.class)
    TotalAmount selectSum(@Param("order") ExchangeActivateOrder order, @Param("page") Page<ExchangeActivateOrder> page);

    //兑换订单详情
    @Select(
            "select ord.*," +
                    " decOrd.price,decOrd.receive_status,decOrd.check_status,decOrd.check_oper," +
                    " decOrd.check_time,decOrd.check_reason,decOrd.origin_price, " +
                    " decOrd.channel,decOrd.sale_order_no,decOrd.write_off_price,decOrd.check_status_one, " +
                    " decOrd.channel_check_status,decOrd.channel_check_reason,decOrd.channel_check_time,"+
                    " decOrd.platform_fee,decOrd.share_rate,"+
                    " mer.user_name,mer.mobile_username,mer.agent_no,mer.one_agent_no,mer.real_name,mer.id_card_no, " +
                    " product.product_name,product.id productId,product.original_price,type.type_code,type.type_name," +
                    " org.org_code,org.org_name,oem.oem_name,oem.oem_no " +
                    " from yfb_order ord " +
                    " LEFT JOIN yfb_declare_order decOrd ON decOrd.order_no=ord.order_no " +
                    " LEFT JOIN yfb_product_info product ON product.id=decOrd.p_id " +
                    " LEFT JOIN yfb_org_info org ON org.org_code=decOrd.org_code " +
                    " LEFT JOIN yfb_product_type type ON type.type_code=decOrd.type_code " +
                    " LEFT JOIN yfb_merchant_info  mer ON mer.merchant_no=ord.mer_no " +
                    " LEFT JOIN yfb_oem_service oem ON oem.oem_no=mer.oem_no " +
                    " where ord.order_type ='D' and ord.id=#{id}"
    )
    ExchangeActivateOrder getExchangeOrder(@Param("id") long id);

    //兑换订单导出查询
    @SelectProvider(type=ExchangeActivateOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeActivateOrder.class)
    List<ExchangeActivateOrder> importDetailSelect(@Param("order") ExchangeActivateOrder order);

    //核销列表查询
    @SelectProvider(type=ExchangeActivateOrderDao.SqlProvider.class,method="selectAuditAll")
    @ResultType(ExchangeActivateOrder.class)
    List<ExchangeActivateOrder> selectAuditAll(@Param("order") ExchangeActivateOrder order, @Param("page") Page<ExchangeActivateOrder> page);

    //核销导出查询
    @SelectProvider(type=ExchangeActivateOrderDao.SqlProvider.class,method="selectAuditAll")
    @ResultType(ExchangeActivateOrder.class)
    List<ExchangeActivateOrder> importAuditDetailSelect(@Param("order") ExchangeActivateOrder order);

    //核销统计
    @SelectProvider(type=ExchangeActivateOrderDao.SqlProvider.class,method="selectAuditSum")
    @ResultType(TotalAmount.class)
    TotalAmount selectAuditSum(@Param("order") ExchangeActivateOrder order, @Param("page") Page<ExchangeActivateOrder> page);

    //核销详情
    @Select(
            " select  ord.*,org.org_code,org.org_name,type.type_code,type.type_name,product.id productId," +
                    " product.product_name,product.original_price, " +
                    " decOrd.upload_image,decOrd.redeem_code,decOrd.validity_date_start,decOrd.validity_date_end," +
                    " decOrd.remark productRemark,decOrd.price,decOrd.receive_status,decOrd.logistics_info," +
                    " decOrd.check_status,decOrd.check_oper,decOrd.check_time,decOrd.check_reason,decOrd.origin_price, " +
                    " decOrd.channel,decOrd.sale_order_no,decOrd.write_off_price,decOrd.check_status_one, " +
                    " decOrd.channel_check_status,decOrd.channel_check_reason,decOrd.channel_check_time,"+
                    " decOrd.platform_fee,decOrd.share_rate,"+
                    " mer.user_name,mer.mobile_username,mer.agent_no,mer.one_agent_no,mer.real_name,mer.id_card_no, " +
                    " oem.oem_no,oem.oem_name " +
                    " from yfb_order ord " +
                    " LEFT JOIN yfb_declare_order decOrd ON decOrd.order_no=ord.order_no " +
                    " LEFT JOIN yfb_org_info org ON org.org_code=decOrd.org_code " +
                    " LEFT JOIN yfb_product_type type ON type.type_code=decOrd.type_code " +
                    " LEFT JOIN yfb_product_info product ON product.id=decOrd.p_id " +
                    " LEFT JOIN yfb_merchant_info  mer ON mer.merchant_no=ord.mer_no " +
                    " LEFT JOIN yfb_oem_service oem ON oem.oem_no=mer.oem_no " +
                    " where ord.order_type ='D' and ord.id=#{id}"
    )
    ExchangeActivateOrder getAuditExchangeOrder(@Param("id") long id);


    //一次核销
    @Update(
            "update yfb_declare_order set " +
                    " check_status='0', "+
                    " check_status_one=#{wo.checkStatus},receive_status=#{wo.receiveStatus}, " +
                    " check_oper=#{wo.checkOper},check_time=#{wo.createTime},check_reason=#{wo.remark}, " +
                    " channel=#{wo.channel},sale_order_no=#{wo.saleOrderNo},write_off_price=#{wo.writeOffPrice} " +
                    " where order_no=#{wo.orderNo} "
    )
    int writeOffOneTrue(@Param("wo") WriteOffHis writeOff);

    //一次核销
    @Update(
            "update yfb_declare_order set " +
                    " check_status=#{wo.checkStatus}, "+
                    " check_status_one=#{wo.checkStatus},receive_status=#{wo.receiveStatus}," +
                    " check_oper=#{wo.checkOper},check_time=#{wo.createTime},check_reason=#{wo.remark}," +
                    " channel=#{wo.channel},sale_order_no=#{wo.saleOrderNo},write_off_price=#{wo.writeOffPrice} " +
                    " where order_no=#{wo.orderNo} "
    )
    int writeOffOneFalse(@Param("wo") WriteOffHis writeOff);

    //二次核销
    @Update(
            "update yfb_declare_order set " +
                    " check_status=#{wo.checkStatus}, "+
                    " receive_status=#{wo.receiveStatus}," +
                    " check_oper=#{wo.checkOper},check_time=#{wo.createTime},check_reason=#{wo.remark}," +
                    " channel=#{wo.channel},sale_order_no=#{wo.saleOrderNo},write_off_price=#{wo.writeOffPrice} " +
                    " where order_no=#{wo.orderNo} "
    )
    int writeOffTwoTrue(@Param("wo") WriteOffHis writeOff);

    //二次核销
    @Update(
            "update yfb_declare_order set " +
                    " check_status=#{wo.checkStatus}, "+
                    " check_status_one='0',receive_status=#{wo.receiveStatus}," +
                    " check_oper=#{wo.checkOper},check_time=#{wo.createTime},check_reason=#{wo.remark}," +
                    " channel=#{wo.channel},sale_order_no=#{wo.saleOrderNo},write_off_price=#{wo.writeOffPrice} " +
                    " where order_no=#{wo.orderNo} "
    )
    int writeOffTwoFalse(@Param("wo") WriteOffHis writeOff);

    @Update(
            "update yfb_declare_order set " +
                    " check_status=#{wo.checkStatus}, "+
                    " check_status_one=#{wo.checkStatus},receive_status=#{wo.receiveStatus}," +
                    " check_oper=#{wo.checkOper},check_time=#{wo.createTime},check_reason=#{wo.remark}," +
                    " channel=#{wo.channel},sale_order_no=#{wo.saleOrderNo},write_off_price=#{wo.writeOffPrice} " +
                    " where order_no=#{wo.orderNo} "
    )
    int writeOffTwoImport(@Param("wo") WriteOffHis writeOff);

    @Select(
            "select * from yfb_declare_order where order_no =#{orderNo}"
    )
    ExchangeActivateOrder selectSaveExchangeOrder(@Param("orderNo") String orderNo);

    @Select(
            "select * from yfb_declare_order " +
                    " where mer_no=#{merNo} and order_status='SUCCESS'" +
                    " and create_time>=#{startDay} and create_time<=#{endDay}" +
                    " and p_id=#{pId}"
    )
    List<ExchangeActivateOrder> ckeckExchangeOrderNum(@Param("merNo") String merNo,
                                              @Param("startDay") Date startDay,
                                              @Param("endDay") Date endDay,
                                              @Param("pId") long pId);

    @Select(
            " select * from yfb_declare_order_his where order_no=#{orderNo} ORDER BY create_time desc limit 0,10 "
    )
    List<WriteOffHis> getWriteOffList(@Param("orderNo") String orderNo);

    //新增核销记录
    @Insert(
            "INSERT INTO yfb_declare_order_his " +
                    " (order_no,check_oper,create_time,remark,check_mode,check_status,channel,sale_order_no) " +
                    " VALUES " +
                    " (#{wo.orderNo},#{wo.checkOper},#{wo.createTime},#{wo.remark},#{wo.checkMode},#{wo.checkStatus}," +
                    "  #{wo.channel},#{wo.saleOrderNo}) "
    )
    int addwriteOffHis(@Param("wo") WriteOffHis writeOff);


    @UpdateProvider(type=ExchangeActivateOrderDao.SqlProvider.class,method="ckeckUpper")
    int ckeckUpper(@Param("order")ExchangeActivateOrder order);

    class SqlProvider{

        public String ckeckUpper(final Map<String, Object> param) {
            ExchangeActivateOrder order = (ExchangeActivateOrder) param.get("order");
            StringBuffer sb=new StringBuffer();
            sb.append(" update yfb_declare_order set ");
            sb.append("  channel_check_status=#{order.channelCheckStatus},channel_check_reason=#{order.channelCheckReason},");
            if(StringUtils.isNotBlank(order.getCheckStatus())){
                sb.append(" check_status_one=#{order.checkStatusOne},check_status=#{order.checkStatus}," +
                        "check_oper=#{order.checkOper},check_time=NOW(),");
            }
            if(StringUtils.isNotBlank(order.getCheckReason())){
                sb.append(" check_reason=#{order.checkReason},");
            }
            if(order.getWriteOffPrice()!=null){
                sb.append(" write_off_price=#{order.writeOffPrice},");
            }
            if(StringUtils.isNotBlank(order.getReceiveStatus())){
                sb.append(" receive_status=#{order.receiveStatus},");
            }
            sb.append(" channel_check_time=NOW()");
            sb.append(" where order_no=#{order.orderNo}");
            return sb.toString();
        }


        public String selectAllList(final Map<String, Object> param) {
            final ExchangeActivateOrder order = (ExchangeActivateOrder) param.get("order");
            return getSelectSql(order,1);
        }
        public String selectSum(final Map<String, Object> param) {
            final ExchangeActivateOrder order = (ExchangeActivateOrder) param.get("order");
            return getSelectSql(order,2);
        }
        public String selectAuditAll(final Map<String, Object> param) {
            final ExchangeActivateOrder order = (ExchangeActivateOrder) param.get("order");
            return getSelectSql(order,3);
        }
        public String selectAuditSum(final Map<String, Object> param) {
            final ExchangeActivateOrder order = (ExchangeActivateOrder) param.get("order");
            return getSelectSql(order,4);
        }
        private String getSelectSql(final ExchangeActivateOrder order, int state){
            StringBuffer sb=new StringBuffer();
            sb.append("select ");
            if(state==1){
                sb.append(" ord.*,org.org_code,org.org_name, ");
                sb.append(" decOrd.price,decOrd.check_status,decOrd.check_oper,decOrd.check_time,decOrd.check_reason,decOrd.origin_price, ");
                sb.append(" decOrd.channel,decOrd.sale_order_no,decOrd.write_off_price,decOrd.check_status_one, ");
                sb.append(" decOrd.channel_check_status,decOrd.channel_check_reason,decOrd.channel_check_time,");
                sb.append(" mer.user_name,mer.mobile_username,mer.agent_no,mer.one_agent_no, ");
                sb.append(" oem.oem_no,oem.oem_name,product.product_name,product.original_price,type.type_code,type.type_name,product.id productId ");
            }else if(state==2){
                sb.append(" SUM(ord.plate_share) plateShareTotal, ");
                sb.append(" SUM(ord.oem_share) oemShareTotal, ");
                sb.append(" SUM(decOrd.price) priceTotal, ");
                sb.append(" SUM(decOrd.write_off_price) writeOffPriceTotal ");
            }else if(state==3){
                sb.append(" ord.*,org.org_code,org.org_name,type.type_code,type.type_name,product.id productId,");
                sb.append(" product.product_name,product.original_price, ");
                sb.append(" decOrd.upload_image,decOrd.redeem_code,decOrd.validity_date_start,decOrd.validity_date_end,");
                sb.append(" decOrd.remark productRemark,decOrd.price,decOrd.check_status,decOrd.check_oper,decOrd.check_time,decOrd.check_reason,decOrd.origin_price, ");
                sb.append(" decOrd.channel,decOrd.sale_order_no,decOrd.write_off_price,decOrd.check_status_one, ");
                sb.append(" decOrd.channel_check_status,decOrd.channel_check_reason,decOrd.channel_check_time,");
                sb.append(" mer.user_name,mer.mobile_username,mer.agent_no,mer.one_agent_no,mer.id_card_no, ");
                sb.append(" oem.oem_no,oem.oem_name ");
            }else if(state==4){
                sb.append(" SUM(ord.plate_share) plateShareTotal,");
                sb.append(" SUM(ord.oem_share) oemShareTotal, ");
                sb.append(" SUM(decOrd.price) priceTotal, ");
                sb.append(" SUM(decOrd.write_off_price) writeOffPriceTotal ");
            }
            sb.append(" from yfb_order ord ");
            sb.append(" LEFT JOIN yfb_declare_order decOrd ON decOrd.order_no=ord.order_no");
            sb.append(" LEFT JOIN yfb_org_info org ON org.org_code=decOrd.org_code");
            sb.append(" LEFT JOIN yfb_product_type type ON type.type_code=decOrd.type_code");
            sb.append(" LEFT JOIN yfb_product_info product ON product.id=decOrd.p_id");
            sb.append(" LEFT JOIN yfb_merchant_info  mer ON mer.merchant_no=ord.mer_no");
            sb.append(" LEFT JOIN yfb_oem_service oem ON oem.oem_no=mer.oem_no");
            sb.append(" where ord.order_type ='D' ");
            if(StringUtils.isNotBlank(order.getOrderNo())){
                sb.append(" and ord.order_no = #{order.orderNo} ");
            }
            if(StringUtils.isNotBlank(order.getOrderStatus())){
                sb.append(" and ord.order_status = #{order.orderStatus} ");
            }
            if(StringUtils.isNotBlank(order.getOemNo())){
                sb.append(" and oem.oem_no = #{order.oemNo} ");
            }
            if(StringUtils.isNotBlank(order.getMerNo())){
                sb.append(" and ord.mer_no = #{order.merNo} ");
            }
            if(StringUtils.isNotBlank(order.getUserName())){
                sb.append(" and mer.user_name like concat(#{order.userName},'%') ");
            }
            if(StringUtils.isNotBlank(order.getMobileUsername())){
                sb.append(" and mer.mobile_username  = #{order.mobileUsername}");
            }
            if(StringUtils.isNotBlank(order.getIdCardNo())){
                sb.append(" and mer.id_card_no  = #{order.idCardNo}");
            }
            if(StringUtils.isNotBlank(order.getOrgCode())){
                sb.append(" and decOrd.org_code = #{order.orgCode} ");
            }
            if(StringUtils.isNotBlank(order.getAccStatus())){
                sb.append(" and ord.acc_status = #{order.accStatus} ");
            }
            if(StringUtils.isNotBlank(order.getCheckStatus())){
                sb.append(" and decOrd.check_status = #{order.checkStatus} ");
            }
            if(StringUtils.isNotBlank(order.getCheckStatusOne())){
                sb.append(" and decOrd.check_status_one = #{order.checkStatusOne} ");
            }
            if(StringUtils.isNotBlank(order.getCheckOper())){
                sb.append(" and decOrd.check_oper = #{order.checkOper} ");
            }
            if(order.getCreateTimeBegin() != null){
                sb.append(" and ord.create_time >= #{order.createTimeBegin}");
            }
            if(order.getCreateTimeEnd() != null){
                sb.append(" and ord.create_time <= #{order.createTimeEnd}");
            }
            if(order.getCheckTimeBegin() != null){
                sb.append(" and decOrd.check_time >= #{order.checkTimeBegin}");
            }
            if(order.getCheckTimeEnd() != null){
                sb.append(" and decOrd.check_time <= #{order.checkTimeEnd}");
            }
            if(order.getAccTimeBegin() != null){
                sb.append(" and ord.acc_time >= #{order.accTimeBegin}");
            }
            if(order.getAccTimeEnd() != null){
                sb.append(" and ord.acc_time <= #{order.accTimeEnd}");
            }
            if(StringUtils.isNotBlank(order.getChannelCheckStatus())){
                sb.append(" and decOrd.channel_check_status = #{order.channelCheckStatus} ");
            }
            if(StringUtils.isNotBlank(order.getChannel())){
                sb.append(" and decOrd.channel = #{order.channel} ");
            }
            if(order.getChannelCheckTimeBegin() != null){
                sb.append(" and decOrd.channel_check_time >= #{order.channelCheckTimeBegin}");
            }
            if(order.getChannelCheckTimeEnd() != null){
                sb.append(" and decOrd.channel_check_time <= #{order.channelCheckTimeEnd}");
            }
            if(StringUtils.isNotBlank(order.getSaleOrderNo())){
                sb.append(" and decOrd.sale_order_no = #{order.saleOrderNo} ");
            }
            sb.append(" ORDER BY ord.create_time DESC ");
            return sb.toString();
        }
    }
}
