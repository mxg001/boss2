package cn.eeepay.boss.action.allAgent;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.allAgent.MachineBuyOrder;
import cn.eeepay.framework.model.allAgent.TerInfo;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.TerminalInfoService;
import cn.eeepay.framework.service.allAgent.MachineBuyOrderService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Md5;
import cn.eeepay.framework.util.ResponseUtil;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWTSigner;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 机具申购订单
 * @author yyao
 */
@Controller
@RequestMapping(value = "/machineBuyOrder")
public class MachineBuyOrderAction {
    private static final Logger log = LoggerFactory.getLogger(MachineBuyOrderAction.class);

    @Resource
    private MachineBuyOrderService machineBuyOrderService;
    @Resource
    private SysDictService sysDictService;
    @Resource
    public TerminalInfoService terminalInfoService;

    /**
     * 申购机具订单查询
     */
    @RequestMapping(value = "/queryMachineBuyOrderList")
    @ResponseBody
    public Map<String,Object> queryMachineBuyOrderList(@RequestParam("info") String param, @ModelAttribute("page")
            Page<MachineBuyOrder> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            MachineBuyOrder info = JSONObject.parseObject(param, MachineBuyOrder.class);
            machineBuyOrderService.queryMachineBuyOrderList(info, page);
            Map<String,Object> pageCount=machineBuyOrderService.queryMachineBuyOrderCount(info);
            msg.put("page",page);
            msg.put("pageCount",pageCount);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询机具申购订单异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询机具申购订单异常!");
        }
        return msg;
    }

    /**
     * 发货机具查询
     */
    @RequestMapping(value = "/querySNList")
    @ResponseBody
    public Map<String,Object> querySNList(@RequestParam("terInfo") String param, @ModelAttribute("page")
            Page<TerInfo> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            TerInfo info = JSONObject.parseObject(param, TerInfo.class);
            machineBuyOrderService.querySNList(info, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询机具申购订单异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询机具申购订单异常!");
        }
        return msg;
    }

    /**
     * 发货机具物料
     */
    @RequestMapping(value = "/sendMachineBuyOrderSN")
    @ResponseBody
    @SystemLog(description = "发货",operCode="machineBuyOrder.sendMachineBuyOrderSN")
    public Map<String,Object> sendMachineBuyOrderSN(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            TerInfo info = JSONObject.parseObject(param,TerInfo.class);
            if(info.getOrderNo()==null||"".equals(info.getOrderNo())
                    ||info.getTransportCompany()==null||"".equals(info.getTransportCompany())
                    ||info.getPostNo()==null||"".equals(info.getPostNo())){
                msg.put("status", false);
                msg.put("msg", "参数错误");
                return msg;
            }
            MachineBuyOrder machineBuyOrder=machineBuyOrderService.queryMachineBuyOrderByOrderNo(info.getOrderNo());
            if(machineBuyOrder==null){
                msg.put("status", false);
                msg.put("msg", "无此订单");
                return msg;
            }else if(machineBuyOrder.getOrderStatus()!=0||machineBuyOrder.getIsPlatform()!=1){
                msg.put("status", false);
                msg.put("msg", "该订单已发货或不是平台发货状态");
                return msg;
            }
            info.setUserCode(machineBuyOrder.getUserCode());
            info.setOneUserCode(machineBuyOrder.getOneUserCode());
            if(machineBuyOrder.getShipWay()==1){
                String sn[]=info.getSn().split(",");
                if(machineBuyOrder.getNum()!=sn.length){
                    msg.put("status", false);
                    msg.put("msg", "当前已选择"+sn.length+"台，机具台数与订单订购数量不符，请确认选择的机具台数。");
                    return msg;
                }
                List<Map<String,String>> list=new ArrayList<Map<String,String>>();
                for(String s:sn){
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("sn",s);
                    list.add(map);
                }
                info.setSn(JSONObject.toJSONString(list));
            }else{
                info.setSn("");
            }
            List<SysDict> sysDictList=sysDictService.selectByKey("AGENT_EXPRESS");
            String transportCompanyName="";
            for(SysDict s:sysDictList){
                if(info.getTransportCompany().equals(s.getSysValue())){
                    transportCompanyName = s.getSysName();
                    info.setTransportCompany(transportCompanyName);
                    break;
                }
            }
            if("".equals(transportCompanyName)){
                msg.put("status", false);
                msg.put("msg", "快递公司错误");
                return msg;
            }
            String result=allAgentSendSN(info);
            if(result==null||"".equals(result)){
                machineBuyOrder=machineBuyOrderService.queryMachineBuyOrderByOrderNo(info.getOrderNo());
                if(machineBuyOrder!=null&&machineBuyOrder.getOrderStatus()!=0){
                    msg.put("status", true);
                    msg.put("msg", "发货成功!");
                    return msg;
                }else{
                    msg.put("status", false);
                    msg.put("msg", "网络异常，发货失败");
                    return msg;
                }
            }else{
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (jsonObject.getInteger("status")!=200) {
                    msg.put("status", false);
                    msg.put("msg", jsonObject.getString("msg"));
                    return msg;
                }
            }
            //发货接口
            msg.put("status", true);
            msg.put("msg", "发货成功!");
            return msg;
        } catch (Exception e){
            log.error("发货异常!",e);
            msg.put("status", false);
            msg.put("msg", "发货异常!");
        }
        return msg;
    }

    /**
     * 批量发货
     * @param file
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sendSNButchUpload")
    public @ResponseBody Object sendSNButchUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        int errorCount=0;
        int successCount=0;
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            if (!file.isEmpty()) {
                String format = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                if (!format.equals(".xls") && !format.equals(".xlsx")) {
                    jsonMap.put("status", false);
                    jsonMap.put("msg", "文件格式错误");
                    return jsonMap;
                }
                Workbook wb = WorkbookFactory.create(file.getInputStream());
                Sheet sheet = wb.getSheetAt(0);
                // 遍历所有单元格，读取单元格
                int row_num = sheet.getLastRowNum();
                if (row_num < 1) {
                    jsonMap.put("status", false);
                    jsonMap.put("msg", "文件内容错误");
                    return jsonMap;
                }
                List<TerInfo> errorlist = new ArrayList<TerInfo>();
                for (int i = 1; i <= row_num; i++) {
                    Row row = sheet.getRow(i);
                    String num1 = getCellValue(row.getCell(0));
                    String num2 = getCellValue(row.getCell(1));
                    String num3 = getCellValue(row.getCell(2));
                    String num4 = getCellValue(row.getCell(3));
                    String num5 = getCellValue(row.getCell(4));

                    String transportCompanyNo=num1;
                    String postNo = num2;
                    String orderNo = num3;
                    String snStart = num4;
                    String snEnd = num5;

                    TerInfo terInfo = new TerInfo();
                    terInfo.setTransportCompany(transportCompanyNo);
                    terInfo.setPostNo(postNo);
                    terInfo.setOrderNo(orderNo);
                    terInfo.setSnStart(snStart);
                    terInfo.setSnEnd(snEnd);
                    if(transportCompanyNo==null||"".equals(transportCompanyNo)){
                        errorCount++;
                        terInfo.setErrorResult("快递公司不能为空");
                        errorlist.add(terInfo);
                        continue;
                    }
                    if(postNo==null||"".equals(postNo)){
                        errorCount++;
                        terInfo.setErrorResult("物流单号不能为空");
                        errorlist.add(terInfo);
                        continue;
                    }
                    if(orderNo==null||"".equals(orderNo)){
                        errorCount++;
                        terInfo.setErrorResult("订单编号不能为空");
                        errorlist.add(terInfo);
                        continue;
                    }
                    MachineBuyOrder machineBuyOrder=machineBuyOrderService.queryMachineBuyOrderByOrderNo(orderNo);
                    if(machineBuyOrder==null){
                        errorCount++;
                        terInfo.setErrorResult("无此订单");
                        errorlist.add(terInfo);
                        continue;
                    }else if(machineBuyOrder.getOrderStatus()!=0||machineBuyOrder.getIsPlatform()!=1){
                        errorCount++;
                        terInfo.setErrorResult("该订单已发货或不是平台发货状态");
                        errorlist.add(terInfo);
                        continue;
                    }
                    terInfo.setUserCode(machineBuyOrder.getUserCode());
                    terInfo.setOneUserCode(machineBuyOrder.getOneUserCode());
                    if(machineBuyOrder.getShipWay()==1){
                        if(snStart==null||"".equals(snStart)){
                            errorCount++;
                            terInfo.setErrorResult("机具SN开始号不能为空");
                            errorlist.add(terInfo);
                            continue;
                        }
                        if(snEnd==null||"".equals(snEnd)){
                            errorCount++;
                            terInfo.setErrorResult("机具SN结束号不能为空");
                            errorlist.add(terInfo);
                            continue;
                        }
                        List<TerInfo> terInfoList=terminalInfoService.querySNByTerInfo(terInfo);
                        List<Map<String,String>> listSN=new ArrayList<Map<String,String>>();
                        if(terInfoList!=null&&terInfoList.size()>0){
                            if(terInfoList.size()!=machineBuyOrder.getNum()){
                                errorCount++;
                                terInfo.setErrorResult("发货机具数量与申购数量不一致");
                                errorlist.add(terInfo);
                                continue;
                            }
                            for (TerInfo t:terInfoList){
                                Map<String,String> map=new HashMap<String,String>();
                                map.put("sn",t.getSn());
                                listSN.add(map);
                            }
                            terInfo.setSn(JSONObject.toJSONString(listSN));
                        }else{
                            errorCount++;
                            terInfo.setErrorResult("下发的机具中存在已经下发过的机具或不存在的SN号");
                            errorlist.add(terInfo);
                            continue;
                        }
                    }else{
                        terInfo.setSn("");
                    }

                    List<SysDict> sysDictList=sysDictService.selectByKey("AGENT_EXPRESS");
                    String transportCompany="";
                    for(SysDict s:sysDictList){
                        if(transportCompanyNo.equals(s.getSysName())){
                            transportCompany = s.getSysValue();
                            break;
                        }
                    }
                    if("".equals(transportCompany)){
                        errorCount++;
                        terInfo.setErrorResult("快递公司错误");
                        errorlist.add(terInfo);
                        continue;
                    }

                    //发货 掉接口
                    String result=allAgentSendSN(terInfo);
                    if(result==null||"".equals(result)){
                        machineBuyOrder=machineBuyOrderService.queryMachineBuyOrderByOrderNo(orderNo);
                        if(machineBuyOrder!=null&&machineBuyOrder.getOrderStatus()!=0){
                            successCount++;
                        }else{
                            errorCount++;
                            terInfo.setErrorResult("网络异常，发货失败");
                            errorlist.add(terInfo);
                            continue;
                        }
                    }else{
                        JSONObject jsonObject = JSONObject.parseObject(result);
                        if (jsonObject.getInteger("status")!=200) {
                            errorCount++;
                            terInfo.setErrorResult(jsonObject.getString("msg"));
                            errorlist.add(terInfo);
                            continue;
                        }
                        successCount++;
                    }
                }
                jsonMap.put("errorCount", errorCount);
                jsonMap.put("successCount", successCount);
                jsonMap.put("errorlist", errorlist);
                jsonMap.put("status", true);
                jsonMap.put("msg", "操作成功");
                return jsonMap;
            } else {
                jsonMap.put("status", false);
                jsonMap.put("msg", "文件格式错误");
            }
        }catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("status", false);
            jsonMap.put("msg", "数据异常");
        }
        return jsonMap;
    }

    /**
     * 下载模板
     */
    @RequestMapping("/downloadTemplate")
    public String downloadMachineBuyOrderTemplate(HttpServletRequest request, HttpServletResponse response) {
        String filePath = request.getServletContext().getRealPath("/") + File.separator + "template" + File.separator
                + "orderSenfSNTemplate.xlsx";
        log.info(filePath);
        ResponseUtil.download(response, filePath, "批量发货机具模板.xls");
        return null;
    }

    public String getCellValue(Cell cell) {
        if(cell!=null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    return String.valueOf(cell.getNumericCellValue());
                case Cell.CELL_TYPE_STRING:
                    return cell.getStringCellValue();
                case Cell.CELL_TYPE_BLANK:
                    return "";
                case Cell.CELL_TYPE_BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case Cell.CELL_TYPE_FORMULA:
                    return cell.getStringCellValue();
            }
        }
        return null;
    }

    /**
     * 超级盟主商机具发货
     * @param terInfo
     * @return
     */
    public String allAgentSendSN(TerInfo terInfo) {
        String url=sysDictService.getValueByKey("ALLAGENT_SERVICE_URL");
        url+="/transOrder/confirmPost";
        Map<String, String> claims = new HashMap<>();
        claims.put("user_code", terInfo.getUserCode());//订单申购用户
        claims.put("current_user_code", terInfo.getOneUserCode());//机构用户
        claims.put("order_no", terInfo.getOrderNo());//订单号
        claims.put("transport_company", terInfo.getTransportCompany());//快递公司
        claims.put("post_no", terInfo.getPostNo());//物流单号
        claims.put("sn_list_array", terInfo.getSn());//机具好json格式数组[{"sn":"12154"},{"sn":"22233"}]
        claims.put("is_app", "0");
        claims.put("sign", Md5.reqSign(claims));//加密
        String accountMsg = ClientInterface.httpPost(url, claims);
        log.info("url:{}，order_no:{}，response:{}",url,terInfo.getOrderNo(),accountMsg);
        return accountMsg;
    }

    /**
     * 批量入账
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/accountEntryMachineBuyOrder")
    @ResponseBody
    @SystemLog(description = "批量入账",operCode="machineBuyOrder.accountEntryMachineBuyOrder")
    public Map<String,Object> accountEntryMachineBuyOrder(@RequestParam("time") String time) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<MachineBuyOrder> list=machineBuyOrderService.queryMachineBuyOrderByTime(time);
            String url=sysDictService.getValueByKey("ACCOUNT_SERVICE_URL");
            //url="http://192.168.3.30:7025";
            url+="/peragentController/peragentDeviceAccount.do";
//            String allAgentValue=sysDictService.getValueByKey("ALLAGENT_SERVICE_URL");
//            String allAgentUrl=allAgentValue+"/activity/terAcc";
            int entryErrorCount=0;
            int entrySuccessCount=0;
            for(MachineBuyOrder m:list){
                MachineBuyOrder machineBuyOrder=new MachineBuyOrder();
                machineBuyOrder.setId(m.getId());
                JSONObject json = JSONObject.parseObject(peragentDeviceAccount(m,url));
                if (json.getBooleanValue("status")) {
                    machineBuyOrder.setEntryRemark(json.getString("msg"));
                    machineBuyOrder.setEntryAmount(m.getGoodsTotal());
                    machineBuyOrder.setEntryStatus("1");
                    machineBuyOrder.setEntryUser(principal.getUsername());
                    machineBuyOrderService.updateMachineBuyOrderAccountEntry(machineBuyOrder);
                    entrySuccessCount++;
                }else{
                    machineBuyOrder.setEntryRemark(json.getString("msg"));
                    machineBuyOrder.setEntryUser(principal.getUsername());
                    machineBuyOrderService.updateMachineBuyOrderAccountEntry(machineBuyOrder);
                    entryErrorCount++;
                }
            }
            msg.put("msg", "批量入账完成!");
            msg.put("entryErrorCount", entryErrorCount);
            msg.put("entrySuccessCount", entrySuccessCount);
            msg.put("status", true);
        } catch (Exception e){
            log.error("批量入账异常!",e);
            msg.put("status", false);
            msg.put("msg", "批量入账异常!");
        }
        return msg;
    }

    public static String allAgentTerAcc(MachineBuyOrder info, String url) {
        Map<String,String> claims=new HashMap<>();
        claims.put("order_no",info.getOrderNo());//订单号
        String response = ClientInterface.httpPost(url, claims);
        log.info("url:{}，response:{}",url,response);
        return response;
    }

    /**
     * 入账
     * @param info
     * @param url
     * @return
     */
    public String peragentDeviceAccount(MachineBuyOrder info,String url) {
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String secret = "zouruijin";
        final long iat = System.currentTimeMillis() / 1000l; // issued at claim
        final long exp = iat + 60L; // expires claim. In this case the token expires in 60 seconds
        final String jti = UUID.randomUUID().toString();
        final JWTSigner signer = new JWTSigner(secret);
        final HashMap<String, Object> claims = new HashMap<String, Object>();

        claims.put("fromSystem", "boss");//来源系统
        claims.put("transDate", sdfTime.format(info.getCreateTime()));//记账日期
        claims.put("amount", StringUtil.filterNull(info.getGoodsTotal()));//代收金额
        claims.put("fromSerialNo", info.getOrderNo());//唯一，建议每笔单号保存
        claims.put("transTypeCode", "000097");//交易码
        claims.put("transOrderNo", info.getOrderNo());//唯一，建议每笔单号保存
        if(info.getAgentNo()!=null&&!"".equals(info.getAgentNo())){
            claims.put("agentNo", info.getAgentNo());//代理商编号
        }else{
            claims.put("agentNo", info.getPosterCode());//代理商编号
        }
        claims.put("exp", exp);
        claims.put("iat", iat);
        claims.put("jti", jti);

        final String token = signer.sign(claims);
        Map<String,String> map=new HashMap<>();
        map.put("token",token);
        String response = ClientInterface.httpPost(url,map);
        log.info("url:{}，orderNo:{}，response:{}",url,info.getOrderNo(),response);
        return response;
    }

    @RequestMapping("/exportMachineBuyOrder")
    public void exportMachineBuyOrder(@RequestParam String param, HttpServletResponse response){
        try {
            MachineBuyOrder info = JSON.parseObject(param,MachineBuyOrder.class);
            machineBuyOrderService.exportMachineBuyOrder(info, response);
        } catch (Exception e) {
            log.info("导出盟主活动返现明细失败,参数:{}");
            log.info(e.toString());
        }
    }

    /**
     * 发货信息查询
     */
    @RequestMapping(value = "/queryShipMachineDetail")
    @ResponseBody
    public Map<String,Object> queryShipMachineDetail(@RequestParam("orderNo") String orderNo, @ModelAttribute("page")
            Page<TerInfo> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            MachineBuyOrder info=machineBuyOrderService.queryMachineBuyOrderByOrderNo(orderNo);
            machineBuyOrderService.queryShipMachineDetail(orderNo, page);
            msg.put("info",info);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询发货信息异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询发货信息异常!");
        }
        return msg;
    }

}
