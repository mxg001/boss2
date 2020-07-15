package cn.eeepay.framework.dao;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditRepayOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * 信用卡还款订单Dao
 * @author liuks
 * 对应主表 yfb_repay_plan
 * 详情表 yfb_repay_plan_detail
 */
public interface CreditRepayOrderDao {
    /**
     * 通过id查询
     */
	@Select("select "
			+ " t1.id,t1.batch_no,t1.merchant_no,t4.nickname,t2.user_name,t2.mobile_no,t2.id_card_no,t1.repay_num,t1.acq_code, "
			+ " t1.status,t1.repay_amount,t1.ensure_amount,t1.ensure_amount_rate,t1.repay_fee,t1.repay_fee_rate,t3.account_no, "
			+ " t3.bank_name,t1.create_time,t1.repay_begin_time,t1.repay_end_time,t1.success_repay_amount,t1.success_pay_amount, "
			+ " t1.success_repay_num,t1.actual_pay_fee,t1.actual_withdraw_fee,t1.remark as mission,yppi.province_name,yppi.city_name,t1.remark,t1.res_msg "
			+ " FROM yfb_repay_plan t1 "
			+ " LEFT JOIN yfb_repay_merchant_info t2 ON t2.merchant_no = t1.merchant_no "
			+ " LEFT JOIN yfb_card_manage t3 ON t3.card_no = t1.card_no "
			+ " LEFT JOIN yfb_wechat_info t4 ON t4.openid = t2.openid "
			+ " LEFT JOIN yfb_perfect_plan_info yppi ON yppi.order_no = t1.batch_no AND yppi.order_type = 'plan' "
			+ " where t1.batch_no=#{batchNo}")
    @ResultType(CreditRepayOrder.class)
    CreditRepayOrder selectById(@Param("batchNo")String batchNo);

    @Select(
            "select " +
                    " t6.order_no as tally_order_no,t6.status as billing_status "+
                    " FROM yfb_pay_order t5 " +
                    " LEFT OUTER JOIN yfb_tally_his t6 ON (t6.service_order_no = t5.order_no AND t6.service = 'trade') " +
                    " where t5.service_order_no=#{batchNo} AND t5.service = 'ensure' AND t5.trans_status='2' " +
                    "       and t6.order_no=#{tallyOrderNo} "
    )
    @ResultType(CreditRepayOrder.class)
    CreditRepayOrder selectByIdAndTallyOrderNo(@Param("batchNo")String batchNo,@Param("tallyOrderNo")String tallyOrderNo);
    /**
     *根据条件动态查询列表
     */
    @SelectProvider(type=CreditRepayOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(CreditRepayOrder.class)
    List<CreditRepayOrder> selectAllList(@Param("queryaram")CreditRepayOrder order, Page<CreditRepayOrder> page);


    /**
     *根据条件动态查询 统计总金额,
     * 返回的实体只含有统计值
     */
    @SelectProvider(type=CreditRepayOrderDao.SqlProvider.class,method="selectAllListSum")
    @ResultType(CreditRepayOrder.class)
    CreditRepayOrder selectAllListSum(@Param("queryaram")CreditRepayOrder order);

    /**
     *导出数据查询
     */
    @SelectProvider(type=CreditRepayOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(CreditRepayOrder.class)
    List<CreditRepayOrder> importSelectAllList(@Param("queryaram")CreditRepayOrder order);

    @Select("select status from yfb_repay_plan where batch_no = #{batchNo}")
    String selectStatusByBatchNo(@Param("batchNo")String batchNo);

    /**
     * 按条件查询订单（不分页）
     * @param order
     * @return
     */
    @SelectProvider(type=CreditRepayOrderDao.SqlProvider.class,method="selectAbnormalByParam")
    @ResultType(CreditRepayOrder.class)
    List<CreditRepayOrder> selectAbnormalByParam(@Param("queryaram")CreditRepayOrder order);


    class SqlProvider {
        public String selectAllListSum(final Map<String, Object> param){
            return getSQL(param, 1);
        }

        public String selectAllList(final Map<String, Object> param) {
            return getSQL(param, 2);
        }

        public String selectAbnormalByParam(final Map<String, Object> param) {
            return getSQL(param, 2);
        }

        /**
         *
         * @param param
         * @param state 1 统计，2,详情
         * @return
         */
        private String getSQL(final Map<String, Object> param,int state){
            final CreditRepayOrder order = (CreditRepayOrder) param.get("queryaram");
            StringBuffer sb=new StringBuffer();
            if(state==1){
                sb.append(" select ");
                sb.append(" sum(t1.repay_amount) as repay_amount_all, ");
                sb.append(" sum(t1.ensure_amount) as ensure_amount_all, ");
                sb.append(" sum(t1.repay_fee) as repay_fee_all, ");
                sb.append(" sum(if((t1.status=0 or t1.status=3),0,t1.ensure_amount)) as ensure_amount_freezing_all ");
            }else if(state==2){
                sb.append(" select ");
                sb.append(" t1.id,t1.batch_no,t1.merchant_no,t4.nickname,t2.user_name,t2.mobile_no,t1.acq_code, ");
                sb.append(" t1.status,t1.repay_amount,t1.ensure_amount,t1.repay_fee,t3.account_no,t1.repay_num, ");
                sb.append(" t3.bank_name,t1.create_time,t1.repay_begin_time,t1.repay_end_time,t1.repay_fee_rate, ");
                sb.append(" t1.remark as mission,t1.success_repay_amount,t1.success_pay_amount,t1.success_repay_num, ");
                sb.append(" t1.actual_pay_fee,t1.actual_withdraw_fee,t1.complete_time,t1.remark,t1.res_msg, ");
                sb.append(" t6.order_no as tally_order_no,t6.status as billing_status, ");
                sb.append(" ai.agent_name,aii.agent_name one_agent_name,t1.repay_type ");
            }

            String str=new SQL() {
                {
                    SELECT("");
                    FROM("yfb_repay_plan t1");
                    INNER_JOIN("yfb_repay_merchant_info t2 ON t2.merchant_no=t1.merchant_no");
                    LEFT_OUTER_JOIN("yfb_card_manage t3 ON t3.card_no=t1.card_no");
                    LEFT_OUTER_JOIN("yfb_wechat_info t4 ON t4.openid=t2.openid");
                    LEFT_OUTER_JOIN("yfb_pay_order t5 ON (t5.service_order_no=t1.batch_no and t5.service='ensure'  AND t5.trans_status='2' ) ");
                    LEFT_OUTER_JOIN("yfb_tally_his t6 ON (t6.service_order_no=t5.order_no and t6.service='trade' )");
                    INNER_JOIN("agent_info ai ON ai.agent_no = t2.agent_no");
                    INNER_JOIN("agent_info aii ON aii.agent_no = t2.one_agent_no");

                    if(StringUtils.isNotBlank(order.getBatchNo())) {
                        WHERE("t1.batch_no=#{queryaram.batchNo}");
                    }
                    if(StringUtils.isNotBlank(order.getMerchantNo())){
                        WHERE("t1.merchant_no=#{queryaram.merchantNo}");
                    }
                    if (StringUtils.isNotBlank(order.getMobileNo())) {
                        WHERE("t2.mobile_no=#{queryaram.mobileNo}");
                    }
                    if (StringUtils.isNotBlank(order.getAcqCode())) {
                        WHERE("t1.acq_code=#{queryaram.acqCode}");
                    }
                    if (StringUtils.isNotBlank(order.getStatus())) {
                        WHERE("t1.status=#{queryaram.status}");
                    }
                    if (StringUtils.isNotBlank(order.getRepayType())) {
                    	WHERE("t1.repay_type=#{queryaram.repayType}");
                    }

                    if(order.getMinRepayAmount()!=null){
                        WHERE("t1.repay_amount>=#{queryaram.minRepayAmount}");
                    }
                    if(order.getMaxRepayAmount()!=null){
                        WHERE("t1.repay_amount<=#{queryaram.maxRepayAmount}");
                    }

                    if(order.getMinEnsureAmount()!=null){
                        WHERE("t1.ensure_amount>=#{queryaram.minEnsureAmount}");
                    }
                    if(order.getMaxEnsureAmount()!=null){
                        WHERE("t1.ensure_amount<=#{queryaram.maxEnsureAmount}");
                    }

                    if(order.getMinRepayFee()!=null){
                        WHERE("t1.repay_fee>=#{queryaram.minRepayFee}");
                    }
                    if(order.getMaxRepayFee()!=null){
                        WHERE("t1.repay_fee<=#{queryaram.maxRepayFee}");
                    }

                    if(order.getCreateTimeBegin() != null){
                        WHERE("t1.create_time >= #{queryaram.createTimeBegin}");
                    }
                    if(order.getCreateTimeEnd() != null){
                        WHERE("t1.create_time <= #{queryaram.createTimeEnd}");
                    }
                    if (StringUtils.isNotBlank(order.getBillingStatus())) {
                        WHERE("t6.status=#{queryaram.billingStatus}");
                    }
                    if(StringUtils.isNotBlank(order.getsCompleteTime())){
                        WHERE("t1.complete_time >= #{queryaram.sCompleteTime}");
                    }
                    if(StringUtils.isNotBlank(order.geteCompleteTime())){
                        WHERE("t1.complete_time <= #{queryaram.eCompleteTime}");
                    }

					if (StringUtils.isNotBlank(order.getAgentNode())) {
						if ("1".equals(order.getContainSub())) {
							WHERE("t2.agent_node like concat(#{queryaram.agentNode},'%') ");
						} else {
							WHERE("t2.agent_node=#{queryaram.agentNode} ");
						}
					}
                }
            }.toString().replace("SELECT","");
            sb.append(str);
            if(state==2){
                sb.append(" ORDER BY t1.create_time DESC,t1.id ASC ");
            }
            return sb.toString();
        }
    }
}
