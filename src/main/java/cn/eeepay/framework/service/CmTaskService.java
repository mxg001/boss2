package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmTaskInfo;

import java.util.List;
import java.util.Map;

public interface CmTaskService {

	/**
	 * 查询limit_add_bank_strategy里的所有银行名称
	 * @author	mays
	 * @date	2018年5月7日
	 */
	List<Map<String,String>> selectBankName();

	/**
	 * 提额任务查询
	 * @author mays
	 * @date 2018年4月8日
	 */
	List<CmTaskInfo> selectTaskInfo(Page<CmTaskInfo> page, CmTaskInfo info);

	/**
	 * 提额任务详情
	 * @author	mays
	 * @date	2018年5月8日
	 */
	Map<String, Object> queryTaskDetail(Map<String, Object> msg, String id);

	List<CmTaskInfo> exportTaskInfo(CmTaskInfo info);

}
