package cn.eeepay.framework.model;

import java.util.List;

/**
 * 超级还订单通道组合实体类
 * @author MXG
 * create 2018/08/30
 */
public class RepayType {
    private RepayPlanInfo repayPlanInfo;
    private List<RepayChannel> repayChannelList;

    public RepayType() {
    }

    public RepayType(RepayPlanInfo repayPlanInfo, List<RepayChannel> repayChannelList) {
        this.repayPlanInfo = repayPlanInfo;
        this.repayChannelList = repayChannelList;
    }

    public RepayPlanInfo getRepayPlanInfo() {
        return repayPlanInfo;
    }

    public void setRepayPlanInfo(RepayPlanInfo repayPlanInfo) {
        this.repayPlanInfo = repayPlanInfo;
    }

    public List<RepayChannel> getRepayChannelList() {
        return repayChannelList;
    }

    public void setRepayChannelList(List<RepayChannel> repayChannelList) {
        this.repayChannelList = repayChannelList;
    }
}
