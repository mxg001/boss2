package cn.eeepay.framework.service.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExcActRouteGood;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/30/030.
 * @author  liuks
 * 路由商品service
 */
public interface ExcActRouteGoodService {

    int addExcActRouteGood(ExcActRouteGood good);

    boolean checkRoute(ExcActRouteGood good);

    List<ExcActRouteGood> selectAllList(ExcActRouteGood good, Page<ExcActRouteGood> page);

    ExcActRouteGood getRouteGood(int id);

    int updateExcActRouteGood(ExcActRouteGood good);

    int closeGood(int id, String state);

    int closeGoodBatch(String ids, String state,Map<String, Object> msg);

    int deleteGood(int id);

    int checkRouteGood(ExcActRouteGood good);

    List<ExcActRouteGood> importDetailSelect(ExcActRouteGood good);

    void importDetail(List<ExcActRouteGood> list, HttpServletResponse response) throws Exception;
}
