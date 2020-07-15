package cn.eeepay.framework.model;

import java.math.BigDecimal;

/**
 * table team_info
 * desc 组织表
 * @author tans
 *
 */
public class TeamInfo {

    private Long teamId;

    private String teamType;

    private String teamName;

    private String lawyer;

    private String busLicenseNo;

    private String logo;

    private String pubName;

    private String publicQrcode;

    private String teamEntryId;

    private String teamEntryName;

    private BigDecimal settleRateAmount;//出款手续费

	public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamType() {
        return teamType;
    }

    public void setTeamType(String teamType) {
        this.teamType = teamType == null ? null : teamType.trim();
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName == null ? null : teamName.trim();
    }

    public String getLawyer() {
        return lawyer;
    }

    public void setLawyer(String lawyer) {
        this.lawyer = lawyer == null ? null : lawyer.trim();
    }

    public String getBusLicenseNo() {
        return busLicenseNo;
    }

    public void setBusLicenseNo(String busLicenseNo) {
        this.busLicenseNo = busLicenseNo == null ? null : busLicenseNo.trim();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    public String getPubName() {
        return pubName;
    }

    public void setPubName(String pubName) {
        this.pubName = pubName == null ? null : pubName.trim();
    }

    public String getPublicQrcode() {
        return publicQrcode;
    }

    public void setPublicQrcode(String publicQrcode) {
        this.publicQrcode = publicQrcode == null ? null : publicQrcode.trim();
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

    public BigDecimal getSettleRateAmount() {
        return settleRateAmount;
    }

    public void setSettleRateAmount(BigDecimal settleRateAmount) {
        this.settleRateAmount = settleRateAmount;
    }
}