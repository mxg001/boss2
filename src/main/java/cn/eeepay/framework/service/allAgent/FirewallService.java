package cn.eeepay.framework.service.allAgent;

import cn.eeepay.framework.db.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/13/013.
 * @author zja
 * 商品查询 service
 */
public interface FirewallService {

    List<Map> selectSysConfig(Map params);

    /**
     * 查询记录列表
     * @param params
     * @param page
     * @return
     */
    List<Map> selectRecordList(Map params, Page<Map> page);

    /**
     * 删除记录
     * @param params
     * @return
     */
    int deleteRecord(Map params);

    /**
     * 更新记录
     * @param params
     * @return
     */
    int updateRecord(Map params);

    /**
     * 新增记录
     * @param params
     * @return
     */
    int addRecord(Map params);
}
