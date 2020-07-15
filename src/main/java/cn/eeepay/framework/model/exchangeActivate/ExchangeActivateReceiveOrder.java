package cn.eeepay.framework.model.exchangeActivate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/6/7/007.
 * @author  liuks
 * 超级兑 收款订单实体
 * 对应表 act_receive_order
 */
public class ExchangeActivateReceiveOrder {
    private Long id;
    private String orderNo;//订单号

    private String merNo;//商户号

    private String sourceOrderNo;//关联订单号

    private String payMethod;//支付方式

    private BigDecimal amount;//金额

    private String rate;//品牌发放总奖金扣率

    private Date createTime;//创建时间
    private Date createTimeBegin;
    private Date createTimeEnd;

    private String orderStatus;//订单状态:INIT 初始化;SUB 已提交;SUCCESS 成功;FAILED 失败;CANCEL 取消;UNKNOW 未知;

    private String oemNo;//组织编码
    private String oemName;//组织名称

    private String userName;//贡献人名称
    private String mobileUsername;//手机号码
    private String receiveMerchantNo;//收款商户号
    private String idCardNo;//身份证

    private String oneAgentNo;//一级代理商编号
    private String oneAgentName;
    private String agentNo;//所属代理商编号
    private String agentName;

    private BigDecimal oemShare;//品牌商分润
    private BigDecimal plateShare;//平台分润
    private String accStatus;//记账状态

    private Date accTime;//入账时间
    private Date accTimeBegin;
    private Date accTimeEnd;

    private BigDecimal provideAmout;//品牌发放总奖金

    List<ExchangeActivateShare> shareList;//代理商分润

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
        this.orderNo = orderNo;
    }

    public String getMerNo() {
        return merNo;
    }

    public void setMerNo(String merNo) {
        this.merNo = merNo;
    }

    public String getSourceOrderNo() {
        return sourceOrderNo;
    }

    public void setSourceOrderNo(String sourceOrderNo) {
        this.sourceOrderNo = sourceOrderNo;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public String getMobileUsername() {
        return mobileUsername;
    }

    public void setMobileUsername(String mobileUsername) {
        this.mobileUsername = mobileUsername;
    }

    public String getReceiveMerchantNo() {
        return receiveMerchantNo;
    }

    public void setReceiveMerchantNo(String receiveMerchantNo) {
        this.receiveMerchantNo = receiveMerchantNo;
    }

    public String getOneAgentNo() {
        return oneAgentNo;
    }

    public void setOneAgentNo(String oneAgentNo) {
        this.oneAgentNo = oneAgentNo;
    }

    public String getOneAgentName() {
        return oneAgentName;
    }

    public void setOneAgentName(String oneAgentName) {
        this.oneAgentName = oneAgentName;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public BigDecimal getOemShare() {
        return oemShare;
    }

    public void setOemShare(BigDecimal oemShare) {
        this.oemShare = oemShare;
    }

    public BigDecimal getPlateShare() {
        return plateShare;
    }

    public void setPlateShare(BigDecimal plateShare) {
        this.plateShare = plateShare;
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
    }

    public BigDecimal getProvideAmout() {
        return provideAmout;
    }

    public void setProvideAmout(BigDecimal provideAmout) {
        this.provideAmout = provideAmout;
    }

    public List<ExchangeActivateShare> getShareList() {
        return shareList;
    }

    public void setShareList(List<ExchangeActivateShare> shareList) {
        this.shareList = shareList;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public Date getAccTime() {
        return accTime;
    }

    public void setAccTime(Date accTime) {
        this.accTime = accTime;
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
}
