package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.model.RechargeRecord;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.SuperBankApiService;
import cn.eeepay.framework.service.SuperBankService;
import cn.eeepay.framework.service.SysDictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author tans
 * @date 2018/11/23 11:11
 */
@Service("superBankApiService")
public class SuperBankApiServiceImpl implements SuperBankApiService {

    @Resource
    private SuperBankService superBankService;

    @Resource
    private SysDictService sysDictService;

    @Override
    public int recharge(Map<String, Object> map) {
        String amount = (String) map.get("amount");

        BigDecimal accountBalance = BigDecimal.ZERO;//账户余额
        BigDecimal rechargeAmount = new BigDecimal(amount);//充值金额
        String accountBalanceStr = superBankService.getOutWarnAccount();
        if(StringUtils.isNotBlank(accountBalanceStr)){
            accountBalance = new BigDecimal(accountBalanceStr);
        }
        //2.给账户加钱
        accountBalance = accountBalance.add(rechargeAmount);
        SysDict sysDict = new SysDict();
        sysDict.setSysKey("SUPER_BANK_ACCOUNT_BALANCE");
        sysDict.setSysValue(accountBalance.toString());
        int num = sysDictService.updateSysValue(sysDict);
        return num;
    }
}
