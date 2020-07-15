package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * table:super_push_user
 * 微创业用户表
 * @author tans
 * @version 创建时间：2017年5月10日 上午9:32:40
 */
public class SuperPushUser {
    private String userId;//商户用户ID

    private String merchantNo;//本级商户编号

    private Date createTime;//创建时间

    private String businessNo;//推荐商户的业务编号

    private String recommendedSource;//推荐来源：1是商户链接2是代理商链接

    private String recommendedUserId;//推荐人ID

    private String oneMerchantNo;//上一级商户编号

    private String twoMerchantNo;//上二级商户编号

    private String threeMerchantNo;//上三级商户编号
    
    //冗余字段
    private String mobilephone;//商户手机号
    private String merchantName;//商户名称
    private String oneAgentNo;//一级代理商编号
    private String agentNo;//代理商编号
    private String agentName;//代理商名称
    private BigDecimal totalAmount;//收益总额
    private BigDecimal avaliBalance;//可用余额
    private Date createTimeStart;//注册开始时间
    private Date createTimeEnd;//注册结束时间
    private String status;//商户审核状态
    private String bpId;//业务产品ID
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo == null ? null : merchantNo.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo == null ? null : businessNo.trim();
    }

    public String getRecommendedSource() {
        return recommendedSource;
    }

    public void setRecommendedSource(String recommendedSource) {
        this.recommendedSource = recommendedSource == null ? null : recommendedSource.trim();
    }

    public String getRecommendedUserId() {
        return recommendedUserId;
    }

    public void setRecommendedUserId(String recommendedUserId) {
        this.recommendedUserId = recommendedUserId == null ? null : recommendedUserId.trim();
    }

    public String getOneMerchantNo() {
        return oneMerchantNo;
    }

    public void setOneMerchantNo(String oneMerchantNo) {
        this.oneMerchantNo = oneMerchantNo == null ? null : oneMerchantNo.trim();
    }

    public String getTwoMerchantNo() {
        return twoMerchantNo;
    }

    public void setTwoMerchantNo(String twoMerchantNo) {
        this.twoMerchantNo = twoMerchantNo == null ? null : twoMerchantNo.trim();
    }

    public String getThreeMerchantNo() {
        return threeMerchantNo;
    }

    public void setThreeMerchantNo(String threeMerchantNo) {
        this.threeMerchantNo = threeMerchantNo == null ? null : threeMerchantNo.trim();
    }

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public BigDecimal getTotalAmount() {
		if(this.totalAmount==null){
			return BigDecimal.ZERO;
		}
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getOneAgentNo() {
		return oneAgentNo;
	}

	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}

	public BigDecimal getAvaliBalance() {
		return avaliBalance;
	}

	public void setAvaliBalance(BigDecimal avaliBalance) {
		this.avaliBalance = avaliBalance;
	}

	public Date getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(Date createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBpId() {
		return bpId;
	}

	public void setBpId(String bpId) {
		this.bpId = bpId;
	}

}