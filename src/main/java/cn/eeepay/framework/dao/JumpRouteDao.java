package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcpWhitelist;
import cn.eeepay.framework.model.JumpRouteConfig;
import cn.eeepay.framework.model.SysDict;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface JumpRouteDao {

	@SelectProvider(type=SqlProvider.class, method="list")
	@ResultType(JumpRouteConfig.class)
	List<JumpRouteConfig> list(@Param("baseInfo")JumpRouteConfig baseInfo, Page<JumpRouteConfig> page);

	@Insert("insert into jump_route_config(acq_id,group_code,card_type,status,"
			+ "start_time,end_time,min_trans_amount,"
			+ "max_trans_amount,week_days,bp_ids,"
			+ "merchant_provinces,remark,card_bin_ids,merchant_city,acq_merchant_type,service_types," +
			"effective_date_type,target_amount,total_amount,start_date,end_date,relation_activity) values (#{acqId},#{groupCode},"
			+ "#{cardType},#{status},#{startTime},"
			+ "#{endTime},#{minTransAmount},#{maxTransAmount},"
			+ "#{weekDays},#{bpIds},#{merchantProvinces},#{remark},#{cardBinIds},#{merchantCity},#{acqMerchantType},#{serviceTypes}," +
			"#{effectiveDateType},#{targetAmount},#{totalAmount},#{startDate},#{endDate},#{relationActivity})")
	int insert(JumpRouteConfig baseInfo);
	
	@Update("UPDATE jump_route_config SET "
			+ "acq_id =#{acqId}, card_type=#{cardType},"
			+ "status=#{status},"
			+ "start_time=#{startTime},"
			+ "end_time=#{endTime},min_trans_amount=#{minTransAmount},"
			+ "max_trans_amount=#{maxTransAmount},"
			+ "week_days=#{weekDays},"
			+ "bp_ids=#{bpIds},merchant_provinces=#{merchantProvinces},"
			+ "remark=#{remark},card_bin_ids=#{cardBinIds}, " +
			" merchant_city=#{merchantCity},acq_merchant_type=#{acqMerchantType},service_types=#{serviceTypes}," +
			" effective_date_type = #{effectiveDateType},target_amount=#{targetAmount},start_date=#{startDate},end_date=#{endDate}," +
			" relation_activity=#{relationActivity} " +
			" where id=#{id}")
	int update(JumpRouteConfig baseInfo);
	
	@Select("select jrc.*,trg.group_name" +
			" from jump_route_config jrc" +
			" left join trans_route_group trg on trg.group_code = jrc.group_code" +
			" where jrc.id=#{id}")
	@ResultType(JumpRouteConfig.class)
	JumpRouteConfig getById(Integer id);
	
	@Delete("delete from jump_route_config where id=#{id}")
	int delete(String id);

    public class SqlProvider{
		
		public String list(Map<String, Object> param){
			JumpRouteConfig baseInfo = (JumpRouteConfig) param.get("baseInfo");
			StringBuilder sb = new StringBuilder();
			sb.append("select DISTINCT jrc.id,jrc.status,jrc.start_date,")
			.append(" jrc.end_date,jrc.start_time,jrc.end_time,jrc.card_type,jrc.jump_times,jrc.remark,")
			.append(" jrc.min_trans_amount,jrc.max_trans_amount,jrc.apart_days,jrc.group_code,jrc.effective_date_type,")
			.append(" trg.group_name,ao.acq_enname")
			.append(" from jump_route_config jrc")
			.append(" left join trans_route_group trg on trg.group_code = jrc.group_code")
			.append(" left join acq_org ao on ao.id = jrc.acq_id")
			.append(" left join business_product_define bpd on find_in_set(bpd.bp_id, jrc.bp_ids) where 1=1 ");
			if(baseInfo != null){
				if(baseInfo.getGroupCode() != null){
					sb.append(" and jrc.group_code like concat(#{baseInfo.groupCode},'%') ");
				}
				if(baseInfo.getStartDate()!=null){
					sb.append(" and jrc.start_date >= #{baseInfo.startDate}");
				}
				if(baseInfo.getEndDate()!=null){
					sb.append(" and jrc.end_date <= #{baseInfo.endDate}" );
				}
				if(baseInfo.getMinTransAmount()!=null){
					sb.append(" and jrc.min_trans_amount >= #{baseInfo.minTransAmount}");
				}
				if(baseInfo.getMaxTransAmount()!=null){
					sb.append(" and jrc.max_trans_amount <= #{baseInfo.maxTransAmount}");
				}
				if(baseInfo.getStatus() != null){
					sb.append(" and jrc.status = #{baseInfo.status}");
				}
				if(baseInfo.getAcqId() != null){
					sb.append(" and jrc.acq_id = #{baseInfo.acqId}");
				}
				if(baseInfo.getTeamId() != null) {
					sb.append(" and bpd.team_id = #{baseInfo.teamId}");
				}
				if(baseInfo.getRelationActivity() != null) {
					sb.append(" and jrc.relation_activity = #{baseInfo.relationActivity}");
				}
			}
			sb.append(" order by jrc.id desc");
			return sb.toString();
		}
		
		public String findServiceTypeSelectByBqIds(Map<String, Object> param){
			Integer[] ids = (Integer[]) param.get("ids");
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT ");
            sql.append("	s_d.* ");
            sql.append("FROM ");
            sql.append("	business_product_info b ");
            sql.append("JOIN service_info s ON b.service_id = s.service_id ");
            sql.append("JOIN sys_dict s_d ON s.service_type = s_d.sys_value ");
            sql.append("AND s_d.sys_key = 'SERVICE_TYPE' ");
            if (ids.length>0) {
            	sql.append("AND b.bp_id IN ( ");
    			for (int i = 0; i < ids.length; i++) {
    				sql.append("'").append(ids[i]).append("'");
    				if (i != ids.length - 1) {
    					sql.append(",");
    				}
    			}
    			sql.append(") ");
			}else{
				sql.append("AND b.bp_id=-1 ");
			}

			return sql.toString();
		}
	}

	@Select("select * from jump_route_whitelist")
	@ResultType(AcpWhitelist.class)
	List<AcpWhitelist> selectAllWlInfo();

	@Delete("delete from jump_route_whitelist where id=#{id}")
	int deleteByWlid(int id);

	@Insert("insert into jump_route_whitelist ( merchant_no,create_time,create_person ) values(#{record.merchantNo},now(),#{record.createPerson} )")
	int insertWl(@Param("record") AcpWhitelist record);

	@Select("select * from jump_route_whitelist where merchant_no =#{merchantNo} limit 1")
	@ResultType(AcpWhitelist.class)
	AcpWhitelist getWlInfoByMerchantNo(@Param("merchantNo") String merchantNo);

	@SelectProvider(type = SqlProvider.class, method = "findServiceTypeSelectByBqIds")
	@ResultType(SysDict.class)
	List<SysDict> findServiceTypeSelectByBqIds(@Param("ids") Integer[] ids);

	@Select("SELECT ju.* from jump_route_config ju WHERE " +
			"now()>=ju.end_time AND ju.target_amount is not null and ju.total_amount>=ju.target_amount " +
			"and (ju.sms_warning_date is null or to_days(now()) > to_days(ju.sms_warning_date))")
	@ResultType(JumpRouteConfig.class)
	List<JumpRouteConfig> findJumpRouteConfigYesReach();

	@Select("SELECT ju.* from jump_route_config ju WHERE " +
			"now()>=ju.end_time AND ju.target_amount is not null and ju.total_amount<ju.target_amount " +
			"and (ju.sms_warning_date is null or to_days(now()) > to_days(ju.sms_warning_date))")
	@ResultType(JumpRouteConfig.class)
	List<JumpRouteConfig> findJumpRouteConfigNoReach();

	@Update("UPDATE jump_route_config SET sms_warning_date=now() WHERE id=#{id}")
	int updateJumpRouteConfigSmsDate(@Param("id") Integer id);

}
