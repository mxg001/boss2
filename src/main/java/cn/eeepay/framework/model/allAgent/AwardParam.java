package cn.eeepay.framework.model.allAgent;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/6/006.
 * @author liuks
 * 奖项参数设置
 * 对应表 pa_brand
 */
public class AwardParam {

    private Integer id;
    private String  brandCode;//品牌编码
    private String  brandName;//品牌名称
    private BigDecimal cost;//商户提现成本
    private Integer outServiceId;//出款服务ID
    private Date createTime;//创建时间
    private Date  lastUpdate;//更新时间

    private AwardParamDiamonds diamonds;//钻石奖

    private List<AwardParamLadder> tradeList;//交易分润收益阶梯
    private List<AwardParamLadder> crownList;//荣耀奖金收益阶梯配置
    private List<AwardParamLadder> vipList;//VIP分润收益阶梯配置

    private String aboutUs;
    private String agentBgi;
    private String merBgi;
    private String ownerImgs;
    private String ownerImg;
    private String merImgs;
    private String merImg;
    private String merContent;
    private String merApp;
    private String leaderboardBgi;
    private Map<String,String> ownerImgsMap;
    private Map<String,String> merImgsMap;
    private Map<String,String> leaImgsMap;
    private String agentBgiUrl;
    private String merBgiUrl;
    private String ownerImgUrl;
    private String merImgUrl;

    private BigDecimal accFee;//账户提现手续费
    private Integer oneAgentStatus;//允许机构调整分润等级 0否,1是
    private Integer twoAgentStatus;//允许大盟主调整分润等级 0否1是
    private Integer userStatus;//允许盟主调整分润等级 0否1是
    private String checkedState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getOutServiceId() {
        return outServiceId;
    }

    public void setOutServiceId(Integer outServiceId) {
        this.outServiceId = outServiceId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public AwardParamDiamonds getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(AwardParamDiamonds diamonds) {
        this.diamonds = diamonds;
    }

    public List<AwardParamLadder> getTradeList() {
        return tradeList;
    }

    public void setTradeList(List<AwardParamLadder> tradeList) {
        this.tradeList = tradeList;
    }

    public List<AwardParamLadder> getCrownList() {
        return crownList;
    }

    public void setCrownList(List<AwardParamLadder> crownList) {
        this.crownList = crownList;
    }

    public String getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }

    public String getAgentBgi() {
        return agentBgi;
    }

    public void setAgentBgi(String agentBgi) {
        this.agentBgi = agentBgi;
    }

    public String getMerBgi() {
        return merBgi;
    }

    public void setMerBgi(String merBgi) {
        this.merBgi = merBgi;
    }

    public String getOwnerImgs() {
        return ownerImgs;
    }

    public void setOwnerImgs(String ownerImgs) {
        this.ownerImgs = ownerImgs;
    }

    public String getOwnerImg() {
        return ownerImg;
    }

    public void setOwnerImg(String ownerImg) {
        this.ownerImg = ownerImg;
    }

    public String getMerImgs() {
        return merImgs;
    }

    public void setMerImgs(String merImgs) {
        this.merImgs = merImgs;
    }

    public String getMerImg() {
        return merImg;
    }

    public void setMerImg(String merImg) {
        this.merImg = merImg;
    }

    public String getMerContent() {
        return merContent;
    }

    public void setMerContent(String merContent) {
        this.merContent = merContent;
    }

    public String getMerApp() {
        return merApp;
    }

    public void setMerApp(String merApp) {
        this.merApp = merApp;
    }

    public Map<String, String> getOwnerImgsMap() {
        return ownerImgsMap;
    }

    public void setOwnerImgsMap(Map<String, String> ownerImgsMap) {
        this.ownerImgsMap = ownerImgsMap;
    }

    public Map<String, String> getMerImgsMap() {
        return merImgsMap;
    }

    public void setMerImgsMap(Map<String, String> merImgsMap) {
        this.merImgsMap = merImgsMap;
    }

    public String getAgentBgiUrl() {
        return agentBgiUrl;
    }

    public void setAgentBgiUrl(String agentBgiUrl) {
        this.agentBgiUrl = agentBgiUrl;
    }

    public String getMerBgiUrl() {
        return merBgiUrl;
    }

    public void setMerBgiUrl(String merBgiUrl) {
        this.merBgiUrl = merBgiUrl;
    }

    public String getOwnerImgUrl() {
        return ownerImgUrl;
    }

    public void setOwnerImgUrl(String ownerImgUrl) {
        this.ownerImgUrl = ownerImgUrl;
    }

    public String getMerImgUrl() {
        return merImgUrl;
    }

    public void setMerImgUrl(String merImgUrl) {
        this.merImgUrl = merImgUrl;
    }

    public BigDecimal getAccFee() {
        return accFee;
    }

    public void setAccFee(BigDecimal accFee) {
        this.accFee = accFee;
    }

    public Integer getOneAgentStatus() {
        return oneAgentStatus;
    }

    public void setOneAgentStatus(Integer oneAgentStatus) {
        this.oneAgentStatus = oneAgentStatus;
    }

    public Integer getTwoAgentStatus() {
        return twoAgentStatus;
    }

    public void setTwoAgentStatus(Integer twoAgentStatus) {
        this.twoAgentStatus = twoAgentStatus;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public List<AwardParamLadder> getVipList() {
        return vipList;
    }

    public void setVipList(List<AwardParamLadder> vipList) {
        this.vipList = vipList;
    }

    public String getCheckedState() {
        return checkedState;
    }

    public void setCheckedState(String checkedState) {
        this.checkedState = checkedState;
    }

    public String getLeaderboardBgi() {
        return leaderboardBgi;
    }

    public void setLeaderboardBgi(String leaderboardBgi) {
        this.leaderboardBgi = leaderboardBgi;
    }

    public Map<String, String> getLeaImgsMap() {
        return leaImgsMap;
    }

    public void setLeaImgsMap(Map<String, String> leaImgsMap) {
        this.leaImgsMap = leaImgsMap;
    }
}
