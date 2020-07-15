package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.luckDraw.LuckDrawOrder;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.HttpUtils;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.SignUtils;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 短信通道管理
 *
 * @author rpc
 */
@Controller
@RequestMapping(value = "/managerSms")
public class ManagerSmsController {

    private Logger log = LoggerFactory.getLogger(ManagerSmsController.class);

    @Resource
    private SysDictService sysDictService;

    /**
     * 获取所有服务类型
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping(value = "/getServiceList")
    @ResponseBody
    public Map<String, Object> getServiceList(@RequestParam("info") String param, @ModelAttribute Page page) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("status", false);
        try {
            Map<String, String> paramMap = JSONObject.parseObject(param, Map.class);
            paramMap.put("nonceStr", RandomUtil.randomString(32));

            String paramStr = SignUtils.getParamSign(paramMap);
            //String baseUrl = "http://192.168.4.24:18080/sms/";
            String baseUrl = sysDictService.getByKey("SMS_MANAGER_URL").getSysValue();
            String url = baseUrl + "open/service/serviceList";
            String str = HttpUtils.sendPost(url, paramStr, "UTF-8");
            if (str != null) {
                JSONObject resultJS = JSONObject.parseObject(str);
                if (resultJS != null && resultJS.getInteger("code") == 200) {
                    msg.put("status", true);
                    List<Map> list=resultJS.getJSONObject("data").getJSONArray("serviceList").toJavaList(Map.class);
                    page.setResult(list);
                    page.setTotalCount(list.size());
                    page.setTotalPages(1);
                    msg.put("page", page);
                }
            }


        } catch (Exception e) {
            log.error("查询获取所有服务类型失败", e);
            msg.put("msg", "查询异常");
        }
        return msg;
    }

    /**
     * 获取所有短信通道
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping(value = "/getSmsChannelList")
    @ResponseBody
    public Map<String, Object> getSmsChannelList(@RequestParam("info") String param, @ModelAttribute Page page) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("status", false);
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("nonceStr", RandomUtil.randomString(32));

            String paramStr = SignUtils.getParamSign(paramMap);
            //String baseUrl = "http://192.168.4.24:18080/sms/";
            String baseUrl = sysDictService.getByKey("SMS_MANAGER_URL").getSysValue();
            String url = baseUrl + "open/service/smsChannelList";
            String str = HttpUtils.sendPost(url, paramStr, "UTF-8");
            if (str != null) {
                JSONObject resultJS = JSONObject.parseObject(str);
                if (resultJS != null && resultJS.getInteger("code") == 200) {
                    msg.put("status", true);
                    page.setResult(resultJS.getJSONObject("data").getJSONArray("smsChannelList").toJavaList(Map.class));
                    msg.put("page", page);
                }
            }


        } catch (Exception e) {
            log.error("查询获取所有服务类型失败", e);
            msg.put("msg", "查询异常");
        }
        return msg;
    }

    /**
     * 修改短信通道状态
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping(value = "/changeChannelStatus")
    @ResponseBody
    public Map<String, Object> changeChannelStatus(@RequestParam Map<String, String> params) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("status", false);
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("nonceStr", RandomUtil.randomString(32));
            paramMap.put("channelCode", params.get("channelCode"));
            paramMap.put("opCode", params.get("opCode"));

            String paramStr = SignUtils.getParamSign(paramMap);
            //String baseUrl = "http://192.168.4.24:18080/sms/";
            String baseUrl = sysDictService.getByKey("SMS_MANAGER_URL").getSysValue();
            String url = baseUrl + "open/service/modifyChannelStatus";
            String str = HttpUtils.sendPost(url, paramStr, "UTF-8");
            if (str != null) {
                JSONObject resultJS = JSONObject.parseObject(str);
                if(resultJS!=null){
                    msg.put("msg", resultJS.get("msg"));
                    if(resultJS.getInteger("code") == 200){
                        msg.put("status", true);
                    }
                }else{
                    msg.put("msg", "操作失败");
                }

            }

        } catch (Exception e) {
            log.error("查询获取所有服务类型失败", e);
            msg.put("msg", "查询异常");
        }
        return msg;
    }

    /**
     * 为服务类型配置短信通道
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping(value = "/changeServiceChannel")
    @ResponseBody
    public Map<String, Object> changeServiceChannel(@RequestParam Map<String, String> params) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("status", false);
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("nonceStr", RandomUtil.randomString(32));
            paramMap.put("channelCode", params.get("channelCode"));
            paramMap.put("serviceName", params.get("serviceName"));

            String paramStr = SignUtils.getParamSign(paramMap);
            //String baseUrl = "http://192.168.4.24:18080/sms/";
            String baseUrl = sysDictService.getByKey("SMS_MANAGER_URL").getSysValue();
            String url = baseUrl + "open/service/serviceJoinChannel";
            String str = HttpUtils.sendPost(url, paramStr, "UTF-8");
            log.info("str=" + str);
            if (str != null) {
                JSONObject resultJS = JSONObject.parseObject(str);
                if(resultJS!=null){
                    msg.put("msg", resultJS.get("msg"));
                    if(resultJS.getInteger("code") == 200){
                        msg.put("status", true);
                    }
                }else{
                    msg.put("msg", "操作失败");
                }
            }


        } catch (Exception e) {
            log.error("查询获取所有服务类型失败", e);
            msg.put("msg", "查询异常");
        }
        return msg;
    }


    /**
     * 导出通道分布列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    public Map<String, Object> importDetail(@RequestParam("info") String param,  HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            param=new String(param.getBytes("ISO-8859-1"),"UTF-8");
            Map<String, String> paramMap = JSONObject.parseObject(param, Map.class);
            paramMap.put("nonceStr", RandomUtil.randomString(32));

            String paramStr = SignUtils.getParamSign(paramMap);
            //String baseUrl = "http://192.168.4.24:18080/sms/";
            String baseUrl = sysDictService.getByKey("SMS_MANAGER_URL").getSysValue();
            String url = baseUrl + "open/service/serviceList";
            String str = HttpUtils.sendPost(url, paramStr, "UTF-8");
            if (str != null) {
                JSONObject resultJS = JSONObject.parseObject(str);
                if (resultJS != null && resultJS.getInteger("code") == 200) {
                    msg.put("status", true);

                    List<Map> list=resultJS.getJSONObject("data").getJSONArray("serviceList").toJavaList(Map.class);
                    importDetail(list,response);
                }
            }


        }catch (Exception e){
            log.error("导出通道分布列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出通道分布列表异常!");
        }
        return msg;
    }

    public void importDetail(List<Map> list, HttpServletResponse response) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "通道分布列表"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("service_name",null);
            maps.put("service_zh_name",null);
            maps.put("channel_name",null);
            data.add(maps);
        }else{


            for (Map map : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("service_name",map.get("service_name")+"");
                maps.put("service_zh_name",map.get("service_zh_name")+"");
                maps.put("channel_name",map.get("channel_name")+"");
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"service_name","service_zh_name","channel_name"

        };
        String[] colsName = new String[]{"服务类型代码","服务类型名称","短信通道"

        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出通道分布列表失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

}
