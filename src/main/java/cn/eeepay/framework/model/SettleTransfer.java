package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class SettleTransfer {
    private Integer id;

    private String transId;

    private String orderNo;

    private String accountSerialNo;

    private String settleBank;

    private String inAccNo;

    private String inAccName;

    private String inSettleBankNo;

    private String inBankNo;

    private String inBankName;

    private String outAccNo;

    private BigDecimal amount;

    private BigDecimal outAmount;

    private BigDecimal feeAmount;

    private Integer proorcost;

    private Integer rateType;

    private Integer outServiceId;

    private Integer serviceRateId;

    private String outRate;

    private Date createTime;

    private String status;

    private String errCode;

    private Integer synAccountStatus;

    private Integer correction;

    private String errMsg;

    private String bak1;

    private String bak2;
    
    private String serviceName;

    private String settleCreator;
    private String settleUserName;
    private String userName;
    private String settleType;
	private String settleUserType;
	private String settleUserNo;
    private BigDecimal deductionFee;//抵扣手续费
    private BigDecimal actualFee;//实际手续费

    public BigDecimal getDeductionFee() {
        return deductionFee;
    }

    public void setDeductionFee(BigDecimal deductionFee) {
        this.deductionFee = deductionFee;
    }

    public BigDecimal getActualFee() {
        return actualFee;
    }

    public void setActualFee(BigDecimal actualFee) {
        this.actualFee = actualFee;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId == null ? null : transId.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getAccountSerialNo() {
        return accountSerialNo;
    }

    public void setAccountSerialNo(String accountSerialNo) {
        this.accountSerialNo = accountSerialNo == null ? null : accountSerialNo.trim();
    }

    public String getSettleBank() {
        return settleBank;
    }

    public void setSettleBank(String settleBank) {
        this.settleBank = settleBank == null ? null : settleBank.trim();
    }

    public String getInAccNo() {
        return inAccNo;
    }

    public void setInAccNo(String inAccNo) {
        this.inAccNo = inAccNo == null ? null : inAccNo.trim();
    }

    public String getInAccName() {
        return inAccName;
    }

    public void setInAccName(String inAccName) {
        this.inAccName = inAccName == null ? null : inAccName.trim();
    }

    public String getInSettleBankNo() {
        return inSettleBankNo;
    }

    public void setInSettleBankNo(String inSettleBankNo) {
        this.inSettleBankNo = inSettleBankNo == null ? null : inSettleBankNo.trim();
    }

    public String getInBankNo() {
        return inBankNo;
    }

    public void setInBankNo(String inBankNo) {
        this.inBankNo = inBankNo == null ? null : inBankNo.trim();
    }

    public String getInBankName() {
        return inBankName;
    }

    public void setInBankName(String inBankName) {
        this.inBankName = inBankName == null ? null : inBankName.trim();
    }

    public String getOutAccNo() {
        return outAccNo;
    }

    public void setOutAccNo(String outAccNo) {
        this.outAccNo = outAccNo == null ? null : outAccNo.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getOutAmount() {
        return outAmount;
    }

    public void setOutAmount(BigDecimal outAmount) {
        this.outAmount = outAmount;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Integer getProorcost() {
        return proorcost;
    }

    public void setProorcost(Integer proorcost) {
        this.proorcost = proorcost;
    }

    public Integer getRateType() {
        return rateType;
    }

    public void setRateType(Integer rateType) {
        this.rateType = rateType;
    }

    public Integer getOutServiceId() {
        return outServiceId;
    }

    public void setOutServiceId(Integer outServiceId) {
        this.outServiceId = outServiceId;
    }

    public Integer getServiceRateId() {
        return serviceRateId;
    }

    public void setServiceRateId(Integer serviceRateId) {
        this.serviceRateId = serviceRateId;
    }

    public String getOutRate() {
        return outRate;
    }

    public void setOutRate(String outRate) {
        this.outRate = outRate == null ? null : outRate.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode == null ? null : errCode.trim();
    }

    public Integer getSynAccountStatus() {
        return synAccountStatus;
    }

    public void setSynAccountStatus(Integer synAccountStatus) {
        this.synAccountStatus = synAccountStatus;
    }

    public Integer getCorrection() {
        return correction;
    }

    public void setCorrection(Integer correction) {
        this.correction = correction;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg == null ? null : errMsg.trim();
    }

    public String getBak1() {
        return bak1;
    }

    public void setBak1(String bak1) {
        this.bak1 = bak1 == null ? null : bak1.trim();
    }

    public String getBak2() {
        return bak2;
    }

    public void setBak2(String bak2) {
        this.bak2 = bak2 == null ? null : bak2.trim();
    }

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getSettleCreator() {
		return settleCreator;
	}

	public void setSettleCreator(String settleCreator) {
		this.settleCreator = settleCreator;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSettleType() {
		return settleType;
	}

	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}

	public String getSettleUserType() {
		return settleUserType;
	}

	public void setSettleUserType(String settleUserType) {
		this.settleUserType = settleUserType;
	}

	public String getSettleUserNo() {
		return settleUserNo;
	}

	public void setSettleUserNo(String settleUserNo) {
		this.settleUserNo = settleUserNo;
	}

	public String getSettleUserName() {
		return settleUserName;
	}

	public void setSettleUserName(String settleUserName) {
		this.settleUserName = settleUserName;
	}
    
    
}