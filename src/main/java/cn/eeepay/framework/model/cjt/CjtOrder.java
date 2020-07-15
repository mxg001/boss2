package cn.eeepay.framework.model.cjt;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商户购买订单表 cjt_order
 * 
 * @author tans
 * @date 2019-05-30
 */
public class CjtOrder
{

	public static final String Order_Status_Not_Paid = "0";
	public static final String Order_Status_Not_Shipped = "1";
	public static final String Order_Status_Shipped = "2";
	public static final String Order_Status_Closed = "4";

	public static Map<String, String> orderStatusMap = new HashMap<>();//订单状态

	public static Map<String, String> transStatusMap = new HashMap<>();//支付状态

	static{
		//订单状态 0-待付款  1-待发货  2-已发货  4-已关闭
		orderStatusMap.put(null, "");
		orderStatusMap.put("0", "待付款");
		orderStatusMap.put("1", "待发货");
		orderStatusMap.put("2", "已发货");
		orderStatusMap.put("4", "已关闭");

		//支付订单状态 0-未支付，1-已提交，2-支付成功，3-支付失败
		transStatusMap.put(null, "");
		transStatusMap.put("0", "未支付");
		transStatusMap.put("1", "已提交");
		transStatusMap.put("2", "支付成功");
		transStatusMap.put("3", "支付失败");
	}

	/**  */
	private Integer id;
	/** 订单编号 */
	private String orderNo;
	/** 购买者商户编号 */
	private String merchantNo;
	/** 购买者商户超级推节点 */
	private String merchantNode;
	/** 订单状态 0-待付款  1-待发货  2-已发货  4-已关闭 */
	private String orderStatus;
	/** 商品编号 */
	private String goodsCode;
	/** 硬件产品ID,对应hardware_product表 */
	private String hpId;
	/** 商品数量 */
	private Integer num;
	/** 单价 */
	private BigDecimal price;
	/** 购买的尺寸 */
	private String size;
	/** 购买的颜色 */
	private String color;
	/** 订单总金额 */
	private BigDecimal totalPrice;
	/** 备注 */
	private String remark;
	/** 收货人 */
	private String receiver;
	/** 收货人联系方式 */
	private String receiverPhone;
	/** 收货地址 */
	private String receiveAddress;
	/** 物流公司 */
	private String logisticsCompany;
	/** 物流单号 */
	private String logisticsOrderNo;
	/** 物流时间 */
	private Date logisticsTime;
	/** 发货机具sn号起始号 */
	private String snBegin;
	/** 发货机具sn号结束号 */
	private String snEnd;
	/** 发货人 */
	private String logisticsOperator;
	/**  */
	private Date createTime;
	/** 数据最后更新时间 */
	private Date lastUpdateTime;
	/** 申购类型 1付费购买  2 免费申领 */
	private Integer goodOrderType;


	private String createTimeStart;
	private String createTimeEnd;
	private String createTimeStr;
	private Date transTime;//支付时间
	private String transTimeStart;
	private String transTimeEnd;
	private String transTimeStr;
	private String logisticsTimeStart;
	private String logisticsTimeEnd;
	private String logisticsTimeStr;
	private String merchantName;
	private String transStatus;//支付状态
	private String transType;//支付方式
	private String rechargeStatus;
	private String status;//订单售后状态
	private String  acqCode;//支付通道
	private String transOrderNo;//支付订单号
	private String mainImgUrl1;//商品主图1
	private String goodsName;//商品名称
	private String orderStatusStr;
	private String statusStr;
	private String transStatusStr;
	private String sn;
	private List<String> snList;

	private String payOrderNo;//支付订单号
	private String acqOrderNo;//渠道订单号(微信支付宝官方订单号或交易主表订单号)

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
	public void setMerchantNode(String merchantNode) 
	{
		this.merchantNode = merchantNode;
	}

	public String getMerchantNode() 
	{
		return merchantNode;
	}
	public void setOrderStatus(String orderStatus) 
	{
		this.orderStatus = orderStatus;
	}

	public String getOrderStatus() 
	{
		return orderStatus;
	}
	public void setGoodsCode(String goodsCode) 
	{
		this.goodsCode = goodsCode;
	}

	public String getGoodsCode() 
	{
		return goodsCode;
	}
	public void setHpId(String hpId) 
	{
		this.hpId = hpId;
	}

	public String getHpId() 
	{
		return hpId;
	}
	public void setNum(Integer num) 
	{
		this.num = num;
	}

	public Integer getNum() 
	{
		return num;
	}
	public void setPrice(BigDecimal price) 
	{
		this.price = price;
	}

	public BigDecimal getPrice() 
	{
		return price;
	}
	public void setSize(String size) 
	{
		this.size = size;
	}

	public String getSize() 
	{
		return size;
	}
	public void setColor(String color) 
	{
		this.color = color;
	}

	public String getColor() 
	{
		return color;
	}
	public void setTotalPrice(BigDecimal totalPrice) 
	{
		this.totalPrice = totalPrice;
	}

	public BigDecimal getTotalPrice() 
	{
		return totalPrice;
	}
	public void setRemark(String remark) 
	{
		this.remark = remark;
	}

	public String getRemark() 
	{
		return remark;
	}
	public void setReceiver(String receiver) 
	{
		this.receiver = receiver;
	}

	public String getReceiver() 
	{
		return receiver;
	}
	public void setReceiverPhone(String receiverPhone) 
	{
		this.receiverPhone = receiverPhone;
	}

	public String getReceiverPhone() 
	{
		return receiverPhone;
	}
	public void setReceiveAddress(String receiveAddress) 
	{
		this.receiveAddress = receiveAddress;
	}

	public String getReceiveAddress() 
	{
		return receiveAddress;
	}
	public void setLogisticsCompany(String logisticsCompany) 
	{
		this.logisticsCompany = logisticsCompany;
	}

	public String getLogisticsCompany() 
	{
		return logisticsCompany;
	}
	public void setLogisticsOrderNo(String logisticsOrderNo) 
	{
		this.logisticsOrderNo = logisticsOrderNo;
	}

	public String getLogisticsOrderNo() 
	{
		return logisticsOrderNo;
	}
	public void setLogisticsTime(Date logisticsTime) 
	{
		this.logisticsTime = logisticsTime;
	}

	public Date getLogisticsTime() 
	{
		return logisticsTime;
	}
	public void setSnBegin(String snBegin)
	{
		this.snBegin = snBegin;
	}

	public String getSnBegin()
	{
		return snBegin;
	}
	public void setSnEnd(String snEnd)
	{
		this.snEnd = snEnd;
	}

	public String getSnEnd()
	{
		return snEnd;
	}
	public void setLogisticsOperator(String logisticsOperator) 
	{
		this.logisticsOperator = logisticsOperator;
	}

	public String getLogisticsOperator() 
	{
		return logisticsOperator;
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

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getTransTimeStart() {
		return transTimeStart;
	}

	public void setTransTimeStart(String transTimeStart) {
		this.transTimeStart = transTimeStart;
	}

	public String getTransTimeEnd() {
		return transTimeEnd;
	}

	public void setTransTimeEnd(String transTimeEnd) {
		this.transTimeEnd = transTimeEnd;
	}

	public String getTransTimeStr() {
		return transTimeStr;
	}

	public void setTransTimeStr(String transTimeStr) {
		this.transTimeStr = transTimeStr;
	}

	public String getLogisticsTimeStart() {
		return logisticsTimeStart;
	}

	public void setLogisticsTimeStart(String logisticsTimeStart) {
		this.logisticsTimeStart = logisticsTimeStart;
	}

	public String getLogisticsTimeEnd() {
		return logisticsTimeEnd;
	}

	public void setLogisticsTimeEnd(String logisticsTimeEnd) {
		this.logisticsTimeEnd = logisticsTimeEnd;
	}

	public String getLogisticsTimeStr() {
		return logisticsTimeStr;
	}

	public void setLogisticsTimeStr(String logisticsTimeStr) {
		this.logisticsTimeStr = logisticsTimeStr;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getRechargeStatus() {
		return rechargeStatus;
	}

	public void setRechargeStatus(String rechargeStatus) {
		this.rechargeStatus = rechargeStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAcqCode() {
		return acqCode;
	}

	public void setAcqCode(String acqCode) {
		this.acqCode = acqCode;
	}

	public String getTransOrderNo() {
		return transOrderNo;
	}

	public void setTransOrderNo(String transOrderNo) {
		this.transOrderNo = transOrderNo;
	}

	public String getMainImgUrl1() {
		return mainImgUrl1;
	}

	public void setMainImgUrl1(String mainImgUrl1) {
		this.mainImgUrl1 = mainImgUrl1;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Date getTransTime() {
		return transTime;
	}

	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}

	public String getOrderStatusStr() {
		return orderStatusStr;
	}

	public void setOrderStatusStr(String orderStatusStr) {
		this.orderStatusStr = orderStatusStr;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getTransStatusStr() {
		return transStatusStr;
	}

	public void setTransStatusStr(String transStatusStr) {
		this.transStatusStr = transStatusStr;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public List<String> getSnList() {
		return snList;
	}

	public void setSnList(List<String> snList) {
		this.snList = snList;
	}

	public String getPayOrderNo() {
		return payOrderNo;
	}

	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
	}

	public String getAcqOrderNo() {
		return acqOrderNo;
	}

	public void setAcqOrderNo(String acqOrderNo) {
		this.acqOrderNo = acqOrderNo;
	}

	public Integer getGoodOrderType() {
		return goodOrderType;
	}

	public void setGoodOrderType(Integer goodOrderType) {
		this.goodOrderType = goodOrderType;
	}
}
