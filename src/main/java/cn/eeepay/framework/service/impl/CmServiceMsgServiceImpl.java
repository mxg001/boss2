package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.daoCreditMgr.CmServiceMsgDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CmUserMessageInfo;
import cn.eeepay.framework.service.CmServiceMsgService;

@Service("cmServiceMsgService")
@Transactional
public class CmServiceMsgServiceImpl implements CmServiceMsgService {

	@Resource
	private CmServiceMsgDao cmServiceMsgDao;

	/**
	 * 服务消息列表查询
	 */
	public List<CmUserMessageInfo> selectMsgInfo(Page<CmUserMessageInfo> page, CmUserMessageInfo info) {
		cmServiceMsgDao.selectMsgInfo(page, info);
		List<CmUserMessageInfo> result = page.getResult();
		for (CmUserMessageInfo i : result) {
			if ("Y".equals(i.getIsDel())) {
				i.setMsgStatus("关闭");
			} else if ("Y".equals(i.getIsRead())) {
				i.setMsgStatus("已读");
			} else {
				i.setMsgStatus("未读");
			}
		}
		page.setResult(result);
		return result;
	}

	/**
	 * 根据id查询消息详情
	 */
	public CmUserMessageInfo selectMsgInfoById(String id) {
		CmUserMessageInfo i = cmServiceMsgDao.selectMsgInfoById(id);
		if ("Y".equals(i.getIsDel())) {
			i.setMsgStatus("关闭");
		} else if ("Y".equals(i.getIsRead())) {
			i.setMsgStatus("已读");
		} else {
			i.setMsgStatus("未读");
		}
		return i;
	}

	/**
	 * 回收服务消息
	 */
	public int updateMsgIsDelById(String id) {
		return cmServiceMsgDao.updateMsgIsDelById(id);
	}

}
