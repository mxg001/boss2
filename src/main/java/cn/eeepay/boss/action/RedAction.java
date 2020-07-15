package cn.eeepay.boss.action;


import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.RedService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 超级银行家-红包
 * @author tans
 * @date 2018-1-17
 */
@Controller
@RequestMapping("/red")
public class RedAction {

    private Logger log = LoggerFactory.getLogger(RedAction.class);

    @Resource
    private RedService redService;
    
    @Resource
    private SysDictService sysDictService;

    @RequestMapping("/redConfig")
    @ResponseBody
    public Result redConfig(@RequestBody Map params, @RequestParam(defaultValue = "1")int pageNo,
                            @RequestParam(defaultValue = "10")int pageSize){
        Result result = new Result();
        try {
            log.info("\n-------------- " + params + "-----------------\n");

            Page<Map<String,Object>> page = new Page<>(pageNo, pageSize);

            result = redService.redConfig(params, page);
            result.setStatus(true);
            result.setMsg("查询成功");
        } catch (Exception e) {
            log.error("信用卡奖金配置查询异常", e);
            result.setStatus(false);
            result.setMsg("查询异常");
        }
        return result;
    }

    @RequestMapping("/redConfigUpdStatus")
    @ResponseBody
    @SystemLog(operCode = "red.redConfigUpdStatus", description = "修改红包业务配置状态")
    public Result redConfigUpdStatus(@RequestBody Map params){
        Result result = new Result();
        try {
            log.info("\n-------------- " + params + "-----------------\n");

            String id = StringUtil.filterNull(params.get("id"));
            String status = StringUtil.filterNull(params.get("status"));
            int num  = redService.updateRedStatus(id,status);
          //刷新业务红包发放配置
            if(num==1){
        	SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
        	String back=ClientInterface.redProductPush(params.get("push_org_id").toString(),sysDict.getSysValue()+Constants.REFRESH_RED_CONF ,params.get("bus_type").toString());
        	 if(!StringUtils.isBlank(back)){
        		 JSONObject jsonObject = JSONObject.parseObject(back);
        		 if(jsonObject.containsValue("success")){
        		 result.setStatus(true);
        		 result.setMsg("修改成功");
        		 }else{
        			 result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
        		 }
        		 }else{
        			 result.setMsg("修改成功 刷新缓存失败  请联系开发人员"); 
        		 }
            }
        } catch (Exception e) {
            log.error("修改状态异常", e);
            result.setStatus(false);
            result.setMsg("修改状态异常");
        }
        return result;
    }


    @RequestMapping("/addRedConfig")
    @ResponseBody
    @SystemLog(operCode = "red.addRedConfig", description = "新增红包业务配置")
    public Result addRedConfig(@RequestBody Map params ){
        Result result = new Result();

        try {
            log.info("-------------- " +  params + "-----------------");
            redService.addRedConfig(params);
            SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
        	String back=ClientInterface.redProductPush(params.get("push_org_id").toString(),sysDict.getSysValue()+Constants.REFRESH_RED_CONF ,params.get("bus_type").toString());
        	 if(!StringUtils.isBlank(back)){
        		 JSONObject jsonObject = JSONObject.parseObject(back);
        		 if(jsonObject.containsValue("success")){
        		 result.setStatus(true);
        		 result.setMsg("操作成功");
        		 }else{
        			 result.setMsg("操作成功 刷新缓存失败  请联系开发人员");
        		 }
        		 }else{
        			 result.setMsg("操作成功 刷新缓存失败  请联系开发人员");
        		 }
        } catch (Exception e) {
            log.error("新增异常", e);
            result.setStatus(false);
            result.setMsg("新增异常");
        }

        return result;
    }

    @RequestMapping("/editRedConfig")
    @ResponseBody
    public Result editRedConfig(@RequestBody Map params ){
        Result result = new Result();
        try {
            log.info("-------------- " + params + "-----------------");
            String id = StringUtil.filterNull(params.get("id"));
            Map<String,Object> redConfigInfo =  redService.redConfigInfo(id);
            if(redConfigInfo.get("allow_org_profit") != null){
                redConfigInfo.put("allow_org_profit",Integer.parseInt(StringUtil.filterNull(redConfigInfo.get("allow_org_profit"))));
            }
            String img_url = StringUtil.filterNull(redConfigInfo.get("img_url"));
            String[] urlArr = img_url.split(";");
            List<String> urlList = new ArrayList<>();
            List<String> imgList = new ArrayList<>();
            for(String url: urlArr){
                if(StringUtils.isNotBlank(url)){
                    urlList.add(CommonUtil.getImgUrlAgent(url));
                    imgList.add(url);
                }
            }
            redConfigInfo.put("urlList", urlList);
            redConfigInfo.put("imgList", imgList);
            //如果该配置已经被使用过，则部分数据不可再修改
            int num = redService.configUseStatus(id);
            if(num > 0){
                redConfigInfo.put("useStatus", "1");
            } else {
                redConfigInfo.put("useStatus", "0");
            }
            result.setStatus(true);
            result.setData(redConfigInfo);
            result.setMsg("查询成功");
        } catch (Exception e) {
            log.error("信用卡奖金配置查询异常", e);
            result.setStatus(false);
            result.setMsg("查询异常");
        }

        return result;
    }


    @RequestMapping("/editRedConfigSub")
    @ResponseBody
    @SystemLog(operCode = "red.editRedConfig", description = "修改红包业务配置")
    public Result editRedConfigSub(@RequestBody Map params ){
        Result result = new Result();
        try {
            log.info("-------------- " + params + "-----------------");
            String img_url = StringUtil.filterNull(params.get("img_url"));
            if(StringUtils.isNotBlank(img_url)){
                String[] urlArr = img_url.split(";");
                if(urlArr.length > 9){
                    result.setMsg("宣传图片最多不能超过9张");
                    return result;
                }
            }
            int num =  redService.redConfigInfoUpdate(params);
            if(num == 1){
            	//刷新业务红包发放配置
            	SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
            	String back=ClientInterface.redProductPush(params.get("push_org_id").toString(),sysDict.getSysValue()+Constants.REFRESH_RED_CONF ,params.get("bus_type").toString());
            	 if(!StringUtils.isBlank(back)){
            		 JSONObject jsonObject = JSONObject.parseObject(back);
            		 if(jsonObject.containsValue("success")){
            		 result.setStatus(true);
            		 result.setMsg("修改成功");
            		 }else{
            			 result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
            		 }
            		 }else{
            			 result.setMsg("修改成功 刷新缓存失败  请联系开发人员"); 
            		 }
            }else{
            	 result.setMsg("修改失敗");
            }
        } catch (Exception e) {
            log.error("信用卡奖金配置修改异常", e);
            result.setStatus(false);
            result.setMsg("修改异常");
        }

        return result;
    }
    /**
     * 红包业务管理
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/redControl")
    @ResponseBody
    public Result redControl(@RequestParam(defaultValue = "1")int pageNo,
                             @RequestParam(defaultValue = "10")int pageSize){
        Result result = new Result();
        try{
            Page<RedControl> page = new Page<>(pageNo, pageSize);
            redService.selectRedControl(page);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(page);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("红包业务管理异常", e);
        }
        return result;
    }

    /**
     * 修改红包业务开关状态
     * @param redControl
     * @return
     */
    @RequestMapping("/updateRedOpen")
    @ResponseBody
    @SystemLog(operCode = "red.updateRedOpen", description = "业务开关")
    public Result updateRedOpen(@RequestBody RedControl redControl){
        Result result = new Result();
        if(redControl == null || redControl.getId() == null
                || redControl.getOpenStatus() == null){
        	 result.setMsg("参数不能为空");
             return result;
        }
        try{
             result = redService.updateRedOpen(redControl);
            if(result.isStatus()){
            	redControl = redService.selectRedControlByPrimaryKey(redControl.getId());
            	//刷新红包开关配置
            	SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
            	String back=ClientInterface.redProductPush(null,sysDict.getSysValue()+Constants.REFRESH_RED_OPEN ,redControl.getBusType());
            	if(!StringUtils.isBlank(back)){
           		 JSONObject jsonObject = JSONObject.parseObject(back);
           		 if(jsonObject.containsValue("success")){
           		 result.setStatus(true);
           		 result.setMsg("操作成功");
           		 }else{
           			 result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
           		 }
           		 }else{
           			 result.setMsg("修改成功 刷新缓存失败  请联系开发人员"); 
           		 }
            	
            	// 刷新红包业务分类组织排序缓存
                refreshRedOrgSortCache(result);
            } else {
                  return result;
//                result.setMsg("操作失败");
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("修改红包业务开关状态异常", e);
        }
        return result;
    }


    /**
     * 修改红包业务是否开启组织控制
     * @param redControl
     * @return
     */
    @RequestMapping("/updateRedOrg")
    @ResponseBody
    @SystemLog(operCode = "red.updateRedOrg", description = "组织开关")
    public Result updateRedOrg(@RequestBody RedControl redControl){
    	Result result = new Result();
    	 if(redControl == null || redControl.getId() == null
                 || redControl.getOrgStatus() == null){
             result.setMsg("参数不能为空");
             return result;
         }
        try{
             int num = redService.updateRedOrg(redControl);
            if(num>0){
            	redControl = redService.selectRedControlByPrimaryKey(redControl.getId());
            	//刷新红包开关配置
            	SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
            	String back=ClientInterface.redProductPush(null,sysDict.getSysValue()+Constants.REFRESH_RED_OPEN ,redControl.getBusType());
            	if(!StringUtils.isBlank(back)){
           		 JSONObject jsonObject = JSONObject.parseObject(back);
           		 if(jsonObject.containsValue("success")){
           		 result.setStatus(true);
           		 result.setMsg("操作成功");
           		 }else{
           			 result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
           		 }
           		 }else{
           			 result.setMsg("修改成功 刷新缓存失败  请联系开发人员"); 
           		 }
            	
            	// 刷新红包业务分类组织排序缓存
                refreshRedOrgSortCache(result);
            } else {
                result.setMsg(result.getMsg()==null?"操作失败":result.getMsg());
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("修改红包业务是否开启组织控制异常", e);
        }
        return result;
    }

    /**
     * 红包业务组织管理
     * @param redOrg
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/redOrg")
    @ResponseBody
    public Result redOrg(@RequestBody RedOrg redOrg,
                         @RequestParam(defaultValue = "1")int pageNo,
                         @RequestParam(defaultValue = "10")int pageSize){
        Result result = new Result();
        try {
            Page<RedOrg> page = new Page<>(pageNo, pageSize);
            redService.selectRedOrg(redOrg, page);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(page);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("红包业务组织管理异常", e);
        }
        return result;
    }
    
    /**
     * 红包业务组织管理
     * @param redOrg
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/redOrgListAll")
    @ResponseBody
    public Result redOrgListAll(@RequestBody RedOrg redOrg){
        Result result = new Result();
        try {
            List<RedOrg> redOrgList =  redService.selectRedOrgListAll(redOrg);
            if(redOrgList != null && redOrgList.size() > 0){
                for(RedOrg org: redOrgList){
                    org.setOrgName(org.getOrgId() + " " + org.getOrgName());
                }
            }
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(redOrgList);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("红包业务组织管理异常", e);
        }
        return result;
    }

    /**
     * 删除红包业务组织
     * @param id
     * @return
     */
    @RequestMapping("/deleteRedOrg")
    @ResponseBody
    @SystemLog(operCode = "red.deleteRedOrg", description = "删除")
    public Result deleteRedOrg(@RequestBody String id){
        Result result = new Result();
        try{
            result = redService.deleteRedOrg(id);
        }catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("删除红包业务组织异常", e);
        }
        return result;
    }

    /**
     * 红包组织模板下载
     * @param request
     * @param response
     */
    @RequestMapping("/redOrgTemplate")
    public void downloadAdjustAccTemplate(HttpServletRequest request, HttpServletResponse response){
        String filePath = request.getServletContext().getRealPath("/")+ File.separator+"template"+File.separator+"redOrgTemplate.xlsx";
        ResponseUtil.download(response, filePath,"超级银行家红包组织导入模板.xlsx");
    }

    /**
     * 红包业务组织管理-批量导入
     * @param file
     * @param busCode
     * @return
     */
    @RequestMapping("/importRedOrg")
    @ResponseBody
    @SystemLog(operCode = "red.importRedOrg" ,description = "批量新增")
    public Result importRedOrg(@RequestParam("file") MultipartFile file,
                               @RequestParam("busCode")String busCode){
        Result result = new Result();
        try {
            result = redService.importRedOrg(file, busCode);
            
            // 刷新红包业务分类组织排序缓存
            if(result.isStatus()) {
            	refreshRedOrgSortCache(result);
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("红包业务组织管理-批量导入异常", e);
        }
        return result;
    }

    /**
     * 新增红包业务组织
     * @param redOrg
     * @return
     */
    @RequestMapping("/addRedOrg")
    @ResponseBody
    @SystemLog(operCode = "red.addRedOrg" ,description = "新增")
    public Result addRedOrg(@RequestBody RedOrg redOrg){
        Result result = new Result();
        try{
            result = redService.addRedOrg(redOrg);
            
            // 刷新红包业务分类组织排序缓存
            if(result.isStatus()) {
                refreshRedOrgSortCache(result);
            }
        }catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("新增红包业务组织异常", e);
        }
        return result;
    }

    /**
     * 新增红包业务组织
     * @param param
     * @return
     */
    @RequestMapping("/redProductInfo")
    @ResponseBody
    public Result redProductInfo(@RequestBody Map param){
        Result result = new Result();
        try{
            String type = StringUtil.filterNull(param.get("conf_type"));
            Map<String,Object> redProductInfo = redService.redProductInfo(type);
            if(redProductInfo!=null) {
                String date_start = "";
                String date_end = "";
                if(redProductInfo.get("date_start")!=null){
                    date_start = redProductInfo.get("date_start").toString();
                    if(date_start.endsWith(".0")){
                        date_start = date_start.substring(0, date_start.length() - 2);
                    }
                } if(redProductInfo.get("date_end")!=null){
                    date_end = redProductInfo.get("date_end").toString();
                    if(date_end.endsWith(".0")){
                        date_end = date_end.substring(0, date_end.length() - 2);
                    }
                }
                redProductInfo.put("date_start", date_start);
                redProductInfo.put("date_end", date_end);
            }
            
            //2018-7-9 从sys_pinfu_chaju 查询   red_first_user  red_first_money
            if(redProductInfo!=null && type.equals("4")) {
            	redProductInfo.put("red_first_user", redService.getSysPinfuChajuKey("red_first_user"));
                redProductInfo.put("red_first_money", redService.getSysPinfuChajuKey("red_first_money"));
            }
            
            result.setData(redProductInfo);
            result.setStatus(true);
            result.setMsg("查询成功");

        }catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("新增红包业务组织异常", e);
        }
        return result;
    }





    /**
     * 获取个人发放红包配置
     * @return
     */
    @RequestMapping("/getRedUserConf")
    @ResponseBody
    public Result getRedUserConf(){
        Result result = new Result();
        try {
            RedUserConf conf = redService.getRedUserConf();
            if(conf == null){
                result.setMsg("数据未配置");
            } else {
                result.setStatus(true);
                result.setMsg("查询成功");
                result.setData(conf);
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("获取个人发放红包配置异常", e);
        }
        return result;
    }

    /**
     * 修改个人发放红包配置
     * @param
     * @return
     */
    @RequestMapping("/redProductInfoUpdate")
    @ResponseBody
    public Result redProductInfoUpdate(@RequestBody  Map param){
        Result result = new Result();
        try {
            int num = redService.redProductInfoUpdate(param);
            if(num == 1){
            	//刷新红包业务管理配置
            	SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
            	String back=ClientInterface.redProductPush(null,sysDict.getSysValue()+Constants.REFRESH_RED_PRO,param.get("conf_type").toString());
            	if(!StringUtils.isBlank(back)){
              		 JSONObject jsonObject = JSONObject.parseObject(back);
              		 if(jsonObject.containsValue("success")){
              		 result.setStatus(true);
              		 result.setMsg("操作成功");
              		 }else{
              			 result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
              		 }
              		 }else{
              			 result.setMsg("修改成功 刷新缓存失败  请联系开发人员"); 
              		 }
            } else{
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("修改个人发放红包配置异常", e);
        }
        return result;
    }

    /**
     * 修改个人发放红包配置
     * @param conf
     * @return
     */
    @RequestMapping("/updateRedUserConf")
    @ResponseBody
    @SystemLog(operCode = "red.updateRedUserConf", description = "修改个人发放红包配置")
    public Result updateRedUserConf(@RequestBody RedUserConf conf){
        Result result = new Result();
        try {
            int num = redService.updateRedUserConf(conf);
            if(num == 1){
            	//刷新个人红包发放配置
            	SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
            	String back=ClientInterface.redProductPush(null,sysDict.getSysValue()+Constants.REFRESH_RED_USER_CONF ,"0");
            	if(!StringUtils.isBlank(back)){
              		 JSONObject jsonObject = JSONObject.parseObject(back);
              		 if(jsonObject.containsValue("success")){
              		 result.setStatus(true);
              		 result.setMsg("操作成功");
              		 }else{
              			 result.setMsg("修改成功 刷新缓存失败  请联系开发人员");
              		 }
              		 }else{
              			 result.setMsg("修改成功 刷新缓存失败  请联系开发人员"); 
              		 }
            } else{
                result.setMsg("操作失败");
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("修改个人发放红包配置异常", e);
        }
        return result;
    }

    /**
     * 获取幸运值配置
     * @return
     */
    @RequestMapping("/getLuckConf")
    @ResponseBody
    public Result getLuckConf(){
        Result result = new Result();
        RedLuckConf baseInfo = redService.getLuckConf();
        if(baseInfo != null){
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(baseInfo);
        } else {
            result.setMsg("找不到对应的配置");
        }
        return result;
    }

    /**
     * 修改红包幸运值配置
     * @param baseInfo
     * @return
     */
    @RequestMapping("/updateLuckConf")
    @ResponseBody
    @SystemLog(operCode = "red.luckConf", description = "修改红包幸运值配置")
    public Result updateLuckConf(@RequestBody RedLuckConf baseInfo){
        Result result = new Result();
        try {
            result= redService.updateLuckConf(baseInfo);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("修改红包幸运值配置异常", e);
        }
        return result;
    }

    /**
     * 查询平台红包账户余额
     * @return
     */
    @RequestMapping("/plateAccountInfo")
    @ResponseBody
    public Result plateAccountInfo(){
        Result result = new Result();
        try{
            RedAccountInfo accountInfo = redService.plateAccountInfo();
            if(accountInfo != null){
                result.setStatus(true);
                result.setMsg("查询成功");
                result.setData(accountInfo);
            }
        }catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("查询平台红包账户余额异常", e);
        }
        return result;
    }

    /**
     * 查询平台红包账户余额明细
     * @param pageNo
     * @param pageSize
     * @param baseInfo
     * @return
     */
    @RequestMapping("/plateAccountDetail")
    @ResponseBody
    public Result plateAccountDetail(@RequestParam(defaultValue = "1") int pageNo,
                                     @RequestParam(defaultValue = "10") int pageSize,
                                     @RequestBody RedAccountDetail baseInfo){
        Result result = new Result();
        try{
            Page<RedAccountDetail> page = new Page<>(pageNo, pageSize);
            RedAccountInfo accountInfo = redService.plateAccountInfo();
            
            baseInfo.setRedAccountId(accountInfo.getId());
            redService.selectAccountDetailPage(baseInfo, page);
            baseInfo.setSelectType(1);
            RedAccountDetail totalInfo = redService.selectAccountDetailSum(baseInfo);
            Map<String, Object> map = new HashMap<>();
            map.put("page", page);
            map.put("totalInfo", totalInfo);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(map);
        }catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("查询平台红包账户余额明细异常", e);
        }
        return result;
    }

    /**
     * 导出平台红包账户明细
     * @param baseInfo
     * @param response
     */
    @RequestMapping("/exportPlateAccountDetail")
    public void exportPlateAccountDetail(String baseInfo, HttpServletResponse response){
        try {
            RedAccountDetail detail = JSONObject.parseObject(baseInfo, RedAccountDetail.class);
            RedAccountInfo accountInfo = redService.plateAccountInfo();
            detail.setRedAccountId(accountInfo.getId());
            redService.exportPlateAccountDetail(response, detail);
        } catch (Exception e){
            log.error("导出平台红包账户明细异常", e);
        }
    }
    
    /**
     * 查询红包业务组织分类排序
     * @param redOrgSort
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/redOrgSort")
    @ResponseBody
    public Result redOrgSort(@RequestBody RedOrgSort redOrgSort,
                         @RequestParam(defaultValue = "1")int pageNo,
                         @RequestParam(defaultValue = "10")int pageSize){
        Result result = new Result();
        try {
            Page<RedOrgSort> page = new Page<>(pageNo, pageSize);
            redService.selectRedOrgSort(redOrgSort, page);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(page);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("红包业务组织管理异常", e);
        }
        return result;
    }

    /**
     * 删除红包业务组织分类排序
     * @param id
     * @return
     */
    @RequestMapping("/deleteRedOrgSort")
    @ResponseBody
    @SystemLog(operCode = "red.deleteRedOrgSort", description = "删除")
    public Result deleteRedOrgSort(@RequestBody RedOrgSort redOrgSort){
        Result result = new Result();
        try{
            int count = redService.deleteRedOrgSort(redOrgSort);
            boolean status = count>0;
            result.setStatus(status);
            String string = status ? "删除成功!" : "删除红包业务组织分类排序异常!";
            result.setMsg(string);
            
            // 刷新红包业务分类组织排序缓存
            if(status) {
                refreshRedOrgSortCache(result);	
            }
        }catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("删除红包业务组织分类排序异常!", e);
        }
        return result;
    }
    
    /**
     * 新增红包业务组织分类排序
     * @param id
     * @return
     */
    @RequestMapping("/addRedOrgSort")
    @ResponseBody
    @SystemLog(operCode = "red.addRedOrgSort", description = "新增")
    public Result addRedOrgSort(@RequestBody RedOrgSort redOrgSort){
        Result result = new Result();
        try{
            String busCode = redOrgSort.getBusCode();
            Long orgId = redOrgSort.getOrgId();
            boolean isExist = validExist(busCode,orgId,null);
        	if(isExist) {
            	result.setStatus(false);
                result.setMsg("对应组织已存在!");
        	}else {
                int count = redService.addRedOrgSort(redOrgSort);
                boolean status = count>0;
                String string = status ? "增加成功!" : "增加红包业务组织分类排序异常!";
                result.setStatus(status);
                result.setMsg(string);
                
                // 刷新红包业务分类组织排序缓存
                if(status) {
                    refreshRedOrgSortCache(result);
                }
        	}
        }catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("增加红包业务组织分类排序异常!", e);
        }
        return result;
    }
    
    /**
     * 修改红包业务组织分类排序
     * @param id
     * @return
     */
    @RequestMapping("/editRedOrgSort")
    @ResponseBody
    @SystemLog(operCode = "red.editRedOrgSort", description = "修改")
    public Result editRedOrgSort(@RequestBody RedOrgSort redOrgSort){
        Result result = new Result();
        try{
            String busCode = redOrgSort.getBusCode();
            Long orgId = redOrgSort.getOrgId();
            Long idLong = redOrgSort.getId();
            boolean isExist = validExist(busCode,orgId,idLong);
        	if(isExist) {
            	result.setStatus(false);
                result.setMsg("对应组织已存在!");
        	}else {
                int count = redService.editRedOrgSort(redOrgSort);
                boolean status = count>0;
                String string = status ? "修改成功!" : "修改红包业务组织分类排序异常!";
                result.setStatus(status);
                result.setMsg(string);
                
                // 刷新红包业务分类组织排序缓存
                if(status) {
                    refreshRedOrgSortCache(result);
                }
        	}
        }catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("修改红包业务组织分类排序异常!", e);
        }
        return result;
    }
    
    /**
     * 	红包业务组织分类排序配置模板下载
     * @param request
     * @param response
     */
    @RequestMapping("/redOrgSortTemplate")
    public void downloadRedOrgSortTemplate(HttpServletRequest request, HttpServletResponse response){
        String filePath = request.getServletContext().getRealPath("/")+ File.separator+"template"+File.separator+"redOrgSortTemplate.xlsx";
        ResponseUtil.download(response, filePath,"超级银行家红包分类布局设置导入模板.xlsx");
    }
    
    @RequestMapping("/importRedOrgSort")
    @ResponseBody
    @SystemLog(operCode = "red.importRedOrgSort" ,description = "批量新增")
    public Result importRedOrgSort(@RequestParam("file") MultipartFile file,
                               @RequestParam("busCode")String busCode,@RequestParam("orgStatus")Integer orgStatus){
        Result result = new Result();
        try {
            result = redService.importRedOrgSort(file, busCode,orgStatus);
            
            // 刷新红包业务分类组织排序缓存
            if(result.isStatus()) {
            	refreshRedOrgSortCache(result);	
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("红包业务组织分类布局管理-批量导入异常", e);
        }
        return result;
    }
    
    /**
     * 	验证组织分类是否已经存在
     * @return
     */
    private boolean validExist(String busCode,Long orgId,Long id) {
    	RedOrgSort queryParam = new RedOrgSort(busCode,orgId);
    	Page<RedOrgSort> page = new Page<>();
        List<RedOrgSort> list = redService.selectRedOrgSort(queryParam, page);
        return list.size() > 0 && list.get(0).getId() != id;
    }
    
    private void refreshRedOrgSortCache(Result result) {
    	SysDict sysDict = sysDictService.getByKey("SUPER_BANK_INTEFACE_URL");
    	String back=ClientInterface.refreshRedOrgSortCache("-1",sysDict.getSysValue()+Constants.REFRESH_RED_ORG_SORT );
    	if(StringUtils.isBlank(back) || !JSONObject.parseObject(back).containsValue("success")){
    		result.setStatus(false); 
    		result.setMsg("操作成功 刷新缓存失败  请联系开发人员"); 
    	}
    }
    
}
