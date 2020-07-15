package cn.eeepay.framework.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class Ad {

	private Long id;
	
	private String postionType;									// 广告位置 (1-首页，2-办卡查询，3-贷款申请)
	
	private Long orgId;											// 所属组织
	
	private String imgUrl;										// 图片
	private String imgUrlStr;										// 图片

	private String title;										// 广告标题
	
	private Integer showNo;										// 权重
	
	private String status;										// 状态

	private String upDate;										// 上线时间

	private String downDate;									// 下线时间
	
	private String link;										// 链接
	
	private String remark;										// 备注
	
	private String orgName;										//组织名称

	private String applyType;	//应用类型 (1-App+公众号，2-App，3-公众号)

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPostionType() {
		return postionType;
	}

	public void setPostionType(String postionType) {
		this.postionType = postionType;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getShowNo() {
		return showNo;
	}

	public void setShowNo(Integer showNo) {
		this.showNo = showNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpDate() {
	    if(StringUtils.isNotBlank(upDate) && upDate.endsWith(".0")){
            upDate = upDate.substring(0, upDate.length() - 2);
        }
		return upDate;
	}

	public void setUpDate(String upDate) {
		this.upDate = upDate;
	}

	public String getDownDate() {
        if(StringUtils.isNotBlank(downDate) && downDate.endsWith(".0")){
            downDate = downDate.substring(0, downDate.length() - 2);
        }
		return downDate;
	}

	public void setDownDate(String downDate) {
		this.downDate = downDate;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getImgUrlStr() {
        return imgUrlStr;
    }

    public void setImgUrlStr(String imgUrlStr) {
        this.imgUrlStr = imgUrlStr;
    }
}
