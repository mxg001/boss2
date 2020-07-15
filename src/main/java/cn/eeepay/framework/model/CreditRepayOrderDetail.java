package cn.eeepay.framework.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 还款订单详情
 * @author liuks
 * 对应实体表 yfb_repay_plan_detail
 */
public class CreditRepayOrderDetail {
    private int id;//id

    private String merchantNo;//'商户号'

    private String cardNo;//'卡片编号'

    private String batchNo;//'计划批次号'

    private String planNo;//'计划编号'

    private String planType;//'计划类型 IN：给用户还款，即代付，OUT：用户消费，即快捷支付'

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date planTime;//'计划时间'
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date planTimeBegin;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date planTimeEnd;

    private int planIndex;//'计划序次号 同一计划批次下面的计划任务，序次号自增'

    private int planCount;//'计划已执行的次数'

    private BigDecimal planAmount;//'计划执行金额'
    private BigDecimal minPlanAmount;
    private BigDecimal maxPlanAmount;

    private String planStatus;//'计划执行状态 0:未执行,1:执行中,2:执行成功,3:执行失败'

    private String resMsg;//'响应信息'

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//'创建时间'
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeBegin;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//'最后更新时间'

    private String bak1;//'备用1'

    private String bak2;//'备用2'

    private String accountNo;//银行卡号

    private String acqCode;//交易通道

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public Date getPlanTime() {
        return planTime;
    }

    public void setPlanTime(Date planTime) {
        this.planTime = planTime;
    }

    public int getPlanIndex() {
        return planIndex;
    }

    public void setPlanIndex(int planIndex) {
        this.planIndex = planIndex;
    }

    public int getPlanCount() {
        return planCount;
    }

    public void setPlanCount(int planCount) {
        this.planCount = planCount;
    }

    public BigDecimal getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(BigDecimal planAmount) {
        this.planAmount = planAmount;
    }

    public BigDecimal getMinPlanAmount() {
        return minPlanAmount;
    }

    public void setMinPlanAmount(BigDecimal minPlanAmount) {
        this.minPlanAmount = minPlanAmount;
    }

    public BigDecimal getMaxPlanAmount() {
        return maxPlanAmount;
    }

    public void setMaxPlanAmount(BigDecimal maxPlanAmount) {
        this.maxPlanAmount = maxPlanAmount;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getBak1() {
        return bak1;
    }

    public void setBak1(String bak1) {
        this.bak1 = bak1;
    }

    public String getBak2() {
        return bak2;
    }

    public void setBak2(String bak2) {
        this.bak2 = bak2;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Date getPlanTimeBegin() {
        return planTimeBegin;
    }

    public void setPlanTimeBegin(Date planTimeBegin) {
        this.planTimeBegin = planTimeBegin;
    }

    public Date getPlanTimeEnd() {
        return planTimeEnd;
    }

    public void setPlanTimeEnd(Date planTimeEnd) {
        this.planTimeEnd = planTimeEnd;
    }

    public String getAcqCode() {
        return acqCode;
    }

    public void setAcqCode(String acqCode) {
        this.acqCode = acqCode;
    }
}
