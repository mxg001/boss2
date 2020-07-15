package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditRepayOrder;
import cn.eeepay.framework.model.WarningEvents;
import cn.eeepay.framework.model.WarningPeople;
import cn.eeepay.framework.service.WarningEventsService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ListDataExcelExport;
import com.alibaba.fastjson.JSON;
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
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/1/10/010.
 * @author  liuks
 * 预警事件
 */
@Controller
@RequestMapping(value = "/warningEvents")
public class WarningEventsAction {

    private static final Logger log = LoggerFactory.getLogger(WarningEventsAction.class);
    @Resource
    private WarningEventsService warningEventsService;

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/allWarningEvents")
    @ResponseBody
    public Map<String, Object> getWarningEventsAll(@ModelAttribute("page") Page<WarningEvents> page, @RequestParam("info") String param) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            WarningEvents we = JSON.parseObject(param, WarningEvents.class);
            warningEventsService.getWarningEventsAll(page,we);
            jsonMap.put("status", true);
            jsonMap.put("page", page);
        } catch (Exception e) {
            log.error("报错!!!", e);
            jsonMap.put("status", false);
            jsonMap.put("msg", "预警事件查询失败!");
        }
        return jsonMap;
    }

    /**
     * 导出数据表
     * @param param
     * @param response
     * @param request
     * @throws Exception
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value="/exportAllInfo")
    @ResponseBody
    public  Map<String, Object> exportAllInfo(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request) throws Exception{
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        WarningEvents we = JSON.parseObject(param, WarningEvents.class);
        List<WarningEvents> list=warningEventsService.exportAllInfo(we);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;

        String fileName = "预警事件查询"+sdf.format(new Date())+".xlsx" ;

        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("eventNumber",null);
            maps.put("createTime", null);
            maps.put("status", null);
            maps.put("acqEnname", null);
            maps.put("message", null);
            data.add(maps);
        }else{
            for (WarningEvents or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("eventNumber",or.getEventNumber()==null?"":or.getEventNumber());
                maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
                if(or.getStatus()!=null){
                    if(or.getStatus()==1){
                        maps.put("status","交易系统");
                    }else if(or.getStatus()==2){
                        maps.put("status","出款系统");
                    }else if(or.getStatus()==3){
                        maps.put("status","定时任务");
                    }else{
                        maps.put("status","");
                    }
                }else{
                    maps.put("status","");
                }
                maps.put("acqEnname", or.getAcqEnname()==null?"":or.getAcqEnname());
                maps.put("message", or.getMessage()==null?"":or.getMessage());
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"eventNumber","createTime","status","acqEnname","message"};
        String[] colsName = new String[]{"预警编号","创建时间","来源系统","收单机构名称","预警内容"};
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出预警事件查询失败!",e);
            jsonMap.put("status", false);
            jsonMap.put("msg", "导出预警事件查询失败!");
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
        return jsonMap;
    }
}
