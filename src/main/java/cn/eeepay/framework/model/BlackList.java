/**
 * 
 */
package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 黑名单类
 * @author yxs
 * @date  2018年10月16日
 */
public class BlackList {

	private Long id;
	private Integer type;//黑名单类型  0添加黑名单 1关闭黑名单 2打开黑名单
	private String userCode;//操作人Id
	private String userPhone;//电话
	private String userIdCard;//身份证
	private Integer status;//状态 0关闭 1打开
	private Date createDate;//创建时间
	private String createBy;//创建人
	private String remark;//备注
	
	private String createDateStart;
	private String createDateEnd;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		return userCode;
	}
	/**
	 * @param userCode the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	/**
	 * @return the userPhone
	 */
	public String getUserPhone() {
		return userPhone;
	}
	/**
	 * @param userPhone the userPhone to set
	 */
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	/**
	 * @return the userIdCard
	 */
	public String getUserIdCard() {
		return userIdCard;
	}
	/**
	 * @param userIdCard the userIdCard to set
	 */
	public void setUserIdCard(String userIdCard) {
		this.userIdCard = userIdCard;
	}
	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the createBy
	 */
	public String getCreateBy() {
		return createBy;
	}
	/**
	 * @param createBy the createBy to set
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the createDateStart
	 */
	public String getCreateDateStart() {
		return createDateStart;
	}
	/**
	 * @param createDateStart the createDateStart to set
	 */
	public void setCreateDateStart(String createDateStart) {
		this.createDateStart = createDateStart;
	}
	/**
	 * @return the createDateEnd
	 */
	public String getCreateDateEnd() {
		return createDateEnd;
	}
	/**
	 * @param createDateEnd the createDateEnd to set
	 */
	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}
	
	
	
}
