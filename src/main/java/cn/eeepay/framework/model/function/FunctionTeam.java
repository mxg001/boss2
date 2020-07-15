package cn.eeepay.framework.model.function;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 开关-组织控制
 * 对应表 function_manage_config
 */
public class FunctionTeam {

    private Integer id;//
    private String functionNumber;//功能编码
    private String teamId;//组织编号
    private String teamName;//组织名称
    private String teamEntryId;//子组织编号
    private String teamEntryName;//子组织名称
    private String appNo;//app编号
    private String appName;//app名称
    private String bakJson;//保存其他业务数据，json格式
    private Date createTime;//创建时间
    private String operator;//创建人
    private Date lastUpdateTime;//最后更新时间

    //066功能开关个性JSON实体
    private Integer activity_days;//激活天数
    private Integer activity_trans_count;//激活交易笔数
    private Integer activity_trans_amount;//普通刷卡累计金额

    private String beginTime;
    private String endTime;


    //069功能开关个性JSON实体
    private String activity_type_nos;//关联子类型
    private Integer cycle;//弹窗周期
    private String remarks;//提示内容


    //072功能开关个性JSON实体
    private BigDecimal txFee;//提现手续费

    //073功能开关个性JSON实体
    private String merchantType;//商户类型,多个用英文逗号分隔
    private String orderType;//订单类型,多个用英文逗号分隔
    private String serviceType;//交易服务类型,多个用英文逗号分隔
    private String rateType;//费率类型:1-每笔固定金额，2-扣率
    private BigDecimal singleNumAmount;//每笔固定值
    private BigDecimal rate;//扣率
    private String vasServiceNo;//增值服务编号

    private Integer status;//状态

    //074功能开关个性JSON实体
    private String effectiveTime;//生效时间
    private String invalidTime;//失效时间
    private Integer actLimitDay;//激活天数限制

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        this.invalidTime = invalidTime;
    }

    public Integer getActLimitDay() {
        return actLimitDay;
    }

    public void setActLimitDay(Integer actLimitDay) {
        this.actLimitDay = actLimitDay;
    }

    public String getVasServiceNo() {
        return vasServiceNo;
    }

    public void setVasServiceNo(String vasServiceNo) {
        this.vasServiceNo = vasServiceNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public BigDecimal getSingleNumAmount() {
        return singleNumAmount;
    }

    public void setSingleNumAmount(BigDecimal singleNumAmount) {
        this.singleNumAmount = singleNumAmount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getTxFee() {
        return txFee;
    }

    public void setTxFee(BigDecimal txFee) {
        this.txFee = txFee;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFunctionNumber() {
        return functionNumber;
    }

    public void setFunctionNumber(String functionNumber) {
        this.functionNumber = functionNumber;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamEntryId() {
        return teamEntryId;
    }

    public void setTeamEntryId(String teamEntryId) {
        this.teamEntryId = teamEntryId;
    }

    public String getTeamEntryName() {
        return teamEntryName;
    }

    public void setTeamEntryName(String teamEntryName) {
        this.teamEntryName = teamEntryName;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getBakJson() {
        return bakJson;
    }

    public void setBakJson(String bakJson) {
        this.bakJson = bakJson;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getActivity_days() {
        return activity_days;
    }

    public void setActivity_days(Integer activity_days) {
        this.activity_days = activity_days;
    }

    public Integer getActivity_trans_count() {
        return activity_trans_count;
    }

    public void setActivity_trans_count(Integer activity_trans_count) {
        this.activity_trans_count = activity_trans_count;
    }

    public Integer getActivity_trans_amount() {
        return activity_trans_amount;
    }

    public void setActivity_trans_amount(Integer activity_trans_amount) {
        this.activity_trans_amount = activity_trans_amount;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getActivity_type_nos() {
        return activity_type_nos;
    }

    public void setActivity_type_nos(String activity_type_nos) {
        this.activity_type_nos = activity_type_nos;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
