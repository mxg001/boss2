package cn.eeepay.framework.service.impl.allAgent;

import cn.eeepay.framework.dao.risk.SequenceNpospDao;
import cn.eeepay.framework.daoAllAgent.RandomNumAllAgentDao;
import cn.eeepay.framework.daoExchange.exchangeActivate.SequenceRedemDao;
import cn.eeepay.framework.model.risk.SeqData;
import cn.eeepay.framework.service.allAgent.RandomNumAllAgentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/7/13/013.
 * @author  liuks
 * 获取序列随机生成编码
 */
@Service("randomNumAllAgentService")
public class RandomNumAllAgentServiceImpl implements RandomNumAllAgentService {

    @Resource
    private RandomNumAllAgentDao randomNumAllAgentDao;

    @Resource
    private SequenceNpospDao sequenceNpospDao;

    @Resource
    private SequenceRedemDao sequenceRedemDao;

    @Override
    public String getRandomNumber(String start, String key) {
        int num=randomNumAllAgentDao.getSequence(key);
        String str=start+String.format("%05d",num);
        return str;
    }

    /**
     * 生产开头的序列号
     * 后续扩展
     */
    @Override
    public String getRandomNumberByData(String start,String dataBaseName) {
        String str=null;
        if("nposp".equals(dataBaseName)){
            SeqData seq=new SeqData();
            sequenceNpospDao.getSequence(seq);
            str=start+String.format("%09d",seq.getId());
        }else if("redem".equals(dataBaseName)){
            SeqData seq=new SeqData();
            sequenceRedemDao.getSequence(seq);
            str=start+String.format("%09d",seq.getId());
        }
        return str;
    }
}
