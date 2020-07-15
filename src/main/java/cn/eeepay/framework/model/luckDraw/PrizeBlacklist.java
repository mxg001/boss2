package cn.eeepay.framework.model.luckDraw;

import java.util.Date;

/**
 * Created by Administrator on 2018/11/28/028.
 * @author  liuks
 * 抽奖 奖项黑名单
 * 对应表 awards_config_blacklist
 */
public class PrizeBlacklist {

    private Integer id;
    private Integer awardConfigId;//奖项配置 ID
    private String merchantNo;//商户编号
    private String creater;//创建人
    private Date createTime;//创建时间

    private String merchantName;//商户名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAwardConfigId() {
        return awardConfigId;
    }

    public void setAwardConfigId(Integer awardConfigId) {
        this.awardConfigId = awardConfigId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
