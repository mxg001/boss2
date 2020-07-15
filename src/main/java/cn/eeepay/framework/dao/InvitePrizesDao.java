package cn.eeepay.framework.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.InvitePrizesConfig;
import cn.eeepay.framework.model.InvitePrizesMerchantInfo;

/**
 * 邀请有奖
 * 
 * @author tans
 * @date 2017年8月19日 上午11:14:18
 */
public interface InvitePrizesDao {

	@SelectProvider(type = SqlProvider.class, method = "getAgentListByParam")
	@ResultType(InvitePrizesConfig.class)
	List<InvitePrizesConfig> getAgentListByParam(@Param("baseInfo")InvitePrizesConfig baseInfo, @Param("page")Page<InvitePrizesConfig> page);

	@Update("UPDATE `invite_prizes_config` SET `start_date`=#{startDate}, `end_date`=#{endDate}, `operator`=#{operator}"
			+" WHERE (`id`=#{id} AND agent_no=#{agentNo} )")
	int updateAgentActivityDate(InvitePrizesConfig baseInfo);

	@Insert("INSERT INTO `nposp`.`invite_prizes_config` ( `agent_no`, `start_date`, `end_date`, `activity_action`, `operator`) "
			 + " VALUES ( #{agentNo}, #{startDate}, #{endDate}, #{activityAction},  #{operator})")
	int insertAgent(InvitePrizesConfig baseInfo);

	@DeleteProvider(type=SqlProvider.class, method="deleteBatch")
	int deleteBatch(@Param("agentNoList")List<String> agentNoList);

	@Select("select count(1) from invite_prizes_config where agent_no = #{agentNo} and activity_action=#{activityAction}")
	int existsAgentNo(InvitePrizesConfig baseInfo);

	@SelectProvider(type = SqlProvider.class, method = "selectInvitePrizesByParam")
	@ResultType(InvitePrizesMerchantInfo.class)
	List<InvitePrizesMerchantInfo> selectInvitePrizesByParam(@Param("page") Page<InvitePrizesMerchantInfo> page,
			@Param("info") InvitePrizesMerchantInfo info);

	@SelectProvider(type = SqlProvider.class, method = "selectInvitePrizesByParam")
	@ResultType(InvitePrizesMerchantInfo.class)
	List<InvitePrizesMerchantInfo> exportInvitePrizesByParam(@Param("info") InvitePrizesMerchantInfo info);

	@InsertProvider(type=SqlProvider.class, method="insertAgentBatch")
	int insertAgentBatch(@Param("list")List<InvitePrizesConfig> list);

	@SelectProvider(type = SqlProvider.class, method = "countInvitePrizesMerchant")
	Map<String, String> countInvitePrizesMerchant(@Param("info") InvitePrizesMerchantInfo info);

	@Select("SELECT ipmi.*,ai.one_level_id,ai.agent_no FROM invite_prizes_merchant_info ipmi "
			+ "INNER JOIN agent_info ai ON ai.agent_node = ipmi.agent_node WHERE ipmi.id=#{id}")
	@ResultType(InvitePrizesMerchantInfo.class)
	InvitePrizesMerchantInfo queryInvitePrizesMerchantInfo(@Param("id")Integer id);

	@Update("update invite_prizes_merchant_info set account_status=#{accountStatus}, account_time=#{accountTime}, operator=#{operator} where id=#{id}")
	int updateAccountStatus(InvitePrizesMerchantInfo info);

	public class SqlProvider{

		public String getAgentListByParam(Map<String, Object> param){
			final InvitePrizesConfig baseInfo = (InvitePrizesConfig) param.get("baseInfo");
			String sql = new SQL(){{
				SELECT("ipc.id, ipc.agent_no, ai.agent_name, ipc.start_date, ipc.end_date");
				FROM("invite_prizes_config ipc");
				INNER_JOIN("agent_info ai on ai.agent_no = ipc.agent_no");
				if(baseInfo!=null && StringUtils.isNotBlank(baseInfo.getAgentNo())){
					WHERE("ipc.agent_no = #{baseInfo.agentNo}");
				}
				if(baseInfo!=null && baseInfo.getActivityStatus()!=null){
					//未开始
					if(baseInfo.getActivityStatus()==0){
						WHERE("ipc.start_date > #{baseInfo.currentDate}");
					}
					//进行中
					if(baseInfo.getActivityStatus()==1){
						WHERE("ipc.start_date <= #{baseInfo.currentDate} and ipc.end_date >= #{baseInfo.currentDate}");			
					}
					//已结束
					if(baseInfo.getActivityStatus()==2){
						WHERE("ipc.end_date < #{baseInfo.currentDate}");
					}
				}
				ORDER_BY("ipc.create_time desc");
			}}.toString();
			return sql;
		}

		public String deleteBatch(Map<String, Object> param){
    		List<String> list =  (List<String>) param.get("agentNoList");
    		//进行中的不能删除
    		StringBuffer sb = new StringBuffer("delete from invite_prizes_config where !(DATEDIFF(now(),start_date)>=0 AND DATEDIFF(now(),end_date)<=0) and agent_no in(");
    		MessageFormat message = new MessageFormat("#'{'agentNoList[{0}]},");
    		for(int i=0; i<list.size(); i++){
    			sb.append(message.format(new Integer[]{i}));
    		}
    		sb.setLength(sb.length()-1);
    		sb.append(")");
    		System.out.println(sb.toString());
    		return sb.toString();
    	}

		public String insertAgentBatch(Map<String, Object> param){
			List<InvitePrizesConfig> list = (List<InvitePrizesConfig>) param.get("list");
			StringBuilder values = new StringBuilder();
			MessageFormat message = new MessageFormat("(#'{'list[{0}].agentNo}, #'{'list[{0}].startDate}, #'{'list[{0}].endDate}, #'{'list[{0}].activityAction},"
					+ " #'{'list[{0}].operator}),");
			for(int i = 0; i < list.size(); i++){
				values.append(message.format(new Integer[]{i}));
			}
			values.setLength(values.length() - 2);//去掉最后一个逗号和括号
			final String valuesSql  = values.substring(1);//去掉最前面那个括号
			String sql = new SQL(){{
				INSERT_INTO("invite_prizes_config");
				VALUES("`agent_no`, `start_date`, `end_date`, `activity_action`, `operator`", valuesSql);
				
			}}.toString();
			System.out.println("批量插入代理商sql:" + sql);
			return sql;
		}

		public String selectInvitePrizesByParam(Map<String, Object> param) {
			final InvitePrizesMerchantInfo info = (InvitePrizesMerchantInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("ipmi.*,mi.merchant_name,ai.agent_name,ai.agent_no,ai.one_level_id,bsu.real_name ");
					FROM("invite_prizes_merchant_info ipmi "
							+ "LEFT JOIN merchant_info mi ON mi.merchant_no = ipmi.merchant_no "
							+ "LEFT JOIN agent_info ai ON ai.agent_node = ipmi.agent_node "
							+ "LEFT JOIN boss_shiro_user bsu ON bsu.id = ipmi.operator ");
				}
			};
			whereSql(info, sql);
			sql.ORDER_BY(" create_time DESC");
			return sql.toString();
		}

		public String countInvitePrizesMerchant(Map<String, Object> param) {
			final InvitePrizesMerchantInfo info = (InvitePrizesMerchantInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("SUM(ipmi.prizes_amount) sumAmount,"
							+ "SUM(IF(ipmi.account_status=0,ipmi.prizes_amount, 0)) unrecordedAmount,"
							+ "SUM(IF(ipmi.account_status=1,ipmi.prizes_amount, 0)) recordedAmount");
					FROM("invite_prizes_merchant_info ipmi "
							+ "LEFT JOIN merchant_info mi ON mi.merchant_no = ipmi.merchant_no "
							+ "LEFT JOIN agent_info ai ON ai.agent_node = ipmi.agent_node ");
				}
			};
			whereSql(info, sql);
			return sql.toString();
		}

		public void whereSql(InvitePrizesMerchantInfo info,SQL sql){
			if(info == null){
				return;
			}
			if (StringUtils.isNotBlank(info.getMerchantNo())) {
				sql.WHERE(" ipmi.merchant_no=#{info.merchantNo} ");
			}

			if (StringUtils.isNotBlank(info.getPrizesObject())) {
				sql.WHERE(" ipmi.prizes_object=#{info.prizesObject} ");
			}
			if (StringUtils.isNotBlank(info.getAgentNode())) {
				if ("1".equals(info.getContainSub())) {
					sql.WHERE(" ipmi.agent_node like concat(#{info.agentNode},'%') ");
				} else {
					sql.WHERE(" ipmi.agent_node=#{info.agentNode} ");
				}
			}
			if (StringUtils.isNotBlank(info.getAccountStatus())) {
				sql.WHERE(" ipmi.account_status=#{info.accountStatus} ");
			}
			if (StringUtils.isNotBlank(info.getStartCreateTime())) {
				sql.WHERE(" ipmi.create_time >= #{info.startCreateTime} ");
			}
			if (StringUtils.isNotBlank(info.getEndCreateTime())) {
				sql.WHERE(" ipmi.create_time <= #{info.endCreateTime} ");
			}
			if (StringUtils.isNotBlank(info.getPrizesType())) {
				sql.WHERE(" ipmi.prizes_type = #{info.prizesType} ");
			}
		}

	}

}
