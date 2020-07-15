package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ActivityDetailService {
	//欢乐送业务活动查询
	List<ActivityDetail> selectActivityDetail(Page<ActivityDetail> page,ActivityDetail activityDetail);

	/**
	 * 导出满足查询条件的所有数据
	 * @author tans
	 * @date 2017年3月29日 下午4:56:04
	 * @param page
	 * @param activityDetail
	 * @param response
	 * @throws IOException
	 */
	void exportExcel(Page<ActivityDetail> page, ActivityDetail activityDetail, HttpServletResponse response) throws IOException;

	/**
	 * 回盘导入
	 * 改写activity_detail里面的扣回状态
	 * @author tans
	 * @date 2017年3月29日 下午4:55:41
	 * @param file
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	Map<String, Object> importDiscount(MultipartFile file) throws EncryptedDocumentException, InvalidFormatException, IOException;

	/**
	 * 根据ID查询详情信息
	 * @author  Ivan
	 * @date 2017/03/28
	 * @param id
	 * @return
	 */
	ActivityDetail getActivityDetailById(int id);
	/**
	 * 订单审核，核算同意的订单所对应的补贴代理商金额数据自动到送至代理商欢乐送账户余额。核算不同意数据不做处理
	 * @author  Ivan
	 * @date 2017/03/29
	 * @param status 核算状态
	 * @return
	 */
	Map<String, Object> updateAdjust(ActivityDetail ad,Page<ActivityDetail> page,String status);

	List<ActivityDetail> selectHappyBackDetail(Page<ActivityDetail> page, ActivityDetail activityDetail);

	List<HappySendNewOrder> selectHappySendOrderDetail(Page<HappySendNewOrder> page, HappySendNewOrder happySendNewOrder);

	List<CashBackDetail> selectAgentReturnCashDetailAll(Integer id,int amountType);

	void exportHappySendOrder(HappySendNewOrder happySendNewOrder,HttpServletResponse response) throws IOException;

	ActivityDetail selectHappyBackDetailById(Integer id);
	String selectHappyTixianSwitch();

	void exportHappyBack(Page<ActivityDetail> page, ActivityDetail activityDetail,HttpServletResponse response) throws IOException;

	Map<String, Object> selectHappyBackTotalAmount(ActivityDetail activityDetail);

	Map<String, Object> selectHappySendOrderTotalAmount(HappySendNewOrder happySendNewOrder);

	/**
	 * 欢乐返清算核算
	 * @author tans
	 * @date 2017年6月27日 上午9:52:29
	 * @return
	 */
	Map<String, Object> updateLiquidation(ActivityDetail ad,Page<ActivityDetail> page,String liquidationStatus);

	/**
	 * 欢乐返财务核算
	 * @author tans
	 * @date 2017年6月27日 上午10:33:54
	 * @return
	 */
	Map<String, Object> updateAccountCheck(ActivityDetail ad,Page<ActivityDetail> page, String accountCheckStatus);


	/**
	 * 批量奖励入账
	 * 2017-12-26
	 */
	Map<String, Object> rewardIsBooked(ActivityDetail ad,Page<ActivityDetail> page);

	/**
	 * 奖励入账
	 * 2017-12-26
	 */
	Map<String, Object> oneRewardIsBooked(Integer id);

	/**
	 * 欢乐返批量奖励入账
	 */
	Map<String, Object> joyToAccount(ActivityDetail ad,Page<ActivityDetail> page);

	NewHappyBackActivityResult newHappyBackCount(NewHappyBackActivityQo qo);

	NewHappyBackActivityResult newHappyBackQuery(NewHappyBackActivityQo qo);

	List<AgentAwardDetailVo> agentAwardDetail(Long id);

	List<XhlfActivityMerchantOrder> getXhlfMerOrderLists(String ids);

	List<XhlfActivityOrder> getXhlfAgentOrderLists(String ids);

}
