package cn.eeepay.framework.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @see 商户迁移
 * @author ws
 * @date 2017年3月3日2:07:27
 * 
 *
 */
public class MerchantMigrate {
	
	private int 		id;				//ID编号
	private String 		oaNo;			//OA单号
	private String 		agentNo;		//一级代理商编号
	private String 		nodeAgentNo;	//所属代理商编号
	private String 		checkPerson;	//审核人
	private String 		checkStatus;	//审核状态
	private String 		createPerson;	//申请人
	private int 		goSn;			//设备随迁
	private String 		fileName;		//上传文件名称
	private Timestamp 	checkTime;		//审核时间
	private Timestamp 	createTime;		//申请时间
	private Timestamp 	migrateTime;	//迁移时间
	private String		oneAgentName;	//一级代理商名称
	private String		nodeAgentName;	//所属代理商名称
	private Timestamp		sTime;		//开始时间
	private Timestamp		eTime;		//截止时间
	private String			checkRemark;//审核意见	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCheckRemark() {
		return checkRemark;
	}
	public void setCheckRemark(String checkRemark) {
		this.checkRemark = checkRemark;
	}
	public Timestamp getsTime() {
		return sTime;
	}
	public void setsTime(Timestamp sTime) {
		this.sTime = sTime;
	}
	public Timestamp geteTime() {
		return eTime;
	}
	public void seteTime(Timestamp eTime) {
		this.eTime = eTime;
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
	public String getOaNo() {
		return oaNo;
	}
	public void setOaNo(String oaNo) {
		this.oaNo = oaNo;
	}
	public String getAgentNo() {
		return agentNo;
	}
	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}
	public String getNodeAgentNo() {
		return nodeAgentNo;
	}
	public void setNodeAgentNo(String nodeAgentNo) {
		this.nodeAgentNo = nodeAgentNo;
	}
	public String getCheckPerson() {
		return checkPerson;
	}
	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getCreatePerson() {
		return createPerson;
	}
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}
	public int getGoSn() {
		return goSn;
	}
	public void setGoSn(int goSn) {
		this.goSn = goSn;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Timestamp getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Timestamp checkTime) {
		this.checkTime = checkTime;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getMigrateTime() {
		return migrateTime;
	}
	public void setMigrateTime(Timestamp migrateTime) {
		this.migrateTime = migrateTime;
	}
	
	
	
	
	

}
