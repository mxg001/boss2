package cn.eeepay.framework.model;

import java.util.Date;

/**
 * table acq_merchant_info_log
 * 收单商户进件
 * @author zxs
 *
 */
public class AcqInMerchantRecord {
	
	private Long acqMerchantInfoId;
	
	//审核状态 1.正常 2.审核通过 3 审核不通过
	private Integer auditStatus;
	private String examinationOpinions;
	private String operator;
	private Date createTime;
	
	private String realName;
	
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public AcqInMerchantRecord(Long acqMerchantInfoId, Integer auditStatus, String examinationOpinions, String operator,
			Date createTime) {
		this.acqMerchantInfoId = acqMerchantInfoId;
		this.auditStatus = auditStatus;
		this.examinationOpinions = examinationOpinions;
		this.operator = operator;
		this.createTime = createTime;
	}
	public AcqInMerchantRecord() {
		super();
	}
	public Long getAcqMerchantInfoId() {
		return acqMerchantInfoId;
	}
	public void setAcqMerchantInfoId(Long acqMerchantInfoId) {
		this.acqMerchantInfoId = acqMerchantInfoId;
	}
	
	
	public Integer getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getExaminationOpinions() {
		return examinationOpinions;
	}
	public void setExaminationOpinions(String examinationOpinions) {
		this.examinationOpinions = examinationOpinions;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
}