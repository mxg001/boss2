package cn.eeepay.framework.model.exchange;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/10/010.
 * @author  liuks
 * 产品类别实体
 * 对应表 rdmp_product_type
 */
public class ProductType {

    private Long id;//id

    private String typeName;//类别名称

    private String typeCode;//类别代码

    private String typeStatus;//类别状态

    private String orgCode;//机构

    private String declaraType;//报单类型 1 微信报单 2 自主报单

    private String remark;//备注

    private Date createTime;//创建时间

    private String orgName;//机构名称

    private String videoUrl;//视频地址url

    private String courseUrl;//兑换教程url

    private String bankUrl;//兑换入口

    private String hint;//兑换入口跳转提示语


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeStatus() {
        return typeStatus;
    }

    public void setTypeStatus(String typeStatus) {
        this.typeStatus = typeStatus;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getDeclaraType() {
        return declaraType;
    }

    public void setDeclaraType(String declaraType) {
        this.declaraType = declaraType;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCourseUrl() {
        return courseUrl;
    }

    public void setCourseUrl(String courseUrl) {
        this.courseUrl = courseUrl;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getBankUrl() {
        return bankUrl;
    }

    public void setBankUrl(String bankUrl) {
        this.bankUrl = bankUrl;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
