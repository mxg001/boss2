package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentAccountControlDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentAccountControl;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.service.AgentAccountControlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("agentAccountControlService")
@Transactional
public class AgentAccountControlServiceImpl implements AgentAccountControlService {
    @Resource
    private AgentAccountControlDao agentAccountControlDao;

    public List<AgentAccountControl> queryAgentAccountControl(AgentAccountControl agentAccountControl,Page<AgentAccountControl> page){
        List<AgentAccountControl> list=agentAccountControlDao.queryAgentAccountControl(agentAccountControl,page);
        return list;
    }

    public AgentAccountControl queryAgentAccountControlByDefault(){
        AgentAccountControl agentAccountControl=agentAccountControlDao.queryAgentAccountControlByDefault();
        return agentAccountControl;
    }

    public boolean saveAgentAccountControl(AgentAccountControl agentAccountControl){
        AgentAccountControl a=agentAccountControlDao.queryAgentAccountControlByDefault();
        Integer i=null;
        //判断默认存不存在，存在修改，不存在添加
        if(a!=null){
            a.setStatus(agentAccountControl.getStatus());
            a.setRetainAmount(agentAccountControl.getRetainAmount());
            i=agentAccountControlDao.updateAgentAccountControlDefault(a);
        }else{
            agentAccountControl.setDefaultStatus(1);
            i=agentAccountControlDao.insertAgentAccountControl(agentAccountControl);
        }
        if (i == null)
            return false;
        else
            return i > 0;
    }

    public AgentInfo queryAgentByID(String agentNo){
        AgentInfo agentInfo=agentAccountControlDao.queryAgentByID(agentNo);
        return agentInfo;
    }

    public boolean queryAgentAccountControlByAgentNoCount(String agentNo){
        Integer i=agentAccountControlDao.queryAgentAccountControlByAgentNoCount(agentNo);
        if (i == null)
            return false;
        else
            return i > 0;
    }

    public boolean addAgentAccountControl(AgentAccountControl agentAccountControl){
        agentAccountControl.setStatus(1);
        agentAccountControl.setDefaultStatus(0);
        Integer i=agentAccountControlDao.insertAgentAccountControl(agentAccountControl);
        if (i == null)
            return false;
        else
            return i > 0;
    }

    public boolean updateAgentAccountControl(AgentAccountControl agentAccountControl){
        Integer i=agentAccountControlDao.updateAgentAccountControl(agentAccountControl);
        if (i == null)
            return false;
        else
            return i > 0;
    }

    public AgentAccountControl queryAgentAccountControlByAgentNo(String agentNo){
        AgentAccountControl agentAccountControl=agentAccountControlDao.queryAgentAccountControlByAgentNo(agentNo);
        return agentAccountControl;
    }

    public boolean deleteAgentAccountControl(String agentNo){
        Integer i=agentAccountControlDao.deleteAgentAccountControl(agentNo);
        if (i == null)
            return false;
        else
            return i > 0;
    }
}
