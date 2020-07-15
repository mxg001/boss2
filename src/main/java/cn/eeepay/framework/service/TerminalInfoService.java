package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.TerminalInfo;
import cn.eeepay.framework.model.allAgent.TerInfo;
import cn.eeepay.framework.model.TerActivityCheck;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface TerminalInfoService {

	int insert(TerminalInfo record);

	int insertSelective(TerminalInfo terminalInfo);

	long countTerNoBySn(String sn);

	long countBySn(String sn);

	int updateByPrimaryKey(TerminalInfo record);

//	List<TerminalInfo> selectAllInfo(Page<TerminalInfo> page);

	TerminalInfo selectObjInfo(Long id);

	List<TerminalInfo> selectByParam(Page<TerminalInfo> page, TerminalInfo terminalInfo);

	List<TerminalInfo> importDetailSelect(TerminalInfo terminalInfo);

	List<TerminalInfo> selectByAddParam(TerminalInfo terminalInfo);

	// 解分
	int updateSolutionById(TerminalInfo record);

	int updateRecoverSolutionById(TerminalInfo record);

	// 解绑
	Map<String, Object> updateUnbundlingById(Long id);

	// 批量修改机具活动
	int updateAllTerActivity(TerminalInfo record);

	int updateBundlingById(TerminalInfo record);

	TerminalInfo selectBySameData(TerminalInfo record);

	List<TerminalInfo> selectAllInfoBymerNoAndBpId(String merNo, String bpId, Page<TerminalInfo> tiPage);

	int insertBatch(Integer codeNumber, String hpId);

	List<TerminalInfo> getByIds(String[] idList);

	// 根据商户号获取收款码
	String getReceivableCodeByMerchant(String merchantNo);

	// 根据sn获取激活码
	String getActiveCodeBySn(String sn);

	int updateOpenStatusBatch(List<String> ids, String opentStatus);

	List<Map<String, String>> selectAllActivityType();

	// 批量修改机具硬件种类及活动时，先判断时候存在已使用的机具
	List<TerminalInfo> selectAllTerActivity(TerminalInfo terInfo);

	/**
	 * 绑定机具之前，对机具做相应的判断，是否符合绑定条件： 1.要属于当前的代理商 2.未被商户绑定
	 * 
	 * @author tans
	 * @date 2017年4月25日 下午2:02:20
	 * @param terminalInfo
	 * @return
	 */
//	Map<String, Object> checkBeforeBind(TerminalInfo terminalInfo);

	/**
	 * 绑定机具
	 * 
	 * @author tans
	 * @date 2017年4月25日 下午6:15:46
	 * @param terminalInfo
	 * @return
	 */
	int updateBundlingBySn(TerminalInfo terminalInfo);

	List<TerminalInfo> getPageByMerchant(String merchantNo, Page<TerminalInfo> page);

	/**
	 * 根据活动名称匹配活动类型
	 * 
	 * @author tans
	 * @date 2017年6月15日 上午11:42:43
	 * @param activityName
	 * @param activityList
	 * @return
	 */
	String getActivityType(String activityName, List<SysDict> activityList);

	Map<String, Object> updateAllTerActivity(String param, boolean checkStatus);

    Result batchBindingTerminal(String merNo, String bpId, List<TerminalInfo> terList);

    Map<String,Object> bindBatch(InputStream inputStream, String agentNo, String merchantNo, String bpId) throws IOException, InvalidFormatException;

	List<TerInfo> querySNList(TerInfo info, Page<TerInfo> page);

	List<TerInfo> querySNByTerInfo(TerInfo info);

	List<TerInfo> queryShipMachineDetail(String orderNo, Page<TerInfo> page);

	void importDetail(List<TerminalInfo> list, HttpServletResponse response) throws Exception;

    Map<String,Object> downloadResult(String batchNo, HttpServletResponse response,Map<String, Object> msg) throws Exception;

    List<TerInfo> queryHlfBindSn(String activityTypeNo);

	int insertTerminalOperate(Long id, Integer oper_detail_type,Integer oper_type);

	int insertAgentRerminalOperate(String agentNo,String sn, Integer oper_detail_type,Integer oper_type);

	void selectCjtTerPage(TerminalInfo baseInfo, Page<TerminalInfo> page);

    int insertOne(TerActivityCheck tac);

	List<TerminalInfo> selectWithTACByCondition(Page<TerminalInfo> page, TerminalInfo terminalInfo);

    void exportTerminalWithTAC(HttpServletResponse response, TerminalInfo terminalInfo) throws Exception;

    Map<String, Object> changeDueDays(MultipartFile file, HttpServletRequest request) throws Exception;
}
