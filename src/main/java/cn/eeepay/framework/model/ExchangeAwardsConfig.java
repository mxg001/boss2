package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class ExchangeAwardsConfig {
    private Integer id;
    private String funcCode;
    private String awardDesc;
    private String awardName;
    private Integer couponId;
    private Integer effectDays;
    private String awardHit;
    private BigDecimal money;
    private String awardPic;
    private Integer awardType;
    private Integer status;
    private Date create_time;
    private String operator;

    private String activityFirst;
    private String cancelVerificationCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    public String getAwardDesc() {
        return awardDesc;
    }

    public void setAwardDesc(String awardDesc) {
        this.awardDesc = awardDesc;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public Integer getEffectDays() {
        return effectDays;
    }

    public void setEffectDays(Integer effectDays) {
        this.effectDays = effectDays;
    }

    public String getAwardHit() {
        return awardHit;
    }

    public void setAwardHit(String awardHit) {
        this.awardHit = awardHit;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getAwardPic() {
        return awardPic;
    }

    public void setAwardPic(String awardPic) {
        this.awardPic = awardPic;
    }

    public Integer getAwardType() {
        return awardType;
    }

    public void setAwardType(Integer awardType) {
        this.awardType = awardType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getActivityFirst() {
        return activityFirst;
    }

    public void setActivityFirst(String activityFirst) {
        this.activityFirst = activityFirst;
    }

    public String getCancelVerificationCode() {
        return cancelVerificationCode;
    }

    public void setCancelVerificationCode(String cancelVerificationCode) {
        this.cancelVerificationCode = cancelVerificationCode;
    }
}
