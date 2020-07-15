package cn.eeepay.framework.service.cjt;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.TerminalInfo;
import cn.eeepay.framework.model.cjt.CjtOrder;
import cn.eeepay.framework.model.cjt.CjtOrderSn;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 商户购买订单 服务层
 *
 * @author tans
 * @date 2019-05-30
 */
public interface CjtOrderService {

    /**
     * 条件查询商户购买订单
     * @param cjtOrder
     * @return
     */
    void selectPage(Page<CjtOrder> page, CjtOrder cjtOrder);

    /**
     * 查询商户购买订单
     * @param orderNo
     * @return
     */
    CjtOrder selectDetail(String orderNo);

    Map<String,Object> selectTotalMap(CjtOrder baseInfo);

    void export(HttpServletResponse response, CjtOrder cjtOrder);

    Result ship(CjtOrder baseInfo);

    Result importShip(MultipartFile file);

    List<CjtOrderSn> selectCjtOrderSnList(String orderNo);

    CjtOrder orderDetail(String orderNo);

}
