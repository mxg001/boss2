package cn.eeepay.framework.model.risk;

import cn.eeepay.framework.model.CollectiveTransOrder;
import cn.eeepay.framework.model.PosCardBin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/9/7/007.
 * @author  liuks
 * 调单管理
 */
public class SurveyOrder {

    private  Integer id;
    private  String orderNo;//调单单号
    private  String merchantNo;//商户编号
    private  String transOrderNo;//交易订单编号
    private  String acqReferenceNo ;//收单机构参考号
    private  String transOrderDatabase;//交易订单所在数据库 now当前库 old历史库
    private  String orderServiceCode;//业务类型编号，对应字典表数据
    private  String orderTypeCode;//调单类型编号,对应字典表数据

    private  Date replyEndTime;//回复截止时间
    private  Date replyEndTimeBegin;
    private  Date replyEndTimeEnd;

    private  String templateFilesName;//模板附件,多个以,隔开
    private  String orderRemark;//调单添加说明
    private  String orderStatus;//调单状态（针对调单）0异常（已删除） 1正常
    private  String replyStatus;//回复状态（针对代理商） 0未提交，1已提交，2已确认，3已逾期，4逾期提交，5逾期确认
    private  String dealStatus;//调单处理状态(针对业务人员) 0未处理，1部分提供，2持卡人承认交易，3全部提供，4无法提供，5逾期部分提供，6逾期全部提供，7逾期未回，8已回退，9已处理完，无需代理商提交资料
    private  String dealRemark;//调单处理备注说明
    private  String agentNode;//代理商编号节点
    private  BigDecimal transAmount;//交易金额
    private  String transAccountNo;//交易卡号
    private  Date transTime;//交易时间
    private  String acqCode;//收单机构编号
    private  String acqMerchantNo;//收单机构商户编号
    private  String payMethod;//交易方式 1 POS，2 支付宝，3 微信，4 快捷
    private  Integer urgeNum;//催单次数
    private  String haveAddDeduct;//是否添加了扣款  0没有添加 1已添加
    private  String agentDeductDealStatus;//代理商扣款处理状态 0未处理 1已处理
    private  String agentDeductDealRemark;//代理商扣款处理备注
    private  String agentIssueDealStatus;//代理商下发处理状态 0未处理 1已处理
    private  String agentIssueDealRemark;//代理商下发处理备注

    private  Date createTime;//创建时间
    private Date createTimeBegin;
    private Date createTimeEnd;

    private  Date lastUpdateTime;//数据最后更新时间

    private  Integer dataSta;//数据库
    private  Integer orderSta;//调单处理

    private  String agentNo;//所属代理商
    private  String agentName;
    private  String oneAgentNo;//一级代理商
    private  String oneAgentName;

    private  Integer bool;//是否包含下级

    private  String ids;//选中的集合 ,间隔

    private  BigDecimal acqDeductAmount;//上游扣款金额
    private  Date acqDeductTime;//上游扣款时间
    private  String acqDeductRemark;//上游扣款备注
    private  String operator;//操作人


    private  String merchantName;//商户名称
    private  String merchantPhone;//商户手机号
    private  String acqMerchantName;//收单机构商户名称

    private  String saleName;//一级代理商所属销售


    private CollectiveTransOrder transOrder;

    private List<SurveyReply> replyList;
    private SurveyReply reply;

    private List<SurveyOrderLog> logList;

    private  List<FileType> templateList;//模板数组

    private String creator;//创建人
    private String acqReplyStatus;//上游回复状态 0未回复 1已回复
    private String acqReplyRemark;//上游回复备注
    private String transStatus;//交易状态
    private Date lastReplyTime;//代理商最后回复时间
    private Date lastReplyTimeBegin;
    private Date lastReplyTimeEnd;

    private PosCardBin card;//交易卡类型

    private String paUserNo;//超级盟主用户编号
    private String userNode;//超级盟主用户节点

    private String replyTypeName;//回复的角色类型名称


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

    public String getOrderTypeCode() {
        return orderTypeCode;
    }

    public void setOrderTypeCode(String orderTypeCode) {
        this.orderTypeCode = orderTypeCode;
    }

    public Date getReplyEndTime() {
        return replyEndTime;
    }

    public void setReplyEndTime(Date replyEndTime) {
        this.replyEndTime = replyEndTime;
    }

    public String getTemplateFilesName() {
        return templateFilesName;
    }

    public void setTemplateFilesName(String templateFilesName) {
        this.templateFilesName = templateFilesName;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getReplyStatus() {
        return replyStatus;
    }

    public void setReplyStatus(String replyStatus) {
        this.replyStatus = replyStatus;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }

    public String getDealRemark() {
        return dealRemark;
    }

    public void setDealRemark(String dealRemark) {
        this.dealRemark = dealRemark;
    }

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode;
    }

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }

    public String getAcqCode() {
        return acqCode;
    }

    public void setAcqCode(String acqCode) {
        this.acqCode = acqCode;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Integer getUrgeNum() {
        return urgeNum;
    }

    public void setUrgeNum(Integer urgeNum) {
        this.urgeNum = urgeNum;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getDataSta() {
        return dataSta;
    }

    public void setDataSta(Integer dataSta) {
        this.dataSta = dataSta;
    }

    public Integer getOrderSta() {
        return orderSta;
    }

    public void setOrderSta(Integer orderSta) {
        this.orderSta = orderSta;
    }

    public String getTransAccountNo() {
        return transAccountNo;
    }

    public void setTransAccountNo(String transAccountNo) {
        this.transAccountNo = transAccountNo;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public String getAcqMerchantNo() {
        return acqMerchantNo;
    }

    public void setAcqMerchantNo(String acqMerchantNo) {
        this.acqMerchantNo = acqMerchantNo;
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

    public Date getReplyEndTimeBegin() {
        return replyEndTimeBegin;
    }

    public void setReplyEndTimeBegin(Date replyEndTimeBegin) {
        this.replyEndTimeBegin = replyEndTimeBegin;
    }

    public Date getReplyEndTimeEnd() {
        return replyEndTimeEnd;
    }

    public void setReplyEndTimeEnd(Date replyEndTimeEnd) {
        this.replyEndTimeEnd = replyEndTimeEnd;
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

    public Integer getBool() {
        return bool;
    }

    public void setBool(Integer bool) {
        this.bool = bool;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
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

    public String getAcqDeductRemark() {
        return acqDeductRemark;
    }

    public void setAcqDeductRemark(String acqDeductRemark) {
        this.acqDeductRemark = acqDeductRemark;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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

    public String getAcqMerchantName() {
        return acqMerchantName;
    }

    public void setAcqMerchantName(String acqMerchantName) {
        this.acqMerchantName = acqMerchantName;
    }

    public CollectiveTransOrder getTransOrder() {
        return transOrder;
    }

    public void setTransOrder(CollectiveTransOrder transOrder) {
        this.transOrder = transOrder;
    }

    public List<SurveyReply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<SurveyReply> replyList) {
        this.replyList = replyList;
    }

    public SurveyReply getReply() {
        return reply;
    }

    public void setReply(SurveyReply reply) {
        this.reply = reply;
    }

    public List<SurveyOrderLog> getLogList() {
        return logList;
    }

    public void setLogList(List<SurveyOrderLog> logList) {
        this.logList = logList;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public List<FileType> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(List<FileType> templateList) {
        this.templateList = templateList;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getAcqReplyStatus() {
        return acqReplyStatus;
    }

    public void setAcqReplyStatus(String acqReplyStatus) {
        this.acqReplyStatus = acqReplyStatus;
    }

    public String getAcqReplyRemark() {
        return acqReplyRemark;
    }

    public void setAcqReplyRemark(String acqReplyRemark) {
        this.acqReplyRemark = acqReplyRemark;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public Date getLastReplyTime() {
        return lastReplyTime;
    }

    public void setLastReplyTime(Date lastReplyTime) {
        this.lastReplyTime = lastReplyTime;
    }

    public Date getLastReplyTimeBegin() {
        return lastReplyTimeBegin;
    }

    public void setLastReplyTimeBegin(Date lastReplyTimeBegin) {
        this.lastReplyTimeBegin = lastReplyTimeBegin;
    }

    public Date getLastReplyTimeEnd() {
        return lastReplyTimeEnd;
    }

    public void setLastReplyTimeEnd(Date lastReplyTimeEnd) {
        this.lastReplyTimeEnd = lastReplyTimeEnd;
    }

    public PosCardBin getCard() {
        return card;
    }

    public void setCard(PosCardBin card) {
        this.card = card;
    }

    public String getPaUserNo() {
        return paUserNo;
    }

    public void setPaUserNo(String paUserNo) {
        this.paUserNo = paUserNo;
    }

    public String getUserNode() {
        return userNode;
    }

    public void setUserNode(String userNode) {
        this.userNode = userNode;
    }

    public String getReplyTypeName() {
        return replyTypeName;
    }

    public void setReplyTypeName(String replyTypeName) {
        this.replyTypeName = replyTypeName;
    }
}
