package cn.eeepay.framework.model.cjt;

import java.util.Date;

/**
 * @author tans
 * @date 2019/5/31 16:59
 */
public class CjtOrderSn {

    private Long id;
    private String merchantNo;//购买者商户编号
    private String buyOrderNo;//购买订单号
    private String sn;//机具sn号
    private Date createTime; //创建时间

    private String hpName;//硬件产品类型名称

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

    public String getBuyOrderNo() {
        return buyOrderNo;
    }

    public void setBuyOrderNo(String buyOrderNo) {
        this.buyOrderNo = buyOrderNo;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getHpName() {
        return hpName;
    }

    public void setHpName(String hpName) {
        this.hpName = hpName;
    }
}
