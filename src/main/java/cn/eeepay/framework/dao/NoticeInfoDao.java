package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.NoticeInfo;

public interface NoticeInfoDao {

    @Delete({
        "delete from notice_info_bak",
        "where nt_id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into notice_info (nt_id,title,content,attachment,create_time,issued_time,"
        + "login_user,sys_type,receive_type,agent_role,agent_no,bp_id,status,issued_org,"
        + "link,message_img,oem_type,oem_list,msg_type,valid_begin_time,valid_end_time,show_status,title_img) "
        + "values "+
        "(#{n.ntId},#{n.title},#{n.content},#{n.attachment},#{n.createTime},#{n.issuedTime}"
        + ",#{n.loginUser},#{n.sysType},#{n.receiveType},#{n.agentRole},#{n.agentNo},#{n.bpId},"
        + "#{n.status},#{n.issuedOrg},#{n.link},#{n.messageImg},#{n.oemType},#{n.oemList},#{n.msgType},#{n.validBeginTime},#{n.validEndTime},#{n.showStatus},#{n.titleImg});"
    })
    int insert(@Param("n")NoticeInfo notice);

    @Select({
        "select nt_id, title, content, attachment, create_time, issued_time, sys_type, "+
        "bp_id, status,receive_type,agent_no,link,message_img,oem_type,oem_list,valid_begin_time,valid_end_time,show_status,title_img " +
        "from notice_info " +
        "where nt_id = #{id}"
    })
    @ResultType(NoticeInfo.class)
    NoticeInfo selectById(@Param("id")String id);
    
    @Update({
			"update notice_info set title = #{record.title},"
					+ "content = #{record.content},"
					+ "attachment = #{record.attachment},"
					+ "sys_type = #{record.sysType},"
					+ "bp_id = #{record.bpId},"
					+ "status = #{record.status},"
					+ "agent_no = #{record.agentNo},"
					+ "receive_type = #{record.receiveType},"
					+ "link=#{record.link},"
					+ "message_img=#{record.messageImg},"
					+ "issued_org=#{record.issuedOrg}, "
					+"issued_org=#{record.issuedOrg}, "
					+"oem_type=#{record.oemType},"
					+"oem_list=#{record.oemList},"
					+"valid_begin_time=#{record.validBeginTime},"
					+"valid_end_time=#{record.validEndTime},"
					+"show_status=#{record.showStatus}"
					+",title_img=#{record.titleImg}"
					+" where nt_id = #{record.ntId}"
    })
    int update(@Param("record")NoticeInfo record);
    
    @Update({
    	"update notice_info set status='2',issued_org='0',issued_time=now(),login_user=#{map.loginUser} "
    	+ "where nt_id=#{map.id}"
    })
    int deliverNotice(@Param("map")Map<String, Object> map);
    
    @Update("update notice_info set status='1' where nt_id=#{id}")
	int updateRecoverNotice(Integer id);

	@Update("update notice_info set status='3' where nt_id=#{id}")
	int deleteRecoverNotice(Integer id);

	@Update("update notice_info set strong=0 where sys_type=#{sysType}")
	int clearStrongNotice(String sysType);

	@Update("update notice_info set strong=#{strong},issued_time=now() where nt_id=#{id}")
	int strongNotice(@Param("id") Integer id, @Param("strong") Integer strong);

	@Update("update notice_info set strong=#{strong} where nt_id=#{id}")
	int strongNoticeTime(@Param("id") Integer id, @Param("strong") Integer strong);

    @SelectProvider(type=SqlProvider.class,method="selectByParam")
    @ResultType(NoticeInfo.class)
	List<NoticeInfo> selectByParam(@Param("queryaram")NoticeInfo info,Page<NoticeInfo> page);
    
	public class SqlProvider {
		public String selectByParam(final Map<String, Object> param) {
			final NoticeInfo info = (NoticeInfo) param.get("queryaram");
			return new SQL() {
				{
					SELECT("nt_id, title, content, attachment, create_time, issued_time, sys_type, bp_id, status, issued_org, receive_type,"
							+ "link,message_img,strong,msg_type,valid_begin_time,valid_end_time,show_status");
					FROM("notice_info");
					if (info.getTitle() != null && StringUtils.isNotBlank(info.getTitle())) {
						info.setTitle(info.getTitle()+"%");
						WHERE("title like #{queryaram.title}");
					}
					if(info.getSysType() != null){
						String sysType = info.getSysType();
						if ( StringUtils.isNotBlank(sysType) && !"0".equals(sysType)) {
							WHERE("sys_type=#{queryaram.sysType}");
						}
					}
					if(info.getMsgType() != null){
						WHERE("msg_type=#{queryaram.msgType}");
					}
					if(info.getStatus() != null){
						String status = info.getStatus();
						if (StringUtils.isNotBlank(status) && !"0".equals(status)) {
							WHERE("status=#{queryaram.status}");
						}
					}
					if(info.getCreateTimeBegin() != null){
						WHERE("create_time >= #{queryaram.createTimeBegin}");
					}
					if(info.getCreateTimeEnd() != null){
						WHERE("create_time < #{queryaram.createTimeEnd}");
					}
					if(info.getIssuedTimeBegin() != null){
						WHERE("issued_time >= #{queryaram.issuedTimeBegin}");
					}
					if(info.getIssuedTimeEnd() != null){
						WHERE("issued_time < #{queryaram.issuedTimeEnd}");
					}
					WHERE("status != 3");			//状态为3的 表示是已删除的消息
					ORDER_BY("strong desc");
					ORDER_BY("IF (isnull(issued_time), 0, 1) ASC");
					ORDER_BY("issued_time desc");
					ORDER_BY("create_time desc");
				}
			}.toString();
		}

		public String propertyMapping(String name, int type) {
			final String[] propertys = {"ntId", "title", "content", "attachment", "createTime", "issuedTime",
					"sysType", "bpId","status", "receiveType","agentNo","issuedOrg","loginUser"};
			final String[] columns = { "id", "nt_id", "title", "content", "attachment", "create_time", "issued_time",
					"sys_type", "bp_id","status","receive_type","agent_no","issued_org","login_user"};
			if (StringUtils.isNotBlank(name)) {
				if (type == 0) {// 属性查出字段名
					for (int i = 0; i < propertys.length; i++) {
						if (name.equalsIgnoreCase(propertys[i])) {
							return columns[i];
						}
					}
				} else if (type == 1) {// 字段名查出属性
					for (int i = 0; i < propertys.length; i++) {
						if (name.equalsIgnoreCase(columns[i])) {
							return propertys[i];
						}
					}
				}
			}
			return null;
		}
	}

	
}