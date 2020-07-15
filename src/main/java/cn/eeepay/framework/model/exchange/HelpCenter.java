package cn.eeepay.framework.model.exchange;

import java.util.Date;

/**
 * Created by Administrator on 2018/5/11/011.
 * @author  liuks
 * 帮助中心实体
 * 对应表 rdmp_help_center
 */
public class HelpCenter {

    private Long id;

    private String title;//标题

    private String link;//H5链接

    private String category;//类别:1使用教程2常见问题

    private String model;//类型:1兑换视频教程2图文教程3常见问题

    private String status;//状态:1关闭2启用

    private Integer sort;//排序,序号越大越靠前

    private Date createTime;//创建时间

    private String remark;//备注


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
