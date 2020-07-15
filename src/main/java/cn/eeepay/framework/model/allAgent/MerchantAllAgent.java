package cn.eeepay.framework.model.allAgent;

import java.util.Date;

/**
 * @author  yyao
 * 超级盟主商户
 * 对应表 pa_mer_info
 */
public class MerchantAllAgent {

    private Integer id;
    private String merchantNo;
    private String agentNo;
    private String agentNode;
    private String userCode;
    private String userNode;
    private Date createTime;
    private String status;
    private String bindTer;
    private String mobilePhone;
    private String userId;

    private String merchantName;
    private String realName;
    private String mobile;
    private String oneUserCode;
    private String oneAgentNo;
    private String oneAgentName;
    private String brandCode;
    private String brandName;
    private String nickName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
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

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserNode() {
        return userNode;
    }

    public void setUserNode(String userNode) {
        this.userNode = userNode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBindTer() {
        return bindTer;
    }

    public void setBindTer(String bindTer) {
        this.bindTer = bindTer;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOneUserCode() {
        return oneUserCode;
    }

    public void setOneUserCode(String oneUserCode) {
        this.oneUserCode = oneUserCode;
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

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
