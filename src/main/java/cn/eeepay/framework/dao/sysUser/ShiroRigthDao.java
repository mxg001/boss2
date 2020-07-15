package cn.eeepay.framework.dao.sysUser;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.ShiroRigth;

public interface ShiroRigthDao{
	
	@Insert("insert into boss_shiro_rigth(rigth_code,rigth_name,rigth_comment,rigth_type)"
			+"values(#{shiroRigth.rigthCode},#{shiroRigth.rigthName},#{shiroRigth.rigthComment},#{shiroRigth.rigthType})"
			)
	int insertShiroRigth(@Param("shiroRigth")ShiroRigth shiroRigth);
	
	@Delete("delete from boss_shiro_rigth where rigth_code = #{rigthCode} ")
	int deleteShiroRigthByRigthCode(@Param("rigthCode")String rigthCode);
	
	@Select("select * from boss_shiro_rigth where rigth_code = #{rigthCode}")
	@ResultMap("cn.eeepay.framework.dao.ShiroRigthMapper.BaseResultMap")
	ShiroRigth findShiroRigthByRigthCode(@Param("rigthCode")String rigthCode);
	
	@Select("select id,rigth_code,rigth_name,rigth_comment,rigth_type from boss_shiro_rigth where id =#{id}")
	@ResultMap("cn.eeepay.framework.dao.ShiroRigthMapper.OneToManyResultMap")
	ShiroRigth findShiroRigthById(@Param("id")Integer id);
	
	@Select("select * from boss_shiro_rigth ")
	@ResultMap("cn.eeepay.framework.dao.ShiroRigthMapper.BaseResultMap")
	List<ShiroRigth> findAllShiroRigth();
	
	@Select("select DISTINCT shiro_rigth.id,shiro_rigth.rigth_code,shiro_rigth.rigth_name,shiro_rigth.rigth_comment,shiro_rigth.rigth_type"
			+ " from boss_shiro_user,boss_user_role,boss_shiro_role,boss_role_rigth,boss_shiro_rigth,boss_sys_menu"
			+ " where shiro_user.id = user_role.user_id"
			+ " and user_role.role_id = shiro_role.id"
			+ " and shiro_role.id = role_rigth.role_id"
			+ " and role_rigth.rigth_id = shiro_rigth.id"
			+ " and shiro_rigth.rigth_code = sys_menu.menu_code"
			+ " and shiro_user.id=#{userId} "
			+ " order by sys_menu.order_no ")
	@ResultMap("cn.eeepay.framework.dao.ShiroRigthMapper.BaseResultMap")
	List<ShiroRigth> findUserRolePrivilegeRigth(@Param("userId")Integer userId);
	
	
	@Select("select shiro_rigth.id,shiro_rigth.rigth_code,shiro_rigth.rigth_name,shiro_rigth.rigth_comment,shiro_rigth.rigth_type"
			+ " from boss_shiro_user,boss_user_role,boss_shiro_role,boss_role_rigth,boss_shiro_rigth,boss_sys_menu"
			+ " where shiro_user.id = user_role.user_id"
			+ " and user_role.role_id = shiro_role.id"
			+ " and shiro_role.id = role_rigth.role_id"
			+ " and role_rigth.rigth_id = shiro_rigth.id"
			+ " and shiro_rigth.rigth_code = sys_menu.menu_code"
			+ " and shiro_user.id = #{userId} "
			+ " and sys_menu.parent_id = #{parentId} "
			+ " order by sys_menu.order_no ")
	@ResultMap("cn.eeepay.framework.dao.ShiroRigthMapper.BaseResultMap")
	List<ShiroRigth> findUserWithRolesPrivilegeRigthByParentId(@Param("userId")Integer userId,@Param("parentId")Integer parentId);
	
	
	@Select(" select shiro_rigth.id,shiro_rigth.rigth_code,shiro_rigth.rigth_name,shiro_rigth.rigth_comment,shiro_rigth.rigth_type "+
			"  from boss_role_rigth,boss_shiro_rigth,boss_sys_menu "+
			"  where  role_rigth.rigth_id = shiro_rigth.id "+
			"  and shiro_rigth.rigth_code = sys_menu.menu_code "+
			"  and sys_menu.parent_id = #{parentId}"+ 
			"  and role_rigth.role_id = #{roleId}  "+
			"  order by sys_menu.order_no ")
	@ResultMap("cn.eeepay.framework.dao.ShiroRigthMapper.BaseResultMap")
	List<ShiroRigth> findRolePrivilegeRigthByParentId(@Param("roleId")Integer roleId,@Param("parentId")Integer parentId);
	
	@Select(" select r.id,r.rigth_code,r.rigth_name,r.rigth_comment,r.rigth_type  "+
			" from boss_sys_menu as m,boss_shiro_rigth as r "+
			" where m.menu_code = r.rigth_code "+
			" and m.parent_id = #{parentId} "+ 
			" and m.menu_type='page' ")
	@ResultMap("cn.eeepay.framework.dao.ShiroRigthMapper.BaseResultMap")
	List<ShiroRigth> findShireRigthByParentId(@Param("parentId")Integer parentId);
	
//	public class SqlProvider{
//		public String findUsers(Map<String,Object> param){
//			final ShiroUser user=(ShiroUser)param.get("user");
//			final Sort sord=(Sort)param.get("sort");
//			return new SQL(){{
//				SELECT("user_Name,`password`,real_Name,tel_No,email,state,theme,create_Operator,create_Time");
//				FROM("shiro_user");
//				if(StringUtils.isNotBlank(user.getUserName())){
//					WHERE("user_name=#{user.userName}");
//				}
//				if(StringUtils.isNotBlank(user.getRealName())){
//					WHERE("real_name=#{user.realName}");
//				}
//				if(StringUtils.isNotBlank(user.getEmail())){
//					WHERE("email=#{user.email}");
//				}
//				if(StringUtils.isNotBlank(sord.getSidx())){
//					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
//				}
//			}}.toString();
//		}
//		public String propertyMapping(String name,int type){
//			final String[] propertys={"userName","password","realName","telNo","email","state","theme",
//		    		"createOperator","createTime"};
//		    final String[] columns={"user_Name","password","real_Name","tel_No","email","state","theme",
//		    		"create_Operator","create_Time"};
//		    if(StringUtils.isNotBlank(name)){
//		    	if(type==0){//属性查出字段名
//		    		for(int i=0;i<propertys.length;i++){
//		    			if(name.equalsIgnoreCase(propertys[i])){
//		    				return columns[i];
//		    			}
//		    		}
//		    	}else if(type==1){//字段名查出属性
//		    		for(int i=0;i<propertys.length;i++){
//		    			if(name.equalsIgnoreCase(columns[i])){
//		    				return propertys[i];
//		    			}
//		    		}
//		    	}
//		    }
//			return null;
//		}
//	}
}
