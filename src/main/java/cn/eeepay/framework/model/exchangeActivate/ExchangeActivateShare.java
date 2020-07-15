package cn.eeepay.framework.model.exchangeActivate;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/17/017.
 * 分润
 * 对应表 yfb_agent_share_detail
 */
public class ExchangeActivateShare{

    private  long id;//

    private  String orderNo;//关联订单号

    private  String agentNo;//代理商编号
    private  String agentName;

    private  String shareRate;//分润比例

    private  String shareGrade;//分润级别

    private  String shareType;//分润类型 A 激活  D 报单

    private  BigDecimal amount;//报单金额

    private  BigDecimal nextShareAmount;//分给下级的分润金额

    private  BigDecimal totalShareAmount;//当前订单的总分润金额

    private  BigDecimal shareAmount;//分润金额

    private  String shareStatus;//分润状态

    private  String checkStatus;//审核状态

    private Date checkTime;//审核时间

    private  String checkOper;//审核人

    private  String accStatus;//入账 状态 0 未入账 1 已入账

    private Date accTime;//入账时间
    private Date accTimeBegin;
    private Date accTimeEnd;


    private  String remark;//备注信息

    private Date createTime;//创建时间
    private Date createTimeBegin;
    private Date createTimeEnd;

    private String orderStatus;//订单状态
    private  String merNo;//贡献人商户号
    private String mobileUsername;//贡献人手机号
    private String userName;//贡献人名称
    private String realName;//真实姓名

    private String oemNo;//贡献人组织编码
    private String oemName;//贡献人组织名称

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getShareRate() {
        return shareRate;
    }

    public void setShareRate(String shareRate) {
        this.shareRate = shareRate;
    }

    public String getShareGrade() {
        return shareGrade;
    }

    public void setShareGrade(String shareGrade) {
        this.shareGrade = shareGrade;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getNextShareAmount() {
        return nextShareAmount;
    }

    public void setNextShareAmount(BigDecimal nextShareAmount) {
        this.nextShareAmount = nextShareAmount;
    }

    public BigDecimal getTotalShareAmount() {
        return totalShareAmount;
    }

    public void setTotalShareAmount(BigDecimal totalShareAmount) {
        this.totalShareAmount = totalShareAmount;
    }

    public BigDecimal getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(BigDecimal shareAmount) {
        this.shareAmount = shareAmount;
    }

    public String getShareStatus() {
        return shareStatus;
    }

    public void setShareStatus(String shareStatus) {
        this.shareStatus = shareStatus;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckOper() {
        return checkOper;
    }

    public void setCheckOper(String checkOper) {
        this.checkOper = checkOper;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getMerNo() {
        return merNo;
    }

    public void setMerNo(String merNo) {
        this.merNo = merNo;
    }

    public String getMobileUsername() {
        return mobileUsername;
    }

    public void setMobileUsername(String mobileUsername) {
        this.mobileUsername = mobileUsername;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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
