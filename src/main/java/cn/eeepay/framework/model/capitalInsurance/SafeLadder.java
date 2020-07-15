package cn.eeepay.framework.model.capitalInsurance;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/7/23/023.
 * 资金险设置阶梯
 * 对应表
 * zjx_safe_ladder
 */
public class SafeLadder {

    private Integer id;
    private String title;//标题
    private String proCode;//产品编码
    private BigDecimal minNum;//阶梯最小值
    private BigDecimal maxNum;//阶梯最大值,-1标识无穷大
    private BigDecimal safeQuota;//保额-资金安全限额
    private BigDecimal cost;//单笔保费成本
    private BigDecimal price;//单笔保费售价(元)
    private Date createTime;//创建时间
    private Date lastUpdateTime;//最后修改时间

    private int editState;//是否编辑0否1是

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProCode() {
        return proCode;
    }

    public void setProCode(String proCode) {
        this.proCode = proCode;
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

    public BigDecimal getSafeQuota() {
        return safeQuota;
    }

    public void setSafeQuota(BigDecimal safeQuota) {
        this.safeQuota = safeQuota;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getEditState() {
        return editState;
    }

    public void setEditState(int editState) {
        this.editState = editState;
    }
}
