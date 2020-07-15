package cn.eeepay.framework.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class OrgSourceConf {

    private int id;

    private  int orgId;

    private  int sourceId;
    
    private  String code;

    private  String orgName;

    private  String sourceName;

    private  String sourceNickName;
    
    private String type;

    private  String application;

    private Date createDate;

    private String remark;

    private String status;
    
    private String showOrder;
    
    private String createBy;
    
    private Date updateTime;
    
    private String updateBy;
    
    private String isRecommend;
    
    private String openType;
    
    private int statusInt;
    
    private String openUrl;
    
    private String specialImage;
    private String specialImageUrl;
    
    public Integer getStatusInt() {
		if("on".equals(this.status)){
			this.statusInt = 1;
		}else{
			this.statusInt = 0 ;
		}
		return this.statusInt;
	}


	public void setStatusInt(Integer statusInt) {
		if("on".equals(this.status)){
			this.statusInt = 1;
		}else{
			this.statusInt = 0 ;
		}
	}
    
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(String showOrder) {
		this.showOrder = (StringUtils.isEmpty(showOrder) ? "0" : showOrder);
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(String isRecommend) {
		this.isRecommend = isRecommend;
	}

	public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public String getSourceNickName() {
		return sourceNickName;
	}

	public void setSourceNickName(String sourceNickName) {
		this.sourceNickName = sourceNickName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getOpenUrl() {
		return openUrl;
	}


	public void setOpenUrl(String openUrl) {
		this.openUrl = openUrl;
	}


	public String getSpecialImage() {
		return specialImage;
	}


	public void setSpecialImage(String specialImage) {
		this.specialImage = specialImage;
	}


	public String getSpecialImageUrl() {
		return specialImageUrl;
	}


	public void setSpecialImageUrl(String specialImageUrl) {
		this.specialImageUrl = specialImageUrl;
	}

}
