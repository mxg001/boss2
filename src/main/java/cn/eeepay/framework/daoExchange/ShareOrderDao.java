package cn.eeepay.framework.daoExchange;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ShareOrder;
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
 * 订单分润dao
 */
public interface ShareOrderDao {

    @Select(
            " select detail.id,detail.order_no,detail.share_mer_no,detail.share_mer_capa," +
                    " detail.share_grade,mer.user_name share_mer_name,detail.share_amount " +
                    " from rdmp_share_detail detail " +
                    " LEFT JOIN rdmp_merchant_info mer ON mer.merchant_no=detail.share_mer_no" +
                    " where  detail.share_type=#{shareType} and detail.order_no=#{orderId} " +
                    " ORDER BY ABS(detail.share_grade) "
    )
    List<ShareOrder> getOrderShare(@Param("shareType") String shareType,@Param("orderId") String orderId);

    @SelectProvider(type=ShareOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(ShareOrder.class)
    List<ShareOrder> selectAllList(@Param("order")ShareOrder order, @Param("page")Page<ShareOrder> page);

    @SelectProvider(type=ShareOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(ShareOrder.class)
    List<ShareOrder> importDetailSelect(@Param("order")ShareOrder order);

    @SelectProvider(type=ShareOrderDao.SqlProvider.class,method="selectSum")
    @ResultType(TotalAmount.class)
    TotalAmount selectSum(@Param("order")ShareOrder order, @Param("page")Page<ShareOrder> page);


    class SqlProvider{

        public String selectAllList(final Map<String, Object> param) {
            final ShareOrder order = (ShareOrder) param.get("order");
            return getSelectSql(order,1);
        }
        public String selectSum(final Map<String, Object> param) {
            final ShareOrder order = (ShareOrder) param.get("order");
            return getSelectSql(order,2);
        }

        private String getSelectSql(final ShareOrder order,int state){
            StringBuffer sb=new StringBuffer();
            sb.append("select ");
            if(state==1){
                sb.append(" detail.*,oem.oem_no,oem.oem_name,ord.order_status,mer.mobile_username, ");
                sb.append(" card.account_name,card1.account_name share_name,mer1.mobile_username share_mobile ");
            }else if(state==2){
                sb.append(" SUM(detail.share_amount) shareAmountTotal ");
            }
            sb.append(" from rdmp_share_detail detail ");
            sb.append(" LEFT JOIN rdmp_order ord ON ord.order_no=detail.order_no ");
            sb.append(" LEFT JOIN rdmp_merchant_info  mer ON mer.merchant_no=detail.mer_no ");
            sb.append(" LEFT JOIN rdmp_card_manage card ON card.mer_no=mer.merchant_no ");

            sb.append(" LEFT JOIN rdmp_merchant_info  mer1 ON mer1.merchant_no=detail.share_mer_no ");
            sb.append(" LEFT JOIN rdmp_card_manage card1 ON card1.mer_no=mer1.merchant_no ");
            sb.append(" LEFT JOIN rdmp_oem_service oem ON oem.oem_no=mer1.oem_no ");
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
            if(StringUtils.isNotBlank(order.getShareMerNo())){
                sb.append(" and detail.share_mer_no = #{order.shareMerNo} ");
            }
            if(StringUtils.isNotBlank(order.getShareMobile())){
                sb.append(" and mer1.mobile_username = #{order.shareMobile} ");
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
