package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.MerchantWarningDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MerchantWarning;
import cn.eeepay.framework.service.MerchantWarningService;
import cn.eeepay.framework.util.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service("merchantWarningService")
public class MerchantWarningServiceImpl implements MerchantWarningService {

    @Resource
    private MerchantWarningDao merchantWarningDao;

    @Override
    public List<MerchantWarning> selectMerchantWarningPage(MerchantWarning merchantWarning, Page<MerchantWarning> page) {
        List<MerchantWarning> list= merchantWarningDao.selectMerchantWarningPage(merchantWarning, page);
        for (MerchantWarning m:list){
            m.setWarningImgUrl(CommonUtil.getImgUrlAgent(m.getWarningImg()));
            m.setTeamName(merchantWarningDao.selectTeamNameGroupConcat(m.getTeamId()));
        }
        return list;
    }

    @Override
    public MerchantWarning selectMerchantWarningDetail(Integer id) {
        MerchantWarning m=merchantWarningDao.selectMerchantWarningDetail(id);
        m.setWarningImgUrl(CommonUtil.getImgUrlAgent(m.getWarningImg()));
        return m;
    }

    @Override
    public int deleteMerchantWarning(Integer id) {
        return merchantWarningDao.deleteMerchantWarning(id);
    }

    @Override
    public Map<String, Object> updateIsUsedStatus(MerchantWarning merchantWarning) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("status", false);
        msg.put("msg", "操作失败");
        int num = merchantWarningDao.updateIsUsedStatus(merchantWarning.getId(), merchantWarning.getIsUsed());
        if (num > 0) {
            msg.put("status", true);
            msg.put("msg", "操作成功");
        }
        return msg;
    }

    @Override
    public int insertMerWarning(MerchantWarning merchantWarning){
        return merchantWarningDao.insertMerWarning(merchantWarning);
    }

    @Override
    public int updateMerWarning(MerchantWarning merchantWarning){
        return merchantWarningDao.updateMerWarning(merchantWarning);
    }
}
