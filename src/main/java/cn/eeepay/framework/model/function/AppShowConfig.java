package cn.eeepay.framework.model.function;

import org.apache.ibatis.annotations.Insert;

import java.util.Date;

/**
 * APP前端功能显示控制
 * 对应表 `app_function_config`
 */
public class AppShowConfig {

    private Integer id;
    private Integer fmcId;//关联 功能开关配置表id
    private String functionCode;//app功能编码
    private String functionName;//app功能名称
    private String functionModular;//app功能模块位置
    private Integer sort;//排序
    private Integer isShow;//是否展示 0否1是
    private Integer isRecommend;//是否推荐 0否1是
    private String recommendIcon;//推荐图标地址
    private Date createTime;//创建时间
    private String operator;//创建人
    private Date lastUpdateTime;//最后更新时间
    private String showName;//下发显示名称

    private String imgUrl;//阿里云路径

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFmcId() {
        return fmcId;
    }

    public void setFmcId(Integer fmcId) {
        this.fmcId = fmcId;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionModular() {
        return functionModular;
    }

    public void setFunctionModular(String functionModular) {
        this.functionModular = functionModular;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Integer isRecommend) {
        this.isRecommend = isRecommend;
    }

    public String getRecommendIcon() {
        return recommendIcon;
    }

    public void setRecommendIcon(String recommendIcon) {
        this.recommendIcon = recommendIcon;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
