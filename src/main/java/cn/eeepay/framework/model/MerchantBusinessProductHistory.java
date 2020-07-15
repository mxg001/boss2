package cn.eeepay.framework.model;

import java.util.Date;
/**
 * 商户业务产品历史表
 * @author Administrator
 *
 */
public class MerchantBusinessProductHistory {
	 private Long id;	 
	 private String sourceBpId;
	 private String newBpId;
	 private String operationType;
	 private String operationPersonType;
	 private Date createTime;
	 private String operationPersonNo;
	 private String merchantNo;
	 
	 private String bpName1;
	 public String getBpName1() {
		return bpName1;
	}
	public void setBpName1(String bpName1) {
		this.bpName1 = bpName1;
	}
	public String getBpName2() {
		return bpName2;
	}
	public void setBpName2(String bpName2) {
		this.bpName2 = bpName2;
	}
	private String bpName2;
	 private String operationPerson;
	 

	
	public String getOperationPerson() {
		return operationPerson;
	}
	public void setOperationPerson(String operationPerson) {
		this.operationPerson = operationPerson;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSourceBpId() {
		return sourceBpId;
	}
	public void setSourceBpId(String sourceBpId) {
		this.sourceBpId = sourceBpId;
	}
	public String getNewBpId() {
		return newBpId;
	}
	public void setNewBpId(String newBpId) {
		this.newBpId = newBpId;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getOperationPersonType() {
		return operationPersonType;
	}
	public void setOperationPersonType(String operationPersonType) {
		this.operationPersonType = operationPersonType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getOperationPersonNo() {
		return operationPersonNo;
	}
	public void setOperationPersonNo(String operationPersonNo) {
		this.operationPersonNo = operationPersonNo;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	 
	 


}
