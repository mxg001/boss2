package cn.eeepay.framework.service.impl.risk;

import cn.eeepay.framework.dao.risk.DealTemplateDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.risk.DealTemplate;
import cn.eeepay.framework.service.risk.DealTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author MXG
 * create 2018/12/20
 */
@Service
public class DealTemplateServiceImpl implements DealTemplateService {

    @Resource
    private DealTemplateDao templateDao;

    @Override
    public List<DealTemplate> selectTemplateListWithPage(Page<DealTemplate> page, String templateNo) {
        if(StringUtils.isNotBlank(templateNo)){
            return templateDao.selectByTemplateNoWithPage(page, templateNo);
        }else {
            return templateDao.selectAllWithPage(page);
        }
    }

    @Override
    public int add(DealTemplate template) {
        return templateDao.add(template);
    }

    @Override
    public int update(DealTemplate template) {
        return templateDao.update(template);
    }

    @Override
    public int delete(String id) {
        return templateDao.delete(id);
    }

    @Override
    public DealTemplate selectById(String id) {
        return templateDao.selectById(id);
    }

    @Override
    public List<DealTemplate> selectAll() {
        return templateDao.selectAll();
    }
}
