package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.NumberFormat;

/**
 * table Acq_service_trans_rules 
 * desc 收单服务限额表
 * @author thj
 *
 */
public class AcqServiceTransRules {
    private Integer id;

    private Integer acqServiceId;

    private Integer bankCardType;

	@NumberFormat(pattern = "#.##")
    private BigDecimal savingsSingleMinAmount;

	@NumberFormat(pattern = "#.##")
    private BigDecimal savingsSingleMaxAmount;

	@NumberFormat(pattern = "#.##")
    private BigDecimal savingsDayTotalAmount;

	@NumberFormat(pattern = "#.##")
    private BigDecimal creditSingleMinAmount;

	@NumberFormat(pattern = "#.##")
    private BigDecimal creditSingleMaxAmount;

	@NumberFormat(pattern = "#.##")
    private BigDecimal creditDayTotalAmount;

	@NumberFormat(pattern = "#.##")
    private BigDecimal dayTotalAmount;

    private String warningPhone;

	@NumberFormat(pattern = "#.##")
    private BigDecimal transLimitMinAmount;

	@NumberFormat(pattern = "#.##")
    private BigDecimal transLimitMaxAmount;

    private String clintMsg;

    private Date createTime;

    private String createPerson;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAcqServiceId() {
        return acqServiceId;
    }

    public void setAcqServiceId(Integer acqServiceId) {
        this.acqServiceId = acqServiceId;
    }

    public Integer getBankCardType() {
        return bankCardType;
    }

    public void setBankCardType(Integer bankCardType) {
        this.bankCardType = bankCardType;
    }

    public BigDecimal getSavingsSingleMinAmount() {
        return savingsSingleMinAmount;
    }

    public void setSavingsSingleMinAmount(BigDecimal savingsSingleMinAmount) {
        this.savingsSingleMinAmount = savingsSingleMinAmount;
    }

    public BigDecimal getSavingsSingleMaxAmount() {
        return savingsSingleMaxAmount;
    }

    public void setSavingsSingleMaxAmount(BigDecimal savingsSingleMaxAmount) {
        this.savingsSingleMaxAmount = savingsSingleMaxAmount;
    }

    public BigDecimal getSavingsDayTotalAmount() {
        return savingsDayTotalAmount;
    }

    public void setSavingsDayTotalAmount(BigDecimal savingsDayTotalAmount) {
        this.savingsDayTotalAmount = savingsDayTotalAmount;
    }

    public BigDecimal getCreditSingleMinAmount() {
        return creditSingleMinAmount;
    }

    public void setCreditSingleMinAmount(BigDecimal creditSingleMinAmount) {
        this.creditSingleMinAmount = creditSingleMinAmount;
    }

    public BigDecimal getCreditSingleMaxAmount() {
        return creditSingleMaxAmount;
    }

    public void setCreditSingleMaxAmount(BigDecimal creditSingleMaxAmount) {
        this.creditSingleMaxAmount = creditSingleMaxAmount;
    }

    public BigDecimal getCreditDayTotalAmount() {
        return creditDayTotalAmount;
    }

    public void setCreditDayTotalAmount(BigDecimal creditDayTotalAmount) {
        this.creditDayTotalAmount = creditDayTotalAmount;
    }

    public BigDecimal getDayTotalAmount() {
        return dayTotalAmount;
    }

    public void setDayTotalAmount(BigDecimal dayTotalAmount) {
        this.dayTotalAmount = dayTotalAmount;
    }

    public String getWarningPhone() {
        return warningPhone;
    }

    public void setWarningPhone(String warningPhone) {
        this.warningPhone = warningPhone == null ? null : warningPhone.trim();
    }

    public BigDecimal getTransLimitMinAmount() {
        return transLimitMinAmount;
    }

    public void setTransLimitMinAmount(BigDecimal transLimitMinAmount) {
        this.transLimitMinAmount = transLimitMinAmount;
    }

    public BigDecimal getTransLimitMaxAmount() {
        return transLimitMaxAmount;
    }

    public void setTransLimitMaxAmount(BigDecimal transLimitMaxAmount) {
        this.transLimitMaxAmount = transLimitMaxAmount;
    }

    public String getClintMsg() {
        return clintMsg;
    }

    public void setClintMsg(String clintMsg) {
        this.clintMsg = clintMsg == null ? null : clintMsg.trim();
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
}