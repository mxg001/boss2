package cn.eeepay.framework.service.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.ExchangeConfig;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivationCodeBean;


import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2017/10/26.
 */
public interface ExchangeActivationCodeService {

    /**
     * 批量生产激活码
     * @param count 数量
     * @return
     */
    Map<String, String> buildActivationCode(int count);

    /**
     * 分页获取激活码信息
     * @param bean 查询信息
     * @param page 分页信息
     * @return
     */
    List<ExchangeActivationCodeBean> listActivationCode(ExchangeActivationCodeBean bean, Page<ExchangeActivationCodeBean> page);

    /**
     * 划分激活码
     * @param startId   激活码开始id
     * @param endId     激活码结束id
     * @param agentNode   代理商节点
     * @return
     */
    long divideActivationCode(long startId, long endId, String agentNode);

    /**
     * 回收激活码
     * @param startId   激活码开始id
     * @param endId     激活码结束id
     * @return
     */
    long recoveryActivation(long startId, long endId);

    /**
     * 获取激活url前缀
     * @return
     */
    ExchangeConfig getRedeemJHCode();
}
