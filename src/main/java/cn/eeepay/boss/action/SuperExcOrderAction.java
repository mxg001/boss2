package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrderMain;
import cn.eeepay.framework.model.OrderMainSum;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.service.SuperBankService;
import cn.eeepay.framework.service.SuperExcOrderService;
import cn.eeepay.framework.util.DateUtils;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 保险产品管理
 * @author xieh
 * @date 2018/7/13
 */
@Controller
@RequestMapping("/superExcOrder")
public class SuperExcOrderAction {

    private Logger log = LoggerFactory.getLogger(SuperExcOrderAction.class);

    @Resource
    private SuperExcOrderService superExcOrderService;

    @Resource
    private SuperBankService superBankService;
    /**
     * 订单分页条件查询
     * @param baseInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/orderManager")
    @ResponseBody
    public Result orderManager(@RequestBody OrderMain baseInfo,
                               @RequestParam(defaultValue = "1") int pageNo,
                               @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        try {

            Page<OrderMain> page = new Page<>(pageNo, pageSize);
            superExcOrderService.selectOrderPage(baseInfo, page);
            OrderMainSum orderMainSum = superExcOrderService.selectOrderSum(baseInfo);
            Map<String, Object> map = new HashMap<>();
            map.put("page", page);
            map.put("orderMainSum", orderMainSum);
            result.setStatus(true);
            result.setData(map);
        } catch (Exception e){
            result.setStatus(false);
            result.setMsg("查询数据异常!");
            log.error("分页条件查询订单异常", e);
        }
        return result;
    }

    @RequestMapping("/superExcOrderDetail")
    @ResponseBody
    public Result superExcOrderDetail(String orderNo){
        Result result = new Result();
        try {
            if(StringUtil.isBlank(orderNo)){
                result.setMsg("订单号不能为空");
                return result;
            }
            OrderMain orderMain = superExcOrderService.selectExcOrderDetail(orderNo);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(orderMain);
        } catch (Exception e){
            log.error("超级银行家订单详情", e);
        }
        return result;
    }

    @RequestMapping("/changeOrg")
    @ResponseBody
    public Result changeOrg(Long orgId){
        Result result = new Result();
        try {
            String pointExchangeRatio = superExcOrderService.selectByOrgId(orgId);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(pointExchangeRatio);
        } catch (Exception e){
            log.error("查询组织异常", e);
        }
        return result;
    }

    /**
     * 导出超级银行家积分兑换订单
     * @param baseInfo
     * @param response
     */
    @RequestMapping("/exportSuperExcOrder")
    @SystemLog(description = "导出超级银行家积分兑换订单")
    public void exportSuperExcOrder(String baseInfo, HttpServletResponse response){
        try {
            if(StringUtils.isNotBlank(baseInfo)){
                baseInfo = URLDecoder.decode(baseInfo, "utf-8");
            }
            OrderMain order = JSONObject.parseObject(baseInfo, OrderMain.class);
            superBankService.exportSuperExcOrder(response, order);
        } catch (Exception e){
            log.error("导出超级银行家贷款订单异常", e);
        }
    }
    /**
     * 校验查询时间间隔不能超过三个月
     * @param createTimeStartStr
     * @param createTimeEndStr
     * @param result
     * @return
     */
    private boolean checkOrderDate(String createTimeStartStr,String createTimeEndStr, Result result) {
        if(StringUtils.isBlank(createTimeStartStr)
                || StringUtils.isBlank(createTimeEndStr)) {
            result.setMsg("时间不能为空");
            return true;
        }
        Date createTimeStart = DateUtils.parseDateTime(createTimeStartStr);
        Date createTimeEnd = DateUtils.parseDateTime(createTimeEndStr);
        if(createTimeEnd.getTime() < createTimeStart.getTime()){
            result.setMsg("结束时间时间不能小于起始时间");
            return true;
        }
        if(createTimeEnd.getTime() - createTimeStart.getTime() > 3*30*24*60*60*1000L){
            result.setMsg("时间间隔不能超过三个月");
            return true;
        }
        return false;
    }
}
