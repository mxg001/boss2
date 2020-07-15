package cn.eeepay.framework.model;


/**
 * 排行榜组织各级奖金设置表
 * @author dxy
 *
 */
public class RankingRuleLevel {
	private Long id;
	private String ruleId;            //排行榜规则表id
	private String levelNum;          //级别数字形式： 1,2,3...20
	private String level;             //级别 一,二,三...二十级，最多二十级
	private Integer prizePeopleCount; //获奖人数
	private String singlePrize;       //单人奖金
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	public String getLevelNum() {
		return levelNum;
	}
	public void setLevelNum(String levelNum) {
		this.levelNum = levelNum;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Integer getPrizePeopleCount() {
		return prizePeopleCount;
	}
	public void setPrizePeopleCount(Integer prizePeopleCount) {
		this.prizePeopleCount = prizePeopleCount;
	}
	public String getSinglePrize() {
		return singlePrize;
	}
	public void setSinglePrize(String singlePrize) {
		this.singlePrize = singlePrize;
	}
	@Override
	public String toString() {
		return "RankingRuleLevel [id=" + id + ", ruleId=" + ruleId
				+ ", levelNum=" + levelNum + ", level=" + level
				+ ", prizePeopleCount=" + prizePeopleCount + ", singlePrize="
				+ singlePrize + "]";
	}
	
}
