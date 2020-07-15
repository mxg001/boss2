package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface AgentAuthorizedService {

	Map<String, Object> getData(String record_code);

	int upRecord(Map params);

	int upCloseStatus(Map params);

	int upIsLook(Map params);

	int delRecord(String record_code);

	int addRecord(Map params);

	List<Map> getDatas(Map params, Page<Map> page);

	List<Map<String,Object>> importDetailSelect(Map<String, Object> params);

	void importDetail(List<Map<String,Object>> list, HttpServletResponse response) throws Exception;

    Map<String,Object> importDiscount(MultipartFile file) throws Exception;

	List<Map<String,Object>> selectTopAgentManagement(Map<String, Object> map, Page page);

	Map<String,Object> selectAgentAuthorizedAgentLink(String agent_link);

	List<Map<String, Object>> getAgentAuthorized(String agent_authorized);

	int updateTopAgentManagement(String agent_authorized);

	int addTopAgentManagement(String agent_authorized);

	int deleteTopAgentManagement(String agent_authorized);

	boolean isCorrectChain(String recordCode);

	boolean isTop(String recordCode);
}
