package cn.eeepay.framework.service.exchange;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ExcRouteGood;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/30/030.
 * @author  liuks
 * 路由商品service
 */
public interface ExcRouteGoodService {

    int addExcRouteGood(ExcRouteGood good);

    boolean checkRoute(ExcRouteGood good);

    List<ExcRouteGood> selectAllList(ExcRouteGood good, Page<ExcRouteGood> page);

    ExcRouteGood getRouteGood(int id);

    int updateExcRouteGood(ExcRouteGood good);

    int closeGood(int id, String state);

    int closeGoodBatch(String ids, String state, Map<String, Object> msg);

    int deleteGood(int id);

    int checkRouteGood(ExcRouteGood good);

    List<ExcRouteGood> importDetailSelect(ExcRouteGood good);

    void importDetail(List<ExcRouteGood> list, HttpServletResponse response) throws Exception;
}
