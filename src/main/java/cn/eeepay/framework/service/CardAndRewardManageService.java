package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CardAndReward;
import cn.eeepay.framework.model.CouponActivityEntity;

public interface CardAndRewardManageService {

	List<CardAndReward> selectUserInfo(Page<CardAndReward> page, CardAndReward info);

	void selectByKey(String type,Map map);

	Map<String, Object> allSend(List<String> ids, CardAndReward cardAndReward,String type);

	List<CardAndReward> selectCardLoanHeartenLogById(String id);
	List<CardAndReward> exportCardLoanHeartenLogById(String id);

	List<CardAndReward> exportUserList(CardAndReward bean);

	CardAndReward getCardLoanHearten(String merchantNo, String orderNo);

	CouponActivityEntity selectCouponActivityInfoById(String sendTypeId);

	int updateCardLoanHearten(CardAndReward card);

	int insertCardLoanHeartenLog(CardAndReward card);

	void sendJG(String merchantNo, String type);
}
