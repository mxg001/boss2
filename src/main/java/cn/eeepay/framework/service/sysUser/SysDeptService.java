package cn.eeepay.framework.service.sysUser;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.SysDept;

public interface SysDeptService {
	int insert(SysDept sysDept) throws Exception;
	List<SysDept> findSysDeptList(SysDept sysDept,Sort sort,Page<SysDept> page) throws Exception;
	List<SysDept> findAllSysDeptList() throws Exception;
	SysDept findSysDeptById(String id) throws Exception;
}
