package cn.eeepay.framework.model;

import java.util.Date;

/**
 * superbank.t_sys_option
 */
public class SysOption {
    private Long id;

    private Long optionGroupId;

    private Long subOptionGroupId;

    private String code;

    private String name;

    private String description;

    private Long rank;

    private Boolean enabled;

    private String createdBy;

    private String updatedBy;

    private Date dateCreated;

    private Date dateUpdated;

    private String optionGroupCode;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOptionGroupId() {
        return optionGroupId;
    }

    public void setOptionGroupId(Long optionGroupId) {
        this.optionGroupId = optionGroupId;
    }

    public Long getSubOptionGroupId() {
        return subOptionGroupId;
    }

    public void setSubOptionGroupId(Long subOptionGroupId) {
        this.subOptionGroupId = subOptionGroupId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy == null ? null : updatedBy.trim();
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

	public String getOptionGroupCode() {
		return optionGroupCode;
	}

	public void setOptionGroupCode(String optionGroupCode) {
		this.optionGroupCode = optionGroupCode;
	}
    
    
}