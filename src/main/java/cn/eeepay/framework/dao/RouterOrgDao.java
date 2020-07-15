package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * 集群中收单商户dao
 * 
 * @author junhu
 *
 */
public interface RouterOrgDao {
	@SelectProvider(type = SqlProvider.class, method = "listRouterAcqMerchantByCon")
	@ResultType(Map.class)
	List<Map> listRouterAcqMerchantByCon(Map<String, Object> param, Page<Map> page);

	@Delete("delete from trans_route_group_acq_merchant where id=#{id}")
	int deleteRouterAcqMerchantById(Long id);

	@Update("update acq_merchant set quota=#{quota} where acq_merchant_no=#{acq_merchant_no}")
	int updateAcqMerchantQuota(Map<String, Object> param);
	
	@SelectProvider(type = SqlProvider.class, method = "selecrAllInfoRecordInfoExport")
	List<Map<String,Object>> selecrAllInfoRecordInfoExport(Map<String, Object> map);


	@Select("select * from trans_route_group_acq_merchant where acq_merchant_no=#{acqMerchantNo} and group_code=#{groupCode}")
	List<Map<String,Object>> getTransRouteGroupAcqMerchant(@Param("acqMerchantNo")String acqMerchantNo,@Param("groupCode")String groupCode);

	@Delete("delete from trans_route_group_acq_merchant where acq_merchant_no=#{acqMerchantNo} and group_code=#{groupCode}")
	int deleteAcqMer(@Param("acqMerchantNo")String acqMerchantNo,@Param("groupCode")String groupCode);

	@Select("select count(acq_merchant_no) from trans_route_group_acq_merchant where group_code=#{groupCode}")
	int selectAcqMerCount(@Param("groupCode")Integer groupCode);

	@Insert("INSERT INTO trans_route_group_acq_merchant_import_delete (group_code, acq_merchant_no, delete_no) VALUES " +
			"(#{map.groupCode}, #{map.acqMerchantNo}, #{map.deleteNo})")
	int insertAcqMerImportDel(@Param("map")Map map);

	@Select("select * from trans_route_group_acq_merchant_import_delete where delete_no=#{delete_no}")
	@ResultType(Map.class)
	List<Map> selectAcqMerImportDel(@Param("delete_no")String delete_no);

	@Delete("delete from trans_route_group_acq_merchant_import_delete where delete_no=#{delete_no}")
	int deleteAcqMerImportDel(@Param("delete_no")String delete_no);

	public class SqlProvider {
		public String listRouterAcqMerchantByCon(final Map<String, Object> param) {
			String sql = new SQL() {
				{	
					SELECT("gam.*, g.group_name,am.acq_merchant_name,am.acq_merchant_type,am.quota quota,am.quota_status,am.create_time AS last_update_time,ao.acq_name");
					FROM("trans_route_group_acq_merchant gam"
							+ " LEFT JOIN acq_merchant am ON gam.acq_merchant_no = am.acq_merchant_no"
							+ " LEFT JOIN acq_org ao ON am.acq_org_id = ao.id"
							+ " LEFT JOIN trans_route_group g ON gam.group_code = g.group_code");
					if (StringUtils.isNotEmpty(String.valueOf(param.get("group_code") == null ? "" : param.get("group_code")))) {
						WHERE("gam.group_code =#{group_code}");
					}
					if (StringUtils.isNotEmpty(String.valueOf(param.get("group_name") == null ? "" : param.get("group_name")))) {
						param.put("group_name", "%"+param.get("group_name")+"%");
						WHERE("g.group_name like #{group_name}");
					}
					if (StringUtils.isNotEmpty(String.valueOf(param.get("acq_merchant_no") == null ? "" : param.get("acq_merchant_no")))) {
						param.put("acq_merchant_no", "%"+param.get("acq_merchant_no")+"%");
						WHERE("(gam.acq_merchant_no like #{acq_merchant_no} or am.acq_merchant_name like #{acq_merchant_no})");
					}
					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("quota_status") == null ? "" : param.get("quota_status")))
							&& !(param.get("quota_status").toString()).equals("0")) {
						WHERE("am.quota_status=#{quota_status}");
					}
					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("acqMerchantType") == null ? "" : param.get("quota_status")))
						&& !(param.get("acqMerchantType").toString()).equals("-1")) {
						WHERE("am.acq_merchant_type=#{acqMerchantType}");
					}
				}
			}.toString();
			System.out.println(sql);
			return sql;
		}
	
		public String selecrAllInfoRecordInfoExport(final Map<String, Object> param) {
			return new SQL() {{	
					SELECT("am.acq_merchant_no, am.acq_merchant_name, tam.group_code, tr.group_name, ao.acq_enname, ai.agent_name, mi.province, mi.city, aj.rate j_rate, ax.rate x_rate,"
							+ " sd.sys_name acq_merchant_type_str ");
					FROM("acq_merchant am");
					JOIN("trans_route_group_acq_merchant tam on am.acq_merchant_no = tam.acq_merchant_no");
					JOIN("trans_route_group tr on tam.group_code = tr.group_code");
					JOIN("acq_org ao on tr.acq_id = ao.id");
					LEFT_OUTER_JOIN("acq_service_rate aj ON am.acq_service_id = aj.acq_service_id AND aj.card_rate_type IN (0, 2)");
					LEFT_OUTER_JOIN("acq_service_rate ax ON am.acq_service_id = ax.acq_service_id AND ax.card_rate_type IN (0, 1)");
					LEFT_OUTER_JOIN("agent_info ai ON am.agent_no = ai.agent_no");
					LEFT_OUTER_JOIN("merchant_info mi ON am.merchant_no = mi.merchant_no ");
					LEFT_OUTER_JOIN("sys_dict sd ON sd.sys_value = am.acq_merchant_type and sd.sys_key='ACQ_MERCHANT_TYPE' ");
					if (StringUtils.isNotEmpty(String.valueOf(param.get("group_code") == null ? "" : param.get("group_code")))) {
						WHERE("tam.group_code =#{group_code}");
					}
					if (StringUtils.isNotEmpty(String.valueOf(param.get("group_name") == null ? "" : param.get("group_name")))) {
						param.put("group_name", "%"+param.get("group_name")+"%");
						WHERE("tr.group_name like #{group_name}");
					}
					if (StringUtils.isNotEmpty(String.valueOf(param.get("acq_merchant_no") == null ? "" : param.get("acq_merchant_no")))) {
						param.put("acq_merchant_no", "%"+param.get("acq_merchant_no")+"%");
						WHERE("(am.acq_merchant_no like #{acq_merchant_no} or am.acq_merchant_name like #{acq_merchant_no})");
					}
					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("quota_status") == null ? "" : param.get("quota_status")))
							&& !(param.get("quota_status").toString()).equals("0")) {
						WHERE("am.quota_status=#{quota_status}");
					}
					if (StringUtils.isNotEmpty(
							String.valueOf(param.get("acqMerchantType") == null ? "" : param.get("quota_status")))
						&& !(param.get("acqMerchantType").toString()).equals("-1")) {
						WHERE("am.acq_merchant_type=#{acqMerchantType}");
					}
			}}.toString();
		}
	}
}
