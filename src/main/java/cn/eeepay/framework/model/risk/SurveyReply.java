package cn.eeepay.framework.model.risk;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/9/13/013.
 * @author  liuks
 * 调单回复信息表
 * 对应表 survey_reply_record
 */
public class SurveyReply {

    private Integer id;
    private String orderNo;//调单单号
    private String agentNode;//代理商编号节点
    private String replyRoleType;//回复的角色类型 A：代理商  M:商户
    private String replyRoleNo;//回复的角色编号。商户编号/代理商编号
    private String replyResult;//回复结果 0交易成功，提供凭证；1交易成功，无法提供凭证；2交易未成功，退持卡人；3交易成功，已协商，需退持卡人；4长款需结算给商户
    private String merName;//商户名称
    private String merMobile;//商户电话
    private String cardPersonName;//持卡人姓名
    private String cardPersonMobile;//持卡人电话
    private String realName;//真实商户名称
    private String province;//归属省
    private String city;//归属市
    private String transAddress;//真实交易地址
    private String replyFilesName;//回复附件名称
    private String replyRemark;//回复说明
    private Date createTime;//创建时间
    private Date lastUpdateTime;//数据最后更新时间
    private String bak1;
    private String bak2;
    private String lastDealStatus;//最后一次调单处理状态(针对业务人员) 0未处理，1部分提供，2持卡人承认交易，3全部提供，4无法提供，5逾期部分提供，6逾期全部提供，7逾期未回，8已回退，9已处理完，无需代理商提交资料',
    private String lastDealRemark;//最后一次处理备注

    private List<FileType> replyFilesList;//附件列表

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode;
    }

    public String getReplyRoleType() {
        return replyRoleType;
    }

    public void setReplyRoleType(String replyRoleType) {
        this.replyRoleType = replyRoleType;
    }

    public String getReplyRoleNo() {
        return replyRoleNo;
    }

    public void setReplyRoleNo(String replyRoleNo) {
        this.replyRoleNo = replyRoleNo;
    }

    public String getReplyResult() {
        return replyResult;
    }

    public void setReplyResult(String replyResult) {
        this.replyResult = replyResult;
    }

    public String getMerName() {
        return merName;
    }

    public void setMerName(String merName) {
        this.merName = merName;
    }

    public String getMerMobile() {
        return merMobile;
    }

    public void setMerMobile(String merMobile) {
        this.merMobile = merMobile;
    }

    public String getCardPersonName() {
        return cardPersonName;
    }

    public void setCardPersonName(String cardPersonName) {
        this.cardPersonName = cardPersonName;
    }

    public String getCardPersonMobile() {
        return cardPersonMobile;
    }

    public void setCardPersonMobile(String cardPersonMobile) {
        this.cardPersonMobile = cardPersonMobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTransAddress() {
        return transAddress;
    }

    public void setTransAddress(String transAddress) {
        this.transAddress = transAddress;
    }

    public String getReplyFilesName() {
        return replyFilesName;
    }

    public void setReplyFilesName(String replyFilesName) {
        this.replyFilesName = replyFilesName;
    }

    public String getReplyRemark() {
        return replyRemark;
    }

    public void setReplyRemark(String replyRemark) {
        this.replyRemark = replyRemark;
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

    public String getBak1() {
        return bak1;
    }

    public void setBak1(String bak1) {
        this.bak1 = bak1;
    }

    public String getBak2() {
        return bak2;
    }

    public void setBak2(String bak2) {
        this.bak2 = bak2;
    }

    public String getLastDealStatus() {
        return lastDealStatus;
    }

    public void setLastDealStatus(String lastDealStatus) {
        this.lastDealStatus = lastDealStatus;
    }

    public String getLastDealRemark() {
        return lastDealRemark;
    }

    public void setLastDealRemark(String lastDealRemark) {
        this.lastDealRemark = lastDealRemark;
    }

    public List<FileType> getReplyFilesList() {
        return replyFilesList;
    }

    public void setReplyFilesList(List<FileType> replyFilesList) {
        this.replyFilesList = replyFilesList;
    }

}
