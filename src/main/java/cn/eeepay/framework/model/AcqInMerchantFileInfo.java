package cn.eeepay.framework.model;

/**
 * table acq_merchant_file_info
 * @author zxs
 *
 */
public class AcqInMerchantFileInfo {
	private Integer id;
	
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	private String fileType;
    private String fileUrl;
    private Integer status;
    private String acqIntoNo;
    
    private String content;
    
    private String itemName;
    
    
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getAcqIntoNo() {
		return acqIntoNo;
	}
	public void setAcqIntoNo(String acqIntoNo) {
		this.acqIntoNo = acqIntoNo;
	}
    
    
}