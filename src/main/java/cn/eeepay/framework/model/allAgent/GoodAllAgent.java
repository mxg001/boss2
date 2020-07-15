package cn.eeepay.framework.model.allAgent;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/7/13/013.
 * @author  liuks
 * 商品实体
 * 对应表 pa_goods
 */
public class GoodAllAgent {

    private Integer id;
    private String goodsName;//商户名称
    private String goodsCode;//商品编号
    private String goodsDesc;//商户描述
    private String img;//商户图片
    private String img2;//商户图片2
    private String img3;//商户图片3
    private String price;//单价
    private BigDecimal cost;//成本价
    private Integer minimum;//起购量
    private Integer isMulti;//是否倍增
    private Integer status;//状态 0:下架，1:上架
    private String brandCode;//品牌
    private String produteType;//硬件类型

    private String creater;//创建人
    private Date createTime;//创建时间
    private Date createTimeBegin;
    private Date createTimeEnd;

    private Date updateTime;//更新时间

    private String brandName;//品牌名称
    private String groupCode;//所属商品类型
    private String listType;//名单类型:white 白名单,black 黑名单,normal 普通
    private String detailImgs;//商品详情图片
    private Integer shipper;
    private Integer shipWay;
    private BigDecimal agio;
    private String color;
    private String size;
    private String groupName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getMinimum() {
        return minimum;
    }

    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }

    public Integer getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(Integer isMulti) {
        this.isMulti = isMulti;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getProduteType() {
        return produteType;
    }

    public void setProduteType(String produteType) {
        this.produteType = produteType;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
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

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public GoodAllAgent setGroupCode(String groupCode) {
        this.groupCode = groupCode;
        return this;
    }

    public String getListType() {
        return listType;
    }

    public GoodAllAgent setListType(String listType) {
        this.listType = listType;
        return this;
    }

    public String getDetailImgs() {
        return detailImgs;
    }

    public GoodAllAgent setDetailImgs(String detailImgs) {
        this.detailImgs = detailImgs;
        return this;
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

    public Integer getShipper() {
        return shipper;
    }

    public void setShipper(Integer shipper) {
        this.shipper = shipper;
    }

    public Integer getShipWay() {
        return shipWay;
    }

    public void setShipWay(Integer shipWay) {
        this.shipWay = shipWay;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
