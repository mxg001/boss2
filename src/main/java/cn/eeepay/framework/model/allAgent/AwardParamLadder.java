package cn.eeepay.framework.model.allAgent;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/7/11/011.
 * @author  liuks
 * 阶梯配置 对应表 pa_ladder_setting
 */
public class AwardParamLadder {

    private Integer id;
    private String brandCode;//品牌
    private BigDecimal minNum;//最小值(不包含)
    private BigDecimal maxNum;//最大值(包含,-1为无穷大)
    private BigDecimal val;//比例值(单位:万分)
    private String type;//类型 1 交易/管理津贴阶梯  2荣耀奖金阶梯
    private Integer ladderGrade;//级别

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

    public BigDecimal getMinNum() {
        return minNum;
    }

    public void setMinNum(BigDecimal minNum) {
        this.minNum = minNum;
    }

    public BigDecimal getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(BigDecimal maxNum) {
        this.maxNum = maxNum;
    }

    public BigDecimal getVal() {
        return val;
    }

    public void setVal(BigDecimal val) {
        this.val = val;
    }

    public Integer getLadderGrade() {
        return ladderGrade;
    }

    public void setLadderGrade(Integer ladderGrade) {
        this.ladderGrade = ladderGrade;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
