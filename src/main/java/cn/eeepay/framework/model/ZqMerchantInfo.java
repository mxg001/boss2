package cn.eeepay.framework.model;

import java.util.Date;

/**
 * Created by Administrator on 2017/5/19.
 */
public class ZqMerchantInfo {

    private Long id;

    private String merchantNo;

    private Long bpId;

    private Long mbpId;

    private Long merServiceId;

    private String syncStatus;

    private String effectiveStatus;

    private String channelCode;

    private String unionpayMerNo;

    private String terminalNo;

    private String syncRemark;

    private Date createTime;

    private Date updateTime;

    private String operator;

    private String tradeType;

    private String mobilephone;

    private String merchantName;

    private String agentName;

    private String bpName;

    private String operatorName;

    private String reportStatus;
    
    private String regid;//统一社会信用代码
    private String partnerSystemId;//'合作方产生的唯一流水号(银盛),其他为空
    private String  auditFlag;//银盛返回是否需要他方审核
    private String  trmSn;//终端序列号
    private String  bodyId;//机身号
    
    private String miMobilephone;
    

    
    
    public String getMiMobilephone() {
		return miMobilephone;
	}

	public void setMiMobilephone(String miMobilephone) {
		this.miMobilephone = miMobilephone;
	}

	public String getRegid() {
		return regid;
	}

	public void setRegid(String regid) {
		this.regid = regid;
	}

	public String getPartnerSystemId() {
		return partnerSystemId;
	}

	public void setPartnerSystemId(String partnerSystemId) {
		this.partnerSystemId = partnerSystemId;
	}

	public String getAuditFlag() {
		return auditFlag;
	}

	public void setAuditFlag(String auditFlag) {
		this.auditFlag = auditFlag;
	}

	public String getTrmSn() {
		return trmSn;
	}

	public void setTrmSn(String trmSn) {
		this.trmSn = trmSn;
	}

	public String getBodyId() {
		return bodyId;
	}

	public void setBodyId(String bodyId) {
		this.bodyId = bodyId;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Long getMbpId() {
        return mbpId;
    }

    public void setMbpId(Long mbpId) {
        this.mbpId = mbpId;
    }

    public Long getMerServiceId() {
        return merServiceId;
    }

    public void setMerServiceId(Long merServiceId) {
        this.merServiceId = merServiceId;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getEffectiveStatus() {
        return effectiveStatus;
    }

    public void setEffectiveStatus(String effectiveStatus) {
        this.effectiveStatus = effectiveStatus;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getUnionpayMerNo() {
        return unionpayMerNo;
    }

    public void setUnionpayMerNo(String unionpayMerNo) {
        this.unionpayMerNo = unionpayMerNo;
    }

    public String getSyncRemark() {
        return syncRemark;
    }

    public void setSyncRemark(String syncRemark) {
        this.syncRemark = syncRemark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
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

    public String getBpName() {
        return bpName;
    }

    public void setBpName(String bpName) {
        this.bpName = bpName;
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public Long getBpId() {
        return bpId;
    }

    public void setBpId(Long bpId) {
        this.bpId = bpId;
    }

    public ZqMerchantInfo(String merchantNo, Long merServiceId, String syncStatus, String channelCode, String unionpayMerNo, String terminalNo) {
        this.merchantNo = merchantNo;
        this.merServiceId = merServiceId;
        this.syncStatus = syncStatus;
        this.channelCode = channelCode;
        this.unionpayMerNo = unionpayMerNo;
        this.terminalNo = terminalNo;
    }
    public ZqMerchantInfo(){

    }
}
