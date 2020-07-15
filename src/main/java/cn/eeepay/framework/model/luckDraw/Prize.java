package cn.eeepay.framework.model.luckDraw;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/11/6/006.
 * @author  liuks
 * 奖项配置里的奖项
 */
public class Prize {

    private Integer id;//
    private String funcCode;//奖项配置funcCode
    private String awardDesc;//奖项说明
    private String awardName;//奖品名称
    private Integer awardCount;//奖品数量
    private Integer sort;//序列号
    private BigDecimal prob;//中奖概率
    private Integer dayCount;//每天最多派数量
    private String awardHit;//中奖提示
    private String awardPic;//奖品图片
    private Integer awardType;//奖品类型1鼓励金2超级积分3现金券4未中奖
    private Integer status;//状态 0:关闭,1:开启
    private Date createTime;//创建时间
    private String operator;//操作人

    private BigDecimal money;//金额,临时存储

    private Integer dayCount1;//今日已经发放数量
    private Integer dayCount2;//今日剩余发放数量

    private Integer merDayCount;//单个用户每天最多中奖次数


    private List<PrizeDetail> prizeDetailList;//奖品列表

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    public String getAwardDesc() {
        return awardDesc;
    }

    public void setAwardDesc(String awardDesc) {
        this.awardDesc = awardDesc;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public Integer getAwardCount() {
        return awardCount;
    }

    public void setAwardCount(Integer awardCount) {
        this.awardCount = awardCount;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public BigDecimal getProb() {
        return prob;
    }

    public void setProb(BigDecimal prob) {
        this.prob = prob;
    }

    public Integer getDayCount() {
        return dayCount;
    }

    public void setDayCount(Integer dayCount) {
        this.dayCount = dayCount;
    }

    public String getAwardHit() {
        return awardHit;
    }

    public void setAwardHit(String awardHit) {
        this.awardHit = awardHit;
    }

    public String getAwardPic() {
        return awardPic;
    }

    public void setAwardPic(String awardPic) {
        this.awardPic = awardPic;
    }

    public Integer getAwardType() {
        return awardType;
    }

    public void setAwardType(Integer awardType) {
        this.awardType = awardType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public List<PrizeDetail> getPrizeDetailList() {
        return prizeDetailList;
    }

    public void setPrizeDetailList(List<PrizeDetail> prizeDetailList) {
        this.prizeDetailList = prizeDetailList;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getDayCount1() {
        return dayCount1;
    }

    public void setDayCount1(Integer dayCount1) {
        this.dayCount1 = dayCount1;
    }

    public Integer getDayCount2() {
        return dayCount2;
    }

    public void setDayCount2(Integer dayCount2) {
        this.dayCount2 = dayCount2;
    }

    public Integer getMerDayCount() {
        return merDayCount;
    }

    public void setMerDayCount(Integer merDayCount) {
        this.merDayCount = merDayCount;
    }
}
