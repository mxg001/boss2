package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;

/**
 * 超级还款用户管理
 * @author mays
 * @date 2017年10月31日
 */
public interface RepayMerchantService {

	/**
	 * 用户查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	List<RepayMerchantInfo> selectRepayMerchantByParam(Page<RepayMerchantInfo> page, RepayMerchantInfo info);

	/**
	 * 用户基本资料查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	RepayMerchantInfo queryRepayMerchantByMerchantNo(String merchantNo);

	/**
	 * 用户绑定的贷记卡和借记卡查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	List<YfbCardManage> queryCardByMerchantNo(String merchantNo);

	/**
	 * 用户开户
	 * @author mays
	 * @date 2017年10月31日
	 */
	int updateRepayMerchantAccountStatus(String merchantNo);

	/**
	 * 查询账户余额
	 * @author	mays
	 * @date	2017年12月7日
	 */
	List<YfbBalance> queryBalanceByMerchantNo(String merchantNo);

	/**
	 * 查询通道同步
	 * @author	rpc
	 * @date	2018年04月20日
	 */
	List<YfbChannelSyn> queryChannelSynByMerchantNo(String merchantNo);

	/**
	 * 查询通道同步日志记录
	 * @author	rpc
	 * @date	2018年04月20日
	 */
	List<YfbChannelSynLog> queryChannelSynLogByMerchantNo(String merchantNo);


	RepayMerchantInfo selectByMerchantNo(String merchantNo);

	/**
	 * 修改商户状态
	 * @param merchantNo
	 * @param status
	 */
    int updateRepayMerchantStatus(String merchantNo, String status);

	/**
	 * 获取绑卡记录
	 * @param cardNo
	 * @return
	 */
	List<YfbBindCardRecord> queryBindCardRecord(String cardNo);

	/**
	 *获取敏感数据
	 */
	RepayMerchantInfo getDataProcessing(String merchantNo);
}
