package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoCreditMgr.CmBillDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmBillDetail;
import cn.eeepay.framework.model.CmBillInfo;
import cn.eeepay.framework.service.CmBillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("cmBillService")
@Transactional
public class CmBillServiceImpl implements CmBillService {

	@Resource
	private CmBillDao cmBillDao;

	/**
	 * 账单列表查询
	 */
	public List<CmBillInfo> selectBillInfo(Page<CmBillInfo> page, CmBillInfo info) {
		List<CmBillInfo> list = cmBillDao.selectBillInfo(page, info);
		for (CmBillInfo i : list) {
			if (i.getRepayment() != null) {
				i.setRepayment(i.getRepayment().divide(new BigDecimal(100)));
			}
			if (i.getLowestRepayment() != null) {
				i.setLowestRepayment(i.getLowestRepayment().divide(new BigDecimal(100)));
			}
		}
		return list;
	}

	public List<CmBillInfo> exportBillInfo(CmBillInfo info) {
		List<CmBillInfo> list = cmBillDao.exportBillInfo(info);
		for (CmBillInfo i : list) {
			if (i.getRepayment() != null) {
				i.setRepayment(i.getRepayment().divide(new BigDecimal(100)));
			}
			if (i.getLowestRepayment() != null) {
				i.setLowestRepayment(i.getLowestRepayment().divide(new BigDecimal(100)));
			}
		}
		return list;
	}

	/**
	 * 账单明细查询
	 */
	public List<CmBillDetail> selectBillDetail(Page<CmBillDetail> page, CmBillDetail info) {
		cmBillDao.selectBillDetail(page, info);
		List<CmBillDetail> list = page.getResult();
		for (CmBillDetail i : list) {
			if (i.getTransAmt() != null) {
				i.setTransAmt(i.getTransAmt().divide(new BigDecimal(100)));
			}
			if (i.getTallyAmt() != null) {
				i.setTallyAmt(i.getTallyAmt().divide(new BigDecimal(100)));
			}
		}
		return list;
	}

	public List<CmBillDetail> exportBillDetailInfo(CmBillDetail info) {
		List<CmBillDetail> list=cmBillDao.exportBillDetailInfo(info);
		for (CmBillDetail i : list) {
			if (i.getTransAmt() != null) {
				i.setTransAmt(i.getTransAmt().divide(new BigDecimal(100)));
			}
			if (i.getTallyAmt() != null) {
				i.setTallyAmt(i.getTallyAmt().divide(new BigDecimal(100)));
			}
		}
		return list;
	}

	/**
	 * 查询评测概况
	 */
	public void queryReviewsReport(Map<String, Object> msg, String billId) {
		List<Map<String, Integer>> list = cmBillDao.queryReviewsReport(billId);
		Map<String, String> reviewsReport = new HashMap<>();
		for (Map<String, Integer> m : list) {
			if (10100 == m.get("reviews_id")) {
				reviewsReport.put("ykjkd", m.get("review_result") + "分");
			}
			if (10200 == m.get("reviews_id")) {
				reviewsReport.put("tezs", m.get("review_result") + "%");
			}
			if (1003 == m.get("reviews_id")) {
				reviewsReport.put("ykaqzs", m.get("review_result") + "%");
			}
			if (1004 == m.get("reviews_id")) {
				reviewsReport.put("ewjf", m.get("review_result") + "分");
			}
		}
		reviewsReport.put("transCount", cmBillDao.queryTransCount(billId) + "笔");
		reviewsReport.put("merCount", cmBillDao.queryMerCount(billId) + "个");
		msg.put("reviewsReport", reviewsReport);
		msg.put("amountCount", cmBillDao.queryTransPartCount(billId));
		msg.put("timeCount", cmBillDao.queryTimePartCount(billId));
		msg.put("status", true);
	}

}
