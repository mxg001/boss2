package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 违章订单信息
 * @author Administrator
 *
 */
public class CarOrder {

	private Long id;
	private String orderNo;		//订单ID
	private String status;		//订单状态
	private String accountStatus;//记账状态
	private String orgId;
	private String orgName;
	private String violationType;//违章类型
	private String carNum;		//车牌号
	private String violationCity;//违章城市
	private Date createDate;//创建时间
	private Date completeDate;//订单完成时间
	private Date violationTime;//违章
	
	private String createSTime;//查询条件的  创建开始时间
	private String createETime;
	private String finishSTime;
	private String finishETime;
	private String violationSTime;
	private String violationETime;
	
	private String userCode;
	private String userName;
	private String phone;
	private String orderIdCardNo; //订单身份证号
	
	private String oneUserCode;//1级收益用户编号
	private String oneUserType;// 1级身份
	private String oneUserName;// 1级名称
	private String oneUserProfit;//1级分润
	
	private String twoUserCode;// 2级收益用户编号
	private String twoUserType;// 2级身份
	private String twoUserName;// 2级名称
	private String twoUserProfit;//2级分润
	
	private String thrUserCode;//3级收益用户编号
	private String thrUserType;//3级身份
	private String thrUserName;//3级名称
	private String thrUserProfit;//3级分润
	
	private String fouUserCode;//4级收益用户编号
	private String fouUserType;//4级身份
	private String fouUserName;//4级名称
	private String fouUserProfit;//4级分润
	
	private String openProvince; //开通省份
	private String openCity;     //开通城市
	private String openRegion;   //开通地区
	
	private String score;//违章扣分
	
	private String price;//发放总奖金  (银行家收到的总奖金)
	
	private String receiveAmount;//订单总额

	private String totalBonus;
	private String plateProfit;//平台分润汇总
	private String orgProfit;	//品牌分润汇总
	private String remark;		//订单贡献人备注，user_info表内的备注
	private String outOrderNo;  //车管家订单号

	private BigDecimal realPlatProfit;//平台实际分润

	private BigDecimal adjustRatio;//调节系数

	private  BigDecimal basicBonusAmount;//领地基准分红

	private BigDecimal bonusAmount;//领地分红

	private String redUserCode; //领地分红领取用户编号

	private String redUserName;//领地分红用户姓名

	private BigDecimal actualSum;

	private BigDecimal territorySum;

	private  BigDecimal territoryAvgPrice;

	private BigDecimal territoryPrice;

	private Date receiveTime;

	private  String receiveTimeStr;

	private String rate;

	private int rateType;

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
		this.orderNo = orderNo;
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

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getViolationType() {
		return violationType;
	}

	public void setViolationType(String violationType) {
		this.violationType = violationType;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public String getViolationCity() {
		return violationCity;
	}

	public void setViolationCity(String violationCity) {
		this.violationCity = violationCity;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public Date getViolationTime() {
		return violationTime;
	}

	public void setViolationTime(Date violationTime) {
		this.violationTime = violationTime;
	}

	public String getFinishSTime() {
		return finishSTime;
	}

	public void setFinishSTime(String finishSTime) {
		this.finishSTime = finishSTime;
	}

	public String getFinishETime() {
		return finishETime;
	}

	public void setFinishETime(String finishETime) {
		this.finishETime = finishETime;
	}

	public String getViolationSTime() {
		return violationSTime;
	}

	public void setViolationSTime(String violationSTime) {
		this.violationSTime = violationSTime;
	}

	public String getViolationETime() {
		return violationETime;
	}

	public void setViolationETime(String violationETime) {
		this.violationETime = violationETime;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
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

	public BigDecimal getActualSum() {
		return actualSum;
	}

	public void setActualSum(BigDecimal actualSum) {
		this.actualSum = actualSum;
	}

	public BigDecimal getTerritorySum() {
		return territorySum;
	}

	public void setTerritorySum(BigDecimal territorySum) {
		this.territorySum = territorySum;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOneUserCode() {
		return oneUserCode;
	}

	public void setOneUserCode(String oneUserCode) {
		this.oneUserCode = oneUserCode;
	}

	public String getOneUserType() {
		return oneUserType;
	}

	public void setOneUserType(String oneUserType) {
		this.oneUserType = oneUserType;
	}

	public String getOneUserName() {
		return oneUserName;
	}

	public void setOneUserName(String oneUserName) {
		this.oneUserName = oneUserName;
	}

	public String getTwoUserCode() {
		return twoUserCode;
	}

	public void setTwoUserCode(String twoUserCode) {
		this.twoUserCode = twoUserCode;
	}

	public String getTwoUserType() {
		return twoUserType;
	}

	public void setTwoUserType(String twoUserType) {
		this.twoUserType = twoUserType;
	}

	public String getTwoUserName() {
		return twoUserName;
	}

	public void setTwoUserName(String twoUserName) {
		this.twoUserName = twoUserName;
	}

	public String getThrUserCode() {
		return thrUserCode;
	}

	public void setThrUserCode(String thrUserCode) {
		this.thrUserCode = thrUserCode;
	}

	public String getThrUserType() {
		return thrUserType;
	}

	public void setThrUserType(String thrUserType) {
		this.thrUserType = thrUserType;
	}

	public String getThrUserName() {
		return thrUserName;
	}

	public void setThrUserName(String thrUserName) {
		this.thrUserName = thrUserName;
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

	public String getFouUserName() {
		return fouUserName;
	}

	public void setFouUserName(String fouUserName) {
		this.fouUserName = fouUserName;
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

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(String receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPlateProfit() {
		return plateProfit;
	}

	public void setPlateProfit(String plateProfit) {
		this.plateProfit = plateProfit;
	}

	public String getOrgProfit() {
		return orgProfit;
	}

	public void setOrgProfit(String orgProfit) {
		this.orgProfit = orgProfit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOneUserProfit() {
		return oneUserProfit;
	}

	public void setOneUserProfit(String oneUserProfit) {
		this.oneUserProfit = oneUserProfit;
	}

	public String getTwoUserProfit() {
		return twoUserProfit;
	}

	public void setTwoUserProfit(String twoUserProfit) {
		this.twoUserProfit = twoUserProfit;
	}

	public String getThrUserProfit() {
		return thrUserProfit;
	}

	public void setThrUserProfit(String thrUserProfit) {
		this.thrUserProfit = thrUserProfit;
	}

	public String getFouUserProfit() {
		return fouUserProfit;
	}

	public void setFouUserProfit(String fouUserProfit) {
		this.fouUserProfit = fouUserProfit;
	}

	public String getOrderIdCardNo() {
		return orderIdCardNo;
	}

	public void setOrderIdCardNo(String orderIdCardNo) {
		this.orderIdCardNo = orderIdCardNo;
	}

	public String getTotalBonus() {
		return totalBonus;
	}

	public void setTotalBonus(String totalBonus) {
		this.totalBonus = totalBonus;
	}

	public String getCreateSTime() {
		return createSTime;
	}

	public void setCreateSTime(String createSTime) {
		this.createSTime = createSTime;
	}

	public String getCreateETime() {
		return createETime;
	}

	public void setCreateETime(String createETime) {
		this.createETime = createETime;
	}

	public String getOutOrderNo() {
		return outOrderNo;
	}

	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
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

	public String getRedUserName() {
		return redUserName;
	}

	public void setRedUserName(String redUserName) {
		this.redUserName = redUserName;
	}

	public String getRate() {
		return rate;
	}

	public int getRateType() {
		return rateType;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public void setRateType(int rateType) {
		this.rateType = rateType;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getReceiveTimeStr() {
		return receiveTimeStr;
	}

	public void setReceiveTimeStr(String receiveTimeStr) {
		this.receiveTimeStr = receiveTimeStr;
	}
}
