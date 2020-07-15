package cn.eeepay.framework.model;


/**
 * 数据分析记录
 * @author dxy
 *
 */
public class DataAnalysisRecord {

	
	private Long recordId;
	private String analysisCode;   //数据分析记录编号
	
	
    private String maleNum;         //男性用户总数',
    private String femaleNum;       //女性用户总数',
    private String allNum;          //总用户数',
    private String h5UserNum;       //公众号用户数',
    private String h5AndroidUserNum;//公众号安卓用户数
    private String h5IosUserNum;    //公众号ios用户数
    private String androidUserNum;  //安卓用户数',
	private String iosUserNum;      //IOS用户数
	private String maleRate;        //男性用户占总用户数的百分比
	private String femaleRate;      //女性用户占总用户数的百分比
	private String whiteUser;       //小白用户：没有创建过订单的用户数
	private String registerUser;    //注册用户：已完善资料的用户
	private String registerRate;    //注册转化率：注册用户数/总用户数*100%
	private String activateUser;    //激活用户：已创建订单用户
	private String finishOrderNum;  //已完成订单数
	private String orderNum;        //订单数量
	private String productSuccessRate;//产品使用成功率=已完成订单/已创建订单数（信用卡、贷款、所有 三个维度）
	private String creditOrderNum;   //信用卡创建订单数
	private String creditFinishOrder;//信用卡已完成订单数
	private String sharedUser;      //推荐用户：已分享应用的用户  （已分享，分享成功用户数）
	private String sharedRate;      //推荐用户率= 推荐用户数/总用户数
	private String paidUser;        //付费用户：已付费交钱的用户
	private String paidRate;        //付费转化率= 付费人数/总用户数
	
	private String morrowRetentionRate;//次日留存率：（当天新增的用户中，在注册的第2天还登录的用户数）/第一天新增总用户数
	private String threeRetentionRate;//第3日留存率：（第一天新增用户中，在注册的第3天还有登录的用户数）/第一天新增总用户数
	private String sevenRetentionRate;//第7日留存率：（第一天新增的用户中，在注册的第7天还有登录的用户数）/第一天新增总用户数
	private String fifteenRetentionRate;//第15日留存率：（第一天新增的用户中，在注册的第15天还有登录的用户数）/第一天新增总用户数
	private String thityRetentionRate;//第30日留存率：（第一天新增的用户中，在注册的第30天还有登录的用户数）/第一天新增总用户数
	private java.util.Date createDate;          //创建开始时间
	private java.util.Date finishDate;          //创建结束时间
	private String dayLoginNum;          //启动频率：每天APP和公众号登录次数
	private String dayH5LoginNum;        //每天公众号登录次数
	private String dayH5AndroidLoginNum; //每天公众号安卓登录次数
	private String dayH5IosLoginNum; //每天公众号安卓登录次数
	private String dayAndroidLoginNum;  //每天安卓登录次数
	private String dayIosLoginNum;       //每天IOS登录次数
	
	private String orgId;
	
	private String posterApplyCount;         // 1.银行宣传海报滑动查看数


	private String confirmApplyCount;        // 2.确定申请按钮点击次数

	private String  gotoApplyLoanCount;      //3.点击“前往申请”按钮点击次数。
	
	private String loanPosterCount;          //4.贷款宣传海报滑动查看数

	
	
	public Long getRecordId() {
		return recordId;
	}


	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	

	public String getH5AndroidUserNum() {
		return h5AndroidUserNum;
	}


	public void setH5AndroidUserNum(String h5AndroidUserNum) {
		this.h5AndroidUserNum = h5AndroidUserNum;
	}


	public String getH5IosUserNum() {
		return h5IosUserNum;
	}


	public void setH5IosUserNum(String h5IosUserNum) {
		this.h5IosUserNum = h5IosUserNum;
	}


	public String getDayH5AndroidLoginNum() {
		return dayH5AndroidLoginNum;
	}


	public void setDayH5AndroidLoginNum(String dayH5AndroidLoginNum) {
		this.dayH5AndroidLoginNum = dayH5AndroidLoginNum;
	}


	public String getDayH5IosLoginNum() {
		return dayH5IosLoginNum;
	}


	public void setDayH5IosLoginNum(String dayH5IosLoginNum) {
		this.dayH5IosLoginNum = dayH5IosLoginNum;
	}


	public String getDayH5LoginNum() {
		return dayH5LoginNum;
	}


	public void setDayH5LoginNum(String dayH5LoginNum) {
		this.dayH5LoginNum = dayH5LoginNum;
	}


	public String getDayAndroidLoginNum() {
		return dayAndroidLoginNum;
	}


	public void setDayAndroidLoginNum(String dayAndroidLoginNum) {
		this.dayAndroidLoginNum = dayAndroidLoginNum;
	}


	public String getDayIosLoginNum() {
		return dayIosLoginNum;
	}


	public void setDayIosLoginNum(String dayIosLoginNum) {
		this.dayIosLoginNum = dayIosLoginNum;
	}


	public String getGotoApplyLoanCount() {
		return gotoApplyLoanCount;
	}


	public void setGotoApplyLoanCount(String gotoApplyLoanCount) {
		this.gotoApplyLoanCount = gotoApplyLoanCount;
	}


	public String getLoanPosterCount() {
		return loanPosterCount;
	}


	public void setLoanPosterCount(String loanPosterCount) {
		this.loanPosterCount = loanPosterCount;
	}


	public String getFinishOrderNum() {
		return finishOrderNum;
	}


	public void setFinishOrderNum(String finishOrderNum) {
		this.finishOrderNum = finishOrderNum;
	}


	public String getOrderNum() {
		return orderNum;
	}


	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}


	public String getCreditOrderNum() {
		return creditOrderNum;
	}


	public String getPosterApplyCount() {
		return posterApplyCount;
	}


	public void setPosterApplyCount(String posterApplyCount) {
		this.posterApplyCount = posterApplyCount;
	}




	public String getConfirmApplyCount() {
		return confirmApplyCount;
	}


	public void setConfirmApplyCount(String confirmApplyCount) {
		this.confirmApplyCount = confirmApplyCount;
	}

	public void setCreditOrderNum(String creditOrderNum) {
		this.creditOrderNum = creditOrderNum;
	}


	public String getCreditFinishOrder() {
		return creditFinishOrder;
	}


	public void setCreditFinishOrder(String creditFinishOrder) {
		this.creditFinishOrder = creditFinishOrder;
	}


	public String getAnalysisCode() {
		return analysisCode;
	}


	public void setAnalysisCode(String analysisCode) {
		this.analysisCode = analysisCode;
	}


	public String getDayLoginNum() {
		return dayLoginNum;
	}


	public void setDayLoginNum(String dayLoginNum) {
		this.dayLoginNum = dayLoginNum;
	}


	


	public String getMaleNum() {
		return maleNum;
	}


	public void setMaleNum(String maleNum) {
		this.maleNum = maleNum;
	}


	public String getFemaleNum() {
		return femaleNum;
	}


	public void setFemaleNum(String femaleNum) {
		this.femaleNum = femaleNum;
	}


	public String getAllNum() {
		return allNum;
	}


	public void setAllNum(String allNum) {
		this.allNum = allNum;
	}


	public String getH5UserNum() {
		return h5UserNum;
	}


	public void setH5UserNum(String h5UserNum) {
		this.h5UserNum = h5UserNum;
	}


	public String getAndroidUserNum() {
		return androidUserNum;
	}


	public void setAndroidUserNum(String androidUserNum) {
		this.androidUserNum = androidUserNum;
	}


	public java.lang.String getIosUserNum() {
		return iosUserNum;
	}


	public void setIosUserNum(java.lang.String iosUserNum) {
		this.iosUserNum = iosUserNum;
	}


	public java.lang.String getMaleRate() {
		return maleRate;
	}


	public void setMaleRate(java.lang.String maleRate) {
		this.maleRate = maleRate;
	}


	public java.lang.String getFemaleRate() {
		return femaleRate;
	}


	public void setFemaleRate(java.lang.String femaleRate) {
		this.femaleRate = femaleRate;
	}


	public java.lang.String getWhiteUser() {
		return whiteUser;
	}


	public void setWhiteUser(java.lang.String whiteUser) {
		this.whiteUser = whiteUser;
	}


	public java.lang.String getRegisterUser() {
		return registerUser;
	}


	public void setRegisterUser(java.lang.String registerUser) {
		this.registerUser = registerUser;
	}


	public java.lang.String getRegisterRate() {
		return registerRate;
	}


	public void setRegisterRate(java.lang.String registerRate) {
		this.registerRate = registerRate;
	}


	public java.lang.String getActivateUser() {
		return activateUser;
	}


	public void setActivateUser(java.lang.String activateUser) {
		this.activateUser = activateUser;
	}


	public java.lang.String getProductSuccessRate() {
		return productSuccessRate;
	}


	public void setProductSuccessRate(java.lang.String productSuccessRate) {
		this.productSuccessRate = productSuccessRate;
	}


	public java.lang.String getSharedUser() {
		return sharedUser;
	}


	public void setSharedUser(java.lang.String sharedUser) {
		this.sharedUser = sharedUser;
	}


	public java.lang.String getSharedRate() {
		return sharedRate;
	}


	public void setSharedRate(java.lang.String sharedRate) {
		this.sharedRate = sharedRate;
	}


	public java.lang.String getPaidUser() {
		return paidUser;
	}


	public void setPaidUser(java.lang.String paidUser) {
		this.paidUser = paidUser;
	}


	public java.lang.String getPaidRate() {
		return paidRate;
	}


	public void setPaidRate(java.lang.String paidRate) {
		this.paidRate = paidRate;
	}

	public java.lang.String getMorrowRetentionRate() {
		return morrowRetentionRate;
	}


	public void setMorrowRetentionRate(java.lang.String morrowRetentionRate) {
		this.morrowRetentionRate = morrowRetentionRate;
	}


	public java.lang.String getThreeRetentionRate() {
		return threeRetentionRate;
	}


	public void setThreeRetentionRate(java.lang.String threeRetentionRate) {
		this.threeRetentionRate = threeRetentionRate;
	}


	public java.lang.String getSevenRetentionRate() {
		return sevenRetentionRate;
	}


	public void setSevenRetentionRate(java.lang.String sevenRetentionRate) {
		this.sevenRetentionRate = sevenRetentionRate;
	}


	public java.lang.String getFifteenRetentionRate() {
		return fifteenRetentionRate;
	}


	public void setFifteenRetentionRate(java.lang.String fifteenRetentionRate) {
		this.fifteenRetentionRate = fifteenRetentionRate;
	}


	public java.lang.String getThityRetentionRate() {
		return thityRetentionRate;
	}


	public void setThityRetentionRate(java.lang.String thityRetentionRate) {
		this.thityRetentionRate = thityRetentionRate;
	}


	public java.util.Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}


	public java.util.Date getFinishDate() {
		return finishDate;
	}


	public void setFinishDate(java.util.Date finishDate) {
		this.finishDate = finishDate;
	}


	public String getOrgId() {
		return orgId;
	}


	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}


	@Override
	public String toString() {
		return "DataAnalysisRecord [recordId=" + recordId + ", analysisCode="
				+ analysisCode + ", maleNum=" + maleNum + ", femaleNum="
				+ femaleNum + ", allNum=" + allNum + ", h5UserNum=" + h5UserNum
				+ ", androidUserNum=" + androidUserNum + ", iosUserNum="
				+ iosUserNum + ", maleRate=" + maleRate + ", femaleRate="
				+ femaleRate + ", whiteUser=" + whiteUser + ", registerUser="
				+ registerUser + ", registerRate=" + registerRate
				+ ", activateUser=" + activateUser + ", finishOrderNum="
				+ finishOrderNum + ", orderNum=" + orderNum
				+ ", productSuccessRate=" + productSuccessRate
				+ ", creditOrderNum=" + creditOrderNum + ", creditFinishOrder="
				+ creditFinishOrder + ", sharedUser=" + sharedUser
				+ ", sharedRate=" + sharedRate + ", paidUser=" + paidUser
				+ ", paidRate=" + paidRate + ", morrowRetentionRate="
				+ morrowRetentionRate + ", threeRetentionRate="
				+ threeRetentionRate + ", sevenRetentionRate="
				+ sevenRetentionRate + ", fifteenRetentionRate="
				+ fifteenRetentionRate + ", thityRetentionRate="
				+ thityRetentionRate + ", createDate=" + createDate
				+ ", finishDate=" + finishDate + ", dayLoginNum=" + dayLoginNum
				+ ", orgId=" + orgId + ", posterApplyCount=" + posterApplyCount
				+ ", confirmApplyCount=" + confirmApplyCount
				+ ", gotoApplyLoanCount=" + gotoApplyLoanCount
				+ ", loanPosterCount=" + loanPosterCount + "]";
	}
	

}
