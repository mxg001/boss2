package cn.eeepay.framework.model;

import java.util.Date;

/**
 * Created by Administrator on 2019/9/10/010.
 * @author liuks
 * 审核统计
 */
public class AuditorCountInfo {

    private Integer id;//用户id
    private String userName;
    private Integer allCount;//审核数量
    private Integer successNum;//审核成功
    private Integer successNum1;
    private Integer successNum2;
    private Integer successNum3;

    private Integer failureNum;//审核失败
    private Integer failureNum1;
    private Integer failureNum2;
    private Integer failureNum3;

    private Integer notAudited;//待审核
    private Integer notAudited1;
    private Integer notAudited2;
    private Integer notAudited3;

    private Integer auditorId;//审核人
    private String auditorName;//审核人姓名
    private Date sTime;
    private Date eTime;

    private Long bpId;
    private String bpName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    public Integer getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(Integer successNum) {
        this.successNum = successNum;
    }

    public Integer getSuccessNum1() {
        return successNum1;
    }

    public void setSuccessNum1(Integer successNum1) {
        this.successNum1 = successNum1;
    }

    public Integer getSuccessNum2() {
        return successNum2;
    }

    public void setSuccessNum2(Integer successNum2) {
        this.successNum2 = successNum2;
    }

    public Integer getSuccessNum3() {
        return successNum3;
    }

    public void setSuccessNum3(Integer successNum3) {
        this.successNum3 = successNum3;
    }

    public Integer getFailureNum() {
        return failureNum;
    }

    public void setFailureNum(Integer failureNum) {
        this.failureNum = failureNum;
    }

    public Integer getFailureNum1() {
        return failureNum1;
    }

    public void setFailureNum1(Integer failureNum1) {
        this.failureNum1 = failureNum1;
    }

    public Integer getFailureNum2() {
        return failureNum2;
    }

    public void setFailureNum2(Integer failureNum2) {
        this.failureNum2 = failureNum2;
    }

    public Integer getFailureNum3() {
        return failureNum3;
    }

    public void setFailureNum3(Integer failureNum3) {
        this.failureNum3 = failureNum3;
    }

    public Integer getNotAudited() {
        return notAudited;
    }

    public void setNotAudited(Integer notAudited) {
        this.notAudited = notAudited;
    }

    public Integer getNotAudited1() {
        return notAudited1;
    }

    public void setNotAudited1(Integer notAudited1) {
        this.notAudited1 = notAudited1;
    }

    public Integer getNotAudited2() {
        return notAudited2;
    }

    public void setNotAudited2(Integer notAudited2) {
        this.notAudited2 = notAudited2;
    }

    public Integer getNotAudited3() {
        return notAudited3;
    }

    public void setNotAudited3(Integer notAudited3) {
        this.notAudited3 = notAudited3;
    }

    public Integer getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(Integer auditorId) {
        this.auditorId = auditorId;
    }

    public Date getsTime() {
        return sTime;
    }

    public void setsTime(Date sTime) {
        this.sTime = sTime;
    }

    public Date geteTime() {
        return eTime;
    }

    public void seteTime(Date eTime) {
        this.eTime = eTime;
    }

    public Long getBpId() {
        return bpId;
    }

    public void setBpId(Long bpId) {
        this.bpId = bpId;
    }

    public String getBpName() {
        return bpName;
    }

    public void setBpName(String bpName) {
        this.bpName = bpName;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }
}
