package cn.eeepay.framework.service.risk;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.risk.DeductAddInfo;
import cn.eeepay.framework.model.risk.SurveyDeductDetail;
import cn.eeepay.framework.model.risk.SurveyOrder;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/12/012.
 * @author  liuks
 * 调单扣款明细
 */
public interface SurveyDeductDetailService {

    int addDeductDetail(SurveyOrder order);

    List<SurveyDeductDetail> selectAllList(SurveyDeductDetail detail, Page<SurveyDeductDetail> page);

    List<SurveyDeductDetail> importDetailSelect(SurveyDeductDetail detail);

    void importDetail(List<SurveyDeductDetail> list, HttpServletResponse response,int sta) throws Exception;

    List<SurveyDeductDetail> selectGroup(SurveyDeductDetail detail, Page<SurveyDeductDetail> page);

    int tagging(DeductAddInfo info, Map<String, Object> msg);

    List<SurveyDeductDetail> importSelectGroup(SurveyDeductDetail detail);
}
