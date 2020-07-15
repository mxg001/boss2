package cn.eeepay.framework.service.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.MachineBuyOrder;
import cn.eeepay.framework.model.allAgent.MerchantAllAgent;
import cn.eeepay.framework.model.allAgent.TerInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface MerchantAllAgentService {

    List<MerchantAllAgent> queryMerchantAllAgent(MerchantAllAgent info, Page<MerchantAllAgent> page);

    Map<String,Object> queryMerchantAllAgentCount(MerchantAllAgent info);

    MerchantAllAgent queryMerchantAllAgentByMerNo(String merchantNo);

    int updateMerchantAllAgentByMerNo(MerchantAllAgent info);
}
