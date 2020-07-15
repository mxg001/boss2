package cn.eeepay.framework.model.risk;

/**
 * 黑名单资料风控处理记录表
 * @author MXG
 * create 2018/12/22
 */
public class DealRecord {

    private String id;
    private String orderNo; //处理单号
    private String origOrderNo; //原始black_data_info表order_no
    private String riskDealTemplateNo; //风控处理模板编号
    private String riskDealMsg; //风控处理内容
    private String status; //处理记录状态 0已失效 1正常 2已完成（商户回复后即为已完成）
    private String creater;//创建人 system为系统
    private String createTime;
    private String lastUpdateTime;
    private String merchantNo;

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrigOrderNo() {
        return origOrderNo;
    }

    public void setOrigOrderNo(String origOrderNo) {
        this.origOrderNo = origOrderNo;
    }

    public String getRiskDealTemplateNo() {
        return riskDealTemplateNo;
    }

    public void setRiskDealTemplateNo(String riskDealTemplateNo) {
        this.riskDealTemplateNo = riskDealTemplateNo;
    }

    public String getRiskDealMsg() {
        return riskDealMsg;
    }

    public void setRiskDealMsg(String riskDealMsg) {
        this.riskDealMsg = riskDealMsg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
