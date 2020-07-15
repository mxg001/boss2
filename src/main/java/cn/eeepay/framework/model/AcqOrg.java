package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;
/**
 * table Acp_org
 * desc 收单机构
 * @author thj
 *
 */
public class AcqOrg {
    private Integer id;

    private String acqName;

    private String acqEnname;

    private String host;

    private String port;

    private String lmkOmk;

    private String lmkOmkCv;

    private String lmkOpk;

    private String lmkOpkCv;

    private String lmkOak;

    private String lmkOakCv;

    private String workKey;

    private Integer acqStatus;

    private Integer settleType;

    private String dayAlteredTime;

    private Integer settleAccountId;

    private Integer acqTransHaveOut;

    private Integer realtimeT0greatert1;

    private BigDecimal acqSuccessAmount;

    private String phone;

    private BigDecimal acqDefDayamount;

    private Integer dayamountT0greatert1;

    private BigDecimal t0AdvanceMoney;

    private BigDecimal t0OwnMoney;

    private BigDecimal valvesAmount;

    private BigDecimal t1TransAmount;

    private BigDecimal t0TransAdvanceAmount;

    private BigDecimal t0TransOwnAmount;

    private Integer closeType;

    private Date closeStartTime;

    private Date closeEndTime;

    private String acqCloseTips;

    private Date createTime;

    private String createPerson;
    
    private Integer channelStatus;//交易转集群状态:0关闭（直清通道），1开启（集群通道）
    

	public Integer getChannelStatus() {
		return channelStatus;
	}

	public void setChannelStatus(Integer channelStatus) {
		this.channelStatus = channelStatus;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAcqName() {
        return acqName;
    }

    public void setAcqName(String acqName) {
        this.acqName = acqName == null ? null : acqName.trim();
    }

    public String getAcqEnname() {
        return acqEnname;
    }

    public void setAcqEnname(String acqEnname) {
        this.acqEnname = acqEnname == null ? null : acqEnname.trim();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host == null ? null : host.trim();
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port == null ? null : port.trim();
    }

    public String getLmkOmk() {
        return lmkOmk;
    }

    public void setLmkOmk(String lmkOmk) {
        this.lmkOmk = lmkOmk == null ? null : lmkOmk.trim();
    }

    public String getLmkOmkCv() {
        return lmkOmkCv;
    }

    public void setLmkOmkCv(String lmkOmkCv) {
        this.lmkOmkCv = lmkOmkCv == null ? null : lmkOmkCv.trim();
    }

    public String getLmkOpk() {
        return lmkOpk;
    }

    public void setLmkOpk(String lmkOpk) {
        this.lmkOpk = lmkOpk == null ? null : lmkOpk.trim();
    }

    public String getLmkOpkCv() {
        return lmkOpkCv;
    }

    public void setLmkOpkCv(String lmkOpkCv) {
        this.lmkOpkCv = lmkOpkCv == null ? null : lmkOpkCv.trim();
    }

    public String getLmkOak() {
        return lmkOak;
    }

    public void setLmkOak(String lmkOak) {
        this.lmkOak = lmkOak == null ? null : lmkOak.trim();
    }

    public String getLmkOakCv() {
        return lmkOakCv;
    }

    public void setLmkOakCv(String lmkOakCv) {
        this.lmkOakCv = lmkOakCv == null ? null : lmkOakCv.trim();
    }

    public String getWorkKey() {
        return workKey;
    }

    public void setWorkKey(String workKey) {
        this.workKey = workKey == null ? null : workKey.trim();
    }

    public Integer getAcqStatus() {
        return acqStatus;
    }

    public void setAcqStatus(Integer acqStatus) {
        this.acqStatus = acqStatus;
    }

    public Integer getSettleType() {
        return settleType;
    }

    public void setSettleType(Integer settleType) {
        this.settleType = settleType;
    }

    public String getDayAlteredTime() {
        return dayAlteredTime;
    }

    public void setDayAlteredTime(String dayAlteredTime) {
        this.dayAlteredTime = dayAlteredTime;
    }

    public Integer getSettleAccountId() {
        return settleAccountId;
    }

    public void setSettleAccountId(Integer settleAccountId) {
        this.settleAccountId = settleAccountId;
    }

    public Integer getAcqTransHaveOut() {
        return acqTransHaveOut;
    }

    public void setAcqTransHaveOut(Integer acqTransHaveOut) {
        this.acqTransHaveOut = acqTransHaveOut;
    }

    public Integer getRealtimeT0greatert1() {
        return realtimeT0greatert1;
    }

    public void setRealtimeT0greatert1(Integer realtimeT0greatert1) {
        this.realtimeT0greatert1 = realtimeT0greatert1;
    }

    public BigDecimal getAcqSuccessAmount() {
        return acqSuccessAmount;
    }

    public void setAcqSuccessAmount(BigDecimal acqSuccessAmount) {
        this.acqSuccessAmount = acqSuccessAmount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public BigDecimal getAcqDefDayamount() {
        return acqDefDayamount;
    }

    public void setAcqDefDayamount(BigDecimal acqDefDayamount) {
        this.acqDefDayamount = acqDefDayamount;
    }

    public Integer getDayamountT0greatert1() {
        return dayamountT0greatert1;
    }

    public void setDayamountT0greatert1(Integer dayamountT0greatert1) {
        this.dayamountT0greatert1 = dayamountT0greatert1;
    }

    public BigDecimal getT0AdvanceMoney() {
        return t0AdvanceMoney;
    }

    public void setT0AdvanceMoney(BigDecimal t0AdvanceMoney) {
        this.t0AdvanceMoney = t0AdvanceMoney;
    }

    public BigDecimal getT0OwnMoney() {
        return t0OwnMoney;
    }

    public void setT0OwnMoney(BigDecimal t0OwnMoney) {
        this.t0OwnMoney = t0OwnMoney;
    }

    public BigDecimal getValvesAmount() {
        return valvesAmount;
    }

    public void setValvesAmount(BigDecimal valvesAmount) {
        this.valvesAmount = valvesAmount;
    }

    public BigDecimal getT1TransAmount() {
        return t1TransAmount;
    }

    public void setT1TransAmount(BigDecimal t1TransAmount) {
        this.t1TransAmount = t1TransAmount;
    }

    public BigDecimal getT0TransAdvanceAmount() {
        return t0TransAdvanceAmount;
    }

    public void setT0TransAdvanceAmount(BigDecimal t0TransAdvanceAmount) {
        this.t0TransAdvanceAmount = t0TransAdvanceAmount;
    }

    public BigDecimal getT0TransOwnAmount() {
        return t0TransOwnAmount;
    }

    public void setT0TransOwnAmount(BigDecimal t0TransOwnAmount) {
        this.t0TransOwnAmount = t0TransOwnAmount;
    }

    public Integer getCloseType() {
        return closeType;
    }

    public void setCloseType(Integer closeType) {
        this.closeType = closeType;
    }

    public Date getCloseStartTime() {
        return closeStartTime;
    }

    public void setCloseStartTime(Date closeStartTime) {
        this.closeStartTime = closeStartTime;
    }

    public Date getCloseEndTime() {
        return closeEndTime;
    }

    public void setCloseEndTime(Date closeEndTime) {
        this.closeEndTime = closeEndTime;
    }

    public String getAcqCloseTips() {
        return acqCloseTips;
    }

    public void setAcqCloseTips(String acqCloseTips) {
        this.acqCloseTips = acqCloseTips == null ? null : acqCloseTips.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson == null ? null : createPerson.trim();
    }
}