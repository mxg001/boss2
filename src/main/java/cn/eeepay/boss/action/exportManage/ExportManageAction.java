package cn.eeepay.boss.action.exportManage;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ExportManage;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.sysUser.ExportManageService;
import cn.eeepay.framework.util.Constants;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
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

@Controller
@RequestMapping(value = "/exportManage")
public class ExportManageAction {

    private static final Logger log = LoggerFactory.getLogger(ExportManageAction.class);

    @Resource
    private ExportManageService exportManageService;

    /**
     * 查询下载管理列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> selectAllList(@RequestParam("info") String param, @ModelAttribute("page") Page<ExportManage> page){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            // 获取当前登录的人
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            ExportManage info = JSONObject.parseObject(param, ExportManage.class);
            info.setOperator(principal.getUsername());
            exportManageService.selectAllList(info, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询下载列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询下载列表异常!");
        }
        return msg;
    }


    /**
     * 定时获取下载列表
     */
    @RequestMapping(value = "/selectAllListTime")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> selectAllListTime(){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            Page<ExportManage> page =getExportManageList();
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("定时获取下载列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "定时获取下载列表异常!");
        }
        return msg;
    }

    /**
     * 修改读状态
     */
    @RequestMapping(value = "/exportManageClick")
    @ResponseBody
    public Map<String,Object> exportManageClick(@RequestParam("id") int id){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=exportManageService.updateReadStatus(id);
            if(num>0){
                Page<ExportManage> page =getExportManageList();
                msg.put("msg","修改读状态成功");
                msg.put("page",page);
                msg.put("status", true);
            }else{
                msg.put("msg","修改读状态失败");
                msg.put("status", false);
            }
        } catch (Exception e){
            log.error("修改读状态异常!",e);
            msg.put("status", false);
            msg.put("msg", "修改读状态异常!");
        }
        return msg;
    }
    private Page<ExportManage> getExportManageList(){
        // 获取当前登录的人
        Page<ExportManage> page=new Page<ExportManage>();
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ExportManage> list=exportManageService.getReadInfoList(principal.getUsername());
        page.setResult(list);
        page.setTotalCount(list.size());
        return page;
    }

    /**
     * 下载文档
     */
    @RequestMapping(value="/downloadExcel")
    @ResponseBody
    public Map<String, Object> downloadMedia(@RequestParam("id") int id, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();

        ExportManage info =exportManageService.getExportManageInfoByID(id);
        if(info.getStatus().intValue()==1){
            exportManageService.downloadFile(info,response,msg);
        }else{
            msg.put("status", false);
            msg.put("msg", "下载文件不存在!");
        }
        return msg;
    }

    /**
     * 修改读状态
     */
    @RequestMapping(value = "/deleteExportManage")
    @ResponseBody
    public Map<String,Object> deleteExportManage(@RequestParam("id") int id){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=exportManageService.deleteExportManage(id);
            if(num>0){
                Page<ExportManage> page =getExportManageList();
                msg.put("msg","删除成功");
                msg.put("page",page);
                msg.put("status", true);
            }else{
                msg.put("msg","删除失败");
                msg.put("status", false);
            }
        } catch (Exception e){
            log.error("删除异常!",e);
            msg.put("status", false);
            msg.put("msg", "删除异常!");
        }
        return msg;
    }
}
