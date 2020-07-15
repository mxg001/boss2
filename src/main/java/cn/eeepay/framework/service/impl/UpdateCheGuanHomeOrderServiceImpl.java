package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.UpdateCheGuanHomeOrderDao;
import cn.eeepay.framework.model.CheGuanHomeOrder;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.UpdateCheGuanHomeOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *@author liuks
 * 车管家类的订单状态(当前时间的前一天00：00：00分到23:59:59)更新为不结算
 */
@Service("updateCheGuanHomeOrderService")
public class UpdateCheGuanHomeOrderServiceImpl implements UpdateCheGuanHomeOrderService {

    private static final Logger logger = LoggerFactory.getLogger(UpdateCheGuanHomeOrderServiceImpl.class);

    public static final String CHEGUANHOMEKEY="CHE_GUAN_HOME_ORDER_KEY";
    @Resource
    private UpdateCheGuanHomeOrderDao updateCheGuanHomeOrderDao;

    @Resource
    private SysDictService sysDictService;
    /**
     * 车管家类的订单状态(当前时间的前一天00：00：00分到23:59:59)更新为不结算
     */
    @Override
    public int updateCheGuanHomeOrder() {
        SysDict sd =sysDictService.getByKey(CHEGUANHOMEKEY);
        if(sd!=null){
            if(sd.getSysValue()!=null&&!"".equals(sd.getSysValue())){
                CheGuanHomeOrder bean=getCheGuanHomeOrderFilter(sd.getSysValue());
                int count=updateCheGuanHomeOrderDao.updateCheGuanHomeOrder(bean);
                return count;
            }else{
                logger.info("数据字典中未配置车管家的商户号编码值为空!");
            }
        }else{
            logger.info("数据字典中未配置车管家的商户号编码!");
        }
        return -1;
    }
    private CheGuanHomeOrder getCheGuanHomeOrderFilter(String str){
        CheGuanHomeOrder bean=new CheGuanHomeOrder();
        bean.setMerchantNo(str);
        //前天
        Calendar c1 = new GregorianCalendar();
        c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.add(Calendar.DAY_OF_MONTH, -1);
        bean.setCreateTimeBegin(c1.getTime());

        Calendar c2 = new GregorianCalendar();
        c2.set(Calendar.HOUR_OF_DAY, 23);
        c2.set(Calendar.MINUTE, 59);
        c2.set(Calendar.SECOND, 59);
        c2.add(Calendar.DAY_OF_MONTH, -1);
        bean.setCreateTimeEnd(c2.getTime());
        bean.setTransStatus("SUCCESS");//订单成功状态
        bean.setSettleStatus("5");//结算状态为 未结算
        return bean;
    }
}
