package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 增值服务
 *
 * @author Administrator
 */
public class VasServiceInfo {

    private int id;

    private String vasServiceNo;//增值服务编号

    private String vasServiceName;//增值服务名称

    private String sourceServiceNo;//服务来源编号

    private Integer serviceSource;//服务来源,1:功能管理(function_manage.function_number),2:活动管理(coupon_activity_info.activetiy_code)

    private Date createTime;

    private Date lastUpdateTime;

    private String operator;


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

    public String getVasServiceName() {
        return vasServiceName;
    }

    public void setVasServiceName(String vasServiceName) {
        this.vasServiceName = vasServiceName;
    }

    public String getSourceServiceNo() {
        return sourceServiceNo;
    }

    public void setSourceServiceNo(String sourceServiceNo) {
        this.sourceServiceNo = sourceServiceNo;
    }

    public Integer getServiceSource() {
        return serviceSource;
    }

    public void setServiceSource(Integer serviceSource) {
        this.serviceSource = serviceSource;
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
}
