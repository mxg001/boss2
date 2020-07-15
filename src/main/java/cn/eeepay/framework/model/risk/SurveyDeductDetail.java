package cn.eeepay.framework.model.risk;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/9/13/013.
 * @author  liuks
 * 调单明细扣款
 * 对应表 survey_order_deduct
 */
public class SurveyDeductDetail {
    //如果是扣款查询，列表金额是累计值

    private Integer id;//
    private String orderNo;//调单单号

    private BigDecimal acqDeductAmount;//上游扣款金额
    private Date acqDeductTime;//上游扣款时间
    private Date acqDeductTimeBegin;
    private Date acqDeductTimeEnd;
    private String acqDeductRemark;//上游扣款备注

    private BigDecimal merDeductAmount;//商户扣款金额
    private Date merDeductTime;//商户扣款时间
    private BigDecimal agentRemainDeductAmount;//代理商待扣款金额
    private String merDeductRemark;//商户扣款备注（风控扣款备注）

    private BigDecimal agentHaveDeductAmount;//代理商已扣款金额
    private Date agentDeductTime;//代理商扣款时间
    private BigDecimal agentNeedDeductAmount;//代理商需扣款金额
    private String agentDeductRemark;//代理商扣款备注

    private BigDecimal acqIssueAmount;//上游下发金额
    private Date acqIssueTime;//上游下发时间
    private String acqIssueRemark;//上游下发备注

    private BigDecimal merIssueAmount;//商户下发金额
    private Date merIssueTime;//商户下发时间
    private BigDecimal agentRemainIssueAmount;//代理商待下发金额
    private String merIssueRemark;//商户下发备注（风控下发备注）

    private BigDecimal agentHaveIssueAmount;//代理商已下发金额
    private Date agentIssueTime;//代理商下发时间
    private BigDecimal agentNeedIssueAmount;//代理商需下发金额
    private String agentIssueRemark;//代理商下发备注

    private String operator;//操作人
    private Date operateTime;//操作时间
    private Date operateTimeBegin;
    private Date operateTimeEnd;

    private Date lastUpdateTime;//数据最后更新时间

    private  String merchantNo;//商户编号
    private  String transOrderNo;//交易订单编号
    private  String acqReferenceNo ;//收单机构参考号
    private  String orderTypeCode;//调单类型编号,对应字典表数据
    private  BigDecimal transAmount;//交易金额
    private  String transOrderDatabase;//交易订单所在数据库 now当前库 old历史库
    private  String orderServiceCode;//业务类型编号，对应字典表数据

    private  String haveAddDeduct;//是否添加了扣款  0没有添加 1已添加
    private  String agentDeductDealStatus;//代理商扣款处理状态 0未处理 1已处理
    private  String agentDeductDealRemark;//代理商扣款处理备注
    private  String agentIssueDealStatus;//代理商下发处理状态 0未处理 1已处理
    private  String agentIssueDealRemark;//代理商下发处理备注


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getAcqDeductAmount() {
        return acqDeductAmount;
    }

    public void setAcqDeductAmount(BigDecimal acqDeductAmount) {
        this.acqDeductAmount = acqDeductAmount;
    }

    public Date getAcqDeductTime() {
        return acqDeductTime;
    }

    public void setAcqDeductTime(Date acqDeductTime) {
        this.acqDeductTime = acqDeductTime;
    }

    public Date getAcqDeductTimeBegin() {
        return acqDeductTimeBegin;
    }

    public void setAcqDeductTimeBegin(Date acqDeductTimeBegin) {
        this.acqDeductTimeBegin = acqDeductTimeBegin;
    }

    public Date getAcqDeductTimeEnd() {
        return acqDeductTimeEnd;
    }

    public void setAcqDeductTimeEnd(Date acqDeductTimeEnd) {
        this.acqDeductTimeEnd = acqDeductTimeEnd;
    }

    public String getAcqDeductRemark() {
        return acqDeductRemark;
    }

    public void setAcqDeductRemark(String acqDeductRemark) {
        this.acqDeductRemark = acqDeductRemark;
    }

    public BigDecimal getMerDeductAmount() {
        return merDeductAmount;
    }

    public void setMerDeductAmount(BigDecimal merDeductAmount) {
        this.merDeductAmount = merDeductAmount;
    }

    public Date getMerDeductTime() {
        return merDeductTime;
    }

    public void setMerDeductTime(Date merDeductTime) {
        this.merDeductTime = merDeductTime;
    }

    public BigDecimal getAgentRemainDeductAmount() {
        return agentRemainDeductAmount;
    }

    public void setAgentRemainDeductAmount(BigDecimal agentRemainDeductAmount) {
        this.agentRemainDeductAmount = agentRemainDeductAmount;
    }

    public String getMerDeductRemark() {
        return merDeductRemark;
    }

    public void setMerDeductRemark(String merDeductRemark) {
        this.merDeductRemark = merDeductRemark;
    }

    public BigDecimal getAgentHaveDeductAmount() {
        return agentHaveDeductAmount;
    }

    public void setAgentHaveDeductAmount(BigDecimal agentHaveDeductAmount) {
        this.agentHaveDeductAmount = agentHaveDeductAmount;
    }

    public Date getAgentDeductTime() {
        return agentDeductTime;
    }

    public void setAgentDeductTime(Date agentDeductTime) {
        this.agentDeductTime = agentDeductTime;
    }

    public BigDecimal getAgentNeedDeductAmount() {
        return agentNeedDeductAmount;
    }

    public void setAgentNeedDeductAmount(BigDecimal agentNeedDeductAmount) {
        this.agentNeedDeductAmount = agentNeedDeductAmount;
    }

    public String getAgentDeductRemark() {
        return agentDeductRemark;
    }

    public void setAgentDeductRemark(String agentDeductRemark) {
        this.agentDeductRemark = agentDeductRemark;
    }

    public BigDecimal getAcqIssueAmount() {
        return acqIssueAmount;
    }

    public void setAcqIssueAmount(BigDecimal acqIssueAmount) {
        this.acqIssueAmount = acqIssueAmount;
    }

    public Date getAcqIssueTime() {
        return acqIssueTime;
    }

    public void setAcqIssueTime(Date acqIssueTime) {
        this.acqIssueTime = acqIssueTime;
    }

    public String getAcqIssueRemark() {
        return acqIssueRemark;
    }

    public void setAcqIssueRemark(String acqIssueRemark) {
        this.acqIssueRemark = acqIssueRemark;
    }

    public BigDecimal getMerIssueAmount() {
        return merIssueAmount;
    }

    public void setMerIssueAmount(BigDecimal merIssueAmount) {
        this.merIssueAmount = merIssueAmount;
    }

    public Date getMerIssueTime() {
        return merIssueTime;
    }

    public void setMerIssueTime(Date merIssueTime) {
        this.merIssueTime = merIssueTime;
    }

    public BigDecimal getAgentRemainIssueAmount() {
        return agentRemainIssueAmount;
    }

    public void setAgentRemainIssueAmount(BigDecimal agentRemainIssueAmount) {
        this.agentRemainIssueAmount = agentRemainIssueAmount;
    }

    public String getMerIssueRemark() {
        return merIssueRemark;
    }

    public void setMerIssueRemark(String merIssueRemark) {
        this.merIssueRemark = merIssueRemark;
    }

    public BigDecimal getAgentHaveIssueAmount() {
        return agentHaveIssueAmount;
    }

    public void setAgentHaveIssueAmount(BigDecimal agentHaveIssueAmount) {
        this.agentHaveIssueAmount = agentHaveIssueAmount;
    }

    public Date getAgentIssueTime() {
        return agentIssueTime;
    }

    public void setAgentIssueTime(Date agentIssueTime) {
        this.agentIssueTime = agentIssueTime;
    }

    public BigDecimal getAgentNeedIssueAmount() {
        return agentNeedIssueAmount;
    }

    public void setAgentNeedIssueAmount(BigDecimal agentNeedIssueAmount) {
        this.agentNeedIssueAmount = agentNeedIssueAmount;
    }

    public String getAgentIssueRemark() {
        return agentIssueRemark;
    }

    public void setAgentIssueRemark(String agentIssueRemark) {
        this.agentIssueRemark = agentIssueRemark;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Date getOperateTimeBegin() {
        return operateTimeBegin;
    }

    public void setOperateTimeBegin(Date operateTimeBegin) {
        this.operateTimeBegin = operateTimeBegin;
    }

    public Date getOperateTimeEnd() {
        return operateTimeEnd;
    }

    public void setOperateTimeEnd(Date operateTimeEnd) {
        this.operateTimeEnd = operateTimeEnd;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getTransOrderNo() {
        return transOrderNo;
    }

    public void setTransOrderNo(String transOrderNo) {
        this.transOrderNo = transOrderNo;
    }

    public String getAcqReferenceNo() {
        return acqReferenceNo;
    }

    public void setAcqReferenceNo(String acqReferenceNo) {
        this.acqReferenceNo = acqReferenceNo;
    }

    public String getOrderTypeCode() {
        return orderTypeCode;
    }

    public void setOrderTypeCode(String orderTypeCode) {
        this.orderTypeCode = orderTypeCode;
    }

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }

    public String getTransOrderDatabase() {
        return transOrderDatabase;
    }

    public void setTransOrderDatabase(String transOrderDatabase) {
        this.transOrderDatabase = transOrderDatabase;
    }

    public String getOrderServiceCode() {
        return orderServiceCode;
    }

    public void setOrderServiceCode(String orderServiceCode) {
        this.orderServiceCode = orderServiceCode;
    }

    public String getHaveAddDeduct() {
        return haveAddDeduct;
    }

    public void setHaveAddDeduct(String haveAddDeduct) {
        this.haveAddDeduct = haveAddDeduct;
    }

    public String getAgentDeductDealStatus() {
        return agentDeductDealStatus;
    }

    public void setAgentDeductDealStatus(String agentDeductDealStatus) {
        this.agentDeductDealStatus = agentDeductDealStatus;
    }

    public String getAgentDeductDealRemark() {
        return agentDeductDealRemark;
    }

    public void setAgentDeductDealRemark(String agentDeductDealRemark) {
        this.agentDeductDealRemark = agentDeductDealRemark;
    }

    public String getAgentIssueDealStatus() {
        return agentIssueDealStatus;
    }

    public void setAgentIssueDealStatus(String agentIssueDealStatus) {
        this.agentIssueDealStatus = agentIssueDealStatus;
    }

    public String getAgentIssueDealRemark() {
        return agentIssueDealRemark;
    }

    public void setAgentIssueDealRemark(String agentIssueDealRemark) {
        this.agentIssueDealRemark = agentIssueDealRemark;
    }
}
