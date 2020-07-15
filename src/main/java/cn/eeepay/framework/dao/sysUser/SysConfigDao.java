package cn.eeepay.framework.dao.sysUser;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.SysConfig;
/**
 * 系统配置相关这个对数据查的比较多
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public interface SysConfigDao {
	@Insert("insert into sys_config(sys_key,sys_name,sys_value)"
			+ " values(#{sysConfig.sysKey},#{sysConfig.sysName},#{sysConfig.sysValue})")
	@ResultMap("cn.eeepay.framework.dao.SysConfigMapper.BaseResultMap")
	int insertSysConfig(@Param("sysConfig")SysConfig sysConfig);
	
	
	@Update("update sys_config set sys_key=#{sysConfig.sysKey},sys_name=#{sysConfig.sysName},sys_value=#{sysConfig.sysValue}"
			+"  where id=#{sysConfig.id}")
	int updateSysConfig(@Param("sysConfig")SysConfig sysConfig);
	
	@Delete("delete from sys_config where id = #{id}")
	int deleteSysConfig(@Param("id")String id);
	
	@Delete("delete from sys_config where sysKey = #{sysKey} and sysValue = #{sysValue}")
	int deleteSysConfigByParams(@Param("sysKey")String sysKey,@Param("sysValue")String sysValue);
	
	
	@Select("select id,sys_key,sys_name,sys_value from sys_config where sys_key = #{sysKey} and sys_value = #{sysValue} ")
	@ResultMap("cn.eeepay.framework.dao.SysConfigMapper.BaseResultMap")
	SysConfig findSysConfig(@Param("sysKey")String sysKey,@Param("sysValue")String sysValue);
	
	
	@Select("select sys_key,sys_name,sys_value from sys_config where sys_key = #{sysKey} ")
	@ResultMap("cn.eeepay.framework.dao.SysConfigMapper.BaseResultMap")
	List<SysConfig> findSysConfigGroup(@Param("sysKey")String sysKey);
	
	
	@Select("select sys_key,sys_name,sys_value from sys_config ")
	@ResultMap("cn.eeepay.framework.dao.SysConfigMapper.BaseResultMap")
	List<SysConfig> findAllSysConfig();

	@Select("select param_value from sys_config where param_key=#{key} limit 1")
	String getStringValueByKey(@Param("key") String key);
}
