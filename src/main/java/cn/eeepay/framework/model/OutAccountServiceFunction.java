package cn.eeepay.framework.model;

public class OutAccountServiceFunction {
    private Integer id;

    private String closeAdvanceMobile;

    private String outAccountMobile;

    private String skipChannelMobile;
    
    private String outAccountFailure;//出款手机号

    private Integer secondOut;

    private Integer selfAudit;

    private Integer outAccount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCloseAdvanceMobile() {
        return closeAdvanceMobile;
    }

    public void setCloseAdvanceMobile(String closeAdvanceMobile) {
        this.closeAdvanceMobile = closeAdvanceMobile;
    }

    public String getOutAccountMobile() {
        return outAccountMobile;
    }

    public void setOutAccountMobile(String outAccountMobile) {
        this.outAccountMobile = outAccountMobile;
    }

    public String getSkipChannelMobile() {
        return skipChannelMobile;
    }

    public void setSkipChannelMobile(String skipChannelMobile) {
        this.skipChannelMobile = skipChannelMobile;
    }

    public Integer getSecondOut() {
        return secondOut;
    }

    public void setSecondOut(Integer secondOut) {
        this.secondOut = secondOut;
    }

    public Integer getSelfAudit() {
        return selfAudit;
    }

    public void setSelfAudit(Integer selfAudit) {
        this.selfAudit = selfAudit;
    }

    public Integer getOutAccount() {
        return outAccount;
    }

    public void setOutAccount(Integer outAccount) {
        this.outAccount = outAccount;
    }

	public String getOutAccountFailure() {
		return outAccountFailure;
	}

	public void setOutAccountFailure(String outAccountFailure) {
		this.outAccountFailure = outAccountFailure;
	}
}