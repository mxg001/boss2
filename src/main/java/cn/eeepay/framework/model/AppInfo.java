package cn.eeepay.framework.model;

/**
 * table app_info
 * desc app信息表
 *
 * @author tans
 */
public class AppInfo {

    private Integer id;

    private String appNo;

    private String appName;

    private String teamId;

    private String teamName;

    private String lastVersion;

    private Integer status;

    private Integer protocolVer;

    private String apply;

    private String codeUrl;
    private String logUrl;
    private String parentId;
    private String parenName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParenName() {
        return parenName;
    }

    public void setParenName(String parenName) {
        this.parenName = parenName;
    }

    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    public Integer getProtocolVer() {
        return protocolVer;
    }

    public void setProtocolVer(Integer protocolVer) {
        this.protocolVer = protocolVer;
    }

}
