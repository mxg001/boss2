package cn.eeepay.framework.model.capitalInsurance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/7/23/023.
 * 资金保险配置表
 * 对应表 zjx_safe_config
 */
public class SafeConfig {

    private Integer id;
    private String proCode;//产品编码
    private String proName;//产品名称
    private String bxUnit;//保险通道
    private String bxUnitName;//保险通道名称
    private String routeScale;//通道路由比例
    private String newRouteScale;//通道路由比例,编辑显示
    private Integer t0Time;//T0约定到账时间(单位:小时)
    private Integer t1Time;//T1约定到账时间(单位:小时)
    private String phone;//理赔电话
    private Integer status;//App默认勾选状态0否1是
    private BigDecimal agentShare;//代理商分润(只对一级代理商生效,单位%)
    private Date createTime;//创建时间
    private Date lastUpdateTime;//最后修改时间

    public String getBxUnit() {
        return bxUnit;
    }

    public void setBxUnit(String bxUnit) {
        this.bxUnit = bxUnit;
    }


    public String getBxUnitName() {
        return bxUnitName;
    }

    public void setBxUnitName(String bxUnitName) {
        this.bxUnitName = bxUnitName;
    }

    public String getRouteScale() {
        return routeScale;
    }

    public void setRouteScale(String routeScale) {
        this.routeScale = routeScale;
    }

    public String getNewRouteScale() {
        return newRouteScale;
    }

    public void setNewRouteScale(String newRouteScale) {
        this.newRouteScale = newRouteScale;
    }

    private List<SafeLadder> safeLadder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProCode() {
        return proCode;
    }

    public void setProCode(String proCode) {
        this.proCode = proCode;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public Integer getT0Time() {
        return t0Time;
    }

    public void setT0Time(Integer t0Time) {
        this.t0Time = t0Time;
    }

    public Integer getT1Time() {
        return t1Time;
    }

    public void setT1Time(Integer t1Time) {
        this.t1Time = t1Time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getAgentShare() {
        return agentShare;
    }

    public void setAgentShare(BigDecimal agentShare) {
        this.agentShare = agentShare;
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

    public List<SafeLadder> getSafeLadder() {
        return safeLadder;
    }

    public void setSafeLadder(List<SafeLadder> safeLadder) {
        this.safeLadder = safeLadder;
    }
}
