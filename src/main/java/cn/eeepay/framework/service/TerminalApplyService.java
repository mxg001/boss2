package cn.eeepay.framework.service;

import java.util.List;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TerminalApply;

public interface TerminalApplyService {

	List<TerminalApply> queryAllInfo(Page<TerminalApply> page,TerminalApply terminalApply);
	TerminalApply queryInfoDetail(String id);
	int updateInfo(String id);
}
