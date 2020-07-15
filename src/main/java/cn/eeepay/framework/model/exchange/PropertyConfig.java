package cn.eeepay.framework.model.exchange;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/11/011.
 * @author  liuks
 * 属性配置表
 * 对应表 rdmp_property_config
 */
public class PropertyConfig {

    private  long id;//id

    private  String  propertyDesc;//属性描述

    private  String  propertyCode;//属性code

    private  String  propertyType;//属性类型 字段分类

    private  String  configType;//属性配置类型 对应表

    private  String  codeType;//属性显示类型 file  文件  image 图片  text 文本

    private  String  propertyDefaultValue;//属性默认值

    private  String isNeed;//是否必填

    private  String  propertyRemark;//属性备注

    private  Integer  sort;//排序字段

    private  Date  createTime;//创建时间

    private String configCode;//编码

    private String configValue;//属性值

    private String bak;//备用字段

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPropertyDesc() {
        return propertyDesc;
    }

    public void setPropertyDesc(String propertyDesc) {
        this.propertyDesc = propertyDesc;
    }

    public String getPropertyCode() {
        return propertyCode;
    }

    public void setPropertyCode(String propertyCode) {
        this.propertyCode = propertyCode;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getPropertyDefaultValue() {
        return propertyDefaultValue;
    }

    public void setPropertyDefaultValue(String propertyDefaultValue) {
        this.propertyDefaultValue = propertyDefaultValue;
    }

    public String getPropertyRemark() {
        return propertyRemark;
    }

    public void setPropertyRemark(String propertyRemark) {
        this.propertyRemark = propertyRemark;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getBak() {
        return bak;
    }

    public void setBak(String bak) {
        this.bak = bak;
    }

    public String getIsNeed() {
        return isNeed;
    }

    public void setIsNeed(String isNeed) {
        this.isNeed = isNeed;
    }
}
