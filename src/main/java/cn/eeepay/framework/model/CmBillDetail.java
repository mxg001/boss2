package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * cm_bill_detail
 * @author	mays
 * @date	2018年4月9日
 */
public class CmBillDetail {

	private Integer id;
	private Date transDate;
	private Date tallyDate;
	private String transDesc;
	private String transCurrency;
	private String tallyCurrency;
	private BigDecimal transAmt;
	private BigDecimal tallyAmt;
	private String transType;
	private String transMerName;
	private Date createTime;
	private Integer refBillId;
	private String transArea;

	private String userNo;
	private String cardNo;
	private String sTransDate;
	private String eTransDate;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getTransDate() {
		return transDate;
	}
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public String getTransDesc() {
		return transDesc;
	}
	public void setTransDesc(String transDesc) {
		this.transDesc = transDesc;
	}
	public String getTransCurrency() {
		return transCurrency;
	}
	public void setTransCurrency(String transCurrency) {
		this.transCurrency = transCurrency;
	}
	public String getTallyCurrency() {
		return tallyCurrency;
	}
	public void setTallyCurrency(String tallyCurrency) {
		this.tallyCurrency = tallyCurrency;
	}
	public BigDecimal getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(BigDecimal transAmt) {
		this.transAmt = transAmt;
	}
	public BigDecimal getTallyAmt() {
		return tallyAmt;
	}
	public void setTallyAmt(BigDecimal tallyAmt) {
		this.tallyAmt = tallyAmt;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getTransMerName() {
		return transMerName;
	}
	public void setTransMerName(String transMerName) {
		this.transMerName = transMerName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getRefBillId() {
		return refBillId;
	}
	public void setRefBillId(Integer refBillId) {
		this.refBillId = refBillId;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getsTransDate() {
		return sTransDate;
	}
	public void setsTransDate(String sTransDate) {
		this.sTransDate = sTransDate;
	}
	public String geteTransDate() {
		return eTransDate;
	}
	public void seteTransDate(String eTransDate) {
		this.eTransDate = eTransDate;
	}

	public String getTransArea() {
		return transArea;
	}

	public void setTransArea(String transArea) {
		this.transArea = transArea;
	}
}
