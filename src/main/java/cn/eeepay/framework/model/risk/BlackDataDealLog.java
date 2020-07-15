package cn.eeepay.framework.model.risk;

/**
 * @author MXG
 * create 2018/12/24
 */
public class BlackDataDealLog {
    private String id;
    private String origOrderNo;
    private String operateType; //0添加黑名单资料信息 1风控回复处理 2风控添加备注
    private String operator;
    private String operateTable;
    private String preValue;
    private String afterValue;
    private String operateDetail;
    private String operateTime;

    public BlackDataDealLog() {
    }

    public BlackDataDealLog(String origOrderNo, String operateType, String operator, String operateTable, String preValue, String afterValue) {
        this.origOrderNo = origOrderNo;
        this.operateType = operateType;
        this.operator = operator;
        this.operateTable = operateTable;
        this.preValue = preValue;
        this.afterValue = afterValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrigOrderNo() {
        return origOrderNo;
    }

    public void setOrigOrderNo(String origOrderNo) {
        this.origOrderNo = origOrderNo;
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

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }
}
