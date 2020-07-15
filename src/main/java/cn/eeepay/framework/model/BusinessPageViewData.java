package cn.eeepay.framework.model;

import java.util.Date;


/**
 * 业务访问量汇总
 * @author dxy
 *
 */
public class BusinessPageViewData {

	private Long recordId;      //记录ID',
	private String analysisCode;//数据分析编号',
	private String loginSource; //设备访问来源 1.安卓；2.IOS；',
	private String businessSource; //业务来源:1.办信用卡、2.贷款申请、3.商户收款、4.保险、5.智能信用查询、6.违章代缴、7.办卡查询、8.信用卡还款、9.抢红包、10.排行榜、11.领地',
	
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
	public String getAnalysisCode() {
		return analysisCode;
	}
	
	public Date getAnalysisDate() {
		return analysisDate;
	}
	public void setAnalysisDate(Date analysisDate) {
		this.analysisDate = analysisDate;
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
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}
