package cn.eeepay.framework.service.impl.creditRepay;

import cn.eeepay.framework.dao.OperateLogDao;
import cn.eeepay.framework.dao.YfbWarnDao;
import cn.eeepay.framework.model.OperateLog;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.creditRepay.YfbWarnInfo;
import cn.eeepay.framework.service.creditRepay.YfbWarnService;
import com.alibaba.fastjson.JSON;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author MXG
 * create 2018/10/22
 */
@Service
public class YfbWarnServiceImpl implements YfbWarnService{

    @Resource
    private YfbWarnDao yfbWarnDao;
    @Resource
    private OperateLogDao operateLogDao;

    @Override
    public YfbWarnInfo selectWarnInfoByCode(String code) {
        return yfbWarnDao.selectWarnInfoByWarnCode(code);
    }

    @Override
    public void update(YfbWarnInfo info) {
        //更新yfb_warn_info
        YfbWarnInfo oldInfo = yfbWarnDao.selectOne(info.getId());
        int i = yfbWarnDao.update(info);
        //将操作记录到日志：yfb_operate_log
        if(i > 0){
            UserLoginInfo principal =
                    (UserLoginInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            OperateLog operateLog = new OperateLog();
            operateLog.setOperator(principal.getUsername());
            operateLog.setOperateCode("update");
            operateLog.setOperateTable("yfb_warn_info");
            operateLog.setPreValue(JSON.toJSONString(oldInfo));
            operateLog.setAfterValue(JSON.toJSONString(info));
            operateLog.setOperateTime(new Date());
            operateLog.setOperateFrom("repay");
            operateLogDao.save(operateLog);
        }
    }

}
