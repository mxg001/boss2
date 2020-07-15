package cn.eeepay.framework.model.cjt;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 售后订单表 cjt_after_sale
 * 
 * @author tans
 * @date 2019-06-06
 */
public class CjtAfterSale
{

	public static final String AFTER_SALE_TYPE = "AFTER_SALE_TYPE";
	public static Map<String, String> statusMap = new HashMap<>();//售后状态
	public static final String Status_No_Deal = "0";
	public static final String Status_Dealing = "1";
	public static final String Status_Success = "2";
	public static final String Status_Closed = "3";

	static {
		//售后申请订单状态 0-待平台处理1-售后中 2-已处理 3-已取消'
		statusMap.put(null, "");
		statusMap.put("0", "待平台处理");
		statusMap.put("1", "售后中");
		statusMap.put("2", "已处理");
		statusMap.put("3", "已取消");
	}

	/**  */
	private Integer id;
	/** 售后订单编号 */
	private String orderNo;
	/** 售后订单申请人商户编号 */
	private String merchantNo;
	/** 售后订单类型，对应数据字典AFTER_SALE_TYPE */
	private String afterSaleType;
	/** 售后申请说明 */
	private String applyRemark;
	/** 售后申请图片，多张以英文逗号分开 */
	private String applyImg;
	/** 售后申请订单状态 0-待平台处理1-处理中 2-已处理 3-已取消 */
	private String status;
	/** 业务订单号（即cjt_order的order_no） */
	private String serviceOrderNo;
	/** 处理结果说明 */
	private String dealRemark;
	/** 处理结果图片，多张以英文逗号分开 */
	private String dealImg;
	/** 最后处理人 */
	private String dealPerson;
	/** 创建时间 */
	private Date createTime;
	/** 最后处理时间 */
	private Date dealTime;
	/** 数据最后更新时间 */
	private Date lastUpdateTime;

	private String createTimeStr;
	private String createTimeStart;
	private String createTimeEnd;
	private String dealTimeStr;
	private String dealTimeStart;
	private String dealTimeEnd;
	private String applyImgUrl1;
	private String applyImgUrl2;
	private String applyImgUrl3;
	private String dealImgUrl1;
	private String dealImgUrl2;
	private String dealImgUrl3;
	private String afterSaleTypeStr;
	private String statusStr;
	private String noDealTimeStr;

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setOrderNo(String orderNo) 
	{
		this.orderNo = orderNo;
	}

	public String getOrderNo() 
	{
		return orderNo;
	}
	public void setMerchantNo(String merchantNo) 
	{
		this.merchantNo = merchantNo;
	}

	public String getMerchantNo() 
	{
		return merchantNo;
	}
	public void setAfterSaleType(String afterSaleType) 
	{
		this.afterSaleType = afterSaleType;
	}

	public String getAfterSaleType() 
	{
		return afterSaleType;
	}
	public void setApplyRemark(String applyRemark) 
	{
		this.applyRemark = applyRemark;
	}

	public String getApplyRemark() 
	{
		return applyRemark;
	}
	public void setApplyImg(String applyImg) 
	{
		this.applyImg = applyImg;
	}

	public String getApplyImg() 
	{
		return applyImg;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}
	public void setServiceOrderNo(String serviceOrderNo) 
	{
		this.serviceOrderNo = serviceOrderNo;
	}

	public String getServiceOrderNo() 
	{
		return serviceOrderNo;
	}
	public void setDealRemark(String dealRemark) 
	{
		this.dealRemark = dealRemark;
	}

	public String getDealRemark() 
	{
		return dealRemark;
	}
	public void setDealImg(String dealImg) 
	{
		this.dealImg = dealImg;
	}

	public String getDealImg() 
	{
		return dealImg;
	}
	public void setDealPerson(String dealPerson) 
	{
		this.dealPerson = dealPerson;
	}

	public String getDealPerson() 
	{
		return dealPerson;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}
	public void setDealTime(Date dealTime) 
	{
		this.dealTime = dealTime;
	}

	public Date getDealTime() 
	{
		return dealTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) 
	{
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getLastUpdateTime() 
	{
		return lastUpdateTime;
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

	public String getDealTimeStr() {
		return dealTimeStr;
	}

	public void setDealTimeStr(String dealTimeStr) {
		this.dealTimeStr = dealTimeStr;
	}

	public String getDealTimeStart() {
		return dealTimeStart;
	}

	public void setDealTimeStart(String dealTimeStart) {
		this.dealTimeStart = dealTimeStart;
	}

	public String getDealTimeEnd() {
		return dealTimeEnd;
	}

	public void setDealTimeEnd(String dealTimeEnd) {
		this.dealTimeEnd = dealTimeEnd;
	}

	public String getApplyImgUrl1() {
		return applyImgUrl1;
	}

	public void setApplyImgUrl1(String applyImgUrl1) {
		this.applyImgUrl1 = applyImgUrl1;
	}

	public String getApplyImgUrl2() {
		return applyImgUrl2;
	}

	public void setApplyImgUrl2(String applyImgUrl2) {
		this.applyImgUrl2 = applyImgUrl2;
	}

	public String getApplyImgUrl3() {
		return applyImgUrl3;
	}

	public void setApplyImgUrl3(String applyImgUrl3) {
		this.applyImgUrl3 = applyImgUrl3;
	}

	public String getDealImgUrl1() {
		return dealImgUrl1;
	}

	public void setDealImgUrl1(String dealImgUrl1) {
		this.dealImgUrl1 = dealImgUrl1;
	}

	public String getDealImgUrl2() {
		return dealImgUrl2;
	}

	public void setDealImgUrl2(String dealImgUrl2) {
		this.dealImgUrl2 = dealImgUrl2;
	}

	public String getDealImgUrl3() {
		return dealImgUrl3;
	}

	public void setDealImgUrl3(String dealImgUrl3) {
		this.dealImgUrl3 = dealImgUrl3;
	}

	public String getAfterSaleTypeStr() {
		return afterSaleTypeStr;
	}

	public void setAfterSaleTypeStr(String afterSaleTypeStr) {
		this.afterSaleTypeStr = afterSaleTypeStr;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getNoDealTimeStr() {
		return noDealTimeStr;
	}

	public void setNoDealTimeStr(String noDealTimeStr) {
		this.noDealTimeStr = noDealTimeStr;
	}

}
