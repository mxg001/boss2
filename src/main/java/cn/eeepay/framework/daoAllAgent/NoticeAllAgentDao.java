package cn.eeepay.framework.daoAllAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.NoticeAllAgent;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * 公告Dao
 */
public interface NoticeAllAgentDao {

    @SelectProvider(type=NoticeAllAgentDao.SqlProvider.class,method="selectAllList")
    @ResultType(NoticeAllAgent.class)
    List<NoticeAllAgent> selectAllList(@Param("notice") NoticeAllAgent notice, @Param("page") Page<NoticeAllAgent> page);

    @Insert(
            "INSERT INTO pa_notice " +
                    " (title,content,oem_no_set,status,create_user," +
                    "  create_time,up_time,down_time,remark,last_update_time,org_set," +
                    "  img,type,summary,link_url,priority,home_img) " +
                    " VALUES " +
                    " (#{notice.title},#{notice.content},#{notice.oemNoSet},'2',#{notice.createUser}," +
                    "  NOW(),#{notice.upTime},#{notice.downTime},#{notice.remark},NOW(),#{notice.orgSet}," +
                    "  #{notice.img},#{notice.type},#{notice.summary},#{notice.linkUrl},#{notice.priority},#{notice.homeImg}) "
    )
    int addNotice(@Param("notice") NoticeAllAgent notice);

    @Select(
            "select * from pa_notice where id=#{id}"
    )
    NoticeAllAgent getNotice(@Param("id") long id);

    @Update(
            "update pa_notice set " +
                    " title=#{notice.title},content=#{notice.content},oem_no_set=#{notice.oemNoSet}, " +
                    " up_time=#{notice.upTime},down_time=#{notice.downTime},remark=#{notice.remark}," +
                    " last_update_time=NOW(),org_set=#{notice.orgSet},img=#{notice.img},type=#{notice.type}," +
                    " summary=#{notice.summary},link_url=#{notice.linkUrl},priority=#{notice.priority},home_img=#{notice.homeImg} " +
                    " where id=#{notice.id}"
    )
    int updateNotice(@Param("notice") NoticeAllAgent notice);

    @Update(
            "update pa_notice set status=#{state},last_update_time=NOW() where id=#{id}"
    )
    int updateNoticeState(@Param("id") long id, @Param("state") String state);

    @Update(
            "update pa_notice set home_status=#{homeStatus},last_update_time=NOW() where id=#{id}"
    )
    int updateNoticeHome(@Param("id") long id,@Param("homeStatus") int homeStatus);

    //下发
    @Update(
            "update pa_notice set status=#{state},last_update_time=NOW()," +
                    " send_time=NOW(),send_user=#{user}" +
                    " where id=#{id}"
    )
    int sendNotice(@Param("id") long id, @Param("state") String state, @Param("user") String user);

    //回收
    @Update(
            "update pa_notice set status=#{state},last_update_time=NOW()," +
                    " send_time=null,send_user=null" +
                    " where id=#{id}"
    )
    int sendColseNotice(@Param("id") long id, @Param("state") String state);

    @Update(
            "update pa_notice set user_code_set=#{userCodeSet} where id=#{id}"
    )
    int updateUserCodeSet(@Param("id")long id,@Param("userCodeSet") String userCodeSet);

    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final NoticeAllAgent notice = (NoticeAllAgent) param.get("notice");
            String str=new SQL(){{
                SELECT("notice.*");
                FROM("pa_notice notice");
                WHERE("notice.status!='0'");
                if(StringUtils.isNotBlank(notice.getTitle())){
                    WHERE("notice.title like concat(#{notice.title},'%') ");
                }
                if(StringUtils.isNotBlank(notice.getOemNo())){
                    WHERE("find_in_set(#{notice.oemNo},notice.oem_no_set)");
                }
                if(notice.getCreateTimeBegin() != null){
                    WHERE("notice.create_time >= #{notice.createTimeBegin}");
                }
                if(notice.getCreateTimeEnd() != null){
                    WHERE("notice.create_time <= #{notice.createTimeEnd}");
                }
                if(notice.getSendTimeBegin() != null){
                    WHERE("notice.send_time >= #{notice.sendTimeBegin}");
                }
                if(notice.getSendTimeEnd() != null){
                    WHERE("notice.send_time <= #{notice.sendTimeEnd}");
                }
                if(notice.getStatus()!=null&&!"".equals(notice.getStatus())&&!"-1".equals(notice.getStatus())){
                    WHERE("notice.status = #{notice.status} ");
                }
                if(notice.getType()!=null){
                    WHERE("notice.type = #{notice.type} ");
                }
                if(StringUtil.isNotBlank(notice.getOrgSet())){
                    WHERE("notice.org_set like concat('%',#{notice.orgSet},'%')");
                }
                ORDER_BY("notice.status DESC");
                ORDER_BY("notice.last_update_time DESC");
            }}.toString();
            return str;
        }
    }
}
