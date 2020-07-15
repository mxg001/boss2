package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class TransInfoPreFreezeLog {
	
    private Integer id;

    private String merchantNo;
    
    private String preFreezeNote;
    
    private Date operTime;

    private String operLog;

    private String operId;

    private String operName;
    
    //增加vo
    private BigDecimal preFreezeAmount;//预冻结金额

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getPreFreezeNote() {
		return preFreezeNote;
	}

	public void setPreFreezeNote(String preFreezeNote) {
		this.preFreezeNote = preFreezeNote;
	}

	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	public String getOperLog() {
		return operLog;
	}

	public void setOperLog(String operLog) {
		this.operLog = operLog;
	}

	public String getOperId() {
		return operId;
	}

	public void setOperId(String operId) {
		this.operId = operId;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public BigDecimal getPreFreezeAmount() {
		return preFreezeAmount;
	}

	public void setPreFreezeAmount(BigDecimal preFreezeAmount) {
		this.preFreezeAmount = preFreezeAmount;
	}


   
}