package cn.eeepay.framework.service.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExcActProductMedia;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/10/11/011.
 * @author  liuks
 * 媒体资源 service
 */
public interface ExcActProductMediaService {

    List<ExcActProductMedia> selectAllList(ExcActProductMedia media, Page<ExcActProductMedia> page);

    int deleteMedia(int id);

    void downloadFile(String name,String reName, HttpServletResponse response,Map<String, Object> msg);

    ExcActProductMedia getProductMedia(int id);

    int addExcActProductMedia(ExcActProductMedia media,Map<String, Object> msg) throws Exception;
}
