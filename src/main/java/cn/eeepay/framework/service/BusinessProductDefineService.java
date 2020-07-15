package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BusinessProductDefine;
import cn.eeepay.framework.model.Result;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface BusinessProductDefineService {

	public List<BusinessProductDefine> selectAllInfo();

	public List<BusinessProductDefine> selectAllInfoByBpId(Long bpId);

	public List<BusinessProductDefine> selectAllInfoByName(String bpId);

	public Map<Long, String> selectBpNameMap();

	public List<BusinessProductDefine> selectByCondition(Page<BusinessProductDefine> page,BusinessProductDefine bpd);

	public BusinessProductDefine selectById(String id);
	
	public BusinessProductDefine selectBybpId(String bpId);
	
	public BusinessProductDefine selectBybpIdAndAgentNo(String bpId,String agentNo);

	public List<BusinessProductDefine> selectBpTeam();

	public List<BusinessProductDefine> selectOtherProduct(String i);

	public int insertOrUpdate(Map<String, Object> info);

	public Map<String, Object> selectLinkInfo(String bpId);

	public Map<String, Object> selectDetailById(String id);

	public boolean selectRecord(Integer bpId);

	public int selectExistName(Long long1, String bpName, String type);

	public List<Long> findByService(Long serviceId);

	public Map<String, Object> getProductBase(Integer bpId);

	public int update(BusinessProductDefine product);

	public int updateProductBase(String params);

	public List<BusinessProductDefine> getProductByServiceType(String[] serviceTypes);

	public List<BusinessProductDefine> getProduct();

	/**
	 * 根据组织ID获取对应的业务产品以及群组号
	 * @author tans
	 * @date 2017年4月13日 下午2:31:27
	 * @param teamId
	 * @return
	 */
	public List<BusinessProductDefine> getProductByTeam(String teamId);

	public List<BusinessProductDefine> getTeamOtherBp(String teamId, String bpId);


    Result updateEffectiveStatus(BusinessProductDefine baseInfo);

    List<String> selectTeamIdsWithBpIds(List<String> ids);

    List<BusinessProductDefine> selectAllInfoByTeamId(String teamId);

	void exportQueryProduct(BusinessProductDefine bpd, HttpServletResponse response,Map<String, Object> msgMap);

	String selectBpNameByBpId(String bpId);

	List<BusinessProductDefine> selectProdcuteByTeamIdAndAgentNo(String agentNo,String teamId);
}
