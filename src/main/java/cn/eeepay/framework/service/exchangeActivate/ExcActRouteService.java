package cn.eeepay.framework.service.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExcActRoute;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * banner service
 */
public interface ExcActRouteService {

    List<ExcActRoute> selectAllList(ExcActRoute route, Page<ExcActRoute> page);

    int addRoute(ExcActRoute route);

    ExcActRoute getRoute(int id);

    int updateRoute(ExcActRoute route);

    int closeRoute(int id, String state);

    boolean checkRoute(ExcActRoute route);

    Map<String,Object> importDiscount(MultipartFile file) throws Exception;

    List<ExcActRoute> getRouteALLList();
}
