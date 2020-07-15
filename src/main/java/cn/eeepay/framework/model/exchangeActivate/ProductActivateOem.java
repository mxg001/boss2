package cn.eeepay.framework.model.exchangeActivate;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/16/016.
 * @author  liuks
 * oem产品
 * 对应表yfb_oem_product_info
 */
public class ProductActivateOem {

    private Long  id;

    private Long pId;//产品id

    private String productName;//产品名称

    private String  oemNo;//oem编号

    private String  oemName;//oem名称

    private String orgName;//机构名称

    private String orgCode;//机构编码

    private String typeCode;//产品类别

    private String typeName;//产品类别名称

    private String shelve;//是否上架 1 上架 2 下架

    private BigDecimal brandPrice;//品牌价格

    private Date createTime;//创建时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOemNo() {
        return oemNo;
    }

    public void setOemNo(String oemNo) {
        this.oemNo = oemNo;
    }

    public String getOemName() {
        return oemName;
    }

    public void setOemName(String oemName) {
        this.oemName = oemName;
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

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getShelve() {
        return shelve;
    }

    public void setShelve(String shelve) {
        this.shelve = shelve;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getBrandPrice() {
        return brandPrice;
    }

    public void setBrandPrice(BigDecimal brandPrice) {
        this.brandPrice = brandPrice;
    }
}
