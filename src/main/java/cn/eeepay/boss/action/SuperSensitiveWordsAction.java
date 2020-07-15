package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SensitiveWords;
import cn.eeepay.framework.service.SuperSensitiveWordsService;
import cn.eeepay.framework.util.RandomNumber;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuks
 * 敏感词列表
 */
@Controller
@RequestMapping(value = "/superSensitiveWords")
public class SuperSensitiveWordsAction {
    private static final Logger log = LoggerFactory.getLogger(SuperSensitiveWordsAction.class);

    @Resource
    private SuperSensitiveWordsService superSensitiveWordsService;

    /**
     * 查询列表
     */
    @RequestMapping(value = "/selectByParam.do")
    @ResponseBody
    public Page<SensitiveWords> selectByParam(@RequestParam("baseInfo") String param, @ModelAttribute("page")
            Page<SensitiveWords> page) throws Exception{
        try{
            SensitiveWords sensitiveWords = JSONObject.parseObject(param, SensitiveWords.class);
            superSensitiveWordsService.selectAllList(sensitiveWords, page);
        } catch (Exception e){
            log.error("条件查询敏感词列表失败!",e);
        }
        return page;
    }

    /**
     * 保存敏感字
     */
    @RequestMapping(value = "/saveSensitiveWords")
    @SystemLog(description = "保存敏感字",operCode="superSensitiveWords.saveSensitiveWords")
    @ResponseBody
    public Map<String, Object> saveSensitiveWords(@RequestParam("baseInfo") String param) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        try {
            SensitiveWords sensitiveWords = JSONObject.parseObject(param, SensitiveWords.class);
            if(sensitiveWords.getId()<=0){
                SensitiveWords sw =superSensitiveWordsService.selectListbyKeyWord(sensitiveWords.getKeyWord());
                if(sw==null){
                    sensitiveWords.setSensitiveNo(RandomNumber.mumberRandom("SW",8,0));
                    sensitiveWords.setStatus("1");
                    int ret = superSensitiveWordsService.insertSensitiveWords(sensitiveWords);
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
    @SystemLog(description = "删除敏感字",operCode="superSensitiveWords.deleteSensitiveWords")
    @ResponseBody
    public Map<String, Object> deleteSensitiveWords(@PathVariable("id") int id) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        if(id>0){
            SensitiveWords sensitiveWords = new SensitiveWords();
            sensitiveWords.setId(id);
            try {
                int ret = superSensitiveWordsService.deleteSensitiveWords(sensitiveWords);
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
    @SystemLog(description = "批量删除敏感字",operCode="superSensitiveWords.batchDeleteSensitiveWords")
    @ResponseBody
    public Map<String, Object> deleteSensitiveWords(@RequestParam("swlist") String param) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        List list = JSONObject.parseObject(param, List.class);
        if(list!=null&&list.size()>0){
            try {
                int ret = superSensitiveWordsService.batchDeleteSensitiveWords(list);
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
                List<SensitiveWords> addlist = new ArrayList<SensitiveWords>();
                List<SensitiveWords> updatelist = new ArrayList<SensitiveWords>();
                List<String> listStr = new ArrayList<String>();
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
                            jsonMap.put("status", false);
                            jsonMap.put("msg", "文件中有重复的数据!");
                            return jsonMap;
                        }else{
                            listStr.add(keyWord);
                        }
                        SensitiveWords sw =superSensitiveWordsService.selectListbyKeyWord(keyWord);
                        if(sw==null){
                            SensitiveWords bean=new SensitiveWords();
                            bean.setSensitiveNo(RandomNumber.mumberRandom("SW",8,0));
                            bean.setKeyWord(keyWord);
                            bean.setStatus("1");
                            addlist.add(bean);
                        }else{
                            updatelist.add(sw);
                        }
                    }else{
                        jsonMap.put("status", false);
                        jsonMap.put("msg", "文件中有为空的数据!");
                        return jsonMap;
                    }
                }

                if(addlist.size()>0){
                    superSensitiveWordsService.batchInsertSensitiveWords(addlist);
                }
                if(updatelist.size()>0){
                    for(SensitiveWords t:updatelist){
                        superSensitiveWordsService.updateSensitiveWords(t);
                    }
                    jsonMap.put("status", true);
                    jsonMap.put("msg", "操作成功,有"+ updatelist.size()+"条数据已存在!");
                    return jsonMap;
                }

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

}
