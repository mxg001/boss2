package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class RankingPushRecordInfo {

    private String orderNo;         //获奖订单号
    private String rankingNo;       //排行榜编号
    private String batchNo;         //期号
    private String ruleNo;          //规则编号
    private String rankingName;     //排行榜名称
    private Integer rankingType;     //排行榜类型
    private String orgId;           //所属组织
    private String orgName;         //组织名称
    private String showNo;          //排名
    private String userName;        //用户姓名
    private String nickName;        //微信昵称
    private String deNickName;      //转义昵称
    private String userCode;        //用户ID
    private String phone;           //手机号
    private String rankingData;     //统计总额
    private String rankingLevel;    //获奖等级
    private BigDecimal rankingMoney;//获奖金额
    private Integer pushStatus;      //用户发放状态
    private Integer accountStatus;   //记账状态
    private String removeRemark;    //移除说明
    private Date pushTime;          //发放时间
    private Date removeTime;        //移除时间
    private String dataType;		//统计数据类型
    private Date startTime;			//统计开始时间
    private Date endTime;			//统计结束时间
    

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public Integer getRankingType() {
        return rankingType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getShowNo() {
        return showNo;
    }

    public void setShowNo(String showNo) {
        this.showNo = showNo;
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

    public String getRankingData() {
        return rankingData;
    }

    public void setRankingData(String rankingData) {
        this.rankingData = rankingData;
    }

    public String getRankingLevel() {
        return rankingLevel;
    }

    public void setRankingLevel(String rankingLevel) {
        this.rankingLevel = rankingLevel;
    }

    public BigDecimal getRankingMoney() {
        return rankingMoney;
    }

    public void setRankingMoney(BigDecimal rankingMoney) {
        this.rankingMoney = rankingMoney;
    }

    public void setRankingType(Integer rankingType) {
        this.rankingType = rankingType;
    }

    public Integer getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(Integer pushStatus) {
        this.pushStatus = pushStatus;
    }

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getRemoveRemark() {
        return removeRemark;
    }

    public void setRemoveRemark(String removeRemark) {
        this.removeRemark = removeRemark;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public Date getRemoveTime() {
        return removeTime;
    }

    public void setRemoveTime(Date removeTime) {
        this.removeTime = removeTime;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getDeNickName() {
        return deNickName;
    }

    public void setDeNickName(String deNickName) {
        this.deNickName = deNickName;
    }

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
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
}
