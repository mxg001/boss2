package cn.eeepay.framework.model;

import java.io.Serializable;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public class SysConfig implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 private Integer id;
	 private String sysKey;
	 private String sysName;
	 private String sysValue;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSysKey() {
		return sysKey;
	}
	public void setSysKey(String sysKey) {
		this.sysKey = sysKey;
	}
	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	public String getSysValue() {
		return sysValue;
	}
	public void setSysValue(String sysValue) {
		this.sysValue = sysValue;
	}

	 
}
