package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.RepayTypeManagerDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RepayChannel;
import cn.eeepay.framework.model.RepayPlanInfo;
import cn.eeepay.framework.service.RepayTypeManagerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author MXG
 * create 2018/08/29
 */
@Service
public class RepayTypeManagerServiceImpl implements RepayTypeManagerService {

    @Resource
    private RepayTypeManagerDao repayTypeManagerDao;

    @Override
    public List<RepayPlanInfo> selectRepayTypeList(Page<RepayPlanInfo> page, String planType) {
        if(StringUtils.isNotBlank(planType)){
            return repayTypeManagerDao.selectRepayTypeByPlanType(page, planType);
        }else {
            return repayTypeManagerDao.selectRepayTypeList(page);
        }
    }

    @Override
    public RepayPlanInfo queryTypeDetailById(String id) {
        return repayTypeManagerDao.queryTypeDetailById(id);
    }

    @Override
    public List<RepayChannel> selectChannelsByRepayType(String type) {
        return repayTypeManagerDao.selectChannelsByRepayType(type);
    }

    @Override
    public List<RepayChannel> selectChannelsWithoutType() {
        return repayTypeManagerDao.selectChannelsWithoutType();
    }

    @Override
    public int updateRepayPlanInfo(RepayPlanInfo info) {
        return repayTypeManagerDao.UpdateRepayType(info);
    }

    @Override
    public int updateChannel(RepayChannel channel) {
        return repayTypeManagerDao.updateChannel(channel);
    }

    @Override
    public int relieve(String id) {
        return repayTypeManagerDao.relieve(id);
    }


}
