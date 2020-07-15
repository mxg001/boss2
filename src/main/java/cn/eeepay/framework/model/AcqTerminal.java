package cn.eeepay.framework.model;

import java.util.Date;
/**
 * table Acq_terminal 
 * desc 收单机构终端
 * @author thj
 *
 */
public class AcqTerminal {
    private Integer id;

    private String acqOrgId;

    private String acqMerchantNo;

    private String acqTerminalNo;

    private String batchNo;

    private String serialNo;

    private String lmkZmk;

    private String lmkZmkCv;

    private String lmkZpk;

    private String lmkZpkCv;

    private String lmkZak;

    private String lmkZakCv;

    private String lmkZdk;

    private String lmkZdkCv;

    private String workKey;

    private Date lastUpdateTime;

    private Date lastUsedTime;

    private Integer status;

    private Integer locked;

    private String lockedMsg;

    private Date lockedTime;

    private Date createTime;

    private String createPerson;
    
    private String acqMerchantName;
    
    //代理商ID
    private String agentNo;
    
    private String agentName;
    
    //实体商户
    private String merchantNo;
    
    private String merchantName;
    
    //是否大小套
    private String largeSmallFlag;
    
    //收单机构名称
    private String acqName;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAcqOrgId() {
        return acqOrgId;
    }

    public void setAcqOrgId(String acqOrgId) {
        this.acqOrgId = acqOrgId == null ? null : acqOrgId.trim();
    }

    public String getAcqMerchantNo() {
        return acqMerchantNo;
    }

    public void setAcqMerchantNo(String acqMerchantNo) {
        this.acqMerchantNo = acqMerchantNo == null ? null : acqMerchantNo.trim();
    }

    public String getAcqTerminalNo() {
        return acqTerminalNo;
    }

    public void setAcqTerminalNo(String acqTerminalNo) {
        this.acqTerminalNo = acqTerminalNo == null ? null : acqTerminalNo.trim();
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo == null ? null : serialNo.trim();
    }

    public String getLmkZmk() {
        return lmkZmk;
    }

    public void setLmkZmk(String lmkZmk) {
        this.lmkZmk = lmkZmk == null ? null : lmkZmk.trim();
    }

    public String getLmkZmkCv() {
        return lmkZmkCv;
    }

    public void setLmkZmkCv(String lmkZmkCv) {
        this.lmkZmkCv = lmkZmkCv == null ? null : lmkZmkCv.trim();
    }

    public String getLmkZpk() {
        return lmkZpk;
    }

    public void setLmkZpk(String lmkZpk) {
        this.lmkZpk = lmkZpk == null ? null : lmkZpk.trim();
    }

    public String getLmkZpkCv() {
        return lmkZpkCv;
    }

    public void setLmkZpkCv(String lmkZpkCv) {
        this.lmkZpkCv = lmkZpkCv == null ? null : lmkZpkCv.trim();
    }

    public String getLmkZak() {
        return lmkZak;
    }

    public void setLmkZak(String lmkZak) {
        this.lmkZak = lmkZak == null ? null : lmkZak.trim();
    }

    public String getLmkZakCv() {
        return lmkZakCv;
    }

    public void setLmkZakCv(String lmkZakCv) {
        this.lmkZakCv = lmkZakCv == null ? null : lmkZakCv.trim();
    }

    public String getLmkZdk() {
        return lmkZdk;
    }

    public void setLmkZdk(String lmkZdk) {
        this.lmkZdk = lmkZdk == null ? null : lmkZdk.trim();
    }

    public String getLmkZdkCv() {
        return lmkZdkCv;
    }

    public void setLmkZdkCv(String lmkZdkCv) {
        this.lmkZdkCv = lmkZdkCv == null ? null : lmkZdkCv.trim();
    }

    public String getWorkKey() {
        return workKey;
    }

    public void setWorkKey(String workKey) {
        this.workKey = workKey == null ? null : workKey.trim();
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getLastUsedTime() {
        return lastUsedTime;
    }

    public void setLastUsedTime(Date lastUsedTime) {
        this.lastUsedTime = lastUsedTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLocked() {
        return locked;
    }

    public void setLocked(Integer locked) {
        this.locked = locked;
    }

    public String getLockedMsg() {
        return lockedMsg;
    }

    public void setLockedMsg(String lockedMsg) {
        this.lockedMsg = lockedMsg == null ? null : lockedMsg.trim();
    }

    public Date getLockedTime() {
        return lockedTime;
    }

    public void setLockedTime(Date lockedTime) {
        this.lockedTime = lockedTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson == null ? null : createPerson.trim();
    }

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getLargeSmallFlag() {
		return largeSmallFlag;
	}

	public void setLargeSmallFlag(String largeSmallFlag) {
		this.largeSmallFlag = largeSmallFlag;
	}

	public String getAcqMerchantName() {
		return acqMerchantName;
	}

	public void setAcqMerchantName(String acqMerchantName) {
		this.acqMerchantName = acqMerchantName;
	}

	public String getAcqName() {
		return acqName;
	}

	public void setAcqName(String acqName) {
		this.acqName = acqName;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
}