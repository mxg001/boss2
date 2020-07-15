package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.boss.action.AuditorManagerAction.AuditorRecord;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AuditorCountInfo;
import cn.eeepay.framework.model.AuditorManagerInfo;

public interface AuditorManagerService {

	List<AuditorManagerInfo> selectByCondition(Page<AuditorManagerInfo> page, AuditorManagerInfo baseInfo);

	List<AuditorManagerInfo> getBpByAuditor(String auditorId);

	int insertBatch(String auditorId, List<AuditorManagerInfo> list);

	int updateStatus(AuditorManagerInfo info);

	int deleteData(Integer id);

	// 记录
	List<AuditorCountInfo> selecrAllInfoRecord(Page<AuditorCountInfo> page, AuditorCountInfo info,Map<String, Object> msg);

	List<AuditorCountInfo> selecrAllInfoRecordDetail(Page<AuditorCountInfo> page,AuditorCountInfo info,Map<String, Object> msg);

	List<AuditorManagerInfo> selecrAllInfoRecordDetail2(Page<AuditorManagerInfo> page, AuditorCountInfo info,Map<String, Object> msg);

	List<AuditorManagerInfo> exportInfoList(AuditorCountInfo info);

}
