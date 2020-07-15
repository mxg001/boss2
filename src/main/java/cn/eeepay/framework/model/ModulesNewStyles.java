package cn.eeepay.framework.model;

/**
 * table:super.modules_new_styles desc:超级银行家三大模块新样式表
 *
 * @author zhangjuan
 * @date 2018-11-10
 */
public class ModulesNewStyles {
	private Long id;

	private String orgId;// 组织ID

	private String modulesImages;// 信用卡活动图片

	private String modulesMatchUrl;// 信用卡活动对应链接

	private String style;// 优秀导师活动图片

	private String order;// 排序

	private String modulesImagesUrl;// 信用卡活动图片

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getModulesImages() {
		return modulesImages;
	}

	public void setModulesImages(String modulesImages) {
		this.modulesImages = modulesImages;
	}

	public String getModulesMatchUrl() {
		return modulesMatchUrl;
	}

	public void setModulesMatchUrl(String modulesMatchUrl) {
		this.modulesMatchUrl = modulesMatchUrl;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getModulesImagesUrl() {
		return modulesImagesUrl;
	}

	public void setModulesImagesUrl(String modulesImagesUrl) {
		this.modulesImagesUrl = modulesImagesUrl;
	}
}