package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 排行榜记录表
 * @author Administrator
 *
 */
public class RankingRecord {

	private String id;
	private String rankingNo;		//排行榜编号
	private String batchNo;			//期号
	private String ruleNo;			//规则编号
	private String rankingName;		//排行榜名称
	private String rankingType;		//排行榜类型
	private String orgId;
	private String pushNum;			//本期获奖人数
	private String pushTotalAmount;	//本期奖金
	private String pushRealNum;		//实发人数
	private String pushRealAmount;	//实发奖金
	private String status;			//榜单状态
	private Date createDate;		//榜单生成时间
	private Date startDate;			//统计开始时间
	private Date endDate;			//统计结束时间
	private String orgName;			//所属组织
	private String dataType;		//统计数据类型
	
	private String totalPushPerson;//总发放人数   (记录汇总)
	
	private String totalPushAmount;//总发放金额	(记录汇总)
	
	private String allUsersTotal;  //榜单详细所有用户统计总额
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRankingNo() {
		return rankingNo;
	}
	public void setRankingNo(String rankingNo) {
		this.rankingNo = rankingNo;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getRuleNo() {
		return ruleNo;
	}
	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}
	public String getRankingName() {
		return rankingName;
	}
	public void setRankingName(String rankingName) {
		this.rankingName = rankingName;
	}
	public String getRankingType() {
		return rankingType;
	}
	public void setRankingType(String rankingType) {
		this.rankingType = rankingType;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getPushNum() {
		return pushNum;
	}
	public void setPushNum(String pushNum) {
		this.pushNum = pushNum;
	}
	public String getPushTotalAmount() {
		return pushTotalAmount;
	}
	public void setPushTotalAmount(String pushTotalAmount) {
		this.pushTotalAmount = pushTotalAmount;
	}
	public String getPushRealNum() {
		return pushRealNum;
	}
	public void setPushRealNum(String pushRealNum) {
		this.pushRealNum = pushRealNum;
	}
	public String getPushRealAmount() {
		return pushRealAmount;
	}
	public void setPushRealAmount(String pushRealAmount) {
		this.pushRealAmount = pushRealAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getTotalPushPerson() {
		return totalPushPerson;
	}
	public void setTotalPushPerson(String totalPushPerson) {
		this.totalPushPerson = totalPushPerson;
	}
	public String getTotalPushAmount() {
		return totalPushAmount;
	}
	public void setTotalPushAmount(String totalPushAmount) {
		this.totalPushAmount = totalPushAmount;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getAllUsersTotal() {
		return allUsersTotal;
	}
	public void setAllUsersTotal(String allUsersTotal) {
		this.allUsersTotal = allUsersTotal;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
}
