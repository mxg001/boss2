package cn.eeepay.framework.model;


public class IndustryMcc {

    private Integer id;
    private String channelCode;
    private String merType;
    private String industryLevel;
    private String industryName1;
    private String industryName;
    private String mcc;
    private Integer parentId;


    public String getIndustryName1() {
        return industryName1;
    }

    public void setIndustryName1(String industryName1) {
        this.industryName1 = industryName1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getMerType() {
        return merType;
    }

    public void setMerType(String merType) {
        this.merType = merType;
    }

    public String getIndustryLevel() {
        return industryLevel;
    }

    public void setIndustryLevel(String industryLevel) {
        this.industryLevel = industryLevel;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }


    @Override
    public String toString() {
        return "IndustryMcc{" +
                "id=" + id +
                ", channelCode='" + channelCode + '\'' +
                ", merType='" + merType + '\'' +
                ", industryLevel='" + industryLevel + '\'' +
                ", industryName1='" + industryName1 + '\'' +
                ", industryName='" + industryName + '\'' +
                ", mcc='" + mcc + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}
