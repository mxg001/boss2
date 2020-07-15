package cn.eeepay.framework.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/26/026.
 */
public class UpdatehappyBackSumAmountInfo {
    private String merchantNo;//商户编号
    private BigDecimal total;//前一日汇总金额

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;//开始时间

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;//结束时间


    private Integer startPage;//分页开始
    private Integer length;//分页结束

    private Integer state;//0:分页查询,1:统计总条数

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getStartPage() {
        return startPage;
    }

    public void setStartPage(Integer startPage) {
        this.startPage = startPage;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
