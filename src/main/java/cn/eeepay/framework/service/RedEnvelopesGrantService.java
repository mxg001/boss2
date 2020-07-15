package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/1/19/019.
 * 红包发放查询 service
 * @author  liuks
 */
public interface RedEnvelopesGrantService {

    List<RedEnvelopesGrant> selectAllByParam(RedEnvelopesGrant reg, Page<RedEnvelopesGrant> page);

    List<RedEnvelopesGrant> exportInfo(RedEnvelopesGrant reg);

    RedEnvelopesGrant sumCount(RedEnvelopesGrant reg);

    RedEnvelopesGrant selectRedEnvelopesGrantById(Long id);

    List<RedEnvelopesGrantImage> getImages(Long id);

    List<RedEnvelopesGrantDiscuss> getRedEnvelopesGrantDiscuss(Long id,Page<RedEnvelopesGrantDiscuss> page);

    List<RedEnvelopesGrantOption> selectRedEnvelopesGrantOption(Long id,Page<RedEnvelopesGrantOption> page);

    void exportRedEnvelopesGrant(List<RedEnvelopesGrant> list, HttpServletResponse response) throws Exception;

    int updateRedEnvelopesGrantImage(RedEnvelopesGrantImage img);

    int updateRedEnvelopesGrantImageAll(RedEnvelopesGrantImage img);

    int updateRemark(Long id);

    int updateStatusRisk(RedOrdersOption baseInfo);

    int deleteRedEnvelopesGrantDiscuss(RedEnvelopesGrantDiscuss baseInfo);
}
