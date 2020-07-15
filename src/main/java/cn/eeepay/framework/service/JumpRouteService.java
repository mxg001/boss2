package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcpWhitelist;
import cn.eeepay.framework.model.JumpRouteConfig;
import cn.eeepay.framework.model.SysDict;

import javax.servlet.http.HttpServletResponse;

public interface JumpRouteService {

	List<JumpRouteConfig> list(JumpRouteConfig baseInfo, Page<JumpRouteConfig> page);

	int save(String params);

	JumpRouteConfig getById(Integer id);

	int delete(String id);

	List<AcpWhitelist> selectAllWlInfo();

	int deleteByWlid(int id);

	int insertWl(AcpWhitelist aw);

	AcpWhitelist getWlInfoByMerchantNo(String merchantNo);

	List<SysDict> getServiceTypeSelectByBqIds(Integer[] ids);

    void export(HttpServletResponse response, JumpRouteConfig config);
}
