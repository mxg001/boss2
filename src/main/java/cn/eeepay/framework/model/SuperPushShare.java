package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * table:super_push_share
 * 微创业商户分润记录
 * @author tans
 * @version 创建时间：2017年5月10日 上午9:29:40
 */
public class SuperPushShare {
    private Integer id;

    private String orderNo;//订单号

    private BigDecimal transAmount;//交易金额

    private Date transTime;//交易时间

    private String merchantNo;//支付商户

    private String mobile;//交易商户手机号

    private BigDecimal selfShare;//本级商户分润

    private BigDecimal oneShare;//一级商户分润

    private BigDecimal twoShare;//二级商户分润

    private BigDecimal threeShare;//三级商户分润

    private String selfRule;//本级分润公式

    private String oneRule;//一级商户分润公式

    private String twoRule;//二级商户分润公式

    private String threeRule;//三级商户分润公式
    
    private String agentNode;//交易商户直属代理商node
    
    private String shareType;//0: 一级代理商分润, 1: 直属代理商分润, 2 上一级商户 3 上二级商户 4 上三级商户
    
    private String shareNo;//分润的商户/代理商编号
    
    private BigDecimal shareAmount;//分润金额
    
    private BigDecimal shareRate;//分润费率
    
    private String shareStatus;//分润入账状态,0 未入账, 1 入账, 2 入账失败
    
    private Date shareTime;//分润入账时间
    
    private Date createTime;//分润创建时间
    
    private String collectionStatus;//汇总状态(NOCOLLECTION未汇总,COLLECTIONED已汇总)
    
    //额外属性
    private String level;//商户级别
    private String merchantName;//商户名称
    private Date transTimeStart;//查询交易起始时间
    private Date transTimeEnd;//查询交易结束时间
    private BigDecimal share;//贡献的分润
    private String rule;//分润规则
    private Date createTimeStart;//查询分润创建起始时间
    private Date createTimeEnd;//查询分润创建结束时间
    private Date shareTimeStart;//查询分润汇总起始时间
    private Date shareTimeEnd;//查询分润汇总结束时间
    private String shareName;//分润商户/代理商名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public BigDecimal getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}

	public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo == null ? null : merchantNo.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public BigDecimal getSelfShare() {
        return selfShare;
    }

    public void setSelfShare(BigDecimal selfShare) {
        this.selfShare = selfShare;
    }

    public BigDecimal getOneShare() {
        return oneShare;
    }

    public void setOneShare(BigDecimal oneShare) {
        this.oneShare = oneShare;
    }

    public BigDecimal getTwoShare() {
        return twoShare;
    }

    public void setTwoShare(BigDecimal twoShare) {
        this.twoShare = twoShare;
    }

    public BigDecimal getThreeShare() {
        return threeShare;
    }

    public void setThreeShare(BigDecimal threeShare) {
        this.threeShare = threeShare;
    }

    public String getSelfRule() {
        return selfRule;
    }

    public void setSelfRule(String selfRule) {
        this.selfRule = selfRule == null ? null : selfRule.trim();
    }

    public String getOneRule() {
        return oneRule;
    }

    public void setOneRule(String oneRule) {
        this.oneRule = oneRule == null ? null : oneRule.trim();
    }

    public String getTwoRule() {
        return twoRule;
    }

    public void setTwoRule(String twoRule) {
        this.twoRule = twoRule == null ? null : twoRule.trim();
    }

    public String getThreeRule() {
        return threeRule;
    }

    public void setThreeRule(String threeRule) {
        this.threeRule = threeRule == null ? null : threeRule.trim();
    }

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Date getTransTimeStart() {
		return transTimeStart;
	}

	public void setTransTimeStart(Date transTimeStart) {
		this.transTimeStart = transTimeStart;
	}

	public Date getTransTimeEnd() {
		return transTimeEnd;
	}

	public void setTransTimeEnd(Date transTimeEnd) {
		this.transTimeEnd = transTimeEnd;
	}

	public BigDecimal getShare() {
		return share;
	}

	public void setShare(BigDecimal share) {
		this.share = share;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getAgentNode() {
		return agentNode;
	}

	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}

	public String getShareType() {
		return shareType;
	}

	public void setShareType(String shareType) {
		this.shareType = shareType;
	}

	public String getShareNo() {
		return shareNo;
	}

	public void setShareNo(String shareNo) {
		this.shareNo = shareNo;
	}

	public BigDecimal getShareAmount() {
		return shareAmount;
	}

	public void setShareAmount(BigDecimal shareAmount) {
		this.shareAmount = shareAmount;
	}

	public BigDecimal getShareRate() {
		return shareRate;
	}

	public void setShareRate(BigDecimal shareRate) {
		this.shareRate = shareRate;
	}

	public String getShareStatus() {
		return shareStatus;
	}

	public void setShareStatus(String shareStatus) {
		this.shareStatus = shareStatus;
	}

	public Date getShareTime() {
		return shareTime;
	}

	public void setShareTime(Date shareTime) {
		this.shareTime = shareTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCollectionStatus() {
		return collectionStatus;
	}

	public void setCollectionStatus(String collectionStatus) {
		this.collectionStatus = collectionStatus;
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

	public Date getShareTimeStart() {
		return shareTimeStart;
	}

	public void setShareTimeStart(Date shareTimeStart) {
		this.shareTimeStart = shareTimeStart;
	}

	public Date getShareTimeEnd() {
		return shareTimeEnd;
	}

	public void setShareTimeEnd(Date shareTimeEnd) {
		this.shareTimeEnd = shareTimeEnd;
	}

	public String getShareName() {
		return shareName;
	}

	public void setShareName(String shareName) {
		this.shareName = shareName;
	}
	
	
    
}