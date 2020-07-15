package cn.eeepay.framework.model;

import java.util.List;

public class NewHappyBackActivityResult {
	private String totalReward;
	private String totalNoReward;
	private Long totalCount;
	private List<NewHappyBackActivityVo> list;
	private String rewardAccountStatus;
	
	public String getTotalReward() {
		return totalReward;
	}

	public void setTotalReward(String totalReward) {
		this.totalReward = totalReward;
	}

	public String getTotalNoReward() {
		return totalNoReward;
	}

	public void setTotalNoReward(String totalNoReward) {
		this.totalNoReward = totalNoReward;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public List<NewHappyBackActivityVo> getList() {
		return list;
	}

	public void setList(List<NewHappyBackActivityVo> list) {
		this.list = list;
	}

	public String getRewardAccountStatus() {
		return rewardAccountStatus;
	}

	public void setRewardAccountStatus(String rewardAccountStatus) {
		this.rewardAccountStatus = rewardAccountStatus;
	}

}
