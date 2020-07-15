package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.MsgDao;
import cn.eeepay.framework.dao.SysCalendarDao;
import cn.eeepay.framework.daoSuperbank.SequenceDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Msg;
import cn.eeepay.framework.model.Sequence;
import cn.eeepay.framework.model.SysCalendar;
import cn.eeepay.framework.service.MsgService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("msgService")
@Transactional
public class MsgServiceImpl implements MsgService {
	
	@Resource
	private MsgDao msgDao;

	@Resource
	public SequenceDao sequenceDao;
	
	@Override
	public void selectMsgByCondition(Page<Msg> page, Msg msg) {
		msgDao.selectMsgByCondition(page,msg);
	}

	@Override
	public int insertMsg(Msg msg) {
		return msgDao.insertMsg(msg);
	}

	@Override
	public int updateMsg(Msg msg) {
		return msgDao.updateMsg(msg);
		
	}
	
	@Override
	public Msg msgDetail(String id) {
		return msgDao.msgDetail(id);
	}

	@Override
	public int selectByReasonAndOrg(String reason, String sourceOrg,Integer id) {
		return msgDao.selectByReasonAndOrg(reason,sourceOrg,id);
	}

	@Override
	public String queryByResonAndAcqName(String resMsg, String acqName) {
		return msgDao.queryByResonAndAcqName(resMsg,acqName);
	}

	@Override
	public String queryMsgByReason(String resMsg) {
		return msgDao.queryMsgByReason(resMsg);
	}

	private synchronized String getSeqValue(String moduleName) {
		
		String value = msgDao.getSeqByKey(moduleName);
		
		if(StringUtils.isBlank(value)){
			return "0";
		}
		msgDao.updateSeq(Integer.valueOf(value)+1,moduleName);
		
		return value;
	}
	
	
	@Override
	public void addMsg(Msg msg,Map msgMap) {
		//校验
		int num = selectByReasonAndOrg(msg.getReason(),msg.getSourceOrg(),null);
		
		if(num>=1){
			msgMap.put("msg", "已经存在相同的错误原因和收单结构");
			return ;
		}
		
		//String value = sequenceDao.getValue("msg_"+msg.getModuleName());
		
		 String	 value = this.getSeqValue("msg_"+msg.getModuleName());
		
		
		if ("0".equals(value)){
			msgMap.put("msg", "不合法的字典值！");
			return ;
		}
		if(Integer.valueOf(value)>9999){
			msgMap.put("msg", "序列号已超出！");
			return ;
		}
		msg.setMsgCode(value);
		msg.setCreateTime(new Date());
		msg.setMsgCode(msg.getModuleName()+new DecimalFormat("0000").format(Integer.valueOf(msg.getMsgCode())));
		int i =insertMsg(msg);
		
		if(i==1){
			msgMap.put("status", true);
			msgMap.put("msg", "添加成功！");
		}else{
			msgMap.put("status", false);
			msgMap.put("msg", "添加失败！");
		}
	}

	@Override
	public void updateMsg(Msg msg, Map msgMap) {
		//校验
		int num = selectByReasonAndOrg(msg.getReason(),msg.getSourceOrg(),msg.getId());
		
		if(num>=1){
			msgMap.put("msg", "已经存在相同的错误原因和收单机构");
			return ;
		}
		msg.setLast_updateTime(new Date());
		int i = updateMsg(msg);
		
		if(i==1){
			msgMap.put("status", true);
			msgMap.put("msg", "更新成功！");
		}else{
			msgMap.put("status", false);
			msgMap.put("msg", "更新失败！");
		}
	}

	@Override
	public int changeStatus(String id, String status) {
		return msgDao.changeStatus(id,status);
	}

	@Override
	public List<Msg> queryAgentTips() {
		return msgDao.queryAgentTips();
	}

	@Override
	public Msg queryMsgByMsgCode(String msgCode) {
		return msgDao.queryMsgByMsgCode(msgCode);
	}
}
