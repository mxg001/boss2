package cn.eeepay.framework.service.impl.exchangeActivate;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.daoExchange.exchangeActivate.ExchangeActivationCodeDao;
import cn.eeepay.framework.daoExchange.RandomNumberDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.exchange.ExchangeConfig;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivationCodeBean;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivationCodeService;
import cn.eeepay.framework.util.BossBaseException;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by 666666 on 2018/5/15.
 */
@Service
public class ExchangeActivationCodeServiceImpl implements ExchangeActivationCodeService{

    @Resource
    private ExchangeActivationCodeDao exchangeActivationCodeDao;
    @Resource
    private AgentInfoDao agentInfoDao;
    @Resource
    private RandomNumberDao randomNumberDao;

    private static Lock buildLock = new ReentrantLock();

    @Override
    public Map<String, String> buildActivationCode(int count) {
        if(buildLock.tryLock()){
            try{
                int maxCount = getIntValueFromSysDIct("activation_code_build_max_count", 10000);
                if (count <= 0 || count > maxCount){
                    throw new BossBaseException("输入个数必须大于0且小于" + maxCount);
                }
                Map<String, String> resultMap = new HashedMap();
                ExchangeActivationCodeBean minBean = new ExchangeActivationCodeBean();
                ExchangeActivationCodeBean maxBean = new ExchangeActivationCodeBean();
                exchangeActivationCodeDao.insert(minBean);
                resultMap.put("minId", minBean.getId() + "");
                if (count == 1){
                    resultMap.put("maxId", minBean.getId() + "");
                    return resultMap;
                }
                if (count > 2){
                    batchInsert(count - 2);
                }
                exchangeActivationCodeDao.insert(maxBean);
                resultMap.put("maxId", maxBean.getId() + "");
                return resultMap;
            } finally {
                buildLock.unlock();
            }
        }else{
            throw new BossBaseException("激活码正在生成中,请稍后再试.");
        }
    }

    private int getIntValueFromSysDIct(String key, int defaultValue) {
        ExchangeConfig config = randomNumberDao.getConfig(key);
        if (config == null){
            return defaultValue;
        }
        try {
            Integer result = Integer.valueOf(config.getSysValue());
            if (result <= 0){
                return defaultValue;
            }else{
                return result;
            }
        }catch (NumberFormatException e){
            return defaultValue;
        }
    }

    private void batchInsert(int sumCount) {
        if (sumCount <= 0){
            return;
        }
        int maxCountPerTime = getIntValueFromSysDIct("activation_code_build_max_count_per_time", 1000);
        int count = sumCount / maxCountPerTime;
        for (int i = 0; i < count; i ++){
            exchangeActivationCodeDao.batchInsert(maxCountPerTime);
            sumCount -= maxCountPerTime;
        }
        if (sumCount > 0){
            exchangeActivationCodeDao.batchInsert(sumCount);
        }
    }

    @Override
    public List<ExchangeActivationCodeBean> listActivationCode(ExchangeActivationCodeBean bean, Page<ExchangeActivationCodeBean> page) {
        return exchangeActivationCodeDao.listActivationCode(bean, page);
    }

    @Override
    public long divideActivationCode(long startId, long endId, String agentNode) {
        if (StringUtils.isBlank(agentNode)){
            throw new BossBaseException("未选择服务商.");
        }
        AgentInfo agentInfo = agentInfoDao.selectByAgentNode(agentNode);
        if (agentInfo == null || agentInfo.getAgentLevel() == null || !agentInfo.getAgentLevel().equals(1)){
            throw new BossBaseException("只能分配给一级代理商.");
        }
        if(exchangeActivationCodeDao.chechAgentOpenExchange(agentInfo.getAgentNo()) == 0){
            throw new BossBaseException("该代理没有开通超级兑-激活版功能.");
        }
        if ((startId + "").length() != 12){
            throw new BossBaseException("开始编号格式不正确");
        }
        if ((endId + "").length() != 12){
            throw new BossBaseException("结束编号格式不正确");
        }
        if (endId < startId){
            throw new BossBaseException("结束编号不能小于开始编号");
        }

        String oemNo = exchangeActivationCodeDao.queryOemNoByAgentNo(agentInfo.getAgentNo());
        if (StringUtils.isBlank(oemNo)){
            ExchangeConfig activate_oem_no = randomNumberDao.getConfig("activate_oem_no");
            if (activate_oem_no == null){
                throw new BossBaseException("请配置默认的交易服务费率.");
            }else {
                oemNo = activate_oem_no.getSysValue();
            }
        }
        return exchangeActivationCodeDao.allotActivationCode2Agent(startId, endId, agentInfo, oemNo);
    }

    @Override
    public long recoveryActivation(long startId, long endId) {
//        int count = exchangeActivationCodeDao.countActivationCodeByStatus(startId, endId, "1");
//        if (endId - startId + 1 != count){
//            throw new BossBaseException("输入的编号存在不能回收的激活码");
//        }
        if ((startId + "").length() != 12){
            throw new BossBaseException("开始编号格式不正确");
        }
        if ((endId + "").length() != 12){
            throw new BossBaseException("结束编号格式不正确");
        }
        if (endId < startId){
            throw new BossBaseException("结束编号不能小于开始编号");
        }
        return exchangeActivationCodeDao.recoveryActivation(startId, endId);
    }

    @Override
    public ExchangeConfig getRedeemJHCode() {
        return randomNumberDao.getConfig("redeem_jhcode");
    }
}
