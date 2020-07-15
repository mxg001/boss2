package cn.eeepay.framework.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * table banner_info
 * desc Banner信息表
 * @author tans
 */
public class BannerInfo {

    private Long bannerId;

    private String bannerName;

    private Integer weight;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date onlineTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date offlineTime;

    private String teamId;

    private String agentNo;

    private String bannerContent;

    private Integer bannerStatus;

    private String bannerAttachment;
    
    private String bannerAttachmentUrl;

    private String bannerLink;
    
    private String teamName;
    
    private String teamType;
    
    private String agentName;
    
    private String appNo;
    
    private Integer bannerPosition;
    
    private String appName;
    
    public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppNo() {
		return appNo;
	}

	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}

	public Integer getBannerPosition() {
		return bannerPosition;
	}

	public void setBannerPosition(Integer bannerPosition) {
		this.bannerPosition = bannerPosition;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getTeamType() {
		return teamType;
	}

	public void setTeamType(String teamType) {
		this.teamType = teamType == null ? null : teamType.trim();
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName == null ? null : teamName.trim();
	}

	public String getTeamId() {
		return teamId;
	}
	
	 public void setTeamId(String teamId) {
	        this.teamId = teamId == null ? null : teamId.trim();
	    }

    public Long getBannerId() {
        return bannerId;
    }

    public void setBannerId(Long bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName == null ? null : bannerName.trim();
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Date getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(Date offlineTime) {
        this.offlineTime = offlineTime;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo == null ? null : agentNo.trim();
    }

    public String getBannerContent() {
        return bannerContent;
    }

    public void setBannerContent(String bannerContent) {
        this.bannerContent = bannerContent == null ? null : bannerContent.trim();
    }

    public Integer getBannerStatus() {
        return bannerStatus;
    }

    public void setBannerStatus(Integer bannerStatus) {
        this.bannerStatus = bannerStatus;
    }

    public String getBannerAttachment() {
        return bannerAttachment;
    }

    public void setBannerAttachment(String bannerAttachment) {
        this.bannerAttachment = bannerAttachment == null ? null : bannerAttachment.trim();
    }

    public String getBannerLink() {
        return bannerLink;
    }

    public void setBannerLink(String bannerLink) {
        this.bannerLink = bannerLink == null ? null : bannerLink.trim();
    }

	public String getBannerAttachmentUrl() {
		return bannerAttachmentUrl;
	}

	public void setBannerAttachmentUrl(String bannerAttachmentUrl) {
		this.bannerAttachmentUrl = bannerAttachmentUrl;
	}
    
    
}