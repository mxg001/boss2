package cn.eeepay.framework.daoExchange.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateReceiveOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/7/007.
 * @author liuks
 * 超级兑激活版 还款Dao
 */
public interface ExchangeActivateReceiveOrderDao {

    @SelectProvider(type=ExchangeActivateReceiveOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeActivateReceiveOrder.class)
    List<ExchangeActivateReceiveOrder> selectAllList(@Param("order") ExchangeActivateReceiveOrder order, @Param("page") Page<ExchangeActivateReceiveOrder> page);

    @SelectProvider(type=ExchangeActivateReceiveOrderDao.SqlProvider.class,method="selectSum")
    @ResultType(TotalAmount.class)
    TotalAmount selectSum(@Param("order") ExchangeActivateReceiveOrder order,  @Param("page") Page<ExchangeActivateReceiveOrder> page);

    @Select(
            "select  ord.*, " +
                    " receive.source_order_no,receive.pay_method,receive.amount, " +
                    " receive.rate,receive.source_order_no, " +
                    " mer.user_name,mer.mobile_username,mer.one_agent_no,mer.agent_no,mer.id_card_no, " +
                    " rec.receive_merchant_no,rec.merchant_status,rec.success_time, " +
                    " oem.oem_name,oem.oem_no " +
                    " from yfb_order ord " +
                    "   INNER JOIN act_receive_order receive ON receive.order_no=ord.order_no " +
                    "   LEFT JOIN yfb_merchant_info  mer ON mer.merchant_no=ord.mer_no " +
                    "   LEFT JOIN act_receive_merchant_info  rec ON rec.source_merchant_no=receive.mer_no " +
                    "   LEFT JOIN yfb_oem_service oem ON oem.oem_no=mer.oem_no " +
                    " where ord.id=#{id}"
    )
    ExchangeActivateReceiveOrder getReceiveOrder(@Param("id")long id);

    @SelectProvider(type=ExchangeActivateReceiveOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeActivateReceiveOrder.class)
    List<ExchangeActivateReceiveOrder> importDetailSelect(@Param("order")ExchangeActivateReceiveOrder order);

    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final ExchangeActivateReceiveOrder order = (ExchangeActivateReceiveOrder) param.get("order");
            return getSelectSql(order,1);
        }
        public String selectSum(final Map<String, Object> param) {
            final ExchangeActivateReceiveOrder order = (ExchangeActivateReceiveOrder) param.get("order");
            return getSelectSql(order,2);
        }

        private String getSelectSql(final ExchangeActivateReceiveOrder order,int state){
            StringBuffer sb=new StringBuffer();
            sb.append("select ");
            if(state==1){
                sb.append(" ord.*,");
                sb.append(" receive.source_order_no,receive.pay_method,receive.amount, ");
                sb.append(" receive.rate,receive.source_order_no, ");
                sb.append(" mer.user_name,mer.mobile_username,mer.one_agent_no,mer.agent_no, ");
                sb.append(" rec.receive_merchant_no,rec.merchant_status,rec.success_time, ");
                sb.append(" oem.oem_name,oem.oem_no ");
            }else if(state==2){
                sb.append(" SUM(receive.amount) receiveAmountTotal,");
                sb.append(" SUM(ord.provide_amout) shareAmountTotal, ");
                sb.append(" SUM(ord.plate_share) plateShareTotal, ");
                sb.append(" SUM(ord.oem_share) oemShareTotal ");
            }
            sb.append(" from yfb_order ord ");
            sb.append("  INNER JOIN act_receive_order receive ON receive.order_no=ord.order_no ");
            sb.append("  LEFT JOIN yfb_merchant_info  mer ON mer.merchant_no=ord.mer_no ");
            sb.append("  LEFT JOIN act_receive_merchant_info  rec ON rec.source_merchant_no=receive.mer_no ");
            sb.append("  LEFT JOIN yfb_oem_service oem ON oem.oem_no=mer.oem_no ");
            sb.append(" where ord.order_type ='O' ");

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
                sb.append(" and mer.mobile_username=#{order.mobileUsername} ");
            }
            if(StringUtils.isNotBlank(order.getReceiveMerchantNo())){
                sb.append(" and rec.receive_merchant_no = #{order.receiveMerchantNo} ");
            }
            if(StringUtils.isNotBlank(order.getAgentNo())){
                sb.append(" and mer.agent_no=#{order.agentNo} ");
            }
            if(StringUtils.isNotBlank(order.getOneAgentNo())){
                sb.append(" and mer.one_agent_no=#{order.oneAgentNo} ");
            }
            if(StringUtils.isNotBlank(order.getSourceOrderNo())){
                sb.append(" and receive.source_order_no = #{order.sourceOrderNo} ");
            }
            if(order.getCreateTimeBegin() != null){
                sb.append(" and ord.create_time >= #{order.createTimeBegin}");
            }
            if(order.getCreateTimeEnd() != null){
                sb.append(" and ord.create_time <= #{order.createTimeEnd}");
            }
            if(StringUtils.isNotBlank(order.getAccStatus())){
                sb.append(" and ord.acc_status = #{order.accStatus} ");
            }
            if(order.getAccTimeBegin() != null){
                sb.append(" and ord.acc_time >= #{order.accTimeBegin}");
            }
            if(order.getAccTimeEnd() != null){
                sb.append(" and ord.acc_time <= #{order.accTimeEnd}");
            }
            sb.append(" ORDER BY ord.create_time DESC ");
            return sb.toString();
        }
    }
}
