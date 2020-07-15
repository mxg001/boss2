package cn.eeepay.framework.model;

/**
 * 自动审核路由通道
 *
 * @author rpc
 */
public class AutoCheckRoute {

    private int id;

    private String channelCode;//

    private String channelName;//

    private String account;//

    private String pwd;//

    private int status;//

    private int percent;//
    
    private String routeType;

    private int phohoCompProp;

    private int idCardOcr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

	public String getRouteType() {
		return routeType;
	}

	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}

    public int getPhohoCompProp() {
        return phohoCompProp;
    }

    public void setPhohoCompProp(int phohoCompProp) {
        this.phohoCompProp = phohoCompProp;
    }

    public int getIdCardOcr() {
        return idCardOcr;
    }

    public void setIdCardOcr(int idCardOcr) {
        this.idCardOcr = idCardOcr;
    }
}
