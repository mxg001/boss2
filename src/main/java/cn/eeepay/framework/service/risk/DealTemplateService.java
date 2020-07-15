package cn.eeepay.framework.service.risk;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.risk.DealTemplate;

import java.util.List;

/**
 * @author MXG
 * create 2018/12/20
 */
public interface DealTemplateService {

    List<DealTemplate> selectTemplateListWithPage(Page<DealTemplate> page, String templateNo);

    int add(DealTemplate template);

    int update(DealTemplate template);

    int delete(String id);

    DealTemplate selectById(String id);

    List<DealTemplate> selectAll();
}
