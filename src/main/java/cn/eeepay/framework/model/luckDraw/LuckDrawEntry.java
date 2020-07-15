package cn.eeepay.framework.model.luckDraw;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/11/8/008.
 * @author liuks
 * 中奖纪录的奖品
 * 对应表 awards_item_log
 */
public class LuckDrawEntry {

    private Integer id;
    private Integer awardsRecodeId;//中奖记录ID
    private String  couponId;//券ID
    private BigDecimal money;//金额
    private Date createTime;//创建时间
    private Integer status;//0未发放1已发放(暂用于系统内部)
    private String couponName;//券名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAwardsRecodeId() {
        return awardsRecodeId;
    }

    public void setAwardsRecodeId(Integer awardsRecodeId) {
        this.awardsRecodeId = awardsRecodeId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }
}
