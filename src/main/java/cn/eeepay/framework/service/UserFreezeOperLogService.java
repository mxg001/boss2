package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserFreezeOperLog;

public interface UserFreezeOperLogService {

	/**
	 * 查询:用户冻结解冻日志记录
	 * 
	 * @param userCode
	 * @param page
	 * @return
	 */
	List<UserFreezeOperLog> getUserFreezeOperLog(String userCode, Page<UserFreezeOperLog> page);

	/**
	 * 新增：一条用户冻结解冻日志记录
	 * 
	 * @param record
	 * @return
	 */
	int insertUserFreezeOperLog(UserFreezeOperLog record);

}
