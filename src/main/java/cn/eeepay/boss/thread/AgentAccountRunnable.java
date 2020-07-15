package cn.eeepay.boss.thread;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.service.SuperBankService;
import cn.eeepay.framework.util.ClientInterface;

/**
 * 代理商开户
 * @author tans
 * @date 2118/1/10
 */
public class AgentAccountRunnable implements Runnable {

    private String agentNo;//代理商编号

    private String subjectNo;//科目号

    private SuperBankService superBankService;

    public AgentAccountRunnable(String agentNo, String subjectNo,
                                SuperBankService superBankService) {
        this.agentNo = agentNo;
        this.subjectNo = subjectNo;
        this.superBankService = superBankService;
    }

    @Override
    public void run() {
        if(StringUtils.isBlank(agentNo) || StringUtils.isBlank(subjectNo)){
            return;
        }
        String resultMsg = ClientInterface.createAccountByAcc(agentNo, subjectNo);
        if(StringUtils.isBlank(resultMsg)){
            return;
        }
        JSONObject json = JSONObject.parseObject(resultMsg);
        if(json.getBoolean("status")){
            superBankService.updateOrgAccount(agentNo);
        }
    }
}
