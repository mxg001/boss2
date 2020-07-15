package cn.eeepay.framework.model.allAgent;

import java.math.BigDecimal;

public class GoodsPrice {
    private Integer id;
    private String goodsCode;
    private BigDecimal price;
    private BigDecimal cost;
    private BigDecimal agio;
    private String color;
    private String size;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getAgio() {
        return agio;
    }

    public void setAgio(BigDecimal agio) {
        this.agio = agio;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
