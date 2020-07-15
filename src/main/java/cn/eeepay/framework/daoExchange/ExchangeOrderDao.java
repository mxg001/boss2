package cn.eeepay.framework.daoExchange;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ExchangeOrder;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchange.WriteOffHis;
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
public interface ExchangeOrderDao {

    //兑换订单查询
    @SelectProvider(type=ExchangeOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeOrder.class)
    List<ExchangeOrder> selectAllList(@Param("order")ExchangeOrder order, @Param("page")Page<ExchangeOrder> page);

    //兑换订单统计
    @SelectProvider(type=ExchangeOrderDao.SqlProvider.class,method="selectSum")
    @ResultType(TotalAmount.class)
    TotalAmount selectSum(@Param("order")ExchangeOrder order, @Param("page")Page<ExchangeOrder> page);

    //兑换订单详情
    @Select(
            "select ord.*,org.org_code,org.org_name,mer.user_name,mer.mobile_username, " +
                    " decOrd.price,decOrd.receive_status,decOrd.check_status,decOrd.check_oper," +
                    " decOrd.check_time,decOrd.check_reason, " +
                    " decOrd.channel,decOrd.sale_order_no,decOrd.write_off_price,decOrd.check_status_one, " +
                    " decOrd.channel_check_status,decOrd.channel_check_reason,decOrd.channel_check_time,"+
                    " decOrd.platform_fee,decOrd.share_rate,decOrd.mer_capa, " +
                    " oem.oem_name,oem.oem_no,card.business_code," +
                    " product.product_name,product.id productId,product.original_price,type.type_code,type.type_name," +
                    " type.type_code,type.type_name " +
                    " from rdmp_order ord " +
                    " LEFT JOIN rdmp_declare_order decOrd ON decOrd.order_no=ord.order_no " +
                    " LEFT JOIN rdmp_product_info product ON product.id=decOrd.p_id " +
                    " LEFT JOIN rdmp_org_info org ON org.org_code=decOrd.org_code " +
                    " LEFT JOIN rdmp_product_type type ON type.type_code=decOrd.type_code " +
                    " LEFT JOIN rdmp_merchant_info  mer ON mer.merchant_no=ord.mer_no " +
                    " LEFT JOIN rdmp_card_manage card ON card.mer_no=mer.merchant_no" +
                    " LEFT JOIN rdmp_oem_service oem ON oem.oem_no=mer.oem_no " +
                    " where ord.order_type ='D' and ord.id=#{id}"
    )
    ExchangeOrder getExchangeOrder(@Param("id")long id);

    //兑换订单导出查询
    @SelectProvider(type=ExchangeOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeOrder.class)
    List<ExchangeOrder> importDetailSelect(@Param("order")ExchangeOrder order);

    //核销列表查询
    @SelectProvider(type=ExchangeOrderDao.SqlProvider.class,method="selectAuditAll")
    @ResultType(ExchangeOrder.class)
    List<ExchangeOrder> selectAuditAll(@Param("order")ExchangeOrder order, @Param("page")Page<ExchangeOrder> page);

    //核销导出查询
    @SelectProvider(type=ExchangeOrderDao.SqlProvider.class,method="selectAuditAll")
    @ResultType(ExchangeOrder.class)
    List<ExchangeOrder> importAuditDetailSelect(@Param("order")ExchangeOrder order);

    //核销统计
    @SelectProvider(type=ExchangeOrderDao.SqlProvider.class,method="selectAuditSum")
    @ResultType(TotalAmount.class)
    TotalAmount selectAuditSum(@Param("order")ExchangeOrder order, @Param("page")Page<ExchangeOrder> page);

    //核销详情
    @Select(
            " select  ord.*,org.org_code,org.org_name,type.type_code,type.type_name,product.id productId," +
                    " product.product_name,product.original_price, " +
                    " decOrd.upload_image,decOrd.redeem_code,decOrd.validity_date_start,decOrd.validity_date_end," +
                    " decOrd.remark productRemark,decOrd.price,decOrd.receive_status,decOrd.logistics_info," +
                    " decOrd.check_status,decOrd.check_oper,decOrd.check_time,decOrd.check_reason, " +
                    " decOrd.channel,decOrd.sale_order_no,decOrd.write_off_price,decOrd.check_status_one, " +
                    " decOrd.channel_check_status,decOrd.channel_check_reason,decOrd.channel_check_time,"+
                    " decOrd.platform_fee,decOrd.share_rate,decOrd.mer_capa, " +
                    " mer.user_name,mer.mobile_username,card.business_code, " +
                    " oem.oem_no,oem.oem_name " +
                    " from rdmp_order ord " +
                    " LEFT JOIN rdmp_declare_order decOrd ON decOrd.order_no=ord.order_no " +
                    " LEFT JOIN rdmp_org_info org ON org.org_code=decOrd.org_code " +
                    " LEFT JOIN rdmp_product_type type ON type.type_code=decOrd.type_code " +
                    " LEFT JOIN rdmp_product_info product ON product.id=decOrd.p_id " +
                    " LEFT JOIN rdmp_merchant_info  mer ON mer.merchant_no=ord.mer_no " +
                    " LEFT JOIN rdmp_card_manage  card ON card.mer_no=mer.merchant_no " +
                    " LEFT JOIN rdmp_oem_service oem ON oem.oem_no=mer.oem_no " +
                    " where ord.order_type ='D' and ord.id=#{id}"
    )
    ExchangeOrder getAuditExchangeOrder(@Param("id")long id);


    //一次核销
    @Update(
            "update rdmp_declare_order set " +
                    " check_status='0', "+
                    " check_status_one=#{wo.checkStatus},receive_status=#{wo.receiveStatus}, " +
                    " check_oper=#{wo.checkOper},check_time=#{wo.createTime},check_reason=#{wo.remark}, " +
                    " channel=#{wo.channel},sale_order_no=#{wo.saleOrderNo},write_off_price=#{wo.writeOffPrice} " +
                    " where order_no=#{wo.orderNo} "
    )
    int writeOffOneTrue(@Param("wo")WriteOffHis writeOff);

    //一次核销
    @Update(
            "update rdmp_declare_order set " +
                    " check_status=#{wo.checkStatus}, "+
                    " check_status_one=#{wo.checkStatus},receive_status=#{wo.receiveStatus}," +
                    " check_oper=#{wo.checkOper},check_time=#{wo.createTime},check_reason=#{wo.remark}," +
                    " channel=#{wo.channel},sale_order_no=#{wo.saleOrderNo},write_off_price=#{wo.writeOffPrice} " +
                    " where order_no=#{wo.orderNo} "
    )
    int writeOffOneFalse(@Param("wo")WriteOffHis writeOff);

    //二次核销
    @Update(
            "update rdmp_declare_order set " +
                    " check_status=#{wo.checkStatus}, "+
                    " receive_status=#{wo.receiveStatus}," +
                    " check_oper=#{wo.checkOper},check_time=#{wo.createTime},check_reason=#{wo.remark}," +
                    " channel=#{wo.channel},sale_order_no=#{wo.saleOrderNo},write_off_price=#{wo.writeOffPrice} " +
                    " where order_no=#{wo.orderNo} "
    )
    int writeOffTwoTrue(@Param("wo")WriteOffHis writeOff);

    //二次核销
    @Update(
            "update rdmp_declare_order set " +
                    " check_status=#{wo.checkStatus}, "+
                    " check_status_one='0',receive_status=#{wo.receiveStatus}," +
                    " check_oper=#{wo.checkOper},check_time=#{wo.createTime},check_reason=#{wo.remark}," +
                    " channel=#{wo.channel},sale_order_no=#{wo.saleOrderNo},write_off_price=#{wo.writeOffPrice} " +
                    " where order_no=#{wo.orderNo} "
    )
    int writeOffTwoFalse(@Param("wo")WriteOffHis writeOff);

    @Update(
            "update rdmp_declare_order set " +
                    " check_status=#{wo.checkStatus}, "+
                    " check_status_one=#{wo.checkStatus},receive_status=#{wo.receiveStatus}," +
                    " check_oper=#{wo.checkOper},check_time=#{wo.createTime},check_reason=#{wo.remark}," +
                    " channel=#{wo.channel},sale_order_no=#{wo.saleOrderNo},write_off_price=#{wo.writeOffPrice} " +
                    " where order_no=#{wo.orderNo} "
    )
    int writeOffTwoImport(@Param("wo")WriteOffHis writeOff);

    @Insert(
            " INSERT INTO rdmp_order_seq (create_time) VALUES(NOW())"
    )
    @Options(useGeneratedKeys=true, keyProperty="id")
    Long getOrderNo();

    @Insert(
            "INSERT INTO rdmp_order " +
                    "(order_no,mer_no,order_type,order_status,create_time,share_status,acc_status)" +
                    "VALUES (#{order.orderNo},#{order.merNo},'D','SUCCESS',NOW(),'1','0')"
    )
    int addOrder(@Param("order")ExchangeOrder order);


    @Insert(
            "INSERT INTO rdmp_declare_order " +
                    "(p_id,order_no,mer_no,par_mer_no,mer_capa,upload_image," +
                    " validity_date_start,validity_date_end,exec_num,redeem_code, " +
                    " logistics_info,order_status,remark,create_time,org_code," +
                    " type_code,declare_people,price )" +
                    " VALUES " +
                    "(#{order.productId},#{order.orderNo},#{order.merNo},#{order.parMerNo},#{order.merCapa},#{order.uploadImage}," +
                    " #{order.validityDateStart},#{order.validityDateEnd},#{order.execNum},#{order.redeemCode}," +
                    " #{order.logisticsInfo},'SUCCESS',#{order.productRemark},NOW(),#{order.orgCode}," +
                    " #{order.typeCode},#{order.declarePeople},#{order.price} ) "
    )
    int addDeclareOrder(@Param("order")ExchangeOrder order);

    //修改详情
    @Select(
            "select ord.*,decOrd.org_code,decOrd.type_code,decOrd.upload_image,decOrd.redeem_code," +
                    " decOrd.validity_date_start,decOrd.validity_date_end,decOrd.remark productRemark," +
                    " decOrd.price,decOrd.exec_num,decOrd.logistics_info,decOrd.check_status,decOrd.check_oper," +
                    " decOrd.check_time,decOrd.check_reason, " +
                    " product.id productId,product.product_name,product.original_price " +
                    " from rdmp_order ord " +
                    " LEFT JOIN rdmp_declare_order decOrd ON decOrd.order_no=ord.order_no " +
                    " LEFT JOIN rdmp_product_info product ON product.id=decOrd.p_id " +
                    " where ord.order_type ='D' and ord.id=#{id}"
    )
    ExchangeOrder getExchangeOrerEdit(@Param("id")long id);

    @Select(
            "select * from rdmp_declare_order where order_no =#{orderNo}"
    )
    ExchangeOrder selectSaveExchangeOrder(@Param("orderNo")String orderNo);


    @Update(
            "update rdmp_declare_order " +
                    " set p_id=#{order.productId},mer_no=#{order.merNo},par_mer_no=#{order.parMerNo}," +
                    " mer_capa=#{order.merCapa},validity_date_start=#{order.validityDateStart}," +
                    " validity_date_end=#{order.validityDateEnd},exec_num=#{order.execNum}, " +
                    " redeem_code=#{order.redeemCode},logistics_info=#{order.logisticsInfo}," +
                    " remark=#{order.productRemark},org_code=#{order.orgCode},type_code=#{order.typeCode}," +
                    " price=#{order.price} " +
                    " where order_no =#{order.orderNo}"
    )
    int updateDeclareOrderNoImage(@Param("order")ExchangeOrder order);

    @Update(
            "update rdmp_declare_order " +
                    " set p_id=#{order.productId},mer_no=#{order.merNo},par_mer_no=#{order.parMerNo}," +
                    " mer_capa=#{order.merCapa},validity_date_start=#{order.validityDateStart}," +
                    " validity_date_end=#{order.validityDateEnd},exec_num=#{order.execNum}, " +
                    " redeem_code=#{order.redeemCode},logistics_info=#{order.logisticsInfo}," +
                    " remark=#{order.productRemark},org_code=#{order.orgCode},type_code=#{order.typeCode}," +
                    " price=#{order.price},upload_image=#{order.uploadImage} " +
                    " where order_no =#{order.orderNo}"
    )
    int updateDeclareOrder(@Param("order")ExchangeOrder order);

    @Update(
            "update rdmp_order set mer_no=#{order.merNo} where order_no=#{order.orderNo} "
    )
    int updateOrder(@Param("order")ExchangeOrder order);


    @Select(
            "select * from rdmp_declare_order " +
                    " where mer_no=#{merNo} and order_status='SUCCESS'" +
                    " and create_time>=#{startDay} and create_time<=#{endDay}" +
                    " and p_id=#{pId}"
    )
    List<ExchangeOrder> ckeckExchangeOrderNum(@Param("merNo")String merNo,
                                              @Param("startDay")Date startDay,
                                              @Param("endDay")Date endDay,
                                              @Param("pId")long pId);

    @Select(
            " select * from rdmp_declare_order_his where order_no=#{orderNo} ORDER BY create_time desc limit 0,10 "
    )
    List<WriteOffHis> getWriteOffList(@Param("orderNo")String orderNo);

    //新增核销记录
    @Insert(
            "INSERT INTO rdmp_declare_order_his " +
                    " (order_no,check_oper,create_time,remark,check_mode,check_status,channel,sale_order_no) " +
                    " VALUES " +
                    " (#{wo.orderNo},#{wo.checkOper},#{wo.createTime},#{wo.remark},#{wo.checkMode},#{wo.checkStatus}," +
                    "  #{wo.channel},#{wo.saleOrderNo}) "
    )
    int addwriteOffHis(@Param("wo")WriteOffHis writeOff);

    @UpdateProvider(type=ExchangeOrderDao.SqlProvider.class,method="ckeckUpper")
    int ckeckUpper(@Param("order")ExchangeOrder order);

    class SqlProvider{

        public String ckeckUpper(final Map<String, Object> param) {
            ExchangeOrder order = (ExchangeOrder) param.get("order");
            StringBuffer sb=new StringBuffer();
            sb.append(" update rdmp_declare_order set ");
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
            final ExchangeOrder order = (ExchangeOrder) param.get("order");
            return getSelectSql(order,1);
        }
        public String selectSum(final Map<String, Object> param) {
            final ExchangeOrder order = (ExchangeOrder) param.get("order");
            return getSelectSql(order,2);
        }
        public String selectAuditAll(final Map<String, Object> param) {
            final ExchangeOrder order = (ExchangeOrder) param.get("order");
            return getSelectSql(order,3);
        }
        public String selectAuditSum(final Map<String, Object> param) {
            final ExchangeOrder order = (ExchangeOrder) param.get("order");
            return getSelectSql(order,4);
        }
        private String getSelectSql(final ExchangeOrder order, int state){
            StringBuffer sb=new StringBuffer();
            sb.append("select ");
            if(state==1){
                sb.append(" ord.*,org.org_code,org.org_name,mer.user_name,mer.mobile_username, ");
                sb.append(" decOrd.price,decOrd.check_status,decOrd.check_oper,decOrd.check_time,decOrd.check_reason, ");
                sb.append(" decOrd.channel,decOrd.sale_order_no,decOrd.write_off_price,decOrd.check_status_one, ");
                sb.append(" decOrd.channel_check_status,decOrd.channel_check_reason,decOrd.channel_check_time,");
                sb.append(" decOrd.platform_fee,decOrd.share_rate,decOrd.mer_capa, ");
                sb.append(" oem.oem_no,oem.oem_name,product.product_name,product.original_price,type.type_code,type.type_name,product.id productId ");
            }else if(state==2){
                sb.append(" SUM(ord.plate_share) plateShareTotal,");
                sb.append(" SUM(ord.oem_share) oemShareTotal, ");
                sb.append(" SUM(decOrd.price) priceTotal, ");
                sb.append(" SUM(ord.agent_amout) agentAmoutTotal, ");
                sb.append(" SUM(ord.mer_amout) merAmoutTotal, ");
                sb.append(" SUM(decOrd.write_off_price) writeOffPriceTotal ");
            }else if(state==3){
                sb.append(" ord.*,org.org_code,org.org_name,type.type_code,type.type_name,product.id productId,");
                sb.append(" product.product_name,product.original_price, ");
                sb.append(" decOrd.upload_image,decOrd.redeem_code,decOrd.validity_date_start,decOrd.validity_date_end,");
                sb.append(" decOrd.remark productRemark,decOrd.price,decOrd.check_status,decOrd.check_oper,decOrd.check_time,decOrd.check_reason, ");
                sb.append(" decOrd.channel,decOrd.sale_order_no,decOrd.write_off_price,decOrd.check_status_one, ");
                sb.append(" decOrd.channel_check_status,decOrd.channel_check_reason,decOrd.channel_check_time,");
                sb.append(" decOrd.platform_fee,decOrd.share_rate,decOrd.mer_capa, ");
                sb.append(" mer.user_name,mer.mobile_username,card.business_code, ");
                sb.append(" oem.oem_no,oem.oem_name ");
            }else if(state==4){
                sb.append(" SUM(ord.plate_share) plateShareTotal,");
                sb.append(" SUM(ord.oem_share) oemShareTotal, ");
                sb.append(" SUM(decOrd.price) priceTotal, ");
                sb.append(" SUM(ord.agent_amout) agentAmoutTotal, ");
                sb.append(" SUM(ord.mer_amout) merAmoutTotal, ");
                sb.append(" SUM(decOrd.write_off_price) writeOffPriceTotal ");
            }
            sb.append(" from rdmp_order ord ");
            sb.append(" LEFT JOIN rdmp_declare_order decOrd ON decOrd.order_no=ord.order_no");
            sb.append(" LEFT JOIN rdmp_org_info org ON org.org_code=decOrd.org_code");
            sb.append(" LEFT JOIN rdmp_product_type type ON type.type_code=decOrd.type_code");
            sb.append(" LEFT JOIN rdmp_product_info product ON product.id=decOrd.p_id");
            sb.append(" LEFT JOIN rdmp_merchant_info  mer ON mer.merchant_no=ord.mer_no");
            sb.append(" LEFT JOIN rdmp_card_manage  card ON card.mer_no=mer.merchant_no");
            sb.append(" LEFT JOIN rdmp_oem_service oem ON oem.oem_no=mer.oem_no");
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
            if(StringUtils.isNotBlank(order.getBusinessCode())){
                sb.append(" and card.business_code  = #{order.businessCode}");
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
