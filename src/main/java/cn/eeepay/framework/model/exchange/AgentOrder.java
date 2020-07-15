package cn.eeepay.framework.model.exchange;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/4/17/017.
 * @author  liuks
 * 代理费订单
 * 对应表 rdmp_order  rdmp_activity_order
 */
public class AgentOrder {

    private long id;//id

    private String  payOrderNo;//付款订单号

    private String orderNo;//订单号

    private String merNo;//商户号

    private String orderType;//订单类型 A 激活订单 D 报单单

    private String orderStatus;//订单状态 INIT 初始化 SUB 已提交 SUCCESS 成功 FAILED 失败 UNKNOW 未知

    private String shareStatus;//分润状态 1 未分润 2 已分润

    private String remark;//备注信息

    private Date createTime;//创建时间
    private Date createTimeBegin;
    private Date createTimeEnd;

    //rdmp_activity_order 表中的字段
    private String merCapa;//订单激活的身份

    private String parMerCapa;//激活时 上级身份

    private String parMerNo;//邀请人商户号

    private BigDecimal amount;//金额

    private Date payTime;//支付完成时间
    private Date payTimeBegin;
    private Date payTimeEnd;

    private String oemNo;//组织编码

    private String oemName;//组织名称

    private String userName;//用户名

    private String accStatus;//记账状态0 未记账 1 记账成功 2 记账失败 3 已提交记账

    private Date accTime;//入账时间
    private Date accTimeBegin;
    private Date accTimeEnd;


    private BigDecimal plateShare;//平台分润

    private BigDecimal oemShare;//品牌分润

    private BigDecimal provideAmout;//总奖金

    private BigDecimal oemFee;//成本

    private BigDecimal agentAmout;//代理商总分润
    private BigDecimal merAmout;//用户总分润


    private List<ShareOrder> shareOrderList;//等级分润

    private List<AgentShare> agentShareList;//代理商分润

    private String agentNoOne;//一级分润代理商编号
    private BigDecimal agentOneAmout;//一级分润代理商分润

    private String agentNoTwo;//二级分润代理商编号
    private BigDecimal agentTwoAmout;//二级分润代理商分润

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMerNo() {
        return merNo;
    }

    public void setMerNo(String merNo) {
        this.merNo = merNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getShareStatus() {
        return shareStatus;
    }

    public void setShareStatus(String shareStatus) {
        this.shareStatus = shareStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getMerCapa() {
        return merCapa;
    }

    public void setMerCapa(String merCapa) {
        this.merCapa = merCapa;
    }

    public String getParMerCapa() {
        return parMerCapa;
    }

    public void setParMerCapa(String parMerCapa) {
        this.parMerCapa = parMerCapa;
    }

    public String getParMerNo() {
        return parMerNo;
    }

    public void setParMerNo(String parMerNo) {
        this.parMerNo = parMerNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getPayTimeBegin() {
        return payTimeBegin;
    }

    public void setPayTimeBegin(Date payTimeBegin) {
        this.payTimeBegin = payTimeBegin;
    }

    public Date getPayTimeEnd() {
        return payTimeEnd;
    }

    public void setPayTimeEnd(Date payTimeEnd) {
        this.payTimeEnd = payTimeEnd;
    }

    public String getOemNo() {
        return oemNo;
    }

    public void setOemNo(String oemNo) {
        this.oemNo = oemNo;
    }

    public String getOemName() {
        return oemName;
    }

    public void setOemName(String oemName) {
        this.oemName = oemName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
    }

    public Date getAccTime() {
        return accTime;
    }

    public void setAccTime(Date accTime) {
        this.accTime = accTime;
    }

    public BigDecimal getPlateShare() {
        return plateShare;
    }

    public void setPlateShare(BigDecimal plateShare) {
        this.plateShare = plateShare;
    }

    public BigDecimal getOemShare() {
        return oemShare;
    }

    public void setOemShare(BigDecimal oemShare) {
        this.oemShare = oemShare;
    }

    public BigDecimal getProvideAmout() {
        return provideAmout;
    }

    public void setProvideAmout(BigDecimal provideAmout) {
        this.provideAmout = provideAmout;
    }

    public BigDecimal getOemFee() {
        return oemFee;
    }

    public void setOemFee(BigDecimal oemFee) {
        this.oemFee = oemFee;
    }

    public List<ShareOrder> getShareOrderList() {
        return shareOrderList;
    }

    public void setShareOrderList(List<ShareOrder> shareOrderList) {
        this.shareOrderList = shareOrderList;
    }

    public List<AgentShare> getAgentShareList() {
        return agentShareList;
    }

    public void setAgentShareList(List<AgentShare> agentShareList) {
        this.agentShareList = agentShareList;
    }

    public Date getAccTimeBegin() {
        return accTimeBegin;
    }

    public void setAccTimeBegin(Date accTimeBegin) {
        this.accTimeBegin = accTimeBegin;
    }

    public Date getAccTimeEnd() {
        return accTimeEnd;
    }

    public void setAccTimeEnd(Date accTimeEnd) {
        this.accTimeEnd = accTimeEnd;
    }

    public BigDecimal getAgentAmout() {
        return agentAmout;
    }

    public void setAgentAmout(BigDecimal agentAmout) {
        this.agentAmout = agentAmout;
    }

    public BigDecimal getMerAmout() {
        return merAmout;
    }

    public void setMerAmout(BigDecimal merAmout) {
        this.merAmout = merAmout;
    }

    public String getAgentNoOne() {
        return agentNoOne;
    }

    public void setAgentNoOne(String agentNoOne) {
        this.agentNoOne = agentNoOne;
    }

    public BigDecimal getAgentOneAmout() {
        return agentOneAmout;
    }

    public void setAgentOneAmout(BigDecimal agentOneAmout) {
        this.agentOneAmout = agentOneAmout;
    }

    public String getAgentNoTwo() {
        return agentNoTwo;
    }

    public void setAgentNoTwo(String agentNoTwo) {
        this.agentNoTwo = agentNoTwo;
    }

    public BigDecimal getAgentTwoAmout() {
        return agentTwoAmout;
    }

    public void setAgentTwoAmout(BigDecimal agentTwoAmout) {
        this.agentTwoAmout = agentTwoAmout;
    }
}
