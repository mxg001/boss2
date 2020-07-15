package cn.eeepay.framework.daoExchange;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.AgentOrder;
import cn.eeepay.framework.model.exchange.AgentShare;
import cn.eeepay.framework.model.exchange.TotalAmount;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/17/017.
 * @author  liuks
 * 代理费订单 dao
 */
public interface AgentOrderDao {


    @SelectProvider(type=AgentOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(AgentOrder.class)
    List<AgentOrder> selectAllList(@Param("order")AgentOrder order, @Param("page")Page<AgentOrder> page);

    @Select(
            "select ord.*,act.amount,act.pay_order_no,act.pay_time,mer.user_name, " +
                    " oem.oem_name,oem.oem_no  " +
                    " from rdmp_order ord " +
                    " LEFT JOIN rdmp_activity_order act ON act.order_no=ord.order_no " +
                    " LEFT JOIN rdmp_merchant_info  mer ON mer.merchant_no=ord.mer_no " +
                    " LEFT JOIN rdmp_oem_service oem ON oem.oem_no=mer.oem_no " +
                    " where ord.order_type ='A' and ord.id=#{id}"
    )
    AgentOrder getAgentOrder(@Param("id")long id);

    //导出查询
    @SelectProvider(type=AgentOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(AgentOrder.class)
    List<AgentOrder> importDetailSelect(@Param("order")AgentOrder order);

    //统计
    @SelectProvider(type=AgentOrderDao.SqlProvider.class,method="selectSum")
    @ResultType(TotalAmount.class)
    TotalAmount selectSum(@Param("order")AgentOrder order, @Param("page")Page<AgentOrder> page);

    @Select(
            "select * from rdmp_agent_share_detail where order_no=#{orderNo} ORDER BY ABS(share_grade)"
    )
    List<AgentShare> getOrderShare(@Param("orderNo")String orderNo);

    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final AgentOrder order = (AgentOrder) param.get("order");
            return getSelectSql(order,1);
        }
        public String selectSum(final Map<String, Object> param) {
            final AgentOrder order = (AgentOrder) param.get("order");
            return getSelectSql(order,2);
        }
        private String getSelectSql(final AgentOrder order,int state){
            StringBuffer sb=new StringBuffer();
            sb.append("select ");
            if(state==1){
                sb.append(" ord.*,act.amount,act.pay_order_no,act.pay_time,mer.user_name,");
                sb.append(" oem.oem_name,oem.oem_no ");
            }else if(state==2){
                sb.append(" SUM(ord.plate_share) plateShareTotal,");
                sb.append(" SUM(ord.oem_share) oemShareTotal, ");
                sb.append(" SUM(ord.agent_amout) agentAmoutTotal, ");
                sb.append(" SUM(ord.mer_amout) merAmoutTotal ");
            }
            sb.append(" from rdmp_order ord ");
            sb.append("  LEFT JOIN rdmp_activity_order act ON act.order_no=ord.order_no ");
            sb.append("  LEFT JOIN rdmp_merchant_info  mer ON mer.merchant_no=ord.mer_no ");
            sb.append("  LEFT JOIN rdmp_oem_service oem ON oem.oem_no=mer.oem_no ");
            sb.append(" where ord.order_type ='A' ");
            if(StringUtils.isNotBlank(order.getOrderNo())){
                sb.append(" and ord.order_no = #{order.orderNo} ");
            }
            if(StringUtils.isNotBlank(order.getMerNo())){
                sb.append(" and ord.mer_no = #{order.merNo} ");
            }
            if(StringUtils.isNotBlank(order.getPayOrderNo())){
                sb.append(" and act.pay_order_no = #{order.payOrderNo} ");
            }
            if(StringUtils.isNotBlank(order.getOemNo())){
                sb.append(" and oem.oem_no = #{order.oemNo} ");
            }
            if(StringUtils.isNotBlank(order.getOrderStatus())){
                sb.append(" and ord.order_status = #{order.orderStatus} ");
            }
            if(StringUtils.isNotBlank(order.getAccStatus())){
                sb.append(" and ord.acc_status = #{order.accStatus} ");
            }
            if(order.getCreateTimeBegin() != null){
                sb.append(" and ord.create_time >= #{order.createTimeBegin}");
            }
            if(order.getCreateTimeEnd() != null){
                sb.append(" and ord.create_time <= #{order.createTimeEnd}");
            }
            if(order.getPayTimeBegin() != null){
                sb.append(" and act.pay_time >= #{order.payTimeBegin}");
            }
            if(order.getPayTimeEnd() != null){
                sb.append(" and act.pay_time <= #{order.payTimeEnd}");
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
