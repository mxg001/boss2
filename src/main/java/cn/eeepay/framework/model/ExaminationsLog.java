package cn.eeepay.framework.model;

import java.util.Date;

/**
 * table  examinations_log
 * desc  商户审核记录表
 */
public class ExaminationsLog {
    private Integer id;

    private String itemNo;

    private String bpId;

    private String openStatus;

    private String examinationOpinions;

    private String operator;

    private Date createTime;
    
    private String realName;

    private String merchantType;

    public Integer getExamineType() {
        return examineType;
    }

    public void setExamineType(Integer examineType) {
        this.examineType = examineType;
    }

    private Integer examineType;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBpId() {
        return bpId;
    }

    public void setBpId(String bpId) {
        this.bpId = bpId == null ? null : bpId.trim();
    }

    public String getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(String openStatus) {
        this.openStatus = openStatus == null ? null : openStatus.trim();
    }

    public String getExaminationOpinions() {
        return examinationOpinions;
    }

    public void setExaminationOpinions(String examinationOpinions) {
        this.examinationOpinions = examinationOpinions == null ? null : examinationOpinions.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }
}