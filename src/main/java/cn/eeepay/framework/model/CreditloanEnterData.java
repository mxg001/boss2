package cn.eeepay.framework.model;

import java.util.Date;



/**
 * 信用卡或者贷款申请入口统计表
 * @author dxy
 *
 */
public class CreditloanEnterData {

	private Long recordId;      //记录ID',
	private String analysisCode;//数据分析编号',
	private String loginSource; //设备访问来源 1.安卓；2.IOS；',
	private String businessSource;   //申请入口类型 1.信用卡；2.贷款',
	private String creditBank;  //信用卡银行:1.平安银行、2.交通银行、3.兴业银行、4.浦发银行、5.光大银行、6.广州银行、7.招商银行、8.广发银行、9.上海银行、10.温州银行、11.温州银行全卡种、12.华夏银行、13.建设银行（深圳）',
	private String loanSource;  //贷款来源:1.全民购、2.宜人贷、3.钱隆柜、4.你我贷借款、5.快易花、6.众安杏仁派、7.万达普惠、8.豆豆钱、9.360借条、10.卡卡贷、11.苏宁消费金融、12.小树时代、13.功夫贷、14.人品贷、15.微贷',
	
	private String dayPvNum;    //日点击数量
	private String weekPvNum;   //周点击数量
	private String monthPvNum;  //月点击数量
	
	private String dayUvNum;    //日访客数量
	private String weekUvNum;   //周访客数量
	private String monthUvNum;  //月访客数量
	
	private Date createDate;    //创建时间',
	private String openType;    //打开位置 1-APP 2-微信H5',
	private String orgId;       //组织Id
	private Date analysisDate;    //分析的时间,
	
	
	public Long getRecordId() {
		return recordId;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	
	public Date getAnalysisDate() {
		return analysisDate;
	}
	public void setAnalysisDate(Date analysisDate) {
		this.analysisDate = analysisDate;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getDayPvNum() {
		return dayPvNum;
	}
	public void setDayPvNum(String dayPvNum) {
		this.dayPvNum = dayPvNum;
	}
	public String getWeekPvNum() {
		return weekPvNum;
	}
	public void setWeekPvNum(String weekPvNum) {
		this.weekPvNum = weekPvNum;
	}
	public String getMonthPvNum() {
		return monthPvNum;
	}
	public void setMonthPvNum(String monthPvNum) {
		this.monthPvNum = monthPvNum;
	}
	public String getDayUvNum() {
		return dayUvNum;
	}
	public void setDayUvNum(String dayUvNum) {
		this.dayUvNum = dayUvNum;
	}
	public String getWeekUvNum() {
		return weekUvNum;
	}
	public void setWeekUvNum(String weekUvNum) {
		this.weekUvNum = weekUvNum;
	}
	public String getMonthUvNum() {
		return monthUvNum;
	}
	public void setMonthUvNum(String monthUvNum) {
		this.monthUvNum = monthUvNum;
	}
	public String getAnalysisCode() {
		return analysisCode;
	}
	public void setAnalysisCode(String analysisCode) {
		this.analysisCode = analysisCode;
	}
	public String getLoginSource() {
		return loginSource;
	}
	public void setLoginSource(String loginSource) {
		this.loginSource = loginSource;
	}
	
	public String getBusinessSource() {
		return businessSource;
	}
	public void setBusinessSource(String businessSource) {
		this.businessSource = businessSource;
	}
	public String getCreditBank() {
		return creditBank;
	}
	public void setCreditBank(String creditBank) {
		this.creditBank = creditBank;
	}
	public String getLoanSource() {
		return loanSource;
	}
	public void setLoanSource(String loanSource) {
		this.loanSource = loanSource;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getOpenType() {
		return openType;
	}
	public void setOpenType(String openType) {
		this.openType = openType;
	}
	
	
	
}
