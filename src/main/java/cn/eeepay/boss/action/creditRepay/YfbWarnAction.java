package cn.eeepay.boss.action.creditRepay;

import cn.eeepay.framework.model.creditRepay.YfbWarnInfo;
import cn.eeepay.framework.service.creditRepay.YfbWarnService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通道失败预警
 * @author MXG
 * create 2018/10/22
 */
@Controller
@RequestMapping("/warn")
public class YfbWarnAction {

    @Resource
    private YfbWarnService warnService;

    private Logger log = LoggerFactory.getLogger(YfbWarnAction.class);

    /**
     * 通道失败预警配置显示
     * @param code
     * @return
     */
    @ResponseBody
    @RequestMapping("/selectWarnInfoByCode")
    public Map<String, Object> selectWarnInfoByCode(String code){
        Map<String, Object> map = new HashMap<>();
        try {
            YfbWarnInfo warnInfo = warnService.selectWarnInfoByCode(code);
            map.put("status", true);
            map.put("info", warnInfo);
        } catch (Exception e) {
           log.error("查询通道失败预警配置失败", e);
            map.put("status", false);
        }
        return map;
    }

    /**
     * 提交修改
     * @return
     */
    @ResponseBody
    @RequestMapping("/update")
    public Map<String, Object> update(@RequestBody YfbWarnInfo info){
        Map<String, Object> map = new HashMap<>();
        try {
            //手机号检查
            String warnphone = info.getWarnPhone().replace(" ","").replace("；",";");
            if(StringUtils.isNotBlank(warnphone)){
                String[] phones = warnphone.split(";");
                List<String> list = Arrays.asList(phones);
                for (String s : list) {
                    if(s.length() != 11){
                        map.put("status", false);
                        map.put("msg", "手机号输入有误");
                        return map;
                    }
                }
            }
            warnService.update(info);
            map.put("status", true);
        } catch (Exception e) {
            log.error("通道失败预警配置提交失败", e);
            map.put("status", false);
            map.put("msg", "提交失败");
        }
        return map;
    }
}
