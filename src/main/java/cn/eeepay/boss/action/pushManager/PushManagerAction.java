package cn.eeepay.boss.action.pushManager;

import cn.eeepay.boss.action.MsgAction;
import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AppInfo;
import cn.eeepay.framework.model.Msg;
import cn.eeepay.framework.model.pushManager.PushManager;
import cn.eeepay.framework.service.pushManager.PushManagerService;
import cn.eeepay.framework.serviceImpl.couponImport.PushManagerJobServiceImpl;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.*;

@Controller
@RequestMapping("/pushManager")
public class PushManagerAction {

    private static final Logger log = LoggerFactory.getLogger(PushManagerAction.class);

    @Resource
    private PushManagerService pushManagerService;

    @Resource
    private PushManagerJobServiceImpl pushManagerJobService;


    @RequestMapping("/checkCanPush")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Object checkCanPush(@RequestParam(value = "pushId") String pushId){
        Map<String, Object> msgMap = new HashMap<>();
        try {
            msgMap.put("status", true);
            msgMap.put("msg", "检查推送用户为部分时是否已经导入商户信息");
            pushManagerService.checkCanPush(msgMap,pushId);
        } catch (Exception e) {
            log.error("检查推送用户为部分时是否已经导入商户信息出错",e);
            msgMap.put("status", false);
            msgMap.put("msg", "检查推送用户为部分时是否已经导入商户信息出错");
        }
        return msgMap;
    }

    @RequestMapping("/exportPushManager")
    @ResponseBody
    @SystemLog(description = "导出推送管理内容",operCode = "pushManager.exportPushManager")
    public List<AppInfo> exportPushManager(@RequestParam("baseInfo") String baseInfo, HttpServletResponse response, HttpServletRequest request)throws Exception{
        Map<String, Object> msgMap = new HashMap<>();
        try {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            baseInfo = URLDecoder.decode(baseInfo, "UTF-8");
            PushManager pm = JSON.parseObject(baseInfo, PushManager.class);
            List<PushManager> pms =  pushManagerService.getPushManager(pm);
            String fileName = "推送管理.xlsx";
            String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);

            for (PushManager pushManager : pms) {
                Map<String,String> map = new HashMap<>();
                map.put("id",pushManager.getId()+"");
                map.put("pushContent",StringUtil.filterNull(pushManager.getPushContent()));
                map.put("jumpUrl", StringUtil.filterNull(pushManager.getJumpUrl()));
                String pushObj = pushManager.getPushObj();
                if(StringUtils.isNotEmpty(pushObj)){
                    String pushObjName = pushManagerService.getPushObjName(pushObj);
                    pushManager.setPushObjName(pushObjName);
                }
                String mobileTypeName = "";
                if(pushManager.getMobileTerminalType()!=null){
                    if(pushManager.getMobileTerminalType()==1){
                        mobileTypeName = "android";
                    }else if(pushManager.getMobileTerminalType()==2){
                        mobileTypeName = "ios";
                    }
                }else{
                    mobileTypeName = "全部";
                }
                map.put("mobileType",mobileTypeName);

                map.put("pushObj",pushManager.getPushObjName());
                map.put("pushTime",pushManager.getPushTime()==null?"": DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss",pushManager.getPushTime()));
                map.put("createTime",pushManager.getCreateTime()==null?"": DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss",pushManager.getCreateTime()));
                map.put("createPerson",pushManager.getCreatePerson()+"");
                String pushStatus =null;
                if(pushManager.getPushStatus()!=null){
                    if(pushManager.getPushStatus()==0){
                        pushStatus = "未推送";
                    }else if(pushManager.getPushStatus()==1){
                        pushStatus = "已推送";
                    }else if(pushManager.getPushStatus()==2){
                        pushStatus = "推送失败";
                    }
                }
                map.put("pushStatus",pushStatus);
                data.add(map);
            }


            ListDataExcelExport export = new ListDataExcelExport();
            String[] cols = new String[] { "id", "pushContent", "jumpUrl", "mobileType",
                    "pushObj","pushTime","createTime","createPerson","pushStatus"};
            String[] colsName = new String[] { "推送id", "推送内容", "跳转链接", "移动端类型",
                    "推送对象","推送时间","创建时间","创建人", "推送状态"};
            OutputStream outputStream = response.getOutputStream();
            export.export(cols, colsName, data, outputStream);
            outputStream.close();
        } catch (Exception e) {
            log.error("推送导出出错",e);
            msgMap.put("status", false);
            msgMap.put("msg", "推送导出出错！");
        }
        return null;
    }

    @RequestMapping("/testPush")
    @ResponseBody
    public List<AppInfo> testPush(){
        try {
            pushManagerJobService.PushManagerJob();
        } catch (Exception e) {
            log.error("推送出错",e);
        }
        return null;
    }


    @RequestMapping("/getAppInfo")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Object getAppInfo(@RequestParam(value = "apply",defaultValue = "2") String apply){
        Map<String, Object> msgMap = new HashMap<>();
        try {
            msgMap.put("status", true);
            msgMap.put("msg", "获取推送对象成功！");
            msgMap.put("appInfos",pushManagerService.getAppInfo(apply));
        } catch (Exception e) {
            log.error("获取推送对象出错",e);
            msgMap.put("status", false);
            msgMap.put("msg", "获取推送对象出错！");
        }
        return msgMap;
    }

    @RequestMapping("/getByPushManagerId")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public PushManager selectPushManagerByCondition(@RequestParam("id") Long id) throws Exception{
        Map<String, Object> msgMap = new HashMap<>();
        try {
            return pushManagerService.getByPushManagerId(id,msgMap);
        } catch (Exception e) {
            log.error("查询推送信息出错",e);
            msgMap.put("status", false);
            msgMap.put("msg", "查询推送信息出错！");
        }
        return null;
    }

    @RequestMapping("/selectPushManagerByCondition")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Page<PushManager> selectPushManagerByCondition(@RequestParam("baseInfo") String baseInfo,@RequestParam("jumpUrl")String jumpUrl,
                                   @Param("page") Page<PushManager> page) throws Exception{
        Map<String, Object> msgMap = new HashMap<>();
        try {
            jumpUrl = URLDecoder.decode(jumpUrl, "UTF-8");
            PushManager pm = JSON.parseObject(baseInfo, PushManager.class);
            pm.setJumpUrl(jumpUrl);
            pushManagerService.selectPushManagerByCondition(page,pm);
        } catch (Exception e) {
            log.error("查询推送信息出错",e);
            msgMap.put("status", false);
            msgMap.put("msg", "查询推送信息出错！");
        }
        return page;
    }

    @DataSource(Constants.DATA_SOURCE_MASTER)
    @RequestMapping("/saveOrUpdatePushManager")
    @ResponseBody
    @SystemLog(description = "新增或者修改推送管理内容",operCode="pushManager.saveOrUpdatePushManager")
    public Object saveOrUpdatePushManager(@RequestParam("baseInfo") String baseInfo ,@RequestParam("jumpUrl") String jumpUrl)throws Exception{
        baseInfo = URLDecoder.decode(baseInfo, "UTF-8");
        jumpUrl = URLDecoder.decode(jumpUrl, "UTF-8");
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("status", false);
        msgMap.put("msg", "添加失败！");

        try {
            PushManager pm = JSON.parseObject(baseInfo, PushManager.class);
            pm.setJumpUrl(jumpUrl);
            pushManagerService.saveOrUpdatePushManager(pm,msgMap);
        }catch (Exception e){
            log.error("新增推送内容失败",e);
            msgMap.put("status", false);
            msgMap.put("msg", "添加失败！");
        }
        return msgMap;
    }


    @DataSource(Constants.DATA_SOURCE_MASTER)
    @RequestMapping("/delPushManagerById")
    @ResponseBody
    @SystemLog(description = "删除推送管理内容",operCode="pushManager.delPushManagerById")
    public Object delPushManagerById(@RequestParam("id") Long id)throws Exception{
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("status", true);
        msgMap.put("msg", "推送消息已删除！");

        try {
            pushManagerService.delPushManagerById(id,msgMap);
        }catch (Exception e){
            log.error("删除推送内容失败",e);
            msgMap.put("status", false);
            msgMap.put("msg", "删除失败！");
        }
        return msgMap;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/getPushManagerById")
    @ResponseBody
    public Object getPushManagerById(@RequestParam("id") Long id)throws Exception{
        Map<String, Object> msgMap = new HashMap<>();
        try {
            msgMap.put("baseInfo",pushManagerService.getByPushManagerId(id,msgMap));
            msgMap.put("status", true);
            msgMap.put("msg", "查询成功！");
        }catch (Exception e){
            log.error("查询推送内容失败",e);
            msgMap.put("status", false);
            msgMap.put("msg", "查询推送内容失败！");
        }
        return msgMap;
    }

    @DataSource(Constants.DATA_SOURCE_MASTER)
    @RequestMapping("/imPortPushManagerFromExcel")
    @ResponseBody
    @SystemLog(description = "导入商户编号",operCode = "pushManager.imPortPushManagerFromExcel")
    public Object imPortPushManagerFromExcel(@RequestParam("file") MultipartFile file,@RequestParam("pushId") Long id)throws Exception{
        Map<String, Object> msg = new HashMap<>();
        try {
            if (!file.isEmpty()) {
                String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                if(!format.equals(".xls") && !format.equals(".xlsx")){
                    msg.put("status", false);
                    msg.put("msg", "商户导入文件格式错误");
                    return msg;
                }
            }
            msg = pushManagerService.importPushManagerFromExcel(file,id);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "商户信息导入失败");
            log.error("商户信息导入失败",e);
        }
        return msg;
    }

    /***
     * 下载商户编号模板
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/downloadMerchantNoTemplate")
    @SystemLog(description = "下载商户编号模板",operCode = "pushManager.downloadMerchantNoTemplate")
    public Object downloadMerchantNoTemplate(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> msg = new HashMap<>();
        String filePath = null;
        try {
            filePath = request.getServletContext().getRealPath("/")+"template"+ File.separator+"pushManagerMerchantNoTemplate.xlsx";
        } catch (Exception e) {
            msg.put("status",false);
            msg.put("msg","下载商户信息模板失败");
            log.error("下载商户信息模板失败",e);
        }
        ResponseUtil.download(response, filePath,"推送管理商户信息模板.xlsx");
        return null;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/previewPushManager")
    @ResponseBody
    @SystemLog(description = "推送管理预览",operCode = "pushManager.previewPushManager")
    public Object previewPushManager(@RequestParam("merchantNo") String merchantNo,@RequestParam("pushId")Long pushId)throws Exception{
        Map<String, Object> msgMap = new HashMap<>();
        try {
            pushManagerService.previewPushManager(merchantNo,pushId,msgMap);
        }catch (Exception e){
            log.error("预览失败",e);
            msgMap.put("status", false);
            msgMap.put("msg", "预览失败！");
        }
        return msgMap;
    }
    @DataSource(Constants.DATA_SOURCE_MASTER)
    @RequestMapping("/toPush")
    @ResponseBody
    @SystemLog(description = "推送管理实时推送",operCode = "pushManager.toPush")
    public Object tuPush(@RequestParam("pushId")Long id)throws Exception{
        Map<String, Object> msgMap = new HashMap<>();
        try {
            msgMap.put("status", true);
            msgMap.put("msg", "推送成功！");
            pushManagerService.tuPush(id,msgMap);


        }catch (Exception e){
            log.error("推送失败",e);
            msgMap.put("status", false);
            msgMap.put("msg", "推送失败！");
        }
        return msgMap;
    }
}
