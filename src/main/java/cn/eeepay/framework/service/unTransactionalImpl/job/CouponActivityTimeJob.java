package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.CouponActivityService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Scope("prototype")
public class CouponActivityTimeJob extends ScheduleJob {
    private static final Logger log = LoggerFactory.getLogger(CouponActivityTimeJob.class);
    @Resource
    private CouponActivityService couponActivityService;

    @Override
    protected void runTask(String runNo){
        try {
            log.info("充值返,购买鼓励金自动上下线时间线程开始:----------->");
            couponActivityService.execute();
            log.info("充值返,购买鼓励金自动上下线时间线程结束!");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("充值返,购买鼓励金自动上下线时间线程异常:"+e.getMessage());
        }
    }
}