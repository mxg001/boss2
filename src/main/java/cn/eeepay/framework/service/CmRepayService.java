package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmBillInfo;

public interface CmRepayService {

	/**
	 * 还款查询
	 * @author	mays
	 * @date	2018年5月24日
	 */
	List<CmBillInfo> selectRepayInfo(Page<CmBillInfo> page, CmBillInfo info);

	List<CmBillInfo> exportRepayInfo(CmBillInfo info);

	/**
	 * 根据id查询还款信息
	 * @author	mays
	 * @date	2018年5月24日
	 */
	CmBillInfo selectRepayInfoById(String id,int sta);

}
