package cn.eeepay.framework.model.risk;

import java.util.List;

/**
 * 黑名单资料商户回复记录表
 * @author MXG
 * create 2018/12/22
 */
public class ReplyRecord {
    private String id;
    private String orderNo;
    private String merchantNo;
    private String origOrderNo;
    private String dealRecordOrderNo; //风控处理单号
    private String status; //回复记录状态 0 已失效 1 正常
    private String replyRemark; //回复说明
    private String replyFilesName;
    private String createTime;
    private List<FileType> filesList;//附件列表
    private String merchantName;// 回复名称
    private Integer replierType;//　回复类型

    public List<FileType> getFilesList() {
        return filesList;
    }

    public void setFilesList(List<FileType> filesList) {
        this.filesList = filesList;
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

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getOrigOrderNo() {
        return origOrderNo;
    }

    public void setOrigOrderNo(String origOrderNo) {
        this.origOrderNo = origOrderNo;
    }

    public String getDealRecordOrderNo() {
        return dealRecordOrderNo;
    }

    public void setDealRecordOrderNo(String dealRecordOrderNo) {
        this.dealRecordOrderNo = dealRecordOrderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReplyRemark() {
        return replyRemark;
    }

    public void setReplyRemark(String replyRemark) {
        this.replyRemark = replyRemark;
    }

    public String getReplyFilesName() {
        return replyFilesName;
    }

    public void setReplyFilesName(String replyFilesName) {
        this.replyFilesName = replyFilesName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Integer getReplierType() {
		return replierType;
	}

	public void setReplierType(Integer replierType) {
		this.replierType = replierType;
	}
}
