package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

/**
 * 排行榜明细
 * @author Administrator
 *
 */

public class RankingRecordDetail implements Comparator<RankingRecordDetail> {

	private String id;
	private String recordId;
	private String userName;			//用户姓名
	private String userCode;			//用户id		
	private String userTotalAmount;		//统计的用户总额
	private String isRank;				//是否获奖
	private String rankingLevel;		//获奖等级
	private String rankingAmount;		//获奖奖金
	private String status;				//用户发放状态		(个人的发放状态)
	private Date removeTime;			//移除时间
	private Date pushTime;				//发放时间
	private String remark;
	private String nickName;			//微信昵称
	private String totalAmount;			//统计总额  
	
	private String rankingNo;			//排行榜编号
	private String rankingStatus;		//榜单状态	(排行榜榜单的状态)
	private String rankingName;			//排行榜名称
	private String rankingType;			//排行榜类型
	private String batchNo;				//期号
	private String pushNum;				//本期获奖人数
	private String pushTotalAmount;		//本期奖金
	private String orgName;				//所属组织
	private String pushRealNum;			//实发人数
	private String pushRealAmount;		//实发奖金
	private Date startDate;				//统计开始时间
	private Date endDate;				//统计结束时间
	private String phone;				//用户手机号
	private Date toagentDate;			//成为专员时间
	private String allUsersTotal;		//所有用户的统计总额
	
	private String rankingIndex;		//排名序号
	
	private String ruleNo;				//规则编号
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserTotalAmount() {
		return userTotalAmount;
	}
	public void setUserTotalAmount(String userTotalAmount) {
		this.userTotalAmount = userTotalAmount;
	}
	public String getIsRank() {
		return isRank;
	}
	public void setIsRank(String isRank) {
		this.isRank = isRank;
	}
	public String getRankingLevel() {
		return rankingLevel;
	}
	public void setRankingLevel(String rankingLevel) {
		this.rankingLevel = rankingLevel;
	}
	public String getRankingAmount() {
		return rankingAmount;
	}
	public void setRankingAmount(String rankingAmount) {
		this.rankingAmount = rankingAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getRankingNo() {
		return rankingNo;
	}
	public void setRankingNo(String rankingNo) {
		this.rankingNo = rankingNo;
	}
	public String getRankingStatus() {
		return rankingStatus;
	}
	public void setRankingStatus(String rankingStatus) {
		this.rankingStatus = rankingStatus;
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
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
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

	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getAllUsersTotal() {
		return allUsersTotal;
	}
	public void setAllUsersTotal(String allUsersTotal) {
		this.allUsersTotal = allUsersTotal;
	}
	public String getRankingIndex() {
		return rankingIndex;
	}
	public void setRankingIndex(String rankingIndex) {
		this.rankingIndex = rankingIndex;
	}
	public String getRuleNo() {
		return ruleNo;
	}
	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getToagentDate() {
		return toagentDate;
	}
	public void setToagentDate(Date toagentDate) {
		this.toagentDate = toagentDate;
	}
	public Date getRemoveTime() {
		return removeTime;
	}
	public void setRemoveTime(Date removeTime) {
		this.removeTime = removeTime;
	}
	public Date getPushTime() {
		return pushTime;
	}
	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
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
	/**倒序*/
	@Override
	public int compare(RankingRecordDetail o1, RankingRecordDetail o2) {
		
		if(new BigDecimal(o1.getUserTotalAmount()).compareTo(new BigDecimal(o2.getUserTotalAmount()))==1){
			return -1;
		}else if(new BigDecimal(o1.getUserTotalAmount()).compareTo(new BigDecimal(o2.getUserTotalAmount()))==-1){
			return 1;
		}else{
			if(o1.getToagentDate()==null&&o2.getToagentDate()!=null){//o1排在后面
				return 1;
			}
			if(o1.getToagentDate()!=null&&o2.getToagentDate()==null){//o1排在前面
				return -1;
			}
			if(o1.getToagentDate()!=null&&o2.getToagentDate()!=null){
				if(o1.getToagentDate().before(o2.getToagentDate())){  // (第一个早于第二个)   (排名应该排在前面)
					return -1;
				}else if(o1.getToagentDate().after(o2.getToagentDate())){
					return 1;
				}
			}
		}
		
		return 0;
	}
	
}
