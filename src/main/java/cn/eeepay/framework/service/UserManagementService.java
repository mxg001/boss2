package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.exchange.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/8/008.
 * @author  liuks
 * 超级兑用户 service
 */
public interface UserManagementService {

    List<UserManagement> selectAllList(UserManagement userManagement, Page<UserManagement> page);

    MerInfoTotal selectSum(UserManagement userManagement, Page<UserManagement> page);

    UserManagement getUserManagement(String merchantNo);

    List<AccountInfo> getUserBalance(String merchantNo);

    Result getAccountTranInfo(AccountInfoRecord record, int pageNo, int pageSize);

    Map<String,Date> getUserManagementMember(String merchantNo);

    Subordinate getSubordinate(String merchantNo);

    int updateUserManagement(UserManagement userManagement);

    void updateSettlementCard(SettlementCard settlementCard, Map<String, Object> msg);

    List<UserManagement> importDetailSelect(UserManagement user);

    void importDetail(List<UserManagement> list,HttpServletResponse response) throws Exception;

    List<UserManagement> getUserManagementList(String merchantNo);

    UserManagement getUserManagementOne(String merchantNo);

    int userFreeze(MerchantFreeze freeze);

    List<MerchantFreeze> getUserFreezeHis(String merchantNo);
}
