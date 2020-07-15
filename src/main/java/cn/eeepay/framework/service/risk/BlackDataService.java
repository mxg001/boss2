package cn.eeepay.framework.service.risk;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.risk.BlackDataDealLog;
import cn.eeepay.framework.model.risk.BlackDataInfo;
import cn.eeepay.framework.model.risk.DealRecord;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author MXG
 * create 2018/12/21
 */
public interface BlackDataService {


    List<BlackDataInfo> selectByParamWithPage(Page<BlackDataInfo> page, BlackDataInfo blackDataInfo);

    List<Map> selectTeamList();

    void export(BlackDataInfo info, HttpServletRequest request, HttpServletResponse response) throws Exception;

    Map<String,Object> getDetail(String id,int editState);

    void addRemark(String id, String remark);

    void deal(DealRecord record, int type, String mobileNo, String merchantNo);

    String queryMbpId(String merchantNo);
}
