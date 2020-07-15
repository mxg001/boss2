package cn.eeepay.framework.model;

/**
 * Created by Administrator on 2018/1/20/020.
 * @author liuks
 * 红包图片信息
 * 对应表 red_orders_imgs
 */
public class RedEnvelopesGrantImage {
    private Long id;//图片id

    private Long redOrderId;//红包订单id关联red_orders的主键

    private String status;//图片状态(0正常1已屏蔽)

    private String imgUrl;//图片地址或者路径

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRedOrderId() {
        return redOrderId;
    }

    public void setRedOrderId(Long redOrderId) {
        this.redOrderId = redOrderId;
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
}
