package cn.eeepay.boss.action.exchangeActivate;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.exchange.ExchangeConfig;
import cn.eeepay.framework.model.exchangeActivate.*;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.RandomNumberService;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateOemService;
import cn.eeepay.framework.service.exchangeActivate.PropertyConfigActivateService;
import cn.eeepay.framework.util.CommonUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/13/013.
 * @author  liuks
 * 超级兑 组织oem
 */
@Controller
@RequestMapping(value = "/exchangeActivateOem")
public class ExchangeActivateOemAction {

    private static final Logger log = LoggerFactory.getLogger(ExchangeActivateOemAction.class);

    @Resource
    private ExchangeActivateOemService exchangeActivateOemService;

    @Resource
    private PropertyConfigActivateService propertyConfigActivateService;

    @Resource
    private RandomNumberService randomNumberService;

    @Resource
    private AgentInfoService agentInfoService;
    /**
     * 查询oem列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ExchangeActivateOem> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateOem oem = JSONObject.parseObject(param, ExchangeActivateOem.class);
            exchangeActivateOemService.selectAllList(oem, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询oem列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "查询oem列表失败!");
        }
        return msg;
    }

    /**
     * 新增获取字段默认值
     */
    @RequestMapping(value = "/getOemDefault")
    @ResponseBody
    public Map<String,Object> getOemDefault() throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeConfig ec=randomNumberService.getExchangeConfig("activate_oem_no");

            List<PropertyConfigActivate> list1=propertyConfigActivateService.getOrgConfig("oem_pro_config","oem_config");
            List<PropertyConfigActivate> list2=propertyConfigActivateService.getOemConfigAndValue("share_config","oem_config",ec.getSysValue());
            msg.put("status", true);
            msg.put("fileListDefault", list1);
            msg.put("shareListDefault", list2);
        } catch (Exception e){
            log.error("新增获取字段默认值失败!",e);
            msg.put("status", false);
            msg.put("msg", "新增获取字段默认值失败!");
        }
        return msg;
    }
    /**
     * 新增oem
     */
    @RequestMapping(value = "/addExchangeOem")
    @ResponseBody
    @SystemLog(description = "新增oem",operCode="exchangeActivateOem.addExchangeOem")
    public Map<String,Object> addExchangeOem(@RequestParam("info") String param,
                                             @RequestParam("agentOem") String paramAgent,
                                             @RequestParam("fileList") String listStr1,
                                             @RequestParam("shareList") String listStr2) throws Exception{

        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateOem oem = JSONObject.parseObject(param, ExchangeActivateOem.class);
            AgentOemActivate  agentOem = JSONObject.parseObject(paramAgent, AgentOemActivate.class);
            if(exchangeActivateOemService.checkAgentOem(agentOem)){
                msg.put("status", false);
                msg.put("msg", "新增oem失败,该代理商已开通过oem!");
                return msg;
            }
            getAgentOemTimeId(agentOem);
            List<PropertyConfigActivate> fileList = JSONObject.parseArray(listStr1,PropertyConfigActivate.class);
            List<PropertyConfigActivate> shareList = JSONObject.parseArray(listStr2,PropertyConfigActivate.class);
            List<PropertyConfigActivate> list=new ArrayList<PropertyConfigActivate>();
            list.addAll(fileList);
            list.addAll(shareList);
            String code=randomNumberService.getRandomNumber("OEM","activate_oem_unique");
            oem.setOemNo(code);
            int num=exchangeActivateOemService.addExchangeOem(oem,list,agentOem);
            if(num>0){
                msg.put("msg","新增成功!");
                msg.put("status", true);
                return msg;
            }
            msg.put("status", false);
            msg.put("msg", "新增oem失败!");
        } catch (Exception e){
            log.error("新增oem失败!",e);
            msg.put("status", false);
            msg.put("msg", "新增oem失败!");
        }
        return msg;
    }
    private void getAgentOemTimeId(AgentOemActivate  agentOem){
        AgentInfo agentInfo =agentInfoService.getAgentByNo(agentOem.getAgentNo());
        agentOem.setTeamId(agentInfo.getTeamId()+"");
        agentOem.setAgentNode(agentInfo.getAgentNode());
    }

    /**
     * 获取OEM详情
     */
    @RequestMapping(value = "/getExchangeOem")
    @ResponseBody
    public Map<String,Object> getExchangeOem(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateOem oem=exchangeActivateOemService.getExchangeOem(id);
            if(oem!=null){
                List<PropertyConfigActivate> list1=propertyConfigActivateService.getOemConfigAndValue("oem_pro_config","oem_config",oem.getOemNo());
                List<PropertyConfigActivate> list2=propertyConfigActivateService.getOemConfigAndValue("share_config","oem_config",oem.getOemNo());
                AgentOemActivate   agentOem=exchangeActivateOemService.getAgentOem(oem.getOemNo(),"1");
                if(list1!=null&&list1.size()>0){
                    for(PropertyConfigActivate pc:list1){
                        if(pc.getConfigValue()!=null&&!"".equals(pc.getConfigValue())){
                            pc.setConfigValue(CommonUtil.getImgUrlAgent(pc.getConfigValue()));
                        }
                    }
                }
                msg.put("status", true);
                msg.put("oem", oem);
                msg.put("fileListDefault", list1);
                msg.put("shareListDefault", list2);
                msg.put("agentOem", agentOem);
            }
        } catch (Exception e){
            log.error("获取OEM信息失败!",e);
            msg.put("status", false);
            msg.put("msg", "获取OEM信息失败!");
        }
        return msg;
    }

    /**
     * 更新oem
     */
    @RequestMapping(value = "/updateExchangeOem")
    @ResponseBody
    @SystemLog(description = "修改oem",operCode="exchangeActivateOem.updateExchangeOem")
    public Map<String,Object> updateExchangeOem(@RequestParam("info") String param,
                                                @RequestParam("agentOem") String paramAgent,
                                                @RequestParam("fileList") String listStr1,
                                                @RequestParam("shareList") String listStr2) throws Exception{

        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateOem oem = JSONObject.parseObject(param, ExchangeActivateOem.class);
            AgentOemActivate agentOem = JSONObject.parseObject(paramAgent, AgentOemActivate.class);
            //getAgentOemTimeId(agentOem);
            List<PropertyConfigActivate> fileList = JSONObject.parseArray(listStr1,PropertyConfigActivate.class);
            List<PropertyConfigActivate> shareList = JSONObject.parseArray(listStr2,PropertyConfigActivate.class);

            List<PropertyConfigActivate> list=new ArrayList<PropertyConfigActivate>();
            list.addAll(shareList);
            int num=exchangeActivateOemService.updateExchangeOem(oem,list,fileList,agentOem);
            if(num>0){
                msg.put("msg","修改成功!");
                msg.put("status", true);
                return msg;
            }
            msg.put("status", false);
            msg.put("msg", "修改oem失败!");
        } catch (Exception e){
            log.error("修改oem失败!",e);
            msg.put("status", false);
            msg.put("msg", "修改oem失败!");
        }
        return msg;
    }

    /**
     * 获取OEM列表
     */
    @RequestMapping(value = "/getOemList")
    @ResponseBody
    public Map<String,Object> getOemList() throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<ExchangeActivateOem> list=exchangeActivateOemService.getOemList();
            msg.put("list",list);
            msg.put("status", true);
        } catch (Exception e){
            log.error("获取OEM列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "获取OEM列表失败!");
        }
        return msg;
    }

    /**
     * 查询oem产品列表
     */
    @RequestMapping(value = "/selectProductOemList")
    @ResponseBody
    public Map<String,Object> selectProductOemList(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ProductActivateOem> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ProductActivateOem proOem = JSONObject.parseObject(param, ProductActivateOem.class);
            exchangeActivateOemService.selectProductOemList(proOem, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询oem产品列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "查询oem产品列表失败!");
        }
        return msg;
    }

    /**
     * OEM产品上下架
     */
    @RequestMapping(value = "/updateProductOemShelve")
    @ResponseBody
    @SystemLog(description = "OEM产品上下架",operCode="exchangeActivateOem.updateProductOemShelve")
    public Map<String,Object> updateProductOemShelve(@RequestParam("id") long id,@RequestParam("state") String state) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=exchangeActivateOemService.updateProductOemShelve(id,state);
            if(num>0){
                if("1".equals(state)){
                    msg.put("msg","上架成功!");
                }else{
                    msg.put("msg","下架成功!");
                }
                msg.put("status", true);
            }else{
                if("1".equals(state)){
                    msg.put("msg","上架失败!");
                }else{
                    msg.put("msg","下架失败!");
                }
                msg.put("status", false);
            }
        } catch (Exception e){
            log.error("OEM产品上下架失败!",e);
            msg.put("status", false);
            msg.put("msg", "OEM产品上下架失败!");
        }
        return msg;
    }
    /**
     * OEM产品批量上/下架
     */
    @RequestMapping(value = "/updateProductOemShelveBatch")
    @ResponseBody
    @SystemLog(description = "OEM产品批量上/下架",operCode="exchangeActivateOem.updateProductOemShelveBatch")
    public Map<String,Object> updateProductOemShelveBatch(@RequestParam("ids") String ids,@RequestParam("state") String state) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            exchangeActivateOemService.updateProductOemShelveBatch(ids,state,msg);
        } catch (Exception e){
            log.error("OEM产品批量上/下架异常!",e);
            msg.put("status", false);
            msg.put("msg", "OEM产品批量上/下架异常!");
        }
        return msg;
    }

    /**
     * OEM产品新增
     */
    @RequestMapping(value = "/addProductOem")
    @ResponseBody
    @SystemLog(description = "OEM产品新增",operCode="exchangeActivateOem.addProductOem")
    public Map<String,Object> addProductOem(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ProductActivateOem proOem = JSONObject.parseObject(param, ProductActivateOem.class);
            if(exchangeActivateOemService.checkProductOem(proOem)){
                msg.put("status", false);
                msg.put("msg", "该OEM下已存在该产品!");
                return msg;
            }
            int num=exchangeActivateOemService.addProductOem(proOem);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "OEM产品新增成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "OEM产品新增失败!");
            }
        } catch (Exception e){
            log.error("OEM产品新增失败!",e);
            msg.put("status", false);
            msg.put("msg", "OEM产品新增失败!");
        }
        return msg;
    }
    /**
     * OEM产品详情
     */
    @RequestMapping(value = "/getProductOemDetail")
    @ResponseBody
    public Map<String,Object> getProductOemDetail(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ProductActivateOem proOem =exchangeActivateOemService.getProductOemDetail(id);
            msg.put("status", true);
            msg.put("proOem", proOem);
        } catch (Exception e){
            log.error("获取OEM产品详情失败!",e);
            msg.put("status", false);
            msg.put("msg", "获取OEM产品详情失败!");
        }
        return msg;
    }

    /**
     * OEM产品修改
     */
    @RequestMapping(value = "/updateProductOem")
    @ResponseBody
    @SystemLog(description = "OEM产品修改",operCode="exchangeActivateOem.updateProductOem")
    public Map<String,Object> updateProductOem(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ProductActivateOem proOem = JSONObject.parseObject(param, ProductActivateOem.class);
            if(exchangeActivateOemService.checkProductOem(proOem)){
                msg.put("status", false);
                msg.put("msg", "该OEM下已存在该产品!");
                return msg;
            }
            int num=exchangeActivateOemService.updateProductOem(proOem);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "OEM产品修改成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "OEM产品修改失败!");
            }
        } catch (Exception e){
            log.error("OEM产品修改失败!",e);
            msg.put("status", false);
            msg.put("msg", "OEM产品修改失败!");
        }
        return msg;
    }

    /**
     * 同步直营的产品
     */
    @RequestMapping(value = "/synchronizationProductOem")
    @ResponseBody
    @SystemLog(description = "OEM产品修改",operCode="exchangeActivateOem.synchronizationProductOem")
    public Map<String,Object> synchronizationProductOem(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            exchangeActivateOemService.synchronizationProductOem(id,msg);
        } catch (Exception e){
            log.error("同步直营的产品异常!",e);
            msg.put("status", false);
            msg.put("msg", "同步直营的产品异常!");
        }
        return msg;
    }

    /**
     * 开通超级还组织/商户收款组织
     */
    @RequestMapping(value = "/openUpOem")
    @ResponseBody
    @SystemLog(description = "开通超级还组织/商户收款组织",operCode="exchangeActivateOem.openUpOem")
    public Map<String,Object> openUpOem(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            exchangeActivateOemService.openUpOem(id,msg);
        } catch (Exception e){
            log.error("开通超级还组织/商户收款组织异常!",e);
            msg.put("status", false);
            msg.put("msg", "开通超级还组织/商户收款组织异常!");
        }
        return msg;
    }

    /**
     * 导出oem列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @SystemLog(description = "导出oem列表",operCode="exchangeActivateOem.importDetail")
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            param=new String(param.getBytes("ISO-8859-1"),"UTF-8");
            ExchangeActivateOem oem = JSONObject.parseObject(param, ExchangeActivateOem.class);
            List<ExchangeActivateOem> list=exchangeActivateOemService.importDetailSelect(oem);
            exchangeActivateOemService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出oem列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出oem列表异常!");
        }
        return msg;
    }
}
