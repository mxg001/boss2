package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RedEnvelopesReceive;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/1/18/018.
 */
public interface RedEnvelopesReceiveService {

    List<RedEnvelopesReceive> selectAllByParam(RedEnvelopesReceive red, Page<RedEnvelopesReceive> page);

    List<RedEnvelopesReceive> exportInfo(RedEnvelopesReceive order);

    RedEnvelopesReceive sumCount(RedEnvelopesReceive order);

    List<RedEnvelopesReceive> selectRedEnvelopesReceive(Long id,Page<RedEnvelopesReceive> page);

    void exportRedEnvelopesReceive(List<RedEnvelopesReceive> list, HttpServletResponse response) throws Exception;
}
