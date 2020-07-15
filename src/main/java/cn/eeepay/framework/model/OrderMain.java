package cn.eeepay.framework.model;

import cn.eeepay.framework.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * table:super.order_main
 * desc:订单表
 * @author tans
 * @date 2017-11-30
 */
public class OrderMain {
    private Long id;//ID

    private String orderNo;//订单编号

    private String userCode;//订单用户编码

    private Long orgId;//组织id

    private String orgName;//组织名称

    private String orderType;//订单类型:1代理授权；2信用卡申请 3收款 4信用卡还款

    private String status;//订单状态:1已创建；2待支付；3:待审核 4已授权 5订单成功 6订单失败  9已关闭

    private Date createDate;//创建时间

    private BigDecimal totalBonus;//总发放奖金

    private String shareUserCode;//分享链接用户

    private String oneUserCode;//1级收益用户编号

    private String oneUserType;//1级身份

    private BigDecimal oneUserProfit;//1级分润

    private BigDecimal orgProfit;//品牌商(组织)分润; =品牌的固定分润 + 分润剩余

    private BigDecimal orgLeftProfit;//用户奖金分配后的剩余收益归组织

    private BigDecimal plateProfit;//平台分润值; =银行或贷款返还-组织成本

    private String accountStatus;//记账状态;0未记账；1已记账

    private Date payDate;//支付时间

    private String payMethod;//支付方式

    private String payChannel;//付款通道：V2,WEIXIN

    private String payOrderNo;//上游的支付订单号

    private String updateBy;//操作人

    private Date updateDate;//操作时间

    private BigDecimal price;//售价

    private Long bankSourceId;//银行id

    private String bankName;//银行名称

    private String orderName;//订单名称

    private String orderPhone;//订单手机号

    private String orderIdNo;//订单证件号

    private Long loanSourceId;//放款机构id

    private String loanName;//放款机构名称

    private BigDecimal loanAmount;//贷款金额

    private String loanPushPro;//发放比例

    private String receiveAgentId;//收款商户id

    private BigDecimal receiveAmount;//收款金额

    private String repaymentAgentId;//还款商户id

    private String remark;//备注

    private String twoUserCode;//2级收益用户编号

    private String twoUserType;//2级身份

    private BigDecimal twoUserProfit;//2级分润

    private String thrUserCode;//3级收益用户编号

    private String thrUserType;//3级身份

    private BigDecimal thrUserProfit;//3级分润

    private String fouUserCode;//4级收益用户编号

    private String fouUserType;//4级身份

    private BigDecimal fouUserProfit;//4级分润

    private String createDateStart;
    private String createDateEnd;
    private String payDateStart;
    private String payDateEnd;
    private String createDateStr;
    private String payDateStr;
    private String shareUserName;
    private String userName;//贡献人名称
    private String oneUserName;//一级贡献人名称
    private String twoUserName;//二级贡献人名称
    private String thrUserName;//三级贡献人名称
    private String fouUserName;//四级贡献人名称
    private String oneProfitFormula;//一级分润规则
    private String twoProfitFormula;//二级分润规则
    private String thrProfitFormula;//三级分润规则
    private String fouProfitFormula;//四级分润规则
    private String plateProfitFormula;//平台分润规则
    private String orgProfitFormula;//组织的分润规则
    private String[] orderTypeList;//订单类型的集合
    private String oneMerchantNo;
    private String twoMerchantNo;
    private String thrMerchantNo;
    private String fouMerchantNo;
    private String batchNo;//批次号
    private BigDecimal repaymentAmount;//还款金额
    private String transRate;//收款还款等交易的费率(%)
    private String agentInputRate;//代理授收钱公司成本费率
    private BigDecimal agentInputFee;//收钱公司成本费
    private BigDecimal creditcardBankBonus;//信用卡办理银行发放总奖金
    private String loanBankRate;//贷款机构奖金发放扣率(金额或者百分比)
    private String loanOrgRate;//贷款-品牌代理成本扣率
    private String loanOrgBonus;//贷款-品牌发放总奖金扣率
    private String receiveOrgRate;//收款-代理成本
    private String receiveOrgBonus;//品牌发放总奖金扣率(拿去分的钱)
    private String profitStatus;//计算分润状态 0为计算失败 1为计算成功
    private String orderPhoneStart;
    private String orderPhoneEnd;
    private String refundStatus;//退款状态，0未退款 1已退款
    private Date refundDate;//退款时间
    private String refundDateStr;//退款时间
    private String refundDateStart;
    private String refundDateEnd;
    private String refundMsg;//退款原由
    private String shareUserPhone;//贡献人手机号
    private String payChannelNo;//收款通道商户号
    private String refundOrderNo;//退款订单号
    private String openProvince;//订单贡献人省
    private String openCity;//订单贡献人市
    private String openRegion;//订单贡献人区
    private Date completeDate;//订单完成时间
    private String completeDateStr;
    private String completeDateStart;
    private String completeDateEnd;
    private String repayTransCardNo;//还款卡号
    private BigDecimal repayTransscucons;
    private String repayTransChannel;//还款通道
    private String repayTransStatus;//还款状态:3成功，4失败，6终止
    private String companyCostRate;//公司成本扣率
    private String userRemark;
    private BigDecimal repayTransfee;//还款手续费(金额)
    private BigDecimal repayTransfeeAdd;//还款手续费额外(金额)

    private BigDecimal creditcardBankBonus2;//信用卡办理银行发放总奖金(首刷)
    private BigDecimal totalBonus2;//品牌总发放奖金(首刷)
    private BigDecimal oneUserProfit2;//一级分润(首刷)
    private BigDecimal twoUserProfit2;//二级分润(首刷)
    private BigDecimal thrUserProfit2;//三级分润(首刷)
    private BigDecimal fouUserProfit2;//四级分润(首刷)
    private BigDecimal orgProfit2;//品牌商(组织)分润(首刷)
    private BigDecimal plateProfit2;//平台分润值(首刷)
    private String accountStatus2;//(首刷)记账状态;0待入账；1已记账；2记账失败
    private String profitStatus2;//(首刷)计算分润状态2  0为计算失败 1为计算成功

    private Date payDate2;//首刷分润时间
    private String payDate2Str;//首刷分润时间
    private String payDate2Start;//首刷分润时间
    private String payDate2End;//首刷分润时间
    private String bankNickName;//银行别称
    private String bankCode;//银行编码
    private String ruleCode;//银行导入匹配规则编码
    private String oldStatus;//之前的状态

    private String loanType;//贷款类型，1:有效注册，2有效借款，3授信成功
    private String loanTypeStr;//贷款类型，1:有效注册，2有效借款，3授信成功
    private String profitType;//1-固定奖金，2-按比例发放
    private String profitTypeStr;//1-固定奖金，2-按比例发放
    private String companyBonusConf;//(配置)公司截留奖金
    private String orgBonusConf;//(配置)品牌截留奖金
    private BigDecimal companyBonus;//(奖金)公司截留奖金
    private BigDecimal orgBonus;//(奖金)品牌截留奖金

    private String batchNo2;//（首刷）批次号2
    private String loanAlias;//贷款机构别称

    private String bonusType;//1发卡，2首刷

    private String orderNameStart;
    private String orderNameEnd;
    private Integer orderNameEndLength;
    private Integer orderNameLength;

    private String productName;		//保险产品名称
    private String productType;		//保险产品种类
    private String companyNickName;	//保险公司别称
    private Long companyNo;		//保险公司Id
    private String upperProductId;//上游产品id
    private Integer productId;//产品id
    private String insuranceName;//被保人姓名
    private String insurancePhone;//被保人手机号
    private String insuranceIdNo;//被保人证件号
    private  BigDecimal productPrice;//保险价格
    private String bonusSettleTime;//奖金结算方式
    private BigDecimal bxzongAmount;//保险总奖金
    private String loanOrderNo;//上游借款订单号
    private BigDecimal realPlatProfit;//平台实际分润

    private BigDecimal adjustRatio;//调节系数

    private  BigDecimal basicBonusAmount;//领地基准分红

    private BigDecimal bonusAmount;//领地分红

    private String redUserCode; //领地分红领取用户编号

    private String redUserName;//领地分红用户姓名

    private Date receiveTime;//领取时间

    private String receiveTimeStr;//领取时间

    private BigDecimal territoryAvgPrice;//领地均价

    private BigDecimal territoryPrice;//领地价格

    private  long redId;

    private  BigDecimal redAmount;

    private  String rate;

    private int rateType;
    private int ppStatus;
    private int accessWay;
    private String proofreadingResult;

    private String proofreadingMethod;

    public int getAccessWay() {
        return accessWay;
    }

    public void setAccessWay(int accessWay) {
        this.accessWay = accessWay;
    }

    public int getPpStatus() {
        return ppStatus;
    }

    public void setPpStatus(int ppStatus) {
        this.ppStatus = ppStatus;
    }

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
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode == null ? null : userCode.trim();
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName == null ? null : orgName.trim();
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(BigDecimal totalBonus) {
        this.totalBonus = totalBonus;
    }

    public String getShareUserCode() {
        return shareUserCode;
    }

    public void setShareUserCode(String shareUserCode) {
        this.shareUserCode = shareUserCode == null ? null : shareUserCode.trim();
    }

    public BigDecimal getOrgProfit() {
        return orgProfit != null ? orgProfit : BigDecimal.ZERO;
    }

    public void setOrgProfit(BigDecimal orgProfit) {
        this.orgProfit = orgProfit;
    }

    public BigDecimal getOrgLeftProfit() {
        return orgLeftProfit;
    }

    public void setOrgLeftProfit(BigDecimal orgLeftProfit) {
        this.orgLeftProfit = orgLeftProfit;
    }

    public BigDecimal getPlateProfit() {
        return plateProfit != null ? plateProfit : BigDecimal.ZERO;
    }

    public void setPlateProfit(BigDecimal plateProfit) {
        this.plateProfit = plateProfit;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus == null ? null : accountStatus.trim();
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel == null ? null : payChannel.trim();
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo == null ? null : payOrderNo.trim();
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public BigDecimal getPrice() {
        return price != null ? price :BigDecimal.ZERO;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getBankSourceId() {
        return bankSourceId;
    }

    public void setBankSourceId(Long bankSourceId) {
        this.bankSourceId = bankSourceId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone == null ? null : orderPhone.trim();
    }

    public String getOrderIdNo() {
        return orderIdNo;
    }

    public void setOrderIdNo(String orderIdNo) {
        this.orderIdNo = orderIdNo == null ? null : orderIdNo.trim();
    }

    public Long getLoanSourceId() {
        return loanSourceId;
    }

    public void setLoanSourceId(Long loanSourceId) {
        this.loanSourceId = loanSourceId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName == null ? null : loanName.trim();
    }

    public BigDecimal getLoanAmount() {
        return loanAmount != null ? loanAmount : BigDecimal.ZERO;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanPushPro() {
        return loanPushPro;
    }

    public void setLoanPushPro(String loanPushPro) {
        this.loanPushPro = loanPushPro == null ? null : loanPushPro.trim();
    }

    public String getReceiveAgentId() {
        return receiveAgentId;
    }

    public void setReceiveAgentId(String receiveAgentId) {
        this.receiveAgentId = receiveAgentId == null ? null : receiveAgentId.trim();
    }

    public BigDecimal getReceiveAmount() {
        return receiveAmount != null ? receiveAmount.setScale(2, BigDecimal.ROUND_DOWN) : BigDecimal.ZERO;
    }

    public void setReceiveAmount(BigDecimal receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public String getRepaymentAgentId() {
        return repaymentAgentId;
    }

    public void setRepaymentAgentId(String repaymentAgentId) {
        this.repaymentAgentId = repaymentAgentId == null ? null : repaymentAgentId.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getOneUserCode() {
        return oneUserCode;
    }

    public void setOneUserCode(String oneUserCode) {
        this.oneUserCode = oneUserCode == null ? null : oneUserCode.trim();
    }

    public String getOneUserType() {
        return oneUserType;
    }

    public void setOneUserType(String oneUserType) {
        this.oneUserType = oneUserType == null ? null : oneUserType.trim();
    }

    public BigDecimal getOneUserProfit() {
        return oneUserProfit != null ? oneUserProfit : BigDecimal.ZERO;
    }

    public void setOneUserProfit(BigDecimal oneUserProfit) {
        this.oneUserProfit = oneUserProfit;
    }

    public String getTwoUserCode() {
        return twoUserCode;
    }

    public void setTwoUserCode(String twoUserCode) {
        this.twoUserCode = twoUserCode == null ? null : twoUserCode.trim();
    }

    public String getTwoUserType() {
        return twoUserType;
    }

    public void setTwoUserType(String twoUserType) {
        this.twoUserType = twoUserType == null ? null : twoUserType.trim();
    }

    public BigDecimal getTwoUserProfit() {
        return twoUserProfit != null ? twoUserProfit : BigDecimal.ZERO;
    }

    public void setTwoUserProfit(BigDecimal twoUserProfit) {
        this.twoUserProfit = twoUserProfit;
    }

    public String getThrUserCode() {
        return thrUserCode;
    }

    public void setThrUserCode(String thrUserCode) {
        this.thrUserCode = thrUserCode == null ? null : thrUserCode.trim();
    }

    public String getThrUserType() {
        return thrUserType;
    }

    public void setThrUserType(String thrUserType) {
        this.thrUserType = thrUserType == null ? null : thrUserType.trim();
    }

    public BigDecimal getThrUserProfit() {
        return thrUserProfit != null ? thrUserProfit : BigDecimal.ZERO;
    }

    public void setThrUserProfit(BigDecimal thrUserProfit) {
        this.thrUserProfit = thrUserProfit;
    }

    public String getFouUserCode() {
        return fouUserCode;
    }

    public void setFouUserCode(String fouUserCode) {
        this.fouUserCode = fouUserCode;
    }

    public String getFouUserType() {
        return fouUserType;
    }

    public void setFouUserType(String fouUserType) {
        this.fouUserType = fouUserType;
    }

    public BigDecimal getFouUserProfit() {
        return fouUserProfit != null ? fouUserProfit : BigDecimal.ZERO;
    }

    public void setFouUserProfit(BigDecimal fouUserProfit) {
        this.fouUserProfit = fouUserProfit;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getCreateDateStart() {
        return createDateStart;
    }

    public void setCreateDateStart(String createDateStart) {
        this.createDateStart = createDateStart;
    }

    public String getCreateDateEnd() {
        return createDateEnd;
    }

    public void setCreateDateEnd(String createDateEnd) {
        this.createDateEnd = createDateEnd;
    }

    public String getPayDateStart() {
        return payDateStart;
    }

    public void setPayDateStart(String payDateStart) {
        this.payDateStart = payDateStart;
    }

    public String getPayDateEnd() {
        return payDateEnd;
    }

    public void setPayDateEnd(String payDateEnd) {
        this.payDateEnd = payDateEnd;
    }

    public String getShareUserName() {
        return shareUserName;
    }

    public void setShareUserName(String shareUserName) {
        this.shareUserName = shareUserName;
    }

    public String getOneUserName() {
        return oneUserName;
    }

    public void setOneUserName(String oneUserName) {
        this.oneUserName = oneUserName;
    }

    public String getTwoUserName() {
        return twoUserName;
    }

    public void setTwoUserName(String twoUserName) {
        this.twoUserName = twoUserName;
    }

    public String getThrUserName() {
        return thrUserName;
    }

    public void setThrUserName(String thrUserName) {
        this.thrUserName = thrUserName;
    }

    public String getFouUserName() {
        return fouUserName;
    }

    public void setFouUserName(String fouUserName) {
        this.fouUserName = fouUserName;
    }

    public String getCreateDateStr() {
        return createDate == null ? "" : DateUtil.getLongFormatDate(createDate);
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public String getPayDateStr() {
        return payDate == null ? "" : DateUtil.getLongFormatDate(payDate);
    }

    public void setPayDateStr(String payDateStr) {
        this.payDateStr = payDateStr;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOneProfitFormula() {
        return oneProfitFormula;
    }

    public void setOneProfitFormula(String oneProfitFormula) {
        this.oneProfitFormula = oneProfitFormula;
    }

    public String getTwoProfitFormula() {
        return twoProfitFormula;
    }

    public void setTwoProfitFormula(String twoProfitFormula) {
        this.twoProfitFormula = twoProfitFormula;
    }

    public String getThrProfitFormula() {
        return thrProfitFormula;
    }

    public void setThrProfitFormula(String thrProfitFormula) {
        this.thrProfitFormula = thrProfitFormula;
    }

    public String getFouProfitFormula() {
        return fouProfitFormula;
    }

    public void setFouProfitFormula(String fouProfitFormula) {
        this.fouProfitFormula = fouProfitFormula;
    }

    public String getPlateProfitFormula() {
        return plateProfitFormula;
    }

    public void setPlateProfitFormula(String plateProfitFormula) {
        this.plateProfitFormula = plateProfitFormula;
    }

    public String getOrgProfitFormula() {
        return orgProfitFormula;
    }

    public void setOrgProfitFormula(String orgProfitFormula) {
        this.orgProfitFormula = orgProfitFormula;
    }

    public String[] getOrderTypeList() {
        return orderTypeList;
    }

    public void setOrderTypeList(String[] orderTypeList) {
        this.orderTypeList = orderTypeList;
    }

    public String getOneMerchantNo() {
        return oneMerchantNo;
    }

    public void setOneMerchantNo(String oneMerchantNo) {
        this.oneMerchantNo = oneMerchantNo;
    }

    public String getTwoMerchantNo() {
        return twoMerchantNo;
    }

    public void setTwoMerchantNo(String twoMerchantNo) {
        this.twoMerchantNo = twoMerchantNo;
    }

    public String getThrMerchantNo() {
        return thrMerchantNo;
    }

    public void setThrMerchantNo(String thrMerchantNo) {
        this.thrMerchantNo = thrMerchantNo;
    }

    public String getFouMerchantNo() {
        return fouMerchantNo;
    }

    public void setFouMerchantNo(String fouMerchantNo) {
        this.fouMerchantNo = fouMerchantNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public BigDecimal getRepaymentAmount() {
        return repaymentAmount != null ? repaymentAmount.setScale(2, BigDecimal.ROUND_DOWN) : BigDecimal.ZERO;
    }

    public void setRepaymentAmount(BigDecimal repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public String getTransRate() {
        return transRate;
    }

    public void setTransRate(String transRate) {
        this.transRate = transRate;
    }

    public String getAgentInputRate() {
        return agentInputRate;
    }

    public void setAgentInputRate(String agentInputRate) {
        this.agentInputRate = agentInputRate;
    }

    public BigDecimal getAgentInputFee() {
        return agentInputFee != null ? agentInputFee : BigDecimal.ZERO;
    }

    public void setAgentInputFee(BigDecimal agentInputFee) {
        this.agentInputFee = agentInputFee;
    }

    public BigDecimal getCreditcardBankBonus() {
        return creditcardBankBonus != null ? creditcardBankBonus : BigDecimal.ZERO;
    }

    public void setCreditcardBankBonus(BigDecimal creditcardBankBonus) {
        this.creditcardBankBonus = creditcardBankBonus;
    }

    public String getLoanBankRate() {
        return loanBankRate;
    }

    public void setLoanBankRate(String loanBankRate) {
        this.loanBankRate = loanBankRate;
    }

    public String getLoanOrgRate() {
        return loanOrgRate;
    }

    public void setLoanOrgRate(String loanOrgRate) {
        this.loanOrgRate = loanOrgRate;
    }

    public String getLoanOrgBonus() {
        return loanOrgBonus;
    }

    public void setLoanOrgBonus(String loanOrgBonus) {
        this.loanOrgBonus = loanOrgBonus;
    }

    public String getReceiveOrgRate() {
        return receiveOrgRate;
    }

    public void setReceiveOrgRate(String receiveOrgRate) {
        this.receiveOrgRate = receiveOrgRate;
    }

    public String getReceiveOrgBonus() {
        return receiveOrgBonus;
    }

    public void setReceiveOrgBonus(String receiveOrgBonus) {
        this.receiveOrgBonus = receiveOrgBonus;
    }

    public String getProfitStatus() {
        return profitStatus;
    }

    public void setProfitStatus(String profitStatus) {
        this.profitStatus = profitStatus;
    }

    public String getOrderPhoneStart() {
        return orderPhoneStart;
    }

    public void setOrderPhoneStart(String orderPhoneStart) {
        this.orderPhoneStart = orderPhoneStart;
    }

    public String getOrderPhoneEnd() {
        return orderPhoneEnd;
    }

    public void setOrderPhoneEnd(String orderPhoneEnd) {
        this.orderPhoneEnd = orderPhoneEnd;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Date getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
    }

    public String getRefundMsg() {
        return refundMsg;
    }

    public void setRefundMsg(String refundMsg) {
        this.refundMsg = refundMsg;
    }

    public String getRefundDateStr() {
        return refundDate == null ? "" : DateUtil.getLongFormatDate(refundDate);
    }

    public void setRefundDateStr(String refundDateStr) {
        this.refundDateStr = refundDateStr;
    }

    public String getShareUserPhone() {
        return shareUserPhone;
    }

    public void setShareUserPhone(String shareUserPhone) {
        this.shareUserPhone = shareUserPhone;
    }

    public String getPayChannelNo() {
        return payChannelNo;
    }

    public void setPayChannelNo(String payChannelNo) {
        this.payChannelNo = payChannelNo;
    }

    public String getRefundDateStart() {
        return refundDateStart;
    }

    public void setRefundDateStart(String refundDateStart) {
        this.refundDateStart = refundDateStart;
    }

    public String getRefundDateEnd() {
        return refundDateEnd;
    }

    public void setRefundDateEnd(String refundDateEnd) {
        this.refundDateEnd = refundDateEnd;
    }

    public String getRefundOrderNo() {
        return refundOrderNo;
    }

    public void setRefundOrderNo(String refundOrderNo) {
        this.refundOrderNo = refundOrderNo;
    }

    public String getOpenProvince() {
        return openProvince;
    }

    public void setOpenProvince(String openProvince) {
        this.openProvince = openProvince;
    }

    public String getOpenCity() {
        return openCity;
    }

    public void setOpenCity(String openCity) {
        this.openCity = openCity;
    }

    public String getOpenRegion() {
        return openRegion;
    }

    public void setOpenRegion(String openRegion) {
        this.openRegion = openRegion;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public String getCompleteDateStr() {
        return completeDate == null ? "" : DateUtil.getLongFormatDate(completeDate);
    }

    public void setCompleteDateStr(String completeDateStr) {
        this.completeDateStr = completeDateStr;
    }

    public String getCompleteDateStart() {
        return completeDateStart;
    }

    public void setCompleteDateStart(String completeDateStart) {
        this.completeDateStart = completeDateStart;
    }

    public String getCompleteDateEnd() {
        return completeDateEnd;
    }

    public void setCompleteDateEnd(String completeDateEnd) {
        this.completeDateEnd = completeDateEnd;
    }

    public String getRepayTransCardNo() {
        return repayTransCardNo;
    }

    public void setRepayTransCardNo(String repayTransCardNo) {
        this.repayTransCardNo = repayTransCardNo;
    }

    public String getRepayTransChannel() {
        return repayTransChannel;
    }

    public void setRepayTransChannel(String repayTransChannel) {
        this.repayTransChannel = repayTransChannel;
    }

    public String getRepayTransStatus() {
        return repayTransStatus;
    }

    public void setRepayTransStatus(String repayTransStatus) {
        this.repayTransStatus = repayTransStatus;
    }

    public String getCompanyCostRate() {
        return companyCostRate;
    }

    public void setCompanyCostRate(String companyCostRate) {
        this.companyCostRate = companyCostRate;
    }

    public String getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }

    public BigDecimal getRepayTransfee() {
        return repayTransfee != null ? repayTransfee.setScale(2, BigDecimal.ROUND_DOWN) : BigDecimal.ZERO;
    }

    public void setRepayTransfee(BigDecimal repayTransfee) {
        this.repayTransfee = repayTransfee;
    }

    public BigDecimal getRepayTransfeeAdd() {
        return repayTransfeeAdd;
    }

    public void setRepayTransfeeAdd(BigDecimal repayTransfeeAdd) {
        this.repayTransfeeAdd = repayTransfeeAdd;
    }

    public BigDecimal getCreditcardBankBonus2() {
        return creditcardBankBonus2 != null ? creditcardBankBonus2 : BigDecimal.ZERO;
    }

    public void setCreditcardBankBonus2(BigDecimal creditcardBankBonus2) {
        this.creditcardBankBonus2 = creditcardBankBonus2;
    }

    public BigDecimal getTotalBonus2() {
        return totalBonus2 != null ? totalBonus2 : BigDecimal.ZERO;
    }

    public void setTotalBonus2(BigDecimal totalBonus2) {
        this.totalBonus2 = totalBonus2;
    }

    public BigDecimal getOneUserProfit2() {
        return oneUserProfit2 != null ? oneUserProfit2 : BigDecimal.ZERO;
    }

    public void setOneUserProfit2(BigDecimal oneUserProfit2) {
        this.oneUserProfit2 = oneUserProfit2;
    }

    public BigDecimal getTwoUserProfit2() {
        return twoUserProfit2 != null ? twoUserProfit2 : BigDecimal.ZERO;
    }

    public void setTwoUserProfit2(BigDecimal twoUserProfit2) {
        this.twoUserProfit2 = twoUserProfit2;
    }

    public BigDecimal getThrUserProfit2() {
        return thrUserProfit2 != null ? thrUserProfit2 : BigDecimal.ZERO;
    }

    public void setThrUserProfit2(BigDecimal thrUserProfit2) {
        this.thrUserProfit2 = thrUserProfit2;
    }

    public BigDecimal getFouUserProfit2() {
        return fouUserProfit2 != null ? fouUserProfit2 : BigDecimal.ZERO;
    }

    public void setFouUserProfit2(BigDecimal fouUserProfit2) {
        this.fouUserProfit2 = fouUserProfit2;
    }

    public BigDecimal getOrgProfit2() {
        return orgProfit2 != null ? orgProfit2 : BigDecimal.ZERO;
    }

    public void setOrgProfit2(BigDecimal orgProfit2) {
        this.orgProfit2 = orgProfit2;
    }

    public BigDecimal getPlateProfit2() {
        return plateProfit2 != null ? plateProfit2 : BigDecimal.ZERO;
    }

    public void setPlateProfit2(BigDecimal plateProfit2) {
        this.plateProfit2 = plateProfit2;
    }

    public String getAccountStatus2() {
        return accountStatus2;
    }

    public void setAccountStatus2(String accountStatus2) {
        this.accountStatus2 = accountStatus2;
    }

    public String getProfitStatus2() {
        return profitStatus2;
    }

    public void setProfitStatus2(String profitStatus2) {
        this.profitStatus2 = profitStatus2;
    }

    public Date getPayDate2() {
        return payDate2;
    }

    public void setPayDate2(Date payDate2) {
        this.payDate2 = payDate2;
    }

    public String getPayDate2Str() {
        return  payDate2 == null ? "" : DateUtil.getLongFormatDate(payDate2);
    }

    public void setPayDate2Str(String payDate2Str) {
        this.payDate2Str = payDate2Str;
    }

    public String getPayDate2Start() {
        return payDate2Start;
    }

    public void setPayDate2Start(String payDate2Start) {
        this.payDate2Start = payDate2Start;
    }

    public String getPayDate2End() {
        return payDate2End;
    }

    public void setPayDate2End(String payDate2End) {
        this.payDate2End = payDate2End;
    }

    public String getBankNickName() {
        return bankNickName;
    }

    public void setBankNickName(String bankNickName) {
        this.bankNickName = bankNickName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getProfitType() {
        return profitType;
    }

    public void setProfitType(String profitType) {
        this.profitType = profitType;
    }

    public String getCompanyBonusConf() {
        return companyBonusConf;
    }

    public void setCompanyBonusConf(String companyBonusConf) {
        this.companyBonusConf = companyBonusConf;
    }

    public String getOrgBonusConf() {
        return orgBonusConf;
    }

    public void setOrgBonusConf(String orgBonusConf) {
        this.orgBonusConf = orgBonusConf;
    }

    public BigDecimal getCompanyBonus() {
        return companyBonus;
    }

    public void setCompanyBonus(BigDecimal companyBonus) {
        this.companyBonus = companyBonus;
    }

    public BigDecimal getOrgBonus() {
        return orgBonus;
    }

    public void setOrgBonus(BigDecimal orgBonus) {
        this.orgBonus = orgBonus;
    }

    public String getLoanTypeStr() {
        return loanTypeStr;
    }

    public void setLoanTypeStr(String loanTypeStr) {
        this.loanTypeStr = loanTypeStr;
    }

    public String getProfitTypeStr() {
        return profitTypeStr;
    }

    public void setProfitTypeStr(String profitTypeStr) {
        this.profitTypeStr = profitTypeStr;
    }

    public String getBatchNo2() {
        return batchNo2;
    }

    public void setBatchNo2(String batchNo2) {
        this.batchNo2 = batchNo2;
    }

    public String getLoanAlias() {
        return loanAlias;
    }

    public void setLoanAlias(String loanAlias) {
        this.loanAlias = loanAlias;
    }

    public String getBonusType() {
        return bonusType;
    }

    public void setBonusType(String bonusType) {
        this.bonusType = bonusType;
    }

    public String getOrderNameStart() {
        return orderNameStart;
    }

    public void setOrderNameStart(String orderNameStart) {
        this.orderNameStart = orderNameStart;
    }

    public String getOrderNameEnd() {
        return orderNameEnd;
    }

    public void setOrderNameEnd(String orderNameEnd) {
        this.orderNameEnd = orderNameEnd;
    }

    public Integer getOrderNameLength() {
        return orderNameLength;
    }

    public void setOrderNameLength(Integer orderNameLength) {
        this.orderNameLength = orderNameLength;
    }

    public Integer getOrderNameEndLength() {
        return orderNameEndLength;
    }

    public void setOrderNameEndLength(Integer orderNameEndLength) {
        this.orderNameEndLength = orderNameEndLength;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCompanyNickName() {
        return companyNickName;
    }

    public void setCompanyNickName(String companyNickName) {
        this.companyNickName = companyNickName;
    }

    public Long getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(Long companyNo) {
        this.companyNo = companyNo;
    }

    public String getUpperProductId() {
        return upperProductId;
    }

    public void setUpperProductId(String upperProductId) {
        this.upperProductId = upperProductId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public String getInsurancePhone() {
        return insurancePhone;
    }

    public void setInsurancePhone(String insurancePhone) {
        this.insurancePhone = insurancePhone;
    }

    public String getInsuranceIdNo() {
        return insuranceIdNo;
    }

    public void setInsuranceIdNo(String insuranceIdNo) {
        this.insuranceIdNo = insuranceIdNo;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getBonusSettleTime() {
        return bonusSettleTime;
    }

    public void setBonusSettleTime(String bonusSettleTime) {
        this.bonusSettleTime = bonusSettleTime;
    }

    public BigDecimal getBxzongAmount() {
        return bxzongAmount;
    }

    public void setBxzongAmount(BigDecimal bxzongAmount) {
        this.bxzongAmount = bxzongAmount;
    }

    public BigDecimal getRealPlatProfit() {
        return realPlatProfit;
    }

    public void setRealPlatProfit(BigDecimal realPlatProfit) {
        this.realPlatProfit = realPlatProfit;
    }

    public BigDecimal getAdjustRatio() {
        return adjustRatio;
    }

    public void setAdjustRatio(BigDecimal adjustRatio) {
        this.adjustRatio = adjustRatio;
    }

    public BigDecimal getBasicBonusAmount() {
        return basicBonusAmount;
    }

    public void setBasicBonusAmount(BigDecimal basicBonusAmount) {
        this.basicBonusAmount = basicBonusAmount;
    }

    public BigDecimal getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(BigDecimal bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public String getRedUserCode() {
        return redUserCode;
    }

    public void setRedUserCode(String redUserCode) {
        this.redUserCode = redUserCode;
    }

    public String getLoanOrderNo() {
        return loanOrderNo;
    }

    public void setLoanOrderNo(String loanOrderNo) {
        this.loanOrderNo = loanOrderNo;
    }

    public String getRedUserName() {
        return redUserName;
    }

    public void setRedUserName(String redUserName) {
        this.redUserName = redUserName;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public BigDecimal getTerritoryAvgPrice() {
        return territoryAvgPrice;
    }

    public void setTerritoryAvgPrice(BigDecimal territoryAvgPrice) {
        this.territoryAvgPrice = territoryAvgPrice;
    }

    public BigDecimal getTerritoryPrice() {
        return territoryPrice;
    }

    public void setTerritoryPrice(BigDecimal territoryPrice) {
        this.territoryPrice = territoryPrice;
    }

    public String getReceiveTimeStr() {
        return receiveTimeStr;
    }

    public void setReceiveTimeStr(String receiveTimeStr) {
        this.receiveTimeStr = receiveTimeStr;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getRateType() {
        return rateType;
    }

    public void setRateType(int rateType) {
        this.rateType = rateType;
    }

    public long getRedId() {
        return redId;
    }

    public void setRedId(long redId) {
        this.redId = redId;
    }

    public BigDecimal getRedAmount() {
        return redAmount;
    }

    public void setRedAmount(BigDecimal redAmount) {
        this.redAmount = redAmount;
    }

	public BigDecimal getRepayTransscucons() {
		return repayTransscucons == null ? BigDecimal.ZERO : repayTransscucons.setScale(2,BigDecimal.ROUND_DOWN);
	}

	public void setRepayTransscucons(BigDecimal repayTransscucons) {
		this.repayTransscucons = repayTransscucons;
	}

    public String getProofreadingResult() {
        return proofreadingResult;
    }

    public void setProofreadingResult(String proofreadingResult) {
        this.proofreadingResult = proofreadingResult;
    }

    public String getProofreadingMethod() {
        return proofreadingMethod;
    }

    public void setProofreadingMethod(String proofreadingMethod) {
        this.proofreadingMethod = proofreadingMethod;
    }
}