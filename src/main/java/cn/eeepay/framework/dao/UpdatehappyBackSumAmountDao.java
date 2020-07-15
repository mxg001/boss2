package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.ActivityDetail;
import cn.eeepay.framework.model.happyBack.FilterDate;
import cn.eeepay.framework.model.happyBack.FilterPage;
import cn.eeepay.framework.model.happyBack.HappyBackSumAmount;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * 欢乐返商户交易金额累计 dao
 * liuks
 *2017/12/26/026.
 */
public interface UpdatehappyBackSumAmountDao {

    /**
     *分页查询欢乐返奖励商户
     */
    @SelectProvider(type=UpdatehappyBackSumAmountDao.SqlProvider.class,method="happyBackListRewardPage")
    @ResultType(ActivityDetail.class)
    List<ActivityDetail> happyBackListRewardPage(@Param("adCon") FilterDate adCon, @Param("filterPage")FilterPage filterPage);

    /**
     *统计单个商户指定时间内交易总量
     */
    @SelectProvider(type=UpdatehappyBackSumAmountDao.SqlProvider.class,method="selectSum")
    @ResultType(HappyBackSumAmount.class)
    HappyBackSumAmount happyBackSumAmoun(@Param("sumCon") FilterDate sumCon);

    /**
     *查询欢乐返扣减商户
     */
    @SelectProvider(type=UpdatehappyBackSumAmountDao.SqlProvider.class,method="happyBackListDeduction")
    @ResultType(ActivityDetail.class)
    List<ActivityDetail> happyBackListDeduction(@Param("adCon") FilterDate adCon);

    class SqlProvider {
        //奖励
        public String happyBackListRewardPage(final Map<String, Object> param){
            FilterDate adCon=(FilterDate)param.get("adCon");
            FilterPage filterPage=(FilterPage)param.get("filterPage");
            StringBuffer sb=new StringBuffer();
            sb.append("select");
            sb.append(" ad.*,harType.count_trade_scope, ");
            sb.append(" mi.one_agent_no one_agent_no,ai.agent_name one_agent_name ");
            sb.append(" FROM activity_detail ad ");
            sb.append(" LEFT JOIN  merchant_info mi ON mi.merchant_no = ad.merchant_no ");
            sb.append(" LEFT JOIN  agent_info ai ON ai.agent_no=mi.one_agent_no ");
            sb.append(" LEFT JOIN  activity_hardware_type harType ON harType.activity_type_no = ad.activity_type_no ");

            sb.append(" where 1=1");
            if(0==adCon.getTimeNull()){
                sb.append("   and ad.overdue_time>=#{adCon.endDate} ");
                sb.append("   and min_overdue_time is null ");
            }else if(1==adCon.getTimeNull()){
                sb.append("   and ad.min_overdue_time>=#{adCon.endDate} ");
            }
            sb.append("   and ad.activity_code IN ('008', '009') ");
            sb.append("   and ad.status!='1' ");
            sb.append("  ORDER BY ad.id ");
            sb.append("  LIMIT "+filterPage.getStartPage()+","+filterPage.getLength());
            return sb.toString();
        }

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
        //扣减
        public String happyBackListDeduction(final Map<String, Object> param){
            FilterDate adCon=(FilterDate)param.get("adCon");
            StringBuffer sb=new StringBuffer();
            sb.append("select");
            sb.append(" ad.*,harType.count_trade_scope, ");
            sb.append(" mi.one_agent_no one_agent_no,ai.agent_name one_agent_name ");
            sb.append(" FROM activity_detail ad ");
            sb.append(" LEFT JOIN  merchant_info mi ON mi.merchant_no = ad.merchant_no ");
            sb.append(" LEFT JOIN  agent_info ai ON ai.agent_no=mi.one_agent_no ");
            sb.append(" LEFT JOIN  activity_hardware_type harType ON harType.activity_type_no = ad.activity_type_no ");

            sb.append(" where 1=1");
            sb.append("   and ad.overdue_time>=#{adCon.startDate} ");
            sb.append("   and ad.overdue_time<=#{adCon.endDate} ");
            sb.append("   and ad.activity_code IN ('008', '009') ");
            sb.append("   and ad.status!='1' ");
            sb.append("   and ad.empty_amount>0 ");
            sb.append("   and ad.minus_amount_time is NULL ");
            sb.append("  ORDER BY ad.id ");
            return sb.toString();
        }
    }
}
