package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import cn.eeepay.framework.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.daoCreditMgr.CmRepayDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmBillInfo;
import cn.eeepay.framework.service.CmRepayService;

@Service("cmRepayService")
@Transactional
public class CmRepayServiceImpl implements CmRepayService{

	@Resource
	private CmRepayDao cmRepayDao;

	public List<CmBillInfo> selectRepayInfo(Page<CmBillInfo> page, CmBillInfo info) {
		List<CmBillInfo> list=cmRepayDao.selectRepayInfo(page, info);
		dataProcessingList(page.getResult());
		return list;
	}

	public List<CmBillInfo> exportRepayInfo(CmBillInfo info) {
		List<CmBillInfo>list=cmRepayDao.exportRepayInfo(info);
		dataProcessingList(list);
		return list;
	}

	/**
	 * 数据处理List
	 */
	private void dataProcessingList(List<CmBillInfo> list){
		if(list!=null&&list.size()>0){
			for(CmBillInfo item:list){
				if(item!=null){
					item.setMobileNo(StringUtil.sensitiveInformationHandle(item.getMobileNo(),0));
				}
			}
		}
	}

	public CmBillInfo selectRepayInfoById(String type,int sta) {
		CmBillInfo info=cmRepayDao.selectRepayInfoById(type);
		if(1==sta){
			dataProcessingItem(info);
		}

		return info;
	}

	/**
	 * 数据处理item
	 */
	private void dataProcessingItem(CmBillInfo item){
		if(item!=null){
			item.setMobileNo(StringUtil.sensitiveInformationHandle(item.getMobileNo(),0));
		}
	}
}
