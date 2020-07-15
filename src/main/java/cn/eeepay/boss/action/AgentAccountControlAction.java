package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentAccountControl;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.service.AgentAccountControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/agentAccountControl")
public class AgentAccountControlAction {
    private static final Logger log = LoggerFactory.getLogger(AgentAccountControlAction.class);

    @Resource
    private AgentAccountControlService agentAccountControlService;

    /**
     * 代理商账户控制列表
     * @return
     */
    @RequestMapping("/queryAgentAccountControl")
    @ResponseBody
    public Object queryAgentAccountControl(@RequestBody AgentAccountControl agentAccountControl,@ModelAttribute("page") Page<AgentAccountControl> page) {
        Map<String, Object> msg = new HashMap<>();
        try {
            agentAccountControlService.queryAgentAccountControl(agentAccountControl,page);
            msg.put("page", page);
            msg.put("status", true);
            msg.put("msg", "查询成功");
        } catch (Exception e) {
            log.error("服务异常！", e);
            msg.put("status", false);
            msg.put("msg", "服务异常");
        }
        return msg;
    }

    /**
     * 代理商账户控制-默认值
     * @return
     */
    @RequestMapping("/queryAgentAccountControlByDefault")
    @ResponseBody
    public Object queryAgentAccountControlByDefault(){
        Map<String, Object> msg = new HashMap<>();
        try {
            AgentAccountControl agentAccountControlDefault=agentAccountControlService.queryAgentAccountControlByDefault();
            msg.put("agentAccountControlDefault", agentAccountControlDefault);
            msg.put("status", true);
            msg.put("msg", "查询成功");
        } catch (Exception e) {
            log.error("服务异常！", e);
            msg.put("status", false);
            msg.put("msg", "服务异常");
        }
        return msg;
    }

    /**
     * 代理商账户控制-添加默认
     * @return
     */
    @RequestMapping("/saveAgentAccountControl")
    @ResponseBody
    @SystemLog(description = "修改默认设置与控制金额", operCode = "agentAccountControl.saveAgentAccountControl")
    public Object saveAgentAccountControl(@RequestBody AgentAccountControl agentAccountControl){
        Map<String, Object> msg = new HashMap<>();
        try {
            if(agentAccountControlService.saveAgentAccountControl(agentAccountControl)){
                msg.put("status", true);
                msg.put("msg", "提交成功");
            }else{
                msg.put("status", false);
                msg.put("msg", "提交失败");
            }
        } catch (Exception e) {
            log.error("服务异常！", e);
            msg.put("status", false);
            msg.put("msg", "服务异常");
        }
        return msg;
    }

    /**
     * 查找代理商id
     */
    @RequestMapping("/queryAgentByID")
    @ResponseBody
    public Object queryAgentByID(@RequestBody String agentNo){
        Map<String, Object> msg = new HashMap<>();
        try {
            AgentInfo agent=agentAccountControlService.queryAgentByID(agentNo);
            if(agent==null){
                msg.put("status", false);
                msg.put("msg", "查找失败");
            }else{
                msg.put("agent", agent);
                msg.put("status", true);
                msg.put("msg", "查找成功");
            }
        } catch (Exception e) {
            log.error("服务异常！", e);
            msg.put("status", false);
            msg.put("msg", "服务异常");
        }
        return msg;
    }


    /**
     * 新增代理商账户控制
     * @param agentAccountControl
     * @return
     */
    @RequestMapping("/addAgentAccountControl")
    @SystemLog(description = "新增代理商账户控制", operCode = "agentAccountControl.addAgentAccountControl")
    public @ResponseBody Object addAgentAccountControl(@RequestBody AgentAccountControl agentAccountControl){
        Map<String, Object> msg = new HashMap<>();
        try {
            if(agentAccountControl.getAgentNo()!=null&&!"".equals(agentAccountControl.getAgentNo())) {
                AgentInfo agent=agentAccountControlService.queryAgentByID(agentAccountControl.getAgentNo());
                if(agent==null){
                    msg.put("status", false);
                    msg.put("msg", "参数错误");
                }else {
                    if (!agentAccountControlService.queryAgentAccountControlByAgentNoCount(agentAccountControl.getAgentNo())) {
                        if (agentAccountControlService.addAgentAccountControl(agentAccountControl)) {
                            msg.put("status", true);
                            msg.put("msg", "新增成功");
                        } else {
                            msg.put("status", false);
                            msg.put("msg", "新增失败");
                        }
                    } else {
                        msg.put("status", false);
                        msg.put("msg", "代理商已存在列表中");
                    }
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "参数错误");
            }
        } catch (Exception e) {
            log.error("服务异常！", e);
            msg.put("status", false);
            msg.put("msg", "服务异常");
        }
        return msg;
    }

    @RequestMapping(value="/updateAgentAccountControlSwitch.do")
    @ResponseBody
    public  Object updateAgentAccountControlSwitch(@RequestBody AgentAccountControl agentAccountControl ){
        Map<String, Object> msg = new HashMap<>();
        try {
            if(agentAccountControlService.updateAgentAccountControl(agentAccountControl)){
                msg.put("status", true);
                msg.put("msg", "设置成功");
            } else {
                msg.put("status", false);
                msg.put("msg", "设置失败");
            }
        } catch (Exception e) {
            msg.put("msg", "服务异常");
            e.printStackTrace();
            msg.put("status", false);
        }
        return msg;
    }

    /**
     * 修改代理商账户控制
     * @param agentAccountControl
     * @return
     */
    @RequestMapping("/editAgentAccountControl")
    @SystemLog(description = "修改代理商账户控制", operCode = "agentAccountControl.editAgentAccountControl")
    public @ResponseBody Object editAgentAccountControl(@RequestBody AgentAccountControl agentAccountControl){
        Map<String, Object> msg = new HashMap<>();
        try {
            if(agentAccountControl.getAgentNo()!=null&&!"".equals(agentAccountControl.getAgentNo())) {
                AgentInfo agent=agentAccountControlService.queryAgentByID(agentAccountControl.getAgentNo());
                if(agent==null){
                    msg.put("status", false);
                    msg.put("msg", "参数错误");
                }else {
                    if (agentAccountControlService.updateAgentAccountControl(agentAccountControl)) {
                        msg.put("status", true);
                        msg.put("msg", "修改成功");
                    } else {
                        msg.put("status", false);
                        msg.put("msg", "修改成功");
                    }
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "参数错误");
            }
        } catch (Exception e) {
            log.error("服务异常！", e);
            msg.put("status", false);
            msg.put("msg", "服务异常");
        }
        return msg;
    }

    /**
     * 删除代理商账户控制
     * @param agentNo
     * @return
     */
    @RequestMapping("/deleteAgentAccountControl")
    @SystemLog(description = "删除代理商账户控制", operCode = "agentAccountControl.deleteAgentAccountControl")
    public @ResponseBody Object deleteAgentAccountControl(@RequestBody String agentNo){
        Map<String, Object> msg = new HashMap<>();
        try {
            if(agentNo!=null&&!"".equals(agentNo)) {
                AgentAccountControl agentAccountControl=agentAccountControlService.queryAgentAccountControlByAgentNo(agentNo);
                if(agentAccountControl!=null&&agentAccountControl.getStatus()==0){
                    if (agentAccountControlService.deleteAgentAccountControl(agentNo)) {
                        msg.put("status", true);
                        msg.put("msg", "删除成功");
                    } else {
                        msg.put("status", false);
                        msg.put("msg", "删除成功");
                    }
                }else {
                    msg.put("status", false);
                    msg.put("msg", "状态为开启中，不可以删除");
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "参数错误");
            }
        } catch (Exception e) {
            log.error("服务异常！", e);
            msg.put("status", false);
            msg.put("msg", "服务异常");
        }
        return msg;
    }

    /**
     * 删除代理商账户控制
     * @param agentNo
     * @return
     */
    @RequestMapping("/queryAgentAccountControlByAgentNo")
    public @ResponseBody Object queryAgentAccountControlByAgentNo(@RequestBody String agentNo){
        Map<String, Object> msg = new HashMap<>();
        try {
            AgentAccountControl agentAccountControl=agentAccountControlService.queryAgentAccountControlByAgentNo(agentNo);
            msg.put("param", agentAccountControl);
            msg.put("status", true);
        } catch (Exception e) {
            log.error("查询失败！", e);
            msg.put("status", false);
        }
        return msg;
    }


}
