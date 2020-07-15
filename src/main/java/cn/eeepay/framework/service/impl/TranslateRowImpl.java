package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoSuperbank.LoanSourceDao;
import cn.eeepay.framework.daoSuperbank.OrderMainDao;
import cn.eeepay.framework.model.LoanSource;
import cn.eeepay.framework.model.OrderMain;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.TranslateRow;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.ExcelErrorMsgBean;
import cn.eeepay.framework.util.LoanSourceCode;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TranslateRowImpl implements TranslateRow<OrderMain> {

    private static final Logger log = LoggerFactory.getLogger(TranslateRowImpl.class);

    private ThreadLocal<String> batchThreadLocal = new ThreadLocal<>();//批次号

    private ThreadLocal<String> loanThreadLocal = new ThreadLocal<>();//放款机构

    private ThreadLocal<String> ruleCodeThread = new ThreadLocal<>();//放款机构导入编码

    @Resource
    private OrderMainDao orderMainDao;
    @Resource
    private LoanSourceDao loanSourceDao;

    @Override
    public OrderMain translate(Row row, int index, List<ExcelErrorMsgBean> errors) {
        OrderMain order;
        String ruleCode = ruleCodeThread.get();
        if(ruleCode.equals(LoanSourceCode.YRD)){
            order = getYrdOrder(row, index, errors);
        } else if(ruleCode.equals(LoanSourceCode.KKD)){
            order = getKadOrder(row, index, errors);
        } else if(ruleCode.equals(LoanSourceCode.XSPH)){
            order = getXsphOrder(row, index, errors);
        } else if(ruleCode.equals(LoanSourceCode.JKD)){
            order = getJkdOrder(row, index, errors);
        } else if(ruleCode.equals(LoanSourceCode.CRK)){
            order = getCrkOrder(row, index, errors);
        } else if(ruleCode.equals(LoanSourceCode.WDPH)){
            order = getWdphOrder(row, index, errors);
        } else if(ruleCode.equals(LoanSourceCode.HKD)){
            order = getHkdOrder(row, index, errors);
        } else if(ruleCode.equals(LoanSourceCode.QLG)){
            order = getQlgOrder(row, index, errors);
        } else if(ruleCode.equals(LoanSourceCode.RPD)){
            order = getRpdOrder(row, index, errors);
        } else if(ruleCode.equals(LoanSourceCode.GFD)){
            order = getGfdOrder(row, index, errors);
        }else if(ruleCode.equals(LoanSourceCode.GT360)){
            order = getGt360Order(row, index, errors);
        }else if(ruleCode.equals(LoanSourceCode.XEF)){
            order = getXefOrder(row, index, errors);
        }else if(ruleCode.equals(LoanSourceCode.WD)){
            order = getWdOrder(row, index, errors);
        }else if(ruleCode.equals(LoanSourceCode.ZAXY)){
            order = getZaxyOrder(row, index, errors);
        } else if(ruleCode.equals(LoanSourceCode.KYH)){
            order = getKyhOrder(row, index, errors);
        }else if(ruleCode.equals(LoanSourceCode.DDQ)){
            order = getDdqOrder(row, index, errors);
        }else if(ruleCode.equals(LoanSourceCode.ZA)){
            order = getZaOrder(row, index, errors);
        }else if(ruleCode.equals(LoanSourceCode.SNJR)){
            order = getSnjrOrder(row, index, errors);
        }else if(ruleCode.equals(LoanSourceCode.QMG)){
            order = getQmgOrder(row, index, errors);
        }else if(ruleCode.equals(LoanSourceCode.NWD)){
            order = getNwdOrder(row, index, errors);
        }else if(ruleCode.equals(LoanSourceCode.QHFQ)){
            order = getQhfqOrder(row, index, errors);
        }else if(ruleCode.equals(LoanSourceCode.TQY)){
            order = getTqyOrder(row, index, errors);
        }else if(ruleCode.equals(LoanSourceCode.JFXLK)){
            order = getJfxlkOrder(row, index, errors);
        }else if(ruleCode.equals(LoanSourceCode.LFQ)){
            order = getLfqOrder(row, index, errors);
        }else if(ruleCode.equals(LoanSourceCode.PAPH)){
            order = getPaphOrder(row, index, errors);
        }else if(ruleCode.equals(LoanSourceCode.DW)){
            order = getDwOrder(row, index, errors);
        }else if(ruleCode.equals(LoanSourceCode.MB)){
            order = getMbOrder(row, index, errors);
        }else if(ruleCode.equals(LoanSourceCode.JDBT)){
            order = getJdbtOrder(row, index, errors);
        }
        else {
            errors.add(new ExcelErrorMsgBean(index, 0, "贷款机构不能为空"));
            order = null;
        }
//        switch (ruleCode){
//            case "YRD002"://宜人贷
//                order = getYrdOrder(row, index, errors);
//                break;
//            case "KKD002"://卡卡贷
//                order = getKadOrder(row, index, errors);
//                break;
//            case "XSPH002"://小树普惠
//                order = getXsphOrder(row, index, errors);
//                break;
//            case "JKD002"://嘉卡贷
//                order = getJkdOrder(row, index, errors);
//                break;
//            case "CRK002"://超人卡
//                order = getCrkOrder(row, index, errors);
//                break;
//            case "WDPH002"://万达普惠
//                order = getWdphOrder(row, index, errors);
//                break;
//            case "HKD002"://恒快贷
//                order = getHkdOrder(row, index, errors);
//                break;
//            default:
//                errors.add(new ExcelErrorMsgBean(index, 0, "贷款机构不能为空"));
//                order = null;
//                break;
//        }

        return order;

    }

    /**
     * 功夫贷
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getGfdOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int phoneLine = 4;//手机号
        int userNameLine = 3;//姓名
        int loanAmountLine = 2;//借款金额
        int createDateLine = 0;//放款时间
        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));

        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额必须为数字"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "手机号码不能为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "姓名不能为空"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额必须大于0"));
            return null;
        }
//        if(StringUtils.isBlank(createDateStr)){
//            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "放款时间不能为空"));
//            return null;
//        }
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName.substring(0, 1) + "%");
        order.setOrderPhoneStart(orderPhone.substring(0,3) + "%");
        order.setOrderPhoneEnd(orderPhone.substring(orderPhone.length() - 4,orderPhone.length()));
//        order.setCreateDate(CommonUtil.getFormatDate(createDateStr));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setLoanAmount(loanAmount);
        order.setOrderNo(item.getOrderNo());
        order.setBatchNo(batchThreadLocal.get());
        return order;
    }

    /**
     * 360借条
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getGt360Order(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int phoneLine = 2;//手机号
        int userNameLine = 1;//姓名
        int registeredDateLine = 4;//注册时间
        int creditDateLine = 5;//授信时间
        String registeredDateStr = String.valueOf(row.getCell(registeredDateLine));
        String creditDateStr= String.valueOf(row.getCell(creditDateLine));
        Date registeredDate=null;
        Date creditDate=null;
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));

        if(StringUtils.isBlank(registeredDateStr) || "null".equals(registeredDateStr)){
            errors.add(new ExcelErrorMsgBean(index, registeredDateLine+1, "注册时间不能为空"));
            return null;
        }else{
            registeredDate=CommonUtil.getFormatDate(registeredDateStr);
        }
        if(StringUtils.isBlank(creditDateStr) || "null".equals(creditDateStr)){
            errors.add(new ExcelErrorMsgBean(index, creditDateLine+1, "授信时间不能为空"));
            return null;
        }else{
            creditDate=CommonUtil.getFormatDate(creditDateStr);
        }
        if(creditDate.getTime()<registeredDate.getTime()){
            errors.add(new ExcelErrorMsgBean(index, creditDateLine+1, "授信时间不能小于注册时间"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "手机号码不能为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "姓名不能为空"));
            return null;
        }
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName.substring(0, 1) + "%");
        order.setOrderPhoneStart(orderPhone.substring(0,3) + "%");
        order.setOrderPhoneEnd(orderPhone.substring(orderPhone.length() - 4,orderPhone.length()));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setOrderNo(item.getOrderNo());
        order.setBatchNo(batchThreadLocal.get());
        return order;
    }

    /**
     * 信而富
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getXefOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int phoneLine = 1;//手机号
        int userNameLine = 0;//姓名
        int loanAmountLine = 3;//借款金额
        int createDateLine = 2;//放款时间
        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));

        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "放款金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "放款金额必须为数字"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "手机号码不能为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "姓名不能为空"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额必须大于0"));
            return null;
        }
        if(StringUtils.isBlank(createDateStr)){
            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "放款时间不能为空"));
            return null;
        }
        String orderNameStart = orderName.substring(0, 1) + "%";
        int orderNameLength = orderName.length() * 3;
        String orderNameEnd = "";
        if(orderName.length() >= 2){
            orderNameEnd = orderName.substring(2);
        }
        int orderNameEndLength = orderNameEnd.length();
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderNameStart(orderNameStart);
        order.setOrderNameEnd(orderNameEnd);
        order.setOrderNameLength(orderNameLength);
        order.setOrderNameEndLength(orderNameEndLength);
//        order.setOrderName(orderName.substring(0, 1) + "%");
        order.setOrderPhoneStart(orderPhone.substring(0,3) + "%");
        order.setOrderPhoneEnd(orderPhone.substring(orderPhone.length() - 4,orderPhone.length()));
//        order.setCreateDate(CommonUtil.getFormatDate(createDateStr));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setLoanAmount(loanAmount);
        order.setOrderNo(item.getOrderNo());
        order.setBatchNo(batchThreadLocal.get());
        return order;
    }

    /**
     * 微贷
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getWdOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int phoneLine = 4;//手机号
        int userNameLine = 3;//姓名
        int loanAmountLine = 1;//借款金额
        int createDateLine = 0;//进件时间
        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));

        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额必须为数字"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "手机号码不能为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "姓名不能为空"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额必须大于0"));
            return null;
        }
        if(StringUtils.isBlank(createDateStr)){
            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "进件时间不能为空"));
            return null;
        }
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName);
        order.setOrderPhoneStart(orderPhone.substring(0,3) + "%");
        order.setOrderPhoneEnd(orderPhone.substring(orderPhone.length() - 4,orderPhone.length()));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setLoanAmount(loanAmount);
        order.setOrderNo(item.getOrderNo());
        order.setBatchNo(batchThreadLocal.get());
        return order;
    }

    /**
     * 中安信业
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getZaxyOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int userNameLine = 2;//姓名
        int loanAmountLine = 7;//借款金额
        int createDateLine = 7;//进件时间
        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String orderName = String.valueOf(row.getCell(userNameLine));

        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额必须为数字"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "姓名不能为空"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额必须大于0"));
            return null;
        }
        if(StringUtils.isBlank(createDateStr)){
            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "进件时间不能为空"));
            return null;
        }
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName);
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setLoanAmount(loanAmount);
        order.setOrderNo(item.getOrderNo());
        order.setBatchNo(batchThreadLocal.get());
        return order;
    }

    /**
     * 苏宁金融
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getSnjrOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int phoneLine = 1;//手机号
        int userNameLine = 2;//姓名
        int createDateLine = 3;//进件时间
        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));

        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "手机号码不能为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "姓名不能为空"));
            return null;
        }
        if(StringUtils.isBlank(createDateStr)){
            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "进件时间不能为空"));
            return null;
        }
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        int length=orderName.length();
        order.setOrderName("%"+orderName.substring(1,length));
        order.setOrderPhoneStart(orderPhone.substring(0,3) + "%");
        order.setOrderPhoneEnd(orderPhone.substring(orderPhone.length() - 4,orderPhone.length()));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setOrderNo(item.getOrderNo());
        order.setBatchNo(batchThreadLocal.get());
        return order;
    }

    /**
     * 全民购
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getQmgOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int phoneLine = 2;//手机号
        int userNameLine = 1;//姓名
        int createDateLine = 7;//进件时间
        int loanAmountLine = 9;//借款金额
        int loanOrderNo = 5;//借款金额

        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));
        String loanOrderNoStr=String.valueOf(row.getCell(loanOrderNo));

        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额必须为数字"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额必须大于0"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "手机号码不能为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "姓名不能为空"));
            return null;
        }
        if(StringUtils.isBlank(createDateStr)|| "null".equals(createDateStr)){
            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "进件时间不能为空"));
            return null;
        }
        if(StringUtils.isBlank(loanOrderNoStr)|| "null".equals(loanOrderNoStr)){
            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "借款订单号不能为空"));
            return null;
        }
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName);
        order.setLoanAmount(loanAmount);
        order.setLoanOrderNo(loanOrderNoStr);
        order.setOrderPhoneStart(orderPhone.substring(0,3) + "%");
        order.setOrderPhoneEnd(orderPhone.substring(orderPhone.length() - 4,orderPhone.length()));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setBatchNo(batchThreadLocal.get());
        order.setOrderNo(item.getOrderNo());
        return order;
    }

    /**
     * 趣花分期
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getQhfqOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int phoneLine = 2;//手机号
        int userNameLine = 1;//姓名
        int createDateLine = 3;//放款日期
        int loanAmountLine = 4;//借款金额

        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));

        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "放款金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "放款金额必须为数字"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "放款金额必须大于0"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "手机号码不能为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "姓名不能为空"));
            return null;
        }
        if(StringUtils.isBlank(createDateStr)|| "null".equals(createDateStr)){
            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "放款日期不能为空"));
            return null;
        }
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName);
        order.setLoanAmount(loanAmount);
        order.setOrderPhoneStart(orderPhone.substring(0,3) + "%");
        order.setOrderPhoneEnd(orderPhone.substring(orderPhone.length() - 4,orderPhone.length()));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setBatchNo(batchThreadLocal.get());
        order.setOrderNo(item.getOrderNo());
        return order;
    }

    /**
     * 提钱游
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getTqyOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int phoneLine = 1;//手机号
        int userNameLine = 0;//姓名
        int createDateLine = 3;//放款日期
        int loanAmountLine = 2;//借款金额

        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));

        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额必须为数字"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额必须大于0"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "借款人手机号为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "借款人姓名不能为空"));
            return null;
        }
        if(StringUtils.isBlank(createDateStr)|| "null".equals(createDateStr)){
            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "借款时间不能为空"));
            return null;
        }
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName.substring(0, 1) + "%");
        order.setLoanAmount(loanAmount);
        order.setOrderPhoneStart(orderPhone.substring(0,3) + "%");
        order.setOrderPhoneEnd(orderPhone.substring(orderPhone.length() - 4,orderPhone.length()));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setBatchNo(batchThreadLocal.get());
        order.setOrderNo(item.getOrderNo());
        return order;
    }

    /**
     * 玖富小蓝卡
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getJfxlkOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int phoneLine = 1;//手机号
        int userNameLine = 0;//姓名
        int createDateLine = 2;//日期
        int loanAmountLine = 3;//提现金额

        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));

        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "提现金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "提现金额必须为数字"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "提现金额必须大于0"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "客户手机号为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "客户姓名姓名不能为空"));
            return null;
        }
        if(StringUtils.isBlank(createDateStr)|| "null".equals(createDateStr)){
            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "日期不能为空"));
            return null;
        }
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName.substring(0,1)+"%");
        order.setLoanAmount(loanAmount);
        order.setOrderPhoneStart(orderPhone.substring(0,4) + "%");
        order.setOrderPhoneEnd(orderPhone.substring(orderPhone.length() - 3,orderPhone.length()));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setBatchNo(batchThreadLocal.get());
        order.setOrderNo(item.getOrderNo());
        return order;
    }

    /**
     * 平安普惠i贷
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getPaphOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {

        int phoneLine = 3;//手机号
        int userNameLine = 2;//姓名
        int createDateLine = 4;//日期
        int loanAmountLine = 5;//提现金额

        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));

        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "首动金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "首动金额必须为数字"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "首动金额必须大于0"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "MOBILE为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "name1不能为空"));
            return null;
        }
        if(StringUtils.isBlank(createDateStr)|| "null".equals(createDateStr)){
            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "首动日期不能为空"));
            return null;
        }
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName+"%");
        order.setLoanAmount(loanAmount);
        order.setOrderPhoneStart(orderPhone.substring(0,3) + "%");
        order.setOrderPhoneEnd(orderPhone.substring(orderPhone.length() - 4,orderPhone.length()));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setBatchNo(batchThreadLocal.get());
        order.setOrderNo(item.getOrderNo());
        return order;
    }


    /**
     * 大王贷款
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getDwOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {

        int phoneLine = 2;//手机号
        int userNameLine = 4;//姓名
        int createDateLine = 0;//日期
        int loanAmountLine = 1;//提现金额

        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));

        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "放款金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "放款金额必须为数字"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "放款金额必须大于0"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "电话为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "姓名不能为空"));
            return null;
        }
        if(StringUtils.isBlank(createDateStr)|| "null".equals(createDateStr)){
            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "放款时间不能为空"));
            return null;
        }
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName.substring(0,1)+"%");
        order.setLoanAmount(loanAmount);
        order.setOrderPhoneStart(orderPhone.substring(0,3) + "%");
        order.setOrderPhoneEnd(orderPhone.substring(orderPhone.length() - 4,orderPhone.length()));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setBatchNo(batchThreadLocal.get());
        order.setOrderNo(item.getOrderNo());
        return order;
    }

    /**
     * 秒贝
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getMbOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {

        int phoneLine = 0;//手机号
        int userNameLine = 1;//姓名
        int createDateLine = 2;//日期
        int loanAmountLine = 3;//提现金额

        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));

        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "金额必须为数字"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "金额必须大于0"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "手机号为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "姓名不能为空"));
            return null;
        }
        if(StringUtils.isBlank(createDateStr)|| "null".equals(createDateStr)){
            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "时间不能为空"));
            return null;
        }
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName);
        order.setLoanAmount(loanAmount);
        BigDecimal phone=new BigDecimal(orderPhone);
        order.setOrderPhone(String.valueOf(phone));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        log.info("贷款匹配数据"+ JSON.toJSONString(order));
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setBatchNo(batchThreadLocal.get());
        order.setOrderNo(item.getOrderNo());
        return order;
    }

    /**
     * 京东白条
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getJdbtOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {

        int phoneLine = 1;//手机号
        int userNameLine = 0;//姓名
        int createDateLine = 5;//日期

        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "手机号为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "姓名不能为空"));
            return null;
        }
        if(StringUtils.isBlank(createDateStr)|| "null".equals(createDateStr)){
            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "时间不能为空"));
            return null;
        }
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderPhoneStart(orderPhone.substring(0,3)+"%");
        int len=orderPhone.length();
        order.setOrderPhone(orderPhone);
        order.setOrderPhoneEnd(orderPhone.substring(len-4,len));
        int orderNameLength = orderName.length() * 3;
        String orderNameStart = "";
        if(orderName.length() > 2){
            orderNameStart=orderName.substring(0,1)+"%";
        }
        order.setOrderNameStart(orderNameStart);
        order.setOrderNameEnd(orderName.substring(orderName.length()-1));
        order.setOrderNameLength(orderNameLength);
        order.setOrderType("6");//贷款批贷的订单才可以导入
        log.info("贷款匹配数据"+ JSON.toJSONString(order));
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setBatchNo(batchThreadLocal.get());
        order.setOrderNo(item.getOrderNo());
        return order;
    }



    /**
     * 龙分期
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getLfqOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int phoneLine = 1;//手机号
        int userNameLine = 0;//姓名
        int loanAmountLine = 2;//提现金额

        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));

        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "放款金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "放款金额必须为数字"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "放款金额必须大于0"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "手机号为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "姓名姓名不能为空"));
            return null;
        }

        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        int orderNameLength = orderName.length() * 3;
        String orderNameEnd = "";
        if(orderName.length() >= 2){
            orderNameEnd = orderName.substring(2);
        }
        int orderNameEndLength = orderNameEnd.length();
        order.setOrderNameStart(orderName.substring(0, 1) + "%");
        order.setOrderNameEnd(orderNameEnd);
        order.setOrderNameLength(orderNameLength);
        order.setOrderNameEndLength(orderNameEndLength);
        order.setLoanAmount(loanAmount);
        order.setOrderPhoneStart(orderPhone.substring(0,3) + "%");
        order.setOrderPhoneEnd(orderPhone.substring(orderPhone.length() - 4,orderPhone.length()));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setBatchNo(batchThreadLocal.get());
        order.setOrderNo(item.getOrderNo());
        return order;
    }

    /**
     * 你我贷
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getNwdOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int phoneLine = 3;//手机号
        int userNameLine = 2;//姓名
        int createDateLine = 5;//放款日期
        int loanAmountLine = 4;//借款金额

        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));

        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "放款金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "放款金额必须为数字"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "放款金额必须大于0"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "手机号码不能为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "姓名不能为空"));
            return null;
        }
        if(StringUtils.isBlank(createDateStr)|| "null".equals(createDateStr)){
            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "放款日期不能为空"));
            return null;
        }
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName);
        order.setLoanAmount(loanAmount);
        order.setOrderPhoneStart(orderPhone.substring(0,4) + "%");
        order.setOrderPhoneEnd(orderPhone.substring(orderPhone.length() - 4,orderPhone.length()));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setBatchNo(batchThreadLocal.get());
        order.setOrderNo(item.getOrderNo());
        return order;
    }


    /**
     * 众安杏仁派
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getZaOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int phoneLine = 0;//手机号
        int userNameLine = 1;//姓名
        int loanAmountLine = 4;//借款金额
        int createDateLine = 3;//放款时间
        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));

        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额必须为数字"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "手机号码不能为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "姓名不能为空"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额必须大于0"));
            return null;
        }
        if(StringUtils.isBlank(createDateStr)){
            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "借款时间不能为空"));
            return null;
        }
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName.substring(0, 1) + "%");
        order.setOrderPhoneStart(orderPhone.substring(0,3) + "%");
        order.setOrderPhoneEnd(orderPhone.substring(orderPhone.length() - 4,orderPhone.length()));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setLoanAmount(loanAmount);
        order.setOrderNo(item.getOrderNo());
        order.setBatchNo(batchThreadLocal.get());
        return order;
    }

    /**
     * 豆豆钱
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getDdqOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int phoneLine = 3;//手机号
        int userNameLine = 2;//姓名
        int loanAmountLine = 4;//借款金额
        int createDateLine = 5;//放款时间
        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));

        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "首贷金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "首贷金额必须为数字"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "实名手机号不能为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "姓名不能为空"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "首贷金额必须大于0"));
            return null;
        }
        if(StringUtils.isBlank(createDateStr)){
            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "放款时间不能为空"));
            return null;
        }
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        int length=orderName.length();
        order.setOrderName(orderName.substring(0, length-1) + "%");
        order.setOrderPhoneStart(orderPhone.substring(0,3) + "%");
        order.setOrderPhoneEnd(orderPhone.substring(orderPhone.length() - 4,orderPhone.length()));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setLoanAmount(loanAmount);
        order.setOrderNo(item.getOrderNo());
        order.setBatchNo(batchThreadLocal.get());
        return order;
    }

    /**
     * 快易花
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getKyhOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int phoneLine = 1;//手机号
        int userNameLine = 2;//姓名
        int loanAmountLine = 5;//借款金额
        int createDateLine = 4;//放款时间
        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));

        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "实动金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "实动金额必须为数字"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "手机号码不能为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "姓氏不能为空"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "实动金额必须大于0"));
            return null;
        }
        if(StringUtils.isBlank(createDateStr)){
            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "实动日期不能为空"));
            return null;
        }
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName.substring(0, 1) + "%");
        order.setOrderPhoneStart(orderPhone.substring(0,3) + "%");
        order.setOrderPhoneEnd(orderPhone.substring(orderPhone.length() - 4,orderPhone.length()));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setLoanAmount(loanAmount);
        order.setOrderNo(item.getOrderNo());
        order.setBatchNo(batchThreadLocal.get());
        return order;
    }


    /**
     * 人品贷
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getRpdOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        int phoneLine = 1;//手机号
        int userNameLine = 0;//姓名
        int loanAmountLine = 3;//借款金额
        int createDateLine = 2;//放款时间
        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));

        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额必须为数字"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone) || orderPhone.length() < 11){
            errors.add(new ExcelErrorMsgBean(index, phoneLine+1, "手机号码不能为空或者不够11位"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine+1, "姓名不能为空"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine+1, "借款金额必须大于0"));
            return null;
        }
//        if(StringUtils.isBlank(createDateStr)){
//            errors.add(new ExcelErrorMsgBean(index, createDateLine+1, "放款时间不能为空"));
//            return null;
//        }
        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName.substring(0, 1) + "%");
        order.setOrderPhoneStart(orderPhone.substring(0,3) + "%");
        order.setOrderPhoneEnd(orderPhone.substring(orderPhone.length() - 4,orderPhone.length()));
//        order.setCreateDate(CommonUtil.getFormatDate(createDateStr));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus("5");//订单成功
        order.setLoanAmount(loanAmount);
        order.setOrderNo(item.getOrderNo());
        order.setBatchNo(batchThreadLocal.get());
        return order;
    }

    /**
     * 钱隆柜
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getQlgOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        //手机号
        int phoneLine = 1;
        //姓名
        int userNameLine = 0;
        //借款金额
        int loanAmountLine = 2;
        //有效时间
        int createDateLine = 3;
        int updateDateLine = 4;
        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));
        String createDateStr = String.valueOf(row.getCell(createDateLine));
        String updateDateStr = String.valueOf(row.getCell(updateDateLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));
        String status = "5";//订单成功
        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额必须为数字"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone)){
            errors.add(new ExcelErrorMsgBean(index, phoneLine, "手机号码不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine, "姓名不能为空"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额必须大于0"));
            return null;
        }
        BigDecimal orderPhoneDecimal = new BigDecimal(orderPhone);
        orderPhone = orderPhoneDecimal.toPlainString();
        //放款时间
//        log.info("贷款时间:loanDateStr:{}",loanDateStr);
//        Map<String, Object> monthMap = CommonUtil.getMonthMap();
//        String[] loanDateArr = loanDateStr.split("-");
//        String createDateEnd = loanDateArr[2] + "-" + monthMap.get(loanDateArr[1]) + "-" + loanDateArr[0]
//                + " 23:59:59";
//        Date date = DateUtil.parseLongDateTime(createDateEnd);
//        Date createDateStart = new Date(date.getTime() - 30*24*60*60*1000L);//30天内的订单

        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName);
        order.setOrderPhone(orderPhone);
//        order.setCreateDateEnd(createDateEnd);
//        order.setCreateDateStart(DateUtil.getLongFormatDate(createDateStart));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus(status);
        order.setLoanAmount(loanAmount);
        order.setOrderNo(item.getOrderNo());
        order.setBatchNo(batchThreadLocal.get());
//        orderMainDao.updateStatus(order);
//        orderMainDao.updateExpired(order);//将其他订单置为失效
        return order;
    }

    /**
     * 恒快贷
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getHkdOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        //手机号
        int phoneLine = 2;
        //姓名
        int userNameLine = 1;
        //借款金额
        int loanAmountLine = 3;
        //有效时间
        int effectiveLine = 4;
        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));
        String loanDateStr = String.valueOf(row.getCell(effectiveLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));
        String status = "5";//订单成功
        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额必须为数字"));
            return null;
        }
        if(StringUtils.isBlank(loanDateStr) || "null".equals(loanDateStr)){
            errors.add(new ExcelErrorMsgBean(index, effectiveLine, "有效时间不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone)){
            errors.add(new ExcelErrorMsgBean(index, phoneLine, "手机号码不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine, "姓名不能为空"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额必须大于0"));
            return null;
        }
        //手机号前三位
        String orderPhoneStart = orderPhone.substring(0,3) + "%";
        //手机号末四位
        String orderPhoneEnd = orderPhone.substring(orderPhone.length() - 4,orderPhone.length());
        //姓
        orderName = orderName.substring(0,1) + "%";
        //放款时间
//        log.info("贷款时间:loanDateStr:{}",loanDateStr);
//        Map<String, Object> monthMap = CommonUtil.getMonthMap();
//        String[] loanDateArr = loanDateStr.split("-");
//        String createDateEnd = loanDateArr[2] + "-" + monthMap.get(loanDateArr[1]) + "-" + loanDateArr[0]
//                + " 23:59:59";
//        Date date = DateUtil.parseLongDateTime(createDateEnd);
//        Date createDateStart = new Date(date.getTime() - 30*24*60*60*1000L);//30天内的订单

        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName);
        order.setOrderPhoneStart(orderPhoneStart);
        order.setOrderPhoneEnd(orderPhoneEnd);
//        order.setCreateDateEnd(createDateEnd);
//        order.setCreateDateStart(DateUtil.getLongFormatDate(createDateStart));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus(status);
        order.setLoanAmount(loanAmount);
        order.setOrderNo(item.getOrderNo());
        order.setBatchNo(batchThreadLocal.get());
//        orderMainDao.updateStatus(order);
//        orderMainDao.updateExpired(order);//将其他订单置为失效
        return order;
    }

    /**
     * 万达普惠
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getWdphOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        //手机号
        int phoneLine = 2;
        //姓名
        int userNameLine = 1;
        //借款金额
        int loanAmountLine = 3;
        //有效时间
        int effectiveLine = 0;
        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));
        String loanDateStr = String.valueOf(row.getCell(effectiveLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));
        String status = "5";//订单成功
        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额必须为数字"));
            return null;
        }
        if(StringUtils.isBlank(loanDateStr) || "null".equals(loanDateStr)){
            errors.add(new ExcelErrorMsgBean(index, effectiveLine, "有效时间不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone)){
            errors.add(new ExcelErrorMsgBean(index, phoneLine, "手机号码不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine, "姓名不能为空"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额必须大于0"));
            return null;
        }
        //手机号前三位
        String orderPhoneStart = orderPhone.substring(0,3) + "%";
        //手机号末四位
        String orderPhoneEnd = orderPhone.substring(orderPhone.length() - 4,orderPhone.length());

        //放款时间
//        log.info("贷款时间:loanDateStr:{}",loanDateStr);
//        Map<String, Object> monthMap = CommonUtil.getMonthMap();
//        String[] loanDateArr = loanDateStr.split("-");
//        String createDateEnd = loanDateArr[2] + "-" + monthMap.get(loanDateArr[1]) + "-" + loanDateArr[0]
//                + " 23:59:59";
//        Date date = DateUtil.parseLongDateTime(createDateEnd);
//        Date createDateStart = new Date(date.getTime() - 30*24*60*60*1000L);//30天内的订单

        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName);
        order.setOrderPhoneStart(orderPhoneStart);
        order.setOrderPhoneEnd(orderPhoneEnd);
//        order.setCreateDateEnd(createDateEnd);
//        order.setCreateDateStart(DateUtil.getLongFormatDate(createDateStart));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus(status);
        order.setLoanAmount(loanAmount);
        order.setOrderNo(item.getOrderNo());
        order.setBatchNo(batchThreadLocal.get());
//        orderMainDao.updateStatus(order);
//        orderMainDao.updateExpired(order);//将其他订单置为失效
        return order;
    }


    /**
     * 超人卡
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getCrkOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        //手机号
        int phoneLine = 2;
        //姓名
        int userNameLine = 1;
        //借款金额
        int loanAmountLine = 3;
        //有效时间
        int effectiveLine = 4;
        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));
        String loanDateStr = String.valueOf(row.getCell(effectiveLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));
        String status = "5";//订单成功
        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额必须为数字"));
            return null;
        }
        if(StringUtils.isBlank(loanDateStr) || "null".equals(loanDateStr)){
            errors.add(new ExcelErrorMsgBean(index, effectiveLine, "有效时间不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone)){
            errors.add(new ExcelErrorMsgBean(index, phoneLine, "手机号码不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine, "姓名不能为空"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额必须大于0"));
            return null;
        }
        //手机号前三位
        String orderPhoneStart = orderPhone.substring(0,3) + "%";
        //手机号末四位
        String orderPhoneEnd = orderPhone.substring(orderPhone.length() - 4,orderPhone.length());

        //放款时间
//        log.info("贷款时间:loanDateStr:{}",loanDateStr);
//        Map<String, Object> monthMap = CommonUtil.getMonthMap();
//        String[] loanDateArr = loanDateStr.split("-");
//        String createDateEnd = loanDateArr[2] + "-" + monthMap.get(loanDateArr[1]) + "-" + loanDateArr[0]
//                + " 23:59:59";
//        Date date = DateUtil.parseLongDateTime(createDateEnd);
//        Date createDateStart = new Date(date.getTime() - 30*24*60*60*1000L);//30天内的订单

        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName);
        order.setOrderPhoneStart(orderPhoneStart);
        order.setOrderPhoneEnd(orderPhoneEnd);
//        order.setCreateDateEnd(createDateEnd);
//        order.setCreateDateStart(DateUtil.getLongFormatDate(createDateStart));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus(status);
        order.setLoanAmount(loanAmount);
        order.setOrderNo(item.getOrderNo());
        order.setBatchNo(batchThreadLocal.get());
//        orderMainDao.updateStatus(order);
//        orderMainDao.updateExpired(order);//将其他订单置为失效
        return order;
    }

    /**
     * 嘉卡贷
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getJkdOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        //手机号
        int phoneLine = 1;
        //姓名
        int userNameLine = 0;
        //借款金额
        int loanAmountLine = 2;
        //有效时间
        int effectiveLine = 4;
        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));
        String loanDateStr = String.valueOf(row.getCell(effectiveLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));
        String status = "5";//订单成功
        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额不能为空"));
            return null;
        }
        if(!StringUtil.isNumeric(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额必须为数字"));
            return null;
        }
        if(StringUtils.isBlank(loanDateStr) || "null".equals(loanDateStr)){
            errors.add(new ExcelErrorMsgBean(index, effectiveLine, "有效时间不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone)){
            errors.add(new ExcelErrorMsgBean(index, phoneLine, "手机号码不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine, "姓名不能为空"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额必须大于0"));
            return null;
        }
        //手机号前三位
        String orderPhoneStart = orderPhone.substring(0,3) + "%";
        //手机号末四位
        String orderPhoneEnd = orderPhone.substring(orderPhone.length() - 4,orderPhone.length());

        //放款时间
//        log.info("贷款时间:loanDateStr:{}",loanDateStr);
//        Map<String, Object> monthMap = CommonUtil.getMonthMap();
//        String[] loanDateArr = loanDateStr.split("-");
//        String createDateEnd = loanDateArr[2] + "-" + monthMap.get(loanDateArr[1]) + "-" + loanDateArr[0]
//                + " 23:59:59";
//        Date date = DateUtil.parseLongDateTime(createDateEnd);
//        Date createDateStart = new Date(date.getTime() - 30*24*60*60*1000L);//30天内的订单

        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName);
        order.setOrderPhoneStart(orderPhoneStart);
        order.setOrderPhoneEnd(orderPhoneEnd);
//        order.setCreateDateEnd(createDateEnd);
//        order.setCreateDateStart(DateUtil.getLongFormatDate(createDateStart));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus(status);
        order.setLoanAmount(loanAmount);
        order.setOrderNo(item.getOrderNo());
        order.setBatchNo(batchThreadLocal.get());
//        orderMainDao.updateStatus(order);
//        orderMainDao.updateExpired(order);//将其他订单置为失效
        return order;
    }

    /**
     * 小树普惠
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getXsphOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        //手机号
        int phoneLine = 0;
        //姓名
        int userNameLine = 1;
        //借款金额
        int loanAmountLine = 2;
        //有效时间
        int effectiveLine = 4;
        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));
        String loanDateStr = String.valueOf(row.getCell(effectiveLine));
        String orderPhone = String.valueOf(row.getCell(phoneLine));
        String orderName = String.valueOf(row.getCell(userNameLine));
        String status = "5";//订单成功
        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额不能为空"));
            return null;
        }
        if(StringUtils.isBlank(loanDateStr) || "null".equals(loanDateStr)){
            errors.add(new ExcelErrorMsgBean(index, effectiveLine, "有效时间不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone)){
            errors.add(new ExcelErrorMsgBean(index, phoneLine, "手机号码不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine, "姓名不能为空"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额必须大于0"));
            return null;
        }
        //手机号前三位
        String orderPhoneStart = orderPhone.substring(0,3) + "%";
        //手机号末四位
        String orderPhoneEnd = orderPhone.substring(orderPhone.length() - 4,orderPhone.length());
        //姓
        orderName = orderName.substring(0,1) + "%";
        //放款时间
//        log.info("贷款时间:loanDateStr:{}",loanDateStr);
//        Map<String, Object> monthMap = CommonUtil.getMonthMap();
//        String[] loanDateArr = loanDateStr.split("-");
//        String createDateEnd = loanDateArr[2] + "-" + monthMap.get(loanDateArr[1]) + "-" + loanDateArr[0]
//                + " 23:59:59";
//        Date date = DateUtil.parseLongDateTime(createDateEnd);
//        Date createDateStart = new Date(date.getTime() - 30*24*60*60*1000L);//30天内的订单

        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName);
        order.setOrderPhoneStart(orderPhoneStart);
        order.setOrderPhoneEnd(orderPhoneEnd);
//        order.setCreateDateEnd(createDateEnd);
//        order.setCreateDateStart(DateUtil.getLongFormatDate(createDateStart));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus(status);
        order.setLoanAmount(loanAmount);
        order.setOrderNo(item.getOrderNo());
        order.setBatchNo(batchThreadLocal.get());
//        orderMainDao.updateStatus(order);
//        orderMainDao.updateExpired(order);//将其他订单置为失效
        return order;
    }

    /**
     * 检查订单是否已存在成功订单 | 是否存在符合规则的 | 是否已入账
     * @param index
     * @param errors
     * @param order
     * @return
     */
    private OrderMain checkExistsOrder(int index, List<ExcelErrorMsgBean> errors, OrderMain order) {
        List<OrderMain> itemList;//检查订单是否存在已成功的，若有，则返回
        String ruleCode = ruleCodeThread.get();
        if(ruleCode.equals(LoanSourceCode.YRD)
                || ruleCode.equals(LoanSourceCode.XSPH)
                || ruleCode.equals(LoanSourceCode.HKD)
                || ruleCode.equals(LoanSourceCode.RPD)
                || ruleCode.equals(LoanSourceCode.GFD)
                || ruleCode.equals(LoanSourceCode.GT360)
                || ruleCode.equals(LoanSourceCode.KYH)
                || ruleCode.equals(LoanSourceCode.DDQ)
                || ruleCode.equals(LoanSourceCode.ZA)
                || ruleCode.equals(LoanSourceCode.SNJR)
                || ruleCode.equals(LoanSourceCode.TQY)
                || ruleCode.equals(LoanSourceCode.PAPH)
                || ruleCode.equals(LoanSourceCode.DW)
                ){
            itemList = orderMainDao.selectYRDOrder(order);
        } else if(ruleCode.equals(LoanSourceCode.QLG)||ruleCode.equals(LoanSourceCode.MB)){
            itemList = orderMainDao.selecQLGOrder(order);
        } else if (ruleCode.equals(LoanSourceCode.XEF)||ruleCode.equals(LoanSourceCode.LFQ)){
            itemList = orderMainDao.selectXEFOrder(order);
        } else if (ruleCode.equals(LoanSourceCode.ZAXY)){
            itemList = orderMainDao.selectZAXYOrder(order);
        } else if (ruleCode.equals(LoanSourceCode.QMG)){
            itemList = orderMainDao.selectQMGOrder(order);
        } else if (ruleCode.equals(LoanSourceCode.JFXLK)){
            itemList = orderMainDao.selectJfxlkOrder(order);
        }else if (ruleCode.equals(LoanSourceCode.JDBT)){
            itemList = orderMainDao.selectJdbtOrder(order);
        }
        else {
            itemList = orderMainDao.selectKKDOrder(order);
        }
        if(itemList == null || itemList.size() < 1){
            errors.add(new ExcelErrorMsgBean(index, 1, "找不到符合规则的订单"));
            return null;
        }
        boolean existsSuccess = false;//是否存在已完成的
        for (OrderMain orderItem : itemList) {
            if ("5".equals(orderItem.getStatus())) {
                existsSuccess = true;
                log.info("二次贷款,最近贷款成功单号为:{}", orderItem.getOrderNo());
                errors.add(new ExcelErrorMsgBean(index, 1, "二次贷款,最近贷款成功单号为:" + orderItem.getOrderNo()));
                break;
            }
        }
        if(existsSuccess){
            return null;
        }
        //将第一条订单（最新的一条，之前在sql已排序），准备置为已完成
        OrderMain orderSuccess = itemList.get(0);
        //检查订单的 身份证号 + 银行名称 + 状态已完成，是否已存在，若存在，则返回
        LoanSource loanSource = loanSourceDao.selectDetail(order.getLoanSourceId());

        if(ruleCode.equals(LoanSourceCode.QMG)&&"0".equals(loanSource.getLoanStatus())){
            int i = orderMainDao.selectByLoanOrderNo(order.getLoanOrderNo());
            if(i>0){
                log.info("二次贷款,最近贷款成功借款订单号为:{}", order.getLoanOrderNo());
                errors.add(new ExcelErrorMsgBean(index, 1, "二次贷款,最近贷款成功借款订单号为:" + order.getLoanOrderNo()));
                return null;
            }
            order.setOrderNo(orderSuccess.getOrderNo());
            UserLoginInfo userInfo = CommonUtil.getLoginUser();
            order.setUpdateBy(userInfo.getId().toString());
            order.setUpdateDate(new Date());
            order.setStatus("5");//订单成功
            order.setBatchNo(batchThreadLocal.get());
            orderMainDao.updateLoanOrder(order);
        }else{
            Long longNum = orderMainDao.checkExistsSuccessLoanOrder(orderSuccess);
            if(longNum != null && longNum > 0){
                errors.add(new ExcelErrorMsgBean(index, 1, "身份证号:" + orderSuccess.getOrderIdNo() + "和贷款机构:" + orderSuccess.getLoanName() + "组合存在已完成的订单"));
                log.info("身份证号:{}和贷款机构:{}组合存在已完成的订单", orderSuccess.getOrderIdNo(), orderSuccess.getLoanName());
                return null;
            }
        }
        return orderSuccess;
    }

    /**
     * 卡卡贷
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getKadOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        //放款日期
        int loanDateLine = 0;
        //姓名
        int orderNameLine = 1;
        //借款金额
        int orderPhoneLine = 2;
        //有效时间
        int loanAmountLine = 3;
        //校验借款金额、有效时间、手机号、姓名，是否为空或格式不正确
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));
        String loanDateStr = String.valueOf(row.getCell(loanDateLine));
        String orderPhone = String.valueOf(row.getCell(orderPhoneLine));
        String orderName = String.valueOf(row.getCell(orderNameLine));
        String status = "5";//订单成功

        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额不能为空"));
            return null;
        }
        if(StringUtils.isBlank(loanDateStr) || "null".equals(loanDateStr)){
            errors.add(new ExcelErrorMsgBean(index, loanDateLine, "放款日期不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone)){
            errors.add(new ExcelErrorMsgBean(index, orderPhoneLine, "手机号码不能为空"));
            return null;
        }
        if(StringUtils.isBlank(orderName) || "null".equals(orderName)){
            errors.add(new ExcelErrorMsgBean(index, orderNameLine, "姓名不能为空"));
            return null;
        }
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额必须大于0"));
            return null;
        }
        //手机号前三位
        String orderPhoneStart = orderPhone.substring(0,3) + "%";
        //手机号末四位
        String orderPhoneEnd = orderPhone.substring(orderPhone.length() - 4,orderPhone.length());

        //放款时间
//        log.info("贷款时间:loanDateStr:{}",loanDateStr);
//        Map<String, Object> monthMap = CommonUtil.getMonthMap();
//        String[] loanDateArr = loanDateStr.split("-");
//        String createDateEnd = loanDateArr[2] + "-" + monthMap.get(loanDateArr[1]) + "-" + loanDateArr[0]
//                + " 23:59:59";
//        Date date = DateUtil.parseLongDateTime(createDateEnd);
//        Date createDateStart = new Date(date.getTime() - 30*24*60*60*1000L);//30天内的订单

        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName);
        order.setOrderPhoneStart(orderPhoneStart);
        order.setOrderPhoneEnd(orderPhoneEnd);
//        order.setCreateDateEnd(createDateEnd);
//        order.setCreateDateStart(DateUtil.getLongFormatDate(createDateStart));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus(status);
        order.setLoanAmount(loanAmount);
        order.setOrderNo(item.getOrderNo());
        order.setBatchNo(batchThreadLocal.get());
//        orderMainDao.updateStatus(order);
//        orderMainDao.updateExpired(order);
        return order;
    }

    /**
     * 宜人贷
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private OrderMain getYrdOrder(Row row, int index, List<ExcelErrorMsgBean> errors) {
        //宜人贷 0借款人，1借款人电话，2注册时间，3借款时间，
        // 4借款状态（成功放款，借款失败），5借款金额（万）
        //订单状态：5订单成功 6订单失败
        //宜人贷处理流程
        //1.根据订单状态，申请中的不予处理
        //2.剩下的订单
        //2.1校验每一笔的借款人、借款人电话、借款时间要大于订单创建时间，且最多只能一天内
        //2.1若借款失败，则更新订单状态为订单失败
        //2.2如果成功放款，更新订单状态为订单成功，调用计算分润接口

        int statusLine = 4;
        String successMsg = "成功放款";
        String failMsg = "借款失败";
        String status = String.valueOf(row.getCell(4));
        if(StringUtils.isBlank(status) || "null".equals(status)){
            errors.add(new ExcelErrorMsgBean(index, statusLine, "借款状态不能为空"));
            return null;
        }
        if(successMsg.equals(status)){
            status = "5";
        }else if(failMsg.equals(status)){
            status = "6";
        } else {
            return null;
        }
        int orderNameLine = 0;
        String orderName = String.valueOf(row.getCell(orderNameLine));
        if(StringUtils.isBlank(orderName) || "null".equals(orderName) || orderName.length() < 1
                ){
            errors.add(new ExcelErrorMsgBean(index, orderNameLine, "借款人不能为空"));
            return null;
        }
        int orderPhoneLine = 1;
        String orderPhone = String.valueOf(row.getCell(orderPhoneLine));
        if(StringUtils.isBlank(orderPhone) || "null".equals(orderPhone)
                || orderPhone.length() < 4){
            errors.add(new ExcelErrorMsgBean(index, orderPhoneLine, "借款人电话不能为空"));
            return null;
        }
        int loanDateLine = 3;
        String loanDateStr = String.valueOf(row.getCell(loanDateLine));
        if(StringUtils.isBlank(loanDateStr) || "null".equals(loanDateStr)){
            errors.add(new ExcelErrorMsgBean(index, loanDateLine, "借款时间不能为空"));
            return null;
        }
        int loanAmountLine = 5;
        String loanAmountStr = String.valueOf(row.getCell(loanAmountLine));
        if(StringUtils.isBlank(loanAmountStr) || "null".equals(loanAmountStr)){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额不能为空"));
            return null;
        }
//        if(! StringUtils.isNumeric(loanAmountStr)){
//            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额必须为数字"));
//            return null;
//        }
//        log.info("贷款时间:loanDateStr:{}",loanDateStr);
        BigDecimal loanAmount = new BigDecimal(loanAmountStr);
        if(loanAmount.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ExcelErrorMsgBean(index, loanAmountLine, "借款金额必须大于0"));
            return null;
        } else {
            loanAmount = loanAmount.multiply(new BigDecimal(10000));
        }
        orderName = orderName.substring(0,1) + "%";
        String orderPhoneStart = orderPhone.substring(0,3) + "%";
        String orderPhoneEnd = orderPhone.substring(orderPhone.length() - 4,orderPhone.length());
//        String createDateEnd = loanDateStr + " 23:59:59";
//        Date date = DateUtil.parseDateTime(loanDateStr);
//        Date createDateStart = new Date(date.getTime() - 30*24*60*60*1000L);

        OrderMain order = new OrderMain();
        order.setLoanSourceId(Long.parseLong(loanThreadLocal.get()));
        order.setOrderName(orderName);
        order.setOrderPhoneStart(orderPhoneStart);
        order.setOrderPhoneEnd(orderPhoneEnd);
//        order.setCreateDateEnd(createDateEnd);
//        order.setCreateDateStart(DateUtil.getLongFormatDate(createDateStart));
        order.setOrderType("6");//贷款批贷的订单才可以导入
        //检查订单是否符合规则
        OrderMain item = checkExistsOrder(index, errors, order);
        if(item == null){
            return null;
        }
        UserLoginInfo userInfo = CommonUtil.getLoginUser();
        order.setUpdateBy(userInfo.getId().toString());
        order.setUpdateDate(new Date());
        order.setStatus(status);
        order.setLoanAmount(loanAmount);
        order.setOrderNo(item.getOrderNo());

        if(status.equals("5")){
            order.setBatchNo(batchThreadLocal.get());
//            orderMainDao.updateExpired(order);
        }
//        orderMainDao.updateStatus(order);
        return order;
    }

    public void setBatchNo(String batchNo) {
        batchThreadLocal.set(batchNo);
    }

    public void setLoanSourceId(String loanSourceId){
        loanThreadLocal.set(loanSourceId);
    }

    public void setRuleCode(String ruleCode){
        ruleCodeThread.set(ruleCode);
    }
}
