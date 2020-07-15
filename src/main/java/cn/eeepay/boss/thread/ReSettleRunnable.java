package cn.eeepay.boss.thread;

import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 暂时不用，待定
 * 异步再次出款
 * @author tans
 * @date 2019/1/9 9:55
 */
public class ReSettleRunnable implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ReSettleRunnable.class);

    private Integer transferId;

    private String orderNo;

    public ReSettleRunnable(Integer transferId, String orderNo) {
        this.transferId = transferId;
        this.orderNo = orderNo;
    }

    @Override
    public void run() {
        String url = Constants.SETTLE_TRANS+"?transferId="+this.transferId+"&userId=reSettle";
        log.info("再次结算订单reSettle_transferId:{}",this.transferId);
        String result = ClientInterface.baseNoClient(url,null);
        log.info("再次结算订单[{}]返回信息：{}",new String[]{this.orderNo, result});
    }
}
