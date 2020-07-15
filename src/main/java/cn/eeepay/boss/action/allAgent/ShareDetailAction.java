package cn.eeepay.boss.action.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.ShareDetail;
import cn.eeepay.framework.service.allAgent.ShareDetailService;
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
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/shareDetail")
public class ShareDetailAction {
    private static final Logger log = LoggerFactory.getLogger(ShareDetailAction.class);

    @Resource
    private ShareDetailService shareDetailService;

    /**
     * 盟主分润明细查询
     */
    @RequestMapping(value = "/queryShareDetailList")
    @ResponseBody
    public Map<String,Object> queryShareDetailList(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ShareDetail> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ShareDetail info = JSONObject.parseObject(param, ShareDetail.class);
            shareDetailService.queryShareDetailList(info, page);
            Map<String,Object> pageCount=shareDetailService.queryShareDetailCount(info);
            msg.put("page",page);
            msg.put("pageCount",pageCount);
            msg.put("status", true);
        } catch (Exception e){
            log.error("申购售后订单异常!",e);
            msg.put("status", false);
            msg.put("msg", "申购售后订单异常!");
        }
        return msg;
    }

    @RequestMapping("/exportShareDetail")
    public void exportShareDetail(@RequestParam String param, HttpServletResponse response){
        try {
            ShareDetail info = JSON.parseObject(param,ShareDetail.class);
            shareDetailService.exportShareDetail(info, response);
        } catch (Exception e) {
            log.info("导出盟主分润明细失败,参数:{}");
            log.info(e.toString());
        }
    }
}
