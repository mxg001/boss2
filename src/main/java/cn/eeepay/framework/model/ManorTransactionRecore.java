package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * superbank.red_territory_order
 */
public class ManorTransactionRecore {
	private Long orderId;
	
	private String orgName;
	
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	private String orderNo; 
	private Long territoryId;  //领地id
	
	private Date payDate; //交易时间 
	private String payDateStart; //交易时间 
	private String payDateEnd; //交易时间 
	
	private Date cDate; //交易时间 
	private String cDateStart; //交易时间 
	private String cDateEnd; //交易时间 
	
	
	
	public Date getcDate() {
		return cDate;
	}
	public void setcDate(Date cDate) {
		this.cDate = cDate;
	}
	public String getcDateStart() {
		return cDateStart;
	}
	public void setcDateStart(String cDateStart) {
		this.cDateStart = cDateStart;
	}
	public String getcDateEnd() {
		return cDateEnd;
	}
	public void setcDateEnd(String cDateEnd) {
		this.cDateEnd = cDateEnd;
	}
	private Integer type ;  
	private BigDecimal sumMoney ;
	private BigDecimal sumPlatMoney;
	private BigDecimal sumPremiumMoney ;
	private BigDecimal sumDividedMoney;
	//地区
	private String provinceName;
	
	
	private BigDecimal premiumTerritory;
	private BigDecimal premiumProfit;
	private String orgBonusConf;
	
	public BigDecimal getPremiumTerritory() {
		return premiumTerritory;
	}
	public void setPremiumTerritory(BigDecimal premiumTerritory) {
		this.premiumTerritory = premiumTerritory;
	}
	public BigDecimal getPremiumProfit() {
		return premiumProfit;
	}
	public void setPremiumProfit(BigDecimal premiumProfit) {
		this.premiumProfit = premiumProfit;
	}
	public String getOrgBonusConf() {
		return orgBonusConf;
	}
	public void setOrgBonusConf(String orgBonusConf) {
		this.orgBonusConf = orgBonusConf;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public BigDecimal getSumMoney() {
		return sumMoney;
	}
	public void setSumMoney(BigDecimal sumMoney) {
		this.sumMoney = sumMoney;
	}
	public BigDecimal getSumPlatMoney() {
		return sumPlatMoney;
	}
	public void setSumPlatMoney(BigDecimal sumPlatMoney) {
		this.sumPlatMoney = sumPlatMoney;
	}
	private String cityName;
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
	private String regionName;
	//订单状态
	private String status;
	//记账状态
	private String accountStatus;
	//原领主组织id
	private Long oldLordsOrgId;
	private String oldLordsOrgName;
	//原领主组织name
	
	//原领主编号
	private String oldLordsUserCode;
	private Long oid;
	private Long nid;
	private String userName;
	private String nickName;
	private String phone;
	
	private Date oldPayDate;
	private BigDecimal oldPayPrice;
	private BigDecimal premiumPrice;
	private BigDecimal oldTotalProfit; 
	private BigDecimal tradeProfit; 
	
	//转让价格
	private BigDecimal price; 
	private BigDecimal tradeFeeConf;
	private String tradeFeeConfStr;
	private BigDecimal tradeFee;
	private String tradeFeeStr;
	private BigDecimal sumTradeFee;
	private Long newLordsOrgId;
	private String newLordsOrgName;
	private String newLordsUserCode;
	private String newUserName;
	private String newNickName;
	private String newPhone;
	//支付方式
	private String payMethod;
	private String payChannel;
	private String payOrderNo;
	private String payChannelNo;
	
	
	public Long getOid() {
		return oid;
	}
	public void setOid(Long oid) {
		this.oid = oid;
	}
	public Long getNid() {
		return nid;
	}
	public void setNid(Long nid) {
		this.nid = nid;
	}
	public String getPayChannelNo() {
		return payChannelNo;
	}
	public void setPayChannelNo(String payChannelNo) {
		this.payChannelNo = payChannelNo;
	}
	//总奖金
	private BigDecimal totalBonus;
	
	
	//
	private String oneUserCode;//一级编号
	private String oneUserName;//一级姓名
	private String oneUserType;//一级身份
	private String oneUserProfit;//一级分润
	
	private String twoUserCode;//一级编号
	private String twoUserName;//一级姓名
	private String twoUserType;//一级身份
	private String twoUserProfit;//一级分润
	
	private String thrUserCode;//一级编号
	private String thrUserName;//一级姓名
	private String thrUserType;//一级身份
	private String thrUserProfit;//一级分润
	
	private String fouUserCode;//一级编号
	private String fouUserName;//一级姓名
	private String fouUserType;//一级身份
	private String fouUserProfit;//一级分润
	
	//分润
	private BigDecimal orgProfit;
	private BigDecimal plateProfit;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Long getTerritoryId() {
		return territoryId;
	}
	public void setTerritoryId(Long territoryId) {
		this.territoryId = territoryId;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
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
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public Long getOldLordsOrgId() {
		return oldLordsOrgId;
	}
	public void setOldLordsOrgId(Long oldLordsOrgId) {
		this.oldLordsOrgId = oldLordsOrgId;
	}
	public String getOldLordsOrgName() {
		return oldLordsOrgName;
	}
	public void setOldLordsOrgName(String oldLordsOrgName) {
		this.oldLordsOrgName = oldLordsOrgName;
	}
	public String getOldLordsUserCode() {
		return oldLordsUserCode;
	}
	public void setOldLordsUserCode(String oldLordsUserCode) {
		this.oldLordsUserCode = oldLordsUserCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getOldPayDate() {
		return oldPayDate;
	}
	public void setOldPayDate(Date oldPayDate) {
		this.oldPayDate = oldPayDate;
	}
	public BigDecimal getOldPayPrice() {
		return oldPayPrice;
	}
	public void setOldPayPrice(BigDecimal oldPayPrice) {
		this.oldPayPrice = oldPayPrice;
	}
	public BigDecimal getPremiumPrice() {
		return premiumPrice;
	}
	public void setPremiumPrice(BigDecimal premiumPrice) {
		this.premiumPrice = premiumPrice;
	}
	public BigDecimal getOldTotalProfit() {
		return oldTotalProfit;
	}
	public void setOldTotalProfit(BigDecimal oldTotalProfit) {
		this.oldTotalProfit = oldTotalProfit;
	}
	public BigDecimal getTradeProfit() {
		return tradeProfit;
	}
	public void setTradeProfit(BigDecimal tradeProfit) {
		this.tradeProfit = tradeProfit;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Long getNewLordsOrgId() {
		return newLordsOrgId;
	}
	public void setNewLordsOrgId(Long newLordsOrgId) {
		this.newLordsOrgId = newLordsOrgId;
	}
	public String getNewLordsOrgName() {
		return newLordsOrgName;
	}
	public void setNewLordsOrgName(String newLordsOrgName) {
		this.newLordsOrgName = newLordsOrgName;
	}
	public String getNewLordsUserCode() {
		return newLordsUserCode;
	}
	public void setNewLordsUserCode(String newLordsUserCode) {
		this.newLordsUserCode = newLordsUserCode;
	}
	public String getNewUserName() {
		return newUserName;
	}
	public void setNewUserName(String newUserName) {
		this.newUserName = newUserName;
	}
	public String getNewNickName() {
		return newNickName;
	}
	public void setNewNickName(String newNickName) {
		this.newNickName = newNickName;
	}
	public String getNewPhone() {
		return newPhone;
	}
	public void setNewPhone(String newPhone) {
		this.newPhone = newPhone;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public String getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}
	public String getPayOrderNo() {
		return payOrderNo;
	}
	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
	}
	public BigDecimal getTotalBonus() {
		return totalBonus;
	}
	public void setTotalBonus(BigDecimal totalBonus) {
		this.totalBonus = totalBonus;
	}
	public String getOneUserCode() {
		return oneUserCode;
	}
	public void setOneUserCode(String oneUserCode) {
		this.oneUserCode = oneUserCode;
	}
	public String getOneUserName() {
		return oneUserName;
	}
	public void setOneUserName(String oneUserName) {
		this.oneUserName = oneUserName;
	}
	public String getOneUserType() {
		return oneUserType;
	}
	public void setOneUserType(String oneUserType) {
		this.oneUserType = oneUserType;
	}
	public String getOneUserProfit() {
		return oneUserProfit;
	}
	public void setOneUserProfit(String oneUserProfit) {
		this.oneUserProfit = oneUserProfit;
	}
	public String getTwoUserCode() {
		return twoUserCode;
	}
	public void setTwoUserCode(String twoUserCode) {
		this.twoUserCode = twoUserCode;
	}
	public String getTwoUserName() {
		return twoUserName;
	}
	public void setTwoUserName(String twoUserName) {
		this.twoUserName = twoUserName;
	}
	public String getTwoUserType() {
		return twoUserType;
	}
	public void setTwoUserType(String twoUserType) {
		this.twoUserType = twoUserType;
	}
	public String getTwoUserProfit() {
		return twoUserProfit;
	}
	public void setTwoUserProfit(String twoUserProfit) {
		this.twoUserProfit = twoUserProfit;
	}
	public String getThrUserCode() {
		return thrUserCode;
	}
	public void setThrUserCode(String thrUserCode) {
		this.thrUserCode = thrUserCode;
	}
	public String getThrUserName() {
		return thrUserName;
	}
	public void setThrUserName(String thrUserName) {
		this.thrUserName = thrUserName;
	}
	public String getThrUserType() {
		return thrUserType;
	}
	public void setThrUserType(String thrUserType) {
		this.thrUserType = thrUserType;
	}
	public String getThrUserProfit() {
		return thrUserProfit;
	}
	public void setThrUserProfit(String thrUserProfit) {
		this.thrUserProfit = thrUserProfit;
	}
	public String getFouUserCode() {
		return fouUserCode;
	}
	public void setFouUserCode(String fouUserCode) {
		this.fouUserCode = fouUserCode;
	}
	public String getFouUserName() {
		return fouUserName;
	}
	public void setFouUserName(String fouUserName) {
		this.fouUserName = fouUserName;
	}
	public String getFouUserType() {
		return fouUserType;
	}
	public void setFouUserType(String fouUserType) {
		this.fouUserType = fouUserType;
	}
	public String getFouUserProfit() {
		return fouUserProfit;
	}
	public void setFouUserProfit(String fouUserProfit) {
		this.fouUserProfit = fouUserProfit;
	}
	public BigDecimal getOrgProfit() {
		return orgProfit;
	}
	public void setOrgProfit(BigDecimal orgProfit) {
		this.orgProfit = orgProfit;
	}
	public BigDecimal getPlateProfit() {
		return plateProfit;
	}
	public void setPlateProfit(BigDecimal plateProfit) {
		this.plateProfit = plateProfit;
	}

	public BigDecimal getSumPremiumMoney() {
		return sumPremiumMoney;
	}

	public void setSumPremiumMoney(BigDecimal sumPremiumMoney) {
		this.sumPremiumMoney = sumPremiumMoney;
	}

	public BigDecimal getSumDividedMoney() {
		return sumDividedMoney;
	}

	public void setSumDividedMoney(BigDecimal sumDividedMoney) {
		this.sumDividedMoney = sumDividedMoney;
	}

	public BigDecimal getTradeFeeConf() {
		return tradeFeeConf;
	}

	public void setTradeFeeConf(BigDecimal tradeFeeConf) {
		this.tradeFeeConf = tradeFeeConf;
	}

	public BigDecimal getTradeFee() {
		return tradeFee;
	}

	public void setTradeFee(BigDecimal tradeFee) {
		this.tradeFee = tradeFee;
	}

	public String getTradeFeeConfStr() {
		return tradeFeeConfStr;
	}

	public void setTradeFeeConfStr(String tradeFeeConfStr) {
		this.tradeFeeConfStr = tradeFeeConfStr;
	}

	public BigDecimal getSumTradeFee() {
		return sumTradeFee;
	}

	public void setSumTradeFee(BigDecimal sumTradeFee) {
		this.sumTradeFee = sumTradeFee;
	}

	public String getTradeFeeStr() {
		return tradeFeeStr;
	}

	public void setTradeFeeStr(String tradeFeeStr) {
		this.tradeFeeStr = tradeFeeStr;
	}
}


