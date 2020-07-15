package cn.eeepay.framework.model.exchangeActivate;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/11/011.
 * @author  liuks
 * 超级兑产品表
 * 对应表 yfb_product_info
 */
public class ExchangeActivateProduct {

    private Long id;//id

    private String productName;//产品名称

    private String status;//产品状态 0 无效, 1 有效

    private String typeCode;//产品类别

    private String typeName;//产品类别名称

    private BigDecimal excPoint;//兑换积分

    private BigDecimal excPrice;//兑换价格

    private Integer excNum;//兑换次数 -1 表示无限制

    private Integer settleDay;//结算周期/天

    private String remark;//备注

    private Date createTime;//创建时间

    private Integer minDay;//最短有效天数/天

    private String productShorthand;//名称简称

    private String orgName;//机构名称

    private String orgCode;//机构编码

    private BigDecimal originalPrice;//券面价格

    private String underlineWriteoff;//是否走线下核销渠道0否,1是

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public BigDecimal getExcPoint() {
        return excPoint;
    }

    public void setExcPoint(BigDecimal excPoint) {
        this.excPoint = excPoint;
    }

    public BigDecimal getExcPrice() {
        return excPrice;
    }

    public void setExcPrice(BigDecimal excPrice) {
        this.excPrice = excPrice;
    }

    public Integer getExcNum() {
        return excNum;
    }

    public void setExcNum(Integer excNum) {
        this.excNum = excNum;
    }

    public Integer getSettleDay() {
        return settleDay;
    }

    public void setSettleDay(Integer settleDay) {
        this.settleDay = settleDay;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getMinDay() {
        return minDay;
    }

    public void setMinDay(Integer minDay) {
        this.minDay = minDay;
    }

    public String getProductShorthand() {
        return productShorthand;
    }

    public void setProductShorthand(String productShorthand) {
        this.productShorthand = productShorthand;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getUnderlineWriteoff() {
        return underlineWriteoff;
    }

    public void setUnderlineWriteoff(String underlineWriteoff) {
        this.underlineWriteoff = underlineWriteoff;
    }
}
