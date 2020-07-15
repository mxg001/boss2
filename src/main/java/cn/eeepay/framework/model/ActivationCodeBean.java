package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 激活码
 * Created by 666666 on 2017/10/26.
 */
public class ActivationCodeBean {
    private long id;
    private String uuidCode;
    private String unifiedMerchantNo;
    private String unifiedMerchantName;
    private String oneAgentNo;
    private String oneAgentName;
    private String agentNo;
    private String parentId;
    private String agentName;
    private String agentNode;
    private String status;
    private Date activateTime;
    private Date createTime;
    private Date updateTime;

    private String minId;
    private String maxId;
    private String startActivateTime;
    private String endActivateTime;

    private String codeType;//激活码类型,repay: 信用卡超级还款,nfc:超级碰一碰

    public String getParentId() {
        return parentId;
    }

    public ActivationCodeBean setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    public String getMinId() {
        return minId;
    }

    public void setMinId(String minId) {
        this.minId = minId;
    }

    public String getMaxId() {
        return maxId;
    }

    public void setMaxId(String maxId) {
        this.maxId = maxId;
    }

    public String  getStartActivateTime() {
        return startActivateTime;
    }

    public void setStartActivateTime(String startActivateTime) {
        this.startActivateTime = startActivateTime;
    }

    public String getEndActivateTime() {
        return endActivateTime;
    }

    public void setEndActivateTime(String endActivateTime) {
        this.endActivateTime = endActivateTime;
    }

    public String getUnifiedMerchantName() {
        return unifiedMerchantName;
    }

    public void setUnifiedMerchantName(String unifiedMerchantName) {
        this.unifiedMerchantName = unifiedMerchantName;
    }

    public String getOneAgentNo() {
        return oneAgentNo;
    }

    public void setOneAgentNo(String oneAgentNo) {
        this.oneAgentNo = oneAgentNo;
    }

    public String getOneAgentName() {
        return oneAgentName;
    }

    public void setOneAgentName(String oneAgentName) {
        this.oneAgentName = oneAgentName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuidCode() {
        return uuidCode;
    }

    public void setUuidCode(String uuidCode) {
        this.uuidCode = uuidCode;
    }

    public String getUnifiedMerchantNo() {
        return unifiedMerchantNo;
    }

    public void setUnifiedMerchantNo(String unifiedMerchantNo) {
        this.unifiedMerchantNo = unifiedMerchantNo;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getActivateTime() {
        return activateTime;
    }

    public void setActivateTime(Date activateTime) {
        this.activateTime = activateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }
}
