package cn.eeepay.framework.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.model.AuditorCountInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.boss.action.AuditorManagerAction.AuditorRecord;
import cn.eeepay.framework.dao.AuditorManagerDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AuditorManagerInfo;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.AuditorManagerService;

@Service("AuditorManagerService")
@Transactional
public class AuditorManagerServiceImpl implements AuditorManagerService {
	
	@Resource
	private AuditorManagerDao auditorManagerDao;
	
	@Resource
	private AgentInfoService agentInfoService;
	
	@Override
	public List<AuditorManagerInfo> selectByCondition(Page<AuditorManagerInfo> page, AuditorManagerInfo baseInfo) {
		return auditorManagerDao.selectByCondition(page,baseInfo);
	}

	@Override
	public List<AuditorManagerInfo> getBpByAuditor(String auditorId) {
		return auditorManagerDao.getBpByAuditor(auditorId);
	}

	@Override
	public int insertBatch(String auditorId, List<AuditorManagerInfo> list) {
		auditorManagerDao.deleteByAuditor(auditorId);
		int i = auditorManagerDao.insertBatch(list);
		return i;
	}

	@Override
	public int updateStatus(AuditorManagerInfo info) {
		return auditorManagerDao.updateStatus(info);
		
	}

	@Override
	public int deleteData(Integer id) {
		return auditorManagerDao.deleteData(id);
	}

	@Override
	public List<AuditorCountInfo> selecrAllInfoRecord(Page<AuditorCountInfo> page,AuditorCountInfo info,Map<String, Object> msg) {
		if(checkDate(info,msg)){
			List<AuditorCountInfo> list =auditorManagerDao.selectAllInfoRecord(page,info);
			msg.put("page", page);
			msg.put("status",true);
			return list;
		}
		return null;
	}

	@Override
	public List<AuditorCountInfo> selecrAllInfoRecordDetail(Page<AuditorCountInfo> page,AuditorCountInfo info,Map<String, Object> msg){
		if(checkDate(info,msg)){
			List<AuditorCountInfo> list=auditorManagerDao.getRecordByUserList(page,info);
			msg.put("page", page);
			msg.put("status", true);
		}
		return null;
	}

	@Override
	public List<AuditorManagerInfo> selecrAllInfoRecordDetail2(Page<AuditorManagerInfo> page, AuditorCountInfo info,Map<String, Object> msg) {
		if(checkDate(info,msg)){
			List<AuditorManagerInfo> flists = auditorManagerDao.getlogList(page,info);
			msg.put("page", page);
			msg.put("status", true);
		}
		return null;
	}
	private boolean checkDate(AuditorCountInfo info,Map<String, Object> msg){
		if (info.getsTime() != null && info.geteTime() != null) {
			// 判断起始时间是否大于结束时间
			long stime = info.getsTime().getTime();
			long etime = info.geteTime().getTime();
			if (stime > etime) {
				msg.put("status", false);
				msg.put("msg", "起始时间不能大于结束时间");
				return false;
			}
			// 判断时间是否超过了30天
			int sum = (int) ((etime - stime) / (24 * 60 * 60 * 1000));
			if (sum > 31) {
				msg.put("status", false);
				msg.put("msg", "审核时间最长不超过一个月");
				return false;
			}
		} else {
			msg.put("status", false);
			msg.put("msg", "审核时间必须输入");
			return false;
		}
		return true;
	}

	@Override
	public List<AuditorManagerInfo> exportInfoList(AuditorCountInfo info){
		return auditorManagerDao.exportInfoList(info);
	}

}
