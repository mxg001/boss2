package cn.eeepay.framework.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.dao.SysDictDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.RiskRulesDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RiskRules;
import cn.eeepay.framework.model.RiskRulesLog;
import cn.eeepay.framework.model.TeamInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.RiskRulesService;

@Service("riskRulesService")
@Transactional
public class RiskRulesServiceImpl implements RiskRulesService {

	@Resource
	private RiskRulesDao riskRulesDao;

	@Resource
	private SysDictDao sysDictDao;

	@Override
	public List<RiskRules> selectAllInfo(Page<RiskRules> page, RiskRules rr) {
		return riskRulesDao.selectAllInfo(page, rr);
	}

	@Override
	public RiskRules selectDetail(int id) {
		return riskRulesDao.selectDetail(id);
	}

	@Override
	public int updateInfo(RiskRules rr) {
		int num =0;
		RiskRules r1=riskRulesDao.selectDetail(rr.getRulesNo());
		RiskRulesLog rrl=new RiskRulesLog();
		rrl.setRulesNo(rr.getRulesNo());
		UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		rrl.setUpdatePerson(principal.getRealName());
		String content="修改了:";
		if(rr.getRulesValues()!=null&&r1.getRulesValues()!=null&&
				!rr.getRulesValues().trim().equals(r1.getRulesValues().trim())){
			content+="规则,";
		}
		if(rr.getTreatmentMeasures()!=null&&r1.getTreatmentMeasures()!=null&&
				!rr.getTreatmentMeasures().trim().equals(r1.getTreatmentMeasures().trim())){
			content+="处理措施,";
		}
		String con="";
		for (int i = 0; i < content.length(); i++) {
			if(i==content.length()-1){
				break;
			}
			con+=content.charAt(i);
		}
		rrl.setContent(con);
		if(content.length()>4){
			num += riskRulesDao.insertLogInfo(rrl);
		}
		num += riskRulesDao.updateInfo(rr);
		return num;
	}

	@Override
	public int updateStatus(RiskRules rr) {
		return riskRulesDao.updateStatus(rr);
	}

	@Override
	public RiskRules selectByRoll(String roll) {
		return riskRulesDao.selectByRoll(roll);
	}

	@Override
	public List<RiskRules> selectAll() {
		return riskRulesDao.selectAll();
	}

	@Override
	public List<RiskRules> selectAllWithOutStatus() {
		return riskRulesDao.selectAllWithOutStatus();
	}

	@Override
	public int updateBatch(List<RiskRules> list) {
		return riskRulesDao.updateBatchForWhiteRoll(list);
	}

	@Override
	public int updateRulesInstruction(RiskRules rr) {
		return riskRulesDao.updateRulesInstruction(rr);
	}

	@Override
	public RiskRules selectByRulesNo(Integer rulesNo) {
		return riskRulesDao.selectByRulesNo(rulesNo);
	}

	@Override
	public List<TeamInfo> getAllScope() {
		return riskRulesDao.getAllScope();
	}

	@Override
	public void updateFaceRecognition(String jskj, String xskj) {
		sysDictDao.updateValueBySysKey(jskj,"FACE_RECOGNITION_JSKJ");
		sysDictDao.updateValueBySysKey(xskj,"FACE_RECOGNITION_XSKJ");
	}
}
