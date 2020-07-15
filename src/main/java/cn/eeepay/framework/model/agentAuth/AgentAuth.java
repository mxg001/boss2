package cn.eeepay.framework.model.agentAuth;

import java.util.Date;

/**
 * Created by Administrator on 2019/1/10/010.
 * @author  liuks
 */
public class AgentAuth {

    private Integer id;
    private String recordCode;
    private String agentAuthorized;//代理商编号(agent_authorized可以访问agent_link)
    private String agentLink;//授权查询代理商编号
    private String checkUser;//审核人员
    private Integer recordCheck;//审核状态：0未通过，1通过
    private Integer recordStatus;//开启状态：0关闭;1:开启
    private String recordCreator;//创建者
    private Date createTime;//创建时间
    private Date lastUpdatTime;//最后修改时间

    private String agentAuthorizedName;
    private String agentLinkName;

    private String topAgent;
    private Integer linkLevel;
    private Integer isTop;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecordCode() {
        return recordCode;
    }

    public void setRecordCode(String recordCode) {
        this.recordCode = recordCode;
    }

    public String getAgentAuthorized() {
        return agentAuthorized;
    }

    public void setAgentAuthorized(String agentAuthorized) {
        this.agentAuthorized = agentAuthorized;
    }

    public String getAgentLink() {
        return agentLink;
    }

    public void setAgentLink(String agentLink) {
        this.agentLink = agentLink;
    }

    public String getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }

    public Integer getRecordCheck() {
        return recordCheck;
    }

    public void setRecordCheck(Integer recordCheck) {
        this.recordCheck = recordCheck;
    }

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getRecordCreator() {
        return recordCreator;
    }

    public void setRecordCreator(String recordCreator) {
        this.recordCreator = recordCreator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdatTime() {
        return lastUpdatTime;
    }

    public void setLastUpdatTime(Date lastUpdatTime) {
        this.lastUpdatTime = lastUpdatTime;
    }

    public String getAgentAuthorizedName() {
        return agentAuthorizedName;
    }

    public void setAgentAuthorizedName(String agentAuthorizedName) {
        this.agentAuthorizedName = agentAuthorizedName;
    }

    public String getAgentLinkName() {
        return agentLinkName;
    }

    public void setAgentLinkName(String agentLinkName) {
        this.agentLinkName = agentLinkName;
    }

    public String getTopAgent() {
        return topAgent;
    }

    public void setTopAgent(String topAgent) {
        this.topAgent = topAgent;
    }

    public Integer getLinkLevel() {
        return linkLevel;
    }

    public void setLinkLevel(Integer linkLevel) {
        this.linkLevel = linkLevel;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }
}
