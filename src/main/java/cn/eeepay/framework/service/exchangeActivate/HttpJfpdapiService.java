package cn.eeepay.framework.service.exchangeActivate;

/**
 * Created by Administrator on 2018/10/12/012.
 * @author  liuks
 * 封装请求 JFPD_API 通道接口
 */
public interface HttpJfpdapiService {

    String httpMedia(String goodTypeNo);

    String httpOrder(String orderNo);
}
