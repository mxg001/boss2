package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.ZqServiceInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
/**
 * 直清商户服务报件 服务层
 *
 * @author tans
 * @date 2019-04-02
 */
public interface ZqServiceInfoService {

    /**
     * 条件查询直清商户服务报件
     * @param zqServiceInfo
     * @return
     */
    void selectZqServiceInfoPage(Page<ZqServiceInfo> page, ZqServiceInfo zqServiceInfo);

    /**
     * 查询直清商户服务报件
     * @param id
     * @return
     */
    ZqServiceInfo selectZqServiceInfoById(Long id);

    void export(HttpServletResponse response, ZqServiceInfo zqServiceInfo);

    Result zqSyncSerBatch(List<ZqServiceInfo> list);

    Result zqSyncSer(ZqServiceInfo zqServiceInfo, String accessUrl);

    Result updateDealStatus(ZqServiceInfo baseInfo);
}
