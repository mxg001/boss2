package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;

import java.util.List;
import java.util.Map;

public interface TransInfoService {

	List<CollectiveTransOrder> queryAllInfo(CollectiveTransOrder transInfo,Page<CollectiveTransOrder> page);
	
	CollectiveTransOrder queryNumAndMoney(CollectiveTransOrder transInfo);
	
	CollectiveTransOrder queryInfoDetail(String id);

	CollectiveTransOrder queryCollectiveTransOrder(String id);

	int updateInfoByOrderNo(CollectiveTransOrder tt);
	
	TransInfo queryInfo(String orderNo);
	
	ScanCodeTrans queryScanInfo(String orderNo);
	
	CollectiveTransOrder queryCtoInfo(String orderNo);
	
	CollectiveTransOrder selectByOrderNo(int agentNode,String orderNo);
	
	List<SettleTransfer> selectSettleInfo(String tranId,String orderNo);
	
	List<CollectiveTransOrder> queryAllInfoSale(String str,CollectiveTransOrder transInfo,Page<CollectiveTransOrder> page);
	
	//账户调用
	AccountCollectiveTransOrder queryInfoAccount(String orderNo);

	List<CollectiveTransOrder> selectAllRecordAccountFail();

	int updateAccount(CollectiveTransOrder info);
	
	int updateFreezeStatusByOrderNo(String orderNo,String freezeStatus);

	ScanCodeTrans getScanCodeTransByOrder(String orderNo);
	
	String findCardById(int id);

	Map<String, Object> judgePreFreezeaMountAngFreezaTrans(CollectiveTransOrder info);
	
	TransInfo findTransInfoByAcqReferenceNo(String acqReferenceNo);

	/**
	 * 查询出所有选中的结算数据tgh
	 * @param orderNoArr
	 * @return
	 */
	Map<String, Object> getSettle(List<String> orderNoArr);

	/**
	 * 手工结算
	 * @author tans
	 * @date 2017年4月5日 下午2:20:46
	 * @param id
	 * @return
	 */
	Map<String, Object> settleTransInfo(String id);

	/**
	 * 批量手工结算
	 * @author tans
	 * @date 2017年4月5日 下午2:37:26
	 * @param collectiveTransOrders
	 * @return
	 */
	Map<String, Object> settleButch(List<CollectiveTransOrder> collectiveTransOrders, String type);
	/**
	 * 代付订单查询
	 * @param tis
	 * @param page
	 * @return
	 */
	List<ShareSettleInfo>  queryShareSettleInfo(ShareSettleInfo tis, Page<ShareSettleInfo> page);
	int  insertShareSettleInfo(ShareSettleInfo ssInfo);

	List<ShareSettleInfo> exportShareSettleInfo(ShareSettleInfo tis);

//    List<CollectiveTransOrder> getUnSettle(String channelNames, Date startDate, Date endDate, Integer limitNumbers);

	Map<String, Object> syncOrder(List<CollectiveTransOrder> collectiveTransOrders);

	Map<String, Object> getAmountAndNum(String merchantNo,String startTime,String endTime);

	/**
	 * 同步交易状态
	 * @author	mays
	 * @date	2017年12月11日
	 */
	Map<String, Object> syncTransStatus();

	/**
	 * 查询交易成功、记账失败的订单
	 * @param yesDate
	 * @return
	 */
	List<CollectiveTransOrder> selectRecordAccountFail(String yesDate);

	/**
	 * 用收单商户号去acq_terminal查询acqTerminalNo
	 * @author	mays
	 * @date	2018年2月2日
	 */
	String queryAcqTerminalNo(String acqMerchantNo);

	ShareSettleInfo shareSettleInfo(String orderNo);

	/**
	 * 获取交易表数据
	 * 用于调单
	 * liuks
	 */
	CollectiveTransOrder getCtoBySurvey(String orderNo);

	ZqMerchantInfo getAcqMerchant(String uniMerNo);

	AcqTerminalStore getAcqTer(String uniMerNo);

    void accountRecordBatch(String ids, Map<String, Object> msg);

	List<Map<String, Object>> queryResetTransfer(String merchantNo);

	Map<String, Object> updateTradeWarning(Map<String, Object> info,List<Map> list,List<Integer> ids);

    CollectiveTransOrder queryNumAndMoneyByOrderNos(String[] orderNos);

	List<CollectiveTransOrder> selectByOrderNos(String[] orderNos, Page<CollectiveTransOrder> page);

	List<ShareSettleInfo> selectShareSettleInfoByOrderNosWithPage(String[] orderNos, Page<ShareSettleInfo> page);

	List<ShareSettleInfo> selectShareSettleInfoByOrderNos(String[] orderNos);

	List<CollectiveTransOrder> selectT0ByMerchantNoAndTransTime(String rollNo, int reSettleTaskCount);

	List<CollectiveTransOrder> selectT1ByMerchantNo(String rollNo);

	CollectiveTransOrder selectByOrderNo(String orderNo);

    List<CollectiveTransOrder> selectToT1ByMerchantNo(String rollNo);
}
