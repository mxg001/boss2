package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 直清商户导入报备记录表
 * `zq_file_sync_record`
 * @author tans
 * @date 2019/4/11 22:31
 */
public class ZqFileSyncRecord {

    private Integer id;
    /** 批次号 */
    private String batchNo;
    /** 通道编码 */
    private String channelCode;
    /** 操作人 */
    private String operator;
    /** 创建时间 */
    private Date createTime;
    /** 数据最后更新时间 */
    private Date lastUpdateTime;

    private String status;
    private String merchantNo;
    private String resultMsg;
    private Long bpId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public Long getBpId() {
        return bpId;
    }

    public void setBpId(Long bpId) {
        this.bpId = bpId;
    }
}
