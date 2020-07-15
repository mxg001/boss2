package cn.eeepay.framework.service.allAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.allAgent.CountSet;
import cn.eeepay.framework.model.allAgent.UserAllAgent;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/7/12/012.
 */
public interface UserAllAgentService {

    List<UserAllAgent> selectAllList(UserAllAgent user, Page<UserAllAgent> page);

    List<UserAllAgent> importDetailSelect(UserAllAgent user);

    void importDetail(List<UserAllAgent> list, HttpServletResponse response) throws Exception;

    UserAllAgent getUserAllAgent(int id);

    List<UserAllAgent> getOrgList(String brandCodes, int userType);

    CountSet selectAllSum(UserAllAgent user, Page<UserAllAgent> page);

    int saveUserCardAllAgent(AgentInfo agent);

    int saveUserAllAgent(UserAllAgent user);

    List<UserAllAgent> selectDividedAdjustDetail(UserAllAgent user);

    List<UserAllAgent> selectDividedAdjustDetailList(UserAllAgent user, Page<UserAllAgent> page);

    void importDividedAdjustDetail(List<UserAllAgent> list, HttpServletResponse response) throws Exception;

    List<UserAllAgent> selectOneUserCodeList();

    UserAllAgent getUserOne(String userCode);

    List<UserAllAgent> getUserByStr(String str,String level);

    int resetPassword(String userCode);
}
