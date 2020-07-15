package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * superbank.red_account_detail
 */
public class RedAccountDetail {
    private Long id;

    private Long redAccountId;//所属账户id关联red_account_info表主键

    private String accountCode;//红包账号

    private Date createDate;//交易时间

    private String type;//交易类型(0发红包，1抢红包，2红包分润，3过期余额回收，4其他账户转入，5转出其他账户，6风控关闭红包，7风控打开关闭的红包)

    private BigDecimal transAmount;//交易金额（金额可正，可负）

    private Long redOrderId;//红包订单id关联red_orders的主键

    private String remark;//备注

    private String createDateStart;

    private String createDateEnd;

    private String busType;//业务类型 red_orders

    private String transAmountStr;
    private String beforetransredmoneytStr;
    private String accountName;
    private  String userOrgName;
    private String userType;

    private String createDateStr;

    private Integer selectType;//查询类型，1表示汇总
    private Integer methodType;//查询方法类型，1表示查询红包账户明细

    private BigDecimal transAmountSum;//汇总金额
    private BigDecimal incomeTransAmountSum;//汇总入账的金额
    private BigDecimal lossTransAmountSum;//汇总出账的金额
    
    private BigDecimal redmoney;//红包账户余额
    private String orderNo;//order_no
    private Integer relationId;//用户ID/组织编号 red_account_info
    private BigDecimal m0;
    private BigDecimal m1;
    private BigDecimal m2;
    private BigDecimal m3;
    private BigDecimal m4;
    private BigDecimal m5;
    private BigDecimal m6;
    private BigDecimal m7;
    private BigDecimal m8;
    private BigDecimal m9;
    private BigDecimal m10;
    private BigDecimal m11;
    private BigDecimal m12;
    private BigDecimal m13;

    public BigDecimal getM0() {
        return m0;
    }

    public void setM0(BigDecimal m0) {
        this.m0 = m0;
    }

    public BigDecimal getM1() {
        return m1;
    }

    public void setM1(BigDecimal m1) {
        this.m1 = m1;
    }

    public BigDecimal getM2() {
        return m2;
    }

    public void setM2(BigDecimal m2) {
        this.m2 = m2;
    }

    public BigDecimal getM3() {
        return m3;
    }

    public void setM3(BigDecimal m3) {
        this.m3 = m3;
    }

    public BigDecimal getM4() {
        return m4;
    }

    public void setM4(BigDecimal m4) {
        this.m4 = m4;
    }

    public BigDecimal getM5() {
        return m5;
    }

    public void setM5(BigDecimal m5) {
        this.m5 = m5;
    }

    public BigDecimal getM6() {
        return m6;
    }

    public void setM6(BigDecimal m6) {
        this.m6 = m6;
    }

    public BigDecimal getM7() {
        return m7;
    }

    public void setM7(BigDecimal m7) {
        this.m7 = m7;
    }

    public BigDecimal getM8() {
        return m8;
    }

    public void setM8(BigDecimal m8) {
        this.m8 = m8;
    }

    public BigDecimal getM9() {
        return m9;
    }

    public void setM9(BigDecimal m9) {
        this.m9 = m9;
    }

    public BigDecimal getM10() {
        return m10;
    }

    public void setM10(BigDecimal m10) {
        this.m10 = m10;
    }

    public BigDecimal getM11() {
        return m11;
    }

    public void setM11(BigDecimal m11) {
        this.m11 = m11;
    }

    public BigDecimal getM12() {
        return m12;
    }

    public void setM12(BigDecimal m12) {
        this.m12 = m12;
    }

    public BigDecimal getM13() {
        return m13;
    }

    public void setM13(BigDecimal m13) {
        this.m13 = m13;
    }

    public String getUserOrgName() {
        return userOrgName;
    }

    public void setUserOrgName(String userOrgName) {
        this.userOrgName = userOrgName;
    }

    public BigDecimal getIncomeTransAmountSum() {
        return incomeTransAmountSum;
    }

    public void setIncomeTransAmountSum(BigDecimal incomeTransAmountSum) {
        this.incomeTransAmountSum = incomeTransAmountSum;
    }

    public BigDecimal getLossTransAmountSum() {
        return lossTransAmountSum;
    }

    public void setLossTransAmountSum(BigDecimal lossTransAmountSum) {
        this.lossTransAmountSum = lossTransAmountSum;
    }

    public String getBeforetransredmoneytStr() {
        return beforetransredmoneytStr;
    }

    public void setBeforetransredmoneytStr(String beforetransredmoneytStr) {
        this.beforetransredmoneytStr = beforetransredmoneytStr;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public Integer getMethodType() {
        return methodType;
    }

    public void setMethodType(Integer methodType) {
        this.methodType = methodType;
    }

    public BigDecimal getRedmoney() {
		return redmoney;
	}

	public void setRedmoney(BigDecimal redmoney) {
		this.redmoney = redmoney;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	private String typeInter;//转换
    
    
    public String getTypeInter() {
		return typeInter;
	}

	public void setTypeInter(String typeInter) {
		this.typeInter = typeInter;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRedAccountId() {
        return redAccountId;
    }

    public void setRedAccountId(Long redAccountId) {
        this.redAccountId = redAccountId;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode == null ? null : accountCode.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }

    public Long getRedOrderId() {
        return redOrderId;
    }

    public void setRedOrderId(Long redOrderId) {
        this.redOrderId = redOrderId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getTransAmountStr() {
        return transAmountStr;
    }

    public void setTransAmountStr(String transAmountStr) {
        this.transAmountStr = transAmountStr;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public Integer getSelectType() {
        return selectType;
    }

    public void setSelectType(Integer selectType) {
        this.selectType = selectType;
    }

    public BigDecimal getTransAmountSum() {
        return transAmountSum;
    }

    public void setTransAmountSum(BigDecimal transAmountSum) {
        this.transAmountSum = transAmountSum;
    }
}