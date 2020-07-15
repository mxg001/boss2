package cn.eeepay.framework.model.allAgent;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/12/6/006.
 * @author  liuks
 * 排行榜 list
 * 对应表 pa_month_rank_list
 */
public class RankList {

    private Long id;
    private String userCode;//用户编号
    private String batchNo;//汇总批次
    private String orderNo;//排行榜入账单号
    private Integer actNum;//当月总激活数量
    private BigDecimal rewardAmount;//奖励金额
    private String countMonth;//统计月份,yyyy-mm格式
    private Date createTime;//创建时间
    private Integer rank;//名次
    private String entryStatus;//入账状态 0 未入账  1 已入账  2 不需要入账
    private String entryRemark;//入账描述
    private Date entryTime;//入账时间
    private Date entryTimeBegin;
    private Date entryTimeEnd;


    private String nickName;//昵称
    private String oneAgentNo;//一级代理编号
    private String mobile;//手机号
    private String realName;//盟主姓名
    private String userType;//用户类型
    private String oneUserCode;//一级用户编码

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getActNum() {
        return actNum;
    }

    public void setActNum(Integer actNum) {
        this.actNum = actNum;
    }

    public BigDecimal getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(BigDecimal rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public String getCountMonth() {
        return countMonth;
    }

    public void setCountMonth(String countMonth) {
        this.countMonth = countMonth;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getEntryStatus() {
        return entryStatus;
    }

    public void setEntryStatus(String entryStatus) {
        this.entryStatus = entryStatus;
    }

    public String getEntryRemark() {
        return entryRemark;
    }

    public void setEntryRemark(String entryRemark) {
        this.entryRemark = entryRemark;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public Date getEntryTimeBegin() {
        return entryTimeBegin;
    }

    public void setEntryTimeBegin(Date entryTimeBegin) {
        this.entryTimeBegin = entryTimeBegin;
    }

    public Date getEntryTimeEnd() {
        return entryTimeEnd;
    }

    public void setEntryTimeEnd(Date entryTimeEnd) {
        this.entryTimeEnd = entryTimeEnd;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOneAgentNo() {
        return oneAgentNo;
    }

    public void setOneAgentNo(String oneAgentNo) {
        this.oneAgentNo = oneAgentNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getOneUserCode() {
        return oneUserCode;
    }

    public void setOneUserCode(String oneUserCode) {
        this.oneUserCode = oneUserCode;
    }
}
