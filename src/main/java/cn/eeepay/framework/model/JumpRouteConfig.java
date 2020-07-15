package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * table jump_route_config
 * desc 按交易跳转路由集群配置
 * @author tans
 * @date 2017年3月14日 下午3:10:35
 * 
 */
public class JumpRouteConfig {

	private Integer id;
	private Integer acqId;//'收单机构ID编号'
	private Integer groupCode;//'跳转目标路由集群编号'
	private Integer cardType;//'卡类型 1：货记卡，2：借记卡'
	private Integer status;//'状态 0:关闭，1：开启'
	private Date startDate;//'开始生效日期'
	private Date endDate;//'截止生效日期'
	private String startTime;//'每天生效时间'
	private String endTime;//'每天截止时间'
	private BigDecimal minTransAmount;//'最小交易金额'
	private BigDecimal maxTransAmount;//'最大交易金额'
	private Integer jumpTimes;//'跳转次数'
	private Integer apartDays;//'相隔天数'
	private String weekDays;//'每周重复:0,1,2,3,4,5,6,7分别表示周,周六，如果有多个，用逗号隔开'
	private String bpIds;//'业务产品:0为不限，如果有多个，用逗号隔开'
	private String merchantProvinces;//'商户省份:0位不限，如果有多个，用逗号隔开'
	private String remark;//'备注'
	private String cardBinIds;
	private Integer bpType;//业务产品（不限：0，指定：1）
	private Integer provinceType;//商户省份（不限：0，指定：1）
	private Integer cardBinType;
	private Long[] bpList;//业务产品的集合
	private String[] provinceList;//商户省份的集合
	
	private String[] cardBinList;//发卡行集合

    private String merchantCity;//商户市区
    private String acqMerchantType;//指定行业

	private Integer acqMerchantState;//指定行业状态
	private String[] cityList;//所选市集合
	private String[] acqMerchantList;//所选行业集合

    private Integer acqMerchantShowState;//指定行业是否显示
	
    private String serviceTypes;

    private Integer effectiveDateType;//生效日期类型，1：每天，2：周一至周五，3：法定节假日，4：自定义
	private BigDecimal targetAmount;//目标金额
	private BigDecimal totalAmount;//已累计金额
	private String groupName;//跳转集群名称
	private String acqEnname;//收单机构名称
	private Integer teamId;
	private Integer relationActivity;//是否关联活动 0否1是

	public Integer getCardBinType() {
		return cardBinType;
	}

	public void setCardBinType(Integer cardBinType) {
		this.cardBinType = cardBinType;
	}



	public String getCardBinIds() {
		return cardBinIds;
	}

	public void setCardBinIds(String cardBinIds) {
		this.cardBinIds = cardBinIds;
	}

	public String[] getCardBinList() {
		return cardBinList;
	}

	public void setCardBinList(String[] cardBinList) {
		this.cardBinList = cardBinList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAcqId() {
		return acqId;
	}

	public void setAcqId(Integer acqId) {
		this.acqId = acqId;
	}

	public Integer getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(Integer groupCode) {
		this.groupCode = groupCode;
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		if(this.startTime!=null&&this.startTime.length()==5){
			this.startTime+=":00";
		}
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		if(this.endTime!=null&&this.endTime.length()==5){
			this.endTime+=":59";
		}
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public BigDecimal getMinTransAmount() {
		return minTransAmount;
	}

	public void setMinTransAmount(BigDecimal minTransAmount) {
		this.minTransAmount = minTransAmount;
	}

	public BigDecimal getMaxTransAmount() {
		return maxTransAmount;
	}

	public void setMaxTransAmount(BigDecimal maxTransAmount) {
		this.maxTransAmount = maxTransAmount;
	}

	public Integer getJumpTimes() {
		return jumpTimes;
	}

	public void setJumpTimes(Integer jumpTimes) {
		this.jumpTimes = jumpTimes;
	}

	public Integer getApartDays() {
		return apartDays;
	}

	public void setApartDays(Integer apartDays) {
		this.apartDays = apartDays;
	}

	public String getWeekDays() {
		return weekDays;
	}

	public void setWeekDays(String weekDays) {
		this.weekDays = weekDays;
	}

	public String getBpIds() {
		return bpIds;
	}

	public void setBpIds(String bpIds) {
		this.bpIds = bpIds;
	}

	public String getMerchantProvinces() {
		return merchantProvinces;
	}

	public void setMerchantProvinces(String merchantProvinces) {
		this.merchantProvinces = merchantProvinces;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getBpType() {
		return bpType;
	}

	public void setBpType(Integer bpType) {
		this.bpType = bpType;
	}

	public Integer getProvinceType() {
		return provinceType;
	}

	public void setProvinceType(Integer provinceType) {
		this.provinceType = provinceType;
	}

	public Long[] getBpList() {
		return bpList;
	}

	public void setBpList(Long[] bpList) {
		this.bpList = bpList;
	}

	public String[] getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(String[] provinceList) {
		this.provinceList = provinceList;
	}

    public Integer getAcqMerchantState() {
        return acqMerchantState;
    }

    public void setAcqMerchantState(Integer acqMerchantState) {
        this.acqMerchantState = acqMerchantState;
    }

    public String[] getCityList() {
        return cityList;
    }

    public void setCityList(String[] cityList) {
        this.cityList = cityList;
    }

    public String[] getAcqMerchantList() {
        return acqMerchantList;
    }

    public void setAcqMerchantList(String[] acqMerchantList) {
        this.acqMerchantList = acqMerchantList;
    }

    public String getMerchantCity() {
        return merchantCity;
    }

    public void setMerchantCity(String merchantCity) {
        this.merchantCity = merchantCity;
    }

    public String getAcqMerchantType() {
        return acqMerchantType;
    }

    public void setAcqMerchantType(String acqMerchantType) {
        this.acqMerchantType = acqMerchantType;
    }

    public Integer getAcqMerchantShowState() {
        return acqMerchantShowState;
    }

    public void setAcqMerchantShowState(Integer acqMerchantShowState) {
        this.acqMerchantShowState = acqMerchantShowState;
    }

	public String getServiceTypes() {
		return serviceTypes;
	}

	public void setServiceTypes(String serviceTypes) {
		this.serviceTypes = serviceTypes;
	}

	public Integer getEffectiveDateType() {
		return effectiveDateType;
	}

	public void setEffectiveDateType(Integer effectiveDateType) {
		this.effectiveDateType = effectiveDateType;
	}

	public BigDecimal getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(BigDecimal targetAmount) {
		this.targetAmount = targetAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getAcqEnname() {
		return acqEnname;
	}

	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
	}

	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	public Integer getRelationActivity() {
		return relationActivity;
	}

	public void setRelationActivity(Integer relationActivity) {
		this.relationActivity = relationActivity;
	}
}
