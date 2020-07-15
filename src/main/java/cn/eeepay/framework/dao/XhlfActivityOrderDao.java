package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.*;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author tans
 * @date 2019/9/26 10:59
 */
public interface XhlfActivityOrderDao {

    /**
     * 代理商7方式统计同一天一张卡涮多个商户，按激活顺序给代理商奖励，后面激活的不统计这卡
     * @param queryOrder
     * @return
     */
    @Select("select xao.* from xhlf_activity_order xao " +
            " where xao.current_target_status = #{info.currentTargetStatus} and (xao.xhlf_activity_type=1 or (xao.xhlf_activity_type=2 and xao.current_cycle>=2))" +
            " and #{info.rewardTime} between xao.reward_start_time and xao.reward_end_time order by xao.active_time ASC ")
    @ResultType(XhlfActivityOrder.class)
    List<XhlfActivityOrder> queryNeedCountList(@Param("info") XhlfActivityOrder queryOrder);

    @Select("select sum(cto.trans_amount) from collective_trans_order cto  " +
            " where cto.merchant_no = #{info.merchantNo} and cto.pay_method = '1' and cto.trans_status = 'SUCCESS' " +
            " and cto.order_type in ('0', '5') " +
            " and cto.trans_time BETWEEN #{info.transStartTime} and #{info.transEndTime};")
    @ResultType(BigDecimal.class)
    BigDecimal queryMerchantTransTotal(@Param("info") XhlfActivityOrder info);

    @Select("select sum(cto.trans_amount) from collective_trans_order cto " +
            " INNER JOIN add_creaditcard_log acl on acl.account_no = cto.account_no " +
            " and acl.merchant_no = cto.merchant_no and cto.trans_time >= acl.create_time" +
            " where cto.merchant_no = #{info.merchantNo} and cto.pay_method = '1' and cto.trans_status = 'SUCCESS' " +
            " and cto.order_type in ('0', '5') " +
            " and cto.trans_time BETWEEN #{info.transStartTime} and #{info.transEndTime};")
    @ResultType(BigDecimal.class)
    BigDecimal queryTransTotal(@Param("info") XhlfActivityOrder info);

    @Select("select sum(cto.trans_amount) from collective_trans_order cto  " +
            " where cto.merchant_no = #{info.merchantNo} " +
            " and ((cto.pay_method = '1' and cto.card_type='1')||(cto.pay_method in ('2','3','5','6','7'))) " +
            " and cto.trans_status = 'SUCCESS' " +
            " and cto.order_type in ('0', '5') " +
            " and cto.trans_time BETWEEN #{info.transStartTime} and #{info.transEndTime};")
    @ResultType(BigDecimal.class)
    BigDecimal querySmartTransTotal(@Param("info") XhlfActivityOrder info);

    @Update("update xhlf_activity_order set current_target_status = #{currentTargetStatus},current_target_time = #{currentTargetTime}," +
            " reward_account_status = #{rewardAccountStatus}" +
            " where id = #{id}")
    int updateCurrentOrder(XhlfActivityOrder itemOrder);

    @Update("update xhlf_activity_order set " +
            " current_target_status = #{currentTargetStatus},current_target_time = #{nowDate}" +
            " where merchant_no = #{merchantNo} and current_cycle=#{currentCycle} and current_target_status=#{oldCurrentTargetStatus}")
    int updateActivityOrderCurrentTargetStatus(@Param("merchantNo") String merchantNo,@Param("nowDate")Date nowDate,
                                               @Param("currentTargetStatus") String currentTargetStatus,
                                               @Param("currentCycle") String currentCycle,
                                               @Param("oldCurrentTargetStatus") String oldCurrentTargetStatus);

    @Update("update xhlf_activity_order set activity_target_status = #{activityTargetStatus}" +
            " where merchant_no = #{merchantNo}")
    int updateOrder(XhlfActivityOrder itemOrder);

    @Update("update xhlf_activity_order set current_target_status = #{currentTargetStatus}" +
            " where merchant_no = #{merchantNo} and reward_end_time >= #{rewardEndTime} and current_cycle>=#{currentCycle}")
    int updateCurrenAndAfterOrder(XhlfActivityOrder itemOrder);

    @Update("update xhlf_activity_order set current_target_status = #{currentTargetStatus}" +
            " where merchant_no = #{merchantNo} and reward_end_time >= #{rewardEndTime} and current_cycle=#{currentCycle}")
    int updateCurrenOrder(XhlfActivityOrder itemOrder);


    @Update("update xhlf_activity_order set activity_target_status = #{activityTargetStatus},activity_target_time = #{activityTargetTime}" +
            " where merchant_no = #{merchantNo}")
    int updateActivityOrder(XhlfActivityOrder itemOrder);

    @Insert("replace into xhlf_merchant_trans_total_day(merchant_no,total_day,total_amount,type)" +
            " values(#{merchantNo},#{totalDay},#{totalAmount},#{type})")
    int insertMerchantTransTotalRecord(XhlfMerchantTransTotalDay xhlfMerchantTransTotalDay);

    @Select("select * from xhlf_activity_order " +
            " where merchant_no = #{merchantNo} and reward_start_time > #{transEndTime} limit 1")
    @ResultType(XhlfActivityOrder.class)
    XhlfActivityOrder queryLastOrder(XhlfActivityOrder itemOrder);

    @Update("update xhlf_activity_order set current_target_status = '1' where current_target_status = '0' " +
            " and #{nowDate} between reward_start_time and reward_end_time and activity_target_status = '0'")
    int updateCurrentOrderChecking(@Param("nowDate") Date nowDate);

    @Select("select * from xhlf_activity_order where current_target_status in ('2','4') and reward_account_status = '0'" +
            " and DATE_FORMAT(#{nowDate},'%Y-%m-%d') = DATE_FORMAT(current_target_time,'%Y-%m-%d')")
    @ResultType(XhlfActivityOrder.class)
    List<XhlfActivityOrder> queryNeedAccountList(@Param("nowDate") Date nowDate);

    @Select("select xaad.*, xao.merchant_no,actiType.sub_type from xhlf_agent_account_detail xaad" +
            " left join xhlf_activity_order xao on xao.id = xaad.xhlf_activity_order_id" +
            " LEFT JOIN xhlf_activity_record rec ON rec.merchant_no=xao.merchant_no "+
            " LEFT JOIN activity_hardware_type actiType ON actiType.activity_type_no=rec.activity_type_no "+
            " where xaad.xhlf_activity_order_id = #{id} and xaad.account_status = '0'" +
            " order by xaad.agent_level+0")
    @ResultType(XhlfAgentAccountDetail.class)
    List<XhlfAgentAccountDetail> queryNeedAccountDetail(Integer id);

    @Update("update xhlf_agent_account_detail set account_status = #{accountStatus} where xhlf_activity_order_id = #{xhlfActivityOrderId} and account_status = #{oldAccountStatus}")
    int updateAccountDetailByActivityOrderId(XhlfAgentAccountDetail accountDetail);

    @Update("update xhlf_activity_order set reward_account_status = #{rewardAccountStatus},reward_account_time=#{rewardAccountTime}," +
            "operator = #{operator} where id = #{id}")
    int updateOrderAccount(XhlfActivityOrder order);

    @Select("select amount from xhlf_agent_account_detail where xhlf_activity_order_id = #{xhlfActivityOrderId} and agent_no = #{agentNo} limit 1")
    @ResultType(BigDecimal.class)
    BigDecimal queryParentAount(@Param("agentNo") String agentNo, @Param("xhlfActivityOrderId") Integer xhlfActivityOrderId);

    @Update("update xhlf_agent_account_detail set remark = #{remark} where id = #{id}")
    int updateAccountDetailRemark(XhlfAgentAccountDetail accountDetail);

    @Update("update xhlf_agent_account_detail set account_status = #{accountStatus},account_time=#{accountTime}," +
            "operator=#{operator}, remark = #{remark} where id = #{id}")
    int updateAccountDetail(XhlfAgentAccountDetail accountDetail);

    @Update("update xhlf_agent_account_detail set account_status = #{rewardAccountStatus},account_time=#{rewardAccountTime}" +
            " where xhlf_activity_order_id = #{id}")
    int updateAccountDetailByXhlfActivityOrder(XhlfActivityOrder xhlfActivityOrder);

    @Select("select sum(total_amount) as totalAmount from xhlf_merchant_trans_total_day where merchant_no = #{merchantNo}" +
            " and type = #{type} and total_day between DATE_FORMAT(#{startDay},'%Y-%m-%d') and #{endDay}")
    BigDecimal selectMerchantSumAount(XhlfMerchantTransTotalDay info);

    @Select("select * from xhlf_merchant_trans_total_day where merchant_no = #{merchantNo} " +
            " and type = #{type} and total_day = DATE_FORMAT(#{endTime},'%Y-%m-%d')")
    @ResultType(XhlfMerchantTransTotalDay.class)
    XhlfMerchantTransTotalDay queryMerchantTransTotalDay(@Param("merchantNo")String merchantNo,
                                                         @Param("type")String type, @Param("endTime")Date endTime);

    @Select("select xao.* from xhlf_activity_order xao where xao.merchant_no = #{merchantNo} and current_cycle = #{currentCycle} and current_target_status = #{currentTargetStatus}")
    @ResultType(XhlfActivityOrder.class)
    XhlfActivityOrder queryActivityOrder(@Param("merchantNo")String merchantNo, @Param("currentCycle")String currentCycle, @Param("currentTargetStatus")String currentTargetStatus);


    @Select("select cto.trans_amount,cto.account_no from collective_trans_order cto " +
            " where cto.merchant_no = #{info.merchantNo} and cto.pay_method = '1' and cto.trans_status = 'SUCCESS' " +
            " and cto.order_type in ('0', '5') and cto.card_type = '1'" +
            " and cto.trans_time BETWEEN #{info.transStartTime} and #{info.transEndTime} " +
            " and not exists (" +
            "   select 1 from xhlf_merchant_trans_card card where card.account_no = cto.account_no and card.merchant_no <> #{info.merchantNo}" +
            "   and card.type = #{info.agentTransTotalType}" +
            ")")
    @ResultType(CollectiveTransOrder.class)
    List<CollectiveTransOrder> queryMerchantTransListType7(@Param("info") XhlfActivityOrder order);

    @Select("select cto.trans_amount,cto.account_no from collective_trans_order cto " +
            " inner join add_creaditcard_log acl on cto.merchant_no = acl.merchant_no and acl.account_no = cto.account_no and cto.trans_time >= acl.create_time" +
            " where cto.merchant_no = #{info.merchantNo} and cto.pay_method = '1' and cto.trans_status = 'SUCCESS' " +
            " and cto.order_type in ('0', '5') and cto.card_type = '1'" +
            " and cto.trans_time BETWEEN #{info.transStartTime} and #{info.transEndTime} " +
            " and not exists (" +
            "   select 1 from xhlf_merchant_trans_card card where card.account_no = cto.account_no and card.merchant_no <> #{info.merchantNo}" +
            ")")
    @ResultType(CollectiveTransOrder.class)
    List<CollectiveTransOrder> queryMerchantTransListType9(@Param("info") XhlfActivityOrder order);


    @Insert("<script>" +
            "insert into xhlf_merchant_trans_card ( merchant_no,active_order,type,account_no) values " +
            " <foreach collection=\"list\" item=\"item\" index=\"index\"  "+
            "         separator=\",\"> "+
            "(#{order.merchantNo},#{order.activeOrder},#{order.agentTransTotalType},#{item.accountNo})"+
            "   </foreach> "+
            "</script>"
    )
    int insertxhlfMerchantTransCard(@Param("order") XhlfActivityOrder order, @Param("list")List<XhlfMerchantTransCard> list);


    @Select("select * from xhlf_merchant_trans_card where account_no = #{accountNo} and type = #{order.agentTransTotalType} limit 1")
    @ResultType(XhlfMerchantTransCard.class)
    XhlfMerchantTransCard queryMerchantTransCard(@Param("order") XhlfActivityOrder order, @Param("accountNo") String accountNo);


    @Select("select sum(cto.trans_amount) from collective_trans_order cto  " +
            " where cto.merchant_no = #{info.merchantNo} and cto.pay_method = '1' and cto.trans_status = 'SUCCESS' " +
            " and cto.order_type in ('0', '5') and cto.card_type = '1'" +
            " and cto.trans_time BETWEEN #{info.transStartTime} and #{info.transEndTime};")
    @ResultType(BigDecimal.class)
    BigDecimal queryMerchantCreditTransTotal(@Param("info") XhlfActivityOrder info);

}
