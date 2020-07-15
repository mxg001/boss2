package cn.eeepay.framework.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqOrg;
import cn.eeepay.framework.model.AcqService;
import cn.eeepay.framework.model.TransRouteGroup;
import cn.eeepay.framework.model.TransRouteGroupAcqMerchant;
import cn.eeepay.framework.model.TransRouteGroupMerchant;

/**
 * 路由集群dao
 * 
 * @author junhu
 *
 */
public interface RouteGroupDao {

	@Select("select * from acq_org")
	@ResultType(AcqOrg.class)
	List<AcqOrg> acqOrgSelectBox();

	@Select("select * from acq_service where acq_id = #{acqId}")
	@ResultType(AcqService.class)
	List<AcqService> acqServiceSelectBox(int acqId);

	@Insert("insert into trans_route_group (group_name, group_code, acq_id,"
			+ "acq_service_id, agent_no, service_type,accounts_period, my_settle, sales_no,"
			+ "allow_trans_start_time,"
			+ "allow_trans_end_time, def_acq_day_amount, backups_group_code,"
			+ "merchant_type, route_last, route_describe,route_type, status, group_province,"
			+ "group_city, create_time, create_person, warn_mobile,map_group_id) values (#{groupName}, #{groupCode}, #{acqId},"
			+ "#{acqServiceId}, #{agentNo}, #{serviceType}, #{accountsPeriod}, #{mySettle}, #{salesNo},"
			+ "#{allowTransStartTime},"
			+ "#{allowTransEndTime}, #{defAcqDayAmount}, #{backupsGroupCode},"
			+ "#{merchantType}, #{routeLast}, #{routeDescribe}, #{routeType}, #{status}, #{groupProvince},"
			+ "#{groupCity}, #{createTime}, #{createPerson}, #{warnMobile},#{mapGroupId})")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
	int insertRouteGroup(TransRouteGroup routeGroup);

	/*@Insert("INSERT INTO trans_route_group_merchant (group_code,pos_merchant_no,service_type,create_time,create_person )SELECT b.group_code"
			+ ",b.merchant_no,b.service_type,SYSDATE(),'admin' FROM (	SELECT a.merchant_no,IFNULL( MIN(tg.group_code), MIN(a.def_group_code) ) group_code,"
			+"a.service_type,a.start_pc,a.acq_org_id FROM (SELECT mi.merchant_no,dt.def_group_code,mi.province,mi.city,si.service_type,dt.start_pc,"
			+"dt.acq_org_id FROM def_trans_route_group dt,merchant_service ms,merchant_info mi,service_info si "
			+"	WHERE dt.service_id = ms.service_id AND si.service_id = ms.service_id AND ms.merchant_no = #{merNo} AND ms.bp_id = #{bpId} "
			+"	 AND mi.merchant_no = ms.merchant_no AND NOT EXISTS (SELECT 1 FROM trans_route_group_merchant tm2,"
			+"trans_route_group g2 WHERE tm2.pos_merchant_no = ms.merchant_no AND g2.group_code = tm2.group_code and si.service_type=tm2.service_type ) ) AS a "
			+" LEFT JOIN trans_route_group tg ON a.province = tg.group_province AND a.service_type = tg.service_type AND"
			+" a.start_pc = 1 AND tg.status=0 GROUP BY a.merchant_no,a.service_type ) AS b" )*/
	
	@Insert("INSERT INTO trans_route_group_merchant (pos_merchant_no,service_type,group_code,create_time,create_person"
			+") SELECT mi.merchant_no,si.service_type,IFNULL((SELECT g.group_code FROM trans_route_group g ,acq_service ase  WHERE"
			+" g.group_province = mi.province	AND g.group_city IN (mi.city, '不限') AND dg.start_pc = 1 AND dg.acq_org_id = g.acq_id AND g.acq_service_id = ase.id AND ase.service_type = dg.acq_service_type AND g.route_type IN ('1', '2')"
			+" ORDER BY CASE g.group_city WHEN mi.city THEN 2 WHEN '不限' THEN 1 ELSE 0 END DESC LIMIT 1 ),dg.def_group_code ) group_code,"
			+" SYSDATE(),'admin' FROM	merchant_info mi,merchant_service ms,service_info si,def_trans_route_group dg"
			+" WHERE ms.service_id = si.service_id AND ms.merchant_no = mi.merchant_no  AND mi.merchant_no = #{merNo} "
			+" AND NOT EXISTS (SELECT 1 FROM trans_route_group_merchant gm WHERE gm.pos_merchant_no = mi.merchant_no "
			+" AND gm.service_type = si.service_type) AND si.service_id = dg.service_id")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
	int insertRouteGroupByMerchant(@Param("merNo")String merNo);
	
	@SelectProvider(type = SqlProvider.class, method = "queryRouteGroupByCon")
	@ResultType(TransRouteGroup.class)
	List<TransRouteGroup> queryRouteGroupByCon(Map<String, Object> param, Page<TransRouteGroup> page);

	@SelectProvider(type = SqlProvider.class, method = "queryRouteGroupByCon")
	@ResultType(TransRouteGroup.class)
	TransRouteGroup queryRouteGroupById(Map<String, Object> param);

	@Update("update trans_route_group set "
			+ "group_name = #{groupName}, group_code = #{groupCode}, acq_id = #{acqId}, acq_service_id = #{acqServiceId},"
			+ "agent_no = #{agentNo}, service_type = #{serviceType}, accounts_period = #{accountsPeriod}, my_settle = #{mySettle},"
			+ "sales_no = #{salesNo},"
			+ "allow_trans_start_time = #{allowTransStartTime}, allow_trans_end_time = #{allowTransEndTime}, def_acq_day_amount = #{defAcqDayAmount},"
			+ "backups_group_code = #{backupsGroupCode}, merchant_type = #{merchantType}, route_last = #{routeLast}, route_describe = #{routeDescribe},"
			+ "route_type = #{routeType}, status = #{status}, group_province = #{groupProvince}, group_city = #{groupCity}, warn_mobile=#{warnMobile},map_group_id = #{mapGroupId}"
			+ " where id = #{id}")
	int updateRouteGroup(TransRouteGroup routeGroup);

	@Insert("insert into trans_route_group_merchant (group_code,pos_merchant_no,service_type,create_time,create_person)"
			+ " values (#{record.groupCode},#{record.posMerchantNo},#{record.serviceType},#{record.createTime},#{record.createPerson})")
	int insertRouteGroupMerchant(@Param("record")TransRouteGroupMerchant routeGroupMerchant);

	@Select("select * from trans_route_group_merchant where pos_merchant_no=#{record.posMerchantNo} and service_type=#{record.serviceType}")
	@ResultType(TransRouteGroupMerchant.class)
	TransRouteGroupMerchant selectInfo(@Param("record")TransRouteGroupMerchant routeGroupMerchant);
	
	@Insert("insert into trans_route_group_acq_merchant (group_code, acq_merchant_no, create_time, create_person)"
			+ " values (#{groupCode}, #{acqMerchantNo}, #{createTime}, #{createPerson})")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
	int insertRouteGroupAcqMerchant(TransRouteGroupAcqMerchant routeGroupAcqMerchant);
	
	@Select("select * from acq_service where id=#{id}")
	@ResultType(AcqService.class)
	AcqService queryAcqServiceById(Integer id);

	@Update("update trans_route_group set status=#{status} where id=#{id}")
	int updateGroupStatus(TransRouteGroup group);

	@Select("select * from trans_route_group where group_code=#{groupCode}")
	@ResultType(TransRouteGroup.class)
	TransRouteGroup getGroupByCode(@Param("groupCode")String groupCode);

	@Select("select * from trans_route_group_acq_merchant where acq_merchant_no=#{acqMerchantNo} ")
	@ResultType(TransRouteGroupMerchant.class)
	TransRouteGroupMerchant getTransRouteGroupMerchant(@Param("acqMerchantNo")String acqMerchantNo);

	@Delete("delete from trans_route_group where id=#{id}")
	int deleteRouteGroup(@Param("id") int id);

	@SelectProvider(type=SqlProvider.class, method="getGroupByServiceType")
	@ResultType(TransRouteGroup.class)
	List<TransRouteGroup> getGroupByServiceType(@Param("serviceTypes")String[] serviceTypes,@Param("group") String group);

	public class SqlProvider {
		public String queryRouteGroupByCon(final Map<String, Object> param) {
			return new SQL() {

				{
					SELECT("t.*,u.user_name");
					FROM("trans_route_group t");
					LEFT_OUTER_JOIN("boss_shiro_user u on u.id=t.create_person");
					if (StringUtils
							.isNotEmpty(String.valueOf(param.get("groupCode") == null ? "" : param.get("groupCode")))) {
						WHERE("t.group_code like CONCAT('%',#{groupCode},'%')");
					}
					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("groupName") == null ? "" : param.get("groupName")))) {
						WHERE("t.group_name like CONCAT('%',#{groupName},'%')");
					}
					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("status") == null ? "" : param.get("status")))) {
						WHERE("t.status = #{status}");
					}
					if (StringUtils
							.isNotEmpty(String.valueOf(param.get("agentNo") == null ? "" : param.get("agentNo")))) {
						WHERE("t.agent_no = #{agentNo}");
					}
					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("acqId") == null ||"-1".equals(String.valueOf(param.get("acqId")))? "" : param.get("acqId")))) {
						WHERE("t.acq_id = #{acqId}");
					}
					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("acqServiceId") == null ? "" : param.get("acqServiceId")))) {
						WHERE("t.acq_service_id = #{acqServiceId}");
					}
					if (StringUtils
							.isNotEmpty(String.valueOf(param.get("routeType") == null ? "" : param.get("routeType")))) {
						WHERE("t.route_type = #{routeType}");
					}
					if (StringUtils
							.isNotEmpty(String
									.valueOf(param.get("serviceType") == null ? "" : param.get("serviceType")))) {
						WHERE("t.service_type = #{serviceType}");
					}
					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("accountsPeriod") == null ? "" : param.get("accountsPeriod")))) {
						WHERE("t.accounts_period = #{accountsPeriod}");
					}
					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("groupProvince") == null ? "" : param.get("groupProvince")))) {
						WHERE("t.group_province = #{groupProvince}");
					}
					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("mySettle") == null ? "" : param.get("mySettle")))) {
						WHERE("t.my_settle = #{mySettle}");
					}
					if (StringUtils
							.isNotEmpty(String.valueOf(param.get("id") == null ? "" : param.get("id")))) {
						WHERE("t.id = #{id}");
					}

					if (StringUtils
							.isNotEmpty(String.valueOf(param.get("createPerson") == null ? "" : param.get("createPerson")))) {
						WHERE("u.user_name like CONCAT('%',#{createPerson},'%')");
					}

					if (StringUtils
							.isNotEmpty(String.valueOf(param.get("createTimeBegin") == null ? "" : param.get("createTimeBegin")))) {
						WHERE("t.create_time >= #{createTimeBegin}");
					}

					if (StringUtils
							.isNotEmpty(String.valueOf(param.get("createTimeEnd") == null ? "" : param.get("createTimeEnd")))) {
						WHERE("t.create_time <= #{createTimeEnd}");
					}


					System.out.println("SqlProvider.this.toString() = " + SqlProvider.this.toString());
				}
			}.toString();
		}
		
		public String getGroupByServiceType(final Map<String,Object> map){
			final String[] serviceTypes = (String[]) map.get("serviceTypes");
			final String group = (String)map.get("group");
			String sql = new SQL(){{
				SELECT("trg.group_code, trg.group_name");
				FROM("trans_route_group trg");
				if(serviceTypes!=null&&serviceTypes.length>0){
					StringBuilder sb = new StringBuilder();
					for(int i=0; i<serviceTypes.length;i++){
						sb.append(serviceTypes[i]).append(",");
					}
					sb.setLength(sb.length()-1);
					WHERE("trg.service_type IN (" + sb + ")");
				}

				if(StringUtils.isNotBlank(group)){
					if(StringUtils.isNumeric(group)){
						WHERE("trg.group_code=#{group}");
					}else{
						map.put("group","%"+group+"%");
						WHERE("trg.group_name like #{group}");
					}
				}
				WHERE(" trg.status=0");
				//ORDER_BY("trg.acq_id limit 2000");
			}}.toString();
			return sql;
 		}
	}
	
	@Select("SELECT t_r_g.id AS id, t_r_g.group_code AS group_code,t_r_g.group_name AS group_name FROM trans_route_group t_r_g WHERE t_r_g.group_code NOT IN (#{groupCode})")
	@ResultType(HashMap.class)
	List<Map<String, String>> findAll(Integer groupCode);


}
