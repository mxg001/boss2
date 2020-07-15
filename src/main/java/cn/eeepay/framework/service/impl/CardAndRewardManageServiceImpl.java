package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.CardAndRewardManageDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.CardAndRewardManageService;
import cn.eeepay.framework.service.CouponActivityService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.DateUtils;
import cn.eeepay.framework.util.HttpUtils;
import cn.eeepay.framework.util.Md5;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("cardAndRewardManageService")
@Transactional
public class CardAndRewardManageServiceImpl implements CardAndRewardManageService {

	
	private static final Logger log = LoggerFactory.getLogger(CardAndRewardManageServiceImpl.class);

	
	@Resource
	private CardAndRewardManageDao cardAndRewardManageDao;

	@Resource
	private CouponActivityService couponActivityService;

	@Resource
	private SensorsService sensorsService;

	
	
	@Resource
	private SysDictService sysDictService;
	
    private final String Token = "a185faf4c55807bca4bcfdaae06b58fa";
    private final String USER_INFO_ID = "4IE5DhZE4IJ2gdv9YmDr6jfaaIBBlyafFNPz2wYXtqN";
    private final String PATH = "/riskhandle/commonJPush";
	
	
	
	@Override
	public List<CardAndReward> selectUserInfo(Page<CardAndReward> page, CardAndReward info) {
		List<CardAndReward> list = cardAndRewardManageDao.selectUserInfo(page, info);
		change(list);
		return list;
	}
	@Override
	public List<CardAndReward> selectCardLoanHeartenLogById(String id) {
		 List<CardAndReward> list = cardAndRewardManageDao.selectCardLoanHeartenLogById(id);
		 change(list);
		return list;
	}
	
	
	public List<CardAndReward> exportCardLoanHeartenLogById(String id) {
		 List<CardAndReward> list = cardAndRewardManageDao.selectCardLoanHeartenLogById(id);
		 changeReload(list);
		return list;
	}
	
	private void change(List<CardAndReward> list) {
		List<SysDict> selectByKey = sysDictService.selectByKey("SENDCHANNEL");
			Map<String,String> map = new HashMap<>();
			for (SysDict sysDict : selectByKey) {
				map.put(sysDict.getSysValue(), sysDict.getSysName());
			}
			for (CardAndReward cardAndReward : list) {
				if (StringUtils.isNotEmpty(cardAndReward.getStatus())) {
					if ("5".equals(cardAndReward.getStatus())) {
						cardAndReward.setStatusName("成功订单");
					} else {
						cardAndReward.setStatusName("失败订单");
					}
				}
				if(map.containsKey(cardAndReward.getGivenChannel())){
					cardAndReward.setGivenChannelName(map.get(cardAndReward.getGivenChannel()));
				}
			}
	}
	
	private void changeReload(List<CardAndReward> list) {
		List<SysDict> selectByKey = sysDictService.selectByKey("SENDCHANNEL");
			Map<String,String> map = new HashMap<>();
			for (SysDict sysDict : selectByKey) {
				map.put(sysDict.getSysValue(), sysDict.getSysName());
			}
			for (CardAndReward cardAndReward : list) {
				if (StringUtils.isNotEmpty(cardAndReward.getStatus())) {
					if ("5".equals(cardAndReward.getStatus())) {
						cardAndReward.setStatusName("成功订单");
					} else {
						cardAndReward.setStatusName("失败订单");
					}
				}
				//	$scope.riskStatusaa=[{text:"全部",value:""},{text:"已赠送",value:1},{text:"赠送失败",value:2},{text:"未赠送",value:3}]
				if (cardAndReward.getGivenStatus()!=null) {
					if (1==cardAndReward.getGivenStatus()) {
						cardAndReward.setGivenStatusName("已赠送");
					} else if(2==cardAndReward.getGivenStatus()){
						cardAndReward.setGivenStatusName("赠送失败");
					} else if(3==cardAndReward.getGivenStatus()){
						cardAndReward.setGivenStatusName("未赠送");
					} else {
						cardAndReward.setGivenStatusName("");
					}
				}
				
				//$scope.givenTypeaa=[{text:"全部",value:""},{text:"鼓励金",value:1},{text:"积分",value:2},{text:"刷卡金",value:3}]

				if (StringUtils.isNotEmpty(cardAndReward.getGivenType())) {
					if ("1".equals(cardAndReward.getGivenType()+"")) {
						cardAndReward.setGivenType("鼓励金");
					} else if ("2".equals(cardAndReward.getGivenType()+"")){
						cardAndReward.setGivenType("积分");
					}else if ("3".equals(cardAndReward.getGivenType()+"")){
						cardAndReward.setGivenType("刷卡金");
					}else{
						cardAndReward.setGivenType("");
					}
				}
				//$scope.orderStatus=[{text:"信用卡办理",value:2},{text:"贷款",value:6}]
				if (cardAndReward.getOrderType()!=null) {
					if (2==cardAndReward.getOrderType()) {
						cardAndReward.setOrderTypeName("信用卡办理");
					} else if(6==cardAndReward.getOrderType()){
						cardAndReward.setOrderTypeName("贷款");
					} else{
						cardAndReward.setOrderTypeName("");
					}
				}
				if(map.containsKey(cardAndReward.getGivenChannel())){
					cardAndReward.setGivenChannelName(map.get(cardAndReward.getGivenChannel()));
				}
			}
	}
	
	
	@Override
	public void selectByKey(String type,Map map) {
		List<CouponActivityInfo> selectByKey = cardAndRewardManageDao.selectByKey(type);
		
		int id = 0;
		if ("12".equals(type)) {
			//199
			for (CouponActivityInfo couponActivityInfo : selectByKey) {
				if (couponActivityInfo.getCouponAmount().intValue()==199) {
					id=couponActivityInfo.getId();
					break;
				}
			}
		}else if("13".equals(type)){
			for (CouponActivityInfo couponActivityInfo : selectByKey) {
				if (couponActivityInfo.getCouponAmount().intValue()==99) {
					id=couponActivityInfo.getId();
					break;
				}
			}
		}
		
		if (id==0) {
			id = selectByKey.get(0).getId();
		}
		
		map.put("list", selectByKey);
		map.put("sendTypeId",id);
	}

	public Map<String, Object> allSend(List<String> ids, CardAndReward cardAndReward, String type) {
		Map<String, Object> map = new HashMap<>();
		// 单个赠送
		if (ids == null || ids.size() <= 0) {
			map.put("bols", false);
			map.put("msg", "无可赠送商户");
			return map;
		}

		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		for (String id : ids) {
			try {
			String sendId = cardAndReward.getSendId();
			String sendTypeId = cardAndReward.getSendTypeId();

			CardAndReward card = cardAndRewardManageDao.selectCardLoanHeartenById(id);
			CouponActivityEntity couponActivityEntity = cardAndRewardManageDao.selectCouponActivityInfoById(sendTypeId);

			card.setEffectiveDays(couponActivityEntity.getEffectiveDays());
			card.setTicketId(couponActivityEntity.getId());
			card.setCouponAmount(couponActivityEntity.getCouponAmount());
			card.setGivenChannel(cardAndReward.getSendId());

			String merchantNo = card.getMerchantNo();
			String couponCode = type;

			SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
			String couDate = sdf.format(new Date());
			String random = StringUtil.filterNull(Math.random());
			random = random.substring(random.indexOf(".") + 1, 10);
			couDate = couDate + random;

			String token = "";
			String currentDate = DateUtils.getMessageTextTime();

			if ("12".equals(type)) {
				token = currentDate + "BK" + couDate;
			} else {
				token = currentDate + "DK" + couDate;
			}
			Date couponStartDate = new Date();
			Date endDate = DateUtils.addDate(couponStartDate, couponActivityEntity.getEffectiveDays());
			Map<String, Object> couponMap = new HashMap<String, Object>();
			couponMap.put("face_value", couponActivityEntity.getCouponAmount());
			couponMap.put("balance", couponActivityEntity.getCouponAmount());
			couponMap.put("coupon_amount", new BigDecimal(0));
			couponMap.put("gift_amount", new BigDecimal(0));
			couponMap.put("coupon_code", couponCode);
			couponMap.put("cancel_verification_code", couponActivityEntity.getCancelVerificationCode());
			couponMap.put("coupon_status", "1"); // 1 未使用
			couponMap.put("start_time", couponStartDate);
			couponMap.put("end_time", endDate);
			couponMap.put("token", token);
			couponMap.put("merchant_no", merchantNo);
			couponMap.put("activity_first", couponActivityEntity.getActivityFirst());
			couponMap.put("activity_entity_id", couponActivityEntity.getId());
			couponMap.put("given_status_id", id);

			couponMap.put("coupon_type", couponActivityEntity.getCouponType());//走购买鼓励金逻辑
//			String couponNo = getNext("coupon_no_seq", "100000000000");

			String couponNo="B"+couponActivityService.getNext("coupon_no_seq").get("t").toString();
			couponMap.put("coupon_no", couponNo);
			
			card.setUpdateTime(couponStartDate);
			card.setSuccessTime(couponStartDate);
			card.setOperTime(endDate);
			card.setGivenChannel(sendId);
			card.setOperUsername(principal.getUsername());
			card.setGivenType("1");
			// 防并发
			int addUserCoupon = couponActivityService.addUserCouponReload(couponMap);
			if (addUserCoupon == 0) {
				continue;
			}
			if (addUserCoupon == 1) {
				card.setGivenStatus(1);
				// 赠送成功更改状态
				 cardAndRewardManageDao.updateCardLoanHearten(card);
			} else {
				card.setSuccessTime(null);
				card.setOperTime(null);
				card.setGivenStatus(2);
				 cardAndRewardManageDao.updateCardLoanHearten(card);
			}
			// 赠送日志
			    cardAndRewardManageDao.insertCardLoanHeartenLog(card);
			  
			    try {
			    	sendJG(merchantNo,type);

					sensorsService.buyCouponDetail(couponMap,couponNo,merchantNo,couponStartDate,endDate,couponCode);
				} catch (Exception e) {
					log.error("赠送下发出现异常",e);
				}
			} catch (Exception e) {
				log.error("单个赠送出现异常",e);
			}
		}
		map.put("bols", true);
		map.put("msg", "赠送成功");
		return map;
	}

	@Override
	public void sendJG(String merchantNo,String type){
		 String authCode = Md5.md5Str(USER_INFO_ID + merchantNo + Token).toUpperCase();
	        String url = sysDictService.getByKey("CORE_URL").getSysValue() + PATH;
	        Map<String, String> requestMap = new HashMap<>();
	        requestMap.put("token", Token);
	        requestMap.put("merchantNo", merchantNo);
	        requestMap.put("authCode", authCode);
	        
	        if ("12".equals(type)) {
	        	 requestMap.put("tittle", "办卡送鼓励金");
	        }else {
	        	 requestMap.put("tittle", "贷款送鼓励金");
	        }
	        CouponActivityInfo queryRewardDetail = couponActivityService.queryRewardDetail(Integer.valueOf(type));
	        requestMap.put("content", queryRewardDetail.getActivityNotice());
	        String str = HttpUtils.sendPostRequest(url, requestMap);
	        log.info("办卡(贷款)赠送鼓励金" + merchantNo + "的结果：" + str+",content："+queryRewardDetail.getActivityNotice());
	}
	
	
	private synchronized String getNext(String seqName, String defValue) {

		Map<String, Object> map = couponActivityService.getNext(seqName);

		BigInteger v = new BigInteger(defValue);
		Object t = map.get("t");
		if (null != t) {
			String src = t.toString();
			v = new BigInteger(src);
			v = v.add(new BigInteger("1"));
		}
		return v.toString();
	}
	@Override
	public List<CardAndReward> exportUserList(CardAndReward bean) {
		List<CardAndReward> list = cardAndRewardManageDao.exportUserList(bean);
		changeReload(list);
		return list;
	}

	@Override
	public CardAndReward getCardLoanHearten(String merchantNo, String orderNo) {
		return cardAndRewardManageDao.getCardLoanHearten(merchantNo,orderNo);
	}

	@Override
	public CouponActivityEntity selectCouponActivityInfoById(String sendTypeId) {
		return cardAndRewardManageDao.selectCouponActivityInfoById(sendTypeId);
	}

	@Override
	public int updateCardLoanHearten(CardAndReward card) {
		return cardAndRewardManageDao.updateCardLoanHearten(card);
	}

	@Override
	public int insertCardLoanHeartenLog(CardAndReward card) {
		return cardAndRewardManageDao.insertCardLoanHeartenLog(card);
	}

}
