package cn.eeepay.framework.model.couponImport;

import cn.eeepay.framework.model.CardAndReward;

/**
 * Created by Administrator on 2019/6/5/005.
 * @author  liuks
 * 导入新增券实体 用于办卡，贷款
 */
public class CouponImportCard {

    private int couponCode;

    private String sendChannel;

    private String sendTypeId;

    private String merchantNo;

    private String merchantName;

    private String orderNo;

    private CardAndReward card;

    public int getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(int couponCode) {
        this.couponCode = couponCode;
    }

    public String getSendChannel() {
        return sendChannel;
    }

    public void setSendChannel(String sendChannel) {
        this.sendChannel = sendChannel;
    }

    public String getSendTypeId() {
        return sendTypeId;
    }

    public void setSendTypeId(String sendTypeId) {
        this.sendTypeId = sendTypeId;
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

    public CardAndReward getCard() {
        return card;
    }

    public void setCard(CardAndReward card) {
        this.card = card;
    }
}
