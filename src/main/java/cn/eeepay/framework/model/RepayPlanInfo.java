package cn.eeepay.framework.model;

/**
 * 超级还计划类型
 * @author MXG
 * create 2018/08/29
 */
public class RepayPlanInfo {
    private String id;
    private String planType;//计划类型 1-分期还款 2-全额还款  3-完美还款
    private String planName;
    private String status;//状态 0-关闭 1-开启
    private String allowBeginTime;//计划服务允许开始时间
    private String allowEndTime;//计划服务允许结束时间
    private String allowDayMinNum;//计划每日还款最小笔数
    private String allowDayMaxNum;//计划每日还款最大笔数
    private Integer allowRepayMinAmount;//计划服务允许还款最小金额（前端显示为整数）
    private Integer allowRepayMaxAmount;//计划服务允许还款最大金额（前端显示为整数）
    private Integer allowFirstMinAmount;//首笔交易金额最小值（前端显示为整数）
    private Integer allowFirstMaxAmount;//首笔交易金额最大值（前端显示为整数）
    private String closeTip;//关闭提示语

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAllowBeginTime() {
        return allowBeginTime;
    }

    public void setAllowBeginTime(String allowBeginTime) {
        this.allowBeginTime = allowBeginTime;
    }

    public String getAllowEndTime() {
        return allowEndTime;
    }

    public void setAllowEndTime(String allowEndTime) {
        this.allowEndTime = allowEndTime;
    }

    public String getAllowDayMinNum() {
        return allowDayMinNum;
    }

    public void setAllowDayMinNum(String allowDayMinNum) {
        this.allowDayMinNum = allowDayMinNum;
    }

    public String getAllowDayMaxNum() {
        return allowDayMaxNum;
    }

    public void setAllowDayMaxNum(String allowDayMaxNum) {
        this.allowDayMaxNum = allowDayMaxNum;
    }

    public Integer getAllowRepayMinAmount() {
        return allowRepayMinAmount;
    }

    public void setAllowRepayMinAmount(Integer allowRepayMinAmount) {
        this.allowRepayMinAmount = allowRepayMinAmount;
    }

    public Integer getAllowRepayMaxAmount() {
        return allowRepayMaxAmount;
    }

    public void setAllowRepayMaxAmount(Integer allowRepayMaxAmount) {
        this.allowRepayMaxAmount = allowRepayMaxAmount;
    }

    public Integer getAllowFirstMinAmount() {
        return allowFirstMinAmount;
    }

    public void setAllowFirstMinAmount(Integer allowFirstMinAmount) {
        this.allowFirstMinAmount = allowFirstMinAmount;
    }

    public Integer getAllowFirstMaxAmount() {
        return allowFirstMaxAmount;
    }

    public void setAllowFirstMaxAmount(Integer allowFirstMaxAmount) {
        this.allowFirstMaxAmount = allowFirstMaxAmount;
    }

    public String getCloseTip() {
        return closeTip;
    }

    public void setCloseTip(String closeTip) {
        this.closeTip = closeTip;
    }
}
