package cn.eeepay.framework.service.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.TerminalBack;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface TerminalBackService {

    List<TerminalBack> queryTerminalBackList(TerminalBack info, Page<TerminalBack> page);

    List<Map<String, Object>> queryTerminalBackSN(String orderNo);

    void exportTerminalBack(TerminalBack info, HttpServletResponse response)  throws Exception;
}
