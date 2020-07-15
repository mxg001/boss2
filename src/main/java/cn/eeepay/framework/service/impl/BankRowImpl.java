package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoSuperbank.OrderMainDao;
import cn.eeepay.framework.model.CreditcardApplyRecord;
import cn.eeepay.framework.model.OrderMain;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.TranslateRow;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.ExcelErrorMsgBean;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

@Service
public class BankRowImpl implements TranslateRow<CreditcardApplyRecord> {

    private static final Logger log = LoggerFactory.getLogger(BankRowImpl.class);

    private ThreadLocal<String> batchThreadLocal = new ThreadLocal<>();//批次号

    private ThreadLocal<String> bankThreadLocal = new ThreadLocal<>();//银行编码

    private ThreadLocal<String> bankIdThreadLocal = new ThreadLocal<>();//银行ID

    private ThreadLocal<String> bonusTypeThreadLocal = new ThreadLocal<>();//订单类型

    @Resource
    private OrderMainDao orderMainDao;

    @Override
    public CreditcardApplyRecord translate(Row row, int index, List<ExcelErrorMsgBean> errors) {
        CreditcardApplyRecord re = null;
        String ruleCode = bankThreadLocal.get();
        if(StringUtils.isBlank(ruleCode)){
            errors.add(new ExcelErrorMsgBean(index, 0, "银行不能为空"));
            re = null;
            return re;
        }
        switch (ruleCode){
            case "SHYH002":
                re = getShyh(row, index, errors);
                break;
            case "MSYH002":
                re = getMsyhOrder(row, index, errors);
                break;
            case "PAYH002":
                re = getPayhOrder(row, index, errors);
                break;
            case "XYYH002":
                re = getXyyhOrder(row, index, errors);
                break;
            case "ZXYH002":
                re = getZxyhOrder(row, index, errors);
                break;
            case "JTYH002":
                re = getJtyhOrder(row, index, errors);
                break;
            case "ZSYH002":
                re = getZsyhOrder(row, index, errors);
                break;
            case "JSYH002":
                re = getJsyhOrder(row, index, errors);
                break;
            case "GFYH002":
                re = getGfyhOrder(row, index, errors);
                break;
            case "WZYH002":
                re = getWzyhQkzOrder(row, index, errors);
                break;
            case "WZYH2002":
                re = getWzyhOrder(row, index, errors);
                break;
            case "HXYH002":
                re = getHxyhOrder(row, index, errors);
                break;
            case "PFYH002":
                re = getPfyhOrder(row, index, errors);
                break;
            case "ZGYH002":
                re = getZgyhOrder(row, index, errors);
                break;
            default:
                errors.add(new ExcelErrorMsgBean(index, 0, "银行不能为空"));
                re = null;
                break;
        }
        return re;

    }

    /**
     * 招商银行
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private CreditcardApplyRecord getZsyhOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int applyDateLine = 6;//网申日期
        int mobileLine = 5;//手机号
        int orderNameLine = 4;//姓名
        int userCodeLine = 3;//第三方ID

        String applyDateStr = String.valueOf(row.getCell(applyDateLine));
        String mobile = String.valueOf(row.getCell(mobileLine));
        String orderName = String.valueOf(row.getCell(orderNameLine));
        String userCode = String.valueOf(row.getCell(userCodeLine));

        Date applyDate = null;
        if(StringUtils.isNotBlank(applyDateStr) && !"null".equals(applyDateStr)){
            applyDate = CommonUtil.getFormatDate(applyDateStr);
        }else{
            errors.add(new ExcelErrorMsgBean(index, applyDateLine+1, "申请时间不能为空"));
            return null;
        }
        if(StringUtils.isBlank(mobile) || "null".equals(mobile)){
            errors.add(new ExcelErrorMsgBean(index, mobileLine+1, "手机号不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "申卡人姓名不能为空"));
            return null;
        }
        if(StringUtils.isBlank(userCode) || "null".equals(userCode)){
            errors.add(new ExcelErrorMsgBean(index, userCodeLine+1, "第三方ID不能为空"));
            return null;
        }
        if(userCode.endsWith(".0")){
            userCode = userCode.split("\\.")[0];
        }

        String bankId = bankIdThreadLocal.get();
        String batchNo = batchThreadLocal.get();

        //如果符合规则
        Date nowDate = new Date();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        CreditcardApplyRecord record = new CreditcardApplyRecord();
        record.setBankCode(bankId);
        record.setUserCode(userCode);
        record.setMobilephone(mobile);
        record.setUserName(orderName);
        record.setCreateTime(nowDate);
        record.setOperator(loginInfo.getId());
        record.setBatchNo(batchNo);
        record.setApplyDate(applyDate);
        return record;
    }

    /**
     * 建设银行
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private CreditcardApplyRecord getJsyhOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int applyDateLine = 2;//提交时间
        int mobileLine = 3;//手机号
        int orderNameLine = 4;//客户姓名

        String applyDateStr = String.valueOf(row.getCell(applyDateLine));
        String mobile = String.valueOf(row.getCell(mobileLine));
        String orderName = String.valueOf(row.getCell(orderNameLine));

        Date applyDate = null;
        if(StringUtils.isNotBlank(applyDateStr) && !"null".equals(applyDateStr)){
            applyDate = CommonUtil.getFormatDate(applyDateStr);
        }else{
            errors.add(new ExcelErrorMsgBean(index, applyDateLine+1, "提交时间不能为空"));
            return null;
        }
        if(StringUtils.isBlank(mobile) || "null".equals(mobile)){
            errors.add(new ExcelErrorMsgBean(index, mobileLine+1, "手机号不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "客户姓名不能为空"));
            return null;
        }

        String bankId = bankIdThreadLocal.get();
        String batchNo = batchThreadLocal.get();

        //如果符合规则
        Date nowDate = new Date();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        CreditcardApplyRecord record = new CreditcardApplyRecord();
        record.setBankCode(bankId);
        record.setMobilephone(mobile);
        record.setUserName(orderName);
        record.setCreateTime(nowDate);
        record.setOperator(loginInfo.getId());
        record.setBatchNo(batchNo);
        record.setApplyDate(applyDate);
        return record;
    }

    /**
     * 温州银行
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private CreditcardApplyRecord getWzyhOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int applyDateLine = 0;//提交时间
        int mobileLine = 2;//手机号
        int orderNameLine = 1;//客户姓名

        String applyDateStr = String.valueOf(row.getCell(applyDateLine));
        String mobile = String.valueOf(row.getCell(mobileLine));
        String orderName = String.valueOf(row.getCell(orderNameLine));

        Date applyDate = null;
        if(StringUtils.isNotBlank(applyDateStr) && !"null".equals(applyDateStr)){
            applyDate = CommonUtil.getFormatDate(applyDateStr);
        }else{
            errors.add(new ExcelErrorMsgBean(index, applyDateLine+1, "audit_time不能为空"));
            return null;
        }
        if(StringUtils.isBlank(mobile) || "null".equals(mobile)){
            errors.add(new ExcelErrorMsgBean(index, mobileLine+1, "customer_mobile不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "customer_name不能为空"));
            return null;
        }

        String bankId = bankIdThreadLocal.get();
        String batchNo = batchThreadLocal.get();

        //如果符合规则
        Date nowDate = new Date();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        CreditcardApplyRecord record = new CreditcardApplyRecord();
        record.setBankCode(bankId);
        record.setMobilephone(mobile);
        record.setUserName(orderName);
        record.setCreateTime(nowDate);
        record.setOperator(loginInfo.getId());
        record.setBatchNo(batchNo);
        record.setApplyDate(applyDate);
        return record;
    }

    /**
     * 温州银行全卡种
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private CreditcardApplyRecord getWzyhQkzOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int applyDateLine = 0;//提交时间
        int mobileLine = 2;//手机号
        int orderNameLine = 1;//客户姓名

        String applyDateStr = String.valueOf(row.getCell(applyDateLine));
        String mobile = String.valueOf(row.getCell(mobileLine));
        String orderName = String.valueOf(row.getCell(orderNameLine));

        Date applyDate = null;
        if(StringUtils.isNotBlank(applyDateStr) && !"null".equals(applyDateStr)){
            applyDate = CommonUtil.getFormatDate(applyDateStr);
        }else{
            errors.add(new ExcelErrorMsgBean(index, applyDateLine+1, "核卡日期不能为空"));
            return null;
        }
        if(StringUtils.isBlank(mobile) || "null".equals(mobile)){
            errors.add(new ExcelErrorMsgBean(index, mobileLine+1, "手机号不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "姓名不能为空"));
            return null;
        }

        String bankId = bankIdThreadLocal.get();
        String batchNo = batchThreadLocal.get();

        //如果符合规则
        Date nowDate = new Date();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        CreditcardApplyRecord record = new CreditcardApplyRecord();
        record.setBankCode(bankId);
        record.setMobilephone(mobile);
        record.setUserName(orderName);
        record.setCreateTime(nowDate);
        record.setOperator(loginInfo.getId());
        record.setBatchNo(batchNo);
        record.setApplyDate(applyDate);
        return record;
    }


    /**
     * 华夏银行
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private CreditcardApplyRecord getHxyhOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int applyDateLine = 5;//提交时间
        int mobileLine = 1;//手机号
        int orderNameLine = 0;//客户姓名

        String applyDateStr = String.valueOf(row.getCell(applyDateLine));
        String mobile = String.valueOf(row.getCell(mobileLine));
        String orderName = String.valueOf(row.getCell(orderNameLine));

        Date applyDate = null;
        if(StringUtils.isNotBlank(applyDateStr) && !"null".equals(applyDateStr)){
            applyDate = CommonUtil.getFormatDate(applyDateStr);
        }else{
            errors.add(new ExcelErrorMsgBean(index, applyDateLine+1, "OPEN_DT不能为空"));
            return null;
        }
        if(StringUtils.isBlank(mobile) || "null".equals(mobile)){
            errors.add(new ExcelErrorMsgBean(index, mobileLine+1, "mobile不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "cust_nm不能为空"));
            return null;
        }

        String bankId = bankIdThreadLocal.get();
        String batchNo = batchThreadLocal.get();

        //如果符合规则
        Date nowDate = new Date();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        CreditcardApplyRecord record = new CreditcardApplyRecord();
        record.setBankCode(bankId);
        record.setMobilephone(mobile);
        record.setUserName(orderName);
        record.setCreateTime(nowDate);
        record.setOperator(loginInfo.getId());
        record.setBatchNo(batchNo);
        record.setApplyDate(applyDate);
        return record;
    }

    /**
     * 浦发银行
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private CreditcardApplyRecord getPfyhOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int applyDateLine = 2;//审批时间
        int mobileLine = 6;//手机号
        int orderNameLine = 5;//客户姓名

        String applyDateStr = String.valueOf(row.getCell(applyDateLine));
        String mobile = String.valueOf(row.getCell(mobileLine));
        String orderName = String.valueOf(row.getCell(orderNameLine));

        Date applyDate = null;
        if(StringUtils.isNotBlank(applyDateStr) && !"null".equals(applyDateStr)){
            if(applyDateStr.indexOf("E")!=-1 || applyDateStr.indexOf("e")!=-1 || applyDateStr.indexOf("+")!=-1){
                BigDecimal bd = new BigDecimal(applyDateStr);
                applyDateStr=bd.toPlainString();
            }
            applyDate = CommonUtil.getFormatDate(applyDateStr);
        }else{
            errors.add(new ExcelErrorMsgBean(index, applyDateLine+1, "审批日期不能为空"));
            return null;
        }
        if(StringUtils.isBlank(mobile) || "null".equals(mobile)){
            errors.add(new ExcelErrorMsgBean(index, mobileLine+1, "客户手机不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "客户姓名不能为空"));
            return null;
        }

        String bankId = bankIdThreadLocal.get();
        String batchNo = batchThreadLocal.get();

        //如果符合规则
        Date nowDate = new Date();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        CreditcardApplyRecord record = new CreditcardApplyRecord();
        record.setBankCode(bankId);
        record.setMobilephone(mobile);
        record.setUserName(orderName);
        record.setCreateTime(nowDate);
        record.setOperator(loginInfo.getId());
        record.setBatchNo(batchNo);
        record.setApplyDate(applyDate);
        log.info("插入信用卡申请记录表 数据 "+ JSON.toJSONString(record));
        return record;
    }

    /**
     * 中国银行
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private CreditcardApplyRecord getZgyhOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int applyDateLine = 1;//申请日期
        int mobileLine = 3;//手机号
        int orderNameLine = 4;//客户姓名
        int newAccountStatus = 2;//新老客户标识
        int checkStatus = 5;//申请状态
        String applyDateStr = String.valueOf(row.getCell(applyDateLine));
        String mobile = String.valueOf(row.getCell(mobileLine));
        String orderName = String.valueOf(row.getCell(orderNameLine));
        String newAccountStatusStr =  String.valueOf(row.getCell(newAccountStatus));;//新老客户标识
        String checkStatusStr =  String.valueOf(row.getCell(checkStatus));;//申请状态


        Date applyDate = null;
        if(StringUtils.isNotBlank(applyDateStr) && !"null".equals(applyDateStr)){
            if(applyDateStr.indexOf("E")!=-1 || applyDateStr.indexOf("e")!=-1 || applyDateStr.indexOf("+")!=-1){
                BigDecimal bd = new BigDecimal(applyDateStr);
                applyDateStr=bd.toPlainString();
            }
            applyDate = CommonUtil.getFormatDate(applyDateStr);
        }else{
            errors.add(new ExcelErrorMsgBean(index, applyDateLine+1, "审批日期不能为空"));
            return null;
        }
        if(StringUtils.isBlank(mobile) || "null".equals(mobile)){
            errors.add(new ExcelErrorMsgBean(index, mobileLine+1, "客户手机不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "客户姓名不能为空"));
            return null;
        }

        String bankId = bankIdThreadLocal.get();
        String batchNo = batchThreadLocal.get();

        //如果符合规则
        Date nowDate = new Date();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        CreditcardApplyRecord record = new CreditcardApplyRecord();
        if(newAccountStatusStr.equals("新客户")){
            record.setNewAccountStatus("1");
        }
        if(checkStatusStr.equals("审批通过")){
            record.setCheckStatus("1");
        }
        record.setBankCode(bankId);
        record.setMobilephone(mobile);
        record.setUserName(orderName);
        record.setCreateTime(nowDate);
        record.setOperator(loginInfo.getId());
        record.setBatchNo(batchNo);
        record.setApplyDate(applyDate);
        log.info("插入信用卡申请记录表 数据 "+ JSON.toJSONString(record));
        return record;
    }


    /**
     * 广发银行
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private CreditcardApplyRecord getGfyhOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int applyDateLine = 1;//开卡时间
        int orderNameLine = 4;//客户姓名
        int idNoLine = 6;//身份证

        String applyDateStr = String.valueOf(row.getCell(applyDateLine));
        String idNo = String.valueOf(row.getCell(idNoLine));
        String orderName = String.valueOf(row.getCell(orderNameLine));

        Date applyDate = null;
        if(StringUtils.isNotBlank(applyDateStr) && !"null".equals(applyDateStr)){
            applyDate = CommonUtil.getFormatDate(applyDateStr);
            if(applyDate==null){
                errors.add(new ExcelErrorMsgBean(index, applyDateLine+1, "开卡时间格式有误"));
                return null;
            }
        }else{
            errors.add(new ExcelErrorMsgBean(index, applyDateLine+1, "开卡时间不能为空"));
            return null;
        }
        if(StringUtils.isBlank(idNo) || "null".equals(idNo)){
            errors.add(new ExcelErrorMsgBean(index, idNoLine+1, "证件号码不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "客户姓名不能为空"));
            return null;
        }

        String bankId = bankIdThreadLocal.get();
        String batchNo = batchThreadLocal.get();

        //如果符合规则
        Date nowDate = new Date();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        CreditcardApplyRecord record = new CreditcardApplyRecord();
        record.setBankCode(bankId);
        idNo= idNo.substring(0,4);
        record.setIdCardNo(idNo);
        record.setUserName(orderName);
        record.setCreateTime(nowDate);
        record.setOperator(loginInfo.getId());
        record.setBatchNo(batchNo);
        record.setApplyDate(applyDate);
        return record;
    }

    /**
     * 交通银行
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private CreditcardApplyRecord getJtyhOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int orderNameLine = 3;//姓名
        int mobileLine = 2;//手机号
        int applyDateLine = 1;//网申日期
//        int issueDateLine = 2;//核卡日期
        DecimalFormat df = new DecimalFormat("0");
        String orderName = String.valueOf(row.getCell(orderNameLine));
        String mobile = String.valueOf(row.getCell(mobileLine));
        String applyDateString = String.valueOf(row.getCell(applyDateLine));
        Date applyDate=null;
        if( StringUtils.isBlank(applyDateString) ){
            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "进件日期不能为空"));
            return null;
        } else {
            applyDate = CommonUtil.getFormatDate(applyDateString);
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "姓名不能为空"));
            return null;
        }
        if(StringUtils.isBlank(mobile) || "null".equals(mobile)){
            errors.add(new ExcelErrorMsgBean(index, mobileLine+1, "手机号不能为空"));
            return null;
        }

        String bankId = bankIdThreadLocal.get();
        String batchNo = batchThreadLocal.get();

        //如果符合规则
        Date nowDate = new Date();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        CreditcardApplyRecord record = new CreditcardApplyRecord();
        record.setBankCode(bankId);
        record.setUserName(orderName);
        record.setMobilephone(mobile);
        record.setCheckStatus("1");//审核通过
        record.setCreateTime(nowDate);
        record.setOperator(loginInfo.getId());
        record.setBatchNo(batchNo);
//        record.setIssueDate(issueDate);
        record.setApplyDate(applyDate);
        return record;
    }



    /**
     * 兴业银行
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private CreditcardApplyRecord getXyyhOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int orderNameLine = 2;//姓名
        int idNoLine = 7;//身份证
        int applyDateLine = 0;//网申日期
        int mobileLine = 3;//手机号

        String orderName = String.valueOf(row.getCell(orderNameLine));
        String mobile = String.valueOf(row.getCell(mobileLine));
        String applyDateString = String.valueOf(row.getCell(applyDateLine));
        String idNo = String.valueOf(row.getCell(idNoLine));
        Date  applyDate = null;
        if(StringUtils.isBlank(orderName) || "null".equals(orderName) || "0".equals(applyDateString)){
            errors.add(new ExcelErrorMsgBean(index, applyDateLine+1, "申请日期不能为空"));
            return null;
        }else{
            applyDateString = new BigDecimal(applyDateString).toPlainString();
            applyDate = CommonUtil.getFormatDate(applyDateString);
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "姓名不能为空"));
            return null;
        }
        if(StringUtils.isBlank(idNo) || "null".equals(idNo)){
            errors.add(new ExcelErrorMsgBean(index, idNoLine+1, "出生日期不能为空"));
            return null;
        } else {
            idNo = new BigDecimal(idNo).toPlainString();
        }
        if(StringUtils.isBlank(mobile) || "null".equals(mobile)){
            errors.add(new ExcelErrorMsgBean(index, mobileLine+1, "手机号不能为空"));
            return null;
        }
        String bankId = bankIdThreadLocal.get();
        String batchNo = batchThreadLocal.get();

        //如果符合规则
        Date nowDate = new Date();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        CreditcardApplyRecord record = new CreditcardApplyRecord();
        record.setBankCode(bankId);
        record.setUserName(orderName);
        record.setIdCardNo(idNo);
        record.setCheckStatus("1");//审核通过
        record.setCreateTime(nowDate);
        record.setOperator(loginInfo.getId());
        record.setBatchNo(batchNo);
        record.setApplyDate(applyDate);
        record.setMobilephone(mobile);
        return record;
    }

    /**
     * 平安银行
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private CreditcardApplyRecord getPayhOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        String bonusType = bonusTypeThreadLocal.get();
        //如果是发卡类型
        if("1".equals(bonusType)){
            return getPaCardRecord(row, index, errors);
        } else if("2".equals(bonusType)){//如果是首刷类型
            return getPaBrushRecord(row, index, errors);
        } else {
            errors.add(new ExcelErrorMsgBean(index, 1, "类型不能为空"));
            return null;
        }

    }

    /**
     * 获取平安首刷记录
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private CreditcardApplyRecord getPaBrushRecord(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int orderNameLine = 3;//客户姓名
        int idNoLine = 4;//身份证
        int firstBrushDateLine = 6;//首刷日期
        int newStatusLine = 9;//是否新户
        int checkStatusLine = 10;//申请状态

        if(StringUtils.isBlank( String.valueOf(row.getCell(firstBrushDateLine))) && !"null".equals(String.valueOf(row.getCell(firstBrushDateLine)))){
            errors.add(new ExcelErrorMsgBean(index, firstBrushDateLine+1, "首刷日期不能为空"));
            return null;
        }
        Date firstBrushDate = row.getCell(firstBrushDateLine).getDateCellValue();
        String orderName = String.valueOf(row.getCell(orderNameLine));
        String idNo = String.valueOf(row.getCell(idNoLine));
        String newStatus = String.valueOf(row.getCell(newStatusLine));
        String checkStatus = String.valueOf(row.getCell(checkStatusLine));

        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "姓名不能为空"));
            return null;
        }
        if(StringUtils.isBlank(idNo) || "null".equals(idNo)){
            errors.add(new ExcelErrorMsgBean(index, idNoLine+1, "证件号码不能为空"));
            return null;
        }
        if(idNo.endsWith(".0")){
            idNo = idNo.split("\\.")[0];
        }
        if(idNo.length() == 3){
            idNo = "0" + idNo;
        }
        if(idNo.length() == 2){
            idNo = "00" + idNo;
        }
        if(idNo.length() == 1){
            idNo = "000" + idNo;
        }
        if(StringUtils.isBlank(newStatus) || "null".equals(newStatus)){
            errors.add(new ExcelErrorMsgBean(index, newStatusLine+1, "是否销售新户不能为空"));
            return null;
        }
        if(StringUtils.isBlank(checkStatus) || "null".equals(checkStatus)){
            errors.add(new ExcelErrorMsgBean(index, checkStatusLine+1, "申请状态不能为空"));
            return null;
        }

        String bankId = bankIdThreadLocal.get();
        String batchNo = batchThreadLocal.get();
        //如果符合规则
        Date nowDate = new Date();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        CreditcardApplyRecord record = new CreditcardApplyRecord();
        record.setBankCode(bankId);
        record.setFirstBrushDate(firstBrushDate);
        record.setUserName(orderName);
        record.setIdCardNo(idNo);
        record.setNewAccountStatus(newStatus);
        record.setCheckStatus(checkStatus);
        record.setCreateTime(nowDate);
        record.setOperator(loginInfo.getId());
        record.setBatchNo(batchNo);
        return record;
    }

    /**
     * 获取平安发卡记录
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private CreditcardApplyRecord getPaCardRecord(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int applyDateLine = 3;//网申日期
        int issueDateLine = 4;//发卡日期
        int orderNameLine = 5;//客户姓名
        int idNoLine = 6;//身份证
        int newStatusLine = 9;//是否新户
        int checkStatusLine = 10;//申请状态
        if(StringUtils.isBlank( String.valueOf(row.getCell(applyDateLine))) && !"null".equals(String.valueOf(row.getCell(applyDateLine)))){
            errors.add(new ExcelErrorMsgBean(index, applyDateLine+1, "进件日期不能为空"));
            return null;
        }
        if(StringUtils.isBlank( String.valueOf(row.getCell(issueDateLine))) && !"null".equals(String.valueOf(row.getCell(issueDateLine)))){
            errors.add(new ExcelErrorMsgBean(index, issueDateLine+1, "发卡日期不能为空"));
            return null;
        }
        Date applyDate = row.getCell(applyDateLine).getDateCellValue();
        Date issueDate = row.getCell(issueDateLine).getDateCellValue();
        String orderName = String.valueOf(row.getCell(orderNameLine));
        String idNo = String.valueOf(row.getCell(idNoLine));
        String newStatus = String.valueOf(row.getCell(newStatusLine));
        String checkStatus = String.valueOf(row.getCell(checkStatusLine));

        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "姓名不能为空"));
            return null;
        }
        if(StringUtils.isBlank(idNo) || "null".equals(idNo)){
            errors.add(new ExcelErrorMsgBean(index, idNoLine+1, "证件号码不能为空"));
            return null;
        }
        if(idNo.endsWith(".0")){
            idNo = idNo.split("\\.")[0];
        }
        if(idNo.length() == 3){
            idNo = "0" + idNo;
        }
        if(idNo.length() == 2){
            idNo = "00" + idNo;
        }
        if(idNo.length() == 1){
            idNo = "000" + idNo;
        }
        if(StringUtils.isBlank(newStatus) || "null".equals(newStatus)){
            errors.add(new ExcelErrorMsgBean(index, newStatusLine+1, "是否新户不能为空"));
            return null;
        }
        if(StringUtils.isBlank(checkStatus) || "null".equals(checkStatus)){
            errors.add(new ExcelErrorMsgBean(index, checkStatusLine+1, "申请状态不能为空"));
            return null;
        }
        String bankId = bankIdThreadLocal.get();
        String batchNo = batchThreadLocal.get();

        //如果符合规则
        Date nowDate = new Date();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        CreditcardApplyRecord record = new CreditcardApplyRecord();
        record.setBankCode(bankId);
        record.setApplyDate(applyDate);
        record.setIssueDate(issueDate);
        record.setUserName(orderName);
        record.setIdCardNo(idNo);
        record.setCheckStatus(checkStatus);
        record.setNewAccountStatus(newStatus);
        record.setCreateTime(nowDate);
        record.setOperator(loginInfo.getId());
        record.setBatchNo(batchNo);
        return record;
    }

    /**
     * 中信银行
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private CreditcardApplyRecord getZxyhOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int orderNameLine = 9;//姓名
//        int idNoLine = 8;//身份证
        int mobilephoneLine = 10;//手机号
        int applyDateLine = 8;//录入日
        int issueDateLine = 1;//批核日期

        String orderName = String.valueOf(row.getCell(orderNameLine));
//        String idNo = String.valueOf(row.getCell(idNoLine));
        String mobilephone = String.valueOf(row.getCell(mobilephoneLine));
        String applyDateStr = String.valueOf(row.getCell(applyDateLine));
        String issueDateStr = String.valueOf(row.getCell(issueDateLine));
        Date applyDate = null;
        Date issueDate = null;
        if(StringUtils.isNotBlank(applyDateStr) && !"null".equals(applyDateStr)){
            applyDate = CommonUtil.getFormatDate(applyDateStr);
        }else{
            errors.add(new ExcelErrorMsgBean(index, applyDateLine+1, "录入日不能为空"));
            return null;
        }
        if(StringUtils.isNotBlank(issueDateStr) && !"null".equals(issueDateStr)){
            issueDate = CommonUtil.getFormatDate(issueDateStr);
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "姓名不能为空"));
            return null;
        }
//        if(StringUtils.isBlank(idNo) || "null".equals(idNo)){
//            errors.add(new ExcelErrorMsgBean(index, idNoLine+1, "证件号码不能为空"));
//            return null;
//        }
        if(StringUtils.isBlank(mobilephone) || "null".equals(mobilephone)){
            errors.add(new ExcelErrorMsgBean(index, mobilephoneLine+1, "手机不能为空"));
            return null;
        }

        //手机号前三位
        String orderPhoneStart = mobilephone.substring(0,3) + "%";
        //手机号末四位
        String orderPhoneEnd = mobilephone.substring(mobilephone.length() - 4,mobilephone.length());

        String bankId = bankIdThreadLocal.get();
//        String ruleCode = bankThreadLocal.get();
        String batchNo = batchThreadLocal.get();

//        OrderMain order = new OrderMain();
//        order.setOrderType("2");//办卡订单
//        order.setBankSourceId(Long.valueOf(bankId));
//        order.setOrderName(orderName);
//        order.setOrderPhoneStart(orderPhoneStart);
//        order.setOrderPhoneEnd(orderPhoneEnd);
//        order.setOrderIdNo(idNo.substring(0,14) + "%");//身份证前14位
//        order.setRuleCode(ruleCode);
//        //检查订单是否符合规则
//        OrderMain item = checkExists(index, errors, orderNameLine, order);
//        if(item == null){
//            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "找不到对应的订单"));
//            return null;
//        }
        //如果符合规则
        Date nowDate = new Date();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        CreditcardApplyRecord record = new CreditcardApplyRecord();
        record.setBankCode(bankId);
        record.setUserName(orderName);
        record.setMobilephone(mobilephone);
//        record.setIdCardNo(idNo);
        record.setCheckStatus("1");//审核通过
        record.setCreateTime(nowDate);
        record.setOperator(loginInfo.getId());
        record.setBatchNo(batchNo);
        record.setApplyDate(applyDate);
        record.setIssueDate(issueDate);
        return record;
    }

    /**
     * 检查订单是否已存在
     * @param index
     * @param errors
     * @param orderNameLine
     * @param order
     * @return
     */
    private OrderMain checkExists(int index, List<ExcelErrorMsgBean> errors, int orderNameLine, OrderMain order) {
        OrderMain item = null;
        String status = "1,3,6";
        order.setStatus(status);//订单状态：1已创建、3待审核、6审核不通过的订单
        //检查订单是否存在
        if("SHYH002".equals(order.getRuleCode())){
            item = orderMainDao.selectShBankOrderExistsSuccess(order);
        } else if("MSYH002".equals(order.getRuleCode())){
            item = orderMainDao.selectMsBankOrderExistsSuccess(order);
        } else if("ZXYH002".equals(order.getRuleCode())){
            item = orderMainDao.selectZxBankOrderExistsSuccess(order);
        }
        if(item == null){
            return null;
        }
        return item;
    }

    /**
     * 民生银行
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private CreditcardApplyRecord getMsyhOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int orderNameLine = 2;//姓名
        int orderPhoneLine = 3;//手机号
        int resultLine = 4;//有效核准标识
        int applyDateLine = 0;//申请日期
        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String orderPhone = String.valueOf(row.getCell(orderPhoneLine));
        String orderName = String.valueOf(row.getCell(orderNameLine));
        String resultStatus = String.valueOf(row.getCell(resultLine));
        String applyDateStr = String.valueOf(row.getCell(applyDateLine));
        Date applyDate = null;
        if(StringUtils.isNotBlank(applyDateStr) && !"null".equals(applyDateStr)){
            applyDate = CommonUtil.getFormatDate(applyDateStr);
        }else{
            errors.add(new ExcelErrorMsgBean(index, applyDateLine+1, "贴条形码日期不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone)){
            errors.add(new ExcelErrorMsgBean(index, orderPhoneLine+1, "手机号码不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "姓名不能为空"));
            return null;
        }
        if(StringUtils.isBlank(resultStatus) || "null".equals(resultStatus)){
            errors.add(new ExcelErrorMsgBean(index, resultLine+1, "有效结算核准标识不能为空"));
            return null;
        }
        //手机号前三位
//        String orderPhoneStart = orderPhone.substring(0,3) + "%";
//        //手机号末四位
//        String orderPhoneEnd = orderPhone.substring(orderPhone.length() - 4,orderPhone.length());

        String bankId = bankIdThreadLocal.get();
//        String ruleCode = bankThreadLocal.get();
        String batchNo = batchThreadLocal.get();
//        OrderMain order = new OrderMain();
//        order.setOrderType("2");//办卡订单
//        order.setBankSourceId(Long.valueOf(bankId));
//        order.setOrderName(orderName.substring(0,1) + "%");
//        order.setOrderPhoneStart(orderPhoneStart);
//        order.setOrderPhoneEnd(orderPhoneEnd);
//        order.setRuleCode(ruleCode);
//        //检查订单是否符合规则
//        OrderMain item = checkExists(index, errors, orderNameLine, order);
//        if(item == null){
//            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "找不到对应的订单"));
//            return null;
//        }
        //如果符合规则
        Date nowDate = new Date();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        CreditcardApplyRecord record = new CreditcardApplyRecord();
        record.setBankCode(bankId);
        record.setUserName(orderName);
        record.setMobilephone(orderPhone);
        if(resultStatus.endsWith(".0")){
            resultStatus = resultStatus.split("\\.")[0];
        }
        record.setCheckStatus(resultStatus);
        record.setCreateTime(nowDate);
        record.setOperator(loginInfo.getId());
        record.setBatchNo(batchNo);
        record.setApplyDate(applyDate);
        return record;
    }

    /**
     * 上海银行
     * @param row
     * @param index
     * @param errors
     * @return
     * 需求：循环读取处理银行返回的每一条数据，根据客户姓名（订单姓名），证件号（订单证件号）末4位，查找出系统中对应的办理信用卡订单，进行如下处理：
     * 如果有符合规则的订单，则保存到list，最后return
     * 如果有不符合规则的订单，放在errors里面
     */
    private CreditcardApplyRecord getShyh(Row row, int index, List<ExcelErrorMsgBean> errors) {
        //上海银行 4客户姓名，5证件末四位，8是否本行新户（Y表示是）,10审批结果（01表示成功，04表示拒绝）
        //订单状态：5订单成功 6订单失败，7已办理过
        int applyDateLine = 0;
        int orderNameLine = 2;
        int idNoLine = 3;
        int cardNoLine = 4;
        int orderPhoneLine = 5;
        Date applyDate = null;
        String applyDateStr = String.valueOf(row.getCell(applyDateLine));
        String orderName = String.valueOf(row.getCell(orderNameLine));
        String idNo = String.valueOf(row.getCell(idNoLine));
        String cardNo = String.valueOf(row.getCell(cardNoLine));
        String orderPhone = String.valueOf(row.getCell(orderPhoneLine));
        if(StringUtils.isNotBlank(applyDateStr) && !"null".equals(applyDateStr)){
            applyDate = CommonUtil.getFormatDate(applyDateStr);
        }else{
            errors.add(new ExcelErrorMsgBean(index, applyDateLine+1, "网申日期不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "客户姓名不能为空"));
            return null;
        }
        if(StringUtils.isBlank(idNo) || "null".equals(idNo)){
            errors.add(new ExcelErrorMsgBean(index, idNoLine+1, "证件末四位不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone)){
            errors.add(new ExcelErrorMsgBean(index, orderPhoneLine+1, "手机号不能为空"));
            return null;
        }
        String bankId = bankIdThreadLocal.get();
        String batchNo = batchThreadLocal.get();
        if(idNo.endsWith(".0")){
            idNo = idNo.split("\\.")[0];
        }
//        OrderMain order = new OrderMain();
//        order.setOrderType("2");//办卡订单
//        order.setBankSourceId(Long.valueOf(bankId));
//        order.setOrderName(orderName);
//        if(idNo.endsWith(".0")){
//            idNo = idNo.split("\\.")[0];
//        }
//        order.setOrderIdNo(idNo);
//        order.setRuleCode(ruleCode);
//        //检查订单是否符合规则
//        OrderMain item = checkExists(index, errors, orderNameLine, order);
//        if(item == null){
//            errors.add(new ExcelErrorMsgBean(index, orderNameLine+1, "找不到对应的订单"));
//            return null;
//        }
        //如果符合规则
        Date nowDate = new Date();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        CreditcardApplyRecord record = new CreditcardApplyRecord();
        record.setBankCode(bankId);
        record.setUserName(orderName);
        record.setIdCardNo(idNo);
        record.setCardNo(cardNo);
        record.setMobilephone(orderPhone);
        record.setCreateTime(nowDate);
        record.setOperator(loginInfo.getId());
        record.setBatchNo(batchNo);
        record.setApplyDate(applyDate);
        return record;
    }



    public void setBatchNo(String batchNo) {
        batchThreadLocal.set(batchNo);
    }

    public void setRuleCode(String ruleCode){
        bankThreadLocal.set(ruleCode);
    }

    public void setBankId(String bankId){
        bankIdThreadLocal.set(bankId);
    }

    public void setBonusType(String bonusType){
        bonusTypeThreadLocal.set(bonusType);
    }
}
