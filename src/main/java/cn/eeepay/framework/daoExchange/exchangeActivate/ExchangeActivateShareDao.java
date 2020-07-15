package cn.eeepay.framework.daoExchange.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateShare;
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
 * 订单分润dao
 */
public interface ExchangeActivateShareDao {

    @Select(
            " select detail.* " +
                    " from yfb_agent_share_detail detail " +
                    " where  detail.share_type=#{shareType} and detail.order_no=#{orderId} " +
                    " ORDER BY ABS(detail.share_grade) "
    )
    List<ExchangeActivateShare> getOrderShare(@Param("shareType") String shareType, @Param("orderId") String orderId);

    @SelectProvider(type=ExchangeActivateShareDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeActivateShare.class)
    List<ExchangeActivateShare> selectAllList(@Param("order") ExchangeActivateShare order, @Param("page") Page<ExchangeActivateShare> page);

    @SelectProvider(type=ExchangeActivateShareDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeActivateShare.class)
    List<ExchangeActivateShare> importDetailSelect(@Param("order") ExchangeActivateShare order);

    @SelectProvider(type=ExchangeActivateShareDao.SqlProvider.class,method="selectSum")
    @ResultType(TotalAmount.class)
    TotalAmount selectSum(@Param("order") ExchangeActivateShare order, @Param("page") Page<ExchangeActivateShare> page);


    class SqlProvider{

        public String selectAllList(final Map<String, Object> param) {
            final ExchangeActivateShare order = (ExchangeActivateShare) param.get("order");
            return getSelectSql(order,1);
        }
        public String selectSum(final Map<String, Object> param) {
            final ExchangeActivateShare order = (ExchangeActivateShare) param.get("order");
            return getSelectSql(order,2);
        }

        private String getSelectSql(final ExchangeActivateShare order,int state){
            StringBuffer sb=new StringBuffer();
            sb.append("select ");
            if(state==1){
                sb.append(" detail.*,oem.oem_no,oem.oem_name,ord.order_status,mer.mobile_username, ");
                sb.append(" mer.user_name,mer.real_name ");
            }else if(state==2){
                sb.append(" SUM(detail.amount) totalShareAmountTotal, ");
                sb.append(" SUM(detail.share_amount) shareAmountTotal ");
            }
            sb.append(" from yfb_agent_share_detail detail ");
            sb.append(" LEFT JOIN yfb_order ord ON ord.order_no=detail.order_no ");
            sb.append(" LEFT JOIN yfb_merchant_info  mer ON mer.merchant_no=detail.mer_no ");
            sb.append(" LEFT JOIN yfb_oem_service oem ON oem.oem_no=mer.oem_no ");
            sb.append(" where 1=1 ");
            if(StringUtils.isNotBlank(order.getOrderNo())){
                sb.append(" and detail.order_no = #{order.orderNo} ");
            }
            if(StringUtils.isNotBlank(order.getOrderStatus())){
                sb.append(" and ord.order_status = #{order.orderStatus} ");
            }
            if(StringUtils.isNotBlank(order.getShareType())){
                sb.append(" and detail.share_type = #{order.shareType} ");
            }
            if(StringUtils.isNotBlank(order.getOemNo())){
                sb.append(" and oem.oem_no = #{order.oemNo} ");
            }
            if(order.getCreateTimeBegin() != null){
                sb.append(" and detail.create_time >= #{order.createTimeBegin}");
            }
            if(order.getCreateTimeEnd() != null){
                sb.append(" and detail.create_time <= #{order.createTimeEnd}");
            }
            if(StringUtils.isNotBlank(order.getMerNo())){
                sb.append(" and detail.mer_no = #{order.merNo} ");
            }
            if(StringUtils.isNotBlank(order.getMobileUsername())){
                sb.append(" and mer.mobile_username = #{order.mobileUsername} ");
            }
            if(StringUtils.isNotBlank(order.getAccStatus())){
                sb.append("and detail.acc_status = #{order.accStatus} ");
            }
            if(order.getAccTimeBegin() != null){
                sb.append(" and detail.acc_time >= #{order.accTimeBegin}");
            }
            if(order.getAccTimeEnd() != null){
                sb.append(" and detail.acc_time <= #{order.accTimeEnd}");
            }
            sb.append(" ORDER BY detail.create_time DESC ");
            return sb.toString();
        }
    }
}
