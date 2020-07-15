package cn.eeepay.framework.service.capitalInsurance;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.BillEntry;
import cn.eeepay.framework.model.capitalInsurance.CheckBill;

public interface CheckBillService {
	/**
	 * 对账列表，分页
	 * @param tis
	 * @param page
	 * @return
	 */
	List<CheckBill>  queryAllInfo(CheckBill tis, Page<CheckBill> page);
	/**
	 * 清除对账数据
	 * @param batchNo 批次号
	 * @return
	 */
	Integer cleanCheckBill(String batchNo);
	/**
	 * 时间段内保险交易单号
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	LinkedList<String> getTransOrderSuccess(String bxUnit,String startDate, String endDate);
	/**
	 * 批量核对
	 * @param batcList
	 * @return
	 */
	Map<String,Object>  chechBatchTransOrder(Map<String, BillEntry> batcList,String startDate, String endDate, Integer orderType);
	/**
	 * 
	 * @param bill
	 * @return
	 */
	Integer insertBill(CheckBill bill);
	/**
	 * 系统单边数据核对
	 * @param orderNoList
	 * @param orderType 
	 * @return
	 */
	Map<String, Object> chechSysTransOrder(LinkedList<String> orderNoList,CheckBill bill, Integer orderType);
	/**
	 * 退保订单集合
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	LinkedList<String> getTransOrderOver(String bxUnit,String startDate, String endDate);

}
