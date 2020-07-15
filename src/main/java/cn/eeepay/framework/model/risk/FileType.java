package cn.eeepay.framework.model.risk;

/**
 * Created by Administrator on 2018/11/30/030.
 * @author  liuks
 * 文件类型
 */
public class FileType {

    private String name;
    private String type;//1文件,2图片
    private String imgUrl;//图片路径

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
