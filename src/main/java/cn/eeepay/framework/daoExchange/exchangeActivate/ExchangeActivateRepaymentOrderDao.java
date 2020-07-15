package cn.eeepay.framework.daoExchange.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateRepaymentOrder;
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
public interface ExchangeActivateRepaymentOrderDao {

    @SelectProvider(type=ExchangeActivateRepaymentOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeActivateRepaymentOrder.class)
    List<ExchangeActivateRepaymentOrder> selectAllList(@Param("order") ExchangeActivateRepaymentOrder order, @Param("page") Page<ExchangeActivateRepaymentOrder> page);

    @SelectProvider(type=ExchangeActivateRepaymentOrderDao.SqlProvider.class,method="selectSum")
    @ResultType(TotalAmount.class)
    TotalAmount selectSum(@Param("order") ExchangeActivateRepaymentOrder order, @Param("page") Page<ExchangeActivateRepaymentOrder> page);


    @Select(
            "select  ord.*, " +
                    " repay.source_order_no,repay.reimbursement_chann,repay.repay_status,repay.plan_amount, " +
                    " repay.actual_amount,repay.completion_time,repay.rate,repay.pay_type, " +
                    " mer.user_name,mer.mobile_username,mer.one_agent_no,mer.agent_no,mer.repay_merchant_no, " +
                    " mer.id_card_no, " +
                    " oem.oem_name,oem.oem_no " +
                    " from yfb_order ord " +
                    "   INNER JOIN act_repay_order repay ON repay.order_no=ord.order_no " +
                    "   LEFT JOIN yfb_merchant_info  mer ON mer.merchant_no=ord.mer_no " +
                    "   LEFT JOIN yfb_oem_service oem ON oem.oem_no=mer.oem_no " +
                    " where ord.id=#{id}"
    )
    ExchangeActivateRepaymentOrder getRepaymentOrder(@Param("id") long id);

    @SelectProvider(type=ExchangeActivateRepaymentOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeActivateRepaymentOrder.class)
    List<ExchangeActivateRepaymentOrder> importDetailSelect(@Param("order") ExchangeActivateRepaymentOrder order);

    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final ExchangeActivateRepaymentOrder order = (ExchangeActivateRepaymentOrder) param.get("order");
            return getSelectSql(order,1);
        }
        public String selectSum(final Map<String, Object> param) {
            final ExchangeActivateRepaymentOrder order = (ExchangeActivateRepaymentOrder) param.get("order");
            return getSelectSql(order,2);
        }

        private String getSelectSql(final ExchangeActivateRepaymentOrder order,int state){
            StringBuffer sb=new StringBuffer();
            sb.append("select ");
            if(state==1){
                sb.append(" ord.*,");
                sb.append(" repay.source_order_no,repay.reimbursement_chann,repay.repay_status,repay.plan_amount, ");
                sb.append(" repay.actual_amount,repay.completion_time,repay.rate,repay.pay_type, ");
                sb.append(" mer.user_name,mer.mobile_username,mer.one_agent_no,mer.agent_no,mer.repay_merchant_no, ");
                sb.append(" oem.oem_name,oem.oem_no ");
            }else if(state==2){
                sb.append(" SUM(repay.plan_amount) planAmountTotal, ");
                sb.append(" SUM(repay.actual_amount) actualAmountTotal, ");
                sb.append(" SUM(ord.provide_amout) shareAmountTotal, ");
                sb.append(" SUM(ord.plate_share) plateShareTotal, ");
                sb.append(" SUM(ord.oem_share) oemShareTotal ");
            }
            sb.append(" from yfb_order ord ");
            sb.append("  INNER JOIN act_repay_order repay ON repay.order_no=ord.order_no ");
            sb.append("  LEFT JOIN yfb_merchant_info  mer ON mer.merchant_no=ord.mer_no ");
            sb.append("  LEFT JOIN yfb_oem_service oem ON oem.oem_no=mer.oem_no ");
            sb.append(" where ord.order_type ='R' ");

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
            if(StringUtils.isNotBlank(order.getAgentNo())){
                sb.append(" and mer.agent_no=#{order.agentNo} ");
            }
            if(StringUtils.isNotBlank(order.getOneAgentNo())){
                sb.append(" and mer.one_agent_no=#{order.oneAgentNo} ");
            }
            if(StringUtils.isNotBlank(order.getRepayStatus())){
                sb.append(" and repay.repay_status = #{order.repayStatus} ");
            }
            if(StringUtils.isNotBlank(order.getPayType())){
                sb.append(" and repay.pay_type = #{order.payType} ");
            }
            if(StringUtils.isNotBlank(order.getRepayMerchantNo())){
                sb.append(" and mer.repay_merchant_no = #{order.repayMerchantNo} ");
            }
            if(order.getCompletionTimeBegin() != null){
                sb.append(" and repay.completion_time >= #{order.completionTimeBegin}");
            }
            if(order.getCompletionTimeEnd() != null){
                sb.append(" and repay.completion_time <= #{order.completionTimeEnd}");
            }
            if(StringUtils.isNotBlank(order.getReimbursementChann())){
                sb.append(" and repay.reimbursement_chann = #{order.reimbursementChann} ");
            }
            if(order.getCreateTimeBegin() != null){
                sb.append(" and ord.create_time >= #{order.createTimeBegin}");
            }
            if(order.getCreateTimeEnd() != null){
                sb.append(" and ord.create_time <= #{order.createTimeEnd}");
            }
            if(StringUtils.isNotBlank(order.getSourceOrderNo())){
                sb.append(" and repay.source_order_no = #{order.sourceOrderNo} ");
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
