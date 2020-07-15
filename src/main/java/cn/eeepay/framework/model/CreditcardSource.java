package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 信用卡银行
 * @author Administrator
 *
 */
public class CreditcardSource {

	private Long id;

	private String code;//银行编码
	
	private String bankName;//银行名称
	
	private String showOrder;//排序
	
	private String status;//是否上架，off否，on是
	private Integer statusInt;//是否上架，0否，1是

	private String showLogo;//显示logo
	private String showLogoUrl;//显示logo

	private String h5Link;//申请材料说明的图片
	private String h5LinkUrl;//申请材料说明的图片

	private String sendLink;//申请h5
	
	private String bankBonus;//银行返还总奖金

	private String bankNickName;//银行别称

	private String source;//来源

	private String slogan;//宣传语

	private BigDecimal cardBonus;//发卡奖金

	private BigDecimal firstBrushBonus;//首刷奖金

	private Long approveStatus;//是否秒批，0否，1是

	private Long autoShareStatus;//是否导入自动分润，0否，1是

	private Long batchStatus;//查询是否秒结，0否，1是

	private String shareRemark;//分润规则备注

	private String specialPosition;//特别推荐位置

	private String specialImage;//特别推荐图片
	private String specialImageUrl;//特别推荐图片

	private Date updateTime;//修改时间

	private Date createTime;//创建时间

	private String updateBy;//操作人

	private String ruleCode;//导入匹配规则编码

//	private String cardType;//卡类型，1普通卡，2校园卡

//	private String cardMessage;//办卡提示内容

	
	private String cardActiveStatus;//发卡奖金是否要求卡片激活，有是与否，默认为否
	
	private String accessMethods;//“接入方式”有H5和API两种形式，默认H5
	private String sendApiLink;//申请API接口，H5链接是sendLink
	
	private String quickCardStatus;//是否展示极速办卡标签：0否，1是
	
	private String specialLabel;//为银行增加特别标签 
	
	/*private String activityImage;//活动图片
	private String activityImageUrl;//活动图片
	
	private String activityLink;//活动链接
*/	
	private String applyCardGuideImg;//办卡攻略图片
	private String applyCardGuideImgUrl;//办卡攻略图片

	public String getCardActiveStatus() {
		return cardActiveStatus;
	}

	public void setCardActiveStatus(String cardActiveStatus) {
		this.cardActiveStatus = cardActiveStatus;
	}

	public String getAccessMethods() {
		return accessMethods;
	}

	public void setAccessMethods(String accessMethods) {
		this.accessMethods = accessMethods;
	}

	public String getSendApiLink() {
		return sendApiLink;
	}

	public void setSendApiLink(String sendApiLink) {
		this.sendApiLink = sendApiLink;
	}

	public String getQuickCardStatus() {
		return quickCardStatus;
	}

	public void setQuickCardStatus(String quickCardStatus) {
		this.quickCardStatus = quickCardStatus;
	}

	public String getSpecialLabel() {
		return specialLabel;
	}

	public void setSpecialLabel(String specialLabel) {
		this.specialLabel = specialLabel;
	}


	public String getApplyCardGuideImg() {
		return applyCardGuideImg;
	}

	public void setApplyCardGuideImg(String applyCardGuideImg) {
		this.applyCardGuideImg = applyCardGuideImg;
	}

	
	public String getApplyCardGuideImgUrl() {
		return applyCardGuideImgUrl;
	}

	public void setApplyCardGuideImgUrl(String applyCardGuideImgUrl) {
		this.applyCardGuideImgUrl = applyCardGuideImgUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(String showOrder) {
		this.showOrder = showOrder;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getShowLogo() {
		return showLogo;
	}

	public void setShowLogo(String showLogo) {
		this.showLogo = showLogo;
	}

	public String getH5Link() {
		return h5Link;
	}

	public void setH5Link(String h5Link) {
		this.h5Link = h5Link;
	}

	public String getSendLink() {
		return sendLink;
	}

	public void setSendLink(String sendLink) {
		this.sendLink = sendLink;
	}

	public String getBankBonus() {
		return bankBonus;
	}

	public void setBankBonus(String bankBonus) {
		this.bankBonus = bankBonus;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBankNickName() {
		return bankNickName;
	}

	public void setBankNickName(String bankNickName) {
		this.bankNickName = bankNickName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public BigDecimal getCardBonus() {
		return cardBonus;
	}

	public void setCardBonus(BigDecimal cardBonus) {
		this.cardBonus = cardBonus;
	}

	public BigDecimal getFirstBrushBonus() {
		return firstBrushBonus;
	}

	public void setFirstBrushBonus(BigDecimal firstBrushBonus) {
		this.firstBrushBonus = firstBrushBonus;
	}

	public Integer getStatusInt() {
		return statusInt;
	}

	public void setStatusInt(Integer statusInt) {
		this.statusInt = statusInt;
	}

	public Long getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Long approveStatus) {
		this.approveStatus = approveStatus;
	}

	public Long getAutoShareStatus() {
		return autoShareStatus;
	}

	public void setAutoShareStatus(Long autoShareStatus) {
		this.autoShareStatus = autoShareStatus;
	}

	public String getShareRemark() {
		return shareRemark;
	}

	public void setShareRemark(String shareRemark) {
		this.shareRemark = shareRemark;
	}

	public String getSpecialPosition() {
		return specialPosition;
	}

	public void setSpecialPosition(String specialPosition) {
		this.specialPosition = specialPosition;
	}

	public String getSpecialImage() {
		return specialImage;
	}

	public void setSpecialImage(String specialImage) {
		this.specialImage = specialImage;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getShowLogoUrl() {
        return showLogoUrl;
    }

    public void setShowLogoUrl(String showLogoUrl) {
        this.showLogoUrl = showLogoUrl;
    }

    public String getSpecialImageUrl() {
        return specialImageUrl;
    }

    public void setSpecialImageUrl(String specialImageUrl) {
        this.specialImageUrl = specialImageUrl;
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

	public Long getBatchStatus() {
		return batchStatus;
	}

	public void setBatchStatus(Long batchStatus) {
		this.batchStatus = batchStatus;
	}
}
