//package cn.eeepay.boss.job;
//
//import cn.eeepay.framework.model.MerchantInfo;
//import cn.eeepay.framework.service.MerchantInfoService;
//import cn.eeepay.framework.util.ClientInterface;
//import com.alibaba.fastjson.JSONObject;
//import org.quartz.DisallowConcurrentExecution;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//@DisallowConcurrentExecution
//public class CreateMerAccJob implements Job {
//
//    private static final Logger log = LoggerFactory.getLogger(CreateMerAccJob.class);
//
//    @Resource
//    private MerchantInfoService merchantInfoService;
//
//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        log.info("=============开户定时任务开始=========");
//        List<MerchantInfo> merList = merchantInfoService.selectByMerAccount();
//        log.info("===========需要开户数量==="+merList.size());
//        if(merList!=null && merList.size()>0){
//            for (MerchantInfo merchantInfo : merList){
//                String merchantNo = merchantInfo.getMerchantNo();
//                String acc= ClientInterface.createMerchantAccount(merchantNo);
//                log.info("商户批量开户,商户号:{},返回结果:{}", merchantNo,acc);
//                JSONObject returnJson = JSONObject.parseObject(acc);
//                if(returnJson.getBooleanValue("status") || "外部账号已经存在".equals(returnJson.getString("msg"))){
//                    int i = merchantInfoService.updateMerAcoount(merchantNo);
//                    if(i>0){
//                        log.info("开立商户账户成功");
//                    }else{
//
//                        log.info( "开立商户账户失败");
//                    }
//                }else{
//                    log.info( "开立商户账户失败");
//                }
//            }
//        }
//        log.info("=============开户结束============");
//
//    }
//}
