package cn.eeepay.framework.model.exchangeActivate;

import cn.eeepay.framework.model.exchange.WriteOffHis;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/4/17/017.
 * @author  liuks
 * 积分兑换
 * 对应表 yfb_order  yfb_declare_order
 */
public class ExchangeActivateOrder {
    private long id;//id
    private String orderNo;//订单号
    private String merNo;//商户号
    private String orderType;//订单类型 A 激活订单 D 报单单
    private String orderStatus;//订单状态 INIT 初始化 SUB 已提交 SUCCESS 成功 FAILED 失败 UNKNOW 未知
    private String shareStatus;//分润状态 1 未分润 2 已分润
    private String remark;//备注信息
    private String accStatus;//记账状态0 未记账 1 记账成功 2 记账失败 3 已提交记账

    private Date accTime;//入账时间
    private Date accTimeBegin;
    private Date accTimeEnd;

    private BigDecimal plateShare;//平台分润
    private BigDecimal oemShare;//品牌分润

    private Date createTime;//创建时间
    private Date createTimeBegin;
    private Date createTimeEnd;

    private String orgCode;//机构编码
    private String orgName;//机构名称
    private String oemNo;//组织编码
    private String oemName;//组织名称
    private String typeCode;//产品类别code
    private String typeName;//产品类别名称
    private long productId;//产品id
    private String productName;//产品名称

    private String uploadImage;//上传附件
    private String execNum;//兑换数量
    private String redeemCode;//兑换码
    private Date validityDateStart;//有效期 开始时间
    private Date validityDateEnd;//有效期 结束时间

    private String logisticsInfo;//物流信息
    private String productRemark;//订单备注
    private String checkStatus;//审核状态:0审核中;1审核成功;2审核失败
    private String checkOper;//审核人
    private Date checkTime;//审核时间
    private Date checkTimeBegin;
    private Date checkTimeEnd;

    private String checkReason;//审核原因


    private BigDecimal price;//金额
    private String receiveStatus;//收货 状态 0未收到 1已收到

    private String userName;//商户名称
    private String realName;//真实姓名
    private String mobileUsername;//商户手机号
    private String idCardNo;//商户身份证号

    private String declarePeople;//报单人

    private BigDecimal originPrice;//商品基础价钱
    private BigDecimal originalPrice;//券面价格

    private String channel;//渠道,数据字典配置
    private String saleOrderNo;//核销渠道订单号
    private BigDecimal writeOffPrice;//核销价格
    private String checkStatusOne;//一次核销状态

    private String batchNo;//导入批次号

    private String oneAgentNo;//一级代理商编号
    private String oneAgentName;
    private String agentNo;//所属代理商编号
    private String agentName;

    List<WriteOffHis> writeOffHisList;

    List<ExchangeActivateShare> shareList;

    private String channelCheckStatus;//上游审核状态:0审核中;1审核成功;2审核失败
    private String channelCheckReason;//上游审核原因
    private Date channelCheckTime;//上游审核时间
    private Date channelCheckTimeBegin;
    private Date channelCheckTimeEnd;

    private String platformFee;//平台成本
    private String shareRate;//商户分润比例
    private String oemShareRate;//OEM分润比例
    private BigDecimal oemFee;//成本
    private BigDecimal oemShareFee;//OEM固定成本


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

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
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

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
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

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUploadImage() {
        return uploadImage;
    }

    public void setUploadImage(String uploadImage) {
        this.uploadImage = uploadImage;
    }

    public String getExecNum() {
        return execNum;
    }

    public void setExecNum(String execNum) {
        this.execNum = execNum;
    }

    public String getRedeemCode() {
        return redeemCode;
    }

    public void setRedeemCode(String redeemCode) {
        this.redeemCode = redeemCode;
    }

    public Date getValidityDateStart() {
        return validityDateStart;
    }

    public void setValidityDateStart(Date validityDateStart) {
        this.validityDateStart = validityDateStart;
    }

    public Date getValidityDateEnd() {
        return validityDateEnd;
    }

    public void setValidityDateEnd(Date validityDateEnd) {
        this.validityDateEnd = validityDateEnd;
    }

    public String getLogisticsInfo() {
        return logisticsInfo;
    }

    public void setLogisticsInfo(String logisticsInfo) {
        this.logisticsInfo = logisticsInfo;
    }

    public String getProductRemark() {
        return productRemark;
    }

    public void setProductRemark(String productRemark) {
        this.productRemark = productRemark;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckOper() {
        return checkOper;
    }

    public void setCheckOper(String checkOper) {
        this.checkOper = checkOper;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Date getCheckTimeBegin() {
        return checkTimeBegin;
    }

    public void setCheckTimeBegin(Date checkTimeBegin) {
        this.checkTimeBegin = checkTimeBegin;
    }

    public Date getCheckTimeEnd() {
        return checkTimeEnd;
    }

    public void setCheckTimeEnd(Date checkTimeEnd) {
        this.checkTimeEnd = checkTimeEnd;
    }

    public String getCheckReason() {
        return checkReason;
    }

    public void setCheckReason(String checkReason) {
        this.checkReason = checkReason;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
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

    public String getMobileUsername() {
        return mobileUsername;
    }

    public void setMobileUsername(String mobileUsername) {
        this.mobileUsername = mobileUsername;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getDeclarePeople() {
        return declarePeople;
    }

    public void setDeclarePeople(String declarePeople) {
        this.declarePeople = declarePeople;
    }

    public BigDecimal getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(BigDecimal originPrice) {
        this.originPrice = originPrice;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSaleOrderNo() {
        return saleOrderNo;
    }

    public void setSaleOrderNo(String saleOrderNo) {
        this.saleOrderNo = saleOrderNo;
    }

    public BigDecimal getWriteOffPrice() {
        return writeOffPrice;
    }

    public void setWriteOffPrice(BigDecimal writeOffPrice) {
        this.writeOffPrice = writeOffPrice;
    }

    public String getCheckStatusOne() {
        return checkStatusOne;
    }

    public void setCheckStatusOne(String checkStatusOne) {
        this.checkStatusOne = checkStatusOne;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public List<WriteOffHis> getWriteOffHisList() {
        return writeOffHisList;
    }

    public void setWriteOffHisList(List<WriteOffHis> writeOffHisList) {
        this.writeOffHisList = writeOffHisList;
    }

    public List<ExchangeActivateShare> getShareList() {
        return shareList;
    }

    public void setShareList(List<ExchangeActivateShare> shareList) {
        this.shareList = shareList;
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

    public String getChannelCheckStatus() {
        return channelCheckStatus;
    }

    public void setChannelCheckStatus(String channelCheckStatus) {
        this.channelCheckStatus = channelCheckStatus;
    }

    public String getChannelCheckReason() {
        return channelCheckReason;
    }

    public void setChannelCheckReason(String channelCheckReason) {
        this.channelCheckReason = channelCheckReason;
    }

    public Date getChannelCheckTime() {
        return channelCheckTime;
    }

    public void setChannelCheckTime(Date channelCheckTime) {
        this.channelCheckTime = channelCheckTime;
    }

    public Date getChannelCheckTimeBegin() {
        return channelCheckTimeBegin;
    }

    public void setChannelCheckTimeBegin(Date channelCheckTimeBegin) {
        this.channelCheckTimeBegin = channelCheckTimeBegin;
    }

    public Date getChannelCheckTimeEnd() {
        return channelCheckTimeEnd;
    }

    public void setChannelCheckTimeEnd(Date channelCheckTimeEnd) {
        this.channelCheckTimeEnd = channelCheckTimeEnd;
    }

    public String getPlatformFee() {
        return platformFee;
    }

    public void setPlatformFee(String platformFee) {
        this.platformFee = platformFee;
    }

    public String getShareRate() {
        return shareRate;
    }

    public void setShareRate(String shareRate) {
        this.shareRate = shareRate;
    }

    public String getOemShareRate() {
        return oemShareRate;
    }

    public void setOemShareRate(String oemShareRate) {
        this.oemShareRate = oemShareRate;
    }

    public BigDecimal getOemFee() {
        return oemFee;
    }

    public void setOemFee(BigDecimal oemFee) {
        this.oemFee = oemFee;
    }

    public BigDecimal getOemShareFee() {
        return oemShareFee;
    }

    public void setOemShareFee(BigDecimal oemShareFee) {
        this.oemShareFee = oemShareFee;
    }
}
