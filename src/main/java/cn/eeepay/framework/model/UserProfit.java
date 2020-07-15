package cn.eeepay.framework.model;

/**
 * table:super.user_profit
 * desc:分润表
 * @author tans
 * @date 2017-12-04
 */

import cn.eeepay.framework.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public class UserProfit {
    private Long id;

    private String orderType;//订单类型:1代理授权；2信用卡申请 3收款 4信用卡还款 5贷款

    private String status;//订单状态:1已创建；2待支付；3:待审核 4已授权 5订单成功 6订单失败  9已关闭

    private String orderNo;//订单编号

    private Long orgId;//所属组织ID

    private String profitFormula;//分润公式，例如：固定金额的填1，比率的填0.6%

    private Integer profitLevel;//收益层级:1 1级；2 2级；3 3级；4 4级

    private String shareUserCode;//贡献人ID

    private String userCode;//受益人用户编号，如果是平台分润，默认：plate

    private Integer topUserLevel;//受益人所属上级用户层级

    private String userType;//10:普通用户； 20专员；30经理；40银行家；50组织；60平台

    private BigDecimal totalProfit;//总奖金

    private BigDecimal userProfit;//收益人奖金分润

    private Date createDate;//创建时间

    private Date completeDate;//完成时间

    private String accountStatus;//入账状态; 0：未入账； 1已入账；入账状态暂时记录在order_main表

    private String remark;//备注

    private String createDateStr;//创建时间的字符串形式
    private String createDateStart;//
    private String createDateEnd;//
    private String userName;//收益人名称
    private String userPhone;//收益人手机号
    private String shareUserName;//贡献人名称
    private String shareNickName;//贡献人昵称
    private String shareUserPhone;//贡献人手机号
    private String orgName;//组织名称
    private String openProvince;//订单贡献人省
    private String openCity;//订单贡献人市
    private String openRegion;//订单贡献人区
    private String shareUserRemark;//贡献人备注

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getProfitFormula() {
        return profitFormula;
    }

    public void setProfitFormula(String profitFormula) {
        this.profitFormula = profitFormula == null ? null : profitFormula.trim();
    }

    public Integer getProfitLevel() {
        return profitLevel;
    }

    public void setProfitLevel(Integer profitLevel) {
        this.profitLevel = profitLevel;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode == null ? null : userCode.trim();
    }

    public Integer getTopUserLevel() {
        return topUserLevel;
    }

    public void setTopUserLevel(Integer topUserLevel) {
        this.topUserLevel = topUserLevel;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
    }

    public BigDecimal getTotalProfit() {
        return totalProfit != null ? totalProfit : new BigDecimal(0);
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public BigDecimal getUserProfit() {
        return userProfit != null ? userProfit : new BigDecimal(0);
    }

    public void setUserProfit(BigDecimal userProfit) {
        this.userProfit = userProfit;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus == null ? null : accountStatus.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getShareUserCode() {
        return shareUserCode;
    }

    public void setShareUserCode(String shareUserCode) {
        this.shareUserCode = shareUserCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDateStr() {
        return createDate == null ? "" : DateUtil.getLongFormatDate(createDate);
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public String getCreateDateStart() {
        return createDateStart;
    }

    public void setCreateDateStart(String createDateStart) {
        this.createDateStart = createDateStart;
    }

    public String getCreateDateEnd() {
        return createDateEnd;
    }

    public void setCreateDateEnd(String createDateEnd) {
        this.createDateEnd = createDateEnd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getShareUserName() {
        return shareUserName;
    }

    public void setShareUserName(String shareUserName) {
        this.shareUserName = shareUserName;
    }

    public String getShareUserPhone() {
        return shareUserPhone;
    }

    public void setShareUserPhone(String shareUserPhone) {
        this.shareUserPhone = shareUserPhone;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getShareNickName() {
        return shareNickName;
    }

    public void setShareNickName(String shareNickName) {
        this.shareNickName = shareNickName;
    }

    public String getOpenProvince() {
        return openProvince;
    }

    public void setOpenProvince(String openProvince) {
        this.openProvince = openProvince;
    }

    public String getOpenCity() {
        return openCity;
    }

    public void setOpenCity(String openCity) {
        this.openCity = openCity;
    }

    public String getOpenRegion() {
        return openRegion;
    }

    public void setOpenRegion(String openRegion) {
        this.openRegion = openRegion;
    }

    public String getShareUserRemark() {
        return shareUserRemark;
    }

    public void setShareUserRemark(String shareUserRemark) {
        this.shareUserRemark = shareUserRemark;
    }
}