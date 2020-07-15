package cn.eeepay.framework.model.exchange;

/**
 * Created by Administrator on 2018/4/10/010.
 * 系统配置实体
 * 对应表 rdmp_sys_config
 */
public class ExchangeConfig {

    private  int id;//id

    private  String sysKey;//key

    private  String sysValue;//值

    private  String remark;//说明

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSysKey() {
        return sysKey;
    }

    public void setSysKey(String sysKey) {
        this.sysKey = sysKey;
    }

    public String getSysValue() {
        return sysValue;
    }

    public void setSysValue(String sysValue) {
        this.sysValue = sysValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
