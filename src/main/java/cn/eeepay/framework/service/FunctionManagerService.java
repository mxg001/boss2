package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.FunctionManager;
import cn.eeepay.framework.model.IndustrySwitch;
import cn.eeepay.framework.model.IndustrySwitchInfo;
import cn.eeepay.framework.model.VasRate;
import cn.eeepay.framework.model.function.AppShowConfig;
import cn.eeepay.framework.model.function.FunctionTeam;

public interface FunctionManagerService {

	/**
	 * 查询所有
	 * @return
	 */
	List<FunctionManager> selectFunctionManagers();

	int updateFunctionSwitch(FunctionManager info);

	int updateVasRateStatus(VasRate info);

	int updateFunctionManageConfigStatus(FunctionTeam info);

	int updateAgentControl(FunctionManager info);

	FunctionManager getFunctionManager(int id);

	FunctionManager getFunctionManagerByNum(String funcNum);

	VasRate getVasRateById(int id);

	IndustrySwitchInfo getIndustrySwitchInfo();

	void industrySwitchSave(IndustrySwitch data);

	void industrySwitchDelete(Long id);

	void industrySwitchUpdate(Integer industrySwitch);

    int updateBaseInfo(FunctionManager baseInfo);

	List<FunctionTeam> getFunctionTeamList(String functionNumber);

	FunctionTeam getFunctionTeamById(String functionNumber,int id);

	List<VasRate> getVasRateList(String functionNumber);

	int saveFunctionTeam(FunctionTeam info, Map<String, Object> msg);

	int updateFunctionTeam(FunctionTeam info, Map<String, Object> msg);

	int saveFunctionConfigure(FunctionTeam info, Map<String, Object> msg);

	int deleteFunctionTeam(int id);

	int deleteSpecialFunctionTeam(String functionNumber,int id);

	List<AppShowConfig> getAppShowList(int fmcId);

	int saveAppShowList(int fmcId, List<AppShowConfig> addList);

    FunctionTeam getFunctionTeam(int id);

	int updateFunctionTeamVas(FunctionTeam info, Map<String, Object> msg);
}
