package cn.eeepay.framework.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.boss.action.MerchantBusinessProductAction.SelectParams;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import com.alibaba.fastjson.JSONObject;

public interface MerchantBusinessProductService {

	public List<BusinessProductDefine> selectSourceBpInfo();

	public List<BusinessProductDefine> selectNewBpInfo();

	public MerchantBusinessProduct selectByPrimaryKey(Long id);

	public List<MerchantBusinessProduct> selectAllByStatusInfo(Page<MerchantBusinessProduct> page);

	public List<MerchantBusinessProduct> selectAllInfo(Page<MerchantBusinessProduct> page);

	public List<MerchantBusinessProduct> selectByParam(Page<MerchantBusinessProduct> page,SelectParams selectParams);

	public List<MerchantBusinessProduct> selectMerBusProByIds(List<String> ids);

	public int updateStatusById(String status,String id);

	public String getCoreUrl();

	public List<MerchantBusinessProduct> selectByStatusParam(Page<MerchantBusinessProduct> page,SelectParams selectParams);

	public int updateBymbpId(Long mbpId,String status,String auditor);

	public int reexamineBymbpId(Long mbpId,String status,String auditor);

	public int resetReexamineBymbpId(Long mbpId,String status,Date time);

	public Map<String, Object> updateByItemAndMbpId(MerchantBusinessProduct record,MerchantInfo mis,List<MerchantRequireItem> mris,List<MerchantServiceRate> msr,List<MerchantServiceQuota> msq,Integer userId,UserModel userModel);

	//商户审核更新
    int updateMbpExamineInfo(MbpStatusCondition mbpStatus, MerchantBusinessProduct mbpInfo, List<MerchantRequireItem> mri, MerchantInfo merchantInfo, MerchantCardInfo mci, ExaminationsLog el);

	public List<String> querySerivceId(String bpId);

	public List<MerchantBusinessProduct> selectAllInfoSale(Page<MerchantBusinessProduct> page,String name);

	public List<MerchantBusinessProduct> selectByParamSale(Page<MerchantBusinessProduct> page,SelectParams selectParams,String name);

	public List<AutoCheckResult> selectAutoCheckResult(String merchantNo,String bpId);

	public List<Map<String, Object>> findDefRouteGroupAdd(String merchantNo, String bpId, String channelCode);

	//修改APP自动审件次数为3
	public int updateCheckNum(String mobile,String teamId);

	public MerchantBusinessProduct selectMerBusPro(String merchantNo,String bpId);

	/**
	 * 查询商户的业务产品个数
	 * @param merchantNo
	 * @return
	 */
	public Integer selectMerProLimit(String merchantNo);

	public MerchantBusinessProduct findCollectionCodeMbp(String merchantNo);

	Page<MerchantBusinessProductHistory> selectMerBpHistoryList(Map<String, Object> params,
																Page<MerchantBusinessProductHistory> page);

	public List<MerchantBusinessProduct> getByMer(String merchantNo);

	int updateTradeTypeById(Long primaryKey, String tradeType);

	MerchantBusinessProduct selectByServiceId(Long merServiceId);

	int updateTradeTypeByServices(MerchantBusinessProduct merBusPro);

	/**
	 * 查询商户审核状态
	 * @author	mays
	 * @date	2017年8月14日 下午4:18:21
	 */
	String queryExamineStatus(String bpId, String merchantNo);

	Result ysUpdateMer(String merchantNo, String mbpId, String bpId, String acqEnname);

	List<Map<String, Object>> examineTotalByParam(SelectParams selectParams);

	void exportExamine(SelectParams selectParams, HttpServletResponse response, HttpServletRequest request);

    String getTeamName(String teamId);

	public String getUserIdByMerchantInfo(String merchantNo);

	public MerchantBusinessProduct selectByMerchantNo(String merchantNo);

	public List<MerchantBusinessProduct> selectByMerchantNoAll(String merchantNo);

	public String getDeviceIdByPhone(String mobilephone);

	public void initSysConfigByKey();

	public void SendHttpSc(Map<String, Object> oneMap);

	int getMerCreditCard(String merNo);

	public List<MerchantBusinessProduct> getByMerAndBpId(String merchantNo);

	public String getByCodeAndType(String autoMbpChannel, int i);

	List<MerchantBusinessProduct> selectNextMbpInfo(MerchantBusinessProduct info);

	MerchantBusinessProduct getMerchantBusinessProductInfo(Long mbpId);

	MerchantBusinessProduct getMbpInfoDetail(Long mbpId);

	//获取活体检测通道
	List<Map> queryHuoTiChannels()throws Exception;

	//获取鉴权通道
	List<Map<String, String>> queryAuthChannels() throws Exception;

    List<String> selectBpIdsByMerchantNo(String merchantNo);

	MerchantBusinessProduct selectByMerchantNoAndBpId(String merchantNo, String bpId);

	List<Map<String, Object>> selectTerBpInfo(String merchantNo, String bpId);

	int updateMerchantBusinessProduct(String merchantNo, String oldBpId, String newBpId);

	int insertMerBusProHis(MerchantBusinessProductHistory hisMbp);

	int deleteMerBusItem(String merchantNo);

	List<ServiceInfoBean> listServiceInfoByBpId(String bpId);

	int deleteMerRate(String bpId, String merchantNo);

	int deleteMerQuota(String bpId, String merchantNo);

	int updateMerchantService(String merchantNo, String oldBpId, String newBpId, String oldServiceId, String newServiceId);

	void bacthInsertServiceRate(List<ServiceRate> newServiceRateList, String merchantNo);

	int bacthInsertServiceQuota(List<ServiceQuota> newServiceQuotaList, String merchantNo);

	int countZF_ZQAndSyncSuccess(String merchantNo, String bpId);
}