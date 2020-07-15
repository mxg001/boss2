package cn.eeepay.framework.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 信用卡还款订单
 * @author liuks
 * 对应表 yfb_repay_plan
 */
public class CreditRepayOrder {
    private int id;//id

    private String merchantNo;//'业务商户号' 还款人id

    private String batchNo;//'批次号' 订单id

    private String cardNo;//'卡片编号' 银行卡号

    private BigDecimal repayAmount;//'还款总金额'

    private BigDecimal minRepayAmount;//还款总金额条件过滤区间
    private BigDecimal maxRepayAmount;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date repayBeginTime;//'还款开始时间'

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date repayEndTime;//'还款结束时间'

    private String ensureAmountRate;//'保证金比例'

    private BigDecimal ensureAmount;//'保证金'

    private BigDecimal minEnsureAmount;//保证金条件过滤区间
    private BigDecimal maxEnsureAmount;

    private String status;//'计划状态 0：初始化（未生成还款明细），1：未执行（还没执行还款明细），2：还款中，3：还款成功，4：还款失败'

    private String repayFeeRate;//'手续费扣除比例+固定费用，如0.6%+2'

    private BigDecimal repayFee;//'手续费'

    private BigDecimal minRepayFee;//手续费条件过滤区间
    private BigDecimal maxRepayFee;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//'创建时间'

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeBegin;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;//'最后更新时间'

    private String mobileNo;//手机号码

    private String nickname;//用户昵称

    private String userName;//用户姓名

    private String accountNo;//银行卡号

    private String bankName;//银行名称

    private String mission;//任务

    private String billingStatus;//入账状态

    //汇总时候数据统计
    private BigDecimal repayAmountAll;
    private BigDecimal ensureAmountAll;
    private BigDecimal repayFeeAll;
    private BigDecimal ensureAmountFreezingAll;//订单状态为0,3的除外，冻结金额

    private String idCardNo;//身份证号

    private int repayNum;//还款计划次数

    private String tallyOrderNo;

    private BigDecimal successRepayAmount;//已成功还款总金额

    private BigDecimal successPayAmount;//已成功消费总金额

    private int successRepayNum;//已成功还款总笔数

    private BigDecimal actualPayFee;//商户实际交易手续费

    private BigDecimal actualWithdrawFee;//商户实际代付手续费

    private Date completeTime;//计划变为终态时间
    private String sCompleteTime;//条件查询 终态时间
    private String eCompleteTime;//条件查询 终态时间

    private String acqCode;//交易所走通道

    private String containSub;	// 查询条件,是否包含下级
    private String agentNode;
    private String oneAgentName;
    private String agentName;

    private String repayType;
    
    private String provinceName;
    private String cityName;
    private String remark;//备注
    private String resMsg;//终止原因

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getRepayType() {
		return repayType;
	}

	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}

	public String getContainSub() {
		return containSub;
	}

	public void setContainSub(String containSub) {
		this.containSub = containSub;
	}

	public String getAgentNode() {
		return agentNode;
	}

	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}

	public String getOneAgentName() {
		return oneAgentName;
	}

	public void setOneAgentName(String oneAgentName) {
		this.oneAgentName = oneAgentName;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	public String getsCompleteTime() {
		return sCompleteTime;
	}

	public void setsCompleteTime(String sCompleteTime) {
		this.sCompleteTime = sCompleteTime;
	}

	public String geteCompleteTime() {
		return eCompleteTime;
	}

	public void seteCompleteTime(String eCompleteTime) {
		this.eCompleteTime = eCompleteTime;
	}

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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public BigDecimal getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }

    public BigDecimal getMinRepayAmount() {
        return minRepayAmount;
    }

    public void setMinRepayAmount(BigDecimal minRepayAmount) {
        this.minRepayAmount = minRepayAmount;
    }

    public BigDecimal getMaxRepayAmount() {
        return maxRepayAmount;
    }

    public void setMaxRepayAmount(BigDecimal maxRepayAmount) {
        this.maxRepayAmount = maxRepayAmount;
    }

    public Date getRepayBeginTime() {
        return repayBeginTime;
    }

    public void setRepayBeginTime(Date repayBeginTime) {
        this.repayBeginTime = repayBeginTime;
    }

    public Date getRepayEndTime() {
        return repayEndTime;
    }

    public void setRepayEndTime(Date repayEndTime) {
        this.repayEndTime = repayEndTime;
    }

    public String getEnsureAmountRate() {
        return ensureAmountRate;
    }

    public void setEnsureAmountRate(String ensureAmountRate) {
        this.ensureAmountRate = ensureAmountRate;
    }

    public BigDecimal getEnsureAmount() {
        return ensureAmount;
    }

    public void setEnsureAmount(BigDecimal ensureAmount) {
        this.ensureAmount = ensureAmount;
    }

    public BigDecimal getMinEnsureAmount() {
        return minEnsureAmount;
    }

    public void setMinEnsureAmount(BigDecimal minEnsureAmount) {
        this.minEnsureAmount = minEnsureAmount;
    }

    public BigDecimal getMaxEnsureAmount() {
        return maxEnsureAmount;
    }

    public void setMaxEnsureAmount(BigDecimal maxEnsureAmount) {
        this.maxEnsureAmount = maxEnsureAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRepayFeeRate() {
        return repayFeeRate;
    }

    public void setRepayFeeRate(String repayFeeRate) {
        this.repayFeeRate = repayFeeRate;
    }

    public BigDecimal getRepayFee() {
        return repayFee;
    }

    public void setRepayFee(BigDecimal repayFee) {
        this.repayFee = repayFee;
    }

    public BigDecimal getMinRepayFee() {
        return minRepayFee;
    }

    public void setMinRepayFee(BigDecimal minRepayFee) {
        this.minRepayFee = minRepayFee;
    }

    public BigDecimal getMaxRepayFee() {
        return maxRepayFee;
    }

    public void setMaxRepayFee(BigDecimal maxRepayFee) {
        this.maxRepayFee = maxRepayFee;
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

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getBillingStatus() {
        return billingStatus;
    }

    public void setBillingStatus(String billingStatus) {
        this.billingStatus = billingStatus;
    }

    public BigDecimal getRepayAmountAll() {
        return repayAmountAll;
    }

    public void setRepayAmountAll(BigDecimal repayAmountAll) {
        this.repayAmountAll = repayAmountAll;
    }

    public BigDecimal getEnsureAmountAll() {
        return ensureAmountAll;
    }

    public void setEnsureAmountAll(BigDecimal ensureAmountAll) {
        this.ensureAmountAll = ensureAmountAll;
    }

    public BigDecimal getRepayFeeAll() {
        return repayFeeAll;
    }

    public void setRepayFeeAll(BigDecimal repayFeeAll) {
        this.repayFeeAll = repayFeeAll;
    }

    public BigDecimal getEnsureAmountFreezingAll() {
        return ensureAmountFreezingAll;
    }

    public void setEnsureAmountFreezingAll(BigDecimal ensureAmountFreezingAll) {
        this.ensureAmountFreezingAll = ensureAmountFreezingAll;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public int getRepayNum() {
        return repayNum;
    }

    public void setRepayNum(int repayNum) {
        this.repayNum = repayNum;
    }

    public String getTallyOrderNo() {
        return tallyOrderNo;
    }

    public void setTallyOrderNo(String tallyOrderNo) {
        this.tallyOrderNo = tallyOrderNo;
    }

    public BigDecimal getSuccessRepayAmount() {
        return successRepayAmount;
    }

    public void setSuccessRepayAmount(BigDecimal successRepayAmount) {
        this.successRepayAmount = successRepayAmount;
    }

    public BigDecimal getSuccessPayAmount() {
        return successPayAmount;
    }

    public void setSuccessPayAmount(BigDecimal successPayAmount) {
        this.successPayAmount = successPayAmount;
    }

    public int getSuccessRepayNum() {
        return successRepayNum;
    }

    public void setSuccessRepayNum(int successRepayNum) {
        this.successRepayNum = successRepayNum;
    }

    public BigDecimal getActualPayFee() {
        return actualPayFee;
    }

    public void setActualPayFee(BigDecimal actualPayFee) {
        this.actualPayFee = actualPayFee;
    }

    public BigDecimal getActualWithdrawFee() {
        return actualWithdrawFee;
    }

    public void setActualWithdrawFee(BigDecimal actualWithdrawFee) {
        this.actualWithdrawFee = actualWithdrawFee;
    }

    public String getAcqCode() {
        return acqCode;
    }

    public void setAcqCode(String acqCode) {
        this.acqCode = acqCode;
    }
}
