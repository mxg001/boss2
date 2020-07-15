package cn.eeepay.boss.action;


import cn.eeepay.framework.model.OrderMain;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.YinShangOrder;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.YsService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.Md5;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc 银商订单推送
 */
@Controller
@RequestMapping("/ysOrder")
public class YsOrderAction {

    private final static Logger log = LoggerFactory.getLogger(YsOrderAction.class);

    @Resource
    private YsService ysService;

    @Resource
    private SysDictService sysDictService;

    /**
     * 银商系统回推订单
     *
     * @return
     */

    @RequestMapping(value = "/acceptOrder", method = {RequestMethod.POST})
    @ResponseBody
    public Result acceptOrder(@RequestBody YinShangOrder yinShangOrder) {
        log.info("--------银商推送数据statrt-----------");
        Result result = new Result();
        SysDict sysDict = sysDictService.getByKey("YINSHANG_KEY");
        String s = yinShangOrder.getYsOrder();
        String privateKey = sysDict.getSysValue();
        DES des = SecureUtil.des(privateKey.getBytes());
        String order = des.decryptStr(s);
        log.info("银商回推数据" + order);
        Map<String, String> map = JSON.parseObject(order, new TypeReference<HashMap<String, String>>() {
        });
        yinShangOrder.setBankName(map.get("bankName"));
        yinShangOrder.setOrderIdNo(map.get("orderIdNo"));
        yinShangOrder.setOrderName(map.get("orderName"));
        yinShangOrder.setOrderPhone(map.get("orderPhone"));
        yinShangOrder.setYsOrderNo(map.get("ysOrderNo"));
        String completeTime = map.get("completeTime");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(completeTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        yinShangOrder.setCompleteTime(date);
        OrderMain orderMain = ysService.matchYinShangOrder(yinShangOrder);
        if (orderMain != null) {
            ysService.updateYsOrderResult(orderMain);
            Map<String, String> params = new HashMap<String, String>();
            String orderNo = orderMain.getOrderNo();
            params.put("orderNo", orderMain.getOrderNo());
            String sign = Md5.md5Str(orderNo + "&" + Constants.SUPER_BANK_SECRET);
            params.put("sign", sign);
            sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
            String url = sysDict.getSysValue() + Constants.SUPER_BANK_PROFIT_ORDER_NO;
            log.info("分润接口订单号{},接口地址{}", orderNo, url);
            try {
                String res = ClientInterface.httpPost2(url, params);
                if (!res.contains("success")) {
                    log.info("调用分润接口异常 订单号：" + orderNo);
                }
            } catch (Exception e) {
                log.info("调用分润接口异常 订单号：" + orderNo);
            }
        }
        result.setStatus(true);
        result.setMsg("0000");
        return result;
    }

}
