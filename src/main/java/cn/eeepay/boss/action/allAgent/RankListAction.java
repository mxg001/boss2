package cn.eeepay.boss.action.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.allAgent.RankList;
import cn.eeepay.framework.model.allAgent.UserAllAgent;
import cn.eeepay.framework.model.luckDraw.LuckDrawOrder;
import cn.eeepay.framework.service.allAgent.RankListService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/6/006.
 * @author  liuks
 * 排行榜列表
 */
@Controller
@RequestMapping(value = "/rankList")
public class RankListAction {

    private static final Logger log = LoggerFactory.getLogger(RankListAction.class);

    @Resource
    private RankListService rankListService;

    /**
     * 查询排行榜奖金发放明细列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<RankList> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            RankList info = JSONObject.parseObject(param, RankList.class);
            rankListService.selectAllList(info, page);
            Map<String,Object> sumMap=rankListService.selectAllSum(info, page);
            msg.put("page",page);
            msg.put("sumMap",sumMap);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询排行榜奖金发放明细列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询排行榜奖金发放明细列表异常!");
        }
        return msg;
    }

    /**
     * 导出排行榜奖金发放明细列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            param=new String(param.getBytes("ISO-8859-1"),"UTF-8");//解析中文编码
            RankList info = JSONObject.parseObject(param, RankList.class);
            List<RankList> list=rankListService.importDetailSelect(info);
            rankListService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出排行榜奖金发放明细列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出排行榜奖金发放明细列表异常!");
        }
        return msg;
    }
}
