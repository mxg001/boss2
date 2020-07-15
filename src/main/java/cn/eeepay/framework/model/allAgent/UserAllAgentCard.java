package cn.eeepay.framework.model.allAgent;

import java.util.Date;

/**
 * Created by Administrator on 2018/7/13/013.
 * @author  liuks
 * 用户卡表
 * 对应表 pa_user_card
 */
public class UserAllAgentCard {

    private Integer id;
    private String userCode;//用户编号
    private String bankName;//开户名称
    private String bankBranchName;//开户名称
    private String account;//账号
    private String bankCode;//开户行
    private String mobile;//开户手机号
    private String cnaps;//联行号
    private String address;//开户地区
    private Integer isSettle;//是否结算卡
    private Integer status;//状态
    private Date createTime;//创建时间
    private Date lastUpdate;//修改时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCnaps() {
        return cnaps;
    }

    public void setCnaps(String cnaps) {
        this.cnaps = cnaps;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getIsSettle() {
        return isSettle;
    }

    public void setIsSettle(Integer isSettle) {
        this.isSettle = isSettle;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }
}
