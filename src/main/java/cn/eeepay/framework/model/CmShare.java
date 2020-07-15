package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class CmShare {

	private Integer id;
	private Date createDate;
	private BigDecimal shareCash;
	private Integer sharePercentage;
	private Integer enterStatus;
	private String shareAgentName;
	private String shareAgentNo;
	private String relatedOrderNo;
	private BigDecimal orderCash;
	private Integer orderType;
	private String userId;
	private String belongAgentName;
	private String belongAgentNo;
	private Date enterDate;
	private String agentNode;

	private String sCreateDate;
	private String eCreateDate;
	private String contain;//查询条件,是否包含下级,1-是,0-否

	public String getContain() {
		return contain;
	}
	public void setContain(String contain) {
		this.contain = contain;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public BigDecimal getShareCash() {
		return shareCash;
	}
	public void setShareCash(BigDecimal shareCash) {
		this.shareCash = shareCash;
	}
	public Integer getSharePercentage() {
		return sharePercentage;
	}
	public void setSharePercentage(Integer sharePercentage) {
		this.sharePercentage = sharePercentage;
	}
	public Integer getEnterStatus() {
		return enterStatus;
	}
	public void setEnterStatus(Integer enterStatus) {
		this.enterStatus = enterStatus;
	}
	public String getShareAgentName() {
		return shareAgentName;
	}
	public void setShareAgentName(String shareAgentName) {
		this.shareAgentName = shareAgentName;
	}
	public String getShareAgentNo() {
		return shareAgentNo;
	}
	public void setShareAgentNo(String shareAgentNo) {
		this.shareAgentNo = shareAgentNo;
	}
	public String getRelatedOrderNo() {
		return relatedOrderNo;
	}
	public void setRelatedOrderNo(String relatedOrderNo) {
		this.relatedOrderNo = relatedOrderNo;
	}
	public BigDecimal getOrderCash() {
		return orderCash;
	}
	public void setOrderCash(BigDecimal orderCash) {
		this.orderCash = orderCash;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBelongAgentName() {
		return belongAgentName;
	}
	public void setBelongAgentName(String belongAgentName) {
		this.belongAgentName = belongAgentName;
	}
	public String getBelongAgentNo() {
		return belongAgentNo;
	}
	public void setBelongAgentNo(String belongAgentNo) {
		this.belongAgentNo = belongAgentNo;
	}
	public Date getEnterDate() {
		return enterDate;
	}
	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}
	public String getAgentNode() {
		return agentNode;
	}
	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}
	public String getsCreateDate() {
		return sCreateDate;
	}
	public void setsCreateDate(String sCreateDate) {
		this.sCreateDate = sCreateDate;
	}
	public String geteCreateDate() {
		return eCreateDate;
	}
	public void seteCreateDate(String eCreateDate) {
		this.eCreateDate = eCreateDate;
	}

}
