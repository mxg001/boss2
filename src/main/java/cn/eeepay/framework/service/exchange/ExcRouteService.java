package cn.eeepay.framework.service.exchange;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ExcRoute;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * banner service
 */
public interface ExcRouteService {

    List<ExcRoute> selectAllList(ExcRoute route, Page<ExcRoute> page);

    int addRoute(ExcRoute route);

    ExcRoute getRoute(int id);

    int updateRoute(ExcRoute route);

    int closeRoute(int id, String state);

    boolean checkRoute(ExcRoute route);

    Map<String,Object> importDiscount(MultipartFile file) throws Exception;

    List<ExcRoute> getRouteALLList();
}
