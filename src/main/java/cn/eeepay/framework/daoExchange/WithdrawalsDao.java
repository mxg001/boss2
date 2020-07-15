package cn.eeepay.framework.daoExchange;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchange.Withdrawals;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/18/018.
 * @author  liuks
 * 用户提现dao
 */
public interface WithdrawalsDao {

    @SelectProvider(type=WithdrawalsDao.SqlProvider.class,method="selectAllList")
    @ResultType(Withdrawals.class)
    List<Withdrawals> selectAllList(@Param("wi") Withdrawals wi, @Param("page")Page<Withdrawals> page);

    @SelectProvider(type=WithdrawalsDao.SqlProvider.class,method="selectAllList")
    @ResultType(Withdrawals.class)
    List<Withdrawals> importDetailSelect(@Param("wi")Withdrawals wi);

    @SelectProvider(type=WithdrawalsDao.SqlProvider.class,method="selectSum")
    @ResultType(TotalAmount.class)
    TotalAmount selectSum(@Param("wi") Withdrawals wi, @Param("page")Page<Withdrawals> page);


    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final Withdrawals wi = (Withdrawals) param.get("wi");
            return getSelectSql(wi,1);
        }

        public String selectSum(final Map<String, Object> param) {
            final Withdrawals wi = (Withdrawals) param.get("wi");
            return getSelectSql(wi,2);
        }

        private String getSelectSql(final Withdrawals wi, int state){
            StringBuffer sb=new StringBuffer();
            sb.append("select ");
            if(state==1){
                sb.append(" his.*,wechat.nickname,his.amount-his.fee amountWithdrawals, ");
                sb.append(" oem.oem_name,oem.oem_no ");
            }else if(state==2){
                sb.append(" SUM(his.amount) amountTotal,");
                sb.append(" SUM(his.fee) feeTotal, ");
                sb.append(" SUM(his.amount)-SUM(his.fee) amountWithdrawalsTotal ");
            }
            sb.append(" from rdmp_extraction_his his ");
            sb.append(" LEFT JOIN rdmp_merchant_info mer ON mer.merchant_no=his.mer_no");
            sb.append(" LEFT JOIN rdmp_oem_service oem ON oem.oem_no=mer.oem_no ");
            sb.append(" LEFT JOIN rdmp_wechat_info wechat ON wechat.unionid=mer.unionid");
            sb.append(" where 1=1 ");
            if(StringUtils.isNotBlank(wi.getOrderNo())){
                sb.append(" and his.order_no = #{wi.orderNo} ");
            }
            if(StringUtils.isNotBlank(wi.getMerNo())){
                sb.append(" and his.mer_no = #{wi.merNo} ");
            }
            if(StringUtils.isNotBlank(wi.getMobileNo())){
                sb.append(" and his.mobile_no = #{wi.mobileNo} ");
            }
            if(StringUtils.isNotBlank(wi.getStatus())){
                sb.append(" and his.status = #{wi.status} ");
            }
            if(StringUtils.isNotBlank(wi.getOemNo())){
                sb.append(" and oem.oem_no = #{wi.oemNo} ");
            }
            if(wi.getCreateTimeBegin() != null){
                sb.append(" and his.create_time >= #{wi.createTimeBegin}");
            }
            if(wi.getCreateTimeEnd() != null){
                sb.append(" and his.create_time <= #{wi.createTimeEnd}");
            }
            if(wi.getAmountBegin() != null){
                sb.append(" and his.amount >= #{wi.amountBegin}");
            }
            if(wi.getAmountEnd() != null){
                sb.append(" and his.amount <= #{wi.amountEnd}");
            }
            sb.append(" ORDER BY his.create_time DESC ");
            return sb.toString();
        }
    }
}
