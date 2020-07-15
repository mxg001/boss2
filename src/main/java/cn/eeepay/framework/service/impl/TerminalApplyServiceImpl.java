package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.TerminalApplyDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TerminalApply;
import cn.eeepay.framework.service.TerminalApplyService;

@Service("terminalApplyService")
@Transactional
public class TerminalApplyServiceImpl implements TerminalApplyService{

	@Resource
	private TerminalApplyDao terminalApplyDao;

	@Override
	public List<TerminalApply> queryAllInfo(Page<TerminalApply> page, TerminalApply terminalApply) {
		return terminalApplyDao.queryAllInfo(page, terminalApply);
	}

	@Override
	public TerminalApply queryInfoDetail(String id) {
		return terminalApplyDao.queryInfoDetail(id);
	}

	@Override
	public int updateInfo(String id) {
		return terminalApplyDao.updateInfo(id);
	}
	
}
