package cn.eeepay.framework.service.capitalInsurance;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.BillEntry;
import cn.eeepay.framework.model.capitalInsurance.ShareReport;

public interface BillEneryService {
	/**
	 * 明细列表，分页
	 * @param tis
	 * @param page
	 * @return
	 */
	List<BillEntry> queryAllInfo(BillEntry tis, Page<BillEntry> page);
	
	/**
	 * 明细详情
	 * @param id
	 * @return
	 */
	BillEntry getEneryDetail(String id);
	/**
	 * 对账明细导出查询
	 * @param tis
	 * @return
	 */
	List<BillEntry> importAllInfo(BillEntry tis);
	/**
	 * 时间段内对账明细
	 * @param sDate 保单生成开始日期
	 * @param eDate 保单生成结束日期
	 * @return
	 */
	List<ShareReport> getInitReport(String sDate,String eDate);
	/**
	 * 批次下已汇总数
	 * @param batchNo 批次号
	 * @return
	 */
	int reportNumByBatchNo(String batchNo);
	/**
	 * 是否已经上传
	 * @param orderType
	 * @param insurer
	 * @param startDate
	 * @param endtDate
	 * @return
	 */
	int isUploadFile(String orderType, String insurer, String startDate, String endtDate);

}
