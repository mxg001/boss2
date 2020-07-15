package cn.eeepay.framework.model;

import java.util.Date;

/**
 * Created by 666666 on 2017/11/20.
 */
public class RepayProfitDetailBean {
    // 主键
    private String id;
    // 分润明细编号
    private String profitNo;
    // 订单号/计划批次号
    private String orderNo;
    // 分润的商户类型 A：代理商  M:商户
    private String profitMerType;
    // 商户号/代理商编号
    private String profitMerNo;
    private String agentName;
    // 交易金额
    private String transAmount;
    // 交易时间/计划终态时间
    private Date transTime;
    // 支付商户
    private String merchantNo;
    // 交易商户直属代理商node
    private String agentNode;
    // 分润总金额
    private String shareAmount;
    // 交易分润金额
    private String sharePayAmount;
    // 代付分润金额
    private String shareWithdrawAmount;
    // 交易分润费率+代付分润金额，如：0.001+1
    private String shareRate;
    // 创建时间
    private String createTime;
    // 汇总批次号
    private String collectionBatchNo;
    // 汇总时间
    private String collectionTime;

    private String repayAmount;
    private String ensureAmount;
    private String repayFee;
    private String successPayAmount;
    private String successRepayAmount;
    private String actualPayFee;
    private String actualWithdrawFee;
    private String minShareAmount;
    private String maxShareAmount;

    private String status;//订单状态
    private String containSub;//查询条件，是否包含下级
    private String sTransTime;
    private String eTransTime;

    private String profitType;//分润类型	1：超级还分期还款，2：超级还全额还款，3：保证金分润

    private String toProfitAmount;//产生分润的金额（保证金或者计划成功消费金额）

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfitNo() {
        return profitNo;
    }

    public void setProfitNo(String profitNo) {
        this.profitNo = profitNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getProfitMerType() {
        return profitMerType;
    }

    public void setProfitMerType(String profitMerType) {
        this.profitMerType = profitMerType;
    }

    public String getProfitMerNo() {
        return profitMerNo;
    }

    public void setProfitMerNo(String profitMerNo) {
        this.profitMerNo = profitMerNo;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode;
    }

    public String getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(String shareAmount) {
        this.shareAmount = shareAmount;
    }

    public String getSharePayAmount() {
        return sharePayAmount;
    }

    public void setSharePayAmount(String sharePayAmount) {
        this.sharePayAmount = sharePayAmount;
    }

    public String getShareWithdrawAmount() {
        return shareWithdrawAmount;
    }

    public void setShareWithdrawAmount(String shareWithdrawAmount) {
        this.shareWithdrawAmount = shareWithdrawAmount;
    }

    public String getShareRate() {
        return shareRate;
    }

    public void setShareRate(String shareRate) {
        this.shareRate = shareRate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCollectionBatchNo() {
        return collectionBatchNo;
    }

    public void setCollectionBatchNo(String collectionBatchNo) {
        this.collectionBatchNo = collectionBatchNo;
    }

    public String getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }

    public String getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(String repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getEnsureAmount() {
        return ensureAmount;
    }

    public void setEnsureAmount(String ensureAmount) {
        this.ensureAmount = ensureAmount;
    }

    public String getRepayFee() {
        return repayFee;
    }

    public void setRepayFee(String repayFee) {
        this.repayFee = repayFee;
    }

    public String getSuccessPayAmount() {
        return successPayAmount;
    }

    public void setSuccessPayAmount(String successPayAmount) {
        this.successPayAmount = successPayAmount;
    }

    public String getSuccessRepayAmount() {
        return successRepayAmount;
    }

    public void setSuccessRepayAmount(String successRepayAmount) {
        this.successRepayAmount = successRepayAmount;
    }

    public String getActualPayFee() {
        return actualPayFee;
    }

    public void setActualPayFee(String actualPayFee) {
        this.actualPayFee = actualPayFee;
    }

    public String getActualWithdrawFee() {
        return actualWithdrawFee;
    }

    public void setActualWithdrawFee(String actualWithdrawFee) {
        this.actualWithdrawFee = actualWithdrawFee;
    }

    public String getMinShareAmount() {
        return minShareAmount;
    }

    public void setMinShareAmount(String minShareAmount) {
        this.minShareAmount = minShareAmount;
    }

    public String getMaxShareAmount() {
        return maxShareAmount;
    }

    public void setMaxShareAmount(String maxShareAmount) {
        this.maxShareAmount = maxShareAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContainSub() {
        return containSub;
    }

    public void setContainSub(String containSub) {
        this.containSub = containSub;
    }

    public String getsTransTime() {
        return sTransTime;
    }

    public void setsTransTime(String sTransTime) {
        this.sTransTime = sTransTime;
    }

    public String geteTransTime() {
        return eTransTime;
    }

    public void seteTransTime(String eTransTime) {
        this.eTransTime = eTransTime;
    }

    public String getProfitType() {
        return profitType;
    }

    public void setProfitType(String profitType) {
        this.profitType = profitType;
    }

    public String getToProfitAmount() {
        return toProfitAmount;
    }

    public void setToProfitAmount(String toProfitAmount) {
        this.toProfitAmount = toProfitAmount;
    }
}
