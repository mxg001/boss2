package cn.eeepay.framework.model.risk;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/9/14/014.
 * @author  liuks
 * 标注操作记录表
 */
public class DeductAddInfo {

    private String orderNo;//调单号
    private Integer staAll;//操作状态 1标注上游 2标注商户 3标注代理商  4处理
    private Integer sta;//选中页签 0,1
    private String selectSta;//处理状态
    private String remark;//备注
    private BigDecimal amoutOne;//金额1
    private Date time;//时间
    private BigDecimal amoutTwo;//金额2

    private String operatorType;//操作类型
    private  String operator;//操作人
    private String logSta;//日志操作状态
    private String msg;//反馈信息

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getStaAll() {
        return staAll;
    }

    public void setStaAll(Integer staAll) {
        this.staAll = staAll;
    }

    public Integer getSta() {
        return sta;
    }

    public void setSta(Integer sta) {
        this.sta = sta;
    }

    public String getSelectSta() {
        return selectSta;
    }

    public void setSelectSta(String selectSta) {
        this.selectSta = selectSta;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getAmoutOne() {
        return amoutOne;
    }

    public void setAmoutOne(BigDecimal amoutOne) {
        this.amoutOne = amoutOne;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public BigDecimal getAmoutTwo() {
        return amoutTwo;
    }

    public void setAmoutTwo(BigDecimal amoutTwo) {
        this.amoutTwo = amoutTwo;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getLogSta() {
        return logSta;
    }

    public void setLogSta(String logSta) {
        this.logSta = logSta;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
