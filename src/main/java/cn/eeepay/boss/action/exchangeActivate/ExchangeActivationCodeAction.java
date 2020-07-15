package cn.eeepay.boss.action.exchangeActivate;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExchangeActivationCodeBean;
import cn.eeepay.framework.service.exchangeActivate.ExchangeActivationCodeService;
import cn.eeepay.framework.util.ResponseUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2017/10/26.
 */
@RestController
@RequestMapping("exchangeActivationCodeAction")
public class ExchangeActivationCodeAction {

    @Resource
    private ExchangeActivationCodeService exchangeActivationCodeService;

    @RequestMapping("/listActivationCode")
    public Map<String, Object> listActivationCode(@RequestBody ExchangeActivationCodeBean bean,
                                                  @RequestParam(defaultValue = "1") int pageNo,
                                                  @RequestParam(defaultValue = "20")int pageSize){
        Page<ExchangeActivationCodeBean> page = new Page<>(pageNo, pageSize);
        try {
            List<ExchangeActivationCodeBean> list = exchangeActivationCodeService.listActivationCode(bean, page);
            return ResponseUtil.buildResponseMap(list, page.getTotalCount());
        }catch (Exception e){
            return ResponseUtil.buildResponseMap(e);
        }
    }

    @SystemLog(description = "超级兑生成激活码",operCode="exchangeActivate.build")
    @RequestMapping("/buildActivationCode")
    public Map<String, Object> buildActivationCode(int count){
        try {
            return ResponseUtil.buildResponseMap(exchangeActivationCodeService.buildActivationCode(count));
        }catch (Exception e){
            return ResponseUtil.buildResponseMap(e);
        }
    }

    @SystemLog(description = "超级兑划分激活码",operCode="exchangeActivate.divide")
    @RequestMapping("/divideActivationCode")
    public Map<String, Object> divideActivationCode(long startId, long endId, String agentNode){
        try {
            return ResponseUtil.buildResponseMap(exchangeActivationCodeService.divideActivationCode(startId, endId, agentNode));
        }catch (Exception e){
            return ResponseUtil.buildResponseMap(e);
        }
    }

    @SystemLog(description = "超级兑回收激活码",operCode="exchangeActivate.recovery")
    @RequestMapping("/recoveryActivation")
    public Map<String, Object> recoveryActivation(long startId, long endId){
        try {
            return ResponseUtil.buildResponseMap(exchangeActivationCodeService.recoveryActivation(startId, endId));
        }catch (Exception e){
            return ResponseUtil.buildResponseMap(e);
        }
    }

    @RequestMapping("/getRedeemJHCode")
    public Map<String, Object> getRedeemJHCode(){
        try {
            return ResponseUtil.buildResponseMap(exchangeActivationCodeService.getRedeemJHCode());
        }catch (Exception e){
            return ResponseUtil.buildResponseMap(e);
        }
    }
}
