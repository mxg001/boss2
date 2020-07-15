package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RedControl;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface RedControlDao {

    @Select("select id, bus_code, bus_type, open_status, org_status from red_control order by bus_code")
    @ResultType(RedControl.class)
    List<RedControl> selectRedControl(@Param("page")Page<RedControl> page);
    
    @Select("select id, bus_code, bus_type, open_status, org_status from red_control where id=#{id}")
    @ResultType(RedControl.class)
    RedControl selectByPrimaryKey(Long id);
    
    @Update("update red_control set open_status=#{openStatus},update_time=#{updateTime},"+
            "operator=#{operator} where id=#{id}")
    int updateRedOpen(RedControl redControl);

    @Update("update red_control set org_status=#{orgStatus},update_time=#{updateTime}," +
            "operator=#{operator} where id=#{id}")
    int updateRedOrg(RedControl redControl);

    @Select("select p_value from sys_pinfu_chaju where code=#{key}")
    @ResultType(String.class)
	String getSysPinfuChajuKey(@Param("key")String key);
}
