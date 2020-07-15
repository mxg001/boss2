package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @desc superbank.loan_source
 * 贷款机构表
 * @author tans
 * @date 2017-12-5
 *
 */
public class LoanSource {

    private Long id;

    private String companyName;//公司

    private String loanProduct;//贷款产品

    private Integer showOrder;//顺序

    private String status;//开关
    
    private int statusInt;//开关

    private String registerProfitFlag;//注册是否返佣:0不返 1返
    
    private String loanProfitFlag;//批贷是否返佣：0不返 1返
    
    private BigDecimal registerBonus;
    
    private String showLogo;//显示logo
    
    private String showLogoUrl;

    private String h5Link;//页面h5
    
    private String h5LinkUrl;//页面h5
    
    private String registerLink;//注册申请h5

    private String loanLink;//申请贷款h5
    
    private String profitType;//奖金模式
    
    private String propaganda;//宣传语
    
    private String propaganda2;//宣传语2
    
    private String propaganda3;//宣传语3
    
    private BigDecimal loanBonus;//贷款总奖金
    
    private String showLoanBonus;//贷款总奖金
    
    private String loanAlias;//贷款机构别称
    
    private String rewardRequirements;//奖励要求

	private String  loanStatus;//每次借款是否有佣金0:是 1:否
    
    private String source;
    
    private String settlementCycle;//结算周期
    
    private String settlementRules;//结算规则
    
    private Date updateTime;//修改时间
    
    private Date createTime;//创建时间
    
    private String specialImage;//引导页图片

	private String specialImageUrl;  //引导页图片地址

	private String specialPosition;//特别推荐位置

    private String ruleCode;//导入匹配规则编码
	private  int accessWay;//接入方式
	private  String apiUrl;//API链接

	public int getAccessWay() {
		return accessWay;
	}

	public void setAccessWay(int accessWay) {
		this.accessWay = accessWay;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public Long getId() {
        return id;
    }
    

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getLoanProduct() {
        return loanProduct;
    }

    public void setLoanProduct(String loanProduct) {
        this.loanProduct = loanProduct == null ? null : loanProduct.trim();
    }

	public String getSpecialImageUrl() {
		return specialImageUrl;
	}

	public void setSpecialImageUrl(String specialImageUrl) {
		this.specialImageUrl = specialImageUrl;
	}

	public String getSpecialPosition() {
		return specialPosition;
	}

	public void setSpecialPosition(String specialPosition) {
		this.specialPosition = specialPosition;
	}

	public Integer getShowOrder() {
        return showOrder;
    }

    
    public BigDecimal getRegisterBonus() {
		return registerBonus;
	}


	public String getShowLoanBonus() {
		return showLoanBonus;
	}


	public void setShowLoanBonus(String showLoanBonus) {
		this.showLoanBonus = showLoanBonus;
	}


	public String getShowLogoUrl() {
		return showLogoUrl;
	}


	public void setShowLogoUrl(String showLogoUrl) {
		this.showLogoUrl = showLogoUrl;
	}


	public void setRegisterBonus(BigDecimal registerBonus) {
		this.registerBonus = registerBonus;
	}


	public String getRegisterProfitFlag() {
		return registerProfitFlag;
	}

	public void setRegisterProfitFlag(String registerProfitFlag) {
		this.registerProfitFlag = registerProfitFlag;
	}

	public String getLoanProfitFlag() {
		return loanProfitFlag;
	}

	public void setLoanProfitFlag(String loanProfitFlag) {
		this.loanProfitFlag = loanProfitFlag;
	}

	public String getRegisterLink() {
		return registerLink;
	}

	public void setRegisterLink(String registerLink) {
		this.registerLink = registerLink;
	}

	public String getLoanLink() {
		return loanLink;
	}

	public void setLoanLink(String loanLink) {
		this.loanLink = loanLink;
	}

	public String getPropaganda() {
		return propaganda;
	}

	public void setPropaganda(String propaganda) {
		this.propaganda = propaganda;
	}

	public String getSource() {
		return source;
	}

	
	public void setSource(String source) {
		this.source = source;
	}

	public String getPropaganda2() {
		return propaganda2;
	}


	public void setPropaganda2(String propaganda2) {
		this.propaganda2 = propaganda2;
	}


	public String getPropaganda3() {
		return propaganda3;
	}


	public void setPropaganda3(String propaganda3) {
		this.propaganda3 = propaganda3;
	}


	public String getSettlementCycle() {
		return settlementCycle;
	}

	public void setSettlementCycle(String settlementCycle) {
		this.settlementCycle = settlementCycle;
	}

	public String getSettlementRules() {
		return settlementRules;
	}

	public void setSettlementRules(String settlementRules) {
		this.settlementRules = settlementRules;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSpecialImage() {
		return specialImage;
	}

	public void setSpecialImage(String specialImage) {
		this.specialImage = specialImage;
	}

	public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getShowLogo() {
        return showLogo;
    }

    public void setShowLogo(String showLogo) {
        this.showLogo = showLogo == null ? null : showLogo.trim();
    }

    public String getH5Link() {
        return h5Link;
    }

    public void setH5Link(String h5Link) {
        this.h5Link = h5Link == null ? null : h5Link.trim();
    }


    public BigDecimal getLoanBonus() {
        return loanBonus;
    }

    public void setLoanBonus(BigDecimal loanBonus) {
        this.loanBonus = loanBonus;
    }


	public String getLoanAlias() {
		return loanAlias;
	}

	public void setLoanAlias(String loanAlias) {
		this.loanAlias = loanAlias;
	}

	public String getLoanStatus() {
		return loanStatus;
	}

	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}

	public int getStatusInt() {
		return statusInt;
	}

	public void setStatusInt(int statusInt) {
		this.statusInt = statusInt;
	}

	public String getRewardRequirements() {
		return rewardRequirements;
	}

	public void setRewardRequirements(String rewardRequirements) {
		this.rewardRequirements = rewardRequirements;
	}

	public String getProfitType() {
		return profitType;
	}

	public void setProfitType(String profitType) {
		this.profitType = profitType;
	}


	public String getRuleCode() {
		return ruleCode;
	}


	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}


	public String getH5LinkUrl() {
		return h5LinkUrl;
	}


	public void setH5LinkUrl(String h5LinkUrl) {
		this.h5LinkUrl = h5LinkUrl;
	}
    
}