package cn.eeepay.framework.model;

import java.util.Date;

public class AcqTerminalStore {
    private Integer id;

    private String acqEnname;

    private String unionMerNo;

    private String merchantNo;

    private String terNo;

    private String tmk1;

    private String tmk1Ck;

    private String tmk2;

    private String tmk2Ck;

    private Integer status;

    private String sn;

    private String mbpId;

    private Date createTime;

    private Date lastUpdateTime;

    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAcqEnname() {
        return acqEnname;
    }

    public void setAcqEnname(String acqEnname) {
        this.acqEnname = acqEnname;
    }

    public String getUnionMerNo() {
        return unionMerNo;
    }

    public void setUnionMerNo(String unionMerNo) {
        this.unionMerNo = unionMerNo == null ? null : unionMerNo.trim();
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo == null ? null : merchantNo.trim();
    }

    public String getTerNo() {
        return terNo;
    }

    public void setTerNo(String terNo) {
        this.terNo = terNo == null ? null : terNo.trim();
    }

    public String getTmk1() {
        return tmk1;
    }

    public void setTmk1(String tmk1) {
        this.tmk1 = tmk1 == null ? null : tmk1.trim();
    }

    public String getTmk1Ck() {
        return tmk1Ck;
    }

    public void setTmk1Ck(String tmk1Ck) {
        this.tmk1Ck = tmk1Ck == null ? null : tmk1Ck.trim();
    }

    public String getTmk2() {
        return tmk2;
    }

    public void setTmk2(String tmk2) {
        this.tmk2 = tmk2 == null ? null : tmk2.trim();
    }

    public String getTmk2Ck() {
        return tmk2Ck;
    }

    public void setTmk2Ck(String tmk2Ck) {
        this.tmk2Ck = tmk2Ck == null ? null : tmk2Ck.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn == null ? null : sn.trim();
    }

    public String getMbpId() {
        return mbpId;
    }

    public void setMbpId(String mbpId) {
        this.mbpId = mbpId == null ? null : mbpId.trim();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }
}