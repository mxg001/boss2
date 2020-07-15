package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;


/**
 * table merchant_business_product
 * desc 商户业务产品表
 */
public class MerchantBusinessProduct {
    private Long id;

    private String merchantNo;

    private String bpId;

    //申请时间
    private Date createTime;

    private String saleName;

    private String status;

    private String examinationOpinions;

    private Integer autoCheckTimes;
    //商户名称
    private String merchantName;

    private String merchantType;

    private String auditorId;

    private Date merCreateTime;

    //商户手机号
    private String mobilePhone;

    //业务产品名称
    private String bpName;

    //代理商名称
    private String agentName;

    //组织ID
    private String teamId;
    private String teamName;
    //子组织
    private String teamEntryId;
    private String teamEntryName;

    //交易模式
    private String tradeType;
    //直清同步状态
    private String syncStatus;

    private Date reexamineTime;
    private String reexamineStatus;
    private String reexamineOperator;

    private String recommendedSource;    //推广来源

    private String channelName;//自动审件通道名字

    private String autoMbpChannel;//自动审件通道编码

    private Integer merCreditCardStatus;//是否绑定信用卡

    private String ocr;
    private String face;

    private String autoMbpChannelName;
    private String ocrName;
    private String faceName;

    private Integer auditNum;

    private String auditIds;//商户审核锁定的ID集合

    private String dayNum;//间隔天数

    private String realAuthChannel;//鉴权通道
    private String realAuthChannelName;//鉴权通道名称


    private String huoTiChannel;//自动审核活体通道


    private String acqMerchantNo;
    private String acqMerchantName;
    private String specialMerchant;
    private String bindGeneralMerchantTime;


    public String getBindGeneralMerchantTime() {
        return bindGeneralMerchantTime;
    }

    public void setBindGeneralMerchantTime(String bindGeneralMerchantTime) {
        this.bindGeneralMerchantTime = bindGeneralMerchantTime;
    }

    private Integer sourceSysSta;
    private String sourceSys;//是否是代理商推广


    public String getRealAuthChannelName() {
        return realAuthChannelName;
    }

    public void setRealAuthChannelName(String realAuthChannelName) {
        this.realAuthChannelName = realAuthChannelName;
    }

    public String getHuoTiChannel() {
        return huoTiChannel;
    }

    public void setHuoTiChannel(String huoTiChannel) {
        this.huoTiChannel = huoTiChannel;
    }

    public String getDayNum() {
        return dayNum;
    }

    public void setDayNum(String dayNum) {
        this.dayNum = dayNum;
    }

    public String getAuditIds() {
        return auditIds;
    }

    public void setAuditIds(String auditIds) {
        this.auditIds = auditIds;
    }

    public Integer getAuditNum() {
        return auditNum;
    }

    public void setAuditNum(Integer auditNum) {
        this.auditNum = auditNum;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getAutoMbpChannel() {
        return autoMbpChannel;
    }

    public void setAutoMbpChannel(String autoMbpChannel) {
        this.autoMbpChannel = autoMbpChannel;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getRecommendedSource() {
        return recommendedSource;
    }

    public void setRecommendedSource(String recommendedSource) {
        this.recommendedSource = recommendedSource;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getMerStatus() {
        return merStatus;
    }

    public void setMerStatus(String merStatus) {
        this.merStatus = merStatus;
    }

    private String riskStatus;
    private String merAccount;
    private String itemSource;//进件来源
    private String userName;

    private BigDecimal controlAmount;//冻结金额
    private BigDecimal preFrozenAmount;//预冻结金额

    private String merStatus;
    private String content;


    private String activityCode;//活动002


    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName == null ? null : saleName.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getExaminationOpinions() {
        return examinationOpinions;
    }

    public void setExaminationOpinions(String examinationOpinions) {
        this.examinationOpinions = examinationOpinions == null ? null : examinationOpinions.trim();
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getBpName() {
        return bpName;
    }

    public void setBpName(String bpName) {
        this.bpName = bpName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Date getMerCreateTime() {
        return merCreateTime;
    }

    public void setMerCreateTime(Date merCreateTime) {
        this.merCreateTime = merCreateTime;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
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

    public String getBpId() {
        return bpId;
    }

    public void setBpId(String bpId) {
        this.bpId = bpId;
    }

    public String getMerAccount() {
        return merAccount;
    }

    public void setMerAccount(String merAccount) {
        this.merAccount = merAccount;
    }

    public String getItemSource() {
        return itemSource;
    }

    public void setItemSource(String itemSource) {
        this.itemSource = itemSource;
    }

    public String getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public Integer getAutoCheckTimes() {
        return autoCheckTimes;
    }

    public void setAutoCheckTimes(Integer autoCheckTimes) {
        this.autoCheckTimes = autoCheckTimes;
    }

    public BigDecimal getControlAmount() {
        return controlAmount;
    }

    public void setControlAmount(BigDecimal controlAmount) {
        this.controlAmount = controlAmount;
    }

    public BigDecimal getPreFrozenAmount() {
        return preFrozenAmount;
    }

    public void setPreFrozenAmount(BigDecimal preFrozenAmount) {
        this.preFrozenAmount = preFrozenAmount;
    }


    public String getReexamineOperator() {
        return reexamineOperator;
    }

    public void setReexamineOperator(String reexamineOperator) {
        this.reexamineOperator = reexamineOperator;
    }

    public Date getReexamineTime() {
        return reexamineTime;
    }

    public void setReexamineTime(Date reexamineTime) {
        this.reexamineTime = reexamineTime;
    }

    public String getReexamineStatus() {
        return reexamineStatus;
    }

    public void setReexamineStatus(String reexamineStatus) {
        this.reexamineStatus = reexamineStatus;
    }

    public Integer getMerCreditCardStatus() {
        return merCreditCardStatus;
    }

    public void setMerCreditCardStatus(Integer merCreditCardStatus) {
        this.merCreditCardStatus = merCreditCardStatus;
    }


    public String getRealAuthChannel() {
        return realAuthChannel;
    }

    public void setRealAuthChannel(String realAuthChannel) {
        this.realAuthChannel = realAuthChannel;
    }

    public String getTeamEntryId() {
        return teamEntryId;
    }

    public void setTeamEntryId(String teamEntryId) {
        this.teamEntryId = teamEntryId;
    }

    public String getTeamEntryName() {
        return teamEntryName;
    }

    public void setTeamEntryName(String teamEntryName) {
        this.teamEntryName = teamEntryName;
    }

    public String getOcr() {
        return ocr;
    }

    public void setOcr(String ocr) {
        this.ocr = ocr;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getAutoMbpChannelName() {
        return autoMbpChannelName;
    }

    public void setAutoMbpChannelName(String autoMbpChannelName) {
        this.autoMbpChannelName = autoMbpChannelName;
    }

    public String getOcrName() {
        return ocrName;
    }

    public void setOcrName(String ocrName) {
        this.ocrName = ocrName;
    }

    public String getFaceName() {
        return faceName;
    }

    public void setFaceName(String faceName) {
        this.faceName = faceName;
    }

    public String getAcqMerchantNo() {
        return acqMerchantNo;
    }

    public void setAcqMerchantNo(String acqMerchantNo) {
        this.acqMerchantNo = acqMerchantNo;
    }

    public String getAcqMerchantName() {
        return acqMerchantName;
    }

    public void setAcqMerchantName(String acqMerchantName) {
        this.acqMerchantName = acqMerchantName;
    }

    public String getSpecialMerchant() {
        return specialMerchant;
    }

    public void setSpecialMerchant(String specialMerchant) {
        this.specialMerchant = specialMerchant;
    }

    public Integer getSourceSysSta() {
        return sourceSysSta;
    }

    public void setSourceSysSta(Integer sourceSysSta) {
        this.sourceSysSta = sourceSysSta;
    }

    public String getSourceSys() {
        return sourceSys;
    }

    public void setSourceSys(String sourceSys) {
        this.sourceSys = sourceSys;
    }
}