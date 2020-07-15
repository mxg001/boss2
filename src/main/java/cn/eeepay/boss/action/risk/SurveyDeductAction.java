package cn.eeepay.boss.action.risk;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.risk.DeductAddInfo;
import cn.eeepay.framework.model.risk.SurveyDeductDetail;
import cn.eeepay.framework.service.risk.SurveyDeductDetailService;
import cn.eeepay.framework.util.Constants;
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
 * Created by Administrator on 2018/9/12/012.
 * @author  liuks
 * 调单扣款
 */
@Controller
@RequestMapping(value = "/surveyDeduct")
public class SurveyDeductAction {

    private static final Logger log = LoggerFactory.getLogger(SurveyDeductAction.class);

    @Resource
    private SurveyDeductDetailService surveyDeductDetailService;

    /**
     * 调单扣款查询
     */
    @RequestMapping(value = "/selectGroup")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> selectGroup(@RequestParam("info") String param, @ModelAttribute("page")
            Page<SurveyDeductDetail> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SurveyDeductDetail detail = JSONObject.parseObject(param, SurveyDeductDetail.class);
            surveyDeductDetailService.selectGroup(detail, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("调单扣款查询异常!",e);
            msg.put("status", false);
            msg.put("msg", "调单扣款查询异常!");
        }
        return msg;
    }
    /**
     * 导出调单扣款列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        SurveyDeductDetail detail = JSONObject.parseObject(param, SurveyDeductDetail.class);
        List<SurveyDeductDetail> list=surveyDeductDetailService.importSelectGroup(detail);
        try {
            surveyDeductDetailService.importDetail(list,response,1);
        }catch (Exception e){
            log.error("导出调单扣款明细列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出调单扣款明细列表异常!");
        }
        return msg;
    }

    /**
     * 标注上游
     */
    @RequestMapping(value = "/upperReaches")
    @ResponseBody
    @SystemLog(description = "标注上游",operCode="surveyDeduct.upperReaches")
    public Map<String,Object> upperReaches(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            DeductAddInfo info = JSONObject.parseObject(param, DeductAddInfo.class);
            surveyDeductDetailService.tagging(info,msg);
        } catch (Exception e){
            log.error("标注上游异常!",e);
            msg.put("status", false);
            msg.put("msg", "标注上游异常!");
        }
        return msg;
    }
    /**
     * 标注商户
     */
    @RequestMapping(value = "/merchant")
    @ResponseBody
    @SystemLog(description = "标注商户",operCode="surveyDeduct.merchant")
    public Map<String,Object> merchant(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            DeductAddInfo info = JSONObject.parseObject(param, DeductAddInfo.class);
            surveyDeductDetailService.tagging(info,msg);
        } catch (Exception e){
            log.error("标注商户异常!",e);
            msg.put("status", false);
            msg.put("msg", "标注商户异常!");
        }
        return msg;
    }
    /**
     * 标注代理商
     */
    @RequestMapping(value = "/agent")
    @ResponseBody
    @SystemLog(description = "标注代理商",operCode="surveyDeduct.agent")
    public Map<String,Object> agent(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            DeductAddInfo info = JSONObject.parseObject(param, DeductAddInfo.class);
            surveyDeductDetailService.tagging(info,msg);
        } catch (Exception e){
            log.error("标注代理商异常!",e);
            msg.put("status", false);
            msg.put("msg", "标注代理商异常!");
        }
        return msg;
    }
    /**
     * 扣款处理
     */
    @RequestMapping(value = "/withdrawing")
    @ResponseBody
    @SystemLog(description = "扣款处理",operCode="surveyDeduct.withdrawing")
    public Map<String,Object> withdrawing(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            DeductAddInfo info = JSONObject.parseObject(param, DeductAddInfo.class);
            surveyDeductDetailService.tagging(info,msg);
        } catch (Exception e){
            log.error("扣款处理异常!",e);
            msg.put("status", false);
            msg.put("msg", "扣款处理异常!");
        }
        return msg;
    }
}
