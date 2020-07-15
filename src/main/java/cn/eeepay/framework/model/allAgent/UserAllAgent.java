package cn.eeepay.framework.model.allAgent;

import java.util.Date;

/**
 * Created by Administrator on 2018/7/12/012.
 * @author  liuks
 * 超级盟主用户
 * 对应表 pa_user_info
 */
public class UserAllAgent {

    private Integer id;
    private String brandCode;//所属品牌
    private String userCode;//用户编号
    private String agentNo;//代理商编号
    private String agentNode;//代理商节点
    private String userNode;//用户节点
    private String shareCode;//推荐码
    private Integer userType;//用户类型 1：机构，2：大盟主，3：盟主
    private String pwd;//密码
    private String mobile;//手机号
    private Integer status;//状态 0：已注册，1：实名认证失败，2：正常
    private Integer userLevel;//用户节点级别： 机构1，大盟主2，盟主3，盟主4++
    private String grade;//用户身份:1：黄金盟主，2：铂金盟主，3：钻石盟主，4：钻石盟主，5：王者盟主
    private Integer shareLevel;//分润流水级别:  1: Lv1,  2:Lv2 ....

    private Date createTime;//创建时间
    private Date createTimeBegin;
    private Date createTimeEnd;


    private Date updateTime;//修改时间
    private String creater;//创建人
    private String realName;//真实姓名
    private String idCardNo;//身份证号

    private Integer sumMer;//直营商户
    private Integer sumUser;//发展盟主
    private String parentRealName;//上级用户姓名

    private String brandName;//所属品牌

    private String parentId;//上级用户编码
    private String oneUserCode;//一级用户编码
    private String twoUserCode;//二级用户编码
    private String oneAgentNo;//一级代理商编号
    private String twoAgentNo;//二级代理商编号
    private String oneAgentName;//一级代理商名称
    private String twoAgentName;//二级代理商名称

    private String regType;//注册途径

    private UserAllAgentCard card;//用户结算卡

    private Integer idCardNoState;//是否实名认证 0未认证,1已认证

    private String gradeStr;//分润等级字符

    private String gradeRate;//分润比例

    private String sumActivationMer;
    private String sumMerTransShare;
    private String sumMerUserTransShare;
    private String shareRatio;
    private String preRate;
    private String afterRate;
    private String startDate;
    private String endDate;
    private String transType;
    private String selectAgentNo;
    private String transStartTime;
    private String transEndTime;
    private String nickName;
    private String vipShareRatio;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode;
    }

    public String getUserNode() {
        return userNode;
    }

    public void setUserNode(String userNode) {
        this.userNode = userNode;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOneUserCode() {
        return oneUserCode;
    }

    public void setOneUserCode(String oneUserCode) {
        this.oneUserCode = oneUserCode;
    }

    public String getTwoUserCode() {
        return twoUserCode;
    }

    public void setTwoUserCode(String twoUserCode) {
        this.twoUserCode = twoUserCode;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getShareLevel() {
        return shareLevel;
    }

    public void setShareLevel(Integer shareLevel) {
        this.shareLevel = shareLevel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public Integer getSumMer() {
        return sumMer;
    }

    public void setSumMer(Integer sumMer) {
        this.sumMer = sumMer;
    }

    public Integer getSumUser() {
        return sumUser;
    }

    public void setSumUser(Integer sumUser) {
        this.sumUser = sumUser;
    }

    public String getParentRealName() {
        return parentRealName;
    }

    public void setParentRealName(String parentRealName) {
        this.parentRealName = parentRealName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public UserAllAgentCard getCard() {
        return card;
    }

    public void setCard(UserAllAgentCard card) {
        this.card = card;
    }

    public String getOneAgentNo() {
        return oneAgentNo;
    }

    public void setOneAgentNo(String oneAgentNo) {
        this.oneAgentNo = oneAgentNo;
    }

    public String getTwoAgentNo() {
        return twoAgentNo;
    }

    public void setTwoAgentNo(String twoAgentNo) {
        this.twoAgentNo = twoAgentNo;
    }

    public String getOneAgentName() {
        return oneAgentName;
    }

    public void setOneAgentName(String oneAgentName) {
        this.oneAgentName = oneAgentName;
    }

    public String getTwoAgentName() {
        return twoAgentName;
    }

    public void setTwoAgentName(String twoAgentName) {
        this.twoAgentName = twoAgentName;
    }

    public String getRegType() {
        return regType;
    }

    public void setRegType(String regType) {
        this.regType = regType;
    }

    public Integer getIdCardNoState() {
        return idCardNoState;
    }

    public void setIdCardNoState(Integer idCardNoState) {
        this.idCardNoState = idCardNoState;
    }

    public String getGradeStr() {
        return gradeStr;
    }

    public void setGradeStr(String gradeStr) {
        this.gradeStr = gradeStr;
    }

    public String getGradeRate() {
        return gradeRate;
    }

    public void setGradeRate(String gradeRate) {
        this.gradeRate = gradeRate;
    }

    public String getSumActivationMer() {
        return sumActivationMer;
    }

    public void setSumActivationMer(String sumActivationMer) {
        this.sumActivationMer = sumActivationMer;
    }

    public String getSumMerTransShare() {
        return sumMerTransShare;
    }

    public void setSumMerTransShare(String sumMerTransShare) {
        this.sumMerTransShare = sumMerTransShare;
    }

    public String getSumMerUserTransShare() {
        return sumMerUserTransShare;
    }

    public void setSumMerUserTransShare(String sumMerUserTransShare) {
        this.sumMerUserTransShare = sumMerUserTransShare;
    }

    public String getShareRatio() {
        return shareRatio;
    }

    public void setShareRatio(String shareRatio) {
        this.shareRatio = shareRatio;
    }

    public String getPreRate() {
        return preRate;
    }

    public void setPreRate(String preRate) {
        this.preRate = preRate;
    }

    public String getAfterRate() {
        return afterRate;
    }

    public void setAfterRate(String afterRate) {
        this.afterRate = afterRate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getSelectAgentNo() {
        return selectAgentNo;
    }

    public void setSelectAgentNo(String selectAgentNo) {
        this.selectAgentNo = selectAgentNo;
    }

    public String getTransStartTime() {
        return transStartTime;
    }

    public void setTransStartTime(String transStartTime) {
        this.transStartTime = transStartTime;
    }

    public String getTransEndTime() {
        return transEndTime;
    }

    public void setTransEndTime(String transEndTime) {
        this.transEndTime = transEndTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getVipShareRatio() {
        return vipShareRatio;
    }

    public void setVipShareRatio(String vipShareRatio) {
        this.vipShareRatio = vipShareRatio;
    }
}
