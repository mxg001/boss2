package cn.eeepay.framework.model;

public class YfbChannelSyn {

    private Integer id;
    private String merchantNo;
    private String channelCode;
    private String channelName;
    private String reportStatus;
    private String effectiveStatus;
    private String zqMerchantNo;

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

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getEffectiveStatus() {
        return effectiveStatus;
    }

    public void setEffectiveStatus(String effectiveStatus) {
        this.effectiveStatus = effectiveStatus;
    }

    public String getZqMerchantNo() {
        return zqMerchantNo;
    }

    public void setZqMerchantNo(String zqMerchantNo) {
        this.zqMerchantNo = zqMerchantNo;
    }
}
