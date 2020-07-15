package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TerminalApply;

public interface TerminalApplyDao {

	@SelectProvider(type=SqlProvider.class,method="queryAllInfo")
	@ResultType(TerminalApply.class)
	List<TerminalApply> queryAllInfo(Page<TerminalApply> page,@Param("terminalApply")TerminalApply terminalApply);
	
	@Select("SELECT ta.*, mis.merchant_name,CONCAT(type_name,version_nu) as hp_name,ais.agent_name as agent_name, "
			+ "ais.agent_no as agent_no, ais1.agent_name as one_agent_name,ais1.agent_no as one_agent_no,"
			+ "(case ais.team_id when '100010' then 1 else 0 end) as teamId "
			+ "FROM terminal_apply ta "
			+ "LEFT JOIN merchant_info mis ON mis.merchant_no = ta.merchant_no "
			+ "LEFT JOIN agent_info ais ON ais.agent_no = mis.agent_no "
			+ "LEFT JOIN agent_info ais1 ON ais1.agent_no=mis.one_agent_no "
			+ "LEFT JOIN hardware_product hp on hp.hp_id=ta.product_type "
			+ "where ta.id=#{id}")
	@ResultType(TerminalApply.class)
	TerminalApply queryInfoDetail(@Param("id")String id);
	
	@Update("update terminal_apply set status='1' where id=#{id}")
	int updateInfo(@Param("id")String id);

	public class SqlProvider {
		public String queryAllInfo(Map<String, Object> param) {
			final TerminalApply terminalApply = (TerminalApply) param.get("terminalApply");
			return new SQL() {
				{
					SELECT("ta.*,mis.merchant_name,ais.agent_name,"
							+ "(SELECT COUNT(1) FROM terminal_info ti WHERE ti.merchant_no=ta.merchant_no "
							+ "AND ti.open_status=2 AND ti.collection_code is null) is_bind");
					FROM("terminal_apply ta "
							+ "LEFT JOIN merchant_info mis on mis.merchant_no=ta.merchant_no "
							+ "LEFT JOIN agent_info ais on ais.agent_no=mis.agent_no");
					if (StringUtils.isNotBlank(terminalApply.getMobilephone())) {
						WHERE("ta.mobilephone = #{terminalApply.mobilephone}");
					}
					if(StringUtils.isNotBlank(terminalApply.getSn())){
						WHERE("ta.SN = #{terminalApply.sn}");
					}
					if (StringUtils.isNotBlank(terminalApply.getAgentName())) {
						terminalApply.setAgentName(terminalApply.getAgentName() + "%");
						WHERE("ais.agent_name like #{terminalApply.agentName}");
					}
					if (StringUtils.isNotBlank(terminalApply.getStatus())) {
						WHERE(" ta.status=#{terminalApply.status}");
					}
					if (StringUtils.isNotBlank(terminalApply.getMerAccount())) {
						WHERE(" mis.mer_account=#{terminalApply.merAccount}");
					}
					if(StringUtils.isNotBlank(terminalApply.getSNisNull())){
						if("1".equals(terminalApply.getSNisNull())){
							WHERE(" ta.SN is not NULL");
						}else if("0".equals(terminalApply.getSNisNull())){
							WHERE(" ta.SN is NULL");
						}
					}
					if (terminalApply.getsTime() != null) {
						WHERE(" ta.create_time>=#{terminalApply.sTime}");
					}
					if (terminalApply.geteTime() != null) {
						WHERE(" ta.create_time<=#{terminalApply.eTime}");
					}
					if (StringUtils.isNotBlank(terminalApply.getIsBindParam())) {
						String str = "";
						if ("0".equals(terminalApply.getIsBindParam())) {
							str = " not";
						}
						str += " EXISTS (SELECT 1 FROM terminal_info ti WHERE ti.merchant_no=ta.merchant_no"
								+ " AND ti.open_status=2 AND ti.collection_code is null)";
						WHERE(str);
					}
					ORDER_BY(" create_time DESC");
				}
			}.toString();
		}

	}
}
