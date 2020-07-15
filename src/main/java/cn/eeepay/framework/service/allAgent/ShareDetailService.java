package cn.eeepay.framework.service.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.ShareDetail;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface ShareDetailService {

    List<ShareDetail> queryShareDetailList(ShareDetail info, Page<ShareDetail> page);

    Map<String, Object> queryShareDetailCount(ShareDetail info);

    void exportShareDetail(ShareDetail info, HttpServletResponse response)  throws Exception;
}
