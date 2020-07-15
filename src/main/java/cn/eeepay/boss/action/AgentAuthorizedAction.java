package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.AgentAuthorizedService;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.util.ResponseUtil;
import cn.eeepay.framework.util.StringUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping(value = "/agentAuth")
public class AgentAuthorizedAction {
    private static final Logger log = LoggerFactory.getLogger(AgentAuthorizedAction.class);
    @Resource
    public AgentInfoService agentInfoService;
    @Resource
    public AgentAuthorizedService agentAuthorizedService;

    /**
     * 三方授权管理新增
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/addRecord")
    @ResponseBody
    @SystemLog(description = "三方授权管理新增",operCode="agentAuth.addRecord")
    public Object addRecord(HttpServletRequest request, HttpServletResponse response) {
        String logTag = IdUtil.simpleUUID();
        String data = request.getParameter("data");
        JSONObject dataJS = JSONObject.parseObject(data);
        log.info(String.format("[%s][%s]", logTag, dataJS));
        Map result = new HashMap();
        UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        dataJS.put("record_creator", principal.getUsername());
        dataJS.put("record_status", "0");
        dataJS.put("record_code", IdUtil.simpleUUID());
        result = checkData(dataJS);
        if (result != null) {
            return result;
        }
        result = new HashMap();
        try {
            Map agentLinkMap=agentAuthorizedService.selectAgentAuthorizedAgentLink(dataJS.getString("agent_link"));
            if(agentLinkMap!=null){
                result.put("msg", "代理商已经有直属上级，不可关联多个上级");
                result.put("status", false);
                return result;
            }
            List<Map<String,Object>> agentLinkList=agentAuthorizedService.getAgentAuthorized(dataJS.getString("agent_link"));
            if(agentLinkList.size()>0&&"1".equals(agentLinkList.get(0).get("is_top").toString())){
                result.put("msg", "授权代理商不能为顶级代理商");
                result.put("status", false);
                return result;
            }
            List<Map<String,Object>> list=agentAuthorizedService.getAgentAuthorized(dataJS.getString("agent_authorized"));
            if(list.size()>0&&"1".equals(list.get(0).get("is_top").toString())){
                dataJS.put("agent_node", dataJS.getString("agent_authorized")+"-"+dataJS.getString("agent_link"));
                if(StringUtil.isBlank(list.get(0).get("agent_link"))){
                    list.get(0).put("agent_link", dataJS.getString("agent_link"));
                    list.get(0).put("agent_node", dataJS.getString("agent_authorized")+"-"+dataJS.getString("agent_link"));
                    int rows = agentAuthorizedService.upRecord(list.get(0));
                    result.put("msg", rows == 1 ? "新增成功" : "新增失败");
                    result.put("status", rows == 1);
                    return result;
                }else{
                    dataJS.put("top_agent",list.get(0).get("top_agent"));
                    dataJS.put("link_level",list.get(0).get("link_level"));
                    dataJS.put("is_top",list.get(0).get("is_top"));
                    int rows = agentAuthorizedService.addRecord(dataJS.toJavaObject(Map.class));
                    result.put("msg", rows == 1 ? "新增成功" : "新增失败");
                    result.put("status", rows == 1);
                    return result;
                }
            }
            Map map=agentAuthorizedService.selectAgentAuthorizedAgentLink(dataJS.getString("agent_authorized"));
            if(map==null){
                result.put("msg", "该代理商未与上级代理商关联，请先与上级关联");
                result.put("status", false);
                return result;
            }
            if("0".equals(map.get("record_status").toString())&&"1".equals(map.get("record_check").toString())){
                result.put("msg", "请打开上级关联开关");
                result.put("status", false);
                return result;
            }
            if (Integer.parseInt(map.get("link_level").toString())+1>5){
                result.put("msg", "三方关联层级不能大于5级");
                result.put("status", false);
                return result;
            }
            dataJS.put("agent_node", map.get("agent_node").toString()+"-"+dataJS.getString("agent_link"));
            dataJS.put("top_agent",map.get("top_agent"));
            dataJS.put("link_level",Integer.parseInt(map.get("link_level").toString())+1);
            dataJS.put("is_top",2);
            int rows = agentAuthorizedService.addRecord(dataJS.toJavaObject(Map.class));
            result.put("msg", rows == 1 ? "新增成功" : "新增失败");
            result.put("status", rows == 1);
            return result;
        } catch (DuplicateKeyException de) {
            result.put("msg", "已经存在相同配置");
            result.put("status", false);
        } catch (Exception e) {
            result.put("msg", "新增失败,代码[" + logTag + "]");
            result.put("status", false);
            log.error("logTag", e);
        }
        return result;
    }

    /**
     * 三方授权管理
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getDatas")
    @ResponseBody
    public Object getDatas(HttpServletRequest request, HttpServletResponse response) {
        String logTag = IdUtil.simpleUUID();
        String data = request.getParameter("data");
        JSONObject dataJS = JSONObject.parseObject(data);
        log.info(String.format("[%s][%s]", logTag, dataJS));
        Map result = new HashMap();
        JSONObject baseInfoJS = dataJS.getJSONObject("baseInfo");
        JSONObject pageJS = dataJS.getJSONObject("page");
        Page page = pageJS.toJavaObject(Page.class);
        try {
            agentAuthorizedService.getDatas(baseInfoJS.toJavaObject(Map.class), page);
        } catch (Exception e) {
            log.error("查询代理商异常！", e);
        }
        return page;
    }


    /**
     * 三方授权管理删除
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delRecord")
    @ResponseBody
    @SystemLog(description = "三方授权管理删除",operCode="agentAuth.delRecord")
    public Object delRecord(HttpServletRequest request, HttpServletResponse response) {
        String logTag = IdUtil.simpleUUID();
        String data = request.getParameter("data");
        JSONObject dataJS = JSONObject.parseObject(data);
        log.info(String.format("[%s][%s]", logTag, dataJS));
        Map result = new HashMap();
        //有关联上下级的禁止删除
        Map<String, Object> map = agentAuthorizedService.getData(dataJS.getString("record_code"));
        if ("1".equals(map.get("record_status").toString())) {
            result.put("msg", "该代理商存在下级三方代理商，请先关闭关联后再试");
            result.put("status", false);
            return result;
        }
        int rows = agentAuthorizedService.delRecord(dataJS.getString("record_code"));
        result.put("msg", rows == 1 ? "删除成功" : "删除失败");
        result.put("status", rows == 1);
        return result;
    }

    /**
     * 三方授权管理开关
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upStatus")
    @ResponseBody
    @SystemLog(description = "三方授权管理开关",operCode="agentAuth.upStatus")
    public Object upStatus(@RequestParam("info") String param) {
        Map result = new HashMap();
        Map<String, Object> map = JSONObject.parseObject(param, Map.class);
        try {
            if(StringUtil.isNotBlank(map.get("record_status"))){
                Map<String, Object> m = agentAuthorizedService.getData(map.get("record_code").toString());
                if ("0".equals(map.get("record_check").toString())){
                    result.put("msg", "审核未通过，请先审核");
                    result.put("status", false);
                    return result;
                }
                if("0".equals(map.get("record_status").toString())){
                    m.put("record_status",map.get("record_status"));
                    m.put("is_look",map.get("is_look"));
                    int rows = agentAuthorizedService.upCloseStatus(m);
                    result.put("msg", rows > 0 ? "操作成功" : "操作失败");
                    result.put("status", rows > 1);
                }else{
                    Map m2=agentAuthorizedService.selectAgentAuthorizedAgentLink(m.get("agent_authorized").toString());
                    if(m2==null&&"2".equals(m.get("is_top").toString())){
                        result.put("msg", "该代理商未与上级代理商关联，请先与上级关联");
                        result.put("status", false);
                        return result;
                    }else if(m2!=null){
                        if("0".equals(m2.get("record_status").toString())){
                            result.put("msg", "上级关联关系未打开，请打开后再操作");
                            result.put("status", false);
                            return result;
                        }
                        if (Integer.parseInt(m2.get("link_level").toString())+1>5){
                            result.put("msg", "三方关联层级不能大于5级");
                            result.put("status", false);
                            return result;
                        }
                        m.put("agent_node", m2.get("agent_node").toString()+"-"+map.get("agent_link").toString());
                        m.put("link_level",Integer.parseInt(m2.get("link_level").toString())+1);
                    }else{
                        m.put("agent_node", m.get("agent_authorized").toString()+"-"+map.get("agent_link").toString());
                        m.put("link_level",1);
                    }
                    m.put("record_status",map.get("record_status"));
                    int rows = agentAuthorizedService.upRecord(m);
                    result.put("msg", rows == 1 ? "操作成功" : "操作失败");
                    result.put("status", rows == 1);
                }
            }else{
                result.put("msg", "参数错误");
                result.put("status", false);
            }
        }catch (Exception e){
            log.error("三方授权管理开关异常",e);
            result.put("msg", "三方授权管理开关异常");
            result.put("status", false);
        }
        return result;
    }

    /**
     * 查询数据开关
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upLook")
    @ResponseBody
    @SystemLog(description = "查询数据开关",operCode="agentAuth.upLook")
    public Object upLook(@RequestParam("info") String param) {
        Map result = new HashMap();
        Map<String, Object> map = JSONObject.parseObject(param, Map.class);
        try {
            if("0".equals(map.get("record_status").toString())){
                result.put("msg", "关联关系未打开，请先关联后再开启数据查询开关");
                result.put("status", false);
                return result;
            }
            if(StringUtil.isNotBlank(map.get("is_look"))){
                int rows = agentAuthorizedService.upIsLook(map);
                result.put("msg", rows == 1 ? "操作成功" : "操作失败");
                result.put("status", rows == 1);
            }else{
                result.put("msg", "参数错误");
                result.put("status", false);
            }
        }catch (Exception e){
            log.error("查询数据开关异常",e);
            result.put("msg", "查询数据开关异常");
            result.put("status", false);
        }
        return result;
    }

    /**
     * 修改
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/routing/{pathVar}")
    @ResponseBody
    @SystemLog()
    public Object agentAuthorizedManager(@PathVariable String pathVar, HttpServletRequest request, HttpServletResponse response) {
        String logTag = IdUtil.simpleUUID();
        String data = request.getParameter("data");
        JSONObject dataJS = JSONObject.parseObject(data);
        log.info(String.format("[%s][%s][%s]", logTag, pathVar, dataJS));
        Map result = new HashMap();
        if ("checkRecord".equals(pathVar)) {
            return upRecord(dataJS);
        } else if ("upRecord".equals(pathVar)) {
            return upRecord(dataJS);
        } else {
            result.put("msg", "无此服务");
            result.put("status", false);
            result.put("msg_code", logTag);
            return result;
        }
    }

    /**
     * 修改记录，通过操作类型，来判断具体修改数据
     *
     * @param dataJS
     * @return
     */
    private Object upRecord(JSONObject dataJS) {
        Map result = new HashMap();
        //获取操作类型
        String op_type = dataJS.getString("op_type");
        String record_status = dataJS.getString("record_status");
        String record_check = dataJS.getString("record_check");
        //审核操作，修改审核状态
        if ("check".equals(op_type)) {
        	String recordCode  = dataJS.getString("record_code");
        	//判断
        	boolean correctChain= agentAuthorizedService.isCorrectChain(recordCode);
        	boolean top= agentAuthorizedService.isTop(recordCode);
        	if (!correctChain && !top) {
        	    result.put("msg", "操作失败，请检查上级关联关系");
                result.put("status", false);
                return result;
			}
            UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            dataJS.put("check_user", principal.getUsername());
            if (Objects.equals("0", record_check)) {
                dataJS.put("record_status", "0");
                dataJS.put("is_look", "0");
            }else{
                dataJS.put("record_status", "1");
                dataJS.put("is_look", "1");
            }
        } else {
            if ("modify".equals(op_type)) {
                result = checkData(dataJS);
                if (result != null) {
                    return result;
                } else {
                    result = new HashMap();
                }
            }
            //修改不包含审核状态的字段
            if ("1".equals(record_status) || "true".equalsIgnoreCase(record_status)) {
                String record_code = dataJS.getString("record_code");
                Map record = agentAuthorizedService.getData(record_code);
                record_check = String.valueOf(record.get("record_check"));
                //如果修改开关状态为打开，则需要判断审核状态是否为审核通过
                if (Objects.equals("0", record_check)) {
                    result.put("msg", "操作失败，等审核通过");
                    result.put("status", false);
                    return result;
                }
            }
//			dataJS.put("record_creator", principal.getRealName());
        }
        try {
            int rows = agentAuthorizedService.upRecord(dataJS.toJavaObject(Map.class));
            result.put("msg", rows == 1 ? "操作成功" : "操作失败");
            result.put("status", rows == 1);
        } catch (DuplicateKeyException de) {
            result.put("msg", "已经存在相同配置");
            result.put("status", false);
        } catch (Exception e) {
            String logTag = IdUtil.simpleUUID();
            result.put("msg", "操作失败,代码[" + logTag + "]");
            result.put("status", false);
            log.error("logTag", e);
        }
        return result;
    }

    private Map checkData(JSONObject dataJS) {
        Map result = new HashMap();
        String agent_authorized = dataJS.getString("agent_authorized");
        if (StringUtils.isBlank(agent_authorized)) {
            result.put("msg", "代理商编号不能为空");
            result.put("status", false);
            return result;
        }
        String agent_link = dataJS.getString("agent_link");
        if (StringUtils.isBlank(agent_link)) {
            result.put("msg", "授权查询代理商编号不能为空");
            result.put("status", false);
            return result;
        }
        String agentNo = agent_authorized;
        AgentInfo agentInfo = agentInfoService.getAgentByNo(agentNo);
        if (agentInfo != null) {
            int level = agentInfo.getAgentLevel();
            if (level > 1) {
                result.put("msg", "代理商[" + agentNo + "]非一级代理商");
                result.put("status", false);
                return result;
            }
            agentNo = agent_link;
            agentInfo = agentInfoService.getAgentByNo(agentNo);
            if (agentInfo != null) {
                level = agentInfo.getAgentLevel();
                if (level > 1) {
                    result.put("msg", "代理商[" + agentNo + "]非一级代理商");
                    result.put("status", false);
                    return result;
                }
            }
        }
        if (agentInfo == null) {
            result.put("msg", "代理商[" + agentNo + "]不存在");
            result.put("status", false);
            return result;
        }
        return null;
    }


    /**
     * 导出三方授权查询列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @SystemLog(description = "导出三方授权查询列表", operCode = "agentAuth.importDetail")
    public Map<String, Object> importDetail(HttpServletResponse response, HttpServletRequest request){
        String data = request.getParameter("info");
        JSONObject dataJS = JSONObject.parseObject(data);
        Map<String, Object> msg=new HashMap<String,Object>();
        List<Map<String, Object>> list=agentAuthorizedService.importDetailSelect(dataJS.toJavaObject(Map.class));
        try {
            agentAuthorizedService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出三方授权查询列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出三方授权查询列表异常!");
        }
        return msg;
    }


    /**
     * 模板下载
     */
    @RequestMapping("/downloadTemplate")
    public String downloadAdjustAccTemplate(HttpServletRequest request, HttpServletResponse response){
        String filePath = request.getServletContext().getRealPath("/")+ File.separator+"template"+File.separator+"agentAuthTemplate.xls";
        log.info(filePath);
        ResponseUtil.download(response, filePath,"批量导入代理商模板.xlsx");
        return null;
    }
    /**
     * 批量导入
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
            }
            msg = agentAuthorizedService.importDiscount(file);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "导入失败!");
            log.error("导入失败!",e);
        }
        return msg;
    }


    /**
     * 顶层代理商管理
     * @return
     */
    @RequestMapping(value = "/selectTopAgentManagement")
    @ResponseBody
    public Object selectTopAgentManagement(@RequestParam("info") String param, @ModelAttribute("page") Page page) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            Map<String, Object> map = JSONObject.parseObject(param, Map.class);
            agentAuthorizedService.selectTopAgentManagement(map ,page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("顶层代理商管理查询异常!",e);
            msg.put("status", false);
            msg.put("msg", "顶层代理商管理查询异常!");
        }
        return msg;
    }

    /**
     * 新增顶层代理商管理
     * @return
     */
    @RequestMapping(value = "/addTopAgentManagement")
    @ResponseBody
    public Object addTopAgentManagement(String agent_authorized) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            Map map=agentAuthorizedService.selectAgentAuthorizedAgentLink(agent_authorized);
            if(map!=null){
                msg.put("status", false);
                msg.put("msg", "该代理商已经关联有上级，暂时不能设置为顶层代理商");
                return msg;
            }
            List<Map<String,Object>> list=agentAuthorizedService.getAgentAuthorized(agent_authorized);
            if(list.size()>0){
                msg.put("status", false);
                msg.put("msg", "该代理商已是顶层代理商");
                return msg;
            }else{
                agentAuthorizedService.addTopAgentManagement(agent_authorized);
            }
            msg.put("msg","新增成功");
            msg.put("status", true);
        } catch (Exception e){
            log.error("新增顶层代理商管理异常!",e);
            msg.put("status", false);
            msg.put("msg", "新增顶层代理商管理异常!");
        }
        return msg;
    }

    /**
     * 删除顶层代理商管理
     * @return
     */
    @RequestMapping(value = "/deleteTopAgentManagement")
    @ResponseBody
    public Object deleteTopAgentManagement(String agent_authorized) {
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<Map<String,Object>> list=agentAuthorizedService.getAgentAuthorized(agent_authorized);
            for(Map<String,Object> map:list){
                if("1".equals(map.get("record_status").toString())){
                    msg.put("status", false);
                    msg.put("msg", "该代理商存在下级三方代理商，请先关闭关联后再试");
                    return msg;
                }
            }
            agentAuthorizedService.deleteTopAgentManagement(agent_authorized);
            msg.put("msg","删除成功");
            msg.put("status", true);
        } catch (Exception e){
            log.error("删除顶层代理商管理异常!",e);
            msg.put("status", false);
            msg.put("msg", "删除顶层代理商管理异常!");
        }
        return msg;
    }
}
