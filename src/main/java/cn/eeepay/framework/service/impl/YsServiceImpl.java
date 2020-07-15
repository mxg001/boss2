package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoSuperbank.OrderMainDao;
import cn.eeepay.framework.model.OrderMain;
import cn.eeepay.framework.model.YinShangOrder;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.YsService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service("ysService")
public class YsServiceImpl implements YsService {

    private final static Logger log = LoggerFactory.getLogger(YsServiceImpl.class);

    @Resource
    private OrderMainDao orderMainDao;
    @Resource
    private SysDictService sysDictService;


    @Override
    public int updateYsOrderResult(OrderMain orderMain) {
        return orderMainDao.updateYsOrderResult(orderMain);
    }

    @Override
    public OrderMain matchYinShangOrder(YinShangOrder yinShangOrder) {
        OrderMain orderMain=new OrderMain();
        OrderMain order=null;
        orderMain.setOrderType("2");
        orderMain.setOrderIdNo(yinShangOrder.getOrderIdNo());
        orderMain.setOrderPhoneStart(yinShangOrder.getOrderPhone().substring(0,3)+"%");
        orderMain.setOrderPhoneEnd(yinShangOrder.getOrderPhone().substring(7));
        orderMain.setOrderNameStart(yinShangOrder.getOrderName().substring(0,1)+"%");
        orderMain.setBankName(yinShangOrder.getBankName());
        Date createDate=yinShangOrder.getCompleteTime();
        orderMain.setCreateDate(createDate);
        log.info("银商回推订单匹配数据"+ JSON.toJSONString(orderMain));
        List<OrderMain> orderMains = orderMainDao.selectYsBankOrderExists(orderMain);
        if(orderMains==null||orderMains.size()<=0){
            log.info("银商回推的数据找不到对应的订单 上游订单号："+yinShangOrder.getYsOrderNo());
            return null;
        }
        boolean state=false;
        for (OrderMain OrderMain : orderMains) {
            if ("5".equals(OrderMain.getStatus())) {
                state = true;
                log.info("二次贷款,最近贷款成功单号为:{}", OrderMain.getOrderNo());
                break;
            }
        }
        if (!state) {
            order=new OrderMain();
            String orderNo = orderMains.get(0).getOrderNo();
            order.setOrderNo(orderNo);
            order.setLoanOrderNo(yinShangOrder.getYsOrderNo());
            order.setStatus("5");
        }
        return  order;
    }
}
