package cn.eeepay.framework.daoCreditMgr;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmNoticeInfo;

public interface CmNoticeDao {

	/**
	 * 公告查询
	 * @author mays
	 * @date 2018年4月2日
	 */
	@SelectProvider(type = SqlProvider.class, method = "selectInfo")
	@ResultType(CmNoticeInfo.class)
	List<CmNoticeInfo> selectInfo(@Param("page") Page<CmNoticeInfo> page, @Param("info") CmNoticeInfo info);

	/**
	 * 新增公告
	 * @author mays
	 * @return
	 * @date 2018年4月2日
	 */
	@Insert("INSERT INTO cm_notice (org_id,title,content,link,pic_position,status,create_by,"
			+ "send_time,sender_id,sender_name,remark,icon_url,icon_name) "
			+ "VALUES (#{i.orgId},#{i.title},#{i.content},#{i.link},#{i.picPosition},#{i.status},#{i.createBy},"
			+ "#{i.sendTime},#{i.senderId},#{i.senderName},#{i.remark},#{i.iconUrl},#{i.iconName})")
	int addNotice(@Param("i") CmNoticeInfo info);

	/**
	 * 根据id查询公告
	 * @author mays
	 * @date 2018年4月3日
	 */
	@Select("select * from cm_notice where id = #{id}")
	@ResultType(CmNoticeInfo.class)
	CmNoticeInfo queryNoticeById(@Param("id") String id);

	/**
	 * 修改公告
	 * @author mays
	 * @date 2018年4月3日
	 */
	@UpdateProvider(type = SqlProvider.class, method = "updateNotice")
	int updateNotice(@Param("info") CmNoticeInfo info);

	/**
	 * 根据id删除公告，置为'已废弃'
	 * @author mays
	 * @date 2018年4月2日
	 */
	@Update("update cm_notice set status = '3' where id = #{id}")
	int delNoticeById(@Param("id") String id);

	/**
	 * 修改'弹窗提示开关'
	 * @author mays
	 * @date 2018年4月2日
	 */
	@Update("update cm_notice set pop_switch = #{popSwitch} where id = #{id}")
	int updateNoticePop(@Param("id") String id, @Param("popSwitch") String popSwitch);

	/**
	 * 下发或收回公告
	 * @author mays
	 * @date 2018年4月3日
	 */
	@Update("update cm_notice set status=#{i.status},sender_id=#{i.senderId},"
			+ "sender_name=#{i.senderName},send_time=#{i.sendTime} where id = #{i.id}")
	int sendOrRecoverNotice(@Param("i") CmNoticeInfo info);

	public class SqlProvider {

		public String selectInfo(Map<String, Object> param) {
			final CmNoticeInfo info = (CmNoticeInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("*");
					FROM("cm_notice");
					if (StringUtils.isNotBlank(info.getTitle())) {
						WHERE("title = #{info.title}");
					}
					if (StringUtils.isNotBlank(info.getOrgId())) {
						WHERE("org_id = #{info.orgId}");
					}
					if (StringUtils.isNotBlank(info.getStatus())) {
						WHERE("status = #{info.status}");
					}
					if (info.getPopSwitch() != null) {
						WHERE("pop_switch = #{info.popSwitch}");
					}
					if (StringUtils.isNotBlank(info.getStartCreateTime())) {
						WHERE("create_time >= #{info.startCreateTime}");
					}
					if (StringUtils.isNotBlank(info.getEndCreateTime())) {
						WHERE("create_time <= #{info.endCreateTime}");
					}
					if (StringUtils.isNotBlank(info.getStartSendTime())) {
						WHERE("send_time >= #{info.startSendTime}");
					}
					if (StringUtils.isNotBlank(info.getEndSendTime())) {
						WHERE("send_time <= #{info.endSendTime}");
					}
					ORDER_BY("id desc");
				}
			};
			return sql.toString();
		}

		public String updateNotice(Map<String, Object> param) {
			StringBuffer sql = new StringBuffer("");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			final CmNoticeInfo info = (CmNoticeInfo) param.get("info");
			sql.append("update cm_notice set org_id='" + info.getOrgId());
			sql.append("',title='" + info.getTitle());
			sql.append("',content='" + info.getContent());
			sql.append("',link='" + info.getLink());
			sql.append("',pic_position='" + info.getPicPosition());
			sql.append("',status='" + info.getStatus());
			sql.append("',send_time='" + sdf.format(info.getSendTime()));
			sql.append("',sender_id='" + info.getSenderId());
			sql.append("',sender_name='" + info.getSenderName());
			sql.append("',remark='" + info.getRemark());
			if (StringUtils.isNotBlank(info.getIconName())) {
				sql.append("',icon_name='" + info.getIconName());
				sql.append("',icon_url='" + info.getIconUrl());
			}
			sql.append("' where id=" + info.getId());
			return sql.toString();
		}

	}

}