package cn.eeepay.framework.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
/**
 * table business_product_define
 * desc 业务产品表
 * @author tans
 *
 */

public class BusinessProductDefine {

    private Long bpId;

    private String bpName;

    private String agentShowName;

	@JSONField(format = "yyyy-MM-dd")
    private Date saleStarttime;

	@JSONField(format = "yyyy-MM-dd")
    private Date saleEndtime;

    private String proxy;

    private String bpType;

    private String isOem;

    private String ownBpId;

    private String twoCode;
    
    private String twoCodeUrl;

    private String remark;

    private String bpImg;
    
    private String bpImgUrl;
    
    private String notCheck;
    
    private String teamId;
    
    //关联：所属组织名称
    private String teamName;
    
    //关联：自营业务产品名称
    private String ownBpName;
    
    private String limitHard;
    
    private String link;
    
    private Integer relyHardware;
    
    private Integer linkProduct;
    
    private String linkProductName;
    
    private String hpId;
    
    private Integer allowWebItem;
    
    private Integer allowIndividualApply;
    
    private String groupNo;//群组号

	private Integer effectiveStatus;//生效状态,0:失效,1:生效,默认为1

	private Date createTime;//创建时间

	private Date createTimeBegin;//查询创建时间开始

	private Date createTimeEnd;//创建时间结束

	private String createPerson;//创建人



	public Integer getAllowIndividualApply() {
		return allowIndividualApply;
	}

	public void setAllowIndividualApply(Integer allowIndividualApply) {
		this.allowIndividualApply = allowIndividualApply;
	}

	public Integer getAllowWebItem() {
		return allowWebItem;
	}

	public void setAllowWebItem(Integer allowWebItem) {
		this.allowWebItem = allowWebItem;
	}

	public String getHpId() {
		return hpId;
	}

	public void setHpId(String hpId) {
		this.hpId = hpId;
	}

	public Integer getRelyHardware() {
		return relyHardware;
	}

	public void setRelyHardware(Integer relyHardware) {
		this.relyHardware = relyHardware;
	}

	public Integer getLinkProduct() {
		return linkProduct;
	}

	public void setLinkProduct(Integer linkProduct) {
		this.linkProduct = linkProduct;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLimitHard() {
		return limitHard;
	}

	public void setLimitHard(String limitHard) {
		this.limitHard = limitHard;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId == null ? null : teamId.trim();
	}

	public String getBpImg() {
		return bpImg;
	}

	public void setBpImg(String bpImg) {
		this.bpImg = bpImg == null ? null : bpImg.trim();
	}

	public String getNotCheck() {
		return notCheck;
	}

	public void setNotCheck(String notCheck) {
		this.notCheck = notCheck == null ? null : notCheck.trim();
	}

	public String getOwnBpName() {
		return ownBpName;
	}

	public void setOwnBpName(String ownBpName) {
		this.ownBpName = ownBpName == null ? null : ownBpName.trim();
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		 this.teamName = teamName == null ? null : teamName.trim();
	}

    public Long getBpId() {
        return bpId;
    }

    public void setBpId(Long bpId) {
        this.bpId = bpId ;
    }

    public String getBpName() {
        return bpName;
    }

    public void setBpName(String bpName) {
        this.bpName = bpName == null ? null : bpName.trim();
    }

    public Date getSaleStarttime() {
        return saleStarttime;
    }

    public void setSaleStarttime(Date saleStarttime) {
        this.saleStarttime = saleStarttime;
    }

    public Date getSaleEndtime() {
        return saleEndtime;
    }

    public void setSaleEndtime(Date saleEndtime) {
        this.saleEndtime = saleEndtime;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy == null ? null : proxy.trim();
    }

    public String getBpType() {
        return bpType;
    }

    public void setBpType(String bpType) {
        this.bpType = bpType == null ? null : bpType.trim();
    }

    public String getIsOem() {
        return isOem;
    }

    public void setIsOem(String isOem) {
        this.isOem = isOem == null ? null : isOem.trim();
    }

    public String getOwnBpId() {
        return ownBpId;
    }

    public void setOwnBpId(String ownBpId) {
        this.ownBpId = ownBpId == null ? null : ownBpId.trim();
    }

    public String getTwoCode() {
        return twoCode;
    }

    public void setTwoCode(String twoCode) {
        this.twoCode = twoCode == null ? null : twoCode.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	public String getTwoCodeUrl() {
		return twoCodeUrl;
	}

	public void setTwoCodeUrl(String twoCodeUrl) {
		this.twoCodeUrl = twoCodeUrl;
	}

	public String getBpImgUrl() {
		return bpImgUrl;
	}

	public void setBpImgUrl(String bpImgUrl) {
		this.bpImgUrl = bpImgUrl;
	}

	public String getLinkProductName() {
		return linkProductName;
	}

	public void setLinkProductName(String linkProductName) {
		this.linkProductName = linkProductName;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public Integer getEffectiveStatus() {
		return effectiveStatus;
	}

	public void setEffectiveStatus(Integer effectiveStatus) {
		this.effectiveStatus = effectiveStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(Date createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public String getAgentShowName() {
		return agentShowName;
	}

	public void setAgentShowName(String agentShowName) {
		this.agentShowName = agentShowName;
	}
}