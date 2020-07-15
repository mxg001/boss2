package cn.eeepay.framework.model;

import java.util.Date;



/**
 * 浏览记录表
 * @author dxy
 *
 */
public class PageViewRecord {

	
	private Long recordId;
	private String userCode;      //会员ID',
	private String loginSource;   //设备访问来源 1.安卓；2.苹果；',
	private String loginType;     //登录类型：0.访问；1.登录',
	private String pageLevel;     //页面级别：1.一级入口、2.二级入口',
	private String businessSource; //业务来源:1.办信用卡、2.贷款申请、3.商户收款、4.保险、5.智能信用查询、6.违章代缴、7.办卡查询、8.信用卡还款、9.抢红包、10.排行榜、11.领地',
	private String creditBank;     //信用卡银行:1.平安银行、2.交通银行、3.兴业银行、4.浦发银行、5.光大银行、6.广州银行、7.招商银行、广发银行、上海银行、温州银行、温州银行全卡种、华夏银行、建设银行（深圳）',
	private String loanSource;     //贷款来源:1.全民购、2.宜人贷、3.钱隆柜、4.你我贷借款、5.快易花、6.众安杏仁派、7.万达普惠、8.豆豆钱、9.360借条、10.卡卡贷、11.苏宁消费金融、12.小树时代、13.功夫贷、14.人品贷、15.微贷',
	private String operate;        //操作：1.银行宣传海报滑动查看数、2.我要申请点击数(办卡)、3.前往申请或注册点击数(贷款)、4.贷款宣传海报滑动查看数',
	private Date createDate;       //访问时间',
	private String pageType;       //页面类型1.APP,2.H5
	
	
	public Long getRecordId() {
		return recordId;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getLoginSource() {
		return loginSource;
	}
	public void setLoginSource(String loginSource) {
		this.loginSource = loginSource;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	public String getPageLevel() {
		return pageLevel;
	}
	public void setPageLevel(String pageLevel) {
		this.pageLevel = pageLevel;
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
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	
	
}
