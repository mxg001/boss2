package cn.eeepay.framework.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Date;

/**
 * 报报备表 zq_file_sync
 * 
 * @author tans
 * @date 2019-04-11
 */
public class ZqFileSync
{
	/**  */
	private Integer id;
	/** 批次号 */
	private String batchNo;
	/** 通道编码 */
	private String channelCode;
	/** 文件名 */
	private String fileName;
	/** 文件地址 */
	private String fileUrl;
	/** 文件内有效数据总条数 */
	private Integer innerNum;
	/** 操作人 */
	private String operator;
	/** 创建时间 */
	private Date createTime;
	/** 完成时间 */
	private Date completeTime;
	/** 数据最后更新时间 */
	private Date lastUpdateTime;

	private String createTimeStart;
	private String createTimeEnd;
	private String createTimeStr;
	private String status;
	private String statusName;
	private String lastUpdateTimeStr;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getLastUpdateTimeStr() {
		return lastUpdateTimeStr;
	}

	public void setLastUpdateTimeStr(String lastUpdateTimeStr) {
		this.lastUpdateTimeStr = lastUpdateTimeStr;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setBatchNo(String batchNo) 
	{
		this.batchNo = batchNo;
	}

	public String getBatchNo() 
	{
		return batchNo;
	}
	public void setChannelCode(String channelCode) 
	{
		this.channelCode = channelCode;
	}

	public String getChannelCode() 
	{
		return channelCode;
	}
	public void setFileName(String fileName) 
	{
		this.fileName = fileName;
	}

	public String getFileName() 
	{
		return fileName;
	}
	public void setFileUrl(String fileUrl) 
	{
		this.fileUrl = fileUrl;
	}

	public String getFileUrl() 
	{
		return fileUrl;
	}
	public void setInnerNum(Integer innerNum) 
	{
		this.innerNum = innerNum;
	}

	public Integer getInnerNum() 
	{
		return innerNum;
	}
	public void setOperator(String operator) 
	{
		this.operator = operator;
	}

	public String getOperator() 
	{
		return operator;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}
	public void setCompleteTime(Date completeTime) 
	{
		this.completeTime = completeTime;
	}

	public Date getCompleteTime() 
	{
		return completeTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) 
	{
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getLastUpdateTime() 
	{
		return lastUpdateTime;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("batchNo", getBatchNo())
            .append("channelCode", getChannelCode())
            .append("fileName", getFileName())
            .append("fileUrl", getFileUrl())
            .append("innerNum", getInnerNum())
            .append("operator", getOperator())
            .append("createTime", getCreateTime())
            .append("completeTime", getCompleteTime())
            .append("lastUpdateTime", getLastUpdateTime())
            .toString();
    }
}
