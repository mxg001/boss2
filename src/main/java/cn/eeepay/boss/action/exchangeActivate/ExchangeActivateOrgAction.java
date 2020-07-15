package cn.eeepay.boss.action.exchangeActivate;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateOrg;
import cn.eeepay.framework.model.exchangeActivate.PropertyConfigActivate;
import cn.eeepay.framework.service.RandomNumberService;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivateOrgService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/10/010.
 * @author  liuks
 * 机构查询
 */
@Controller
@RequestMapping(value = "/exchangeActivateOrg")
public class ExchangeActivateOrgAction {

    private static final Logger log = LoggerFactory.getLogger(ExchangeActivateOrgAction.class);

    @Resource
    private ExchangeActivateOrgService exchangeActivateOrgService;

    @Resource
    private RandomNumberService randomNumberService;

    @Resource
    private PropertyConfigActivateService propertyConfigActivateService;

    /**
     * 查询机构列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ExchangeActivateOrg> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateOrg org = JSONObject.parseObject(param, ExchangeActivateOrg.class);
            exchangeActivateOrgService.selectAllList(org, page);
            if(page.getResult().size()>0){
                for(ExchangeActivateOrg org1:page.getResult()){
                    if(org1.getOrgLogo()!=null&&!"".equals(org1.getOrgLogo())){
                        org1.setOrgLogo(CommonUtil.getImgUrlAgent(org1.getOrgLogo()));
                    }
                }
            }
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询机构列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "查询机构列表失败!");
        }
        return msg;
    }

    /**
     * 更新机构状态
     */
    @RequestMapping(value = "/updateOrgStatus")
    @ResponseBody
    @SystemLog(description = "更新机构状态",operCode="exchangeActivateOrg.updateOrgStatus")
    public Map<String,Object> updateOrgStatus(@RequestParam("id") long id, @ModelAttribute("state")
            int state) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            if(id>0){
                int num=exchangeActivateOrgService.updateOrgStatus(id, state);
                if(num>0){
                    msg.put("msg","操作成功!");
                    msg.put("status", true);
                    return msg;
                }
            }
            msg.put("status", false);
            msg.put("msg", "更新机构状态失败!");
        } catch (Exception e){
            log.error("更新机构状态失败!",e);
            msg.put("status", false);
            msg.put("msg", "更新机构状态失败!");
        }
        return msg;
    }

    /**
     * 新增获取字段默认值
     */
    @RequestMapping(value = "/getaddOrgDefault")
    @ResponseBody
    public Map<String,Object> getaddOrgDefault() throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<PropertyConfigActivate> list=propertyConfigActivateService.getOrgConfig("org_config","oem_config");
            msg.put("status", true);
            msg.put("listOrgDefault", list);
        } catch (Exception e){
            log.error("新增获取字段默认值失败!",e);
            msg.put("status", false);
            msg.put("msg", "新增获取字段默认值失败!");
        }
        return msg;
    }

    /**
     * 新增机构
     */
    @RequestMapping(value = "/addOrgManagement")
    @ResponseBody
    @SystemLog(description = "新增机构",operCode="exchangeActivateOrg.addOrgManagement")
    public Map<String,Object> addOrgManagement(@RequestParam("info") String param,@RequestParam("list") String listStr) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateOrg org = JSONObject.parseObject(param, ExchangeActivateOrg.class);
            List<PropertyConfigActivate> list = JSONObject.parseArray(listStr,PropertyConfigActivate.class);
            if(exchangeActivateOrgService.checkOrgName(org)){
                msg.put("status", false);
                msg.put("msg", "机构名称已存在!");
                return msg;
            }
            String code=randomNumberService.getRandomNumber("ORG","activate_org_unique");
            org.setOrgCode(code);
            int num=exchangeActivateOrgService.addOrgManagement(org,list);
            if(num>0){
                msg.put("msg","添加成功!");
                msg.put("status", true);
                return msg;
            }
            msg.put("status", false);
            msg.put("msg", "新增机构失败!");
        } catch (Exception e){
            log.error("新增机构失败!",e);
            msg.put("status", false);
            msg.put("msg", "新增机构失败!");
        }
        return msg;
    }

    /**
     * 机构详情
     */
    @RequestMapping(value = "/getOrgManagementDetail")
    @ResponseBody
    public Map<String,Object> getOrgManagementDetail(@RequestParam("id") long id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateOrg org =exchangeActivateOrgService.getOrgManagementDetail(id);
            if(org.getOrgLogo()!=null&&!"".equals(org.getOrgLogo())){
                org.setOrgLogo(CommonUtil.getImgUrlAgent(org.getOrgLogo()));
            }
            List<PropertyConfigActivate> list=propertyConfigActivateService.getOrgConfigAndValue("org_config","oem_config",org.getOrgCode());
            msg.put("org",org);
            msg.put("listOrgDefault", list);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询机构列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "查询机构列表失败!");
        }
        return msg;
    }
    /**
     * 修改机构
     */
    @RequestMapping(value = "/updateOrgManagement")
    @ResponseBody
    @SystemLog(description = "修改机构",operCode="exchangeActivateOrg.updateOrgManagement")
    public Map<String,Object> updateOrgManagement(@RequestParam("info") String param,@RequestParam("list") String listStr) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExchangeActivateOrg org = JSONObject.parseObject(param, ExchangeActivateOrg.class);
            List<PropertyConfigActivate> list = JSONObject.parseArray(listStr,PropertyConfigActivate.class);
            if(exchangeActivateOrgService.checkOrgName(org)){
                msg.put("status", false);
                msg.put("msg", "机构名称已存在!");
                return msg;
            }
            int num=exchangeActivateOrgService.updateOrgManagement(org,list);
            if(num>0){
                msg.put("msg","修改成功!");
                msg.put("status", true);
                return msg;
            }
            msg.put("status", false);
            msg.put("msg", "修改机构失败!");
        } catch (Exception e){
            log.error("修改机构失败!",e);
            msg.put("status", false);
            msg.put("msg", "修改机构失败!");
        }
        return msg;
    }
    /**
     * 查询机构列表
     */
    @RequestMapping(value = "/getOrgManagementList")
    @ResponseBody
    public Map<String,Object> getOrgManagementList() throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<ExchangeActivateOrg> list=exchangeActivateOrgService.getOrgManagementList();
            msg.put("list",list);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询机构列表失败!",e);
            msg.put("status", false);
            msg.put("msg", "查询机构列表失败!");
        }
        return msg;
    }

    /**
     * 导出积分机构列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @SystemLog(description = "导出积分机构列表",operCode="exchangeActivateOrg.importDetail")
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            param=new String(param.getBytes("ISO-8859-1"),"UTF-8");
            ExchangeActivateOrg org = JSONObject.parseObject(param, ExchangeActivateOrg.class);
            List<ExchangeActivateOrg> list=exchangeActivateOrgService.importDetailSelect(org);
            exchangeActivateOrgService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出积分机构列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出积分机构列表异常!");
        }
        return msg;
    }
}
