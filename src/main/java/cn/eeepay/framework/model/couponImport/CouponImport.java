package cn.eeepay.framework.model.couponImport;

/**
 * Created by Administrator on 2019/6/5/005.
 * @author  liuks
 * 导入新增券实体
 */
public class CouponImport {

    private int couponCode;

    private int activityEntityId;

    private int addNum;

    private String merchantNo;

    private String merchantName;

    private String orderNo;

    public int getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(int couponCode) {
        this.couponCode = couponCode;
    }

    public int getActivityEntityId() {
        return activityEntityId;
    }

    public void setActivityEntityId(int activityEntityId) {
        this.activityEntityId = activityEntityId;
    }

    public int getAddNum() {
        return addNum;
    }

    public void setAddNum(int addNum) {
        this.addNum = addNum;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
