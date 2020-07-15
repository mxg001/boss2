package cn.eeepay.framework.model;

/**
 * 彩票奖励配置
 * @author dxy
 *
 */
public class LotteryBonusConf {

	private Long id; 
	
	private Long orgId;			    //  组织id,
	
	private String orgName;        //组织名称
	
	private String bonusType;      //奖励方式-默认1.按购彩金额比例
	
	private String bonusRequest;   // '奖励要求-默认1.成功出票购彩
	
	private String updateBy;       //修改人
	
	private String updateDate;     //修改时间
	
	private String lotteryOrgTotalBonus; //彩票机构总奖金
	
	private String companyBonus;   //公司截留奖金
	
	private String orgBonus;       //品牌截留奖金
	
	private String profitType;
	

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

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

	public String getBonusType() {
		return bonusType;
	}

	public void setBonusType(String bonusType) {
		this.bonusType = bonusType;
	}

	public String getBonusRequest() {
		return bonusRequest;
	}

	public void setBonusRequest(String bonusRequest) {
		this.bonusRequest = bonusRequest;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getLotteryOrgTotalBonus() {
		return lotteryOrgTotalBonus;
	}

	public void setLotteryOrgTotalBonus(String lotteryOrgTotalBonus) {
		this.lotteryOrgTotalBonus = lotteryOrgTotalBonus;
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

	public String getProfitType() {
		return profitType;
	}

	public void setProfitType(String profitType) {
		this.profitType = profitType;
	}   

}
