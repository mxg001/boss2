package cn.eeepay.boss.action.cjt;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.cjt.CjtAfterSale;
import cn.eeepay.framework.service.cjt.CjtAfterSaleService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tans
 * @date 2019-06-06
 */
@RestController
@RequestMapping("/cjtAfterSale")
public class CjtAfterSaleAction {

    private static final Logger log = LoggerFactory.getLogger(CjtAfterSaleAction.class);

    @Resource
    private CjtAfterSaleService cjtAfterSaleService;

    /**
     * 条件查询售后订单
     * @param baseInfo
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/selectPage")
    public Result selectPage(@RequestBody CjtAfterSale baseInfo,
                                     @RequestParam(defaultValue = "1") int pageNo,
                                     @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        try {
            Page<CjtAfterSale> page = new Page<>(pageNo, pageSize);
            cjtAfterSaleService.selectPage(page, baseInfo);

            Map<String, Object> totalMap = cjtAfterSaleService.selectTotal(baseInfo);

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("page", page);
            dataMap.put("totalMap", totalMap);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(dataMap);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("条件查询售后订单异常", e);
        }
        return result;
    }

    /**
     * 导出申领售后订单
     * @param baseInfo
     * @param response
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/export")
    @SystemLog(operCode = "cjtAfterSale.export", description = "导出")
    public void export(String baseInfo, HttpServletResponse response){
        try {
            if(StringUtils.isNotEmpty(baseInfo)){
                baseInfo = URLDecoder.decode(baseInfo, "utf-8");
            }
            CjtAfterSale cjtAfterSale = JSONObject.parseObject(baseInfo, CjtAfterSale.class);
            cjtAfterSaleService.export(response, cjtAfterSale);
        } catch (Exception e){
            log.error("导出申领售后订单异常", e);
        }
    }

    /**
     * 立即处理
     * 将待品台处理的订单，置为售后中
     * @param orderNo
     * @return
     */
    @RequestMapping("/updateStatus")
    @SystemLog(operCode = "cjtAfterSale.updateStatus", description = "立即处理")
    public Result updateStatus( @RequestParam("orderNo") String orderNo){
        Result result;
        try {
            if(StringUtils.isEmpty(orderNo)){
                return Result.fail("参数有误");
            }
            //校验订单是否已取消，如果已取消，则无需进行售后
            cjtAfterSaleService.checkStatus(orderNo);
            int num = cjtAfterSaleService.updateStatus(orderNo, CjtAfterSale.Status_Dealing);
            if(num == 1) {
                result = Result.success();
            } else {
                result = Result.fail();
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("立即处理售后订单异常", e);
        }
        return result;
    }

    /**
     * 继续处理
     * 提交售后处理信息
     * @param baseInfo
     * @return
     */
    @RequestMapping("/deal")
    @SystemLog(operCode = "cjtAfterSale.deal", description = "继续处理")
    public Result deal( @RequestBody CjtAfterSale baseInfo){
        Result result;
        try {
            String dealRemark = baseInfo.getDealRemark();
            if(StringUtils.isEmpty(dealRemark)) {
                return Result.fail("请输入处理结果说明，不超过200字");
            }
            if(dealRemark.length() > 200) {
                return Result.fail("请输入处理结果说明，不超过200字");
            }
            //校验订单是否已取消，如果已取消，则无需进行售后
            cjtAfterSaleService.checkStatus(baseInfo.getOrderNo());
            int num = cjtAfterSaleService.deal(baseInfo);
            if(num == 1) {
                result = Result.success();
            } else {
                result = Result.fail();
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("继续处理售后订单异常", e);
        }
        return result;
    }

}
