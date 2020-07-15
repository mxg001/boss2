package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.ZqFileSync;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
/**
 * 报报备 服务层
 *
 * @author tans
 * @date 2019-04-11
 */
public interface ZqFileSyncService {

    /**
     * 条件查询报报备
     * @param zqFileSync
     * @return
     */
    void selectPage(Page<ZqFileSync> page, ZqFileSync zqFileSync);

    void export(HttpServletResponse response, ZqFileSync zqServiceInfo);

    Result importData(MultipartFile file, String channelCode);

    Result updateStatus(ZqFileSync baseInfo);
}
