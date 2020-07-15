package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 红包业务组织分类排序表
 * @author Administrator
 *
 */
public class RedOrgSort {
	
	private Long id;
    private String busCode;	//活动编号,跟red_control表相关联
    private Long orgId;	//品牌组织id
    private Integer sortNum; //排序
    private String category; //红包业务活动分类
    private Date createTime; //创建时间
    private Date updateTime; //修改时间
    private Integer operator; //操作人ID
    private String ids;
    private String orgIds;
    
	public RedOrgSort() {
		super();
	}
	
	public RedOrgSort(String busCode, Long orgId) {
		super();
		this.busCode = busCode;
		this.orgId = orgId;
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBusCode() {
		return busCode;
	}
	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Integer getSortNum() {
		return sortNum;
	}
	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	public Integer getOperator() {
		return operator;
	}
	public void setOperator(Integer operator) {
		this.operator = operator;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getOrgIds() {
		return orgIds;
	}
	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}
    
}
