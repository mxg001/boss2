package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoSuperbank.CreditcardApplyRecordDao;
import cn.eeepay.framework.model.CreditcardApplyRecord;
import cn.eeepay.framework.service.TranslateRow;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ExcelErrorMsgBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
@Service
public class CreditRowImpl implements TranslateRow<CreditcardApplyRecord> {

    private ThreadLocal<String> bankSourceThread = new ThreadLocal<>();//银行编码

    @Resource
    private CreditcardApplyRecordDao creditcardApplyRecordDao;

    @Override
    public CreditcardApplyRecord translate(Row row, int index, List<ExcelErrorMsgBean> errors) {
        CreditcardApplyRecord record = null;
        String bankSourceId = bankSourceThread.get();
        switch (bankSourceId){
            case "12":
                record = getShBankRecord(bankSourceId, row, index, errors);
                break;
        }
        return record;
    }

    /**
     * 获取上海银行的记录
     * @param bankSourceId
     * @param row
     * @param index
     * @param errors
     * @return
     */
    private CreditcardApplyRecord getShBankRecord(String bankSourceId, Row row, int index
            , List<ExcelErrorMsgBean> errors) {
        //0：网申日期
        int applyDateLine = 0;
        //11：客户姓名
        int userNameLine = 4;
        //12：证件号
        int idCardNoLine = 5;
        //13.是否是本行新户
        int newStatusLine = 8;
        String applyDateStr = String.valueOf(row.getCell(applyDateLine));
        if(StringUtils.isBlank(applyDateStr) || "null".equals(applyDateStr)){
            errors.add(new ExcelErrorMsgBean(index, applyDateLine, "网申日期不能为空"));
            return null;
        }
        Date applyDate = DateUtil.parseDateTime(applyDateStr);
        String userName = String.valueOf(row.getCell(userNameLine));
        if(StringUtils.isBlank(userName) || "null".equals(userName)){
            errors.add(new ExcelErrorMsgBean(index, userNameLine, "客户姓名不能为空"));
            return null;
        }
        String idCardNo = String.valueOf(row.getCell(idCardNoLine));
        if(StringUtils.isBlank(idCardNo) || "null".equals(idCardNo)){
            errors.add(new ExcelErrorMsgBean(index, idCardNoLine, "证件号不能为空"));
            return null;
        }
        String newAccountStatus = String.valueOf(row.getCell(newStatusLine));
        if(StringUtils.isBlank(newAccountStatus) || "null".equals(newAccountStatus)){
            errors.add(new ExcelErrorMsgBean(index, newStatusLine, "是否本行新户不能为空"));
            return null;
        }
        if(!"Y".equals(newAccountStatus)){
            errors.add(new ExcelErrorMsgBean(index, newStatusLine, "必须是本行新户"));
            return null;
        }
        newAccountStatus = "1";
        CreditcardApplyRecord record = new CreditcardApplyRecord();
        record.setBankCode(bankSourceId);
        record.setApplyDate(applyDate);
        record.setUserName(userName);
        record.setIdCardNo(idCardNo);
        record.setNewAccountStatus(newAccountStatus);
        if(checkExists(record)){
            errors.add(new ExcelErrorMsgBean(index, 0, "记录已存在"));
            return null;
        }
        return record;
    }

    private boolean checkExists(CreditcardApplyRecord record) {
        if(record == null){
            return  false;
        }
        int num = creditcardApplyRecordDao.checkExists(record);
        if(num > 0){
            return true;
        }
        return false;
    }

    public void setBankSourceId(String bankSourceId) {
        bankSourceThread.set(bankSourceId);
    }
}
