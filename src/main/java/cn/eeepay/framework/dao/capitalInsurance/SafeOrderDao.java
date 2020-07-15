package cn.eeepay.framework.dao.capitalInsurance;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.OrderTotal;
import cn.eeepay.framework.model.capitalInsurance.SafeOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/23/023.
 * @author liuks
 * 保险订单DAO
 */
public interface SafeOrderDao {

    @SelectProvider(type=SafeOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(SafeOrder.class)
    List<SafeOrder> selectAllList(@Param("order") SafeOrder order, @Param("page") Page<SafeOrder> page);

    @SelectProvider(type=SafeOrderDao.SqlProvider.class,method="selectSum")
    @ResultType(OrderTotal.class)
    OrderTotal selectSum(@Param("order") SafeOrder order, @Param("page") Page<SafeOrder> page);

    @SelectProvider(type=SafeOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(SafeOrder.class)
    List<SafeOrder> importDetailSelect(@Param("order")SafeOrder order);

    @Select(
            "select ord.*, " +
                    " cto.settlement_method,cto.trans_amount,cto.trans_status,cto.trans_time, "+
                    " det.c_app_nme,det.c_clnt_mrk,det.c_certf_cls,det.c_certf_cde,det.c_mobile, " +
                    " det.c_rel_code,det.c_tel_phone,det.c_clnt_addr,det.c_zip_cde," +
                    " det.c_nme,det.c_clnt_mrk1,det.c_cert_typ,det.c_cert_no,det.c_sex,det.c_clnt_addr1,det.c_mobile1, " +
                    " agent.agent_no agentNo,agent.agent_name agentName,agent1.agent_name oneAgentName "+
                    " from zjx_trans_order ord " +
                    "  LEFT JOIN collective_trans_order cto ON cto.order_no=ord.order_no " +
                    "  LEFT JOIN zjx_trans_order_people det ON det.bx_order_no=ord.bx_order_no " +
                    "  LEFT JOIN merchant_info mer ON mer.merchant_no=ord.merchant_no " +
                    "  LEFT JOIN agent_info agent ON agent.agent_no=mer.agent_no " +
                    "  LEFT JOIN agent_info agent1 ON agent1.agent_no=mer.one_agent_no " +
                    " where ord.id=#{id}"
    )
    SafeOrder getSafeOrderDetail(@Param("id") int id);

    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            return  getSelectSql(param,1);
        }
        public String selectSum(final Map<String, Object> param) {
            return  getSelectSql(param,2);
        }
        public String getSelectSql(final Map<String, Object> param,int sta) {
            final SafeOrder order = (SafeOrder) param.get("order");
            StringBuffer sb=new StringBuffer();
            sb.append("select ");
            if(sta==1){
                sb.append(" ord.*, ");
                sb.append(" cto.settlement_method,cto.trans_amount,cto.trans_status  ");
            }else if(sta==2){
                sb.append(" count(*) countTotal, ");
                sb.append(" sum(ord.n_prm) nPrmTotal, ");
                sb.append(" sum(ord.n_fee) nFeeTotal ");
            }
            sb.append("from zjx_trans_order ord ");
            sb.append(" LEFT JOIN collective_trans_order cto ON cto.order_no=ord.order_no ");

            sb.append(" where 1=1 ");
            if(StringUtils.isNotBlank(order.getBxOrderNo())){
                sb.append(" and ord.bx_order_no = #{order.bxOrderNo} ");
            }
            if(StringUtils.isNotBlank(order.getBxUnit())&&!"0".equals(order.getBxUnit())){
                sb.append(" and ord.bx_unit = #{order.bxUnit} ");
            }
            if(StringUtils.isNotBlank(order.getProdNo())){
                sb.append(" and ord.prod_no = #{order.prodNo} ");
            }
            if(StringUtils.isNotBlank(order.getThirdOrderNo())){
                sb.append(" and ord.third_order_no = #{order.thirdOrderNo} ");
            }
            if(StringUtils.isNotBlank(order.getBxType())){
                sb.append(" and ord.bx_type = #{order.bxType} ");
            }
            if(StringUtils.isNotBlank(order.getTransStatus())){
                sb.append(" and cto.trans_status = #{order.transStatus} ");
            }
            if(StringUtils.isNotBlank(order.getOrderNo())){
                sb.append(" and ord.order_no = #{order.orderNo} ");
            }
            if(StringUtils.isNotBlank(order.getSettlementMethod())){
                sb.append(" and cto.settlement_method = #{order.settlementMethod} ");
            }
            if(StringUtils.isNotBlank(order.getMerchantNo())){
                sb.append(" and ord.merchant_no = #{order.merchantNo} ");
            }
            //代理商是否包含下级
            if(StringUtils.isNotBlank(order.getAgentNo())){
                if(order.getLowerAgent()!=null){
                    if(order.getLowerAgent().intValue()==1){
                        sb.append(" and cto.agent_node LIKE (SELECT CONCAT(agent_node,'%') FROM agent_info a where a.agent_no=#{order.agentNo})");
                    }else{
                        sb.append(" and cto.agent_node = (SELECT agent_node FROM agent_info a where a.agent_no=#{order.agentNo})");
                    }
                }
            }
            if (order.gettTimeBegin()!=null) {
                sb.append(" and  ord.t_time>=#{order.tTimeBegin}");
            }
            if (order.gettTimeEnd()!=null) {
                sb.append(" and  ord.t_time<=#{order.tTimeEnd}");
            }
            sb.append("order by ord.id desc ");
            return  sb.toString();
        }
    }
}
