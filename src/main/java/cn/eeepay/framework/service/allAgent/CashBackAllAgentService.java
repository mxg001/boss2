package cn.eeepay.framework.service.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.CashBackAllAgent;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface CashBackAllAgentService {

    List<CashBackAllAgent> queryCashBackDetailAllAgentList(CashBackAllAgent info, Page<CashBackAllAgent> page);

    void exportCashBackAllAgent(CashBackAllAgent info, HttpServletResponse response)  throws Exception;
}
