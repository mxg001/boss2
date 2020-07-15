package cn.eeepay.framework.model;

import java.sql.Timestamp;

/**
 * @see 商户迁移子对象
 * @author ws
 * @date 2017年3月3日2:07:27
 * 
 *
 */
public class MerchantMigrateInfo {
	
	private int 		id;					//ID编号
	private int 		migrateId;			//商户迁移主表ID
	private String 		merchantNo;			//商户编号
	private int 		migrateStatus;		//1.未迁移 2.已迁移 3.迁移失败 
	private int			modifyAgentNo;		//是否修改一级代理商 1.是 2.否 本字段值为1时由定时任务次月1日凌晨修改
	private String 		beforeAgentNo;		//迁移前的一级代理商编号
	private String		beforeNodeAgentNo;	//迁移前的所属代理商编号
	private String		remark;				//描述
	private Timestamp 	migrate_time;		//迁移时间
	private String		oneAgentName;	//一级代理商名称
	private String		nodeAgentName;	//所属代理商名称
	private String		agentLevel;		//所属代理商级别
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public String getOneAgentName() {
		return oneAgentName;
	}
	public void setOneAgentName(String oneAgentName) {
		this.oneAgentName = oneAgentName;
	}
	public String getNodeAgentName() {
		return nodeAgentName;
	}
	public void setNodeAgentName(String nodeAgentName) {
		this.nodeAgentName = nodeAgentName;
	}
	public String getAgentLevel() {
		return agentLevel;
	}
	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
	}
	public int getModifyAgentNo() {
		return modifyAgentNo;
	}
	public void setModifyAgentNo(int modifyAgentNo) {
		this.modifyAgentNo = modifyAgentNo;
	}
	
	public int getMigrateId() {
		return migrateId;
	}
	public void setMigrateId(int migrateId) {
		this.migrateId = migrateId;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public int getMigrateStatus() {
		return migrateStatus;
	}
	public void setMigrateStatus(int migrateStatus) {
		this.migrateStatus = migrateStatus;
	}
	public String getBeforeAgentNo() {
		return beforeAgentNo;
	}
	public void setBeforeAgentNo(String beforeAgentNo) {
		this.beforeAgentNo = beforeAgentNo;
	}
	public String getBeforeNodeAgentNo() {
		return beforeNodeAgentNo;
	}
	public void setBeforeNodeAgentNo(String beforeNodeAgentNo) {
		this.beforeNodeAgentNo = beforeNodeAgentNo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Timestamp getMigrate_time() {
		return migrate_time;
	}
	public void setMigrate_time(Timestamp migrate_time) {
		this.migrate_time = migrate_time;
	}

}
