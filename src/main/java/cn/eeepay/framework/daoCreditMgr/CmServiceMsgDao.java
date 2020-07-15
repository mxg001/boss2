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
import cn.eeepay.framework.model.CmUserMessageInfo;

public interface CmServiceMsgDao {

	/**
	 * 服务消息列表查询
	 * @author mays
	 * @date 2018年4月9日
	 */
	@SelectProvider(type = SqlProvider.class, method = "selectMsgInfo")
	@ResultType(CmUserMessageInfo.class)
	List<CmUserMessageInfo> selectMsgInfo(@Param("page") Page<CmUserMessageInfo> page,
			@Param("info") CmUserMessageInfo info);

	/**
	 * 根据id查询消息详情
	 * @author mays
	 * @date 2018年4月9日
	 */
	@Select("select * from cm_user_message where id = #{id}")
	@ResultType(CmUserMessageInfo.class)
	CmUserMessageInfo selectMsgInfoById(@Param("id") String id);

	/**
	 * 回收服务消息
	 * @author	mays
	 * @date	2018年4月9日
	 */
	@Update("update cm_user_message set is_del = 'Y' where id = #{id}")
	int updateMsgIsDelById(@Param("id") String id);

	public class SqlProvider {

		public String selectMsgInfo(Map<String, Object> param) {
			final CmUserMessageInfo info = (CmUserMessageInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("*");
					FROM("cm_user_message");
					if (StringUtils.isNotBlank(info.getUserNo())) {
						WHERE("user_no = #{info.userNo}");
					}
					if (StringUtils.isNotBlank(info.getCardNo())) {
						WHERE("card_no = #{info.cardNo}");
					}
					if (StringUtils.isNotBlank(info.getMsgType())) {
						WHERE("msg_type = #{info.msgType}");
					}
					if (StringUtils.isNotBlank(info.getsCreateTime())) {
						WHERE("create_time >= #{info.sCreateTime}");
					}
					if (StringUtils.isNotBlank(info.geteCreateTime())) {
						WHERE("create_time <= #{info.eCreateTime}");
					}
					if (StringUtils.isNotBlank(info.getMsgStatus())) {
						if ("0".equals(info.getMsgStatus())) {
							WHERE("is_read = 'N' and is_del = 'N'");
						} else if ("1".equals(info.getMsgStatus())) {
							WHERE("is_read = 'Y' and is_del = 'N'");
						} else if ("2".equals(info.getMsgStatus())) {
							WHERE("is_del = 'Y'");
						}
					}
					ORDER_BY("id desc");
				}
			};
			return sql.toString();
		}

	}

}