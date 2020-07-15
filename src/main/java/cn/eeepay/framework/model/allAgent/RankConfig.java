package cn.eeepay.framework.model.allAgent;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/12/5/005.
 * @author  liuks
 * 排行版实体
 * 对应表 pa_rank_config
 */
public class RankConfig {

    private Long id;
    private Integer rankMax;//最大排行名次
    private String brandCodeSet;//品牌编码,对应表pa_brand的编码,多个逗号间隔
    private Date startTime;//上线时间
    private Date endTime;//下线时间
    private String rankImg;//封面图
    private String rankUrl;//封面图跳转连接
    private String rankRule;//排行榜规则说明
    private Date createTime;//创建时间
    private Integer oneJoin;//机构是否参与 0 否 1 是
    private Integer oneShow;//机构不参与时是否展示入口 0 否 1 是
    private Integer twoJoin;//大盟主是否参与 0 否 1 是
    private Integer twoShow;//大盟主不参与时是否显示入口 0 否  1 是

    private List<String> brandCodeList;//组织列表
    private List<RankReward> rankRewardList;//各个排名奖励金额

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRankMax() {
        return rankMax;
    }

    public void setRankMax(Integer rankMax) {
        this.rankMax = rankMax;
    }

    public String getBrandCodeSet() {
        return brandCodeSet;
    }

    public void setBrandCodeSet(String brandCodeSet) {
        this.brandCodeSet = brandCodeSet;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getRankImg() {
        return rankImg;
    }

    public void setRankImg(String rankImg) {
        this.rankImg = rankImg;
    }

    public String getRankUrl() {
        return rankUrl;
    }

    public void setRankUrl(String rankUrl) {
        this.rankUrl = rankUrl;
    }

    public String getRankRule() {
        return rankRule;
    }

    public void setRankRule(String rankRule) {
        this.rankRule = rankRule;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<String> getBrandCodeList() {
        return brandCodeList;
    }

    public void setBrandCodeList(List<String> brandCodeList) {
        this.brandCodeList = brandCodeList;
    }

    public List<RankReward> getRankRewardList() {
        return rankRewardList;
    }

    public void setRankRewardList(List<RankReward> rankRewardList) {
        this.rankRewardList = rankRewardList;
    }

    public Integer getOneJoin() {
        return oneJoin;
    }

    public void setOneJoin(Integer oneJoin) {
        this.oneJoin = oneJoin;
    }

    public Integer getOneShow() {
        return oneShow;
    }

    public void setOneShow(Integer oneShow) {
        this.oneShow = oneShow;
    }

    public Integer getTwoJoin() {
        return twoJoin;
    }

    public void setTwoJoin(Integer twoJoin) {
        this.twoJoin = twoJoin;
    }

    public Integer getTwoShow() {
        return twoShow;
    }

    public void setTwoShow(Integer twoShow) {
        this.twoShow = twoShow;
    }
}
