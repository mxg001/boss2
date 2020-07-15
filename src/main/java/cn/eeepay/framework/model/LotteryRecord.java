package cn.eeepay.framework.model;

/**
 * 彩票信息表
 * @author dxy
 *
 */
public class LotteryRecord {

	private String id;              //id
	
	private String ieNo;	       //  投注设备号
	 
	private String ieSerialNo;     //投注设备流水号
	
	private String userId;         //用户ID
	 
	private String userNo;         //用户编号
	
	private String merchantNo;      //商户编号
	
	private String merchantName;    //商户名称
	
	private String orderNo;        //订单号
	
	private String lotteryType;    //彩种
	
	private String lotteryPeriods;  //投注期号
	
    private String lotteryDate;    //投注时间 
    
    private String prizeDate;      //兑奖时间
    
    private String buyStatus;      //购买状态
    
    private String prizeMark;     //大奖标志
    
    private String cashPrizeMark; //兑奖标志
    
    private String castEBean;     //消费e豆
    
    private String totalPrize;    //中奖总金额

    

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIeNo() {
		return ieNo;
	}

	public void setIeNo(String ieNo) {
		this.ieNo = ieNo;
	}

	public String getIeSerialNo() {
		return ieSerialNo;
	}

	public void setIeSerialNo(String ieSerialNo) {
		this.ieSerialNo = ieSerialNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}

	public String getLotteryPeriods() {
		return lotteryPeriods;
	}

	public void setLotteryPeriods(String lotteryPeriods) {
		this.lotteryPeriods = lotteryPeriods;
	}

	public String getLotteryDate() {
		return lotteryDate;
	}

	public void setLotteryDate(String lotteryDate) {
		this.lotteryDate = lotteryDate;
	}

	public String getPrizeDate() {
		return prizeDate;
	}

	public void setPrizeDate(String prizeDate) {
		this.prizeDate = prizeDate;
	}

	public String getBuyStatus() {
		return buyStatus;
	}

	public void setBuyStatus(String buyStatus) {
		this.buyStatus = buyStatus;
	}

	public String getPrizeMark() {
		return prizeMark;
	}

	public void setPrizeMark(String prizeMark) {
		this.prizeMark = prizeMark;
	}

	public String getCashPrizeMark() {
		return cashPrizeMark;
	}

	public void setCashPrizeMark(String cashPrizeMark) {
		this.cashPrizeMark = cashPrizeMark;
	}

	public String getCastEBean() {
		return castEBean;
	}

	public void setCastEBean(String castEBean) {
		this.castEBean = castEBean;
	}

	public String getTotalPrize() {
		return totalPrize;
	}

	public void setTotalPrize(String totalPrize) {
		this.totalPrize = totalPrize;
	}
    
}
