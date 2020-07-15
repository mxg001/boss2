package cn.eeepay.boss.action;

import cn.eeepay.boss.system.websocket.SpringWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author luzijun
 * @Date 2019/4/20 20:51
 * @Remark
 **/
@Controller
@RequestMapping(value = "/websocket")
public class WebsocketController {

    @Bean
    public SpringWebSocketHandler infoHandler() {
        return new SpringWebSocketHandler();
    }

    @RequestMapping("/send")
    @ResponseBody
    public String send(HttpServletRequest request) {
        //String username = request.getParameter("username");
        HttpSession session = request.getSession(false);
        infoHandler().sendMessageToUser(session, new TextMessage("有新的下载任务已完成"));
//        infoHandler().sendMessageToUsers(new TextMessage("你好，所有测试！！！"));
        return null;
    }

}
