package cn.eeepay.framework.model;

/**
 * 自动审件控制
 * @author Administrator
 *
 */
public class SysConfigAutoCheck {
	private Integer id;

	private String paramKey;

	private String paramValue;

	private String remark;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
