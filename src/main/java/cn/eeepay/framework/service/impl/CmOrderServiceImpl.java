package cn.eeepay.framework.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.daoCreditMgr.CmOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmPayment;
import cn.eeepay.framework.service.CmOrderService;

@Service("cmOrderService")
@Transactional
public class CmOrderServiceImpl implements CmOrderService{

	@Resource
	private CmOrderDao cmOrderDao;

	public List<CmPayment> selectOrderInfo(Page<CmPayment> page, CmPayment info) {
		List<CmPayment> list = cmOrderDao.selectOrderInfo(page, info);
		for (CmPayment i : list) {
			if (i.getTransAmount() != null) {
				i.setTransAmount(i.getTransAmount().divide(new BigDecimal(100)));
			}
		}
		return list;
	}

	public List<CmPayment> exportOrderInfo(CmPayment info) {
		List<CmPayment> list = cmOrderDao.exportOrderInfo(info);
		for (CmPayment i : list) {
			if (i.getTransAmount() != null) {
				i.setTransAmount(i.getTransAmount().divide(new BigDecimal(100)));
			}
		}
		return list;
	}
	
	public String sumOrderInfo(CmPayment info) {
		return cmOrderDao.sumOrderInfo(info);
	}

	public CmPayment queryOrderInfoById(String tradeNo) {
		CmPayment info = cmOrderDao.queryOrderInfoById(tradeNo);
		info.setTransAmount(info.getTransAmount().divide(new BigDecimal(100)));
		return info;
	}

}
