package cn.eeepay.framework.model.allAgent;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/7/11/011.
 * @author  liuks
 * 用户身份月结分润规则表
 * 对应表 pa_level_share_config
 */
public class AwardParamDiamonds {

    private Integer id;
    private String brandCode;//品牌编码

    private Integer goldNum;//黄金推广人数
    private BigDecimal goldAmount;//黄金团队流水
    private BigDecimal goldAward;//黄金奖励元/人

    private Integer ptNum;//铂金推广人数
    private BigDecimal ptAmount;//铂金团队流水
    private BigDecimal ptAward;//铂金奖励元/人

    private Integer bgoldNum;//黑金推广人数
    private BigDecimal bgoldAmount;//黑金团队流水
    private BigDecimal bgoldAward;//黑金奖励元/人

    private Integer diamoNum;//钻石推广人数
    private BigDecimal diamoAmount;//钻石团队流水
    private BigDecimal diamoAward;//钻石奖励元/人

    private Integer adoNum;//成长津贴 推广人数
    private BigDecimal adoAmount;//成长津贴 团队流水
    private BigDecimal adoRate;//成长津贴 比例值
    private BigDecimal adoLimit;//成长津贴封顶

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public Integer getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(Integer goldNum) {
        this.goldNum = goldNum;
    }

    public BigDecimal getGoldAmount() {
        return goldAmount;
    }

    public void setGoldAmount(BigDecimal goldAmount) {
        this.goldAmount = goldAmount;
    }

    public BigDecimal getGoldAward() {
        return goldAward;
    }

    public void setGoldAward(BigDecimal goldAward) {
        this.goldAward = goldAward;
    }

    public Integer getPtNum() {
        return ptNum;
    }

    public void setPtNum(Integer ptNum) {
        this.ptNum = ptNum;
    }

    public BigDecimal getPtAmount() {
        return ptAmount;
    }

    public void setPtAmount(BigDecimal ptAmount) {
        this.ptAmount = ptAmount;
    }

    public BigDecimal getPtAward() {
        return ptAward;
    }

    public void setPtAward(BigDecimal ptAward) {
        this.ptAward = ptAward;
    }

    public Integer getBgoldNum() {
        return bgoldNum;
    }

    public void setBgoldNum(Integer bgoldNum) {
        this.bgoldNum = bgoldNum;
    }

    public BigDecimal getBgoldAmount() {
        return bgoldAmount;
    }

    public void setBgoldAmount(BigDecimal bgoldAmount) {
        this.bgoldAmount = bgoldAmount;
    }

    public BigDecimal getBgoldAward() {
        return bgoldAward;
    }

    public void setBgoldAward(BigDecimal bgoldAward) {
        this.bgoldAward = bgoldAward;
    }

    public Integer getDiamoNum() {
        return diamoNum;
    }

    public void setDiamoNum(Integer diamoNum) {
        this.diamoNum = diamoNum;
    }

    public BigDecimal getDiamoAmount() {
        return diamoAmount;
    }

    public void setDiamoAmount(BigDecimal diamoAmount) {
        this.diamoAmount = diamoAmount;
    }

    public BigDecimal getDiamoAward() {
        return diamoAward;
    }

    public void setDiamoAward(BigDecimal diamoAward) {
        this.diamoAward = diamoAward;
    }

    public Integer getAdoNum() {
        return adoNum;
    }

    public void setAdoNum(Integer adoNum) {
        this.adoNum = adoNum;
    }

    public BigDecimal getAdoAmount() {
        return adoAmount;
    }

    public void setAdoAmount(BigDecimal adoAmount) {
        this.adoAmount = adoAmount;
    }

    public BigDecimal getAdoRate() {
        return adoRate;
    }

    public void setAdoRate(BigDecimal adoRate) {
        this.adoRate = adoRate;
    }

    public BigDecimal getAdoLimit() {
        return adoLimit;
    }

    public void setAdoLimit(BigDecimal adoLimit) {
        this.adoLimit = adoLimit;
    }
}
