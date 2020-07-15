package cn.eeepay.framework.model.cjt;

import java.util.Date;

/**
 * 超级推极光推送模板
 * @author tans
 * @date 2019/5/27 11:23
 */
public class CjtPushTemplate {

    public static final String type_trans = "trans";
    public static final String type_recommend = "recommend";
    public static final String type_activity_one = "activity_one";
    public static final String type_activity_two = "activity_two";

    private Integer id;
    private String type;//类型,trans:交易分润,recommend:推荐奖励,activity_one:活动补贴直推,activity_two:活动补贴间推
    private String content;//模板内容
    private Date createTime;
    private String operator;
    private Date lastUpdateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
