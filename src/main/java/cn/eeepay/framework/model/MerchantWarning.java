package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * merchant_warning_service
 * 商户预警服务表
 */
public class MerchantWarning {

	private Integer id;
	private String warningType;
	private String warningName;
	private String teamId;
	private Integer isUsed;
	private String warningImg;
	private String warningTitle;
	private String warningUrl;
	private Integer noTranDay;
	private BigDecimal tranSlideRate;
	private Date createTime;
	private String remark;

	private String teamName;

	private String warningImgUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWarningType() {
		return warningType;
	}

	public void setWarningType(String warningType) {
		this.warningType = warningType;
	}

	public String getWarningName() {
		return warningName;
	}

	public void setWarningName(String warningName) {
		this.warningName = warningName;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public Integer getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Integer isUsed) {
		this.isUsed = isUsed;
	}

	public String getWarningImg() {
		return warningImg;
	}

	public void setWarningImg(String warningImg) {
		this.warningImg = warningImg;
	}

	public String getWarningTitle() {
		return warningTitle;
	}

	public void setWarningTitle(String warningTitle) {
		this.warningTitle = warningTitle;
	}

	public String getWarningUrl() {
		return warningUrl;
	}

	public void setWarningUrl(String warningUrl) {
		this.warningUrl = warningUrl;
	}

	public Integer getNoTranDay() {
		return noTranDay;
	}

	public void setNoTranDay(Integer noTranDay) {
		this.noTranDay = noTranDay;
	}

	public BigDecimal getTranSlideRate() {
		return tranSlideRate;
	}

	public void setTranSlideRate(BigDecimal tranSlideRate) {
		this.tranSlideRate = tranSlideRate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getWarningImgUrl() {
		return warningImgUrl;
	}

	public void setWarningImgUrl(String warningImgUrl) {
		this.warningImgUrl = warningImgUrl;
	}
}
