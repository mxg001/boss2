package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TimedTask;
import cn.eeepay.framework.service.TimedTaskService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/8/008.
 * @author liuks
 * 定时任务监控表controller
 */
@Controller
@RequestMapping(value = "/timedTask")
public class TimedTaskAction {
    private static final Logger log = LoggerFactory.getLogger(TimedTaskAction.class);
    @Resource
    private TimedTaskService timedTaskService;

    /**
     * 分页监控列表
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectByParam")
    @ResponseBody
    public Map<String, Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<TimedTask> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            TimedTask tim = JSONObject.parseObject(param, TimedTask.class);
            timedTaskService.selectAllList(tim, page);
            msg.put("page",page);
            msg.put("status",true);
        } catch (Exception e){
            log.error("查询定时任务监控列表失败!",e);
            msg.put("msg","查询定时任务监控列表失败!");
            msg.put("status",false);
        }
        return msg;
    }

    /**
     * 查询定时任务监控列表
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/getTimedTaskByWarningState")
    @ResponseBody
    public Map<String, Object> getTimedTaskByWarningState(@RequestParam("warningState") int warningState) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<TimedTask> list=timedTaskService.getTimedTaskByWarningState(warningState);
            msg.put("list",list);
            msg.put("status",true);
        } catch (Exception e){
            log.error("查询定时任务监控列表失败!",e);
            msg.put("msg","查询定时任务监控列表失败!");
            msg.put("status",false);
        }
        return msg;
    }

    /**
     * 定时任务监控详情/编辑
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/detailTimedTask")
    @ResponseBody
    public Map<String, Object> detailTimedTask(@RequestParam("id") int id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            TimedTask tim=timedTaskService.getdetailTimedTask(id);
            msg.put("info",tim);
            msg.put("status",true);
        } catch (Exception e){
            log.error("定时任务监控详情失败!",e);
            msg.put("msg","定时任务监控详情失败!");
            msg.put("status",false);
        }
        return msg;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/editSubmit")
    @SystemLog(description = "定时任务监控修改",operCode="timedTask.editSubmit")
    @ResponseBody
    public Map<String, Object> editSubmit(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            TimedTask tim = JSONObject.parseObject(param, TimedTask.class);
            int  num=timedTaskService.saveDetailTimedTask(tim);
            if(num>0){
                msg.put("msg","定时任务修改成功");
                msg.put("status",true);
            }
        } catch (Exception e){
            log.error("定时任务修改失败!",e);
            msg.put("msg","定时任务修改失败!");
            msg.put("status",false);
        }
        return msg;
    }

    /**
     * 停止
     */
    @RequestMapping(value = "/closedTimedTask")
    @SystemLog(description = "定时任务停止",operCode="timedTask.closedTimedTask")
    @ResponseBody
    public Map<String, Object> closedTimedTask(@RequestParam("jobName")String jobName,@RequestParam("jobGroup")String jobGroup){
        Map<String, Object> msg=new HashMap<String,Object>();
        if(jobName!=null&&!"".equals(jobName)&&jobGroup!=null&&!"".equals(jobGroup)){
            try {
                timedTaskService.closeTimedTask(jobName,jobGroup);
                int num=timedTaskService.updateEnabledState(jobName,jobGroup);
                msg.put("msg","关闭定时任务成功!");
                msg.put("status",true);
            }catch (Exception e){
                msg.put("msg","关闭定时任务失败!");
                msg.put("status",false);
                e.printStackTrace();
            }
        }else{
            msg.put("msg","关闭定时任务失败!");
            msg.put("status",false);
        }
        return msg;
    }

    /**
     *重启
     */
    @RequestMapping(value = "/resetTimedTask")
    @SystemLog(description = "定时任务重启",operCode="timedTask.resetTimedTask")
    @ResponseBody
    public Map<String, Object> resetTimedTask(@RequestParam("jobName")String jobName,@RequestParam("jobGroup")String jobGroup){
        Map<String, Object> msg=new HashMap<String,Object>();
        if(jobName!=null&&!"".equals(jobName)&&jobGroup!=null&&!"".equals(jobGroup)){
            try {
                timedTaskService.resetTimedTask(jobName,jobGroup);
                msg.put("msg","重启定时任务成功!");
                msg.put("status",true);
            }catch (Exception e){
                msg.put("msg","重启定时任务失败!");
                msg.put("status",false);
                e.printStackTrace();
            }
        }else{
            msg.put("msg","重启定时任务失败!");
            msg.put("status",false);
        }
        return msg;
    }

}
