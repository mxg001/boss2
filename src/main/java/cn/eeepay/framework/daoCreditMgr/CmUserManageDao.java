package cn.eeepay.framework.daoCreditMgr;

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
import cn.eeepay.framework.model.CmCardInfo;
import cn.eeepay.framework.model.CmOrgInfo;
import cn.eeepay.framework.model.CmUserInfo;

public interface CmUserManageDao {

	/**
	 * 查询所有组织
	 * @author	mays
	 * @date	2018年3月28日
	 */
	@Select("SELECT id,org_id,org_name FROM cm_org_info")
	@ResultType(CmOrgInfo.class)
	List<CmOrgInfo> selectOrgAllInfo();

	/**
	 * 查询用户信息
	 * @author mays
	 * @date 2018年4月3日
	 */
	@SelectProvider(type = SqlProvider.class, method = "selectUserInfo")
	@ResultType(CmUserInfo.class)
	List<CmUserInfo> selectUserInfo(@Param("page") Page<CmUserInfo> page, @Param("info") CmUserInfo info);

	/**
	 * 导出用户信息
	 * @author	mays
	 * @date	2018年5月22日
	 */
	@SelectProvider(type = SqlProvider.class, method = "selectUserInfo")
	@ResultType(CmUserInfo.class)
	List<CmUserInfo> exportCmUser(@Param("info") CmUserInfo info);

	/**
	 * 根据userNo查询用户信息
	 * @author	mays
	 * @date	2018年4月6日
	 */
	@Select("SELECT cu.*,coi.org_name,cu.member_expire > now() AS isVip FROM cm_user cu "
			+ "LEFT JOIN cm_org_info coi ON coi.org_id = cu.src_org_id WHERE cu.user_no = #{userNo}")
	@ResultType(CmUserInfo.class)
	CmUserInfo selectUserInfoByUserNo(@Param("userNo") String userNo);

	/**
	 * 根据userNo查询卡片信息
	 * @author	mays
	 * @date	2018年4月6日
	 */
	@Select("SELECT * FROM cm_card_info WHERE user_no = #{userNo}")
	@ResultType(CmCardInfo.class)
	List<CmCardInfo> selectCardInfoByUserNo(@Param("userNo") String userNo);

	/**
	 * 修改用户信息
	 * @author	mays
	 * @date	2018年4月6日
	 */
	@Update("update cm_user set mobile_no = #{mobileNo} where user_no = #{userNo}")
	int updateUserInfo(@Param("userNo") String userNo, @Param("mobileNo") String mobileNo);

	public class SqlProvider {

		public String selectOrgAllInfo(final Map<String, Object> param) {
			final String item = (String) param.get("item");
			return new SQL() {
				{
					SELECT("id,org_id,org_name");
					FROM("cm_org_info");
					if (StringUtils.isNotBlank(item)) {
						if (StringUtils.isNumeric(item)) {
							WHERE("org_id like CONCAT(#{item},'%')");
						} else {
							WHERE("org_name like CONCAT(#{item},'%')");
						}
					}
					ORDER_BY("id");
				}
			}.toString();
		}

		public String selectUserInfo(Map<String, Object> param) {
			final CmUserInfo info = (CmUserInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("cu.*,coi.org_name,cu.member_expire > now() AS isVip");
					FROM("cm_user cu");
					LEFT_OUTER_JOIN("cm_org_info coi ON coi.org_id = cu.src_org_id");
					if (StringUtils.isNotBlank(info.getUserNo())) {
						WHERE("cu.user_no = #{info.userNo}");
					}
					if (StringUtils.isNotBlank(info.getSrcUserId())) {
						WHERE("cu.src_user_id = #{info.srcUserId}");
					}
					if (StringUtils.isNotBlank(info.getSrcOrgId())) {
						WHERE("cu.src_org_id = #{info.srcOrgId}");
					}
					if (StringUtils.isNotBlank(info.getMobileNo())) {
						WHERE("cu.mobile_no = #{info.mobileNo}");
					}
					if (StringUtils.isNotBlank(info.getsCreateTime())) {
						WHERE("cu.create_time >= #{info.sCreateTime}");
					}
					if (StringUtils.isNotBlank(info.geteCreateTime())) {
						WHERE("cu.create_time <= #{info.eCreateTime}");
					}
					if (StringUtils.isNotBlank(info.getAgentNode())) {
						if ("0".equals(info.getContain())) {//不包含下级
							WHERE("cu.agent_node = #{info.agentNode}");
						} else {
							WHERE("cu.agent_node like CONCAT(#{info.agentNode},'%')");
						}
					}
					if (info.getUserType() == null) {
					} else if (info.getUserType() == 0) {
						//根据会员到期时间判断是否为会员
						WHERE("(cu.member_expire <= now() OR cu.member_expire is null)");
					} else if (info.getUserType() == 1) {
						WHERE("cu.member_expire > now()");
					}
					ORDER_BY("cu.create_time desc");
				}
			};
			return sql.toString();
		}

	}

}