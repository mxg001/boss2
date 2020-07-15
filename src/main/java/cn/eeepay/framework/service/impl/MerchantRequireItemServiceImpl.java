package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.MerchantRequireItemDao;
import cn.eeepay.framework.model.IndustryMcc;
import cn.eeepay.framework.model.MerchantRequireItem;
import cn.eeepay.framework.service.MerchantRequireItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("merchantRequireItemService")
@Transactional
public class MerchantRequireItemServiceImpl implements MerchantRequireItemService {

	@Resource
	private MerchantRequireItemDao merchantRequireItemDao;
	
	@Override
	public int updateBymriId(Long id, String status) {
		return merchantRequireItemDao.updateBymriId(id, status);
	}

	@Override
	public int updateByMbpId(MerchantRequireItem record) {
		return merchantRequireItemDao.updateByMbpId(record);
	}

	@Override
	public MerchantRequireItem selectByMriId(String mriId,String merId) {
		return merchantRequireItemDao.selectByMriId(mriId,merId);
	}

	@Override
	public MerchantRequireItem selectByAccountNo(String merId) {
		return merchantRequireItemDao.selectByAccountNo(merId);
	}

	@Override
	public MerchantRequireItem selectByIdCardNo(String merId) {
		return merchantRequireItemDao.selectByIdCardNo(merId);
	}

	@Override
	public List<MerchantRequireItem> getByMer(String merchantNo) {
		return merchantRequireItemDao.getByMer(merchantNo);
	}

	@Override
	public List<MerchantRequireItem> getItemByMerId(String merchantNo) {
		return merchantRequireItemDao.getItemByMerId(merchantNo);
	}

	@Override
	public List<MerchantRequireItem> selectItemByBpIdAndMerNo(String merchantNo,String bpId) {
		return merchantRequireItemDao.selectItemByBpIdAndMerNo(merchantNo,bpId);
	}

	@Override
	public IndustryMcc selectIndustryMccByMcc(String mcc) {
		return merchantRequireItemDao.selectIndustryMccByMcc(mcc);
	}

	@Override
	public List<IndustryMcc> selectIndustryMccByLevel(String industryLevel) {
		return merchantRequireItemDao.selectIndustryMccByLevel(industryLevel);
	}

	@Override
	public List<IndustryMcc> selectIndustryMccByParentId(String parentId) {
		return merchantRequireItemDao.selectIndustryMccByParentId(parentId);
	}

	@Override
	public int updateMccById(String id, String mcc) {
		return merchantRequireItemDao.updateMccById(id,mcc);
	}

	@Override
	public int updateMccByMerNo(String merNo, String mcc) {
		return merchantRequireItemDao.updateMccByMerNo(merNo,mcc);
	}

	@Override
	public List<MerchantRequireItem> getMerchantRequireItemList(String merchantNo, String bpId) {
		return merchantRequireItemDao.getMerchantRequireItemList(merchantNo,bpId);
	}
}
