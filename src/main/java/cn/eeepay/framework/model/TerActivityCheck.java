package cn.eeepay.framework.model;

import java.math.BigInteger;
import java.util.Date;


public class TerActivityCheck {
    private BigInteger id;
    private String sn;
    private int dueDays;
    private int status;
    private Date checkTime;
    private Date updateTime;

    public TerActivityCheck() {
    }

    public TerActivityCheck(String sn, int dueDays, Date checkTime) {
        this.sn = sn;
        this.dueDays = dueDays;
        this.checkTime = checkTime;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getDueDays() {
        return dueDays;
    }

    public void setDueDays(int dueDays) {
        this.dueDays = dueDays;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
