package cn.eeepay.framework.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Created by Administrator on 2018/1/12/012.
 * @author  liuks
 * 预警统计查询交易数
 */
public class TimingProduce {

    private Integer total;//笔数

    private Integer acqOrgId;//收单机构ID

    private  String acqName;//收单服务中文名

    private String acqEnname;//收单服务英文名

    private Integer acqServiceId;//收单服务id

    private String serviceName;//收单服务名

    private String transStatus;//交易状态

    private Integer outServiceId;//出款服务id

    private String outServiceName;//出款服务名称

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private String paymentStatus;//出款状态 '1',结算中,'2'结算失败

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getAcqOrgId() {
        return acqOrgId;
    }

    public void setAcqOrgId(Integer acqOrgId) {
        this.acqOrgId = acqOrgId;
    }

    public String getAcqName() {
        return acqName;
    }

    public void setAcqName(String acqName) {
        this.acqName = acqName;
    }

    public String getAcqEnname() {
        return acqEnname;
    }

    public void setAcqEnname(String acqEnname) {
        this.acqEnname = acqEnname;
    }

    public Integer getAcqServiceId() {
        return acqServiceId;
    }

    public void setAcqServiceId(Integer acqServiceId) {
        this.acqServiceId = acqServiceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public Integer getOutServiceId() {
        return outServiceId;
    }

    public void setOutServiceId(Integer outServiceId) {
        this.outServiceId = outServiceId;
    }

    public String getOutServiceName() {
        return outServiceName;
    }

    public void setOutServiceName(String outServiceName) {
        this.outServiceName = outServiceName;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
