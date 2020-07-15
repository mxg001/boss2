package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.exchange.*;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.ExchangeOemService;
import cn.eeepay.framework.service.PropertyConfigService;
import cn.eeepay.framework.service.RandomNumberService;
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
@RequestMapping(value = "/exchangeOem")
public class ExchangeOemAction {

    private static final Logger log = LoggerFactory.getLogger(ExchangeOemAction.class);

    @Resource
    private ExchangeOemService exchangeOemService;
    @Resource
    private PropertyConfigService propertyConfigService;
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
            Page<ExchangeOem> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeOem oem = JSONObject.parseObject(param, ExchangeOem.class);
            exchangeOemService.selectAllList(oem, page);
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
            ExchangeConfig ec=randomNumberService.getExchangeConfig("default_oem_no");

            List<PropertyConfig> list1=propertyConfigService.getOrgConfig("oem_pro_config","oem_config");
            List<PropertyConfig> list2=propertyConfigService.getOemConfigAndValue("oem_agent_config","oem_config",ec.getSysValue());
            List<PropertyConfig> list3=propertyConfigService.getOemConfigAndValue("mort_config","oem_config",ec.getSysValue());
            List<PropertyConfig> list4=propertyConfigService.getOemConfigAndValue("share_config","oem_config",ec.getSysValue());
            List<PropertyConfig> list5=propertyConfigService.getOemConfigAndValue("product_config","oem_config",ec.getSysValue());
            List<PropertyConfig> list6=propertyConfigService.getOemConfigAndValue("mer_config","oem_config",ec.getSysValue());

            msg.put("status", true);
            msg.put("fileListDefault", list1);
            msg.put("agentListDefault", list2);
            msg.put("mortListDefault", list3);
            msg.put("shareListDefault", list4);
            msg.put("exchangeListDefault", list5);
            msg.put("merListDefault", list6);
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
    @SystemLog(description = "新增oem",operCode="exchangeOem.addExchangeOem")
    public Map<String,Object> addExchangeOem(@RequestParam("info") String param,
                                             @RequestParam("agentOem") String paramAgent,
                                             @RequestParam("fileList") String listStr1,
                                             @RequestParam("agentList") String listStr2,
                                             @RequestParam("mortList") String listStr3,
                                             @RequestParam("shareList") String listStr4,
                                             @RequestParam("exchangeList") String listStr5,
                                             @RequestParam("merList") String listStr6) throws Exception{

        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeOem oem = JSONObject.parseObject(param, ExchangeOem.class);
            AgentOem agentOem = JSONObject.parseObject(paramAgent, AgentOem.class);
            if(exchangeOemService.checkAgentOem(agentOem)){
                msg.put("status", false);
                msg.put("msg", "新增oem失败,该代理商已开通过oem!");
                return msg;
            }
            getAgentOemTimeId(agentOem);
            List<PropertyConfig> fileList = JSONObject.parseArray(listStr1,PropertyConfig.class);
            List<PropertyConfig> agentList = JSONObject.parseArray(listStr2,PropertyConfig.class);
            List<PropertyConfig> mortList = JSONObject.parseArray(listStr3,PropertyConfig.class);
            List<PropertyConfig> shareList = JSONObject.parseArray(listStr4,PropertyConfig.class);
            List<PropertyConfig> exchangeList = JSONObject.parseArray(listStr5,PropertyConfig.class);
            List<PropertyConfig> merList = JSONObject.parseArray(listStr6,PropertyConfig.class);

            List<PropertyConfig> list=new ArrayList<PropertyConfig>();
            list.addAll(fileList);
            list.addAll(agentList);
            list.addAll(mortList);
            list.addAll(shareList);
            list.addAll(exchangeList);
            list.addAll(merList);
            String code=randomNumberService.getRandomNumber("OEM","oem_unique");
            oem.setOemNo(code);
            int num=exchangeOemService.addExchangeOem(oem,list,agentOem);
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
    private void getAgentOemTimeId(AgentOem agentOem){
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
            ExchangeOem oem=exchangeOemService.getExchangeOem(id);
            if(oem!=null){
                List<PropertyConfig> list1=propertyConfigService.getOemConfigAndValue("oem_pro_config","oem_config",oem.getOemNo());
                List<PropertyConfig> list2=propertyConfigService.getOemConfigAndValue("oem_agent_config","oem_config",oem.getOemNo());
                List<PropertyConfig> list3=propertyConfigService.getOemConfigAndValue("mort_config","oem_config",oem.getOemNo());
                List<PropertyConfig> list4=propertyConfigService.getOemConfigAndValue("share_config","oem_config",oem.getOemNo());
                List<PropertyConfig> list5=propertyConfigService.getOemConfigAndValue("product_config","oem_config",oem.getOemNo());
                List<PropertyConfig> list6=propertyConfigService.getOemConfigAndValue("mer_config","oem_config",oem.getOemNo());

                AgentOem  agentOem=exchangeOemService.getAgentOem(oem.getOemNo(),"1");
                if(list1!=null&&list1.size()>0){
                    for(PropertyConfig pc:list1){
                        if(pc.getConfigValue()!=null&&!"".equals(pc.getConfigValue())){
                            pc.setConfigValue(CommonUtil.getImgUrlAgent(pc.getConfigValue()));
                        }
                    }
                }
                msg.put("status", true);
                msg.put("oem", oem);
                msg.put("fileListDefault", list1);
                msg.put("agentListDefault", list2);
                msg.put("mortListDefault", list3);
                msg.put("shareListDefault", list4);
                msg.put("exchangeListDefault",list5);
                msg.put("merListDefault",list6);
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
    @SystemLog(description = "修改oem",operCode="exchangeOem.updateExchangeOem")
    public Map<String,Object> updateExchangeOem(@RequestParam("info") String param,
                                                @RequestParam("agentOem") String paramAgent,
                                                @RequestParam("fileList") String listStr1,
                                                @RequestParam("agentList") String listStr2,
                                                @RequestParam("mortList") String listStr3,
                                                @RequestParam("shareList") String listStr4,
                                                @RequestParam("exchangeList") String listStr5,
                                                @RequestParam("merList") String listStr6) throws Exception{

        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeOem oem = JSONObject.parseObject(param, ExchangeOem.class);
            AgentOem agentOem = JSONObject.parseObject(paramAgent, AgentOem.class);
            //getAgentOemTimeId(agentOem);
            List<PropertyConfig> fileList = JSONObject.parseArray(listStr1,PropertyConfig.class);
            List<PropertyConfig> agentList = JSONObject.parseArray(listStr2,PropertyConfig.class);
            List<PropertyConfig> mortList = JSONObject.parseArray(listStr3,PropertyConfig.class);
            List<PropertyConfig> shareList = JSONObject.parseArray(listStr4,PropertyConfig.class);
            List<PropertyConfig> exchangeList = JSONObject.parseArray(listStr5,PropertyConfig.class);
            List<PropertyConfig> merList = JSONObject.parseArray(listStr6,PropertyConfig.class);

            List<PropertyConfig> list=new ArrayList<PropertyConfig>();
            list.addAll(agentList);
            list.addAll(mortList);
            list.addAll(shareList);
            list.addAll(exchangeList);
            list.addAll(merList);

            int num=exchangeOemService.updateExchangeOem(oem,list,fileList,agentOem);
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
            List<ExchangeOem> list=exchangeOemService.getOemList();
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
            Page<ProductOem> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ProductOem proOem = JSONObject.parseObject(param, ProductOem.class);
            exchangeOemService.selectProductOemList(proOem, page);
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
    @SystemLog(description = "OEM产品上下架",operCode="exchangeOem.updateProductOemShelve")
    public Map<String,Object> updateProductOemShelve(@RequestParam("id") long id,@RequestParam("state") String state) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=exchangeOemService.updateProductOemShelve(id,state);
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
    @SystemLog(description = "OEM产品批量上/下架",operCode="exchangeOem.updateProductOemShelveBatch")
    public Map<String,Object> updateProductOemShelveBatch(@RequestParam("ids") String ids,@RequestParam("state") String state) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            exchangeOemService.updateProductOemShelveBatch(ids,state,msg);
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
    @SystemLog(description = "OEM产品新增",operCode="exchangeOem.addProductOem")
    public Map<String,Object> addProductOem(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ProductOem proOem = JSONObject.parseObject(param, ProductOem.class);
            if(exchangeOemService.checkProductOem(proOem)){
                msg.put("status", false);
                msg.put("msg", "该OEM下已存在该产品!");
                return msg;
            }
            int num=exchangeOemService.addProductOem(proOem);
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
            ProductOem proOem =exchangeOemService.getProductOemDetail(id);
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
    @SystemLog(description = "OEM产品修改",operCode="exchangeOem.updateProductOem")
    public Map<String,Object> updateProductOem(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ProductOem proOem = JSONObject.parseObject(param, ProductOem.class);
            if(exchangeOemService.checkProductOem(proOem)){
                msg.put("status", false);
                msg.put("msg", "该OEM下已存在该产品!");
                return msg;
            }
            int num=exchangeOemService.updateProductOem(proOem);
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
    @SystemLog(description = "OEM产品修改",operCode="exchangeOem.synchronizationProductOem")
    public Map<String,Object> synchronizationProductOem(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            exchangeOemService.synchronizationProductOem(id,msg);
        } catch (Exception e){
            log.error("同步直营的产品异常!",e);
            msg.put("status", false);
            msg.put("msg", "同步直营的产品异常!");
        }
        return msg;
    }

    /**
     * 导出oem列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @SystemLog(description = "导出oem列表",operCode="exchangeOem.importDetail")
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            param=new String(param.getBytes("ISO-8859-1"),"UTF-8");
            ExchangeOem oem = JSONObject.parseObject(param, ExchangeOem.class);
            List<ExchangeOem> list=exchangeOemService.importDetailSelect(oem);
            exchangeOemService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出oem列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出oem列表异常!");
        }
        return msg;
    }
}
