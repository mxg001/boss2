package cn.eeepay.framework.model.risk;

import java.util.Date;

/**
 * Created by Administrator on 2018/9/11/011.
 * @author  liuks
 * 调单操作日志
 * 对应表
 * survey_order_log
 */
public class SurveyOrderLog {

    private Integer id;
    private String orderNo;//调单编号
    private String operateType;//操作类型 0添加调单,1回退，2处理，3催单，4删除，5添加扣款，6标注扣款处理，7标注下发处理
    private String operator;//操作人
    private String operateTable;//操作表
    private String preValue;//操作前的值
    private String afterValue;//操作后的值
    private String operateDetail;//日志操作详细
    private Date operateTime;//操作时间
    private String bak1;
    private String bak2;

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

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperateTable() {
        return operateTable;
    }

    public void setOperateTable(String operateTable) {
        this.operateTable = operateTable;
    }

    public String getPreValue() {
        return preValue;
    }

    public void setPreValue(String preValue) {
        this.preValue = preValue;
    }

    public String getAfterValue() {
        return afterValue;
    }

    public void setAfterValue(String afterValue) {
        this.afterValue = afterValue;
    }

    public String getOperateDetail() {
        return operateDetail;
    }

    public void setOperateDetail(String operateDetail) {
        this.operateDetail = operateDetail;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getBak1() {
        return bak1;
    }

    public void setBak1(String bak1) {
        this.bak1 = bak1;
    }

    public String getBak2() {
        return bak2;
    }

    public void setBak2(String bak2) {
        this.bak2 = bak2;
    }
}
