package cn.eeepay.framework.service.luckDraw;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.luckDraw.AccessCount;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/11/7/007.
 * @author liuks
 * 访问统计 service
 */
public interface AccessCountService {

    List<AccessCount> selectAllList(AccessCount acc, Page<AccessCount> page);

    List<AccessCount> importDetailSelect(AccessCount acc);

    void importDetail(List<AccessCount> list, HttpServletResponse response) throws Exception;
}
