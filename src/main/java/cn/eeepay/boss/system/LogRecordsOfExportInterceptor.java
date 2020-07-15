package cn.eeepay.boss.system;

import cn.eeepay.framework.model.BossOperLog;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.BossOperLogService;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 全局导出功能日志记录拦截器
 */
public class LogRecordsOfExportInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(LogRecordsOfExportInterceptor.class);

    @Resource
    BossOperLogService bossOperLogService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("-------- LogRecordsOfExportInterceptor - preHandle ---------");
        try {
            logRecordsOfExport(request);
        }catch (Exception e){
            logger.info("LogRecordsOfExportInterceptor-Exception:" + e.getMessage());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    /**
     * 记录导出操作的日志
     * @param request  请求
     */
    private void logRecordsOfExport(HttpServletRequest request){
        UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BossOperLog log =new BossOperLog();
        log.setOper_time(new Date());
        log.setUser_name(userInfo.getRealName());
        log.setUser_id(userInfo.getId());
        log.setOper_status(CommonConst.ONE);
        log.setOper_ip(ServletUtil.getClientIP(request));
        log.setRequest_method(request.getRequestURI());
        log.setRequest_params(JSON.toJSONString(request.getParameterMap()));
        log.setMethod_desc("数据导出");
        log.setOper_code("BOSS_USER_EXPORT");
        bossOperLogService.insert(log);
    }
}
