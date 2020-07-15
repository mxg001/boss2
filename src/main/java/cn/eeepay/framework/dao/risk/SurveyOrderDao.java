package cn.eeepay.framework.dao.risk;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.risk.DeductAddInfo;
import cn.eeepay.framework.model.risk.Reminder;
import cn.eeepay.framework.model.risk.SurveyOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/7/007.
 * @author  liuks
 * 调单管理
 */

public interface SurveyOrderDao {

    @SelectProvider(type=SurveyOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(SurveyOrder.class)
    List<SurveyOrder> selectAllList(@Param("order") SurveyOrder order,@Param("page") Page<SurveyOrder> page);

    @SelectProvider(type=SurveyOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(SurveyOrder.class)
    List<SurveyOrder> importDetailSelect(@Param("order")SurveyOrder order);

    @Insert(
            "<script> INSERT INTO " +
                    " survey_order_info " +
                    " (order_no,merchant_no,trans_order_no,acq_reference_no," +
                    "  trans_order_database, order_service_code,order_type_code," +
                    "  reply_end_time,template_files_name,order_remark," +
                    "  reply_status,deal_status,agent_node,trans_amount," +
                    "  trans_account_no,trans_time,acq_code,acq_merchant_no," +
                    "  agent_no,one_agent_no,pa_user_no,user_node," +
                    "  pay_method,trans_status,creator,create_time) " +
                    " VALUES " +
                    "<foreach  collection=\"list\" item=\"info\" index=\"index\" separator=\",\" > " +

                    "(#{info.orderNo},#{info.merchantNo},#{info.transOrderNo},#{info.acqReferenceNo}," +
                    " #{info.transOrderDatabase},#{info.orderServiceCode},#{info.orderTypeCode}," +
                    " #{info.replyEndTime},#{info.templateFilesName},#{info.orderRemark}," +
                    " #{info.replyStatus},#{info.dealStatus},#{info.agentNode},#{info.transAmount}," +
                    " #{info.transAccountNo},#{info.transTime},#{info.acqCode},#{info.acqMerchantNo}," +
                    " #{info.agentNo},#{info.oneAgentNo},#{info.paUserNo},#{info.userNode}," +
                    " #{info.payMethod},#{info.transStatus},#{info.creator},now())" +

                    " </foreach ></script>"
    )
    int addSurveyOrder(@Param("list")List<SurveyOrder> list);

    @Select(
            " select * from survey_order_info where" +
                    "  order_status='1' " +
                    "  and order_type_code=#{order.orderTypeCode} " +
                    "  and find_in_set(trans_order_no,#{order.transOrderNo}) "
    )
    List<SurveyOrder> checkOrder(@Param("order")SurveyOrder order);

    @Select(
            "select * from survey_order_info where id=#{id}"
    )
    SurveyOrder getSurveyOrder(@Param("id")int id);

    @Update(
            "update survey_order_info set order_status='0' where id=#{id}"
    )
    int deleteSurveyOrder(@Param("id")int id);

    @Update(
            "update survey_order_info set urge_num=urge_num+1 where id=#{id}"
    )
    int reminder(@Param("id")int id);

    @Insert(
            " INSERT INTO survey_urge_record " +
                    " (order_no,agent_node,operator,create_time) " +
                    "VALUES" +
                    " (#{re.orderNo},#{re.agentNode},#{re.operator},NOW())"
    )
    void insertReminderLog(@Param("re")Reminder re);

    @Update(
            "update survey_order_info " +
                    "set deal_status=#{order.dealStatus},reply_status='0',deal_remark=#{order.dealRemark},final_have_look_no='' " +
                    " where id=#{order.id}"
    )
    int regresses(@Param("order")SurveyOrder order);

    @Update(
            "update survey_order_info set deal_status=#{order.dealStatus},deal_remark=#{order.dealRemark} where id=#{order.id}"
    )
    int handle(@Param("order")SurveyOrder order);

    @Update(
        "update survey_urge_record set status='0' where order_no=#{orderNo} "
    )
    void regressesUpdateReminderLog(@Param("orderNo") String orderNo);

    @Update(
            "update survey_order_info set have_add_deduct='1' where id=#{id}"
    )
    int updateDeduct(@Param("id")int id);

    @Select(
            "select " +
                    " ord.*," +
                    " mer.merchant_name merchantName,mer.mobilephone merchantPhone," +
                    " agent1.sale_name saleName,am.acq_merchant_name acqMerchantName " +
                    " from survey_order_info ord " +
                    "  LEFT JOIN merchant_info mer ON mer.merchant_no=ord.merchant_no " +
                    "  LEFT JOIN agent_info agent1 ON agent1.agent_no=mer.one_agent_no " +
                    "  LEFT JOIN acq_merchant am on am.acq_merchant_no=ord.acq_merchant_no" +
                    " where ord.id=#{id}"
    )
    SurveyOrder getSurveyOrderDetail(@Param("id")int id);


    @Select(
            "select * from survey_order_info where order_no=#{orderNo}"
    )
    SurveyOrder selectSurveyOrder(@Param("orderNo") String orderNo);

    @Update(
        "update survey_order_info " +
                "set agent_deduct_deal_status=#{info.selectSta},agent_deduct_deal_remark=#{info.remark}" +
                " where order_no=#{info.orderNo} "
    )
    int updateAgentState1(@Param("info")DeductAddInfo info);

    @Update(
            "update survey_order_info " +
                    "set agent_issue_deal_status=#{info.selectSta},agent_issue_deal_remark=#{info.remark}" +
                    " where order_no=#{info.orderNo} "
    )
    int updateAgentState2(@Param("info")DeductAddInfo info);

    @Update(
            " update survey_order_info set reply_status='3' where reply_status='0' and reply_end_time<=NOW() "
    )
    int updateOrderStateOverdue();

    @Select(
            "select @rowNo:=@rowNo+1 rowNo, a.* from survey_urge_record a ,(select @rowNo:=0) b " +
                    " where a.order_no=#{orderNo} " +
                    "  ORDER BY a.create_time desc "
    )
    List<Reminder> getRecordList(@Param("orderNo")String orderNo);

    @Update(
            "update survey_order_info " +
                    " set acq_reply_status=#{info.acqReplyStatus},acq_reply_remark=#{info.acqReplyRemark}" +
                    " where id=#{info.id}"
    )
    int upstream(@Param("info")SurveyOrder order);

    @Select("select * from survey_order_info where id=#{id}")
    @ResultType(SurveyOrder.class)
    SurveyOrder selectById(@Param("id") Integer id);

    class SqlProvider{

        public String selectAllList(final Map<String, Object> param) {
            SurveyOrder order = (SurveyOrder) param.get("order");
            return selectSQL(order,1);
        }

        public String selectSQL(SurveyOrder order,int sta){

            StringBuffer sb=new StringBuffer();
            sb.append("select ");
            if(sta==1){
                sb.append(" ord.*, ");
                sb.append(" agent.agent_no agentNo,agent.agent_name agentName, ");
                sb.append(" if(ord.order_service_code='3',ord.merchant_no,mer.merchant_name) merchantName, ");
                sb.append(" agent1.agent_no oneAgentNo,agent1.agent_name oneAgentName,agent1.sale_name saleName ");
                sb.append(" from survey_order_info ord ");
                sb.append("    LEFT JOIN merchant_info mer ON mer.merchant_no=ord.merchant_no ");
                sb.append("    LEFT JOIN agent_info agent ON agent.agent_no=ord.agent_no ");
                sb.append("    LEFT JOIN agent_info agent1 ON agent1.agent_no=ord.one_agent_no ");
            }
            sb.append(" where 1=1 ");
            sb.append("     and ord.order_status='1' ");
            if(StringUtils.isNotBlank(order.getOrderNo())){
                sb.append(" and ord.order_no=#{order.orderNo}");
            }
            if(StringUtils.isNotBlank(order.getTransOrderNo())){
                sb.append(" and ord.trans_order_no=#{order.transOrderNo}");
            }
            if(StringUtils.isNotBlank(order.getAcqReferenceNo())){
                sb.append(" and ord.acq_reference_no=#{order.acqReferenceNo}");
            }
            if(StringUtils.isNotBlank(order.getTransAccountNo())){
                sb.append(" and ord.trans_account_no=#{order.transAccountNo}");
            }
            if(StringUtils.isNotBlank(order.getAcqCode())){
                sb.append(" and ord.acq_code=#{order.acqCode}");
            }
            if(StringUtils.isNotBlank(order.getOrderTypeCode())){
                sb.append(" and ord.order_type_code=#{order.orderTypeCode}");
            }
            if(StringUtils.isNotBlank(order.getReplyStatus())){
                sb.append(" and ord.reply_status=#{order.replyStatus}");
            }
            if(StringUtils.isNotBlank(order.getDealStatus())){
                sb.append(" and ord.deal_status=#{order.dealStatus}");
            }
            if(StringUtils.isNotBlank(order.getHaveAddDeduct())){
                sb.append(" and ord.have_add_deduct=#{order.haveAddDeduct}");
            }
            if(StringUtils.isNotBlank(order.getMerchantNo())){
                sb.append(" and ord.merchant_no=#{order.merchantNo}");
            }
            if(StringUtils.isNotBlank(order.getPaUserNo())){
                sb.append(" and ord.pa_user_no=#{order.paUserNo}");
            }
            if(StringUtils.isNotBlank(order.getAgentNo())){
                if(order.getBool()!=null){
                    if(order.getBool().intValue()==1){
                        sb.append(" and ord.agent_node LIKE (SELECT CONCAT(agent_node,'%') FROM agent_info a where a.agent_no=#{order.agentNo})");
                    }else{
                        sb.append(" and ord.agent_node = (SELECT agent_node FROM agent_info a where a.agent_no=#{order.agentNo})");
                    }
                }else{
                    sb.append(" and ord.agent_node = (SELECT agent_node FROM agent_info a where a.agent_no=#{order.agentNo})");
                }
            }
            if(StringUtils.isNotBlank(order.getOneAgentNo())){
                sb.append(" and agent1.agent_no=#{order.oneAgentNo}");
            }
            if(StringUtils.isNotBlank(order.getPayMethod())){
                sb.append(" and ord.pay_method=#{order.payMethod}");
            }
            if(StringUtils.isNotBlank(order.getOrderServiceCode())){
                sb.append(" and ord.order_service_code=#{order.orderServiceCode}");
            }
            if(StringUtils.isNotBlank(order.getAcqMerchantNo())){
                sb.append(" and ord.acq_merchant_no=#{order.acqMerchantNo}");
            }
            if(order.getCreateTimeBegin() != null){
                sb.append("  and ord.create_time >= #{order.createTimeBegin}");
            }
            if(order.getCreateTimeEnd() != null){
                sb.append("  and ord.create_time <= #{order.createTimeEnd}");
            }
            if(order.getReplyEndTimeBegin() != null){
                sb.append("  and ord.reply_end_time >= #{order.replyEndTimeBegin}");
            }
            if(order.getReplyEndTimeEnd() != null){
                sb.append("  and ord.reply_end_time <= #{order.replyEndTimeEnd}");
            }

            if(StringUtils.isNotBlank(order.getCreator())){
                sb.append(" and ord.creator=#{order.creator}");
            }
            if(StringUtils.isNotBlank(order.getAcqReplyStatus())){
                sb.append(" and ord.acq_reply_status=#{order.acqReplyStatus}");
            }
            if(StringUtils.isNotBlank(order.getTransStatus())){
                sb.append(" and ord.trans_status=#{order.transStatus}");
            }
            if(order.getLastReplyTimeBegin() != null){
                sb.append("  and ord.last_reply_time >= #{order.lastReplyTimeBegin}");
            }
            if(order.getLastReplyTimeEnd() != null){
                sb.append("  and ord.last_reply_time <= #{order.lastReplyTimeEnd}");
            }
            sb.append(" order by ord.id desc ");
            return sb.toString();
        }
    }
}
