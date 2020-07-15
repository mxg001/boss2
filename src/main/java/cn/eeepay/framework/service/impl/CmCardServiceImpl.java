package cn.eeepay.framework.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.daoCreditMgr.CmCardDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmCardInfo;
import cn.eeepay.framework.service.CmCardService;

@Service("cmCardService")
@Transactional
public class CmCardServiceImpl implements CmCardService {

	@Resource
	private CmCardDao cmCardDao;

	/**
	 * 卡片列表查询
	 */
	public List<CmCardInfo> selectCardInfo(Page<CmCardInfo> page, CmCardInfo info) {
		List<CmCardInfo> list = cmCardDao.selectCardInfo(page, info);
		for (CmCardInfo cci : list) {
			if (cci.getTotalAmount() != null) {
				cci.setTotalAmount(cci.getTotalAmount().divide(new BigDecimal(100)));
			}
		}
		return list;
	}

	/**
	 * 导出
	 */
	public List<CmCardInfo> exportCmCard(CmCardInfo info) {
		List<CmCardInfo> list = cmCardDao.exportCmCard(info);
		for (CmCardInfo cci : list) {
			if (cci.getTotalAmount() != null) {
				cci.setTotalAmount(cci.getTotalAmount().divide(new BigDecimal(100)));
			}
		}
		return list;
	}

	/**
	 * 根据id查询卡片信息
	 */
	public CmCardInfo queryCardInfoById(String id) {
		return cmCardDao.queryCardInfoById(id);
	}

	/**
	 * 修改卡片信息
	 */
	public int updateCardInfo(CmCardInfo info) {
		return cmCardDao.updateCardInfo(info);
	}

}
