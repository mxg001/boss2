package cn.eeepay.boss.system;

import cn.hutool.extra.servlet.ServletUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class GlobalDispatcherServlet extends DispatcherServlet {
    private static Logger log = LoggerFactory.getLogger(GlobalDispatcherServlet.class);
    public static final String charsetName = "UTF-8";
    public static final String split = "&";

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding(charsetName);
        response.setCharacterEncoding(charsetName);
        String clientIP = ServletUtil.getClientIP(request);
        log.info(String.format("[%s]RequestURL[%s]:%s?%s", clientIP, request.getMethod(), request.getRequestURL(), getParamsString(request)));
        super.doService(request, response);
    }

    /**
     * 获取请求参数
     * @param request
     * @return
     */
    public static String getParamsString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        boolean needSplit = false;
        Enumeration<String> names = request.getParameterNames();
        if (null != names) {
            String name;
            while (names.hasMoreElements()) {
                name = names.nextElement();
                if("pwd".equals(name) || "password".equals(name) || "newPWD".equals(name)) {
                    continue;
                }
                sb.append(needSplit ? split : "");
                needSplit = true;
                sb.append(name).append("=").append(request.getParameter(name));
            }
        }
        return sb.toString();
    }
}
