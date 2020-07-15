package cn.eeepay.boss.action;

import cn.eeepay.framework.model.FunctionManager;
import cn.eeepay.framework.model.MobileVerInfo;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.service.AppApiService;
import cn.eeepay.framework.service.FunctionManagerService;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * @author tans
 * @date 2018/1/12
 */
@Controller
@RequestMapping("/appApi")
public class AppApiAction {

    private static final Logger log = LoggerFactory.getLogger(AppApiAction.class);

    @Resource
    private AppApiService appApiService;

    @Resource
    private FunctionManagerService functionManagerService;

    /**
     *
     * @param appType 表示是哪个app
     * @param platform 0:android 1:ios
     * @param version 版本号
     * @return
     */
    @RequestMapping("/getVersion")
    @ResponseBody
    public Result getAppVersion(String appType, String platform,
                                String version){
        Result result = new Result();
        if(StringUtils.isBlank(appType) || StringUtils.isBlank(platform)
                || StringUtils.isBlank(version)){
            result.setMsg("参数不能为空");
            log.info("获取app版本信息,返回结果:{}", JSONObject.toJSONString(result));
            return result;
        }
        log.info("获取app版本信息参数，appType:{},platform:{},version:{}"
                , appType, platform, version);
        result.setStatus(true);
        MobileVerInfo currentVer = new MobileVerInfo();
        currentVer.setAppType(appType);
        currentVer.setPlatform(Integer.parseInt(platform));
        currentVer.setVersion(version);
        try {
            MobileVerInfo ver = appApiService.getVersion(currentVer);
            Map<String, String> map = new HashMap<>();
            if(ver != null){
                result.setMsg("获取成功");
                map.put("version", ver.getVersion());
                map.put("appUrl", ver.getAppUrl());
                map.put("downFlag", String.valueOf(ver.getDownFlag()));
                map.put("verDesc", ver.getVerDesc());
            } else {
                result.setMsg("已是最新版本");
            }
            String functionNumber = "021";
            FunctionManager functionManager = functionManagerService.getFunctionManagerByNum(functionNumber);
            if(functionManager != null){
                map.put("funcSwitch", functionManager.getFunctionSwitch().toString());
            }
            result.setData(map);
        } catch (Exception e){
            log.error("获取app版本信息异常", e);
            result = ResponseUtil.buildResult(e);
        }
        String returnMsg = JSONObject.toJSONString(result);
        log.info("获取app版本信息,返回结果:{}", returnMsg);
        return result;
    }
}
