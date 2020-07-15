package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * superbank.redTerritory_order
 */
public class ManorAdjustRecore {
	private Long id;
	
	private Date createTime;
	
	private String createTimeStart;
	private String createTimeEnd;
	
	private Long territoryId;
	
	private String provinceName;
	
	private String oderByCol;
	private String type ;
	
	
	
	public String getOderByCol() {
		return oderByCol;
	}
	public void setOderByCol(String oderByCol) {
		this.oderByCol = oderByCol;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	private String cityName;
	private String regionName;

	private Long userId;
	private Long lordsOrgId;
	private String lordsUserCode;
	private String userName;
	private String nickName;
	private String phone;
	private String orgName;
	
	private Date payDate;
	private BigDecimal payPrice;
	
	private String isTrade;
	
	private BigDecimal oldTradePrice;
	private BigDecimal newTradePrice;
	
	public Long getTerritoryId() {
		return territoryId;
	}
	public void setTerritoryId(Long territoryId) {
		this.territoryId = territoryId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getLordsOrgId() {
		return lordsOrgId;
	}
	public void setLordsOrgId(Long lordsOrgId) {
		this.lordsOrgId = lordsOrgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getLordsUserCode() {
		return lordsUserCode;
	}
	public void setLordsUserCode(String lordsUserCode) {
		this.lordsUserCode = lordsUserCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	public BigDecimal getPayPrice() {
		return payPrice;
	}
	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}
	public String getIsTrade() {
		return isTrade;
	}
	public void setIsTrade(String isTrade) {
		this.isTrade = isTrade;
	}
	public BigDecimal getOldTradePrice() {
		return oldTradePrice;
	}
	public void setOldTradePrice(BigDecimal oldTradePrice) {
		this.oldTradePrice = oldTradePrice;
	}
	public BigDecimal getNewTradePrice() {
		return newTradePrice;
	}
	public void setNewTradePrice(BigDecimal newTradePrice) {
		this.newTradePrice = newTradePrice;
	}
}


