package cn.eeepay.framework.service;

import cn.eeepay.framework.model.exchange.ExchangeConfig;

/**
 * Created by Administrator on 2018/4/10/010.
 * 超级兑，获取序列生成编码
 */
public interface RandomNumberService {

    String getRandomNumber(String start,String key);

    ExchangeConfig getExchangeConfig(String key);
}
