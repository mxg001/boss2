package cn.eeepay.framework.service;

import cn.eeepay.framework.model.OrderMain;
import cn.eeepay.framework.model.YinShangOrder;

public interface YsService {

  int  updateYsOrderResult(OrderMain orderMain);

    /**
     *银商回推数据匹配
     */
    OrderMain matchYinShangOrder(YinShangOrder yinShangOrder);

}
