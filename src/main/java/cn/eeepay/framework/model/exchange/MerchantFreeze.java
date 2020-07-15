package cn.eeepay.framework.model.exchange;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/5/10/010.
 * @author  liuks
 * 商户预冻结记录
 * 对应表 rdmp_merchant_info_his
 */
public class MerchantFreeze {

    private Long id;

    private String merchantNo;//商户号

    private String freezeOper;//操作人

    private Date createTime;//创建时间

    private BigDecimal freezeAmount;//预冻结金额字段

    private String remark;//冻结原因

    private String freezeStatus;//操作状态0解冻1冻结

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

    public String getFreezeOper() {
        return freezeOper;
    }

    public void setFreezeOper(String freezeOper) {
        this.freezeOper = freezeOper;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFreezeStatus() {
        return freezeStatus;
    }

    public void setFreezeStatus(String freezeStatus) {
        this.freezeStatus = freezeStatus;
    }
}
