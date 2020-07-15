package cn.eeepay.framework.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Date;

/**
 * 直清商户服务报件表 zq_service_info
 * 
 * @author tans
 * @date 2019-04-02
 */
public class ZqServiceInfo {
	private Long id;
	/** 商户号 */
	private String merchantNo;
	/** 进件ID */
	private Long mbpId;
	/** 服务ID */
	private Long serviceId;
	/** 0待进件,1进件失败2进件成功 */
	private String status;
	/** 创建时间 */
	private Date createTime;
	/** 最后更新时间 */
	private Date lastUpdateTime;
	/** 消息描述 */
	private String msg;
	/** 上游返回状态码 */
	private String code;
	/** 操作人 */
	private String operator;
	/** 通道编码 */
	private String channelCode;
	/** 业务产品ID */
	private Long bpId;
	private String acqServiceMerNo;//服务对应的上游商户号
	private String merWxNo;//商户申请服务的微信号
	private String merRealName;//商户微信对应真实姓名
	private String dealStatus;//业务处理状态 0初始化 1未处理 2处理成功 3处理失败
	private String dealOperator;//业务处理操作人

	private String merchantName;//商户名称
	private String mobilephone;//手机号
	private String bpName;//业务产品名称
	private String serviceName;//服务名称
	private String unionpayMerNo;//直清商户号
	private String createTimeStart;//创建时间开始
	private String createTimeEnd;//创建时间结束
	private String lastUpdateTimeStart;//最后更新时间开始
	private String lastUpdateTimeEnd;//最后更新时间结束
	private String statusName;//状态名称
	private String createTimeStr;//创建时间
	private String lastUpdateTimeStr;//最后更新时间
	private String dealStatusName;//业务处理状态 0初始化 1未处理 2处理成功 3处理失败

	public void setId(Long id) 
	{
		this.id = id;
	}

	public Long getId() 
	{
		return id;
	}
	public void setMerchantNo(String merchantNo) 
	{
		this.merchantNo = merchantNo;
	}

	public String getMerchantNo() 
	{
		return merchantNo;
	}
	public void setMbpId(Long mbpId) 
	{
		this.mbpId = mbpId;
	}

	public Long getMbpId() 
	{
		return mbpId;
	}
	public void setServiceId(Long serviceId) 
	{
		this.serviceId = serviceId;
	}

	public Long getServiceId() 
	{
		return serviceId;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) 
	{
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getLastUpdateTime() 
	{
		return lastUpdateTime;
	}
	public void setMsg(String msg) 
	{
		this.msg = msg;
	}

	public String getMsg() 
	{
		return msg;
	}
	public void setCode(String code) 
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	public void setOperator(String operator) 
	{
		this.operator = operator;
	}

	public String getOperator() 
	{
		return operator;
	}
	public void setChannelCode(String channelCode) 
	{
		this.channelCode = channelCode;
	}

	public String getChannelCode() 
	{
		return channelCode;
	}
	public void setBpId(Long bpId) 
	{
		this.bpId = bpId;
	}

	public Long getBpId() 
	{
		return bpId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getBpName() {
		return bpName;
	}

	public void setBpName(String bpName) {
		this.bpName = bpName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getUnionpayMerNo() {
		return unionpayMerNo;
	}

	public void setUnionpayMerNo(String unionpayMerNo) {
		this.unionpayMerNo = unionpayMerNo;
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

	public String getLastUpdateTimeStart() {
		return lastUpdateTimeStart;
	}

	public void setLastUpdateTimeStart(String lastUpdateTimeStart) {
		this.lastUpdateTimeStart = lastUpdateTimeStart;
	}

	public String getLastUpdateTimeEnd() {
		return lastUpdateTimeEnd;
	}

	public void setLastUpdateTimeEnd(String lastUpdateTimeEnd) {
		this.lastUpdateTimeEnd = lastUpdateTimeEnd;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getLastUpdateTimeStr() {
		return lastUpdateTimeStr;
	}

	public void setLastUpdateTimeStr(String lastUpdateTimeStr) {
		this.lastUpdateTimeStr = lastUpdateTimeStr;
	}

	public String getAcqServiceMerNo() {
		return acqServiceMerNo;
	}

	public void setAcqServiceMerNo(String acqServiceMerNo) {
		this.acqServiceMerNo = acqServiceMerNo;
	}

	public String getMerWxNo() {
		return merWxNo;
	}

	public void setMerWxNo(String merWxNo) {
		this.merWxNo = merWxNo;
	}

	public String getMerRealName() {
		return merRealName;
	}

	public void setMerRealName(String merRealName) {
		this.merRealName = merRealName;
	}

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}

	public String getDealStatusName() {
		return dealStatusName;
	}

	public void setDealStatusName(String dealStatusName) {
		this.dealStatusName = dealStatusName;
	}

	public String getDealOperator() {
		return dealOperator;
	}

	public void setDealOperator(String dealOperator) {
		this.dealOperator = dealOperator;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("merchantNo", getMerchantNo())
            .append("mbpId", getMbpId())
            .append("serviceId", getServiceId())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("lastUpdateTime", getLastUpdateTime())
            .append("msg", getMsg())
            .append("code", getCode())
            .append("operator", getOperator())
            .append("channelCode", getChannelCode())
            .append("bpId", getBpId())
            .toString();
    }
}
