package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.CouponActivityDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.CouponActivityService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.ZxIndustryConfigService;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 
 * @author ivan
 * @date 2017/06/19
 *
 */
@Service("rewardActivityService")
@Transactional
public class CouponActivityServiceImpl implements CouponActivityService{
	private static final Logger log = LoggerFactory.getLogger(CouponActivityServiceImpl.class);

	@Resource
	private CouponActivityDao rewardActivityDao;

	@Resource
	private ZxIndustryConfigService zxIndustryConfigService;

	@Resource
	private SysDictService sysDictService;

	@Override
	public List<CouponActivityInfo> getAllInfo() {
		return rewardActivityDao.getAllInfo();
	}

	@Override
	public CouponActivityInfo queryRewardDetail(int actId) {
		return rewardActivityDao.queryRewardDetail(actId);
	}

	@Override
	public CouponActivityInfo queryRegisteredDetail() {
		return rewardActivityDao.queryRegisteredDetail();
	}

	@Override
	public List<CouponActivityInfo> selectRegisteredCoupon() {
		return rewardActivityDao.selectRegisteredCoupon();
	}

	@Override
	public int updateRegisteredCoupon(CouponActivityInfo info) {
		return rewardActivityDao.updateRegisteredCoupon(info);
	}

	@Override
	public CouponActivityInfo selectInfoDetail(String activetiyCode) {
		CouponActivityInfo info = rewardActivityDao.selectInfoDetail(activetiyCode);
		if(info != null){
			//兼容前端的开关组件
			if(info.getStatus() != null && "0".equals(info.getStatus())){
				info.setStatusInt(1);//页面开关，1表示打开
			} else {
				info.setStatusInt(0);
			}
		}
		return info;
	}

	@Override
	public int updateInfo(CouponActivityInfo baseInfo) {
		return rewardActivityDao.updateInfo(baseInfo);
	}

	@Override
	public Result updateZxIndustry(Map<String, Object> map) {
		Result result = new Result();
		JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(map));
		CouponActivityInfo baseInfo = JSONObject.parseObject(json.getString("baseInfo"), CouponActivityInfo.class);
		List<ZxIndustryConfig> list = JSONObject.parseArray(json.getString("list"), ZxIndustryConfig.class);
		if(baseInfo != null){
			//0表示打开，1表示关闭
			if(baseInfo.getStatusInt() != null && baseInfo.getStatusInt() == 1){
				baseInfo.setStatus("0");
			} else {
				baseInfo.setStatus("1");
			}
		}
		UserLoginInfo loginInfo = CommonUtil.getLoginUser();
		Date nowDate = new Date();
		if(list != null && list.size() > 0){
			Pattern pattern = Pattern.compile("^(0|[1-9]\\d+)|(0|[1-9]\\d+).(\\d){1,2}$");
			for(ZxIndustryConfig config: list){
				Matcher mather = pattern.matcher(String.valueOf(config.getRate()).trim());
				if(!mather.matches()){
					result.setMsg(config.getTeamName()+"费率格式有误,请输入最多三位小数的正数");
					return result;
				}
				config.setUpdateTime(nowDate);
				config.setOperator(loginInfo.getRealName());
			}
		}

		baseInfo.setUpdateTime(nowDate);
		baseInfo.setOperator(loginInfo.getRealName());
		int numBaseInfo = rewardActivityDao.updateInfo(baseInfo);
		if(numBaseInfo != 1){
			throw new BossBaseException("更新自选行业收费基本信息失败");
		}
		int numConfig = zxIndustryConfigService.updateBatch(list);
		if(numConfig != list.size()){
			throw new BossBaseException("更新自选行业收费费率失败");
		}
		result.setStatus(true);
		result.setMsg("操作成功");
		return result;
	}

	@Override
	public int rewardSave(CouponActivityInfo reward) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		reward.setOperator(principal.getUsername());
		reward.setUpdateTime(new Date());
		rewardActivityDao.rewardSave(reward);
		rewardActivityDao.rewardEntrySave(reward);
		return 1;
	}
	@Override
	public int cardAndRewardSave(CouponActivityInfo reward) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		reward.setOperator(principal.getUsername());
		reward.setUpdateTime(new Date());
		rewardActivityDao.cardAndRewardSave(reward);
		rewardActivityDao.rewardEntrySave(reward);
		return 1;
	}

	
	
	
	@Override
	public int registeredSave(CouponActivityInfo reward) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		reward.setOperator(principal.getUsername());
		reward.setUpdateTime(new Date());
		rewardActivityDao.registeredSave(reward);
		rewardActivityDao.rewardEntrySave(reward);
		return 1;
	}

	@Override
	public List<CouponTotal> queryCouponAll(CouponTotal coupon, Page<CouponTotal> page) {
		coupon.setPageSize(page.getPageSize());
		coupon.setPageFirst(page.getFirst());
		List<CouponTotal> list=rewardActivityDao.queryCouponAll(coupon);
		page.setResult(list);
		return list;
	}

	@Override
	public CouponTotal queryCouponAllTotal(CouponTotal coupon) {
		return rewardActivityDao.queryCouponAllTotal(coupon);
	}

	@Override
	public CouponActivityInfo couponRecharge(int actId) {
		return rewardActivityDao.queryCouponRecharge(actId);
	}

	@Override
	public List<CouponActivityEntity> couponRechargeEntry(String activityCode) {
		return rewardActivityDao.couponRechargeEntry(activityCode);
	}

	@Override
	public int saveCouponActivity(CouponActivityInfo couponActivity) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		couponActivity.setOperator(principal.getUsername());
		return rewardActivityDao.saveCouponActivity(couponActivity);
	}

	@Override
	public int saveCouponEntity(CouponActivityEntity entry,List<CouponActivityTime> timeList, List<CouponActivityTime> deleteList) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		entry.setOperator(principal.getUsername());
		int i=0;
		if(entry.getId() == 0){
			//活动类型转换成对于的券类型，根据核销方式来转换
			//1 --》 6  鼓励金
			//2 -- 》 3  抵用金
			//5 -- 》 7  满溢金
			if("1".equals(entry.getCancelVerificationCode())){
				entry.setCouponType("6");
			}else if("2".equals(entry.getCancelVerificationCode())){
				entry.setCouponType("3");
			}else if("5".equals(entry.getCancelVerificationCode())){
				entry.setCouponType("7");
			}
			i = rewardActivityDao.insertCouponEntity(entry);
			for(CouponActivityTime c:timeList){
				c.setEntityId(entry.getId());
				rewardActivityDao.insertCouponTime(c);
			}
		}else{
			i = rewardActivityDao.saveCouponEntity(entry);
			for(CouponActivityTime c:timeList){
				if(c.getId()!=null){
					rewardActivityDao.saveCouponTime(c);
				}else{
					c.setEntityId(entry.getId());
					rewardActivityDao.insertCouponTime(c);
				}
			}
			for(CouponActivityTime c:deleteList){
				if(c.getId()!=null){
					rewardActivityDao.deleteCouponTime(c.getId());
				}
			}
		}
		return i;
	}

	@Override
	public List<CouponActivityTime> getCouponActivityTime(Integer entityId){
		return rewardActivityDao.getCouponActivityTime(entityId);
	}

	@Override
	public int updateStatus(int id, String col, int val) {
		if("isshelves".equals(col)){
			if(val==1){
				rewardActivityDao.deleteCouponTimeByEntityId(id);
			}
			return rewardActivityDao.updateIsshelves(id,val);
		}else if("status".equals(col)){
			return rewardActivityDao.updateStatus(id,val);
		}else{
			return 0;
		}
	}

	@Override
	public CouponActivityEntity couponEntityView(int entityId) {
		return rewardActivityDao.couponEntityView(entityId);
	}
	public List<CouponTotal> queryCouponList(CouponTotal coupon) {
		return rewardActivityDao.queryCouponList(coupon);
	}

	@Override
	public Map<String,Object> querySysConfig(String key) {
		return rewardActivityDao.querySysConfig(key);
	}

	@Override
	public List<Map<String, Object>> actEntityList(String couponCoude) {
		return rewardActivityDao.actEntityList(couponCoude);
	}

	@Override
	public int updateMerActStatus(String merchantNo) {
		return rewardActivityDao.updateMerActStatus(merchantNo);
	}

	@Override
	public Map<String, Object> activtyOrderInfo(String orderNo) {
		return rewardActivityDao.activtyOrderInfo(orderNo);
	}

	@Override
	public int addCouponOrderInfo(String couponNo, String orderNo) {
		return rewardActivityDao.addCouponOrderInfo(couponNo,orderNo);
	}

	@Override
	public Map<String, Object> couponInfo(String id) {
		return rewardActivityDao.couponInfo(id);
	}

	@Override
	public int updateCouponStatus(String id) {
		return rewardActivityDao.updateCouponStatus(id);
	}

	@Override
	public Map<String, Object> getNext(String seqName) {
		return rewardActivityDao.getNext(seqName);
	}

	@Override
	public Map<String, Object> getActivityById(int activityEntityId) {
		return rewardActivityDao.getActivityById(activityEntityId);
	}

	@Override
	public Map<String, Object> getActivityById2(String couponCode, int activityEntityId) {
		return rewardActivityDao.getActivityById2(couponCode,activityEntityId);
	}

	@Override
	public int addUserCoupon(Map<String, Object> couponMap) {
		return rewardActivityDao.addUserCoupon(couponMap);
	}
	
	
	public synchronized int addUserCouponReload(Map<String, Object> couponMap) {
		
		int i = rewardActivityDao.selectForStatusById(couponMap.get("given_status_id").toString());
		
		if (i==0) {
			return rewardActivityDao.addUserCoupon(couponMap);
		}
		
		return 0;
	}

	@Override
	public Map<String, Object> getActivityByCode(String couponCode) {
		return rewardActivityDao.getActivityByCode(couponCode);
	}

	@Override
	public Map<String, Object> getCouponActivityEntityById(String couponCode) {
		return rewardActivityDao.getCouponActivityEntityById(couponCode);
	}
	
	
	@Override
	public int cloudPaySave(CouponActivityInfo reward) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		reward.setOperator(principal.getUsername());
		reward.setUpdateTime(new Date());
		rewardActivityDao.cloudPaySave(reward);
		rewardActivityDao.cloudPayEntrySave(reward);
		return 1;
	}

	@Override
	public void execute() {
		List<CouponActivityEntity> list=rewardActivityDao.getCouponActivityTimeJob();
		int count=0;
		if(list!=null){
			for(CouponActivityEntity entity:list){
				if(entity.getSum()>0){
					if(entity.getIsshelves()==0){
						continue;
					}
					count+=rewardActivityDao.updateIsshelves(entity.getId(),0);
				}else{
					if(entity.getIsshelves()==1){
						continue;
					}
					count+=rewardActivityDao.updateIsshelves(entity.getId(),1);
				}
			}
		}
		if(count>0){
			clearAllCache();
		}
	}

	@Override
	public List<Map> couponActivityVip(String activityCode) {
		return rewardActivityDao.couponActivityVip(activityCode);
	}

	@Override
	public int updateVipStatus(int id, String col, int val,String teamId) {
		if("is_recommend".equals(col)){
			if(val==1){
				rewardActivityDao.updateIsRecommendClose(teamId);
			}
			return rewardActivityDao.updateIsRecommend(id,val);
		}else if("is_switch".equals(col)){
			return rewardActivityDao.updateIsSwitch(id,val);
		}else{
			return 0;
		}
	}

	@Override
	public int addActivityVip(Map<String, Object> map){
		return rewardActivityDao.addActivityVip(map);
	}

	@Override
	public int updateActivityVip(Map<String, Object> map){
		return rewardActivityDao.updateActivityVip(map);
	}

	public String clearAllCache() {
		String url=sysDictService.getValueByKey("CORE_URL");
		url+="/rechargeReturn/clearAllCache";
		String accountMsg = ClientInterface.httpPost(url, null);
		log.info("清空缓存 url:{}，response:{}",url,accountMsg);
		return accountMsg;
	}

	@Override
	public List<CouponActivityInfo> selectCardAndReward(String activetiyCode) {
		// TODO Auto-generated method stub
		return rewardActivityDao.selectCardAndReward(activetiyCode);
	}

	@Override
	public boolean checkPurchaseCount(Map<String, String> params, Map<String, String> res) {

		try {
			String addNum = params.get("addNum")==null?"0":params.get("addNum");
			String activityEntityId = StringUtil.filterNull(params.get("activityEntityId"));
			String couponCode = StringUtil.filterNull(params.get("couponCode"));
			String merchantNo = StringUtil.filterNull(params.get("merchantNo"));


			CouponActivityEntity entiity = rewardActivityDao.selectCouponEntityById(Integer.valueOf(activityEntityId));

			//获取当日赠送数
			Date now = new Date();
			String begin = DateUtil.getFormatDate("yyyy-MM-dd", now)+" 00:00:00";

			String end = DateUtil.getFormatDate("yyyy-MM-dd", DateUtils.addDays(now,1))+" 00:00:00";

			Integer purchaseCount = rewardActivityDao.countTodayPurchaseCount(Integer.valueOf(activityEntityId), begin, end);
			//当前商户当天赠送数量
			Integer merchantPurchaseCount = rewardActivityDao.countMerchantTodayPurchaseCount(Integer.valueOf(activityEntityId), begin, end,merchantNo);


			if(entiity.getPurchaseCount() != -1 && ((Integer.valueOf(addNum)+merchantPurchaseCount)-entiity.getPurchaseCount())>0){
				res.put("msg","赠送的优惠券超过商户当日可赠送数上限！");
				return false;
			}

			if((Integer.valueOf(addNum)+purchaseCount)>entiity.getCouponCount()){
				res.put("msg","赠送的优惠券发行数已超过当日上限！");
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}


	@Override
	public int saveVipTicket(CouponActivityEntity entity,Map<String,Object> msg) {
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			entity.setOperator(principal.getUsername());
			if(entity.getId()==0){//新增
				return rewardActivityDao.saveVipTicket(entity);
			}else{//修改
				return rewardActivityDao.updateVipTicket(entity);
			}

		} catch (Exception e) {
			msg.put("msg","保存物品信息失败");
			msg.put("status",false);
			log.error("保存物品信息失败",e);
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int delVipEntity(Integer id) {
		try {
			return rewardActivityDao.delVipEntity(id);
		} catch (Exception e) {
			log.error("删除物品信息失败",e);
			e.printStackTrace();
		}
		return -1;
	}
}
