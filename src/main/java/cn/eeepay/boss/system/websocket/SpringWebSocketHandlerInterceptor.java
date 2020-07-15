package cn.eeepay.boss.system.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author luzijun
 * @Date 2019/4/20 20:49
 * @Remark
 **/
public class SpringWebSocketHandlerInterceptor extends HttpSessionHandshakeInterceptor {

    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
//        System.out.println("Before Handshake");
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(false);
//            System.out.println("Interceptor===="+session.getId());
            attributes.put("currHttpSession",session);
            /*if (session != null) {
                //使用userName区分WebSocketHandler，以便定向发送消息
                String userName = (String) session.getAttribute("SESSION_USERNAME");
                System.out.println("userName===="+userName);
                if (userName==null) {
                    userName="default-system";
                }
                attributes.put("WEBSOCKET_USERNAME",userName);
            }*/
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);

    }

    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }

}
