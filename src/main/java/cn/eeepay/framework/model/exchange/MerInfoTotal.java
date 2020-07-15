package cn.eeepay.framework.model.exchange;

/**
 * Created by Administrator on 2018/6/20/020.
 * 超级兑用户统计
 */
public class MerInfoTotal {

    private Integer merTotal;//用户统计

    private Integer ordmemTotal;//普通用户统计
    private Integer supermemTotal;//超级用户统计
    private Integer ordparTotal;//会员统计
    private Integer goldparTotal;//黄金会员统计
    private Integer diamparTotal;//钻石会员统计

    private Integer merActTotal;//激活统计

    public Integer getMerTotal() {
        return merTotal;
    }

    public void setMerTotal(Integer merTotal) {
        this.merTotal = merTotal;
    }

    public Integer getOrdmemTotal() {
        return ordmemTotal;
    }

    public void setOrdmemTotal(Integer ordmemTotal) {
        this.ordmemTotal = ordmemTotal;
    }

    public Integer getSupermemTotal() {
        return supermemTotal;
    }

    public void setSupermemTotal(Integer supermemTotal) {
        this.supermemTotal = supermemTotal;
    }

    public Integer getOrdparTotal() {
        return ordparTotal;
    }

    public void setOrdparTotal(Integer ordparTotal) {
        this.ordparTotal = ordparTotal;
    }

    public Integer getGoldparTotal() {
        return goldparTotal;
    }

    public void setGoldparTotal(Integer goldparTotal) {
        this.goldparTotal = goldparTotal;
    }

    public Integer getDiamparTotal() {
        return diamparTotal;
    }

    public void setDiamparTotal(Integer diamparTotal) {
        this.diamparTotal = diamparTotal;
    }

    public Integer getMerActTotal() {
        return merActTotal;
    }

    public void setMerActTotal(Integer merActTotal) {
        this.merActTotal = merActTotal;
    }
}
