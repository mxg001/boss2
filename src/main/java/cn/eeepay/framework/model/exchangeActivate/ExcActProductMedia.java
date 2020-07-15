package cn.eeepay.framework.model.exchangeActivate;

import java.util.Date;

/**
 * Created by Administrator on 2018/10/11/011.
 * @author  liuks
 * 媒体资源
 */
public class ExcActProductMedia {

    private  Integer id;//
    private  String channelNo;//核销渠道编号
    private  String goodTypeNo;//商品类别(上游渠道ID)
    private  String channelGoodNo;//核销渠道商品编号(上游)
    private  String channelGoodName;//核销渠道商品编号名称(上游)
    private  String message;//接口返回信息(上游)
    private  String goodFile;//商品附件包(阿里云的附件名)
    private  Date createTime;//创建时间

    private  String channelName;//核销渠道名称

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGoodFile() {
        return goodFile;
    }

    public void setGoodFile(String goodFile) {
        this.goodFile = goodFile;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
