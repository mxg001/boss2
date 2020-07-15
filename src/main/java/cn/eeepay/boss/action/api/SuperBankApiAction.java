package cn.eeepay.boss.action.api;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.SettleOrderInfo;
import cn.eeepay.framework.model.SettleTransfer;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.service.historyquery.SettleOrderInfoHistoryService;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 超级银行家外放接口
 * @author tans
 * @date 2018/11/13 10:09
 */
@RestController
@RequestMapping("/superBankApi")
public class SuperBankApiAction {

    private static final Logger log = LoggerFactory.getLogger(SuperBankApiAction.class);

    @Resource
    private SettleOrderInfoService settleOrderInfoService;

    @Resource
    private AgentInfoService agentInfoService;

    @Resource
    private AgentOemService agentOemService;

    @Resource
    private SuperBankService superBankService;

    @Resource
    private SuperBankApiService superBankApiService;

    @Resource
    private SettleOrderInfoHistoryService settleOrderInfoHistoryService;

    /**
     * 查询出款订单
     * @param paramJson
     * @return
     */
    @RequestMapping(value = "/selectSettleOrderInfo",method = RequestMethod.POST)
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Result selectSettleOrderInfo(@RequestBody JSONObject paramJson){

        log.info("查询出款订单start,param:{}", paramJson.toJSONString());
        //1.解析参数，md5加密校验，是否正常
        //2.解析参数，校验查询时间范围，不得超过一个月
        //3.查询数据
        //4.返回查询结果

        Result result = new Result();
        try {
            //1.解析参数，md5加密校验，是否正常
            if ( !checkSign(paramJson, result)) return result;

            String infoStr = paramJson.getString("info");
//            SettleOrderInfo settleOrderInfo = paramJson.getObject(jsonStr, SettleOrderInfo.class);
            SettleOrderInfo settleOrderInfo=JSONObject.parseObject(infoStr,SettleOrderInfo.class);
            if(settleOrderInfo == null){
                result.setMsg("参数异常");
                return result;
            }

            //2.解析参数，校验查询时间范围，不得超过一个月
            Date sDate = settleOrderInfo.getSdate();
            Date eDate = settleOrderInfo.getEdate();
            int limitDay = 30;
            if(!DateUtil.checkOrderDate(sDate, eDate, result, limitDay)){
                return result;
            }
            int pageNo = paramJson.getInteger("pageNo");
            int pageSize = paramJson.getInteger("pageSize");

            Page<SettleOrderInfo> page = new Page<>(pageNo, pageSize);
            String money;
            if(settleOrderInfo.getHistoryStatus() != null && settleOrderInfo.getHistoryStatus() == 1){
                settleOrderInfoHistoryService.selectAllInfo(page, settleOrderInfo);
                money = settleOrderInfoHistoryService.getTotalNumAndTotalMoney(settleOrderInfo);
            } else {
                settleOrderInfoService.selectAllInfo(page, settleOrderInfo);
                money = settleOrderInfoService.getTotalNumAndTotalMoney(settleOrderInfo);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("page", page);
            map.put("money", money);

            result.setStatus(true);
            result.setCode(200);
            result.setMsg("查询成功");
            result.setData(map);
        } catch (Exception e) {
            log.error("查询出款订单异常", e);
            result.setCode(400);
            result.setMsg("参数异常");
            return result;
        }
        log.info("查询出款订单end");
        return result;
    }

    /**
     * 导出出款订单
     * @param paramJson
     * @return
     */
    @RequestMapping(value = "/exportSettleOrderInfo",method = RequestMethod.POST)
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Result exportSettleOrderInfo(@RequestBody JSONObject paramJson){

        log.info("导出出款订单start,param:{}", paramJson.toJSONString());
        //1.解析参数，md5加密校验，是否正常
        //2.解析参数，校验查询时间范围，不得超过一个月
        //3.查询数据
        //4.返回查询结果

        Result result = new Result();
        try {
            //1.解析参数，md5加密校验，是否正常
            if ( !checkSign(paramJson, result)) return result;

            String jsonStr = paramJson.getString("info");
//            SettleOrderInfo settleOrderInfo = paramJson.getObject(jsonStr, SettleOrderInfo.class);
            SettleOrderInfo settleOrderInfo=JSONObject.parseObject(jsonStr,SettleOrderInfo.class);
            if(settleOrderInfo == null){
                result.setMsg("参数异常");
                return result;
            }

            //2.解析参数，校验查询时间范围，不得超过一个月
            Date sDate = settleOrderInfo.getSdate();
            Date eDate = settleOrderInfo.getEdate();
            int limitDay = 30;
            if(!DateUtil.checkOrderDate(sDate, eDate, result, limitDay)){
                return result;
            }

            List<SettleOrderInfo> list;
            if(settleOrderInfo.getHistoryStatus() != null && settleOrderInfo.getHistoryStatus() == 1){
                list = settleOrderInfoHistoryService.importAllInfo(settleOrderInfo);
            } else {
                list = settleOrderInfoService.importAllInfo(settleOrderInfo);
            }

            result.setStatus(true);
            result.setCode(200);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("导出出款订单异常", e);
            result.setCode(400);
            result.setMsg("参数异常");
            return result;
        }
        log.info("导出出款订单end");
        return result;
    }

    /**
     * 查询出款订单详情
     * @param paramJson
     * @return
     */
    @RequestMapping(value = "/selectSettleOrderDetail",method = RequestMethod.POST)
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Result selectSettleOrderDetail(@RequestBody JSONObject paramJson){

        log.info("查询出款订单详情start,param:{}", paramJson.toJSONString());

        Result result = new Result();
        try {
            //1.解析参数，拿到来源系统，md5加密校验，是否正常
            if ( !checkSign(paramJson, result)) return result;

            String jsonStr = paramJson.getString("info");
            JSONObject infoJson = JSONObject.parseObject(jsonStr);
            String id = infoJson.getString("id");
            Integer historyStatus = infoJson.getInteger("historyStatus");
            if(StringUtils.isBlank(id)){
                result.setMsg("参数不能为空");
                return result;
            }
            SettleOrderInfo settleOrderInfo = null;
            List<SettleTransfer> orderDetailList = null;
            if(historyStatus != null && historyStatus == 1){
                settleOrderInfo = settleOrderInfoHistoryService.selectInfo(id);
                orderDetailList = settleOrderInfoHistoryService.selectSettleInfo(id);
            } else {
                settleOrderInfo = settleOrderInfoService.selectInfo(id);
                orderDetailList = settleOrderInfoService.selectSettleInfo(id);
            }
            if(settleOrderInfo == null){
                result.setMsg("找不到对应的订单");
                return result;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("settleOrderInfo", settleOrderInfo);
            map.put("orderDetailList", orderDetailList);

            result.setStatus(true);
            result.setCode(200);
            result.setMsg("查询成功");
            result.setData(map);
        } catch (Exception e) {
            log.error("查询出款订单详情异常", e);
            result.setCode(400);
            result.setMsg("参数异常");
            return result;
        }
        log.info("查询出款订单详情end");
        return result;
    }

    /**
     * 校验代理商编号是否存在
     *
     * @param paramJson
     * @return
     */
    @RequestMapping(value = "/checkAgentExists", method = RequestMethod.POST)
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Result checkAgentExists(@RequestBody JSONObject paramJson){

        log.info("校验代理商编号是否存在start, param:{}", paramJson.toJSONString());

        Result result = new Result();
        try {
            //校验签名
            if ( !checkSign(paramJson, result)) return result;

            String infoStr = paramJson.getString("info");
            JSONObject infoJson = JSONObject.parseObject(infoStr);
            String agentNo = infoJson.getString("agentNo");
            if(StringUtils.isBlank(agentNo)){
                result.setMsg("代理商编号不能为空");
                return result;
            }
            AgentInfo agentInfo = agentInfoService.getAgentByNo(agentNo);
            result.setCode(200);
            if (agentInfo == null){
                result.setMsg("代理商编号不存在");
            } else {
                result.setData(agentInfo);
                result.setStatus(true);
                result.setMsg("代理商编号存在");
            }
        } catch (Exception e){
            log.error("校验代理商编号是否存在异常", e);
        }
        log.info("校验代理商编号是否存在end");
        return result;
    }

    /**
     * 将组织添加到agent_oem_info表
     * @param paramJson
     * @return
     */
    @RequestMapping(value = "/addOrgOem", method = RequestMethod.POST)
    public Result addOrgOem(@RequestBody JSONObject paramJson){

        log.info("将组织添加到agent_oem_info表start, param:{}", paramJson.toJSONString());

        Result result = new Result();
        try {
            //校验签名
            if ( !checkSign(paramJson, result)) return result;

            String infoStr = paramJson.getString("info");
            JSONObject infoJson = JSONObject.parseObject(infoStr);
            String agentNo = infoJson.getString("agentNo");
            String oemType = infoJson.getString("oemType");
            //检验代理商编号是否存在
            AgentInfo agentInfo = agentInfoService.getAgentByNo(agentNo);
            if (agentInfo == null){
                result.setCode(200);
                result.setMsg("代理商编号不存在");
                return result;
            }

            int count = agentOemService.checkExists(agentNo);
            if(count == 0){
                agentOemService.insert(agentNo, oemType);
                result.setStatus(true);
                result.setCode(200);
                result.setMsg("添加成功");
            } else {
                result.setStatus(true);
                result.setCode(200);
                result.setMsg("代理商编号已存在agent_oem_info,不能再添加");
            }
        } catch (Exception e){
            log.error("将组织添加到agent_oem_info表异常", e);
            result = ResponseUtil.buildResult(e);
        }
        log.info("将组织添加到agent_oem_info表end");
        return result;
    }

    /**
     * 获取出款预警的基本信息设置
     * @return
     */
    @RequestMapping(value = "/getOutWarnInfo", method = RequestMethod.POST)
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Result getOutWarn(@RequestBody JSONObject paramJson){

        log.info("获取出款预警的基本信息设置start, param:{}", paramJson.toJSONString());

        Result result = new Result();
        try {
            //校验签名
            if ( !checkSign(paramJson, result)) return result;

            Map<String, Object> warnInfo = superBankService.getOutWarn();
            if(warnInfo != null){
                result.setStatus(true);
                result.setMsg("查询成功");
                result.setData(warnInfo);
            }
        }catch (Exception e){
            log.info("获取出款预警信息异常", e);
            result = ResponseUtil.buildResult(e);
        }
        log.info("获取出款预警的基本信息设置end");
        return result;
    }

    /**
     * 获取出款预警的余额
     * @return
     */
    @RequestMapping(value = "/getOutWarnAccount", method = RequestMethod.POST)
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Result getOutWarnAccount(@RequestBody JSONObject paramJson){

        log.info("获取出款预警的余额start, param:{}", paramJson.toJSONString());

        Result result = new Result();
        try {
            //校验签名
            if ( !checkSign(paramJson, result)) return result;

            String warnAccount = superBankService.getOutWarnAccount();
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(warnAccount);
        }catch (Exception e){
            log.info("获取出款预警余额异常", e);
            result = ResponseUtil.buildResult(e);
        }
        log.info("获取出款预警的余额end");
        return result;
    }

    /**
     * 修改出款预警信息
     * @param paramJson
     * @return
     */
    @RequestMapping("/updateOutWarn")
//    @SystemLog(operCode = "superbank.updateOutWarn", description = "修改出款预警信息")
    public Result updateOutWarn(@RequestBody JSONObject paramJson){

        log.info("修改出款预警信息start, param:{}", paramJson.toJSONString());

        Result result = new Result();
        try {
            //校验签名
            if ( !checkSign(paramJson, result)) return result;

            String infoStr = paramJson.getString("info");
            Map<String, Object> mapJson = JSONObject.parseObject(infoStr);
            result = superBankService.updateOutWarn(mapJson);
            result.setCode(200);
        }catch (Exception e){
            log.info("修改出款预警信息异常", e);
            result = ResponseUtil.buildResult(e);
        }
        log.info("修改出款预警信息end");
        return result;
    }

    /**
     * 出款账户充值
     * @param paramJson
     * @return
     */
    @RequestMapping("/recharge")
    public Result recharge(@RequestBody JSONObject paramJson){
        Result result = new Result();
        try {
            //校验签名
            if ( !checkSign(paramJson, result)) return result;

            String infoStr = paramJson.getString("info");
            Map<String, Object> mapJson = JSONObject.parseObject(infoStr);
            int num = superBankApiService.recharge(mapJson);
            result.setCode(200);
            if( num == 1){
                result.setStatus(true);
                result.setMsg("操作成功");
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            log.error("出款账户充值异常", e);
        }
        return result;
    }

    /**
     * 查询出款订单明细
     * @param paramJson
     * @return
     */
    @RequestMapping(value = "/selectOutDetailAllInfo",method = RequestMethod.POST)
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Result selectOutDetailAllInfo(@RequestBody JSONObject paramJson){

        log.info("查询出款订单明细start,param:{}", paramJson.toJSONString());
        //1.解析参数，md5加密校验，是否正常
        //2.解析参数，校验查询时间范围，不得超过一个月
        //3.查询数据
        //4.返回查询结果

        Result result = new Result();
        try {
            //1.解析参数，md5加密校验，是否正常
            if ( !checkSign(paramJson, result)) return result;

            String jsonStr = paramJson.getString("info");
            SettleOrderInfo settleOrderInfo = JSONObject.parseObject(jsonStr, SettleOrderInfo.class);
            if(settleOrderInfo == null){
                result.setMsg("参数异常");
                return result;
            }

            //2.解析参数，校验查询时间范围，不得超过一个月
            Date sDate = settleOrderInfo.getSdate();
            Date eDate = settleOrderInfo.getEdate();
            int limitDay = 30;
            if(!DateUtil.checkOrderDate(sDate, eDate, result, limitDay)){
                return result;
            }
            int pageNo = paramJson.getInteger("pageNo");
            int pageSize = paramJson.getInteger("pageSize");

            Page<SettleOrderInfo> page = new Page<>(pageNo, pageSize);

            settleOrderInfoService.selectOutDetailAllInfo(page, settleOrderInfo);
            Map<String, String> count = settleOrderInfoService.getOutDetailTotalMoney(settleOrderInfo);

            Map<String, Object> map = new HashMap<>();
            map.put("page", page);
            map.put("count", count);

            result.setStatus(true);
            result.setCode(200);
            result.setMsg("查询成功");
            result.setData(map);
        } catch (Exception e) {
            log.error("查询出款订单明细异常", e);
            result.setCode(400);
            result.setMsg("参数异常");
            return result;
        }
        log.info("查询出款订单明细end");
        return result;
    }

    /**
     * 查询出款订单明细详情
     * @param paramJson
     * @return
     */
    @RequestMapping(value = "/selectOutInfoDetail",method = RequestMethod.POST)
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Result selectOutInfoDetail(@RequestBody JSONObject paramJson){

        log.info("查询出款订单明细详情start,param:{}", paramJson.toJSONString());
        //1.解析参数，md5加密校验，是否正常
        //2.解析参数
        //3.查询数据
        //4.返回查询结果

        Result result = new Result();
        try {
            //1.解析参数，md5加密校验，是否正常
            if ( !checkSign(paramJson, result)) return result;

            //2.解析参数
            String infoStr = paramJson.getString("info");
            JSONObject infoJson = JSONObject.parseObject(infoStr);
            String id = infoJson.getString("id");
            if(StringUtils.isBlank(id) ){
                result.setMsg("参数异常");
                return result;
            }

            //3.查询数据
            SettleTransfer st = settleOrderInfoService.selectOutSettleInfo(id);
            if(st == null){
                result.setMsg("找不到对应的订单");
                return result;
            }
            SettleOrderInfo soi;
            if(st.getOrderNo()==null||st.getOrderNo().equals("")){
                soi = settleOrderInfoService.selectInfo(st.getTransId());
            }else{
                soi = settleOrderInfoService.selectOrderNoInfo(st.getOrderNo());
            }
            //4.返回查询结果
            Map<String, Object> map = new HashMap<>();
            map.put("soi",soi);
            map.put("st",st);
            result.setStatus(true);
            result.setCode(200);
            result.setMsg("查询成功");
            result.setData(map);
        } catch (Exception e) {
            log.error("查询出款订单明细详情异常", e);
            result.setCode(400);
            result.setMsg("参数异常");
            return result;
        }
        log.info("查询出款订单明细详情end");
        return result;
    }

    /**
     * 导出出款明细
     * @param paramJson
     * @return
     */
    @RequestMapping(value = "/exportOutDetailAllInfo",method = RequestMethod.POST)
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Result exportOutDetailAllInfo(@RequestBody JSONObject paramJson){

        log.info("导出出款明细start,param:{}", paramJson.toJSONString());
        //1.解析参数，md5加密校验，是否正常
        //2.解析参数，校验查询时间范围，不得超过一个月
        //3.查询数据
        //4.返回查询结果

        Result result = new Result();
        try {
            //1.解析参数，md5加密校验，是否正常
            if ( !checkSign(paramJson, result)) return result;

            String jsonStr = paramJson.getString("info");
            SettleOrderInfo settleOrderInfo = JSONObject.parseObject(jsonStr, SettleOrderInfo.class);
            if(settleOrderInfo == null){
                result.setMsg("参数异常");
                return result;
            }

            //2.解析参数，校验查询时间范围，不得超过一个月
            Date sDate = settleOrderInfo.getSdate();
            Date eDate = settleOrderInfo.getEdate();
            int limitDay = 30;
            if(!DateUtil.checkOrderDate(sDate, eDate, result, limitDay)){
                return result;
            }

            List<SettleOrderInfo> list = settleOrderInfoService.exportOutDetailAllInfo(settleOrderInfo);

            result.setStatus(true);
            result.setCode(200);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("导出出款明细异常", e);
            result.setCode(400);
            result.setMsg("参数异常");
            return result;
        }
        log.info("导出出款明细end");
        return result;
    }

    /**
     * 校验请求签名
     * @param paramJson
     * @param result
     * @return
     */
    private boolean checkSign(JSONObject paramJson, Result result) {
        String info = paramJson.getString("info");
        String signParam = paramJson.getString("sign");
        if(StringUtils.isBlank(info) || StringUtils.isBlank(signParam)){
            result.setMsg("参数异常");
            return false;
        }

        String superBankApiKey = SuperBankApiConstant.SUPER_BANK_API_KEY;
        String sign = Md5.md5Str(info + superBankApiKey);
        if(StringUtils.isBlank(signParam) || !signParam.equals(sign)){
            result.setCode(403);
            result.setMsg("请求非法");
            return false;
        }
        return true;
    }


}
