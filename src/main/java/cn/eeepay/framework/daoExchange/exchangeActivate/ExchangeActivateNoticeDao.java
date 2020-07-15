package cn.eeepay.framework.daoExchange.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateNotice;
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
public interface ExchangeActivateNoticeDao {

    @SelectProvider(type=ExchangeActivateNoticeDao.SqlProvider.class,method="selectAllList")
    @ResultType(ExchangeActivateNotice.class)
    List<ExchangeActivateNotice> selectAllList(@Param("notice") ExchangeActivateNotice notice, @Param("page") Page<ExchangeActivateNotice> page);

    @Insert(
            "INSERT INTO yfb_notice " +
                    " (title,content,oem_no_set,status,create_user," +
                    "  create_time,up_time,down_time,remark,last_update_time) " +
                    " VALUES " +
                    " (#{notice.title},#{notice.content},#{notice.oemNoSet},'2',#{notice.createUser}," +
                    "  NOW(),#{notice.upTime},#{notice.downTime},#{notice.remark},NOW()) "
    )
    int addNotice(@Param("notice") ExchangeActivateNotice notice);

    @Select(
            "select * from yfb_notice where id=#{id}"
    )
    ExchangeActivateNotice getNotice(@Param("id") long id);

    @Update(
            "update yfb_notice set " +
                    " title=#{notice.title},content=#{notice.content},oem_no_set=#{notice.oemNoSet}, " +
                    " up_time=#{notice.upTime},down_time=#{notice.downTime},remark=#{notice.remark}," +
                    " last_update_time=NOW() " +
                    " where id=#{notice.id}"
    )
    int updateNotice(@Param("notice") ExchangeActivateNotice notice);

    @Update(
            "update yfb_notice set status=#{state},last_update_time=NOW() where id=#{id}"
    )
    int updateNoticeState(@Param("id") long id, @Param("state") String state);

    //下发
    @Update(
            "update yfb_notice set status=#{state},last_update_time=NOW()," +
                    " send_time=NOW(),send_user=#{user}" +
                    " where id=#{id}"
    )
    int sendNotice(@Param("id") long id, @Param("state") String state, @Param("user") String user);

    //回收
    @Update(
            "update yfb_notice set status=#{state},last_update_time=NOW()," +
                    " send_time=null,send_user=null" +
                    " where id=#{id}"
    )
    int sendColseNotice(@Param("id") long id, @Param("state") String state);

    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final ExchangeActivateNotice notice = (ExchangeActivateNotice) param.get("notice");
            String str=new SQL(){{
                SELECT("notice.*");
                FROM("yfb_notice notice");
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
                ORDER_BY("notice.status DESC");
                ORDER_BY("notice.last_update_time DESC");
            }}.toString();
            return str;
        }
    }
}
