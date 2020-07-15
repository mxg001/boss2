package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 违章代缴订单奖金设置
 * 
 * @author Administrator
 * 
 */
public class CarOrderProfitConf {

	private Long id;
	
	private String orgPushAmount;// 每单组织发放金额
	
	private String orgGainAmount;// 每单OEM获得金额
	
	private Date createTime;
	
	private Date updateTime;
	
	private String createBy;
	
	private String updateBy;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrgPushAmount() {
		return orgPushAmount;
	}

	public void setOrgPushAmount(String orgPushAmount) {
		this.orgPushAmount = orgPushAmount;
	}

	public String getOrgGainAmount() {
		return orgGainAmount;
	}

	public void setOrgGainAmount(String orgGainAmount) {
		this.orgGainAmount = orgGainAmount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
}
