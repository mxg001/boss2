package cn.eeepay.framework.model;

import java.util.Date;

/**
 * Created by Administrator on 2018/2/6/006.
 * @author  liuks
 * 商户进件项修改记录表
 * 对应表 merchant_require_history
 */
public class MerchantRequireHistory {

    private Long id;//id

    private String merchantNo;//商户号

    private String mriId;//进件要求项ID

    private String modifyType;//修改方式

    private String historyContent;//历史数据

    private String newContent;//现在数据

    private String batchNo;//修改批次号

    private String status;//1成功0失败

    private Date createTime;//创建时间

    private String creator;//创建人

    private String remark;//备注

    private String attribute1;//备用字段1

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMriId() {
        return mriId;
    }

    public void setMriId(String mriId) {
        this.mriId = mriId;
    }

    public String getModifyType() {
        return modifyType;
    }

    public void setModifyType(String modifyType) {
        this.modifyType = modifyType;
    }

    public String getHistoryContent() {
        return historyContent;
    }

    public void setHistoryContent(String historyContent) {
        this.historyContent = historyContent;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }
}
