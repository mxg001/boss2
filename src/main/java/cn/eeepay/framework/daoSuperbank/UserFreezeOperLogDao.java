package cn.eeepay.framework.daoSuperbank;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserFreezeOperLog;

public interface UserFreezeOperLogDao {
	/**
	 * 查询用户冻结解冻日志记录
	 * 
	 * @param userCode 被冻结解冻用户编码
	 * @param page
	 * @return
	 */
	@Select("select * from user_freeze_oper_log where user_code=#{userCode} ORDER BY oper_time desc")
	@ResultType(UserFreezeOperLog.class)
	List<UserFreezeOperLog> getUserFreezeOperLog(@Param("userCode") String userCode,
			@Param("page") Page<UserFreezeOperLog> page);

	/**
	 * 新增用户冻结解冻操作日志记录
	 * 
	 * @param userCode
	 * @return
	 */
	@Insert("insert into user_freeze_oper_log(user_code,oper_id,oper_name,oper_type,oper_time,oper_reason)"
			+ " values(#{record.userCode},#{record.operId},#{record.operName},#{record.operType},now(),#{record.operReason})")
	int insertUserFreezeOperLog(@Param("record") UserFreezeOperLog record);
}
