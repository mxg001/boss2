package cn.eeepay.boss.action.func;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.func.LoanFinancing;
import cn.eeepay.framework.service.func.LoanFinancingService;
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
import java.util.Map;

/**
 * @author liuks
 * 贷款理财产品
 */
@Controller
@RequestMapping(value = "/loanFinancing")
public class LoanFinancingAction {

    private static final Logger log = LoggerFactory.getLogger(LoanFinancingAction.class);


    @Resource
    private LoanFinancingService loanFinancingService;

    /**
     * 查询贷款理财产品列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> selectAllList(@RequestParam("info") String param, @ModelAttribute("page") Page<LoanFinancing> page){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            LoanFinancing info = JSONObject.parseObject(param, LoanFinancing.class);
            loanFinancingService.selectAllList(info, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            e.printStackTrace();
            log.error("查询贷款理财产品列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询贷款理财产品列表异常!");
        }
        return msg;
    }

    /**
     * 贷款理财产品配置新增
     */
    @RequestMapping(value = "/addLoanFinancing")
    @ResponseBody
    @SystemLog(description = "贷款理财产品配置新增",operCode="loanFinancing.addLoanFinancing")
    public Map<String,Object> addLoanFinancing(@RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            LoanFinancing info = JSONObject.parseObject(param, LoanFinancing.class);
            int num=loanFinancingService.addLoanFinancing(info);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "新增成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "新增失败!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("贷款理财产品配置新增异常!",e);
            msg.put("status", false);
            msg.put("msg", "贷款理财产品配置新增异常!");
        }
        return msg;
    }

    /**
     * 上/下架
     */
    @RequestMapping(value = "/updateLoanFinancingStatus")
    @ResponseBody
    @SystemLog(description = "上/下架操作",operCode="loanFinancing.updateLoanFinancingStatus")
    public Map<String,Object> updateLoanFinancingStatus(@RequestParam("id") int id,@RequestParam("status") int status){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=loanFinancingService.updateLoanFinancingStatus(id,status);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "操作成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "操作失败!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("上/下架操作异常!",e);
            msg.put("status", false);
            msg.put("msg", "上/下架操作异常!");
        }
        return msg;
    }

    /**
     * 贷款理财产品配置详情
     */
    @RequestMapping(value = "/getLoanFinancing")
    @ResponseBody
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    public Map<String,Object> getLoanFinancing(@RequestParam("id") int id){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            LoanFinancing info = loanFinancingService.getLoanFinancing(id);
            msg.put("status", true);
            msg.put("info", info);
        } catch (Exception e){
            e.printStackTrace();
            log.error("贷款理财产品配置详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "贷款理财产品配置详情异常!");
        }
        return msg;
    }

    /**
     * 贷款理财产品配置修改
     */
    @RequestMapping(value = "/updateLoanFinancing")
    @ResponseBody
    @SystemLog(description = "贷款理财产品配置修改",operCode="loanFinancing.updateLoanFinancing")
    public Map<String,Object> updateLoanFinancing(@RequestParam("info") String param){
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            LoanFinancing info = JSONObject.parseObject(param, LoanFinancing.class);
            int num=loanFinancingService.updateLoanFinancing(info);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "修改成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "修改失败!");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("贷款理财产品配置修改异常!",e);
            msg.put("status", false);
            msg.put("msg", "贷款理财产品配置修改异常!");
        }
        return msg;
    }

    /**
     * 导出贷款理财配置列表失败
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @SystemLog(description = "贷款理财产品配置修改",operCode="loanFinancing.importDetail")
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            LoanFinancing info = JSONObject.parseObject(param, LoanFinancing.class);
            loanFinancingService.importDetail(info,response);
        }catch (Exception e){
            e.printStackTrace();
            log.error("导出贷款理财配置列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出贷款理财配置列表异常!");
        }
        return msg;
    }
}
