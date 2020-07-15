package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.ChangeLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface ChangeLogDao {

    @Insert("INSERT INTO " +
            "pa_change_log(change_pre,change_after,remark,create_time,operater,oper_method) " +
            "VALUE(#{changeLog.changePre},#{changeLog.changeAfter},#{changeLog.remark},now()," +
            "#{changeLog.operater},#{changeLog.operMethod})")
    int insertChangeLog(@Param("changeLog") ChangeLog changeLog);
}
