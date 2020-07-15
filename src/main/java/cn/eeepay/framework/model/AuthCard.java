package cn.eeepay.framework.model;

import java.util.Date;

public class AuthCard {
    private Integer id;

    private String cardNo;


    private String merchantNo;
    
    private String merchantName;

    private String mobileNo;
    
    private String mobile;
    private String idCard;
  
    private String status;
    
    private Date createTime;
    
    private String userName;
    
    
    private Date sdate;
    private Date edate;


	public Date getSdate() {
		return sdate;
	}


	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}


	public Date getEdate() {
		return edate;
	}


	public void setEdate(Date edate) {
		this.edate = edate;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getCardNo() {
		return cardNo;
	}


	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}



	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getMerchantNo() {
		return merchantNo;
	}


	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}


	public String getMobileNo() {
		return mobileNo;
	}


	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getIdCard() {
		return idCard;
	}


	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}




	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getMerchantName() {
		return merchantName;
	}


	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
    
    
    
    
}