package cn.eeepay.framework.model;

import java.util.Date;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public class SystemInfo {
    private Integer id;

    private String status;

    private Date currentDate;

    private Date parentTransDate;

    private Date nextTransDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public Date getParentTransDate() {
        return parentTransDate;
    }

    public void setParentTransDate(Date parentTransDate) {
        this.parentTransDate = parentTransDate;
    }

    public Date getNextTransDate() {
        return nextTransDate;
    }

    public void setNextTransDate(Date nextTransDate) {
        this.nextTransDate = nextTransDate;
    }
}