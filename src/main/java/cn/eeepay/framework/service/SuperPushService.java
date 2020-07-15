package cn.eeepay.framework.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.SettleOrderInfo;
import cn.eeepay.framework.model.SuperPushShare;
import cn.eeepay.framework.model.SuperPushUser;

/** 
 * @author tans 
 * @version ：2017年5月10日 上午11:29:04 
 * 
 */
public interface SuperPushService {

	List<SuperPushUser> getByParam(SuperPushUser baseInfo, Page<SuperPushUser> page);

	MerchantInfo getSuperPushMerchantDetail(String merchantNo);

	List<SuperPushShare> getSuperPushShareDetail(SuperPushShare baseInfo, Page<SuperPushShare> page);

	void exportExcel(Page<SuperPushShare> page, SuperPushShare baseInfo, HttpServletResponse response) throws IOException;

	SuperPushUser getCashMerchantDetail(String merchantNo);

	BigDecimal getTotalAmount(String merchantNo);

	BigDecimal getSuperPushUserBalance(String merchantNo);

	List<SettleOrderInfo> getCashPage(SettleOrderInfo settleOrderInfo, Page<SuperPushUser> page);

	List<SuperPushShare> getShareByParam(SuperPushShare baseInfo, Page<SuperPushShare> page);

	Map<String, Object> getTotalShareByParam(SuperPushShare baseInfo);

	Map<String, Object> getTotalTransAmountByParam(SuperPushShare baseInfo);

	void exportShare(SuperPushShare baseInfo, HttpServletResponse response) throws IOException;

}
