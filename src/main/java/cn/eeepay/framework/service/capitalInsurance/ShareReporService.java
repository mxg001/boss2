package cn.eeepay.framework.service.capitalInsurance;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.ShareReport;
import cn.eeepay.framework.model.capitalInsurance.ShareReportTotal;

import javax.servlet.http.HttpServletResponse;

public interface ShareReporService {
	/**
	 * 分润月表列表 分页
	 * @param tis
	 * @param page
	 * @return
	 */
	List<ShareReport>  queryAllInfo(ShareReport tis, Page<ShareReport> page);

	List<ShareReport> importDetailSelect(ShareReport tis);

	/**
	 * 分润列表汇总金额
	 * @param cto
	 * @return
	 */
	ShareReportTotal queryAmount(ShareReport cto);
	/**
	 * 是否已汇总
	 * @param reportMonth
	 * @return
	 */
	Integer isInitShareReport(String reportMonth);
	/**
	 * 汇总分润
	 * @param report
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	Integer initShareReport(ShareReport report, String sDate, String eDate);
	/**
	 * 分润入账
	 * @param id
	 */
	int insuranceProfitRecordAccount(Integer id);

	void importDetail(List<ShareReport> list, HttpServletResponse response) throws Exception;
}
