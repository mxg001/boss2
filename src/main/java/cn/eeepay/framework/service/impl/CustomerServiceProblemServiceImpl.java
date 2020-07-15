package cn.eeepay.framework.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.CustomerServiceProblemMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AppScopeInfo;
import cn.eeepay.framework.model.CustomerServiceProblem;
import cn.eeepay.framework.model.CustomerServiceQo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.CustomerServiceProblemService;

@org.springframework.stereotype.Service
public class CustomerServiceProblemServiceImpl implements CustomerServiceProblemService {

	@Autowired
	private CustomerServiceProblemMapper customerServiceProblemMapper;

	public List<CustomerServiceProblem> listByQo(CustomerServiceQo qo) {
		return customerServiceProblemMapper.selectByQo(qo);
	}

	public void queryByQo(Page<CustomerServiceProblem> page, CustomerServiceQo qo) {
		customerServiceProblemMapper.queryByQo(page, qo);
	}

	@Override
	@Transactional
	public void removeById(String id) {
		// 逻辑删除 更新时间 更新用户
		Date date = new Date();
		customerServiceProblemMapper.updateStatusById(id, date, getCurrentUsername());
	}

	private String getCurrentUsername() {
		UserLoginInfo user = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user.getUsername();
	}

	@Override
	@Transactional
	public void save(CustomerServiceProblem data) {
		Date date = new Date();
		String username = getCurrentUsername();
		Integer problemId = data.getProblemId();
		data.setUpdateTime(date);
		data.setUpdateUser(username);
		List<AppScopeInfo> appScopeList = data.getAppScopeList();
		StringBuilder sb = new StringBuilder();
		if (appScopeList != null && appScopeList.size() > 0 ) {
			for (AppScopeInfo appScopeInfo : appScopeList) {
				if (appScopeInfo.getCheckStatus() == 1) {
					sb.append(appScopeInfo.getSysValue()).append("-");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		data.setAppScope(sb.toString());
		if(problemId == null) {
			data.setCreateTime(date);
			data.setCreateUser(username);
			customerServiceProblemMapper.insertSelective(data);
		}else {
			customerServiceProblemMapper.updateByPrimaryKeySelective(data);
		}
	}

}
