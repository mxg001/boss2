package cn.eeepay.framework.model;

import java.util.Date;
import java.util.List;

/**
 * 排行榜修改历史记录表
 * @author dxy
 *
 */
public class RankingRuleHistory {
	private Long id;
	private Long ruleId;      //规则Id
	private String orgBefore; //组织编号修改前
	private String orgAfter;  //类型(0周榜 1月榜 2年榜)
	private Date updateTime;  //操作时间
	private String updateBy;  //操作人
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getRuleId() {
		return ruleId;
	}
	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}
	public String getOrgBefore() {
		return orgBefore;
	}
	public void setOrgBefore(String orgBefore) {
		this.orgBefore = orgBefore;
	}
	public String getOrgAfter() {
		return orgAfter;
	}
	public void setOrgAfter(String orgAfter) {
		this.orgAfter = orgAfter;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	
	
	
}
