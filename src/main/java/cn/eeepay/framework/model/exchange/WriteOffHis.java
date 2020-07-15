package cn.eeepay.framework.model.exchange;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/5/9/009.
 * @author  liuks
 * 核销实体
 * 对应表 rdmp_declare_order_his
 */
public class WriteOffHis {

    private Long id;
    private String checkOper;//审核人
    private Date createTime;//创建时间

    private String orderNo;//订单号
    private String channel;//渠道,数据字典配置
    private String saleOrderNo;//核销渠道订单号
    private BigDecimal writeOffPrice;//核销价格
    private String remark;//核销原因
    private String checkStatus;//核销状态0核销中1核销成功2核销失败
    private String checkMode;//核销方式1一次核销2二次核销3导入核销4API核销
    private String receiveState;//是否是预付卡
    private String receiveStatus;//收货状态

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCheckOper() {
        return checkOper;
    }

    public void setCheckOper(String checkOper) {
        this.checkOper = checkOper;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCheckMode() {
        return checkMode;
    }

    public void setCheckMode(String checkMode) {
        this.checkMode = checkMode;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSaleOrderNo() {
        return saleOrderNo;
    }

    public void setSaleOrderNo(String saleOrderNo) {
        this.saleOrderNo = saleOrderNo;
    }

    public BigDecimal getWriteOffPrice() {
        return writeOffPrice;
    }

    public void setWriteOffPrice(BigDecimal writeOffPrice) {
        this.writeOffPrice = writeOffPrice;
    }

    public String getReceiveState() {
        return receiveState;
    }

    public void setReceiveState(String receiveState) {
        this.receiveState = receiveState;
    }

    public String getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
    }
}
