package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoExchange.RandomNumberDao;
import cn.eeepay.framework.model.exchange.ExchangeConfig;
import cn.eeepay.framework.service.RandomNumberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/4/10/010.
 * @author  liuks
 * 超级兑，获取序列生成编码实现类
 */
@Service("randomNumberService")
public class RandomNumberServiceImpl implements RandomNumberService {

    @Resource
    private RandomNumberDao randomNumberDao;

    @Override
    public String getRandomNumber(String start,String key) {
        ExchangeConfig config =randomNumberDao.getConfig(key);
        String next=(Integer.valueOf(config.getSysValue())+1)+"";
        randomNumberDao.saveConfig(key,next+"");
        int max=6;
        int length=next.length();
        if(length>=max){
            return start+next;
        }else{
            StringBuffer sb=new StringBuffer();
            sb.append(start);
            for(int i=0;i<max-length;i++){
                sb.append("0");
            }
            sb.append(next);
            return sb.toString();
        }
    }

    @Override
    public ExchangeConfig getExchangeConfig(String key) {
        return randomNumberDao.getConfig(key);
    }
}
