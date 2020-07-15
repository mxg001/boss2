package cn.eeepay.framework.model.exchangeActivate;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/10/010.
 * @author  liuks
 * 机构表 对应表:yfb_org_info
 */
public class ExchangeActivateOrg{

    private Long id;//id

    private String  orgName;//机构名称

    private String  orgCode;//机构代码

    private String  orgStatus;//机构状态

    private String  orgLogo;//机构logo

    private String  remark;//积分查询方式

    private Date createTime;//创建时间

    private Integer sort;//顺序

    private String  finance;//是否具备金融属性 0否1是

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgStatus() {
        return orgStatus;
    }

    public void setOrgStatus(String orgStatus) {
        this.orgStatus = orgStatus;
    }

    public String getOrgLogo() {
        return orgLogo;
    }

    public void setOrgLogo(String orgLogo) {
        this.orgLogo = orgLogo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getFinance() {
        return finance;
    }

    public void setFinance(String finance) {
        this.finance = finance;
    }
}
