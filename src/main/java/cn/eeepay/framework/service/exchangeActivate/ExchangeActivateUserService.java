package cn.eeepay.framework.service.exchangeActivate;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AccountInfo;
import cn.eeepay.framework.model.AccountInfoRecord;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.exchange.MerchantFreeze;
import cn.eeepay.framework.model.exchange.SettlementCard;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateUser;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivateUserCard;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/8/008.
 * @author  liuks
 * 超级兑用户 service
 */
public interface ExchangeActivateUserService {

    List<ExchangeActivateUser> selectAllList(ExchangeActivateUser userManagement, Page<ExchangeActivateUser> page);

    ExchangeActivateUser getUserManagement(String merchantNo);

    List<AccountInfo> getUserBalance(String merchantNo);

    Result getAccountTranInfo(AccountInfoRecord record, int pageNo, int pageSize);

    int updateUserManagement(ExchangeActivateUser userManagement);

    void updateSettlementCard(SettlementCard settlementCard, Map<String, Object> msg);

    List<ExchangeActivateUser> importDetailSelect(ExchangeActivateUser user);

    void importDetail(List<ExchangeActivateUser> list, HttpServletResponse response) throws Exception;

    List<ExchangeActivateUser> getUserManagementList(String merchantNo);

    int userFreeze(MerchantFreeze freeze);

    List<MerchantFreeze> getUserFreezeHis(String merchantNo);

    List<ExchangeActivateUserCard> getUserCard(String merchantNo,String isSettle);
}
