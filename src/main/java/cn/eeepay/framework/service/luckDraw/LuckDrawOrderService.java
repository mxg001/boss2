package cn.eeepay.framework.service.luckDraw;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.luckDraw.LuckDrawOrder;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/11/7/007.
 */
public interface LuckDrawOrderService {

    List<LuckDrawOrder> selectAllList(LuckDrawOrder order, Page<LuckDrawOrder> page);

    List<LuckDrawOrder> importDetailSelect(LuckDrawOrder order);

    void importDetail(List<LuckDrawOrder> list, HttpServletResponse response) throws Exception;

    Integer sumAwardsConfigId(LuckDrawOrder order);

    LuckDrawOrder getLuckDrawOrder(int id);
}
