package cn.eeepay.framework.dao.capitalInsurance;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.SafeConfig;
import cn.eeepay.framework.model.capitalInsurance.SafeLadder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/23/023.
 * @author liuks
 * 资金险dao
 */
public interface SafeConfigDao {

    @SelectProvider(type=SafeConfigDao.SqlProvider.class,method="selectAllList")
    @ResultType(SafeConfig.class)
    List<SafeConfig> selectAllList(@Param("safe") SafeConfig safe, @Param("page") Page<SafeConfig> page);

    @Select(
            "select * from zjx_safe_config where id=#{id}"
    )
    SafeConfig getSafeConfig(@Param("id")int id);

    @Select(
            "select * from zjx_safe_ladder where pro_code=#{proCode}"
    )
    List<SafeLadder> getSafeLadderList(@Param("proCode")String proCode);

    @Update(
            "update zjx_safe_config " +
                    " set phone=#{safe.phone},agent_share=#{safe.agentShare},last_update_time=now() " +
                    " where id=#{safe.id}"
    )
    int saveSafeConfig(@Param("safe")SafeConfig safe);

    @Update(
            "update zjx_safe_config " +
                    " set route_scale=#{safe.newRouteScale},last_update_time=now() " +
                    " where id=#{safe.id}"
    )
    int saveRouteScale(@Param("safe")SafeConfig safe);

    @Update(
            "update zjx_safe_ladder " +
                    " set price=#{ladder.price} " +
                    " where id=#{ladder.id}"
    )
    int saveSafeLadder(@Param("ladder")SafeLadder ladder);

    @Select(
            "select * from zjx_safe_config "
    )
    List<SafeConfig> getSafeConfigList();

    @Select("select * from zjx_safe_config where pro_code=#{proCode}")
    @ResultType(SafeConfig.class)
    SafeConfig getSafeConfigByProCode(@Param("proCode") String proCode);

    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            final SafeConfig safe = (SafeConfig) param.get("safe");
            return new SQL(){{
                SELECT("safe.*,safe.route_scale as newRouteScale,sc.sys_name as bxUnitName ");
                FROM("zjx_safe_config safe");
                LEFT_OUTER_JOIN("sys_dict sc on safe.bx_unit=sc.sys_value and sc.sys_key='insurer_number'");
                if(StringUtils.isNotBlank(safe.getProCode())){
                    WHERE("safe.pro_code = #{safe.proCode} ");
                }
                if(StringUtils.isNotBlank(safe.getProName())){
                    WHERE("safe.pro_name like concat(#{safe.proName},'%') ");
                }
            }}.toString();
        }
    }

}
