package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * superbamk.red_user_conf
 * @author tans
 * @date 2018-01-20
 * 个人发放红包管理表
 */
public class RedUserConf {
    private Long id;

    private Integer userLimit;//普通用户单日领取上限个数(-1表示无上限)

    private Integer memberLimit;//专员每人领取的上限数(-1表示无上限)

    private Integer managerLimit;//经理每日领取的上限数(-1表示无上限)

    private Integer bankerLimit;//银行家每日领取的上限数(-1表示无上限)

    private Integer userSendLimit;//普通用户单日可发上限个数(-1表示无上限)

    private Integer memberSendLimit;//专员每人可发的上限数(-1表示无上限)

    private Integer managerSendLimit;//经理每日可发的上限数(-1表示无上限)

    private Integer bankerSendLimit;//银行家每日可发的上限数(-1表示无上限)

    private Integer effectiveTime;//红包有效期(单位是小时并且只能为整数,-1表示不限制)

    private BigDecimal totalAmountMin;//红包总金额最小值

    private BigDecimal totalAmountMax;//红包总金额最大值

    private Integer redNumber;//红包可抢人数上限

    private BigDecimal amountMin;//单个红包最小额

    private String scalePlate;//红包分成比例配置-平台(必须为整数的百分比)

    private String scaleOrg;//红包分成比例配置-OEM(必须为整数的百分比)

    private String scaleAgentTotal;//红包分成比例配置-各级代理总奖金(必须为整数的百分比)

    private String scaleUser;//红包分成比例配置-用户领抢(必须为整数的百分比)

    private String scaleMember;//红包分成比例配置-专员分佣(必须为整数的百分比)

    private String scaleManager;//红包分成比例配置-经理分佣(必须为整数的百分比)

    private String scaleBanker;//红包分成比例配置-银行家分佣(必须为整数的百分比)

    private String recoveryType;//到期处理方式(0原路退回1归平台所有)

    private Date createTime;//创建时间

    private Date updateTime;//修改时间

    private Integer operator;//操作人ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(Integer userLimit) {
        this.userLimit = userLimit;
    }

    public Integer getMemberLimit() {
        return memberLimit;
    }

    public void setMemberLimit(Integer memberLimit) {
        this.memberLimit = memberLimit;
    }

    public Integer getManagerLimit() {
        return managerLimit;
    }

    public void setManagerLimit(Integer managerLimit) {
        this.managerLimit = managerLimit;
    }

    public Integer getBankerLimit() {
        return bankerLimit;
    }

    public void setBankerLimit(Integer bankerLimit) {
        this.bankerLimit = bankerLimit;
    }

    public Integer getUserSendLimit() {
        return userSendLimit;
    }

    public void setUserSendLimit(Integer userSendLimit) {
        this.userSendLimit = userSendLimit;
    }

    public Integer getMemberSendLimit() {
        return memberSendLimit;
    }

    public void setMemberSendLimit(Integer memberSendLimit) {
        this.memberSendLimit = memberSendLimit;
    }

    public Integer getManagerSendLimit() {
        return managerSendLimit;
    }

    public void setManagerSendLimit(Integer managerSendLimit) {
        this.managerSendLimit = managerSendLimit;
    }

    public Integer getBankerSendLimit() {
        return bankerSendLimit;
    }

    public void setBankerSendLimit(Integer bankerSendLimit) {
        this.bankerSendLimit = bankerSendLimit;
    }

    public Integer getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Integer effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public BigDecimal getTotalAmountMin() {
        return totalAmountMin;
    }

    public void setTotalAmountMin(BigDecimal totalAmountMin) {
        this.totalAmountMin = totalAmountMin;
    }

    public BigDecimal getTotalAmountMax() {
        return totalAmountMax;
    }

    public void setTotalAmountMax(BigDecimal totalAmountMax) {
        this.totalAmountMax = totalAmountMax;
    }

    public Integer getRedNumber() {
        return redNumber;
    }

    public void setRedNumber(Integer redNumber) {
        this.redNumber = redNumber;
    }

    public BigDecimal getAmountMin() {
        return amountMin;
    }

    public void setAmountMin(BigDecimal amountMin) {
        this.amountMin = amountMin;
    }

    public String getScalePlate() {
        return scalePlate;
    }

    public void setScalePlate(String scalePlate) {
        this.scalePlate = scalePlate == null ? null : scalePlate.trim();
    }

    public String getScaleOrg() {
        return scaleOrg;
    }

    public void setScaleOrg(String scaleOrg) {
        this.scaleOrg = scaleOrg == null ? null : scaleOrg.trim();
    }

    public String getScaleAgentTotal() {
        return scaleAgentTotal;
    }

    public void setScaleAgentTotal(String scaleAgentTotal) {
        this.scaleAgentTotal = scaleAgentTotal == null ? null : scaleAgentTotal.trim();
    }

    public String getScaleUser() {
        return scaleUser;
    }

    public void setScaleUser(String scaleUser) {
        this.scaleUser = scaleUser == null ? null : scaleUser.trim();
    }

    public String getScaleMember() {
        return scaleMember;
    }

    public void setScaleMember(String scaleMember) {
        this.scaleMember = scaleMember == null ? null : scaleMember.trim();
    }

    public String getScaleManager() {
        return scaleManager;
    }

    public void setScaleManager(String scaleManager) {
        this.scaleManager = scaleManager == null ? null : scaleManager.trim();
    }

    public String getScaleBanker() {
        return scaleBanker;
    }

    public void setScaleBanker(String scaleBanker) {
        this.scaleBanker = scaleBanker == null ? null : scaleBanker.trim();
    }

    public String getRecoveryType() {
        return recoveryType;
    }

    public void setRecoveryType(String recoveryType) {
        this.recoveryType = recoveryType == null ? null : recoveryType.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }
}