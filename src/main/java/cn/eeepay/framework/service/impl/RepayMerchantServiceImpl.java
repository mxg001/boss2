package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import cn.eeepay.framework.model.*;
import cn.eeepay.framework.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.RepayMerchantDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.RepayMerchantService;

/**
 * 超级还款用户管理
 * @author mays
 * @date 2017年10月31日
 */
@Service("repayMerchantService")
@Transactional
public class RepayMerchantServiceImpl implements RepayMerchantService {

	@Resource
	private RepayMerchantDao repayMerchantDao;

	@Override
	public RepayMerchantInfo selectByMerchantNo(String merchantNo) {
		return repayMerchantDao.selectByMerchantNo(merchantNo);

	}

	@Override
	public int updateRepayMerchantStatus(String merchantNo, String status) {
		return repayMerchantDao.updateRepayMerchantStatus(status, merchantNo);
	}

	/**
	 * 用户查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	public List<RepayMerchantInfo> selectRepayMerchantByParam(Page<RepayMerchantInfo> page,
			RepayMerchantInfo info) {
		List<RepayMerchantInfo> list=repayMerchantDao.selectRepayMerchantByParam(page, info);
		dataProcessingList(page.getResult());
		return list;
	}

	/**
	 * 数据处理List
	 */
	private void dataProcessingList(List<RepayMerchantInfo> list){
		if(list!=null&&list.size()>0){
			for(RepayMerchantInfo item:list){
				if(item!=null){
					item.setMobileNo(StringUtil.sensitiveInformationHandle(item.getMobileNo(),0));
				}
			}
		}
	}

	/**
	 * 数据处理item
	 */
	private void dataProcessingItem(RepayMerchantInfo item){
		if(item!=null){
			item.setMobileNo(StringUtil.sensitiveInformationHandle(item.getMobileNo(),0));
			item.setIdCardNo(StringUtil.sensitiveInformationHandle(item.getIdCardNo(),1));
		}
	}
	/**
	 * 用户基本资料查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	public RepayMerchantInfo queryRepayMerchantByMerchantNo(String merchantNo) {
		RepayMerchantInfo info=repayMerchantDao.queryRepayMerchantByMerchantNo(merchantNo);
		dataProcessingItem(info);
		return info;
	}

	/**
	 * 用户绑定的贷记卡和借记卡查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	public List<YfbCardManage> queryCardByMerchantNo(String merchantNo) {
		return repayMerchantDao.queryCardByMerchantNo(merchantNo);
	}

	/**
	 * 用户开户
	 * @author mays
	 * @date 2017年10月31日
	 */
	public int updateRepayMerchantAccountStatus(String merchantNo) {
		return repayMerchantDao.updateRepayMerchantAccountStatus(merchantNo);
	}

	/**
	 * 查询账户余额
	 * @author	mays
	 * @date	2017年12月7日
	 */
	public List<YfbBalance> queryBalanceByMerchantNo(String merchantNo) {
		return repayMerchantDao.queryBalanceByMerchantNo(merchantNo);
	}

	/**
	 * 查询通道同步
	 * @author	rpc
	 * @date	2018年04月20日
	 */
	public List<YfbChannelSyn> queryChannelSynByMerchantNo(String merchantNo) {
		return repayMerchantDao.queryYfbChannelSynByMerchantNo(merchantNo);
	}

	/**
	 * 查询通道同步日志记录
	 * @author	rpc
	 * @date	2018年04月20日
	 */
	public List<YfbChannelSynLog> queryChannelSynLogByMerchantNo(String merchantNo) {
		return repayMerchantDao.queryYfbChannelSynLogByMerchantNo(merchantNo);
	}

	@Override
	public List<YfbBindCardRecord> queryBindCardRecord(String cardNo) {
		return repayMerchantDao.queryBindCardRecord(cardNo);
	}

	@Override
	public RepayMerchantInfo getDataProcessing(String merchantNo) {
		return repayMerchantDao.getDataProcessing(merchantNo);
	}
}
