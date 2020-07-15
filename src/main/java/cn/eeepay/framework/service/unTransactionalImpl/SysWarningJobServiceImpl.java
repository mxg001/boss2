package cn.eeepay.framework.service.unTransactionalImpl;

import cn.eeepay.framework.dao.JumpRouteDao;
import cn.eeepay.framework.dao.SysWarningDao;
import cn.eeepay.framework.model.JumpRouteConfig;
import cn.eeepay.framework.service.SysWarningJobService;
import cn.eeepay.framework.util.Sms;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("sysWarningJobService")
public class SysWarningJobServiceImpl implements SysWarningJobService {

    private final static Logger log = LoggerFactory.getLogger(SysWarningJobServiceImpl.class);

    @Resource
    private SysWarningDao sysWarningDao;

    @Resource
    private JumpRouteDao jumpRouteDao;

    @Override
    public void sysWarningJob() {
        try{
            Map yesAmountWarning=sysWarningDao.getByType("3");
            //您好，【集群编号】【备注】于date已达到目标金额【配置的值】，累计金额XX元，请知悉！
            String content=yesAmountWarning.get("content").toString();
            String phones=yesAmountWarning.get("phones").toString();
            List<JumpRouteConfig> yseJumpRouteConfigs=jumpRouteDao.findJumpRouteConfigYesReach();
            log.info("--------预警已达到--------content:"+content+"------------phones:"+phones);
            sendSms(yseJumpRouteConfigs,content,phones);

            Map noAmountWarning=sysWarningDao.getByType("4");
            //您好，【集群编号】【备注】于date未达到目标金额【配置的值】，累计金额XX元，请知悉！
            String content2=noAmountWarning.get("content").toString();
            String phones2=noAmountWarning.get("phones").toString();
            List<JumpRouteConfig> noJumpRouteConfigs=jumpRouteDao.findJumpRouteConfigNoReach();
            log.info("--------预警未达到--------content:"+content2+"------------phones:"+phones2);
            sendSms(noJumpRouteConfigs,content2,phones2);
        }catch (Exception e){
            log.error("--------------预警报错",e);
        }
    }

    public void sendSms(List<JumpRouteConfig> jumpRouteConfigs,String content,String phones){
        String[] stringPhones=phones.split(",");
        for (JumpRouteConfig j:jumpRouteConfigs){
            String sendContent=content;
            sendContent=sendContent.replace("集群编号",j.getGroupCode().toString());
            if(StringUtil.isEmpty(j.getRemark())){
                sendContent=sendContent.replace("【备注】","");
            }else{
                sendContent=sendContent.replace("备注",j.getRemark());
            }
            sendContent=sendContent.replace("date",j.getEndTime());
            sendContent=sendContent.replace("配置的值",j.getTargetAmount().toString());
            sendContent=sendContent.replace("XX",j.getTotalAmount().toString());
            log.info("---------预警手机数组-------id:"+j.getId()+"---phones:"+phones+"--------------stringPhones:"+JSON.toJSONString(stringPhones));
            for (String s:stringPhones){
                if(StringUtil.isNotBlank(s)){
                    Sms.sendMsg(s,sendContent);
                    log.info("--------预警--------"+s+":"+sendContent);
                }
            }
            jumpRouteDao.updateJumpRouteConfigSmsDate(j.getId());
        }
    }
}
