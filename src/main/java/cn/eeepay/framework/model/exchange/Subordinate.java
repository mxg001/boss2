package cn.eeepay.framework.model.exchange;

/**
 * Created by Administrator on 2018/4/9/009.
 * @author  liuks
 * 统计用户下级数量
 */
public class Subordinate {

    private int subordinate;//直营用户数量

    private int subordinateMoney;//下级代理数量

    public int getSubordinate() {
        return subordinate;
    }

    public void setSubordinate(int subordinate) {
        this.subordinate = subordinate;
    }

    public int getSubordinateMoney() {
        return subordinateMoney;
    }

    public void setSubordinateMoney(int subordinateMoney) {
        this.subordinateMoney = subordinateMoney;
    }
}
