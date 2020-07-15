package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.SuperCollection;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by Administrator on 2017/12/18/018.
 * @author liuks
 * 超级还款设置dao
 */
public interface SuperCollectionDao {

    @Select(
            "SELECT id,number,start_time,end_time,day_lines,create_time,modify_time " +
                    " FROM super_collection_switch " +
                    " WHERE number=#{number}"
    )
    SuperCollection selectByNumber(@Param("number")String number);

    @Update(
            "UPDATE super_collection_switch set start_time=#{info.startTime},end_time=#{info.endTime}," +
                    " day_lines=#{info.dayLines},modify_time=NOW() " +
                    " WHERE number=#{info.number}"
    )
    int saveSuperCollection(@Param("info")SuperCollection sc);
}
