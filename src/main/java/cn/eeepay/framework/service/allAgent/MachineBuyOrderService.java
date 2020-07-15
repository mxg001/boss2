package cn.eeepay.framework.service.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.MachineBuyOrder;
import cn.eeepay.framework.model.allAgent.TerInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface MachineBuyOrderService {

    List<MachineBuyOrder> queryMachineBuyOrderList(MachineBuyOrder info, Page<MachineBuyOrder> page);

    MachineBuyOrder queryMachineBuyOrderByOrderNo(String orderNo);

    List<TerInfo> querySNList(TerInfo info, Page<TerInfo> page);

    Map<String,Object> queryMachineBuyOrderCount(MachineBuyOrder info);

    List<MachineBuyOrder> queryMachineBuyOrderByTime(String time);

    int updateMachineBuyOrderAccountEntry(MachineBuyOrder info);

    void exportMachineBuyOrder(MachineBuyOrder info, HttpServletResponse response)  throws Exception;

    List<TerInfo> queryShipMachineDetail(String orderNo, Page<TerInfo> page);

}
