package cn.eeepay.framework.model.exchangeActivate;

/**
 * Created by Administrator on 2018/10/12/012.
 * @author  liuks
 * 用于封装下载资料存储
 */
public class MediaPage {

    private  int type;//0 文本，1文本

    private  String name;//文件名称

    private  String remark;//文件

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
