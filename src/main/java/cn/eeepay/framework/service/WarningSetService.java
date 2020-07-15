package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.WarningSet;

import javax.servlet.http.HttpServletResponse;

/**
 * 预警设置选项服务
 * 
 * @author Qiujian
 *
 */
public interface WarningSetService {

	/**
	 * 根据服务ID获取服务对应的预警信息
	 * 
	 * @param serviceId
	 * @return
	 */
	WarningSet getWaringInfoByService(Integer serviceId);

	/**
	 * 更新收单服务预警设置
	 * 
	 * @param info
	 */
	int updateWarningSet(WarningSet info);

    void selectPage(WarningSet baseInfo, Page<WarningSet> page);

    int updateWarnStatus(WarningSet baseInfo);

	int deleteWarning(WarningSet baseInfo);

    void exportInfo(HttpServletResponse response, WarningSet baseInfo);

    void selectSettlePage(WarningSet baseInfo, Page<WarningSet> page);

	void exportSettleInfo(HttpServletResponse response, WarningSet warningSet);

	WarningSet getWaringInfoByServiceAndStatus(Integer outServiceId, Integer warningSetTypeOut);
}
