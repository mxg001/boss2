package cn.eeepay.framework.service.impl;

import cn.eeepay.boss.thread.SuperBankBankProfitRunnable;
import cn.eeepay.framework.daoSuperbank.*;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.BankImportService;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("bankImportService")
public class BankImportServiceImpl implements BankImportService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private BankRowImpl bankRowImpl;

    @Resource
    private BankImportDao bankImportDao;

    @Resource
    private SequenceDao sequenceDao;

    @Resource
    private CreditcardSourceDao creditcardSourceDao;

    @Resource
    private CreditcardApplyRecordDao creditcardApplyRecordDao;

    @Resource
    private OrderMainDao orderMainDao;

    @Override
    public List<BankImport> selectList(BankImport baseInfo, Page<BankImport> page) {
        bankImportDao.selectList(baseInfo, page);
        List<BankImport> list = page.getResult();
        if(list != null && list.size() > 0){
            for(BankImport info: list){
                if(StringUtils.isNotBlank(info.getFileUrl())){
                    info.setFileUrlStr(CommonUtil.getImgUrlAgent(info.getFileUrl()));
                }
                if("1".equals(info.getStatus()) && info.getTotalNum()!=null){
                    info.setMessage("总条数：" + info.getTotalNum() + "，成功条数：" + info.getSuccessNum() +
                            "，失败条数：" + info.getFailNum());
                }
            }
        }
        return list;
    }

    /**
     * 导入银行数据
     * @param file
     * @param id
     * @return
     */
    @Override
    public Result importData(MultipartFile file, Long id, String bonusType) throws IOException {
        Result result = new Result();
        CreditcardSource bankInfo = creditcardSourceDao.selectDetail(id);
        if(bankInfo == null){
            result.setMsg("找不到对应的银行");
            return result;
        }
        String batchNo = sequenceDao.getValue(Constants.SUPER_BANK_BATCH_NO);
        bankRowImpl.setRuleCode(bankInfo.getRuleCode());
        bankRowImpl.setBankId(bankInfo.getId().toString());
        bankRowImpl.setBatchNo(batchNo);
        bankRowImpl.setBonusType(bonusType);
        try {
            List<ExcelErrorMsgBean> errors = new ArrayList<>();
            List<CreditcardApplyRecord> list = ExcelUtils.parseWorkbook(file.getInputStream(),
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    errors, bankRowImpl);
            int successSum = 0;
            if(list != null){
                successSum = list.size();
            }
            int failSum = 0;
            if(errors != null){
                failSum = errors.size();
            }
            String msg = "";
            String errMsg = "";
            //如果不符合条件的数据，将提示信息返回
            //只有全部成功，就插入导入的数据记录,并插入导入记录
            if (failSum > 0) {
                msg = "导入失败";
                log.info("导入失败，原因:{}", JSONObject.toJSONString(errors));
                errMsg =
                        "，第" + errors.get(0).getRow() + "行，" +
                                "第" + errors.get(0).getLine() + "列：" + errors.get(0).getMessage();
            } else if(successSum > 0){
                //上传文件、插入上传记录
                uploadBankData(file, bankInfo, batchNo, bonusType);
                //批量插入文件内容
                insertBatch(list, bonusType);
                msg = "导入成功";
            }
            result.setStatus(true);
            result.setMsg(msg + "，成功条数：" + successSum + "，失败条数：" + failSum + errMsg);
        } catch (Exception e){
            log.error("导入银行数据异常", e);
            throw new BossBaseException("导入银行数据异常");
        }
        return result;
    }

    private int insertBatch(List<CreditcardApplyRecord> list, String bonusType) {
        int num = 0;
        List<CreditcardApplyRecord> itemlist = new ArrayList<>();
        if(list != null){
            for(int i = 0; i < list.size(); i++ ){
                itemlist.add(list.get(i));
                if(itemlist.size() >= 300){
                    creditcardApplyRecordDao.insertBatch(itemlist);
                    itemlist.clear();
                }
            }
            if(itemlist.size() > 0){
                creditcardApplyRecordDao.insertBatch(itemlist);
            }
            num = list.size();
        }
        return num;
    }

    /**
     * 导入银行数据
     * 并插入导入记录
     * @param file
     * @param bankInfo
     * @return
     * @throws IOException
     */
    private int uploadBankData(MultipartFile file, CreditcardSource bankInfo, String batchNo, String bonusType) throws IOException {
        String nowStr = DateUtil.getMessageTextTime();
        String fileName = bankInfo.getBankNickName() + nowStr + ".xlsx";
        ALiYunOssUtil.saveFile(Constants.ALIYUN_OSS_ATTCH_TUCKET,fileName,file.getInputStream());
        Date nowTime = new Date();
        UserLoginInfo loginInfo = CommonUtil.getLoginUser();
        //3.增加上传记录
        BankImport info = new BankImport();
        info.setStatus("2");//状态，0无需操作，1已匹配，2待匹配
        info.setBankCode(bankInfo.getId().toString());
        info.setBankName(bankInfo.getBankName());
        info.setBankNickName(bankInfo.getBankNickName());
        info.setFileUrl(fileName);
        info.setCreateTime(nowTime);
        info.setImportTime(nowTime);
        info.setImportBy(loginInfo.getUsername());
        info.setUpdateBy(loginInfo.getId().toString());
        info.setBatchNo(batchNo);
        info.setBonusType(bonusType);
        return bankImportDao.insert(info);
    }

    @Override
    public Result matchData(Integer id) {
        Result result = new Result();
        //校验是否待匹配
        BankImport bankImport = bankImportDao.selectDetail(id);
        if(bankImport == null){
            result.setMsg("找不到对应的记录");
            return result;
        }
        if(!"2".equals(bankImport.getStatus())){
            result.setMsg("只能处理待匹配的数据");
            return result;
        }
        CreditcardSource bankInfo = creditcardSourceDao.selectDetail(Long.valueOf(bankImport.getBankCode()));
        if(bankInfo == null){
            result.setMsg("找不到对应的银行");
            return result;
        }
//        判断下该行首刷奖金<=0且自动结算分润为否，则无需处理，提示“无需匹配”
        if(bankInfo.getFirstBrushBonus().compareTo(BigDecimal.ZERO)<=0 && 0==bankInfo.getAutoShareStatus() ){
            bankImportDao.updateStatus(id, "0");
            result.setStatus(true);
            result.setMsg("无需匹配");
            return result;
        }
        try {
            List<CreditcardApplyRecord> list = creditcardApplyRecordDao.selectListByBatchNo(bankImport.getBatchNo());
            int totalNum = 0;
            int successNum = 0;
            int failNum = 0;
//            StringBuilder errMsg = new StringBuilder();
            //将list去订单表做匹配，匹配完成，调用计算分润接口

            if(list != null && list.size() > 0){
                String bonusType = bankImport.getBonusType();
                totalNum = list.size();
                List<OrderMain> orderList = new ArrayList<>();
                List<OrderMain> brushList = new ArrayList<>();
                for(CreditcardApplyRecord record: list){
                    //匹配规则
                    result = matchRule(record, bonusType);
                    String orderNo = "";
                    if(result.isStatus()){
                        successNum ++;
                        OrderMain order = (OrderMain) result.getData();
                        orderNo = order.getOrderNo();
                        if("2".equals(bonusType)){
                            brushList.add(order);
                        } else {
                            orderList.add(order);
                        }
                    } else {
                        failNum ++;
//                        errMsg.append(result.getMsg());
                    }
                    //将提示信息更新到creditcard_apply_record表
                    creditcardApplyRecordDao.updateMessage(record.getId(), result.getMsg(), orderNo);
                }
                //批量更新订单表，并加上批次号
                if("2".equals(bonusType)){
                    updateBrushStatusBatch(brushList);
                } else {
                    updateOrderStatusBatch(orderList);
                }

            }
            //置为已匹配
            bankImport.setStatus("1");
            bankImport.setTotalNum(totalNum);
            bankImport.setSuccessNum(successNum);
            bankImport.setFailNum(failNum);
            bankImportDao.updateInfo(bankImport);
            result.setStatus(true);
//            result.setMsg("操作成功" + "，成功条数：" + successSum + "，失败条数：" + failSum + ";" + errMsg);
            result.setMsg("操作成功" + "，总条数：" + totalNum + "，成功条数：" + successNum + "，失败条数：" + failNum);
//            起一个异步线程，调用计算分润接口
            if(successNum > 0){
                new Thread(new SuperBankBankProfitRunnable(bankImport.getBatchNo(), bankImport.getBonusType())).start();
            }
        } catch (Exception e){
            log.error("匹配银行数据异常", e);
            result.setMsg("操作异常");
        }
        return result;
    }

    private void updateOrderStatusBatch(List<OrderMain> orderList) {
        if(orderList.size() > 0){
            List<OrderMain> itemList = new ArrayList<>();
            for (int i = 0; i < orderList.size(); i++){
                itemList.add(orderList.get(i));
                if(i % 200 > 0){
                    orderMainDao.updateOrderStatusBatch(itemList);
                    itemList.clear();
                }
            }
            if(itemList.size() > 0){
                orderMainDao.updateOrderStatusBatch(itemList);
            }
        }
    }

    private void updateBrushStatusBatch(List<OrderMain> orderList) {
        if(orderList.size() > 0){
            List<OrderMain> itemList = new ArrayList<>();
            for (int i = 0; i < orderList.size(); i++){
                itemList.add(orderList.get(i));
                if(i % 200 > 0){
                    orderMainDao.updateBrushStatusBatch(itemList);
                    itemList.clear();
                }
            }
            if(itemList.size() > 0){
                orderMainDao.updateBrushStatusBatch(itemList);
            }
        }
    }

    /**
     * 匹配规则
     * @param record
     * @return
     */
    private Result matchRule(CreditcardApplyRecord record, String bonusType) {
        Result result = new Result();
        String ruleCode = record.getRuleCode();
        if(StringUtils.isBlank(ruleCode)){
            result.setMsg("找不到对应的匹配规则");
            return result;
        }
        switch (ruleCode){
            case "SHYH002":
                result = getShyhResult(record, result, bonusType);
                break;
            case "MSYH002":
                result = getMsyhResult(record, result, bonusType);
                break;
            case "ZXYH002":
                result = getZxyhResult(record, result, bonusType);
                break;
            case "PAYH002":
                result = getPayhResult(record, result, bonusType);
                break;
            case "XYYH002":
                result = getXyyhResult(record, result, bonusType);
                break;
            case "JTYH002":
                result = getJtyhResult(record, result, bonusType);
                break;
            case "ZSYH002":
                result = getZsyhResult(record, result, bonusType);
                break;
            case "JSYH002":
                result = getJsyhResult(record, result, bonusType);
                break;
            case "GFYH002":
                result = getGfyhResult(record, result, bonusType);
                break;
            case "WZYH002":
                result = getWzyhResult(record, result, bonusType);
                break;
            case "WZYH2002":
                result = getWzyhResult(record, result, bonusType);
                break;
            case "HXYH002":
                result = getHxyhResult(record, result, bonusType);
                break;
            case "PFYH002":
                result = getPfyhResult(record, result, bonusType);
                break;
            case "ZGYH002":
                result = getZgyhResult(record, result, bonusType);
                break;
            default:
                result.setMsg("找不到对应的银行");
                break;
        }
        return result;
    }

    /**
     * 招商银行匹配规则
     * @param record
     * @param result
     * @return
     */
    private Result getZsyhResult(CreditcardApplyRecord record, Result result, String bonusType) {
        String userCode = record.getUserCode();
        String mobile = record.getMobilephone();
        String mobileStart = mobile.substring(0, 3) + "%";
        String mobileEnd = mobile.substring(mobile.length() - 4, mobile.length());
        String orderName = record.getUserName();
        if(StringUtils.isNotBlank(orderName) && orderName.length() > 0){
            orderName = orderName.substring(0, 1) + "%";
        }

        String batchNo = record.getBatchNo();
        String bankName = record.getBankName();
        String ruleCode = record.getRuleCode();

        Calendar c = Calendar.getInstance();
        Date applyDate=record.getApplyDate();
        c.setTime(applyDate);
        c.add(Calendar.DAY_OF_MONTH, 1);// +1天
        OrderMain order = new OrderMain();
        order.setCreateDate(c.getTime());
        order.setOrderType("2");//办卡订单
        order.setBankName(bankName);
        order.setRuleCode(ruleCode);
        order.setUserCode(userCode);
        order.setOrderPhoneStart(mobileStart);
        order.setOrderPhoneEnd(mobileEnd);
        order.setOrderName(orderName);
        //校验订单是否存在
        List<OrderMain> itemList = checkExistsOrder(order);
        if(itemList == null || itemList.size() == 0){
            result.setMsg("找不到对应的订单");
            return result;
        }
        result = getNeedSuccessOrder(result, batchNo, itemList);
        return result;
    }

    /**
     * 建设银行匹配规则
     * @param record
     * @param result
     * @return
     */
    private Result getJsyhResult(CreditcardApplyRecord record, Result result, String bonusType) {
        String mobile = record.getMobilephone();
        String mobileStart = mobile.substring(0, 3) + "%";
        String mobileEnd = mobile.substring(mobile.length() - 4, mobile.length());
        String orderName = record.getUserName();
        if(StringUtils.isNotBlank(orderName) && orderName.length() > 0){
            orderName = orderName.substring(0, 1) + "%";
        }

        String batchNo = record.getBatchNo();
        String bankName = record.getBankName();
        String ruleCode = record.getRuleCode();

        Calendar c = Calendar.getInstance();
        Date applyDate=record.getApplyDate();
        c.setTime(applyDate);
        c.add(Calendar.DAY_OF_MONTH, 1);// +1天
        OrderMain order = new OrderMain();
        order.setCreateDate(c.getTime());
        order.setOrderType("2");//办卡订单
        order.setBankName(bankName);
        order.setRuleCode(ruleCode);
        order.setOrderPhoneStart(mobileStart);
        order.setOrderPhoneEnd(mobileEnd);
        order.setOrderName(orderName);
        //校验订单是否存在
        List<OrderMain> itemList = checkExistsOrder(order);
        if(itemList == null || itemList.size() == 0){
            result.setMsg("找不到对应的订单");
            return result;
        }
        result = getNeedSuccessOrder(result, batchNo, itemList);
        return result;
    }

    /**
     * 华夏银行匹配规则
     * @param record
     * @param result
     * @return
     */
    private Result getHxyhResult(CreditcardApplyRecord record, Result result, String bonusType) {
        String mobile = record.getMobilephone();
        String mobileStart = mobile.substring(0, 3) + "%";
        String mobileEnd = mobile.substring(mobile.length() - 4, mobile.length());
        String orderName = record.getUserName();
        String batchNo = record.getBatchNo();
        String bankName = record.getBankName();
        String ruleCode = record.getRuleCode();
        Calendar c = Calendar.getInstance();
        Date applyDate=record.getApplyDate();
        c.setTime(applyDate);
        c.add(Calendar.DAY_OF_MONTH, 1);// +1天
        OrderMain order = new OrderMain();
        order.setCreateDate(c.getTime());
        order.setOrderType("2");//办卡订单
        order.setBankName(bankName);
        order.setRuleCode(ruleCode);
        order.setOrderPhoneStart(mobileStart);
        order.setOrderPhoneEnd(mobileEnd);
        order.setOrderName(orderName);
        //校验订单是否存在
        List<OrderMain> itemList = checkExistsOrder(order);
        if(itemList == null || itemList.size() == 0){
            result.setMsg("找不到对应的订单");
            return result;
        }
        result = getNeedSuccessOrder(result, batchNo, itemList);
        return result;
    }

    /**
     * 浦发银行匹配规则
     * @param record
     * @param result
     * @return
     */
    private Result getPfyhResult(CreditcardApplyRecord record, Result result, String bonusType) {
        String mobile = record.getMobilephone();
        String mobileStart = mobile.substring(0, 3) + "%";
        String mobileEnd = mobile.substring(mobile.length() - 4, mobile.length());
        String orderName = record.getUserName().substring(0,1)+"%";
        String batchNo = record.getBatchNo();
        String bankName = record.getBankName();
        String ruleCode = record.getRuleCode();
        Calendar c = Calendar.getInstance();
        Date applyDate=record.getApplyDate();
        c.setTime(applyDate);
        c.add(Calendar.DAY_OF_MONTH, 1);// +1天
        OrderMain order = new OrderMain();
        order.setCreateDate(c.getTime());
        order.setOrderType("2");//办卡订单
        order.setBankName(bankName);
        order.setRuleCode(ruleCode);
        order.setOrderPhoneStart(mobileStart);
        order.setOrderPhoneEnd(mobileEnd);
        order.setOrderName(orderName);
        //校验订单是否存在
        List<OrderMain> itemList = checkExistsOrder(order);
        if(itemList == null || itemList.size() == 0){
            result.setMsg("找不到对应的订单");
            return result;
        }
        result = getNeedSuccessOrder(result, batchNo, itemList);
        return result;
    }

    /**
     * 中国银行匹配规则
     * @param record
     * @param result
     * @return
     */
    private Result getZgyhResult(CreditcardApplyRecord record, Result result, String bonusType) {
        String mobile = record.getMobilephone();
        String mobileStart = mobile.substring(0, 3) + "%";
        String mobileEnd = mobile.substring(mobile.length() - 4, mobile.length());
        String orderName = record.getUserName();
        String batchNo = record.getBatchNo();
        String bankName = record.getBankName();
        String ruleCode = record.getRuleCode();
        Calendar c = Calendar.getInstance();
        Date applyDate=record.getApplyDate();
        c.setTime(applyDate);
        c.add(Calendar.DAY_OF_MONTH, 1);// +1天
        OrderMain order = new OrderMain();
        order.setCreateDate(c.getTime());
        order.setOrderType("2");//办卡订单
        order.setBankName(bankName);
        order.setRuleCode(ruleCode);
        order.setOrderPhoneStart(mobileStart);
        order.setOrderPhoneEnd(mobileEnd);
        order.setOrderName(orderName);
//        log.info("匹配数据"+ JSON.toJSONString(order));
        //校验订单是否存在
        List<OrderMain> itemList = checkExistsOrder(order);
        if(itemList == null || itemList.size() == 0){
            result.setMsg("找不到对应的订单");
            return result;
        }
        result = getNeedSuccessOrder(result, batchNo, itemList);
        return result;
    }

    /**
     * 广发银行匹配规则
     * @param record
     * @param result
     * @return
     */
    private Result getGfyhResult(CreditcardApplyRecord record, Result result, String bonusType) {
        String orderName = record.getUserName();
        String batchNo = record.getBatchNo();
        String bankName = record.getBankName();
        String ruleCode = record.getRuleCode();
        String idNo = record.getIdCardNo();
        Calendar c = Calendar.getInstance();
        Date applyDate=record.getApplyDate();
        c.setTime(applyDate);
        c.add(Calendar.DAY_OF_MONTH, 1);// +1天
        OrderMain order = new OrderMain();
        order.setCreateDate(c.getTime());
        order.setOrderType("2");//办卡订单
        order.setBankName(bankName);
        order.setRuleCode(ruleCode);
        order.setOrderName(orderName);
        order.setOrderIdNo(idNo);
        //校验订单是否存在
        List<OrderMain> itemList = checkExistsOrder(order);
        if(itemList == null || itemList.size() == 0){
            result.setMsg("找不到对应的订单");
            return result;
        }
        result = getNeedSuccessOrder(result, batchNo, itemList);
        return result;
    }

    /**
     * 获取交通银行的匹配记录
     * @param record
     * @param result
     * @return
     */
    private Result getJtyhResult(CreditcardApplyRecord record, Result result, String bonusType) {
        String orderName = record.getUserName();
        if(StringUtils.isBlank(orderName) || orderName.length() < 2){
            result.setMsg("姓名长度不能小于2");
            return result;
        }
//        String orderNameStart = orderName.substring(0,1) + "%";
        String orderNameEnd = orderName.substring(1);
        Integer orderNameEndLength = orderNameEnd.length();
        Integer orderNameLength = orderName.length() * 3;//数据库length(str)，一个汉字长度为3
        String mobile = record.getMobilephone();
        if(StringUtils.isBlank(mobile) || mobile.length() < 11){
            result.setMsg("手机号长度不能小于11");
            return result;
        }
        String mobileStart = mobile.substring(0, 3) + "%";
        String mobileEnd = mobile.substring(mobile.length() - 4, mobile.length());
        //身份证前14位
        String batchNo = record.getBatchNo();
        String bankName = record.getBankName();
        String ruleCode = record.getRuleCode();
        Calendar c = Calendar.getInstance();
        Date applyDate=record.getApplyDate();
        c.setTime(applyDate);
        c.add(Calendar.DAY_OF_MONTH, 1);// +1天
        OrderMain order = new OrderMain();
        order.setCreateDate(c.getTime());
        order.setOrderType("2");//办卡订单
        order.setBankName(bankName);
        order.setRuleCode(ruleCode);
//        order.setOrderNameStart(orderNameStart);
        order.setOrderNameEnd(orderNameEnd);
        order.setOrderNameEndLength(orderNameEndLength);
        order.setOrderNameLength(orderNameLength);
        order.setOrderPhoneStart(mobileStart);
        order.setOrderPhoneEnd(mobileEnd);
        //校验订单是否存在
        List<OrderMain> itemList = checkExistsOrder(order);
        if(itemList == null || itemList.size() == 0){
            result.setMsg("找不到对应的订单");
            return result;
        }
        result = getNeedSuccessOrder(result, batchNo, itemList);
        return result;
    }

    /**
     * 获取兴业银行的匹配记录
     * @param record
     * @param result
     * @return
     */
    private Result getXyyhResult(CreditcardApplyRecord record, Result result, String bonusType) {
        String orderName = record.getUserName();
        String idNo = record.getIdCardNo();
        //身份证前14位
        String batchNo = record.getBatchNo();
        String bankName = record.getBankName();
        String ruleCode = record.getRuleCode();
        String mobile = record.getMobilephone();
        if ((mobile == null) || (mobile.length() < 11)) {
            result.setMsg("手机号不能为空或者不足11位");
            return result;
        }
        String mobileStart = mobile.substring(0, 3) + "%";
        String mobileEnd = mobile.substring(7, 11);
        Date applyDate=record.getApplyDate();
        Calendar c = Calendar.getInstance();
        c.setTime(applyDate);
        c.add(Calendar.DAY_OF_MONTH, 1);// +1天
        OrderMain order = new OrderMain();
        order.setOrderType("2");//办卡订单
        order.setBankName(bankName);
        order.setOrderName(orderName);
        order.setOrderIdNo(idNo);
        order.setRuleCode(ruleCode);
        order.setCreateDate(c.getTime());
        order.setOrderPhoneStart(mobileStart);
        order.setOrderPhoneEnd(mobileEnd);

        //校验订单是否存在
        List<OrderMain> itemList = checkExistsOrder(order);
        if(itemList == null || itemList.size() == 0){
            result.setMsg("找不到对应的订单");
            return result;
        }
//        OrderMain itemSuccess = checkExistsSuccessOrder(item);
        //如果存在已完成的，将其他不是已完成的订单，置为二次办卡
//        if (checkExistsSuccessOrder(item)) {
////            order.setStatus("7");//二次办卡
////            updateBankOrderNotComplete(order);
//            result.setMsg("二次办卡");
//        } else {
//            //如果不存在，将最新的一条置为已完成
//            order.setBatchNo(batchNo);
//            updateBankOrderSuccess(order);
//            result.setStatus(true);
//            result.setMsg("处理成功");
//        }
        result = getNeedSuccessOrder(result, batchNo, itemList);
        return result;
    }

    /**
     * 获取平安银行的匹配记录
     * @param record
     * @param result
     * @return
     */
    private Result getPayhResult(CreditcardApplyRecord record, Result result, String bonusType) {
        String orderName = record.getUserName();
        String idNo = record.getIdCardNo();
        String batchNo = record.getBatchNo();
        String bankName = record.getBankName();
        String ruleCode = record.getRuleCode();
        Calendar c = Calendar.getInstance();
        Date applyDate=record.getApplyDate();

        OrderMain order = new OrderMain();
        if(applyDate!=null) {
            c.setTime(applyDate);
            c.add(Calendar.DAY_OF_MONTH, 1);// +1天
            order.setCreateDate(c.getTime());
        }
        order.setOrderType("2");//办卡订单
        order.setBankName(bankName);
        order.setOrderName(orderName);
        order.setOrderIdNo(idNo);
        order.setRuleCode(ruleCode);
        order.setBonusType(bonusType);

        //校验订单是否存在
        List<OrderMain> itemList = checkExistsOrder(order);
        if(itemList == null || itemList.size() == 0){
            result.setMsg("找不到对应的订单");
            return result;
        }
        //不是首刷，默认办卡
        if(!"2".equals(bonusType)){
            if("Y".equals(record.getNewAccountStatus()) && "办卡成功".equals(record.getCheckStatus()) ){
                result = getNeedSuccessOrder(result, batchNo, itemList);
            } else {
                result.setMsg("办卡失败，或者二次办卡");
            }
        } else {
            if("Y".equals(record.getNewAccountStatus())  && "首刷成功".equals(record.getCheckStatus()) ){
                result = getNeedBrushOrder(result, batchNo, itemList);
            } else{
                result.setMsg("二次办卡，或者不是首刷");
            }
        }
        return result;
    }



    /**
     * 获取中兴银行的匹配结果
     * @param record
     * @param result
     * @return
     */
    private Result getZxyhResult(CreditcardApplyRecord record, Result result, String bonusType) {
        String orderName = record.getUserName();
        String orderPhone = record.getMobilephone();
//        String idNo = record.getIdCardNo();
        //手机号前三位
        String orderPhoneStart = orderPhone.substring(0,3) + "%";
        //手机号末四位
        String orderPhoneEnd = orderPhone.substring(orderPhone.length() - 4,orderPhone.length());
        //身份证前14位
        String batchNo = record.getBatchNo();
        String bankName = record.getBankName();
        String ruleCode = record.getRuleCode();
        Calendar c = Calendar.getInstance();
        Date applyDate=record.getApplyDate();
        c.setTime(applyDate);
        c.add(Calendar.DAY_OF_MONTH, 1);// +1天
        OrderMain order = new OrderMain();
        order.setCreateDate(c.getTime());
        order.setOrderType("2");//办卡订单
        order.setBankName(bankName);
        order.setOrderName(orderName);
        order.setOrderPhoneStart(orderPhoneStart);
        order.setOrderPhoneEnd(orderPhoneEnd);
//        order.setOrderIdNo(idNo.substring(0,14) + "%");
        order.setRuleCode(ruleCode);

        //校验订单是否存在
        List<OrderMain> itemList = checkExistsOrder(order);
        if(itemList == null || itemList.size() == 0){
            result.setMsg("找不到对应的订单");
            return result;
        }
//        OrderMain itemSuccess = checkExistsSuccessOrder(order);
        //如果存在已完成的，将其他不是已完成的订单，置为二次办卡
//        if (checkExistsSuccessOrder(item)) {
////            order.setStatus("7");//二次办卡
////            updateBankOrderNotComplete(order);
//            result.setMsg("二次办卡");
//        } else {
//            //如果不存在，将最新的一条置为已完成
//            order.setBatchNo(batchNo);
//            updateBankOrderSuccess(order);
//            result.setStatus(true);
//            result.setMsg("处理成功");
//        }
        result = getNeedSuccessOrder(result, batchNo, itemList);
        return result;
    }

    /**
     * 获取民生银行的匹配结果
     * @param record
     * @param result
     * @return
     */
    private Result getMsyhResult(CreditcardApplyRecord record, Result result, String bonusType) {
        String orderName = record.getUserName();
        String orderPhone = record.getMobilephone();
        String bankName = record.getBankName();
        String ruleCode = record.getRuleCode();
        //手机号前三位
        String orderPhoneStart = orderPhone.substring(0,3) + "%";
        //手机号末四位
        String orderPhoneEnd = orderPhone.substring(orderPhone.length() - 4,orderPhone.length());
        String batchNo = record.getBatchNo();
        Calendar c = Calendar.getInstance();
        Date applyDate=record.getApplyDate();
        c.setTime(applyDate);
        c.add(Calendar.DAY_OF_MONTH, 1);// +1天
        OrderMain order = new OrderMain();
        order.setCreateDate(c.getTime());
        order.setOrderType("2");//办卡订单
        order.setBankName(bankName);
        order.setOrderName(orderName.substring(0,1) + "%");
        order.setOrderPhoneStart(orderPhoneStart);
        order.setOrderPhoneEnd(orderPhoneEnd);
        order.setRuleCode(ruleCode);

        //校验订单是否存在
        List<OrderMain> itemList = checkExistsOrder(order);
        if(itemList == null || itemList.size() == 0){
            result.setMsg("找不到对应的订单");
            return result;
        }
        //如果民生银行给的数据，有效标志为1
        //校验订单是否存在已成功数据
        if("1".equals(record.getCheckStatus())) {
//            OrderMain itemSuccess = checkExistsSuccessOrder(order);
//            if (checkExistsSuccessOrder(item)) {
////                order.setOrderNo(itemSuccess.getOrderNo());
////                order.setStatus("7");//二次办卡
////                updateBankOrderNotComplete(order);
//                result.setMsg("二次办卡");
//            } else {
//                order.setBatchNo(batchNo);
//                updateBankOrderSuccess(order);
//                result.setStatus(true);
//                result.setMsg("处理成功");
//            }
            result = getNeedSuccessOrder(result, batchNo, itemList);
            return result;
        }
        if (!"1".equals(record.getCheckStatus())) {
//            order.setStatus("6");//审核失败
//            updateBankOrderNotComplete(order);
            result.setMsg("审核失败");
        }
        return result;
    }

    /**
     * 获取上海银行的匹配结果
     * @param record
     * @param result
     * @return
     */
    private Result getShyhResult(CreditcardApplyRecord record, Result result, String bonusType) {
        String orderName = record.getUserName();
        int orderNameLength = orderName.length() * 3;
        String orderNameStart = orderName.substring(0, 1) + "%";
        if(StringUtils.isBlank(orderName) || orderName.length() < 1){
            result.setMsg("客户姓名不能为空或者长度不能小于1");
            return result;
        }
        String orderNameEnd = "";
        if(orderName.length() >= 2){
            orderNameEnd = orderName.substring(2);
        }
        int orderNameEndLength = orderNameEnd.length();
        String idNo = record.getIdCardNo();
        String bankName = record.getBankName();
        String ruleCode = record.getRuleCode();
        String batchNo = record.getBatchNo();
        String mobile = record.getMobilephone();
        if(StringUtils.isBlank(mobile) || mobile.length() < 11){
            result.setMsg("手机号不能为空或者不能小于11位");
            return result;
        }
        String mobileStart = mobile.substring(0, 3) + "%";
        String mobileEnd = mobile.substring(mobile.length() - 4, mobile.length());
        Date applyDate=record.getApplyDate();
        Calendar c = Calendar.getInstance();
        c.setTime(applyDate);
        c.add(Calendar.DAY_OF_MONTH, 1);// +1天
        OrderMain order = new OrderMain();
        order.setCreateDate(c.getTime());
        order.setOrderType("2");//办卡订单
        order.setBankName(bankName);
        order.setOrderNameStart(orderNameStart);
        order.setOrderNameEnd(orderNameEnd);
        order.setOrderNameLength(orderNameLength);
        order.setOrderNameEndLength(orderNameEndLength);
        order.setOrderIdNo(idNo);
        order.setOrderPhoneStart(mobileStart);
        order.setOrderPhoneEnd(mobileEnd);
        order.setRuleCode(ruleCode);
        //校验订单是否存在
        List<OrderMain> itemList = checkExistsOrder(order);
        if(itemList == null || itemList.size() == 0){
            result.setMsg("找不到对应的订单");
            return result;
        }
        result = getNeedSuccessOrder(result, batchNo, itemList);
//        //如果上海银行给的数据为审批通过，且为新户，则去匹配订单表是否存在已完成的
//        //      如果存在已完成的，将系统状态不为已完成的，置为二次办卡
//        //      如果不存在已完成的，将最新的一条订单置为已完成，并添加批次号
//        //如果数据本行新户为否，返回二次办卡
//        //如果数据审批状态不通过，返回审核失败
//        if("01".equals(record.getCheckStatus()) && "Y".equalsIgnoreCase(record.getNewAccountStatus())){
//            result = getNeedSuccessOrder(result, batchNo, itemList);
//            return result;
//        }
//        if(!"Y".equalsIgnoreCase(record.getNewAccountStatus())){
////            order.setStatus("7");//已办理过
////            updateBankOrderNotComplete(order);
//            result.setMsg("二次办卡");
//            return result;
//        }
//        if(!"01".equals(record.getCheckStatus())){
////            order.setStatus("6");//审核失败
////            updateBankOrderNotComplete(order);
//            result.setMsg("审核失败");
//        }

        return result;
    }

    /**
     * 获取温州银行的匹配结果
     * @param record
     * @param result
     * @return
     */
    private Result getWzyhResult(CreditcardApplyRecord record, Result result, String bonusType) {
        String orderName = record.getUserName();
        int orderNameLength = orderName.length() * 3;
        String orderNameStart = orderName.substring(0, 1) + "%";
        if(StringUtils.isBlank(orderName) || orderName.length() < 1){
            result.setMsg("客户姓名不能为空或者长度不能小于1");
            return result;
        }
        String orderNameEnd = "";
        if(orderName.length() >= 2){
            orderNameEnd = orderName.substring(2);
        }
        int orderNameEndLength = orderNameEnd.length();
        String idNo = record.getIdCardNo();
        String bankName = record.getBankName();
        String ruleCode = record.getRuleCode();
        String batchNo = record.getBatchNo();
        String mobile = record.getMobilephone();
        if(StringUtils.isBlank(mobile) || mobile.length() < 11){
            result.setMsg("手机号不能为空或者不能小于11位");
            return result;
        }
        String mobileStart = mobile.substring(0, 3) + "%";
        String mobileEnd = mobile.substring(mobile.length() - 4, mobile.length());
        Date applyDate=record.getApplyDate();
        Calendar c = Calendar.getInstance();
        c.setTime(applyDate);
        c.add(Calendar.DAY_OF_MONTH, 1);// +1天
        OrderMain order = new OrderMain();
        order.setCreateDate(c.getTime());
        order.setOrderType("2");//办卡订单
        order.setBankName(bankName);
        order.setOrderNameStart(orderNameStart);
        order.setOrderNameEnd(orderNameEnd);
        order.setOrderNameLength(orderNameLength);
        order.setOrderNameEndLength(orderNameEndLength);
        order.setOrderIdNo(idNo);
        order.setOrderPhoneStart(mobileStart);
        order.setOrderPhoneEnd(mobileEnd);
        order.setRuleCode(ruleCode);
        //校验订单是否存在
        List<OrderMain> itemList = checkExistsOrder(order);
        if(itemList == null || itemList.size() == 0){
            result.setMsg("找不到对应的订单");
            return result;
        }
        result = getNeedSuccessOrder(result, batchNo, itemList);
//        //如果上海银行给的数据为审批通过，且为新户，则去匹配订单表是否存在已完成的
//        //      如果存在已完成的，将系统状态不为已完成的，置为二次办卡
//        //      如果不存在已完成的，将最新的一条订单置为已完成，并添加批次号
//        //如果数据本行新户为否，返回二次办卡
//        //如果数据审批状态不通过，返回审核失败
//        if("01".equals(record.getCheckStatus()) && "Y".equalsIgnoreCase(record.getNewAccountStatus())){
//            result = getNeedSuccessOrder(result, batchNo, itemList);
//            return result;
//        }
//        if(!"Y".equalsIgnoreCase(record.getNewAccountStatus())){
////            order.setStatus("7");//已办理过
////            updateBankOrderNotComplete(order);
//            result.setMsg("二次办卡");
//            return result;
//        }
//        if(!"01".equals(record.getCheckStatus())){
////            order.setStatus("6");//审核失败
////            updateBankOrderNotComplete(order);
//            result.setMsg("审核失败");
//        }

        return result;
    }


    private Result getNeedSuccessOrder(Result result, String batchNo, List<OrderMain> itemList) {
        boolean existsSuccess = false;//是否存在已完成的
        for (OrderMain orderItem: itemList){
            if("5".equals(orderItem.getStatus())){
                existsSuccess = true;
                result.setMsg("二次办卡,最近办卡成功单号为:" + orderItem.getOrderNo());
                orderItem.setProofreadingResult("1");
                orderMainDao.updateProofreadingResult(orderItem);
                break;
            }
        }
        if(existsSuccess){
            return result;
        }
        //将第一条订单（最新的一条，之前在sql已排序），准备置为已完成
        OrderMain orderSuccess = itemList.get(0);
        //检查订单的 身份证号 + 银行名称 + 状态已完成，是否已存在，若存在，则返回
        Long longNum = orderMainDao.checkExistsSuccessOrder(orderSuccess);
        if(longNum != null && longNum > 0){
            result.setMsg("身份证号:" + orderSuccess.getOrderIdNo() + "和银行名称:" + orderSuccess.getBankName() + "组合存在已完成的订单");
            log.info("身份证号:{}和银行名称:{}组合存在已完成的订单", orderSuccess.getOrderIdNo(), orderSuccess.getBankName());
            orderSuccess.setProofreadingResult("1");
            orderMainDao.updateProofreadingResult(orderSuccess);
            return result;
        }
        orderSuccess.setStatus("5");
        orderSuccess.setBatchNo(batchNo);
        orderSuccess.setProofreadingResult("1");
        orderSuccess.setProofreadingMethod("2");
        result.setStatus(true);
        result.setMsg("处理成功");
        result.setData(orderSuccess);
        return result;
    }

    private Result getNeedBrushOrder(Result result, String batchNo, List<OrderMain> itemList) {
        boolean existsAccount = false;//是否存在已入账的
        for(OrderMain orderItem: itemList){
            if("1".equals(orderItem.getAccountStatus2())){
                existsAccount = true;
                break;
            }
        }
        if(existsAccount){
            result.setMsg("首刷分润已入账，无需处理");
            return result;
        }
        OrderMain orderItem = itemList.get(0);
        //检查订单的 身份证号 + 银行名称 + 订单状态已完成 + 首刷入账状态已完成，是否已存在，若存在，则返回
        Long longNum = orderMainDao.checkExistsAccount2Order(orderItem);
        if(longNum != null && longNum > 0){
            result.setMsg("身份证号和银行名称组合存在已入账的订单");
            log.info("身份证号:{}和银行名称:{}组合存在已入账的订单", orderItem.getOrderIdNo(), orderItem.getBankName());
            return result;
        }
        orderItem.setBatchNo2(batchNo);
        orderItem.setProfitStatus2("1");//首刷状态置为已完成
        result.setStatus(true);
        result.setMsg("处理成功");
        result.setData(orderItem);
        return result;
    }

    /**
     * 更新银行办卡订单，置为已完成，加上批次号
     * @param order
     */
    private void updateBankOrderSuccess(OrderMain order) {
        order.setStatus("5");//订单状态：已完成
        switch (order.getRuleCode()){
            case "SHYH002":
                orderMainDao.updateShBankOrderSuccess(order);
                break;
            case "MSYH002":
                orderMainDao.updateMsBankOrderSuccess(order);
                break;
            case "ZXYH002":
                orderMainDao.updateZxBankOrderSuccess(order);
                break;
            case "PAYH002":
                orderMainDao.updatePaBankOrderSuccess(order);
                break;
            case "XYYH002":
                orderMainDao.updateXyBankOrderSuccess(order);
                break;
        }
    }

    /**
     * 将银行的办卡订单，除了已完成的，置为其他状态
     * @param order
     */
//    private void updateBankOrderNotComplete(OrderMain order) {
//        switch (order.getRuleCode()){
//            case "SHYH002":
//                orderMainDao.updateShBankOrderNotComplete(order);
//                break;
//            case "MSYH002":
//                orderMainDao.updateMsBankOrderNotComplete(order);
//                break;
//            case "ZXYH002":
//                orderMainDao.updateZxBankOrderNotComplete(order);
//                break;
//        }
//    }

    /**
     * 检查是否存在符合条件的订单
     * @param order
     */
    private List<OrderMain> checkExistsOrder(OrderMain order) {
        List<OrderMain> item = null;
        if(order == null || StringUtils.isBlank(order.getRuleCode())){
            return item;
        }
        switch (order.getRuleCode()){
            case "SHYH002":
                item = orderMainDao.selectShBankOrderExists(order);
                break;
            case "MSYH002":
                item = orderMainDao.selectMsBankOrderExists(order);
                break;
            case "ZXYH002":
                item = orderMainDao.selectZxBankOrderExists(order);
                break;
            case "ZGYH002":
                item = orderMainDao.selectZxBankOrderExists(order);
                break;
            case "PAYH002":
                if(order.getBonusType() != null && "1".equals(order.getBonusType())){
                    item = orderMainDao.selectPaBankOrderExists(order);
                }
                if(order.getBonusType() != null && "2".equals(order.getBonusType())){
                    item = orderMainDao.selectPaBankBrushOrderExists(order);
                }
                break;
            case "XYYH002":
                 item = orderMainDao.selectXyBankOrderExists(order);
                break;
            case "JTYH002":
                item = orderMainDao.selectJtBankOrderExists(order);
                break;
            case "ZSYH002":
                item = orderMainDao.selectZsBankOrderExists(order);
                break;
            case "JSYH002":
                item = orderMainDao.selectJsBankOrderExists(order);
                break;
            case "GFYH002":
                item = orderMainDao.selectGfBankOrderExists(order);
                break;
            case "WZYH002":
                item = orderMainDao.selectWzBankOrderExists(order);
                break;
            case "WZYH2002":
                item = orderMainDao.selectWzBankOrderExists(order);
                break;
            case "HXYH002":
                item = orderMainDao.selectJsBankOrderExists(order);
                break;
            case "PFYH002":
                item = orderMainDao.selectJsBankOrderExists(order);
                break;
        }
        return item;
    }

    /**
     * 检查是否存在成功的订单
     * @param list
     * @return
     */
//    private boolean checkExistsSuccessOrder(List<OrderMain> list) {
////        OrderMain item = null;
////        order.setStatus("5");//订单状态：已完成
////        switch (order.getRuleCode()){
////            case "SHYH002":
////                item = orderMainDao.selectShBankOrderExistsSuccess(order);
////                break;
////            case "MSYH002":
////                item = orderMainDao.selectMsBankOrderExistsSuccess(order);
////                break;
////            case "ZXYH002":
////                item = orderMainDao.selectZxBankOrderExistsSuccess(order);
////                break;
////            case "PAYH002":
////                item = orderMainDao.selectPaBankOrderExistsSuccess(order);
////                break;
////            case "XYYH002":
////                item = orderMainDao.selectXyBankOrderExistsSuccess(order);
////                break;
////        }
//        boolean status = false;
//        if(list == null || list.size() == 0){
//            return status;
//        }
//        for (OrderMain order: list){
//            if("5".equals(order.getStatus())){
//                status = true;
//                break;
//            }
//        }
//        return status;
//    }
}
