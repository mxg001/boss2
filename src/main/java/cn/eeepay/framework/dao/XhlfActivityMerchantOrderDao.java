package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.XhlfActivityMerchantOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * @author tans
 * @date 2019/9/27 11:34
 */
public interface XhlfActivityMerchantOrderDao {

    @Select("select xamo.* from xhlf_activity_merchant_order xamo " +
            " where xamo.activity_target_status = #{activityTargetStatus}" +
            " and #{rewardTime} between xamo.reward_start_time and xamo.reward_end_time" +
            " and xamo.reward_amount > 0")
    @ResultType(XhlfActivityMerchantOrder.class)
    List<XhlfActivityMerchantOrder> queryMerchantNeedCountList(XhlfActivityMerchantOrder queryOrder);

    @Update("update xhlf_activity_merchant_order set " +
            " reward_account_status = #{rewardAccountStatus},activity_target_status = #{activityTargetStatus}," +
            " activity_target_time = #{activityTargetTime} where merchant_no = #{merchantNo}")
    int updateActivityOrder(XhlfActivityMerchantOrder itemOrder);

    @Select("select * from xhlf_activity_merchant_order where activity_target_status in ('1','5') and reward_account_status = '0'" +
            "  and reward_amount > '0' and DATE_FORMAT(#{nowDate},'%Y-%m-%d') = DATE_FORMAT(activity_target_time,'%Y-%m-%d')")
    @ResultType(XhlfActivityMerchantOrder.class)
    List<XhlfActivityMerchantOrder> queryMerchantNeedAccountList(@Param("nowDate") Date nowDate);

    @Update("update xhlf_activity_merchant_order set reward_account_status = #{rewardAccountStatus},reward_account_time = #{rewardAccountTime}," +
            " operator = #{operator} where id = #{id}")
    int updateAccount(XhlfActivityMerchantOrder order);

    @Select("select * from xhlf_activity_merchant_order where merchant_no = #{merchantNo}")
    @ResultType(XhlfActivityMerchantOrder.class)
    XhlfActivityMerchantOrder queryMerchantOrder(@Param("merchantNo") String merchantNo);
}
