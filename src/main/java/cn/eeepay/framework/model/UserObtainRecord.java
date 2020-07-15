package cn.eeepay.framework.model;

import cn.eeepay.framework.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tans
 * @data 2017-12-6
 * super.user_obtain_record
 * 分润提现记录表
 */
public class UserObtainRecord {
    private Long id;

    private String userCode;//用户编码

    private String obtainNo;//提现订单号

    private String orderType;//提现订单类型;1 用户分润提现

    private BigDecimal obtainAmount;//提现金额

    private BigDecimal realObtainAmount;//实际提现金额

    private BigDecimal obtainFee;//提现手续费

    private String status;//提现状态:1未提交；2结算中；3已结算 4结算失败

    private Date createDate;//提现时间

    private String remark;//备注

    private Date updateDate;//修改时间

    private String createDateStr;
    private String createDateStart;
    private String createDateEnd;
    private String userName;
    private String nickName;
    private String phone;
    private BigDecimal obtainAmountStart;//提现金额起
    private BigDecimal obtainAmountEnd;//提现金额止

    private String userType;//用户身份
    private Long orgId;//组织ID
    private String orgName;//组织名称

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getObtainNo() {
        return obtainNo;
    }

    public void setObtainNo(String obtainNo) {
        this.obtainNo = obtainNo == null ? null : obtainNo.trim();
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }

    public BigDecimal getObtainAmount() {
        return obtainAmount != null ? obtainAmount : new BigDecimal(0);
    }

    public void setObtainAmount(BigDecimal obtainAmount) {
        this.obtainAmount = obtainAmount;
    }

    public BigDecimal getObtainFee() {
        return obtainFee != null ? obtainFee : new BigDecimal(0);
    }

    public void setObtainFee(BigDecimal obtainFee) {
        this.obtainFee = obtainFee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateDateStr() {
        return createDate != null ? DateUtil.getLongFormatDate(createDate) : "";
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public String getCreateDateStart() {
        return createDateStart;
    }

    public void setCreateDateStart(String createDateStart) {
        this.createDateStart = createDateStart;
    }

    public String getCreateDateEnd() {
        return createDateEnd;
    }

    public void setCreateDateEnd(String createDateEnd) {
        this.createDateEnd = createDateEnd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getObtainAmountStart() {
        return obtainAmountStart;
    }

    public void setObtainAmountStart(BigDecimal obtainAmountStart) {
        this.obtainAmountStart = obtainAmountStart;
    }

    public BigDecimal getObtainAmountEnd() {
        return obtainAmountEnd;
    }

    public void setObtainAmountEnd(BigDecimal obtainAmountEnd) {
        this.obtainAmountEnd = obtainAmountEnd;
    }

    public BigDecimal getRealObtainAmount() {
        return realObtainAmount;
    }

    public void setRealObtainAmount(BigDecimal realObtainAmount) {
        this.realObtainAmount = realObtainAmount;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}