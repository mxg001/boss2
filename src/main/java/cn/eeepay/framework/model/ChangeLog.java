package cn.eeepay.framework.model;

/**
 * @author MXG
 * create 2018/06/29
 */
public class ChangeLog {
    private Integer id;
    private String changePre;
    private String changeAfter;
    private String remark;
    private String createTime;
    private String operater;
    private String operMethod;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChangePre() {
        return changePre;
    }

    public void setChangePre(String changePre) {
        this.changePre = changePre;
    }

    public String getChangeAfter() {
        return changeAfter;
    }

    public void setChangeAfter(String changeAfter) {
        this.changeAfter = changeAfter;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOperater() {
        return operater;
    }

    public void setOperater(String operater) {
        this.operater = operater;
    }

    public String getOperMethod() {
        return operMethod;
    }

    public void setOperMethod(String operMethod) {
        this.operMethod = operMethod;
    }
}
