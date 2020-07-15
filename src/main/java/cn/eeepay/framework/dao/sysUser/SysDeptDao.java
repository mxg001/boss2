package cn.eeepay.framework.dao.sysUser;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.ShiroUser;
import cn.eeepay.framework.model.SysDept;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public interface SysDeptDao{
	/**
	 * 新增用户信息
	 */
	@Insert("insert into shiro_user(user_Name,`password`,real_Name,tel_No,email,state,theme,create_Operator,create_Time)"
			+ " values(#{user.userName},#{user.password},#{user.realName},#{user.telNo},#{user.email},#{user.state},"
			+ "#{user.theme},#{user.createOperator},#{user.createTime})")
	@SelectKey(statement="select LAST_INSERT_ID()", keyProperty="id", before=false, resultType=int.class)  
	int insert(@Param("sysDept")SysDept sysDept);
	
	@SelectProvider(type=SqlProvider.class,method="findSysDeptList")
	@ResultMap("cn.eeepay.framework.dao.SysDeptMapper.BaseResultMap")
	List<SysDept> findSysDeptList(@Param("sysDept")SysDept sysDept,@Param("sort")Sort sort,Page<SysDept> page);
	
	@Select("select * from sys_dept")
	@ResultMap("cn.eeepay.framework.dao.SysDeptMapper.OneToManyResultMap")
	List<SysDept> findAllSysDeptList();
	
	@Select("select id,dept_name from sys_dept where id =#{id}")
	@ResultMap("cn.eeepay.framework.dao.SysDeptMapper.OneToManyResultMap")
	SysDept findSysDeptById(@Param("id")String id);
	
	
	
	public class SqlProvider{
		public String findSysDeptList(Map<String,Object> param){
			final ShiroUser user=(ShiroUser)param.get("user");
			final Sort sord=(Sort)param.get("sort");
			return new SQL(){{
				SELECT("user_Name,`password`,real_Name,tel_No,email,state,theme,create_Operator,create_Time");
				FROM("shiro_user");
				if(StringUtils.isNotBlank(user.getUserName())){
					WHERE("user_name=#{user.userName}");
				}
				if(StringUtils.isNotBlank(user.getRealName())){
					WHERE("real_name=#{user.realName}");
				}
				if(StringUtils.isNotBlank(user.getEmail())){
					WHERE("email=#{user.email}");
				}
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
		}
		public String propertyMapping(String name,int type){
			final String[] propertys={"userName","password","realName","telNo","email","state","theme",
		    		"createOperator","createTime"};
		    final String[] columns={"user_Name","password","real_Name","tel_No","email","state","theme",
		    		"create_Operator","create_Time"};
		    if(StringUtils.isNotBlank(name)){
		    	if(type==0){//属性查出字段名
		    		for(int i=0;i<propertys.length;i++){
		    			if(name.equalsIgnoreCase(propertys[i])){
		    				return columns[i];
		    			}
		    		}
		    	}else if(type==1){//字段名查出属性
		    		for(int i=0;i<propertys.length;i++){
		    			if(name.equalsIgnoreCase(columns[i])){
		    				return propertys[i];
		    			}
		    		}
		    	}
		    }
			return null;
		}
	}
}
