package cn.eeepay.framework.service.risk;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.risk.DeductAddInfo;
import cn.eeepay.framework.model.risk.Reminder;
import cn.eeepay.framework.model.risk.SurveyOrder;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/7/007.
 * @author  liuks
 * 调单管理
 */
public interface SurveyOrderService {

    List<SurveyOrder> selectAllList(SurveyOrder order, Page<SurveyOrder> page);

    int addSurveyOrder(SurveyOrder order,Map<String, Object> msg);

    int deleteSurveyOrder(int id);

    void deleteOrderBatch(String ids, Map<String, Object> msg);

    int reminder(int id,List<String> list);

    void reminderBatch(String ids, Map<String, Object> msg,List<String> list);

    int regresses(SurveyOrder order);

    int handle(SurveyOrder order);

    void handleBatch(SurveyOrder order, Map<String, Object> msg);

    int deduct(SurveyOrder order, Map<String, Object> msg);

    List<SurveyOrder> importDetailSelect(SurveyOrder order);

    void importDetail(List<SurveyOrder> list, HttpServletResponse response) throws Exception;

    SurveyOrder getSurveyOrderDetail(int id);

    SurveyOrder selectSurveyOrder(String orderNo);

    int updateAgentState(DeductAddInfo info);

    int updateOrderStateOverdue();

    List<Reminder> getRecordList(String orderNo);

    int upstream(SurveyOrder order);

    void sendJPush(String merchantNo, String tittle, String content, String orderNo, String ownStatus);

    SurveyOrder selectById(Integer id);
}
