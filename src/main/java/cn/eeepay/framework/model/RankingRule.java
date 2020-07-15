package cn.eeepay.framework.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 排行榜排行榜规则表
 * @author dxy
 *
 */
public class RankingRule {
	private Long id;
	private String ruleCode; //规则编号
	private String ruleName; //规则名称
	private String ruleType; //类型(0周榜 1月榜 2年榜)
	private String dataType; //统计数据类型(0收益金额 1会员数 2用户数)
	private String status;   //活动开关(0关闭 1打开)
	private int statusInt;
	private String showOrderNo;//显示顺序
	private String advertOrderNo;//广告位置
	private String advertUrl;  //广告图片
	private String openOrg;    //开通组织
	private String openOrgInfo;//开通组织名称
	private String totalAmount;//奖金金额
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")  
	private Date startTime;    //活动开始时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")  
	private Date endTime;      //活动截止时间
	private Date updateTime;   //规则修改时间
	private String introduction;//活动介绍
	private String bonusNote;   //奖金说明
	private int openTime;       //打开次数
	
	private List<RankingRuleLevel> levelList; //排行榜组织各级奖金设置
	private String operate;  //操作类型：0.新增；1.修改；2.显示
	private String orgType;  //参入活动组织 0.全部组织；1.指定组织
	private String historyUrl;//历史榜单查看
	private String levelTotalMoney; //每个组织中各级奖金总设置
	private List<RankingRuleHistory> hisList; //历史操作记录
	private List<OrgInfo> selectOrgs;  //已选择的组织
	private List<OrgInfo> orgList;   //所有组织
	
	private String busiType;    //业务集合   用  - 连接
	
	
	public String getOpenOrgInfo() {
		return openOrgInfo;
	}
	public void setOpenOrgInfo(String openOrgInfo) {
		this.openOrgInfo = openOrgInfo;
	}
	public List<OrgInfo> getOrgList() {
		return orgList;
	}
	public void setOrgList(List<OrgInfo> orgList) {
		this.orgList = orgList;
	}
	public List<OrgInfo> getSelectOrgs() {
		return selectOrgs;
	}
	public void setSelectOrgs(List<OrgInfo> selectOrgs) {
		this.selectOrgs = selectOrgs;
	}
	public int getStatusInt() {
		return statusInt;
	}
	public void setStatusInt(int statusInt) {
		this.statusInt = statusInt;
	}
	public List<RankingRuleHistory> getHisList() {
		return hisList;
	}
	public void setHisList(List<RankingRuleHistory> hisList) {
		this.hisList = hisList;
	}
	public String getLevelTotalMoney() {
		return levelTotalMoney;
	}
	public void setLevelTotalMoney(String levelTotalMoney) {
		this.levelTotalMoney = levelTotalMoney;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRuleCode() {
		return ruleCode;
	}
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getRuleType() {
		return ruleType;
	}
	public String getAdvertOrderNo() {
		return advertOrderNo;
	}
	public void setAdvertOrderNo(String advertOrderNo) {
		this.advertOrderNo = advertOrderNo;
	}
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getShowOrderNo() {
		return showOrderNo;
	}
	public void setShowOrderNo(String showOrderNo) {
		this.showOrderNo = showOrderNo;
	}
	public String getAdvertUrl() {
		return advertUrl;
	}
	public void setAdvertUrl(String advertUrl) {
		this.advertUrl = advertUrl;
	}
	public String getOpenOrg() {
		return openOrg;
	}
	public void setOpenOrg(String openOrg) {
		this.openOrg = openOrg;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getBonusNote() {
		return bonusNote;
	}
	public void setBonusNote(String bonusNote) {
		this.bonusNote = bonusNote;
	}
	public List<RankingRuleLevel> getLevelList() {
		return levelList;
	}
	public void setLevelList(List<RankingRuleLevel> levelList) {
		this.levelList = levelList;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getHistoryUrl() {
		return historyUrl;
	}
	public void setHistoryUrl(String historyUrl) {
		this.historyUrl = historyUrl;
	}
	
	public int getOpenTime() {
		return openTime;
	}
	public void setOpenTime(int openTime) {
		this.openTime = openTime;
	}
	public String getBusiType() {
		return busiType;
	}
	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}
	
	@Override
	public String toString() {
		return "RankingRule [id=" + id + ", ruleCode=" + ruleCode
				+ ", ruleName=" + ruleName + ", ruleType=" + ruleType
				+ ", dataType=" + dataType + ", status=" + status
				+ ", showOrderNo=" + showOrderNo + ", advertOrderNo="
				+ advertOrderNo + ", advertUrl=" + advertUrl + ", openOrg="
				+ openOrg + ", totalAmount=" + totalAmount + ", startTime="
				+ startTime + ", endTime=" + endTime + ", updateTime="
				+ updateTime + ", introduction=" + introduction
				+ ", bonusNote=" + bonusNote + ", levelList=" + levelList
				+ ", operate=" + operate + ", orgType=" + orgType
				+ ", historyUrl=" + historyUrl + "]";
	}
	
}
