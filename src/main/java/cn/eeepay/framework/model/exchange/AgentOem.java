package cn.eeepay.framework.model.exchange;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/14/014.
 * @author  liuks
 * 对应表rdmp_oem_agent
 */
public class AgentOem {

    private long id;

    private String oemNo;//组织

    private String agentNo;//代理商编号

    private String agentLevel;//代理商级别

    private String agentNode;//代理商节点

    private String teamId;//V2 组织编号

    private Date createTime;//创建时间

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
}
