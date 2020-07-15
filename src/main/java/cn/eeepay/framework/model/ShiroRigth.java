package cn.eeepay.framework.model;

import java.util.ArrayList;
import java.util.List;

public class ShiroRigth {
    private Integer id;

    private String rightCode;

    private String rightName;

    private String rightComment;

    private Integer rightType;
    
    private List<ShiroRole> roles = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
  
	public String getRightCode() {
		return rightCode;
	}

	public String getRightName() {
		return rightName;
	}

	public String getRightComment() {
		return rightComment;
	}

	public Integer getRightType() {
		return rightType;
	}

	public List<ShiroRole> getRoles() {
		return roles;
	}

	public void setRightCode(String rightCode) {
		this.rightCode = rightCode;
	}

	public void setRightName(String rightName) {
		this.rightName = rightName;
	}

	public void setRightComment(String rightComment) {
		this.rightComment = rightComment;
	}

	public void setRightType(Integer rightType) {
		this.rightType = rightType;
	}

	public void setRoles(List<ShiroRole> roles) {
		this.roles = roles;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ShiroRigth) {   
			ShiroRigth shiroRigth = (ShiroRigth) obj;   
            return this.rightCode.equals(shiroRigth.rightCode);   
        }   
        return super.equals(obj); 
	}
    
}