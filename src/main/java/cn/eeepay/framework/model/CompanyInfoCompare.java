package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 企业信息表
 * 对应表 company_info
 */
public class CompanyInfoCompare {

    private Integer id;
    private String channelCode;//通道编码
    private String orderNo;//订单号
    private String acqOrderNo;//'上游订单号'
    private String companyName;//'企业名称'
    private String licenseCode;//'组织机构代码'
    private String licenseSocialCode;//'统一社会信用代码'
    private String registerNo;//'企业工商注册号'
    private String legalName;//法人名称
    private String companyStatus;//企业状态 0-不存在 1-在营 2-吊销  3-注销  4-迁出  5-停业  9-其他
    private String companyStatusName;//企业状态名称
    private String businessScope;//'经营范围'
    private String registerAmount;//'注册资本'
    private String registerTime;//'注册日期'
    private String businessEndTime;//'经营截止日期'
    private String returnMsg;//'上游返回详细信息'
    private Date createTime;//'创建时间'

    //返回前端数据
    private int returnState;
    private String msg;


    public int getReturnState() {
        return returnState;
    }

    public void setReturnState(int returnState) {
        this.returnState = returnState;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAcqOrderNo() {
        return acqOrderNo;
    }

    public void setAcqOrderNo(String acqOrderNo) {
        this.acqOrderNo = acqOrderNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLicenseCode() {
        return licenseCode;
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode;
    }

    public String getLicenseSocialCode() {
        return licenseSocialCode;
    }

    public void setLicenseSocialCode(String licenseSocialCode) {
        this.licenseSocialCode = licenseSocialCode;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getCompanyStatus() {
        return companyStatus;
    }

    public void setCompanyStatus(String companyStatus) {
        this.companyStatus = companyStatus;
    }

    public String getCompanyStatusName() {
        return companyStatusName;
    }

    public void setCompanyStatusName(String companyStatusName) {
        this.companyStatusName = companyStatusName;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getRegisterAmount() {
        return registerAmount;
    }

    public void setRegisterAmount(String registerAmount) {
        this.registerAmount = registerAmount;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getBusinessEndTime() {
        return businessEndTime;
    }

    public void setBusinessEndTime(String businessEndTime) {
        this.businessEndTime = businessEndTime;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
