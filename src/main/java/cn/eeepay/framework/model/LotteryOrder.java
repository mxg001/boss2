package cn.eeepay.framework.model;


/**
 * 彩票订单
 * @author Administrator
 *
 */
public class LotteryOrder {

	
	/***************************【lottery_order表字段】***********************/
	private Long id;
	
	private String outId; //外部记录ID
	
	private String outOrderNo;//外部订单号
	
	private String outUserId;//外部系统用户ID
	
	private String orderNo;//order_main表订单号
	
	private String deviceJno;//投注设备流水号
	
	private String awardRequire;//奖励要求
	
	private String betTime;//投注时间
	
	private String lotteryType;//彩种
	
	private String issue;//投注期号
	
	private String redeemTime;//兑奖时间
	
	private String awardAmount;//中奖总金额
	
	private String isBigPrize;//大奖标志
	
	private String redeemFlag;//兑奖状态
	
	private String menchantNo;//商户号

	private String deviceNo;  //投注设备号
	
	private String menchantName;  //商户名称
	
	private String buyStatus;//购买状态
	
	private Integer consumeE; //消费e豆 (100e豆=1元)
	
	private String createDate; //创建时间
	
	private String batchNo;   //批次号
	
	private String sportLottery;//体彩福彩
	/***************************【lottery_order表字段】***********************/
	
	
	
	/***************************【order_main表字段】***********************/
	private String orgId;
	
	private String orgName;
	
	private String orderStatus;//订单状态 order_main表的状态
	
	private String userCode;
	
	private String userName;
	
	private String phone;
	
	private String price;// 彩票机构发放总金额
	
	private String loanAmount; //总购买金额
	
	private String profitType;//奖金方式
	
	private String loanBankRate;//彩票机构总奖金扣率
	
	private String totalBonus;//彩票机构总发放奖金
	
	private String loanOrgRate;//品牌发放总奖金扣率
	
	private String loanOrgBonus;//品牌发放总奖金
	
	private String companyBonusConf;//公司截留扣率
	
	private String orgBonusConf;//品牌截留扣率
	
	private String oneCode;
	private String oneName;
	private String oneRole;
	private String oneProfit;
	
	private String twoCode;
	private String twoName;
	private String twoRole;
	private String twoProfit;
	
	private String threeCode;
	private String threeName;
	private String threeRole;
	private String threeProfit;
	
	private String fourCode;
	private String fourName;
	private String fourRole;
	private String fourProfit;
	
	private String orgProfit;//品牌分润
	
	private String plateProfit;//平台分润
	
	private String accountStatus;//记账状态
	
	private String openProvince;
	
	private String openCity;
	
	private String openRegion;//区
	
	private String remark;
	
	private String payOrderNo;
	/***************************【order_main表字段】***********************/
	
	
	private String sumAmount;//订单金额汇总
	
	private String sumPlatProfit;//平台分润汇总
	
	private String sumOrgProfit;//品牌分润汇总
	
	private String startTime;
	
	private String endTime;
	
	
	private String userId; //超级银行家的用户Id;
	
	private String isGenOrder; //是否已经生成了主订单
	
	
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

	public String getDeviceJno() {
		return deviceJno;
	}

	public String getIsGenOrder() {
		return isGenOrder;
	}

	public void setIsGenOrder(String isGenOrder) {
		this.isGenOrder = isGenOrder;
	}

	public void setDeviceJno(String deviceJno) {
		this.deviceJno = deviceJno;
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

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getProfitType() {
		return profitType;
	}

	public void setProfitType(String profitType) {
		this.profitType = profitType;
	}

	public String getAwardRequire() {
		return awardRequire;
	}

	public void setAwardRequire(String awardRequire) {
		this.awardRequire = awardRequire;
	}

	public String getLoanBankRate() {
		return loanBankRate;
	}

	public void setLoanBankRate(String loanBankRate) {
		this.loanBankRate = loanBankRate;
	}

	public String getTotalBonus() {
		return totalBonus;
	}

	public void setTotalBonus(String totalBonus) {
		this.totalBonus = totalBonus;
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

	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getAwardAmount() {
		return awardAmount;
	}

	public void setAwardAmount(String awardAmount) {
		this.awardAmount = awardAmount;
	}

	public String getIsBigPrize() {
		return isBigPrize;
	}

	public void setIsBigPrize(String isBigPrize) {
		this.isBigPrize = isBigPrize;
	}

	public String getRedeemFlag() {
		return redeemFlag;
	}

	public void setRedeemFlag(String redeemFlag) {
		this.redeemFlag = redeemFlag;
	}

	public String getOneCode() {
		return oneCode;
	}

	public void setOneCode(String oneCode) {
		this.oneCode = oneCode;
	}

	public String getOneName() {
		return oneName;
	}

	public void setOneName(String oneName) {
		this.oneName = oneName;
	}

	public String getOneRole() {
		return oneRole;
	}

	public void setOneRole(String oneRole) {
		this.oneRole = oneRole;
	}

	public String getOneProfit() {
		return oneProfit;
	}

	public void setOneProfit(String oneProfit) {
		this.oneProfit = oneProfit;
	}

	public String getTwoCode() {
		return twoCode;
	}

	public void setTwoCode(String twoCode) {
		this.twoCode = twoCode;
	}

	public String getTwoName() {
		return twoName;
	}

	public void setTwoName(String twoName) {
		this.twoName = twoName;
	}

	public String getTwoRole() {
		return twoRole;
	}

	public void setTwoRole(String twoRole) {
		this.twoRole = twoRole;
	}

	public String getTwoProfit() {
		return twoProfit;
	}

	public void setTwoProfit(String twoProfit) {
		this.twoProfit = twoProfit;
	}

	public String getThreeCode() {
		return threeCode;
	}

	public void setThreeCode(String threeCode) {
		this.threeCode = threeCode;
	}

	public String getThreeName() {
		return threeName;
	}

	public void setThreeName(String threeName) {
		this.threeName = threeName;
	}

	public String getThreeRole() {
		return threeRole;
	}

	public void setThreeRole(String threeRole) {
		this.threeRole = threeRole;
	}

	public String getThreeProfit() {
		return threeProfit;
	}

	public void setThreeProfit(String threeProfit) {
		this.threeProfit = threeProfit;
	}

	public String getFourCode() {
		return fourCode;
	}

	public void setFourCode(String fourCode) {
		this.fourCode = fourCode;
	}

	public String getFourName() {
		return fourName;
	}

	public void setFourName(String fourName) {
		this.fourName = fourName;
	}

	public String getFourRole() {
		return fourRole;
	}

	public void setFourRole(String fourRole) {
		this.fourRole = fourRole;
	}

	public String getFourProfit() {
		return fourProfit;
	}

	public void setFourProfit(String fourProfit) {
		this.fourProfit = fourProfit;
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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getOutId() {
		return outId;
	}

	public void setOutId(String outId) {
		this.outId = outId;
	}

	public String getOutOrderNo() {
		return outOrderNo;
	}

	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPlateProfit() {
		return plateProfit;
	}

	public void setPlateProfit(String plateProfit) {
		this.plateProfit = plateProfit;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
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

	public String getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(String sumAmount) {
		this.sumAmount = sumAmount;
	}

	public String getSumPlatProfit() {
		return sumPlatProfit;
	}

	public void setSumPlatProfit(String sumPlatProfit) {
		this.sumPlatProfit = sumPlatProfit;
	}

	public String getSumOrgProfit() {
		return sumOrgProfit;
	}

	public void setSumOrgProfit(String sumOrgProfit) {
		this.sumOrgProfit = sumOrgProfit;
	}

	public String getOutUserId() {
		return outUserId;
	}

	public void setOutUserId(String outUserId) {
		this.outUserId = outUserId;
	}

	public String getMenchantNo() {
		return menchantNo;
	}

	public void setMenchantNo(String menchantNo) {
		this.menchantNo = menchantNo;
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

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public String getMenchantName() {
		return menchantName;
	}

	public void setMenchantName(String menchantName) {
		this.menchantName = menchantName;
	}

	public String getBuyStatus() {
		return buyStatus;
	}

	public void setBuyStatus(String buyStatus) {
		this.buyStatus = buyStatus;
	}

	public Integer getConsumeE() {
		return consumeE;
	}

	public void setConsumeE(Integer consumeE) {
		this.consumeE = consumeE;
	}

	public String getBetTime() {
		return betTime;
	}

	public void setBetTime(String betTime) {
		this.betTime = betTime;
	}

	public String getRedeemTime() {
		return redeemTime;
	}

	public void setRedeemTime(String redeemTime) {
		this.redeemTime = redeemTime;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getPayOrderNo() {
		return payOrderNo;
	}

	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
	}

	public String getSportLottery() {
		return sportLottery;
	}

	public void setSportLottery(String sportLottery) {
		this.sportLottery = sportLottery;
	}

}
