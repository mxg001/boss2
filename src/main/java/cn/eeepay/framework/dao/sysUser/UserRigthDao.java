package cn.eeepay.framework.dao.sysUser;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.UserRight;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public interface UserRigthDao{
	
//	@Insert("insert into user_rigth(user_id,rigth_id)" 
//			+"values(#{userRigth.userId},#{userRigth.rigthId})"
//			)
//	int insertUserRigth(@Param("userRigth")UserRigth userRigth);
	
	@Insert("insert into boss_user_rigth(user_id,rigth_id) values(#{userId},#{rigthId})")
	int insertUserRigth(@Param("userId")Integer userId,@Param("rigthId")Integer rigthId);
	
//	@Delete("delete from user_rigth where user_id = #{userRigth.userId} and rigth_id = #{userRigth.rigthId}")
//	int deleteUserRigth(@Param("userRigth")UserRigth userRigth);
	
	@Delete("delete from boss_user_rigth where user_id = #{userId} and rigth_id = #{rigthId}")
	int deleteUserRigth(@Param("userId")Integer userId,@Param("rigthId")Integer rigthId);
	
	@Delete("delete from boss_user_rigth where rigth_id = #{rigthId}")
	int deleteUserRigthByRigthId(@Param("rigthId")Integer rigthId);
	
	@Select(" select ur.id,ur.user_id,ur.rigth_id,sr.id as rigth_id,sr.rigth_code,sr.rigth_name,sr.rigth_comment,sr.rigth_type "+
			" from boss_user_rigth as ur,boss_shiro_rigth as sr "+
			" where ur.rigth_id = sr.id "+
			" and user_id = #{userId}")
	@ResultMap("cn.eeepay.framework.dao.UserRigthMapper.OneToOneResultMap")
	List<UserRight> findUserRigthByUserId(@Param("userId")Integer userId);

}
