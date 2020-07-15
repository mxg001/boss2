package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ActivationCodeBean;
import cn.eeepay.framework.service.ActivationCodeService;
import cn.eeepay.framework.service.ActivityService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2017/10/26.
 */
@RestController
@RequestMapping("activationCodeAction")
public class ActivationCodeAction {

    private static final Logger log = LoggerFactory.getLogger(ActivationCodeAction.class);

    @Resource
    private ActivationCodeService activationCodeService;

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/listActivationCode")
    public Map<String, Object> listActivationCode(@RequestBody ActivationCodeBean bean,
                                                  @RequestParam(defaultValue = "1") int pageNo,
                                                  @RequestParam(defaultValue = "20")int pageSize){
        Page<ActivationCodeBean> page = new Page<>(pageNo, pageSize);
        try {
            List<ActivationCodeBean> list = activationCodeService.listActivationCode(bean, page);
            return ResponseUtil.buildResponseMap(list, page.getTotalCount());
        }catch (Exception e){
            log.error("查询激活码异常", e);
            return ResponseUtil.buildResponseMap(e);
        }
    }

    @SystemLog(description = "生成激活码",operCode="activationCode.build")
    @RequestMapping("/buildActivationCode")
    public Map<String, Object> buildActivationCode(int count,@RequestParam(defaultValue = "repay") String codeType){
        try {
            return ResponseUtil.buildResponseMap(activationCodeService.buildActivationCode(count, codeType));
        }catch (Exception e){
            log.error("生成激活码异常", e);
            return ResponseUtil.buildResponseMap(e);
        }
    }

    @SystemLog(description = "划分激活码",operCode="activationCode.divide")
    @RequestMapping("/divideActivationCode")
    public Map<String, Object> divideActivationCode(long startId, long endId, String agentNode,
                                                    @RequestParam(defaultValue = "repay") String codeType){
        try {
            return ResponseUtil.buildResponseMap(activationCodeService.divideActivationCode(startId, endId, agentNode, codeType));
        }catch (Exception e){
            log.error("划分激活码异常", e);
            return ResponseUtil.buildResponseMap(e);
        }
    }

    @SystemLog(description = "回收激活码",operCode="activationCode.recovery")
    @RequestMapping("/recoveryActivation")
    public Map<String, Object> recoveryActivation(long startId, long endId,
                                                  @RequestParam(defaultValue = "repay") String codeType){
        try {
            return ResponseUtil.buildResponseMap(activationCodeService.recoveryActivation(startId, endId, codeType));
        }catch (Exception e){
            log.error("回收激活码异常", e);
            return ResponseUtil.buildResponseMap(e);
        }
    }
}
