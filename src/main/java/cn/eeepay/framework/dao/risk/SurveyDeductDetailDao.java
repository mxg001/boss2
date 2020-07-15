package cn.eeepay.framework.dao.risk;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.risk.DeductAddInfo;
import cn.eeepay.framework.model.risk.SurveyDeductDetail;
import cn.eeepay.framework.model.risk.SurveyOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/12/012.
 * @author  liuks
 * 调单扣款明细
 */
public interface SurveyDeductDetailDao {

    @Insert(
            "INSERT INTO survey_order_deduct" +
                    "(order_no,acq_deduct_amount,acq_deduct_time," +
                    " acq_deduct_remark,operator,operate_time,operator_type)" +
                    " VALUES" +
                    " (#{order.orderNo},#{order.acqDeductAmount},#{order.acqDeductTime}," +
                    "  #{order.acqDeductRemark},#{order.operator},NOW(),'0')"
    )
    int addDeductDetail(@Param("order") SurveyOrder order);

    @SelectProvider(type=SurveyDeductDetailDao.SqlProvider.class,method="selectAllList")
    @ResultType(SurveyDeductDetail.class)
    List<SurveyDeductDetail> selectAllList(@Param("detail") SurveyDeductDetail detail,@Param("page") Page<SurveyDeductDetail> page);

    @SelectProvider(type=SurveyDeductDetailDao.SqlProvider.class,method="selectAllList")
    @ResultType(SurveyDeductDetail.class)
    List<SurveyDeductDetail> importDetailSelect(@Param("detail")SurveyDeductDetail detail);


    @SelectProvider(type=SurveyDeductDetailDao.SqlProvider.class,method="selectGroup")
    @ResultType(SurveyDeductDetail.class)
    List<SurveyDeductDetail> selectGroup(@Param("detail")SurveyDeductDetail detail,@Param("page") Page<SurveyDeductDetail> page);

    @SelectProvider(type=SurveyDeductDetailDao.SqlProvider.class,method="selectGroup")
    @ResultType(SurveyDeductDetail.class)
    List<SurveyDeductDetail> importSelectGroup(@Param("detail")SurveyDeductDetail detail);

    @InsertProvider(type=SurveyDeductDetailDao.SqlProvider.class,method="saveDeductDetail")
    @ResultType(SurveyDeductDetail.class)
    int saveDeductDetail(@Param("info")DeductAddInfo info);



    class SqlProvider{

        public String saveDeductDetail(final Map<String, Object> param) {
            DeductAddInfo info = (DeductAddInfo) param.get("info");
            int sta = Integer.parseInt(info.getOperatorType());
            StringBuffer sb=new StringBuffer();
            sb.append(" INSERT INTO survey_order_deduct ");
            sb.append("    (order_no,operator,operate_time,operator_type,  ");
            if(sta==0){
                sb.append(" acq_deduct_amount,acq_deduct_time,acq_deduct_remark ");
            }else if(sta==1){
                sb.append(" acq_issue_amount,acq_issue_time,acq_issue_remark ");
            }else if(sta==2){
                sb.append(" mer_deduct_amount,agent_remain_deduct_amount,mer_deduct_time,mer_deduct_remark ");
            }else if(sta==3){
                sb.append(" mer_issue_amount,agent_remain_issue_amount,mer_issue_time,mer_issue_remark ");
            }else if(sta==4){
                sb.append(" agent_have_deduct_amount,agent_need_deduct_amount,agent_deduct_time,agent_deduct_remark ");
            }else if(sta==5){
                sb.append(" agent_have_issue_amount,agent_need_issue_amount,agent_issue_time,agent_issue_remark ");
            }
            sb.append("     ) ");
            sb.append("   VALUES ");
            sb.append("     (#{info.orderNo},#{info.operator},NOW(),#{info.operatorType}, ");
            if(sta==0||sta==1){
                sb.append("  #{info.amoutOne},#{info.time},#{info.remark} ");
            }else if(sta==2||sta==3||sta==4||sta==5){
                sb.append("  #{info.amoutOne},#{info.amoutTwo},#{info.time},#{info.remark} ");
            }
            sb.append("     ) ");
            return sb.toString();
        }

        public String selectAllList(final Map<String, Object> param) {
            SurveyDeductDetail detail = (SurveyDeductDetail) param.get("detail");
            return selectSQL(detail,1);
        }
        /**
         * 扣款明细查询
         */
        public String selectSQL(SurveyDeductDetail detail,int sta){
            StringBuffer sb=new StringBuffer();
            sb.append("select ");
            if(sta==1){
                sb.append(" deduct.*,deduct.agent_need_deduct_amount-deduct.agent_remain_deduct_amount agentDeductAmount, ");
                sb.append(" ord.merchant_no,ord.trans_order_no,ord.acq_reference_no,ord.trans_order_database,ord.trans_amount,");
                sb.append(" ord.order_service_code,ord.order_type_code,ord.have_add_deduct,ord.agent_deduct_deal_status,");
                sb.append(" ord.agent_deduct_deal_remark,ord.agent_issue_deal_status,ord.agent_issue_deal_remark ");

            }
            sb.append(" from survey_order_deduct deduct ");
            sb.append("   LEFT JOIN survey_order_info ord ON ord.order_no=deduct.order_no");
            sb.append(" where 1=1 ");
            sb.append("     and  ord.order_status='1' ");
            sb.append("     and  ord.have_add_deduct='1' ");
            if(StringUtils.isNotBlank(detail.getOrderNo())){
                sb.append(" and ord.order_no=#{detail.orderNo}");
            }
            if(StringUtils.isNotBlank(detail.getTransOrderNo())){
                sb.append(" and ord.trans_order_no=#{detail.transOrderNo}");
            }
            if(StringUtils.isNotBlank(detail.getAcqReferenceNo())){
                sb.append(" and ord.acq_reference_no=#{detail.acqReferenceNo}");
            }
            if(StringUtils.isNotBlank(detail.getOrderTypeCode())){
                sb.append(" and ord.order_type_code=#{detail.orderTypeCode}");
            }
            if(StringUtils.isNotBlank(detail.getAgentDeductDealStatus())){
                sb.append(" and ord.agent_deduct_deal_status=#{detail.agentDeductDealStatus}");
            }
            if(StringUtils.isNotBlank(detail.getAgentIssueDealStatus())){
                sb.append(" and ord.agent_issue_deal_status=#{detail.agentIssueDealStatus}");
            }
            if(StringUtils.isNotBlank(detail.getMerchantNo())){
                sb.append(" and ord.merchant_no=#{detail.merchantNo}");
            }
            if(StringUtils.isNotBlank(detail.getOperator())){
                sb.append(" and deduct.operator=#{detail.operator}");
            }
            if(detail.getAcqDeductTimeBegin() != null){
                sb.append("  and deduct.acq_deduct_time >= #{detail.acqDeductTimeBegin}");
            }
            if(detail.getAcqDeductTimeEnd() != null){
                sb.append("  and deduct.acq_deduct_time <= #{detail.acqDeductTimeEnd}");
            }
            if(detail.getOperateTimeBegin() != null){
                sb.append("  and deduct.operate_time >= #{detail.operateTimeBegin}");
            }
            if(detail.getOperateTimeEnd() != null){
                sb.append("  and deduct.operate_time <= #{detail.operateTimeEnd}");
            }
            sb.append(" order by deduct.operate_time desc ");
            return sb.toString();
        }

        public String selectGroup(final Map<String, Object> param) {
            SurveyDeductDetail detail = (SurveyDeductDetail) param.get("detail");
            return selectGroupSQL(detail,1);
        }

        /**
         *扣款查询
         */
        public String selectGroupSQL(SurveyDeductDetail detail,int sta){
            StringBuffer sb=new StringBuffer();
            sb.append("select ");
            if(sta==1){
                sb.append(" ord.*, ");
                sb.append(" (SELECT sum(acq_deduct_amount) FROM survey_order_deduct where order_no = ord.order_no GROUP BY order_no) acq_deduct_amount, ");
                sb.append(" (SELECT sum(mer_deduct_amount) FROM survey_order_deduct where order_no = ord.order_no GROUP BY order_no) mer_deduct_amount, ");
                sb.append(" (SELECT sum(agent_need_deduct_amount) FROM survey_order_deduct where order_no = ord.order_no GROUP BY order_no) agent_need_deduct_amount, ");
                sb.append(" (SELECT sum(agent_remain_deduct_amount) FROM survey_order_deduct where order_no = ord.order_no GROUP BY order_no) agent_remain_deduct_amount, ");
                sb.append(" (SELECT sum(agent_have_deduct_amount) FROM survey_order_deduct where order_no = ord.order_no GROUP BY order_no) agent_have_deduct_amount, ");
                sb.append(" (SELECT sum(acq_issue_amount) FROM survey_order_deduct where order_no = ord.order_no GROUP BY order_no) acq_issue_amount, ");
                sb.append(" (SELECT sum(mer_issue_amount) FROM survey_order_deduct where order_no = ord.order_no GROUP BY order_no) mer_issue_amount, ");
                sb.append(" (SELECT sum(agent_need_issue_amount) FROM survey_order_deduct where order_no = ord.order_no GROUP BY order_no) agent_need_issue_amount, ");
                sb.append(" (SELECT sum(agent_remain_issue_amount) FROM survey_order_deduct where order_no = ord.order_no GROUP BY order_no) agent_remain_issue_amount, ");
                sb.append(" (SELECT sum(agent_have_issue_amount) FROM survey_order_deduct where order_no = ord.order_no GROUP BY order_no) agent_have_issue_amount, ");

                sb.append(" (SELECT acq_deduct_time FROM survey_order_deduct WHERE operator_type = '0' and order_no = ord.order_no ORDER BY operate_time DESC LIMIT 1) acq_deduct_time, ");
                sb.append(" (SELECT acq_deduct_remark FROM survey_order_deduct WHERE operator_type = '0' and order_no = ord.order_no ORDER BY operate_time DESC LIMIT 1) acq_deduct_remark, ");
                sb.append(" (SELECT acq_issue_time FROM survey_order_deduct WHERE operator_type = '1' and order_no = ord.order_no ORDER BY operate_time DESC LIMIT 1) acq_issue_time, ");
                sb.append(" (SELECT acq_issue_remark FROM survey_order_deduct WHERE operator_type = '1' and order_no = ord.order_no ORDER BY operate_time DESC LIMIT 1) acq_issue_remark, ");
                sb.append(" (SELECT mer_deduct_time FROM survey_order_deduct WHERE operator_type = '2' and order_no = ord.order_no ORDER BY operate_time DESC LIMIT 1) mer_deduct_time, ");
                sb.append(" (SELECT mer_deduct_remark FROM survey_order_deduct WHERE operator_type = '2' and order_no = ord.order_no ORDER BY operate_time DESC LIMIT 1) mer_deduct_remark, ");
                sb.append(" (SELECT mer_issue_time FROM survey_order_deduct WHERE operator_type = '3' and order_no = ord.order_no ORDER BY operate_time DESC LIMIT 1) mer_issue_time, ");
                sb.append(" (SELECT mer_issue_remark FROM survey_order_deduct WHERE operator_type = '3' and order_no = ord.order_no ORDER BY operate_time DESC LIMIT 1) mer_issue_remark, ");
                sb.append(" (SELECT agent_deduct_time FROM survey_order_deduct WHERE operator_type = '4' and order_no = ord.order_no ORDER BY operate_time DESC LIMIT 1) agent_deduct_time,");
                sb.append(" (SELECT agent_deduct_remark FROM survey_order_deduct WHERE operator_type = '4' and order_no = ord.order_no ORDER BY operate_time DESC LIMIT 1) agent_deduct_remark, ");
                sb.append(" (SELECT agent_issue_time FROM survey_order_deduct WHERE operator_type = '5' and order_no = ord.order_no ORDER BY operate_time DESC LIMIT 1) agent_issue_time, ");
                sb.append(" (SELECT agent_issue_remark FROM survey_order_deduct WHERE operator_type = '5' and order_no = ord.order_no ORDER BY operate_time DESC LIMIT 1) agent_issue_remark ");
            }
            sb.append(" from survey_order_info ord ");
            sb.append(" where 1=1 ");
            sb.append("     and  ord.order_status='1' ");
            sb.append("     and  ord.have_add_deduct='1' ");
            if(StringUtils.isNotBlank(detail.getOrderNo())){
                sb.append(" and ord.order_no=#{detail.orderNo}");
            }
            if(StringUtils.isNotBlank(detail.getTransOrderNo())){
                sb.append(" and ord.trans_order_no=#{detail.transOrderNo}");
            }
            if(StringUtils.isNotBlank(detail.getAcqReferenceNo())){
                sb.append(" and ord.acq_reference_no=#{detail.acqReferenceNo}");
            }
            if(StringUtils.isNotBlank(detail.getOrderTypeCode())){
                sb.append(" and ord.order_type_code=#{detail.orderTypeCode}");
            }
            if(StringUtils.isNotBlank(detail.getAgentDeductDealStatus())){
                sb.append(" and ord.agent_deduct_deal_status=#{detail.agentDeductDealStatus}");
            }
            if(StringUtils.isNotBlank(detail.getAgentIssueDealStatus())){
                sb.append(" and ord.agent_issue_deal_status=#{detail.agentIssueDealStatus}");
            }
            if(StringUtils.isNotBlank(detail.getMerchantNo())){
                sb.append(" and ord.merchant_no=#{detail.merchantNo}");
            }
            sb.append(" ORDER BY ord.id desc ");
            return sb.toString();
        }
    }
}
