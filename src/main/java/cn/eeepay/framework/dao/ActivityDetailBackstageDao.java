package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.ActivityDetailBackstage;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Administrator on 2018/1/30/030.
 * @author  liuks
 * 欢乐送,欢乐返活动延时核算清算 dao
 */
public interface ActivityDetailBackstageDao {
    @Insert(
            "INSERT INTO activity_detail_backstage(batch_no,act_id,act_state,user_id,send_num,create_time) " +
                    " VALUES(#{act.batchNo},#{act.actId},#{act.actState},#{act.userId},0,NOW())"
    )
    int insertActivityDetailBackstage(@Param("act")ActivityDetailBackstage act);

    @Select(
            "SELECT * FROM activity_detail_backstage where act_id=#{actId} and act_state=#{actState} "
    )
    List<ActivityDetailBackstage> getActivityDetailBackstage(@Param("actId")int actId,@Param("actState")String actState);

    @Delete(
            "delete from activity_detail_backstage where id=#{id}"
    )
    int deleteActivityDetailBackstage(@Param("id")int id);


    @Select(
            "select * from activity_detail_backstage "
    )
    List<ActivityDetailBackstage> getActivityDetailBackstageListAll();

    //更新时间
    @Update(
            "update activity_detail_backstage set send_num=#{sendNum} where id=#{id}"
    )
    int updateActivityDetailBackstage(@Param("sendNum")int sendNum,@Param("id")int id);

    @Select(
            "select count(*) from activity_detail_backstage where act_state=#{actState}"
    )
    int countActivityDetailBackstage(@Param("actState")String actState);

    @Select("select * from activity_detail_backstage where act_state in ('2','3') limit 0,50000")
    List<ActivityDetailBackstage> getActivityDetailBackstageListByHappyReturn();

}
