package cn.eeepay.framework.service.cusSms;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.cusSms.CusSmsTemplate;
import cn.eeepay.framework.model.cusSms.SendResult;

import java.util.List;
import java.util.Map;

public interface CusSmsTemplateService {

    List<CusSmsTemplate> selectAllList(CusSmsTemplate info, Page<CusSmsTemplate> page);

    int addSmsTemplate(CusSmsTemplate info);

    int editSmsTemplate(CusSmsTemplate info);

    int deleteSmsTemplate(int id);

    CusSmsTemplate getSmsTemplateInfo(int id);

    int sendTemplate(SendResult info, Map<String, Object> msg);
}
