package cn.eeepay.framework.service.impl.allAgent;

import cn.eeepay.framework.dao.AgentTerminalDao;
import cn.eeepay.framework.daoAllAgent.AgentUserDao;
import cn.eeepay.framework.service.allAgent.AgentTerminalService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/18/018.
 * @author  liuks
 * 机具下发代理商更新到超级盟主
 */
@Service("agentTerminalService")
public class AgentTerminalServiceImpl implements AgentTerminalService {

    @Resource
    private AgentTerminalDao agentTerminalDao;

    @Resource
    private AgentUserDao agentUserDao;

    @Override
    public int insertAgentTerminal(String agentNo,String sn,String agentLevel) {
        int num=0;
        if(agentNo!=null){
            List<Map<String,Object>> list= agentUserDao.selectAgentUser(agentNo);
            if(list!=null&&list.size()>0){
                String userCode=list.get(0).get("user_code").toString();
                if(userCode!=null){
                    if(agentLevel.equals("1")){
                        num=agentTerminalDao.InsertAgentTerminal(agentNo,sn,userCode,1);
                    }else if(agentLevel.equals("2")){
                        num=agentTerminalDao.InsertAgentTerminal(agentNo,sn,userCode,2);
                    }
                }
            }
        }
        return num;
    }
}
