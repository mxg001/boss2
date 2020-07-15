package cn.eeepay.boss.action.capitalInsurance;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.capitalInsurance.BillEntry;
import cn.eeepay.framework.model.capitalInsurance.CheckBill;
import cn.eeepay.framework.service.capitalInsurance.BillEneryService;
import cn.eeepay.framework.service.capitalInsurance.CheckBillService;
import cn.eeepay.framework.util.RandomNumber;
import cn.eeepay.framework.util.ResponseUtil;
import cn.eeepay.framework.util.StringUtil;

/**
 * 保单对账管理
 *
 * @author ivan
 */

@Controller
@RequestMapping(value = "/checkBillAction")
public class CheckBillAction {
    private static final Logger log = LoggerFactory.getLogger(CheckBillAction.class);
    @Autowired
    private CheckBillService checkBillService;
    @Autowired
    private BillEneryService billEneryService;

    /**
     * @param page
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping("/getBillAllInfo")
    @ResponseBody
    public Object getBillAllInfo(@ModelAttribute("page") Page<CheckBill> page,
                                 @RequestParam("info") String param) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            CheckBill tis = JSON.parseObject(param, CheckBill.class);
            checkBillService.queryAllInfo(tis, page);
            map.put("page", page);
            map.put("bols", true);
        } catch (Exception e) {
            log.error("查询报错", e);
            map.put("bols", false);
            map.put("msg", "查询报错");
        }
        return map;
    }

    /**
     * 下载对账文件模板
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/downloadBillTemplate.do")
    public String downloadZqMerTemplate(HttpServletRequest request, HttpServletResponse response) {
        String orderType = request.getParameter("orderType");
        String insurer = request.getParameter("insurer");
        String filePath = null;
        String fileName = "";
        if ("1".equals(orderType)) {// 投保订单
            if ("yilian".equals(insurer)) {
                fileName = "前海财险投保对账文件导入模板.xlsx";
                filePath = request.getServletContext().getRealPath("/") + "template" + File.separator
                        + "insureBill.xlsx";
            } else if ("zhlh".equals(insurer)) {
                fileName = "中华联合投保对账文件导入模板.csv";
                filePath = request.getServletContext().getRealPath("/") + "template" + File.separator
                        + "zhlhinsureBill.csv";
            }
        } else {
            if ("yilian".equals(insurer)) {
                // 退保订单
                fileName = "退保对账文件导入模板.xlsx";
                filePath = request.getServletContext().getRealPath("/") + "template" + File.separator
                        + "retreatsBill.xlsx";
            }

        }
        log.info(filePath);
        if (filePath != null) {
            ResponseUtil.download(response, filePath, fileName);
        }
        return null;
    }

    /**
     * 清除对账数据
     *
     * @param batchNo
     * @return
     * @throws Exception
     */
    @RequestMapping("/cleanCheckBill")
    @ResponseBody
    public Object cleanCheckBill(@RequestParam("batchNo") String batchNo) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            //校验当前批次数据是否汇总
            int reportCount = billEneryService.reportNumByBatchNo(batchNo);
            if (reportCount > 0) {
                map.put("bols", false);
                map.put("msg", "当前批次已汇总，不可清除");
                return map;
            }
            int count = checkBillService.cleanCheckBill(batchNo);
            if (count > 0) {
                map.put("bols", true);
            } else {
                map.put("bols", false);
                map.put("msg", "清除失败");
            }
        } catch (Exception e) {
            log.error("清除报错", e);
            map.put("bols", false);
            map.put("msg", "清除报错");
        }
        return map;
    }

    /**
     * 对账文件导入
     *
     * @param file
     * @param orderType
     * @param insurer
     * @param request
     * @return
     */
    @RequestMapping(value = "/importInsure.do")
    @ResponseBody
    public Object importInsure(@RequestParam("file") MultipartFile file, @RequestParam("orderType") final String orderType,
                               @RequestParam("insurer") final String insurer, final HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        log.info("导入对账文件>>>>>>orderType=" + orderType + ";insurer=" + insurer);
        try {
            // 参数校验
            result = checkImportParam(file, orderType, insurer, result);
            String res = StringUtil.filterNull(result.get("result"));
            if (!Boolean.valueOf(res)) {
                return result;
            }
            final Map<String, Object> fileMap = (Map<String, Object>) result.get("data");

            Workbook wb = WorkbookFactory.create(file.getInputStream());
            final Sheet sheet = wb.getSheetAt(0);

            result= checkSheet(insurer,sheet,result);
            res = StringUtil.filterNull(result.get("result"));
            if (!Boolean.valueOf(res)) {
                return result;
            }
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            String perfix = null;
            if ("1".equals(orderType)) {
                if ("yilian".equals(insurer)) {
                    perfix = "QHCX";
                } else if ("zhlh".equals(insurer)) {
                    perfix = "ZHLH";
                }
            } else {
                if ("yilian".equals(insurer)) {
                    perfix = "QHCX_TB";
                }
            }
            if (perfix != null) {
                final String batchNo = perfix + RandomNumber.mumberRandom(new SimpleDateFormat("yyyyMMdd").format(new Date()), 3, 0);
                log.info("开始对账，批次号：" + batchNo);
                new Thread(
                        new Runnable() {
                            public void run() {
                                insureFileCheck(insurer, Integer.valueOf(orderType), fileMap, sheet, principal.getRealName(), batchNo);
                            }
                        }
                ).start();
                result.put("batchNo", batchNo);
            } else {
                result.put("result", false);
                result.put("msg", "保存失败");
            }

            return result;
        } catch (Exception e) {
            log.error("导入对账文件------", e);
            result.put("result", false);
            result.put("msg", "保存失败，请稍后再试");
        }
        return result;
    }

    /**
     * 保单对账
     *
     * @param sheet
     * @return
     */
    private void insureFileCheck(String insurer, Integer order_type, Map<String, Object> fileMap, Sheet sheet, String createPerson, String batchNo) {
        log.info("执行对账，批次号：" + batchNo);
        Map<String, Object> result = new HashMap<>();
        String resMsg;
        int row_num = sheet.getLastRowNum();
        Row row;
        //对账日期
        String startDate = StringUtil.filterNull(fileMap.get("startDate"));
        String endDate = StringUtil.filterNull(fileMap.get("endDate"));
        //平台平台交易
        LinkedList<String> orderNoList;
        if (order_type == 1) {
            orderNoList = checkBillService.getTransOrderSuccess(insurer, startDate, endDate);
        } else {
            orderNoList = checkBillService.getTransOrderOver(insurer,startDate, endDate);
        }
        int sys_total_count = orderNoList.size();

        //初始化对账结果
        CheckBill bill = new CheckBill();
        bill.setBatchNo(batchNo);
        bill.setInsurer(insurer);
        bill.setOrderType(order_type);
        bill.setSysTotalCount(sys_total_count);
        bill.setAcqTotalAmount(new BigDecimal(0));
        int acqTotalCount = row_num;
        bill.setAcqSuccessCount(0);
        bill.setAcqFailCount(0);
        bill.setSysSuccessCount(0);
        bill.setSysFailCount(0);
        bill.setSysTotalAmount(new BigDecimal(0));
        bill.setCreatePerson(createPerson);
        bill.setFileDate(StringUtil.filterNull(fileMap.get("fileDate")));
        bill.setFileName(StringUtil.filterNull(fileMap.get("fileName")));
        bill.setCheckTime(new Date());
        Map<String, BillEntry> batcList = new HashMap<String, BillEntry>();
        for (int i = 1; i <= row_num; i++) {
            row = sheet.getRow(i);
            if (null == row) {
                resMsg = "第" + (i + 1) + "行数据不能为空";
                bill.setRemark(resMsg);
                continue;
            }
            result = fillBillEntryData(batchNo, i, row, order_type, insurer);
            //中途数据校验不通过，终止
            if (!(Boolean) result.get("flag")) {
                String data = (String) result.get("data");
                if("BX".equals(data)){
                    acqTotalCount--;
                }else {
                    bill.setRemark((String) result.get("data"));
                }
                continue;
            }
            BillEntry billEntry = (BillEntry) result.get("data");
            billEntry.setCreatePerson(createPerson);
            batcList.put(billEntry.getAcqOrderNo(), billEntry);
            //移除对账文件已有的订单号
            orderNoList.remove(billEntry.getAcqOrderNo());
            if (batcList.size() >= 1000) {//1000条一个批次
                Map<String, Object> resultMap = checkBillService.chechBatchTransOrder(batcList, startDate, endDate, order_type);
                calculate(bill, resultMap);
                batcList.clear();
            }
        }
        //for循环数据组装未通过
        if (!(Boolean) result.get("flag")) {
            //直接生成对账失败
            bill.setCheckStatus(2);
            checkBillService.insertBill(bill);
            return;
        }
        if (batcList.size() > 0) {//截止数据，最后一批次
            Map<String, Object> resultMap = checkBillService.chechBatchTransOrder(batcList, startDate, endDate, order_type);
            calculate(bill, resultMap);
            batcList.clear();
        }
        //剩余订单号，核对平台单边数据
        if (orderNoList.size() > 0) {
            Map<String, Object> resultMap = checkBillService.chechSysTransOrder(orderNoList, bill, order_type);
            calculate(bill, resultMap);
        }
        //上游&平台任意有失败的记录，整体对账失败
        if (bill.getAcqFailCount() > 0 || bill.getSysFailCount() > 0 || orderNoList.size() > 0) {
            bill.setCheckStatus(2);
        } else {
            bill.setCheckStatus(1);
        }
        //记录对账结果
        bill.setAcqTotalCount(acqTotalCount);
        checkBillService.insertBill(bill);
    }

    /**
     * 计算累计金额&条数
     *
     * @param bill
     * @param resultMap
     */
    private void calculate(CheckBill bill, Map<String, Object> resultMap) {
        bill.setAcqTotalAmount(bill.getAcqTotalAmount().add((BigDecimal) resultMap.get("acq_total_amount")));
        bill.setSysTotalAmount(bill.getSysTotalAmount().add((BigDecimal) resultMap.get("sys_total_amount")));
        bill.setAcqSuccessCount(bill.getAcqSuccessCount() + (int) resultMap.get("acq_success_count"));
        bill.setAcqFailCount(bill.getAcqFailCount() + (int) resultMap.get("acq_fail_count"));
        bill.setSysSuccessCount(bill.getSysSuccessCount() + (int) resultMap.get("sys_success_count"));
        bill.setSysFailCount(bill.getSysFailCount() + (int) resultMap.get("sys_fail_count"));
    }


    /**
     * 数据对账组装
     *
     * @param i
     * @param row
     * @param order_type
     * @return
     */
    private Map<String, Object> fillBillEntryData(String batchNo, int i, Row row, Integer order_type, String insurer) {
        log.info("对账数据组装-----");
        BillEntry billEntry = new BillEntry();
        billEntry.setBatchNo(batchNo);
        billEntry.setInsurer(insurer);
        if (order_type == 1) {
            billEntry.setAcqBillStatus("SUCCESS");
        } else {
            billEntry.setAcqBillStatus("OVERLIMIT");
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            billEntry.setOrderType(order_type);
            /**
             * 订单号
             */
            String acq_order_no = getCellValue(row.getCell(getTypeRow(insurer, 1))).trim();
            if (StringUtils.isBlank(acq_order_no)) {
                return returnMap(false, "第" + (i + 1) + "行,订单号不能为空");
            }
            if(!acq_order_no.startsWith("BX")){
                //return returnMap(false,"第" + (i + 1) + "行,订单号不合法");
                return returnMap(false,"BX");
            }
            billEntry.setAcqOrderNo(acq_order_no);
            /**
             * 保单号
             */
            String acq_bill_no = getCellValue(row.getCell(getTypeRow(insurer, 2))).trim();
            if (StringUtils.isBlank(acq_bill_no)) {
                return returnMap(false, "第" + (i + 1) + "行,保单号不能为空");
            }
            billEntry.setAcqBillNo(acq_bill_no);
            /**
             * 被保险人
             */
            String holder = getCellValue(row.getCell(getTypeRow(insurer, 4))).trim();
            if (StringUtils.isBlank(holder)) {
                return returnMap(false, "第" + (i + 1) + "行,被保险人不能为空");
            }
            billEntry.setHolder(holder);
            /**
             * 投保时间
             */
            String insure_time = getCellValue(row.getCell(getTypeRow(insurer, 5))).trim();
            if (StringUtils.isBlank(insure_time)) {
                return returnMap(false, "第" + (i + 1) + "行,投保时间不能为空");
            }
            if (isDouble(insure_time)) {
                billEntry.setInsureTime(HSSFDateUtil.getJavaDate(Double.valueOf(insure_time)));
            } else {
                if (insure_time.indexOf("-") != -1) {
                    billEntry.setInsureTime(sdf1.parse(insure_time));
                } else if (insure_time.indexOf("/") != -1) {
                    billEntry.setInsureTime(sdf2.parse(insure_time));
                } else {
                    return returnMap(false, "第" + (i + 1) + "行,投保时间格式有误");
                }
            }

            /**
             * 保险起期
             */
            String effective_stime = getCellValue(row.getCell(getTypeRow(insurer, 6))).trim();
            if (StringUtils.isBlank(effective_stime)) {
                return returnMap(false, "第" + (i + 1) + "行,保险起期不能为空");
            }
            if (isDouble(effective_stime)) {
                billEntry.setEffectiveStime(HSSFDateUtil.getJavaDate(Double.valueOf(effective_stime)));
            } else {
                if (effective_stime.indexOf("-") != -1) {
                    billEntry.setEffectiveStime(sdf1.parse(effective_stime));
                } else if (effective_stime.indexOf("/") != -1) {
                    billEntry.setEffectiveStime(sdf2.parse(effective_stime));

                } else {
                    return returnMap(false, "第" + (i + 1) + "行,保险起期格式有误");
                }
            }
            /**
             * 保险止期
             */
            String effective_etime = getCellValue(row.getCell(getTypeRow(insurer, 7))).trim();
            if (StringUtils.isBlank(effective_etime)) {
                return returnMap(false, "第" + (i + 1) + "行,保险止期不能为空");
            }
            if (isDouble(effective_etime)) {
                billEntry.setEffectiveEtime(HSSFDateUtil.getJavaDate(Double.valueOf(effective_etime)));
            } else {
                if (effective_etime.indexOf("-") != -1) {
                    billEntry.setEffectiveEtime(sdf1.parse(effective_etime));
                } else if (effective_etime.indexOf("/") != -1) {
                    billEntry.setEffectiveEtime(sdf2.parse(effective_etime));
                } else {
                    return returnMap(false, "第" + (i + 1) + "行,保险止期格式有误");
                }
            }


            /**
             * 生成日期
             */

            String bill_time = getCellValue(row.getCell(getTypeRow(insurer, 8))).trim();
            if (StringUtils.isBlank(bill_time)) {
                return returnMap(false, "第" + (i + 1) + "行,生成日期不能为空");
            }
            if (isDouble(bill_time)) {
                billEntry.setBillTime(HSSFDateUtil.getJavaDate(Double.valueOf(bill_time)));
            } else {
                if (bill_time.indexOf("-") != -1) {
                    billEntry.setBillTime(sdf1.parse(bill_time));
                } else if (bill_time.indexOf("/") != -1) {
                    billEntry.setBillTime(sdf2.parse(bill_time));
                } else {
                    return returnMap(false, "第" + (i + 1) + "行,生成日期格式有误");
                }
            }


            /**
             * 保险金额
             */
            String insure_amount = getCellValue(row.getCell(getTypeRow(insurer, 10))).trim();
            if (StringUtils.isBlank(insure_amount)) {
                return returnMap(false, "第" + (i + 1) + "行,保险金额不能为空");
            }
            //退保订单中金额为负数，全处理为正数
            billEntry.setInsureAmount(new BigDecimal(insure_amount).abs());
            /**
             * 含税保费
             */
            String acq_amount = getCellValue(row.getCell(getTypeRow(insurer, 11))).trim();
            if (StringUtils.isBlank(acq_amount)) {
                return returnMap(false, "第" + (i + 1) + "行,含税保费不能为空");
            }
            billEntry.setAcqAmount(new BigDecimal(acq_amount).abs());


        } catch (Exception e) {
            log.info("对账数据组装异常", e);
            return returnMap(false, "文件数据解析数据异常");
        }


        log.info("对账数据组装完成-----");
        return returnMap(true, billEntry);
    }

    //这个方法是匹配不同的通道对应的字段在哪一列
    private int getTypeRow(String insurer, int mType) {
        //yilian前海财险默认mType就是row
        //其他mType没特别的也是row
        int typeRow = 0;
        if ("yilian".equals(insurer)) {
            typeRow = mType;
        } else if ("zhlh".equals(insurer)) {
            typeRow = mType;
            switch (mType) {
                case 1:
                    typeRow = 2;
                    break;
                case 2:
                    typeRow = 3;
                    break;
                case 5:
                    typeRow = 7;
                    break;
                case 6:
                    typeRow = 8;
                    break;
                case 7:
                    typeRow = 9;
                    break;
                case 8:
                    typeRow = 7;
                    break;
            }
        }
        return typeRow;
    }


    private boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    private Map<String, Object> checkSheet(String insurer,Sheet sheet,Map<String, Object> result){
        Row firstRow=sheet.getRow(0);
        result.put("result", true);
        ArrayList<String> lists=new ArrayList<String>();
        if("yilian".equals(insurer)){
            lists.add("ROW_NUM");
            lists.add("订单号");
            lists.add("保单号");
            lists.add("批单号");
            lists.add("被保险人");
            lists.add("投保时间");
            lists.add("保险起期");
            lists.add("保险止期");
            lists.add("保/批单生成日期");
            lists.add("业务类型");
            lists.add("保险金额");
            lists.add("含税保费");
        }else if("zhlh".equals(insurer)){
            lists.add("机构代码");
            lists.add("险种代码");
            lists.add("订单号");
            lists.add("保单号");
            lists.add("被保险人");
            lists.add("证件号码");
            lists.add("电话");
            lists.add("投保时间");
            lists.add("保险起期");
            lists.add("保险止期");
            lists.add("保险金额（元）");
            lists.add("含税保费（元）");
        }
        for(int i=0;i<lists.size();i++){
            String a=getCellValue(firstRow.getCell(i));
            String b=lists.get(i);
            if(a==null||!b.equals(a.trim())){
                result.put("result", false);
                result.put("msg", "对账模板格式有误");
            }
        }
        return result;
    }


    /**
     * 参数校验
     *
     * @param file
     * @param orderType
     * @param insurer
     * @param result
     * @return
     */
    private Map<String, Object> checkImportParam(MultipartFile file, String orderType, String insurer,
                                                 Map<String, Object> result) {
        if (StringUtil.isBlank(insurer)) {
            result.put("result", false);
            result.put("msg", "承保单位不能为空");
            return result;
        }
        if (StringUtil.isBlank(orderType)) {
            result.put("result", false);
            result.put("msg", "订单类型不能为空");
            return result;
        }
        if (file.isEmpty()) {
            result.put("result", false);
            result.put("msg", "导入文件不存在");
            return result;
        }
        int index = file.getOriginalFilename().lastIndexOf(".");
        if (index < 0) {
            result.put("result", false);
            result.put("msg", "导入文件文件格式有误");
            return result;
        }
        String format = file.getOriginalFilename().substring(index);
        if (!".xls".equals(format) && !".xlxs".equals(format) && !".xlsx".equals(format) && !".csv".equals(format)) {
            result.put("result", false);
            result.put("msg", "导入文件文件格式有误");
            return result;
        }

        String fileName = file.getOriginalFilename().substring(0, index);
        String[] dateArr = fileName.split("-");
        String path = "\\d{4}\\d{2}\\d{2}";//定义匹配规则
        if (dateArr.length != 3) {
            result.put("result", false);
            result.put("msg", "文件名格式不正确");
            return result;
        }
        Pattern p = Pattern.compile(path);
        Matcher startDate = p.matcher(dateArr[1]);
        Matcher endDate = p.matcher(dateArr[2]);
        if (!startDate.matches() || !endDate.matches()) {
            result.put("result", false);
            result.put("msg", "文件名日期格式不正确");
            return result;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat fileSdf = new SimpleDateFormat("yyyyMMdd");
        Map<String, String> fileMap = new HashMap<>();
        try {

            fileMap.put("startDate", sdf.format(fileSdf.parse(dateArr[1])) + " 00:00:00");
            fileMap.put("endDate", sdf.format(fileSdf.parse(dateArr[2])) + " 23:59:59");
            fileMap.put("fileDate", dateArr[1] + "-" + dateArr[2]);
            fileMap.put("fileName", fileName);
        } catch (Exception e) {
            log.error("日期解析异常", e);
            result.put("result", false);
            result.put("msg", "对账文件日期解析异常");
            return result;
        }
        //是否已经上传过对账文件
        int count = billEneryService.isUploadFile(orderType, insurer, StringUtil.filterNull(fileMap.get("startDate")), StringUtil.filterNull(fileMap.get("endDate")));
        if (count > 0) {
            result.put("result", false);
            result.put("msg", "当前时间段已存在对账数据，请勿重复上传对账文件");
            return result;
        }
        result.put("result", true);
        result.put("data", fileMap);
        return result;
    }


    /**
     * @param cell
     * @return
     */
    public String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
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
        return "";
    }

    /**
     * @param falg
     * @param data
     * @return
     */
    private Map<String, Object> returnMap(Boolean falg, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("flag", falg);
        map.put("data", data);
        return map;
    }
}
