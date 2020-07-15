package cn.eeepay.framework.model.func;

import java.util.Date;

/**
 * @author liuks
 * 贷款理财配置
 * 对应表 loan_product_config
 */
public class LoanFinancing {

    private Integer id;//产品编号
    private String productName;//产品名称
    private Integer sortNo;//排序
    private Integer status;//上下架状态,0:下架,1:上架
    private Integer clickTimes;//点击次数
    private String productLinkUrl;//产品链接
    private String logImg;//产品log图片

    private Date createTime;//创建时间
    private Date createTimeBegin;
    private Date createTimeEnd;

    private String operator;//操作人
    private Date lastUpdateTime;//数据最后更新时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getClickTimes() {
        return clickTimes;
    }

    public void setClickTimes(Integer clickTimes) {
        this.clickTimes = clickTimes;
    }

    public String getProductLinkUrl() {
        return productLinkUrl;
    }

    public void setProductLinkUrl(String productLinkUrl) {
        this.productLinkUrl = productLinkUrl;
    }

    public String getLogImg() {
        return logImg;
    }

    public void setLogImg(String logImg) {
        this.logImg = logImg;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
