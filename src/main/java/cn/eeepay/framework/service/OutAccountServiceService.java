package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;

/**
 * 出款服务service
 * 
 * @author junhu
 *
 */
public interface OutAccountServiceService {

	int saveOutAccountServiceFunction(OutAccountServiceFunction function);

	OutAccountServiceFunction queryOutAccountServiceFunction();

	int insertOutAccountService(OutAccountService service, List<OutAccountServiceRate> serviceRates);

	List<OutAccountService> queryOutAccountService(Map<String, Object> param, Page<OutAccountService> page);

	int updateOutAccountServiceStatus(Integer id, Integer outAccountStatus);
	
	Map<String, Object> getOutAccountServiceDetail(Integer serviceId);

	List<OutAccountServiceRateTask> queryOutAccountServiceRateLog(Integer serviceRateId);

	int updateOutAccountService(OutAccountService service);

	int insertOutAccountServiceRateTask(OutAccountServiceRateTask serviceRateTask);

	int deleteOutAccountServiceRateTask(Integer id);
	
	/**
	 * 根据id获取分润信息
	 * @param id
	 * @return
	 */
	OutAccountServiceRate getById(Integer id);
	
	/**
	 * 根据生效日期查询
	 * @param dateStr
	 * @return
	 */
	List<OutAccountServiceRateTask> findByEffective();
	
	int updateByTaskBatch(List<OutAccountServiceRateTask> taskList);
	
	int updateByRateBatch(List<OutAccountServiceRate> rateList, List<Integer> taskIdList);
	
	/**
	 * 出账服务 当日累计已出款额度归零
	 * @param acq_org_id
	 * @return
	 */
	int updateResetDayTotalAmount(Integer acq_org_id);

	List<OutAccountService> queryBoxAllInfo();

	/**
	 * 根据id获取 OutAccountService
	 * @param serviceId
	 * @return
	 */
	OutAccountService getOutAccountServiceById(Integer serviceId);
	
	/**
	 * 不分页查询
	 * @param param
	 * @return
	 */
	List<OutAccountService> queryOutAccountServiceNoPage();

    void updateUserBalance();

    Result selectAgentWithdraw();

	Result saveAgentWithdraw(Map<String, Object> map);

    OutAccountService findServiceId(Integer serviceId);

}
