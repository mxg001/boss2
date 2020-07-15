package cn.eeepay.framework.model;

import java.util.Date;

/**
 * @author zxs 
 * 预警阈值设置
 * 对应表 warning_set
 */
public class WarningSet {
	//类型 出款类型   1出款类型  2收单 默认1  
	public static final Integer warningSetTypeOut = 1;
	public static final Integer warningSetTypeIn = 2;
	
    private Integer id;
    private Integer serviceId;//服务ID
    private String serviceName;//名称
    private Integer waringCycle;//预警周期
	private Integer warningCycle;// 预警周期 收单
    private Integer exceptionNumber;//异常笔数
    private Integer failurWaringCycle;//失败预警周期
    private Integer failurExceptionNumber;//失败异常笔数
    private Integer status = warningSetTypeOut;
    private Integer warnTimeType;//预警时间类型，1：全天，2：个性化
    private String warnStartTime;//预警开始时间
    private String warnEndTime;//预警结束时间
    private Integer warnStatus;//预警状态，0：关闭，1：打开，2：删除

	private Integer acqId;//收单机构ID
	private String acqEnname;//收单机构名称
	private String serviceType;//服务类型
	private String warningCycleStr;//预警周期字符串
	private String failurWarningCycleStr;//预警周期字符串
	private String warnTimeStr;//预警时间
	private Date warnDate;//预警时间

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Integer getWaringCycle() {
		return waringCycle;
	}
	public void setWaringCycle(Integer waringCycle) {
		this.waringCycle = waringCycle;
	}
	public Integer getExceptionNumber() {
		return exceptionNumber;
	}
	public void setExceptionNumber(Integer exceptionNumber) {
		this.exceptionNumber = exceptionNumber;
	}
	public Integer getFailurWaringCycle() {
		return failurWaringCycle;
	}
	public void setFailurWaringCycle(Integer failurWaringCycle) {
		this.failurWaringCycle = failurWaringCycle;
	}
	public Integer getFailurExceptionNumber() {
		return failurExceptionNumber;
	}
	public void setFailurExceptionNumber(Integer failurExceptionNumber) {
		this.failurExceptionNumber = failurExceptionNumber;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getWarningCycle() {
		return warningCycle;
	}
	public void setWarningCycle(Integer warningCycle) {
		this.warningCycle = warningCycle;
	}

	public Integer getWarnTimeType() {
		return warnTimeType;
	}

	public void setWarnTimeType(Integer warnTimeType) {
		this.warnTimeType = warnTimeType;
	}

	public static Integer getWarningSetTypeOut() {
		return warningSetTypeOut;
	}

	public static Integer getWarningSetTypeIn() {
		return warningSetTypeIn;
	}

	public String getWarnStartTime() {
		return warnStartTime;
	}

	public void setWarnStartTime(String warnStartTime) {
		this.warnStartTime = warnStartTime;
	}

	public String getWarnEndTime() {
		return warnEndTime;
	}

	public void setWarnEndTime(String warnEndTime) {
		this.warnEndTime = warnEndTime;
	}

	public Integer getWarnStatus() {
		return warnStatus;
	}

	public void setWarnStatus(Integer warnStatus) {
		this.warnStatus = warnStatus;
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

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getWarningCycleStr() {
		return warningCycleStr;
	}

	public void setWarningCycleStr(String warningCycleStr) {
		this.warningCycleStr = warningCycleStr;
	}

	public String getWarnTimeStr() {
		return warnTimeStr;
	}

	public void setWarnTimeStr(String warnTimeStr) {
		this.warnTimeStr = warnTimeStr;
	}

	public Date getWarnDate() {
		return warnDate;
	}

	public void setWarnDate(Date warnDate) {
		this.warnDate = warnDate;
	}

	public String getFailurWarningCycleStr() {
		return failurWarningCycleStr;
	}

	public void setFailurWarningCycleStr(String failurWarningCycleStr) {
		this.failurWarningCycleStr = failurWarningCycleStr;
	}

	public String toString() {
		return "WarningSet [id=" + id + ", serviceId=" + serviceId + ", serviceName=" + serviceName + ", waringCycle="
				+ waringCycle + ", exceptionNumber=" + exceptionNumber + ", failurWaringCycle=" + failurWaringCycle
				+ ", failurExceptionNumber=" + failurExceptionNumber + ", status=" + status + "]";
	}
}
