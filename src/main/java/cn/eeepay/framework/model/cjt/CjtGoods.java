package cn.eeepay.framework.model.cjt;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 超级推商品表 cjt_goods
 * 
 * @author tans
 * @date 2019-05-28
 */
public class CjtGoods
{
	private static final long serialVersionUID = 1L;

	public static final String cjx_goods_code = "cjx_goods_code";
	
	/** id */
	private Integer id;
	/** 商品编号 */
	private String goodsCode;
	/** 状态 0：下架，1：上架 */
	private String status;
	/** 商品名称 */
	private String goodsName;
	/** 商品描述 */
	private String goodsDesc;
	/** 商品主图,多张以英文逗号分开 */
	private String mainImg;
	/** 商品详情图,多张以英文逗号分开 */
	private String descImg;
	/** 单价 */
	private BigDecimal price;
	/** 尺寸 */
	private String size;
	/** 颜色，多种以英文逗号分开 */
	private String color;
	/** 起购量 */
	private Integer minNum;
	/** 硬件产品ID,对应hardware_product表 */
	private Integer hpId;
	/** 创建时间 */
	private Date createTime;
	/** 创建人 */
	private String creater;
	/** 最后更新时间 */
	private Date lastUpdateTime;
	/** 申购类型 1付费购买  2 免费申领 */
	private Integer goodOrderType;

	private String whiteStatus;//白名单状态,0:否,1:是

	private String createTimeStr;
	private String createTimeStart;
	private String createTimeEnd;
	private String lastUpdateTimeStr;
	private String mainImgUrl1;
	private String mainImgUrl2;
	private String mainImgUrl3;
	private String mainImgName1;
	private String mainImgName2;
	private String mainImgName3;
	private String descImgUrl;
	private String typeName;//硬件产品类型名称


	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setGoodsCode(String goodsCode) 
	{
		this.goodsCode = goodsCode;
	}

	public String getGoodsCode() 
	{
		return goodsCode;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}
	public void setGoodsName(String goodsName) 
	{
		this.goodsName = goodsName;
	}

	public String getGoodsName() 
	{
		return goodsName;
	}
	public void setGoodsDesc(String goodsDesc) 
	{
		this.goodsDesc = goodsDesc;
	}

	public String getGoodsDesc() 
	{
		return goodsDesc;
	}
	public void setMainImg(String mainImg) 
	{
		this.mainImg = mainImg;
	}

	public String getMainImg() 
	{
		return mainImg;
	}
	public void setDescImg(String descImg) 
	{
		this.descImg = descImg;
	}

	public String getDescImg() 
	{
		return descImg;
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
	public void setMinNum(Integer minNum) 
	{
		this.minNum = minNum;
	}

	public Integer getMinNum() 
	{
		return minNum;
	}
	public void setHpId(Integer hpId) 
	{
		this.hpId = hpId;
	}

	public Integer getHpId() 
	{
		return hpId;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}
	public void setCreater(String creater) 
	{
		this.creater = creater;
	}

	public String getCreater() 
	{
		return creater;
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

	public String getLastUpdateTimeStr() {
		return lastUpdateTimeStr;
	}

	public void setLastUpdateTimeStr(String lastUpdateTimeStr) {
		this.lastUpdateTimeStr = lastUpdateTimeStr;
	}

	public String getWhiteStatus() {
		return whiteStatus;
	}

	public void setWhiteStatus(String whiteStatus) {
		this.whiteStatus = whiteStatus;
	}

	public String getDescImgUrl() {
		return descImgUrl;
	}

	public void setDescImgUrl(String descImgUrl) {
		this.descImgUrl = descImgUrl;
	}

	public String getMainImgUrl1() {
		return mainImgUrl1;
	}

	public void setMainImgUrl1(String mainImgUrl1) {
		this.mainImgUrl1 = mainImgUrl1;
	}

	public String getMainImgUrl2() {
		return mainImgUrl2;
	}

	public void setMainImgUrl2(String mainImgUrl2) {
		this.mainImgUrl2 = mainImgUrl2;
	}

	public String getMainImgUrl3() {
		return mainImgUrl3;
	}

	public void setMainImgUrl3(String mainImgUrl3) {
		this.mainImgUrl3 = mainImgUrl3;
	}

	public String getMainImgName1() {
		return mainImgName1;
	}

	public void setMainImgName1(String mainImgName1) {
		this.mainImgName1 = mainImgName1;
	}

	public String getMainImgName2() {
		return mainImgName2;
	}

	public void setMainImgName2(String mainImgName2) {
		this.mainImgName2 = mainImgName2;
	}

	public String getMainImgName3() {
		return mainImgName3;
	}

	public void setMainImgName3(String mainImgName3) {
		this.mainImgName3 = mainImgName3;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getGoodOrderType() {
		return goodOrderType;
	}

	public void setGoodOrderType(Integer goodOrderType) {
		this.goodOrderType = goodOrderType;
	}
}
