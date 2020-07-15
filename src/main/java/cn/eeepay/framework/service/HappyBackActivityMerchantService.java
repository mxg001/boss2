package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.HappyBackActivityMerchant;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author tgh
 * @description 欢乐返活跃商户活动查询
 * @date 2019/8/20
 */
public interface HappyBackActivityMerchantService {

	/**
	 * 欢乐返活跃商户活动查询列表
	 * @param page
	 * @param happyBackActivityMerchant
	 */
	void selectHappyBackActivityMerchant(Page<HappyBackActivityMerchant> page,
										 HappyBackActivityMerchant happyBackActivityMerchant);

	/**
	 * 统计奖励金额和扣款金额
	 * @param happyBackActivityMerchant
	 * @return
	 */
	Map<String,Object> countMoney(HappyBackActivityMerchant happyBackActivityMerchant);

	/**
	 * 导出欢乐返活跃商户活动列表
	 * @param happyBackActivityMerchant
	 * @param response
	 */
	void exportExcel(HappyBackActivityMerchant happyBackActivityMerchant, HttpServletResponse response) throws Exception;

}
