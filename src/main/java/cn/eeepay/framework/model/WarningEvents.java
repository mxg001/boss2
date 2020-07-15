package cn.eeepay.framework.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Created by Administrator on 2018/1/10/010.
 * @author liuks
 * 预警事件实体
 * 对应表 warning_events
 */
public class WarningEvents {
    private Integer id;//预警事件ID

    private String eventNumber;//编号

    private Integer acqId;//收单机构id

    private String acqEnname;//收单机构英文名

    private String acqName;//收单机构中文名

    private Integer acqServiceId;//收单服务id

    private String serviceName;//收单服务名

    private Integer outServiceId;//出款服务id

    private String outServiceName;//出款服务名称

    private String taskName;//定时任务名称

    private String taskGroup;//定时任务组

    private String taskStatus;//定时任务状态

    private String message;//预警内容

    private Integer status;//来源系统:1-交易系统,2-出款系统,3-定时任务监控

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date smsTime;//短信最后一次发送时间

    private Integer smsCount;//短信发送次数


    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeBegin;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;

    private String transStatus;//交易异常状态集合

    private String transStatusNumber;//交易异常状态统计数量

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventNumber() {
        return eventNumber;
    }

    public void setEventNumber(String eventNumber) {
        this.eventNumber = eventNumber;
    }

    public Integer getAcqId() {
        return acqId;
    }

    public void setAcqId(Integer acqId) {
        this.acqId = acqId;
    }

    public String getAcqEnname() {
        return acqEnname;
    }

    public void setAcqEnname(String acqEnname) {
        this.acqEnname = acqEnname;
    }

    public String getAcqName() {
        return acqName;
    }

    public void setAcqName(String acqName) {
        this.acqName = acqName;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getSmsTime() {
        return smsTime;
    }

    public void setSmsTime(Date smsTime) {
        this.smsTime = smsTime;
    }

    public Integer getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(Integer smsCount) {
        this.smsCount = smsCount;
    }

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public String getTransStatusNumber() {
        return transStatusNumber;
    }

    public void setTransStatusNumber(String transStatusNumber) {
        this.transStatusNumber = transStatusNumber;
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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
}
