package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 活动
 * @author Administrator
 *
 */
public class ActivityHardware {
	private  String activityCode;
	private  String activiyName;
	private  int  hardId;
	private  BigDecimal  price;
	private  BigDecimal  targetAmout;
	private  Date  createTime;
	private int hpId;
	private String typeName;

	
	
	
	public int getHpId() {
		return hpId;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public void setHpId(int hpId) {
		this.hpId = hpId;
	}
	
	
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	public String getActiviyName() {
		return activiyName;
	}
	public void setActiviyName(String activiyName) {
		this.activiyName = activiyName;
	}
	
	public int getHardId() {
		return hardId;
	}
	public void setHardId(int hardId) {
		this.hardId = hardId;
	}
	
	public BigDecimal getTargetAmout() {
		return targetAmout;
	}
	public void setTargetAmout(BigDecimal targetAmout) {
		this.targetAmout = targetAmout;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
	
}
