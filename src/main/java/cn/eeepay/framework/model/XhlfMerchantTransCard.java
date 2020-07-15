package cn.eeepay.framework.model;

import java.util.Date;

/**
 * @author tans
 * @date 2020/3/15 14:04
 */
public class XhlfMerchantTransCard {

    private Long id;

    private String merchantNo;//商户号

    private String activeOrder;//激活订单号

    private String accountNo;//交易卡号（快钱是脱敏的）

    private String type;//新欢乐送统计类型,参照数据字典:XHLS_TRANS_TOTAL_TYPES

    private Date createTime;//创建时间

    private Date updateTime;//更新时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getActiveOrder() {
        return activeOrder;
    }

    public void setActiveOrder(String activeOrder) {
        this.activeOrder = activeOrder;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
