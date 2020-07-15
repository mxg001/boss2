package cn.eeepay.framework.model;

import java.util.Date;

public class TSysOption {

    private int id;

    private  int optionGroupId;

    private int subOptionGroupId;

    private String code;

    private String description;

    private String name;

    private int  rank;

    private int enabled;

    private String createdBy;

    private  String updateBy;

    private Date dateCreated;

    private Date dateUpdated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOptionGroupId() {
        return optionGroupId;
    }

    public void setOptionGroupId(int optionGroupId) {
        this.optionGroupId = optionGroupId;
    }

    public int getSubOptionGroupId() {
        return subOptionGroupId;
    }

    public void setSubOptionGroupId(int subOptionGroupId) {
        this.subOptionGroupId = subOptionGroupId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

