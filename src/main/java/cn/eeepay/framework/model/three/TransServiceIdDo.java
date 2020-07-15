package cn.eeepay.framework.model.three;

/**
 * 
 * @author qiujian
 *
 */
import java.math.BigDecimal;

public class TransServiceIdDo {
	private String serviceId;
	private BigDecimal transAmountSum;
	private String cardType;
	// 笔数
	private Long num;
	private String agentNode;

	public BigDecimal getTransAmountSum() {
		return transAmountSum;
	}

	public void setTransAmountSum(BigDecimal transAmountSum) {
		this.transAmountSum = transAmountSum;
	}

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public String getAgentNode() {
		return agentNode;
	}

	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

}
