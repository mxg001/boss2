package cn.eeepay.framework.model.three;

import java.util.List;
/**
 * 
 * @author qiujian
 *
 */
public class ImportAgentVo {
	private Integer successCount;
	private Integer errCount;
	private List<ImportReturnVo> list;

	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public Integer getErrCount() {
		return errCount;
	}

	public void setErrCount(Integer errCount) {
		this.errCount = errCount;
	}

	public List<ImportReturnVo> getList() {
		return list;
	}

	public void setList(List<ImportReturnVo> list) {
		this.list = list;
	}

}
