package cn.eeepay.framework.model;

import java.util.Date;
/**
 * table terminal_info
 * desc 机具信息表
 */
public class TerminalInfo {
    private Long id;
	private String sn;
    private String snStart;
    private String snEnd;
    private String terminalId;
    private String merchantNo;
    private String psamNo;
    private String agentNo;
    private String agentNode;
    private String openStatus;
    private String type;
    private String allotBatch;
    private String model;
    private String tmk;
    private String tmkTpk;
    private String tmkTak;
    private Date startTime;
    private Date createTime;
    private String posType;
    private Byte needCheck;
    private Date lastCheckInTime;
    private String cashierNo;
    private String serialNo;
    private String batchNo;
    private String collectionCode;
    private Integer hasKey;//是否有密钥
    private Date startTimeBegin;//申请时间，起始（条件查询）
    private Date startTimeEnd;
    private String merchantName;//商户名称
    private String agentLevel; //代理商级别
    private String agentName; //代理商名称
    private String typeName;//硬件产品
    private String versionNu;
    private String activityType;//活动类型
    private Date checkTime;//考核日期
    private int dueDays;//考核剩余天数
    private String dueDaysValue;
    private String status;//激活状态
    private String checkStatus;//考核达标状态
    private String order;//0-考核天数降序 1-考核天数升序
    private Date checkTimeBegin;
    private Date checkTimeEnd;
    private String daysStart;
    private String daysEnd;
	//查询专用
    private String psamNo1;
    private String bool;
    private String bpId;
    private String bpName;
    private String oneAgentNo;
    private String oneAgentName;//一级代理商姓名
    private String activityTypeName;//活动类型名称

    private String terNo;//上游对应终端号
    private String unionMerNo;//报备商户号

    private String channel;//机具通道
    private String activityTypeNo;//欢乐返子类型编号
    private String activityTypeNoName;//欢乐返子类型名称

    public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

    private String userCode;//所属盟主编号
    private String realName;

    public String getOneAgentNo() {
        return oneAgentNo;
    }

    public void setOneAgentNo(String oneAgentNo) {
        this.oneAgentNo = oneAgentNo;
    }

    public String getOneAgentName() {
		return oneAgentName;
	}

	public void setOneAgentName(String oneAgentName) {
		this.oneAgentName = oneAgentName;
	}
    
    public String getCollectionCode() {
		return collectionCode;
	}

	public void setCollectionCode(String collectionCode) {
		this.collectionCode = collectionCode;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }
    
    
    public void setSn(String sn) {
        this.sn = sn == null ? null : sn.trim();
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId == null ? null : terminalId.trim();
    }

    public String getPsamNo() {
        return psamNo;
    }

    public void setPsamNo(String psamNo) {
        this.psamNo = psamNo == null ? null : psamNo.trim();
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo == null ? null : agentNo.trim();
    }

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode == null ? null : agentNode.trim();
    }

    public String getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(String openStatus) {
        this.openStatus = openStatus == null ? null : openStatus.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getAllotBatch() {
        return allotBatch;
    }

    public void setAllotBatch(String allotBatch) {
        this.allotBatch = allotBatch == null ? null : allotBatch.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getTmk() {
        return tmk;
    }

    public void setTmk(String tmk) {
        this.tmk = tmk == null ? null : tmk.trim();
    }

    public String getTmkTpk() {
        return tmkTpk;
    }

    public void setTmkTpk(String tmkTpk) {
        this.tmkTpk = tmkTpk == null ? null : tmkTpk.trim();
    }

    public String getTmkTak() {
        return tmkTak;
    }

    public void setTmkTak(String tmkTak) {
        this.tmkTak = tmkTak == null ? null : tmkTak.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPosType() {
        return posType;
    }

    public void setPosType(String posType) {
        this.posType = posType;
    }

    public Byte getNeedCheck() {
        return needCheck;
    }

    public void setNeedCheck(Byte needCheck) {
        this.needCheck = needCheck;
    }

    public Date getLastCheckInTime() {
        return lastCheckInTime;
    }

    public void setLastCheckInTime(Date lastCheckInTime) {
        this.lastCheckInTime = lastCheckInTime;
    }

    public String getCashierNo() {
        return cashierNo;
    }

    public void setCashierNo(String cashierNo) {
        this.cashierNo = cashierNo == null ? null : cashierNo.trim();
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo == null ? null : serialNo.trim();
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentLevel() {
		return agentLevel;
	}

	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
	}

	public String getPsamNo1() {
		return psamNo1;
	}

	public void setPsamNo1(String psamNo1) {
		this.psamNo1 = psamNo1;
	}

	public String getBool() {
		return bool;
	}

	public void setBool(String bool) {
		this.bool = bool;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getVersionNu() {
		return versionNu;
	}

	public void setVersionNu(String versionNu) {
		this.versionNu = versionNu;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getBpId() {
		return bpId;
	}

	public void setBpId(String bpId) {
		this.bpId = bpId;
	}

	public String getBpName() {
		return bpName;
	}

	public void setBpName(String bpName) {
		this.bpName = bpName;
	}

	public String getSnStart() {
		return snStart;
	}

	public void setSnStart(String snStart) {
		this.snStart = snStart;
	}

	public String getSnEnd() {
		return snEnd;
	}

	public void setSnEnd(String snEnd) {
		this.snEnd = snEnd;
	}

	public Date getStartTimeBegin() {
		return startTimeBegin;
	}

	public void setStartTimeBegin(Date startTimeBegin) {
		this.startTimeBegin = startTimeBegin;
	}

	public Date getStartTimeEnd() {
		return startTimeEnd;
	}

	public void setStartTimeEnd(Date startTimeEnd) {
		this.startTimeEnd = startTimeEnd;
	}

	public Integer getHasKey() {
		return hasKey;
	}

	public void setHasKey(Integer hasKey) {
		this.hasKey = hasKey;
	}

	public String getActivityTypeName() {
		return activityTypeName;
	}

	public void setActivityTypeName(String activityTypeName) {
		this.activityTypeName = activityTypeName;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

    public String getTerNo() {
        return terNo;
    }

    public void setTerNo(String terNo) {
        this.terNo = terNo;
    }

    public String getUnionMerNo() {
        return unionMerNo;
    }

    public void setUnionMerNo(String unionMerNo) {
        this.unionMerNo = unionMerNo;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getActivityTypeNo() {
        return activityTypeNo;
    }

    public void setActivityTypeNo(String activityTypeNo) {
        this.activityTypeNo = activityTypeNo;
    }

    public String getActivityTypeNoName() {
        return activityTypeNoName;
    }

    public void setActivityTypeNoName(String activityTypeNoName) {
        this.activityTypeNoName = activityTypeNoName;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public int getDueDays() {
        return dueDays;
    }

    public void setDueDays(int dueDays) {
        this.dueDays = dueDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Date getCheckTimeBegin() {
        return checkTimeBegin;
    }

    public void setCheckTimeBegin(Date checkTimeBegin) {
        this.checkTimeBegin = checkTimeBegin;
    }

    public Date getCheckTimeEnd() {
        return checkTimeEnd;
    }

    public void setCheckTimeEnd(Date checkTimeEnd) {
        this.checkTimeEnd = checkTimeEnd;
    }

    public String getDueDaysValue() {
        return dueDaysValue;
    }

    public void setDueDaysValue(String dueDaysValue) {
        this.dueDaysValue = dueDaysValue;
    }

    public String getDaysStart() {
        return daysStart;
    }

    public void setDaysStart(String daysStart) {
        this.daysStart = daysStart;
    }

    public String getDaysEnd() {
        return daysEnd;
    }

    public void setDaysEnd(String daysEnd) {
        this.daysEnd = daysEnd;
    }
}