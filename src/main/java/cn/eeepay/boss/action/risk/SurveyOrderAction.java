package cn.eeepay.boss.action.risk;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.risk.Reminder;
import cn.eeepay.framework.model.risk.SurveyOrder;
import cn.eeepay.framework.model.risk.SurveyReply;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.risk.SurveyOrderService;
import cn.eeepay.framework.service.risk.SurveyReplyService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/7/007.
 * @author  liuks
 * 调单管理
 */
@Controller
@RequestMapping(value = "/surveyOrder")
public class SurveyOrderAction {

    private static final Logger log = LoggerFactory.getLogger(SurveyOrderAction.class);

    @Resource
    private SurveyOrderService surveyOrderService;
    @Resource
    private SurveyReplyService surveyReplyService;

    @Resource
    private SysDictService sysDictService;
    @Resource
    private AgentInfoService agentInfoService;


    /**
     * 查询调单管理列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<SurveyOrder> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SurveyOrder order = JSONObject.parseObject(param, SurveyOrder.class);
            surveyOrderService.selectAllList(order, page);
            List<SurveyOrder> orders = page.getResult();
            addReplyRoleTypeName(orders);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询调单管理列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询调单管理列表异常!");
        }
        return msg;
    }

    /**
     * 新增调单
     */
    @RequestMapping(value = "/addSurveyOrder")
    @ResponseBody
    @SystemLog(description = "新增调单",operCode="surveyOrder.addSurveyOrder")
    public Map<String,Object> addSurveyOrder(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SurveyOrder order = JSONObject.parseObject(param, SurveyOrder.class);
            surveyOrderService.addSurveyOrder(order,msg);
        } catch (Exception e){
            log.error("新增调单异常!",e);
            msg.put("status", false);
            msg.put("msg", "新增调单异常!");
        }
        return msg;
    }


    /**
     * 逻辑删除调单
     */
    @RequestMapping(value = "/deleteSurveyOrder")
    @ResponseBody
    @SystemLog(description = "删除调单",operCode="surveyOrder.deleteSurveyOrder")
    public Map<String,Object> deleteSurveyOrder(@RequestParam("id") int id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=surveyOrderService.deleteSurveyOrder(id);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "删除调单成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "删除调单失败!");
            }
        } catch (Exception e){
            log.error("删除调单异常!",e);
            msg.put("status", false);
            msg.put("msg", "删除调单异常!");
        }
        return msg;
    }
    /**
     * 批量逻辑删除调单
     */
    @RequestMapping(value = "/deleteOrderBatch")
    @ResponseBody
    @SystemLog(description = "批量删除调单",operCode="surveyOrder.deleteOrderBatch")
    public Map<String,Object> deleteOrderBatch(@RequestParam("ids") String ids) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            surveyOrderService.deleteOrderBatch(ids,msg);
        } catch (Exception e){
            log.error("删除调单异常!",e);
            msg.put("status", false);
            msg.put("msg", "删除调单异常!");
        }
        return msg;
    }

    /**
     * 催单
     */
    @RequestMapping(value = "/reminder")
    @ResponseBody
    @SystemLog(description = "催单",operCode="surveyOrder.reminder")
    public Map<String,Object> reminder(@RequestParam("id") int id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<String> list=new ArrayList<String>();
            int num=surveyOrderService.reminder(id,list);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "催单成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "催单失败!");
            }
            sendReminderBatch(list);//调用接口
        } catch (Exception e){
            log.error("催单异常!",e);
            msg.put("status", false);
            msg.put("msg", "催单异常!");
        }
        return msg;
    }

    /**
     * 批量催单
     */
    @RequestMapping(value = "/reminderBatch")
    @ResponseBody
    @SystemLog(description = "批量催单",operCode="surveyOrder.reminderBatch")
    public Map<String,Object> reminderBatch(@RequestParam("ids") String ids) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<String> list=new ArrayList<String>();
            surveyOrderService.reminderBatch(ids,msg,list);
            sendReminderBatch(list);//调用接口
        } catch (Exception e){
            log.error("批量催单异常!",e);
            msg.put("status", false);
            msg.put("msg", "批量催单异常!");
        }
        return msg;
    }


    /**
     * 回退
     */
    @RequestMapping(value = "/regresses")
    @ResponseBody
    @SystemLog(description = "回退",operCode="surveyOrder.regresses")
    public Map<String,Object> regresses(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SurveyOrder order = JSONObject.parseObject(param, SurveyOrder.class);
            int num=surveyOrderService.regresses(order);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "回退成功!");

                SurveyOrder surveyOrder = surveyOrderService.selectById(order.getId());
                //回退后进行极光推送
                String tittle = "回退调单";
                String content = "您有1条回退订单未处理，请及时处理！";

                // 登录的是一级代理商
                if(surveyOrder.getOneAgentNo().equals(surveyOrder.getAgentNo())){
                    surveyOrderService.sendJPush(surveyOrder.getOneAgentNo(), tittle, content, surveyOrder.getOrderNo(),"1");
                }else {
                    // 推送给一级代理商
                    surveyOrderService.sendJPush(surveyOrder.getOneAgentNo(), tittle, content, surveyOrder.getOrderNo(),"1");
                    // 推送给直属代理商
                    surveyOrderService.sendJPush(surveyOrder.getAgentNo(), tittle, content, surveyOrder.getOrderNo(), "2");
                }
                // 3. 推送给商户
                surveyOrderService.sendJPush(surveyOrder.getMerchantNo(), tittle, content, surveyOrder.getOrderNo(),"0");
            }else{
                msg.put("status", false);
                msg.put("msg", "回退失败!");
            }
        } catch (Exception e){
            log.error("回退异常!",e);
            msg.put("status", false);
            msg.put("msg", "回退异常!");
        }
        return msg;
    }

    /**
     * 处理
     */
    @RequestMapping(value = "/handle")
    @ResponseBody
    @SystemLog(description = "处理",operCode="surveyOrder.handle")
    public Map<String,Object> handle(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SurveyOrder order = JSONObject.parseObject(param, SurveyOrder.class);
            int num=surveyOrderService.handle(order);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "处理成功!");

                String dealStatus = order.getDealStatus();
                if(!"2".equals(dealStatus) && !"3".equals(dealStatus) && !"6".equals(dealStatus)){
                    //如果处理结果为非终态则给一级代理商推送提醒
                    SurveyOrder surveyOrder = surveyOrderService.selectById(order.getId());
                    surveyOrderService.sendJPush(surveyOrder.getOneAgentNo(), "调单更新极光推送", "您有1条调单有更新，请及时处理！", surveyOrder.getOrderNo(), "1");
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "处理失败!");
            }
        } catch (Exception e){
            log.error("处理异常!",e);
            msg.put("status", false);
            msg.put("msg", "处理异常!");
        }
        return msg;
    }

    /**
     * 批量处理
     */
    @RequestMapping(value = "/handleBatch")
    @ResponseBody
    @SystemLog(description = "批量处理",operCode="surveyOrder.handleBatch")
    public Map<String,Object> handleBatch(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SurveyOrder order = JSONObject.parseObject(param, SurveyOrder.class);
            surveyOrderService.handleBatch(order,msg);
        } catch (Exception e){
            log.error("处理异常!",e);
            msg.put("status", false);
            msg.put("msg", "处理异常!");
        }
        return msg;
    }

    /**
     * 添加扣款记录
     */
    @RequestMapping(value = "/deduct")
    @ResponseBody
    @SystemLog(description = "添加扣款记录",operCode="surveyOrder.deduct")
    public Map<String,Object> deduct(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SurveyOrder order = JSONObject.parseObject(param, SurveyOrder.class);
            int num=surveyOrderService.deduct(order,msg);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "添加扣款记录成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "添加扣款记录失败!");
            }
        } catch (Exception e){
            log.error("添加扣款记录异常!",e);
            msg.put("status", false);
            msg.put("msg", "添加扣款记录异常!");
        }
        return msg;
    }

    /**
     * 添加上游备注
     */
    @RequestMapping(value = "/upstream")
    @ResponseBody
    @SystemLog(description = "添加上游备注",operCode="surveyOrder.upstream")
    public Map<String,Object> upstream(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SurveyOrder order = JSONObject.parseObject(param, SurveyOrder.class);
            int num=surveyOrderService.upstream(order);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "添加上游备注成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "添加上游备注失败!");
            }
        } catch (Exception e){
            log.error("添加上游备注异常!",e);
            msg.put("status", false);
            msg.put("msg", "添加上游备注异常!");
        }
        return msg;
    }

    /**
     * 导出调单列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        SurveyOrder order = JSONObject.parseObject(param, SurveyOrder.class);

        List<SurveyOrder> list=surveyOrderService.importDetailSelect(order);
        addReplyRoleTypeName(list);
        try {
            surveyOrderService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出调单列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出调单列表异常!");
        }
        return msg;
    }

    /**
     * 调单列表页最后提交回复的类型
     * @param list
     */
    private void addReplyRoleTypeName(List<SurveyOrder> list){
        for (SurveyOrder surveyOrder : list) {
            addReplyRoleTypeName(surveyOrder);
        }
    }

    private void addReplyRoleTypeName(SurveyOrder surveyOrder){
        SurveyReply surveyReply = surveyReplyService.getMaxReply(surveyOrder.getOrderNo());
        if(surveyReply != null){
            if("A".equals(surveyReply.getReplyRoleType())){
                String agentNo = surveyReply.getReplyRoleNo();
                AgentInfo agentInfo = agentInfoService.selectByagentNo(agentNo);
                if(new Integer("1").equals(agentInfo.getAgentLevel())){
                    surveyOrder.setReplyTypeName("一级代理商");
                }else {
                    surveyOrder.setReplyTypeName("所属代理商");
                }
            }else if ("M".equals(surveyReply.getReplyRoleType())){
                surveyOrder.setReplyTypeName("商户");
            }else if("P".equals(surveyReply.getReplyRoleType())){
                surveyOrder.setReplyTypeName("超级盟主");
            }
        }
    }

    /**
     * 获取调单详情
     */
    @RequestMapping(value = "/getSurveyOrderDetail")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> getSurveyOrderDetail(@RequestParam("id") int id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SurveyOrder order=surveyOrderService.getSurveyOrderDetail(id);
            addReplyRoleTypeName(order);
            msg.put("status", true);
            msg.put("order",order);
        } catch (Exception e){
            log.error("获取调单详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取调单详情异常!");
        }
        return msg;
    }

    /**
     * 催单记录
     */
    @RequestMapping(value = "/getRecordList")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> getRecordList(@RequestParam("orderNo") String orderNo) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<Reminder> list=surveyOrderService.getRecordList(orderNo);
            msg.put("status", true);
            msg.put("list",list);
        } catch (Exception e){
            log.error("获取调单异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取调单异常!");
        }
        return msg;
    }

    /**
     * 调单催单,调用超级盟主接口
     * @param list
     * @return
     */
    private String sendReminderBatch(List<String> list){
        String returnStr=null;
        try {
            SysDict sysDict = sysDictService.getByKey("ALLAGENT_SERVICE_URL");
            if(sysDict!=null&&list!=null&&list.size()>0){
                String url=sysDict.getSysValue()+"/orderManager/reminder";
                final HashMap<String, String> claims = new HashMap<>();
                StringBuffer sb=new StringBuffer();
                for(String str:list){
                    if(str!=null&&!"".equals(str)){
                        sb.append(str).append(",");
                    }
                }
                String reStr =sb.toString().substring(0,sb.toString().length()-1);
                claims.put("order_no",reStr);
                log.info("调单催单调用超级盟接口,请求路径:{},参数：{}",url, JSONObject.toJSONString(claims));
                returnStr = ClientInterface.httpPost(url, claims);
                log.info("调单催单调用超级盟接口,返回结果：{}", returnStr);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnStr;
    }
}
