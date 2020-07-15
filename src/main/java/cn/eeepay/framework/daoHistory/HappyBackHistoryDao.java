package cn.eeepay.framework.daoHistory;

import cn.eeepay.framework.model.happyBack.FilterDate;
import cn.eeepay.framework.model.happyBack.HappyBackSumAmount;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.Map;

/**
 * @author liuks
 * 2018/12/26/
 * 统计历史库的交易
 */
public interface HappyBackHistoryDao {

    /**
     *统计单个商户指定时间内交易总量
     */
    @SelectProvider(type=HappyBackHistoryDao.SqlProvider.class,method="selectSum")
    @ResultType(HappyBackSumAmount.class)
    HappyBackSumAmount happyBackSumAmoun(@Param("sumCon") FilterDate sumCon);

    class SqlProvider {
        public String selectSum(final Map<String, Object> param){
            FilterDate sumCon=(FilterDate)param.get("sumCon");
            StringBuffer sb=new StringBuffer();
            sb.append("select cto.merchant_no,sum(cto.trans_amount) total ");
            sb.append(" FROM collective_trans_order cto ");
            sb.append(" where ");
            sb.append("   cto.merchant_no=#{sumCon.merchantNo} ");
            sb.append("   and cto.trans_time>=#{sumCon.startDate} ");
            sb.append("   and cto.trans_time<=#{sumCon.endDate} ");
            //如果有交易类型直接查询
            if(StringUtils.isNotBlank(sumCon.getCountTradeScope())){
                sb.append("   and cto.pay_method in ("+sumCon.getCountTradeScope()+") ");
            }else{
                sb.append("   and cto.pay_method=1 ");//无默认1
            }
            sb.append("   and cto.trans_status='SUCCESS' ");
            sb.append(" GROUP BY cto.merchant_no  ");
            return sb.toString();
        }
    }
}
