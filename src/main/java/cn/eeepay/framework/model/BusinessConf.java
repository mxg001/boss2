package cn.eeepay.framework.model;

//组织升级业务功能点配置
public class BusinessConf {

    private Long id;
    private String businessName;        //业务名称
    private int isCheck;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }
}
