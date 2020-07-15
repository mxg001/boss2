package cn.eeepay.framework.model;

import java.math.BigDecimal;

/**
 * superbank.red_account_info
 */
public class RedAccountInfo {
    private Long id;

    private String type;//账户类型(0平台、1品牌商、2个人)

    private String accountCode;//红包账号

    private Long relationId;//关联id(平台账号、品牌商账号填org_info表org_id、用户账号填user_info表user_id)

    private String userCode;//当账号类型为个人时有值，组织账号填空字符串，为了方便移动端查询

    private BigDecimal totalAmount;//红包账号余额(平台红包账户余额可为负数，用户和品牌商的红包账户余额不能为负数)

    private String status;//账号状态 0.正常；1.冻结

    private BigDecimal receviceMax;//最佳手气

    private Integer luckyValue;//总计100分，分数越高幸运值越高,必须为正整数

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode == null ? null : accountCode.trim();
    }

    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode == null ? null : userCode.trim();
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public BigDecimal getReceviceMax() {
        return receviceMax;
    }

    public void setReceviceMax(BigDecimal receviceMax) {
        this.receviceMax = receviceMax;
    }

    public Integer getLuckyValue() {
        return luckyValue;
    }

    public void setLuckyValue(Integer luckyValue) {
        this.luckyValue = luckyValue;
    }
}