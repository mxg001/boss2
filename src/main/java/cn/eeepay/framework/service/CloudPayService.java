package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CloudPayInfo;

import java.util.List;

/**
 * 云闪付
 * @author mays
 * @date 2017年10月23日
 */
public interface CloudPayService {

	/**
	 * 商户收益查询
	 * @author mays
	 * @date 2017年10月23日
	 */
	List<CloudPayInfo> selectCloudPayByParam(Page<CloudPayInfo> page, CloudPayInfo info);

	/**
	 * 收益金额统计
	 * @param info
	 * @return
	 */
	String selectMerchantProfitCount(CloudPayInfo info);

	/**
	 * 导出商户收益
	 * @author mays
	 * @date 2017年10月23日
	 */
	List<CloudPayInfo> importCloudPayByParam(CloudPayInfo info);

}
