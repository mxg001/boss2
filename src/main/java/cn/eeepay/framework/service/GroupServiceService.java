package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqOrg;
import cn.eeepay.framework.model.AcqService;
import cn.eeepay.framework.model.AcqServiceRate;
import cn.eeepay.framework.model.AcqServiceRateTask;
import cn.eeepay.framework.model.AcqServiceTransRules;

/**
 * 收单服务service
 * 
 * @author junhu
 *
 */
public interface GroupServiceService {

	List<AcqOrg> acqOrgSelectBox();

	List<AcqService> listAcqServiceByCon(Map<String, Object> param, Page<AcqService> page);

	Integer insertService(AcqService acqService, List<AcqServiceRate> acqServiceRates, AcqServiceTransRules acqServiceTransRules);

	int updateAcqServiceStatus(AcqService acq);

	Map<String, Object> serviceDetail(Long id);

	List<AcqServiceRate> listEffectiveServiceRateByServiceId(Long id);

	List<AcqServiceRate> listServiceRateLogByRateIdAndCardType(Long rateId, Integer CardType);

	int deleteServiceRateTask(Long id);

	int insertAcqServiceRateTask(AcqServiceRateTask acqServiceRateTask);

	int updateAcqServiceTransRules(AcqServiceTransRules acqServiceTransRules);

	AcqServiceTransRules getAcqServiceTransRule(Long acqServiceId);

	int updateTimeSwitch(AcqService acq);
	
	void setAcqServiceStatus(AcqService acqService);
	
	/**
	 * 查询所有收单服务信息
	 * 
	 * @return
	 */
	Page<Map<String, Object>> queryServiceInfoList();

}
