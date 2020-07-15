package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;

import java.util.List;
import java.util.Map;

public interface CouponActivityService {
	/**
	 * 鼓励金活动列表
	 * @return
	 */
	List<CouponActivityInfo> getAllInfo();
	/**
	 * 鼓励金活动详情
	 * @param actId 活动ID
	 * @return
	 */
	CouponActivityInfo queryRewardDetail(int actId);

	/**
	 * 注册返活动详情
	 * @author	mays
	 * @date	2018年1月22日
	 */
	CouponActivityInfo queryRegisteredDetail();

	/**
	 * 查询注册返券信息
	 * @author	mays
	 * @date	2018年1月23日
	 */
	List<CouponActivityInfo> selectRegisteredCoupon();
	
	
	List<CouponActivityInfo> selectCardAndReward(String activetiy_code);

	/**
	 * 鼓励金活动数据更新
	 * @param couponActivity
	 * @return
	 */
	int rewardSave(CouponActivityInfo reward);
	
	
	int cardAndRewardSave(CouponActivityInfo reward);

	/**
	 * 保存注册返活动配置
	 * @author	mays
	 * @date	2018年1月22日
	 */
	int registeredSave(CouponActivityInfo reward);

	/**
	 * 领用明细统计
	 * @param paramMap
	 * @param page
	 * @return
	 */
	List<CouponTotal> queryCouponAll(CouponTotal coupon, Page<CouponTotal> page);
	/**
	 * 金额统计：赠送金额、可用金额、已用金额、过期金额
	 * @param coupon
	 * @return
	 */
	CouponTotal queryCouponAllTotal(CouponTotal coupon);
	/**
	 * 
	 * @param actId
	 * @return
	 */
	CouponActivityInfo couponRecharge(int actId);
	/**
	 * 
	 * @param actId
	 * @return
	 */
	List<CouponActivityEntity> couponRechargeEntry(String activityCode);
	/**
	 * 
	 * @param couponActivity
	 * @return
	 */
	int saveCouponActivity(CouponActivityInfo couponActivity);
	/**
	 * 
	 * @param entry
	 * @return
	 */
	int saveCouponEntity(CouponActivityEntity entry, List<CouponActivityTime> timeList, List<CouponActivityTime> deleteList);

	List<CouponActivityTime> getCouponActivityTime(Integer entityId);

	/**
	 * 
	 * @param id
	 * @param col
	 * @param val
	 * @return
	 */
	int updateStatus(int id, String col, int val);
	/**
	 * 
	 * @param entityId
	 * @return
	 */
	CouponActivityEntity couponEntityView(int entityId);

	/**
	 * 导出
	 * @param coupon
	 * @return
	 */
    List<CouponTotal> queryCouponList(CouponTotal coupon);

	Map<String,Object> querySysConfig(String key);

	List<Map<String,Object>> actEntityList(String couponCoude);

	int updateMerActStatus(String merchantNo);

	Map<String,Object> activtyOrderInfo(String orderNo);

	int addCouponOrderInfo(String couponNo, String orderNo);

	Map<String,Object> couponInfo(String id);

	int updateCouponStatus(String id);

    Map<String,Object> getNext(String seqName);

	Map<String,Object> getActivityById(int activityEntityId);

	Map<String,Object> getActivityById2(String couponCode, int activityEntityId);

	 int addUserCoupon(Map<String, Object> couponMap);
	 
	 /**
	  * 仅供办卡及贷款鼓励卷专用
	  * @param couponMap
	  * @return
	  */
	 int addUserCouponReload(Map<String, Object> couponMap);

	Map<String,Object> getActivityByCode(String couponCode);
	
	Map<String,Object> getCouponActivityEntityById(String couponCode);

	int cloudPaySave(CouponActivityInfo couponActivity);

	/**
	 * 修改注册返券面值
	 * @author	mays
	 * @date	2018年1月23日
	 */
	int updateRegisteredCoupon(CouponActivityInfo info);

    CouponActivityInfo selectInfoDetail(String activetiyCode);

	int updateInfo(CouponActivityInfo baseInfo);

    Result updateZxIndustry(Map<String, Object> map);

	void execute();

	List<Map> couponActivityVip(String activityCode);

	int updateVipStatus(int id, String col, int val,String teamId);

	int addActivityVip(Map<String, Object> map);

	int updateActivityVip(Map<String, Object> map);

	/***
	 * 判断赠送数量是否大于发行数量
	 * @param params
	 * @param res
	 */
    boolean checkPurchaseCount(Map<String, String> params, Map<String, String> res);

	/**
	 * 保存会员系统券信息
	 * @param entity
	 * @return
	 */
	int saveVipTicket(CouponActivityEntity entity,Map<String,Object> msg);

	/***
	 * 删除会员系统物品信息
	 * @param id
	 * @param msg
	 * @return
	 */
	int delVipEntity(Integer id);
}
