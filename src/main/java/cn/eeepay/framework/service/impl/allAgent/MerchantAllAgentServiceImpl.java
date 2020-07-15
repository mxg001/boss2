package cn.eeepay.framework.service.impl.allAgent;

import cn.eeepay.framework.daoAllAgent.MerchantAllAgentDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.allAgent.MerchantAllAgent;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.MerchantInfoService;
import cn.eeepay.framework.service.allAgent.MerchantAllAgentService;
import cn.eeepay.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("merchantAllAgentService")
public class MerchantAllAgentServiceImpl implements MerchantAllAgentService {

    private static final Logger log = LoggerFactory.getLogger(MerchantAllAgentServiceImpl.class);

    @Resource
    private MerchantAllAgentDao merchantAllAgentDao;
    @Resource
    private MerchantInfoService merchantInfoService;
    @Resource
    public AgentInfoService agentInfoService;

    @Override
    public List<MerchantAllAgent> queryMerchantAllAgent(MerchantAllAgent info, Page<MerchantAllAgent> page) {
        List<MerchantAllAgent> list=merchantAllAgentDao.queryMerchantAllAgent(info,page);
        for(MerchantAllAgent m:list){
            if(m.getMerchantNo()!=null&&!"".equals(m.getMerchantNo())){
                MerchantInfo merchantInfo=merchantInfoService.selectByMerNo(m.getMerchantNo());
                if(merchantInfo!=null){
                    m.setMerchantName(merchantInfo.getMerchantName());
                }
            }
            if(m.getOneAgentNo()!=null&&!"".equals(m.getOneAgentNo())){
                AgentInfo agentInfo=agentInfoService.getAgentByNo(m.getOneAgentNo());
                if(agentInfo!=null){
                    m.setOneAgentName(agentInfo.getAgentName());
                }
            }
            m.setMobile(StringUtil.sensitiveInformationHandle(m.getMobile(),0));
            m.setMobilePhone(StringUtil.sensitiveInformationHandle(m.getMobilePhone(),0));
        }
        return list;
    }

    @Override
    public Map<String, Object> queryMerchantAllAgentCount(MerchantAllAgent info) {
        return merchantAllAgentDao.queryMerchantAllAgentCount(info);
    }

    @Override
    public MerchantAllAgent queryMerchantAllAgentByMerNo(String merchantNo) {
        return merchantAllAgentDao.queryMerchantAllAgentByMerNo(merchantNo);
    }

    @Override
    public int updateMerchantAllAgentByMerNo(MerchantAllAgent info){
        return merchantAllAgentDao.updateMerchantAllAgentByMerNo(info);
    }
}
