package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * table:super_push_config
 * 微创业配置表
 * @author tans
 * @version 创建时间：2017年5月5日 上午9:36:21
 */
public class SuperPushConfig {
    private Integer id;

    private String bpId;//'业务产品ID'

    private String txServiceId;//'微创业提现服务ID'

    private String appAgentNoList;//'自定义客户端代理商集合'

    private String messageModule;//'推送短信模板'
    
    private Date createDate;//创建时间
    
    private Date updateDate;//修改时间
    
    private String operator;//操作人
    
    private Integer incentiveFundSwitch;//关联鼓励金开关(0关闭1打开) 

    //liuks 微创业二期
    private Integer rewardPointSwitch;//'分享奖励开关(0关闭1打开)'

    private BigDecimal prizesAmount;//'奖励金额'

    private String bonusMessageModule;//'分享奖励推送短信模板'

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

    public String getTxServiceId() {
        return txServiceId;
    }

    public void setTxServiceId(String txServiceId) {
        this.txServiceId = txServiceId == null ? null : txServiceId.trim();
    }

    public String getAppAgentNoList() {
        return appAgentNoList;
    }

    public void setAppAgentNoList(String appAgentNoList) {
        this.appAgentNoList = appAgentNoList == null ? null : appAgentNoList.trim();
    }

    public String getMessageModule() {
        return messageModule;
    }

    public void setMessageModule(String messageModule) {
        this.messageModule = messageModule == null ? null : messageModule.trim();
    }

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Integer getIncentiveFundSwitch() {
		return incentiveFundSwitch;
	}

	public void setIncentiveFundSwitch(Integer incentiveFundSwitch) {
		this.incentiveFundSwitch = incentiveFundSwitch;
	}

    public Integer getRewardPointSwitch() {
        return rewardPointSwitch;
    }

    public void setRewardPointSwitch(Integer rewardPointSwitch) {
        this.rewardPointSwitch = rewardPointSwitch;
    }

    public String getBonusMessageModule() {
        return bonusMessageModule;
    }

    public void setBonusMessageModule(String bonusMessageModule) {
        this.bonusMessageModule = bonusMessageModule;
    }

    public BigDecimal getPrizesAmount() {
        return prizesAmount;
    }

    public void setPrizesAmount(BigDecimal prizesAmount) {
        this.prizesAmount = prizesAmount;
    }
}
