package cn.eeepay.framework.model.allAgent;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/12/5/005.
 * @author  liuks
 * 排行版设置 各个名次的 奖励
 * 对应表pa_rank_reward
 */
public class RankReward {

    private Long id;
    private Long rankConfigId;//排行版配置ID,对应pa_rank_config的id
    private Integer rank;//排行名次
    private BigDecimal rankAmount;//奖励金额

    private Integer reachNum;//达标激活数
    private Integer reachAmount;//未达标奖励金额

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRankConfigId() {
        return rankConfigId;
    }

    public void setRankConfigId(Long rankConfigId) {
        this.rankConfigId = rankConfigId;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public BigDecimal getRankAmount() {
        return rankAmount;
    }

    public void setRankAmount(BigDecimal rankAmount) {
        this.rankAmount = rankAmount;
    }

    public Integer getReachNum() {
        return reachNum;
    }

    public void setReachNum(Integer reachNum) {
        this.reachNum = reachNum;
    }

    public Integer getReachAmount() {
        return reachAmount;
    }

    public void setReachAmount(Integer reachAmount) {
        this.reachAmount = reachAmount;
    }
}
