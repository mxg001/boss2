package cn.eeepay.framework.serviceImpl.sysUser;


import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.sysUser.BossSmsService;
import cn.eeepay.framework.util.Sms;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("bossSmsService")
public class BossSmsServiceImpl implements BossSmsService {

    private static final Logger log = LoggerFactory.getLogger(BossSmsServiceImpl.class);

    @Resource
    private SysDictService sysDictService;

    @Override
    public int sendCusSmsTemplate(String mobile,String context) {
        String serviceUrl=sysDictService.getValueByKey("SMS_SERVICE_URL");
        String platform="customer_send_msg";
        String md5key="customer_send_msg_20200211";
        int sendStatus = 0;
        if(StringUtils.isNotBlank(serviceUrl)){
            sendStatus=Sms.sendMsgBoss(mobile,context,serviceUrl,platform,md5key);
        }else{
            log.info("发送短信服务器url错误!");
        }
        return sendStatus;
    }
}
