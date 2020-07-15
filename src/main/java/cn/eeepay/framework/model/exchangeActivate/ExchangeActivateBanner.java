package cn.eeepay.framework.model.exchangeActivate;

import java.util.Date;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * 超级兑广告实体
 * 对应表 yfb_banner
 */
public class ExchangeActivateBanner {

    private Long id;

    private String applyType;//应用类型 (1全部,2公众号,3App)

    private String oemNo;//组织编码

    private String bannerName;//广告名称

    private Date upTime;//上线时间

    private Date downTime;//下线时间

    private Integer showNo;//权重

    private String status;//状态(1启用2关闭)

    private String imgUrl;//图片url

    private String link;//连接

    private String remark;//备注

    private String postionType;//广告位置(1首页,2其他)

    private String oemName;//组织名称

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getOemNo() {
        return oemNo;
    }

    public void setOemNo(String oemNo) {
        this.oemNo = oemNo;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public Date getUpTime() {
        return upTime;
    }

    public void setUpTime(Date upTime) {
        this.upTime = upTime;
    }

    public Date getDownTime() {
        return downTime;
    }

    public void setDownTime(Date downTime) {
        this.downTime = downTime;
    }

    public Integer getShowNo() {
        return showNo;
    }

    public void setShowNo(Integer showNo) {
        this.showNo = showNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPostionType() {
        return postionType;
    }

    public void setPostionType(String postionType) {
        this.postionType = postionType;
    }

    public String getOemName() {
        return oemName;
    }

    public void setOemName(String oemName) {
        this.oemName = oemName;
    }
}
