package cn.eeepay.framework.model.luckDraw;

import java.util.Date;

/**
 * Created by Administrator on 2018/11/7/007.
 * @author  liuks
 * 访问数据统计
 * 对应表 luck_report
 */
public class AccessCount {

    private Integer id;
    private Date reqDate;//日期
    private Date reqDateBegin;
    private Date reqDateEnd;
    private Integer reqSole;//独立访问
    private Integer reqCount;//访问量
    private Integer  reqPerson;//参与人次
    private Integer  reqMer;//参与商户

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getReqDate() {
        return reqDate;
    }

    public void setReqDate(Date reqDate) {
        this.reqDate = reqDate;
    }

    public Integer getReqSole() {
        return reqSole;
    }

    public void setReqSole(Integer reqSole) {
        this.reqSole = reqSole;
    }

    public Integer getReqCount() {
        return reqCount;
    }

    public void setReqCount(Integer reqCount) {
        this.reqCount = reqCount;
    }

    public Integer getReqPerson() {
        return reqPerson;
    }

    public void setReqPerson(Integer reqPerson) {
        this.reqPerson = reqPerson;
    }

    public Integer getReqMer() {
        return reqMer;
    }

    public void setReqMer(Integer reqMer) {
        this.reqMer = reqMer;
    }

    public Date getReqDateBegin() {
        return reqDateBegin;
    }

    public void setReqDateBegin(Date reqDateBegin) {
        this.reqDateBegin = reqDateBegin;
    }

    public Date getReqDateEnd() {
        return reqDateEnd;
    }

    public void setReqDateEnd(Date reqDateEnd) {
        this.reqDateEnd = reqDateEnd;
    }
}
