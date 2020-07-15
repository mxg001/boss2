package cn.eeepay.framework.model;

public class ServiceInfoBean {
    private String serviceId;
    private String serviceType;
    private String fixedRate;
    private String fixedQuota;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getFixedRate() {
        return fixedRate;
    }

    public void setFixedRate(String fixedRate) {
        this.fixedRate = fixedRate;
    }

    public String getFixedQuota() {
        return fixedQuota;
    }

    public void setFixedQuota(String fixedQuota) {
        this.fixedQuota = fixedQuota;
    }

    @Override
    public String toString() {
        return "ServiceInfoBean{" +
                "serviceId='" + serviceId + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", fixedRate='" + fixedRate + '\'' +
                ", fixedQuota='" + fixedQuota + '\'' +
                '}';
    }
}
