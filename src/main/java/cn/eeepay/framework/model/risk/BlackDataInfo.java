package cn.eeepay.framework.model.risk;

/**
 * 风控黑名单资料
 * @author MXG
 * create 2018/12/21
 */
public class BlackDataInfo {
    private String id;
    private String orderNo; //处理单号
    private String merchantNo; //商户编号
    private String merchantName;//商户名称
    private String merchantPhone;//商户手机号
    private String merchantIdCard;//法人身份证号
    private String merBusinessProId;//商户业务产品表id
    private String recommendedSource;//推广来源 :正常,1:为超级推，2：代理商推荐的商户，3人人代理推)
    private String lawyer;//商户法人姓名
    private String transOrderNo; //交易订单编号
    private String accountNo;//交易卡号
    private String bankName;//交易卡所属银行
    private String merLastDealStatus; //商户最后处理状态 0初始化 1 未处理 2已处理
    private String riskLastDealStatus; //风控最后处理状态 0未处理 1已处理 2解冻
    private String riskLastDealTemplateNo; //风控最后处理模板编号
    private String riskLastDealOperator; //风控最后处理人
    private String riskLastRemark; //风控备注
    private String blackCreateRemark;//黑名单备注
    private String oneAgentNo; //
    private String oneAgentName; //
    private String agentNode; //代理商节点
    private String agentNo; //代理商编号
    private String agentName; //代理商名称
    private String haveTriggerHis; //商户是否有历史触发记录 0没有 1有
    private String teamId; //
    private String teamName; //
    private String blackCreator; //该商户黑名单记录的创建人
    private String merRiskRulesNo; //商户所有触发过的风险事件规则编号
    private String createTime;
    private String createTimeBegin;
    private String createTimeEnd;
    //private String lastUpdateTime;
    private String riskLastDealTime;//风控最后处理时间
    private String riskLastDealTimeBegin;
    private String riskLastDealTimeEnd;
    private String merLastDealTime; //商户最后回复时间
    private String merLastDealTimeBegin;
    private String merLastDealTimeEnd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getMerchantIdCard() {
        return merchantIdCard;
    }

    public void setMerchantIdCard(String merchantIdCard) {
        this.merchantIdCard = merchantIdCard;
    }

    public String getMerBusinessProId() {
        return merBusinessProId;
    }

    public void setMerBusinessProId(String merBusinessProId) {
        this.merBusinessProId = merBusinessProId;
    }

    public String getRecommendedSource() {
        return recommendedSource;
    }

    public void setRecommendedSource(String recommendedSource) {
        this.recommendedSource = recommendedSource;
    }

    public String getLawyer() {
        return lawyer;
    }

    public void setLawyer(String lawyer) {
        this.lawyer = lawyer;
    }

    public String getTransOrderNo() {
        return transOrderNo;
    }

    public void setTransOrderNo(String transOrderNo) {
        this.transOrderNo = transOrderNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getMerLastDealStatus() {
        return merLastDealStatus;
    }

    public void setMerLastDealStatus(String merLastDealStatus) {
        this.merLastDealStatus = merLastDealStatus;
    }

    public String getRiskLastDealStatus() {
        return riskLastDealStatus;
    }

    public void setRiskLastDealStatus(String riskLastDealStatus) {
        this.riskLastDealStatus = riskLastDealStatus;
    }

    public String getRiskLastDealTemplateNo() {
        return riskLastDealTemplateNo;
    }

    public void setRiskLastDealTemplateNo(String riskLastDealTemplateNo) {
        this.riskLastDealTemplateNo = riskLastDealTemplateNo;
    }

    public String getRiskLastDealOperator() {
        return riskLastDealOperator;
    }

    public void setRiskLastDealOperator(String riskLastDealOperator) {
        this.riskLastDealOperator = riskLastDealOperator;
    }

    public String getRiskLastRemark() {
        return riskLastRemark;
    }

    public void setRiskLastRemark(String riskLastRemark) {
        this.riskLastRemark = riskLastRemark;
    }

    public String getBlackCreateRemark() {
        return blackCreateRemark;
    }

    public void setBlackCreateRemark(String blackCreateRemark) {
        this.blackCreateRemark = blackCreateRemark;
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

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode;
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

    public String getHaveTriggerHis() {
        return haveTriggerHis;
    }

    public void setHaveTriggerHis(String haveTriggerHis) {
        this.haveTriggerHis = haveTriggerHis;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getBlackCreator() {
        return blackCreator;
    }

    public void setBlackCreator(String blackCreator) {
        this.blackCreator = blackCreator;
    }

    public String getMerRiskRulesNo() {
        return merRiskRulesNo;
    }

    public void setMerRiskRulesNo(String merRiskRulesNo) {
        this.merRiskRulesNo = merRiskRulesNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(String createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getRiskLastDealTime() {
        return riskLastDealTime;
    }

    public void setRiskLastDealTime(String riskLastDealTime) {
        this.riskLastDealTime = riskLastDealTime;
    }

    public String getRiskLastDealTimeBegin() {
        return riskLastDealTimeBegin;
    }

    public void setRiskLastDealTimeBegin(String riskLastDealTimeBegin) {
        this.riskLastDealTimeBegin = riskLastDealTimeBegin;
    }

    public String getRiskLastDealTimeEnd() {
        return riskLastDealTimeEnd;
    }

    public void setRiskLastDealTimeEnd(String riskLastDealTimeEnd) {
        this.riskLastDealTimeEnd = riskLastDealTimeEnd;
    }

    public String getMerLastDealTime() {
        return merLastDealTime;
    }

    public void setMerLastDealTime(String merLastDealTime) {
        this.merLastDealTime = merLastDealTime;
    }

    public String getMerLastDealTimeBegin() {
        return merLastDealTimeBegin;
    }

    public void setMerLastDealTimeBegin(String merLastDealTimeBegin) {
        this.merLastDealTimeBegin = merLastDealTimeBegin;
    }

    public String getMerLastDealTimeEnd() {
        return merLastDealTimeEnd;
    }

    public void setMerLastDealTimeEnd(String merLastDealTimeEnd) {
        this.merLastDealTimeEnd = merLastDealTimeEnd;
    }
}
