package cn.eeepay.framework.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.MerchantInfoDao;
import cn.eeepay.framework.dao.MerchantPreFreezeLogDao;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.TransInfoPreFreezeLog;
import cn.eeepay.framework.service.MerchantPreFreezeLogService;


@Service("merchantFreezeNewLogService")
@Transactional
public class MerchantPreFreezeLogServiceImpl implements MerchantPreFreezeLogService{
	
	private static final Logger log = LoggerFactory.getLogger(MerchantPreFreezeLogServiceImpl.class);
	@Resource
	private MerchantPreFreezeLogDao merchantPreFreezeLogDao;
	@Resource
	private MerchantInfoDao merchantInfoDao;
	
	@Override
	public int insertPreFreezeLog(TransInfoPreFreezeLog record) {
		return merchantPreFreezeLogDao.insertPreFreezeLog(record);
	}

	@Override
	public void insertLogAndUpdateMerchantInfoAmount(TransInfoPreFreezeLog record) {
		MerchantInfo merchantInfo = null;
		try {
			// 获得该商户之前的预冻结金额				
			BigDecimal originalPreFrozenMoney = BigDecimal.ZERO;
			merchantInfo = merchantInfoDao.selectByMerchantNo(record.getMerchantNo());
			originalPreFrozenMoney = originalPreFrozenMoney.add(merchantInfo.getPreFrozenAmount());
			record.setOperLog("预冻结金额从" + originalPreFrozenMoney + "更改为" + record.getPreFreezeAmount());
			// 插入预冻结金额操作日志
			merchantPreFreezeLogDao.insertPreFreezeLog(record);
			// 更新商户表中的预冻结金额
			merchantInfoDao.updatePreFrozenAmountByMerId(record.getPreFreezeAmount(), record.getMerchantNo());
		} catch (Exception e) {
			log.info("修改预冻结金额异常" + e);
			throw new RuntimeException("修改预冻结金额失败");
		}
	}
	
	@Override
	public List<TransInfoPreFreezeLog> selectByMerchantNo(String merchantNo) {
		return merchantPreFreezeLogDao.selectByMerchantNo(merchantNo);
	}
	
}
