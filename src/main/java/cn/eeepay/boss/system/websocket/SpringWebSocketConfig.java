package cn.eeepay.boss.system.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @Author luzijun
 * @Date 2019/4/20 20:04
 * @Remark
 **/
@Configuration
@EnableWebMvc
@EnableWebSocket
public class SpringWebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        //分发路由，addHandler里面前面是路由结果，后面的匹配路径，最后面是路由拦截

        //允许连接的域,只能以http或https开头
        //String[] allowsOrigins = {"http://www.xxx.com"};
        //webSocketHandlerRegistry.addHandler(webSocketHandler(),"/websocket/socketServer.do").setAllowedOrigins(allowsOrigins).addInterceptors(new SpringWebSocketHandlerInterceptor());
        webSocketHandlerRegistry.addHandler(webSocketHandler(),"/websocket/socketServer").addInterceptors(new SpringWebSocketHandlerInterceptor());
        webSocketHandlerRegistry.addHandler(webSocketHandler(), "/sockjs/socketServer").addInterceptors(new SpringWebSocketHandlerInterceptor()).withSockJS();
    }

    @Bean
    public TextWebSocketHandler webSocketHandler(){
        return new SpringWebSocketHandler();
    }
}
