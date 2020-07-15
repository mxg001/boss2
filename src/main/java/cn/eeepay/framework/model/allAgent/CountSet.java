package cn.eeepay.framework.model.allAgent;

/**
 * Created by Administrator on 2018/7/18/018.
 * @author  liuks
 * 用户统计实体
 */
public class CountSet {

    private Integer allyCount;//盟主数量

    private Integer allyOneCount;//机构数量

    private Integer allyTwoCount;//大盟主数量

    private Integer businessCount;//有商家成为的盟主

    public Integer getAllyCount() {
        return allyCount;
    }

    public void setAllyCount(Integer allyCount) {
        this.allyCount = allyCount;
    }

    public Integer getBusinessCount() {
        return businessCount;
    }

    public void setBusinessCount(Integer businessCount) {
        this.businessCount = businessCount;
    }

    public Integer getAllyOneCount() {
        return allyOneCount;
    }

    public void setAllyOneCount(Integer allyOneCount) {
        this.allyOneCount = allyOneCount;
    }

    public Integer getAllyTwoCount() {
        return allyTwoCount;
    }

    public void setAllyTwoCount(Integer allyTwoCount) {
        this.allyTwoCount = allyTwoCount;
    }
}
