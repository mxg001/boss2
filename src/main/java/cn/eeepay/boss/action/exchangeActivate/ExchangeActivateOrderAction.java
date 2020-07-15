package cn.eeepay.boss.action.exchangeActivate;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchange.WriteOffHis;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateOrder;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateOrderService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.ResponseUtil;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/17/017.
 * @author  liuks
 * 兑换订单
 */
@Controller
@RequestMapping(value = "/exchangeActivateOrder")
public class ExchangeActivateOrderAction {
    private static final Logger log = LoggerFactory.getLogger(ExchangeActivateOrderAction.class);

    @Resource
    private ExchangeActivateOrderService exchangeActivateOrderService;

    @Resource
    private SysDictService sysDictService;
    /**
     * 查询兑换订单列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ExchangeActivateOrder> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateOrder order = JSONObject.parseObject(param, ExchangeActivateOrder.class);
            exchangeActivateOrderService.selectAllList(order, page);
            TotalAmount totalAmount=exchangeActivateOrderService.selectSum(order, page);
            msg.put("totalAmount",totalAmount);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询兑换订单列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询兑换订单列表异常!");
        }
        return msg;
    }



    /**
     * 兑换订单详情
     */
    @RequestMapping(value = "/getExchangeOrder")
    @ResponseBody
    public Map<String,Object> getExchangeOrder(@RequestParam("id") long id) throws Exception{
        return getUserDetail(id,0);
    }
    /**
     * 敏感信息获取
     */
    @RequestMapping(value = "/getDataProcessing")
    @ResponseBody
    public Map<String,Object> getDataProcessing(@RequestParam("id") long id) throws Exception{
        return getUserDetail(id,3);
    }

    private Map<String,Object> getUserDetail( long id,int editState){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateOrder order=exchangeActivateOrderService.getExchangeOrder(id);
            if(0==editState){
                order.setMobileUsername(StringUtil.sensitiveInformationHandle(order.getMobileUsername(),0));
                order.setIdCardNo(StringUtil.sensitiveInformationHandle(order.getIdCardNo(),1));
            }
            msg.put("order",order);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询兑换订单详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询兑换订单详情异常!");
        }
        return msg;
    }
    /**
     * 导出兑换订单列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            param=new String(param.getBytes("ISO-8859-1"),"UTF-8");
            ExchangeActivateOrder order = JSONObject.parseObject(param, ExchangeActivateOrder.class);
            List<ExchangeActivateOrder> list=exchangeActivateOrderService.importDetailSelect(order);
            exchangeActivateOrderService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出兑换订单列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出兑换订单列表异常!");
        }
        return msg;
    }

    /**
     * 查询兑换核销管理列表
     */
    @RequestMapping(value = "/selectAuditAll")
    @ResponseBody
    public Map<String,Object> selectAuditByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ExchangeActivateOrder> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateOrder order = JSONObject.parseObject(param, ExchangeActivateOrder.class);
            exchangeActivateOrderService.selectAuditAll(order, page);
            if(page.getResult().size()>0){
                for(ExchangeActivateOrder item:page.getResult()){
                    if(item.getUploadImage()!=null&&!"".equals(item.getUploadImage())){
                        item.setUploadImage(CommonUtil.getImgUrlAgent(item.getUploadImage()));
                    }
                }
            }
            TotalAmount totalAmount=exchangeActivateOrderService.selectAuditSum(order, page);
            msg.put("totalAmount",totalAmount);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询兑换核销管理列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询兑换核销管理列表异常!");
        }
        return msg;
    }

    /**
     * 兑换核销详情
     */
    @RequestMapping(value = "/getAuditExchangeOrder")
    @ResponseBody
    public Map<String,Object> getAuditExchangeOrder(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateOrder order=exchangeActivateOrderService.getAuditExchangeOrder(id);
            if(order!=null&&order.getUploadImage()!=null&&!"".equals(order.getUploadImage())){
                order.setUploadImage(CommonUtil.getImgUrlAgent(order.getUploadImage()));
            }
            String receiveState="0";
            if(order!=null&&order.getLogisticsInfo()!=null&&!"".equals(order.getLogisticsInfo())){
                receiveState="1";
            }
            msg.put("receiveState",receiveState);
            msg.put("order",order);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询兑换核销详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询兑换核销详情异常!");
        }
        return msg;
    }

    /**
     * 核销
     */
    @RequestMapping(value = "/updateWriteOff")
    @ResponseBody
    @SystemLog(description = "核销",operCode="exchangeActivateOrder.updateWriteOff")
    public Map<String,Object> updateWriteOff(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            WriteOffHis writeOff = JSONObject.parseObject(param, WriteOffHis.class);
            int num=exchangeActivateOrderService.updateWriteOff(writeOff);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "核销操作成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "核销操作失败!");
            }
        } catch (Exception e){
            log.error("核销异常!",e);
            msg.put("status", false);
            msg.put("msg", "核销异常!");
        }
        return msg;
    }
    /**
     * 二次核销
     */
    @RequestMapping(value = "/updateAgainWriteOff")
    @ResponseBody
    @SystemLog(description = "二次核销",operCode="exchangeActivateOrder.updateAgainWriteOff")
    public Map<String,Object> updateAgainWriteOff(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            WriteOffHis writeOff = JSONObject.parseObject(param, WriteOffHis.class);
            int num=exchangeActivateOrderService.updateWriteOff(writeOff);
            if(num>0){
                if("1".equals(writeOff.getCheckStatus())){
                    sendCoreVerification(writeOff.getOrderNo());
                }
                msg.put("status", true);
                msg.put("msg", "二次核销操作成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "二次核销操作失败!");
            }
        } catch (Exception e){
            log.error("二次核销异常!",e);
            msg.put("status", false);
            msg.put("msg", "二次核销异常!");
        }
        return msg;
    }
    /**
     * 导出积分兑换核销管理列表
     */
    @RequestMapping(value="/importAuditDetail")
    @ResponseBody
    public Map<String, Object> importAuditDetail(@RequestParam("info") String param,
                                                 @RequestParam("state") int state,
                                                 HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            param=new String(param.getBytes("ISO-8859-1"),"UTF-8");
            ExchangeActivateOrder order = JSONObject.parseObject(param, ExchangeActivateOrder.class);
            List<ExchangeActivateOrder> list=exchangeActivateOrderService.importAuditDetailSelect(order);
            if(state==1){
                exchangeActivateOrderService.importAuditDetail(list,response);
            }else if(state==2){
                exchangeActivateOrderService.importAuditDetailNoImg(list,response);
            }
        }catch (Exception e){
            log.error("导出积分兑换核销管理列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出积分兑换核销管理列表异常!");
        }
        return msg;
    }

    /**
     * 批量核销导入
     */
    @RequestMapping(value="/importDiscount")
    @ResponseBody
    public Map<String, Object> importDiscount(@RequestParam("file") MultipartFile file){
        Map<String, Object> msg = new HashMap<>();
        try {
            if (!file.isEmpty()) {
                String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                if(!format.equals(".xls") && !format.equals(".xlsx")){
                    msg.put("status", false);
                    msg.put("msg", "导入文件格式错误!");
                    return msg;
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "导入文件不能为空!");
                return msg;
            }
            msg = exchangeActivateOrderService.importDiscount(file);
            boolean status=(Boolean)msg.get("status");
            if(status){
                List<WriteOffHis> list=(List<WriteOffHis>)msg.get("list");
                if(list.size()>0){
                    for(WriteOffHis writeOff:list){
                        if("1".equals(writeOff.getCheckStatus())){
                            sendCoreVerification(writeOff.getOrderNo());
                        }
                    }
                }
            }
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "批量核销导入失败!");
            log.error("批量核销导入失败!",e);
        }
        return msg;
    }

    /**
     * 超级兑批量核销导入模板下载
     */
    @RequestMapping("/downloadTemplate")
    public String downloadAdjustAccTemplate(HttpServletRequest request, HttpServletResponse response){
        String filePath = request.getServletContext().getRealPath("/")+ File.separator+"template"+File.separator+"exchangeOrderTemplate.xlsx";
        log.info(filePath);
        ResponseUtil.download(response, filePath,"超级兑批量核销导入模板.xlsx");
        return null;
    }

    /**
     * 核销分润计算
     * @param orderNo
     * @return
     */
    private String  sendCoreVerification(String orderNo){
        String returnStr=null;
        try {
            SysDict sysDict = sysDictService.getByKey("EXCHANGE_ACTIVATE_URL");
            if(sysDict!=null){
                String url=sysDict.getSysValue()+"/declareOrder/verifyDeclaraOrder";
                final HashMap<String, String> claims = new HashMap<>();
                claims.put("orderNo", orderNo);
                log.info("核销,请求路径：{},参数：{}",url, JSONObject.toJSONString(claims));
                returnStr = new ClientInterface(url, claims).postRequest();
                log.info("核销,返回结果：{}", returnStr);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnStr;
    }

    /**
     * 上游查询订单
     */
    @RequestMapping(value = "/orderApiSelect")
    @ResponseBody
    @SystemLog(description = "上游查询订单",operCode="exchangeActivateOrder.orderApiSelect")
    public Map<String,Object> orderApiSelect(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateOrder order=exchangeActivateOrderService.getExchangeOrderLittle(id);
            int sta=exchangeActivateOrderService.orderApiSelect(order,msg);
            if(sta==1){
                sendCoreVerification(order.getOrderNo());
            }
        } catch (Exception e){
            log.error("核销异常!",e);
            msg.put("status", false);
            msg.put("msg", "核销异常!");
        }
        return msg;
    }

    /**
     * 通道核销
     */
    @RequestMapping(value = "/orderApi")
    @ResponseBody
    @SystemLog(description = "通道核销",operCode="exchangeActivateOrder.orderApi")
    public Map<String,Object> orderApi(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        ExchangeActivateOrder order=exchangeActivateOrderService.getExchangeOrderLittle(id);
        if(order!=null){
            String returnStr=sendAPIWriteOff(order.getOrderNo());
            //返回结果处理
            JSONObject json = JSON.parseObject(returnStr);
            if(json!=null){
                if(json.get("status")!=null&&"200".equals(json.getString("status"))){
                    msg.put("status", true);
                    msg.put("msg", "通道核销成功!");
                }else{
                    String errorStr=json.get("msg")==null?"":json.getString("msg");
                    msg.put("status", true);
                    msg.put("msg", "通道核销失败,"+errorStr);
                }
                return msg;
            }
        }
        msg.put("status", false);
        msg.put("msg", "通道核销失败!");
        return msg;
    }

    /**
     * API通道核销
     * @param orderNo
     * @return
     */
    private String  sendAPIWriteOff(String orderNo){
        String returnStr=null;
        try {
            SysDict sysDict = sysDictService.getByKey("EXCHANGE_ACTIVATE_URL");
            if(sysDict!=null){
                String url=sysDict.getSysValue()+"/callBack/globalExchange";
//                url="http://dev.jazeng.cn/callBack/globalExchange";
                HashMap<String, String> claims = new HashMap<>();
                claims.put("order_no", orderNo);
                log.info("API通道核销,请求路径：{},参数：{}",url, JSONObject.toJSONString(claims));
                returnStr = new ClientInterface(url, claims).postRequest();
                log.info("API通道核销,返回结果：{}", returnStr);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnStr;
    }
}
