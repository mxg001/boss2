package cn.eeepay.framework.model.capitalInsurance;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/7/23/023.
 * @author  liuks
 * 保险订单
 * 对应表 zjx_trans_order
 * settle_transfer 出款表
 * collective_trans_order 交易表
 */
public class SafeOrder {

    private  Integer id;//id
    private  String bxOrderNo;//保险订单号
    private  String orderNo;//交易订单号(外键交易表)
    private  String thirdOrderNo;//保单号(合作方的订单号)
    private  String merchantNo;//商户号(外键商户表)
    private  String oneAgentNo;//一级代理商编号
    private  String bxUnit;//承保单位(目前只有前海财险)
    private  String prodNo;//产品编码(前海财险提供的)
    private  String prodDetail;//产品描述
    private  BigDecimal nAmt;//保额(元，保留两位小数)
    private  BigDecimal nPrm;//保费-售价(元，保留两位小数)
    private  BigDecimal nFee;//保费-成本价
    private  String bxType;//投保状态:SUCCESS：成功,FAILED：失败,INIT：初始化,OVERLIMIT：已退保
    private  String resultMsg;//结果信息

    private Date tTime;//投保时间
    private Date tTimeBegin;
    private Date tTimeEnd;
    private Date tBeginTime;//保险起期
    private Date tEndTime;//保险止期
    private Date createTime;//创建时间


    private  String settlementMethod;//结算方式 0 t0,1 t1
    private BigDecimal transAmount;//交易金额
    private String transStatus;//交易状态
    private Date transTime;//交易时间

    private Integer lowerAgent;//是否包含下级状态

    private  String oneAgentName;//一级代理商名称
    private  String agentNo;//所属代理商编号
    private  String agentName;//所属代理商编号

    private String bxTerm;//保险期限(单位天)

    private String cAppNme;//投保人客户名称
    private String cClntMrk;//投保人性质 1为自然人,0为非自然人
    private String cCertfCls;//投保人证件类型 120001居民身份证,120002护照,120003军人证,120009其他
    private String cCertfCde;//投保人证件号码
    private String cMobile;//投保人手机号码
    private String cRelCode;//与被保人关系601001雇佣,601002子女,601003父母,601004配偶,601005本人,601006其它
    private String cTelPhone;//投保人固定电话
    private String cClntAddr;//投保人地址
    private String cZipCde;//投保人邮编

    private String cNme;//被保人客户名称
    private String cClntMrk1;//被保人性质1为自然人,0为非自然人
    private String cCertTyp;//被保人证件类型 120001居民身份证,120002护照,120003军人证,120009其他
    private String cCertNo;//被保人证件号码
    private String cSex;//被保人性别1-男2-女
    private String cClntAddr1;//被保人地址
    private String cMobile1;//被保人移动电话

    private String cPayerName;//支付方名称
    private String cTranNo;//支付成交单号
    private String cArrivalTime;//约定到账时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBxOrderNo() {
        return bxOrderNo;
    }

    public void setBxOrderNo(String bxOrderNo) {
        this.bxOrderNo = bxOrderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getThirdOrderNo() {
        return thirdOrderNo;
    }

    public void setThirdOrderNo(String thirdOrderNo) {
        this.thirdOrderNo = thirdOrderNo;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getOneAgentNo() {
        return oneAgentNo;
    }

    public void setOneAgentNo(String oneAgentNo) {
        this.oneAgentNo = oneAgentNo;
    }

    public String getBxUnit() {
        return bxUnit;
    }

    public void setBxUnit(String bxUnit) {
        this.bxUnit = bxUnit;
    }

    public String getProdNo() {
        return prodNo;
    }

    public void setProdNo(String prodNo) {
        this.prodNo = prodNo;
    }

    public String getProdDetail() {
        return prodDetail;
    }

    public void setProdDetail(String prodDetail) {
        this.prodDetail = prodDetail;
    }

    public BigDecimal getnAmt() {
        return nAmt;
    }

    public void setnAmt(BigDecimal nAmt) {
        this.nAmt = nAmt;
    }

    public BigDecimal getnPrm() {
        return nPrm;
    }

    public void setnPrm(BigDecimal nPrm) {
        this.nPrm = nPrm;
    }

    public BigDecimal getnFee() {
        return nFee;
    }

    public void setnFee(BigDecimal nFee) {
        this.nFee = nFee;
    }

    public String getBxType() {
        return bxType;
    }

    public void setBxType(String bxType) {
        this.bxType = bxType;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public Date gettTime() {
        return tTime;
    }

    public void settTime(Date tTime) {
        this.tTime = tTime;
    }

    public Date gettTimeBegin() {
        return tTimeBegin;
    }

    public void settTimeBegin(Date tTimeBegin) {
        this.tTimeBegin = tTimeBegin;
    }

    public Date gettTimeEnd() {
        return tTimeEnd;
    }

    public void settTimeEnd(Date tTimeEnd) {
        this.tTimeEnd = tTimeEnd;
    }

    public Date gettBeginTime() {
        return tBeginTime;
    }

    public void settBeginTime(Date tBeginTime) {
        this.tBeginTime = tBeginTime;
    }

    public Date gettEndTime() {
        return tEndTime;
    }

    public void settEndTime(Date tEndTime) {
        this.tEndTime = tEndTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSettlementMethod() {
        return settlementMethod;
    }

    public void setSettlementMethod(String settlementMethod) {
        this.settlementMethod = settlementMethod;
    }

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public Integer getLowerAgent() {
        return lowerAgent;
    }

    public void setLowerAgent(Integer lowerAgent) {
        this.lowerAgent = lowerAgent;
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

    public String getBxTerm() {
        return bxTerm;
    }

    public void setBxTerm(String bxTerm) {
        this.bxTerm = bxTerm;
    }

    public String getcAppNme() {
        return cAppNme;
    }

    public void setcAppNme(String cAppNme) {
        this.cAppNme = cAppNme;
    }

    public String getcClntMrk() {
        return cClntMrk;
    }

    public void setcClntMrk(String cClntMrk) {
        this.cClntMrk = cClntMrk;
    }

    public String getcCertfCls() {
        return cCertfCls;
    }

    public void setcCertfCls(String cCertfCls) {
        this.cCertfCls = cCertfCls;
    }

    public String getcCertfCde() {
        return cCertfCde;
    }

    public void setcCertfCde(String cCertfCde) {
        this.cCertfCde = cCertfCde;
    }

    public String getcMobile() {
        return cMobile;
    }

    public void setcMobile(String cMobile) {
        this.cMobile = cMobile;
    }

    public String getcRelCode() {
        return cRelCode;
    }

    public void setcRelCode(String cRelCode) {
        this.cRelCode = cRelCode;
    }

    public String getcTelPhone() {
        return cTelPhone;
    }

    public void setcTelPhone(String cTelPhone) {
        this.cTelPhone = cTelPhone;
    }

    public String getcClntAddr() {
        return cClntAddr;
    }

    public void setcClntAddr(String cClntAddr) {
        this.cClntAddr = cClntAddr;
    }

    public String getcZipCde() {
        return cZipCde;
    }

    public void setcZipCde(String cZipCde) {
        this.cZipCde = cZipCde;
    }

    public String getcNme() {
        return cNme;
    }

    public void setcNme(String cNme) {
        this.cNme = cNme;
    }

    public String getcClntMrk1() {
        return cClntMrk1;
    }

    public void setcClntMrk1(String cClntMrk1) {
        this.cClntMrk1 = cClntMrk1;
    }

    public String getcCertTyp() {
        return cCertTyp;
    }

    public void setcCertTyp(String cCertTyp) {
        this.cCertTyp = cCertTyp;
    }

    public String getcCertNo() {
        return cCertNo;
    }

    public void setcCertNo(String cCertNo) {
        this.cCertNo = cCertNo;
    }

    public String getcSex() {
        return cSex;
    }

    public void setcSex(String cSex) {
        this.cSex = cSex;
    }

    public String getcClntAddr1() {
        return cClntAddr1;
    }

    public void setcClntAddr1(String cClntAddr1) {
        this.cClntAddr1 = cClntAddr1;
    }

    public String getcMobile1() {
        return cMobile1;
    }

    public void setcMobile1(String cMobile1) {
        this.cMobile1 = cMobile1;
    }

    public String getcPayerName() {
        return cPayerName;
    }

    public void setcPayerName(String cPayerName) {
        this.cPayerName = cPayerName;
    }

    public String getcTranNo() {
        return cTranNo;
    }

    public void setcTranNo(String cTranNo) {
        this.cTranNo = cTranNo;
    }

    public String getcArrivalTime() {
        return cArrivalTime;
    }

    public void setcArrivalTime(String cArrivalTime) {
        this.cArrivalTime = cArrivalTime;
    }
}
