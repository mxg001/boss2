package cn.eeepay.framework.model.cusSms;


import java.util.Date;

/**
 * @author liuks
 * 客服投诉下发-短信模板
 * 对应表 cus_sms_template
 */
public class CusSmsTemplate {

    private Integer id;
    private String department;//部门
    private String type;//短信类型
    private String template;//短信模板
    private String status;//状态 1正常 2删除
    private String createOperator;//创建人
    private String lastUpdateOperator;//最后修改人
    private Date createTime;//创建时间
    private Date createTimeBegin;
    private Date createTimeEnd;
    private Date lastUpdateTime;//最后修改时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateOperator() {
        return createOperator;
    }

    public void setCreateOperator(String createOperator) {
        this.createOperator = createOperator;
    }

    public String getLastUpdateOperator() {
        return lastUpdateOperator;
    }

    public void setLastUpdateOperator(String lastUpdateOperator) {
        this.lastUpdateOperator = lastUpdateOperator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
