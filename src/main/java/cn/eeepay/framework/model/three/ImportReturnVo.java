package cn.eeepay.framework.model.three;

/**
 * 
 * @author qiujian
 *
 */
public class ImportReturnVo {

	private String agentNo;
	private Integer containsLower;
	private String errMsg;
	private String handle;

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public Integer getContainsLower() {
		return containsLower;
	}

	public void setContainsLower(Integer containsLower) {
		this.containsLower = containsLower;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

}
