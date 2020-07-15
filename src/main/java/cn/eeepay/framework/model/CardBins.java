package cn.eeepay.framework.model;

import java.util.Date;

/**
 * Created by Administrator on 2018/3/23/023.
 * @author  liuks
 * 卡bin 实体 对应表 card_bins
 */
public class CardBins {

    private Integer id;//id

    private String cardNo;//卡bin

    private Integer businessType;

    private Integer state;//状态:0关闭,1打开

    private Integer cardType;//卡种:0其他,1贷记卡,2借记卡

    private String cardBank;//发卡银行

    private Integer currency;//交易币种:取数据字典值

    private String cardStyle;//卡类型

    private String remarks;//备注

    private Date createTime;//创建时间
    private Date createTimeBegin;
    private Date createTimeEnd;

    private Integer createId;//创建人id

    private String createName;//创建人名称

    private Integer cardNum;
    private Integer cardDigit;

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public String getCardBank() {
        return cardBank;
    }

    public void setCardBank(String cardBank) {
        this.cardBank = cardBank;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public String getCardStyle() {
        return cardStyle;
    }

    public void setCardStyle(String cardStyle) {
        this.cardStyle = cardStyle;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Integer getCreateId() {
        return createId;
    }

    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Integer getCardNum() {
        return cardNum;
    }

    public void setCardNum(Integer cardNum) {
        this.cardNum = cardNum;
    }

    public Integer getCardDigit() {
        return cardDigit;
    }

    public void setCardDigit(Integer cardDigit) {
        this.cardDigit = cardDigit;
    }
}
