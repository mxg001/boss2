package cn.eeepay.framework.model.exchange;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/9/30/030.
 * @author  liuks
 * 核销渠道商品映射
 * 对应表rdmp_pass_good
 */
public class ExcRouteGood {

    private  Integer  id;
    private  String channelNo;//核销渠道编号
    private  String goodTypeNo;//商品类别(上游渠道ID)
    private  String channelGoodNo;//核销渠道商品编号(上游)
    private  String channelGoodName;//核销渠道商品名称(上游)
    private  String goodContentId;//商品内容ID(上游gather_content_id)
    private  String goodMode;//核销方式(1文本2图片)
    private  Long  pId;//产品ID
    private  Integer status;//路由状态 0关闭 1开启
    private BigDecimal channelPrice;//核销渠道价格
    private Date createTime;//创建时间
    private Date lastUpdateTime;//最后修改时间

    private  String channelName;//核销渠道名称
    private  String pName;//商品名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getGoodTypeNo() {
        return goodTypeNo;
    }

    public void setGoodTypeNo(String goodTypeNo) {
        this.goodTypeNo = goodTypeNo;
    }

    public String getChannelGoodNo() {
        return channelGoodNo;
    }

    public void setChannelGoodNo(String channelGoodNo) {
        this.channelGoodNo = channelGoodNo;
    }

    public String getChannelGoodName() {
        return channelGoodName;
    }

    public void setChannelGoodName(String channelGoodName) {
        this.channelGoodName = channelGoodName;
    }

    public String getGoodContentId() {
        return goodContentId;
    }

    public void setGoodContentId(String goodContentId) {
        this.goodContentId = goodContentId;
    }

    public String getGoodMode() {
        return goodMode;
    }

    public void setGoodMode(String goodMode) {
        this.goodMode = goodMode;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getChannelPrice() {
        return channelPrice;
    }

    public void setChannelPrice(BigDecimal channelPrice) {
        this.channelPrice = channelPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }
}
