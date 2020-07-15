package cn.eeepay.framework.service.impl.risk;

import cn.eeepay.framework.dao.risk.SurveyOrderLogDao;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.risk.SurveyOrderLog;
import cn.eeepay.framework.service.risk.SurveyOrderLogService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/9/11/011.
 * @author liuks
 * 调单操作日志
 */
@Service("surveyOrderLogService")
public class SurveyOrderLogServiceImpl implements SurveyOrderLogService {

    @Resource
    private SurveyOrderLogDao surveyOrderLogDao;

    @Override
    public int insertSurveyOrderLog(String orderNo,String operateType,String msg) {
        SurveyOrderLog log=getNewSurveyOrderLog(orderNo,operateType,msg);
        return surveyOrderLogDao.insertSurveyOrderLog(log);
    }

    @Override
    public List<SurveyOrderLog> getSurveyOrderLogAll(String orderNo) {
        return surveyOrderLogDao.getSurveyOrderLogAll(orderNo);
    }
    @Override
    public SurveyOrderLog getNewSurveyOrderLog(String orderNo,String operateType,String msg) {
        if(operateType!=null){
            //0添加调单，1回退，2处理，3催单，4删除，5添加扣款，6标注扣款处理，7标注下发处理
            SurveyOrderLog log=new SurveyOrderLog();
            log.setOrderNo(orderNo);
            log.setOperateDetail(msg);
            UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            log.setOperator(principal.getUsername());
            log.setOperateType(operateType);
            return log;
        }
        return null;
    }
}
