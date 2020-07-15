package cn.eeepay.framework.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/18/018.
 * @author liuks
 * 超级还款设置实体
 * 对应表 super_collection_switch
 */

public class SuperCollection {
    private int id;//id

    private String number;//编码

    private String startTime;//'交易开始时间'

    private String endTime;//'交易结束时间'

    private BigDecimal dayLines;//每日额度

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//'创建时间'

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;//'最后修改时间'

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getDayLines() {
        return dayLines;
    }

    public void setDayLines(BigDecimal dayLines) {
        this.dayLines = dayLines;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
