package cn.eeepay.framework.model.risk;

import java.util.List;

/**
 * @author MXG
 * create 2018/12/25
 */
public class DealReplyRecord {
    private String orderNo;
    private String rulesNo;
    private String dealMsg;
    private String dealTime;
    private String replyMsg;
    private String replyTime;
    private String filesName;
    private List<FileType> filesList;//附件列表
    private Integer replierType;
    private String merchantName;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRulesNo() {
        return rulesNo;
    }

    public void setRulesNo(String rulesNo) {
        this.rulesNo = rulesNo;
    }

    public String getDealMsg() {
        return dealMsg;
    }

    public void setDealMsg(String dealMsg) {
        this.dealMsg = dealMsg;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getReplyMsg() {
        return replyMsg;
    }

    public void setReplyMsg(String replyMsg) {
        this.replyMsg = replyMsg;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public String getFilesName() {
        return filesName;
    }

    public void setFilesName(String filesName) {
        this.filesName = filesName;
    }

    public List<FileType> getFilesList() {
        return filesList;
    }

    public void setFilesList(List<FileType> filesList) {
        this.filesList = filesList;
    }

	public Integer getReplierType() {
		return replierType;
	}

	public void setReplierType(Integer replierType) {
		this.replierType = replierType;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
}
