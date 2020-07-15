package cn.eeepay.framework.model;

import java.util.List;

/**
 * @author MXG
 * create 2018/09/04
 */
public class Channel {
    private RepayChannel repayChannel;
    private List<ExcludeCard> excludeCardList;

    public Channel() {
    }

    public Channel(RepayChannel repayChannel, List<ExcludeCard> excludeCardList) {
        this.repayChannel = repayChannel;
        this.excludeCardList = excludeCardList;
    }

    public RepayChannel getRepayChannel() {
        return repayChannel;
    }

    public void setRepayChannel(RepayChannel repayChannel) {
        this.repayChannel = repayChannel;
    }

    public List<ExcludeCard> getExcludeCardList() {
        return excludeCardList;
    }

    public void setExcludeCardList(List<ExcludeCard> excludeCardList) {
        this.excludeCardList = excludeCardList;
    }
}
