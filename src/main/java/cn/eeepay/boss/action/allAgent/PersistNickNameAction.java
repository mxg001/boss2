package cn.eeepay.boss.action.allAgent;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.allAgent.PersistNickName;
import cn.eeepay.framework.service.allAgent.PersistNickNameService;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/persistNickName")
public class PersistNickNameAction {
    private static final Logger log = LoggerFactory.getLogger(PersistNickNameAction.class);

    @Resource
    private PersistNickNameService persistNickNameService;

    /**
     * 保留昵称列表查询
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/selectPersistNickNameList")
    @ResponseBody
    public Map<String, Object> selectPersistNickNameList(@RequestParam("info") String param, @ModelAttribute("page")
            Page<PersistNickName> page) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            PersistNickName info = JSONObject.parseObject(param, PersistNickName.class);
            persistNickNameService.selectPersistNickNameList(info, page);
            msg.put("page", page);
            msg.put("status", true);
        } catch (Exception e) {
            log.error("保留昵称列表查询异常!", e);
            msg.put("status", false);
            msg.put("msg", "保留昵称列表查询异常!");
        }
        return msg;
    }

    @RequestMapping(value = "/addPersistNickName")
    @ResponseBody
    @SystemLog(description = "新增保留昵称",operCode="persistNickName.addPersistNickName")
    public Map<String, Object> insertPersistNickName(@RequestParam("info") String param) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            PersistNickName info = JSONObject.parseObject(param, PersistNickName.class);
            persistNickNameService.insertPersistNickName(info);
            msg.put("status", true);
            msg.put("msg", "新增成功!");
        } catch (Exception e) {
            log.error("编辑异常!", e);
            msg.put("status", false);
            msg.put("msg", "新增异常!");
        }
        return msg;
    }

    /**
     * 保留昵称详情
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/selectPersistNickNameDetail")
    @ResponseBody
    public Map<String, Object> selectPersistNickNameDetail(@RequestParam("id") Integer id) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            PersistNickName data = persistNickNameService.selectPersistNickNameById(id);
            msg.put("data", data);
            msg.put("status", true);
        } catch (Exception e) {
            log.error("保留昵称详情异常!", e);
            msg.put("status", false);
            msg.put("msg", "保留昵称详情异常!");
        }
        return msg;
    }

    @RequestMapping(value = "/updatePersistNickName")
    @ResponseBody
    @SystemLog(description = "编辑保留昵称",operCode="persistNickName.updatePersistNickName")
    public Map<String, Object> updatePersistNickName(@RequestParam("info") String param) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            PersistNickName info = JSONObject.parseObject(param, PersistNickName.class);
            persistNickNameService.updatePersistNickName(info);
            msg.put("status", true);
            msg.put("msg", "编辑成功!");
        } catch (Exception e) {
            log.error("编辑异常!", e);
            msg.put("status", false);
            msg.put("msg", "编辑异常!");
        }
        return msg;
    }

    @RequestMapping(value = "/deletePersistNickName")
    @ResponseBody
    @SystemLog(description = "删除保留昵称",operCode="persistNickName.deletePersistNickName")
    public Map<String, Object> deletePersistNickName(@RequestParam("id") Integer id) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            persistNickNameService.deletePersistNickName(id);
            msg.put("status", true);
            msg.put("msg", "删除成功!");
        } catch (Exception e) {
            log.error("删除异常!", e);
            msg.put("status", false);
            msg.put("msg", "删除异常!");
        }
        return msg;
    }

    /**
     * 下载模板
     */
    @RequestMapping("/downloadTemplate")
    public String downloadMachineBuyOrderTemplate(HttpServletRequest request, HttpServletResponse response) {
        String filePath = request.getServletContext().getRealPath("/") + File.separator + "template" + File.separator
                + "persistNickNameTemplate.xlsx";
        log.info(filePath);
        ResponseUtil.download(response, filePath, "保留昵称批量导入模板.xls");
        return null;
    }

    public String getCellValue(Cell cell) {
        if(cell!=null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    return String.valueOf(cell.getNumericCellValue());
                case Cell.CELL_TYPE_STRING:
                    return cell.getStringCellValue();
                case Cell.CELL_TYPE_BLANK:
                    return "";
                case Cell.CELL_TYPE_BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case Cell.CELL_TYPE_FORMULA:
                    return cell.getStringCellValue();
            }
        }
        return null;
    }

    /**
     * 批量导入
     * @param file
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/importButchUpload")
    public @ResponseBody Object importButchUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        int errorCount=0;
        int successCount=0;
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            if (!file.isEmpty()) {
                String format = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                if (!format.equals(".xls") && !format.equals(".xlsx")) {
                    jsonMap.put("status", false);
                    jsonMap.put("msg", "文件格式错误");
                    return jsonMap;
                }
                Workbook wb = WorkbookFactory.create(file.getInputStream());
                Sheet sheet = wb.getSheetAt(0);
                // 遍历所有单元格，读取单元格
                int row_num = sheet.getLastRowNum();
                if (row_num < 1) {
                    jsonMap.put("status", false);
                    jsonMap.put("msg", "文件内容错误");
                    return jsonMap;
                }
                List<PersistNickName> errorlist = new ArrayList<PersistNickName>();
                for (int i = 1; i <= row_num; i++) {
                    Row row = sheet.getRow(i);
                    String num1 = getCellValue(row.getCell(0));

                    String keyWord=num1;

                    PersistNickName persistNickName = new PersistNickName();
                    persistNickName.setKeyWord(keyWord);
                    if(keyWord==null||"".equals(keyWord)){
                        errorCount++;
                        persistNickName.setErrorResult("保留昵称不能为空");
                        errorlist.add(persistNickName);
                        continue;
                    }
                    try {
                        int num=persistNickNameService.insertPersistNickNameAll(persistNickName);
                        successCount+=num;
                    }catch (Exception e){
                        log.error("导入异常");
                        persistNickName.setErrorResult("网络异常");
                        errorlist.add(persistNickName);
                        continue;
                    }
                }
                jsonMap.put("errorCount", errorCount);
                jsonMap.put("successCount", successCount);
                jsonMap.put("errorlist", errorlist);
                jsonMap.put("status", true);
                jsonMap.put("msg", "操作成功");
                return jsonMap;
            } else {
                jsonMap.put("status", false);
                jsonMap.put("msg", "文件格式错误");
            }
        }catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("status", false);
            jsonMap.put("msg", "数据异常");
        }
        return jsonMap;
    }

    @RequestMapping("/exportPersistNickName")
    public void exportPersistNickName(@RequestParam String param, HttpServletResponse response){
        try {
            PersistNickName info = JSON.parseObject(param,PersistNickName.class);
            persistNickNameService.exportPersistNickName(info, response);
        } catch (Exception e) {
            log.info("导出盟主活动返现明细失败,参数:{}");
            log.info(e.toString());
        }
    }
}
