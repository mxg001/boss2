package cn.eeepay.framework.model;

/**
 * 彩票导入记录
 * @author dxy
 *
 */
public class LotteryImportRecords {

	private Long id; 
	
	private String batchNo;	  //  批次号,
	
	private String importDate;    //导入时间
	
	private String fileName;      //文件名
	
	private String status;        // 进度状态 0.无需操作；1.待匹配；2.已匹配;3.匹配中 定时任务执行后为2',
	
	private String updateBy;      //修改人
	
	private String updateDate;    //修改时间
	
	private String importDateStart; //查询导入开始时间
	
	private String importDateEnd;  //查询导入结束时间
	
	private String fileUrl;//导入文件地址
	
    private String fileUrlStr;//导入文件地址 
    
    private Integer totalNum;
    
    private Integer successNum;
    
    private Integer failNum;
    
    private String message;
    
    private String remark; //匹配结果
	
    private String lotteryType;	//彩票类型，1-福彩，2-体彩
    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getImportDate() {
		return importDate;
	}

	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getImportDateStart() {
		return importDateStart;
	}

	public void setImportDateStart(String importDateStart) {
		this.importDateStart = importDateStart;
	}

	public String getImportDateEnd() {
		return importDateEnd;
	}

	public void setImportDateEnd(String importDateEnd) {
		this.importDateEnd = importDateEnd;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileUrlStr() {
		return fileUrlStr;
	}

	public void setFileUrlStr(String fileUrlStr) {
		this.fileUrlStr = fileUrlStr;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public Integer getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(Integer successNum) {
		this.successNum = successNum;
	}

	public Integer getFailNum() {
		return failNum;
	}

	public void setFailNum(Integer failNum) {
		this.failNum = failNum;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}
	
}
