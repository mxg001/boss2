package cn.eeepay.framework.service.risk;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.risk.TradeRestrict;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/5/5/005.
 * @author  liuks
 */
public interface TradeRestrictService {

    List<TradeRestrict> selectAllList(TradeRestrict info, Page<TradeRestrict> page);

    List<TradeRestrict> importDetailSelect(TradeRestrict info);

    void importDetail(List<TradeRestrict> list, HttpServletResponse response) throws Exception;

    int riskStatusChangeBatch(String ids, int status,Map<String, Object> msg);

    int changeStatus(int id, int status, Map<String, Object> msg);

    int updateFailureTime();
}
