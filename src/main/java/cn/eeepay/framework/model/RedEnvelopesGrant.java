package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/19/019.
 * @author  liuks
 * 红包发放
 * red_orders
 */
public class RedEnvelopesGrant {

    private Long id;//红包id

    private Date createDate;//创建时间
    private Date createDateMin;
    private Date createDateMax;

    private Date expDate;//红包失效时间

    private String pushArea;//发放范围(0三公里1全市2全国3单个定向)

    private String pushType;//发放人类型(0个人发红包、1平台发放红包、2组织发放红包)

    private String receiveType;//接收类型(0单个定下、1群抢)

    private String busType;//业务类型(0个人发红包1新用户红包、2信用卡奖励红包、3贷款奖励红包、4登录红包、5敲门红包)

    private Long orgId;//发放人组织id

    private String orgName;//发放人组织名称

    private String hasProfit;//是否收取佣金(0收取佣金1不收取佣金)

    private Long confId;//红包配置id(对应red_configure表主键)

    private String pushUserCode;//发红包用户编号

    private String pushUserName;//发红包用户姓名

    private String pushUserPhone;//发红包用户手机号

    private String dxUserCode;//单个定向接收用户编号(当类型为单个定向红包时有值)

    private String dxUserName;//单个定向接收用户姓名(当类型为单个定向红包时有值)

    private String dxUserPhone;//单个定向接收用户手机号(当类型为单个定向红包时有值)

    private String status;//红包状态(0发放中1已领完2已到期)

    private String statusRisk;//风控状态(0正常1屏蔽)

    private String statusRecovery;//剩余金额处理状态(0待处理1处理成功2处理失败3处理中)

    private String statusAccount;//记账状态;0待入账；1已记账；2记账失败

    private String recoveryType;//剩余金额处理方式(0原路退回1归平台所有2无需处理)

    private String orderNo;//关联业务订单号,对应订单表订单号,当没有值时填空字符串

    private String payOrderNo;//关联支付订单号对应订单表支付订单号,当没有值时填空字符串

    private String payType;//支付方式(0分润账户余额1微信支付2红包账户余额3内部账户)

    private BigDecimal pushAmount;//红包金额

    private String pushEachAmount;//单个领取金额、范围

    private BigDecimal pushMinConf;//发红包的单个金额阀值(当个人发红包时需要,没有时填0)

    private BigDecimal pushFee;//服务费

    private BigDecimal pushUserAmount;//用户实际领抢金额

    private String oneUserCode;//一级分润用户编号
    private String oneUserType;//一级分润身份
    private BigDecimal oneUserProfit;//一级分润
    private String oneUserName;

    private String twoUserCode;//二级分润用户编号
    private String twoUserType;//二级分润身份
    private BigDecimal twoUserProfit;//二级分润
    private String twoUserName;

    private String thrUserCode;//三级分润用户编号
    private String thrUserType;//三级分润身份
    private BigDecimal thrUserProfit;//三级分润
    private String thrUserName;

    private String fouUserCode;//四级分润用户编号
    private String fouUserType;//四级分润身份
    private BigDecimal fouUserProfit;//四级分润
    private String fouUserName;

    private BigDecimal plateProfit;//平台分润

    private BigDecimal orgProfit;//OEM品牌分润

    private Integer pushNum;//发放个数

    private Integer hadsendNum;//已领红包数

    private Integer discussNum;//评论数

    private Integer goodNum;//点赞数

    private Integer readNum;//阅读数

    private Integer storeNum;//收藏数

    private String pushAddress;//红包发放地点

    private String remark;//红包说明

    private String remarkStatus;//红包说明状态

    private BigDecimal amountCount;//选中记录的总金额

    private Integer sumState;//是否是统计 0，查询 1统计

    private String pushRealName;//发放人昵称

    private BigDecimal plateProfitSum;//平台分润汇总

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateDateMin() {
        return createDateMin;
    }

    public void setCreateDateMin(Date createDateMin) {
        this.createDateMin = createDateMin;
    }

    public Date getCreateDateMax() {
        return createDateMax;
    }

    public void setCreateDateMax(Date createDateMax) {
        this.createDateMax = createDateMax;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public String getPushArea() {
        return pushArea;
    }

    public void setPushArea(String pushArea) {
        this.pushArea = pushArea;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getHasProfit() {
        return hasProfit;
    }

    public void setHasProfit(String hasProfit) {
        this.hasProfit = hasProfit;
    }

    public Long getConfId() {
        return confId;
    }

    public void setConfId(Long confId) {
        this.confId = confId;
    }

    public String getPushUserCode() {
        return pushUserCode;
    }

    public void setPushUserCode(String pushUserCode) {
        this.pushUserCode = pushUserCode;
    }

    public String getPushUserName() {
        return pushUserName;
    }

    public void setPushUserName(String pushUserName) {
        this.pushUserName = pushUserName;
    }

    public String getPushUserPhone() {
        return pushUserPhone;
    }

    public void setPushUserPhone(String pushUserPhone) {
        this.pushUserPhone = pushUserPhone;
    }

    public String getDxUserCode() {
        return dxUserCode;
    }

    public void setDxUserCode(String dxUserCode) {
        this.dxUserCode = dxUserCode;
    }

    public String getDxUserName() {
        return dxUserName;
    }

    public void setDxUserName(String dxUserName) {
        this.dxUserName = dxUserName;
    }

    public String getDxUserPhone() {
        return dxUserPhone;
    }

    public void setDxUserPhone(String dxUserPhone) {
        this.dxUserPhone = dxUserPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusRisk() {
        return statusRisk;
    }

    public void setStatusRisk(String statusRisk) {
        this.statusRisk = statusRisk;
    }

    public String getStatusRecovery() {
        return statusRecovery;
    }

    public void setStatusRecovery(String statusRecovery) {
        this.statusRecovery = statusRecovery;
    }

    public String getStatusAccount() {
        return statusAccount;
    }

    public void setStatusAccount(String statusAccount) {
        this.statusAccount = statusAccount;
    }

    public String getRecoveryType() {
        return recoveryType;
    }

    public void setRecoveryType(String recoveryType) {
        this.recoveryType = recoveryType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public BigDecimal getPushAmount() {
        return pushAmount;
    }

    public void setPushAmount(BigDecimal pushAmount) {
        this.pushAmount = pushAmount;
    }

    public String getPushEachAmount() {
        return pushEachAmount;
    }

    public void setPushEachAmount(String pushEachAmount) {
        this.pushEachAmount = pushEachAmount;
    }

    public BigDecimal getPushMinConf() {
        return pushMinConf;
    }

    public void setPushMinConf(BigDecimal pushMinConf) {
        this.pushMinConf = pushMinConf;
    }

    public BigDecimal getPushFee() {
        return pushFee;
    }

    public void setPushFee(BigDecimal pushFee) {
        this.pushFee = pushFee;
    }

    public BigDecimal getPushUserAmount() {
        return pushUserAmount;
    }

    public void setPushUserAmount(BigDecimal pushUserAmount) {
        this.pushUserAmount = pushUserAmount;
    }

    public String getOneUserCode() {
        return oneUserCode;
    }

    public void setOneUserCode(String oneUserCode) {
        this.oneUserCode = oneUserCode;
    }

    public String getOneUserType() {
        return oneUserType;
    }

    public void setOneUserType(String oneUserType) {
        this.oneUserType = oneUserType;
    }

    public BigDecimal getOneUserProfit() {
        return oneUserProfit;
    }

    public void setOneUserProfit(BigDecimal oneUserProfit) {
        this.oneUserProfit = oneUserProfit;
    }

    public String getOneUserName() {
        return oneUserName;
    }

    public void setOneUserName(String oneUserName) {
        this.oneUserName = oneUserName;
    }

    public String getTwoUserCode() {
        return twoUserCode;
    }

    public void setTwoUserCode(String twoUserCode) {
        this.twoUserCode = twoUserCode;
    }

    public String getTwoUserType() {
        return twoUserType;
    }

    public void setTwoUserType(String twoUserType) {
        this.twoUserType = twoUserType;
    }

    public BigDecimal getTwoUserProfit() {
        return twoUserProfit;
    }

    public void setTwoUserProfit(BigDecimal twoUserProfit) {
        this.twoUserProfit = twoUserProfit;
    }

    public String getTwoUserName() {
        return twoUserName;
    }

    public void setTwoUserName(String twoUserName) {
        this.twoUserName = twoUserName;
    }

    public String getThrUserCode() {
        return thrUserCode;
    }

    public void setThrUserCode(String thrUserCode) {
        this.thrUserCode = thrUserCode;
    }

    public String getThrUserType() {
        return thrUserType;
    }

    public void setThrUserType(String thrUserType) {
        this.thrUserType = thrUserType;
    }

    public BigDecimal getThrUserProfit() {
        return thrUserProfit;
    }

    public void setThrUserProfit(BigDecimal thrUserProfit) {
        this.thrUserProfit = thrUserProfit;
    }

    public String getThrUserName() {
        return thrUserName;
    }

    public void setThrUserName(String thrUserName) {
        this.thrUserName = thrUserName;
    }

    public String getFouUserCode() {
        return fouUserCode;
    }

    public void setFouUserCode(String fouUserCode) {
        this.fouUserCode = fouUserCode;
    }

    public String getFouUserType() {
        return fouUserType;
    }

    public void setFouUserType(String fouUserType) {
        this.fouUserType = fouUserType;
    }

    public BigDecimal getFouUserProfit() {
        return fouUserProfit;
    }

    public void setFouUserProfit(BigDecimal fouUserProfit) {
        this.fouUserProfit = fouUserProfit;
    }

    public String getFouUserName() {
        return fouUserName;
    }

    public void setFouUserName(String fouUserName) {
        this.fouUserName = fouUserName;
    }

    public BigDecimal getPlateProfit() {
        return plateProfit;
    }

    public void setPlateProfit(BigDecimal plateProfit) {
        this.plateProfit = plateProfit;
    }

    public BigDecimal getOrgProfit() {
        return orgProfit;
    }

    public void setOrgProfit(BigDecimal orgProfit) {
        this.orgProfit = orgProfit;
    }

    public Integer getPushNum() {
        return pushNum;
    }

    public void setPushNum(Integer pushNum) {
        this.pushNum = pushNum;
    }

    public Integer getHadsendNum() {
        return hadsendNum;
    }

    public void setHadsendNum(Integer hadsendNum) {
        this.hadsendNum = hadsendNum;
    }

    public Integer getDiscussNum() {
        return discussNum;
    }

    public void setDiscussNum(Integer discussNum) {
        this.discussNum = discussNum;
    }

    public Integer getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(Integer goodNum) {
        this.goodNum = goodNum;
    }

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    public Integer getStoreNum() {
        return storeNum;
    }

    public void setStoreNum(Integer storeNum) {
        this.storeNum = storeNum;
    }

    public String getPushAddress() {
        return pushAddress;
    }

    public void setPushAddress(String pushAddress) {
        this.pushAddress = pushAddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getAmountCount() {
        return amountCount;
    }

    public void setAmountCount(BigDecimal amountCount) {
        this.amountCount = amountCount;
    }

    public Integer getSumState() {
        return sumState;
    }

    public void setSumState(Integer sumState) {
        this.sumState = sumState;
    }

    public String getRemarkStatus() {
        return remarkStatus;
    }

    public void setRemarkStatus(String remarkStatus) {
        this.remarkStatus = remarkStatus;
    }

    public String getPushRealName() {
        return pushRealName;
    }

    public void setPushRealName(String pushRealName) {
        this.pushRealName = pushRealName;
    }

    public BigDecimal getPlateProfitSum() {
        return plateProfitSum;
    }

    public void setPlateProfitSum(BigDecimal plateProfitSum) {
        this.plateProfitSum = plateProfitSum;
    }
}
