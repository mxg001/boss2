package cn.eeepay.boss.action.cjt;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.TerminalInfo;
import cn.eeepay.framework.model.cjt.CjtOrder;
import cn.eeepay.framework.model.cjt.CjtOrderSn;
import cn.eeepay.framework.service.TerminalInfoService;
import cn.eeepay.framework.service.cjt.CjtOrderService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import com.sun.xml.internal.xsom.impl.Ref;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tans
 * @date 2019-05-30
 */
@RestController
@RequestMapping("/cjtOrder")
public class CjtOrderAction {

    private static final Logger log = LoggerFactory.getLogger(CjtOrderAction.class);

    @Resource
    private CjtOrderService cjtOrderService;

    @Resource
    private TerminalInfoService terminalInfoService;

    /**
     * 条件查询商户购买订单
     * @param baseInfo
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/selectPage")
    public Result selectPage(@RequestBody CjtOrder baseInfo,
                                     @RequestParam(defaultValue = "1") int pageNo,
                                     @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        try {
            Page<CjtOrder> page = new Page<>(pageNo, pageSize);
            cjtOrderService.selectPage(page, baseInfo);

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("page", page);
            if(pageNo == 1) {
                Map<String, Object> totalMap = cjtOrderService.selectTotalMap(baseInfo);
                dataMap.put("totalMap", totalMap);
            }
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(dataMap);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("条件查询商户购买订单异常", e);
        }
        return result;
    }

    /**
     * 超级推机具申领订单导出
     * @param baseInfo
     * @param response
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/export")
    @SystemLog(operCode = "cjtOrder.export", description = "导出")
    public void export(String baseInfo, HttpServletResponse response){
        try {
            if(StringUtils.isNotEmpty(baseInfo)){
                baseInfo = URLDecoder.decode(baseInfo, "utf-8");
            }
            CjtOrder cjtOrder = JSONObject.parseObject(baseInfo, CjtOrder.class);
            cjtOrderService.export(response, cjtOrder);
        } catch (Exception e){
            log.error("超级推机具申领订单导出异常", e);
        }
    }

    /**
     * 批量发货
     * @return
     */
    @RequestMapping("/importShip")
    @SystemLog(operCode = "cjtOrder.importShip", description = "批量发货")
    public Result importShip(@RequestParam("file") MultipartFile file){
        Result result;
        try {
            result = cjtOrderService.importShip(file);
        } catch (Exception e) {
            log.error("超级推机具批量发货异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 机具发货
     * @param baseInfo
     * @return
     */
    @RequestMapping("/ship")
    @SystemLog(operCode = "cjtOrder.ship", description = "机具发货")
    public Result ship(@RequestBody CjtOrder baseInfo){
        Result result;
        try {
            result = cjtOrderService.ship(baseInfo);
        } catch (Exception e) {
            log.error("超级推机具发货异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 发货详情
     * @param orderNo
     * @return
     */
    @RequestMapping("/shipDetail")
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Result shipDetail(String orderNo){
        Result result;
        try {
            CjtOrder baseInfo = cjtOrderService.selectDetail(orderNo);
            List<CjtOrderSn> snList = cjtOrderService.selectCjtOrderSnList(orderNo);
            result = Result.success("查询成功");
            Map<String, Object> map = new HashMap<>();
            map.put("baseInfo", baseInfo);
            map.put("snList", snList);
            result.setData(map);
        } catch (Exception e) {
            log.error("查看超级推机具发货详情异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 订单详情
     * @param orderNo
     * @return
     */
    @RequestMapping("/orderDetail")
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Result orderDetail(String orderNo){
        Result result;
        try {
            CjtOrder baseInfo = cjtOrderService.orderDetail(orderNo);
            List<CjtOrderSn> snList = cjtOrderService.selectCjtOrderSnList(orderNo);
            result = Result.success("查询成功");
            Map<String, Object> map = new HashMap<>();
            map.put("baseInfo", baseInfo);
            map.put("snList", snList);
            result.setData(map);
        } catch (Exception e) {
            log.error("查看超级推机具发货详情异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    /**
     * 分页查询超级推的机具
     * @param pageNo
     * @param pageSize
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/selectCjtTerPage")
    public Result selectCjtTerPage(@RequestParam(defaultValue = "1") int pageNo,
                                    @RequestParam(defaultValue = "10") int pageSize,
                                    @RequestBody TerminalInfo baseInfo){
        Result result = new Result();
        try {
            Page<TerminalInfo> page = new Page<>(pageNo, pageSize);
            terminalInfoService.selectCjtTerPage(baseInfo, page);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(page);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("分页查询超级推的机具异常", e);
        }
        return result;
    }


}
