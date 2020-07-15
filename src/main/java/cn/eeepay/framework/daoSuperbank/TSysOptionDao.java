package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.model.TSysOption;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface TSysOptionDao {


    @Update("update t_sys_option set name=#{name},updated_by=#{updateBy} ,date_updated=#{dateUpdated} where id=#{id}")
    long updTSysOption(TSysOption tSysOption);

    @Select("SELECT id,option_group_id,sub_option_group_id,code,name,description,rank,enabled  FROM t_sys_option order by option_group_id desc")
    @ResultType(TSysOption.class)
    List<TSysOption> querySysOptionList();

}
