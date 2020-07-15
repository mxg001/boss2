package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class ZxProductOrder extends  OrderMain{
    private Long id;

    private String orderNo;

    private String yhjOrderNo;

    private String payNo;

    private String userCode;

    private Integer productId;

    private String applyNo;

    private String reportUrl;

    private String applyName;

    private String status;

    private String reportNo;

    private String recordIdNo;

    private String recordName;

    private String recordPhone;

    private String generationTimeBegin;

    private String generationTimeEnd;

    private String contactPhone;

    private BigDecimal price;

    private String payMethod;

    private Date payDate;

    private String payDateStr;

    private String reportType;

    private String reportTypeName;

    private String createDateStart;

    private  String createDateEnd;

    private String productName;

    private Date generationTime;

    private String generationTimeStr;

    private Date createTime;

    private String createTimeStr;

    private Date updateTime;

    private String operate;

    private Date expiryTime;

    private String  expiryTimeStr;

    private  String zxCostPrice;

    private Date profitDate;

    private Date refundTime;

    private String refundTimeStr;

    private String refundTimeBegin;

    private String refundTimeEnd;

    private String profitDateStr;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getYhjOrderNo() {
        return yhjOrderNo;
    }

    public void setYhjOrderNo(String yhjOrderNo) {
        this.yhjOrderNo = yhjOrderNo == null ? null : yhjOrderNo.trim();
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo == null ? null : payNo.trim();
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode == null ? null : userCode.trim();
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo == null ? null : applyNo.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo == null ? null : reportNo.trim();
    }

    public String getRecordIdNo() {
        return recordIdNo;
    }

    public void setRecordIdNo(String recordIdNo) {
        this.recordIdNo = recordIdNo == null ? null : recordIdNo.trim();
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName == null ? null : recordName.trim();
    }

    public String getRecordPhone() {
        return recordPhone;
    }

    public void setRecordPhone(String recordPhone) {
        this.recordPhone = recordPhone == null ? null : recordPhone.trim();
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod == null ? null : payMethod.trim();
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getProductName() {
        return productName;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public String getRefundTimeStr() {
        return refundTimeStr;
    }

    public void setRefundTimeStr(String refundTimeStr) {
        this.refundTimeStr = refundTimeStr;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public Date getGenerationTime() {
        return generationTime;
    }

    public void setGenerationTime(Date generationTime) {
        this.generationTime = generationTime;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate == null ? null : operate.trim();
    }

    public String getCreateDateStart() {
        return createDateStart;
    }

    public void setCreateDateStart(String createDateStart) {
        this.createDateStart = createDateStart;
    }

    public String getCreateDateEnd() {
        return createDateEnd;
    }

    public void setCreateDateEnd(String createDateEnd) {
        this.createDateEnd = createDateEnd;
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

    @Override
    public String getPayDateStr() {
        return payDateStr;
    }

    public String getReportTypeName() {
        return reportTypeName;
    }

    public void setReportTypeName(String reportTypeName) {
        this.reportTypeName = reportTypeName;
    }

    @Override
    public void setPayDateStr(String payDateStr) {
        this.payDateStr = payDateStr;
    }

    public String getGenerationTimeStr() {
        return generationTimeStr;
    }

    public void setGenerationTimeStr(String generationTimeStr) {
        this.generationTimeStr = generationTimeStr;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getGenerationTimeBegin() {
        return generationTimeBegin;
    }

    public void setGenerationTimeBegin(String generationTimeBegin) {
        this.generationTimeBegin = generationTimeBegin;
    }

    public String getGenerationTimeEnd() {
        return generationTimeEnd;
    }

    public void setGenerationTimeEnd(String generationTimeEnd) {
        this.generationTimeEnd = generationTimeEnd;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getExpiryTimeStr() {
        return expiryTimeStr;
    }

    public void setExpiryTimeStr(String expiryTimeStr) {
        this.expiryTimeStr = expiryTimeStr;
    }

    public String getZxCostPrice() {
        return zxCostPrice;
    }

    public void setZxCostPrice(String zxCostPrice) {
        this.zxCostPrice = zxCostPrice;
    }

    public Date getProfitDate() {
        return profitDate;
    }

    public String getRefundTimeBegin() {
        return refundTimeBegin;
    }

    public void setRefundTimeBegin(String refundTimeBegin) {
        this.refundTimeBegin = refundTimeBegin;
    }

    public String getRefundTimeEnd() {
        return refundTimeEnd;
    }

    public void setRefundTimeEnd(String refundTimeEnd) {
        this.refundTimeEnd = refundTimeEnd;
    }

    public String getReportUrl() {
        return reportUrl;
    }
    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public void setProfitDate(Date profitDate) {
        this.profitDate = profitDate;
    }

    public String getProfitDateStr() {
        return profitDateStr;
    }

    public void setProfitDateStr(String profitDateStr) {
        this.profitDateStr = profitDateStr;
    }

}