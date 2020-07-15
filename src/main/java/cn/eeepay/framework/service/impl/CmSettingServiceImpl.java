package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoCreditMgr.CmSettingDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmVipConfig;
import cn.eeepay.framework.model.CmVipConfigAgent;
import cn.eeepay.framework.service.CmSettingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("cmSettingService")
@Transactional
public class CmSettingServiceImpl implements CmSettingService{

	@Resource
	private CmSettingDao cmSettingDao;
	

	/**
	 * 根据type查询会员配置信息
	 */
	public CmVipConfig selectVipConfigByType(int type) {
		return cmSettingDao.selectVipConfigByType(type);
	}

	/**
	 * 修改会员配置信息
	 */
	public int updateVipConfig(CmVipConfig info) {
		return cmSettingDao.updateVipConfig(info);
	}

	public List<CmVipConfigAgent> selectVipConfigAgent(Page<CmVipConfigAgent> page, CmVipConfigAgent info){
		return cmSettingDao.selectVipConfigAgent(page,info);
	}

	public CmVipConfigAgent selectVipConfigAgentByAgentNo(String agentNo){
		return cmSettingDao.selectVipConfigAgentByAgentNo(agentNo);
	}

	public int selectVipConfigAgentByAgentNoCount(String agentNo){
		return cmSettingDao.selectVipConfigAgentByAgentNoCount(agentNo);
	}

	public int selectVipConfigAgentCount(CmVipConfigAgent info){
		return cmSettingDao.selectVipConfigAgentCount(info);
	}

	public int saveCmSettingAgent(CmVipConfigAgent info){
		return cmSettingDao.saveCmSettingAgent(info);
	}

	public int deleteCmSettingAgentByAgentNo(String agentNo){
		return cmSettingDao.deleteCmSettingAgentByAgentNo(agentNo);
	}

	public int updateCmSettingAgent(CmVipConfigAgent info){
		return cmSettingDao.updateCmSettingAgent(info);
	}

	public int addButchCmSettingAgent(List<CmVipConfigAgent> list){
		return cmSettingDao.addButchCmSettingAgent(list);
	}
}
