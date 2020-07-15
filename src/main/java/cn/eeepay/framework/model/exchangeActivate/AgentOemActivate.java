package cn.eeepay.framework.model.exchangeActivate;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/14/014.
 * @author  liuks
 * 对应表yfb_oem_agent
 */
public class AgentOemActivate {

    private long id;

    private String oemNo;//组织

    private String agentNo;//代理商编号

    private String agentLevel;//代理商级别

    private String agentNode;//代理商节点

    private String teamId;//V2 组织编号

    private Date createTime;//创建时间

    private String agentAccount;//代理商是否已在账户(1为是0为否)


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOemNo() {
        return oemNo;
    }

    public void setOemNo(String oemNo) {
        this.oemNo = oemNo;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getAgentLevel() {
        return agentLevel;
    }

    public void setAgentLevel(String agentLevel) {
        this.agentLevel = agentLevel;
    }

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAgentAccount() {
        return agentAccount;
    }

    public void setAgentAccount(String agentAccount) {
        this.agentAccount = agentAccount;
    }
}
