package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;

/**
 * 集群中普通商户dao
 * 
 * @author junhu
 *
 */
@SuppressWarnings("all")
public interface OrgRouteMerDao {

	@SelectProvider(type = SqlProvider.class, method = "listOrgRouteMerByCon")
	@ResultType(Map.class)
	List<Map> listOrgRouteMerByCon(Map<String, Object> param, Page<Map> page);

	@Delete("delete from trans_route_group_merchant where id=#{id}")
	int deleteOrgRouteMerById(Long id);

	public class SqlProvider {
		public String listOrgRouteMerByCon(final Map<String, Object> param) {
			return new SQL() {
				{
					SELECT("trgm.*, trg.group_name, mi.merchant_name");
					FROM("trans_route_group_merchant trgm"
							+ " LEFT JOIN trans_route_group trg ON trgm.group_code = trg.group_code"
							+ " LEFT JOIN merchant_info mi ON trgm.pos_merchant_no = mi.merchant_no");
					if (StringUtils.isNotEmpty(String.valueOf(param.get("group_code") == null ? "" : param.get("group_code")))) {
						WHERE("trg.group_code like CONCAT('%',#{group_code},'%')");
					}
					if (StringUtils.isNotEmpty(String.valueOf(param.get("group_name") == null ? "" : param.get("group_name")))) {
						WHERE("trg.group_name like CONCAT('%',#{group_name},'%')");
					}
					if (StringUtils.isNotEmpty(String.valueOf(param.get("pos_merchant_no") == null ? "" : param.get("pos_merchant_no")))) {
						WHERE("(mi.merchant_no like CONCAT('%',#{pos_merchant_no},'%') or mi.merchant_name like CONCAT('%',#{pos_merchant_no},'%'))");
					}
				}
			}.toString();
		}
	}
}
