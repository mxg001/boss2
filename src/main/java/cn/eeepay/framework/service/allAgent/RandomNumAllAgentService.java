package cn.eeepay.framework.service.allAgent;

/**
 * Created by Administrator on 2018/7/13/013.
 * @author  liuks
 * 随机生成 编号
 */
public interface RandomNumAllAgentService {

    String getRandomNumber(String start,String key);

    String getRandomNumberByData(String start,String dataBaseName);

}
