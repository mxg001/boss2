package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 欢乐返分组管理
 *
 * @author Administrator
 */
public class HlfGroup {
    private Integer id;
    private String groupName;
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "HlfGroup{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
