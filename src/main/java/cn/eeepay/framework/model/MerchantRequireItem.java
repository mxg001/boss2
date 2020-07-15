package cn.eeepay.framework.model;

import java.util.Date;

/**
 * table  merchant_require_item
 * desc  商户进件要求项资料表
 */
public class MerchantRequireItem {
    private Long id;

    private String merchantNo;

    private String mriId;

    private String content;

    private String status;

    private Date auditTime;
    
    //进件要求的名字
    private String itemName;
    
    private String checkStatus;
    
    private String remark;
    
    private String exampleType;
    
    private String photo;
    
    //页面传来个状态
    private String aStatus;
    
    private String merBpId;
    
    private String logContent;

	private IndustryMcc industryMcc;

	public IndustryMcc getIndustryMcc() {
		return industryMcc;
	}

	public void setIndustryMcc(IndustryMcc industryMcc) {
		this.industryMcc = industryMcc;
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
        this.merchantNo = merchantNo == null ? null : merchantNo.trim();
    }

    public String getMriId() {
        return mriId;
    }

    public void setMriId(String mriId) {
        this.mriId = mriId == null ? null : mriId.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExampleType() {
		return exampleType;
	}

	public void setExampleType(String exampleType) {
		this.exampleType = exampleType;
	}

	public String getaStatus() {
		return aStatus;
	}

	public void setaStatus(String aStatus) {
		this.aStatus = aStatus;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getMerBpId() {
		return merBpId;
	}

	public void setMerBpId(String merBpId) {
		this.merBpId = merBpId;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getLogContent() {
		return logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}
}