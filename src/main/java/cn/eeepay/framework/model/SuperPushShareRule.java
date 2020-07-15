package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * table:super_push_share_rule
 * 微创业分润规则表
 * @author tans
 * @version 创建时间：2017年5月5日 上午9:36:45
 */
public class SuperPushShareRule {
    private Integer id;
    
    private String bpId;//'业务产品ID'

    private String serviceId;//'服务ID'

    private String serviceType;//'服务类型'

    private Integer profitType;//'本级分润方式:1-每笔固定收益额；2-每笔固定收益率'

    private BigDecimal perFixIncome;//'本级每笔固定收益额'

    private BigDecimal perFixInrate;//'本级每笔固定收益率'

    private Integer profitType1;//'一级分润方式:1-每笔固定收益额；2-每笔固定收益率'

    private BigDecimal perFixIncome1;//'一级每笔固定收益额'

    private BigDecimal perFixInrate1;//'一级每笔固定收益率'

    private Integer profitType2;//'二级分润方式:1-每笔固定收益额；2-每笔固定收益率'

    private BigDecimal perFixIncome2;//'二级每笔固定收益额'

    private BigDecimal perFixInrate2;//'二级每笔固定收益率'

    private Integer profitType3;//'三级分润方式:1-每笔固定收益额；2-每笔固定收益率'

    private BigDecimal perFixIncome3;//'三级每笔固定收益额'

    private BigDecimal perFixInrate3;//'三级每笔固定收益率'
    
    private Integer agentProfitType;//'直属代理商分润方式:1-每笔固定收益额；2-每笔固定收益率'

    private BigDecimal agentPerFixIncome;//'直属代理商每笔固定收益额'

    private BigDecimal agentPerFixInrate;//'直属代理商每笔固定收益率'
    
    private Integer oneAgentProfitType;//'一级代理商分润方式:1-每笔固定收益额；2-每笔固定收益率'

    private BigDecimal oneAgentPerFixIncome;//'一级代理商每笔固定收益额'

    private BigDecimal oneAgentPerFixInrate;//'一级代理商每笔固定收益率'
    
    private Date createDate;//创建时间
    
    private Date updateDate;//修改时间
    
    private Integer operator;//操作人
    
    /** 冗余字段 */
    private String serviceName; //服务名称
    
    private String bpName;//业务产品名称

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Integer getProfitType() {
		return profitType;
	}

	public void setProfitType(Integer profitType) {
		this.profitType = profitType;
	}

	public BigDecimal getPerFixIncome() {
		return perFixIncome;
	}

	public void setPerFixIncome(BigDecimal perFixIncome) {
		this.perFixIncome = perFixIncome;
	}

	public BigDecimal getPerFixInrate() {
		return perFixInrate;
	}

	public void setPerFixInrate(BigDecimal perFixInrate) {
		this.perFixInrate = perFixInrate;
	}

	public Integer getProfitType1() {
		return profitType1;
	}

	public void setProfitType1(Integer profitType1) {
		this.profitType1 = profitType1;
	}

	public BigDecimal getPerFixIncome1() {
		return perFixIncome1;
	}

	public void setPerFixIncome1(BigDecimal perFixIncome1) {
		this.perFixIncome1 = perFixIncome1;
	}

	public BigDecimal getPerFixInrate1() {
		return perFixInrate1;
	}

	public void setPerFixInrate1(BigDecimal perFixInrate1) {
		this.perFixInrate1 = perFixInrate1;
	}

	public Integer getProfitType2() {
		return profitType2;
	}

	public void setProfitType2(Integer profitType2) {
		this.profitType2 = profitType2;
	}

	public BigDecimal getPerFixIncome2() {
		return perFixIncome2;
	}

	public void setPerFixIncome2(BigDecimal perFixIncome2) {
		this.perFixIncome2 = perFixIncome2;
	}

	public BigDecimal getPerFixInrate2() {
		return perFixInrate2;
	}

	public void setPerFixInrate2(BigDecimal perFixInrate2) {
		this.perFixInrate2 = perFixInrate2;
	}

	public Integer getProfitType3() {
		return profitType3;
	}

	public void setProfitType3(Integer profitType3) {
		this.profitType3 = profitType3;
	}

	public BigDecimal getPerFixIncome3() {
		return perFixIncome3;
	}

	public void setPerFixIncome3(BigDecimal perFixIncome3) {
		this.perFixIncome3 = perFixIncome3;
	}

	public BigDecimal getPerFixInrate3() {
		return perFixInrate3;
	}

	public void setPerFixInrate3(BigDecimal perFixInrate3) {
		this.perFixInrate3 = perFixInrate3;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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

	public Integer getOperator() {
		return operator;
	}

	public void setOperator(Integer operator) {
		this.operator = operator;
	}

	public String getBpId() {
		return bpId;
	}

	public void setBpId(String bpId) {
		this.bpId = bpId;
	}

	public Integer getAgentProfitType() {
		return agentProfitType;
	}

	public void setAgentProfitType(Integer agentProfitType) {
		this.agentProfitType = agentProfitType;
	}

	public BigDecimal getAgentPerFixIncome() {
		return agentPerFixIncome;
	}

	public void setAgentPerFixIncome(BigDecimal agentPerFixIncome) {
		this.agentPerFixIncome = agentPerFixIncome;
	}

	public BigDecimal getAgentPerFixInrate() {
		return agentPerFixInrate;
	}

	public void setAgentPerFixInrate(BigDecimal agentPerFixInrate) {
		this.agentPerFixInrate = agentPerFixInrate;
	}

	public Integer getOneAgentProfitType() {
		return oneAgentProfitType;
	}

	public void setOneAgentProfitType(Integer oneAgentProfitType) {
		this.oneAgentProfitType = oneAgentProfitType;
	}

	public BigDecimal getOneAgentPerFixIncome() {
		return oneAgentPerFixIncome;
	}

	public void setOneAgentPerFixIncome(BigDecimal oneAgentPerFixIncome) {
		this.oneAgentPerFixIncome = oneAgentPerFixIncome;
	}

	public BigDecimal getOneAgentPerFixInrate() {
		return oneAgentPerFixInrate;
	}

	public void setOneAgentPerFixInrate(BigDecimal oneAgentPerFixInrate) {
		this.oneAgentPerFixInrate = oneAgentPerFixInrate;
	}

	public String getBpName() {
		return bpName;
	}

	public void setBpName(String bpName) {
		this.bpName = bpName;
	}

    
}
