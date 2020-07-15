package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 增值服务费率
 *
 * @author Administrator
 */
public class VasRate {

    private int id;

    private String vasServiceNo;//增值服务编号

    private String teamId;//组织ID
    private String teamName;//组织名称

    private String teamEntryId;//子级组织ID
    private String teamEntryName;//子级组织名称

    private String rateType;//费率类型:1-每笔固定金额，2-扣率
    private BigDecimal singleNumAmount;//每笔固定值
    private BigDecimal rate;//扣率
    private String showRate;//扣率显示
    private String merchantType;//商户类型,多个用英文逗号分隔
    private String merchantTypeName;//商户类型名称,多个用英文逗号分隔
    private String orderType;//订单类型,多个用英文逗号分隔
    private String orderTypeName;//订单类型名称,多个用英文逗号分隔
    private String serviceType;//交易服务类型,多个用英文逗号分隔
    private String serviceTypeName;//交易服务类型名称,多个用英文逗号分隔
    private Integer status;//状态：1开启，0关闭

    private Date createTime;

    private Date lastUpdateTime;

    private String operator;

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getMerchantTypeName() {
        return merchantTypeName;
    }

    public void setMerchantTypeName(String merchantTypeName) {
        this.merchantTypeName = merchantTypeName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getShowRate() {
        return showRate;
    }

    public void setShowRate(String showRate) {
        this.showRate = showRate;
    }

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVasServiceNo() {
        return vasServiceNo;
    }

    public void setVasServiceNo(String vasServiceNo) {
        this.vasServiceNo = vasServiceNo;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamEntryId() {
        return teamEntryId;
    }

    public void setTeamEntryId(String teamEntryId) {
        this.teamEntryId = teamEntryId;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public BigDecimal getSingleNumAmount() {
        return singleNumAmount;
    }

    public void setSingleNumAmount(BigDecimal singleNumAmount) {
        this.singleNumAmount = singleNumAmount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamEntryName() {
        return teamEntryName;
    }

    public void setTeamEntryName(String teamEntryName) {
        this.teamEntryName = teamEntryName;
    }

    @Override
    public String toString() {
        return "VasRate{" +
                "id=" + id +
                ", vasServiceNo='" + vasServiceNo + '\'' +
                ", teamId='" + teamId + '\'' +
                ", teamName='" + teamName + '\'' +
                ", teamEntryId='" + teamEntryId + '\'' +
                ", teamEntryName='" + teamEntryName + '\'' +
                ", rateType='" + rateType + '\'' +
                ", singleNumAmount=" + singleNumAmount +
                ", rate=" + rate +
                ", showRate='" + showRate + '\'' +
                ", orderType='" + orderType + '\'' +
                ", orderTypeName='" + orderTypeName + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", serviceTypeName='" + serviceTypeName + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", operator='" + operator + '\'' +
                '}';
    }
}
