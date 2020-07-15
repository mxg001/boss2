package cn.eeepay.framework.model;

/**
 * 超级还通道
 * @author MXG
 * create 2018/08/29
 */
public class RepayChannel {
    private String id;
    private String channelCode;
    private String channelName;
    private String channelStatus;
    private String allowBeginTime;//还款支持开始时间
    private String allowEndTime;//还款支持结束时间
    private String allowQuickMinAmount;//单笔交易最小金额
    private String allowQuickMaxAmount;//单笔交易最大金额
    private String allowSplitMinute;//计划间隔分钟数
    private String repayType;
    private Integer percent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelStatus() {
        return channelStatus;
    }

    public void setChannelStatus(String channelStatus) {
        this.channelStatus = channelStatus;
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

    public String getAllowQuickMinAmount() {
        return allowQuickMinAmount;
    }

    public void setAllowQuickMinAmount(String allowQuickMinAmount) {
        this.allowQuickMinAmount = allowQuickMinAmount;
    }

    public String getAllowQuickMaxAmount() {
        return allowQuickMaxAmount;
    }

    public void setAllowQuickMaxAmount(String allowQuickMaxAmount) {
        this.allowQuickMaxAmount = allowQuickMaxAmount;
    }

    public String getAllowSplitMinute() {
        return allowSplitMinute;
    }

    public void setAllowSplitMinute(String allowSplitMinute) {
        this.allowSplitMinute = allowSplitMinute;
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }
}
