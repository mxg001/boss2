package cn.eeepay.framework.dao.sysUser;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.ShiroRole;

public interface ShiroRoleDao{
	
	
	@Select("select id,role_code,role_name,role_remake,role_state,create_operator,create_time,update_time from boss_shiro_rigth where role_code = #{rigthCode}")
	@ResultMap("cn.eeepay.framework.dao.ShiroRoleMapper.BaseResultMap")
	ShiroRole findShiroRoleByRoleCode(@Param("roleCode")String roleCode);
	
	@Select("select id,role_code,role_name,role_remake,role_state,create_operator,create_time,update_time from boss_shiro_role where id = #{id}")
	@ResultMap("cn.eeepay.framework.dao.ShiroRoleMapper.BaseResultMap")
	ShiroRole findShiroRoleById(@Param("id")Integer id);
	
	@Select("select id,role_code,role_name,role_remake,role_state,create_operator,create_time,update_time from boss_shiro_role ")
	@ResultMap("cn.eeepay.framework.dao.ShiroRoleMapper.BaseResultMap")
	List<ShiroRole> findAllShiroRole();
	
	@Update("update boss_shiro_role set role_code = #{shiroRole.roleCode},role_name= #{shiroRole.roleName},role_remake= #{shiroRole.roleRemake},update_time= #{shiroRole.updateTime} where id = #{shiroRole.id}")
	int updateShiroRole(@Param("shiroRole")ShiroRole shiroRole);
	
	@Insert("insert into boss_shiro_role(role_code,role_name,role_remake,role_state,create_operator,create_time) "
			+ "values(#{shiroRole.roleCode},#{shiroRole.roleName},#{shiroRole.roleRemake},#{shiroRole.roleState},#{shiroRole.createOperator},#{shiroRole.createTime})")
	int insertShiroRole(@Param("shiroRole")ShiroRole shiroRole);
}
