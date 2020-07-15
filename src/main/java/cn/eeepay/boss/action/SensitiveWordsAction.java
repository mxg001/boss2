package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SensitiveWords;
import cn.eeepay.framework.service.SensitiveWordsService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.RandomNumber;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

/**
 * @author liuks
 * 敏感词列表
 */
@Controller
@RequestMapping(value = "/sensitiveWords")
public class SensitiveWordsAction {
    private static final Logger log = LoggerFactory.getLogger(SensitiveWordsAction.class);

    @Resource
    private SensitiveWordsService sensitiveWordsService;

    /**
     * 查询列表
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectByParam.do")
    @ResponseBody
    public Page<SensitiveWords> selectByParam(@RequestParam("baseInfo") String param, @ModelAttribute("page")
            Page<SensitiveWords> page) throws Exception{
        try{
            SensitiveWords sensitiveWords = JSONObject.parseObject(param, SensitiveWords.class);
            sensitiveWordsService.selectAllList(sensitiveWords, page);
        } catch (Exception e){
            log.error("条件查询敏感词列表失败!",e);
        }
        return page;
    }

    /**
     * 保存敏感字
     */
    @RequestMapping(value = "/saveSensitiveWords")
    @SystemLog(description = "保存敏感字",operCode="sensitiveWords.saveSensitiveWords")
    @ResponseBody
    public Map<String, Object> saveSensitiveWords(@RequestParam("baseInfo") String param) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try {
            SensitiveWords sensitiveWords = JSONObject.parseObject(param, SensitiveWords.class);
            if(sensitiveWords.getId()<=0){
                SensitiveWords sw =sensitiveWordsService.selectListbyKeyWord(sensitiveWords.getKeyWord());
                if(sw==null){
                    sensitiveWords.setSensitiveNo(RandomNumber.mumberRandom("SW",8,0));
                    sensitiveWords.setStatus("1");
                    int ret = sensitiveWordsService.insertSensitiveWords(sensitiveWords);
                    if (ret > 0) {
                        msg.put("status", true);
                        msg.put("msg", "新增敏感字成功!");
                    }
                }else{
                    msg.put("status", true);
                    msg.put("msg", "关键字已存在!");
                }
            }
        } catch (Exception e) {
            log.error("新增敏感字失败!", e);
            msg.put("status", false);
            msg.put("msg", "新增敏感字失败!");
        }
        return msg;
    }


    /**
     * 删除敏感字
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deleteSensitiveWords/{id}")
    @SystemLog(description = "删除敏感字",operCode="sensitiveWords.deleteSensitiveWords")
    @ResponseBody
    public Map<String, Object> deleteSensitiveWords(@PathVariable("id") int id) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        if(id>0){
            SensitiveWords sensitiveWords = new SensitiveWords();
            sensitiveWords.setId(id);
            try {
                int ret = sensitiveWordsService.deleteSensitiveWords(sensitiveWords);
                if (ret > 0) {
                    msg.put("status", true);
                    msg.put("msg", "删除敏感字成功!");
                }
            }catch (Exception e) {
                log.error("删除敏感字失败!", e);
                msg.put("status", false);
                msg.put("msg", "删除敏感字失败!");
            }
        }else{
            msg.put("status", false);
            msg.put("msg", "删除敏感字失败!");
        }
        return msg;
    }

    /**
     * 批量删除敏感字
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/batchDeleteSensitiveWords")
    @SystemLog(description = "批量删除敏感字",operCode="sensitiveWords.batchDeleteSensitiveWords")
    @ResponseBody
    public Map<String, Object> deleteSensitiveWords(@RequestParam("swlist") String param) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        List list = JSONObject.parseObject(param, List.class);
        if(list!=null&&list.size()>0){
            try {
                int ret = sensitiveWordsService.batchDeleteSensitiveWords(list);
                if (ret > 0) {
                    msg.put("status", true);
                    msg.put("msg", "批量删除敏感字成功!");
                }
            }catch (Exception e) {
                log.error("批量删除敏感字失败!", e);
                msg.put("status", false);
                msg.put("msg", "批量删除敏感字失败!");
            }
        }else{
            msg.put("status", false);
            msg.put("msg", "批量删除敏感字失败!");
        }
        return msg;
    }


    // 批量新增关键字,如果已含有提示错误
    @RequestMapping(value = "/addButchSensitiveWords")
    @ResponseBody
    public Object addButchSensitiveWords(@RequestParam("file") MultipartFile file) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
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
                if(row_num > 50000){
                    jsonMap.put("status", false);
                    jsonMap.put("msg", "导入的文件超出限制");
                    return jsonMap;
                }
                List<SensitiveWords> addlist = new ArrayList<SensitiveWords>();
                List<SensitiveWords> updatelist = new ArrayList<SensitiveWords>();
                List<String> listStr = new ArrayList<String>();
                int successCount = 0;
                int failCount = 0 ;
                List<String> msgList = new ArrayList<>();
                for (int i = 1; i <= row_num; i++) {
                    Row row = sheet.getRow(i);
                    String num1 = getCellValue(row.getCell(0));
                    if (StringUtils.isBlank(num1)){
                        break;
                    }
                    String[] str1 = num1.split("\\.");
                    String keyWord = str1[0];
                    if(keyWord!=null&&!"".equals(keyWord)){
                        if(listStr.contains(keyWord)){
                            failCount++;
                            msgList.add("第"+(i+1)+ "行有重复的数据!");
                            continue;
                        }else{
                            listStr.add(keyWord);
                        }
                        SensitiveWords sw =sensitiveWordsService.selectListbyKeyWord(keyWord);
                        if(sw==null){
                            SensitiveWords bean=new SensitiveWords();
                            bean.setSensitiveNo(RandomNumber.mumberRandom("SW",8,0));
                            bean.setKeyWord(keyWord);
                            bean.setStatus("1");
                            addlist.add(bean);
                        }else{
                            failCount++;
                            msgList.add("第"+(i+1)+ "行数据已存在!");
                            updatelist.add(sw);
                        }
                    }else{
                        failCount++;
                        msgList.add("第"+(i+1)+ "行为空的数据!");
                        continue;
                    }
                }

                if(addlist.size()>0){
                    sensitiveWordsService.batchInsertSensitiveWords(addlist);
                    successCount+= addlist.size();
                }
                if(updatelist.size()>0){
                    for(SensitiveWords t:updatelist){
                        sensitiveWordsService.updateSensitiveWords(t);
                    }
                    //jsonMap.put("status", true);
                    //jsonMap.put("msg", "操作成功,有"+ updatelist.size()+"条数据已存在!");
                    //return jsonMap;
                }

                StringBuffer sb = new StringBuffer();
                for (String str: msgList) {
                    sb.append(str).append("\\n");
                }
                jsonMap.put("msg", sb.toString());
                jsonMap.put("status", true);
                jsonMap.put("successCount", successCount);
                jsonMap.put("failCount", failCount);
                //jsonMap.put("msg", "操作成功");
                return jsonMap;
            } else {
                jsonMap.put("successCount", 0);
                jsonMap.put("failCount", 0);
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

    public String getCellValue(Cell cell) {
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
        return null;
    }

    /**
     * 批量新增模板导出
     */
    @RequestMapping("/downloadTemplate")
    public String downloadSensitiveWordsTemplate(HttpServletRequest request, HttpServletResponse response){
        String filePath = request.getServletContext().getRealPath("/")+ File.separator+"template"+File.separator+"sensitiveWordsTemplate.xls";
        ResponseUtil.download(response, filePath,"敏感词批量新增模板.xlsx");
        return null;
    }
}
