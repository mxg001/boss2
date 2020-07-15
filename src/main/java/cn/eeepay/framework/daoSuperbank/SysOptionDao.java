package cn.eeepay.framework.daoSuperbank;

import cn.eeepay.framework.daoSuperbank.RedOrgSortDao.SqlProvider;
import cn.eeepay.framework.model.RedOrgSort;
import cn.eeepay.framework.model.SysOption;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

public interface SysOptionDao {

    @Select("select * from t_sys_option a where a.`code` = #{code}")
    @ResultType(SysOption.class)
    SysOption selectByCode(@Param("code") String code);
    
	@SelectProvider(type=SqlProvider.class, method="selectSysOption")
    @ResultType(SysOption.class)
    List<SysOption> selectSysOption(@Param("sysOption")SysOption sysOption);
	
    class SqlProvider{
        public String selectSysOption(Map<String, Object> param){
        	SysOption sysOption = (SysOption) param.get("sysOption");
            SQL sql = new SQL();
            sql.SELECT("T1.id,T1.option_group_id,T1.sub_option_group_id,T1.code,T1.name,T1.description,T1.rank,T1.enabled,T1.created_by,T1.updated_by,T1.date_created,T1.date_updated");
            sql.FROM("t_sys_option T1");
            sql.LEFT_OUTER_JOIN("t_sys_option_group T2 ON T1.option_group_id = T2.id");
            sql.WHERE("T1.enabled = 1");
            if(sysOption != null){
                if(StringUtils.isNotBlank(sysOption.getOptionGroupCode())){
                    sql.WHERE("T2.code=#{sysOption.optionGroupCode}");
                }
                if(StringUtils.isNotBlank(sysOption.getCode())){
                    sql.WHERE("T1.code=#{sysOption.code}");
                }
                if(StringUtils.isNotBlank(sysOption.getName())){
                    sql.WHERE("T1.name=#{sysOption.name}");
                }
                if(null != sysOption.getId()){
                    sql.WHERE("T1.id=#{sysOption.id}");
                }
            }
            sql.ORDER_BY("rank asc");
            return sql.toString();
        }
    }
}
