package cn.eeepay.framework.service.impl.capitalInsurance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.capitalInsurance.BillEneryDao;
import cn.eeepay.framework.dao.capitalInsurance.CheckBillDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.BillEntry;
import cn.eeepay.framework.model.capitalInsurance.CheckBill;
import cn.eeepay.framework.model.capitalInsurance.TransOrder;
import cn.eeepay.framework.service.capitalInsurance.CheckBillService;

@Service("checkBillService")
@Transactional
public class CheckBillServiceImpl implements CheckBillService{
	private static final Logger log = LoggerFactory.getLogger(CheckBillServiceImpl.class);
	@Resource
	private CheckBillDao checkBillDao;
	@Resource
	private BillEneryDao billEneryDao;
	
	@Override
	public List<CheckBill> queryAllInfo(CheckBill tis, Page<CheckBill> page) {
		return checkBillDao.queryAllInfo(tis,page);
	}
	@Override
	public Integer cleanCheckBill(String batchNo) {
		Integer count =  checkBillDao.delByBatchNo(batchNo);
		if(count > 0){
			log.info("对账清除成功，batchNo：{}",batchNo);
			int eCount = billEneryDao.delByBatchNo(batchNo);
			log.info("对账明细清除成功,batchNo：{},count:{}",batchNo,eCount);
			return 1;
		}else{
			log.info("对账清除失败");
			return 0;
		}
	}
	@Override
	public LinkedList<String>  getTransOrderSuccess(String bxUnit,String startDate, String endDate) {
		return checkBillDao.getTransOrderSuccess(bxUnit,startDate,endDate);
	}
	@Override
	public LinkedList<String>  getTransOrderOver(String bxUnit,String startDate, String endDate) {
		return checkBillDao.getTransOrderOver(bxUnit,startDate,endDate);
	}
	@Override
	public Map<String,Object> chechBatchTransOrder(Map<String, BillEntry> batcList,String startDate,String endDate,Integer orderType) {
		Set<String>keys = batcList.keySet();
		List<TransOrder> orderList = checkBillDao.getBatchTransOrder(keys, startDate, endDate,orderType);
		List<BillEntry> newEntry = new ArrayList<>();
		BigDecimal acq_total_amount = new BigDecimal(0);
		BigDecimal sys_total_amount = new BigDecimal(0);
		int acq_success_count = 0;
		int acq_fail_count = 0;
		int sys_success_count = 0;
		int sys_fail_count = 0;
		for (TransOrder order : orderList) {
			BillEntry entry = batcList.get(order.getOrderNo());
			entry.setSysOrderNo(order.getOrderNo());
			entry.setHolder(order.getHolder());
			entry.setSysAmount(order.getSysAmount());
			entry.setBillAmount(order.getBillAmount());
			entry.setSysBillStatus(order.getSysBillStatus());
			entry.setBillTime(order.getBillTime());
			entry.setInsureTime(order.getBillTime());
			entry.setEffectiveStime(order.getEffectiveStime());
			entry.setEffectiveEtime(order.getEffectiveEtime());
			entry.setOneAgentNo(order.getOneAgentNo());
			entry.setProductNo(order.getProductNo());
			entry.setAcqBillNo(order.getAcqBillNo());
			entry.setSysBillNo(order.getAcqBillNo());
			entry.setInsureStatus(order.getSysBillStatus());
			if(orderType == 1){
				//投保订单交易状态为成功
				entry.setTransStatus("SUCCESS");
			}else{
				entry.setTransStatus("FAILED");
			}
			//保费、保额相同
			if ("yilian".equals(order.getInsurer())) {
				if(entry.getAcqAmount().compareTo(order.getAcqAmount())==0
						&& entry.getInsureAmount().compareTo(order.getInsureAmount())==0){
					entry.setCheckStatus(1);
					acq_total_amount = acq_total_amount.add(entry.getAcqAmount());
					sys_total_amount = sys_total_amount.add(entry.getSysAmount());
					acq_success_count++;
					sys_success_count++;
				}else{
					entry.setCheckStatus(4);
					acq_total_amount = acq_total_amount.add(entry.getAcqAmount());
					sys_total_amount = sys_total_amount.add(entry.getSysAmount());
					acq_fail_count++;
					sys_fail_count++;
				}
			}else if ("zhlh".equals(order.getInsurer())) {
				//中华联合-对账时，对账文件导入取消对保额的校验
				if(entry.getAcqAmount().compareTo(order.getAcqAmount())==0){
					entry.setCheckStatus(1);
					acq_total_amount = acq_total_amount.add(entry.getAcqAmount());
					sys_total_amount = sys_total_amount.add(entry.getSysAmount());
					acq_success_count++;
					sys_success_count++;
				}else{
					entry.setCheckStatus(4);
					acq_total_amount = acq_total_amount.add(entry.getAcqAmount());
					sys_total_amount = sys_total_amount.add(entry.getSysAmount());
					acq_fail_count++;
					sys_fail_count++;
				}
			}

			newEntry.add(entry);
			batcList.remove(order.getOrderNo());//去除已经对账完成数据
		}
		/**
		 * 上游单边数据  start
		 */
		if(batcList.size() > 0){
			Set<String>surplusKeys = batcList.keySet();
			for (String surplusKey : surplusKeys) {
				BillEntry surplusEnery =  batcList.get(surplusKey);
				surplusEnery.setSysOrderNo("");
				surplusEnery.setSysAmount(new BigDecimal(0));
				surplusEnery.setBillAmount(new BigDecimal(0));
				surplusEnery.setSysBillStatus("未知");
				surplusEnery.setOneAgentNo("");
				surplusEnery.setProductNo("");
				surplusEnery.setCheckStatus(2);
				surplusEnery.setInsureStatus("INIT");
				surplusEnery.setSysBillNo(surplusEnery.getAcqBillNo());
				newEntry.add(surplusEnery);
				acq_total_amount = acq_total_amount.add(surplusEnery.getAcqAmount());
				acq_fail_count++;
			}
		}
		/****end****/
		//批量记录对账明细
		checkBillDao.insertBatchEnery(newEntry);
		//返回当前批次统计结果
		Map<String,Object> returnMap = new HashMap<>();
		returnMap.put("acq_total_amount", acq_total_amount);
		returnMap.put("sys_total_amount", sys_total_amount);
		returnMap.put("acq_success_count", acq_success_count);
		returnMap.put("acq_fail_count", acq_fail_count);
		returnMap.put("sys_success_count", sys_success_count);
		returnMap.put("sys_fail_count", sys_fail_count);
		return returnMap;
	}
	
	@Override
	public Map<String, Object> chechSysTransOrder(LinkedList<String> orderNoList,CheckBill bill,Integer orderType) {
		Set<String> keys = new HashSet<>();
		for (String orderNo : orderNoList) {
			keys.add(orderNo);
		}
		List<TransOrder> orderList = checkBillDao.getBatchTransOrder(keys,null,null,orderType);
		BigDecimal acq_total_amount = new BigDecimal(0);
		BigDecimal sys_total_amount = new BigDecimal(0);
		int acq_success_count = 0;
		int acq_fail_count = 0;
		int sys_success_count = 0;
		int sys_fail_count = 0;
		List<BillEntry> newEntry = new ArrayList<>();

		for (TransOrder order : orderList) {
			BillEntry entry = new BillEntry();
			entry.setBatchNo(bill.getBatchNo());
			entry.setInsurer(bill.getInsurer());
			entry.setOrderType(bill.getOrderType());
			entry.setSysOrderNo(order.getOrderNo());
			entry.setAcqOrderNo(order.getOrderNo());
			entry.setHolder(order.getHolder());
			entry.setInsureAmount(order.getInsureAmount());
			entry.setAcqAmount(order.getAcqAmount());
			entry.setSysAmount(order.getSysAmount());
			entry.setBillAmount(order.getBillAmount());
			entry.setSysBillStatus(order.getSysBillStatus());
			entry.setBillTime(order.getBillTime());
			entry.setEffectiveStime(order.getEffectiveStime());
			entry.setEffectiveEtime(order.getEffectiveEtime());
			entry.setOneAgentNo(order.getOneAgentNo());
			entry.setProductNo(order.getProductNo());
			entry.setAcqBillNo(order.getAcqBillNo());
			entry.setInsureTime(order.getBillTime());
			entry.setSysBillNo(order.getAcqBillNo());
			entry.setCheckStatus(3);
			entry.setInsureStatus(order.getSysBillStatus());
			entry.setCreatePerson(bill.getCreatePerson());
			sys_total_amount = sys_total_amount.add(order.getSysAmount());
			sys_fail_count++;
			newEntry.add(entry);
		}
		checkBillDao.insertBatchEnery(newEntry);
		//返回当前批次统计结果
		Map<String,Object> returnMap = new HashMap<>();
		returnMap.put("acq_total_amount", acq_total_amount);
		returnMap.put("sys_total_amount", sys_total_amount);
		returnMap.put("acq_success_count", acq_success_count);
		returnMap.put("acq_fail_count", acq_fail_count);
		returnMap.put("sys_success_count", sys_success_count);
		returnMap.put("sys_fail_count", sys_fail_count);
		return returnMap;
	}
	
	@Override
	public Integer insertBill(CheckBill bill) {
		return checkBillDao.insertBill(bill);
	}

}
