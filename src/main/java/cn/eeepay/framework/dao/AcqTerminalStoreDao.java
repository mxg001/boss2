package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.AcqTerminalStore;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

/**
 * @author tans
 * @date 2019/3/26 17:15
 */
public interface AcqTerminalStoreDao {

    @Select("select ter_no from acq_terminal_store where sn = #{sn}")
    @ResultType(AcqTerminalStore.class)
    AcqTerminalStore selectBySn(@Param("sn")String sn);
}
