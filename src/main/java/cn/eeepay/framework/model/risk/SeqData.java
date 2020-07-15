package cn.eeepay.framework.model.risk;

import java.util.Date;

/**
 * Created by Administrator on 2018/9/10/010.
 * @author  liuks
 * 序列
 */
public class SeqData {
    private Long id;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
