package cn.eeepay.framework.daoExchange;

import cn.eeepay.framework.model.exchange.ExchangeConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by Administrator on 2018/4/10/010.
 * @author liuks
 */
public interface RandomNumberDao {

    @Select(
        " select id id,PARAM_KEY sysKey,PARAM_VALUE sysValue, REMARK remark from rdmp_sys_config where PARAM_KEY=#{key}"
    )
    ExchangeConfig getConfig(@Param("key")String key);

    @Update(
            "update rdmp_sys_config set PARAM_VALUE=#{value} where PARAM_KEY=#{key} "
    )
    void saveConfig(@Param("key")String key, @Param("value")String value);
}
