package cn.eeepay.boss.action.risk;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.db.pagination.Page;
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
 * 调单扣款明细
 */
@Controller
@RequestMapping(value = "/surveyDeductDetail")
public class SurveyDeductDetailAction {

    private static final Logger log = LoggerFactory.getLogger(SurveyDeductDetailAction.class);

    @Resource
    private SurveyDeductDetailService surveyDeductDetailService;

    /**
     * 调单扣款明细查询
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<SurveyDeductDetail> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SurveyDeductDetail detail = JSONObject.parseObject(param, SurveyDeductDetail.class);
            surveyDeductDetailService.selectAllList(detail, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("调单扣款明细查询异常!",e);
            msg.put("status", false);
            msg.put("msg", "调单扣款明细查询异常!");
        }
        return msg;
    }
    /**
     * 导出调单扣款明细列表
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        SurveyDeductDetail detail = JSONObject.parseObject(param, SurveyDeductDetail.class);
        List<SurveyDeductDetail> list=surveyDeductDetailService.importDetailSelect(detail);
        try {
            surveyDeductDetailService.importDetail(list,response,2);
        }catch (Exception e){
            log.error("导出调单扣款明细列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出调单扣款明细列表异常!");
        }
        return msg;
    }
}
