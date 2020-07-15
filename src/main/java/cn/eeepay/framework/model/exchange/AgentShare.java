package cn.eeepay.framework.model.exchange;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/5/15/015.
 * @author  liuks
 * 代理分润详情
 * 对应表 rdmp_agent_share_detail
 */
public class AgentShare {

    private Long id;

    private String orderNo;//关联订单号

    private String agentNo;//代理商编号

    private String agentName;//代理商名称

    private String shareGrade;//分润级别

    private BigDecimal shareAmount;//分润金额

    private BigDecimal totalShareAmount;//当前订单的总分润金额

    private String merNo;//贡献人商户号

    private String shareRate;//分润比例

    private Date createTime;//创建时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getShareGrade() {
        return shareGrade;
    }

    public void setShareGrade(String shareGrade) {
        this.shareGrade = shareGrade;
    }

    public BigDecimal getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(BigDecimal shareAmount) {
        this.shareAmount = shareAmount;
    }

    public BigDecimal getTotalShareAmount() {
        return totalShareAmount;
    }

    public void setTotalShareAmount(BigDecimal totalShareAmount) {
        this.totalShareAmount = totalShareAmount;
    }

    public String getMerNo() {
        return merNo;
    }

    public void setMerNo(String merNo) {
        this.merNo = merNo;
    }

    public String getShareRate() {
        return shareRate;
    }

    public void setShareRate(String shareRate) {
        this.shareRate = shareRate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
