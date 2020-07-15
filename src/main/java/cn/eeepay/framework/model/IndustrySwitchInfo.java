package cn.eeepay.framework.model;

import java.util.List;

/**
 * 
 * @author qiujian
 *
 */
public class IndustrySwitchInfo {

	private Integer industrySwitch;
	private List<SysDict> sysDicts;
	private List<IndustrySwitch> industrySwitchList;

	public Integer getIndustrySwitch() {
		return industrySwitch;
	}

	public void setIndustrySwitch(Integer industrySwitch) {
		this.industrySwitch = industrySwitch;
	}

	public List<IndustrySwitch> getIndustrySwitchList() {
		return industrySwitchList;
	}

	public void setIndustrySwitchList(List<IndustrySwitch> industrySwitchList) {
		this.industrySwitchList = industrySwitchList;
	}

	public List<SysDict> getSysDicts() {
		return sysDicts;
	}

	public void setSysDicts(List<SysDict> sysDicts) {
		this.sysDicts = sysDicts;
	}

}
