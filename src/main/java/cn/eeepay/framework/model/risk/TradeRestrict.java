package cn.eeepay.framework.model.risk;

import java.util.Date;

/**
 * Created by Administrator on 2019/5/5/005.
 * @author  liuks
 * 交易限制实体
 * 对应表
 */
public class TradeRestrict {

    private int id;
    private String merchantNo;//商户号
    private String orderNo;//交易订单号
    private Integer type;//限制类型：1、银行卡号，2、商户号，3、其他
    private String limitNumber;//限制号码(卡号或者商户号)
    private Integer status;//限制状态: 0、初始化，1、开启，2、关闭
    private String rollNo;//风控规则编号
    private String agentNo;//代理商编号
    private Integer triggerNumber;//当天累计触发次数
    private String resultNo;//失败返回码
    private Date createTime;//创建时间
    private Date createTimeBegin;
    private Date createTimeEnd;

    private String createPerson;//操作人
    private Date operationTime;//操作时间
    private Date operationTimeBegin;
    private Date operationTimeEnd;

    private String remark;//备注

    private String merchantName;//商户名称
    private String merchantPhone;//商户手机号
    private Date merchantTime;//商户注册时间
    private Date merchantTimeBegin;
    private Date merchantTimeEnd;

    private Date invalidTime;//限制失效时间


    private int buttonStatus;//按钮状态

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLimitNumber() {
        return limitNumber;
    }

    public void setLimitNumber(String limitNumber) {
        this.limitNumber = limitNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public Integer getTriggerNumber() {
        return triggerNumber;
    }

    public void setTriggerNumber(Integer triggerNumber) {
        this.triggerNumber = triggerNumber;
    }

    public String getResultNo() {
        return resultNo;
    }

    public void setResultNo(String resultNo) {
        this.resultNo = resultNo;
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
        this.createPerson = createPerson;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantPhone() {
        return merchantPhone;
    }

    public void setMerchantPhone(String merchantPhone) {
        this.merchantPhone = merchantPhone;
    }

    public Date getMerchantTime() {
        return merchantTime;
    }

    public void setMerchantTime(Date merchantTime) {
        this.merchantTime = merchantTime;
    }

    public int getButtonStatus() {
        return buttonStatus;
    }

    public void setButtonStatus(int buttonStatus) {
        this.buttonStatus = buttonStatus;
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

    public Date getOperationTimeBegin() {
        return operationTimeBegin;
    }

    public void setOperationTimeBegin(Date operationTimeBegin) {
        this.operationTimeBegin = operationTimeBegin;
    }

    public Date getOperationTimeEnd() {
        return operationTimeEnd;
    }

    public void setOperationTimeEnd(Date operationTimeEnd) {
        this.operationTimeEnd = operationTimeEnd;
    }

    public Date getMerchantTimeBegin() {
        return merchantTimeBegin;
    }

    public void setMerchantTimeBegin(Date merchantTimeBegin) {
        this.merchantTimeBegin = merchantTimeBegin;
    }

    public Date getMerchantTimeEnd() {
        return merchantTimeEnd;
    }

    public void setMerchantTimeEnd(Date merchantTimeEnd) {
        this.merchantTimeEnd = merchantTimeEnd;
    }

    public Date getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Date invalidTime) {
        this.invalidTime = invalidTime;
    }
}
