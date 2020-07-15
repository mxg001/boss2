package cn.eeepay.framework.model;

import java.util.Date;

/**
 * @author ：quanhz
 * @date ：Created in 2020/3/18 11:03
 */
public class CommonCode {

    private Long id;
    private String agentNo;
    private String agentName;
    private String commonCodeUrl;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getCommonCodeUrl() {
        return commonCodeUrl;
    }

    public void setCommonCodeUrl(String commonCodeUrl) {
        this.commonCodeUrl = commonCodeUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
