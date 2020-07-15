package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class ScanCodeTrans {
    private String tradeNo;

    private String tradeType;

    private String authCode;

    private Integer resultCode;

    private String tradeState;

    private String merchantNo;

    private String acqEnname;

    private String acqTransId;

    private String acqMerchantNo;

    private BigDecimal totalFee;

    private BigDecimal merchantFee;

    private BigDecimal acqFee;

    private BigDecimal refundFee;

    private String refundChannel;

    private String mchCreateIp;

    private String nonceStr;

    private String message;

    private String codeUrl;

    private String codeImgUrl;

    private String openid;

    private String clientType;

    private String jpushId;

    private Integer ist0;

    private Date timeStart;

    private Date timeExpire;

    private Date timeEnd;

    private Date timeLast;

    private String settleStatus;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType == null ? null : tradeType.trim();
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode == null ? null : authCode.trim();
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState == null ? null : tradeState.trim();
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo == null ? null : merchantNo.trim();
    }

    public String getAcqEnname() {
        return acqEnname;
    }

    public void setAcqEnname(String acqEnname) {
        this.acqEnname = acqEnname == null ? null : acqEnname.trim();
    }

    public String getAcqTransId() {
        return acqTransId;
    }

    public void setAcqTransId(String acqTransId) {
        this.acqTransId = acqTransId == null ? null : acqTransId.trim();
    }

    public String getAcqMerchantNo() {
        return acqMerchantNo;
    }

    public void setAcqMerchantNo(String acqMerchantNo) {
        this.acqMerchantNo = acqMerchantNo == null ? null : acqMerchantNo.trim();
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getMerchantFee() {
        return merchantFee;
    }

    public void setMerchantFee(BigDecimal merchantFee) {
        this.merchantFee = merchantFee;
    }

    public BigDecimal getAcqFee() {
        return acqFee;
    }

    public void setAcqFee(BigDecimal acqFee) {
        this.acqFee = acqFee;
    }

    public BigDecimal getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(BigDecimal refundFee) {
        this.refundFee = refundFee;
    }

    public String getRefundChannel() {
        return refundChannel;
    }

    public void setRefundChannel(String refundChannel) {
        this.refundChannel = refundChannel == null ? null : refundChannel.trim();
    }

    public String getMchCreateIp() {
        return mchCreateIp;
    }

    public void setMchCreateIp(String mchCreateIp) {
        this.mchCreateIp = mchCreateIp == null ? null : mchCreateIp.trim();
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr == null ? null : nonceStr.trim();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl == null ? null : codeUrl.trim();
    }

    public String getCodeImgUrl() {
        return codeImgUrl;
    }

    public void setCodeImgUrl(String codeImgUrl) {
        this.codeImgUrl = codeImgUrl == null ? null : codeImgUrl.trim();
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType == null ? null : clientType.trim();
    }

    public String getJpushId() {
        return jpushId;
    }

    public void setJpushId(String jpushId) {
        this.jpushId = jpushId == null ? null : jpushId.trim();
    }

    public Integer getIst0() {
        return ist0;
    }

    public void setIst0(Integer ist0) {
        this.ist0 = ist0;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(Date timeExpire) {
        this.timeExpire = timeExpire;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Date getTimeLast() {
        return timeLast;
    }

    public void setTimeLast(Date timeLast) {
        this.timeLast = timeLast;
    }

    public String getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(String settleStatus) {
        this.settleStatus = settleStatus == null ? null : settleStatus.trim();
    }
}