package cn.eeepay.framework.service.risk;
import cn.eeepay.framework.model.risk.SurveyOrderLog;

import java.util.List;

/**
 * Created by Administrator on 2018/9/11/011.
 * @author liuks
 */
public interface SurveyOrderLogService {

    int insertSurveyOrderLog(String orderNo,String operateType,String msg);

    /**
     *最多最近20条
     */
    List<SurveyOrderLog> getSurveyOrderLogAll(String orderNo);

    SurveyOrderLog getNewSurveyOrderLog(String orderNo,String operateType,String msg);
}
