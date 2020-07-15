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

public interface CmCardDao {

	/**
	 * 卡片列表查询
	 * @author	mays
	 * @date	2018年4月8日
	 */
	@SelectProvider(type = SqlProvider.class, method = "selectCardInfo")
	@ResultType(CmCardInfo.class)
	List<CmCardInfo> selectCardInfo(@Param("page") Page<CmCardInfo> page, @Param("info") CmCardInfo info);

	@SelectProvider(type = SqlProvider.class, method = "selectCardInfo")
	@ResultType(CmCardInfo.class)
	List<CmCardInfo> exportCmCard(@Param("info") CmCardInfo info);

	/**
	 * 根据id查询卡片信息
	 * @author	mays
	 * @date	2018年4月8日
	 */
	@Select("SELECT cci.*,coi.org_name FROM cm_card_info cci LEFT JOIN cm_user cu ON cu.user_no = cci.user_no "
			+ "LEFT JOIN cm_org_info coi ON coi.org_id = cu.src_org_id WHERE cci.id = #{id}")
	@ResultType(CmCardInfo.class)
	CmCardInfo queryCardInfoById(@Param("id") String id);

	/**
	 * 修改卡片信息
	 * @author	mays
	 * @date	2018年4月8日
	 */
	@Update("update cm_card_info set mobile_no=#{i.mobileNo},total_amount=#{i.totalAmount},mail=#{i.mail} where id = #{i.id}")
	int updateCardInfo(@Param("i") CmCardInfo info);

	public class SqlProvider {

		public String selectCardInfo(Map<String, Object> param) {
			final CmCardInfo info = (CmCardInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("cci.*,coi.org_name,cu.mobile_no user_mobile");
					FROM("cm_card_info cci");
					LEFT_OUTER_JOIN("cm_user cu ON cu.user_no = cci.user_no");
					LEFT_OUTER_JOIN("cm_org_info coi ON coi.org_id = cu.src_org_id");
					if (StringUtils.isNotBlank(info.getUserNo())) {
						WHERE("cci.user_no = #{info.userNo}");
					}
					if (StringUtils.isNotBlank(info.getCardNo())) {
						WHERE("cci.card_no = #{info.cardNo}");
					}
					if (StringUtils.isNotBlank(info.getMail())) {
						WHERE("cci.mail = #{info.mail}");
					}
					if (info.getCardStatus() != null) {
						WHERE("cci.card_status = #{info.cardStatus}");
					}
					if (StringUtils.isNotBlank(info.getSrcOrgId())) {
						WHERE("cu.src_org_id = #{info.srcOrgId}");
					}
					if (StringUtils.isNotBlank(info.getUserMobile())) {
						WHERE("cu.mobile_no = #{info.userMobile}");
					}
					if (StringUtils.isNotBlank(info.getsCreateTime())) {
						WHERE("cci.create_time >= #{info.sCreateTime}");
					}
					if (StringUtils.isNotBlank(info.geteCreateTime())) {
						WHERE("cci.create_time <= #{info.eCreateTime}");
					}
					ORDER_BY("cci.create_time desc");
				}
			};
			return sql.toString();
		}

	}

}