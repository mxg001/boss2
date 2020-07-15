package cn.eeepay.boss.action;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BankImport;
import cn.eeepay.framework.model.CreditcardSource;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.service.BankImportService;
import cn.eeepay.framework.service.CreditcardSourceService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author tans
 * 信用卡银行数据导入管理
 */
@Controller
@RequestMapping("/bankImport")
public class BankImportAction {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private BankImportService bankImportService;

    @Resource
    private CreditcardSourceService creditcardSourceService;

    @RequestMapping("/selectList")
    @ResponseBody
    public Result selectList(@RequestBody BankImport baseInfo,
                             @RequestParam(defaultValue = "1")int pageNo,
                             @RequestParam(defaultValue = "10")int pageSize){
        Result result = new Result();
        try {
            Page<BankImport> page = new Page<>(pageNo, pageSize);
            bankImportService.selectList(baseInfo, page);
            result.setMsg("查询成功");
            result.setStatus(true);
            result.setData(page);
        } catch (Exception e){
            log.error("查询银行导入记录异常", e);
            result = ResponseUtil.buildResult(e);
        }
        return result;
    }

    @RequestMapping("/importData")
    @ResponseBody
    public Result importData(@RequestParam("file") MultipartFile file,
                             @RequestParam Long id,
                             @RequestParam(defaultValue = "1") String bonusType){
        Result result = new Result();
        try {
            if(id == null){
                result.setMsg("银行不能为空");
                return result;
            }
            if(!CommonUtil.checkExcelFile(file, result)){
                return result;
            }
            result = bankImportService.importData(file, id, bonusType);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("导入银行数据异常", e);
        }
        return result;
    }

    /**
     * 匹配
     * @param param
     * @return
     */
    @RequestMapping("/matchData")
    @ResponseBody
    public Result matchData(@RequestBody String param){
        JSONObject json = JSONObject.parseObject(param);
        Integer id = json.getInteger("id");
        Result result = new Result();
        try {
            if(id == null){
                result.setMsg("参数不能为空");
                return result;
            }
            result = bankImportService.matchData(id);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("匹配异常", e);
        }
        return result;
    }

    /**
     * 导入模板下载
     * @param request
     * @param response
     * @param id
     */
    @RequestMapping("/importTemplate")
    public void loanOrderTemplate(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam Long id,
                                  @RequestParam String bonusType) throws UnsupportedEncodingException {
        String template = "";
        String fileName = "";
        CreditcardSource bankInfo = creditcardSourceService.selectDetail(id);
        String ruleCode = bankInfo.getRuleCode();
        if(StringUtils.isBlank(ruleCode)){
            try {
                String url = request.getRequestURI();
                if(url.contains("boss")){
                    String[] urlArr = url.split("/");
                    response.sendRedirect("/" + urlArr[1] + "/welcome.do#/superBank/superBank/bankImport");
                } else {
                    response.sendRedirect("/welcome.do#/superBank/superBank/bankImport");
                }

            } catch (IOException e) {
                log.error("重定向异常", e);
            }
        } else {
            switch (ruleCode){
                case "SHYH002":
                    template = "shyhTemplate.xlsx";
                    fileName = "上海银行导入模板.xlsx";
                    break;
                case "MSYH002":
                    template = "msyhTemplate.xlsx";
                    fileName = "民生银行导入模板.xlsx";
                    break;
                case "PAYH002":
                    if("2".equals(bonusType)){
                        template = "payhBrushTemplate.xlsx";
                        fileName = "平安银行首刷导入模板.xlsx";
                    } else {
                        template = "payhTemplate.xlsx";
                        fileName = "平安银行发卡导入模板.xlsx";
                    }
                    break;
                case "XYYH002":
                    template = "xyyhTemplate.xlsx";
                    fileName = "兴业银行导入模板.xlsx";
                    break;
                case "ZXYH002":
                    template = "zxyhTemplate.xlsx";
                    fileName = "中信银行导入模板.xlsx";
                    break;
                case "JTYH002":
                    template = "jtyhTemplate.xlsx";
                    fileName = "交通银行导入模板.xlsx";
                    break;
                case "ZSYH002":
                    template = "zsyhTemplate.xlsx";
                    fileName = "招商银行导入模板.xlsx";
                    break;
                case "JSYH002":
                    template = "jsyhTemplate.xlsx";
                    fileName = "建设银行导入模板.xlsx";
                    break;
                case "GFYH002":
                    template = "gfyhTemplate.xlsx";
                    fileName = "广发银行导入模板.xlsx";
                    break;
                case "WZYH002":
                    template = "wzyhTemplate.xlsx";
                    fileName = "温州银行全卡种导入模板.xlsx";
                    break;
                case "WZYH2002":
                    template = "wzyhTemplate01.xlsx";
                    fileName = "温州银行导入模板.xlsx";
                    break;
                case "HXYH002":
                    template = "hxyhTemplate.xlsx";
                    fileName = "华夏银行导入模板.xlsx";
                    break;
                case "PFYH002":
                    template = "pfyhTemplate.xlsx";
                    fileName = "浦发银行导入模板.xlsx";
                    break;
                case "ZGYH002":
                    template = "zgyhTemplate.xlsx";
                    fileName = "中国银行导入模板.xlsx";
                    break;
                default:
                    break;
            }
            String filePath = request.getServletContext().getRealPath("/")+ File.separator+"template"+File.separator+template;
            ResponseUtil.download(response, filePath,fileName);
        }

    }
}
