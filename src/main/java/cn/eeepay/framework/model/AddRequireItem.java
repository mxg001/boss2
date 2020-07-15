package cn.eeepay.framework.model;

/**
 * table add_require_item
 * desc 进件要求项
 * @author tans
 *
 */
public class AddRequireItem {

	private Long itemId;

	private String itemName;

	private Integer exampleType;

	private String example;

	private String remark;

	private String photo;

	private String photoAddress;
	
	private String photoAddressUrl;
	
	private String dataAll;
	
	private String checkStatus;
	
	private String checkMsg;

	public String getCheckMsg() {
		return checkMsg;
	}

	public void setCheckMsg(String checkMsg) {
		this.checkMsg = checkMsg == null ? null : checkMsg.trim();
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName == null ? null : itemName.trim();
	}

	public Integer getExampleType() {
		return exampleType;
	}

	public void setExampleType(Integer exampleType) {
		this.exampleType = exampleType;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example == null ? null : example.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo == null ? null : photo.trim();
	}

	public String getPhotoAddress() {
		return photoAddress;
	}

	public void setPhotoAddress(String photoAddress) {
		this.photoAddress = photoAddress == null ? null : photoAddress.trim();
	}

	public String getDataAll() {
		return dataAll;
	}

	public void setDataAll(String dataAll) {
		this.dataAll = dataAll == null ? null : dataAll.trim();
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus == null ? null : checkStatus.trim();
	}

	public String getPhotoAddressUrl() {
		return photoAddressUrl;
	}

	public void setPhotoAddressUrl(String photoAddressUrl) {
		this.photoAddressUrl = photoAddressUrl;
	}
	
}