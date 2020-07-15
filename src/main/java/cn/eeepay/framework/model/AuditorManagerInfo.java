package cn.eeepay.framework.model;

import java.util.Date;

/**
 * audior_manager
 * @author tans
 */
public class AuditorManagerInfo implements Cloneable {

	private Long id;

	private String auditorId;

	private String bpId;

	private Integer status;

	private String bpName;

	private String userName;

	private Integer successNum;

	private Integer failureNum;

	private Integer stayNum;

	private Integer sum;

	private Date createTime;

	private Date auditorTime;

	private String describes;

	private String itemNo;

	private String merchantName;

	private String agentName;

	private String auditorStatus;

	private String oneAgentNo;

	private String merchantNo;

	private String saleName;

	private String openStatus;

	private String mbpId;

	public String getMbpId() {
		return mbpId;
	}

	public void setMbpId(String mbpId) {
		this.mbpId = mbpId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(String auditorId) {
		this.auditorId = auditorId;
	}

	public String getBpId() {
		return bpId;
	}

	public void setBpId(String bpId) {
		this.bpId = bpId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getBpName() {
		return bpName;
	}

	public void setBpName(String bpName) {
		this.bpName = bpName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(Integer successNum) {
		this.successNum = successNum;
	}

	public Integer getFailureNum() {
		return failureNum;
	}

	public void setFailureNum(Integer failureNum) {
		this.failureNum = failureNum;
	}

	public Integer getStayNum() {
		return stayNum;
	}

	public void setStayNum(Integer stayNum) {
		this.stayNum = stayNum;
	}

	public Integer getSum() {
		return sum;
	}

	public void setSum(Integer sum) {
		this.sum = sum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getAuditorTime() {
		return auditorTime;
	}

	public void setAuditorTime(Date auditorTime) {
		this.auditorTime = auditorTime;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAuditorStatus() {
		return auditorStatus;
	}

	public void setAuditorStatus(String auditorStatus) {
		this.auditorStatus = auditorStatus;
	}

	public String getDescribes() {
		return describes;
	}

	public void setDescribes(String describes) {
		this.describes = describes;
	}

	public String getOneAgentNo() {
		return oneAgentNo;
	}

	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}

	public String getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}

	@Override
	public AuditorManagerInfo clone() {
		AuditorManagerInfo o = null;
		try {
			o = (AuditorManagerInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

}
