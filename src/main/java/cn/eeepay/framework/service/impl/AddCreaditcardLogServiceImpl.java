package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AddCreaditcardLogDao;
import cn.eeepay.framework.model.AddCreaditcardLog;
import cn.eeepay.framework.service.AddCreaditcardLogService;
import cn.eeepay.framework.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tans
 * @date 2019/9/20 14:30
 */
@Service
public class AddCreaditcardLogServiceImpl implements AddCreaditcardLogService {

    @Resource
    private AddCreaditcardLogDao addCreaditcardLogDao;

    @Override
    public List<AddCreaditcardLog> selectMerchantCreditcard(String merchantNo) {
        if(StringUtil.isEmpty(merchantNo)) {
            return null;
        }
        List<AddCreaditcardLog> list = addCreaditcardLogDao.selectMerchantCreditcard(merchantNo);
        if(list != null && list.size() > 0) {
            for(AddCreaditcardLog item: list) {
                item.setAccountNo(StringUtil.sensitiveInformationHandle(item.getAccountNo(), 2));
            }
        }
        return list;
    }

    @Override
    public AddCreaditcardLog selectMerchantCreditcardDetail(Long id) {
        if(id == null) {
            return null;
        }
        return addCreaditcardLogDao.selectMerchantCreditcardDetail(id);
    }
}
