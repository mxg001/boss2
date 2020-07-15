package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ProviderBean;
import cn.eeepay.framework.model.RepayOemServiceCostBean;
import cn.eeepay.framework.model.RepayProfitDetailBean;

/**
 * Created by 666666 on 2017/10/27.
 */

public interface ProviderDao {
    @SelectProvider(type=SqlProvider.class, method = "listActivationCode")
    List<ProviderBean> listProvider(@Param("bean") ProviderBean providerBean, Page<AgentInfo> page);

    @SelectProvider(type=SqlProvider.class, method = "chechAgentNoIsLevelOne")
    int chechAgentNoIsLevelOne(@Param("list") List<String> agentNoList);

    @InsertProvider(type=SqlProvider.class, method = "openSuperRepayment")
    void openSuperRepayment(@Param("list") List<RepayOemServiceCostBean> wantAddAgent);

    @Update("update yfb_service_cost set rate = #{bean.rate}, single_amount = #{bean.singleAmount}, account_ratio = #{bean.accountRatio}, "
    		+ "full_repay_rate = #{bean.fullRepayRate}, full_repay_single_amount = #{bean.fullRepaySingleAmount}, "
    		+ "perfect_repay_rate = #{bean.perfectRepayRate}, perfect_repay_single_amount = #{bean.perfectRepaySingleAmount} where agent_no = #{bean.agentNo}")
    int updateServiceCost(@Param("bean") ProviderBean bean);

    @Select("SELECT agent_no, trade_fee_rate rate,trade_single_fee singleAmount,oem_no,full_repay_fee_rate fullRepayRate,"
    		+ "full_repay_single_fee fullRepaySingleAmount,perfect_repay_fee_rate perfectRate,perfect_repay_single_fee perfectSingleAmount "
    		+ "FROM yfb_oem_service WHERE agent_no = #{agentNo} AND oem_type = 'repay'")
    RepayOemServiceCostBean queryRepayOemServiceCost(String agentNo);

	@Select("SELECT agent_no, trade_fee_rate rate,trade_single_fee singleAmount,oem_no,full_repay_fee_rate fullRepayRate,"
			+ "full_repay_single_fee fullRepaySingleAmount,perfect_repay_fee_rate perfectRate,perfect_repay_single_fee perfectSingleAmount "
			+ "FROM yfb_oem_service WHERE agent_no = #{agentNo} AND oem_type = #{oemType}")
	RepayOemServiceCostBean queryRepayOemServiceCostByOemType(@Param("agentNo") String agentNo, @Param("oemType")String oemType);

    @SelectProvider(type = SqlProvider.class, method = "listRepayProfitDetail")
	@ResultType(RepayProfitDetailBean.class)
	List<RepayProfitDetailBean> listRepayProfitDetail(@Param("info") RepayProfitDetailBean bean,
			Page<RepayProfitDetailBean> page);

    @SelectProvider(type = SqlProvider.class, method = "listRepayProfitDetail")
	@ResultType(RepayProfitDetailBean.class)
	List<RepayProfitDetailBean> exportRepayProfitDetail(@Param("info") RepayProfitDetailBean bean);

    @SelectProvider(type = SqlProvider.class, method = "sumRepayProfitDetail")
	@ResultType(RepayProfitDetailBean.class)
	RepayProfitDetailBean sumRepayProfitDetail(@Param("info") RepayProfitDetailBean bean);

    @Select("select agent_node from agent_info where agent_no = #{profitMerNo}")
    String queryAgentNode(@Param("profitMerNo") String profitMerNo);

	//添加
	@Insert("insert into agent_oem_info(one_agent_no,oem_type,create_time) values (#{oneAgentNo},'5',now())")
	int insertoneAgentNo( @Param("oneAgentNo")String oneAgentNo);

	//查询下级代理商的最小成本
	@Select("SELECT MIN(ysc.rate) rate, MIN(ysc.single_amount) single_amount,"
			+ "MIN(ysc.full_repay_rate) full_repay_rate, MIN(ysc.full_repay_single_amount) full_repay_single_amount,"
			+ "MIN(ysc.perfect_repay_rate) perfect_repay_rate, MIN(ysc.perfect_repay_single_amount) perfect_repay_single_amount "
			+ "FROM yfb_service_cost ysc LEFT JOIN agent_info ai ON ai.agent_no = ysc.agent_no "
			+ "WHERE ai.one_level_id = #{agentNo} AND ysc.agent_no <> #{agentNo}")
	ProviderBean queryServiceMinCost(String agentNo);

	@Insert("insert into yfb_service_cost(agent_no,service_type,nfc_orig_code)" +
			" values(#{agentNo}, #{serviceType}, UUID())")
	int insertServiceCost(@Param("agentNo") String agentNo, @Param("serviceType") String serviceType);

	class SqlProvider{
        public String listActivationCode(Map<String, Object> param){
            final ProviderBean bean = (ProviderBean) param.get("bean");
            SQL sql = new SQL(){{
                SELECT("ysc.account_ratio, ai.agent_no,ai.agent_name,ai.mobilephone,ysc.rate,ysc.single_amount,ai.agent_level,ai.one_level_id,"
                		+ "ysc.full_repay_rate,ysc.full_repay_single_amount,ysc.perfect_repay_rate,ysc.perfect_repay_single_amount ");
                FROM("agent_info ai");
                LEFT_OUTER_JOIN("yfb_service_cost ysc ON ysc.agent_no = ai.agent_no AND ysc.service_type = 'repay'");
                //WHERE("ai.agent_level = 1");
                if (StringUtils.isNotBlank(bean.getAgentLevel())){
                    WHERE("ai.agent_level = #{bean.agentLevel}");
                }
                if (StringUtils.isNotBlank(bean.getAgentNo())){
                    WHERE("ai.agent_no = #{bean.agentNo}");
                }
                if (StringUtils.isNotBlank(bean.getAgentName())){
                    WHERE("position(#{bean.agentName} in ai.agent_name)");
                }
                if (StringUtils.isNotBlank(bean.getMobilephone())){
                    WHERE("ai.mobilephone = #{bean.mobilephone}");
                }
                ORDER_BY("ai.agent_no");
            }};
            return sql.toString();
        }

        public String chechAgentNoIsLevelOne(Map<String, Object> param){
            final List<String> agentNoList = (List<String>) param.get("list");
            SQL sql = new SQL(){{
                SELECT("count(*)");
                FROM("agent_info ai");
                LEFT_OUTER_JOIN("yfb_service_cost ysc ON ai.agent_no =  ysc.agent_no");
                WHERE("ai.agent_level = 1");
                WHERE("IFNULL(ysc.agent_no, '') = ''");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < agentNoList.size(); i ++){
//                    sb.append("'"+agentNo+"',");
                    sb.append("#{list["+i+"]},");
                }
                sb.deleteCharAt(sb.lastIndexOf(","));
                WHERE("ai.agent_no in ("+sb.toString()+")");
            }};
            return sql.toString();
        }

        public String openSuperRepayment(Map<String, Object> param){
            List<RepayOemServiceCostBean> agentList = (List<RepayOemServiceCostBean>) param.get("list");
            StringBuilder sb = new StringBuilder();
            sb.append("insert into yfb_service_cost(agent_no, rate, single_amount, service_type, full_repay_rate,full_repay_single_amount,perfect_repay_rate,perfect_repay_single_amount) values ");
            for (int i = 0; i < agentList.size(); i ++){
                sb.append("(#{list["+i+"].agentNo}, #{list["+i+"].rate}, #{list["+i+"].singleAmount}, 'repay', #{list["+i+"].fullRepayRate}, #{list["+i+"].fullRepaySingleAmount}, #{list["+i+"].perfectRate}, #{list["+i+"].perfectSingleAmount}),");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            return sb.toString();
        }

		public String listRepayProfitDetail(Map<String, Object> param) {
			final RepayProfitDetailBean info = (RepayProfitDetailBean) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("ypd.profit_no,ypd.order_no,ypd.merchant_no,ypd.trans_time,ai.agent_no profitMerNo,ai.agent_name,ypd.share_amount,ypd.profit_type,ypd.to_profit_amount,yrp.status,yrp.complete_time,"
							+ "yrp.repay_amount,yrp.ensure_amount,yrp.repay_fee,yrp.success_pay_amount,yrp.success_repay_amount,"
							+ "yrp.actual_pay_fee,yrp.actual_withdraw_fee ");
					FROM("yfb_profit_detail ypd");
					LEFT_OUTER_JOIN("yfb_repay_plan yrp ON ypd.order_no = yrp.batch_no");
					LEFT_OUTER_JOIN("agent_info ai ON ypd.profit_mer_no = ai.agent_no AND ypd.profit_mer_type = 'A'");
				}
			};
			whereSql(info, sql);
			sql.ORDER_BY("ypd.create_time DESC");
			return sql.toString();
		}

		public String sumRepayProfitDetail(Map<String, Object> param) {
			final RepayProfitDetailBean info = (RepayProfitDetailBean) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("ifnull(sum(ypd.share_amount),0) share_amount,"
							+ "ifnull(sum(yrp.actual_pay_fee),0) actual_pay_fee,"
							+ "ifnull(sum(yrp.actual_withdraw_fee),0) actual_withdraw_fee");
					FROM("yfb_profit_detail ypd");
					LEFT_OUTER_JOIN("yfb_repay_plan yrp ON ypd.order_no = yrp.batch_no");
					LEFT_OUTER_JOIN("agent_info ai ON ypd.profit_mer_no = ai.agent_no AND ypd.profit_mer_type = 'A'");
				}
			};
			whereSql(info, sql);
			return sql.toString();
		}

		public void whereSql(RepayProfitDetailBean info, SQL sql){
			if(info == null){
				return;
			}
			if (StringUtils.isNotBlank(info.getProfitMerNo())) {
				// sql.WHERE("ai.agent_no = #{info.profitMerNo}");
				if ("1".equals(info.getContainSub())) {
					sql.WHERE("ypd.agent_node like concat(#{info.agentNode},'%') ");
				} else {
					sql.WHERE("ypd.agent_node = #{info.agentNode} ");
				}
			}
			if (StringUtils.isNotBlank(info.getProfitType())) {
				sql.WHERE("ypd.profit_type = #{info.profitType}");
			}
			if (StringUtils.isNotBlank(info.getMinShareAmount())) {
				sql.WHERE("ypd.share_amount >= #{info.minShareAmount}");
			}
			if (StringUtils.isNotBlank(info.getMaxShareAmount())) {
				sql.WHERE("ypd.share_amount <= #{info.maxShareAmount}");
			}
			if (StringUtils.isNotBlank(info.getOrderNo())) {
				sql.WHERE("ypd.order_no = #{info.orderNo}");
			}
			if (StringUtils.isNotBlank(info.getProfitNo())) {
				sql.WHERE("ypd.profit_no = #{info.profitNo}");
			}
			if (StringUtils.isNotBlank(info.getStatus())) {
				sql.WHERE("yrp.status=#{info.status} ");
			}
			if (StringUtils.isNotBlank(info.getsTransTime())) {
				sql.WHERE("ypd.trans_time >= #{info.sTransTime}");
			}
			if (StringUtils.isNotBlank(info.geteTransTime())) {
				sql.WHERE("ypd.trans_time <= #{info.eTransTime}");
			}
		}

    }

}
