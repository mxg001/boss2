package cn.eeepay.framework.model;

import java.util.Date;

public class IndustrySwitch {
	private Long id;
	private String startTime;
	private String endTime;
	private String acqMerchantType;
	private Date createTime;
	private String acqMerchantTypeName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getAcqMerchantType() {
		return acqMerchantType;
	}

	public void setAcqMerchantType(String acqMerchantType) {
		this.acqMerchantType = acqMerchantType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAcqMerchantTypeName() {
		return acqMerchantTypeName;
	}

	public void setAcqMerchantTypeName(String acqMerchantTypeName) {
		this.acqMerchantTypeName = acqMerchantTypeName;
	}

}
