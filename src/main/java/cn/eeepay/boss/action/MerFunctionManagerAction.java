package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.MerFunctionManagerService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
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
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/merFunctionManager")
public class MerFunctionManagerAction {

    @Resource
    private MerFunctionManagerService merFunctionManagerService;

    private static final Logger log = LoggerFactory.getLogger(MerFunctionManagerAction.class);

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/selectByParam.do")
    @ResponseBody
    public Result selectByParam(@RequestParam("info") String param,
                                @ModelAttribute("page") Page<FunctionMerchant> page) {

        Result result = new Result();
        try {
            FunctionMerchant functionMerchant = JSONObject.parseObject(param, FunctionMerchant.class);
            merFunctionManagerService.selectByParam(functionMerchant, page);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(page);
        } catch (Exception e) {
            result = ResponseUtil.buildResult(e);
            log.error("条件查询商户黑名单异常", e);
        }
        return result;
    }

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/exportConfig")
    @ResponseBody
    @SystemLog(description = "导出商户黑名单", operCode = "func.exportMer")
    public Object exportConfig(@RequestParam("info") String param, HttpServletRequest request, HttpServletResponse response) {
        try {
            JSONObject object = JSONObject.parseObject(param);
            //每次请求都会上传随机数，如果出现重复，则不给予下载
            String random = String.valueOf(object.get("random"));
            Object has = request.getSession().getAttribute(random);
            //人工处理重复请求
            if (null != has) {
                return null;
            }
            request.getSession().setAttribute(random, random);
            FunctionMerchant functionMerchant = object.toJavaObject(FunctionMerchant.class);

            List<FunctionMerchant> list = merFunctionManagerService.exportConfig(functionMerchant);

            if (list != null && list.size() > 0) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String fileName = "商户黑名单导出-" + sdf.format(new Date()) + ".csv";
                String fileNameFormat = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
                response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);

                OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
                out.append("业务组织,商户编号,商户名称\r");
                for (FunctionMerchant item : list) {
                    out.append(item.getTeamName());
                    if (item.getTeamEntryName() != null) {
                        out.append("-" + item.getTeamEntryName());
                    }
                    out.append(",");
                    out.append(item.getMerchantNo()).append("\t,");
                    out.append((item.getMerchantName() + "").replaceAll("\n", "").replaceAll("\r", ""));
                    out.append("\r");
                }
                out.close();
            }
        } catch (Exception e) {
            log.error("导出异常", e);
        }
        return null;
    }

    @RequestMapping(value = "/findMerInfoByMerNo.do")
    @ResponseBody
    public MerchantInfo findMerInfo(String merNo) {
        return merFunctionManagerService.findMerInfoByMerNo(merNo);
    }

    @RequestMapping(value = "/addMerFunctionManager")
    @ResponseBody
    @SystemLog(description = "新增商户黑名单", operCode = "func.insertMer")
    public Result addMerFunctionManager(@RequestBody String param) {
        Result result = new Result();
        try {
            JSONObject json = JSON.parseObject(param);
            FunctionMerchant functionMerchant = json.getObject("info", FunctionMerchant.class);
            functionMerchant.setOperator(CommonUtil.getLoginUserName());
            functionMerchant.setType("1");
            int existsNum = merFunctionManagerService.selectExists(functionMerchant);
            if (existsNum > 0) {
                result.setMsg("该商户已存在");
                return result;
            }
            MerchantInfo merInfo = merFunctionManagerService.findMerInfoByMerNo(functionMerchant.getMerchantNo());
            if (merInfo == null) {
                result.setMsg("该商户不存在");
                return result;
            }
            int i = merFunctionManagerService.addFunctionMerchant(functionMerchant);
            if (i > 0) {
                result.setMsg("添加成功");
                result.setStatus(true);
            } else if (i == 0) {
                result.setMsg("添加失败");
            }

        } catch (Exception e) {
            result = ResponseUtil.buildResult(e);
            log.error("新增商户黑名单异常", e);
        }
        return result;
    }

    @RequestMapping(value = "/deleteInfo")
    @ResponseBody
    @SystemLog(description = "批量删除商户黑名单", operCode = "func.deleteMerBatch")
    public Result deleteInfo(@RequestBody String param) {
        Result result = new Result();
        try {
            List<FunctionMerchant> functionMerchantList = JSON.parseArray(param, FunctionMerchant.class);
            int num = 0;
            for (FunctionMerchant functionMerchant : functionMerchantList) {
                num += merFunctionManagerService.delete(functionMerchant.getId());
            }
            if (num > 0) {
                result.setMsg("操作成功");
                result.setStatus(true);
            } else {
                result.setMsg("操作失败");
            }
        } catch (Exception e) {
            result = ResponseUtil.buildResult(e);
            log.error("批量删除商户黑名单异常", e);
        }
        return result;
    }

    @RequestMapping(value = "/deleteById")
    @ResponseBody
    @SystemLog(description = "删除商户黑名单", operCode = "func.deleteMer")
    public Result deleteById(@RequestParam("id") Integer id) {
        Result result = new Result();
        try {
            int i = merFunctionManagerService.delete(id);
            if (i > 0) {
                result.setStatus(true);
                result.setMsg("删除成功");
            } else {
                result.setMsg("删除失败");
            }
        } catch (Exception e) {
            result = ResponseUtil.buildResult(e);
            log.error("删除商户黑名单异常", e);
        }
        return result;
    }


    @RequestMapping(value = "/addButchMerFunctionManager")
    @SystemLog(description = "批量新增商户黑名单", operCode = "func.insertMerBatch")
    public @ResponseBody
    Result importTerminal(@RequestParam("file") MultipartFile file, @RequestParam("functionNumber") String functionNumber) throws Exception {
        Result result = new Result();
        try {
            if (file.isEmpty()) {
                result.setMsg("上传文件为空");
                return result;
            }

            String format = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            if (!format.equals(".xls") && !format.equals(".xlsx")) {
                result.setMsg("文件格式错误");
                return result;
            }
            Workbook wb = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = wb.getSheetAt(0);
            // 遍历所有单元格，读取单元格
            int row_num = sheet.getLastRowNum();
            if (row_num < 1) {
                result.setMsg("文件内容错误");
                return result;
            }
            int state = 0;
            Map<String, Object> importMap = new HashMap<>();
            List<Map<String, Object>> returnList = new ArrayList<>();
            for (int i = 1; i <= row_num; i++) {
                Map<String, Object> importReturnMap = new HashMap<>();
                Row row = sheet.getRow(i);

                String merchantNo = getCellValue(row.getCell(1));
                String merchantName = getCellValue(row.getCell(2));
                String handle = getCellValue(row.getCell(3));//操作:新增,删除
                if (StringUtil.isBlank(handle)) {//为空默认新增
                    handle = "新增";
                }


                if (StringUtils.isBlank(merchantNo)) {
                    importReturnMap.put("errMsg", "第" + (i + 1) + "行商户编号为空");
                    returnList.add(importReturnMap);
                    continue;
                }
                merchantNo=merchantNo.trim();
                merchantNo=merchantNo.replaceAll("\r","");
                merchantNo=merchantNo.replaceAll(" ","");
                merchantNo=merchantNo.replaceAll("\n","");
                importReturnMap.put("merchantNo", merchantNo);
                importReturnMap.put("handle", handle);

                MerchantInfo merchantInfo = merFunctionManagerService.findMerInfoByMerNo(merchantNo);

                if (merchantInfo == null) {
                    importReturnMap.put("errMsg", "第" + (i + 1) + "行【" + merchantNo + "】【" + merchantName + "】不存在该商户");
                    returnList.add(importReturnMap);
                    continue;
                }


                if ("新增".equals(handle)) {
                    FunctionMerchant functionMerchant = new FunctionMerchant();
                    functionMerchant.setMerchantNo(merchantNo);
                    functionMerchant.setFunctionNumber(functionNumber);
                    functionMerchant.setOperator(CommonUtil.getLoginUserName());
                    functionMerchant.setType("1");
                    //检查是否已存在
                    int hasNum = merFunctionManagerService.selectExists(functionMerchant);
                    if (hasNum > 0) {
                        importReturnMap.put("errMsg", "第" + (i + 1) + "行【" + merchantNo + "】【" + merchantInfo.getMerchantName() + "】已存在");
                        returnList.add(importReturnMap);
                        continue;
                    }
                    merFunctionManagerService.addFunctionMerchant(functionMerchant);
                    state++;
                } else if ("删除".equals(handle)) {
                    merFunctionManagerService.deleteInfo(merchantNo, functionNumber);
                    state++;
                }

            }
            importMap.put("successCount", state);
            importMap.put("errCount", row_num - state);
            importMap.put("list", returnList);

            result.setStatus(true);
            result.setData(importMap);
            result.setMsg("操作成功");
            return result;

        } catch (Exception e) {
            result = ResponseUtil.buildResult(e);
            log.error("批量新增商户黑名单异常", e);
        }
        return result;
    }

    /**
     * 下载模板
     */
    @RequestMapping("/downloadTemplate")
    public String downloadAdjustAccTemplate(HttpServletRequest request, HttpServletResponse response) {
        String filePath = request.getServletContext().getRealPath("/") + File.separator + "template" + File.separator + "merFunctionManagerTemplate.xlsx";
        ResponseUtil.download(response, filePath, "商户批量新增模板.xlsx");
        return null;
    }

    public String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
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
