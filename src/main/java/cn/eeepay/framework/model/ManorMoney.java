package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * superbank.red_territory_lords_profit
 */
public class ManorMoney {
	private Long id;

	private Long territoryId;  //领地id
	private Long lordsOrgId;   //所属组织id
	private Long userId;   //领主id
	
	private String lordsUserCode;//领主用户编码
	private String userName;//领主用户姓名
	private String nickName;//领主用户姓名
	private String phone;//领主用户姓名
	private String orgName;//领主用户姓名

	private Date createDate;//转入时间
	private Date createTime;
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	private String createDateStart;//转入开始
	private String createDateEnd;//转入结束
	
	private BigDecimal totalProfit; //领地累计收益
	private BigDecimal redAccountProfit;//已转入红包账户领地收益
	private BigDecimal transAmount;//已转入红包账户领地收益

	private String provinceName;
	private String cityName;
	private String regionName;
	
	
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
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTerritoryId() {
		return territoryId;
	}
	public void setTerritoryId(Long territoryId) {
		this.territoryId = territoryId;
	}
	public Long getLordsOrgId() {
		return lordsOrgId;
	}
	public void setLordsOrgId(Long lordsOrgId) {
		this.lordsOrgId = lordsOrgId;
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
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreateDateStart() {
		return createDateStart;
	}
	public void setCreateDateStart(String createDateStart) {
		this.createDateStart = createDateStart;
	}
	public String getCreateDateEnd() {
		return createDateEnd;
	}
	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}
	public BigDecimal getTotalProfit() {
		return totalProfit;
	}
	public void setTotalProfit(BigDecimal totalProfit) {
		this.totalProfit = totalProfit;
	}
	public BigDecimal getRedAccountProfit() {
		return redAccountProfit;
	}
	public void setRedAccountProfit(BigDecimal redAccountProfit) {
		this.redAccountProfit = redAccountProfit;
	}
	public BigDecimal getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}
	
	
	
}