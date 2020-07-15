package cn.eeepay.framework.model.exchange;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/9/009.
 * @author liuks
 * 超级兑用户成为会员等级时间
 * 对应表 rdmp_merchant_act_info
 */
public class UserManagementMember {

    private Long id;

    private String  merchantNo;//商户号

    private String  merCapa;//商户身份标识

    private Date createTime;//激活时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMerCapa() {
        return merCapa;
    }

    public void setMerCapa(String merCapa) {
        this.merCapa = merCapa;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
