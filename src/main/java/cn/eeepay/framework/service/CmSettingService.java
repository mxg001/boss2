package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmVipConfig;
import cn.eeepay.framework.model.CmVipConfigAgent;

import java.util.List;

public interface CmSettingService {

	/**
	 * 根据type查询会员配置信息
	 * @author	mays
	 * @date	2018年4月6日
	 */
	CmVipConfig selectVipConfigByType(int type);

	/**
	 * 修改会员配置信息
	 * @author	mays
	 * @date	2018年5月19日
	 */
	int updateVipConfig(CmVipConfig info);

	List<CmVipConfigAgent> selectVipConfigAgent(Page<CmVipConfigAgent> page, CmVipConfigAgent info);

	CmVipConfigAgent selectVipConfigAgentByAgentNo(String agentNo);

	int selectVipConfigAgentByAgentNoCount(String agentNo);

	int selectVipConfigAgentCount(CmVipConfigAgent info);

	int saveCmSettingAgent(CmVipConfigAgent info);

	int deleteCmSettingAgentByAgentNo(String agentNo);

	int updateCmSettingAgent(CmVipConfigAgent info);

	int addButchCmSettingAgent(List<CmVipConfigAgent> list);
}
