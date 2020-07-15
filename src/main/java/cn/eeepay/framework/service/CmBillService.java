package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmBillDetail;
import cn.eeepay.framework.model.CmBillInfo;

import java.util.List;
import java.util.Map;

public interface CmBillService {

	/**
	 * 账单列表查询
	 * @author	mays
	 * @date	2018年4月9日
	 */
	List<CmBillInfo> selectBillInfo(Page<CmBillInfo> page, CmBillInfo info);

	List<CmBillInfo> exportBillInfo(CmBillInfo info);

	/**
	 * 账单明细查询
	 * @author	mays
	 * @date	2018年4月9日
	 */
	List<CmBillDetail> selectBillDetail(Page<CmBillDetail> page, CmBillDetail info);

	List<CmBillDetail> exportBillDetailInfo(CmBillDetail info);

	/**
	 * 查询评测概况
	 * @author	mays
	 * @param msg 
	 * @date	2018年4月24日
	 */
	void queryReviewsReport(Map<String, Object> msg, String billId);

}
