package cn.eeepay.framework.model;

public class Node {
	public Node(String id, String pId,String isParent, String name, String open,String checked) {
		super();
		this.id = id;
		this.pId = pId;
		this.isParent = isParent;
		this.name = name;
		this.open = open;
		this.checked = checked;
	}
	
	public Node(){
		
	}
	private String id;
	private String pId;
	private String isParent;
	private String name;
	private String open;
	private String checked;
	private String rigthCode;

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getIsParent() {
		return isParent;
	}
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOpen() {
		return open;
	}
	public void setOpen(String open) {
		this.open = open;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getRigthCode() {
		return rigthCode;
	}

	public void setRigthCode(String rigthCode) {
		this.rigthCode = rigthCode;
	}
	
	
	
}
