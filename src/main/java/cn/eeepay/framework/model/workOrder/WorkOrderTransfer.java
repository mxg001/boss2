package cn.eeepay.framework.model.workOrder;

/**
 * @author ：quanhz
 * @date ：Created in 2020/5/7 10:48
 */
public class WorkOrderTransfer {
    private String[] orderNoArr;
    private Integer receiverId;

    public String[] getOrderNoArr() {
        return orderNoArr;
    }

    public void setOrderNoArr(String[] orderNoArr) {
        this.orderNoArr = orderNoArr;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }
}
