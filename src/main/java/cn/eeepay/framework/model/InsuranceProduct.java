package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class InsuranceProduct {
    private Integer productId;//保险产品ID

    private String upperProductId;//上游保险产品ID

    private String productName;//保险产品名称

    private Integer companyNo;//保险公司ID

    private String companyName;//保险公司名称

    private String companyNickName;//保险公司别称

    private Integer productType;//产品类型,1:健康险,2:人寿险,3:车险,4:财产险

    private String productTypeStr;//产品类型,1:健康险,2:人寿险,3:车险,4:财产险

    private Integer recommendStatus;//是否推荐,0:否,1:是

    private String recommendStatusStr;

    private Integer showOrder;//顺序

    private BigDecimal productPrice;//产品价格

    private Integer bonusType;//奖金方式,1:固定奖金,2:按比例发放

    private String bonusTypeStr;//奖金方式,1:固定奖金,2:按比例发放

    private Integer bonusSettleTime;//奖金结算时间,1:实时,2:周结,2:月结

    private String bonusSettleTimeStr;//奖金结算时间,1:实时,2:周结,2:月结

    private  Integer status;//状态,0:关闭,1:开启

    private  String statusStr;//状态,0:关闭,1:开启

    private String title1;//副标题1

    private String title2;//副标题2

    private String title3;//副标题3

    private String h5Link;//产品详情h5链接

    private String productImage;//产品图片

    private String productImageUrl;//产品图片

    private String createBy;//创建人

    private String updateBy;//修改人

    private Date createDate;//创建时间

    private Date updateDate;//修改时间

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public Integer getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(Integer companyNo) {
        this.companyNo = companyNo;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Integer getRecommendStatus() {
        return recommendStatus;
    }

    public void setRecommendStatus(Integer recommendStatus) {
        this.recommendStatus = recommendStatus;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getBonusType() {
        return bonusType;
    }

    public String getUpperProductId() {
        return upperProductId;
    }

    public void setUpperProductId(String upperProductId) {
        this.upperProductId = upperProductId;
    }

    public void setBonusType(Integer bonusType) {
        this.bonusType = bonusType;
    }

    public Integer getBonusSettleTime() {
        return bonusSettleTime;
    }

    public void setBonusSettleTime(Integer bonusSettleTime) {
        this.bonusSettleTime = bonusSettleTime;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1 == null ? null : title1.trim();
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2 == null ? null : title2.trim();
    }

    public String getTitle3() {
        return title3;
    }

    public void setTitle3(String title3) {
        this.title3 = title3 == null ? null : title3.trim();
    }

    public String getH5Link() {
        return h5Link;
    }

    public void setH5Link(String h5Link) {
        this.h5Link = h5Link == null ? null : h5Link.trim();
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage == null ? null : productImage.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNickName() {
        return companyNickName;
    }

    public void setCompanyNickName(String companyNickName) {
        this.companyNickName = companyNickName;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getProductTypeStr() {
        return productTypeStr;
    }

    public void setProductTypeStr(String productTypeStr) {
        this.productTypeStr = productTypeStr;
    }

    public String getBonusTypeStr() {
        return bonusTypeStr;
    }

    public void setBonusTypeStr(String bonusTypeStr) {
        this.bonusTypeStr = bonusTypeStr;
    }

    public String getBonusSettleTimeStr() {
        return bonusSettleTimeStr;
    }

    public void setBonusSettleTimeStr(String bonusSettleTimeStr) {
        this.bonusSettleTimeStr = bonusSettleTimeStr;
    }

    public String getRecommendStatusStr() {
        return recommendStatusStr;
    }

    public void setRecommendStatusStr(String recommendStatusStr) {
        this.recommendStatusStr = recommendStatusStr;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }
}