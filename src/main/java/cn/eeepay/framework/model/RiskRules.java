package cn.eeepay.framework.model;

import java.util.Date;

public class RiskRules {
    private Integer rulesNo;

    private String rulesEngine;
    
    private String rulesValues;
    
    private Integer rulesInstruction;

    private Integer efficientNodeNo;

    private String blackRollNo;

    private String whiteRollNo;

    private Integer status;

    private String treatmentMeasures;

    private Integer warningNo;

    private String warningInfo;

    private String remark;
    
    private Integer merWhiteRoll;
    
    private Integer realNameWhiteRoll;

    private Integer walletWhiteRoll;
    
    private Date createTime;

    private String rulesProvinces;

    private String rulesCity;

    private String rulesProvincesList;

    private String rulesCityList;

    private String[] provincesList;

    private String[] cityList;

    private String rulesTeamIds;
    private String rulesMerchantType;

    public String getRulesMerchantType() {
        return rulesMerchantType;
    }

    public void setRulesMerchantType(String rulesMerchantType) {
        this.rulesMerchantType = rulesMerchantType;
    }

    public String getRulesTeamIds() {
		return rulesTeamIds;
	}

	public void setRulesTeamIds(String rulesTeamIds) {
		this.rulesTeamIds = rulesTeamIds;
	}

	public Integer getWalletWhiteRoll() {
		return walletWhiteRoll;
	}

	public void setWalletWhiteRoll(Integer walletWhiteRoll) {
		this.walletWhiteRoll = walletWhiteRoll;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getRulesNo() {
        return rulesNo;
    }

    public void setRulesNo(Integer rulesNo) {
        this.rulesNo = rulesNo;
    }

    public String getRulesEngine() {
        return rulesEngine;
    }

    public void setRulesEngine(String rulesEngine) {
        this.rulesEngine = rulesEngine == null ? null : rulesEngine.trim();
    }

    public Integer getRulesInstruction() {
		return rulesInstruction;
	}

	public void setRulesInstruction(Integer rulesInstruction) {
		this.rulesInstruction = rulesInstruction;
	}

	public Integer getEfficientNodeNo() {
        return efficientNodeNo;
    }

    public void setEfficientNodeNo(Integer efficientNodeNo) {
        this.efficientNodeNo = efficientNodeNo;
    }

    public String getBlackRollNo() {
        return blackRollNo;
    }

    public void setBlackRollNo(String blackRollNo) {
        this.blackRollNo = blackRollNo == null ? null : blackRollNo.trim();
    }

    public String getWhiteRollNo() {
        return whiteRollNo;
    }

    public void setWhiteRollNo(String whiteRollNo) {
        this.whiteRollNo = whiteRollNo == null ? null : whiteRollNo.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTreatmentMeasures() {
        return treatmentMeasures;
    }

    public void setTreatmentMeasures(String treatmentMeasures) {
        this.treatmentMeasures = treatmentMeasures == null ? null : treatmentMeasures.trim();
    }

    public Integer getWarningNo() {
        return warningNo;
    }

    public void setWarningNo(Integer warningNo) {
        this.warningNo = warningNo;
    }

    public String getWarningInfo() {
        return warningInfo;
    }

    public void setWarningInfo(String warningInfo) {
        this.warningInfo = warningInfo == null ? null : warningInfo.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	public Integer getMerWhiteRoll() {
		return merWhiteRoll;
	}

	public void setMerWhiteRoll(Integer merWhiteRoll) {
		this.merWhiteRoll = merWhiteRoll;
	}

	public Integer getRealNameWhiteRoll() {
		return realNameWhiteRoll;
	}

	public void setRealNameWhiteRoll(Integer realNameWhiteRoll) {
		this.realNameWhiteRoll = realNameWhiteRoll;
	}

	public String getRulesValues() {
		return rulesValues;
	}

	public void setRulesValues(String rulesValues) {
		this.rulesValues = rulesValues;
	}

    public String getRulesProvinces() {
        return rulesProvinces;
    }

    public void setRulesProvinces(String rulesProvinces) {
        this.rulesProvinces = rulesProvinces;
    }

    public String getRulesCity() {
        return rulesCity;
    }

    public void setRulesCity(String rulesCity) {
        this.rulesCity = rulesCity;
    }

    public String getRulesProvincesList() {
        return rulesProvincesList;
    }

    public void setRulesProvincesList(String rulesProvincesList) {
        this.rulesProvincesList = rulesProvincesList;
    }

    public String getRulesCityList() {
        return rulesCityList;
    }

    public void setRulesCityList(String rulesCityList) {
        this.rulesCityList = rulesCityList;
    }

    public String[] getProvincesList() {
        return provincesList;
    }

    public void setProvincesList(String[] provincesList) {
        this.provincesList = provincesList;
    }

    public String[] getCityList() {
        return cityList;
    }

    public void setCityList(String[] cityList) {
        this.cityList = cityList;
    }
}