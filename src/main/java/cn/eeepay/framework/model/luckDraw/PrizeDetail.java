package cn.eeepay.framework.model.luckDraw;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/11/6/006.
 * @author  liuks
 * 奖项里的 奖品
 * 对应表 awards_item
 */
public class PrizeDetail {

    private Integer id;
    private Integer awardConfigId;//奖项配置 ID
    private Integer couponId;//奖项编号,对应coupon_activity_entity的ID
    private Integer itemCount;//数量
    private BigDecimal money;//金额
    private String couponName;//券名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAwardConfigId() {
        return awardConfigId;
    }

    public void setAwardConfigId(Integer awardConfigId) {
        this.awardConfigId = awardConfigId;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }
}
