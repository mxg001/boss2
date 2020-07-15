package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.AcqTerminalStore;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

/**
 * 报备机具与上游终端绑定
 */
public interface AcqTterminalStoreDao {

    @Select("select * from acq_terminal_store where sn = #{sn} and acq_enname = #{acqEnname} limit 1")
    @ResultType(AcqTerminalStore.class)
    AcqTerminalStore selectBySnAcq(@Param("sn") String sn, @Param("acqEnname") String acqEnname);
}
