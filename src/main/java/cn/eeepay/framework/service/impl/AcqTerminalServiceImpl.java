package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.AcqTerminalDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqMerchant;
import cn.eeepay.framework.model.AcqTerminal;
import cn.eeepay.framework.service.AcqTerminalService;

@Service("acqTerminalService")
@Transactional
public class AcqTerminalServiceImpl implements AcqTerminalService{

	@Resource
	public AcqTerminalDao acqTerminalDao;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		return acqTerminalDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(AcqTerminal record) {
		return acqTerminalDao.insert(record);
	}

	@Override
	public AcqTerminal selectByPrimaryKey(Integer id) {
		return acqTerminalDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(AcqTerminal record) {
		return acqTerminalDao.updateByPrimaryKey(record);
	}

	@Override
	public List<AcqTerminal> selectAllInfo(Page<AcqTerminal> page, AcqTerminal act) {
		return acqTerminalDao.selectAllInfo(page, act);
	}

	@Override
	public int updateStatusByid(AcqTerminal record) {
		return acqTerminalDao.updateStatusByid(record);
	}

	@Override
	public AcqTerminal selectTerminalByTerNo(AcqTerminal record) {
		return acqTerminalDao.selectTerminalByTerNo(record);
	}

	@Override
	public int insertInfo(AcqTerminal record) {
		return acqTerminalDao.insertInfo(record);
	}

	@Override
	public int updateInfo(AcqTerminal record) {
		return acqTerminalDao.updateInfo(record);
	}
}
