package cn.eeepay.framework.daoExchange.exchangeActivate;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Administrator on 2018/10/13/013.
 * @author  liuks
 * 系统配置的 rdmp_sys_config
 */
public interface RdmpSysConfigDao {

    @Select(
            "select PARAM_VALUE from rdmp_sys_config where PARAM_KEY=#{key}"
    )
    @ResultType(String.class)
    String getConfigValue(@Param("key") String key);
}
