package cn.eeepay.framework.model;

import java.util.Date;

/**
 * @author MXG
 * create 2018/06/29
 */
public class OperateLog {
    private Integer id;
    private String operator;
    private String operateCode;
    private String operateTable;
    private String preValue;
    private String afterValue;
    private String operateDetail;
    private Date operateTime;
    private String operateFrom;
    private String beOperator;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(String operateCode) {
        this.operateCode = operateCode;
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

    public String getOperateFrom() {
        return operateFrom;
    }

    public void setOperateFrom(String operateFrom) {
        this.operateFrom = operateFrom;
    }

    public String getBeOperator() {
        return beOperator;
    }

    public void setBeOperator(String beOperator) {
        this.beOperator = beOperator;
    }
}
