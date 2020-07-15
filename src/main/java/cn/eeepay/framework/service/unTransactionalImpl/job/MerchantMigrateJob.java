package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.MerchantMigrateService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商户迁移转移一级代理商
 * @author MXG
 * create 2018/11/06
 */
@Component
@Scope("prototype")
public class MerchantMigrateJob  extends ScheduleJob {

    @Autowired
    private MerchantMigrateService merchantMigrateService;

    private static final Logger log = LoggerFactory.getLogger(MerchantMigrateJob.class);

    @Override
    protected void runTask(String runNo) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
            log.info("商户迁移转移一级代理商:" + sdf.format(new Date()));
            
            List<String> merchantNos = new ArrayList<>();
            
            merchantMigrateService.migrate(merchantNos);
            
            //神策传送
            merchantMigrateService.scBymerchantNos(merchantNos);
            
            
        } catch (Exception e) {
            log.error("商户迁移转移一级代理商失败", e);
        }
    }
}
