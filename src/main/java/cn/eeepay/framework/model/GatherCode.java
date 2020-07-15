package cn.eeepay.framework.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * table gather_code
 * desc 收款码管理
 * @author tans
 */
public class GatherCode {
    private Long id;

    private String sn;

    private String gatherCode;

    private String merchantNo;
    
    private Integer status;

    private Integer materialType;
    
    private Date createTime;
    
    private Date startTime;

    private String merchantName;
    
    private int number;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn == null ? null : sn.trim();
    }

    public String getGatherCode() {
        return gatherCode;
    }

    public void setGatherCode(String gatherCode) {
        this.gatherCode = gatherCode == null ? null : gatherCode.trim();
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo == null ? null : merchantNo.trim();
    }

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getMaterialType() {
		return materialType;
	}

	public void setMaterialType(Integer materialType) {
		this.materialType = materialType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
    
    
}