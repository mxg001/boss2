package cn.eeepay.framework.model;

public class OrgSourceConfigSumInfo {
	
	private int totalCount;
	
	private int openCount;
	
	private int offCount;
	
	private String openModel;
	
	private String sourceName;

	public String getOpenModel() {
		return openModel;
	}

	public void setOpenModel(String openModel) {
		this.openModel = openModel;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getOpenCount() {
		return openCount;
	}

	public void setOpenCount(int openCount) {
		this.openCount = openCount;
	}

	public int getOffCount() {
		return offCount;
	}

	public void setOffCount(int offCount) {
		this.offCount = offCount;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
	
}
