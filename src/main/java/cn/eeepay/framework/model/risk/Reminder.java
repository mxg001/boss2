package cn.eeepay.framework.model.risk;

import java.util.Date;

/**
 * Created by Administrator on 2018/9/11/011.
 * @author liuks
 * 催单记录
 * 对应表 survey_urge_record
 */
public class Reminder {

    private  Integer id;
    private String orderNo;//调单单号
    private String agentNode;//代理商编号节点
    private String haveLook;//是否浏览过  0没有浏览 1已浏览 2调单已回退
    private String operator;//操作人
    private Date createTime;//创建时间
    private Date lastUpdateTime;//数据最后更新时间
    private  Integer rowNo;//排序行号

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
        this.orderNo = orderNo;
    }

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode;
    }

    public String getHaveLook() {
        return haveLook;
    }

    public void setHaveLook(String haveLook) {
        this.haveLook = haveLook;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getRowNo() {
        return rowNo;
    }

    public void setRowNo(Integer rowNo) {
        this.rowNo = rowNo;
    }
}
