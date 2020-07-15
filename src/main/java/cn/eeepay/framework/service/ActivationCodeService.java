package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ActivationCodeBean;

import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2017/10/26.
 */
public interface ActivationCodeService {

    /**
     * 批量生产激活码
     * @param count 数量
     * @param codeType
     * @return
     */
    Map<String, String> buildActivationCode(int count, String codeType);

    /**
     * 分页获取激活码信息
     * @param bean 查询信息
     * @param page 分页信息
     * @return
     */
    List<ActivationCodeBean> listActivationCode(ActivationCodeBean bean, Page<ActivationCodeBean> page);

    /**
     * 划分激活码
     * @param startId   激活码开始id
     * @param endId     激活码结束id
     * @param agentNode   代理商节点
     * @param codeType
     * @return
     */
    long divideActivationCode(long startId, long endId, String agentNode, String codeType);

    /**
     * 回收激活码
     * @param startId   激活码开始id
     * @param endId     激活码结束id
     * @param codeType
     * @return
     */
    long recoveryActivation(long startId, long endId, String codeType);
}
