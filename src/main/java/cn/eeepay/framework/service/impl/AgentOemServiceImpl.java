package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentOemInfoDao;
import cn.eeepay.framework.service.AgentOemService;
import cn.eeepay.framework.util.BossBaseException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author tans
 * @date 2018/11/13 17:13
 */
@Service("agentOemService")
@Transactional
public class AgentOemServiceImpl implements AgentOemService {

    @Resource
    private AgentOemInfoDao agentOemInfoDao;

    @Override
    public int checkExists(String agentNo) {
        if(StringUtils.isBlank(agentNo)){
            throw new BossBaseException("代理商编号不能为空");
        }
        return agentOemInfoDao.checkExists(agentNo);
    }

    @Override
    public int insert(String agentNo, String oemType) {
        if(StringUtils.isBlank(agentNo) || StringUtils.isBlank(oemType)){
            throw new BossBaseException("代理商编号或者oemType不能为空");
        }
        return agentOemInfoDao.insert(agentNo,oemType);
    }
}
