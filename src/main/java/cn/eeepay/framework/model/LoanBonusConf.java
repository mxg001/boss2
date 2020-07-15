package cn.eeepay.framework.model;

/**
 * 贷款奖励配置
 * @author Administrator
 *
 */
public class LoanBonusConf {

	private Long id; 
	
	private Long orgId;			//  组织id,
	
	private Long sourceId;		//  贷款机构id,
	
	private String orgCostLoan; //品牌成本-放贷         (按比例)
	
	private String orgCostReg; //品牌成本-注册           (固定金额)
	
	private String orgPushLoan;//品牌发放成本-放贷    (按比例)
	
	private String orgPushReg; //品牌发放成本-注册     (固定金额)
	
	private String updateBy;	 //  修改人,
	
	private String registerBonus;//注册奖金   (固定金额)
	
	private String loanBonus;	//放贷奖金     (按比例)
	
	private String orgName;		//组织名称
	
	private String companyName;	//贷款机构名称
	
	private String busiType;	//1:开户授信;2批贷
	
	private String profitType;	//比例类型
	
	private java.util.Date updateDate;

	private String totalCost;//总奖金

	private String companyBonus;//公司截留奖金

	private String orgBonus;//组织截留奖金

    private String rewardRequirements;//奖励要求(1:有效注册，2有效借款，3授信成功)
    
    private String loanAlias;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public java.util.Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getLoanBonus() {
		return loanBonus;
	}

	public void setLoanBonus(String loanBonus) {
		this.loanBonus = loanBonus;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBusiType() {
		return busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	public String getProfitType() {
		return profitType;
	}

	public void setProfitType(String profitType) {
		this.profitType = profitType;
	}

	public String getOrgCostLoan() {
		return orgCostLoan;
	}

	public void setOrgCostLoan(String orgCostLoan) {
		this.orgCostLoan = orgCostLoan;
	}

	public String getOrgCostReg() {
		return orgCostReg;
	}

	public void setOrgCostReg(String orgCostReg) {
		this.orgCostReg = orgCostReg;
	}

	public String getOrgPushLoan() {
		return orgPushLoan;
	}

	public void setOrgPushLoan(String orgPushLoan) {
		this.orgPushLoan = orgPushLoan;
	}

	public String getOrgPushReg() {
		return orgPushReg;
	}

	public void setOrgPushReg(String orgPushReg) {
		this.orgPushReg = orgPushReg;
	}

	public String getRegisterBonus() {
		return registerBonus;
	}

	public void setRegisterBonus(String registerBonus) {
		this.registerBonus = registerBonus;
	}

	public String getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}

	public String getCompanyBonus() {
		return companyBonus;
	}

	public void setCompanyBonus(String companyBonus) {
		this.companyBonus = companyBonus;
	}

	public String getOrgBonus() {
		return orgBonus;
	}

	public void setOrgBonus(String orgBonus) {
		this.orgBonus = orgBonus;
	}

    public String getRewardRequirements() {
        return rewardRequirements;
    }

    public void setRewardRequirements(String rewardRequirements) {
        this.rewardRequirements = rewardRequirements;
    }

	public String getLoanAlias() {
		return loanAlias;
	}

	public void setLoanAlias(String loanAlias) {
		this.loanAlias = loanAlias;
	}
    
	private String isOpen;// 是否外放组织 0-否 1-是

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
	
	
}
