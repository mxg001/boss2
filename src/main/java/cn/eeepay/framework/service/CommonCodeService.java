package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CommonCode;

import java.util.List;

/**
 * @author ：quanhz
 * @date ：Created in 2020/3/18 11:02
 */

public interface CommonCodeService {

    List<CommonCode> query(CommonCode commonCode);

    List<CommonCode> queryAll(Page<CommonCode> page,String agentNo);

    int insert(CommonCode commonCode);

    int update(CommonCode commonCode);

    int delById(Long id);


    String queryAgentNameByNo(String agentNo);

    CommonCode queryById(Long id);
}
