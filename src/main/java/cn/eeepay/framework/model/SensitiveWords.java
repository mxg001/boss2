package cn.eeepay.framework.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * @author liuks
 * 敏感词service接口
 * 表: sensitive_words
 */
public class SensitiveWords {

    private int id;//id

    private String sensitiveNo;//编码

    private String keyWord;//关键字

    private String status;//'启用状态:1-启用,2-禁用,3-删除'

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;//最后修改时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSensitiveNo() {
        return sensitiveNo;
    }

    public void setSensitiveNo(String sensitiveNo) {
        this.sensitiveNo = sensitiveNo;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
